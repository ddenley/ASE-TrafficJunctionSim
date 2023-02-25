package JunctionSim;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class ReadCSV {
	
	//Get CSV headers
	public static Object[] getHeaderValues(String filePath) {
		Object[] header_values = new Object[2];
		String[] header = null;
		ArrayList<String[]> values = new ArrayList<String[]>();
		String fileLine = "";
		int lineCount = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			while((fileLine = br.readLine()) != null) {
				lineCount++;
				if(lineCount == 1) {
					header = fileLine.split(",");
					continue;
				}
				String[] lineValues = fileLine.split(",");
				values.add(lineValues);
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
