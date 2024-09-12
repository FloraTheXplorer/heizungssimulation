import javax.swing.JOptionPane;


public class SollwertTag {

	public static double sollwertTag()
	{
 
		boolean richtig = false;
		String eingabe;
		// Schleife, die sich bei falscher Eingabe wiederholt
		while(!richtig)
		{
		//Ruft Eingabefenster auf
		eingabe = JOptionPane.showInputDialog(null, "Geben Sie eine Tagessolltemperatur zwischen 16°C und 25°C ein.",JOptionPane.PLAIN_MESSAGE);
		// Wenn Fenster geschlossen wird, beendet sich das Programm
		if(eingabe==null)
			System.exit(0);
		// Weist "eingabe" je nach Eingabe den entsprechenden Wert zu
		switch(eingabe)
		{
		
		case "16": return 16;
		case "17": return 17;
		case "18": return 18;
		case "19": return 19;
		case "20": return 20; 
		case "21": return 21;
		case "22": return 22;
		case "23": return 23;
		case "24": return 24;
		case "25": return 25;
		// Zeigt bei unzulässiger Eingabe Hinweisfenster an
		default: JOptionPane.showMessageDialog (null, "Sie müssen eine ganze Zahl zwischen 16 und 25 eingeben!", "Unzulässige Eingabe",JOptionPane.WARNING_MESSAGE);
		}
		}
		//Für den Fehlerfall der niemals existieren kann.
		return 18; 
	}
}

