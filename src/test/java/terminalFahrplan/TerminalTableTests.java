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

	@Before
	public void setUpStreams() {
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
	}

	@Test
	public void headerTest() {
		TerminalTable tt = new TerminalTable(new Row(headerData));
		tt.print();
		for (String header : headerData)
			assertTrue(outContent.toString().contains(header));
	}

	@Test
	public void headerFormatTest() {
		TerminalTable tt = new TerminalTable(new Row(headerData));
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
		TerminalTable tt = new TerminalTable(new Row(headerData));
		tt.print();
		int lineLength = 0;
		for (String s : headerData) {
			lineLength += s.length();
			lineLength += TerminalTable.COLUMNSIZE_OFFSET;
		}
		String dynamicUnderline = StringUtils.repeat("-", lineLength);
		assertTrue(outContent.toString().contains(dynamicUnderline));
	}

	@After
	public void cleanUpStreams() {
		System.setOut(null);
		System.setErr(null);
	}

}
