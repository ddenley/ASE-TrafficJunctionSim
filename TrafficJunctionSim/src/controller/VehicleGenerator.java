package controller;

import java.util.Random;

import exceptions.DuplicateVehicleIDException;
import model.Phases;
import model.Vehicle;
import model.Vehicles;

public class VehicleGenerator implements Runnable{

	private Vehicles vehicles;
	private Phases phases;
	private Random random = new Random();
	
	
	public VehicleGenerator(Vehicles vehicles, Phases phases) {
		this.vehicles = vehicles;
		this.phases = phases;
		this.random = new Random();
	}
	
	private String vehicleID() {
		String acceptableChars = "ZXCVBNMASDFGHJKLQWERTYUIOP1234567890";
		char[] vehicleIDChars = new char[6];
		int i = 0;
		while(i <= 5) {
			//Produce random number between 0 and length of acceptable chars
			int randIndex = random.nextInt(acceptableChars.length());
			vehicleIDChars[i] = acceptableChars.charAt(randIndex);
			i++;
		}
		return new String(vehicleIDChars);
	}
	
	private String type() {
		String[] allowedTypes = {"Bus", "Car", "Van"};
		int randIndex = random.nextInt(allowedTypes.length);
		return allowedTypes[randIndex];
	}
	
	private String crossingTime() {
		float max = 30;
		float randFloat = random.nextFloat() * max;
		return String.format("%.02f", randFloat);
	}
	
	private String direction() {
		String[] allowedDirections = {"Left", "Right", "Straight"};
		int randIndex = random.nextInt(allowedDirections.length);
		return allowedDirections[randIndex];
	}
	
	private String length() {
		float max = 30;
		float randFloat = random.nextFloat() * max;
		return String.format("%.02f", randFloat);
	}
	
	private String emissionRate() {
		float max = 30;
		float randFloat = random.nextFloat() * max;
		return String.format("%.02f", randFloat);
	}
	
	private String status() {
		return "Waiting";
	}
	
	private String segments() {
		String[] allowedSegments = {"S1", "S2", "S3", "S4"};
		int randIndex = random.nextInt(allowedSegments.length);
		return allowedSegments[randIndex];
	}
	
	private Vehicle createVehicle() {
		String[] params = new String[8];
		params[0] = vehicleID();
		params[1] = type();
		params[2] = crossingTime();
		params[3] = direction();
		params[4] = length();
		params[5] = emissionRate();
		params[6] = status();
		params[7] = segments();
		return vehicles.buildVehicle(params);
	}
	
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			while(!Thread.currentThread().isInterrupted()) {
				//issue
				Vehicle vehicle = createVehicle();
				try {
					vehicles.insertVehicleHashMap(vehicle);
					phases.insertVehicleQueue(vehicle);
					Thread vehicle_thread = new Thread(vehicle);
					System.out.println(vehicle.getVehicleID() + " arrived");
					vehicle_thread.start();
					long minSleepTime = 20 * 1000;
					long randTime = (long)random.nextInt(10);
					Thread.sleep(minSleepTime + randTime);
				} catch (DuplicateVehicleIDException e) {
					// TODO Auto-generated catch block
					System.out.println("Vehicle ID DUP ATTEMPTED");
				}
			}
		}
		catch(InterruptedException e) {
			System.out.println("*****Vehicle gen interrupt*****");
		}
	}
	
	

}
