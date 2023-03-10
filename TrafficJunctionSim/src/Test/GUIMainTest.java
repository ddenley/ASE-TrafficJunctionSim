package Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import JunctionSim.GUIMain;

class GUIMainTest {

	//Note all action methods are private within this class
	//They call upon methods that have been thoroughly tested in other test classes
	//Only testing needing done is on the constructor
	
	GUIMain main;
	
	@Test
	final void testGUIMainConstructor() {
		main.vehiclesCSVFile = "TestData/VehiclesTestCSVs/test_vehicles_valid.csv";
		main.intersectionCSVFile = "TestData/IntersectionTestCSVs/test_intersection_valid.csv";
		GUIMain guimain = new GUIMain();
	}

}
