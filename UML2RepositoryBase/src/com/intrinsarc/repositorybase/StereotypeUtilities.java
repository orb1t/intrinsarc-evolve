package com.intrinsarc.repositorybase;

import java.util.*;

import org.eclipse.uml2.*;
import org.eclipse.uml2.Package;

import com.intrinsarc.deltaengine.base.*;
import com.intrinsarc.idraw.foundation.*;

public class StereotypeUtilities
{
	/**
	 * extract the value of a boolean property.
	 * 
	 * @param classifier
	 * @param attributeUUID
	 * @return
	 */
	public static boolean extractBooleanProperty(Element element, String attributeUUID)
	{
    IDeltaEngine engine = GlobalDeltaEngine.engine;
    if (element instanceof Classifier && engine.locateObject(element) != null)
    {
      DEElement elem = engine.locateObject(element).asElement();
      for (DEAppliedStereotype applied : elem.getAppliedStereotypes(elem.getHomeStratum()))
      {
        if (applied.getProperties() != null)
          for (DEAttribute attr : applied.getProperties().keySet())
            if (attr.getUuid().equals(attributeUUID))
              return new Boolean(applied.getProperties().get(attr));
      }
    }
    else
    {
      for (Object obj : element.undeleted_getAppliedBasicStereotypeValues())
      {
        AppliedBasicStereotypeValue value = (AppliedBasicStereotypeValue) obj;
        if (value.getProperty() != null)
        {
	        if (!value.isThisDeleted() && !value.getProperty().isThisDeleted())
	          if (value.getProperty().getUuid().equals(attributeUUID)
	              && value.getValue() instanceof LiteralBoolean)
	          {
	            return ((LiteralBoolean) value.getValue()).isValue();
	          }
        }
      }
    }
    return false;
	}

	public static String extractStringProperty(Element element, String attributeUUID)
	{
    IDeltaEngine engine = GlobalDeltaEngine.engine;
    if (element instanceof Classifier && engine.locateObject(element) != null)
    {
      DEElement elem = engine.locateObject(element).asElement();
      for (DEAppliedStereotype applied : elem.getAppliedStereotypes(elem.getHomeStratum()))
      {
        for (DEAttribute attr : applied.getProperties().keySet())
          if (attr.getUuid().equals(attributeUUID))
            return applied.getProperties().get(attr);
      }
    }
    else
    {
      for (Object obj : element.undeleted_getAppliedBasicStereotypeValues())
      {
        AppliedBasicStereotypeValue value = (AppliedBasicStereotypeValue) obj;
        if (value != null && !value.isThisDeleted() && value.getProperty() != null && !value.getProperty().isThisDeleted())
          if (value.getProperty().getUuid().equals(attributeUUID)
              && value.getValue() instanceof Expression)
          {
            return ((Expression) value.getValue()).getBody();
          }
      }
    }
    return null;
	}
	
	/**
	 * find a stereotype hash which can be used to check if any stereotype related
	 * info has changed for an elemenet
	 */
	public static int calculateStereotypeHash(FigureFacet figure, Element element)
	{
		if (element == null)
			return 0;
		
		int hash = 0;
		if (element instanceof Classifier && GlobalDeltaEngine.engine.locateObject(element) != null)
		{
			// this is complex because changes can come from many, many places: the stereotype hierarchy or the element hierarchy
			DEElement e = GlobalDeltaEngine.engine.locateObject(element).asElement();
			DEStratum perspective;
			if (figure != null)
			{
				Package pkg = GlobalSubjectRepository.repository.findVisuallyOwningPackage(figure.getDiagram(), figure.getContainerFacet());
				perspective = GlobalDeltaEngine.engine.locateObject(pkg).asStratum();
			}
			else
				perspective = e.getHomeStratum();
			
			List<DEElement> stereos = new ArrayList<DEElement>();
			IDeltas deltas = e.getDeltas(ConstituentTypeEnum.DELTA_APPLIED_STEREOTYPE);
			Set<DeltaPair> pairs = deltas.getConstituents(perspective);

			for (DeltaPair pair : pairs)
			{
				DEAppliedStereotype applied = pair.getConstituent().asAppliedStereotype();
				DEComponent stereo = applied.getStereotype();
				
				stereos.add(stereo);
				stereos.addAll(stereo.getSuperElementClosure(perspective, false));
				for (DEElement c : stereos)
				{
					hash ^= c.getName(perspective).hashCode();
					for (DeltaPair p : c.getDeltas(ConstituentTypeEnum.DELTA_ATTRIBUTE).getConstituents(perspective))
					{
					  DEAttribute attr = p.getConstituent().asAttribute();
					  if (attr.getName() != null && attr.getType() != null)
					  	hash ^= attr.getName().hashCode() ^ attr.getType().getUuid().hashCode();
					}
				}
				
				// handle all the possible values
				Map<DEAttribute, String> props = applied.getProperties();
				if (props != null)
					for (DEAttribute prop : applied.getProperties().keySet())
					{
						hash ^= prop.getName().hashCode()
						^ ("" + props.get(prop)).hashCode();					
					}
			}					
		}
		else
		{
			for (Object obj : element.undeleted_getAppliedBasicStereotypes())
				hash ^= ((Stereotype) obj).getName().hashCode();
		}

    // now look at all possible values
    for (Object obj : element.undeleted_getAppliedBasicStereotypeValues())
    {
      AppliedBasicStereotypeValue value = (AppliedBasicStereotypeValue) obj;
    	hash ^= (value.getProperty() != null ? value.getProperty().getName().hashCode() : 0)
        	^ ("" + value.getValue()).hashCode();
    }
		
    // add in the readonly bit
    if (GlobalSubjectRepository.repository.isReadOnly(element))
    	hash ^= 1;
    
		return hash;
	}
	
	public static int calculateRawStereotypeHash(Element element)
	{
		if (element == null)
			return 0;
		
		int hash = 0;
		for (Object obj : element.undeleted_getAppliedBasicStereotypes())
			hash ^= ((Stereotype) obj).getName().hashCode();

    // now look at all possible values
    for (Object obj : element.undeleted_getAppliedBasicStereotypeValues())
    {
      AppliedBasicStereotypeValue value = (AppliedBasicStereotypeValue) obj;
      if (value.getProperty() != null)
      	hash ^= value.getProperty().getName().hashCode() ^ ("" + value.getValue()).hashCode();
    }
		
    // add in the readonly bit
    if (GlobalSubjectRepository.repository.isReadOnly(element))
    	hash ^= 1;
    
		return hash;
	}
	
	public static boolean isRawStereotypeApplied(Element classifier, String uuid)
	{
    for (Object obj : classifier.undeleted_getAppliedBasicStereotypes())
    {
      Stereotype stereo = (Stereotype) obj;
      if (stereo.getUuid().equals(uuid))
        return true;
    }
    return false;
	}

	public static boolean isStereotypeApplied(Element element, String uuid)
	{
		DEObject obj = GlobalDeltaEngine.engine.locateObject(element);
		if (obj.asElement() != null)
		{
			DEElement elem = obj.asElement();
			DEAppliedStereotype applied = elem.getAppliedStereotype(elem.getHomeStratum());
			if (applied == null)
				return false;
			return applied.getStereotype().getUuid().equals(uuid);
		}
		else
		if (obj.asConstituent() != null)
		{
			for (DEAppliedStereotype appl : obj.asConstituent().getAppliedStereotypes())
				if (appl.getStereotype().getUuid().equals(uuid))
					return true;
			return false;
		}
		return isRawStereotypeApplied(element, uuid);
	}

	public static void formAddRawStereotypeTransaction(ToolCoordinatorFacet coordinator, final Element classifier, final Stereotype stereotype)
	{
		String typeName = classifier.eClass().getName();
		coordinator.startTransaction(
				"added stereotype to " + typeName,
				"un-added stereotype from " + typeName);
		classifier.getAppliedBasicStereotypes().add(stereotype);
		coordinator.commitTransaction();
	}

	public static void formRemoveRawStereotypeTransaction(ToolCoordinatorFacet coordinator, final Element classifier, final Stereotype stereotype)
	{
		String typeName = classifier.eClass().getName();
		coordinator.startTransaction(
				"removed stereotype to " + typeName,
				"re-added stereotype from " + typeName);
		classifier.getAppliedBasicStereotypes().remove(stereotype);
		coordinator.commitTransaction();
	}

	public static List<DEComponent> findAllStereotypes(Element element)
	{
	  List<DEComponent> stereos = new ArrayList<DEComponent>();
	  if (element instanceof Classifier && GlobalDeltaEngine.engine.locateObject(element) != null)
	  {
	    DEElement elem = GlobalDeltaEngine.engine.locateObject(element).asElement();
	    for (DEAppliedStereotype applied : elem.getAppliedStereotypes(elem.getHomeStratum()))
	        stereos.add(applied.getStereotype());
	  }
	  else
	  {
	    for (Object applied : element.undeleted_getAppliedBasicStereotypes())
	      stereos.add(GlobalDeltaEngine.engine.locateObject(applied).asComponent());
	  }
	  
	  return stereos;
	}
	
  public static Map<String, DeltaPair> findAllStereotypeProperties(Element element)
  {
    if (element instanceof Classifier && GlobalDeltaEngine.engine.locateObject(element) != null)
    {
      DEElement elem = GlobalDeltaEngine.engine.locateObject(element).asElement();
      return findAllStereotypeProperties(elem);
    }
    else
      return findAllStereotypePropertiesFromRawAppliedStereotypes(element);
  }
	
	public static Map<String, DeltaPair> findAllStereotypePropertiesFromRawAppliedStereotypes(Element element)
	{
    // find the perspective
    Package pkg = (element instanceof Package) ?
        (Package) element : GlobalSubjectRepository.repository.findOwningPackage(element);
    DEStratum perspective = GlobalDeltaEngine.engine.locateObject(pkg).asStratum();

		Map<String, DeltaPair> properties = new HashMap<String, DeltaPair>();
		for (Object obj : element.undeleted_getAppliedBasicStereotypes())
		{
			Stereotype stereo = (Stereotype) obj;
			DEComponent comp = GlobalDeltaEngine.engine.locateObject(stereo).asComponent();
			addPropertiesForAllHierarchy(properties, perspective, comp);
		}
		return properties;
	}

  public static Map<String, DeltaPair> findAllStereotypeProperties(Element element, DEComponent stereo)
  {
    IDeltaEngine engine = GlobalDeltaEngine.engine;
    Package pkg = GlobalSubjectRepository.repository.findOwningPackage(element);
    Map<String, DeltaPair> properties = new HashMap<String, DeltaPair>();
    if (pkg == null)
    	return properties;
    DEStratum perspective = engine.locateObject(pkg).asStratum();

    if (element instanceof Classifier && GlobalDeltaEngine.engine.locateObject(element) != null)
    {
      for (DeltaPair pair : stereo.getDeltas(ConstituentTypeEnum.DELTA_ATTRIBUTE).getConstituents(perspective))
        properties.put(pair.getUuid(), pair);
    }
    else
    {
      // get the applied elements
      for (Object obj : element.undeleted_getAppliedBasicStereotypes())
      {
        Stereotype st = (Stereotype) obj;
        if (st.equals(stereo.getRepositoryObject()))
          addPropertiesForAllHierarchy(properties, perspective, stereo);        
      }
    }

    return properties;
  }

  public static Map<String, DeltaPair> findAllStereotypeProperties(DEElement element)
  {
    DEStratum perspective = element.getHomeStratum();
    Map<String, DeltaPair> properties = new HashMap<String, DeltaPair>();

    for (DeltaPair pair : element.getDeltas(ConstituentTypeEnum.DELTA_APPLIED_STEREOTYPE).getConstituents(perspective))
    {
      DEAppliedStereotype appl = pair.getConstituent().asAppliedStereotype();
      addPropertiesForAllHierarchy(properties, perspective, appl.getStereotype());
    }

    return properties;
  }

	private static void addPropertiesForAllHierarchy(Map<String, DeltaPair> properties, DEStratum perspective, DEComponent stereo)
	{
	  for (DeltaPair pair : stereo.getDeltas(ConstituentTypeEnum.DELTA_ATTRIBUTE).getConstituents(perspective))
			properties.put(pair.getUuid(), pair);
	}

	/**
	 * form a command to set a stereotype attribute
	 * 
	 * @param classifier
	 * @param propertyName
	 * @param set
	 * @return
	 */
	public static void formSetBooleanRawStereotypeAttributeTransaction(
			ToolCoordinatorFacet coordinator, Element classifier, String propertyUUID, boolean set)
	{
		// find the property, or create it
		AppliedBasicStereotypeValue existing = null;
		for (Object obj : classifier.getAppliedBasicStereotypeValues())
		{
			AppliedBasicStereotypeValue value = (AppliedBasicStereotypeValue) obj;
			if (value.getProperty().getName().equals(propertyUUID))
			{
				existing = value;
				break;
			}
		}
		final AppliedBasicStereotypeValue currentValue = existing;

		// if this is null we need to create the value
		if (currentValue == null)
		{
			final Property property =
			  (Property) findAllStereotypePropertiesFromRawAppliedStereotypes(classifier).get(propertyUUID).getConstituent().getRepositoryObject();
			if (property == null)
			  return;
			coordinator.startTransaction(
					"setting stereotype attribute " + propertyUUID,
					"reverting stereotype attribute " + propertyUUID);
			AppliedBasicStereotypeValue value = classifier.createAppliedBasicStereotypeValues();
			value.setProperty(property);
			LiteralBoolean literal = (LiteralBoolean) value
					.createValue(UML2Package.eINSTANCE.getLiteralBoolean());
			literal.setValue(set);
		}
		else
		{
			coordinator.startTransaction(
					"setting stereotype attribute " + propertyUUID,
					"reverting stereotype attribute " + propertyUUID);
			LiteralBoolean literal = (LiteralBoolean) currentValue.getValue();
			literal.setValue(set);
		}
		coordinator.commitTransaction();
	}

	/**
	 * form a command to set a stereotype attribute
	 * 
	 * @param classifier
	 * @param propertyName
	 * @param set
	 * @return
	 */
	public static void setBooleanRawStereotypeAttribute(
			Element classifier, String propertyUUID, boolean set)
	{
		// find the property, or create it
		AppliedBasicStereotypeValue existing = null;
		for (Object obj : classifier.getAppliedBasicStereotypeValues())
		{
			AppliedBasicStereotypeValue value = (AppliedBasicStereotypeValue) obj;
			if (value.getProperty().getName().equals(propertyUUID))
			{
				existing = value;
				break;
			}
		}
		final AppliedBasicStereotypeValue currentValue = existing;

		// if this is null we need to create the value
		if (currentValue == null)
		{
			final Property property =
			  (Property) findAllStereotypePropertiesFromRawAppliedStereotypes(classifier).get(propertyUUID).getConstituent().getRepositoryObject();
			if (property == null)
			  return;
			AppliedBasicStereotypeValue value = classifier.createAppliedBasicStereotypeValues();
			value.setProperty(property);
			LiteralBoolean literal = (LiteralBoolean) value
					.createValue(UML2Package.eINSTANCE.getLiteralBoolean());
			literal.setValue(set);
		}
		else
		{
			LiteralBoolean literal = (LiteralBoolean) currentValue.getValue();
			literal.setValue(set);
		}
	}

	/**
	 * form a command to set a stereotype attribute
	 * 
	 * @param classifier
	 * @param propertyName
	 * @param set
	 * @return
	 */
	public static void setStringRawStereotypeAttribute(
			Element classifier, String propertyUUID, String set)
	{
		// find the property, or create it
		AppliedBasicStereotypeValue existing = null;
		for (Object obj : classifier.getAppliedBasicStereotypeValues())
		{
			AppliedBasicStereotypeValue value = (AppliedBasicStereotypeValue) obj;
			if (value.getProperty().getName().equals(propertyUUID))
			{
				existing = value;
				break;
			}
		}
		final AppliedBasicStereotypeValue currentValue = existing;

		// if this is null we need to create the value
		if (currentValue == null)
		{
			final Property property =
			  (Property) findAllStereotypePropertiesFromRawAppliedStereotypes(classifier).get(propertyUUID).getConstituent().getRepositoryObject();
			if (property == null)
			  return;
			AppliedBasicStereotypeValue value = classifier.createAppliedBasicStereotypeValues();
			value.setProperty(property);
			Expression literal = (Expression) value
					.createValue(UML2Package.eINSTANCE.getExpression());
			literal.setBody(set);
		}
		else
		{
			Expression literal = (Expression) currentValue.getValue();
			literal.setBody(set);
		}
	}

	/**
	 * form a command to delete the appropriate stereotype property
	 * 
	 * @param classifier
	 * @param propertyName
	 * @param set
	 * @return
	 */
	public static void formDeleteAppliedRawStereotypeValueTransaction(
			ToolCoordinatorFacet coordinator, final Element classifier, final AppliedBasicStereotypeValue value)
	{
		coordinator.startTransaction(
		    "Removing applied stereotype value",
				"Restoring applied stereotype value");
		GlobalSubjectRepository.repository.incrementPersistentDelete(value);
		coordinator.commitTransaction();
	}

	public static int calculateStereotypeHashAndComponentHash(FigureFacet figureFacet, NamedElement subject)
	{
		int hash = 0;
		if (subject instanceof Classifier && GlobalDeltaEngine.engine.locateObject(subject).asComponent() != null)
		{
			DEComponent comp = GlobalDeltaEngine.engine.locateObject(subject).asComponent();
			Package pkg = GlobalSubjectRepository.repository.findVisuallyOwningPackage(figureFacet.getDiagram(), figureFacet.getContainerFacet());
			DEStratum perspective = GlobalDeltaEngine.engine.locateObject(pkg).asStratum();
			if (comp.isLeaf(perspective))
				hash |= 1;
			if (comp.isDestructive())
				hash |= 2;
			if (comp.isFactory(perspective))
				hash |= 4;
			if (comp.isPlaceholder(perspective))
				hash |= 8;
			hash |= 32 * comp.getComponentKind().ordinal();
		}
		return hash ^ calculateStereotypeHash(figureFacet, subject);
	}
}
