package JunctionSim;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    // The instance variable holds the single instance of the Logger class
    private static Logger instance= null;

    // The writer variable is used to write log messages to a file
    private PrintWriter writer;

    // The private constructor is called only once to create the single instance of the Logger class
    private Logger() {
        try {
        	String fileName = getFilename(); 
            // Create a FileWriter object that writes to a file named "log.txt"
            FileWriter fw = new FileWriter(fileName, true);

            // Create a BufferedWriter object that wraps the FileWriter object
            BufferedWriter bw = new BufferedWriter(fw);

            // Create a PrintWriter object that wraps the BufferedWriter object
            writer = new PrintWriter(bw);
        } catch (IOException e) {
            //Auto-generated catch block If an exception occurs while creating the file writer, print the stack trace
            e.printStackTrace();
        }
    }
    
    // getting the log filename so this method can be used in tests
    public String getFilename()
    {
    	String filename = "Log-" + getDateTime().substring(0, 10) + ".txt";
    	//changing "/" ":" characters so that it is a valid filename
    	filename = filename.replace("/", "_").replace(":", "_");
    	// save in directory logs
    	filename = "logs/" + filename;
    	return filename;
    }

    // The static getInstance() method returns the single instance of the Logger class
    public static synchronized Logger getInstance() {
        // If the instance variable is null, create a new instance of the Logger class
        if (instance == null) {
            instance = new Logger();
        }

        // Return the single instance of the Logger class
        return instance;
    }

    // The log() method writes a log message to the log file
    public void log(String message) {
        // Write the message to the log file using the PrintWriter object created in the constructor
        writer.println(getDateTime() + " - " + message);

        // Call flush() to ensure that the message is immediately written to the file
        writer.flush();
    }
    
    private static String getDateTime() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();  
		return dtf.format(now);
	}
	

    // The close() method closes the PrintWriter object and the log file
    public void close() {
        // Close the PrintWriter object
        writer.close();
    }
}
