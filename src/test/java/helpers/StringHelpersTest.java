package helpers;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class StringHelpersTest {

	private String input1, input2, input3;

	@Before
	public void setUp() {
		input1 = "hellohowareyoudoing";
		input2 = "hellohowareyoudoing ";
		input3 = "hello how are you doing";
	}

	@Test
	public void replaceLastWithoutFoundTargetTest() {
		String cache = input1;
		cache = StringHelpers.replaceLast(cache, " ", "");
		assertTrue(cache.contentEquals(input1));
	}

	@Test
	public void replaceLastWithOneTargetTest() {
		String cache = input2;
		cache = StringHelpers.replaceLast(cache, " ", "");
		assertTrue(cache.contentEquals(input1));
	}

	@Test
	public void replaceLastWithManyTargetsTest() {
		String cache = input3;
		cache = StringHelpers.replaceLast(cache, " ", "WOW");
		assertTrue(cache.contentEquals("hello how are youWOWdoing"));
	}

	@Test
	public void parseInputWithoutInputTest() {
		String result[] = StringHelpers.parseInput(new String[0]);
		if (result.length != 0)
			fail();
	}

	@Test
	public void parseInputWithNormalInputTest() {
		String input[] = { "Rapperswil" };
		String result[] = StringHelpers.parseInput(input);
		if (!(result.length == 1 && input[0] == "Rapperswil"))
			fail();
	}

	@Test
	public void parseInputWithLongerInputTest() {
		String input[] = { "Affoltern", "am", "Albis", "Blablastrasse", "123" };
		String result[] = StringHelpers.parseInput(input);
		if (!(result.length == 1 && result[0].contentEquals("Affoltern am Albis Blablastrasse 123")))
			fail();
	}

	@Test
	public void parseInputWithNormalFromToInputTest() {
		String input[] = { "Rapperswil", "to", "Bassersdorf" };
		String result[] = StringHelpers.parseInput(input);
		assertTrue(result.length == 2);
		assertTrue(result[0].contentEquals("Rapperswil"));
		assertTrue(result[1].contentEquals("Bassersdorf"));
	}

	@Test
	public void dateToStringTest() {
		Date ourDate = StringHelpers.dateFromString("2014-12-18T17:19:00+0100", new SimpleDateFormat(
				"YYYY-MM-dd'T'HH:mm:ssZ"));
		if (ourDate.getTime() != 1388420340000L)
			fail();
		// TODO: Check if correct
	}
}
