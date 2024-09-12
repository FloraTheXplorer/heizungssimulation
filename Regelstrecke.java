/**
 * Diese Bibliothek wird fuer Datei-Operationen benoetigt
 */
import java.io.*;
/**
 * @brief Klasse fuer die Regelstrecke. Diese repraesentiert den zu heizenden Raum.
 * @author Florette
 */
public class Regelstrecke implements ILog, IZeit
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
     * aktuelle Innenraumtemperatur in Grad Celsius [°C]
     */
    private double aktuelle_temperatur;
    /**
     * Masse der Luft im Raum in Kilogramm [kg]
     */
    private double masse;
    /**
     * spezifische Waermekapazitaet fuer trockene Luft  [J/(kg*K)]
     */
    private double c; 
    /**
     * aktuell hinzugefuegte Waermemenge in Joule [J]
     */
    private double waermemenge;
    /**
     * aktuelle Waermeleistung in Watt [W]
     */
    private double waermeleistung;

    /**
     * Konstruktor fuer die Regelstrecke
     * @param[in] temperatur initiale Raumtemperatur [°C]
     * @param[in] masse Masse der Luft in [kg]
     * @param[in] c spezifische Waermekapazitaet fuer trockene Luft  [J/(kg*K)]
     * @param[in] wand Diese Zahl entspricht dem Index des aktuellen Wand-Feldes (Array) und dient nur der Benennung der Log-Datei
     * @param[in] fenster Diese Zahl entspricht dem Index des aktuellen Fenster-Feldes (Array) und dient nur der Benennung der Log-Datei
     */
	public Regelstrecke(double temperatur,double masse, double c,int wand, int fenster)
	{
		if(temperatur>50||temperatur<-50)
			throw new IllegalArgumentException("Raumtemperatur ist ungültig.");
        //Parameter uerbnehmen
        this.aktuelle_temperatur = temperatur;
        
        if(masse <=0)
        	throw new IllegalArgumentException("Luftmasse ist ungültig.");
        
        this.masse = masse;
        
        if(c<=0)
        	throw new IllegalArgumentException("Spezifische Wärmekapazität ist ungültig.");
        this.c = c;

        //Fehlerbehandlung fuer Dateizugriff, z.B. Datei ist schreibgeschuetzt
		//Fehler ist nicht kritisch fuer die Ausfuehrung, daher kann fortgefahren werden.
		        try
		        {
					fw = new FileWriter("Regelstrecke"+wand+"_"+fenster+".csv");
		        }
		        catch (IOException e)
        {
			System.out.println("ERROR: Anlegen der Log-Datei fuer Regelstrecke fehlgeschlagen.");
        }
        //Datei an den Schreiber uebergeben
        bw = new BufferedWriter(fw);
    }

    /**
     * Berechnung der abgegebenen Waermemenge und
     * damit die neue Raumtemperatur.
     * \f$ Q = P*t\f$
     * \f$ T_{2} = T_{1} + \frac{Q}{m*c}\f$
     * @param[in] zeit vergangene Zeit in Sekunden seit dem letzten Aufruf
     * @return aktuelle Raumtemperatur in [*C]
     */
    public double berechne(double zeit)
    {
    	if(zeit<=0)
    		throw new IllegalArgumentException("vergangene Zeit ist zu klein.");
        //Berechnung der Energie \f$ Q = P*t\f$
        waermemenge = waermeleistung*zeit;
        //\f$ T_{2} = T_{1} + \frac{Q}{m*c}\f$
        aktuelle_temperatur += waermemenge/(masse*c);
        //speichern
        log();
        return aktuelle_temperatur;
    }

    /**
     * Setter fuer die Waermeleistung
     * @param[in] waermeleistung Waermeleistung in Watt [W]
     */
    public void setWaermeleistung(double waermeleistung)
    {
        this.waermeleistung = waermeleistung;
    }

    /**
	 * Speichert die aktuellen Attributwerte des Objektes zur tabellarischen Auswertung im csv-Format
	 */
    public void log()
    {
        try
        {
			bw.write(aktuelle_temperatur+";"+waermeleistung+";"+waermemenge+"\n");
        }
        catch (IOException e)
        {
			System.out.println("ERROR: loggen von Regelstrecke fehlgeschlagen.");
		}
    }
}