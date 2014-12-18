package terminalFahrplan;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import terminalFahrplan.Row;
import terminalFahrplan.TerminalTable;

public class TerminalTableTests {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	private String headerData[] = { "First", "Second", "Third" };
	private TerminalTable tt;

	@Before
	public void setUpStreams() {
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
		tt = new TerminalTable(new Row(headerData));
	}

	@Test
	public void headerTest() {
		tt.print();
		for (String header : headerData)
			assertTrue(outContent.toString().contains(header));
	}

	@Test
	public void headerFormatTest() {
		tt.print();
		StringBuilder outputBuilder = new StringBuilder();
		for (String header : headerData) {
			outputBuilder.append(header);
			outputBuilder.append(StringUtils.repeat(" ", TerminalTable.COLUMNSIZE_OFFSET));
		}
		assertTrue(outContent.toString().contains(outputBuilder.toString()));
	}

	@Test
	public void dynamicUnderlineLengthTest() {
		tt.print();
		int lineLength = 0;
		for (String s : headerData) {
			lineLength += s.length();
			lineLength += TerminalTable.COLUMNSIZE_OFFSET;
		}
		String dynamicUnderline = StringUtils.repeat("-", lineLength);
		assertTrue(outContent.toString().contains(dynamicUnderline));
	}

	@Test
	public void addRowTest() {
		try {
			tt.addEntry(new Row("I am first", "I am second", "And I am third"));
			tt.addEntry(new Row(new RowEntry("I", true), new RowEntry("say"), new RowEntry("Hello", true)));
		} catch (RowException e) {
			fail();
		}
	}

	@Test(expected = RowException.class)
	public void addRowTooManyFailTest() throws RowException {
		tt.addEntry(new Row("These", "are", "way", "too", "many", "entries", "for", "a", "table", "with", "the",
				"header", "size", "of", "3"));
	}

	@Test(expected = RowException.class)
	public void addRowTooFewFailTest() throws RowException {
		tt.addEntry(new Row(new RowEntry("Hello", true)));
	}

	@After
	public void cleanUpStreams() {
		System.setOut(null);
		System.setErr(null);
	}

}
