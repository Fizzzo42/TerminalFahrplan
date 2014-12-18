package helpers;

import java.util.ArrayList;

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
			if (result.size() < 2) {
				if (s != "to")
					toAdd += s + " ";
				else
					result.add(toAdd);
			} else
				break;
		}
		if (toAdd != "")
			result.add(toAdd);

		if (result.size() > 0) {
			String lastString = result.get(result.size() - 1);
			lastString = StringHelpers.replaceLast(lastString, " ", "");
		}

		return result.toArray(new String[result.size()]);
	}
}
