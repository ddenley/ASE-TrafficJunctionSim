package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import exceptions.DuplicateVehicleIDException;
import model.Phases;
import model.Vehicle;
import model.Vehicles;
import utility.Logger;
import utility.ProduceReport;
import view.GUIMain;


//Main controller thread of application
//Updates GUI
//Recieves calls from  TrafficController
public class GUIMainController {
	private Phases phaseModel;
	private Vehicles vehiclesModel;
	private GUIMain view;
	private TrafficController trafficController;
	private boolean tControllerMade;
	private Thread trafficControllerThread;
	
	
	public GUIMainController(Phases phaseModel, Vehicles vehiclesModel, GUIMain view) {
		this.phaseModel = phaseModel;
		this.vehiclesModel = vehiclesModel;
		this.view = view;
		
		//Register observers to view
		phaseModel.addObserver(view);
		vehiclesModel.addObserver(view);
		
		//Attach listeners
		ActionListener actionHandler = new ActionHandler();
		view.addBtnExitListener(actionHandler);
		view.addBtnAddVehicleListener(actionHandler);
		view.addBtnClearVehicleInputListener(actionHandler);
		view.addBtnStartListener(actionHandler);
		
		//Init the view gui
		updateGUI();
		view.setVisible(true);
		
		//Provide view with controller so it can call GUI update methods
		view.controller = this;
		
		//New traffic controller
		this.trafficController = new TrafficController(phaseModel, this);
		this.tControllerMade = false;
		
		//Start vehicle threads
		initVehiclesThreads();
	}
	
	
	//Starts all vehicle threads - will be in wait state by default
	private void initVehiclesThreads() {
		for(Vehicle vehicle : vehiclesModel.getVehiclesHashMap().values()) {
			Thread vehicle_thread = new Thread(vehicle);
			vehicle_thread.start();
		}
	}
	
	
	//Update methods for tables
	private void updateVehiclesTable() {
		//Get data from model
		String[] vehiclesHeader = vehiclesModel.getVehicleHeaders();
		Object[][] vehiclesContents = vehiclesModel.getVehicles2DArray();
		//Set in view
		view.setTableVehicles(vehiclesHeader, vehiclesContents);
	}
	private void updatePhasesTable() {
		//Get data from model
		String[] phasesHeader = phaseModel.getPhaseHeaders();
		Object[][] phasesContents = phaseModel.getPhases2DObjectArray();
		//Set in view
		view.setTablePhases(phasesHeader, phasesContents);
	}
	private void updatePhasesAllocationTable() {
		//Get data from model
		String[] phaseAllocationHeader = {"Phase", "Vehicle"};
		Object[][] phaseAllocationContent = phaseModel.getPhasesVehicles2DObjectArray();
		//Set in view
		view.setTablePhaseAllocation(phaseAllocationHeader, phaseAllocationContent);
	}
	private void updateStatisticsTable() {
		//Get data from model
		String[] statisticsHeader = {"Segment", "Total Length", "Avg Cross Time", "Number of Vehicles"};
		Object[][] statisticsContent = vehiclesModel.getSegmentStatistics();
		//Set in view
		view.setTableStatistics(statisticsHeader, statisticsContent);
	}
	private void updateAddVehicleTableToEmpty() {
		view.setTableAddVehicleToEmpty();
	}
	private synchronized void updateActivePhasesTable() {
		String[] activePhases = trafficController.getPhaseNameWithLightStatus();
		Object[][] activePhasesContent = new Object[2][1];
		int i = 0;
		for (String phaseName : activePhases) {
			activePhasesContent[i][0] = phaseName;
			i++;
		}
		view.setTableActivePhases(activePhasesContent);
	}
	
	
	//Update methods for labels
	private void updateCO2PerMinuteLabel() {
		String text = "C02: " + vehiclesModel.getTotalCO2PerMinute();
		view.setLblCO2PerMinute(text);
	}
	
	
	//Inner class SetListener responds to user actions
	private class ActionHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String action = e.getActionCommand();
			//Exit button
			if ("btnExit".equals(action)) {
				System.out.println("Exit action");
				exitFunction();
			}
			//Add vehicle button
			else if ("btnAddVehicle".equals(action)) {
				System.out.println("Add Vehicle");
				try {
					addVehicle();
				}
				catch(IllegalArgumentException ae){
					System.out.println("Vehicle could not be added");
					System.out.println(ae.getMessage());
					JOptionPane.showMessageDialog(view, ae.getMessage(),
				               "Could Not Add Vehicle", JOptionPane.ERROR_MESSAGE);
				}
				catch(DuplicateVehicleIDException dke) {
					System.out.println("Vehicle could not be added");
					System.out.println(dke.getMessage());
					JOptionPane.showMessageDialog(view, dke.getMessage(),
				               "Could Not Add Vehicle", JOptionPane.ERROR_MESSAGE);
				}
			}
			//Clear input button
			else if ("btnClearVehicleInput".equals(action)) {
				System.out.println("Clear input");
				updateAddVehicleTableToEmpty();
			}
			//Start button
			else if ("btnStart".equals(action)) {
				//Start traffic controller thread
				if (!tControllerMade) {
					tControllerMade = true;
					trafficControllerThread = new Thread(trafficController);
					trafficControllerThread.start();
					//Start arrival process
					VehicleGenerator vehicleGen = new VehicleGenerator(vehiclesModel, phaseModel);
					Thread vehicleGenThread = new Thread(vehicleGen);
					vehicleGenThread.start();
				}
				else {
					System.out.println("Already started");
				}
			}
		}
	}
	
	
	//Method to add vehicle to model - called on click of add vehicle button
	private void addVehicle() throws DuplicateVehicleIDException {
		//Get user input from view
		String[] vehicleParams = view.getVehicleInput();
		//Build vehicle from input params and insert to hash map and queue
		try {
			//Build vehicle
			Vehicle vehicle = vehiclesModel.buildVehicle(vehicleParams);
			try {
				//Add vehicle to hash map
				vehiclesModel.insertVehicleHashMap(vehicle);				
				//Add vehicle to queue if in waiting state
				if(vehicle.getStatus().equals("Waiting")) {
					phaseModel.insertVehicleQueue(vehicle);
				}
				Logger.getInstance().log(vehicle.getVehicleID() + " added via GUI");
			}
			catch(DuplicateVehicleIDException dke) {
				throw dke;
			}
		}
		catch(IllegalArgumentException ae) {
			throw ae;
		}
	}
	
	
	//Called on change from a model class - i.e. vehicle added, status changed
	public synchronized void updateGUI() {
		//Ensures the code is ran by the main GUI thread - prevents threading errors of concurrent access
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				updateVehiclesTable();
				updatePhasesTable();
				updatePhasesAllocationTable();
				updateStatisticsTable();
				updateCO2PerMinuteLabel();
			}	
		});
	}
	
	
	//Called on click of exit button on GUI
	private void exitFunction() {
		//Section of code below builds the array to be passed to ProduceReport to write 
		String[] lines = new String[8];
		String[][] phasesStats = vehiclesModel.phaseStatistics();
		int i = 0;
		for(String[] phaseStats : phasesStats) {
			String crossedCounts = phaseStats[0];
			String emissionSum = String.format("%.2f",Double.valueOf(phaseStats[1]));
			String waitingTimeSum = phaseStats[2];
			String averageWaitingTime = String.valueOf(Double.valueOf(waitingTimeSum) / Double.valueOf(crossedCounts));
			if(averageWaitingTime.equals("NaN")) {
				averageWaitingTime = "0";
			}
			averageWaitingTime = String.format("%.2f",Double.valueOf(averageWaitingTime));
			lines[i] = String.format("Phase: %s	Vehicles crossed: %s	Emissions Produced: %s	Average Vehicle Wait Time: %s", i+1, crossedCounts, emissionSum, averageWaitingTime);
			i++;
		}
		ProduceReport.createReport(lines);
		System.exit(0);
	}
	
	
		//Called from traffic controller to update lights and active phases table
		public void trafficControllerUpdated() {
			updateActivePhasesTable();
		}
	
}
