package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controller.Intersection;
import view.GUIMain;

/**
 * @author Daniel Denley
 *
 */
public class Vehicle implements Runnable{
	private String vehicleID;
	private String type;
	private float crossingTime;
	private String direction;
	private float length;
	private float emissionRate;
	private String status;
	private String segment;
	
	//Shared resource intersection
	private final Intersection intersection;
	
	//Monitor object for thread communication and syncronization
	private final Object vehicleMonitor;
	
	private final AtomicBoolean isActive = new AtomicBoolean(false);
	
	//TODO: These variables are for implementation in STAGE 2 can ignore
	//This variable will hold how many cycles occurred before this vehicle crossed
	private int cyclesBeforeCross;
	//This variable holds how many vehicles passed before this one in the phase it crossed
	private int phaseVehicleTurn;
	
	//Conscious decision to not use ENUMS here as was reducing code readability
	//Variable for allowed vehicle types
	private final List<String> allowedVehicles = Arrays.asList("Bus", "Car", "Van");
	//Variable for allowed vehicle directions
	private final List<String> allowedDirections = Arrays.asList("Left", "Right", "Straight");
	//Variable for allowed vehicle status - although crossing is a status a vehicle should not be added with this
	private final List<String> allowedStatuses = Arrays.asList("Waiting", "Crossed");
	//Variable for allowed vehicle segments
	private final List<String> allowedSegments = Arrays.asList("S1", "S2", "S3", "S4");
	
	//Crossing time as milliseconds long
	private long crossingTimeMilli;
	private Phases phases;
	
	
	//Constructor for vehicle object
	//Throws illegal argument exceptions and responsible for casting inputs to correct data type
	//Illegal argument exceptions are handled if vehicle is created from GUI
	public Vehicle(String vehicleID, String type, String crossingTime, String direction,
			String length, String emissionRate, String status, String segment, Intersection intersection){
		
		//Initial check all parameters are not empty
		if (vehicleID == null || vehicleID == "") {
			throw new IllegalArgumentException("Vehicle ID not present");
		}
		if (type == null || type == "") {
			throw new IllegalArgumentException("Vehicle Type not present");
		}
		if (crossingTime == null || crossingTime == "") {
			throw new IllegalArgumentException("Vehicle Crossing Time not present");
		}
		if (direction == null || direction == "") {
			throw new IllegalArgumentException("Vehicle Direction not present");
		}
		if (length == null || length == "") {
			throw new IllegalArgumentException("Vehicle Length not present");
		}
		if (emissionRate == null || emissionRate == "") {
			throw new IllegalArgumentException("Vehicle Emission Rate not present");
		}
		if (status == null || status == "") {
			throw new IllegalArgumentException("Vehicle Status not present");
		}
		if (segment == null || segment == "") {
			throw new IllegalArgumentException("Vehicle Segment not present");
		}	
		
		//Capitalise all letters for vehicleID
		vehicleID = vehicleID.toUpperCase();
		//Vehicle ID should be 6 chars long
		if (vehicleID.length() != 6) {
			throw new IllegalArgumentException("Invalid vehicle ID - Ensure 6 Characters Long: " + vehicleID);
		}
		//Vehicle ID should have no special characters - https://stackoverflow.com/questions/1795402/check-if-a-string-contains-a-special-character (adapted from)
		Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(vehicleID);
		if (m.find()) {
			throw new IllegalArgumentException("Invalid vehicle ID - Invalid Characters: " + vehicleID);
		}
		this.vehicleID = vehicleID;
		
		
		//Convert vehicle type to appropriate format
		type = type.toLowerCase();
		type = type.substring(0, 1).toUpperCase() + type.substring(1);
		//Check that vehicle ID is of an allowed type
		if(!this.allowedVehicles.contains(type)) {
			throw new IllegalArgumentException("Invalid vehicle type: " + type);
		}	
		this.type = type;
		
		
		//Check that vehicle crossing time is numerical
		try {
			float t = Float.parseFloat(crossingTime);
		}
		catch(NumberFormatException e) {
			throw new IllegalArgumentException("Invalid crossing time - must be numeric: " + crossingTime);
		}
		//Check that vehicle crossing time is valid (above 0 and smaller than 30)
		if(Float.parseFloat(crossingTime) <= 0 || Float.parseFloat(crossingTime) >= 30) {
			throw new IllegalArgumentException("Invalid crossing time: " + crossingTime);
		}
		this.crossingTime = Float.parseFloat(crossingTime);
		
		
		//Convert direction to appropriate format
		direction = direction.toLowerCase();
		direction = direction.substring(0, 1).toUpperCase() + direction.substring(1);
		//Check that direction is of an allowed type
		if(!this.allowedDirections.contains(direction)) {
			throw new IllegalArgumentException("Invalid direction: " + direction);
		}
		this.direction = direction;
		
		
		//Check that length is numerical
		try {
			float t = Float.parseFloat(length);
		}
		catch(NumberFormatException e) {
			throw new IllegalArgumentException("Invalid length - must be numeric: " + length);
		}
		//Check that length is valid (above 0 and smaller than 30)
		if(Float.parseFloat(length) <= 0 || Float.parseFloat(length) >= 30) {
			throw new IllegalArgumentException("Invalid length: " + length);
		}
		this.length = Float.parseFloat(length);
		
		
		//Check that emission rate is numerical
		try {
			float t = Float.parseFloat(emissionRate);
		}
		catch(NumberFormatException e) {
			throw new IllegalArgumentException("Invalid emission rate - must be numeric: " + emissionRate);
		}
		//Check that emission rate is valid (above 0 and smaller than 30)
		if(Float.parseFloat(emissionRate) <= 0 || Float.parseFloat(emissionRate) >= 30) {
			throw new IllegalArgumentException("Invalid emission rate: " + emissionRate);
		}
		this.emissionRate = Float.parseFloat(emissionRate);
		
		
		//Convert status to appropriate format
		status = status.toLowerCase();
		status = status.substring(0, 1).toUpperCase() + status.substring(1);
		//Check that vehicle status is of an allowed type
		if(!this.allowedStatuses.contains(status)) {
			throw new IllegalArgumentException("Invalid status: " + status);
		}	
		this.status = status;
		
		//Convert segment to appropriate format
		segment = segment.toUpperCase();
		//Check that segment is of an allowed type
		if(!this.allowedSegments.contains(segment)) {
			throw new IllegalArgumentException("Invalid segment: " + segment);
		}	
		this.segment = segment;
		
		this.cyclesBeforeCross = 0;
		this.phaseVehicleTurn = 0;
		this.vehicleMonitor = new Object();
		this.intersection = intersection;
		this.crossingTimeMilli = (long)(Float.parseFloat(crossingTime) * 1000);
	}
	
	public void providePhases(Phases phases) {
		this.phases = phases;
	}
	
	//Method for determining allocation in an eight phase layout
	public String getEightPhaseAllocation() {
		//Phase determined by direction and segment
		if (this.segment.equals("S1")) {
			if (this.direction.equals("Right")) {
				return "P6";
			}
			if (this.direction.equals("Left")) {
				return "P1";
			}
			if (this.direction.equals("Straight")) {
				return "P6";
			}
		}
		if (this.segment.equals("S2")) {
			if (this.direction.equals("Right")) {
				return "P4";
			}
			if (this.direction.equals("Left")) {
				return "P7";
			}
			if (this.direction.equals("Straight")) {
				return "P4";
			}
		}
		if (this.segment.equals("S3")) {
			if (this.direction.equals("Right")) {
				return "P2";
			}
			if (this.direction.equals("Left")) {
				return "P5";
			}
			if (this.direction.equals("Straight")) {
				return "P2";
			}
		}
		if (this.segment.equals("S4")) {
			if (this.direction.equals("Right")) {
				return "P8";
			}
			if (this.direction.equals("Left")) {
				return "P3";
			}
			if (this.direction.equals("Straight")) {
				return "P8";
			}
		}
		return null;
	}
	
	//Getters
	public String getVehicleID() {
		return this.vehicleID;
	}
	
	public String getType() {
		return this.type;
	}
	
	public float getCrossingTime() {
		return this.crossingTime;
	}
	
	public String getDirection() {
		return this.direction;
	}
	
	public float getLength() {
		return this.length;
	}
	
	public float getEmissionRate() {
		return this.emissionRate;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public String getSegment() {
		return this.segment;
	}
	
	public int getCyclesBeforeCross() {
		return this.cyclesBeforeCross;
	}
	
	public int getPhaseVehicleTurn() {
		return this.phaseVehicleTurn;
	}
	
	//Also need a setter for status	
	public void setStatus(String status) {
		this.status = status;
		
	}
	
	//Get vehicle monitor
	public Object getVehicleMonitor() {
		return vehicleMonitor;
	}
	
	public void toggleActive() {
        synchronized (vehicleMonitor) {
            isActive.set(!isActive.get());
            vehicleMonitor.notifyAll();
        }
    }

	
	//NOTE: The run method below was implemented with the help of chat gpt
	//I utilized this tool to help in understanding the syncronization for
	//entering the intersection, the code is adapated and bug fixes implemented
	//however for academic honesty I have included this note
	@Override
    public void run() {
        while (true) {
            synchronized (vehicleMonitor) {
                try {
                    while (!isActive.get()) {
                        vehicleMonitor.wait();
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }

            while (isActive.get()) {
                if(phases.isVehicleNext(this)) {
                	try {
                		intersection.enterIntersection(this);
                		phases.notifyObservers();
                		Thread.sleep(crossingTimeMilli);
                		intersection.exitIntersection(this);
                		phases.notifyObservers();
                		phases.removeVehicle(this);
                	}
                	catch(InterruptedException e) {
                		e.printStackTrace();
                	}
                }
                if (Thread.interrupted()) {
                	System.out.println("Thread Interuppted");
                	return;
                }
            }
        }
    }

}
