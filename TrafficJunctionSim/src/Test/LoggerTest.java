package Test;

import JunctionSim.Logger;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class LoggerTest {
    private Logger logger;
    

    @Before
    public void setUp() {
        // Set up the Logger instance
        logger = Logger.getInstance();
    }

    @After
    public void tearDown() {
        // Close the Logger instance
        logger.close();
    }

    @Test
    public void testLog() {
    	String logMessage = "Test log message";
        // Write a log message
        logger.log(logMessage);

        // Read the log file and check if the message is present
        try {
        
        // get filename the log used
        String filename = logger.getFilename();
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line = "";
            String lastLine = "";
            while ( (line = reader.readLine()) != null)
            {
            	lastLine = line;
            	
            }
          
            reader.close();
            
            Assert.assertTrue(lastLine.contains(logMessage));

          
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

