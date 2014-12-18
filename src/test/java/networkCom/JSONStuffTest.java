package networkCom;

import static org.junit.Assert.*;
import helpers.StringHelpers;

import java.io.IOException;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

public class JSONStuffTest {

	String offlineFilePath;

	@Before
	public void setUp() {
		offlineFilePath = StringHelpers.replaceLast(System.getProperty("user.dir") + "/zuerichHB.json", "/bin/",
				"/lib/");
	}

	@Test
	public void offlineFileExistsTest() {
		try {
			JSONStuff.readJsonFromFile(offlineFilePath);
		} catch (IOException e) {
			fail("File not found");
		}
	}

	@Test
	public void offlineFileWorksTest() {
		try {
			assertTrue(JSONStuff.readJsonFromFile(offlineFilePath).getJSONArray("stationboard").length() > 0);
		} catch (JSONException | IOException e) {
			fail();
		}
	}

	@Test
	public void apiServerOnlineTest() {
		try {
			JSONStuff.readJsonFromUrl("http://transport.opendata.ch/v1/stationboard?station=zurich");
		} catch (JSONException | IOException e) {
			fail();
		}
	}

	@Test
	public void apiServerValidDataTests() {
		try {
			assertTrue(JSONStuff.readJsonFromUrl("http://transport.opendata.ch/v1/stationboard?station=zurich")
					.getJSONArray("stationboard").length() > 0);
		} catch (JSONException | IOException e) {
			fail();
		}
	}
}
