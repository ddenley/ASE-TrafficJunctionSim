package utility;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Queue;

//Logger implements singleton pattern and runs in its own thread
//Logger saves log file on exit of program
public class Logger implements Runnable{
    // The instance variable holds the single instance of the Logger class
    private static Logger instance= null;

    // The writer variable is used to write log messages to a file
    private PrintWriter writer;
    
    //Queue for log messages
    private Queue<String> logMessages;
    
    private final Object lock = new Object();
    
    //Volatile forces boolean to main memory - prevents cached versions to be used
    //Important as this is the thread termination condition
    private volatile boolean running;

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
            logMessages = new LinkedList<>();
            running = true;
        } catch (IOException e) {
            //Auto-generated catch block If an exception occurs while creating the file writer, print the stack trace
            e.printStackTrace();
        }
    }
    
    // getting the log filename so this method can be used in tests
    public String getFilename()
    {
    	String filename = "Log-" + getDateTime() + ".txt";
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
            Thread loggerThread = new Thread(instance);
            loggerThread.start();
        }

        // Return the single instance of the Logger class
        return instance;
    }

    // The log() method adds message to queue
    //queue adding here uses a locked pattern to ensure only one thread accesses
    //the queue at any one time - thread safe
    public void log(String message) {
        synchronized (lock) {
        	logMessages.add(message);
        	lock.notify();
        }
    }
    
    private static String getDateTime() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();  
		return dtf.format(now);
	}
	

    // Updates runnng thus exiting loop within run and saving the log file
    public void close() {
        running = false;
        synchronized (lock){
        	lock.notify();
        }
    }
    
    @Override
    public void run() {
    	while(running) {
    		String message = null;
    		//Only attempt to log from queue in a thread safe manner using lock
    		synchronized (lock) {
    			if (!logMessages.isEmpty()) {
    				message = logMessages.poll();
    			}
    			else {
    				try {
    					lock.wait();
    				}
    				catch (InterruptedException e) {
    					e.printStackTrace();
    				}
    			}
    		}
    		if (message != null) {
    			writer.println(getDateTime() + " - " + message);
    			writer.flush();
    		}
    	}
    	writer.close();
    }
}
