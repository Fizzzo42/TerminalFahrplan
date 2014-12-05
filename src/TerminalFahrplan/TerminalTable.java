package TerminalFahrplan;

import java.util.ArrayList;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Color;
import org.fusesource.jansi.AnsiConsole;
import org.fusesource.jansi.AnsiOutputStream;
import org.fusesource.jansi.AnsiString;

public class TerminalTable {

	private Row header;
	private ArrayList<Row> entries;
	private int columnSizes[];
	private final static int COLUMNSIZE_OFFSET = 5;

	public TerminalTable() {
		//header = new ArrayList<>();
		entries = new ArrayList<>();
	}

	public TerminalTable(Row header) {
		this();
		this.header = header;
		columnSizes = new int[this.header.getData().size()];
		for (int i = 0; i < this.header.getData().size(); i++)
			columnSizes[i] = ((String) this.header.get(i)).length();
	}

	public void addEntry(Row row) throws RowException {
		if (row.getData().size() != header.getData().size())
			throw new RowException(row.getData().size(), header.getData().size());
		entries.add(row);
		//Refresh columnSizes
		refreshColumnSizes(row);
	}

	public void print() {
		Ansi ansi = Ansi.ansi();
		int columnCounter = 0;
		//ansi.bg(Color.YELLOW);
		//Print header
		ansi.bg(Color.YELLOW);
		for (int i = 0; i < header.size(); i++) {
			int columnSize = columnSizes[columnCounter] + COLUMNSIZE_OFFSET;
			ansi.format("%-" + columnSize + "s", header.get(i));
			columnCounter++;
		}
		ansi.reset();
		System.out.print(ansi);
		System.out.println();

		//Variable Line Length
		StringBuilder lineBuilder = new StringBuilder();
		for (int columnSize : columnSizes)
			for (int i = 0; i < columnSize + COLUMNSIZE_OFFSET; i++)
				lineBuilder.append('-');
		//line += StringUtils.repeat("-", columnSize);
		System.out.println(lineBuilder.toString());

		for (Row entry : entries) {
			columnCounter = 0; //New column
			ansi = Ansi.ansi();
			if (entry.isImportant())
				ansi.bg(Color.RED);
			for (Object obj : entry.getData()) {
				ansi.format("%-" + (columnSizes[columnCounter] + COLUMNSIZE_OFFSET) + "s", obj);
				columnCounter++;
			}
			ansi.reset();
			System.out.print(ansi);
			System.out.println();
		}
	}

	private void refreshColumnSizes(Row row) {
		for (int i = 0; i < row.getData().size(); i++)
			columnSizes[i] = columnSizes[i] < ((String) row.getData().get(i)).length() ? ((String) row.getData().get(i)).length()
					: columnSizes[i];
	}
}