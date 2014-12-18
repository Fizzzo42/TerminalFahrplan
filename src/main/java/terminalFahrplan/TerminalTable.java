package terminalFahrplan;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Color;

public class TerminalTable {

	private Row header;
	private ArrayList<Row> entries;
	private int columnSizes[];
	public final static int COLUMNSIZE_OFFSET = 3;

	public TerminalTable(Row header) {
		entries = new ArrayList<>();
		this.header = header;
		columnSizes = new int[this.header.getData().size()];
		for (int i = 0; i < this.header.getData().size(); i++)
			columnSizes[i] = ((String) this.header.get(i).getObj()).length();
	}

	public void addEntry(Row row) throws RowException {
		if (row.getData().size() != header.getData().size())
			throw new RowException(row.getData().size(), header.getData().size());
		entries.add(row);
		// Refresh columnSizes
		refreshColumnSizes(row);
	}

	public void print() {
		Ansi ansi = Ansi.ansi();
		int columnCounter = 0;
		// Print header
		ansi.bg(Color.WHITE);
		for (int i = 0; i < header.size(); i++) {
			int columnSize = columnSizes[columnCounter] + COLUMNSIZE_OFFSET;
			ansi.format("%-" + columnSize + "s", header.get(i).getObj());
			columnCounter++;
		}
		ansi.reset();
		System.out.print(ansi);
		System.out.println();

		// Variable Line Length
		String underline = "";
		for (int columnSize : columnSizes)
			underline += StringUtils.repeat("-", columnSize + COLUMNSIZE_OFFSET);

		System.out.println(underline);

		for (Row entry : entries) {
			columnCounter = 0; // New column
			ansi = Ansi.ansi();
			if (entry.isImportant())
				ansi.bg(Color.YELLOW);
			for (RowEntry rowEntry : entry.getData()) {
				if (rowEntry.isImportant())
					ansi.fg(Color.RED);
				else
					ansi.fg(Color.DEFAULT);

				ansi.format("%-" + (columnSizes[columnCounter] + COLUMNSIZE_OFFSET) + "s", rowEntry.getObj());
				ansi.fg(Color.DEFAULT);
				columnCounter++;
			}
			ansi.reset();
			System.out.print(ansi);
			System.out.println();
		}
	}

	protected void refreshColumnSizes(Row row) {
		for (int i = 0; i < row.getData().size(); i++)
			columnSizes[i] = columnSizes[i] < ((String) row.getData().get(i).getObj()).length() ? ((String) row
					.getData().get(i).getObj()).length() : columnSizes[i];
	}
}
