package controller;

import model.Vehicle;
import utility.Logger;

//Shared resource - one vehicle per active phase can enter at any time
//Active phases are set within traffic controller
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
