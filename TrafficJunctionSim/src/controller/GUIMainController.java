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
import utility.ProduceReport;
import view.GUIMain;

public class GUIMainController {
	private Phases phaseModel;
	private Vehicles vehiclesModel;
	private GUIMain view;
	private TrafficController trafficController;
	private boolean tControllerMade;
	private Thread trafficControllerThread;
	private List<Thread> vehicleThreads;
	
	
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
		view.addBtnStopListener(actionHandler);
		//Init the view gui
		updateGUI();
		view.setVisible(true);
		//Provide view with controller so it can call GUI update methods
		view.controller = this;
		//New traffic controller
		this.trafficController = new TrafficController(phaseModel, this);
		this.tControllerMade = false;
		//Create vehicle threads and store them here
		this.vehicleThreads = new ArrayList();
		initVehiclesThreads();
	}
	
	private void initVehiclesThreads() {
		for(Vehicle vehicle : vehiclesModel.getVehiclesHashMap().values()) {
			Thread vehicle_thread = new Thread(vehicle);
			vehicleThreads.add(vehicle_thread);
			vehicle_thread.start();
		}
	}
	
	//Update methods for tables
	private void updateVehiclesTable() {
		//Get data from model
		String[] vehiclesHeader = vehiclesModel.getVehicleHeaders();
		Object[][] vehiclesContents = vehiclesModel.getVehicles2DArray();
		//Set in view
		System.out.println("TEST");
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
		String[] activePhases = trafficController.getActivePhases();
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
	private void updateCO2EstimateLabel() {
		String text = "Estimated Total C02 During Simulation: " + phaseModel.getTotalCO2Estimate() + " KG";
		view.setLblCO2Estimate(text);
	}
	
	
	//Inner class SetListener responds to user actions
	private class ActionHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String action = e.getActionCommand();
			if ("btnExit".equals(action)) {
				System.out.println("Exit action");
				exitFunction();
			}
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
			else if ("btnClearVehicleInput".equals(action)) {
				System.out.println("Clear input");
				updateAddVehicleTableToEmpty();
			}
			else if ("btnStart".equals(action)) {
				System.out.println("Start");
				//Start traffic controller thread
				if (!tControllerMade) {
					tControllerMade = true;
					trafficControllerThread = new Thread(trafficController);
					trafficControllerThread.start();
				}
				else {
					System.out.println("Already started");
				}
			}
			//TODO: FIX IF TIME ALLOWS
			else if ("btnStop".equals(action)) {
				System.out.println("Start");
				trafficController.endSimulation();
				trafficControllerThread.interrupt();
			}
		}
	}
	
	//Method to add vehicle to model
	private void addVehicle() throws DuplicateVehicleIDException {
		//Get user input from view
		String[] vehicleParams = view.getVehicleInput();
		//Change model
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
			}
			catch(DuplicateVehicleIDException dke) {
				throw dke;
			}
		}
		catch(IllegalArgumentException ae) {
			throw ae;
		}
	}
	
	public void updateGUI() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				System.out.println("Model changed");
				updateVehiclesTable();
				updatePhasesTable();
				updatePhasesAllocationTable();
				updateStatisticsTable();
				updateCO2PerMinuteLabel();
				updateCO2EstimateLabel();
			}
			
		});
	}
	
	private void exitFunction() {
		int[] vehiclesCrossedCounts = vehiclesModel.getVehiclesCrossedCounts();
		float[] averageSegmentWaitTimes = phaseModel.getAverageSegmentWaitingTimes();
		Object[][] vehicleStatistics = vehiclesModel.getSegmentStatistics();
		String[] lines = new String[5];
		lines[0] = "Segment 1:-	Vehicles Crossed:" + vehiclesCrossedCounts[0] + " average waiting time: " + averageSegmentWaitTimes[0];
		lines[1] = "Segment 2:-	Vehicles Crossed:" + vehiclesCrossedCounts[1] + " average waiting time: " + averageSegmentWaitTimes[1];
		lines[2] = "Segment 3:-	Vehicles Crossed:" + vehiclesCrossedCounts[2] + " average waiting time: " + averageSegmentWaitTimes[2];
		lines[3] = "Segment 4:-	Vehicles Crossed:" + vehiclesCrossedCounts[3] + " average waiting time: " + averageSegmentWaitTimes[3];
		lines[4] = "Total emissions: " + vehiclesModel.getTotalCO2PerMinute();
		ProduceReport.createReport(lines);
		System.exit(0);
	}
	
	//Use controller to update GUI
		public void trafficControllerUpdated() {
			System.out.println("Update from traffic controller");
			updateActivePhasesTable();
			//updateVehiclesTable();
		}
	
}
