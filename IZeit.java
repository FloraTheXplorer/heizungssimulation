/**
 * @brief Interface fuer alle Klassen, die die Zeit zwischen zwei Regelaufrufen benoetigen.
 * @author Florette
 */
public interface IZeit
{
    /**
     * Funktion, die von der vergangenen Zeit abhaengig ist.
     * Zeit in Sekunden.
     */
    public double berechne(double zeit);
}