/**
 * 
 */
package JunctionSim;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class GUI extends JFrame implements ActionListener {
	
	private JFrame frame;
	private final int frameWidth = 1400;
	private final int frameHeight = 800;
	
	//TODO: Unsure of access modifiers for these atm
	Vehicles vehicles;
	Phases phases;
	
	private GUI(){
		//Initialisation of required classes
		this.vehicles = new Vehicles();
		this.phases = new Phases(this.vehicles);
		
		//Create frame and frame settings
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(frameWidth,frameHeight);
		frame.setResizable(false);
		frame.setTitle("Road Intersection Simulator");
		frame.setLayout(new BorderLayout());
		
		//Create panels to segment frame for easier GUI editing
		JPanel upperLeftPanel = new JPanel();
		JPanel upperRightPanel = new JPanel();
		JPanel lowerPanel = new JPanel();
		
		//******************************Upper left panel******************************
		upperLeftPanel.setBackground(Color.red);
		upperLeftPanel.setPreferredSize(new Dimension((int)(frameWidth*0.5),(int)(frameHeight*0.8)));
		upperLeftPanel.setLayout(new BorderLayout());
		
		//*****************************Upper right panel******************************
		upperRightPanel.setBackground(Color.blue);
		upperRightPanel.setPreferredSize(new Dimension((int)(frameWidth*0.5),(int)(frameHeight*0.8)));
		upperRightPanel.setLayout(new BorderLayout());
		
		//*****************************Lower panel******************************
		lowerPanel.setBackground(Color.yellow);
		lowerPanel.setPreferredSize(new Dimension((int)(frameWidth*0.5),(int)(frameHeight*0.2)));
		lowerPanel.setLayout(new BorderLayout());

		
		//Set frame visible
		frame.add(upperLeftPanel, BorderLayout.WEST);
		frame.add(upperRightPanel, BorderLayout.EAST);
		frame.add(lowerPanel, BorderLayout.SOUTH);
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		//Ran before constructor
		GUI gui = new GUI();
	}

}
