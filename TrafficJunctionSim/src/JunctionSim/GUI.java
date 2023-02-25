/**
 * 
 */
package JunctionSim;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;


public class GUI extends JFrame implements ActionListener {
	
	//TODO: Unsure of access modifiers for these atm
	Vehicles vehicles;
	Phases phases;
	
	public static void main(String[] args) {
		//Ran before constructor
		GUI gui = new GUI();
	}
	
	private GUI(){
		//Initialisation of required classes
		this.vehicles = new Vehicles();
		this.phases = new Phases();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
