package controller;

import java.util.Random;

import model.Phases;
import model.Vehicles;

public class VehicleGenerator implements Runnable{

	private final Vehicles vehicles;
	private final Phases phases;
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
	
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	

}
