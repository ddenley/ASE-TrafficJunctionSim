package Test;

import utility.Logger;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
	public void testLogConcurrency() throws InterruptedException {
		int numberOfThreads = 2;
		ExecutorService service = Executors.newFixedThreadPool(10);
		CountDownLatch latch = new CountDownLatch(numberOfThreads);
		String logMessage = "Test log message";
		for (int i = 0; i < numberOfThreads; i++) {
			service.submit(() -> {
				try {
					// Write a log message
					logger.log(logMessage);
				} catch (Exception e) {
					e.printStackTrace();
				}
				latch.countDown();
			});
		}
		latch.await();
		// Read the log file and check if the message is present
		try {

			// get filename the log used
			String filename = logger.getFilename();
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line = "";
			String lastLine = "";
			while ((line = reader.readLine()) != null) {
				lastLine = line;

			}

			reader.close();

			Assert.assertTrue(lastLine.contains(logMessage));

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
