package utility;
import java.time.format.DateTimeFormatter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;   

/**
 * @author Daniel Denley
 *
 */
public class ProduceReport {
	
	//Function adapted from: https://www.javatpoint.com/java-get-current-date
	//Function used to include within the report its time of creation
	//Also used when naming files for easier navigation
	private static String getDateTime() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();  
		return dtf.format(now);
	}
	
	//Private class method for file writing
	//Called via the public CreateReport class method
	private static void writeReport(String[] reportLines) throws IOException {
		String fileName = "JunctionReport-" + getDateTime() + ".txt";
		fileName = fileName.replace("/", "_");
		fileName = fileName.replace(":", "_");
		FileWriter fw = new FileWriter(fileName);
	 
		for (int i = 0; i < reportLines.length; i++) {
			fw.write(reportLines[i]);
			fw.write("\n");
		}
	 
		fw.close();
	}
	
	//Static method to be called by GUI
	//Takes any array of strings and appends to a report
	public static void createReport(String[] contents) {
		String[] reportLines = new String[contents.length + 2];
		reportLines[0] = "Traffic Junction Simulator Report || Dated: " + getDateTime();
		reportLines[1] = "Statistics Per Phase:";
		
		for (int i = 0; i < contents.length; i++) {
			reportLines[2 + i] = contents[i];
		}
		
		try {
			writeReport(reportLines);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
