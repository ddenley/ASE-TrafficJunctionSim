package JunctionSim;

import java.awt.EventQueue;

import controller.GUIMainController;
import controller.Intersection;
import controller.TrafficController;
import model.Phases;
import model.Vehicles;
import view.GUIMain;

public class JunctionSim {
	
	//Must be public and accessible for some testing functions
	public static String vehiclesCSVFile = "vehicles.csv";
	public static String intersectionCSVFile = "intersection.csv";
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		//Create instance of intersection
		Intersection intersection = new Intersection();
		Vehicles vehiclesModel = new Vehicles(vehiclesCSVFile, intersection);
		Phases phasesModel = new Phases(vehiclesModel, intersectionCSVFile);
		GUIMain view = new GUIMain();
		GUIMainController controller = new GUIMainController(phasesModel, vehiclesModel, view);
	}
}
