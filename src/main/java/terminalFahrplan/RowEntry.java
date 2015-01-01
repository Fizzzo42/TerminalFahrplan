package terminalFahrplan;

import lombok.Getter;
import lombok.Setter;

public class RowEntry {
	@Getter
	private Object obj;
	@Getter
	@Setter
	private boolean important;

	public RowEntry(Object obj) {
		this.obj = obj;
	}

	public RowEntry(Object obj, boolean important) {
		this.obj = obj;
		this.important = important;
	}

}
