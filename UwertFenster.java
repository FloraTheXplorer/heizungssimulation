import javax.swing.JOptionPane;

public class UwertFenster {

	public static double uwert_fenster()
	{ 
		
		double uwert_fenster=1;
			// Moeglichkeiten f�r Drop-down-menu
	        String[] fenster={"Einfachverglasung","Doppelverglasung","Dreifachverglasung"};  
	        // Auswahlfenster wird angezeigt
	        String antwort = (String) JOptionPane.showInputDialog(null, "U_Wert Fenster ausw�hlen", "Eingabefenster", 
	                JOptionPane.INFORMATION_MESSAGE, null, fenster,"Einfachverglasung"); 
	        // Wenn Fenster geschlossen wird, beendet sich das Programm
			if(antwort==null)
				System.exit(0);
				// Zuweisung eines Wertes an uwert_fenster abh�ngig von der Auswahl
		        if (antwort.equals("Einfachverglasung")) 
		        { 
		            uwert_fenster = 5.5; 
		        } 
			    else if (antwort.equals("Doppelverglasung")) 
		        { 
		            uwert_fenster = 1.3;
		        }
		        else 
		        { 
		            uwert_fenster = 0.5; 
		        }
		     //Gibt U-Wert zur�ck
	         return uwert_fenster;
	}
}
