package JunctionSim;

public class DuplicateVehicleIDException extends Exception{
	
	public DuplicateVehicleIDException(String vehicleID) {
		super("Duplicate Vehicle ID: " + vehicleID);
	}
}
