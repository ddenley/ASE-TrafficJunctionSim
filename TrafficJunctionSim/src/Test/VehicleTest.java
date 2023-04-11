/**
 * 
 */
package Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.Intersection;
import model.Vehicle;

/**
 * @author Daniel Denley and Ahmad
 *
 */
class VehicleTest {

	//Tests for the vehicle constructor
	Intersection intersection = new Intersection();
	
	@Test
	final void testVehicleConstructorValid() {
		
		//Build multiple vehicles which should be valid
		Vehicle vehicle = new Vehicle("123456", "Car", "20", "Left", "20", "5", "Crossed", "S1", this.intersection);
		Vehicle vehicle1 = new Vehicle("123456", "Bus", "29", "Right", "20", "29", "Crossed", "S3", this.intersection);
		Vehicle vehicle2 = new Vehicle("123456", "Van", "20", "Left", "29.9", "5", "WAITING", "S1", this.intersection);
		Vehicle vehicle3 = new Vehicle("rrrIOp", "Car", "0.1", "Left", "20", "5", "Crossed", "S2", this.intersection);
		Vehicle vehicle4 = new Vehicle("12s456", "Car", "20", "Straight", "20", "5", "Crossed", "S4", this.intersection);
		Vehicle vehicle5 = new Vehicle("123456", "Car", "20", "Left", "0.1", "5", "Waiting", "S1", this.intersection);
		Vehicle vehicle6 = new Vehicle("123q56", "Car", "20", "Left", "20", "0.1", "CroSSed", "S1", this.intersection);
	}
	
	@Test
	final void testVehicleConstructorIDInvalidMissing() {
		
		try {
			Vehicle vehicle = new Vehicle("", "Car", "20", "Left", "20", "5", "Crossed", "S1", this.intersection);
		}
		catch(IllegalArgumentException ae){
			assertTrue(ae.getMessage().equals("Vehicle ID not present"));
		}
	}
	
	@Test
	final void testVehicleConstructorIDInvalidLengthTooLong() {
		try {
			Vehicle vehicle = new Vehicle("TOOLONG", "Car", "20", "Left", "20", "5", "Crossed", "S1", this.intersection);
		}
		catch(IllegalArgumentException ae){
			assertTrue(ae.getMessage().equals("Invalid vehicle ID - Ensure 6 Characters Long: " + "TOOLONG"));
		}
	}
	
	@Test
	final void testVehicleConstructorIDInvalidLengthTooShort() {
		Intersection intersection = new Intersection();
		try {
			Vehicle vehicle = new Vehicle("TOOLO", "Car", "20", "Left", "20", "5", "Crossed", "S1", this.intersection);
		}
		catch(IllegalArgumentException ae){
			assertTrue(ae.getMessage().equals("Invalid vehicle ID - Ensure 6 Characters Long: " + "TOOLO"));
		}
	}
	
	@Test
	final void testVehicleConstructorIDInvalidCharacters() {
		try {
			Vehicle vehicle = new Vehicle("TOOLO&", "Car", "20", "Left", "20", "5", "Crossed", "S1", this.intersection);
		}
		catch(IllegalArgumentException ae){
			assertTrue(ae.getMessage().equals("Invalid vehicle ID - Invalid Characters: " + "TOOLO&"));
		}
	}
	
	@Test
	final void testVehicleConstructorTypeMissing() {
		try {
			Vehicle vehicle = new Vehicle("dfasvS", "", "20", "Left", "20", "5", "Crossed", "S1", this.intersection);
		}
		catch(IllegalArgumentException ae){
			assertTrue(ae.getMessage().equals("Vehicle Type not present"));
		}
	}
	
	@Test
	final void testVehicleConstructorTypeInvalid() {
		try {
			Vehicle vehicle = new Vehicle("dfasvS", "Bike", "20", "Left", "20", "5", "Crossed", "S1", this.intersection);
		}
		catch(IllegalArgumentException ae){
			assertTrue(ae.getMessage().equals("Invalid vehicle type: " + "Bike"));
		}
	}
	
	@Test
	final void testVehicleConstructorCrossingTimeMissing() {
		try {
			Vehicle vehicle = new Vehicle("hhy567", "Car", "", "Left", "20", "5", "Crossed", "S1", this.intersection);
		}
		catch(IllegalArgumentException ae){
			assertTrue(ae.getMessage().equals("Vehicle Crossing Time not present"));
		}
	}
	
	@Test
	final void testVehicleConstructorCrossingSmallerThanZero() {
		try {
			Vehicle vehicle = new Vehicle("hhy567", "Car", "-0.1", "Left", "20", "5", "Crossed", "S1", this.intersection);
		}
		catch(IllegalArgumentException ae){
			assertTrue(ae.getMessage().equals("Invalid crossing time: " + "-0.1"));
		}
	}
	
	@Test
	final void testVehicleConstructorCrossingLargerThanFiveHundred() {
		try {
			Vehicle vehicle = new Vehicle("hhy567", "Car", "500.01", "Left", "20", "5", "Crossed", "S1", this.intersection);
		}
		catch(IllegalArgumentException ae){
			assertTrue(ae.getMessage().equals("Invalid crossing time: " + "500.01"));
		}
	}
	
	@Test
	final void testVehicleConstructorCrossingBorderCaseFiveHundred() {
		try {
			Vehicle vehicle = new Vehicle("hhy567", "Car", "500", "Left", "20", "5", "Crossed", "S1", this.intersection);
		}
		catch(IllegalArgumentException ae){
			assertTrue(ae.getMessage().equals("Invalid crossing time: " + "500"));
		}
	}
	
	@Test
	final void testVehicleConstructorCrossingBorderCaseZero() {
		try {
			Vehicle vehicle = new Vehicle("hhy567", "Car", "00", "Left", "20", "5", "Crossed", "S1", this.intersection);
		}
		catch(IllegalArgumentException ae){
			assertTrue(ae.getMessage().equals("Invalid crossing time: " + "00"));
		}
	}
	
	@Test
	final void testVehicleConstructorDirectionMissing() {
		try {
			Vehicle vehicle = new Vehicle("jko980", "Car", "20", "", "20", "5", "Crossed", "S1", this.intersection);
		}
		catch(IllegalArgumentException ae){
			assertTrue(ae.getMessage().equals("Vehicle Direction not present"));
		}
	}
	
	@Test
	final void testVehicleConstructorDirectionInvalid() {
		try {
			Vehicle vehicle = new Vehicle("jko980", "Car", "20", "Back", "20", "5", "Crossed", "S1", this.intersection);
		}
		catch(IllegalArgumentException ae){
			assertTrue(ae.getMessage().equals("Invalid direction: " + "Back"));
		}
	}
	
	@Test
	final void testVehicleConstructorLengthMissing() {
		try {
			Vehicle vehicle = new Vehicle("LLLLLL", "Car", "20", "Left", "", "5", "Crossed", "S1", this.intersection);
		}
		catch(IllegalArgumentException ae){
			assertTrue(ae.getMessage().equals("Vehicle Length not present"));
		}
	}
	
	@Test
	final void testVehicleConstructorLengthNotNumeric() {
		try {
			Vehicle vehicle = new Vehicle("LLLLLL", "Car", "20", "Left", "g6", "5", "Crossed", "S1", this.intersection);
		}
		catch(IllegalArgumentException ae){
			System.out.println(ae.getMessage());
			assertTrue(ae.getMessage().equals("Invalid length - must be numeric: " + "g6"));
		}
	}
	
	@Test
	final void testVehicleConstructorLengthSmallerThanZero() {
		try {
			Vehicle vehicle = new Vehicle("LLLLLL", "Car", "20", "Left", "-0.01", "5", "Crossed", "S1", this.intersection);
		}
		catch(IllegalArgumentException ae){
			assertTrue(ae.getMessage().equals("Invalid length: " + "-0.01"));
		}
	}
	
	@Test
	final void testVehicleConstructorLengthBorderCaseZero() {
		try {
			Vehicle vehicle = new Vehicle("LLLLLL", "Car", "20", "Left", "00", "5", "Crossed", "S1", this.intersection);
		}
		catch(IllegalArgumentException ae){
			assertTrue(ae.getMessage().equals("Invalid length: " + "00"));
		}
	}
	
	@Test
	final void testVehicleConstructorLengthLargerThanThirty() {
		try {
			Vehicle vehicle = new Vehicle("LLLLLL", "Car", "20", "Left", "30.01", "5", "Crossed", "S1", this.intersection);
		}
		catch(IllegalArgumentException ae){
			assertTrue(ae.getMessage().equals("Invalid length: " + "30.01"));
		}
	}
	
	@Test
	final void testVehicleConstructorLengthBorderCaseThirty() {
		try {
			Vehicle vehicle = new Vehicle("LLLLLL", "Car", "20", "Left", "30", "5", "Crossed", "S1", this.intersection);
		}
		catch(IllegalArgumentException ae){
			assertTrue(ae.getMessage().equals("Invalid length: " + "30"));
		}
	}
	
	@Test
	final void testVehicleConstructorEmissionRateMissing() {
		try {
			Vehicle vehicle = new Vehicle("123456", "Car", "20", "Left", "20", "", "Crossed", "S1", this.intersection);
		}
		catch(IllegalArgumentException ae){
			assertTrue(ae.getMessage().equals("Vehicle Emission Rate not present"));
		}
	}
	
	@Test
	final void testVehicleConstructorEmissionRateNotNumeric() {
		try {
			Vehicle vehicle = new Vehicle("123456", "Car", "20", "Left", "20", "67l", "Crossed", "S1", this.intersection);
		}
		catch(IllegalArgumentException ae){
			assertTrue(ae.getMessage().equals("Invalid emission rate - must be numeric: " + "67l"));
		}
	}
	
	@Test
	final void testVehicleConstructorEmissionRateBorderCaseZero() {
		try {
			Vehicle vehicle = new Vehicle("hgbjui", "Car", "20", "Left", "20", "0", "Crossed", "S1", this.intersection);
		}
		catch(IllegalArgumentException ae){
			assertTrue(ae.getMessage().equals("Invalid emission rate: " + "0"));
		}
	}
	
	@Test
	final void testVehicleConstructorEmissionRateLessThanZero() {
		try {
			Vehicle vehicle = new Vehicle("lkjmni", "Car", "20", "Left", "20", "-0.2", "Crossed", "S1", this.intersection);
		}
		catch(IllegalArgumentException ae){
			assertTrue(ae.getMessage().equals("Invalid emission rate: " + "-0.2"));
		}
	}
	
	@Test
	final void testVehicleConstructorEmissionRateBorderCaseThirty() {
		try {
			Vehicle vehicle = new Vehicle("bhgn88", "Car", "20", "Left", "20", "30", "Crossed", "S1", this.intersection);
		}
		catch(IllegalArgumentException ae){
			assertTrue(ae.getMessage().equals("Invalid emission rate: " + "30"));
		}
	}
	
	@Test
	final void testVehicleConstructorEmissionRateLargerThanThirty() {
		try {
			Vehicle vehicle = new Vehicle("AAAWAW", "Car", "20", "Left", "20", "31", "Crossed", "S1", this.intersection);
		}
		catch(IllegalArgumentException ae){
			assertTrue(ae.getMessage().equals("Invalid emission rate: " + "31"));
		}
	}
	
	@Test
	final void testVehicleConstructorStatusMissing() {
		try {
			Vehicle vehicle = new Vehicle("klklkl", "Car", "20", "Left", "20", "5", "", "S1", this.intersection);
		}
		catch(IllegalArgumentException ae){
			assertTrue(ae.getMessage().equals("Vehicle Status not present"));
		}
	}
	
	@Test
	final void testVehicleConstructorStatusInvalid() {
		try {
			Vehicle vehicle = new Vehicle("klklkl", "Car", "20", "Left", "20", "5", "Driving", "S1", this.intersection);
		}
		catch(IllegalArgumentException ae){
			assertTrue(ae.getMessage().equals("Invalid status: " + "Driving"));
		}
	}
	
	@Test
	final void testVehicleConstructorSegmentMissing() {
		try {
			Vehicle vehicle = new Vehicle("SEGMMM", "Car", "20", "Left", "20", "5", "Crossed", "", this.intersection);
		}
		catch(IllegalArgumentException ae){
			assertTrue(ae.getMessage().equals("Vehicle Segment not present"));
		}
	}
	
	@Test
	final void testVehicleConstructorSegmentInvalid() {
		try {
			Vehicle vehicle = new Vehicle("SEGMMM", "Car", "20", "Left", "20", "5", "Crossed", "S9", this.intersection);
		}
		catch(IllegalArgumentException ae){
			assertTrue(ae.getMessage().equals("Invalid segment: " + "S9"));
		}
	}
	
	//Tests for the vehicle segment allocation
	@Test
	final void vehicleEightPhaseAllocationP1() {
		Vehicle vehicle = new Vehicle("SEGMMM", "Car", "20", "Left", "20", "5", "Crossed", "S1", this.intersection);
		assertTrue(vehicle.getEightPhaseAllocation().equals("P1"));
	}
	
	@Test
	final void vehicleEightPhaseAllocationP2() {
		Vehicle vehicle = new Vehicle("SEGMMM", "Car", "20", "Right", "20", "5", "Crossed", "S3", this.intersection);
		Vehicle vehicle2 = new Vehicle("SEGMMM", "Car", "20", "Straight", "20", "5", "Crossed", "S3", this.intersection);
		assertTrue(vehicle.getEightPhaseAllocation().equals("P2") && vehicle2.getEightPhaseAllocation().equals("P2"));
	}
	
	@Test
	final void vehicleEightPhaseAllocationP3() {
		Vehicle vehicle = new Vehicle("SEGMMM", "Car", "20", "Left", "20", "5", "Crossed", "S4", this.intersection);
		assertTrue(vehicle.getEightPhaseAllocation().equals("P3"));
	}
	
	@Test
	final void vehicleEightPhaseAllocationP4() {
		Vehicle vehicle = new Vehicle("SEGMMM", "Car", "20", "Straight", "20", "5", "Crossed", "S2", this.intersection);
		Vehicle vehicle2 = new Vehicle("SEGMMM", "Car", "20", "Right", "20", "5", "Crossed", "S2", this.intersection);
		assertTrue(vehicle.getEightPhaseAllocation().equals("P4") && vehicle.getEightPhaseAllocation().equals("P4"));
	}
	
	@Test
	final void vehicleEightPhaseAllocationP5() {
		Vehicle vehicle = new Vehicle("SEGMMM", "Car", "20", "Left", "20", "5", "Crossed", "S3", this.intersection);
		assertTrue(vehicle.getEightPhaseAllocation().equals("P5"));
	}
	
	@Test
	final void vehicleEightPhaseAllocationP6() {
		Vehicle vehicle = new Vehicle("SEGMMM", "Car", "20", "Straight", "20", "5", "Crossed", "S1", this.intersection);
		Vehicle vehicle2 = new Vehicle("SEGMMM", "Car", "20", "Right", "20", "5", "Crossed", "S1", this.intersection);
		assertTrue(vehicle.getEightPhaseAllocation().equals("P6") && vehicle2.getEightPhaseAllocation().equals("P6"));
	}
	
	@Test
	final void vehicleEightPhaseAllocationP7() {
		Vehicle vehicle = new Vehicle("SEGMMM", "Car", "20", "Left", "20", "5", "Crossed", "S2", this.intersection);
		assertTrue(vehicle.getEightPhaseAllocation().equals("P7"));
	}
	
	@Test
	final void vehicleEightPhaseAllocationP8() {
		Vehicle vehicle = new Vehicle("SEGMMM", "Car", "20", "Straight", "20", "5", "Crossed", "S4", this.intersection);
		Vehicle vehicle2 = new Vehicle("SEGMMM", "Car", "20", "Right", "20", "5", "Crossed", "S4", this.intersection);
		assertTrue(vehicle.getEightPhaseAllocation().equals("P8") && vehicle2.getEightPhaseAllocation().equals("P8"));
	}
}
