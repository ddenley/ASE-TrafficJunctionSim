package JunctionSim;

public class Vehicle {
	private String vehicleID;
	private String type;
	private float crossingTime;
	private String direction;
	private float length;
	private float emissionRate;
	private String status;
	private String segment;
	
	//Constructor
	public Vehicle(String vehicleID, String type, String crossingTime, String direction,
			String length, String emissionRate, String status, String segment){
		this.vehicleID = vehicleID;
		this.type = type;
		this.crossingTime = Float.parseFloat(crossingTime);
		this.direction = direction;
		this.length = Float.parseFloat(length);
		this.emissionRate = Float.parseFloat(emissionRate);
		this.status = status;
		this.segment = segment;
	}
	
	//Need method(s) here to calculate phase allocation
	
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
				return "P5";
			}
			if (this.direction.equals("Left")) {
				return "P2";
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
	
	//Also need a setter for status	
	public void setStatus(String status) {
		this.status = status;
	}
}
