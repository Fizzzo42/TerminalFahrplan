package TerminalFahrplan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Color;
import org.fusesource.jansi.AnsiConsole;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Main {

	final static double VERSION = 1.0;
	final static String TOD_SEARCHLOCATION = "http://transport.opendata.ch/v1/locations?query=";
	final static String TOD_STATIONBOARD = "http://transport.opendata.ch/v1/stationboard?station=";
	final static int NUM_SHOW_ROWS = 20;
	final static SimpleDateFormat SDF = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ssZZZZZ"); //Given from the API

	public static void main(String[] args) {
		AnsiConsole.systemInstall();
		Ansi ansi = Ansi.ansi();
		System.out.println(ansi.fg(Color.MAGENTA).a("Welcome to TerminalFahrplan " + VERSION).reset());
		String station = readStation();
		System.out.println("Loading data for " + station + "...");

		while (true) {
			try {
				TerminalTable tt = new TerminalTable(new Row("Bezeichnung", "Von", "Nach", "Abfahrt", "Versp."));
				String url = TOD_STATIONBOARD + station;
				JSONArray stationboard = readJsonFromUrl(url).getJSONArray("stationboard");
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
			} catch (IOException e) {
				//Connection Error...
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (RowException e) {
				e.printStackTrace();
			}
		}

	}

	private static String readStation() {
		String station;
		Scanner scanner = new Scanner(System.in);

		boolean stationFound = false;

		do {
			System.out.println("Please enter your location: ");
			station = scanner.nextLine();
			try {
				String url = TOD_SEARCHLOCATION + station + "&type=station";
				JSONArray stations = readJsonFromUrl(url).getJSONArray("stations");
				if (stations.length() > 0) {
					System.out.println("Did you mean " + stations.getJSONObject(0).getString("name") + "? (y/n)");
					String result = scanner.nextLine();
					if (result.contains("y")) {
						station = stations.getJSONObject(0).getString("name");
						stationFound = true;
					} else if (result.contains("n")) {
					} else
						System.out.println("Couldn't understand your answer :(");
				} else
					System.out.println("No matching result");
			} catch (JSONException e) {
				System.out.println("Please retry! JSONExpection");
			} catch (IOException e) {
				System.out.println("Conntection Error... Please retry!");
			}
		} while (!stationFound);

		scanner.close();

		return station;
	}

	private static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		url = url.replaceAll(" ", "%20");
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	private static Date dateFromString(String s, SimpleDateFormat df) {
		Date date = null;
		try {
			date = df.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

}
