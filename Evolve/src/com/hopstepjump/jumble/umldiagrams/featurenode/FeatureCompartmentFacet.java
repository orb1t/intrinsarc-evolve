package com.hopstepjump.jumble.umldiagrams.featurenode;

import java.util.*;

import com.hopstepjump.geometry.*;
import com.hopstepjump.idraw.figures.simplecontainernode.*;
import com.hopstepjump.idraw.foundation.*;

/**
 *
 * (c) Andrew McVeigh 01-Aug-02
 *
 */
public interface FeatureCompartmentFacet extends SimpleDeletedUuidsFacet
{
	public UDimension getMinimumExtent();
	public FigureFacet getFigureFacet();
	public boolean isEmpty();
	public UDimension formNewDimensions(PreviewCacheFacet previews, List<PreviewFacet> sortedOps, UPoint reference);
	public List<PreviewFacet> getSortedOperations(PreviewCacheFacet previews, ContainedPreviewFacet[] add, ContainedPreviewFacet[] remove, UPoint movePoint);
}
