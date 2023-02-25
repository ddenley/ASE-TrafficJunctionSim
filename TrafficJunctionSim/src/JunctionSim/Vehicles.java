package JunctionSim;

import java.util.HashMap;

import java.util.ArrayList;

public class Vehicles {
	
	private HashMap<String, Vehicle> vehiclesHMap = new HashMap<String, Vehicle>();
	private String[] vehicleHeaders;
	
	public Vehicles(){
		Object[] header_values = ReadCSV.getHeaderValues("vehicles.csv");
		String[] header = (String[]) header_values[0];
		ArrayList<String[]> values = (ArrayList<String[]>) header_values[1];
		setCSVHeader(header);
		populateFromCSV(values);
	}
	
	private void setCSVHeader(String[] header) {
		this.vehicleHeaders = header;
	}
	
	private void populateFromCSV(ArrayList<String[]> values) {
		for (String[] vehicleParams : values) {
			//Build a vehicle object using the file parameters
			Vehicle vehicle = buildVehicle(vehicleParams);
			//Insert the vehicle into the hash map
			insertVehicleHashMap(vehicle);
		}
	}
	
	private Vehicle buildVehicle(String vehicleParams[]) {
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
		return vehicle;
	}
	

	private void insertVehicleHashMap(Vehicle v) {
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
