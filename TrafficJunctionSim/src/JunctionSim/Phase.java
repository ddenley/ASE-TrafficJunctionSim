package JunctionSim;

import java.util.LinkedList;
import java.util.Queue;

public class Phase {
	
	private float phaseDuration;
	private Queue<String> vehicleKeysQueue = new LinkedList<String>();
	
	//This variable holds a counter of how many vehicles have been popped in this cycle
	//Needs reset each cycle
	private int vehiclesPopped;
	
	public Phase(String phaseDuration) {
		float float_duration = Float.valueOf(phaseDuration);
		this.phaseDuration = float_duration;
		this.vehiclesPopped = 0;
	}
	
	//Queue methods
	public void addQueue(String key) {
		vehicleKeysQueue.offer(key);
	}
	
	public String popQueue() {
		//TODO: When popping ensure that the vehicle object is updated both with the vehiclesPopped and cycle variable
		return vehicleKeysQueue.poll();
	}
	
	//Getters
	public float getPhaseDuration() {
		return this.phaseDuration;
	}
	
	public Queue<String> getVehicleKeysQueue(){
		return this.vehicleKeysQueue;
	}
}
