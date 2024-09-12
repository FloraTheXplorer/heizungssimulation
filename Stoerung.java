/**
 * Diese Bibliothek wird fuer Datei-Operationen benoetigt
 */
import java.io.*;

/**
 * @brief Klasse zur Berechnung der Verlustleistung durch die Waende und Fenster
 * @author Florette
 */
public class Stoerung implements ILog,IZeit
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
     * Wandflaeche nach aussen in [m^2]
     */
    private double flaeche_wand;

    /**
     * U-Wert (Waermedurchgangskoeffizient) der Wand in [W/m^2]
     */
    private double uwert_wand;

    /**
     * U-Wert (Waermedurchgangskoeffizient) der Verglasung in [W/m^2]
     */
    private double uwert_fenster;

    /**
     * Fensterflaeche nach aussen in [m^2]
     */
    private double flaeche_fenster;

    /**
     * Aussentemperatur in Grad Celsius [°C]
     */
    private double aussentemperatur;

    /**
     * Innenraumtemperatur in Grad Celsius [°C]
     */
    private double innentemperatur;

    /**
     * Verlustleistung in Watt [W]
     */
    private double waermeleistung;

    /**
     * Temperaturdifferenz zwischen Innen und Aussen in Grad Celsius [°C]
     */
    private double temperatur_differenz;

    /**
     * Konstruktor fuer die Berechnung der Verlustleistung
     * @param[in] flaeche_wand Wandflaeche in [m^2]
     * @param[in] uwert_wand Waermedurchgangskoeffizient der Wand in [W/m^2]
     * @param[in] flaeche_fenster Fensterflaeche in [m^2]
     * @param[in] uwert_fenster Waermedurchgangskoeffizient der Fenster in [W/m^2]
     * @param[in] wand Diese Zahl entspricht dem Index des aktuellen Wand-Feldes (Array) und dient nur der Benennung der Log-Datei
     * @param[in] fenster Diese Zahl entspricht dem Index des aktuellen Fenster-Feldes (Array) und dient nur der Benennung der Log-Datei
	 */
    public Stoerung (double flaeche_wand, double uwert_wand,double flaeche_fenster, double uwert_fenster,int wand, int fenster)
    {
        //Parameter speichern
    	if(flaeche_fenster<=0)
    		throw new IllegalArgumentException("Die Fensterfläche darf nicht negativ oder 0 sein.");
    	if(flaeche_wand<=0)
    		throw new IllegalArgumentException("Die Wandfläche darf nicht negativ oder 0 sein.");
    	if(uwert_wand<=0)
    		throw new IllegalArgumentException("Der U-wert darf nicht kleiner gleich 0 sein.");
    	if(uwert_fenster<=0)
    		throw new IllegalArgumentException("Der U-wert darf nicht kleiner gleich 0 sein.");
        this.flaeche_wand = flaeche_wand;
        this.uwert_wand = uwert_wand;
        this.flaeche_fenster = flaeche_fenster;
        this.uwert_fenster = uwert_fenster;
        this.aussentemperatur = 0;
        this.innentemperatur = 0;
        this.waermeleistung = 0;
        this.temperatur_differenz =0;

        //Fehlerbehandlung fuer Dateizugriff, z.B. Datei ist schreibgeschuetzt
		//Fehler ist nicht kritisch fuer die Ausfuehrung, daher kann fortgefahren werden.
        try
        {
			fw = new FileWriter("Stoerung"+wand+"_"+fenster+".csv");
        } 
        catch (IOException e)
        {
			System.out.println("ERROR: Anlegen der Log-Datei fuer Stoerung fehlgeschlagen.");
        }
        //Datei an den Schreiber uebergeben
        bw = new BufferedWriter(fw);
    }

    /**
     * Setter fuer die Aussentemperatur
     * @param[in] temperatur Aussentemperatur in Grad Celsius [*C]
     */
    public void setAussentemperatur(double temperatur)
    {
		if(temperatur>50||temperatur<-50)
			throw new IllegalArgumentException("Aussentemperatur ist ungültig.");
        this.aussentemperatur = temperatur;
    }

    /**
     * Setter fuer die Innentemperatur
     * @param[in] temperatur Innentemperatur in Grad Celsius [*C]
     */
    public void setInnentemperatur(double temperatur)
    {
		if(temperatur>50||temperatur<-50)
			throw new IllegalArgumentException("Innentemperatur ist ungültig.");
        this.innentemperatur = temperatur;
    }

    /**
     * Berechnung der Verlustleistung anhand der Flaechen, deren Durchlaessigkeiten
     * und anhand der Temperaturdifferenz
     * \f$ P = \Delta T * A * (u-wert) \f$
     */
    public double berechne(double zeit)
    {
    	if(zeit<=0)
    		throw new IllegalArgumentException("vergangene Zeit ist zu klein.");
        //berechne Delta T
        temperatur_differenz = (innentemperatur-aussentemperatur);
        //Verlustleistung fuer die Waende
        waermeleistung = flaeche_wand*temperatur_differenz*uwert_wand;
        //Verlustleistung fuer die Fenster
        waermeleistung += flaeche_fenster*temperatur_differenz*uwert_fenster;
        //speichern
        log();
        return waermeleistung;
    }

    /**
	 * Speichert die aktuellen Attributwerte des Objektes zur tabellarischen Auswertung im csv-Format
	 */
    public void log()
    {
        try
        {
			bw.write(waermeleistung+";"+temperatur_differenz+";"+innentemperatur+";"+aussentemperatur+";"+uwert_wand+";"+uwert_fenster+"\n");
        }
        catch (IOException e)
        {
            System.out.println("ERROR: loggen von Stoerung fehlgeschlagen.");
		}
    }
}