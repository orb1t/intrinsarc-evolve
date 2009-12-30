/**
 * FeatureSuppressToggleManipulator.java
 * 
 * @author Andrew McVeigh
 */

package com.hopstepjump.jumble.umldiagrams.classifiernode;

import org.eclipse.uml2.*;

import com.hopstepjump.geometry.*;
import com.hopstepjump.idraw.foundation.*;
import com.hopstepjump.idraw.utility.*;
import com.hopstepjump.jumble.umldiagrams.featurenode.*;

import edu.umd.cs.jazz.*;
import edu.umd.cs.jazz.event.*;
import edu.umd.cs.jazz.util.*;

public class FeatureSuppressToggleManipulator extends ManipulatorAdapter
{
  private ZGroup diagramLayer;
  private ZGroup group;
  private int featureType;
  private String featureName;
  private boolean featureSuppressed;
  private ManipulatorListenerFacet listener;
  private UPoint location;
  private int size;
  private FigureReference reference;
  private String name;

  public FeatureSuppressToggleManipulator(FigureReference reference, String name, UPoint location, int size,
      int featureType, String featureName, boolean featureSuppressed)
  {
    this.reference = reference;
    this.name = name;
    this.featureType = featureType;
    this.featureName = featureName;
    this.featureSuppressed = featureSuppressed;
    this.location = location;
    this.size = size;
  }

  /** add the view to the display */
  public void addToView(ZGroup diagramLayer, ZCanvas canvas)
  {
    this.diagramLayer = diagramLayer;

    ZNode icon = FeatureNodeGem.makeIcon(
        featureSuppressed ? VisibilityKind.PUBLIC_LITERAL : VisibilityKind.PRIVATE_LITERAL,
        false,
        location,
        size,
        null,
        featureType,
        false);
    group = new ZGroup();
    group.addChild(icon);

    group.putClientProperty("manipulator", this);
    group.setChildrenPickable(false);
    group.setChildrenFindable(false);

    diagramLayer.addChild(group);
  }

  /**
   * Method cleanUp
   *  
   */
  public void cleanUp()
  {
    diagramLayer.removeChild(group);
  }

  public int getType()
  {
    return TYPE2;
  }

  public void enterViaButtonPress(ManipulatorListenerFacet listener, ZNode source, UPoint point, int modifiers)
  {
    this.listener = listener;
  }

  /** Invoked when a mouse button has been released on this figure */
  public void mouseReleased(FigureFacet over, UPoint point, ZMouseEvent event)
  {
    listener.haveFinished(makeToggleSuppressFeaturesCommand(reference, name, featureType, featureName,
        featureSuppressed));
  }

  /**
   * make a command to allow feature suppression
   */
  public static Command makeToggleSuppressFeaturesCommand(FigureReference reference, String figureName, int featureType,
      String featureName, boolean suppressed)
  {
    return new SuppressFeaturesCommand(
        reference, 
        featureType, 
        !suppressed, (suppressed ? "showed " : "hid ") + " "
        	+ featureName + " for " + figureName, (!suppressed ? "showed " : "hid ") + " " + featureName + " for "
        	+ figureName);
  }

  public void setLayoutOnly()
  {
  }
}