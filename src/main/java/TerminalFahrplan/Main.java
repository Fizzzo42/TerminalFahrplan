package TerminalFahrplan;

import java.io.FileNotFoundException;
import java.io.IOException;

import networkCom.JSONStuff;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Color;
import org.fusesource.jansi.AnsiConsole;
import org.json.JSONArray;
import org.json.JSONException;

public class Main {

	final static double VERSION = 1.0;
	final static String TOD_SEARCHLOCATION = "http://transport.opendata.ch/v1/locations?query=";

	public static void main(String[] args) {
		System.out.println("Wir sind " + args.length + " lang");
		AnsiConsole.systemInstall();
		Ansi ansi = Ansi.ansi();
		System.out.println(ansi.fg(Color.MAGENTA).a("Welcome to TerminalFahrplan " + VERSION).reset());

		switch (args.length) {
		case 0:
			System.out.println("Please give a data!");
			try {
				JSONArray stations = JSONStuff.readJsonFromFile(
						"/Users/Fizzzo/Documents/Workspace/TerminalFahrplan/offline_files/zuerichHB.json").getJSONArray("stationboard");
				;
				StationView stationViewer = new StationView(stations, false);
				stationViewer.run();
				stationViewer.join();

			} catch (FileNotFoundException e2) {
				e2.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		case 1:
			String station = args[0];
			String url = TOD_SEARCHLOCATION + station + "&type=station";
			JSONArray stations;

			try {
				stations = JSONStuff.readJsonFromUrl(url).getJSONArray("stations");
				if (stations.length() > 0) {
					station = stations.getJSONObject(0).getString("name");
				}
				JSONArray stationboard = JSONStuff.readJsonFromUrl(url).getJSONArray("stationboard");
				StationView stationViewer = new StationView(stationboard, true);
				stationViewer.start();
				stationViewer.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case 2:
			System.out.println("Still TODO");
			break;
		default:
			System.out.println("Too many input params.");
		}

	}

}
