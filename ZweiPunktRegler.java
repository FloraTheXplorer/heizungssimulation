/**
 * @brief Klasse fuer den einfachsten und schlechtesten Regler,
 * weil er stark um den Arbeitspunkt (Sollwert) schwingt.
 * @author Florette
 */
public class ZweiPunktRegler extends Regler
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
     * Konstruktor fuer den einfachsten Regler
     * @param[in] min minimaler Stellwert in Watt [W]
     * @param[in] max maximaler Stellwert in Watt [W]
     */
    public ZweiPunktRegler(double min, double max)
    {
        this.min = min;
        this.max = max;
    }

    /**
     * Berechne den neuen Sollwert (Stellgroesse hier Heizleistung).
     * Der auf die Regelstrecke (Wohnraum) gegeben werden soll.
     * @param[in] zeit Zeit in Sekunden, die seit dem letzten Aufruf vergangen ist.
     * @return Stellwert (Leistung) in Watt [W]
     */
    public double berechne(double zeit)
    {
        if(sollwert>istwert)
        { return max; }
        else
        { return min; }
    }
}