package JunctionSim;

public class InvalidFileFormatException extends RuntimeException{
	
	public InvalidFileFormatException(String fileName) {
		System.out.println("Invalid file format: " + fileName);
		System.exit(0);
	}
}
