package terminalFahrplan;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import networkCom.JSONStuff;

import org.fusesource.jansi.Ansi;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StationView extends Thread {

	private String url;
	private boolean autoUpdate;

	final static int NUM_SHOW_ROWS = 20;
	final static SimpleDateFormat SDF = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ssZZZZZ"); // Given
																							// from
																							// the
																							// API

	public StationView(String url, boolean autoUpdate) {
		this.url = url;
		this.autoUpdate = autoUpdate;
	}

	@Override
	public void run() {
		do {
			try {
				JSONArray stationboard;
				if (url.contains("http://"))
					stationboard = JSONStuff.readJsonFromUrl(url).getJSONArray("stationboard");
				else
					stationboard = JSONStuff.readJsonFromFile(url).getJSONArray("stationboard");

				Ansi ansi = Ansi.ansi();
				TerminalTable tt = new TerminalTable(new Row("Bezeichnung", "Von", "Nach", "Abfahrt", "Versp.",
						"Platform"));
				int numRows = NUM_SHOW_ROWS > stationboard.length() ? stationboard.length() : NUM_SHOW_ROWS;
				for (int i = 0; i < numRows; i++) {
					JSONObject route = stationboard.getJSONObject(i);

					Row nextRow = new Row();
					// Bezeichnung
					nextRow.addData(getDescription(route));
					// Von
					nextRow.addData(getFrom(route));
					// Nach
					nextRow.addData(getTo(route));
					// Abfahrtszeit
					nextRow.addData(getDepartureTime(route));
					// Verspätung
					RowEntry entry = getDelay(route);
					if (entry.isImportant())
						nextRow.setImportant(true);
					nextRow.addData(entry);
					// Platform
					entry = getPlatform(route);
					if (entry.isImportant())
						nextRow.setImportant(true);
					nextRow.addData(entry);
					// Add Row to table
					tt.addEntry(nextRow);
				}
				System.out.println(ansi.eraseScreen());
				tt.print();
				Thread.sleep(3000);
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e2) {
				System.out.println("Couldn't find offline JSON file in:\n" + url);
			} catch (RowException e) {
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("Retrying...");
			}
		} while (autoUpdate);
	}

	protected Date dateFromString(String s, SimpleDateFormat df) {
		Date date = null;
		try {
			date = df.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	protected RowEntry getDescription(JSONObject route) {
		return new RowEntry(route.get("name"));
	}

	protected RowEntry getFrom(JSONObject route) {
		return new RowEntry(route.getJSONObject("stop").getJSONObject("station").getString("name"));
	}

	protected RowEntry getTo(JSONObject route) {
		return new RowEntry(route.get("to"));
	}

	protected RowEntry getDepartureTime(JSONObject route) {
		Date departure = dateFromString(route.getJSONObject("stop").get("departure").toString(), SDF);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return new RowEntry(sdf.format(departure));
	}

	protected RowEntry getDelay(JSONObject route) {
		String delay = route.getJSONObject("stop").get("delay").toString();
		boolean important = false;
		if (delay == "null") {
			delay = "";
		} else {
			important = true;
			delay += "'";
		}
		return new RowEntry(delay, important);
	}

	protected RowEntry getPlatform(JSONObject route) {
		String result;
		boolean important = false;
		String shouldplatform = route.getJSONObject("stop").getString("platform");
		String prognosisPlatform = route.getJSONObject("stop").getJSONObject("prognosis").get("platform").toString();
		if (shouldplatform == "" && prognosisPlatform == "null")
			result = "";
		else if (shouldplatform.contains(prognosisPlatform) || prognosisPlatform == "null")
			result = shouldplatform;
		else {
			// Unusual Platform
			result = prognosisPlatform;
			important = true;
		}
		return new RowEntry(result, important);
	}

}
