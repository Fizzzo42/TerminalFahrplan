package terminalFahrplan;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

public class Row {

	@Getter
	private ArrayList<RowEntry> data;
	@Getter
	@Setter
	private boolean important = false;

	public Row() {
		data = new ArrayList<>();
	}

	public Row(RowEntry... data) {
		this();
		for (RowEntry s : data)
			this.data.add(s);
	}

	public Row(String... data) {
		this();
		for (String s : data) {
			this.data.add(new RowEntry(s));
		}
	}

	public void addData(RowEntry data) {
		if (data.isImportant())
			setImportant(true);
		this.data.add(data);
	}

	public RowEntry get(int index) {
		return data.get(index);
	}

	public int size() {
		return data.size();
	}

}
