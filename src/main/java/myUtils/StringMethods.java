package myUtils;

public class StringMethods {
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
}
