package helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StringHelpers {

	public static String replaceLast(String input, String target, String replacement) {
		StringBuilder b = new StringBuilder(input);
		try {
			b.replace(input.lastIndexOf(target), input.lastIndexOf(target) + target.length(), replacement);
		} catch (StringIndexOutOfBoundsException e) {
			return input;
		}
		input = b.toString();
		return input;
	}

	public static String[] parseInput(String[] args) {
		ArrayList<String> result = new ArrayList<>();
		String toAdd = "";
		for (String s : args) {
			if (s.contentEquals("to")) {
				toAdd = StringHelpers.replaceLast(toAdd, " ", "");
				result.add(toAdd);
				toAdd = "";
			} else
				toAdd += (s + " ");
		}
		if (!toAdd.contentEquals(""))
			result.add(toAdd);

		if (result.size() > 0) {
			String lastString = result.get(result.size() - 1);
			lastString = StringHelpers.replaceLast(lastString, " ", "");
			result.remove(result.size() - 1);
			result.add(lastString);
		}

		return result.toArray(new String[result.size()]);
	}

	public static Date dateFromString(String s, SimpleDateFormat df) {
		Date date = null;
		try {
			date = df.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
}
