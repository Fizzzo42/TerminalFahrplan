package terminalFahrplan;

import static org.junit.Assert.fail;
import helpers.StringHelpers;

import java.io.IOException;
import java.nio.file.Paths;

import networkCom.JSONStuff;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.BeforeClass;
import org.junit.Test;

public class StationViewTests {

	private String offlineFilePath;
	JSONArray stationboard;

	@BeforeClass
	public void setUp() throws JSONException, IOException {
		offlineFilePath = StringHelpers.replaceLast(Paths.get("").toAbsolutePath().toString() + "/zuerichHB.json",
				"/bin/", "/lib/");
		stationboard = JSONStuff.readJsonFromFile(offlineFilePath).getJSONArray("stationboard");
		// JSONObject route = stationboard.getJSONObject(i);
	}

	@Test(timeout = 10000)
	public void stationViewFromOfflineFileTest() {
		StationView sv = new StationView(offlineFilePath, false);
		sv.start();
		try {
			sv.join();
		} catch (InterruptedException e) {
			fail();
		}
	}

	@Test
	public void getDescriptionTest() {
		for (int i = 0; i < stationboard.length(); i++) {
			if (StationView.fetchDescription(stationboard.getJSONObject(i)).getObj() == null)
				fail("Couldn't get \"description\"");
		}
	}

	@Test
	public void getFromTest() {
		for (int i = 0; i < stationboard.length(); i++) {
			if (StationView.fetchFrom(stationboard.getJSONObject(i)).getObj() == null)
				fail("Couldn't get \"From\"");
		}
	}

	@Test
	public void getToTest() {
		for (int i = 0; i < stationboard.length(); i++) {
			if (StationView.fetchTo(stationboard.getJSONObject(i)).getObj() == null)
				fail("Couldn't get \"To\"");
		}
	}

	@Test
	public void getDepartureTimeTest() {
		for (int i = 0; i < stationboard.length(); i++) {
			if (StationView.fetchDepartureTime(stationboard.getJSONObject(i)).getObj() == null)
				fail("Couldn't get \"Departure Time\"");
		}
	}

	@Test
	public void getDelayTest() {
		for (int i = 0; i < stationboard.length(); i++) {
			RowEntry delay = StationView.fetchDelay(stationboard.getJSONObject(i));
			if (delay.getObj() == null)
				fail("Couldn't get \"From\"");
			else {
				if (delay.isImportant() && delay.getObj().toString() == "")
					fail("Shouldn't be set as \"important\"");
				if (!delay.isImportant() && delay.getObj().toString() != "")
					fail("Should be set as \"important\"");
			}
		}
	}

	@Test
	public void getPlatformTest() {
		for (int i = 0; i < stationboard.length(); i++) {
			if (StationView.fetchPlatform(stationboard.getJSONObject(i)).getObj() == null)
				fail("Couldn't get \"Platform\"");
		}
	}
}
