package JunctionSim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Phases {
	
	// Hash map will have phase string as ID and a phase object which holds a queue of vehicles
	private HashMap<String, Phase> phasesHMap = new HashMap<String, Phase>();
	private String[] phaseHeaders;
	private Vehicles vehicles;
	
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
	
	public void quickPhaseQueueCheck() {
		for (Phase phase : this.phasesHMap.values()) {
			System.out.println(phase.getVehicleKeysQueue());
		}
	}
}
