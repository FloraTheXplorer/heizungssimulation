/**
 * Diese Bibliothek wird fuer Datei-Operationen benoetigt
 */
import java.io.*;

/** 
 * @brief Klasse fuer einen erweiterten PID-Regler.
 * @author Florette
 */
public class PIDRegler extends Regler implements ILog
{
	/**
	 * Objekt fuer den Zugriff auf das Dateisystem
	 */
	FileWriter fw;
	/**
	 * Bufferobjekt, um in Dateien schreiben zu koennen.
	 */
    BufferedWriter bw;
    /**
     * Proportionalfaktor des Reglers.
     * \f$ K_{P} * e(t)\f$
     */
    private double kp;
    /**
     * Integralfaktor des Reglers.
     * \f$K_{I} * \int{e(t)}\f$
     */
    private double ki;
    /**
     * Differentialfaktor des Reglers.
     * \f$ K_{D} * \dot{e}(t) \f$
     */
    private double kd;
    /**
     * Um die Ableitung zu bilden muss der vorherige
     * Wert gespeichert werden.
     * Die Ableitung wird dann durch finite Differenzen gebildet
     */
    private double alter_wert;
    /**
     * Reglerabweichung.
     * \f$ e(t) \f$
     */
    private double differenz;
    /**
     * Der Wert des aktuellen Integrals. 
     * Das Integral wird mittels der Summe der
     * Stellwerte fuer ein gewissse Zeit gebildet.
     * (numerische Integration)
     */
    private double integral;
    /**
     * Wert der aktuellen Ableitung
     */
    private double ableitung;
    /**
     * minimaler Stellwert.
     * Dies bildet einen Teil der Stellgroessenbegrenzung
     */
    private double min;
    /**
     * maximaler Stellwert.
     * Dies bildet den anderen Teil der Stellgroessenbegrenzung.
     */
    private double max;
    /**
     * Dieser Wert wird von der aktuellen Reglerabweichung \f$ e(t) \f$
     * abgezogen, wenn die Stellwert groesser/kleiner als die
     * Begrenzung ist, damit das Integral nicht zu stark anwaechst.
     */
    private double antiwindup;
    /**
     * Faktor falls die obere Stellgroessenbeschraenkung ueberschritten wird.
     */
    private double antiwindupfactor_max;
    /**
     * Faktor falls die untere Stellgroessenbeschraenkung unterschritten wird.
     */
    private double antiwindupfactor_min;
    /**
     * Integral der Stellgroesse.
     * Bei der Heizung ist dies die verbrauchte Energie,
     * bei einem spezifischen Wirkungsgrad @see wirkungsgrad_.
     * Gehoert nicht zum eigentlichen Regler
     */
    private double energie;

    /**
     * Wirkungsgrad der Heizung.
     * 0% => 0, 100% => 1.0
     */
    private double wirkungsgrad;

    /**
     * Konstruktor des Reglers.
     * @param[in] kp Proportionalfaktor
     * @param[in] ki Integralfaktor
     * @param[in] kd Differentialfaktor
     * @param[in] min minimaler Stellwert (untere Stellgroessenbeschraenkung)
     * @param[in] max maximaler Stellwert (obere Stellgroessenbeschraenkung)
     * @param[in] antiwindupfactor_min Anti-Wind-Up Faktor bei Unterschreitung der unteren Stellgroessenbeschraenkung.
     * @param[in] antiwindupfactor_max Anti-Wind-Up Faktor bei Ueberschreitung der oberen Stellgroessenbeschraenkung.
     * @param[in] wirkungsgrad Wirkungsgrad der Heizung (0% bis 100%) => (0 bis 1.0)
     * @param[in] wand Diese Zahl entspricht dem Index des aktuellen Wand-Feldes (Array) und dient nur der Benennung der Log-Datei
     * @param[in] fenster Diese Zahl entspricht dem Index des aktuellen Fenster-Feldes (Array) und dient nur der Benennung der Log-Datei
     */
    public PIDRegler(double kp, double ki, double kd, double min, double max, double antiwindupfactor_min,double antiwindupfactor_max, double wirkungsgrad, int wand, int fenster)
    {
        //Elternklassenkonstruktor aufrufen
        super();
        //Parameter abspeichern
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
    	if(min>max)
    		throw new IllegalArgumentException("Minimale Stellgröße größer als die maximale Stellgröße.");
        this.min = min;
        this.max = max;
        this.antiwindupfactor_max = antiwindupfactor_max;
        this.antiwindupfactor_min = antiwindupfactor_min;
        this.antiwindup = 0;
        this.alter_wert =0;
        this.differenz = 0;
        this.integral=0;
        this.ableitung =0;
        this.energie = 0;
        if(wirkungsgrad<=0 || wirkungsgrad>1)
        	throw new IllegalArgumentException("Wirkungsgrad muss im Intervall (0,1] liegen");
        this.wirkungsgrad = wirkungsgrad;

        //Fehlerbehandlung fuer Dateizugriff, z.B. Datei ist schreibgeschuetzt
		//Fehler ist nicht kritisch fuer die Ausfuehrung, daher kann fortgefahren werden.
        try {

			this.fw = new FileWriter("PIDRegler"+wand+"_"+fenster+".csv");
        } 
        catch (IOException e)
        {
			System.out.println("ERROR: Anlegen der Log-Datei fuer PIDRegler fehlgeschlagen.");
        }
        //Datei an den Schreiber uebergeben
        this.bw = new BufferedWriter(fw);
    }

    /**
     * Berechne den neuen Sollwert (Stellgroesse hier Heizleistung).
     * Der auf die Regelstrecke (Wohnraum) gegeben werden soll.
     * \f$ y(t) = K_{P}*e(t)+K_{I}*\int{e(t)}+K_{D}*\dot{e}(t) \f$
     * @param[in] zeit Zeit in Sekunden, die seit dem letzten Aufruf vergangen ist.
     * @return Stellwert (Leistung) in Watt [W]
     */
    public double berechne(double zeit)
    {
    	if(zeit<=0)
    		throw new IllegalArgumentException("vergangene Zeit ist zu klein.");
    	
        //Reglerabweichung \f$ e(t) \f$ bestimmen:
        differenz = sollwert-istwert;

        //Berechnung des Integrals mittels numerischer Integration (Summation der Regelabweichung ueber die Zeit)
        integral +=zeit*(differenz-antiwindup);

        //Berechnung der Ableitung mittels finiter Differenzen
        ableitung = (differenz - alter_wert)/zeit;

        //Regelabweichung speichern, um Ableitung berechen zu koennen
        alter_wert=differenz;

        //Reglergesetz: \f$ y(t) = K_{P}*e(t)+K_{I}*\int{e(t)}+K_{D}*\dot{e}(t) \f$
        stellwert = differenz*kp + integral*ki+ ableitung*kd;

        //obere Stellgroessenbeschraenkung beachten
        if(stellwert > max)
        {
            //Anti-Wind-Up berechen
            antiwindup = antiwindupfactor_max*(stellwert-max);
            //Stellwert begrenzen
            stellwert = max;
        }
        //untere Stellgroessenbeschraenkung beachten
        else if(stellwert < min)
        {
            //Anti-Wind-Up berechnen
            antiwindup = antiwindupfactor_min*(stellwert-min);
            //Stellwert begrenzen
            stellwert = min;
        }
        //wenn innerhalb der Stellgroessenbeschraenkungen, so ist der Anti-Wind-Up Faktor 0.
        else
        {
            antiwindup=0;
        }
        //Die Leistung der Heizung - mit Hilfe der numerischen Integration - integrieren,
        //um die umgewandelte Energiemenge zu erhalten.
        //Hierbei wird der Wirkungsgrad beruecksichtigt. 
        energie += (stellwert/wirkungsgrad)*zeit;

        //speichern
        log();
        return stellwert;
    }

    /**
	 * Speichert die aktuellen Attributwerte des Objektes zur tabellarischen Auswertung im csv-Format
	 */
    public void log()
    {
        try 
        {
			bw.write(sollwert+";"+istwert+";"+stellwert+";"+energie+";"+differenz+";"+integral+";"+ableitung+";"+antiwindup+"\n");
        } 
        catch (IOException e)
        {
			System.out.println("ERROR: loggen von PIDRegler fehlgeschlagen.");
		}
    }
}