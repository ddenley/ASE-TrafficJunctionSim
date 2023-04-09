package controller;

import model.Vehicle;

//This will be a shared resource and will have two instances - for each direction
public class Intersection {
	
	
	public synchronized void enterIntersection(Vehicle vehicle) {
		System.out.println("Entered " + vehicle.getVehicleID());
		vehicle.setStatus("Crossing");
	}
	
	public synchronized void exitIntersection(Vehicle vehicle) {
		System.out.println("Crossed " + vehicle.getVehicleID());
		vehicle.setStatus("Crossed");
	}
	
}
