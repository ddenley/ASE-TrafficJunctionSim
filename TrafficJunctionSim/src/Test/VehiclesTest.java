/**
 * 
 */
package Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import JunctionSim.DuplicateVehicleIDException;
import JunctionSim.Vehicle;
import JunctionSim.Vehicles;

/**
 * @author Daniel Denley
 *
 */
class VehiclesTest {

	//Tests for constructor - reading csv files is more thoroughly tested in ReadCSVTest
	@Test
	final void testVehiclesConstructorValidCSV() {
		Vehicles vehicles = new Vehicles("TestData/VehiclesTestCSVs/test_vehicles_valid.csv");
	}
	
	@Test
	final void testVehiclesConstructorMissingRowsCSV() {
		Vehicles vehicles = new Vehicles("TestData/VehiclesTestCSVs/test_vehicles_missing_rows.csv");
	}
	
	@Test
	final void testVehiclesConstructorMissingValuesCSV() {
		Vehicles vehicles = new Vehicles("TestData/VehiclesTestCSVs/test_vehicles_missing_values.csv");
	}
	
	//Tests for inserting vehicles into hash map
	@Test
	final void testinsertVehicleHashMapValid() {
		Vehicles vehicles = new Vehicles("TestData/VehiclesTestCSVs/test_vehicles_valid.csv");
		Vehicle vehicle = new Vehicle("rrrIOp", "Car", "0.1", "Left", "20", "5", "Crossed", "S2");
		try {
			vehicles.insertVehicleHashMap(vehicle);
		}
		catch (Exception e){
			fail("Unable to add valid vehicle to hash map");
		}
	}
	
	//Duplicate vehicle key
	@Test
	final void testinsertVehicleHashMapDuplicateKey() {
		Vehicles vehicles = new Vehicles("TestData/VehiclesTestCSVs/test_vehicles_valid.csv");
		Vehicle vehicle = new Vehicle("rrrIOp", "Car", "0.1", "Left", "20", "5", "Crossed", "S2");
		Vehicle vehicle2 = new Vehicle("rrrIOp", "Bus", "20", "Left", "24", "2", "Waiting", "S1");
		try {
			vehicles.insertVehicleHashMap(vehicle);
			assertThrows(DuplicateVehicleIDException.class, () -> vehicles.insertVehicleHashMap(vehicle2));
		}
		catch (Exception e){
			fail("Other exception thrown");
		}
	}
	
	//Test for calculating CO2
	@Test
	final void testGetTotalC02() {
		Vehicles vehicles = new Vehicles("TestData/VehiclesTestCSVs/test_vehicles_valid_for_stats.csv");
		String actual = vehicles.getTotalCO2();
		float emissionRateSum = 12 + 7 + 9 + 12 + 12;
		float expectedFloat = emissionRateSum / 1000;
		String expected = String.format("%.2f", expectedFloat) + " KG per minute";
		assertEquals(expected,actual);
	}
	
	//Test for getting segment statistics
	@Test
	final void testGetSegmentStatistics() {
		Vehicles vehicles = new Vehicles("TestData/VehiclesTestCSVs/test_vehicles_valid_for_stats.csv");
		Object[][] expected = new Object[4][5];
		expected[0][0] = "S1";
		expected[1][0] = "S2";
		expected[2][0] = "S3";
		expected[3][0] = "S4";
		//Segment 1 expected stats - total length
		expected[0][1] = (float)(5 + 5);
		//Segment 1 expected stats - average crossing time
		expected[0][2] = (float)((10 + 10) / 2);
		//Segment 1 expected stats - number of vehicles
		expected[0][3] = 2;
		//Segment 2 expected stats - total length
		expected[1][1] = (float)(5);
		//Segment 2 expected stats - average crossing time
		expected[1][2] = (float)(10);
		//Segment 2 expected stats - number of vehicles
		expected[1][3] = 1;
		//Segment 3 expected stats - total length
		expected[2][1] = (float)(12);
		//Segment 3 expected stats - average crossing time
		expected[2][2] = (float)(6);
		//Segment 3 expected stats - number of vehicles
		expected[2][3] = 1;
		//Segment 4 expected stats - total length
		expected[3][1] = (float)(14);
		//Segment 4 expected stats - average crossing time
		expected[3][2] = (float)(22);
		//Segment 4 expected stats - number of vehicles
		expected[3][3] = 1;
		
		Object[][] actual = vehicles.getSegmentStatistics();
		assertArrayEquals(expected, actual);
	}
	
	//Test for getting vehicles crossed count
	final void testGetVehiclesCrossedCounts() {
		Vehicles vehicles = new Vehicles("TestData/VehiclesTestCSVs/test_vehicles_valid_for_stats.csv");
		Vehicle vehicle1 = new Vehicle("1rrIOp", "Car", "0.1", "Left", "20", "5", "Crossed", "S2");
		Vehicle vehicle2 = new Vehicle("2rrIOp", "Car", "0.1", "Left", "20", "5", "Crossed", "S2");
		Vehicle vehicle4 = new Vehicle("4rrIOp", "Car", "0.1", "Left", "20", "5", "Crossed", "S2");
		Vehicle vehicle5 = new Vehicle("5rrIOp", "Car", "0.1", "Left", "20", "5", "Crossed", "S1");
		Vehicle vehicle6 = new Vehicle("6rrIOp", "Car", "0.1", "Left", "20", "5", "Crossed", "S4");
		Vehicle vehicle7 = new Vehicle("7rrIOp", "Car", "0.1", "Left", "20", "5", "Crossed", "S4");
		try {
			vehicles.insertVehicleHashMap(vehicle1);
		} catch (DuplicateVehicleIDException e) {
			fail("Unexpected duplicate ID Exception thrown");
		}
		int[] actual = vehicles.getVehiclesCrossedCounts();
		int[] expected = new int[4];
		expected[0] = 1;
		expected[1] = 2;
		expected[2] = 0;
		expected[3] = 2;
		assertArrayEquals(expected,actual);		
	}
	
	//Test for getting the wait time of a vehicle - this is introduced in stage 2 with implementation of simulation
	
}
