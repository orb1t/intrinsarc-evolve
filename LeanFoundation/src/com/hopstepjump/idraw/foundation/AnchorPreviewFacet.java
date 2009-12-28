package com.hopstepjump.idraw.foundation;

import com.hopstepjump.gem.*;
import com.hopstepjump.geometry.*;

import edu.umd.cs.jazz.*;


public interface AnchorPreviewFacet extends Facet
{
	/** form a view to show whether linking is going to be allowed or not */
  public ZNode formAnchorHighlight(boolean showOk);

  public UPoint calculateBoundaryPoint(OrientedPoint oriented, boolean linkFromContained, UPoint possibleBoxPoint, UPoint insidePoint);

  /**returns the linkable bounds given that the next point on the line is nextPointAfterConnection*/
  public UBounds getLinkableBounds(OrientedPoint nextPointAfterConnection);
  public UPoint getMiddlePoint();
  public boolean removeKinksOfLinkingIfIntersects();

  public void addLinked(LinkingPreviewFacet linked);
  public void removeLinked(LinkingPreviewFacet linked);
  
  public PreviewFacet getPreviewFacet();
}