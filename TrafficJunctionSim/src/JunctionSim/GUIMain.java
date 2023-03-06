package JunctionSim;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
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
import javax.swing.JButton;

public class GUIMain extends JFrame implements ActionListener{

	private JPanel contentPane;
	private JTable tableVehicles;
	
	Vehicles vehicles;
	Phases phases;
	private JTable tablePhases;
	private JTable tablePhaseAllocation;
	private JTable tableStatistics;
	private JTable tableAddVehicle;

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
		setTitle("Traffic Junction Simulator");
		
		//Initialisation of required classes
		this.vehicles = new Vehicles();
		this.phases = new Phases(this.vehicles);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1227, 694);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblVehicles = new JLabel("Vehicles");
		lblVehicles.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblVehicles.setBounds(208, 11, 68, 29);
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
		tableVehicles.setFillsViewportHeight(true);
		scrollPaneVehicles.setViewportView(tableVehicles);
		
		JLabel lblPhases = new JLabel("Phases Setup");
		lblPhases.setBounds(778, 20, 75, 14);
		contentPane.add(lblPhases);
		
		JScrollPane scrollPanePhases = new JScrollPane();
		scrollPanePhases.setBounds(714, 48, 211, 295);
		contentPane.add(scrollPanePhases);
		
		//Get header for phases table
		String[] phasesHeader = this.phases.getPhaseHeaders();
		//Get content for phases table
		Object[][] phasesContent = this.phases.getPhases2DObjectArray();
		tablePhases = new JTable(phasesContent, phasesHeader);
		tablePhases.setFillsViewportHeight(true);
		scrollPanePhases.setViewportView(tablePhases);
		
		JLabel lblPhaseVehicles = new JLabel("Vehicles Phase Allocation");
		lblPhaseVehicles.setBounds(986, 20, 167, 14);
		contentPane.add(lblPhaseVehicles);
		
		JScrollPane scrollPanePhaseAllocation = new JScrollPane();
		scrollPanePhaseAllocation.setBounds(969, 48, 232, 294);
		contentPane.add(scrollPanePhaseAllocation);
		
		//Get header for phase allocation table
		String[] phaseAllocationHeader = {"Phase", "Vehicle"};
		//Get content for phase allocation table
		Object[][] phaseAllocationContent = this.phases.getPhasesVehicles2DObjectArray();
		//***************************FOR SOME REASON THIS NEEDS CHANGED OR DESIGNER WILL NOT WORK ON ECLIPSE*******************************************
		//tablePhaseAllocation = new JTable(phaseAllocationContent, phaseAllocationHeader);
		tablePhaseAllocation = new JTable();
		tablePhaseAllocation.setFillsViewportHeight(true);
		scrollPanePhaseAllocation.setViewportView(tablePhaseAllocation);
		
		JLabel lblStatistics = new JLabel("Statistics");
		lblStatistics.setBounds(929, 363, 87, 14);
		contentPane.add(lblStatistics);
		
		JScrollPane scrollPaneStatistics = new JScrollPane();
		scrollPaneStatistics.setBounds(714, 390, 487, 128);
		contentPane.add(scrollPaneStatistics);
		
		//Get header for statistics table
		String[] statisticsHeader = {"Segment", "Waiting Time", "Total Length", "Avg Cross Time", "Number of Vehicles"};
		//Get content for statistics table
		Object[][] statisticsContent = this.vehicles.getSegmentStatistics();
		//tableStatistics = new JTable(statisticsContent, statisticsHeader);
		tableStatistics = new JTable();
		tableStatistics.setFillsViewportHeight(true);
		scrollPaneStatistics.setViewportView(tableStatistics);
		
		JComboBox comboBoxVehicleSort = new JComboBox();
		comboBoxVehicleSort.setModel(new DefaultComboBoxModel(new String[] {"ID", "Type", "Segment"}));
		comboBoxVehicleSort.setBounds(586, 363, 87, 22);
		contentPane.add(comboBoxVehicleSort);
		
		JLabel lblSortBy = new JLabel("Sort By:");
		lblSortBy.setBounds(530, 367, 46, 14);
		contentPane.add(lblSortBy);
		
		JLabel lblAddVehicle = new JLabel("Add Vehicle");
		lblAddVehicle.setBounds(242, 491, 75, 14);
		contentPane.add(lblAddVehicle);
		
		JScrollPane scrollPaneAddVehicle = new JScrollPane();
		scrollPaneAddVehicle.setBounds(10, 530, 766, 58);
		contentPane.add(scrollPaneAddVehicle);
		
		tableAddVehicle = new JTable();
		tableAddVehicle.setFillsViewportHeight(true);
		tableAddVehicle.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null, null, null},
			},
			new String[] {
				"Vehicle ID", "Type", "Crossing Time", "Direction", "Length", "Emission", "Status", "Segment"
			}
		));
		tableAddVehicle.setRowHeight(30);
		scrollPaneAddVehicle.setViewportView(tableAddVehicle);
		
		JButton btnAddVehicle = new JButton("Add Vehicle");
		btnAddVehicle.setBounds(10, 609, 125, 23);
		contentPane.add(btnAddVehicle);
		btnAddVehicle.addActionListener(this);
		btnAddVehicle.setActionCommand("Add Vehicle");
		
		JButton btnClearVehicleInput = new JButton("Clear Input");
		btnClearVehicleInput.setBounds(220, 609, 125, 23);
		contentPane.add(btnClearVehicleInput);
		btnClearVehicleInput.addActionListener(this);
		btnClearVehicleInput.setActionCommand("Clear Input");
		
		JButton btnExit = new JButton("Exit");
		btnExit.setBounds(1076, 609, 125, 23);
		contentPane.add(btnExit);
		btnExit.addActionListener(this);
		btnExit.setActionCommand("Exit");
		
		JLabel lblCO2 = new JLabel("C02: " + this.vehicles.getTotalCO2());
		lblCO2.setBounds(10, 430, 139, 14);
		contentPane.add(lblCO2);
		
		JLabel lblSwithPhaseMode = new JLabel("Phase Mode:");
		lblSwithPhaseMode.setBounds(10, 371, 109, 14);
		contentPane.add(lblSwithPhaseMode);
		
		JComboBox comboBoxPhaseMode = new JComboBox();
		comboBoxPhaseMode.setModel(new DefaultComboBoxModel(new String[] {"8 Phase", "4 Phase"}));
		comboBoxPhaseMode.setBounds(86, 367, 87, 22);
		contentPane.add(comboBoxPhaseMode);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (action.equals("Add Vehicle")) {
			System.out.println("Adding Vehicle");
		}
		if (action.equals("Clear Input")) {
			System.out.println("Clearing Input");
			clearInput();
		}
		if (action.equals("Exit")) {
			System.out.println("Exiting");
		}
	}
	
	private void clearInput() {
		int i = 0;
		while(i <= 7) {
			this.tableAddVehicle.setValueAt(null, 0, i);
			i++;
		}
	}
	
	private void addVehicle() {
		String[] vehicleParams = new String[8];
		int i = 0;
		while(i <= 7) {
			vehicleParams[i] = null;
			i++;
		}
		this.tableAddVehicle.getValueAt(0,0);
	}
}