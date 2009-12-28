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

/**
 *
 * (c) Andrew McVeigh 27-Aug-02
 *
 */
public class ModelCreatorGem implements Gem
{
  public static final String NAME = "Model";
  private static final Color FILL_COLOR =  new Color(255, 213, 255);
  private static final String FIGURE_NAME = "model";
  private NodeCreateFacet nodeCreateFacet = new NodeCreateFacetImpl();
  private boolean autoSized;
  
  public ModelCreatorGem(boolean autoSized)
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

      // no nested classes currently supported
      Package owner = (Package) diagram.getLinkedObject();
      
      // see if we can find a better owner, based on the containing figure
      if (containingReference != null)
      {
        FigureFacet container = GlobalDiagramRegistry.registry.retrieveFigure(containingReference);
        owner = (Package) repository.findVisuallyOwningNamespace(diagram, container.getContainerFacet());
      }
      
      // get the package associated with this diagram, and add the new package to it
      Model pkg = (Model) owner.createChildPackages(UML2Package.eINSTANCE.getModel());
      return pkg;
    }

    public void uncreateNewSubject(Object previouslyCreated)
    {
      GlobalSubjectRepository.repository.incrementPersistentDelete((Element) previouslyCreated);
    }

    public Object createFigure(Object subject, DiagramFacet diagram, String figureId, UPoint location, PersistentProperties properties)
    {
      NamedElement owner = (NamedElement) ((subject == null) ? null : ((Package) subject).getOwner());
      
      BasicNodeGem basicGem = new BasicNodeGem(getRecreatorName(), diagram, figureId, location, autoSized, false);
      BasicNamespaceNodeGem packageGem = new BasicNamespaceNodeGem((Package) subject, owner == null ? "" : owner.getName(), diagram, figureId, FIGURE_NAME, FILL_COLOR, false);
      basicGem.connectBasicNodeAppearanceFacet(packageGem.getBasicNodeAppearanceFacet());
      basicGem.connectBasicNodeContainerFacet(packageGem.getBasicNodeContainerFacet());
      packageGem.connectBasicNodeFigureFacet(basicGem.getBasicNodeFigureFacet());
      ModelMiniAppearanceGem mini = new ModelMiniAppearanceGem();
      packageGem.connectBasicNamespaceMiniAppearanceFacet(mini.getBasicNamespaceMiniAppearanceFacet());
      mini.connectFigureFacet(basicGem.getBasicNodeFigureFacet());
      PackageAppearanceGem appearanceGem = new PackageAppearanceGem();
      packageGem.connectFeaturelessClassifierAppearanceFacet(appearanceGem.getPackageAppearanceFacet());
      appearanceGem.connectFeaturelessClassifierNodeFacet(packageGem.getBasicNamespaceNodeFacet());

  
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
      ModelMiniAppearanceGem mini = new ModelMiniAppearanceGem();
      packageGem.connectBasicNamespaceMiniAppearanceFacet(mini.getBasicNamespaceMiniAppearanceFacet());
      mini.connectFigureFacet(basicGem.getBasicNodeFigureFacet());
      PackageAppearanceGem appearanceGem = new PackageAppearanceGem();
      packageGem.connectFeaturelessClassifierAppearanceFacet(appearanceGem.getPackageAppearanceFacet());
      appearanceGem.connectFeaturelessClassifierNodeFacet(packageGem.getBasicNamespaceNodeFacet());
  
      return basicGem.getBasicNodeFigureFacet();
    }
    
		public void initialiseExtraProperties(PersistentProperties properties)
		{
		}
  }
}