package controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import model.Phase;
import model.Phases;
import view.GUIMain;

//Notifies main controller of changes

public class TrafficController implements Runnable{
		private GUIMainController controller;
		
		//Segments as linked list
		private LinkedList<Object[][]> segments;
		//Active phases
		private String activePhaseOne;
		private float activePhaseOneDuration;
		private String activePhaseTwo;
		private float activePhaseTwoDuration;
		private Phases phases;
		//Index of active segment
		private int segmentIndex;
		//TrafficController active boolean
		private boolean trafficControllerActive;
		
		ScheduledExecutorService schedulerp1;
		ScheduledExecutorService schedulerp2;
		
		//Constructor
		public TrafficController(Phases phases, GUIMainController controller) {
			this.controller = controller;
			this.phases = phases;
			this.segments = createSegmentList(phases.getPhasesHmap());
			segmentIndex = 0;
			updateActivePhaseInfo();
			trafficControllerActive = true;
		}
		
		//Method bundles phases into 2d array and creates a linked list
		//Each 2d array in this linked list will represent a segment
		//Takes the HMAP of phase items
		private LinkedList<Object[][]> createSegmentList(HashMap<String, Phase> phasesHMap) {
			//Converts HMAP phases to segments - 2d array containing phase names and durations
			//This is the linked list to be iterated over
			LinkedList<Object[][]> segments = new LinkedList<Object[][]>();
			String[] phaseOrder = {"P6", "P1", "P4", "P7", "P2", "P5", "P8", "P3"};
			//Get phase durations
			float[] phaseDurations = new float[8];
			for (Map.Entry<String, Phase> entry : phasesHMap.entrySet()) {
				String phaseName = entry.getKey();
				float phaseDuration = entry.getValue().getPhaseDuration();
				int phaseOrderIndex = Arrays.asList(phaseOrder).indexOf(phaseName);
				phaseDurations[phaseOrderIndex] = phaseDuration;
			}
			int i = 0;
			Object[][] segment = new Object[2][2];
			while (i <= 7) {
				
			}
			return segments;
		}
		
		private void updateSegmentIndex() {
			if (segmentIndex >= segments.size() -1) {
				segmentIndex = 0;
			}
			else {
				segmentIndex++;
			}
		}
		
		private void updateActivePhaseInfo() {
			Object[][] segment = segments.get(segmentIndex);
			this.activePhaseOne = (String)segment[0][0];
			this.activePhaseOneDuration = (float)segment[0][1];
			this.activePhaseTwo = (String)segment[1][0];
			this.activePhaseTwoDuration = (float)segment[1][1];
		}
		
		public void phaseLights() {
			notifyController();
			//Get phase durations
			float p1Duration = this.activePhaseOneDuration;
			long p1DurationMilli = (long)(p1Duration * 1000);
			float p2Duration = this.activePhaseTwoDuration;
			long p2DurationMilli = (long)(p2Duration * 1000);

			AtomicBoolean p1Active = new AtomicBoolean(true);
			schedulerp1 = Executors.newScheduledThreadPool(1);
			schedulerp1.schedule(() -> p1Active.set(false), p1DurationMilli, TimeUnit.MILLISECONDS);
			
			AtomicBoolean p2Active = new AtomicBoolean(true);
			schedulerp2 = Executors.newScheduledThreadPool(1);
			schedulerp1.schedule(() -> p2Active.set(false), p2DurationMilli, TimeUnit.MILLISECONDS);
			
			boolean p1Notified = false;
			boolean p2Notified = false;
			while(true) {
				if(!p1Active.get()) {
					if (!p1Notified) {
						p1Notified = true;
						notifyVehicles(activePhaseOne);
						this.activePhaseOne = null;
						notifyController();
					}
				}
				if(!p2Active.get()) {
					if(!p2Notified) {
						p2Notified = true;
						notifyVehicles(activePhaseTwo);
						this.activePhaseTwo = null;
						notifyController();
					}
				}
				if(!p1Active.get() && !p2Active.get()) {
					break;
				}
				try {
	                Thread.sleep(200);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
			}

	        schedulerp1.shutdown();
	        schedulerp2.shutdown();
	        System.out.println("CHANGING SEGMENT");
		}
		
		public void iterateSegments() {
			while (trafficControllerActive) {
				notifyVehicles(activePhaseOne);
				notifyVehicles(activePhaseTwo);
				phaseLights();
				updateSegmentIndex();
				updateActivePhaseInfo();
			}
		}
		
		private void notifyVehicles(String phase) {
			//phases.notifyVehicles(phase);
			System.out.println(activePhaseOne);
			System.out.println(activePhaseTwo);
		}
		
		public String[] getActivePhases() {
			String[] activePhases = {this.activePhaseOne, this.activePhaseTwo};
			return activePhases;
		}
		
		private void notifyController() {
			controller.trafficControllerUpdated();
		}
		
		public void endSimulation() {
			trafficControllerActive = false;
			schedulerp1.shutdown();
			schedulerp2.shutdown();
		}

		@Override
		public void run() {
			iterateSegments();
		}
}
