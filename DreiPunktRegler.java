/**
 * @brief Klasse fuer einen verbesserten Regler mit Toleranz, um uebertriebene Ausgleichsschwingungen um die Solltemperatur zu vermeiden
 * @author Florette
 */
public class DreiPunktRegler extends Regler
{
    /**
     * minimaler Stellwert in Watt [W]
     */
    private double min;

    /**
     * maximaler Stellwert in Watt [W]
     */
    private double max;

    /**
     * Toleranz in Grad Celsius [°C]
     */
    private double toleranz;

    /**
     * Konstruktor fuer den einfachsten Regler
     * @param[in] min minimaler Stellwert in Watt [W]
     * @param[in] max maximaler Stellwert in Watt [W]
     * @param[in] toleranz Toleranz in Grad Celsius [°C], die die Heizung akzeptiert ohne aktiv zu werden.
     */
    public DreiPunktRegler(double min, double max,double toleranz)
    {
        this.min = min;
        this.max = max;
        this.toleranz = toleranz;
    }

    /**
     * Berechne den neuen Sollwert (Stellgroesse hier Heizleistung).
     * Der auf die Regelstrecke (Wohnraum) gegeben werden soll.
     * @param[in] zeit Zeit in Sekunden, die seit dem letzten Aufruf vergangen ist.
     * @return Stellwert (Leistung) in Watt [W]
     */
    public double berechne(double zeit)
    {
        //zu klein
        if(sollwert>(istwert+toleranz))
        { return max; }
        //zu gross
        else if(sollwert<(istwert-toleranz))
        { return min; }
        //in der Toleranzgrenze
        else
        {return 0;}
    }
}