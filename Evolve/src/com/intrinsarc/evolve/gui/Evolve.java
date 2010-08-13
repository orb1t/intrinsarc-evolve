package com.intrinsarc.evolve.gui;

import java.io.*;
import java.util.*;

import javax.swing.*;

import org.eclipse.emf.common.util.*;
import org.freehep.graphicsio.emf.gdi.*;

import com.hopstepjump.notifications.*;
import com.intrinsarc.backbone.generator.*;
import com.intrinsarc.deltaengine.errorchecking.*;
import com.intrinsarc.evolve.gui.lookandfeel.*;
import com.intrinsarc.evolve.html.*;
import com.intrinsarc.evolve.repositorybrowser.*;
import com.intrinsarc.evolve.umldiagrams.colors.*;
import com.intrinsarc.idraw.diagramsupport.*;
import com.intrinsarc.idraw.environment.*;
import com.intrinsarc.idraw.foundation.*;
import com.intrinsarc.idraw.foundation.persistence.*;
import com.intrinsarc.repository.*;
import com.intrinsarc.repositorybase.*;
import com.intrinsarc.swing.*;

/**
 * 
 * (c) Andrew McVeigh 13-Feb-03
 * 
 */
public class Evolve
{
	public static final String EVOLVE_VERSION = "v1.0, released 11th July 2010";
  public static final String LEAST_VERSION = "1.5";
  public static final String BEST_VERSION = "1.6";
	public static final int MAX_UNMODIFIED_UNVIEWED_DIAGRAMS = 5;
	public static final boolean ADVANCED = System.getProperty("Advanced") != null;

	private ToolCoordinatorGem toolManager;
	/** the application window coordinator manages a number of windows */
	private ApplicationWindowCoordinatorGem windowCoordinator;
	private ErrorRegister errors = new ErrorRegister();
	private static Preference OPEN_DIAGRAMS = new Preference(
			"Advanced",
			"Maximum unmodified or unviewed diagrams",
			new PersistentProperty(5));
	public static Preference EVOLVE = new Preference(
			"Variables",
			"EVOLVE",
			new PersistentProperty(""));

	public static void main(String args[])
  {
		// must have at least one arg
		if (args.length == 0)
		{
			System.err.println("Usage: evolve home_directory {logToFile}");
			System.exit(-1);
		}
		String home = args[0];
		
    String version = System.getProperty("java.specification.version");
    System.out.println("Evolve " + EVOLVE_VERSION);
		System.out.println("Evolve home: " + home);
    System.out.println("Detected JRE " + (version == null ? "(unknown)" : version));
		
    // make sure we have the correct version of java
    if (version == null || version.compareTo(LEAST_VERSION) < 0)
    {
      if (version == null)
      	version = "(unknown)";
      
      // complain if we don't have a least version of the runtime
    	Object[] options = {"Run Evolve anyway",
    	                    "Quit"};
    	int n = JOptionPane.showOptionDialog(null,
    	    "You are using version " + version + " of the Java runtime." +
    	    "\nEvolve needs version " + LEAST_VERSION + " or greater." +
    	    "\n\nPlease quit the application and install a newer version.",
    	    "Java version mismatch",
    	    JOptionPane.YES_NO_CANCEL_OPTION,
    	    JOptionPane.ERROR_MESSAGE,
    	    null,
    	    options,
    	    options[1]);

    	if (n == 1)
    		System.exit(0);
    }
    else    
    // complain if we don't have a least version of the runtime
    if (version.compareTo(BEST_VERSION) < 0)
    {
	  	Object[] options = {"Run Evolve anyway",
	  	                    "Quit"};
	  	int n = JOptionPane.showOptionDialog(null,
	  	    "You are using version " + version + " of the Java runtime." +
	  	    "\nEvolve prefers at version " + BEST_VERSION + " or greater.\nIt functions correctly with this version, but the graphical performance is not optimal." +
	  	    "\n\nYou might consider quitting and installing a newer version.",
	  	    "Java version mismatch",
	  	    JOptionPane.YES_NO_CANCEL_OPTION,
	  	    JOptionPane.INFORMATION_MESSAGE,
	  	    null,
	  	    options,
	  	    options[1]);
	
	  	if (n == 1)
	  		System.exit(0);
    }
    
		// set the evolve home directory as the EVOLVE variable
		GlobalPreferences.preferences.setVariableValue("EVOLVE", home);
		
    registerPreferenceSlots();
    registerFileTypes();
    
    // handle any preferences
    RegisteredGraphicalThemes themes = RegisteredGraphicalThemes.getInstance(); 
    themes.registerPreferenceSlots();
    themes.interpretPreferences();
    BaseColors.registerPreferenceSlots();
    BackboneWriter.registerPreferenceSlots();
    HTMLDocumentationGenerator.registerPreferenceSlots();
    PreferenceTypeDirectory.registerPreferenceSlots();
    RegisteredGraphicalThemes.getInstance().setLookAndFeel();
    
    setUpUUIDGenerator();
    Evolve application = new Evolve();
    application.setUpServices(home);
    application.setUpGUI(args.length > 1 && args[1] != null);
    application.showGUI();
    
    EMFOptions.CREATE_LISTS_LAZILY_FOR_GET = false;    
  }
	
	private static void registerFileTypes()
	{
		CustomisedFileChooser.addFileType(
				XMLSubjectRepositoryGem.UML2_SUFFIX_NO_DOT,
				IconLoader.loadIcon("evolve-file.png"),
				"Evolve model file");
		CustomisedFileChooser.addFileType(
				XMLSubjectRepositoryGem.UML2Z_SUFFIX_NO_DOT,
				IconLoader.loadIcon("evolve-compressed-file.png"),
				"Evolve compressed model file");
		CustomisedFileChooser.addFileType(
				XMLSubjectRepositoryGem.UML2_EXPORT_NO_DOT,
				IconLoader.loadIcon("evolve-exported-file.png"),
				"Evolve export file");
		CustomisedFileChooser.addFileType(
				XMLSubjectRepositoryGem.UML2DB_SUFFIX_NO_DOT,
				IconLoader.loadIcon("evolve-database-file.png"),
				"Evolve local database model file");
	}

	private static void registerPreferenceSlots()
	{
		// add location slots
		GlobalPreferences.preferences.addPreferenceSlot(new Preference("Locations", "Initial XML repository"),
				new PreferenceTypeFile(), "The XML file that will be loaded on startup.");
		GlobalPreferences.preferences.addPreferenceSlot(OPEN_DIAGRAMS, new PreferenceTypeInteger(),
				"The maximum number of unviewed, but unmodified, diagrams that are allowed to be open.");
		GlobalPreferences.preferences.addPreferenceSlot(BasicDiagramGem.BACKGROUND_VIEW_UPDATES, new PreferenceTypeBoolean(),
				"Perform any view updates that might be slow in the background, giving a faster response time, but is less tested.");
		GlobalPreferences.preferences.addPreferenceSlot(ToolCoordinatorGem.UNDO_REDO_SIZE, new PreferenceTypeInteger(),
				"The number of undo/redo commands stored.");
		BrowserInvoker.registerPreferenceSlots();
		RepositoryBrowserGem.registerPreferenceSlots();
	}

	private static void setUpUUIDGenerator()
	{
		// set up the global uuid generator
		GlobalUUIDGenerator.GlOBAL_UUID_GENERATOR = new IUUIDGenerator()
		{
			public String generateUUID(Object object)
			{
				return "" + UUID.randomUUID();
			}
		};
	}

	public Evolve()
	{
		windowCoordinator = new ApplicationWindowCoordinatorGem("Evolve");
	}

	private void setUpGUI(boolean logToFile)
	{
		windowCoordinator.setUp(toolManager, errors, logToFile);
	}

	private void showGUI()
	{
		windowCoordinator.getApplicationWindowCoordinatorFacet().openNewApplicationWindow();
	}

	private void setUpServices(String home)
	{
		// make the diagram registry
		int max = GlobalPreferences.preferences.getRawPreference(OPEN_DIAGRAMS).asInteger();
		max = Math.max(Math.min(max, 20), 0);
		BasicDiagramRegistryGem registryGem = new BasicDiagramRegistryGem(max);
		registryGem.connectBasicDiagramRecreatorFacet(new BasicDiagramRecreatorGem().getBasicDiagramRecreatorFacet());
		GlobalDiagramRegistry.registry = registryGem.getDiagramRegistryFacet();

		// start with an empty XML repository
		String initialXMLRepository = GlobalPreferences.preferences.getRawPreference(
				new Preference("Locations", "Initial XML repository")).asString();
		
		try
		{			
			boolean useBase = false;
			if (initialXMLRepository == null || !new File(initialXMLRepository).exists())
			{
				initialXMLRepository = home + "/models/base" + XMLSubjectRepositoryGem.UML2_SUFFIX;
				useBase = true;
			}
			
			// we need the delta engine for setting up the model
			ToolCoordinatorGem.clearDeltaEngine();
			
			if (!new File(initialXMLRepository).exists())
			{
				defaultToBasicRepository(initialXMLRepository);
			}
			else
			{
				RepositoryUtility.useXMLRepository(initialXMLRepository, !useBase);
				// the delta engine needs to be cleared, as it gets into an odd state
				// during CommonRepositoryFunctions.initializeModel()
				ToolCoordinatorGem.clearDeltaEngine();
			}				
		}
		catch (RepositoryOpeningException e)
		{
			defaultToBasicRepository(initialXMLRepository);
		}

		// make the tool manager and connect it up
		toolManager = new ToolCoordinatorGem();
	}
	
	private void defaultToBasicRepository(String failedRepository)
	{
		JOptionPane.showOptionDialog(
				null, "Cannot open repository: " + failedRepository + ", defaulting to empty model",
				"Repository loading error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null);
		try
		{
			RepositoryUtility.useXMLRepository(null, false);
		}
		catch (RepositoryOpeningException ex)
		{
			// can't happen with null repository
		}
		ToolCoordinatorGem.clearDeltaEngine();
	}
}