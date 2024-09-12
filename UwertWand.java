import javax.swing.JOptionPane;

public class UwertWand {
	
	public static double uwert_wand()
	{ 
		
		double uwert_wand = 0.16;
		// Moeglichkeiten f�r Drop-down-menu
        String[] wand={"Keine D�mmung","60mm Styropor","100mm Styropor","150mm Styropor"}; 
        // Auswahlfenster wird angezeigt
        String antwort = (String) JOptionPane.showInputDialog(null, "U_Wert Wand ausw�hlen", "Eingabefenster", 
                JOptionPane.INFORMATION_MESSAGE, null, wand,"Keine D�mmung"); 
     // Wenn Fenster geschlossen wird, beendet sich das Programm
		if(antwort==null)
			System.exit(0);
			// Zuweisung eines Wertes an uwert_wand abh�ngig von der Auswahl
        	if ("Keine D�mmung".equals(antwort)) 
        	{ 
        		uwert_wand = 0.16; 
        	} 
        		else if (antwort.equals("60mm Styropor")) 
        		{ 
        			uwert_wand = 0.111;
        		} 
        		else if (antwort.equals("100mm Styropor")) 
        		{ 
        			uwert_wand = 0.092;
        		}
        		else if (antwort.equals("150mm Styropor"))
        		{
        			uwert_wand = 0.076;
        		}
		    //Gibt U-Wert zur�ck
        	return uwert_wand;
	}
}
