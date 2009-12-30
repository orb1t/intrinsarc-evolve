package com.hopstepjump.jumble.umldiagrams.packagenode;

import java.awt.*;

import org.eclipse.uml2.*;
import org.eclipse.uml2.Package;

import com.hopstepjump.gem.*;
import com.hopstepjump.geometry.*;
import com.hopstepjump.idraw.foundation.*;
import com.hopstepjump.idraw.foundation.persistence.*;
import com.hopstepjump.idraw.nodefacilities.creationbase.*;
import com.hopstepjump.idraw.nodefacilities.nodesupport.*;
import com.hopstepjump.jumble.umldiagrams.basicnamespacenode.*;
import com.hopstepjump.repositorybase.*;


public final class PackageCreatorGem implements Gem
{
	public static final String NAME = "Package";
	private Color fillColor =  new Color(240, 250, 255);
	private static final String FIGURE_NAME = "package";
	private NodeCreateFacet nodeCreateFacet = new NodeCreateFacetImpl();
	private boolean autoSized;
  private boolean relaxed;
  private String stereotype;
  private boolean displayOnlyIcon;
	
  public PackageCreatorGem(boolean autoSized)
  {
  	this.autoSized = autoSized;
  }

	public NodeCreateFacet getNodeCreateFacet()
	{
		return nodeCreateFacet;
	}
	
	private class NodeCreateFacetImpl implements NodeCreateFacet
	{
	  public String getFigureName()
	  {
	    return FIGURE_NAME;
	  }
	
    public Object createNewSubject(Object previouslyCreated, DiagramFacet diagram, FigureReference containingReference, Object relatedSubject, PersistentProperties properties)
    {
      SubjectRepositoryFacet repository = GlobalSubjectRepository.repository;

      // possibly resurrect
      if (previouslyCreated != null)
      {
        repository.decrementPersistentDelete((Element) previouslyCreated);
        return previouslyCreated;
      }

      // find a possible nested package
      Package owner = (Package) diagram.getLinkedObject();
      
      // see if we can find a better owner, based on the containing figure
      if (containingReference != null)
      {
        FigureFacet container = GlobalDiagramRegistry.registry.retrieveFigure(containingReference);
        Package possibleOwner = (Package) repository.findOwningElement(
            container.getContainerFacet().getFigureFacet().getFigureReference(), UML2Package.eINSTANCE.getPackage());
        if (possibleOwner != null)
          owner = possibleOwner;
      }
      
      // get the package associated with this diagram, and add the new package to it
      Package pkg = owner.createChildPackages(UML2Package.eINSTANCE.getPackage());
      
      // should we set a stereotype?
      String stereoName = properties.retrieve(">stereotype", "").asString();
      if (stereoName != null)
      {
        Stereotype stereo = GlobalSubjectRepository.repository.findStereotype(
            UML2Package.eINSTANCE.getPackage(), stereoName);
        if (stereo != null)
          pkg.getAppliedBasicStereotypes().add(stereo);
      }

      // should we set relaxed on a possible stratum?
      boolean relaxed = properties.retrieve(">relaxed", false).asBoolean();
      if (relaxed)
      {
        StereotypeUtilities.formSetBooleanRawStereotypeAttributeCommand(
            pkg, CommonRepositoryFunctions.RELAXED, relaxed).execute(false);
      }

      return pkg;
    }

    public void uncreateNewSubject(Object previouslyCreated)
    {
      GlobalSubjectRepository.repository.incrementPersistentDelete((Element) previouslyCreated);
    }

    public Object createFigure(Object subject, DiagramFacet diagram, String figureId, UPoint location, PersistentProperties properties)
	  {
      NamedElement owner = (NamedElement) ((subject == null) ? null : ((Package) subject).getOwner());
      PersistentProperties actualProperties = new PersistentProperties(properties);
      initialiseExtraProperties(actualProperties);
      
	  	BasicNodeGem basicGem = new BasicNodeGem(getRecreatorName(), diagram, figureId, location, actualProperties.retrieve(">autoSized", false).asBoolean(), false);
	  	BasicNamespaceNodeGem packageGem = new BasicNamespaceNodeGem(
          (Package) subject,
          owner == null ? "" : owner.getName(),
              diagram,
              figureId,
              FIGURE_NAME,
              actualProperties.retrieve(">fillColor").asColor(),
              actualProperties.retrieve(">displayOnlyIcon").asBoolean());
			basicGem.connectBasicNodeAppearanceFacet(packageGem.getBasicNodeAppearanceFacet());
			basicGem.connectBasicNodeContainerFacet(packageGem.getBasicNodeContainerFacet());
			packageGem.connectBasicNodeFigureFacet(basicGem.getBasicNodeFigureFacet());
			PackageAppearanceGem appearanceGem = new PackageAppearanceGem();
			packageGem.connectFeaturelessClassifierAppearanceFacet(appearanceGem.getPackageAppearanceFacet());
			appearanceGem.connectFeaturelessClassifierNodeFacet(packageGem.getBasicNamespaceNodeFacet());

      PackageMiniAppearanceGem miniGem = new PackageMiniAppearanceGem();
      miniGem.connectFigureFacet(basicGem.getBasicNodeFigureFacet());
      miniGem.connectBasicNamespaceNodeFacet(packageGem.getBasicNamespaceNodeFacet());
			packageGem.connectBasicNamespaceMiniAppearanceFacet(miniGem.getBasicNamespaceMiniAppearanceFacet());

	
	    diagram.add(basicGem.getBasicNodeFigureFacet());
	    
	    return new FigureReference(diagram, figureId);
	  }
    
	  public void unCreateFigure(Object memento)
	  {
	    FigureReference figureReference = (FigureReference) memento;
	    DiagramFacet diagram = GlobalDiagramRegistry.registry.retrieveOrMakeDiagram(figureReference.getDiagramReference());
	    FigureFacet figure = GlobalDiagramRegistry.registry.retrieveFigure(figureReference);
	    diagram.remove(figure);
	  }
	  
		/**
		 * @see com.hopstepjump.idraw.foundation.PersistentFigureRecreator#getFullName()
		 */
		public String getRecreatorName()
		{
			return NAME;
		}

		public FigureFacet createFigure(DiagramFacet diagram, PersistentFigure figure)
		{
	  	BasicNodeGem basicGem = new BasicNodeGem(getRecreatorName(), diagram, figure, false);
	  	BasicNamespaceNodeGem packageGem = new BasicNamespaceNodeGem(FIGURE_NAME, figure.getSubject(), figure.getProperties());
			basicGem.connectBasicNodeAppearanceFacet(packageGem.getBasicNodeAppearanceFacet());
			basicGem.connectBasicNodeContainerFacet(packageGem.getBasicNodeContainerFacet());
			packageGem.connectBasicNodeFigureFacet(basicGem.getBasicNodeFigureFacet());
			PackageAppearanceGem appearanceGem = new PackageAppearanceGem();
			packageGem.connectFeaturelessClassifierAppearanceFacet(appearanceGem.getPackageAppearanceFacet());
			appearanceGem.connectFeaturelessClassifierNodeFacet(packageGem.getBasicNamespaceNodeFacet());
      
      PackageMiniAppearanceGem miniGem = new PackageMiniAppearanceGem();
      miniGem.connectFigureFacet(basicGem.getBasicNodeFigureFacet());
      miniGem.connectBasicNamespaceNodeFacet(packageGem.getBasicNamespaceNodeFacet());
      packageGem.connectBasicNamespaceMiniAppearanceFacet(miniGem.getBasicNamespaceMiniAppearanceFacet());
	
	    return basicGem.getBasicNodeFigureFacet();
		}
		
		public void initialiseExtraProperties(PersistentProperties properties)
		{
      properties.addIfNotThere(new PersistentProperty(">stereotype", stereotype));
      properties.addIfNotThere(new PersistentProperty(">displayOnlyIcon", displayOnlyIcon, false));
      properties.addIfNotThere(new PersistentProperty(">fillColor", fillColor, fillColor));
      properties.addIfNotThere(new PersistentProperty(">relaxed", relaxed, false));
      properties.addIfNotThere(new PersistentProperty(">autoSized", autoSized, false));
		}
	}

  public void setStereotype(String stereotypeName)
  {
    this.stereotype = stereotypeName;
  }

  public void setFillColor(Color fillColor)
  {
    this.fillColor = fillColor;
  }

  public void setRelaxed(boolean relaxed)
  {
    this.relaxed = relaxed;
  }

  public void setDisplayOnlyIcon(boolean displayOnlyIcon)
  {
    this.displayOnlyIcon = displayOnlyIcon;
  }
}