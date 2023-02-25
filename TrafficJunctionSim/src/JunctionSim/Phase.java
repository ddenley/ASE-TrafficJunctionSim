package JunctionSim;

import java.util.LinkedList;
import java.util.Queue;

public class Phase {
	
	private float phaseDuration;
	private Queue<String> vehicleKeysQueue = new LinkedList<String>();
	
	public Phase(String phaseDuration) {
		float float_duration = Float.valueOf(phaseDuration);
		this.phaseDuration = float_duration;
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
	
	public Queue<String> getVehicleKeysQueue(){
		return this.vehicleKeysQueue;
	}
}
