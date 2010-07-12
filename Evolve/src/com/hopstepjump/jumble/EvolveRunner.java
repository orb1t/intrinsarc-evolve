package com.hopstepjump.jumble;

import java.io.*;
import java.net.*;
import java.util.*;

import com.hopstepjump.jumble.gui.*;

/**
 * this is to provide a jar so that evolve can be run easily
 * @author andrew
 *
 */
public class EvolveRunner
{
	public static void main(String args[])
	{
		// if no arguments, pass in the actual home directory
		String home;
		if (args.length == 0)
			home = getHomeDirectory();
		else
			home = args[0];
		
		String date = new Date().toString().replace(":", "-");
		Evolve.main(new String[]{home, home + "/logs/" + date + ".log"});
	}
	
	private static String getHomeDirectory()
	{
		// relies on the fact that this jar will be deployed one level below the root level
		File start = new File(EvolveRunner.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		// parent should work for local path in Eclipse, and also when deployed as a top level jar
		start = start.getParentFile();
		return URLDecoder.decode(start.getPath());
	}
}
