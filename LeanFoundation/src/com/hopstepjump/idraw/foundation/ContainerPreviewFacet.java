package com.hopstepjump.idraw.foundation;

import com.hopstepjump.gem.*;
import com.hopstepjump.geometry.*;

import edu.umd.cs.jazz.*;

public interface ContainerPreviewFacet extends Facet
{
  public ZNode formContainerHighlight(boolean showOk);
  public void resizeToAddContainables(ContainedPreviewFacet[] adds, UPoint movePoint);
  public void resizeToRemoveContainables(ContainedPreviewFacet[] takes, UPoint movePoint);
	public void adjustForExistingContainables(ContainedPreviewFacet[] exists, UPoint movePoint);
	public void recalculateSizeForContainables();

  public void restoreSizeForContainables();
  public void boundsHaveBeenSet(UBounds oldBounds, UBounds newBounds, boolean resizedNotMoved);
  
  public PreviewFacet getPreviewFacet();
}