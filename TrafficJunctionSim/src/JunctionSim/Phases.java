package JunctionSim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Phases {
	
	// Hash map will have phase string as ID and a phase object which holds a queue of vehicles
	private HashMap<String, Phase> phasesHMap = new HashMap<String, Phase>();
	private String[] phaseHeaders;
	private Vehicles vehicles;
	
	//This variable holds a counter of how many cycles have occurred
	private int cyclesOccured;
	
	public Phases(Vehicles vehicles) {
		this.vehicles = vehicles;
		//ReadCSV.getHeaderValues("vehicles.csv");
		// First read intersection.csv to create phasesHMap and set phaseHeaders
		Object[] header_values = ReadCSV.getHeaderValues("intersection.csv");
		String[] header = (String[]) header_values[0];
		ArrayList<String[]> values = (ArrayList<String[]>) header_values[1];
		setCSVHeader(header);
		populateFromCSV(values);
		//Now populate each phase queue from the vehicles hash map for initial init
		populatePhaseQueuesFromHashTable();
		
		quickPhaseQueueCheck();
	}
	
	private void populateFromCSV(ArrayList<String[]> values) {
		for (String[] phaseParams : values) {
			//phaseParams[0] will be phase string ID
			String phase_name = phaseParams[0];
			//phaseParamas[1] will be phase duration
			String phase_duration = phaseParams[1];
			//Create phase object using phase duration - float conversion occurs within constructor
			Phase phase = new Phase(phase_duration);
			//Add phase to hash map
			this.phasesHMap.putIfAbsent(phase_name, phase);
		}
	}
	
	private void setCSVHeader(String[] header) {
		this.phaseHeaders = header;
	}
	
	//https://stackoverflow.com/questions/5826384/java-iteration-through-a-hashmap-which-is-more-efficient
	private void populatePhaseQueuesFromHashTable() {
		for (Map.Entry<String, Vehicle> entry : this.vehicles.getVehiclesHashMap().entrySet())
        {
            String key = entry.getKey();
            Vehicle vehicle = entry.getValue();
            
            String vehiclePhaseAllocation = vehicle.getEightPhaseAllocation();
            
            //Get correct phase object and place vehicle ID into queue
            Phase phase = this.phasesHMap.get(vehiclePhaseAllocation);
            phase.addQueue(key);
        }
	}
	
	public void insertVehicleQueue(Vehicle v) {
		String phase_key = v.getEightPhaseAllocation();
		String vehicle_key = v.getVehicleID();
		Phase phase = this.phasesHMap.get(phase_key);
		phase.addQueue(vehicle_key);
	}
	
	public String[] getPhaseHeaders() {
		return this.phaseHeaders;
	}
	
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
				phaseVehiclesArray[index][1] = vehicleID;
				index++;
			}
		}
		return phaseVehiclesArray;
	}
	
	//This method was just to visualise how vehicles were populated into the queues - leaving for future debugging
	public void quickPhaseQueueCheck() {
		for (Phase phase : this.phasesHMap.values()) {
			//System.out.println(phase.getVehicleKeysQueue());
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
	
	public float getCycleTime() {
		//Get the cumulative time of a cycle
		float cycleTime = 0;
		for(float phaseTime: getPhaseDurations()) {
			cycleTime += phaseTime;
		}
		return cycleTime;
	}
	
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
}
