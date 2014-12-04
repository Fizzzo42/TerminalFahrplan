package TerminalFahrplan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Main {

	public static void main(String[] args) {
		String station = readStation();
		System.out.println("Loading data for " + station + "...");
	}

	private static String readStation() {
		String station;
		Scanner scanner = new Scanner(System.in);
		String domain = "http://transport.opendata.ch/v1/locations?query=";
		boolean stationFound = false;

		do {
			System.out.println("Please enter your location: ");
			station = scanner.next();
			try {
				JSONArray stations = readJsonFromUrl(domain + station + "&type=station").getJSONArray("stations");
				if (stations.length() > 0) {
					System.out.println("Did you mean " + stations.getJSONObject(0).getString("name") + "? (y/n)");
					String result = scanner.next();
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
				System.out.println("Please retry! IOException");
			}
		} while (!stationFound);

		scanner.close();

		return station;
	}

	private static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
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

}
