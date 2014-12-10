package TerminalFahrplan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.fusesource.jansi.Ansi;
import org.json.JSONArray;
import org.json.JSONException;

public class StationView extends Thread {

	private JSONArray stationboard;
	private boolean autoUpdate;

	final static String TOD_STATIONBOARD = "http://transport.opendata.ch/v1/stationboard?station=";
	final static int NUM_SHOW_ROWS = 20;
	final static SimpleDateFormat SDF = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ssZZZZZ"); //Given from the API

	public StationView(JSONArray stationboard, boolean autoUpdate) {
		this.stationboard = stationboard;
		this.autoUpdate = autoUpdate;
	}

	@Override
	public void run() {
		do {
			try {
				Ansi ansi = Ansi.ansi();
				TerminalTable tt = new TerminalTable(new Row("Bezeichnung", "Von", "Nach", "Abfahrt", "Versp."));
				//				String url = TOD_STATIONBOARD + station;
				//				JSONArray stationboard = JSONStuff.readJsonFromUrl(url).getJSONArray("stationboard");
				int numRows = NUM_SHOW_ROWS > stationboard.length() ? stationboard.length() : NUM_SHOW_ROWS;
				for (int i = 0; i < numRows; i++) {
					Row nextRow = new Row();
					//Bezeichnung
					nextRow.addData(new RowEntry(stationboard.getJSONObject(i).get("name")));
					//Von
					nextRow.addData(new RowEntry(stationboard.getJSONObject(i).getJSONObject("stop").getJSONObject("station").getString("name")));
					//Nach
					nextRow.addData(new RowEntry(stationboard.getJSONObject(i).get("to")));
					//Abfahrtszeit
					Date departure = dateFromString(stationboard.getJSONObject(i).getJSONObject("stop").get("departure").toString(), SDF);
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
					nextRow.addData(new RowEntry(sdf.format(departure)));
					//Verspätung
					String dateString = stationboard.getJSONObject(i).getJSONObject("stop").getJSONObject("prognosis").get("departure")
							.toString();
					if (dateString == "null")
						nextRow.addData(new RowEntry(""));
					else {
						long tooLateTimeInMillis = dateFromString(dateString, SDF).getTime() - departure.getTime();
						nextRow.addData(new RowEntry(tooLateTimeInMillis / 60000 + "'"));
						nextRow.setImportant(true);
					}
					//Next

					tt.addEntry(nextRow);
				}
				System.out.println(ansi.eraseScreen());
				tt.print();
				Thread.sleep(3000);
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (RowException e) {
				e.printStackTrace();
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
