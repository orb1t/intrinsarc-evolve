package com.hopstepjump.jumble.umldiagrams.classifiernode;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import org.eclipse.uml2.*;
import org.eclipse.uml2.Class;
import org.eclipse.uml2.Package;
import org.eclipse.uml2.impl.*;

import com.hopstepjump.backbone.nodes.*;
import com.hopstepjump.deltaengine.base.*;
import com.hopstepjump.gem.*;
import com.hopstepjump.geometry.*;
import com.hopstepjump.idraw.diagramsupport.moveandresize.*;
import com.hopstepjump.idraw.figurefacilities.selectionbase.*;
import com.hopstepjump.idraw.figurefacilities.textmanipulation.*;
import com.hopstepjump.idraw.figurefacilities.textmanipulationbase.*;
import com.hopstepjump.idraw.figurefacilities.update.*;
import com.hopstepjump.idraw.figures.simplecontainernode.*;
import com.hopstepjump.idraw.foundation.*;
import com.hopstepjump.idraw.foundation.persistence.*;
import com.hopstepjump.idraw.nodefacilities.nodesupport.*;
import com.hopstepjump.idraw.nodefacilities.previewsupport.*;
import com.hopstepjump.idraw.nodefacilities.resize.*;
import com.hopstepjump.idraw.nodefacilities.resizebase.*;
import com.hopstepjump.idraw.nodefacilities.style.*;
import com.hopstepjump.idraw.utility.*;
import com.hopstepjump.jumble.deltaview.*;
import com.hopstepjump.jumble.expander.*;
import com.hopstepjump.jumble.gui.lookandfeel.*;
import com.hopstepjump.jumble.packageview.base.*;
import com.hopstepjump.jumble.umldiagrams.base.*;
import com.hopstepjump.jumble.umldiagrams.basicnamespacenode.*;
import com.hopstepjump.jumble.umldiagrams.constituenthelpers.*;
import com.hopstepjump.jumble.umldiagrams.dependencyarc.*;
import com.hopstepjump.jumble.umldiagrams.featurenode.*;
import com.hopstepjump.jumble.umldiagrams.portnode.*;
import com.hopstepjump.jumble.umldiagrams.slotnode.*;
import com.hopstepjump.repository.*;
import com.hopstepjump.repositorybase.*;
import com.hopstepjump.swing.*;
import com.hopstepjump.swing.enhanced.*;
import com.hopstepjump.uml2deltaengine.*;
import com.hopstepjump.uml2deltaengine.converters.*;

import edu.umd.cs.jazz.*;
import edu.umd.cs.jazz.component.*;


public final class ClassifierNodeGem implements Gem
{
  private static final ImageIcon ATTRIBUTE = IconLoader.loadIcon("tree-private-attribute.png");
  private static final ImageIcon OPERATION = IconLoader.loadIcon("tree-public-operation.png");
  private static final ImageIcon ERROR_ICON = IconLoader.loadIcon("error.png");
  private static final ImageIcon DELTA_ICON = IconLoader.loadIcon("delta.png");
  private static final ImageIcon COMPOSITION_ICON = IconLoader.loadIcon("composition.png");


	private static final int QUICK_ICON_SIZE = 14;
	
	private Font font = ScreenProperties.getTitleFont();
	private Font packageFont = ScreenProperties.getSecondaryFont();
	
	private NamedElement subject;
	
	private Color fillColor;
  private Color initialFillColor;
	private Color lineColor = Color.BLACK;
	
  /** are we acting as a part? */
  private final boolean isPart;
  private final String figureName;
  
	/** persistent attributes */
	private boolean suppressAttributesOrSlots;
	private boolean suppressOperations;
	private boolean suppressContents = false;
	private boolean autoSized = true;
	private boolean displayOnlyIcon = false;
  private boolean showAsState = false;
	private String name = "";
	private boolean isAbstract = false;
	private boolean isActive = false;
	private UDimension rememberedTLOffset = new UDimension(0, 0);
	private UDimension rememberedBROffset = new UDimension(0, 0);
	
	private FigureFacet primitiveAttributesOrSlots;
	private FeatureCompartmentFacet attributesOrSlots;
	private FigureFacet primitiveOperations;
	private FeatureCompartmentFacet operations;
	private FigureFacet primitiveContents;
	private SimpleContainerFacet contents;
	private FigureFacet primitivePorts;
	private PortCompartmentFacet ports;
	private UBounds minVetBounds = null;
	private BasicNodeContainerFacet containerFacet = new ContainerFacetImpl();
	private BasicNodeAppearanceFacet appearanceFacet = new BasicNodeAppearanceFacetImpl();
	private TextableFacet textableFacet = new TextableFacetImpl();
	private ResizeVetterFacet resizeVetterFacet = new ResizeVetterFacetImpl();
	private SuppressFeaturesFacet suppressFeaturesFacet = new SuppressFeaturesFacetImpl();
	private BasicNodeAutoSizedFacetImpl autoSizedFacet = new BasicNodeAutoSizedFacetImpl();
	private ClassifierNodeFacetImpl classifierFacet = new ClassifierNodeFacetImpl();
	private DisplayAsIconFacet displayAsIconFacet = new DisplayAsIconFacetImpl();
  private ShowAsStateFacet showAsStateFacet = new ShowAsStateFacetImpl();
	private LocationFacet locationFacet = new LocationFacetImpl();
	private VisualLockFacetImpl lockFacet = new VisualLockFacetImpl();
	private BasicNodeFigureFacet figureFacet;
	private ClassifierMiniAppearanceFacet miniAppearanceFacet;
  private boolean showStereotype = true;
	
  private String owner = "";
  private SuppressOwningPackageFacet showOwningPackageFacet = new SuppressOwningPackageFacetImpl();
  private StylableFacet stylableFacet = new StylableFacetImpl();
  private HideContentsFacet hideContentsFacet = new HideContentsFacetImpl();
  private boolean showOwningPackage;
  private boolean forceSuppressOwningPackage = false;
  private int stereotypeHashcode;
  private UpdateViewFacet updateViewFacet = new UpdateViewFacetImpl();
  private SimpleDeletedUuidsFacetImpl deletedConnectorUuidsFacet = new SimpleDeletedUuidsFacetImpl();
  private Set<String> addedUuids = new HashSet<String>();
  private Set<String> deletedUuids = new HashSet<String>();
  private boolean attributeEllipsis = false;
  private boolean operationEllipsis = false;
  private boolean bodyEllipsis = false;
  private boolean retired = false;
	private SwitchSubjectFacet switchableFacet = new SwitchSubjectFacetImpl();
	private boolean locked;
	
	private class SwitchSubjectFacetImpl implements SwitchSubjectFacet
	{

		public Object switchSubject(Object newSubject)
		{
			Object old = subject;
			subject = (NamedElement) newSubject;
			
			return new Object[]{old, refreshSuppressedAttributes(), refreshSuppressedPorts()};
		}

		public void unSwitchSubject(Object memento)
		{
			Object[] obj = (Object[]) memento;
			subject = (NamedElement) obj[0];
			if (!isPart)
				attributesOrSlots.setAddedAndDeleted((Set<String>[]) obj[1]);
			ports.setAddedAndDeleted((Set<String>[]) obj[2]);
		}
		
	};
  
  private class SimpleDeletedUuidsFacetImpl implements SimpleDeletedUuidsFacet
  {
		public void addDeleted(String uuid)
		{
			if (!addedUuids.remove(uuid))
				deletedUuids.add(uuid);
		}

		public void removeDeleted(String uuid)
		{
			if (!deletedUuids.remove(uuid))
				addedUuids.add(uuid);
		}

		public void clean(Set<String> visuallySuppressedUuids, Set<String> uuids)
		{
			addedUuids.retainAll(visuallySuppressedUuids);
			deletedUuids.removeAll(visuallySuppressedUuids);
			deletedUuids.retainAll(uuids);
		}

		public Set<String>[] getAddedAndDeleted()
		{
			return (Set<String>[]) new Set[]{new HashSet<String>(addedUuids), new HashSet<String>(deletedUuids)};
		}

		public boolean isDeleted(Set<String> visuallySuppressedUuids, String uuid)
		{
			if (addedUuids.contains(uuid))
				return false;
			return visuallySuppressedUuids.contains(uuid) || deletedUuids.contains(uuid);
		}

		public void resetToDefaults()
		{
			addedUuids.clear();
			deletedUuids.clear();
		}

		public void setAddedAndDeleted(Set<String>[] addedAndDeletedUuids)
		{
			if (addedAndDeletedUuids == null)
				resetToDefaults();
			else
			{
				addedUuids = addedAndDeletedUuids[0];
				deletedUuids = addedAndDeletedUuids[1];
			}
		}

		public void setToShowAll(Set<String> visuallySuppressedUuids)
		{
			addedUuids = visuallySuppressedUuids;
			deletedUuids.clear();
		}
  }

  private class UpdateViewFacetImpl implements UpdateViewFacet
  {
    public Object updateViewAfterSubjectChanged(boolean isTop)
    {
      if (isPart)
        return updatePartViewAfterSubjectChanged(isTop);
      else
        return updateClassifierViewAfterSubjectChanged(isTop);
    }

    public void unUpdateViewAfterSubjectChanged(Object memento)
    {
      if (isPart)
        unUpdatePartViewAfterSubjectChanged(memento);
      else
        unUpdateClassifierViewAfterSubjectChanged(memento);
    }

    private void refreshEllipsis()
    {
      ClassifierSizeInfo info = makeCurrentInfo();
      attributeEllipsis = isPart ? false : info.isEllipsisForAttributes();
      operationEllipsis = isPart ? false : info.isEllipsisForOperations();
      bodyEllipsis = isPart ? false : info.isEllipsisForBody();
    }
    
    private Object updateClassifierViewAfterSubjectChanged(boolean isTop)
    {
      final int actualStereotypeHashcode = StereotypeUtilities.calculateStereotypeHashAndComponentHash(figureFacet, subject);

      // should we be displaying the owner?
      ElementProperties props = new ElementProperties(figureFacet);
      boolean sub = props.getElement().isSubstitution();
      boolean locatedInCorrectView = props.getPerspective() == props.getElement().getHomeStratum();

      final boolean shouldBeDisplayingOwningPackage =
      	!locatedInCorrectView && !forceSuppressOwningPackage || sub;

      // is this active?
      boolean subjectActive = false;
      if (subject instanceof Class)
      {
        Class actualClassSubject = (Class) subject;
        subjectActive = actualClassSubject.isActive();
      }
      final boolean actuallyActive = subjectActive;

      final Classifier classifierSubject = (Classifier) subject;
      
      // get a possible name for a substituted element
      String newName = new ElementProperties(figureFacet, subject).getPerspectiveName();
      
      // preserve the old variables
      String oldName = name;
      String oldOwner = owner;
      boolean oldIsAbstract = isAbstract;
      boolean oldIsRetired = retired;
      int oldStereotypeHashcode = stereotypeHashcode;
      boolean oldIsActive = isActive;
      boolean oldAttributeEllipsis = attributeEllipsis;
      boolean oldOperationEllipsis = operationEllipsis;
      boolean oldBodyEllipsis = bodyEllipsis;
      boolean oldDisplayOnlyIcon = displayOnlyIcon;
      boolean oldAutosized = autoSized;
      boolean oldShowAsState = showAsState;
      refreshEllipsis();
      
      // set the new variables
      name = newName;
      isAbstract = classifierSubject.isAbstract();
      retired = isElementRetired();
      isActive = actuallyActive;
      showOwningPackage = shouldBeDisplayingOwningPackage;
      stereotypeHashcode = actualStereotypeHashcode;
      autoSized = shouldDisplayOnlyIcon() ? true : autoSized;
      
      // get the new stratum owner (or set to the original name in case of evolution)
      final String newOwner = sub ?
      	"(" + (retired ? "retires " : "replaces ") + props.getSubstitutesForName() + ")" :
      	"(from " + GlobalSubjectRepository.repository.getFullStratumNames((Element) props.getHomePackage().getRepositoryObject()) + ")";
      owner = newOwner;
      showAsState = StereotypeUtilities.isStereotypeApplied(subject, "state");
      
      // resize, using a text utility
      CompositeCommand cmd = new CompositeCommand("", "");
      Command icon = new DisplayAsIconCommand(figureFacet.getFigureReference(), shouldDisplayOnlyIcon(), "", "");
      icon.execute(true);
      cmd.addCommand(icon);
      cmd.addCommand(figureFacet.makeAndExecuteResizingCommand(textableFacet.vetTextResizedExtent(name)));
      
      
      return new Object[]{
          oldName, oldOwner, oldIsAbstract, oldIsRetired, oldStereotypeHashcode, oldIsActive, cmd,
          oldAttributeEllipsis, oldOperationEllipsis, oldBodyEllipsis, oldDisplayOnlyIcon,
          oldAutosized, oldShowAsState};
    }

		private void unUpdateClassifierViewAfterSubjectChanged(Object memento)
    {
      Object[] objects = (Object[]) memento;
      
      String oldName = (String) objects[0];
      String oldOwner = (String) objects[1];
      boolean oldIsAbstract = (Boolean) objects[2];
      boolean oldIsRetired = (Boolean) objects[3];
      int oldStereotypeHashcode = (Integer) objects[4];
      boolean oldIsActive = (Boolean) objects[5];
      Command resizing = (Command) objects[6];

      name = oldName;
      owner = oldOwner;
      isAbstract = oldIsAbstract;
      retired = oldIsRetired;
      stereotypeHashcode = oldStereotypeHashcode;
      isActive = oldIsActive;
      attributeEllipsis = (Boolean) objects[7];
      operationEllipsis = (Boolean) objects[8];
      bodyEllipsis = (Boolean) objects[9];
      displayOnlyIcon = (Boolean) objects[10];
      autoSized = (Boolean) objects[11];
      showAsState = (Boolean) objects[12];
      resizing.unExecute();
    }
    
    public Object updatePartViewAfterSubjectChanged(boolean isTop)
    {
      Property part = (Property) subject;
      Classifier type = (Classifier) part.undeleted_getType();
      final int actualStereotypeHashcode =
      	StereotypeUtilities.calculateStereotypeHash(figureFacet, subject) +
      	StereotypeUtilities.calculateStereotypeHashAndComponentHash(figureFacet, type);

      final boolean subjectActive = type == null || !(type instanceof Class) ? false : ((Class) type).isActive();     
      final boolean subjectAbstract = type == null ? false : type.isAbstract();

      String typeName = new ElementProperties(figureFacet, subject).getPerspectiveName();
      final String newName;
      if (subject.getName().length() == 0 && typeName.length() == 0)
        newName = "";
      else
        newName = subject.getName() + " : " + typeName;

      
      // preserve the old variables
      String oldName = name;
      boolean oldIsAbstract = subjectAbstract;
      boolean oldIsActive = subjectActive;
      int oldStereotypeHashcode = stereotypeHashcode;
      
      // set the new variables
      name = newName;
      isAbstract = subjectAbstract;
      isActive = subjectActive;
      if (type != null)
      	showAsState = StereotypeUtilities.isStereotypeApplied(type, "state");
      
      // resize, using a text utility
      stereotypeHashcode = actualStereotypeHashcode;
      Command resizing = figureFacet.makeAndExecuteResizingCommand(textableFacet.vetTextResizedExtent(name));
      return new Object[]{oldName, oldIsAbstract, oldStereotypeHashcode, oldIsActive, resizing, showAsState};
    }
      
    public void unUpdatePartViewAfterSubjectChanged(Object memento)
    {
      Object[] objects = (Object[]) memento;
      
      String oldName = (String) objects[0];
      boolean oldIsAbstract = (Boolean) objects[1];
      int oldStereotypeHashcode = (Integer) objects[2];
      boolean oldIsActive = (Boolean) objects[3];
      Command resizing = (Command) objects[4];

      name = oldName;
      isAbstract = oldIsAbstract;
      isActive = oldIsActive;
      stereotypeHashcode = oldStereotypeHashcode;
      showAsState = (Boolean) objects[5];
      resizing.unExecute();
    }
  }  

  private class StylableFacetImpl implements StylableFacet
  {
    public Object setFill(Color newFill)
    {
      Color oldFill = fillColor;
      fillColor = newFill;
      figureFacet.adjusted();
      return oldFill;
    }

    public void unSetFill(Object memento)
    {
      fillColor = (Color) memento;
      figureFacet.adjusted();
    }
  }	
	
	private class SuppressOwningPackageFacetImpl implements SuppressOwningPackageFacet
	{
		/**
		 * @see com.hopstepjump.jumble.umldiagrams.basicnamespacenode.ShowOwningPackageFacet#setShowOwningPackage(boolean)
		 */
		public Object setSuppressOwningPackage(boolean forceSuppressPackage)
		{
			boolean oldForceSuppressOwningPackage = forceSuppressOwningPackage;
			
			// make the change
			forceSuppressOwningPackage = forceSuppressPackage;
			
			return new Boolean(oldForceSuppressOwningPackage);
		}
		
		/**
		 * @see com.hopstepjump.jumble.umldiagrams.basicnamespacenode.ShowOwningPackageFacet#unSetShowOwningPackage(Object)
		 */
		public void unSetSupressOwningPackage(Object memento)
		{
			forceSuppressOwningPackage = ((Boolean)memento).booleanValue();
		}
	}
  
	private class ShowAsStateFacetImpl implements ShowAsStateFacet
	{
		public Object showAsState(boolean newShowAsState)
		{
			boolean oldDisplayAsIcon = showAsState;
			
			// make the change
			showAsState = newShowAsState;			
			figureFacet.adjusted();
			
			return new Object[] { new Boolean(oldDisplayAsIcon) };
		}
		
		public void unShowAsState(Object memento)
		{
			Object[] array = (Object[]) memento;
			showAsState = ((Boolean) array[0]).booleanValue();
      figureFacet.adjusted();
		}
	}
	
  private class DisplayAsIconFacetImpl implements DisplayAsIconFacet
  {
    /**
     * @see com.hopstepjump.jumble.umldiagrams.base.DisplayAsIconFacet#displayAsIcon(boolean)
     */
    public Object displayAsIcon(boolean displayAsIcon)
    {
      boolean oldDisplayAsIcon = displayOnlyIcon;
      
      // make the change
      displayOnlyIcon = displayAsIcon;
      
      Command resizeCommand = null;
      contents.getFigureFacet().setShowing(isContentsShowing());
      ports.getFigureFacet().setShowing(!displayOnlyIcon);
      
      // we are about to autosize, so need to make a resizings command
      ResizingFiguresFacet resizings = new ResizingFiguresGem(null, figureFacet.getDiagram()).getResizingFiguresFacet();
      resizings.markForResizing(figureFacet);
      
      ClassifierSizeInfo info = makeCurrentInfo();
      UBounds newBounds = info.makeActualSizes().getOuter();
      UBounds centredBounds = ResizingManipulatorGem.formCentrePreservingBoundsExactly(figureFacet.getFullBounds(), newBounds.getDimension());
      resizings.setFocusBounds(centredBounds);
      
      resizeCommand = resizings.end("resized to adjust for displayAsIcon toggle", "restored sizes to adjust for undoing displayAsIcon toggle");
      resizeCommand.execute(false);
      figureFacet.adjusted();
      
      return new Object[] { new Boolean(oldDisplayAsIcon), resizeCommand };
    }
    
    public void unDisplayAsIcon(Object memento)
    {
      Object[] array = (Object[]) memento;
      displayOnlyIcon = (Boolean) array[0];
      
      contents.getFigureFacet().setShowing(isContentsShowing());
      ports.getFigureFacet().setShowing(!displayOnlyIcon);
      
      Command resizeCommand = (Command) array[1];
      if (resizeCommand != null)
        resizeCommand.unExecute();
      figureFacet.adjusted();
    }
  }
  
	private class LocationFacetImpl implements LocationFacet
	{
    public Object setLocation()
    {
      SubjectRepositoryFacet repository = GlobalSubjectRepository.repository;
      
      // cannot locate a part
      if (subject instanceof Property)
      	return null;
      
      // locate to the diagram, or a possible nesting package
      // look upwards, until we find one that has a PackageFacet registered
      Namespace newOwner = (Namespace) figureFacet.getDiagram().getLinkedObject();
      Namespace currentOwner = (Namespace) subject.getOwner();
      Namespace containerOwner = repository.findVisuallyOwningNamespace(figureFacet.getDiagram(), figureFacet.getContainedFacet().getContainer());
      if (containerOwner != null)
        newOwner = containerOwner;
      
      // make sure that the package is not set to be owned by itself somehow
      for (Element owner = newOwner; owner != null; owner = owner.getOwner())
        if (owner == subject)
          return null;
      
      if (currentOwner instanceof Class)
        ((Class) currentOwner).getNestedClassifiers().remove(subject);
      else
        ((Package) currentOwner).getOwnedMembers().remove(subject);
      
      if (newOwner instanceof Class)
        ((Class) newOwner).getNestedClassifiers().add(subject); 
      else
        ((Package) newOwner).getOwnedMembers().add(subject); 

      return new Namespace[]{currentOwner, newOwner};
    }

    /**
     * @see com.hopstepjump.idraw.figurefacilities.selectionbase.LocationFacet#unSetLocation(Object)
     */
    public void unSetLocation(Object memento)
    {
      // don't bother if the memento isn't set
      if (memento == null)
        return;
      
      Namespace[] owners = (Namespace[]) memento;
      Namespace oldOwner = owners[0];
      Namespace newOwner = owners[1];
      
      if (newOwner instanceof Class)
        ((Class) newOwner).getNestedClassifiers().remove(subject);
      else
        ((Package) newOwner).getOwnedMembers().remove(subject);

      if (oldOwner instanceof Class)
        ((Class) oldOwner).getNestedClassifiers().add(subject);
      else
        ((Package) oldOwner).getOwnedMembers().add(subject);
    }
	}
	
	private class ClassifierNodeFacetImpl implements ClassifierNodeFacet
	{
		public void tellContainedAboutResize(PreviewCacheFacet previews, UBounds bounds)
		{
			// calculate the info structure, using the preview figures
			ClassifierSizeInfo info = makeCurrentInfoFromPreviews(previews);
			info.setTopLeft(bounds.getTopLeftPoint());
			info.setExtent(bounds.getDimension());
			
			// sizes should always be set
			ClassifierSizes sizes = info.makeActualSizes();
			PreviewFacet attributeOrSlotPreview = previews.getCachedPreview(attributesOrSlots.getFigureFacet());
			PreviewFacet operationsPreview = previews.getCachedPreview(operations.getFigureFacet());
			PreviewFacet contentsPreview = previews.getCachedPreview(contents.getFigureFacet());
			PreviewFacet portPreview = previews.getCachedPreview(ports.getFigureFacet());
			
			FeatureCompartmentPreviewFacet attributeFacet =
				(FeatureCompartmentPreviewFacet) attributeOrSlotPreview.getDynamicFacet(FeatureCompartmentPreviewFacet.class);
			attributeFacet.adjustPreviewPoint(previews, sizes.getAttributes().getPoint());
			FeatureCompartmentPreviewFacet operationsFacet =
				(FeatureCompartmentPreviewFacet) operationsPreview.getDynamicFacet(FeatureCompartmentPreviewFacet.class);
			operationsFacet.adjustPreviewPoint(previews, sizes.getOperations().getPoint());
			contentsPreview.setFullBounds(sizes.getContents(), true);
			
			portPreview.setFullBounds(sizes.getFull(), true);
		}
		
		public UBounds getBoundsAfterExistingContainablesAlter(PreviewCacheFacet previews, ContainedPreviewFacet[] adjusted)
		{
			// calculate the info structure, using the preview figures
			ClassifierSizeInfo info = makeCurrentInfoFromPreviews(previews);
			
			// info should always be set -- if it isn't, still try something sensible
			if (info == null)
				return figureFacet.getFullBounds();
			
			ClassifierSizes sizes = info.makeActualSizes();
			PreviewFacet contentsPreview = previews.getCachedPreview(contents.getFigureFacet());
			
			// 3 cases:
			// 1) contents is not empty, and the contents is the element which has been altered
			// 2) contents is empty, and we can move it around
			// 3) contents is not empty, and we must preserve positions of it
			
			// case 1: if contents have been altered, and the preview indicates it isn't empty, then don't bother centring...
			boolean contentsIsEmpty = sizes.isContentsEmpty() || !isContentsShowing();
			if (!contentsIsEmpty && adjusted[0].getPreviewFacet() == contentsPreview)
				return sizes.getOuter();
			
			return formCentredBounds(info, makeCurrentInfo().makeActualSizes().getContents()).getOuter();
		}
		
		public double getContentsHeightOffsetViaPreviews(PreviewCacheFacet previews)
		{
			ClassifierSizeInfo info;
			if (previews == null)
				info = makeCurrentInfo();
			else
				info = makeCurrentInfoFromPreviews(previews);
			ClassifierSizes sizes = info.makeActualSizes();
			return sizes.getContents().getTopLeftPoint().subtract(sizes.getOuter().getPoint()).getHeight();
		}
		
		/**
		 * @see com.hopstepjump.jumble.umldiagrams.classifiernode.ClassifierNodeFacet#getShapeForPreview(UBounds)
		 */
		public Shape formShapeForPreview(UBounds bounds)
		{
			return formShape(bounds, true);
		}
		
		/**
		 * @see com.hopstepjump.jumble.umldiagrams.classifiernode.ClassifierNodeFacet#formShapeForBoundaryCalculation(UBounds)
		 */
		public Shape formShapeForBoundaryCalculation(UBounds bounds)
		{
			return formShape(bounds, false);
		}
		
		private Shape formShape(UBounds bounds, boolean onlyIcon)
		{
			if (miniAppearanceFacet != null && displayOnlyIcon)
			{
				ClassifierSizeInfo info = makeCurrentInfo(bounds.getTopLeftPoint(), bounds.getDimension(), autoSized);
				ClassifierSizes sizes = info.makeActualSizes();
				Shape miniShape = miniAppearanceFacet.formShapeForPreview(sizes.getIcon());
				
				UBounds bodyBounds = null;
				
				if (!onlyIcon)
				{
					if (name.length() != 0)
						bodyBounds = new UBounds(sizes.getName());
					if (!forceSuppressOwningPackage && showOwningPackage)
					{
						if (bodyBounds == null)
							bodyBounds = sizes.getOwner();
						else
							bodyBounds = bodyBounds.union(sizes.getOwner());
					}
					
					if (!suppressAttributesOrSlots && !attributesOrSlots.isEmpty())
					{
						if (bodyBounds == null)
							bodyBounds = sizes.getAttributes();
						else
							bodyBounds = bodyBounds.union(sizes.getAttributes());
					}
					
					if (!suppressOperations && !operations.isEmpty())
					{
						if (bodyBounds == null)
							bodyBounds = sizes.getOperations();
						else
							bodyBounds = bodyBounds.union(sizes.getOperations());
					}
					
					// if the body bounds are not null, make sure they extend to the circle
					if (bodyBounds != null)
					{
						UDimension offset = new UDimension(0, 6);
						UBounds topBounds = new UBounds(sizes.getName().getPoint().subtract(offset), new UDimension(0, 0));
						bodyBounds = bodyBounds.union(topBounds);
					}
				}
				
				Area area = new Area(miniShape);
				if (bodyBounds != null)
					area.add(new Area(new ZRectangle(bodyBounds).getShape()));
				
				return area;
			}
			else
			{
				return new ZRectangle(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight()).getShape();
			}
		}
		
		/**
		 * @see com.hopstepjump.jumble.umldiagrams.classifiernode.ClassifierNodeFacet#isDisplayIconOnly()
		 */
		public boolean isDisplayOnlyIcon()
		{
			return displayOnlyIcon;
		}
		
		/**
		 * @see com.hopstepjump.jumble.umldiagrams.classifiernode.ClassifierNodeFacet#getMiddlePointForPreview(UBounds)
		 */
		public UPoint getMiddlePointForPreview(UBounds bounds)
		{
			ClassifierSizeInfo info = makeCurrentInfo(bounds.getTopLeftPoint(), bounds.getDimension(), autoSized);
			ClassifierSizes sizes = info.makeActualSizes();
			
			if (miniAppearanceFacet != null && displayOnlyIcon)
			{
				return sizes.getIcon().getMiddlePoint();
			}
			else
			{
				return sizes.getOuter().getMiddlePoint();
			}
		}
		/**
		 * @see com.hopstepjump.jumble.umldiagrams.classifiernode.ClassifierNodeFacet#getContainmentBounds(UBounds)
		 */
		public UBounds getContainmentBounds(UBounds newBounds)
		{
			if (!displayOnlyIcon)
      {
				return newBounds;
      }
			
			ClassifierSizeInfo info = makeCurrentInfo(newBounds.getTopLeftPoint(), newBounds.getDimension(), false);
			return getBoundsForContainment(info.makeActualSizes());
		}

    public UBounds getPortContainmentBounds(PreviewCacheFacet previews)
    {
      PreviewFacet portPreviewFacet = previews.getCachedPreview(ports.getFigureFacet());
      return portPreviewFacet.getFullBoundsForContainment();
    }
		
	}
	
	private class HideContentsFacetImpl implements HideContentsFacet
	{
		public Object hideContents(boolean hide)
		{
			boolean oldHideContents = suppressContents;
			
			// make the change
			suppressContents = hide;
			contents.getFigureFacet().setShowing(isContentsShowing());

			// we are about to autosize, so need to make a resizings command
			ResizingFiguresFacet resizings = new ResizingFiguresGem(null, figureFacet.getDiagram()).getResizingFiguresFacet();
			resizings.markForResizing(figureFacet);
			
			ClassifierSizeInfo info = makeCurrentInfo();
			UBounds newBounds = info.makeActualSizes().getOuter();
			resizings.setFocusBounds(newBounds);
			
			Command resizeCommand = resizings.end("resized to adjust for hideContents toggle", "restored size after undoing hideContents toggle");
			resizeCommand.execute(false);
			figureFacet.adjusted();
			
			return new Object[] { new Boolean(oldHideContents), resizeCommand };
		}
		
		public void unHideContents(Object memento)
		{
			Object[] array = (Object[]) memento;
			suppressContents = ((Boolean) array[0]).booleanValue();
			contents.getFigureFacet().setShowing(isContentsShowing());
			
			Command resizeCommand = (Command) array[1];
			if (resizeCommand != null)
				resizeCommand.unExecute();
			figureFacet.adjusted();
		}
	}
		
	private class VisualLockFacetImpl implements VisualLockFacet
	{
		public Object lock(boolean lock)
		{
			boolean oldLock = locked;
			locked = lock;
			figureFacet.adjusted();
			return oldLock;
		}

		public void unLock(Object memento)
		{
			locked = (Boolean) memento;
			figureFacet.adjusted();
		}

		public boolean isLocked()
		{
			return locked;
		}		
	}
	
	private class BasicNodeAutoSizedFacetImpl implements BasicNodeAutoSizedFacet
	{
		/**
		 * @see com.giroway.jumble.nodefacilities.resizebase.CmdAutoSizeable#autoSize(boolean)
		 */
		public Object autoSize(boolean newAutoSized)
		{
			boolean oldAutoSized = autoSized;
			
			// make the change
			autoSized = newAutoSized;
			
			Command resizeCommand = null;
			contents.getFigureFacet().setShowing(isContentsShowing());
			
			// we are about to autosize, so need to make a resizings command
			ResizingFiguresFacet resizings = new ResizingFiguresGem(null, figureFacet.getDiagram()).getResizingFiguresFacet();
			resizings.markForResizing(figureFacet);
			
			UBounds autoBounds = getAutoSizedBounds(newAutoSized);
			resizings.setFocusBounds(autoBounds);
			
			resizeCommand = resizings.end("resized to adjust for autoSized toggle", "restored size after undoing autoSized toggle");
			resizeCommand.execute(false);
			figureFacet.adjusted();
			
			return new Object[] { new Boolean(oldAutoSized), resizeCommand };
		}
		
		/**
		 * @see com.giroway.jumble.nodefacilities.resizebase.CmdAutoSizeable#unAutoSize(Object)
		 */
		public void unAutoSize(Object memento)
		{
			Object[] array = (Object[]) memento;
			autoSized = ((Boolean) array[0]).booleanValue();
			contents.getFigureFacet().setShowing(isContentsShowing());
			
			Command resizeCommand = (Command) array[1];
			if (resizeCommand != null)
				resizeCommand.unExecute();
			figureFacet.adjusted();
		}
		
		public JMenuItem getAutoSizedMenuItem(final ToolCoordinatorFacet coordinator)
		{
			// for autosizing
			JCheckBoxMenuItem toggleAutoSizeItem = new JCheckBoxMenuItem("Autosized");
			toggleAutoSizeItem.setState(autoSized);
			toggleAutoSizeItem.addActionListener(new ActionListener()
																					 {
						public void actionPerformed(ActionEvent e)
						{
							// toggle the autosized flag (as a command)
							Command autoSizeCommand =
								new NodeAutoSizeCommand(
								figureFacet.getFigureReference(),
								!autoSized,
							  (autoSized ? "unautosized " : "autosized ") + figureName,
							  (!autoSized ? "unautosized " : "autosized ") + figureName);
							coordinator.executeCommandAndUpdateViews(autoSizeCommand);
						}
					});
			return toggleAutoSizeItem;
		}
		
		/**
		 * @see com.hopstepjump.jumble.nodefacilities.nodesupport.BasicNodeAutoSizedFacet#isAutoSized()
		 */
		public boolean isAutoSized()
		{
			return autoSized;
		}
	}
	
	private class SuppressFeaturesFacetImpl implements SuppressFeaturesFacet
	{
		/**
		 * @see com.giroway.jumble.umldiagrams.classdiagram.classifiernode.CmdOperationsSuppressable#suppressOperations(boolean)
		 */
		public Object suppressFeatures(int featureType, boolean suppress)
		{
			boolean oldSuppressFeature = false;
			
			// we will probably change size, so need to make a resizings command
			ResizingFiguresFacet resizings = new ResizingFiguresGem(null, figureFacet.getDiagram()).getResizingFiguresFacet();
			resizings.markForResizing(figureFacet);
			
			if (featureType == AttributeCreatorGem.FEATURE_TYPE || featureType == SlotCreatorGem.FEATURE_TYPE)
			{
				oldSuppressFeature = suppressAttributesOrSlots;
				suppressAttributesOrSlots = suppress;
				attributesOrSlots.getFigureFacet().setShowing(!suppress);
				resizings.setFocusBounds(getAttributeSuppressedBounds(suppressAttributesOrSlots));
			}
			else
				if (featureType == OperationCreatorGem.FEATURE_TYPE)
				{
					oldSuppressFeature = suppressOperations;
					suppressOperations = suppress;
					operations.getFigureFacet().setShowing(!suppress);
					resizings.setFocusBounds(getOperationSuppressedBounds(suppressOperations));
				}
			
			Command resizeCommand = resizings.end("resized to adjust for suppress feature toggle", "restored size after undoing suppress feature toggle");
			resizeCommand.execute(false);
			
			return new Object[] { new Integer(featureType), new Boolean(oldSuppressFeature), resizeCommand };
		}
		
		/**
		 * @see com.giroway.jumble.umldiagrams.classdiagram.classifiernode.CmdOperationsSuppressable#unSuppressOperations(Object)
		 */
		public void unSuppressFeatures(Object memento)
		{
			Object[] objects = (Object[]) memento;
			int featureType = ((Integer) objects[0]).intValue();
			boolean suppress = ((Boolean) objects[1]).booleanValue();
			Command resizeCommand = (Command) objects[2];
			
			if (featureType == AttributeCreatorGem.FEATURE_TYPE)
			{
				suppressAttributesOrSlots = suppress;
				attributesOrSlots.getFigureFacet().setShowing(!suppressAttributesOrSlots);
			}
			else
				if (featureType == OperationCreatorGem.FEATURE_TYPE)
				{
					suppressOperations = suppress;
					operations.getFigureFacet().setShowing(!suppressOperations);
				}
			
			resizeCommand.unExecute();
		}
	}
	
	private class ResizeVetterFacetImpl implements ResizeVetterFacet
	{
		public void startResizeVet()
		{
			minVetBounds = null;
		}
		
		public UDimension vetResizedExtent(UBounds bounds)
		{
			if (displayOnlyIcon)
			{
				UDimension extent = bounds.getDimension();
				
				// don't allow the extent to go less than the height or width
				UDimension minExtent = miniAppearanceFacet.getMinimumDisplayOnlyAsIconExtent();
				if (autoSized)
					return minExtent;
				
				double vettedWidth = Math.max(extent.getWidth(), minExtent.getWidth());
				return new UDimension(vettedWidth, minExtent.getHeight() / minExtent.getWidth() * vettedWidth);
				// preserve ratio
			}
			
			UDimension extent = bounds.getDimension();
			if (contents.isEmpty() || !isContentsShowing())
			{
				// don't allow the extent to go less than the height or width
				ClassifierSizeInfo info = makeCurrentInfo();
				info.setAutoSized(true);
				ClassifierSizes sizes = info.makeActualSizes();
				
				return sizes.getOuter().getDimension().maxOfEach(extent);
			}
			else
				return extent;
		}
		
		public UBounds vetResizedBounds(DiagramViewFacet view, int corner, UBounds bounds, boolean fromCentre)
		{
			if (autoSized)
				return figureFacet.getFullBounds();
			
			if (displayOnlyIcon || contents.isEmpty() || suppressContents)
				return bounds;
			
			// the following code was stolen from the package figure, which also manages a contents area
			if (minVetBounds == null)
			{
				minVetBounds = contents.getMinimumResizeBounds(new ContainerSizeCalculator()
																											 {
							public ContainerSizeInfo makeInfo(UPoint topLeft, UDimension extent, boolean autoSized)
							{
								return makeCurrentInfo(topLeft, extent, autoSized);
							}
						}, corner, bounds, figureFacet.getFullBounds().getTopLeftPoint(), fromCentre);
			}
			
			UPoint topLeftVet = minVetBounds.getTopLeftPoint().minOfEach(bounds.getTopLeftPoint());
			UPoint bottomRightVet = minVetBounds.getBottomRightPoint().maxOfEach(bounds.getBottomRightPoint());
			
			return new UBounds(topLeftVet, bottomRightVet.subtract(topLeftVet));
		}
	}
	
  
	private class TextableFacetImpl implements TextableFacet
	{
		/*
		 * text resize vetting
		 */
		
		public UBounds getTextBounds(String name)
		{
			UBounds textBounds = new UBounds(vetTextChange(name).getName());
			return textBounds.addToPoint(new UDimension(0, -1));
		}
		
		public UBounds vetTextResizedExtent(String name)
		{
			return vetTextChange(name).getOuter();
		}
		
		private ClassifierSizes vetTextChange(String name)
		{
			// get the old sizes, the new sizes and then get the preferred topleft.  Then, get the sizes with this as the topleft point!!
			ClassifierSizeInfo info = makeCurrentInfo();
			UBounds contentBoundsToPreserve = info.makeActualSizes().getContents();
			info.setName(name);
			
			ClassifierSizes ret = formCentredBounds(info, contentBoundsToPreserve);
			return ret;
		}
		
    public Object setText(String text, Object listSelection, boolean unsuppress, Object oldMemento)
    {
      SetTextPayload payload = miniAppearanceFacet.setText(null, text, listSelection, unsuppress, oldMemento);

      if (payload != null)
      {
        if (payload.getSubject() != null)
          subject = (Classifier) payload.getSubject();
        
        return new Object[]{payload.getMemento(), refreshSuppressedAttributes(), refreshSuppressedPorts()};
      }
      return null;
    }
		
    public void unSetText(Object memento)
    {
    	Object[] data = (Object[]) memento;
    	Object obj = data != null ? data[0] : null;
    	Set<String>[] suppressedAttrs = data != null ? (Set<String>[]) data[1] : null; 
    	Set<String>[] suppressedPorts = data != null ? (Set<String>[]) data[2] : null;
    	    	
    	attributesOrSlots.setAddedAndDeleted(suppressedAttrs);
    	ports.setAddedAndDeleted(suppressedPorts);
    	
      SetTextPayload payload = miniAppearanceFacet.unSetText(obj);
      if (payload != null && payload.getSubject() != null)
        subject = (Classifier) payload.getSubject();      
    }
    
		public FigureFacet getFigureFacet()
		{
			return figureFacet;
		}

    public JList formSelectionList(String textSoFar)
    {
      return miniAppearanceFacet.formSelectionList(textSoFar);
    }
	}
	
	private class BasicNodeAppearanceFacetImpl implements BasicNodeAppearanceFacet
	{
    public String getFigureName()
		{
			return figureName;
		}
		
		public PreviewFacet makeNodePreviewFigure(PreviewCacheFacet previews, DiagramFacet diagram, UPoint start, boolean isFocus)
		{
			BasicNodePreviewGem basicGem = new BasicNodePreviewGem(figureFacet, start, isFocus, true);
			ClassifierPreviewGem previewGem = new ClassifierPreviewGem(figureFacet.getFullBounds());
			basicGem.connectPreviewCacheFacet(previews);
			basicGem.connectFigureFacet(figureFacet);
			basicGem.connectBasicNodePreviewAppearanceFacet(previewGem.getBasicNodePreviewAppearanceFacet());
			basicGem.connectContainerPreviewFacet(previewGem.getContainerPreviewFacet());
			previewGem.connectPreviewFacet(basicGem.getPreviewFacet());
			previewGem.connectPreviewCacheFacet(previews);
			previewGem.connectClassifierNodeFacet(classifierFacet);
			
			return basicGem.getPreviewFacet();
		}
		
		public Manipulators getSelectionManipulators(DiagramViewFacet diagramView, boolean favoured, boolean firstSelected, boolean allowTYPE0Manipulators)
		{
			ManipulatorFacet keyFocus = null;
			if (favoured)
			{
				TextManipulatorGem textGem =
					new TextManipulatorGem(
					isPart ? "changed part details" : "changed classifier name",
					isPart ? "changed part details" : "changed classifier name",
					isPart ? name : subject.getName(),
					font,
					Color.black,
					fillColor,
					TextManipulatorGem.TEXT_AREA_ONE_LINE_TYPE);
				textGem.connectTextableFacet(textableFacet);
				keyFocus = textGem.getManipulatorFacet();
			}
			Manipulators manipulators =
				new Manipulators(
				    keyFocus,
				    new ResizingManipulatorGem(
				        figureFacet,
				        diagramView,
								figureFacet.getFullBounds(),
								resizeVetterFacet,
								firstSelected).getManipulatorFacet());
			if (favoured)
			{
				// add the manipulator for suppressing or showing operations
				ClassifierSizeInfo info = makeCurrentInfo();
				ClassifierSizes sizes = info.makeActualSizes();
				
				UBounds attributes = sizes.getFull();
				UPoint attributePoint = attributes.getPoint().subtract(new UDimension(QUICK_ICON_SIZE - 8, -2));

				manipulators.addOther(
					new FeatureSuppressToggleManipulator(
															 figureFacet.getFigureReference(),
															 figureFacet.getFigureName(),
															 attributePoint,
															 QUICK_ICON_SIZE,
															 AttributeCreatorGem.FEATURE_TYPE,
															 isPart ? "slots" : "attributes", suppressAttributesOrSlots));
				
				manipulators.addOther(
					new FeatureSuppressToggleManipulator(
															 figureFacet.getFigureReference(),
															 figureFacet.getFigureName(),
															 attributePoint.add(new UDimension(-10, 0)),
															 QUICK_ICON_SIZE,
															 OperationCreatorGem.FEATURE_TYPE,
															 "operations", suppressOperations));
			}
			
			// return the manipulators
			return manipulators;
		}
		
		public ZNode formView()
		{
      Color line = !showAsState ? lineColor : new Color(200, 160, 160);
      Color fill = !showAsState ? fillColor : new Color(240, 200, 200, 80);
      
			ClassifierSizeInfo info = makeCurrentInfo();
			ClassifierSizes sizes = info.makeActualSizes();
			
			ZText zName = sizes.getZtext();
			if (!displayOnlyIcon)
				zName.setBackgroundColor(null);
			zName.setPenColor(lineColor);
			
			ZText zOwner = sizes.getZOwningText();
			if (zOwner != null)
			{
				if (!displayOnlyIcon)
					zOwner.setBackgroundColor(null);
				zOwner.setPenColor(lineColor);
			}
			
			// a possible miniappearance
			ZNode miniAppearance = null;      
			if (sizes.getIcon() != null)
				miniAppearance = miniAppearanceFacet.formView(displayOnlyIcon, sizes.getIcon());
			
			// draw the rectangle
			UBounds entireBounds = sizes.getOuter();
      ZShape rect;
      if (showAsState)
      {
        RoundRectangle2D.Double rounded =
          new RoundRectangle2D.Double(
              entireBounds.getX(),
              entireBounds.getY(),
              entireBounds.getWidth(),
              entireBounds.getHeight(),
              25,
              25);
        rect = new ZRoundedRectangle(rounded);
        rect.setFillPaint(fill);
        rect.setPenPaint(line);
      }
      else
      {
        rect = new ZRectangle(entireBounds);
        rect.setFillPaint(fill);
        rect.setPenPaint(line);
      }
			
			// draw a line under the title
			ZLine titleLine = makeFullLine(
			    entireBounds,
			    sizes.getAttributes().getPoint(),
			    sizes.getAttributes().getTopRightPoint());
			
			titleLine.setPenPaint(line);
			ZLine attrLine = makeFullLine(
			    entireBounds,
			    sizes.getOperations().getPoint(),
			    sizes.getOperations().getTopRightPoint());
			attrLine.setPenPaint(line);
			UBounds contentBounds = sizes.getContents();
			ZLine operationsLine = makeFullLine(
			    entireBounds,
			    contentBounds.getTopLeftPoint(),
			    contentBounds.getTopRightPoint());
			operationsLine.setPenPaint(line);
			
			// group them
			ZGroup group = new ZGroup();
			if (!displayOnlyIcon)
			{        
			  group.addChild(new ZVisualLeaf(rect));
				
				if (isActive)
				{
          if (!showAsState)
          {
            // add possible active lines
            UBounds outer = sizes.getOuter().addToPoint(
                new UDimension(4, 0)).addToExtent(new UDimension(-8, 0));
            ZRectangle active = new ZRectangle(outer.getX(), outer.getY(), outer.getWidth(), outer.getHeight());
            active.setPenPaint(lineColor);
            active.setFillPaint(null);
            group.addChild(new ZVisualLeaf(active));
          }
          else
          {
            // add possible active lines
            UBounds outer = sizes.getOuter().addToPoint(new UDimension(4, 0)).addToExtent(new UDimension(-8, 0));
            ZRoundedRectangle active = new ZRoundedRectangle(
                outer.getX(), outer.getY(), outer.getWidth(), outer.getHeight(), 10, 10);
            active.setPenPaint(new Color(200, 160, 160));
            active.setFillPaint(null);
            group.addChild(new ZVisualLeaf(active));
          }
				}				
			}
			if (name.length() > 0 || isPart)
				group.addChild(new ZVisualLeaf(zName));
			if (zOwner != null)
				group.addChild(new ZVisualLeaf(zOwner));
			if (miniAppearance != null)
				group.addChild(miniAppearance);
			
			// avoid drawing the last line if possible, as it looks ugly on leaves
			boolean haveAttributes = !suppressAttributesOrSlots && (!attributesOrSlots.isEmpty() || attributeEllipsis);
			boolean haveOperations = !suppressOperations && (!operations.isEmpty() || operationEllipsis);
			if (haveAttributes || haveOperations)
				group.addChild(new ZVisualLeaf(titleLine));
			if (haveOperations)
				group.addChild(new ZVisualLeaf(attrLine));
      
      // if this is a part, place a line just under the name
      if (isPart)
      {
        UBounds bounds = new UBounds(zName.getBounds());
        ZLine partLine = new ZLine(bounds.getBottomLeftPoint(), bounds.getBottomRightPoint());
        partLine.setPenPaint(line);
        group.addChild(new ZVisualLeaf(partLine));
      }
      
      if (!displayOnlyIcon && !showAsState)
      {
        {          
          ZPolyline poly = new ZPolyline();
          poly.add(entireBounds.getBottomLeftPoint());
          poly.add(entireBounds.getTopLeftPoint());
          poly.add(entireBounds.getTopRightPoint());
          Color newFill = fillColor.equals(Color.WHITE) ? Color.BLACK : fillColor;
          poly.setPenPaint(newFill);
          group.addChild(new ZVisualLeaf(poly));
        }
        {
          ZPolyline poly = new ZPolyline();
          poly.add(entireBounds.getBottomLeftPoint().add(new UDimension(1,-1)));
          poly.add(entireBounds.getTopLeftPoint().add(new UDimension(1,1)));
          poly.add(entireBounds.getTopRightPoint().add(new UDimension(-1,1)));
          poly.setPenPaint(Color.WHITE);
          group.addChild(new ZVisualLeaf(poly));
        }
        {
          Color newFill = fillColor.equals(Color.WHITE) ? Color.BLACK : fillColor.darker();
          {
            ZPolyline poly = new ZPolyline();
            poly.add(entireBounds.getTopRightPoint().add(new UDimension(-1,0)));
            poly.add(entireBounds.getBottomRightPoint().add(new UDimension(-1,-1)));
            poly.add(entireBounds.getBottomLeftPoint().add(new UDimension(1,-1)));
            group.addChild(new ZVisualLeaf(poly));
            poly.setPenPaint(newFill);
          }
          {
            ZPolyline poly = new ZPolyline();
            poly.add(entireBounds.getTopRightPoint());
            poly.add(entireBounds.getBottomRightPoint());
            poly.add(entireBounds.getBottomLeftPoint());
            poly.setPenPaint(newFill);
            group.addChild(new ZVisualLeaf(poly));
          }
        }
      }
      
      // possibly add an error mark
      if (StereotypeUtilities.isStereotypeApplied(subject, "error"))
      {
        ZGroup errors = new ZGroup();
        UDimension size = new UDimension(16, 16);
        UDimension offset = new UDimension(3, 3);
        UBounds bounds = new UBounds(entireBounds.getPoint().subtract(size.multiply(0.5)), size);
        UBounds insetBounds = bounds.addToPoint(offset).addToExtent(offset.multiply(2).negative());
        ZLine line1 = new ZLine(insetBounds.getPoint(), insetBounds.getBottomRightPoint());
        line1.setPenPaint(Color.RED);
        line1.setPenWidth(4);
        ZLine line2 = new ZLine(insetBounds.getBottomLeftPoint(), insetBounds.getTopRightPoint());
        line2.setPenWidth(4);
        line2.setPenPaint(Color.RED);
        ZRectangle errorRect = new ZRectangle(bounds);
        errorRect.setFillPaint(Color.WHITE);
        errors.addChild(new ZVisualLeaf(errorRect));
        errors.addChild(new ZVisualLeaf(line1));
        errors.addChild(new ZVisualLeaf(line2));
        group.addChild(errors);
      }
      
      // possibly add a delta mark
      if (StereotypeUtilities.isStereotypeApplied(subject, "backbone-delta"))
      {
        ZGroup delta = new ZGroup();
        UDimension size = new UDimension(16, 16);
        UBounds bounds = new UBounds(entireBounds.getPoint().subtract(size.multiply(0.5)), size);
        ZPolygon p = new ZPolygon(bounds.getTopLeftPoint().add(new UDimension(bounds.width / 2, 0)));
        p.add(bounds.getBottomLeftPoint());
        p.add(bounds.getBottomRightPoint());
        p.setPenPaint(Color.BLUE);
        p.setFillPaint(new Color(0,0,0,0));
        p.setPenWidth(4);
        delta.addChild(new ZVisualLeaf(p));
        group.addChild(delta);
      }
      
      // possibly add in a ... for missing display attributes
      if (attributeEllipsis)
      {
        UPoint pt = sizes.getAttributes().getBottomRightPoint();
        UPoint start = pt.subtract(new UDimension(14, 6));
        for (int lp = 0; lp < 3; lp++)
        {
          ZRectangle dot = new ZRectangle(new UBounds(start, new UDimension(1, 1)));
          group.addChild(new ZVisualLeaf(dot));
          start = start.add(new UDimension(4, 0));
        }
      }
      
      // possibly add in a ... for missing display attributes
      if (operationEllipsis)
      {
        UPoint pt = sizes.getOperations().getBottomRightPoint();
        UPoint start = pt.subtract(new UDimension(14, 6));
        for (int lp = 0; lp < 3; lp++)
        {
          ZRectangle dot = new ZRectangle(new UBounds(start, new UDimension(1, 1)));
          group.addChild(new ZVisualLeaf(dot));
          start = start.add(new UDimension(4, 0));
        }
      }
      
      // possibly add in a ... for missing display attributes
      if (bodyEllipsis)
      {
        UPoint pt = sizes.getFull().getBottomRightPoint();
        UPoint start = pt.subtract(new UDimension(14, 6));
        for (int lp = 0; lp < 3; lp++)
        {
          ZRectangle dot = new ZRectangle(new UBounds(start, new UDimension(1, 1)));
          group.addChild(new ZVisualLeaf(dot));
          start = start.add(new UDimension(4, 0));
        }
      }
      
      // is this a retirement?
      if (isElementRetired())
    		addCross(sizes, group, 12, Color.RED);

      // add the interpretable properties
			group.setChildrenPickable(false);
			group.setChildrenFindable(false);
			group.putClientProperty("figure", figureFacet);
			
			return group;
		}

		private void addCross(ClassifierSizes sizes, ZGroup group, int s, Color color)
		{
			UPoint top = sizes.getName().getTopLeftPoint().subtract(new UDimension(16, 0));
			UDimension size = new UDimension(s, s);
			UDimension size2 = new UDimension(s, -s);
			ZLine line1 = new ZLine(top.subtract(size), top.add(size));
			line1.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
			line1.setPenPaint(color);
			ZLine line2 = new ZLine(top.subtract(size2), top.add(size2));
			line2.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
			line2.setPenPaint(color);
			group.addChild(new ZVisualLeaf(line1));
			group.addChild(new ZVisualLeaf(line2));
		}
		
		/**
		 * use the y coordinates of the left and right points, and the x coordinates of the entirebounds
     * @param entireBounds
     * @param point
     * @param topRightPoint
     * @return
     */
    private ZLine makeFullLine(UBounds entireBounds, UPoint left, UPoint right)
    {
      // if we are active, expand into the width space slightly
      int offset = isActive ? 4 : 0;
      
      double y = left.getY();
      return new ZLine(
          entireBounds.getTopLeftPoint().getX() + offset, y,
          entireBounds.getTopRightPoint().getX() - offset, y);
    }

    /**
		 * @see com.hopstepjump.jumble.nodefacilities.nodesupport.BasicNodeAppearanceFacet#getCreationExtent()
		 */
		public UDimension getCreationExtent()
		{
			return new UDimension(0, 0);
			// resized using a resizing figure, rather than having a set size
		}
		
		/**
		 * @see com.hopstepjump.jumble.nodefacilities.nodesupport.BasicNodeAppearanceFacet#getActualFigureForSelection()
		 */
		public FigureFacet getActualFigureForSelection()
		{
			return figureFacet;
		}
		
		/**
		 * @see com.giroway.jumble.foundation.interfaces.SelectableFigure#getContextMenu()
		 */
		public JPopupMenu makeContextMenu(final DiagramViewFacet diagramView, final ToolCoordinatorFacet coordinator)
		{
			JPopupMenu popup = new JPopupMenu();
			
			boolean subjectReadOnly = figureFacet.isSubjectReadOnlyInDiagramContext(false);
			boolean diagramReadOnly = diagramView.getDiagram().isReadOnly();
			boolean isInterface = subject instanceof Interface;
			boolean isPrimitive = subject instanceof PrimitiveType;

			if (isPart)
			{
		     // only add a replace if this is not visually at home
	      SubjectRepositoryFacet repository = GlobalSubjectRepository.repository;
	      Namespace visual = repository.findVisuallyOwningNamespace(diagramView.getDiagram(), figureFacet.getContainedFacet().getContainer());
	      Namespace real = (Namespace)
	        GlobalSubjectRepository.repository.findOwningElement(
	            (Element) figureFacet.getSubject(),
	            ClassifierImpl.class); 
	          
	      if (visual != real &&
	          !figureFacet.getContainedFacet().getContainer().getFigureFacet().isSubjectReadOnlyInDiagramContext(false))
	      {
	        JMenuItem replaceItem = getPartReplaceItem(diagramView, coordinator);
	        if (replaceItem != null)
	        {
	          popup.add(replaceItem);
	          Utilities.addSeparator(popup);
	        }
	      }
			}
			
			if (!diagramReadOnly)
			{
  			popup.add(figureFacet.getBasicNodeAutoSizedFacet().getAutoSizedMenuItem(coordinator));
        Utilities.addSeparator(popup);        
			}
			
			if (!subjectReadOnly)
			{
  			if (isPart)
  				popup.add(getAddSlotItem(diagramView, coordinator));
  			else
  			{
          // add a menu option to allow connectors to be fixed if their endpoints are no longer there
          if (!isInterface && !isPrimitive)
          {
            JMenu fix = new JMenu("Fix bad connectors");
            
            // work out which ones require deleting, by looking at it the from home stratum
            DEComponent me = GlobalDeltaEngine.engine.locateObject(subject).asComponent();
            DEStratum perspective = me.getHomeStratum();
            IDeltas conns = me.getDeltas(ConstituentTypeEnum.DELTA_CONNECTOR);
            Set<DeltaPair> pairs = conns.getConstituents(perspective, true);
            boolean someFixes = false;
            
            for (DeltaPair pair : pairs)
            {
              DEConnector conn = pair.getConstituent().asConnector();
              for (int lp = 0; lp < 2; lp++)
              {
                if (conn.getPort(perspective, me, lp) == null)
                {
                  // this is a bad connector
                  JMenuItem delete = new JMenuItem(conn.getName());
                  fix.add(delete);
                  someFixes = true;
                  
                  // if the connector is defined here, simply remove it, otherwise add a delta delete
                  if (conn.getParent() == me)
                  {
                  	final Element real = (Element) conn.getRepositoryObject();
	                  delete.addActionListener(new ActionListener()
	                  {
	                    public void actionPerformed(ActionEvent e)
	                    {
	                      Command cmd = new AbstractCommand("fixed bad local connector", "unfixed bad local connector")
	                      {	
	                        public void execute(boolean isTop)
	                        {
	                          GlobalSubjectRepository.repository.incrementPersistentDelete(real);
	                        }
	
	                        public void unExecute()
	                        {
	                          GlobalSubjectRepository.repository.decrementPersistentDelete(real);
	                        }                        
	                      };
	                      coordinator.executeCommandAndUpdateViews(cmd);
	                    } 
	                  });
                  }
                  else
                  {
	                  final DeltaDeletedConnector del = ((Class) subject).createDeltaDeletedConnectors();
	                  del.setDeleted((Connector) pair.getOriginal().getRepositoryObject());
	                  GlobalSubjectRepository.repository.incrementPersistentDelete(del);
	                  
	                  delete.addActionListener(new ActionListener()
	                  {
	                    public void actionPerformed(ActionEvent e)
	                    {
	                      Command cmd = new AbstractCommand("fixed bad connector", "unfixed bad connector")
	                      {
	
	                        public void execute(boolean isTop)
	                        {
	                          GlobalSubjectRepository.repository.decrementPersistentDelete(del);
	                        }
	
	                        public void unExecute()
	                        {
	                          GlobalSubjectRepository.repository.incrementPersistentDelete(del);
	                        }                        
	                      };
	                      coordinator.executeCommandAndUpdateViews(cmd);
	                    } 
	                  });
	                }
                }
              }
            }
            
            // only enable if we have some fixes
            popup.add(fix);
            fix.setEnabled(someFixes);
          }
  			}
			}
			
      if (miniAppearanceFacet != null)
        miniAppearanceFacet.addToContextMenu(popup, diagramView, coordinator);
			
			if (!diagramReadOnly)
			{
        Utilities.addSeparator(popup);
        
        if (!isPart)
        {
        	popup.add(getShowSpecificAttributesMenuItem(coordinator));
        	popup.add(getShowSpecificPortsMenuItem(coordinator));
        }
        else
        	popup.add(getShowSpecificPortInstancesMenuItem(coordinator));
        popup.addSeparator();
        
		    JMenu show = new JMenu("Show all");
		    popup.add(show);
		    if (isPart)
		    {		      
          show.add(getShowAllSlotsMenuItem(coordinator));
          show.add(getShowAllPortInstancesMenuItem(coordinator));
		    }
		    else
        {
          show.add(getShowAllAttributesMenuItem(coordinator));
          show.add(getShowAllOperationsMenuItem(coordinator));
          if (!isInterface && !isPrimitive)
          {
            show.add(getShowAllPortsMenuItem(coordinator));
            show.add(getShowAllPartsMenuItem(coordinator));
            show.add(getShowAllConnectorsMenuItem(coordinator));
          }
          show.add(getShowStereotypeCommand(diagramView, coordinator));          
			  }
			  
			  JMenu hide = new JMenu("Suppress");
			  popup.add(hide);
  			hide.add(getSuppressAttributesOrSlotsItem(diagramView, coordinator));
  			if (!isPart)
  			  hide.add(getSuppressOperationsItem(diagramView, coordinator));
  			hide.add(getHideContentsItem(diagramView, coordinator));
  			popup.add(getSuppressAllItem(diagramView, coordinator));
  			popup.add(getUnsuppressAllItem(diagramView, coordinator));
  			
        if (!isPart)
          popup.add(getSuppressOwnerItem(diagramView, coordinator));
  			
  			if (!isPart)
  			{
  				// add expansions
  				popup.addSeparator();
  				JMenu expand = new JMenu("Expand");
  				expand.setIcon(Expander.EXPAND_ICON);
  				JMenuItem deps = new JMenuItem("dependencies");
  				expand.add(deps);
  				deps.addActionListener(new ActionListener()
  				{
  					public void actionPerformed(ActionEvent e)
  					{
  						new Expander().expand(
  								figureFacet,
  								null,
  								UML2Package.eINSTANCE.getNamedElement_OwnedAnonymousDependencies(),
  								new DependencyCreatorGem().getArcCreateFacet(),
  								coordinator);
  					}
  				});
  				popup.add(expand);
  			}
			}
			
			if (!subjectReadOnly)
			{
        if (!isPart && !isInterface && !isPrimitive)
        {
          Utilities.addSeparator(popup);
        	JMenuItem blackBoxItem = getBlackboxItem(diagramView, coordinator);
          if (blackBoxItem != null)
            popup.add(blackBoxItem);
          JMenuItem navigableItem = getNavigableItem(diagramView, coordinator);
          if (navigableItem != null)
            popup.add(navigableItem);
          popup.add(getActiveItem(diagramView, coordinator));        
         }
   		}

			if (!isPart && !isPrimitive)
			{
        Utilities.addSeparator(popup);
        popup.add(SubstitutionsMenuMaker.getSubstitutionsMenuItem(figureFacet, coordinator));
        popup.add(ResemblancePerspectiveTree.makeMenuItem(diagramView.getDiagram(), figureFacet, coordinator));

        if (!isInterface)
        {
  	      JMenu backbone = new JMenu("Show Backbone code");
  	      popup.add(backbone);
  	      // show backbone code
  	      final Package pkg = GlobalSubjectRepository.repository.findVisuallyOwningStratum(figureFacet.getDiagram(), figureFacet.getContainerFacet()); 
  	      final DEStratum perspective = GlobalDeltaEngine.engine.locateObject(pkg).asStratum();
  	      final Class component = (Class) figureFacet.getSubject();
  	      final DEComponent me = GlobalDeltaEngine.engine.locateObject(component).asComponent();
  	      if (me != null)
  	      {
  		      backbone.add(PackageMiniAppearanceGem.makeShowBackboneCodeItem("Deltas", coordinator, perspective, me, true));
  		      // show canonical backbone code
  		      BBComponent bb = new BBComponent(me.getUuid());
  		      bb.setRawName(me.getName());
  		      bb.setComponentKind(me.getComponentKind());
  		      copyCanonicalConstituents(perspective, bb, me);
  		      backbone.add(PackageMiniAppearanceGem.makeShowBackboneCodeItem("Expanded", coordinator, perspective, bb, false));
  	      }
  	      
  	      JMenuItem comp = new JMenuItem("Show compositional hierarchy");
  	      comp.setIcon(COMPOSITION_ICON);
  	      popup.add(comp);
  	      if (me != null)
  	      {
  	      	comp.addActionListener(new ActionListener()
  	      	{
  						public void actionPerformed(ActionEvent e)
  						{
  							JPanel panel = new JPanel(new BorderLayout());
  			      	CompositionHierarchyViewer viewer = new CompositionHierarchyViewer(pkg, component, panel);

  			        int x = coordinator.getFrameXPreference(RegisteredGraphicalThemes.INITIAL_EDITOR_X_POS);
  			        int y = coordinator.getFrameYPreference(RegisteredGraphicalThemes.INITIAL_EDITOR_Y_POS);
  			        int width = coordinator.getIntegerPreference(RegisteredGraphicalThemes.INITIAL_EDITOR_WIDTH);
  			        int height = coordinator.getIntegerPreference(RegisteredGraphicalThemes.INITIAL_EDITOR_HEIGHT);

  			        viewer.constructComponent();
  			        coordinator.getDock().createExternalPaletteDockable(
  			            "Compositional hierarchy",
  			            COMPOSITION_ICON,
  			            new Point(x, y), new Dimension(width, height),
  			            true,
  			            true,
  			            new JScrollPane(panel));
  						}
  	      	});	      	
  	      }
        }
			}
      
      if (!diagramReadOnly)
      {
        Utilities.addSeparator(popup);
  			if (miniAppearanceFacet != null && miniAppearanceFacet.haveMiniAppearance())
  				popup.add(getDisplayAsIconItem(diagramView, coordinator));
  			if (!isPart)
  				popup.add(getVisualLockItem(diagramView, coordinator));
        popup.add(BasicNamespaceNodeGem.getChangeColorItem(diagramView, coordinator, figureFacet, fillColor));
      }
      
			return popup;
		}
		
    private void copyCanonicalConstituents(DEStratum perspective, BBComponent bb, DEComponent me)
		{
    	for (DeltaPair p : me.getDeltas(ConstituentTypeEnum.DELTA_PORT).getConstituents(perspective, true))
    		bb.settable_getAddedPorts().add(UML2PortConverter.makePort(p.getUuid(), (UML2Port) p.getConstituent()));
    	for (DeltaPair p : me.getDeltas(ConstituentTypeEnum.DELTA_ATTRIBUTE).getConstituents(perspective, true))
    		bb.settable_getAddedAttributes().add(UML2AttributeConverter.makeAttribute(p.getUuid(), (UML2Attribute) p.getConstituent()));
    	for (DeltaPair p : me.getDeltas(ConstituentTypeEnum.DELTA_PART).getConstituents(perspective, true))
    		bb.settable_getAddedParts().add(UML2PartConverter.makePart(p.getUuid(), (UML2Part) p.getConstituent()));
    	for (DeltaPair p : me.getDeltas(ConstituentTypeEnum.DELTA_CONNECTOR).getConstituents(perspective, true))
    		bb.settable_getAddedConnectors().add(UML2ConnectorConverter.makeConnector(p.getUuid(), (UML2Connector) p.getConstituent()));
    	for (DeltaPair p : me.getDeltas(ConstituentTypeEnum.DELTA_PORT_LINK).getConstituents(perspective, true))
    		bb.settable_getAddedPortLinks().add(UML2ConnectorConverter.makeConnector(p.getUuid(), (UML2Connector) p.getConstituent()));    	
    	for (DeltaPair p : me.getDeltas(ConstituentTypeEnum.DELTA_APPLIED_STEREOTYPE).getConstituents(perspective, true))
    		bb.settable_getReplacedAppliedStereotypes().add(UML2AppliedStereotypeConverter.makeAppliedStereotype((UML2AppliedStereotype) p.getConstituent()));
		}

		private JMenuItem getBlackboxItem(DiagramViewFacet diagramView, final ToolCoordinatorFacet coordinator)
    {
      // we must have an applicable property of "cluster"
      final Classifier cls = (subject instanceof Property) ? (Classifier) ((Property) subject).undeleted_getType() : (Classifier) subject;
      if (cls == null)
        return null;
      
      // find the value
      final boolean value = StereotypeUtilities.extractBooleanProperty(cls, "cluster");
      JCheckBoxMenuItem item = new JCheckBoxMenuItem("Blackbox");
      item.setSelected(!value);
      
      item.addItemListener(new ItemListener()
          {
            public void itemStateChanged(ItemEvent e)
            {
              coordinator.executeCommandAndUpdateViews(
                  StereotypeUtilities.formSetBooleanRawStereotypeAttributeCommand(cls, "cluster", !value));
            }
          });
      return item;
    }
    
    public JMenuItem getNavigableItem(DiagramViewFacet diagramView, final ToolCoordinatorFacet coordinator)
    {
      // we must have an applicable property of "cluster"
      final Classifier cls = (subject instanceof Property) ? (Classifier) ((Property) subject).undeleted_getType() : (Classifier) subject;
      if (cls == null)
        return null;
      
      // find the value
      final boolean value = StereotypeUtilities.extractBooleanProperty(cls, "navigable");
      JCheckBoxMenuItem item = new JCheckBoxMenuItem("Navigable");
      item.setSelected(value);
      
      item.addItemListener(new ItemListener()
          {
            public void itemStateChanged(ItemEvent e)
            {
              coordinator.executeCommandAndUpdateViews(
                  StereotypeUtilities.formSetBooleanRawStereotypeAttributeCommand(cls, "navigable", !value));
            }
          });
      return item;
    }


    private JMenuItem getShowStereotypeCommand(DiagramViewFacet diagramView, final ToolCoordinatorFacet coordinator)
    {
      JMenuItem item = new JCheckBoxMenuItem("Stereotype");
      item.setSelected(showStereotype);
      item.addItemListener(new ItemListener()
          {
            private Command resizeCommand;
        
            public void itemStateChanged(ItemEvent e)
            {
              Command showStereotypeCommand = new AbstractCommand(
                  showStereotype ? "hid stereotype" : "showed stereotype",
                  !showStereotype ? "hid stereotype" : "showed stereotype")
              {
                public void execute(boolean isTop)
                {
                  showStereotype = !showStereotype;
                  ClassifierSizeInfo info = makeCurrentInfo();
                  resizeCommand =
                    figureFacet.makeAndExecuteResizingCommand(
                        formCentredBounds(info, contents.getFigureFacet().getFullBounds()).getOuter());                  
                }

                public void unExecute()
                {
                  showStereotype = !showStereotype;
                  resizeCommand.unExecute();
                } 
              };
              coordinator.executeCommandAndUpdateViews(showStereotypeCommand);
            }
          });
      return item;
    }

    private JMenuItem getActiveItem(DiagramViewFacet diagramView, final ToolCoordinatorFacet coordinator)
    {
      JMenuItem item = new JCheckBoxMenuItem("Active");
      if (subject == null)
      {
        item.setEnabled(false);
        return item;
      }      
      
      Class cls = (Class) subject;
      final boolean oldActive = cls.isActive();
      item.setSelected(oldActive);
      item.addItemListener(new ItemListener()
          {
            public void itemStateChanged(ItemEvent e)
            {
              Command changeActiveCommand = new AbstractCommand(
                  "Changed active to " + !oldActive,
                  "Reverted active to " + oldActive)
              {
                public void execute(boolean isTop)
                {
                  Class cls = (Class) subject;
                  cls.setIsActive(!oldActive);
                }

                public void unExecute()
                {
                  Class cls = (Class) subject;
                  cls.setIsActive(oldActive);
                } 
              };
              coordinator.executeCommandAndUpdateViews(changeActiveCommand);
            }
          });
      return item;
    }

		public JMenuItem getHideContentsItem(final DiagramViewFacet diagramView, final ToolCoordinatorFacet coordinator)
		{
			// for autosizing
			JCheckBoxMenuItem toggleAutoSizeItem = new JCheckBoxMenuItem("Contents");
			toggleAutoSizeItem.setState(suppressContents || autoSized);
			toggleAutoSizeItem.setEnabled(!autoSized);
			toggleAutoSizeItem.addActionListener(new ActionListener()
				 {
						public void actionPerformed(ActionEvent e)
						{
							// toggle the autosized flag (as a command)
							Command hideContentsCommand =
								new HideContentsCommand(
								figureFacet.getFigureReference(),
								!suppressContents,
							  suppressContents ? "unhide contents" : "suppress contents",
							  !suppressContents ? "unhide contents" : "unsuppress contents");
							coordinator.executeCommandAndUpdateViews(hideContentsCommand);
						}
					});
			return toggleAutoSizeItem;
		}
		
		private JMenuItem getSuppressOwnerItem(final DiagramViewFacet diagramView, final ToolCoordinatorFacet coordinator)
		{
			// for adding operations
			JCheckBoxMenuItem showVisibilityItem = new JCheckBoxMenuItem("Suppress owner");
			showVisibilityItem.setState(forceSuppressOwningPackage);
			
			showVisibilityItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					// toggle the suppress operations flag
					Command forceSuppressOwningPackageCommand = new SuppressOwningPackageCommand(figureFacet.getFigureReference(), !forceSuppressOwningPackage, forceSuppressOwningPackage ? "unsuppressed owner package" : "suppressed owner package", forceSuppressOwningPackage ? "suppressed owner package" : "unsuppressed owner package");
					coordinator.executeCommandAndUpdateViews(forceSuppressOwningPackageCommand);
				}
			});
			return showVisibilityItem;
		}
		
		private JMenuItem getSuppressOperationsItem(final DiagramViewFacet diagramView, final ToolCoordinatorFacet coordinator)
		{
			// for adding operations
			JCheckBoxMenuItem suppressOperationsItem = new JCheckBoxMenuItem("Operations");
			suppressOperationsItem.setState(suppressOperations);
			
			suppressOperationsItem.addActionListener(new ActionListener()
																							 {
						public void actionPerformed(ActionEvent e)
						{
							// toggle the suppress attributes flag
							Command suppressFeaturesCommand = FeatureSuppressToggleManipulator.makeToggleSuppressFeaturesCommand(
								figureFacet.getFigureReference(), figureFacet.getFigureName(), OperationCreatorGem.FEATURE_TYPE, "operations", suppressOperations);
							coordinator.executeCommandAndUpdateViews(suppressFeaturesCommand);
						}
					});
			return suppressOperationsItem;
		}
		
		private JMenuItem getDisplayAsIconItem(final DiagramViewFacet diagramView, final ToolCoordinatorFacet coordinator)
		{
			// for adding operations
			JCheckBoxMenuItem displayAsIconItem = new JCheckBoxMenuItem("Display as icon");
			displayAsIconItem.setState(displayOnlyIcon);
			displayAsIconItem.setEnabled(!hasSubstitutions());
			
			displayAsIconItem.addActionListener(new ActionListener()
																					{
						public void actionPerformed(ActionEvent e)
						{
							// toggle the suppress operations flag
							Command displayAsIconCommand =
								new DisplayAsIconCommand(
								figureFacet.getFigureReference(),
								!displayOnlyIcon,
								"displayed " + getFigureName() + (displayOnlyIcon ? " as box" : " as icon"),
								"displayed " + getFigureName() + (!displayOnlyIcon ? " as box" : " as icon"));
							coordinator.executeCommandAndUpdateViews(displayAsIconCommand);
						}
					});
			return displayAsIconItem;
		}
		
		private JMenuItem getSuppressAttributesOrSlotsItem(final DiagramViewFacet diagramView, final ToolCoordinatorFacet coordinator)
		{
			// for adding attributes
      final String name = isPart ? "Slots" : "Attributes";
			JCheckBoxMenuItem suppressAttributesItem = new JCheckBoxMenuItem(name);
			suppressAttributesItem.setState(suppressAttributesOrSlots);
			
			suppressAttributesItem.addActionListener(new ActionListener()
																							 {
						public void actionPerformed(ActionEvent e)
						{
							// toggle the suppress attributes flag
							Command suppressFeaturesCommand = FeatureSuppressToggleManipulator.makeToggleSuppressFeaturesCommand(
								figureFacet.getFigureReference(),
                figureFacet.getFigureName(),
                isPart ? SlotCreatorGem.FEATURE_TYPE : AttributeCreatorGem.FEATURE_TYPE,
                name,
                suppressAttributesOrSlots);
							coordinator.executeCommandAndUpdateViews(suppressFeaturesCommand);
						}
					});
			return suppressAttributesItem;
		}
		
		private JMenuItem getVisualLockItem(final DiagramViewFacet diagramView, final ToolCoordinatorFacet coordinator)
		{
			// for adding attributes
			JCheckBoxMenuItem lockItem = new JCheckBoxMenuItem("Visual lock");
			lockItem.setState(locked);
			
			lockItem.addActionListener(new ActionListener()
																							 {
						public void actionPerformed(ActionEvent e)
						{
							// toggle the suppress attributes flag
							Command lockCommand = new VisualLockCommand(
								figureFacet.getFigureReference(),
                !locked,
                locked ? "Unlocked visuals" : "Locked visuals",
                locked ? "Locked visuals" : "Unlocked visuals");
							coordinator.executeCommandAndUpdateViews(lockCommand);
						}
					});
			return lockItem;
		}
		
		private JMenuItem getSuppressAllItem(final DiagramViewFacet diagramView, final ToolCoordinatorFacet coordinator)
		{
			// for adding attributes
			JMenuItem suppressAllItem = new JMenuItem("Suppress all");
			
			suppressAllItem.addActionListener(new ActionListener()
																				{
						public void actionPerformed(ActionEvent e)
						{
							CompositeCommand comp = new CompositeCommand("suppress all for " + getFigureName(), "unsuppress all for " + getFigureName());
							FigureReference ref = figureFacet.getFigureReference();
              String name = isPart ? "slots" : "attributes";
							comp.addCommand(
								new SuppressFeaturesCommand(
															 ref,
                               isPart ? SlotCreatorGem.FEATURE_TYPE : AttributeCreatorGem.FEATURE_TYPE,
															 true,
															 "hid " + name + " for " + getFigureName(),
															 "showed " + name + " for " + getFigureName()));
							comp.addCommand(
								new SuppressFeaturesCommand(
															 ref,
															 OperationCreatorGem.FEATURE_TYPE,
															 true,
															 "hid operations for " + getFigureName(),
															 "showed operations for " + getFigureName()));
							comp.addCommand(
									new HideContentsCommand(
																 ref,
																 true,
																 "hid contents for " + getFigureName(),
																 "showed contents for " + getFigureName()));
							coordinator.executeCommandAndUpdateViews(comp);
						}
					});
			return suppressAllItem;
		}
		
		private JMenuItem getUnsuppressAllItem(final DiagramViewFacet diagramView, final ToolCoordinatorFacet coordinator)
		{
			// for adding attributes
			JMenuItem suppressAllItem = new JMenuItem("Unsuppress all");
			
			suppressAllItem.addActionListener(new ActionListener()
																				{
						public void actionPerformed(ActionEvent e)
						{
							CompositeCommand comp = new CompositeCommand("unsuppress all for " + getFigureName(), "suppress all for " + getFigureName());
							FigureReference ref = figureFacet.getFigureReference();
              String name = isPart ? "slots" : "attributes";
							comp.addCommand(
								new SuppressFeaturesCommand(
															 ref,
															 isPart ? SlotCreatorGem.FEATURE_TYPE : AttributeCreatorGem.FEATURE_TYPE,
															 false,
															 "showed " + name + " for " + getFigureName(),
															 "unshowed " + name + " for " + getFigureName()));
							comp.addCommand(
								new SuppressFeaturesCommand(
															 ref,
															 OperationCreatorGem.FEATURE_TYPE,
															 false,
															 "showed operations for " + getFigureName(),
															 "unshowed operations for " + getFigureName()));
							comp.addCommand(
									new HideContentsCommand(
																 ref,
																 false,
																 "showed contents for " + getFigureName(),
																 "unshowed contents for " + getFigureName()));
							coordinator.executeCommandAndUpdateViews(comp);
						}
					});
			return suppressAllItem;
		}
		
		private JMenuItem getAddAttributeItem(final DiagramViewFacet diagramView, final ToolCoordinatorFacet coordinator)
		{
			// for adding operations
			JMenuItem addAttributeItem = new JMenuItem("Add attribute");
			addAttributeItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					DiagramFacet diagram = figureFacet.getDiagram();
					final FigureReference reference = diagram.makeNewFigureReference();
          PersistentProperties props = new PersistentProperties();
          props.add(new PersistentProperty(">stereotype", CommonRepositoryFunctions.ATTRIBUTE));
          
					AddFeatureCommand add =
						new AddFeatureCommand(
						attributesOrSlots.getFigureFacet().getFigureReference(),
						reference,
						new AttributeCreatorGem().getNodeCreateFacet(),
				    props,
				    null,
				    null,
						"added " + name + " to classifier",
						"removed " + name + " from classifier",
						null);
					coordinator.executeCommandAndUpdateViews(add);
					
					diagramView.runWhenModificationsHaveBeenProcessed(
							new Runnable()
							{
								public void run()
								{
									diagramView.getSelection().clearAllSelection();
								  diagramView.addFigureToSelectionViaId(reference.getId());
								}
							});
				}
			});
			
			if (suppressAttributesOrSlots)
				addAttributeItem.setEnabled(false);
			return addAttributeItem;
		}
		
    public JMenuItem getShowAllAttributesMenuItem(final ToolCoordinatorFacet coordinator)
    {
      JMenuItem showAllItem = new JMenuItem("Attributes");
      final ClassifierAttributeHelper attributeHelper =
        new ClassifierAttributeHelper(
            figureFacet,
            primitiveAttributesOrSlots,
            attributesOrSlots,
            true);
      showAllItem.setEnabled(!attributeHelper.isShowingAllConstituents() && primitiveAttributesOrSlots.isShowing());
      
      showAllItem.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          // toggle the autosized flag (as a command)
          final FigureReference reference = attributesOrSlots.getFigureFacet().getFigureReference(); 
          Command showAllCmd = new AbstractCommand("Showed all attributes", "Un-showed all attributes")
          {
            private Set<String>[] oldUuids;

            public void execute(boolean isTop)
            {
              oldUuids = getDeletedUuidsFacet().getAddedAndDeleted();
              getDeletedUuidsFacet().setToShowAll(getVisuallySuppressedUUIDs(ConstituentTypeEnum.DELTA_ATTRIBUTE));
            }

            public void unExecute()
            {
              getDeletedUuidsFacet().setAddedAndDeleted(oldUuids);
            }
            
            FeatureCompartmentFacet getDeletedUuidsFacet()
            {
              FigureFacet figure = GlobalDiagramRegistry.registry.retrieveFigure(reference);
              return (FeatureCompartmentFacet) figure.getDynamicFacet(FeatureCompartmentFacet.class);
            }
          };
          
          CompositeCommand cmd = new CompositeCommand(showAllCmd);
          cmd.addCommand(attributeHelper.makeUpdateCommand(false));
            
          coordinator.executeCommandAndUpdateViews(cmd);
        }
      });
      return showAllItem;     
    }
		
    public JMenuItem getShowSpecificAttributesMenuItem(final ToolCoordinatorFacet coordinator)
    {
      JMenu showItem = new JMenu("Show specific attributes...");
      final ClassifierAttributeHelper attributeHelper =
        new ClassifierAttributeHelper(
            figureFacet,
            primitiveAttributesOrSlots,
            attributesOrSlots,
            true);
      Map<String, String> hidden = attributeHelper.getHiddenConstituents();
      showItem.setEnabled(!hidden.isEmpty());
      
      for (final String uuid : hidden.keySet())
      {
      	String name = hidden.get(uuid);
      	JMenuItem selective = new JMenuItem(name);
      	showItem.add(selective);
	      selective.addActionListener(new ActionListener()
	      {
	        public void actionPerformed(ActionEvent e)
	        {
	          // toggle the autosized flag (as a command)
	          final FigureReference reference = attributesOrSlots.getFigureFacet().getFigureReference(); 
	          Command showAllCmd = new AbstractCommand("Showed attribute", "Un-showed attribute")
	          {
	            public void execute(boolean isTop)
	            {
	            	getDeletedUuidsFacet().removeDeleted(uuid);
	            }
	
	            public void unExecute()
	            {
	            	getDeletedUuidsFacet().addDeleted(uuid);
	            }
	            
	            FeatureCompartmentFacet getDeletedUuidsFacet()
	            {
	              FigureFacet figure = GlobalDiagramRegistry.registry.retrieveFigure(reference);
	              return (FeatureCompartmentFacet) figure.getDynamicFacet(FeatureCompartmentFacet.class);
	            }
	          };
	          
	          CompositeCommand cmd = new CompositeCommand(showAllCmd);
	          cmd.addCommand(attributeHelper.makeUpdateCommand(false));
	            
	          coordinator.executeCommandAndUpdateViews(cmd);
	        }
	      });
      }
      MenuAccordion.makeMultiColumn(showItem, null, true);
      return showItem;     
    }
		
    public JMenuItem getShowSpecificPortsMenuItem(final ToolCoordinatorFacet coordinator)
    {
      JMenu showItem = new JMenu("Show specific ports...");
      final ClassPortHelper portHelper =
        new ClassPortHelper(
            figureFacet,
            primitivePorts,
            ports,
            true);
      Map<String, String> hidden = portHelper.getHiddenConstituents();
      showItem.setEnabled(!hidden.isEmpty());
      
      for (final String uuid : hidden.keySet())
      {
      	String name = hidden.get(uuid);
      	JMenuItem selective = new JMenuItem(name);
      	showItem.add(selective);
	      selective.addActionListener(new ActionListener()
	      {
	        public void actionPerformed(ActionEvent e)
	        {
	          // toggle the autosized flag (as a command)
	          final FigureReference reference = ports.getFigureFacet().getFigureReference(); 
	          Command showAllCmd = new AbstractCommand("Showed port", "Un-showed port")
	          {
	            public void execute(boolean isTop)
	            {
	            	getDeletedUuidsFacet().removeDeleted(uuid);
	            }
	
	            public void unExecute()
	            {
	            	getDeletedUuidsFacet().addDeleted(uuid);
	            }
	            
	            SimpleDeletedUuidsFacet getDeletedUuidsFacet()
	            {
	              FigureFacet figure = GlobalDiagramRegistry.registry.retrieveFigure(reference);
	              return (SimpleDeletedUuidsFacet) figure.getDynamicFacet(SimpleDeletedUuidsFacet.class);
	            }
	          };
	          
	          CompositeCommand cmd = new CompositeCommand(showAllCmd);
	          cmd.addCommand(portHelper.makeUpdateCommand(false));
	            
	          coordinator.executeCommandAndUpdateViews(cmd);
	        }
	      });
      }
      MenuAccordion.makeMultiColumn(showItem, null, true);
      return showItem;     
    }
		
    public JMenuItem getShowSpecificPortInstancesMenuItem(final ToolCoordinatorFacet coordinator)
    {
      JMenu showItem = new JMenu("Show specific ports instances...");
      final PartPortInstanceHelper portHelper =
        new PartPortInstanceHelper(
            figureFacet,
            primitivePorts,
            true);
      Map<String, String> hidden = portHelper.getHiddenConstituents();
      showItem.setEnabled(!hidden.isEmpty());
      
      for (final String uuid : hidden.keySet())
      {
      	String name = hidden.get(uuid);
      	JMenuItem selective = new JMenuItem(name);
      	showItem.add(selective);
	      selective.addActionListener(new ActionListener()
	      {
	        public void actionPerformed(ActionEvent e)
	        {
	          // toggle the autosized flag (as a command)
	          final FigureReference reference = ports.getFigureFacet().getFigureReference(); 
	          Command showAllCmd = new AbstractCommand("Showed port instance", "Un-showed port instance")
	          {
	            public void execute(boolean isTop)
	            {
	            	getDeletedUuidsFacet().removeDeleted(uuid);
	            }
	
	            public void unExecute()
	            {
	            	getDeletedUuidsFacet().addDeleted(uuid);
	            }
	            
	            SimpleDeletedUuidsFacet getDeletedUuidsFacet()
	            {
	              FigureFacet figure = GlobalDiagramRegistry.registry.retrieveFigure(reference);
	              return (SimpleDeletedUuidsFacet) figure.getDynamicFacet(SimpleDeletedUuidsFacet.class);
	            }
	          };
	          
	          CompositeCommand cmd = new CompositeCommand(showAllCmd);
	          cmd.addCommand(portHelper.makeUpdateCommand(ports, false));
	            
	          coordinator.executeCommandAndUpdateViews(cmd);
	        }
	      });
      }
      MenuAccordion.makeMultiColumn(showItem, null, true);
      return showItem;     
    }
		
    public JMenuItem getShowAllConnectorsMenuItem(final ToolCoordinatorFacet coordinator)
    {
    	final String name = "Connectors and port links";
      JMenuItem showAllItem = new JMenuItem(name);
      final ClassConnectorHelper connectorHelper =
        new ClassConnectorHelper(
            figureFacet,
            primitivePorts,
            primitiveContents,
            false,
            deletedConnectorUuidsFacet,
            true);
      final ClassConnectorHelper portLinkHelper =
        new ClassConnectorHelper(
            figureFacet,
            primitivePorts,
            primitiveContents,
            true,
            deletedConnectorUuidsFacet,
            true);
      showAllItem.setEnabled(
      		(!connectorHelper.isShowingAllConstituents() || !portLinkHelper.isShowingAllConstituents()) 
      		&& primitiveContents.isShowing());
      
      showAllItem.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          // toggle the autosized flag (as a command)
          final FigureReference reference = figureFacet.getFigureReference(); 
          Command showAllCmd = new AbstractCommand("Showed all " + name, "Un-showed all " + name)
          {
            private Set<String>[] oldUuids;

            public void execute(boolean isTop)
            {
              oldUuids = getDeletedUuidsFacet().getAddedAndDeleted();
              getDeletedUuidsFacet().setToShowAll(getVisuallySuppressedUUIDs(ConstituentTypeEnum.DELTA_PART));
            }

            public void unExecute()
            {
              getDeletedUuidsFacet().setAddedAndDeleted(oldUuids);
            }
            
            SimpleDeletedUuidsFacet getDeletedUuidsFacet()
            {
              FigureFacet figure = GlobalDiagramRegistry.registry.retrieveFigure(reference);
              return (SimpleDeletedUuidsFacet) figure.getDynamicFacet(SimpleDeletedUuidsFacet.class);
            }
          };
          
          CompositeCommand cmd = new CompositeCommand(showAllCmd);
          cmd.addCommand(connectorHelper.makeUpdateCommand(false));
            
          coordinator.executeCommandAndUpdateViews(cmd);
        }
      });
      return showAllItem;     
    }
    
    public JMenuItem getShowAllSlotsMenuItem(final ToolCoordinatorFacet coordinator)
    {
      JMenuItem showAllItem = new JMenuItem("Slots");
      final PartSlotHelper slotHelper =
        new PartSlotHelper(
            figureFacet,
            primitiveAttributesOrSlots,
            true);
      showAllItem.setEnabled(!slotHelper.isShowingAllConstituents() && primitiveAttributesOrSlots.isShowing());
      
      showAllItem.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          // toggle the autosized flag (as a command)
          final FigureReference reference = attributesOrSlots.getFigureFacet().getFigureReference(); 
          Command showAllCmd = new AbstractCommand("Showed all attributes", "Un-showed all attributes")
          {
            private Set<String>[] oldUuids;

            public void execute(boolean isTop)
            {
              oldUuids = getDeletedUuidsFacet().getAddedAndDeleted();
              getDeletedUuidsFacet().setToShowAll(getVisuallySuppressedUUIDs(ConstituentTypeEnum.DELTA_ATTRIBUTE));
            }

            public void unExecute()
            {
              getDeletedUuidsFacet().setAddedAndDeleted(oldUuids);
            }
            
            FeatureCompartmentFacet getDeletedUuidsFacet()
            {
              FigureFacet figure = GlobalDiagramRegistry.registry.retrieveFigure(reference);
              return (FeatureCompartmentFacet) figure.getDynamicFacet(FeatureCompartmentFacet.class);
            }
          };
          
          CompositeCommand cmd = new CompositeCommand(showAllCmd);
          cmd.addCommand(slotHelper.makeUpdateCommand(attributesOrSlots, false));
            
          coordinator.executeCommandAndUpdateViews(cmd);
        }
      });
      return showAllItem;     
    }
    
    public JMenuItem getShowAllPortInstancesMenuItem(final ToolCoordinatorFacet coordinator)
    {
      JMenuItem showAllItem = new JMenuItem("Port instances");
      final PartPortInstanceHelper portInstanceHelper =
        new PartPortInstanceHelper(
            figureFacet,
            primitivePorts,
            true);
      showAllItem.setEnabled(!portInstanceHelper.isShowingAllConstituents() && primitivePorts.isShowing());
      
      showAllItem.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          // toggle the autosized flag (as a command)
          final FigureReference reference = ports.getFigureFacet().getFigureReference(); 
          Command showAllCmd = new AbstractCommand("Showed all port instances", "Un-showed all port instances")
          {
            private Set<String>[] oldUuids;

            public void execute(boolean isTop)
            {
              oldUuids = getDeletedUuidsFacet().getAddedAndDeleted();
              getDeletedUuidsFacet().setToShowAll(getVisuallySuppressedUUIDs(ConstituentTypeEnum.DELTA_PORT));
            }

            public void unExecute()
            {
              getDeletedUuidsFacet().setAddedAndDeleted(oldUuids);
            }
            
            PortCompartmentFacet getDeletedUuidsFacet()
            {
              FigureFacet figure = GlobalDiagramRegistry.registry.retrieveFigure(reference);
              return (PortCompartmentFacet) figure.getDynamicFacet(PortCompartmentFacet.class);
            }
          };
          
          CompositeCommand cmd = new CompositeCommand(showAllCmd);
          cmd.addCommand(portInstanceHelper.makeUpdateCommand(ports, false));
            
          coordinator.executeCommandAndUpdateViews(cmd);
        }
      });
      return showAllItem;     
    }
    
    public JMenuItem getShowAllPartsMenuItem(final ToolCoordinatorFacet coordinator)
    {
      JMenuItem showAllItem = new JMenuItem("Parts");
      final ClassPartHelper partHelper =
        new ConcreteClassPartHelper(
            figureFacet,
            primitiveContents,
            contents,
            true);
      showAllItem.setEnabled(!partHelper.isShowingAllConstituents() && primitiveContents.isShowing());
      
      showAllItem.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          // toggle the autosized flag (as a command)
          final FigureReference reference = contents.getFigureFacet().getFigureReference(); 
          Command showAllCmd = new AbstractCommand("Showed all parts", "Un-showed all parts")
          {
            private Set<String>[] oldUuids;

            public void execute(boolean isTop)
            {
              oldUuids = getDeletedUuidsFacet().getAddedAndDeleted();
              getDeletedUuidsFacet().setToShowAll(getVisuallySuppressedUUIDs(ConstituentTypeEnum.DELTA_PART));
            }

            public void unExecute()
            {
              getDeletedUuidsFacet().setAddedAndDeleted(oldUuids);
            }
            
            SimpleContainerFacet getDeletedUuidsFacet()
            {
              FigureFacet figure = GlobalDiagramRegistry.registry.retrieveFigure(reference);
              return (SimpleContainerFacet) figure.getDynamicFacet(SimpleContainerFacet.class);
            }
          };
          
          CompositeCommand cmd = new CompositeCommand(showAllCmd);
          cmd.addCommand(partHelper.makeUpdateCommand(false));
            
          coordinator.executeCommandAndUpdateViews(cmd);
        }
      });
      return showAllItem;     
    }
    
    public JMenuItem getShowAllPortsMenuItem(final ToolCoordinatorFacet coordinator)
    {
      // for autosizing
      JMenuItem showAllItem = new JMenuItem("Ports");
      final ClassPortHelper portHelper =
        new ClassPortHelper(
            figureFacet,
            primitivePorts,
            ports,
            true);
      showAllItem.setEnabled(!portHelper.isShowingAllConstituents() && primitivePorts.isShowing());
      
      showAllItem.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          // toggle the autosized flag (as a command)
          final FigureReference reference = ports.getFigureFacet().getFigureReference(); 
          Command showAllCmd = new AbstractCommand("Showed all ports", "Un-showed all ports")
          {
            private Set<String>[] oldUuids;

            public void execute(boolean isTop)
            {
              oldUuids = getDeletedUuidsFacet().getAddedAndDeleted();
              getDeletedUuidsFacet().setToShowAll(getVisuallySuppressedUUIDs(ConstituentTypeEnum.DELTA_PORT));
            }

            public void unExecute()
            {
              getDeletedUuidsFacet().setAddedAndDeleted(oldUuids);
            }
            
            PortCompartmentFacet getDeletedUuidsFacet()
            {
              FigureFacet figure = GlobalDiagramRegistry.registry.retrieveFigure(reference);
              return (PortCompartmentFacet) figure.getDynamicFacet(PortCompartmentFacet.class);
            }
          };
          
          CompositeCommand cmd = new CompositeCommand(showAllCmd);
          cmd.addCommand(portHelper.makeUpdateCommand(false));
            
          coordinator.executeCommandAndUpdateViews(cmd);
        }
      });
      return showAllItem;     
    }
    
    public JMenuItem getShowAllOperationsMenuItem(final ToolCoordinatorFacet coordinator)
    {
      // for autosizing
      JMenuItem showAllItem = new JMenuItem("Operations");
      final ClassifierOperationHelper operationHelper =
        new ClassifierOperationHelper(
            figureFacet,
            primitiveOperations,
            operations,
            true);
      showAllItem.setEnabled(!operationHelper.isShowingAllConstituents() && primitiveOperations.isShowing());
      
      showAllItem.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          // toggle the autosized flag (as a command)
          final FigureReference reference = operations.getFigureFacet().getFigureReference(); 
          Command showAllCmd = new AbstractCommand("Showed all operations", "Un-showed all operations")
          {
            private Set<String>[] oldUuids;

            public void execute(boolean isTop)
            {
              oldUuids = getOperations().getAddedAndDeleted();
              getOperations().setToShowAll(getVisuallySuppressedUUIDs(ConstituentTypeEnum.DELTA_OPERATION));
            }

            public void unExecute()
            {
              getOperations().setAddedAndDeleted(oldUuids);
            }
            
            FeatureCompartmentFacet getOperations()
            {
              FigureFacet figure = GlobalDiagramRegistry.registry.retrieveFigure(reference);
              return (FeatureCompartmentFacet) figure.getDynamicFacet(FeatureCompartmentFacet.class);
            }
          };
          
          CompositeCommand cmd = new CompositeCommand(showAllCmd);
          cmd.addCommand(operationHelper.makeUpdateCommand(false));
            
          coordinator.executeCommandAndUpdateViews(cmd);
        }
      });
      return showAllItem;     
    }
    
    private JMenuItem getAddSlotItem(final DiagramViewFacet diagramView, final ToolCoordinatorFacet coordinator)
    {
      JMenu menu = new JMenu("Add slot");

      // only show unfilled slots
      List<DeltaPair> properties = findUnfilledSlots();
      List<JMenuItem> visual = new ArrayList<JMenuItem>();
      for (final DeltaPair pair : properties)
      {
        final String name = pair.getConstituent().getName();
        JMenuItem addSlotItem = new JMenuItem(name);
        menu.add(addSlotItem);
        addSlotItem.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
            DiagramFacet diagram = figureFacet.getDiagram();
            final FigureReference reference = diagram.makeNewFigureReference();
            PersistentProperties props = new PersistentProperties();
            props.add(new PersistentProperty(">stereotype", CommonRepositoryFunctions.SLOT));
            AddFeatureCommand add =
              new AddFeatureCommand(
              attributesOrSlots.getFigureFacet().getFigureReference(),
              reference,
              new SlotCreatorGem().getNodeCreateFacet(),
              props,
              null,
              pair.getOriginal().getRepositoryObject(),
              "added " + name + " slot",
              "removed " + name + " slot",
              null);
            coordinator.executeCommandAndUpdateViews(add);
            
            diagramView.runWhenModificationsHaveBeenProcessed(new Runnable()
            {
              public void run()
              {
                diagramView.getSelection().clearAllSelection();
            	  diagramView.addFigureToSelectionViaId(reference.getId());
              }
            });
          }
        });
        
        // see if this is a preferred item
        Element prop = (Element) pair.getConstituent().getRepositoryObject();
        if (!StereotypeUtilities.isRawStereotypeApplied(prop, CommonRepositoryFunctions.VISUALLY_SUPPRESS))
        	visual.add(addSlotItem);
      }
      
      // if the number of slots is too large, handle as an accordion
      MenuAccordion.makeMultiColumn(menu, visual, true);
      
      if (suppressAttributesOrSlots || properties.isEmpty())
        menu.setEnabled(false);
      return menu;
    }
    
    /**
     * get the unfilled slots, based on the possible slots and what is already taken
     * @return
     */
		private List<DeltaPair> findUnfilledSlots()
    {
      Class partType = (Class) ((Property) subject).undeleted_getType();
		  if (partType == null)
		    return new ArrayList<DeltaPair>();
		  
		  // get the set of properties of the type, from this visual perspective
		  Package current = PerspectiveHelper.extractStratum(figureFacet.getDiagram(), figureFacet.getContainerFacet());
		  List<DeltaPair> pairs = new ArrayList<DeltaPair>(UMLNodeText.getPossibleAttributes(current, partType));
		  
      // remove any slots that are already present in this instance
      InstanceSpecification specification = UMLTypes.extractInstanceOfPart(subject);
      if (specification != null)
      {
      	for (Object obj : specification.undeleted_getSlots())
      	{
      		Slot slot = (Slot) obj;
      		for (Iterator<DeltaPair> iter = pairs.iterator(); iter.hasNext();)
      		  if (iter.next().getOriginal().getRepositoryObject() == slot.getDefiningFeature())
      		    iter.remove();
      	}
      }
      
      return pairs;
    }

		private JMenuItem getAddOperationItem(final DiagramViewFacet diagramView, final ToolCoordinatorFacet coordinator)
		{
			// for adding operations
			JMenuItem addOperationItem = new JMenuItem("Add operation");
			addOperationItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					DiagramFacet diagram = figureFacet.getDiagram();
					final FigureReference reference = diagram.makeNewFigureReference();
					AddFeatureCommand add =
						new AddFeatureCommand(
						operations.getFigureFacet().getFigureReference(),
						reference,
						new OperationCreatorGem().getNodeCreateFacet(),
						null,
						null,
						null,
						"added operation to classifier",
						"removed operation from classifier",
						null);
					coordinator.executeCommandAndUpdateViews(add);
					diagramView.runWhenModificationsHaveBeenProcessed(new Runnable()
					{
						public void run()
						{
							diagramView.getSelection().clearAllSelection();
						  diagramView.addFigureToSelectionViaId(reference.getId());
						}
					});
				}
			});
			
			if (suppressOperations)
				addOperationItem.setEnabled(false);
			return addOperationItem;
		}
		
		/**
		 * find the ports that haven't been instantiated yet...
		 * @return
		 */
    private List<Port> findUnfilledPorts()
		{
    	Class type = (Class) ((Property) subject).undeleted_getType();
    	List<Port> allPorts = new ArrayList<Port>();
    	if (type != null)
    		for (Object obj : type.undeleted_getOwnedPorts())
    			allPorts.add((Port) obj);
    	
    	// remove any references to existing port instances -- have to do this off the visual representation
    	for (Iterator<FigureFacet> iter = ports.getFigureFacet().getContainerFacet().getContents(); iter.hasNext();)
    	{
    		FigureFacet visualPort = iter.next();
    		allPorts.remove(visualPort.getSubject());
    	}
    	
    	return allPorts;
		}

		public UBounds getAutoSizedBounds(boolean autoSized)
		{
			return null; // only used for figures with default auto-sizing
		}
		
		/**
		 * @see com.hopstepjump.jumble.nodefacilities.nodesupport.BasicNodeAppearanceFacet#acceptsContainer(ContainerFacet)
		 */
		public boolean acceptsContainer(ContainerFacet container)
		{
			if (!isPart)
				return true;
			
			// parts can't live outside of a class or another part
			if (container == null)
	  		return false;
	  		
	  	// look up a couple of levels until we find the class
			ContainerFacet classContainer = container.getContainedFacet().getContainer();
			if (classContainer == null)
				return false;
			Element classSubject = (Element) classContainer.getFigureFacet().getSubject();
      boolean visualNestingPartIsOk = UMLTypes.extractInstanceOfPart(classSubject) != null && ((Property) classSubject).undeleted_getType() != null;
			return classSubject instanceof Class  || visualNestingPartIsOk;
		}
		
		/**
		 * @see com.hopstepjump.idraw.nodefacilities.nodesupport.BasicNodeAppearanceFacet#getFullBoundsForContainment()
		 */
		public UBounds getFullBoundsForContainment()
		{
			if (!displayOnlyIcon)
      {
        UBounds classOnly = figureFacet.getFullBounds();
        // ask the port container also
        return classOnly.union(ports.getFigureFacet().getFullBoundsForContainment());
      }
			
			// must do this, because the size info structure doesn't know if the attributes or operations is empty
			ClassifierSizeInfo info = makeCurrentInfo();
			return getBoundsForContainment(info.makeActualSizes());
		}
		
		public UBounds getRecalculatedFullBounds(boolean diagramResize)
		{
			ClassifierSizeInfo info = makeCurrentInfo();
      if (!diagramResize)
        return info.makeActualSizes().getOuter();
			return formCentredBounds(info, info.makeActualSizes().getContents()).getOuter();
		}
		
		/**
		 * @see com.hopstepjump.idraw.nodefacilities.nodesupport.BasicNodeAppearanceFacet#addToPersistentProperties(PersistentProperties)
		 */
		public void addToPersistentProperties(PersistentProperties properties)
		{
			properties.add(new PersistentProperty("name", name));
			properties.add(new PersistentProperty("owner", owner));
			properties.add(new PersistentProperty("supA", suppressAttributesOrSlots, false));
			properties.add(new PersistentProperty("supO", suppressOperations, false));
			properties.add(new PersistentProperty("supC", suppressContents, false));
			properties.add(new PersistentProperty("auto", autoSized, true));
			properties.add(new PersistentProperty("icon", displayOnlyIcon, false));
      properties.add(new PersistentProperty("state", showAsState, false));
			properties.add(new PersistentProperty("tlOff", rememberedTLOffset, new UDimension(0, 0)));
			properties.add(new PersistentProperty("brOff", rememberedBROffset, new UDimension(0, 0)));
			properties.add(new PersistentProperty("showVis", showOwningPackage, false));
      properties.add(new PersistentProperty("showStereo", showStereotype, true));
			properties.add(new PersistentProperty("suppVis", forceSuppressOwningPackage, false));
      properties.add(new PersistentProperty("fill", fillColor, initialFillColor));
      properties.add(new PersistentProperty("addedUuids", deletedUuids));
      properties.add(new PersistentProperty("deletedUuids", deletedUuids));
      properties.add(new PersistentProperty("locked", locked, false));
		}
		
		/**
		 * @see com.hopstepjump.idraw.nodefacilities.nodesupport.BasicNodeAppearanceFacet#formViewUpdateCommandAfterSubjectChanged(boolean)
		 */
		public Command formViewUpdateCommandAfterSubjectChanged(boolean isTop, ViewUpdatePassEnum pass)
		{
      // handle the part
      if (isPart)
      	// handle the part
      	return formViewUpdateCommandAfterPartChanged(isTop, pass);
      else
      	// handle the classifier
      	return formViewUpdateCommandAfterClassifierChanged(isTop, pass);
		}
		
		/**
		 * form a view update command for a classifier
		 * @param isTop
		 * @param pass 
		 * @return
		 */
		private Command formViewUpdateCommandAfterPartChanged(boolean isTop, ViewUpdatePassEnum pass)
		{
		  if (pass == ViewUpdatePassEnum.START || pass == ViewUpdatePassEnum.PENULTIMATE)
		    return null;
		  
      // on the middle pass, add or delete any constituents
      if (pass == ViewUpdatePassEnum.MIDDLE)
      {
        // take the lock value from the enclosing classifier
      	FigureFacet classifier = figureFacet.getContainedFacet().getContainer().getContainedFacet().getContainer().getFigureFacet();
      	VisualLockFacet visual = (VisualLockFacet) classifier.getDynamicFacet(VisualLockFacet.class);
      	boolean lock = visual.isLocked();
        

      	
        // find any attributes to add or delete
        PartSlotHelper slotHelper =
          new PartSlotHelper(
              figureFacet,
              primitiveAttributesOrSlots,
              isTop);
  			attributesOrSlots.clean(getVisuallySuppressedUUIDs(ConstituentTypeEnum.DELTA_ATTRIBUTE), slotHelper.getConstituentUuids());
  
        // find any attributes to add or delete
        PartPortInstanceHelper portInstanceHelper =
          new PartPortInstanceHelper(
              figureFacet,
              primitivePorts,
              isTop);
  			ports.clean(getVisuallySuppressedUUIDs(ConstituentTypeEnum.DELTA_PORT), portInstanceHelper.getConstituentUuids());
  
        CompositeCommand cmd = new CompositeCommand("", "");
        if (!suppressAttributesOrSlots)
          cmd.addCommand(slotHelper.makeUpdateCommand(attributesOrSlots, lock));
        if (!displayOnlyIcon)
          cmd.addCommand(portInstanceHelper.makeUpdateCommand(ports, lock));
        
        return cmd;
      }
	    
			Property part = (Property) subject;

      // is this still a part?
			InstanceSpecification instance = UMLTypes.extractInstanceOfPart(subject);
			if (instance == null)
				return null;
			Classifier type = (Classifier) part.undeleted_getType();
			
      final int actualStereotypeHashcode =
      	StereotypeUtilities.calculateStereotypeHash(figureFacet, subject) +
      	StereotypeUtilities.calculateStereotypeHashAndComponentHash(figureFacet, type);
      boolean shouldBeState = type == null ? showAsState : StereotypeUtilities.isStereotypeApplied(type, "state");
      
			// is this active, or abstract
			boolean subjectActive = type == null || !(type instanceof Class) ? false : ((Class) type).isActive();			
			boolean subjectAbstract = type == null ? false : type.isAbstract();

			String newName;
			String typeName = new ElementProperties(figureFacet, subject).getPerspectiveName();
			if (subject.getName().length() == 0 && typeName.length() == 0)
				newName = "";
			else
				newName = subject.getName() + " : " + typeName;
			// if neither the name or the namespace has changed, or the in-placeness, suppress any command
			if (newName.equals(name) &&
					subjectAbstract == isAbstract &&
					subjectActive == isActive &&
          actualStereotypeHashcode == stereotypeHashcode &&
          shouldBeState == showAsState)
      {
				return null;
      }

			// now we are here, package it up into a nice command
			return new UpdateViewCommand(figureFacet.getFigureReference(), "updated view", "restored view");
		}		
		
		/**
		 * form a view update command for a classifier
		 * @param isTop
		 * @param pass = ViewUpdatePassEnum.FIRST 
		 * @return
		 */
		private Command formViewUpdateCommandAfterClassifierChanged(boolean isTop, ViewUpdatePassEnum pass)
		{
		  if (pass == ViewUpdatePassEnum.MIDDLE)
		    return null;
		  
		  // on the first pass, add or delete any constituents
		  if (pass == ViewUpdatePassEnum.START)
		  {
        CompositeCommand cmd = new CompositeCommand("", "");

	      // find any attributes to add or delete
	      ClassifierAttributeHelper attributeHelper =
          new ClassifierAttributeHelper(
              figureFacet,
              primitiveAttributesOrSlots,
              attributesOrSlots,
              isTop);
	      attributeHelper.cleanUuids();

	      // find any operations to add or delete
        ClassifierOperationHelper operationHelper =
          new ClassifierOperationHelper(
              figureFacet,
              primitiveOperations,
              operations,
              isTop);
        operationHelper.cleanUuids();
	      
        // find any ports to add or delete
        ClassPortHelper portHelper =
          new ClassPortHelper(
              figureFacet,
              primitivePorts,
              ports,
              isTop);
        portHelper.cleanUuids();
        
        // find any parts to add or delete
        ClassPartHelper partHelper =
          new ConcreteClassPartHelper(
              figureFacet,
              primitiveContents,
              contents,
              isTop);
        partHelper.cleanUuids();
        
        if (!suppressAttributesOrSlots)
          cmd.addCommand(attributeHelper.makeUpdateCommand(locked));
        if (!suppressOperations)
          cmd.addCommand(operationHelper.makeUpdateCommand(locked));
        if (!displayOnlyIcon)
          cmd.addCommand(portHelper.makeUpdateCommand(locked));
        if (isContentsShowing())
          cmd.addCommand(partHelper.makeUpdateCommand(locked));
        return cmd;
		  }
		  
      if (pass == ViewUpdatePassEnum.PENULTIMATE)
      {
      	CompositeCommand cmd = new CompositeCommand("", "");

      	// find any connectors to possibly add or delete
        ClassConnectorHelper connectorHelper =
          new ClassConnectorHelper(
              figureFacet,
              primitivePorts,
              primitiveContents,
              false,
              deletedConnectorUuidsFacet,
              isTop);

        // get the composite command for fixing up the connectors
        cmd.addCommand(connectorHelper.makeUpdateCommand(locked));

        // find any connectors or port links to possibly add or delete
        ClassConnectorHelper portLinkHelper =
          new ClassConnectorHelper(
              figureFacet,
              primitivePorts,
              primitiveContents,
              true,
              deletedConnectorUuidsFacet,
              isTop);
        // get the composite command for fixing up the port links
        cmd.addCommand(portLinkHelper.makeUpdateCommand(locked));
        portLinkHelper.cleanUuids(ConstituentTypeEnum.DELTA_CONNECTOR);
        
        return cmd;
      }
		  
      int actualStereotypeHashcode = StereotypeUtilities.calculateStereotypeHashAndComponentHash(figureFacet, subject);
      boolean shouldBeState = StereotypeUtilities.isStereotypeApplied(subject, "state");

			// should we be displaying the owner?
      SubjectRepositoryFacet repository = GlobalSubjectRepository.repository;
      Package visualStratum = repository.findVisuallyOwningStratum(
          figureFacet.getDiagram(),
          figureFacet.getContainedFacet().getContainer());
      Package owningStratum = repository.findOwningStratum(subject);
      boolean locatedInCorrectView = visualStratum == owningStratum;

			ElementProperties props = new ElementProperties(figureFacet, subject); 
			final boolean shouldBeDisplayingOwningPackage =
				!locatedInCorrectView && !forceSuppressOwningPackage || props.getElement().isSubstitution(); 

			// is this active?
			boolean subjectActive = false;
			if (subject instanceof Class)
			{
			  Class actualClassSubject = (Class) subject;
			  subjectActive = actualClassSubject.isActive();
			}
			String newName = props.getPerspectiveName();
			
			// get the new stratum owner
			String newOwner = props.getElement().isSubstitution() ?
					"(replaces " + props.getSubstitutesForName() + ")" :
					"(from " + repository.getFullStratumNames((Element) props.getHomePackage().getRepositoryObject()) + ")";
			
			// if neither the name or the namespace has changed, or the in-placeness, suppress any command
      Classifier classifierSubject = (Classifier) subject;
      ClassifierSizeInfo info = makeCurrentInfo();
			if (shouldBeDisplayingOwningPackage == showOwningPackage
			      && newName.equals(name) && owner.equals(newOwner)
						&& classifierSubject.isAbstract() == isAbstract
						&& subjectActive == isActive
            && stereotypeHashcode == actualStereotypeHashcode
            && attributeEllipsis == info.isEllipsisForAttributes()
            && operationEllipsis == info.isEllipsisForOperations()
            && bodyEllipsis == info.isEllipsisForBody() 
            && displayOnlyIcon == shouldDisplayOnlyIcon()
            && isElementRetired() == retired
            && shouldBeState == showAsState)
      {
				return null;
      }

			return
  			new UpdateViewCommand(
              figureFacet.getFigureReference(),
              "updated view",
              "restored view");
		}
		
		/**
		 * @see com.hopstepjump.idraw.nodefacilities.nodesupport.BasicNodeAppearanceFacet#middleButtonPressed(ToolCoordinatorFacet)
		 */
		public Command middleButtonPressed(ToolCoordinatorFacet coordinator)
		{
		  // look up through the owning namespaces, until we find a possible package
      if (subject != null)
      {
      	Element possiblePackage = isPart ? ((Property) subject).getType() : subject;
      	
  		  while (possiblePackage != null && !(possiblePackage instanceof Package))
  		    possiblePackage = possiblePackage.getOwner();
  		  
  		  if (possiblePackage != null)
  		  {
  				// want to open the diagram for this package
  				GlobalPackageViewRegistry.activeRegistry.open(
  				    (Package) possiblePackage,
  				    true,
  				    false,
  				    figureFacet.getFullBounds(),
  				    GlobalPackageViewRegistry.activeRegistry.getFocussedView().getFixedPerspective(),
  				    true);
  		  }
      }
      return null;
		}
		
		/**
		 * @see com.hopstepjump.idraw.nodefacilities.nodesupport.BasicNodeAppearanceFacet#getSubject()
		 */
		public NamedElement getSubject()
		{
			return subject;
		}
		
		/**
		 * @see com.hopstepjump.idraw.nodefacilities.nodesupport.BasicNodeAppearanceFacet#hasSubjectBeenDeleted()
		 */
		public boolean hasSubjectBeenDeleted()
		{
			// if this is a part, and the part no longer conforms, consider it deleted
			if (isPart && UMLTypes.extractInstanceOfPart(subject) == null)
				return true;
			
		  // cannot delete something with no subject
		  if (subject == null)
		    return false;
		  
			return subject.isThisDeleted();
		}

    public void produceEffect(ToolCoordinatorFacet coordinator, String effect, Object[] parameters)
    {
    }

    public Command getPostContainerDropCommand()
    {
      return null;
    }

		public boolean canMoveContainers()
		{
			return !isPart;
		}

    public boolean isSubjectReadOnlyInDiagramContext(boolean kill)
    {
      if (isPart)
      {
        // pass this on up to the container, as we may not be in the place where we are defined
        ContainerFacet container = figureFacet.getContainedFacet().getContainer();
        if (container == null)
          return true;
        
        // only truly writeable/moveable if this is owned by the same visual classifier
        // however, for a kill, this is fine
        if (!kill)
        {
          SubjectRepositoryFacet repository = GlobalSubjectRepository.repository;
          if (repository.findVisuallyOwningNamespace(figureFacet.getDiagram(), figureFacet.getContainedFacet().getContainer()) !=
              getPossibleDeltaSubject(subject).getOwner())
            return true;
        }
        
        // only writeable if the class is located correctly
        return GlobalSubjectRepository.repository.isContainerContextReadOnly(figureFacet);
      }
      else
      {
        SubjectRepositoryFacet repository = GlobalSubjectRepository.repository;
        Package visualStratum = repository.findVisuallyOwningStratum(
            figureFacet.getDiagram(),
            figureFacet.getContainedFacet().getContainer());
        Package owningStratum = repository.findOwningStratum(subject);
        boolean locatedInCorrectView = visualStratum == owningStratum;
  
        return GlobalSubjectRepository.repository.isContainerContextReadOnly(figureFacet) || !locatedInCorrectView;
      }
    }

    public Set<String> getDisplayStyles(boolean anchorIsTarget)
    {
      if (miniAppearanceFacet != null)
        return miniAppearanceFacet.getDisplayStyles(displayOnlyIcon, anchorIsTarget);
      return null;
    }

		public ToolFigureClassification getToolClassification(UPoint point, DiagramViewFacet diagramView, ToolCoordinatorFacet coordinator)
		{
			ToolFigureClassification tool =
				miniAppearanceFacet.getToolClassification(
					makeCurrentInfo().makeActualSizes(),
					displayOnlyIcon,
					suppressAttributesOrSlots,
					suppressOperations,
					suppressContents,
					primitivePorts.getContainerFacet().getContents().hasNext(),
					point);
			if (tool == null)
				tool = new ToolFigureClassification("?", "?");
			
			if (isPart)
			{
				tool.addMenuItem(getAddSlotItem(diagramView, coordinator));
			}
			else
			{
				if (!suppressAttributesOrSlots)
				{
					JMenuItem item = getAddAttributeItem(diagramView, coordinator);
					item.setIcon(ATTRIBUTE);
					item.setText("Attribute");
					tool.addMenuItem(item);
				}
				if (!suppressOperations)
				{
					JMenuItem item = getAddOperationItem(diagramView, coordinator);
					item.setIcon(OPERATION);
					item.setText("Operation");
					tool.addMenuItem(item);
				}
			}
			
			return tool;
		}

		private boolean inside(UBounds area, UPoint point)
		{
			return area.contains(point);
		}
	}
	
	private class ContainerFacetImpl implements BasicNodeContainerFacet
	{
		/**
		 * container related code
		 */
		public boolean insideContainer(UPoint point)
		{
			return figureFacet.getFullBoundsForContainment().contains(point);
		}
		
		/** returns true if area sweep in the container bounds is supported */
		public boolean isWillingToActAsBackdrop()
		{
			return false;
		}
		
		public void unAddContents(Object memento)
		{
			// not applicable -- has a fixed set of containables
		}
		
		public Object removeContents(ContainedFacet[] containables)
		{
			return null;
		}
		
		public void unRemoveContents(Object memento)
		{
			// not applicable -- has a fixed set of containables
		}
		
		public Object addContents(ContainedFacet[] containables)
		{
			return null;
		}
		
		public Iterator<FigureFacet> getContents()
		{
			List<FigureFacet> cont = new ArrayList<FigureFacet>();
			cont.add(attributesOrSlots.getFigureFacet());
			cont.add(operations.getFigureFacet());
			cont.add(contents.getFigureFacet());
			cont.add(ports.getFigureFacet());
			return cont.iterator();
		}
		
		/**
		 * @see com.giroway.jumble.figurefacilities.containmentbase.ToolContainerFigure#directlyAcceptsContainables()
		 */
		public boolean directlyAcceptsItems()
		{
			return true;
		}
		
		/**
		 * @see com.giroway.jumble.figurefacilities.containmentbase.IContainerable#getAcceptingSubcontainer(CmdContainable[])
		 */
		public ContainerFacet getAcceptingSubcontainer(ContainedFacet[] containables)
		{
			ContainerFacet delegate = null;
			if (!suppressOperations)
				delegate = operations.getFigureFacet().getContainerFacet().getAcceptingSubcontainer(containables);
			if (delegate == null)
			{
				if (!suppressAttributesOrSlots)
					delegate = attributesOrSlots.getFigureFacet().getContainerFacet().getAcceptingSubcontainer(containables);
			}
			if (delegate == null)
			{
			  delegate = ports.getFigureFacet().getContainerFacet().getAcceptingSubcontainer(containables);
			}

			return delegate;
		}
		
		public FigureFacet getFigureFacet()
		{
			return figureFacet;
		}
		
		/** many containers can also be contained */
		public ContainedFacet getContainedFacet()
		{
			return figureFacet.getContainedFacet();
		}
		
		/**
		 * @see com.hopstepjump.jumble.foundation.ContainerFacet#addChildPreviewsToCache(PreviewCacheFacet)
		 */
		public void addChildPreviewsToCache(PreviewCacheFacet previewCache)
		{
			previewCache.getCachedPreviewOrMakeOne(operations.getFigureFacet());
			previewCache.getCachedPreviewOrMakeOne(attributesOrSlots.getFigureFacet());
			previewCache.getCachedPreviewOrMakeOne(contents.getFigureFacet());
			previewCache.getCachedPreviewOrMakeOne(ports.getFigureFacet());
		}
		
		/**
		 * @see com.hopstepjump.idraw.nodefacilities.nodesupport.BasicNodeContainerFacet#setShowingForChildren(boolean)
		 */
		public void setShowingForChildren(boolean showing)
		{
			// if we are showing, don't show any suppressed children
			if (!showing || !suppressAttributesOrSlots)
				attributesOrSlots.getFigureFacet().setShowing(showing);
			if (!showing || !suppressOperations)
				operations.getFigureFacet().setShowing(showing);
			if (!showing || isContentsShowing())
				contents.getFigureFacet().setShowing(showing);
			ports.getFigureFacet().setShowing(showing);			
		}
		
		public void persistence_addContained(FigureFacet contained)
		{
			String containedName = contained.getContainedFacet().persistence_getContainedName();
			
			// set up the operations, attributes, and simple container
			if (containedName.equals("attrs"))
			{
				// make the attribute compartment
				attributesOrSlots = (FeatureCompartmentFacet) contained.getDynamicFacet(FeatureCompartmentFacet.class);
				primitiveAttributesOrSlots = contained;
				contained.getContainedFacet().persistence_setContainer(this);
			}
			
			if (containedName.equals("ops"))
			{
				// make the operation compartment
				operations = (FeatureCompartmentFacet) contained.getDynamicFacet(FeatureCompartmentFacet.class);
				primitiveOperations = contained;
				contained.getContainedFacet().persistence_setContainer(this);
			}
			
			if (containedName.equals("contents"))
			{
				// make the operation compartment
				contents = (SimpleContainerFacet) contained.getDynamicFacet(SimpleContainerFacet.class);
				primitiveContents = contained;
				contained.getContainedFacet().persistence_setContainer(this);
			}

			if (containedName.equals("ports"))
			{
				// make the ports
				primitivePorts = contained;
        ports = (PortCompartmentFacet) primitivePorts.getDynamicFacet(PortCompartmentFacet.class);
				contained.getContainedFacet().persistence_setContainer(this);
			}
			
	    registerAdorner();
		}
	}
	
	public ClassifierNodeGem(
		NamedElement subject,
		DiagramFacet diagram,
		String figureId,
		Color initialFillColor,
		PersistentProperties properties,
		boolean isPart)
	{
    this.isPart = isPart;
    this.figureName = isPart ? "part" : "classifier";
    this.initialFillColor = initialFillColor;
    this.subject = subject;
    
    if (properties == null)
      properties = new PersistentProperties();
		
		// make the attribute compartment
		attributesOrSlots =
			FeatureCompartmentGem.createAndWireUp(
			    diagram,
			    figureId + "_A",
			    containerFacet,
          isPart ? SlotCreatorGem.FEATURE_TYPE : AttributeCreatorGem.FEATURE_TYPE, 
			    "Attributes",
			    "attrs",
			    true).getFeatureCompartmentFacet();
		primitiveAttributesOrSlots = attributesOrSlots.getFigureFacet();
		
		// make the operation compartment
		operations =
			FeatureCompartmentGem.createAndWireUp(
			    diagram,
			    figureId + "_O",
			    containerFacet,
			    OperationCreatorGem.FEATURE_TYPE,
			    "Operations",
			    "ops",
			    true).getFeatureCompartmentFacet();
		primitiveOperations = operations.getFigureFacet();
		
		// make the contents compartment
		SimpleContainerGem simpleGem =
			SimpleContainerGem.createAndWireUp(
          diagram,
          figureId + "_C",
          containerFacet,
          new UDimension(0, 0),
          new UDimension(4, 4),
          "contents",
          true);
		contents = simpleGem.getSimpleContainerFacet();
		primitiveContents = contents.getFigureFacet();
		
		// make the port compartment
		PortCompartmentGem portsSimpleGem =
			PortCompartmentGem.createAndWireUp(diagram, figureId + "_P", containerFacet, "ports", !isPart);
		ports = portsSimpleGem.getPortCompartmentFacet();
		primitivePorts = ports.getFigureFacet();
    interpretOptionalProperties(properties);
	}
  
	// work out what we is suppressed by virtue of a stereotype
  private Set<String> getVisuallySuppressedUUIDs(ConstituentTypeEnum type)
	{
  	DEStratum perspective = GlobalDeltaEngine.engine.locateObject(figureFacet.getDiagram().getLinkedObject()).asStratum();
  	DEObject obj = GlobalDeltaEngine.engine.locateObject(subject);
  	DEComponent comp = isPart ? obj.asConstituent().asPart().getType() : obj.asComponent();
  	if (comp == null)
  		return new HashSet<String>();
  	return ClassifierConstituentHelper.getVisuallySuppressed(perspective, comp, type);
	}

	private void interpretOptionalProperties(PersistentProperties properties)
  {
  	name = properties.retrieve("name", "").asString();
  	owner = properties.retrieve("owner", "").asString();
    suppressAttributesOrSlots = properties.retrieve("supA", false).asBoolean();
    suppressOperations = properties.retrieve("supO", false).asBoolean();
    suppressContents = properties.retrieve("supC", false).asBoolean();
    autoSized = properties.retrieve("auto", true).asBoolean();
    displayOnlyIcon = properties.retrieve("icon", false).asBoolean();
    showAsState = properties.retrieve("state", false).asBoolean();
    
    rememberedTLOffset = properties.retrieve("tlOff", new UDimension(0, 0)).asUDimension();
    rememberedBROffset = properties.retrieve("brOff", new UDimension(0, 0)).asUDimension();
    
    showOwningPackage = properties.retrieve("showVis", false).asBoolean();
    showStereotype = properties.retrieve("showStereo", true).asBoolean();
    forceSuppressOwningPackage = properties.retrieve("suppVis", false).asBoolean();

    fillColor = properties.retrieve("fill", initialFillColor).asColor();
    addedUuids = new HashSet<String>(properties.retrieve("addedUuids", "").asStringCollection());
    deletedUuids = new HashSet<String>(properties.retrieve("deletedUuids", "").asStringCollection());
    locked = properties.retrieve("locked", false).asBoolean();
  }

  public ClassifierNodeGem(Color initialFillColor, boolean isPart, PersistentFigure figure)
	{
    this.isPart = isPart;
    this.figureName = isPart ? "part" : "classifier";
    this.initialFillColor = initialFillColor;
    
    // retrieve the subject
    this.subject = (NamedElement) figure.getSubject();

    name = subject.getName();
    
    PersistentProperties properties = figure.getProperties();
		interpretOptionalProperties(properties);
  }
	
	public BasicNodeAppearanceFacet getBasicNodeAppearanceFacet()
	{
		return appearanceFacet;
	}
	
	public BasicNodeAutoSizedFacet getBasicNodeAutoSizedFacet()
	{
		return autoSizedFacet;
	}
	
	public void connectBasicNodeFigureFacet(BasicNodeFigureFacet figureFacet)
	{
		this.figureFacet = figureFacet;
		figureFacet.registerDynamicFacet(textableFacet, TextableFacet.class);
		figureFacet.registerDynamicFacet(suppressFeaturesFacet, SuppressFeaturesFacet.class);
		figureFacet.registerDynamicFacet(displayAsIconFacet, DisplayAsIconFacet.class);
    figureFacet.registerDynamicFacet(showAsStateFacet, ShowAsStateFacet.class);
		figureFacet.registerDynamicFacet(locationFacet, LocationFacet.class);
		figureFacet.registerDynamicFacet(showOwningPackageFacet, SuppressOwningPackageFacet.class);
		figureFacet.registerDynamicFacet(hideContentsFacet, HideContentsFacet.class);
		figureFacet.registerDynamicFacet(switchableFacet, SwitchSubjectFacet.class);
		// override the default autosizing mechanism, which doesn't work for this
		figureFacet.registerDynamicFacet(autoSizedFacet, AutoSizedFacet.class);
    figureFacet.registerDynamicFacet(stylableFacet, StylableFacet.class);
    figureFacet.registerDynamicFacet(updateViewFacet, UpdateViewFacet.class);
    figureFacet.registerDynamicFacet(deletedConnectorUuidsFacet, SimpleDeletedUuidsFacet.class);
    figureFacet.registerDynamicFacet(lockFacet, VisualLockFacet.class);
    registerAdorner();
	}
	
	private void registerAdorner()
	{
    // register an adorner with the interface or classes
    if (subject instanceof Class)
    {
      ClassDelegatedAdornerGem adorner = new ClassDelegatedAdornerGem(figureFacet, primitiveAttributesOrSlots, primitiveOperations, primitivePorts, primitiveContents);
      figureFacet.registerDynamicFacet(adorner.getDelegatedDeltaAdornerFacet(), DelegatedDeltaAdornerFacet.class);
    }
    if (subject instanceof Interface)
    {
      InterfaceDelegatedAdornerGem adorner = new InterfaceDelegatedAdornerGem(figureFacet, primitiveAttributesOrSlots, primitiveOperations);
      figureFacet.registerDynamicFacet(adorner.getDelegatedDeltaAdornerFacet(), DelegatedDeltaAdornerFacet.class);
    }
	}
	
	public void connectClassifierMiniAppearanceFacet(ClassifierMiniAppearanceFacet miniAppearanceFacet)
	{
		this.miniAppearanceFacet = miniAppearanceFacet;
	}
	
	public ResizeVetterFacet getResizeVetterFacet()
	{
		return resizeVetterFacet;
	}
	
	private UBounds getOperationSuppressedBounds(boolean suppressOperations)
	{
		ClassifierSizeInfo info = makeCurrentInfo();
		info.setSuppressOperations(suppressOperations);
		return formCentredBounds(info, contents.getFigureFacet().getFullBounds()).getOuter();
	}
	
	private UBounds getAttributeSuppressedBounds(boolean suppressAttributes)
	{
		ClassifierSizeInfo info = makeCurrentInfo();
		info.setSuppressAttributes(suppressAttributes);
		return formCentredBounds(info, contents.getFigureFacet().getFullBounds()).getOuter();
	}
	
	private ClassifierSizeInfo makeCurrentInfo()
	{
		UBounds bounds = figureFacet.getFullBounds();
		return makeCurrentInfo(bounds.getTopLeftPoint(), bounds.getDimension(), autoSized);
	}
	
	private ClassifierSizeInfo makeCurrentInfo(UPoint topLeft, UDimension extent, boolean autoSized)
	{
		ClassifierSizeInfo info;
		boolean haveIcon = (showStereotype || displayOnlyIcon) && miniAppearanceFacet != null && miniAppearanceFacet.haveMiniAppearance();
    // if this is a part without a classifier, pretend we have no icon
    if (isPart && subject == null)
      haveIcon = false;    
    
		UDimension minimumIconExtent = (miniAppearanceFacet == null ? null : miniAppearanceFacet.getMinimumDisplayOnlyAsIconExtent());
		
		// work out the package name for visibility calcs
		String owningPackageString = null;
		if (showOwningPackage && owner != null)
			owningPackageString = owner;
		
		// see if we need to turn the compartment ellipsis on
		boolean attributeEllipsis = false;
    boolean operationEllipsis = false;
    boolean portsEllipsis = false;
    boolean partsEllipsis = false;
    boolean connectorsEllipsis = false;
    if (!isPart && subject != null)
    {
		  attributeEllipsis =
        !new ClassifierAttributeHelper(
            figureFacet,
            primitiveAttributesOrSlots,
            attributesOrSlots,
            true).isShowingAllConstituents();
      operationEllipsis =
        !new ClassifierOperationHelper(
            figureFacet,
            primitiveOperations,
            operations,
            true).isShowingAllConstituents();
      if (!displayOnlyIcon && !autoSized)
      {
        portsEllipsis =
          !new ClassPortHelper(
              figureFacet,
              primitivePorts,
              ports,
              true).isShowingAllConstituents();
        partsEllipsis =
          !new ConcreteClassPartHelper(
              figureFacet,
              primitiveContents,
              contents,
              true).isShowingAllConstituents();
        connectorsEllipsis =
          !new ClassConnectorHelper(
              figureFacet,
              primitivePorts,
              primitiveContents,
              false,
              deletedConnectorUuidsFacet,
              true).isShowingAllConstituents() ||
          !new ClassConnectorHelper(
              figureFacet,
              primitivePorts,
              primitiveContents,
              true,
              deletedConnectorUuidsFacet,
              true).isShowingAllConstituents();              
      }
    }
    if (isPart && subject != null)
    {
      attributeEllipsis =
        !new PartSlotHelper(
            figureFacet,
            primitiveAttributesOrSlots,
            true).isShowingAllConstituents();
      portsEllipsis =
        !new PartPortInstanceHelper(
            figureFacet,
            primitivePorts,
            true).isShowingAllConstituents();
    }
    
		info =
			new ClassifierSizeInfo(
			topLeft,
			extent,
			autoSized,
			isPart && name.length() == 0 ? " : " : name,
			font,
			packageFont,
			haveIcon,
			displayOnlyIcon,
			minimumIconExtent,
			attributesOrSlots.getMinimumExtent(),
			suppressAttributesOrSlots,
			operations.getMinimumExtent(),
			suppressOperations,
			suppressContents,
			contents.getMinimumBounds(),
			contents.isEmpty(),
			owningPackageString,
			isAbstract,
			isActive,
			ports.getFigureFacet().getContainerFacet().getContents().hasNext());
		info.setEllipsisForAttributes(attributeEllipsis && !suppressAttributesOrSlots);
    info.setEllipsisForOperations(operationEllipsis && !suppressOperations);
    info.setEllipsisForBody(portsEllipsis || partsEllipsis || connectorsEllipsis);
    
		return info;
	}
	
	private ClassifierSizeInfo makeCurrentInfoFromPreviews(PreviewCacheFacet previews)
	{
		PreviewFacet attributePreview = previews.getCachedPreview(attributesOrSlots.getFigureFacet());
		PreviewFacet operationsPreview = previews.getCachedPreview(operations.getFigureFacet());
		PreviewFacet contentsPreview = previews.getCachedPreview(contents.getFigureFacet());
		PreviewFacet portsPreview = previews.getCachedPreview(ports.getFigureFacet());
		
		if (attributePreview != null && operationsPreview != null && contentsPreview != null) // should always be ok
		{
			ClassifierSizeInfo info = makeCurrentInfo();
			info.setMinAttributeDimensions(attributePreview.getFullBounds().getDimension());
			info.setMinOperationDimensions(operationsPreview.getFullBounds().getDimension());
			
			SimpleContainerPreviewFacet contentsPreviewFacet =
				(SimpleContainerPreviewFacet) contentsPreview.getDynamicFacet(SimpleContainerPreviewFacet.class);
			UBounds minContentsBounds = contentsPreviewFacet.getMinimumBoundsFromPreviews(previews);
			info.setMinContentBounds(minContentsBounds);
			info.setContentsEmpty(contentsPreviewFacet.isEmpty());
			
			// do we have any ports?
			PortCompartmentPreviewFacet portContainerPreviewFacet =
			  (PortCompartmentPreviewFacet) portsPreview.getDynamicFacet(PortCompartmentPreviewFacet.class);			  
			info.setHasPorts(portContainerPreviewFacet.hasPorts());
			
			return info;
		}
		else
			return null;
	}
	
	private ClassifierSizes formCentredBounds(ClassifierSizeInfo info, UBounds contentBoundsToPreserve)
	{
		ClassifierSizes sizes = info.makeActualSizes();
		
		// ok to simply recentre and return, if the contents are empty
		if (sizes.isContentsEmpty() || !isContentsShowing())
		{
			UBounds centred = ResizingManipulatorGem.formCentrePreservingBoundsExactly(figureFacet.getFullBounds(), sizes.getOuter().getDimension());
			info.setTopLeft(centred.getTopLeftPoint());
			return info.makeActualSizes();
		}
		
		// if this is autosized, don't move in the top left
		if (autoSized)
			return sizes;
		
		// case 3: preserve the contents region if we can, by pushing the topleft up or down, and
		// trying to centre horizontally
		info.setMinContentBounds(contentBoundsToPreserve);
		UBounds newBounds = info.makeActualSizes().getInsideOuter();
		
		// adjust the new top left to make the width centred
		double widthDifference = (newBounds.getWidth() - contentBoundsToPreserve.getWidth()) / 2;
		double heightDifference = sizes.getContents().getHeight() - contentBoundsToPreserve.getHeight();
		
		info.setTopLeft(sizes.getOuter().getPoint().subtract(new UDimension(widthDifference, -heightDifference)));
		info.setExtent(info.getExtent().subtract(new UDimension(-widthDifference, heightDifference)));
		
		return info.makeActualSizes();
	}
	
	private UBounds getAutoSizedBounds(boolean autoSized)
	{
		ClassifierSizeInfo info = makeCurrentInfo();
		
		// if this is not autosized, set the contents to the remembered bounds, translating for the offset
		if (isContentsShowing() && !contents.isEmpty())
		{
			UBounds minContents = info.getMinContentBounds();
			UPoint tl = minContents.getTopLeftPoint().subtract(rememberedTLOffset);
			UPoint br = minContents.getBottomRightPoint().add(rememberedBROffset);
			info.setMinContentBounds(new UBounds(tl, br.subtract(tl)));
		}
		
		info.setAutoSized(autoSized);
		UBounds bounds = formCentredBounds(info, info.makeActualSizes().getContents()).getOuter();
		
		// if this is to be autosized, remember the content bounds and the top left bounds
		if (autoSized)
		{
			UBounds fullBounds = figureFacet.getFullBounds();
			info = makeCurrentInfo(fullBounds.getTopLeftPoint(), fullBounds.getDimension(), false);
			
			if (contents.isEmpty())
			{
				rememberedTLOffset = new UDimension(0, 0);
				rememberedBROffset = new UDimension(0, 0);
			}
			else
			{
				UBounds fullContents = info.makeActualSizes().getContents();
				UBounds minContents = info.getMinContentBounds();
				rememberedTLOffset = minContents.getTopLeftPoint().subtract(fullContents.getTopLeftPoint());
				rememberedBROffset = fullContents.getBottomRightPoint().subtract(minContents.getBottomRightPoint());
			}
		}
		
		return bounds;
	}
	
	private boolean isContentsShowing()
	{
		return !autoSized && !displayOnlyIcon && !suppressContents;
	}
	
	private UBounds getBoundsForContainment(ClassifierSizes sizes)
	{
		if (!displayOnlyIcon)
			return sizes.getOuter();
		
		UBounds bounds = sizes.getOuter();
		if (!suppressAttributesOrSlots && !attributesOrSlots.isEmpty())
			bounds = bounds.union(sizes.getAttributes());
		if (!suppressOperations && !operations.isEmpty())
			bounds = bounds.union(sizes.getOperations());
		if (name.length() != 0)
			bounds = bounds.union(sizes.getName());
		if (sizes.getZOwningText() != null)
			bounds = bounds.union(sizes.getOwner());
		return bounds;
	}
	
  private Element getPossibleDeltaSubject(Object subject)
  {
    Element element = (Element) subject;
    if (element.getOwner() instanceof DeltaReplacedConstituent)
      return element.getOwner();
    return element;
  }
  
  private JMenuItem getPartReplaceItem(final DiagramViewFacet diagramView, final ToolCoordinatorFacet coordinator)
  {
    // for replacing parts
    boolean fancy = false;
    final FigureFacet other = diagramView.getSelection().getSingleSelection();
    if (other == null)
      return null;
    Element otherSubject = (Element) other.getSubject();
    if (UMLTypes.extractInstanceOfPart((Element) other.getSubject()) != null && other.getSubject() != subject)
    {
      // the part must be owned by the same classifier and not already be a replacement
      if (!(otherSubject.getOwner() instanceof DeltaReplacedConstituent) &&
          otherSubject.getOwner() == ClassifierConstituentHelper.extractVisualClassifierFromConstituent(figureFacet))
      { 
        fancy = true;
      }
    }
    JMenuItem replacePartItem = new JMenuItem(fancy ? "Replace (with existing part)" : "Replace");
    final Property fancyReplace = fancy ? (Property) other.getSubject() : null;
    
    replacePartItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        final Property replaced = (Property) figureFacet.getSubject();
        final Property original = (Property) ClassifierConstituentHelper.getOriginalSubject(replaced);
        final FigureFacet clsFigure = ClassifierConstituentHelper.extractVisualClassifierFigureFromConstituent(figureFacet);
        final Class cls = (Class) clsFigure.getSubject();
        CompositeCommand comp = new CompositeCommand("replaced part", "removed replaced part");
        
        // now form the port remap based on locations of ports
        final List<PortRemap> remaps = fancyReplace == null ?
            null : new PortRemapper(figureFacet, other, figureFacet).remapPortsBasedOnProximity();
        
        // if we are doing a fancy replace, move the other part into place        
        if (fancyReplace != null)
        {
          MovingFiguresGem movingGem = new MovingFiguresGem(diagramView.getDiagram(), other.getFullBounds().getPoint());
          MovingFiguresFacet movingFacet = movingGem.getMovingFiguresFacet();
          movingFacet.indicateMovingFigures(Arrays.asList(new FigureFacet[]{other}));
          movingFacet.start(other);
          movingFacet.move(figureFacet.getFullBounds().getPoint());
          comp.addCommand(movingFacet.end("", ""));
        }
        
        final DeltaReplacedAttribute replacement[] = new DeltaReplacedAttribute[1];
        comp.addCommand(new AbstractCommand("", "")
        {          
          public void execute(boolean isTop)
          {
          	if (replacement[0]== null)
          	{
          		replacement[0] =
                 fancyReplace == null ?
                     createDeltaReplacedPart(cls, replaced, original) :
                     createFancyDeltaReplacedPart(cls, other, original);
          	}
            GlobalSubjectRepository.repository.decrementPersistentDelete(replacement[0]);
            if (remaps != null)
              for (PortRemap remap : remaps)
                GlobalSubjectRepository.repository.decrementPersistentDelete(remap);
            
            // move fancy replace over
            if (fancyReplace != null)
              replacement[0].setReplacement(fancyReplace);
          }

          public void unExecute()
          {
            GlobalSubjectRepository.repository.incrementPersistentDelete(replacement[0]);
            if (remaps != null)
              for (PortRemap remap : remaps)
                GlobalSubjectRepository.repository.incrementPersistentDelete(remap);

            // move fancy replace back
            if (fancyReplace != null)
            	cls.settable_getOwnedAttributes().add(fancyReplace);
          }            
        });
        coordinator.executeCommandAndUpdateViews(comp);
        
        diagramView.runWhenModificationsHaveBeenProcessed(new Runnable()
        {
          public void run()
          {
            FigureFacet createdFeature = ClassifierConstituentHelper.findSubfigure(clsFigure, replacement[0].getReplacement());
            diagramView.getSelection().clearAllSelection();
            diagramView.getSelection().addToSelection(createdFeature, true);
          }
        });
      }
    });

    return replacePartItem;
  }
  
  private DeltaReplacedAttribute createDeltaReplacedPart(Class owner, Property replaced, Property original)
  { 
    DeltaReplacedAttribute replacement = owner.createDeltaReplacedAttributes();
    replacement.setReplaced(original);
    
    if (replaced != null)
    {
      Property next = (Property) replacement.createReplacement(UML2Package.eINSTANCE.getProperty());
      next.setName(replaced.getName());
      next.setType(replaced.undeleted_getType());
      
      // create the instance value
      InstanceValue instanceValue = (InstanceValue) next.createDefaultValue(UML2Package.eINSTANCE.getInstanceValue());
      instanceValue.setType(original.getDefaultValue().undeleted_getType());
      InstanceSpecification instanceSpecification = instanceValue.createOwnedAnonymousInstanceValue();
      instanceValue.setInstance(instanceSpecification);
      
      // copy over the slots
      InstanceValue oldValue = (InstanceValue) replaced.undeleted_getDefaultValue();
      InstanceSpecification oldSpec = oldValue.undeleted_getInstance();
      instanceSpecification.settable_getClassifiers().addAll(oldSpec.undeleted_getClassifiers());
      for (Object obj : oldSpec.undeleted_getSlots())
      {
        Slot slot = (Slot) obj;
        Slot newSlot = instanceSpecification.createSlot();
  
        newSlot.setDefiningFeature(slot.undeleted_getDefiningFeature());
        for (Object value : slot.undeleted_getValues())
        {
        	if (value instanceof PropertyValueSpecification)
        	{
        		PropertyValueSpecification spec = (PropertyValueSpecification) newSlot.createValue(UML2Package.eINSTANCE.getPropertyValueSpecification());
        		spec.setProperty(((PropertyValueSpecification) value).getProperty());
        		spec.setAliased(((PropertyValueSpecification) value).isAliased());
        	}
        	else
        	{
	          Expression expression = (Expression) newSlot.createValue(UML2Package.eINSTANCE.getExpression());
	          expression.setBody(((Expression) value).getBody());
        	}
        }
      }
      
      // copy over any remaps also
      for (Object obj : oldSpec.undeleted_getPortRemaps())
      {
        PortRemap remap = (PortRemap) obj;
        PortRemap newRemap = instanceSpecification.createPortRemap();
        newRemap.setNewPort(remap.undeleted_getNewPort());
        newRemap.setOriginalPort(remap.undeleted_getOriginalPort());
      }
    }
    
    // delete it so we can bring it back as part of the redo command
    GlobalSubjectRepository.repository.incrementPersistentDelete(replacement);
    
    return replacement;
  }
  
	private boolean shouldDisplayOnlyIcon()
	{
		return hasSubstitutions() ? true : displayOnlyIcon;
	}
	
	public boolean hasSubstitutions()
	{
		// do we have any redefinitions pointing at us in this diagram?
		for (Iterator<LinkingFacet> iter = figureFacet.getAnchorFacet().getLinks(); iter.hasNext();)
		{
			FigureFacet link = iter.next().getFigureFacet(); 
			Object subj = link.getSubject();
			if (subj instanceof Dependency)
			{
				Dependency dep = (Dependency) subj;
				if (dep.isReplacement() && dep.undeleted_getDependencyTarget() == subject)
					return true;
			}
		}
		return false;
	}

  private DeltaReplacedAttribute createFancyDeltaReplacedPart(Class owner, FigureFacet other, Property originalSubject)
  { 
    DeltaReplacedAttribute replacement = owner.createDeltaReplacedAttributes();
    replacement.setReplaced(originalSubject);
    
    // delete it so we can bring it back as part of the redo command
    GlobalSubjectRepository.repository.incrementPersistentDelete(replacement);
    
    return replacement;
  }
  
  private boolean isElementRetired()
	{
		return (!isPart && ((Classifier) subject).isRetired());
	}

	/**
	 * @see com.hopstepjump.jumble.foundation.IFigure#getContainerablePort()
	 */
	public BasicNodeContainerFacet getBasicNodeContainerFacet()
	{
		return containerFacet;
	}
	
  private Set<String>[] refreshSuppressedAttributes()
  {
  	if (isPart)
  		return null;
  	Set<String>[] old = attributesOrSlots.getAddedAndDeleted();
  	attributesOrSlots.resetToDefaults();
  	return old;
  }
  
  private Set<String>[] refreshSuppressedPorts()
  {
  	Set<String>[] old = ports.getAddedAndDeleted();
  	ports.resetToDefaults();
  	return old;
  }
}

