package view;

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
import controller.GUIMainController;

 
/**
 * @author Daniel Denley
 *
 */
public class GUIMain extends JFrame{
	
	//The view needs to know about the controller in this instance
	//Only to call the update GUI methods
	public GUIMainController controller;
	
	private JButton btnExit;
	private JButton btnAddVehicle;
	private JButton btnClearVehicleInput;
	private JButton btnStart;
	
	private JPanel contentPane;
	private JTable tableVehicles;
	private JTable tablePhases;
	private JTable tablePhaseAllocation;
	private JTable tableStatistics;
	private JTable tableAddVehicle;
	private JTable tableActivePhases;
	
	private JLabel lblCO2PerMinute;

	/**
	 * Create the frame.
	 */
	public GUIMain() {
		setResizable(false);
		
		setTitle("Traffic Junction Simulator MVC");
		
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
		
		tableVehicles = new JTable();
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
		
		tablePhases = new JTable();
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
		
		tablePhaseAllocation = new JTable();
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
		
		tableStatistics = new JTable();
		tableStatistics.setEnabled(false);
		tableStatistics.setFillsViewportHeight(true);
		scrollPaneStatistics.setViewportView(tableStatistics);
		
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
		
		btnAddVehicle = new JButton("Add Vehicle");
		btnAddVehicle.setFont(new Font("Verdana", Font.PLAIN, 13));
		btnAddVehicle.setBounds(10, 609, 125, 23);
		contentPane.add(btnAddVehicle);
		
		btnClearVehicleInput = new JButton("Clear Input");
		btnClearVehicleInput.setFont(new Font("Verdana", Font.PLAIN, 13));
		btnClearVehicleInput.setBounds(220, 609, 125, 23);
		contentPane.add(btnClearVehicleInput);
		
		btnExit = new JButton("Exit");
		btnExit.setFont(new Font("Verdana", Font.PLAIN, 13));
		btnExit.setBounds(1076, 609, 125, 23);
		contentPane.add(btnExit);
		
		lblCO2PerMinute = new JLabel();
		lblCO2PerMinute.setFont(new Font("Verdana", Font.PLAIN, 13));
		lblCO2PerMinute.setBounds(10, 381, 338, 22);
		contentPane.add(lblCO2PerMinute);
		
		btnStart = new JButton("Start");
		btnStart.setBackground(new Color(144, 238, 144));
		btnStart.setBounds(822, 597, 103, 35);
		contentPane.add(btnStart);
		
		JLabel lblActivePhases = new JLabel("Active Phases");
		lblActivePhases.setHorizontalAlignment(SwingConstants.CENTER);
		lblActivePhases.setFont(new Font("Verdana", Font.BOLD, 15));
		lblActivePhases.setBounds(512, 365, 171, 14);
		contentPane.add(lblActivePhases);
		
		tableActivePhases = new JTable();
		tableActivePhases.setFillsViewportHeight(true);
		tableActivePhases.setEnabled(false);
		tableActivePhases.setBounds(540, 391, 114, 121);
		contentPane.add(tableActivePhases);
		
		btnExit.setActionCommand("btnExit");
		btnAddVehicle.setActionCommand("btnAddVehicle");
		btnClearVehicleInput.setActionCommand("btnClearVehicleInput");
		btnStart.setActionCommand("btnStart");
		
		
	}
	
	//Getter to allow controller to get user input
	public String[] getVehicleInput() {
		String[] vehicleParams = new String[8];
		int i = 0;
		while(i <= 7) {
			vehicleParams[i] = (String)this.tableAddVehicle.getValueAt(0,i);
			i++;
		}
		return vehicleParams;
	}
	
	//Setter methods for tables
	public void setTableVehicles(String[] header, Object[][] vehiclesContents) {
		TableModel vehiclesTableModel = new DefaultTableModel(vehiclesContents, header);
		this.tableVehicles.setModel(vehiclesTableModel);
	}
	public void setTablePhases(String[] header, Object[][] phasesContents) {
		TableModel phasesTableModel = new DefaultTableModel(phasesContents, header);
		this.tablePhases.setModel(phasesTableModel);
	}
	public void setTablePhaseAllocation(String[] header, Object[][] phaseAllocationContents) {
		TableModel phaseAllocationTableModel = new DefaultTableModel(phaseAllocationContents, header);
		this.tablePhaseAllocation.setModel(phaseAllocationTableModel);
	}
	public void setTableStatistics(String[] header, Object[][] statisticsContent) {
		TableModel phaseStatisticsTableModel = new DefaultTableModel(statisticsContent, header);
		this.tableStatistics.setModel(phaseStatisticsTableModel);
	}
	public void setTableActivePhases(Object[][] activePhasesContent) {
		String[] header = {"Phases"};
		TableModel activePhasesTableModel = new DefaultTableModel(activePhasesContent, header);
		this.tableActivePhases.setModel(activePhasesTableModel);
	}
	public void setLblCO2PerMinute(String co2PerMin) {
		lblCO2PerMinute.setText(co2PerMin);
	}
	public void setTableAddVehicleToEmpty() {
		int i = 0;
		while(i <= 7) {
			this.tableAddVehicle.setValueAt(null, 0, i);
			i++;
		}
	}
	
	//Used to add listeners
	public void addBtnExitListener(ActionListener al) {
		btnExit.addActionListener(al);
	}
	public void addBtnAddVehicleListener(ActionListener al) {
		btnAddVehicle.addActionListener(al);
	}
	public void addBtnClearVehicleInputListener(ActionListener al) {
		btnClearVehicleInput.addActionListener(al);
	}
	public void addBtnStartListener(ActionListener al) {
		btnStart.addActionListener(al);
	}
	
	
	//Use controller to update GUI
	public void modelUpdated() {
		controller.updateGUI();
	}
}
