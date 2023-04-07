package JunctionSim;

import java.awt.EventQueue;

import controller.GUIMainController;
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
		Vehicles vehiclesModel = new Vehicles(vehiclesCSVFile);
		Phases phasesModel = new Phases(vehiclesModel, intersectionCSVFile);
		GUIMain view = new GUIMain();
		GUIMainController controller = new GUIMainController(phasesModel, vehiclesModel, view);

	}
}
