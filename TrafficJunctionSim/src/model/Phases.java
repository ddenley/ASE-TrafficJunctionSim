package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import exceptions.InvalidFileFormatException;
import utility.ReadCSV;
//Imported so I can add it as an observer
import view.GUIMain;

/**
 * @author Daniel Denley
 *
 */
public class Phases {
	//List of observers for this model
	private List <GUIMain> observers;
	
	
	// Hash map will have phase string as ID and a phase object which holds a queue of vehicles
	private HashMap<String, Phase> phasesHMap = new HashMap<String, Phase>();
	//Collection of vehicle monitor objects for each vehicle
	//private HashMap<String, Queue<Object>> phaseMonitorQueues = new HashMap<String, Queue<Object>>();
	private String[] phaseHeaders;
	
	//Holds a reference to the vehicles object for population of its queues
	//Also allows easier calculations for CO2 estimates which require phase duration and queue knowledge
	private Vehicles vehicles;
	
	//TODO: THIS VARIABLE IS TO BE USED IN STAGE 2
	//This variable holds a counter of how many cycles have occurred
	private int cyclesOccured;
	
	//Phases constructor takes reference to vehicles for queue population
	//Reference to init csv file for phase durations and names
	public Phases(Vehicles vehicles, String intersectionCSVFile) {
		observers = new ArrayList<>();
		this.vehicles = vehicles;
		// First read intersection.csv to create phasesHMap and set phaseHeaders
		Object[] header_values = ReadCSV.getHeaderValues(intersectionCSVFile);
		String[] header = (String[]) header_values[0];
		ArrayList<String[]> values = (ArrayList<String[]>) header_values[1];
		setCSVHeader(header);
		populateFromCSV(values);
		//Now populate each phase queue from the vehicles hash map for initial init
		populatePhaseQueuesFromHashTable();
	}
	
	public HashMap<String, Phase> getPhasesHmap() {
		return this.phasesHMap;
	}
	
//	public HashMap<String, Queue<Object>> getVehicleMonitorsQueue(){
//		return this.phaseMonitorQueues;
//	}
	
	//Method can throw an unhandled InvalidFileFormatException to ensure we do not enter a weird state
	//Invalid files include if phase number is invalid
	//Invalid phase paramaters throw from phase constructor and exit gracefully here
	private void populateFromCSV(ArrayList<String[]> values) {
		for (String[] phaseParams : values) {
			try {
				//phaseParams[0] will be phase string ID
				String phase_name = phaseParams[0];
				//phaseParamas[1] will be phase duration
				String phase_duration = phaseParams[1];
				//Create phase object using phase duration - float conversion occurs within constructor
				try {
					Phase phase = new Phase(phase_duration);
					//Add phase to hash map
					this.phasesHMap.putIfAbsent(phase_name, phase);
					//Add monitors
					//phaseMonitorQueues.put(phase_name, new LinkedList<>());
				}
				catch (IllegalArgumentException ae) {
					System.out.println(ae.getMessage());
					System.exit(0);
				}
			}
			catch (ArrayIndexOutOfBoundsException a){
				throw new InvalidFileFormatException("INTERSECTION CSV FILE");
			}
		}
	}
	
	//Sets instane header value used for GUI
	private void setCSVHeader(String[] header) {
		this.phaseHeaders = header;
	}
	
	//Method uses vehicles refrence to populate each vehicle to relevant queue
	//Relies on vehiclesPhaseAllocation method for inserting into correct queue
	private void populatePhaseQueuesFromHashTable() {
		for (Map.Entry<String, Vehicle> entry : this.vehicles.getVehiclesHashMap().entrySet())
        {
			
			//Populates each queue accordingly
            String key = entry.getKey();
            Vehicle vehicle = entry.getValue();
            
            vehicle.providePhases(this);
            
            String vehiclePhaseAllocation = vehicle.getEightPhaseAllocation();
            
            //Get correct phase object and place vehicle ID into queue
            Phase phase = this.phasesHMap.get(vehiclePhaseAllocation);
            //Add reference to vehicle infront
            String vehicleInfrontID = phase.getVehicleKeysQueue().peekLast();
            vehicle.setVehicleInfront(vehicles.getVehiclesHashMap().get(vehicleInfrontID));
            phase.addQueue(key);
            Object vehicleMonitor = vehicle.getVehicleMonitor();
            //phaseMonitorQueues.get(vehiclePhaseAllocation).add(vehicleMonitor);
        }
	}
	
	//Method takes a vehicle object and inserts into correct queue
	//Called when adding vehicles from GUI
	public synchronized void insertVehicleQueue(Vehicle v) {
		String phase_key = v.getEightPhaseAllocation();
		String vehicle_key = v.getVehicleID();
		Phase phase = this.phasesHMap.get(phase_key);
		
		//Get the vehicle infront of this one to hold as reference
		String vehicleInfrontID = phase.getVehicleKeysQueue().peekLast();
		Vehicle vehicleInfront = vehicles.getVehiclesHashMap().get(vehicleInfrontID);
		v.setVehicleInfront(vehicleInfront);
		
		phase.addQueue(vehicle_key);
		v.providePhases(this);
		
		notifyObservers();
	}
	
	public String[] getPhaseHeaders() {
		return this.phaseHeaders;
	}
	
	//Method required as JTables will only accept 2D object arrays not hash maps
	//Method used for GUI population of phases table
	public Object[][] getPhases2DObjectArray(){
		Object[][] phaseArray = new Object[phasesHMap.size()][2];
		int index = 0;
		for (Map.Entry<String, Phase> entry : phasesHMap.entrySet()) {
		    String key = entry.getKey();
		    Phase phase = entry.getValue();
		    phaseArray[index][0] = key;
		    phaseArray[index][1] = phase.getPhaseDuration();
		    index++;
		}
		return phaseArray;
	}
	
	//Method used for GUI population of vehicles-phase allocation table
	public Object[][] getPhasesVehicles2DObjectArray(){
		Object[][] phaseVehiclesArray = new Object[vehicles.getVehiclesHashMap().size()][2];
		int index = 0;
		//Iterate over phases HMAP
		for (Map.Entry<String, Phase> entry : phasesHMap.entrySet()) {
			//Iterate over the vehicle keys within each phase queue
			Phase phase = entry.getValue();
			String phaseName = entry.getKey();
			for(String vehicleID : phase.getVehicleKeysQueue()) {
				phaseVehiclesArray[index][0] = phaseName;
				//System.out.println(phaseName);
				phaseVehiclesArray[index][1] = vehicleID;
				//System.out.println(vehicleID);
				index++;
			}
		}
		return phaseVehiclesArray;
	}
	
	//CAN IGNORE DEBUG METHOD TO HELP VISUALISE QUEUE ALLOCATION
	//This method was just to visualise how vehicles were populated into the queues - leaving for future debugging
	private void quickPhaseQueueCheck() {
		for (Phase phase : this.phasesHMap.values()) {
			System.out.println(phase.getVehicleKeysQueue());
		}
	}
	
	private float[] getPhaseDurations() {
		float[] phaseDurations = new float[phasesHMap.size()];
		int i = 0;
		for (Phase phase : this.phasesHMap.values()) {
			phaseDurations[i] = phase.getPhaseDuration();
			i++;
		}
		return phaseDurations;
	}
	
	//Method sums each phase duration to produce a cycle time/duration
	public float getCycleTime() {
		//Get the cumulative time of a cycle
		float cycleTime = 0;
		for(float phaseTime: getPhaseDurations()) {
			cycleTime += phaseTime;
		}
		return cycleTime;
	}
	
	//Waiting times due to vehicles
	//Method simply produces an average segment waiting time for a vehicle
	//Disregards phase wait time
	public float[] getAverageSegmentWaitingTimes() {
		float[] segmentWaitTimes = new float[4];
		int[] vehiclesCrossedCounts = this.vehicles.getVehiclesCrossedCounts();
		segmentWaitTimes[0] = 0;
		segmentWaitTimes[1] = 0;
		segmentWaitTimes[2] = 0;
		segmentWaitTimes[3] = 0;
		for(Vehicle vehicle: this.vehicles.getVehiclesHashMap().values()) {
			if(vehicle.getStatus().equals("Crossed")) {
				if(vehicle.getSegment().equals("S1")) {
					segmentWaitTimes[0] += this.vehicles.waitingTimeOfVehicle(vehicle, this);
				}
				if(vehicle.getSegment().equals("S2")) {
					segmentWaitTimes[1] += this.vehicles.waitingTimeOfVehicle(vehicle, this);
				}
				if(vehicle.getSegment().equals("S3")) {
					segmentWaitTimes[2] += this.vehicles.waitingTimeOfVehicle(vehicle, this);
				}
				if(vehicle.getSegment().equals("S4")) {
					segmentWaitTimes[3] += this.vehicles.waitingTimeOfVehicle(vehicle, this);
				}
			}
		}
		segmentWaitTimes[0] = segmentWaitTimes[0] / vehiclesCrossedCounts[0];
		segmentWaitTimes[1] = segmentWaitTimes[1] / vehiclesCrossedCounts[1];
		segmentWaitTimes[2] = segmentWaitTimes[2] / vehiclesCrossedCounts[2];
		segmentWaitTimes[3] = segmentWaitTimes[3] / vehiclesCrossedCounts[3];
		return segmentWaitTimes;
	}
	
	//Method calculates the phase that will be the last AND
	//Method calculates how many times the last phase will run
	//Returns phase it will end on(index), and how many times this phase will run
	private int[] phaseRunCounts() {
		float remainderOfLastPhase = 0;
		//First I want to find how many cycles each phase requires
		int[] phaseCycles = new int[8];
		float[] phaseDurations = new float[8];
		//Populate phase durations into an array
		int phaseIndex = 0;
		for(Phase p : phasesHMap.values()) {
			phaseDurations[phaseIndex] = p.getPhaseDuration();
			phaseIndex +=1;
		}
		//Next I will create an array of total crossing times per phase	
		float[] phaseCrossingsSum = new float[8];
		phaseIndex = 0;
		for(Phase p :phasesHMap.values()) {
			//Grab queue within phase - iterate over vehicle keys
			float crossingSum = 0;
			for(String vehicleKey: p.getVehicleKeysQueue()) {
				//Retrieve vehicle
				Vehicle vehicle = vehicles.getVehiclesHashMap().get(vehicleKey);
				//Add crossing time
				crossingSum += vehicle.getCrossingTime();
			}
			phaseCrossingsSum[phaseIndex] = crossingSum;
			phaseIndex +=1;
		}
		//Now I want to divide and cast to an int to remove decimals - this gives phase cycles
		phaseIndex = 0;
		while (phaseIndex <= 7) {
			phaseCycles[phaseIndex] = 0;
			if (phaseCrossingsSum[phaseIndex] != 0) {
				phaseCycles[phaseIndex] += (int)Math.ceil(phaseCrossingsSum[phaseIndex] / phaseDurations[phaseIndex]);
				remainderOfLastPhase = phaseCrossingsSum[phaseIndex] % phaseDurations[phaseIndex];
			}
			phaseIndex +=1;
		}
		//Identify phase it will end on and how many times it will run
		phaseIndex = 0;
		int indexOfPhaseEndedOn = 0;
		int numberOfTimesPhaseRan = 0;
		int prevHighestRuns = 0;
		while (phaseIndex <= 7) {
			if(phaseCycles[phaseIndex] > prevHighestRuns) {
				indexOfPhaseEndedOn = phaseIndex;
				numberOfTimesPhaseRan = phaseCycles[phaseIndex];
				prevHighestRuns = phaseCycles[phaseIndex];
			}
			phaseIndex +=1;
		}
		//Handle cases were only once cycle occurs
		//Return phase index with highest sum of crossing times
		if(numberOfTimesPhaseRan == 1) {
			phaseIndex = 0;
			indexOfPhaseEndedOn = 0;
			float prevHighestSum = 0;
			while (phaseIndex <= 7) {
				if(phaseCrossingsSum[phaseIndex] > prevHighestSum) {
					indexOfPhaseEndedOn = phaseIndex;
					prevHighestSum = phaseCrossingsSum[phaseIndex];
				}
				phaseIndex +=1;
			}
		}
		int[] returnValues = {indexOfPhaseEndedOn,numberOfTimesPhaseRan};
		return returnValues;
	}
	
	//Sum of waiting times due to cycles occured and phases before it
	//Returns a float array of these phase waiting times - 
	//Does not regard the final waiting time of the last crossing vehicles
	//But produces a good estimate of the simulation duration for total CO2 calculation
	//Without having to run a mock simulation
	private float[] getPhaseWaitingTimeTotals() {
		float[] phaseWaitSums = {0,0,0,0,0,0,0,0};
		float[] phaseDurations = getPhaseDurations();
		//Get phase ended on
		int phaseEndedOn = phaseRunCounts()[0];
		int phaseIndex = 1;
		float waitSum = 0;
		while(phaseIndex <= phaseEndedOn) {
			phaseWaitSums[phaseIndex] = phaseDurations[phaseIndex] + waitSum;
			waitSum += phaseDurations[phaseIndex];
			phaseIndex+=1;
		}
		//Now add the cycle times
		int cyclesOccured = phaseRunCounts()[1] - 1;
		phaseIndex = 0;
		while(phaseIndex <= 7) {
			phaseWaitSums[phaseIndex] += (cyclesOccured * getCycleTime());
			//System.out.println(phaseWaitSums[phaseIndex]);
			phaseIndex+=1;
		}
		return phaseWaitSums;
	}
	
	//Returns an estimate of the total CO2 to be produced
	//Average wait time of vehicle / 60 * average emission rate * number of vehicles
	//Divide by 1000 for kg
	public float getTotalCO2Estimate() {
		//Average wait time of vehicle
		float closeEstimateOfSimulationDuration = 0;
		for(float duration : getPhaseWaitingTimeTotals()) {
			if(duration > closeEstimateOfSimulationDuration) {
				closeEstimateOfSimulationDuration = duration;
			}
		}
		float avgVehicleWaitTime = closeEstimateOfSimulationDuration * vehicles.getVehicleCount();
		float avgEmissionRate = vehicles.getEmissionRateSum() / vehicles.getVehicleCount();
		float CO2Estimate = ((avgVehicleWaitTime / 60) * avgEmissionRate * vehicles.getVehicleCount()) / 1000;
		//System.out.println(CO2Estimate);
		return CO2Estimate;
	}
	
	//Methods for subject/observer pattern
	public void addObserver(GUIMain observer) {
		observers.add(observer);
	}
	public void removerObserver(GUIMain observer) {
		observers.remove(observer);
	}
	public void notifyObservers() {
		for(GUIMain observer : observers) {
			observer.modelUpdated();
		}
	}
	
	//Notify all vehicles in the phase they are active
	public void notifyVehicles(String phaseID) {
		//Get vehicle ID queue associated with phaseID
		Queue<String> phaseQueue = phasesHMap.get(phaseID).getVehicleKeysQueue();
		//Get the vehicles hash map
		HashMap<String, Vehicle> vehiclesHashMap = vehicles.getVehiclesHashMap();
		//Notify each vehicle
		for(String vID : phaseQueue) {
			//Get vehicle object
			Vehicle vehicle = vehiclesHashMap.get(vID);
			//Notify vehicle
			synchronized (vehicle.getVehicleMonitor()) {
		        vehicle.toggleActive();
		    }
		}
	}
	
	public synchronized boolean isVehicleNext(Vehicle vehicle) {
		String phaseName = vehicle.getEightPhaseAllocation();
		Phase phase = phasesHMap.get(phaseName);
		if (phase.isEmpty()) {
			return false;
		}
		return phase.peekQueue().equals(vehicle.getVehicleID());
	}
	
	public synchronized void removeVehicle(Vehicle vehicle) {
        String phaseName = vehicle.getEightPhaseAllocation();
        Phase phase = phasesHMap.get(phaseName);
        
        if (phase.peekQueue().equals(vehicle.getVehicleID())) {
            phase.popQueue();
        }
    }
	
	public boolean checkLastInQueue(Vehicle vehicle) {
		String phaseName = vehicle.getEightPhaseAllocation();
		Deque<String> vehicleQueue = phasesHMap.get(phaseName).getVehicleKeysQueue();
		if(vehicleQueue.isEmpty()) {
			return false;
		}
		return(vehicleQueue.getLast().equals(vehicle.getVehicleID()));
	}
}
