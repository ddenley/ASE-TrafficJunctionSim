package Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import JunctionSim.ProduceReport;
/**
 * @author Daniel Denley
 *
 */
class ProduceReportTest {

	//Only need to test validity of method
	//Simply takes a string of contents and prints to a file with some additions
	//Contents provided has been thoroughly tested in other test classes
	@Test
	final void testCreateReport() {
		String[] valid1 = {"This", "is", "valid"};
		ProduceReport.createReport(valid1);
		String[] valid2 = {"2412", "nahs^7", "validsad"};
		ProduceReport.createReport(valid2);
	}
}
