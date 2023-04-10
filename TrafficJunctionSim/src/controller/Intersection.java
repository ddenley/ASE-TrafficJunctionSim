package controller;

import model.Vehicle;
import utility.Logger;

//This will be a shared resource and will have two instances - for each direction
public class Intersection {
	
	
	public synchronized void enterIntersection(Vehicle vehicle) {
		Logger.getInstance().log(vehicle.getVehicleID() + " entered intersecion");
		vehicle.setStatus("Crossing");
		vehicle.setVehicleMoved(true);
		vehicle.setWaitingTime();
	}
	
	public synchronized void exitIntersection(Vehicle vehicle) {
		Logger.getInstance().log(vehicle.getVehicleID() + " exited intersecion");
		vehicle.setStatus("Crossed");
		vehicle.setVehicleMoved(false);
	}
	
}
