package JunctionSim;

import java.util.HashMap;

public class Phases {
	
	// Hash map will have phase string as ID and a phase object which holds a queue of vehicles
	private HashMap<String, Phase> phasesHMap = new HashMap<String, Phase>();
	private String[] phaseHeaders;
	private Vehicles vehicles;
	
	public Phases(Vehicles vehicles) {
		//Init phases information from csv file - e.g. 8-phase set-up or 4-phase
		//Set phase headers for GUI
		//Reference to vehicles object - stores all vehicles
		//Populate the phases (the queues) from vehicles hash table
	}
	
	private void populateFromCSV() {
		
	}
	
	private void setCSVHeader() {
		
	}
	
	private void populatePhasesFromHashTable() {
		
	}
	
	
}
