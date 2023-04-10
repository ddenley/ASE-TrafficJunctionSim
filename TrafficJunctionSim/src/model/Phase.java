package model;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Daniel Denley
 *
 */
public class Phase {
	
	//Phase object composed of a vehicle key queue and phase duration
	private float phaseDuration;
	//Updated to double ended queue as vehicle threads when active need to check if they are last in their queue
	private Deque<String> vehicleKeysQueue = new LinkedList<String>();
	
	//Constructor handles bad paramater input from files
	//Illegal arguments here are left to be handled by the JVM - we do not want to proceed
	public Phase(String phaseDuration) {
		if (phaseDuration == null || phaseDuration == "") {
			throw new IllegalArgumentException("Phase duration null");
		}
		try {
			float float_duration = Float.valueOf(phaseDuration);
		}
		catch(NumberFormatException e){
			throw new IllegalArgumentException("Phase duration not numeric");
		}
		float float_duration = Float.valueOf(phaseDuration);
		if (float_duration <= 0 || float_duration >= 300) {
			throw new IllegalArgumentException("Phase duration out of allowed bounds" + phaseDuration);
		}
		this.phaseDuration = float_duration;
	}
	
	//Queue methods
	public void addQueue(String key) {
		vehicleKeysQueue.offer(key);
	}
	
	public String popQueue() {
		//TODO: When popping ensure that the vehicle object is updated both with the vehiclesPopped and cycle variable
		return vehicleKeysQueue.poll();
	}
	
	public String peekQueue() {
		return vehicleKeysQueue.peek();
	}
	
	public boolean isEmpty() {
		return vehicleKeysQueue.isEmpty();
	}
	
	//Getters
	public float getPhaseDuration() {
		return this.phaseDuration;
	}
	
	public Deque<String> getVehicleKeysQueue(){
		return this.vehicleKeysQueue;
	}
}
