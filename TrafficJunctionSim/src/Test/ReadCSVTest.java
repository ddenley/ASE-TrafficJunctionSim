package Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.After;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exceptions.InvalidFileFormatException;
import utility.ReadCSV;
import view.GUIMain;
/**
 * @author Daniel Denley
 *
 */
class ReadCSVTest {
	
	GUIMain main;
	@BeforeEach
	void test() {
		main.vehiclesCSVFile = "TestData/VehiclesTestCSVs/test_vehicles_valid.csv";
		main.intersectionCSVFile = "TestData/IntersectionTestCSVs/test_intersection_valid.csv";
		this.main = new GUIMain();
	}

	@Test
	final void testGetHeaderValuesValidVehiclesCSV() {
		//this.main = new GUIMain();
		main.vehiclesCSVFile = "TestData/VehiclesTestCSVs/test_vehicles_valid.csv";
		ReadCSV.getHeaderValues("TestData/VehiclesTestCSVs/test_vehicles_valid.csv");
		main.vehiclesCSVFile = "TestData/VehiclesTestCSVs/test_vehicles_missing_rows.csv";
		ReadCSV.getHeaderValues("TestData/VehiclesTestCSVs/test_vehicles_missing_rows.csv");
	}
	
	@Test
	final void testGetHeaderValuesValidIntersectionCSV() {
		//this.main = new GUIMain();
		main.intersectionCSVFile = "TestData/IntersectionTestCSVs/test_intersection_valid.csv";
		ReadCSV.getHeaderValues("TestData/IntersectionTestCSVs/test_intersection_valid.csv");
	}
	
	@Test
	final void testGetHeaderValuesLessThanOneVehicleVehiclesCSV() {
		//this.main = new GUIMain();
		main.vehiclesCSVFile = "TestData/VehiclesTestCSVs/test_vehicles_no_vehicles.csv";
		assertThrows(InvalidFileFormatException.class,
				() -> ReadCSV.getHeaderValues("TestData/VehiclesTestCSVs/test_vehicles_no_vehicles.csv"));
	}
	
	@Test
	final void testGetHeaderValuesBorderCaseOneVehicle() {
		//this.main = new GUIMain();
		main.vehiclesCSVFile = "TestData/VehiclesTestCSVs/test_vehicles_one_vehicle.csv";
		ReadCSV.getHeaderValues("TestData/VehiclesTestCSVs/test_vehicles_one_vehicle.csv");
	}
	
	@Test
	final void testGetHeaderValuesInvalidPhaseCount9PhasesCSV() {
		//this.main = new GUIMain();
		main.vehiclesCSVFile = "TestData/IntersectionTestCSVs/test_intersection_invalid_9_phases.csv";
		assertThrows(InvalidFileFormatException.class,
				() -> ReadCSV.getHeaderValues("TestData/IntersectionTestCSVs/test_intersection_invalid_9_phases.csv"));
	}
	
	@Test
	final void testGetHeaderValuesInvalidPhaseCount7PhasesCSV() {
		//this.main = new GUIMain();
		main.vehiclesCSVFile = "TestData/IntersectionTestCSVs/test_intersection_invalid_7_phases.csv";
		assertThrows(InvalidFileFormatException.class,
				() -> ReadCSV.getHeaderValues("TestData/IntersectionTestCSVs/test_intersection_invalid_7_phases.csv"));
	}

}
