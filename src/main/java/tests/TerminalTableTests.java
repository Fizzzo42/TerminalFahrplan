package tests;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import TerminalFahrplan.Row;
import TerminalFahrplan.TerminalTable;

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
			for (int i = 0; i < TerminalTable.COLUMNSIZE_OFFSET; i++)
				outputBuilder.append(" ");
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
		String dynamicUnderline = "";
		for (int i = 0; i < lineLength; i++)
			dynamicUnderline += "-";
		assertTrue(outContent.toString().contains(dynamicUnderline));
	}

	@After
	public void cleanUpStreams() {
		System.setOut(null);
		System.setErr(null);
	}

}
