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
	
	public Vehicle buildVehicle(String vehicleParams[]) {
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
	

	public void insertVehicleHashMap(Vehicle v) {
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
	
	//Returns the total CO2 of all vehicles in a waiting state as a string
	public String getTotalCO2() {
		float co2 = 0;
		for (Vehicle v : vehiclesHMap.values()) {
			if(v.getStatus().equals("Waiting")) {
				//Grams of C02 per minute
				co2 += v.getEmissionRate();
			}
		}
		//Convert to kg
		float co2_kg = co2 / 1000;
		//Covert to string for GUI
		String co2_string = String.format("%d",(long)co2) + " Grams";
		return co2_string;
	}
	
	
	public Object[][] getSegmentStatistics() {
		Object[][] segmentStatsArray = new Object[4][5];
		//Set segment names for segmentStatsArray
		segmentStatsArray[0][0] = "S1";
		segmentStatsArray[1][0] = "S2";
		segmentStatsArray[2][0] = "S3";
		segmentStatsArray[3][0] = "S4";
		//Init statistic values
		int i = 0;
		while(i <= 3) {
			segmentStatsArray[i][1] = 0f;
			segmentStatsArray[i][2] = 0f;
			segmentStatsArray[i][3] = 0f;
			segmentStatsArray[i][4] = 0;
			i++;
		}
		int index = -1;
		for (Vehicle vehicle : vehiclesHMap.values()) {
			//Calculate index to update values within
			if(vehicle.getSegment().equals("S1")) {
				index = 0;
			}
			else if(vehicle.getSegment().equals("S2")) {
				index = 1;
			}
			else if(vehicle.getSegment().equals("S3")) {
				index = 2;
			}
			else if(vehicle.getSegment().equals("S4")) {
				index = 3;
			}
			//Update total waiting time
			segmentStatsArray[index][1] = ((float)segmentStatsArray[index][1]) + vehicle.getCrossingTime();
			//Update total length
			segmentStatsArray[index][2] = ((float)segmentStatsArray[index][2]) + vehicle.getLength();
			//Update total crossing time - average after
			segmentStatsArray[index][3] = ((float)segmentStatsArray[index][3]) + vehicle.getCrossingTime();
			//Update number of vehicles
			segmentStatsArray[index][4] = ((int)segmentStatsArray[index][4]) + 1;
		}
		//Now average the cross times
		i = 0;
		while(i <= 3) {
			segmentStatsArray[i][3] = ((float)segmentStatsArray[i][3]) / ((int)segmentStatsArray[i][4]);
			i++;
		}
		return segmentStatsArray;
	}
	//TODO: Sorting methods?
}
