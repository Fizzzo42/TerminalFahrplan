package TerminalFahrplan;

import java.util.ArrayList;

public class Row {

	private ArrayList<Object> data;
	private boolean important = false;

	public Row() {
		data = new ArrayList<>();
	}

	public Row(Object... data) {
		this();
		for (Object s : data)
			this.data.add(s);
	}

	public ArrayList<Object> getData() {
		return data;
	}

	public void addData(Object data) {
		this.data.add(data);
	}

	public Object get(int index) {
		return data.get(index);
	}

	public int size() {
		return data.size();
	}

	public boolean isImportant() {
		return important;
	}

	public void setImportant(boolean important) {
		this.important = important;
	}

}
