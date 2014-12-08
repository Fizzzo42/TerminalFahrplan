package TerminalFahrplan;

public class RowEntry {
	private Object obj;
	private boolean important;

	public RowEntry(Object obj) {
		this.obj = obj;
	}

	public RowEntry(Object obj, boolean important) {
		this.obj = obj;
		this.important = important;
	}

	public boolean isImportant() {
		return important;
	}

	public void setImportant(boolean important) {
		this.important = important;
	}

	public Object getObj() {
		return obj;
	}

}
