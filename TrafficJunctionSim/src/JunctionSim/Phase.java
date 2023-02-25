package JunctionSim;

import java.util.LinkedList;
import java.util.Queue;

public class Phase {
	
	private float phaseDuration;
	private String phaseName;
	private Queue<String> vehicleKeysQueue = new LinkedList<String>();
	
	public Phase(String phaseName, float phaseDuration) {
		this.phaseName = phaseName;
		this.phaseDuration = phaseDuration;
	}
	
	//Queue methods
	public void addQueue(String key) {
		vehicleKeysQueue.offer(key);
	}
	
	public String popQueue() {
		return vehicleKeysQueue.poll();
	}
	
	//Getters
	public float getPhaseDuration() {
		return this.phaseDuration;
	}
	
	public String getPhaseName() {
		return this.phaseName;
	}
	
	public Queue<String> getVehicleKeysQueue(){
		return this.vehicleKeysQueue;
	}
}
