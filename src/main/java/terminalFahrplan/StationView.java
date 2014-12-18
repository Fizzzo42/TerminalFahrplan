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
	final static SimpleDateFormat SDF = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ssZZZZZ"); //Given from the API

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
				TerminalTable tt = new TerminalTable(new Row("Bezeichnung", "Von", "Nach", "Abfahrt", "Versp.", "Platform"));
				int numRows = NUM_SHOW_ROWS > stationboard.length() ? stationboard.length() : NUM_SHOW_ROWS;
				for (int i = 0; i < numRows; i++) {
					JSONObject current = stationboard.getJSONObject(i);

					Row nextRow = new Row();
					//Bezeichnung
					nextRow.addData(new RowEntry(current.get("name")));
					//Von
					nextRow.addData(new RowEntry(current.getJSONObject("stop").getJSONObject("station").getString("name")));
					//Nach
					nextRow.addData(new RowEntry(current.get("to")));
					//Abfahrtszeit
					Date departure = dateFromString(current.getJSONObject("stop").get("departure").toString(), SDF);
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
					nextRow.addData(new RowEntry(sdf.format(departure)));
					//Verspätung
					String delay = current.getJSONObject("stop").get("delay").toString();

					if (delay == "null") {
						delay = "";
						nextRow.addData(new RowEntry(delay));
					} else {
						delay += "'";
						nextRow.addData(new RowEntry(delay, true));
						nextRow.setImportant(true);
					}
					//Platform
					String shouldplatform = current.getJSONObject("stop").getString("platform");
					String prognosisPlatform = current.getJSONObject("stop").getJSONObject("prognosis").get("platform").toString();
					if (shouldplatform == "" && prognosisPlatform == "null")
						nextRow.addData(new RowEntry(""));
					else if (shouldplatform.contains(prognosisPlatform) || prognosisPlatform == "null")
						nextRow.addData(new RowEntry(shouldplatform));
					else {
						//Unusual Platform
						nextRow.addData(new RowEntry(prognosisPlatform, true));
						nextRow.setImportant(true);
					}
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

	private Date dateFromString(String s, SimpleDateFormat df) {
		Date date = null;
		try {
			date = df.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

}
