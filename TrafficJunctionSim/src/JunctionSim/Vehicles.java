package JunctionSim;

import java.util.HashMap;

import java.util.ArrayList;

public class Vehicles {
	
	private HashMap<String, Vehicle> vehiclesHMap = new HashMap<String, Vehicle>();
	private String[] vehicleHeaders;
	
	public Vehicles(String vehiclesCSVFile){
		Object[] header_values = ReadCSV.getHeaderValues(vehiclesCSVFile);
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
			try {
				Vehicle vehicle = buildVehicle(vehicleParams);
				
				//Insert the vehicle into the hash map
				try {
					insertVehicleHashMap(vehicle);
				}
				catch(DuplicateVehicleIDException e) {
					System.out.println("Duplicate key in csv file: ignoring");
				}
			}
			catch(IllegalArgumentException ae) {
				System.out.println("Invalid vehicle params in csv file: ignoring");
			}
		}
	}
	
	public Vehicle buildVehicle(String vehicleParams[]) {
		//Decided to handle parsings from strings in vehicles constructor
		try {
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
		catch(IllegalArgumentException ea) {
			throw ea;
		}
	}
	

	public void insertVehicleHashMap(Vehicle v) throws DuplicateVehicleIDException{
		if(this.vehiclesHMap.containsKey(v.getVehicleID())) {
			throw new DuplicateVehicleIDException(v.getVehicleID());
		}
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
		//Sum the total crossing time of all vehicles waiting
		float crossingTimeSum = 0;
		//Get the average emission rate of all vehicles waiting
		float emissionRateSum = 0;
		int waitingCount = 0;
		//Multiple to get a final CO2 emission of all waiting vehicles
		for(Vehicle vehicle : vehiclesHMap.values()) {
			if(vehicle.getStatus().equals("Waiting")) {
				crossingTimeSum += vehicle.getCrossingTime();
				emissionRateSum += vehicle.getEmissionRate();
				waitingCount += 1;
			}
		}
		float averageEmissionRate = emissionRateSum / waitingCount;
		float waitingCO2 = averageEmissionRate * crossingTimeSum;
		//Convert to kg
		waitingCO2 = waitingCO2 / 1000;
		return String.format("%.2f", waitingCO2) + " KG";
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
	
	public int[] getVehiclesCrossedCounts() {
		int[] segmentCrossedCounts = new int[4];
		segmentCrossedCounts[0] = 0;
		segmentCrossedCounts[1] = 0;
		segmentCrossedCounts[2] = 0;
		segmentCrossedCounts[3] = 0;
		for(Vehicle vehicle : vehiclesHMap.values()) {
			if(vehicle.getSegment().equals("S1") && vehicle.getStatus().equals("Crossed")) {
				segmentCrossedCounts[0] += 1;
			}
			else if(vehicle.getSegment().equals("S2") && vehicle.getStatus().equals("Crossed")) {
				segmentCrossedCounts[1] += 1;
			}
			else if(vehicle.getSegment().equals("S3") && vehicle.getStatus().equals("Crossed")) {
				segmentCrossedCounts[2] += 1;
			}
			else if(vehicle.getSegment().equals("S4") && vehicle.getStatus().equals("Crossed")) {
				segmentCrossedCounts[3] += 1;
			}
		}
		return segmentCrossedCounts;
	}
	
	//Require knowledge of phases for phase durations
	public float waitingTimeOfVehicle(Vehicle vehicle, Phases phases) {
		//Get number of cycles that passed before this vehicle crossed
		int cyclesBeforeCross = vehicle.getCyclesBeforeCross();
		//Get how many vehicles passed before this one in its phase for that cycle
		int vehicleTurn = vehicle.getPhaseVehicleTurn();
		//Get cycle time
		float cycleTime = phases.getCycleTime();
		//Multiply cycle time by cycles that occurred before this vehicles cycle
		float cycleWaitingTime = cycleTime * cyclesBeforeCross;
		//For every vehicle that crossed in this vehicles crossing cycle in its segment
		//That crossed before this one
		//Sum these times
		float phaseWaitingTime = 0;
		for(Vehicle v : vehiclesHMap.values()) {
			if(v.getStatus().equals("Crossed") && v.getCyclesBeforeCross() == cyclesBeforeCross) {
				if(v.getPhaseVehicleTurn() < vehicleTurn) {
					phaseWaitingTime += v.getCrossingTime();
				}
			}
		}
		//Return sum of the cycle waiting time and phase waiting time
		return phaseWaitingTime + cycleWaitingTime;
	}
	//TODO: Sorting methods?
}
