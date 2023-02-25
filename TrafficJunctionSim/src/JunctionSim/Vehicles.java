package JunctionSim;

import java.util.HashMap;

import JunctionSim.Vehicle;

public class Vehicles {
	
	private HashMap<String, Vehicle> vehiclesHMap = new HashMap<String, Vehicle>();
	private String[] vehicleHeaders;
	
	public Vehicles(){
		//Set vehicles header
		this.setCSVHeader();
		//Populate hash map of vehicles
		this.populateFromCSV();
	}
	
	private void setCSVHeader() {
		
	}
	
	private void populateFromCSV() {
		
	}
	
	private void buildVehicle(String vehicleParams[]) {
		//Decided to handle parsings from strings in vehicles constructor
		Vehicle vehicle = new Vehicle(
				vehicleParams[0], //vehicleID
				vehicleParams[1], //type
				vehicleParams[2], //crossingTime
				vehicleParams[3], //direction
				vehicleParams[4], //length
				vehicleParams[5], //emissionRate
				vehicleParams[6], //status
				vehicleParams[7] //segment
				);
	}
	
	// Insert a vehicle into the HMAP - and phase queue??
	private void insertVehicle(Vehicle v) {
		this.vehiclesHMap.putIfAbsent(v.getVehicleID(), v);
	}
	
	//Getter for vehicles hash map
	public HashMap<String, Vehicle> getVehiclesHashMap() {
		return this.vehiclesHMap;
	}
	
	//Getter for vehicles 2D array for GUI display
	//Take an optional sorting argument?
	public Object[][] getVehicles2DArray(){
		Object[][] vehiclesArray = new Object[this.vehiclesHMap.size()][8];
		int index = 0;
		for (Vehicle vehicle : vehiclesHMap.values()) {
			vehiclesArray[index][0] = vehicle.getVehicleID();
			vehiclesArray[index][1] = vehicle.getType();
			vehiclesArray[index][2] = vehicle.getCrossingTime();
			vehiclesArray[index][3] = vehicle.getDirection();
			vehiclesArray[index][4] = vehicle.getLength();
			vehiclesArray[index][5] = vehicle.getEmissionRate();
			vehiclesArray[index][6] = vehicle.getStatus();
			vehiclesArray[index][7] = vehicle.getSegment();
			index++;
		}
		return vehiclesArray;
	}
	
	//Getter for vehicles header for GUI display
	public String[] getVehicleHeaders() {
		return this.vehicleHeaders;
	}
	
	//TODO: Method for statistics from HMAP
	//TODO: Sorting methods?
}
