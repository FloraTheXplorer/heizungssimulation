/**
 * @brief Elternklasse fuer Regler im Allgemeinen
 * @author Florette
 */
public abstract class Regler implements IZeit
{
    /**
     * Sollwert
     */
    protected double sollwert;

    /**
     * Istwert
     */
    protected double istwert;

    /**
     * Stellwert/Stellgroesse
     */
    protected double stellwert;

    /**
     * Konstruktor fuer Regler
     */
    public Regler(){ this.sollwert =0; this.istwert = 0;}

    /**
     * Setter fuer den Sollwert
     * @param[in] sollwert Sollwert
     */
    public void setSollwert(double sollwert) {this.sollwert = sollwert;}

    /**
     * Setter fuer den Ist-Wert
     * @param[in] istwert Ist-Wert
     */
    public void setIstwert(double istwert){this.istwert = istwert;}

    /**
     * Funktionsdeklaration zur Berechnung des Stellwertes in Abhaengigkeit von der vergangenen Zeit
     * @param[in] in Sekunden seit dem letzten Aufruf
     */
    public abstract double berechne(double zeit);
}