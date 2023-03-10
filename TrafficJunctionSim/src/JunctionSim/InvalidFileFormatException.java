package JunctionSim;

/**
 * @author Daniel Denley
 *
 */
public class InvalidFileFormatException extends RuntimeException{
	
	public InvalidFileFormatException(String fileName) {
		System.out.println("Invalid file format: " + fileName);
		//A conscious decision here was made to allow the JVM to handle the exception
		//While failing gracefully is nice its harder to test and end result is same
		//System.exit(0);
	}
}
