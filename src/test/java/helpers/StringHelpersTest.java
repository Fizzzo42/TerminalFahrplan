package helpers;

import static org.junit.Assert.*;

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
}
