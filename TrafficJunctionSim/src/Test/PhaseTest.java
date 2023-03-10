package Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import JunctionSim.Phase;
/**
 * @author Daniel Denley
 *
 */
class PhaseTest {

	//Tests for constructor
	
	@Test
	final void testPhaseConstructorValid() {
		Phase phase = new Phase("20.5");
	}
	
	@Test
	final void testPhaseConstructorMissingDuration() {
		assertThrows(IllegalArgumentException.class, () -> new Phase(""));
	}
	
	@Test
	final void testPhaseConstructorBorderCaseDurationZero() {
		assertThrows(IllegalArgumentException.class, () -> new Phase("0"));
	}
	
	@Test
	final void testPhaseConstructorBorderCaseDurationThreeHundred() {
		assertThrows(IllegalArgumentException.class, () -> new Phase("300"));
	}
	
	@Test
	final void testPhaseConstructorDurationExceedsThreeHundred() {
		assertThrows(IllegalArgumentException.class, () -> new Phase("300.01"));
	}
	
	@Test
	final void testPhaseConstructorDurationLessThanZero() {
		assertThrows(IllegalArgumentException.class, () -> new Phase("-2.1"));
	}
	
	//Test for queue functions
	@Test
	final void testAddQueueAndPopping() {
		String exampleKey = "123456";
		Phase phase = new Phase("20.5");
		phase.addQueue(exampleKey);
		assertEquals(phase.popQueue(), exampleKey);
	}
}
