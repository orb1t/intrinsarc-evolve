package com.hopstepjump.jumble.umldiagrams.basicnamespacenode;

import java.awt.*;
import java.awt.geom.*;

import com.hopstepjump.gem.*;
import com.hopstepjump.geometry.*;
import com.hopstepjump.idraw.foundation.*;
import com.hopstepjump.idraw.nodefacilities.previewsupport.*;
import com.hopstepjump.idraw.utility.*;

import edu.umd.cs.jazz.*;
import edu.umd.cs.jazz.component.*;



public final class BasicNamespacePreviewGem implements Gem
{
  private UBounds containerArea;
  private UBounds bounds;
  private UBounds originalBounds;

  private BasicNamespaceNodeFacet featurelessFacet;
  
  private PreviewFacet previewFacet;
  private PreviewCacheFacet previews;
  private BasicNodeAppearanceFacetImpl appearanceFacet = new BasicNodeAppearanceFacetImpl();
  private ContainerPreviewFacetImpl containerFacet = new ContainerPreviewFacetImpl();
  
  private class ContainerPreviewFacetImpl implements ContainerPreviewFacet
  {
	  public ZNode formContainerHighlight(boolean showOk)
	  {
	    ZRectangle rect = new ZRectangle(containerArea);
	    rect.setFillPaint(null);
	
			if (showOk)
			{
	      rect.setPenPaint(new Color(200, 200, 250));
	      rect.setStroke(new BasicStroke(2, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 0, new float[]{0,2}, 0));
			}
			else
			{
	      rect.setPenPaint(Color.red);
	      rect.setStroke(new BasicStroke(2, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 0, new float[]{0,2}, 0));
			}
	
	    return new ZVisualLeaf(rect);
	  }
	
		/**
		 * @see com.hopstepjump.idraw.foundation.ContainerPreviewFacet#recalculateSizeForContainables()
		 */
		public void recalculateSizeForContainables()
		{
	  	UBounds newBounds = featurelessFacet.getBoundsAfterExistingContainablesAlter(previews);
	    previewFacet.setFullBounds(newBounds, true);
		}

	  public void adjustForExistingContainables(ContainedPreviewFacet[] exists, UPoint movePoint)
	  {
	  	UBounds newBounds = featurelessFacet.getBoundsAfterExistingContainablesAlter(previews);
	    previewFacet.setFullBounds(newBounds, true);
	  }
	
	  public void restoreSizeForContainables()
	  {
	    previewFacet.setFullBounds(originalBounds, true);
	  }
	
	  public void resizeToRemoveContainables(ContainedPreviewFacet[] takes, UPoint movePoint)
	  {
			// never called
	  }
	
	  public void resizeToAddContainables(ContainedPreviewFacet[] adds, UPoint movePoint)
	  {
			// never called
	  }
	    
	  public PreviewFacet getPreviewFacet()
	  {
	  	return previewFacet;
	  }
		/**
		 * @see com.hopstepjump.jumble.foundation.ContainerPreviewFacet#boundsHaveBeenSet()
		 */
		public void boundsHaveBeenSet(UBounds oldBounds, UBounds newBounds, boolean resizedNotMoved)
		{
			featurelessFacet.tellContainedAboutResize(previews, bounds);
		}

  }
  
  private class BasicNodeAppearanceFacetImpl implements BasicNodePreviewAppearanceFacet
  {
	  public UPoint calculateBoundaryPoint(PreviewCacheFacet previews, OrientedPoint oriented,  boolean linkFromContained, UPoint boxPoint, UPoint insidePoint)
	  {
	  	return featurelessFacet.calculateBoundaryPoint(previews, bounds, oriented, linkFromContained, boxPoint, insidePoint);
	  }

		public ZNode formView(boolean debugOnly)
		{
	    ZGroup group = new ZGroup();
	    Shape shape = featurelessFacet.formShapeForPreview(bounds);
	    GeneralPath path = new GeneralPath(shape);
	    ZPath zPath = new ZPath(path);
	    zPath.setFillPaint(null);
			zPath.setPenPaint(ScreenProperties.getPreviewColor());
	    zPath.setStroke(new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 0, new float[]{0,2}, 0));
	    group.addChild(new ZVisualLeaf(zPath));
	    return group;
		}
		
	  public ZNode formAnchorHighlight(boolean showOk)
	  {
	    ZGroup group = new ZGroup();
	
	    UBounds anchorBounds = bounds.addToPoint(new UDimension(-5, -5)).addToExtent(new UDimension(10, 10));
	
	    ZRectangle rect = new ZRectangle(anchorBounds);
	    rect.setFillPaint(null);
	
			if (showOk)
			{
        rect.setPenPaint(new Color(200, 250, 200));
        rect.setStroke(new BasicStroke(2, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 0, new float[]{0,2}, 0));
			}
			else
			{
        rect.setPenPaint(new Color(250, 200, 200));
        rect.setStroke(new BasicStroke(2, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 0, new float[]{0,2}, 0));
			}
	
	    group.addChild(new ZVisualLeaf(rect));
	    return group;
	  }
	  
		/**
		 * @see com.hopstepjump.idraw.nodefacilities.previewsupport.BasicNodePreviewAppearanceFacet#getFullBounds()
		 */
		public UBounds getFullBounds()
		{
			return bounds;
		}

		/**
		 * @see com.hopstepjump.idraw.nodefacilities.previewsupport.BasicNodePreviewAppearanceFacet#getFullBoundsForContainment()
		 */
		public UBounds getFullBoundsForContainment()
		{
			return featurelessFacet.getContainmentBounds(bounds);
		}

		/**
		 * @see com.hopstepjump.idraw.nodefacilities.previewsupport.BasicNodePreviewAppearanceFacet#restoreOriginalBounds()
		 */
		public void restoreOriginalBounds()
		{
			bounds = originalBounds;
		}

		/**
		 * @see com.hopstepjump.idraw.nodefacilities.previewsupport.BasicNodePreviewAppearanceFacet#setBounds(UBounds)
		 */
		public void setBounds(UBounds newBounds, boolean resizedNotMoved)
		{
			bounds = newBounds;
		}

		/**
		 * @see com.hopstepjump.idraw.nodefacilities.previewsupport.BasicNodePreviewAppearanceFacet#getOffsetFromOriginal()
		 */
		public UDimension getOffsetFromOriginal()
		{
			return bounds.getPoint().subtract(originalBounds.getPoint());
		}
	}

  public BasicNamespacePreviewGem(UBounds bounds, UBounds containerArea, double tabHeight)
  {
    this.bounds = bounds;
    originalBounds = bounds;
    this.containerArea = containerArea;
  }
  
  public void connectPreviewFacet(PreviewFacet previewFacet)
  {
  	this.previewFacet = previewFacet;
  }
  
  public void connectFeaturelessClassifierNodeFacet(BasicNamespaceNodeFacet packageFacet)
  {
  	this.featurelessFacet = packageFacet;
  }
  
  public void connectPreviewCacheFacet(PreviewCacheFacet previews)
  {
  	this.previews = previews;
  }

	public BasicNodePreviewAppearanceFacet getBasicNodePreviewAppearanceFacet()
	{
		return appearanceFacet;
	}
	
	public ContainerPreviewFacet getContainerPreviewFacet()
	{
		return containerFacet;
	}

}