package com.hopstepjump.jumble.umldiagrams.featurenode;

import java.awt.event.*;
import java.util.*;
import java.util.regex.*;

import javax.swing.*;

import org.eclipse.emf.common.util.*;
import org.eclipse.uml2.*;
import org.eclipse.uml2.Class;
import org.eclipse.uml2.Package;
import org.eclipse.uml2.impl.*;

import com.hopstepjump.idraw.figurefacilities.textmanipulationbase.*;
import com.hopstepjump.idraw.foundation.*;
import com.hopstepjump.idraw.nodefacilities.nodesupport.*;
import com.hopstepjump.jumble.umldiagrams.base.*;
import com.hopstepjump.repository.*;
import com.hopstepjump.repositorybase.*;


/**
 * @author Andrew
 */
public final class AttributeFeatureTypeFacetImpl implements FeatureTypeFacet
{
	public static final String FIGURE_NAME = "attribute";  // for the creator
  private BasicNodeFigureFacet figureFacet;
  private TextableFacet textableFacet;
  private static final String READ_ONLY = "{readonly}";
  private static final String WRITE_ONLY = "{writeonly}";
  private Pattern pattern = Pattern.compile(
      "(\\w+)" +                                                    // attribute name
            "(?:\\s*" +                                              // first bracket and whitespace
                  "(?:\\s*\\[(?:([0-9]+)\\s*\\.\\.\\s*)?([0-9]+|\\*)\\s*\\])?\\s*" +  // multiplicity     (optional)
                  ":\\s*(\\w+)\\s*(?:\\=\\s*([^\\{]*\\S))?" +             // type and default (optional)
            ")?\\s*" +                                               // last bracket and whitespace
            "(\\{readonly\\}|\\{writeonly\\})?\\s*");							   // readonly or writeonly

  public AttributeFeatureTypeFacetImpl(BasicNodeFigureFacet figureFacet, TextableFacet textableFacet)
  {
    this.figureFacet = figureFacet;
    this.textableFacet = textableFacet;
  }
  
	/**
	 * @see com.giroway.jumble.umldiagrams.classdiagram.featurenode.FeatureFigure#getFeatureType()
	 */
	public int getFeatureType()
	{
		return 0;
	}

  public String getFigureName()
  {
    return FIGURE_NAME;
  }
  
    /**
   * the short name is the elided form of the entire operation name
   */
  public String makeShortName(String name)
  {
  	return name;
  }

  public Object setText(String text, Object listSelection, Object memento)
  {
    final SubjectRepositoryFacet repository = GlobalSubjectRepository.repository; 
    Command cmd = (Command) memento;
    if (cmd != null)
    {
    	cmd.execute(false);
    	return cmd;
    }

    // make a command to effect the changes
    CompositeCommand command = new CompositeCommand("", "");
    final Property typed = getSubject();
    
    // save the name and type
    final String oldName = typed.getName();
    final ValueSpecification oldLower = typed.getLowerValue();
    final ValueSpecification oldUpper = typed.getUpperValue();
    final PropertyAccessKind oldReadWriteOnly = typed.getReadWrite();
    final Type oldType = getSubjectType(typed);    
    
    // get the new name and type
    Matcher matcher = pattern.matcher(text);
    final String newName;
    final String newLowerMult;
    final String newUpperMult;
    final String newTypeName;
    final String newDefaultText;
    final String readWriteOnly;
    if (matcher.matches())
    {
      newName = matcher.group(1);
      newLowerMult = matcher.group(2);
      newUpperMult = matcher.group(3);
      newTypeName = matcher.group(4);
      newDefaultText = matcher.group(5);
      readWriteOnly = matcher.group(6);
    }
    else
    {
      newName = text;
      newLowerMult = null;
      newUpperMult = null;
      newTypeName = null;
      newDefaultText = null;
      readWriteOnly = null;
    }
    
    // set the name
    command.addCommand(new AbstractCommand()
    { public void execute(boolean isTop) { typed.setName(newName); }
			public void unExecute()            { typed.setName(oldName); }});
    
    // set the start and end multiplicity
    if (newLowerMult != null)
    {
    	typed.setLowerBound(new Integer(newLowerMult));
    	final ValueSpecification lowerSpec = typed.getLowerValue();
    	repository.incrementPersistentDelete(lowerSpec);
      command.addCommand(new AbstractCommand()
      {
      	public void execute(boolean isTop)
        {
      		repository.decrementPersistentDelete(lowerSpec);
      		typed.setLowerValue(lowerSpec);
        }
  			public void unExecute()
  			{
      		repository.incrementPersistentDelete(lowerSpec);
  				typed.setLowerValue(oldLower);
  			}
  		});
    }
    
    PropertyAccessKind newReadWriteOnly = PropertyAccessKind.READ_WRITE_LITERAL;
    if (readWriteOnly == null)
      newReadWriteOnly = PropertyAccessKind.READ_WRITE_LITERAL;
    else
    if (readWriteOnly.equals(READ_ONLY))
    	newReadWriteOnly = PropertyAccessKind.READ_ONLY_LITERAL;
    else
    if (readWriteOnly.equals(WRITE_ONLY))
    	newReadWriteOnly = PropertyAccessKind.WRITE_ONLY_LITERAL;
    final PropertyAccessKind finalReadWrite = newReadWriteOnly;
    
    // only bother if this changes
    if (!newReadWriteOnly.equals(oldReadWriteOnly))
    {
    	command.addCommand(new AbstractCommand()
    	{
      	public void execute(boolean isTop)
        {
      		typed.setReadWrite(finalReadWrite);
        }
  			public void unExecute()
  			{
      		typed.setReadWrite(oldReadWriteOnly);
  			}	    		
    	});
    }

    if (newUpperMult != null)
    {
    	final int newUpper = newUpperMult.equals("*") ? -1 : new Integer(newUpperMult); 
    	typed.setUpperBound(new Integer(newUpper));
    	final ValueSpecification upperSpec = typed.getUpperValue();
    	repository.incrementPersistentDelete(upperSpec);
      command.addCommand(new AbstractCommand()
      {
      	public void execute(boolean isTop)
        {
      		repository.decrementPersistentDelete(upperSpec);
      		typed.setUpperValue(upperSpec);
        }
  			public void unExecute()
  			{
      		repository.incrementPersistentDelete(upperSpec);
  				typed.setUpperValue(oldUpper);
  			}
  		});
    }

    // decrement the existing lower and upper value
    if (oldLower != null)
	    command.addCommand(new AbstractCommand()
	    { public void execute(boolean isTop) { repository.incrementPersistentDelete(oldLower); }
				public void unExecute()            { repository.decrementPersistentDelete(oldLower); }});
    if (oldUpper!= null)
	    command.addCommand(new AbstractCommand()
	    { public void execute(boolean isTop) { repository.incrementPersistentDelete(oldUpper); }
				public void unExecute()            { repository.decrementPersistentDelete(oldUpper); }});

    // find or create the type
    if (newTypeName != null)
    {
    	Type type;
      if (listSelection != null)
        type = (Classifier) ((ElementSelection) listSelection).getElement();
      else
      {
      	// possibly keep old type
      	if (oldType != null && newTypeName.equals(oldType.getName()))
      		type = oldType;
      	else
      	{
	      	Vector<ElementSelection> possible = locatePrimitiveElements(newTypeName);
		      type = possible.isEmpty() ? null : (Type) possible.get(0).getElement();
      	}
      }
      final Type newType = type;
      
      if (newType == null)
      {
        // if we can't find the type, make a new one
        final Type createdType = ((Package) repository.findOwningElement(typed, Package.class)).createOwnedClass(newTypeName, false);
        repository.incrementPersistentDelete(createdType);
  	    command.addCommand(new AbstractCommand()
  	    {
  	    	public void execute(boolean isTop) {
  	        repository.decrementPersistentDelete(createdType);
  	    		typed.setType(createdType);
  	    	}
  				public void unExecute() {
  	        repository.incrementPersistentDelete(createdType);
  					typed.setType(oldType);
  				}});
      }
      else
  	    command.addCommand(new AbstractCommand()
  	    { public void execute(boolean isTop) { typed.setType(newType); }
  				public void unExecute() { typed.setType(oldType); }});
    }
    else
    {
	    command.addCommand(new AbstractCommand()
	    { public void execute(boolean isTop) { typed.setType(null); }
				public void unExecute() { typed.setType(oldType); }});
    }
    
    // set the default value      
    if (newDefaultText == null)
    {
      List<ValueSpecification> defaultValue = typed.getDefaultValues();
      if (!defaultValue.isEmpty())
  	    command.addCommand(new AbstractCommand()
  	    {
  	    	private List<ValueSpecification> oldValues = new ArrayList<ValueSpecification>(typed.undeleted_getDefaultValues());
  	    	
  	    	public void execute(boolean isTop) { typed.settable_getDefaultValues().clear(); }
  				public void unExecute() { typed.settable_getDefaultValues().clear(); typed.settable_getDefaultValues().addAll(0, oldValues); }});
    }
    else
    {
    	Classifier owner = findOwningVisualElement();    	
    	Package perspective = repository.findVisuallyOwningStratum(figureFacet.getDiagram(), figureFacet.getContainerFacet());
    	final List<ValueSpecification> values = ValueParser.decodeParameters(newDefaultText, perspective, owner);

    	// handle the default value as an opaque expression
      final Expression defaultValue = (Expression) typed.createDefaultValues(UML2Package.eINSTANCE.getExpression());
      repository.incrementPersistentDelete(defaultValue);
	    command.addCommand(new AbstractCommand()
	    {
	    	private List<ValueSpecification> oldValues = new ArrayList<ValueSpecification>(typed.undeleted_getDefaultValues());

	    	public void execute(boolean isTop)
	      {
	    		typed.settable_getDefaultValues().clear(); typed.settable_getDefaultValues().addAll(0, values);
	    	}
				public void unExecute()
				{
					typed.settable_getDefaultValues().clear(); typed.settable_getDefaultValues().addAll(0, oldValues);
				}
	    });
    }
    
    // resize
    String finalText = makeNameFromSubject();
    command.addCommand(figureFacet.makeAndExecuteResizingCommand(textableFacet.vetTextResizedExtent(finalText)));
    command.execute(false);
    return command;
  }
  
  private ValueSpecification getFirst(EList defaultValues)
	{
  	if (defaultValues.isEmpty())
  		return null;
  	return (ValueSpecification) defaultValues.get(0);
	}

	public void unSetText(Object memento)
  {
  	((Command) memento).unExecute();
  }
  
  private Property getSubject()
  {
    return (Property) figureFacet.getSubject();
  }

  public String makeNameFromSubject()
  {
  	DiagramFacet diagram = figureFacet.getDiagram();
  	ContainerFacet container = figureFacet.getContainedFacet().getContainer();
  	Classifier cls = (Classifier) GlobalSubjectRepository.repository.findVisuallyOwningNamespace(diagram, container);
  	Package pkg = GlobalSubjectRepository.repository.findVisuallyOwningPackage(diagram, container);
  	return UMLNodeText.makeNameFromAttribute(pkg, cls, (Property) figureFacet.getSubject());
  }
  	
  private Type getSubjectType(Property subject)
  {
    return subject.undeleted_getType();
  }
  
  private Classifier findOwningVisualElement()
  {
    SubjectRepositoryFacet repository = GlobalSubjectRepository.repository;
    return (Classifier)
      repository.findOwningElement(
          figureFacet.getFigureReference(), UML2Package.eINSTANCE.getClassifier());
  }

  public Command getPostContainerDropCommand()
  {
    final Element subject = FeatureNodeGem.getPossibleDeltaSubject(figureFacet.getSubject());
    final Classifier oldOwner = (Classifier) subject.getOwner();

    return new AbstractCommand("relocated after move", "unrelocated after move")
     {
       private Classifier newOwner;
       
       public void execute(boolean isTop)
       {
         // work out the new owner from the visual nesting
         if (newOwner == null)
           newOwner = findOwningVisualElement();
         
         if (newOwner != oldOwner)
         {
           if (subject instanceof DeltaReplacedConstituent)
           {
             if (oldOwner instanceof Interface)
               ((Interface) oldOwner).getDeltaReplacedAttributes().remove(subject);
             else if (oldOwner instanceof Class)
               ((Class) oldOwner).getDeltaReplacedAttributes().remove(subject);
             
             if (newOwner instanceof Interface)
               ((Interface) newOwner).getDeltaReplacedAttributes().add(subject);
             else if (newOwner instanceof Class)
               ((Class) newOwner).getDeltaReplacedAttributes().add(subject);             
           }
           else
           {
             if (oldOwner instanceof Interface)
               ((Interface) oldOwner).getOwnedAttributes().remove(subject);
             else if (oldOwner instanceof Class)
               ((Class) oldOwner).getOwnedAttributes().remove(subject);
             
             if (newOwner instanceof Interface)
               ((Interface) newOwner).getOwnedAttributes().add(subject);
             else if (newOwner instanceof Class)
               ((Class) newOwner).getOwnedAttributes().add(subject);
           }
         }
       }
  
       public void unExecute()
       {
         if (newOwner != oldOwner)
         {
           if (subject instanceof DeltaReplacedConstituent)
           {
             if (newOwner instanceof Interface)
               ((Interface) newOwner).getDeltaReplacedAttributes().remove(subject);
             else if (newOwner instanceof Class)
               ((Class) newOwner).getDeltaReplacedAttributes().remove(subject);
              
             if (oldOwner instanceof Interface)
               ((Interface) oldOwner).getDeltaReplacedAttributes().add(subject);
             else if (oldOwner instanceof Class)
               ((Class) oldOwner).getDeltaReplacedAttributes().add(subject);           
           }
           else
           {
             if (newOwner instanceof Interface)
               ((Interface) newOwner).getOwnedAttributes().remove(subject);
             else if (newOwner instanceof Class)
               ((Class) newOwner).getOwnedAttributes().remove(subject);
              
             if (oldOwner instanceof Interface)
               ((Interface) oldOwner).getOwnedAttributes().add(subject);
             else if (oldOwner instanceof Class)
               ((Class) oldOwner).getOwnedAttributes().add(subject);
           }
         }
       }        
     };
  }

  public Command generateDeleteDelta(ToolCoordinatorFacet coordinator, final Classifier owner)
  {
    // add this to the classifier as a delete delta
    final Element feature = FeatureNodeGem.getOriginalSubject(figureFacet.getSubject());
    
    return new AbstractCommand("Added delete delta", "Removed delete delta")
    {
      private DeltaDeletedConstituent delete;
      
      public void execute(boolean isTop)
      {
        SubjectRepositoryFacet repository = GlobalSubjectRepository.repository;
        
        // possibly resurrect
        if (delete != null)
        {
          repository.decrementPersistentDelete(delete);
        }
        else
        {
          if (owner instanceof ClassImpl)
            delete = ((Class) owner).createDeltaDeletedAttributes();
          else
          if (owner instanceof InterfaceImpl)
            delete = ((Interface) owner).createDeltaDeletedAttributes();
          delete.setDeleted(feature);
        }
      }

      public void unExecute()
      {
        GlobalSubjectRepository.repository.incrementPersistentDelete(delete);
      } 
    };
  }

  public JMenuItem getReplaceItem(final DiagramViewFacet diagramView, final ToolCoordinatorFacet coordinator)
  {
    // for adding operations
    JMenuItem replaceAttributeItem = new JMenuItem("Replace");
    replaceAttributeItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        final Property replaced = (Property) figureFacet.getSubject();
        final Property original = (Property) ClassifierConstituentHelper.getOriginalSubject(replaced);
        final FigureFacet clsFigure = ClassifierConstituentHelper.extractVisualClassifierFigureFromConstituent(figureFacet);
        final Classifier cls = (Classifier) clsFigure.getSubject();
        final DeltaReplacedAttribute replacement[] = new DeltaReplacedAttribute[1];
        
        Command cmd = new AbstractCommand("replaced attribute", "removed replaced attribute")
        {          
          public void execute(boolean isTop)
          {
          	if (replacement[0] == null)
          		replacement[0] = createDeltaReplacedAttribute(cls, replaced, original);
            GlobalSubjectRepository.repository.decrementPersistentDelete(replacement[0]);
          }

          public void unExecute()
          {
            GlobalSubjectRepository.repository.incrementPersistentDelete(replacement[0]);
          }            
        };
        coordinator.executeCommandAndUpdateViews(cmd);
        
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

    return replaceAttributeItem;
  }
  
  public boolean isSubjectReadOnlyInDiagramContext(boolean kill)
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
          FeatureNodeGem.getPossibleDeltaSubject(figureFacet.getSubject()).getOwner())
        return true;
    }
    
    // only writeable if the class is located correctly
    return GlobalSubjectRepository.repository.isContainerContextReadOnly(figureFacet);
  }
  
  public DeltaReplacedAttribute createDeltaReplacedAttribute(Classifier owner, Property replaced, Property original)
  {     
    DeltaReplacedAttribute replacement = null;
    Property attr = null;
    if (owner instanceof Interface)
    {
      replacement = ((Interface) owner).createDeltaReplacedAttributes();
      replacement.setReplaced(original);
      attr = (Property) replacement.createReplacement(UML2Package.eINSTANCE.getProperty());
    }
    else
    if (owner instanceof StructuredClassifier)
    {
      replacement = ((Class) owner).createDeltaReplacedAttributes();
      replacement.setReplaced(original);
      attr = (Property) replacement.createReplacement(UML2Package.eINSTANCE.getProperty());
    }
    attr.setName(replaced.getName());
    attr.setVisibility(replaced.getVisibility());
    attr.setType(replaced.getType());
    attr.setReadWrite(replaced.getReadWrite());

    // set upper and lower bounds
    if (replaced.getUpperValue() != null)
      attr.setUpperBound(new Integer(replaced.getUpper()));
    if (replaced.getLowerValue() != null)
      attr.setLowerBound(new Integer(replaced.getLower()));

    // set a possible default value
  	for (Object s : replaced.getDefaultValues())
  	{
    	ValueSpecification spec = (ValueSpecification) s;
    	if (spec instanceof PropertyValueSpecification)
    	{
				PropertyValueSpecification v = UML2Factory.eINSTANCE.createPropertyValueSpecification();
				v.setProperty(((PropertyValueSpecification) spec).getProperty());
				attr.settable_getDefaultValues().add(v);
    	}
    	else
    	{
				Expression v = UML2Factory.eINSTANCE.createExpression();
				v.setBody(((Expression) spec).getBody());
				attr.settable_getDefaultValues().add(v);
    	}
    }
    
    // make it deleted so we can resurrect it as the 1st part of the cmd
    GlobalSubjectRepository.repository.incrementPersistentDelete(replacement);
    
    return replacement;
  }

	public JList formSelectionList(String textSoFar)
	{
    // get the class name after the colon
    int pos = textSoFar.indexOf(':');
    if (pos == -1)
      return null;
      
    String name = textSoFar.substring(pos + 1);
    StringTokenizer tok = new StringTokenizer(name);
    String nameSoFar = tok.hasMoreTokens() ? tok.nextToken() : "";
    
    if (nameSoFar.length() == 0)
      return null;
    
    return new JList(locatePrimitiveElements(nameSoFar));
  }
	
	private Vector<ElementSelection> locatePrimitiveElements(String nameSoFar)
	{
    Collection<NamedElement> elements = GlobalSubjectRepository.repository.findElementsStartingWithName(nameSoFar, ClassImpl.class, true);

    Vector<ElementSelection> listElements = new Vector<ElementSelection>();
    for (NamedElement element : elements)
    	if (StereotypeUtilities.isStereotypeApplied(element, CommonRepositoryFunctions.PRIMITIVE_TYPE) || element.getUuid().equals("String") || element.getUuid().equals("boolean"))
    		listElements.add(new ElementSelection(element));

    Collection<NamedElement> ielements = GlobalSubjectRepository.repository.findElementsStartingWithName(nameSoFar, InterfaceImpl.class, true);
    for (NamedElement element : ielements)
  		listElements.add(new ElementSelection(element));
    Collections.sort(listElements);
    return listElements;
	}
}