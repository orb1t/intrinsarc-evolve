package ltsa.lts;

public class LTSInputString implements LTSInput {

	private final String fSrc;
	private int fPos;

	public LTSInputString(String s) {
		fSrc = s;
		fPos = -1;
	}

	public char nextChar() {
		fPos = fPos + 1;
		if (fPos < fSrc.length()) {
			return fSrc.charAt(fPos);
		} else {
			// fPos = fPos - 1;
			return '\u0000';
		}
	}

	public char backChar() {
		fPos = fPos - 1;
		if (fPos < 0) {
			fPos = 0;
			return '\u0000';
		} else
			return fSrc.charAt(fPos);
	}

	public int getMarker() {
		return fPos;
	}

	// >>> AMES: Enhanced Modularity
	public void resetMarker() {
		fPos = -1;
	}
	// <<< AMES
}