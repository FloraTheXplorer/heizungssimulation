/**
 * Diese Bibliothek wird fuer Datei-Operationen benoetigt
 */
import java.io.*;

/**
 * @brief Klasse zur Bestimmung der Soll-Raumtemperatur.
 * Sie beruecksichtigt den Tag-Nacht-Zyklus
 * @author Florette
 */
public class SollwertRaum implements ILog
{

    /**
     * Solltemperatur des Raumes
     */
    private double raumtemperatur;

	/**
	 * Monat des Jahres (0 bis 11)
     * Typ Double, damit keine Konvertierungsprobleme entstehen.
	 */
	private double monat;
	
	/**
	 * Tag des Monats (0 bis 29)
     * Typ Double, damit keine Konvertierungsprobleme entstehen.
	 */
	private double tag;

	/**
	 * Stunde des Tages (0 bis 23)
     * Typ Double, damit keine Konvertierungsprobleme entstehen.
	 */
	private double stunde;

	/**
	 * Minute der Stunde (0 bis 59)
     * Typ Double, damit keine Konvertierungsprobleme entstehen.
	 */
    private double minute;
    
	/**
	 * Objekt fuer den Zugriff auf das Dateisystem
	 */
	FileWriter fw;

	/**
	 * Bufferobjekt, um in Dateien schreiben zu koennen.
	 */
    BufferedWriter bw;
    
    /**
     * Solltagestemperatur
     */
    double sollwertTag;
    /**
     * Sollnachttemperatur;
     */
    double sollwertNacht;

    /**
     * Konstruktor der Sollwertvorgabe fuer die Innenraumtemperatur
     * @param[in] sollwertTag Solltagestemperatur
     * @param[in] sollWertNacht Sollnachttemperatur
	 */
    public SollwertRaum(double sollwertTag, double sollwertNacht)
    {
        //Attribute initialisieren
        this.raumtemperatur=0;
        this.sollwertNacht = sollwertNacht;
        this.sollwertTag = sollwertTag;
        //Fehlerbehandlung fuer Dateizugriff, z.B. Datei ist schreibgeschuetzt
		//Fehler ist nicht kritisch fuer die Ausfuehrung, daher kann fortgefahren werden.
        try
        {
			fw = new FileWriter("SollwertRaum0_0.csv");
        }
        catch (IOException e)
        {
            System.out.println("ERROR: Anlegen der Log-Datei fuer SollwertRaum fehlgeschlagen.");
        }
        //Datei an den Schreiber uebergeben
        bw = new BufferedWriter(fw);
    }

    /**
	 * Berechnung der Innenraumtemperatur anhand der Tages- und
	 * Jahreszeit
	 * @param[in] monat Monat des Jahres (0 bis 11)
	 * @param[in] tag Tag des Monats(0 bis 29)
	 * @param[in] stunde Stunde des Tages (0 bis 23)
	 * @param[in] minute Minute der Stunde (0 bis 59)
	 * @return Innenraumtemperatur in Grad Celsius [*C]
	 */
    public double berechne_temperatur(double monat, double tag, double stunde, double minute)
	{
    	if(monat<0 || (monat-((int)monat))!=0 || monat >11)
    		throw new IllegalArgumentException("Nur ganze Monate zwischen 0 und 11 erlaubt.");
    	if(tag<0 || (tag-((int)tag))!=0 || tag >29)
    		throw new IllegalArgumentException("Nur ganze Tage zwischen 0 und 29 erlaubt.");
    	if(stunde<0 || (stunde-((int)stunde))!=0 || stunde >23)
    		throw new IllegalArgumentException("Nur ganze Stunden zwischen 0 und 23 erlaubt.");
    	if(minute<0 || (minute-((int)minute))!=0 || minute >59)
    		throw new IllegalArgumentException("Nur ganze Minuten zwischen 0 und 59 erlaubt.");
    	
        //Parameter fuer die Log-Datei speichern
		this.monat = monat;
		this.tag = tag;
		this.stunde = stunde;
        this.minute = minute;
        
        //Nacht
        if((0<=stunde && stunde <7)||(22<=stunde))
        {raumtemperatur=sollwertNacht;}
        //Tag
        else
        {raumtemperatur=sollwertTag;}
        //speichern
        log();
        return raumtemperatur;
    }

    /**
	 * Speichert die aktuellen Attributwerte des Objektes zur tabellarischen Auswertung im csv-Format
	 */
    public void log()
    {
        try 
        {
			bw.write(monat+";"+tag+";"+stunde+";"+minute+";"+raumtemperatur+"\n");
        } 
        catch (IOException e)
        {
			System.out.println("ERROR: loggen von SollwertRaum fehlgeschlagen.");
		}
    }
}
