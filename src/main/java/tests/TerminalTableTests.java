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

	@Before
	public void setUpStreams() {
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
	}

	@Test
	public void headerTest() {
		TerminalTable tt = new TerminalTable(new Row("First", "Second", "Third"));
		tt.print();
		assertTrue(outContent.toString().contains("First") && outContent.toString().contains("Second")
				&& outContent.toString().contains("Third"));
	}

	@After
	public void cleanUpStreams() {
		System.setOut(null);
		System.setErr(null);
	}

}
