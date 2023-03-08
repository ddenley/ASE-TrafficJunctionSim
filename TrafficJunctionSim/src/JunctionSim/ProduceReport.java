package JunctionSim;
import java.time.format.DateTimeFormatter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;   

public class ProduceReport {
	
	//Function adapted from: https://www.javatpoint.com/java-get-current-date
	private static String getDateTime() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();  
		return dtf.format(now);
	}
	
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
