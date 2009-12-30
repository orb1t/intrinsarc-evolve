package com.hopstepjump.idraw.arcfacilities.arcsupport;

import java.io.*;
import java.util.*;

import com.hopstepjump.idraw.foundation.*;

/**
 *
 * (c) Andrew McVeigh 28-Jul-02
 *
 */
final class BasicArcState implements Serializable
{
	DiagramFacet diagram;
	DiagramReference diagramReference;
  String id;
  boolean showing;
  boolean curved;
 	CalculatedArcPoints calculatedPoints;
 	Set<LinkingFacet> linked;
 	
 	/** for persistence */
 	PersistentFigureRecreatorFacet recreatorFacet;

	LinkingFacet            linkingFacet;
	FigureFacet             figureFacet;
	BasicArcAppearanceFacet appearanceFacet;
  CurvableFacet           curvableFacet;
  
	/** anchor facet is optional */
  AnchorFacet             anchorFacet;
  /** container facet is optional */
  ContainerFacet          containerFacet;
  
  /** advanced arcs, optional */
  AdvancedArcFacet        advancedFacet;
  ClipboardCommandsFacet  clipboardCommandsFacet;
}