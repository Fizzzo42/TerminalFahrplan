package TerminalFahrplan;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import networkCom.JSONStuff;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Color;
import org.fusesource.jansi.AnsiConsole;
import org.json.JSONArray;
import org.json.JSONException;

public class Main {

	final static double VERSION = 1.0;
	final static String TOD_SEARCHLOCATION = "http://transport.opendata.ch/v1/locations?query=";
	final static String TOD_STATIONBOARD = "http://transport.opendata.ch/v1/stationboard?station=";

	public static void main(String[] args) {

		String parsedInput[] = parseInput(args);

		final String OFFLINEFILEPATH = replaceLast(System.getProperty("user.dir"), "/bin/", "/lib/") + "/zuerichHB.json";
		System.out.println("Our path is " + OFFLINEFILEPATH);
		AnsiConsole.systemInstall();
		Ansi ansi = Ansi.ansi();
		System.out.println(ansi.fg(Color.MAGENTA).a("Welcome to TerminalFahrplan " + VERSION).reset());

		switch (parsedInput.length) {
		//Work with Offline Data
		case 0:
			try {
				JSONArray stations = JSONStuff.readJsonFromFile(OFFLINEFILEPATH).getJSONArray("stationboard");
				StationView stationViewer = new StationView(stations, false);
				stationViewer.start();
				stationViewer.join();
			} catch (FileNotFoundException e2) {
				System.out.println("Couldn't find offline JSON file in:\n" + OFFLINEFILEPATH);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		//Show StationBoard
		case 1:
			String station = parsedInput[0];
			String url = TOD_SEARCHLOCATION + station + "&type=station";
			try {
				System.out.println("Loading location...");
				//Find Station which matches most
				JSONArray stations = JSONStuff.readJsonFromUrl(url).getJSONArray("stations");
				if (stations.length() > 0) {
					station = stations.getJSONObject(0).getString("name");
					url = TOD_STATIONBOARD + station;
					System.out.println("Loading stationboard for " + station);
					JSONArray stationboard = JSONStuff.readJsonFromUrl(url).getJSONArray("stationboard");
					StationView stationViewer = new StationView(stationboard, true);
					stationViewer.start();
					stationViewer.join();
				} else
					System.err.println("Couldn't find station " + station);

			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				System.err.println("Connection to API Server failed!");
			}
			break;
		//Show Route "From-To"
		case 2:
			System.out.println("Still TODO");
			break;
		default:
			System.err.println("Too many input params. Please dont use more than 2!");
		}

	}

	private static String[] parseInput(String[] args) {
		ArrayList<String> result = new ArrayList<>();
		String toAdd = "";
		for (String s : args) {
			if (result.size() < 2) {
				if (s != "to")
					toAdd += s;
				else
					result.add(toAdd);
			} else
				break;
		}
		if (toAdd != "")
			result.add(toAdd);

		return (String[]) result.toArray(new String[result.size()]);
	}

	private static String replaceLast(String input, String target, String replacement) {
		StringBuilder b = new StringBuilder(input);
		try {
			b.replace(input.lastIndexOf(target), input.lastIndexOf(target) + 1, replacement);
		} catch (StringIndexOutOfBoundsException e) {
			return input;
		}
		input = b.toString();
		return input;
	}

}
