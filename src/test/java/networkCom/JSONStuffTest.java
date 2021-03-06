package networkCom;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import helpers.StringHelpers;

import java.io.IOException;
import java.nio.file.Paths;

import org.json.JSONException;
import org.junit.BeforeClass;
import org.junit.Test;

public class JSONStuffTest {

	private static String offlineFilePath;

	@BeforeClass
	public static void setUp() {
		offlineFilePath = StringHelpers.replaceLast(Paths.get("").toAbsolutePath().toString() + "/zuerichHB.json",
				"/bin/", "/lib/");
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
