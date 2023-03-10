package JunctionSim;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Color;
import javax.swing.border.LineBorder;
import javax.swing.ScrollPaneConstants;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class GUIMain extends JFrame implements ActionListener{
	
	//Must be public and accessible for some testing functions
	public static String vehiclesCSVFile = "vehicles.csv";
	public static String intersectionCSVFile = "intersection.csv";

	private JPanel contentPane;
	private JTable tableVehicles;
	
	Vehicles vehicles;
	Phases phases;
	private JTable tablePhases;
	private JTable tablePhaseAllocation;
	private JTable tableStatistics;
	private JTable tableAddVehicle;
	
	private JLabel lblCO2PerMinute;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIMain frame = new GUIMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUIMain() {
		setResizable(false);
		
		setTitle("Traffic Junction Simulator");
		
		//Initialisation of required classes
		this.vehicles = new Vehicles(vehiclesCSVFile);
		this.phases = new Phases(this.vehicles, intersectionCSVFile);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1227, 694);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblVehicles = new JLabel("Vehicles");
		lblVehicles.setHorizontalAlignment(SwingConstants.CENTER);
		lblVehicles.setFont(new Font("Verdana", Font.BOLD, 15));
		lblVehicles.setBounds(10, 11, 663, 29);
		contentPane.add(lblVehicles);
		
		JScrollPane scrollPaneVehicles = new JScrollPane();
		scrollPaneVehicles.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneVehicles.setBounds(10, 48, 663, 295);
		contentPane.add(scrollPaneVehicles);
		
		// Get header for vehicles table
		String[] vehiclesHeader = this.vehicles.getVehicleHeaders();
		// Get content for vehicles table
		Object[][] vehiclesContents = this.vehicles.getVehicles2DArray();
		tableVehicles = new JTable(vehiclesContents, vehiclesHeader);
		tableVehicles.setEnabled(false);
		tableVehicles.setFillsViewportHeight(true);
		scrollPaneVehicles.setViewportView(tableVehicles);
		
		JLabel lblPhases = new JLabel("Phases Setup");
		lblPhases.setHorizontalAlignment(SwingConstants.CENTER);
		lblPhases.setFont(new Font("Verdana", Font.BOLD, 15));
		lblPhases.setBounds(714, 18, 211, 14);
		contentPane.add(lblPhases);
		
		JScrollPane scrollPanePhases = new JScrollPane();
		scrollPanePhases.setBounds(714, 48, 211, 295);
		contentPane.add(scrollPanePhases);
		
		//Get header for phases table
		String[] phasesHeader = this.phases.getPhaseHeaders();
		//Get content for phases table
		Object[][] phasesContent = this.phases.getPhases2DObjectArray();
		tablePhases = new JTable(phasesContent, phasesHeader);
		tablePhases.setEnabled(false);
		tablePhases.setFillsViewportHeight(true);
		scrollPanePhases.setViewportView(tablePhases);
		
		JLabel lblPhaseVehicles = new JLabel("Vehicles Phase Allocation");
		lblPhaseVehicles.setHorizontalAlignment(SwingConstants.CENTER);
		lblPhaseVehicles.setFont(new Font("Verdana", Font.BOLD, 15));
		lblPhaseVehicles.setBounds(969, 20, 232, 14);
		contentPane.add(lblPhaseVehicles);
		
		JScrollPane scrollPanePhaseAllocation = new JScrollPane();
		scrollPanePhaseAllocation.setBounds(969, 48, 232, 294);
		contentPane.add(scrollPanePhaseAllocation);
		
		//Get header for phase allocation table
		String[] phaseAllocationHeader = {"Phase", "Vehicle"};
		//Get content for phase allocation table
		Object[][] phaseAllocationContent = this.phases.getPhasesVehicles2DObjectArray();
		//***************************FOR SOME REASON THIS NEEDS CHANGED OR DESIGNER WILL NOT WORK ON ECLIPSE*******************************************
		tablePhaseAllocation = new JTable(phaseAllocationContent, phaseAllocationHeader);
		//tablePhaseAllocation = new JTable();
		tablePhaseAllocation.setEnabled(false);
		tablePhaseAllocation.setFillsViewportHeight(true);
		scrollPanePhaseAllocation.setViewportView(tablePhaseAllocation);
		
		JLabel lblStatistics = new JLabel("Statistics");
		lblStatistics.setFont(new Font("Verdana", Font.BOLD, 15));
		lblStatistics.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatistics.setBounds(714, 365, 487, 14);
		contentPane.add(lblStatistics);
		
		JScrollPane scrollPaneStatistics = new JScrollPane();
		scrollPaneStatistics.setBounds(714, 390, 487, 128);
		contentPane.add(scrollPaneStatistics);
		
		//Get header for statistics table
		String[] statisticsHeader = {"Segment", "Total Length", "Avg Cross Time", "Number of Vehicles"};
		//Get content for statistics table
		Object[][] statisticsContent = this.vehicles.getSegmentStatistics();
		tableStatistics = new JTable(statisticsContent, statisticsHeader);
		//tableStatistics = new JTable();
		tableStatistics.setEnabled(false);
		tableStatistics.setFillsViewportHeight(true);
		scrollPaneStatistics.setViewportView(tableStatistics);
		
		JComboBox comboBoxVehicleSort = new JComboBox();
		comboBoxVehicleSort.setFont(new Font("Verdana", Font.PLAIN, 13));
		comboBoxVehicleSort.setModel(new DefaultComboBoxModel(new String[] {"ID", "Type", "Segment"}));
		comboBoxVehicleSort.setBounds(586, 363, 87, 22);
		contentPane.add(comboBoxVehicleSort);
		
		JLabel lblSortBy = new JLabel("Sort By:");
		lblSortBy.setFont(new Font("Verdana", Font.PLAIN, 13));
		lblSortBy.setBounds(508, 362, 81, 20);
		contentPane.add(lblSortBy);
		
		JLabel lblAddVehicle = new JLabel("Add Vehicle");
		lblAddVehicle.setHorizontalAlignment(SwingConstants.CENTER);
		lblAddVehicle.setFont(new Font("Verdana", Font.BOLD, 15));
		lblAddVehicle.setBounds(10, 508, 766, 14);
		contentPane.add(lblAddVehicle);
		
		JScrollPane scrollPaneAddVehicle = new JScrollPane();
		scrollPaneAddVehicle.setBounds(10, 530, 766, 58);
		contentPane.add(scrollPaneAddVehicle);
		
		tableAddVehicle = new JTable();
		tableAddVehicle.setFont(new Font("Verdana", Font.PLAIN, 13));
		tableAddVehicle.setFillsViewportHeight(true);
		tableAddVehicle.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null, null, null},
			},
			new String[] {
				"Vehicle ID", "Type", "Crossing Time", "Direction", "Length", "Emission", "Status", "Segment"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, Object.class, String.class, String.class, String.class, String.class, String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		tableAddVehicle.setRowHeight(30);
		scrollPaneAddVehicle.setViewportView(tableAddVehicle);
		
		JButton btnAddVehicle = new JButton("Add Vehicle");
		btnAddVehicle.setFont(new Font("Verdana", Font.PLAIN, 13));
		btnAddVehicle.setBounds(10, 609, 125, 23);
		contentPane.add(btnAddVehicle);
		btnAddVehicle.addActionListener(this);
		btnAddVehicle.setActionCommand("Add Vehicle");
		
		JButton btnClearVehicleInput = new JButton("Clear Input");
		btnClearVehicleInput.setFont(new Font("Verdana", Font.PLAIN, 13));
		btnClearVehicleInput.setBounds(220, 609, 125, 23);
		contentPane.add(btnClearVehicleInput);
		btnClearVehicleInput.addActionListener(this);
		btnClearVehicleInput.setActionCommand("Clear Input");
		
		JButton btnExit = new JButton("Exit");
		btnExit.setFont(new Font("Verdana", Font.PLAIN, 13));
		btnExit.setBounds(1076, 609, 125, 23);
		contentPane.add(btnExit);
		btnExit.addActionListener(this);
		btnExit.setActionCommand("Exit");
		
		lblCO2PerMinute = new JLabel("C02: " + this.vehicles.getTotalCO2PerMinute());
		lblCO2PerMinute.setFont(new Font("Verdana", Font.PLAIN, 13));
		lblCO2PerMinute.setBounds(10, 381, 338, 22);
		contentPane.add(lblCO2PerMinute);
		
		JLabel lblC02Estimate = new JLabel("Estimated Total C02 During Simulation: " + phases.getTotalCO2Estimate());
		lblC02Estimate.setFont(new Font("Verdana", Font.PLAIN, 13));
		lblC02Estimate.setBounds(10, 426, 359, 29);
		contentPane.add(lblC02Estimate);
	}

	//Method handles action events on the GUI
	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (action.equals("Add Vehicle")) {
			try {
				addVehicle();
				System.out.println("Sucesfully added vehicle");
				JOptionPane.showMessageDialog(null, "Sucesfully added vehicle");
			}
			catch(IllegalArgumentException ae){
				System.out.println("Vehicle could not be added");
				System.out.println(ae.getMessage());
				JOptionPane.showMessageDialog(this, ae.getMessage(),
			               "Could Not Add Vehicle", JOptionPane.ERROR_MESSAGE);
			}
			catch(DuplicateVehicleIDException dke) {
				System.out.println("Vehicle could not be added");
				System.out.println(dke.getMessage());
				JOptionPane.showMessageDialog(this, dke.getMessage(),
			               "Could Not Add Vehicle", JOptionPane.ERROR_MESSAGE);
			}
		}
		if (action.equals("Clear Input")) {
			System.out.println("Clearing Input");
			clearInput();
		}
		if (action.equals("Exit")) {
			System.out.println("Exiting");
			exitFunction();
		}
	}
	
	//Clear the JTable for user input
	private void clearInput() {
		int i = 0;
		while(i <= 7) {
			this.tableAddVehicle.setValueAt(null, 0, i);
			i++;
		}
	}
	
	//Takes vehicles params from the user input JTable
	//Can throw duplicate vehicle ID or Illegal Vehicle Constructor Argument Exceptions
	private void addVehicle() throws DuplicateVehicleIDException {
		String[] vehicleParams = new String[8];
		int i = 0;
		while(i <= 7) {
			vehicleParams[i] = (String)this.tableAddVehicle.getValueAt(0,i);
			i++;
		}
		
		//Build vehicle from input params and insert to hash map and queue
		try {
			//Build vehicle
			Vehicle vehicle = this.vehicles.buildVehicle(vehicleParams);
			
			try {
				//Add vehicle to hash map
				this.vehicles.insertVehicleHashMap(vehicle);
				
				//Add vehicle to queue if in waiting state
				if(vehicle.getStatus().equals("Waiting")) {
					this.phases.insertVehicleQueue(vehicle);
				}
				
				//Refresh GUI values
				refreshGUIValues();
			}
			catch(DuplicateVehicleIDException dke) {
				throw dke;
			}
		}
		catch(IllegalArgumentException ae) {
			throw ae;
		}
	}
	
	//Method responsible for reloading relevant GUI components on update
	//Build table models which are then used to update the JTables
	private void refreshGUIValues() {
		//Refresh tables - Vehicle table
		// Get header for vehicles table
		String[] vehiclesHeader = this.vehicles.getVehicleHeaders();
		// Get content for vehicles table
		Object[][] vehiclesContents = this.vehicles.getVehicles2DArray();
		TableModel vehiclesTableModel = new DefaultTableModel(vehiclesContents, vehiclesHeader);
		this.tableVehicles.setModel(vehiclesTableModel);
				
		//Refresh tables - Vehicles Phase Allocation Table
		//Get header for phase allocation table
		String[] phaseAllocationHeader = {"Phase", "Vehicle"};
		//Get content for phase allocation table
		Object[][] phaseAllocationContent = this.phases.getPhasesVehicles2DObjectArray();
		TableModel phaseAllocationTableModel = new DefaultTableModel(phaseAllocationContent, phaseAllocationHeader);
		this.tablePhaseAllocation.setModel(phaseAllocationTableModel);
				
		//Refresh tables - Statistic Table
		//Get header for statistics table
		String[] statisticsHeader = {"Segment", "Total Length", "Avg Cross Time", "Number of Vehicles"};
		//Get content for statistics table
		Object[][] statisticsContent = this.vehicles.getSegmentStatistics();
		TableModel statisticsTableModel = new DefaultTableModel(statisticsContent, statisticsHeader);
		this.tableStatistics.setModel(statisticsTableModel);
		
		//Update CO2 statistic label - unsure If we should do this? - is this lowered as vehicles pass?
		//this.lblCO2.setText("C02: " + this.vehicles.getTotalCO2());
	}
	
	//Function ran on click of JButton exit
	//Here is responsible for building the main content of the report via a string array
	//This is static passed to ProduceReport which handles writing to the report file
	private void exitFunction() {
		int[] vehiclesCrossedCounts = this.vehicles.getVehiclesCrossedCounts();
		float[] averageSegmentWaitTimes = this.phases.getAverageSegmentWaitingTimes();
		Object[][] vehicleStatistics = this.vehicles.getSegmentStatistics();
		String[] lines = new String[5];
		lines[0] = "Segment 1:-	Vehicles Crossed:" + vehiclesCrossedCounts[0] + " average waiting time: " + averageSegmentWaitTimes[0];
		lines[1] = "Segment 2:-	Vehicles Crossed:" + vehiclesCrossedCounts[1] + " average waiting time: " + averageSegmentWaitTimes[1];
		lines[2] = "Segment 3:-	Vehicles Crossed:" + vehiclesCrossedCounts[2] + " average waiting time: " + averageSegmentWaitTimes[2];
		lines[3] = "Segment 4:-	Vehicles Crossed:" + vehiclesCrossedCounts[3] + " average waiting time: " + averageSegmentWaitTimes[3];
		lines[4] = "Total emissions: " + this.vehicles.getTotalCO2PerMinute();
		ProduceReport.createReport(lines);
		System.exit(0);
	}
}
