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
import utility.Logger;
import view.GUIMain;

//Notifies main controller of changes
//Upon an active phase being set that phases vehicles are notified
//On notification vehicles are toggled from wait to running state

public class TrafficController implements Runnable{
		private GUIMainController controller;
		
		//Segments as linked list
		private LinkedList<Object[][]> segments;
		//Active phases variables
		private String activePhaseOne;
		private String phaseOneName;
		private float activePhaseOneDuration;
		private String activePhaseTwo;
		private String phaseTwoName;
		private float activePhaseTwoDuration;
		private Phases phases;
		//Index of active segment
		private int segmentIndex;
		//TrafficController active boolean
		private boolean trafficControllerActive;
		
		//https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ScheduledExecutorService.html#:~:text=An%20ExecutorService%20that%20can%20schedule,to%20cancel%20or%20check%20execution.
		//An ExecutorService that can schedule commands to run after a given delay
		ScheduledExecutorService schedulerp1;
		ScheduledExecutorService schedulerp1yellow;
		ScheduledExecutorService schedulerp1red;
		ScheduledExecutorService schedulerp2;
		ScheduledExecutorService schedulerp2yellow;
		ScheduledExecutorService schedulerp2red;
		
		//Light periods - yellow is signalled to notify upcoming red light
		//Red light signifies a phase is inactive
		private final long yellowLightsSeconds = 10;
		private final long redLightsSeconds = 10;
		private String lightsStatusP1;
		private String lightsStatusP2;
		
		//Constructor
		public TrafficController(Phases phases, GUIMainController controller) {
			this.controller = controller;
			this.phases = phases;
			this.segments = createSegmentList(phases.getPhasesHmap());
			this.segmentIndex = 0;
			this.lightsStatusP1 = "Green";
			this.lightsStatusP2 = "Green";
			updateActivePhaseInfo();
			this.trafficControllerActive = true;
		}
		
		public String getActiveSegment() {
			return String.valueOf(segmentIndex + 1) + "S";
		}
		
		public String[] getLightsStatus() {
			return new String[] {this.lightsStatusP1, this.lightsStatusP2};
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
			int j = 0;
			while (i < phaseOrder.length) {
				segment[j][0] = phaseOrder[i];
				segment[j][1] = phaseDurations[i];
				j++;
				if (j != 0) {
					if((j % 2) == 0) {
						j = 0;
						segments.add(segment);
						segment = new Object[2][2];
					}
				}
				i++;
			}
			return segments;
		}
		
		//Updates the active segment and loops through them - 4 segments
		private void updateSegmentIndex() {
			if (segmentIndex >= segments.size() -1) {
				segmentIndex = 0;
			}
			else {
				segmentIndex++;
			}
		}
		
		//This looks at the segment and turns active the assoicated phases/lanes
		private void updateActivePhaseInfo() {
			Object[][] segment = segments.get(segmentIndex);
			this.activePhaseOne = (String)segment[0][0];
			this.phaseOneName = (String)segment[0][0];
			this.activePhaseOneDuration = (float)segment[0][1];
			this.activePhaseTwo = (String)segment[1][0];
			this.phaseTwoName = (String)segment[1][0];
			this.activePhaseTwoDuration = (float)segment[1][1];
		}
		
		public void phaseLights() {
			lightsStatusP1 = "Green";
			Logger.getInstance().log(phaseOneName + " changed lights to " + lightsStatusP1);
			lightsStatusP2 = "Green";
			Logger.getInstance().log(phaseTwoName + " changed lights to " + lightsStatusP2);
			notifyController();
			//Get phase durations
			float p1Duration = this.activePhaseOneDuration;
			long p1DurationMilli = (long)(p1Duration * 1000);
			float p2Duration = this.activePhaseTwoDuration;
			long p2DurationMilli = (long)(p2Duration * 1000);

			AtomicBoolean p1Active = new AtomicBoolean(true);
			schedulerp1 = Executors.newScheduledThreadPool(1);
			schedulerp1.schedule(() -> p1Active.set(false), p1DurationMilli, TimeUnit.MILLISECONDS);
			
			AtomicBoolean p1Yellow = new AtomicBoolean(true);
			schedulerp1yellow = Executors.newScheduledThreadPool(1);
			schedulerp1yellow.schedule(() -> p1Yellow.set(false), p1DurationMilli - (yellowLightsSeconds * 1000), TimeUnit.MILLISECONDS);
			
			AtomicBoolean p1Red = new AtomicBoolean(true);
			schedulerp1red = Executors.newScheduledThreadPool(1);
			schedulerp1red.schedule(() -> p1Red.set(false), p1DurationMilli, TimeUnit.MILLISECONDS);
			
			AtomicBoolean p2Active = new AtomicBoolean(true);
			schedulerp2 = Executors.newScheduledThreadPool(1);
			schedulerp2.schedule(() -> p2Active.set(false), p2DurationMilli, TimeUnit.MILLISECONDS);
			
			AtomicBoolean p2Yellow = new AtomicBoolean(true);
			schedulerp2yellow = Executors.newScheduledThreadPool(1);
			schedulerp2yellow.schedule(() -> p2Yellow.set(false), p2DurationMilli - (yellowLightsSeconds * 1000), TimeUnit.MILLISECONDS);
			
			AtomicBoolean p2Red = new AtomicBoolean(true);
			schedulerp2red = Executors.newScheduledThreadPool(1);
			schedulerp2red.schedule(() -> p2Red.set(false), p2DurationMilli, TimeUnit.MILLISECONDS);
			
			boolean p1Notified = false;
			boolean p2Notified = false;
			while (true) {
			    if (!p1Yellow.get() && lightsStatusP1.equals("Green")) {
			        lightsStatusP1 = "Yellow";
			        Logger.getInstance().log(phaseOneName + " changed lights to " + lightsStatusP1);
			        notifyController();
			    }
			    if (!p1Red.get() && lightsStatusP1.equals("Yellow")) {
			        lightsStatusP1 = "Red";
			        Logger.getInstance().log(phaseOneName + " changed lights to " + lightsStatusP1);
			        notifyController();
			    }
			    if (!p2Yellow.get() && lightsStatusP2.equals("Green")) {
			        lightsStatusP2 = "Yellow";
			        Logger.getInstance().log(phaseTwoName + " changed lights to " + lightsStatusP2);
			        notifyController();
			    }
			    if (!p2Red.get() && lightsStatusP2.equals("Yellow")) {
			        lightsStatusP2 = "Red";
			        Logger.getInstance().log(phaseTwoName + " changed lights to " + lightsStatusP2);
			        notifyController();
			    }
			    if (!p1Active.get() && !p1Notified) {
			        p1Notified = true;
			        notifyVehicles(activePhaseOne);
			        activePhaseOne = null;
			        notifyController();
			    }
			    if (!p2Active.get() && !p2Notified) {
			        p2Notified = true;
			        notifyVehicles(activePhaseTwo);
			        activePhaseTwo = null;
			        notifyController();
			    }
			    if (!p1Active.get() && !p2Active.get()) {
			        break;
			    }
				try {
	                Thread.sleep(100);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
			}

	        schedulerp1.shutdown();
	        schedulerp2.shutdown();
	        schedulerp1yellow.shutdown();
	        schedulerp2yellow.shutdown();
	        schedulerp1red.shutdown();
	        schedulerp2red.shutdown();
		}
		
		public void iterateSegments() {
			while (trafficControllerActive) {
				notifyVehicles(activePhaseOne);
				notifyVehicles(activePhaseTwo);
				phaseLights();
				try {
		            Thread.sleep(redLightsSeconds * 1000);
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
				updateSegmentIndex();
				Logger.getInstance().log("Segment switched to: " + String.valueOf(segmentIndex));
				updateActivePhaseInfo();
			}
		}
		
		private void notifyVehicles(String phase) {
			phases.notifyVehicles(phase);
		}
		
		public String[] getActivePhases() {
			String[] activePhases = {this.activePhaseOne, this.activePhaseTwo};
			return activePhases;
		}
		
		public synchronized String[] getPhaseNameWithLightStatus() {
			String phaseOne = this.phaseOneName + " - " + this.lightsStatusP1;
			String phaseTwo = this.phaseTwoName + " - " + this.lightsStatusP2;
			String[] activePhases = {phaseOne, phaseTwo};
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
			Logger.getInstance().log("Traffic simulation started");
			iterateSegments();
		}
}
