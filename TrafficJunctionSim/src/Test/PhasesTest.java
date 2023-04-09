package Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import controller.Intersection;
import exceptions.InvalidFileFormatException;
import model.Phases;
import model.Vehicle;
import model.Vehicles;
/**
 * @author Daniel Denley
 *
 */
class PhasesTest {
	Intersection intersection = new Intersection();

	@Test
	final void testPhasesConstructorValidVehiclesValidCSVFile() {
		Vehicles vehicles = new Vehicles("TestData/VehiclesTestCSVs/test_vehicles_valid.csv", this.intersection);
		Phases phases = new Phases(vehicles, "TestData/IntersectionTestCSVs/test_intersection_valid.csv");
	}
	
	@Test
	final void testPhasesConstructorValidVehiclesInvalidCSVFile() {
		Vehicles vehicles = new Vehicles("TestData/VehiclesTestCSVs/test_vehicles_valid.csv", this.intersection);
		assertThrows(InvalidFileFormatException.class,
				() -> new Phases(vehicles, "TestData/IntersectionTestCSVs/test_intersection_invalid.csv"));
	}
	
	//Test cases for invalid vehicles are handled in vehicles test
	
	@Test
	final void testInsertVehicleQueue() {
		Vehicle vehicle = new Vehicle("rrrIOp", "Car", "0.1", "Left", "20", "5", "Crossed", "S2", this.intersection);
		Vehicles vehicles = new Vehicles("TestData/VehiclesTestCSVs/test_vehicles_valid.csv", this.intersection);
		Phases phases = new Phases(vehicles, "TestData/IntersectionTestCSVs/test_intersection_valid.csv");
		phases.insertVehicleQueue(vehicle);	
	}
	
	@Test
	final void testGetCycleTime() {
		Vehicles vehicles = new Vehicles("TestData/VehiclesTestCSVs/test_vehicles_valid.csv", this.intersection);
		Phases phases = new Phases(vehicles, "TestData/IntersectionTestCSVs/test_intersection_valid.csv");
		float actual = phases.getCycleTime();
		float expected = 420;
		assertEquals(expected, actual);
	}
	
	//The test can not be implemented until simulation is implemented in section 2
	@Test
	final void testGetAverageSegmentWaitingTimes() {

	}
}
