package controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import model.Phase;
import model.Phases;

public class TrafficController {
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
		
		//Constructor
		public TrafficController(Phases phases) {
			this.phases = phases;
			this.segments = createSegmentList(phases.getPhasesHmap());
			segmentIndex = 0;
			updateActivePhaseInfo();
		}
		
		//Method bundles phases into 2d array and creates a linked list
		//Each 2d array in this linked list will represent a segment
		//Takes the HMAP of phase items
		private LinkedList<Object[][]> createSegmentList(HashMap<String, Phase> phasesHMap) {
			//Converts HMAP phases to segments - 2d array containing phase names and durations
			//This is the linked list to be iterated over
			LinkedList<Object[][]> segments = new LinkedList<Object[][]>();
			int i = 0;
			Object[][] segment = new Object[2][2];
			for (Map.Entry<String, Phase> entry : phasesHMap.entrySet()) {
				segment[i][0] = entry.getKey();
				segment[i][1] = entry.getValue().getPhaseDuration();
				i++;
				if (i > 1) {
					i = 0;
					segments.add(segment);
					segment = new Object[2][2];
				}
			}
			return segments;
		}
		
		private void updateSegmentIndex() {
			if (segmentIndex > segments.size() -1) {
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
		
		public void iterateSegments() {
			System.out.println(activePhaseOne);
			System.out.println(activePhaseTwo);
			System.out.println(activePhaseOneDuration);
		}
}
