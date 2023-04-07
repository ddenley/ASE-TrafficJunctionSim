package exceptions;

/**
 * @author Daniel Denley
 *
 */
public class DuplicateVehicleIDException extends Exception{
	
	public DuplicateVehicleIDException(String vehicleID) {
		super("Duplicate Vehicle ID: " + vehicleID);
	}
}
