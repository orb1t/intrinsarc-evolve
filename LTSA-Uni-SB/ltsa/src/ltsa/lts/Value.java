package ltsa.lts;

/* -------------------------------------------------------------------------------*/

public class Value {
	private int val;
	private final String sval;
	private boolean sonly;

	public Value(int i) {
		val = i;
		sonly = false;
		sval = String.valueOf(i);
	}

	public Value(String s) { // convert string to integer of possible
		sval = s;
		try {
			val = Integer.parseInt(s);
			sonly = false;
		} catch (NumberFormatException e) {
			sonly = true;
		}
	}

	@Override
	public String toString() {
		return sval;
	}

	public int intValue() {
		return val;
	}

	public boolean isInt() {
		return !sonly;
	}

	public boolean isLabel() {
		return sonly;
	}
}