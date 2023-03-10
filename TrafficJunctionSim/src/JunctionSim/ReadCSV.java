package JunctionSim;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Daniel Denley
 *
 */

public class ReadCSV {
	
	//Static method for reading and parsing the contents of a CSV file - either intersection or vehicles
	//Can throw invalidFileFormatExceptions to JVM to prevent illegal states - e.g. no content after header
	//Will ignore blank lines within a csv file
	//Returns both the header of the csv[0] and the values[1] within an object array
	public static Object[] getHeaderValues(String filePath) {
		Object[] header_values = new Object[2];
		String[] header = null;
		ArrayList<String[]> values = new ArrayList<String[]>();
		String fileLine = "";
		int lineCount = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			while((fileLine = br.readLine()) != null) {
				fileLine = fileLine.replace(" ", "");
				//Check if file line has contents if not skip
				if (fileLine.equals("")) {
					System.out.println("Invalid file line in: " + filePath);
					System.out.println("Skipping line");
					continue;
				}
				//Check if file line has appropriate number of columns if not skip
				if (fileLine.split(",").length != 8 && filePath.equals(GUIMain.vehiclesCSVFile)) {
					System.out.println("Invalid file line in: " + filePath);
					System.out.println("Skipping line");
					continue;
				}
				if (fileLine.split(",").length != 2 && filePath.equals(GUIMain.intersectionCSVFile)) {
					System.out.println("Invalid file line in: " + filePath);
					System.out.println("Skipping line");
					continue;
				}
				lineCount++;
				if(lineCount == 1) {
					header = fileLine.split(",");
					continue;
				}
				String[] lineValues = fileLine.split(",");
				values.add(lineValues);
			}
			//Throw an exception if there isnt at least two lines - a header and one vehicle
			if(lineCount < 2 && filePath.equals(GUIMain.vehiclesCSVFile)) {
				System.out.println(lineCount);
				throw new InvalidFileFormatException(filePath);
			}
			//Throw an exception if there isnt at least eight phases in the intersection file
			if(lineCount < 9 && filePath.equals(GUIMain.intersectionCSVFile)) {
				throw new InvalidFileFormatException(filePath);
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		header_values[0] = header;
		header_values[1] = values;
		return header_values;
	}
}
