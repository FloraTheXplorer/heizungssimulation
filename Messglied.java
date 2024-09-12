/**
 * @brief Klasse fuer die Messung des Ist-Wertes (Ausgangsgroesse).
 * @author Florette
 */
public class Messglied implements ILog, IZeit
{
    /**
     * Faktor wie der Messwert (Ist-Wert fuer die Zustandsrueckfuerung)
     * im Verhaeltnis zur tatsaechlichen Ausgangsgroesse (realer Ist-Wert) steht.
     */
    private double messfaktor;
    /**
     * aktueller Messwert (Rueckfuehrung)
     */
    private double messwert;
    /**
     * aktueller Ist-Wert (Ausgangsgroesse der Regelstrecke)
     */
    private double istwert;

    /**
     * Konstruktor des Messgliedes
     * @param[in] messfaktor (Messwert/Ausgangsgroesse)
     */
    public Messglied(double messfaktor)
    {
        //Attribute initialisieren.
        this.messfaktor = messfaktor;
        this.messwert = 0;
        this.istwert = 0;
    }

    /**
     * Setter fuer den Ist-Wert (Ausgangsgroesse)
     * @param[in] istwert Innenraumtemperatur in Grad Celsius [°C]
     */
    public void setIstwert(double istwert)
    {
        this.istwert = istwert;
    }

    /**
     * Getter fuer den Messwert (Rueckfuehrung)
     * @return Innenraumteperatur in Grad Celsius [°C]
     */
    public double getMesswert()
    {
        return messwert;
    }

    /**
     * Zeit abhaengige Berechnung des Messwertes
     * @param[in] zeit Zeit in Sekunden seit dem letzten Aufruf
     * @return Messwert (Innenraumteperatur) in Grad Celsius [°C]
     */
    public double berechne(double zeit)
    {
    	if(zeit<=0)
    		throw new IllegalArgumentException("vergangene Zeit ist zu klein.");
        //Messwert berechen
        messwert = istwert*messfaktor;
        //speichern
        log();
        return messwert;
    }

    public void log()
    {
        //Es muss nichts gespeichert werden, weil der Messfaktor 1 ist.
    }
}