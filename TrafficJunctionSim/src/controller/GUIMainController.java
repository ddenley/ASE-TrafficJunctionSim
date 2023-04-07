package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.JOptionPane;

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
				SignalController scontroller = new SignalController();
				LinkedList<Object[][]> segments = scontroller.createSegmentList(phaseModel.getPhasesHmap());
				for (Object[][] segment : segments) {
					System.out.println(Arrays.deepToString(segment));
				}
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
		System.out.println("Model changed");
		updateVehiclesTable();
		updatePhasesTable();
		updatePhasesAllocationTable();
		updateStatisticsTable();
		updateCO2PerMinuteLabel();
		updateCO2EstimateLabel();
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
	
}
