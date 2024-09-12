/**
 * @brief Hauptklasse zum Starten des Programms
 * @author Florette
 */
public class Main {
	/**
	 * Haupteinstiegspunkt des Programms
	 * @param[in] args nicht benoetigte Parameter
	 */
	public static void main(String[] args)
	{
		//Übernahme der Sollwerte aus Eingabe
		double sollTag = SollwertTag.sollwertTag();
		double sollNacht = SollwertNacht.sollwertNacht();
		double temperatur_ist = sollNacht;
		//Übernahme der U-Werte aus Auswahl
		double u_wert_wand = UwertWand.uwert_wand();
		double u_wert_fenster = UwertFenster.uwert_fenster();
		//Reglestrecke (Haus bzw. Wohnraum) erstellen und initialisieren.
		Regelstrecke haus = new Regelstrecke(temperatur_ist,490.96,1.005*1000.0,0,0);
		//Sollwertvorgabe (Innenraumtemperatur) fuer den Regler erstellen und initialisieren.
		SollwertRaum temperatur_vorgabe = new SollwertRaum(sollTag,sollNacht);
		//Stoerungsvorgabe (Aussentemperatur) fuer die Stoerung erstellen und initialisieren.
		SollwertStoerung aussentemperatur_vorgabe = new SollwertStoerung(0,0);
		//Stoerung (Verlustleistung) erstellen und initialisieren.
		Stoerung verlustleistung = new Stoerung(311.9763,u_wert_wand,14.4396,u_wert_fenster,0,0);
		//Messglied (Thermostat) erstellen und initialisieren.
		Messglied thermostat = new Messglied(1);
		//Regler (Heizung) erstellen und initialisieren.
		PIDRegler heizung = new PIDRegler(8000,0.042,0.0018,0,15000,0.000122,0.00001,1.0,0,0);
		//Anfangstemperatur des Innenraums.

		//Das ganze Jahr simulieren:
		//Fuer alle Monate im Jahr...
		for(int monat=0; monat<12; monat++)
		{
			{if(monat<0 || (monat-((int)monat))!=0 || monat >11)
	    		throw new IllegalArgumentException("Nur ganze Monate zwischen 0 und 11 erlaubt.");
				}
			//Fuer alle Tage im Monat...
			for(int tag=0; tag<30; tag++)
			{
				{if(tag<0 || (tag-((int)tag))!=0 || tag >29)
		    		throw new IllegalArgumentException("Nur ganze Tage zwischen 0 und 29 erlaubt.");
				}
				//Fuer alle Stunden eines Tages...
				for(int stunde=0; stunde<24; stunde++)
				{
					{if(stunde<0 || (stunde-((int)stunde))!=0 || stunde >23)
			    		throw new IllegalArgumentException("Nur ganze Stunden zwischen 0 und 23 erlaubt.");
					}
					//Fuer alle Minuten einer Stunde...
					for(int minute=0; minute<60; minute++)
					{
						{if(minute<0 || (minute-((int)minute))!=0 || minute >59)
				    		throw new IllegalArgumentException("Nur ganze Minuten zwischen 0 und 59 erlaubt.");
						}
							//Es wird die vergangene Zeit in Sekunden benoetigt.
							double vergangene_zeit =60;
							//Sollwert berechnen und ihn dem Regler uebergeben.
							heizung.setSollwert(temperatur_vorgabe.berechne_temperatur(monat,tag,stunde,minute));
							//aktuelle Raumtemperatur uebergeben.
							heizung.setIstwert(temperatur_ist);
							//Um die Verlustleistung durch die Waende und Fenster zu berechen wird sowohl die Inneraumtemperatur benoetigt
							verlustleistung.setInnentemperatur(temperatur_ist);
							//als auch die Aussentemperatur.
							verlustleistung.setAussentemperatur(aussentemperatur_vorgabe.berechne_temperatur(monat,tag,stunde,minute));
							//Stellwert berechen.
							double leistung = heizung.berechne(vergangene_zeit);
							//Stoerung auf den Stellwert anwenden
							leistung -= verlustleistung.berechne(vergangene_zeit);
							//Temperatur mit der tatsaechlichen Waermeleistung errechen.
							haus.setWaermeleistung(leistung);
							//Temperatur messen. Da kein Messfehler angenommen wurde, aendert dieser Vorgang nichts.
							thermostat.setIstwert(haus.berechne(vergangene_zeit));
							temperatur_ist = thermostat.berechne(vergangene_zeit);

					}
				}
			}
		}
	}

}
