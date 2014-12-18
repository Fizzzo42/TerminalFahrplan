package terminalFahrplan;

public class RowException extends Exception {

	public RowException(int rowSize, int headerSize) {
		super();
		System.out.println("Row size and Header size do not match!");
		System.out.println("Row size: " + rowSize);
		System.out.println("Header size: " + headerSize);
	}
}
