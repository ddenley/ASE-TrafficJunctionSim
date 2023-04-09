package model;

import java.util.HashMap;
import java.util.List;

import controller.Intersection;
import exceptions.DuplicateVehicleIDException;
import utility.ReadCSV;
import view.GUIMain;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Daniel Denley
 *
 */
public class Vehicles {
	//List of observers for this model
	private List <GUIMain> observers;
	
	//Vehicles object holds a hashmap of all vehicles - efficiently accessed via their key
	//Hash map implementation as during simulation popping of vehicle keys from phase queues allows efficient updating
	private HashMap<String, Vehicle> vehiclesHMap = new HashMap<String, Vehicle>();
	//Header variable used in GUI
	private String[] vehicleHeaders;
	private Intersection intersection;
	
	//Constructor reads from csv file
	//Sets header variable for GUI from csv
	//Populates hash map with values from csv
	public Vehicles(String vehiclesCSVFile, Intersection intersection){
		this.intersection = intersection;
		observers = new ArrayList<>();
		Object[] header_values = ReadCSV.getHeaderValues(vehiclesCSVFile);
		String[] header = (String[]) header_values[0];
		ArrayList<String[]> values = (ArrayList<String[]>) header_values[1];
		setCSVHeader(header);
		populateFromCSV(values);
	}
	
	private void setCSVHeader(String[] header) {
		this.vehicleHeaders = header;
	}
	
	//Method which accepts an array list of values produced from csv file
	//Places them into hashmap using vehicle ID as key, vehicle object as value
	//Duplicate key exception handled here as if from CSV should be caught and ignored
	//GUI also handles via producing an error message
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
	
	//Method for creating a vehicle object
	//Improves code reusability and readability
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
					vehicleParams[7], //segment
					intersection
					);
			return vehicle;
		}
		catch(IllegalArgumentException ea) {
			throw ea;
		}
	}
	
	//Method inserts vehicle key, vehicle pair into hashmap
	//Throws duplicate vehicle ID exception
	public synchronized void insertVehicleHashMap(Vehicle v) throws DuplicateVehicleIDException{
		if(this.vehiclesHMap.containsKey(v.getVehicleID())) {
			throw new DuplicateVehicleIDException(v.getVehicleID());
		}
		this.vehiclesHMap.putIfAbsent(v.getVehicleID(), v);
		notifyObservers();
	}
	
	//Getter for vehicles hash map
	public HashMap<String, Vehicle> getVehiclesHashMap() {
		return this.vehiclesHMap;
	}
	
	//Getter for vehicles 2D array for GUI display
	//Take an optional sorting argument? - Stage 2
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
	//Get emission rate for all vehicles waiting and convert to kg per minute
	public synchronized String getTotalCO2PerMinute() {
		float emissionRateSum = 0;
		for(Vehicle vehicle : vehiclesHMap.values()) {
			if(vehicle.getStatus().equals("Waiting")) {
				emissionRateSum += vehicle.getEmissionRate();
			}
		}
		//Convert to kg
		emissionRateSum = emissionRateSum / 1000;
		//Return amount of C02 produced
		return String.format("%.2f", emissionRateSum) + " KG per minute";
	}
	
	//Method builds a 2D object array for the statistics JTable in the GUI
	//Uses class methods to produce needed values
	public synchronized Object[][] getSegmentStatistics() {
		Object[][] segmentStatsArray = new Object[4][5];
		//Set segment names for segmentStatsArray
		segmentStatsArray[0][0] = "S1";
		segmentStatsArray[1][0] = "S2";
		segmentStatsArray[2][0] = "S3";
		segmentStatsArray[3][0] = "S4";
		//Init statistic values
		int i = 0;
		while(i <= 3) {
			//segmentStatsArray[i][1] = 0f;
			segmentStatsArray[i][1] = 0f;
			segmentStatsArray[i][2] = 0f;
			segmentStatsArray[i][3] = 0;
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
			//TODO: Update total waiting time - reintroduce in stage 2 - NEEDS TO ACCOUNT FOR CYCLE TIMES
			//segmentStatsArray[index][1] = ((float)segmentStatsArray[index][1]) + vehicle.getCrossingTime();
			//Update total length
			segmentStatsArray[index][1] = ((float)segmentStatsArray[index][1]) + vehicle.getLength();
			//Update total crossing time - average after
			segmentStatsArray[index][2] = ((float)segmentStatsArray[index][2]) + vehicle.getCrossingTime();
			//Update number of vehicles
			segmentStatsArray[index][3] = ((int)segmentStatsArray[index][3]) + 1;
		}
		//Now average the cross times
		i = 0;
		while(i <= 3) {
			segmentStatsArray[i][2] = ((float)segmentStatsArray[i][2]) / ((int)segmentStatsArray[i][3]);
			i++;
		}
		return segmentStatsArray;
	}
	
	//Method returns the amount of vehicles crossed for each segment
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
	
	//TODO: THIS METHOD IS FOR STAGE 2 FURTHER IMPLEMENTATION PLEASE IGNORE
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
	
	//Simple getter for vehicle count value
	//Useful to phases class also
	public int getVehicleCount() {
		return vehiclesHMap.size();
	}
	
	//Simple getter for emission rate sum
	//Usedul to phases class in producing total CO2 estimate
	public float getEmissionRateSum() {
		float emissionRateSum = 0;
		for(Vehicle vehicle : vehiclesHMap.values()) {
			emissionRateSum += vehicle.getEmissionRate();
		}
		return emissionRateSum;
	}
	
	//Methods for subject/observer pattern
	public void addObserver(GUIMain observer) {
		observers.add(observer);
	}
	public void removerObserver(GUIMain observer) {
		observers.remove(observer);
	}
	private void notifyObservers() {
		for(GUIMain observer : observers) {
			observer.modelUpdated();
		}
	}
	
	//TODO: Emission sum should probably include waiting/crossing also?
	public synchronized String[][] phaseStatistics() {
		List<String> phases = Arrays.asList("P1", "P2", "P3", "P4", "P5", "P6", "P7", "P8");
		int[] crossedCounts = new int[] {0,0,0,0,0,0,0,0};
		float[] emissionSum = new float[] {0,0,0,0,0,0,0,0};
		float[] waitingSum = new float[] {0,0,0,0,0,0,0,0};
		for (Vehicle vehicle : vehiclesHMap.values()) {
			if(vehicle.getStatus().equals("Crossed")) {
				int index = phases.indexOf(vehicle.getEightPhaseAllocation());
				//Increment crossed count
				crossedCounts[index] += 1; 
				//Increment emission count
				emissionSum[index] += vehicle.getEmissionRate();
				//Increment time waited
				waitingSum[index] += vehicle.getWaitingTime();
			}
		}
		String[][] phaseStats = new String[phases.size()][3];
		int i = 0;
		while (i < phases.size()) {
			phaseStats[i][0] = String.valueOf(crossedCounts[i]);
			phaseStats[i][1] = String.valueOf(emissionSum[i]);
			phaseStats[i][2] = String.valueOf(waitingSum[i]);
			i++;
		}
		return phaseStats;
	}
}
