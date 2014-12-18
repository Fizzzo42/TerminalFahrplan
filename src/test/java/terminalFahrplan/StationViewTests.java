package terminalFahrplan;

import static org.junit.Assert.*;
import helpers.StringHelpers;

import org.junit.Before;
import org.junit.Test;

public class StationViewTests {

	private String offlineFilePath;

	@Before
	public void setUp() {
		offlineFilePath = StringHelpers.replaceLast(System.getProperty("user.dir") + "/zuerichHB.json", "/bin/",
				"/lib/");
	}

	@Test(timeout = 5000)
	public void stationViewFromOfflineFileTest() {
		StationView sv = new StationView(offlineFilePath, false);
		sv.start();
		try {
			sv.join();
		} catch (InterruptedException e) {
			fail();
		}
	}

}
