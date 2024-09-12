/**
 * Die Bibliothek wird fuer Sinus und PI benoetigt
 */
import java.lang.Math;

/**
 * Diese Bibliothek wird fuer Datei-Operationen benowtigt
 */
import java.io.*;

/**
 * @brief Klasse zur Berechnung der Aussentemperatur abhaengig von der Jahres- und Tageszeit
 * @author Florette
 */
public class SollwertStoerung implements ILog
{
	/**
	 * Aussentemperatur aktuelle abhaengig von Monat, Tag, Stunde und Minute.
	 */
	private double temperatur_tag;

	/**
	 * Basisaussentemperatur des Tages abhaengig von Monat und Tag
	 */
	private double temperatur_jahr;

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
	 * Wert zwischen 0 und exklusive 1. Wie weit ist der Tag vorangeschritten? (0 =>0%, 1 => 100%)
	 */
	private double x1;

	/**
	 * /Wert zwischen 0 und exklusive 1. Wie weit ist das Jahr vorangeschritten? (0 => 0%, 1 => 100%)
	 */
	private double x2;

	/**
	 * Objekt fuer den Zugriff auf das Dateisystem
	 */
	FileWriter fw;

	/**
	 * Bufferobjekt, um in Dateien schreiben zu koennen.
	 */
    BufferedWriter bw;

	/**
	 * Konstruktor fuer die Berechnung der Aussentemperatur.
	 * Diese bildet einen Signaleingang der eigentlichen Stoerung
	 * und ist abhaengigvon der Jahres und Tageszeit.
	 * @param[in] wand Diese Zahl entspricht dem Index des aktuellen Wand-Feldes (Array) und dient nur der Benennung der Log-Datei
     * @param[in] fenster Diese Zahl entspricht dem Index des aktuellen Fenster-Feldes (Array) und dient nur der Benennung der Log-Datei
	 */
    public SollwertStoerung(int wand, int fenster)
    {
		//Werte initialisieren
    	this.temperatur_tag=0;
    	this.temperatur_jahr =0;
    	this.x1=0;
		this.x1=0;

		//Fehlerbehandlung fuer Dateizugriff, z.B. Datei ist schreibgeschuetzt
		//Fehler ist nicht kritisch fuer die Ausfuehrung, daher kann fortgefahren werden.
		try
		{
			fw = new FileWriter("SollwertStoerung"+wand+"_"+fenster+".csv");
		}
		catch (IOException e)
		{
			System.out.println("ERROR: Anlegen der Log-Datei fuer SollwertStoerung fehlgeschlagen.");
		}
		//Datei an den Schreiber uebergeben
        bw = new BufferedWriter(fw);
    }

	/**
	 * Berechnung der Aussentemperatur anhand der Tages- und
	 * Jahreszeit
	 * @param[in] monat Monat des Jahres (0 bis 11)
	 * @param[in] tag Tag des Monats(0 bis 29)
	 * @param[in] stunde Stunde des Tages (0 bis 23)
	 * @param[in] minute Minute der Stunde (0 bis 59)
	 * @return Aussentemperatur in Grad Celsius [*C]
	 */
	public double berechne_temperatur(double monat, double tag, double stunde, double minute)
	{
		//Parameter fuer die Log-Datei speichern
		this.monat = monat;
		this.tag = tag;
		this.stunde = stunde;
		this.minute = minute;
		//Wert zwischen 0 und exklusive 1. Wie weit ist der Tag vorangeschritten? (0 =>0%, 1 => 100%)
		x1 = ((stunde-2)/24.0)	+(minute)/(60.0*24.0);
		//Wert zwischen 0 und exklusive 1. Wie weit ist das Jahr vorangeschritten? (0 => 0%, 1 => 100%) 
		x2 = monat/12.0	+(tag)/(12*30.0);
		//Sinus-Temperaturprofil des Jahres
		temperatur_jahr = -(19.0/2.0)*Math.cos(x2*2.0*Math.PI)+10.5;
		//Sinus-Temperaturprofil des Tages auf das des Jahres addiert gibt die Aussentemperatur
		temperatur_tag=-4.0*Math.cos(x1*2.0*Math.PI)+temperatur_jahr;
		//speichern
		log();
		return temperatur_tag;
	}

	/**
	 * Speichert die aktuellen Attributwerte des Objektes zur tabellarischen Auswertung im csv-Format
	 */
	public void log()
	{
		try
		{
			bw.write(monat+";"+tag+";"+stunde+";"+minute+";"+temperatur_tag+";"+temperatur_jahr+"\n");
		} 
		catch (IOException e) 
		{
			System.out.println("ERROR: loggen von SollwertStoerung fehlgeschlagen.");
		}
	}
}
