package controller;

import model.Vehicle;

//This will be a shared resource and will have two instances - for each direction
public class Intersection {
	private String acceptingPhase;
	private boolean isEmpty;
	
	private void getVehicle() {
		
	}
	
	
	private synchronized void intersectionProccesor(Vehicle vehicle) {
		isEmpty = false;
		vehicle.setStatus("Crossing");
		float crossingTime = vehicle.getCrossingTime();
		long crossingTimeMilli = (long) (crossingTime * 1000);
		try {
			Thread.sleep(crossingTimeMilli);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		vehicle.setStatus("Crossed");
	}
	
}
