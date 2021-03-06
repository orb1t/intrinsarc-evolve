package ltsa.custom;

import java.awt.*;
import java.io.*;

import ltsa.lts.*;

public abstract class CustomAnimator extends Frame {

	public abstract void init(Animator a, File xml, Relation actionMap,
			Relation controlMap, boolean replay);

	public abstract void stop();

	public void dispose() {
		stop();
		super.dispose();
	}

}