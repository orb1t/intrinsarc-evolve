package com.hopstepjump.backbone.nodes;

import java.io.*;
import java.util.*;

import com.hopstepjump.backbone.nodes.converters.*;
import com.hopstepjump.backbone.nodes.converters.BBXStreamConverters.*;
import com.hopstepjump.backbone.nodes.insides.*;
import com.hopstepjump.deltaengine.base.*;
import com.thoughtworks.xstream.annotations.*;

@XStreamAlias("Interface")
public class BBInterface extends DEInterface implements INode, Serializable
{
  private transient DEObject parent;
  @XStreamAlias("name")
  @XStreamAsAttribute
  private String rawName;
  private String uuid = BBUidGenerator.newUuid(getClass());  
  @XStreamAlias("substitutes")
  @XStreamConverter(InterfaceReferencesConverter.class)
  private List<DEElement> substitutes;
  @XStreamConverter(InterfaceReferencesConverter.class)
  private List<DEElement> resembles;
  private Boolean factoryInterface;
  private Boolean retired;
  
  // the stereotypes
  @XStreamAlias("appliedStereotypes")
  private List<DEAppliedStereotype> replacedAppliedStereotypes;
  
  // the constituents  
  @XStreamConverter(StringReferencesConverter.class)
  private List<String> deletedOperations;
  private List<BBReplacedOperation> replacedOperations;
  private List<DEOperation> addedOperations;

	// for going the other way
  private transient List<DEElement> substituters;
  private transient Set<DEElement> resemblers;
  
  // the deltas for the difference calculations
  private transient boolean initialiseDeltas;
  private transient Deltas operations;
  private transient Deltas appliedStereotypes;
  
  // other cached variables
  private transient Set<String> replacedUuids;
  
  public BBInterface() {}
  
  public BBInterface(String uuid)
  {
  	this.uuid = uuid;
  	this.rawName = uuid;
  	readResolve();
  }

  protected Object readResolve()
  {
  	super.readResolve();
  	GlobalNodeRegistry.registry.addNode(this);
  	
    substituters = new ArrayList<DEElement>();
    resemblers = new HashSet<DEElement>();

  	return this;
  }
  
  public void setParent(DEObject parent)
  {
    this.parent = parent;
    
    // tell all constituents
    informAboutParent(replacedOperations);
    informAboutParent(addedOperations);
    informAboutParent(replacedAppliedStereotypes);
    
  	// tell anything we resemble or substitute about ourselves
  	if (resembles != null)
  		for (DEElement r : resembles)
  			r.getPossibleImmediateSubElements().add(this);
  	if (substitutes != null)
  		for (DEElement r : substitutes)
  			r.getSubstituters().add(this);
  }

  private void informAboutParent(List<? extends BBReplacedConstituent> constituents)
	{
  	if (constituents != null)
  	{
	  	Set<DEObject> objects = new HashSet<DEObject>();    
	    for (BBReplacedConstituent replaced : constituents)
	    	objects.add(replaced.getReplacement());
	    informAboutParent(objects);
  	}  	
	}
  
	private void informAboutParent(Collection<? extends DEObject> nodes)
  {
		if (nodes != null)
		{
	    for (DEObject node : nodes)
	    	if (nodes  instanceof INode)
		  		((INode) node).setParent(this);
		}
  }

  public void setUuid(String uuid)
  {
    this.uuid = uuid;
  }

  public void setRawName(String rawName)
  {
    this.rawName = rawName;
  }
  
  public List<String> settable_getDeletedOperations()
  {
    if (deletedOperations == null)
      deletedOperations = new ArrayList<String>();
    return deletedOperations;
  }

  public List<BBReplacedOperation> settable_getReplacedOperations()
  {
    if (replacedOperations == null)
      replacedOperations = new ArrayList<BBReplacedOperation>();
    return replacedOperations;
  }

  public List<DEOperation> settable_getAddedOperations()
  {
    if (addedOperations == null)
      addedOperations = new ArrayList<DEOperation>();
    return addedOperations;
  }

  public List<DEElement> settable_getRawResembles()
  {
    if (resembles == null)
      resembles = new ArrayList<DEElement>();
    return resembles;
  }
  
  public List<DEElement> settable_getRawSubstitutes()
  {
    if (substitutes == null)
      substitutes = new ArrayList<DEElement>();
    return substitutes;
  }

  ///////////////////////////////// contract functions ///////////////////////


	@Override
	public Set<DEElement> getPossibleImmediateSubElements()
	{
		return resemblers;
	}

	@Override
	public List<DEElement> getSubstituters()
	{
		return substituters;
	}
	
	@Override
	public List<? extends DEElement> getRawResembles()
	{
    if (resembles == null)
      return new ArrayList<DEElement>();
    return resembles;
	}
	
  @Override
  public String getRawName()
  {
    return rawName;
  }
  
  @Override
  public IDeltas getDeltas(ConstituentTypeEnum type)
  {
    if (!initialiseDeltas)
    {
      initialiseDeltas();
      initialiseDeltas = true;
    }
    
    switch (type)
    {
	  	case DELTA_APPLIED_STEREOTYPE:
	  		return appliedStereotypes;
      case DELTA_OPERATION:
        return operations;
    }
    
    return super.getDeltas(type);
  } 

  @Override
  public List<DEElement> getRawSubstitutes()
  {
    List<DEElement> raw = new ArrayList<DEElement>();
    if (substitutes != null)
      raw.addAll(substitutes);
    return raw;
  }

  @Override
  public DEElement asElement()
  {
    return this;
  }

  @Override
  public DEObject getParent()
  {
    return parent;
  }

  @Override
  public Object getRepositoryObject()
  {
    return this;
  }

  @Override
  public String getUuid()
  {
    return uuid;
  }
	
	///////////////////////////////////////////////////
	///////////////// complex delta handling //////////
	
  private void initialiseDeltas()
  {
    // handle operations
    operations = BBComponent.createDeltas(
        this,
        ConstituentTypeEnum.DELTA_ATTRIBUTE,
        addedOperations,
        deletedOperations,
        replacedOperations);
    
    // handle stereotypes
    Set<DeltaPair> pairs = new HashSet<DeltaPair>();
    if (replacedAppliedStereotypes != null)
	    for (DEAppliedStereotype app : replacedAppliedStereotypes)
	    	pairs.add(new DeltaPair(app.getUuid(), app));
    appliedStereotypes = new Deltas(
    		this,
    		new HashSet<DeltaPair>(),
    		new HashSet<String>(),
    		pairs,
    		ConstituentTypeEnum.DELTA_APPLIED_STEREOTYPE);
  }

  @Override
  public Set<String> getReplaceUuidsOnly()
  {
    if (replacedUuids == null)
    {
      replacedUuids = new LinkedHashSet<String>();
      BBComponent.addReplacedUuids(replacedUuids, replacedOperations);
    }
    
    return replacedUuids;
  }

  public List<DEAppliedStereotype> settable_getReplacedAppliedStereotypes()
	{
  	if (replacedAppliedStereotypes == null)
  		replacedAppliedStereotypes = new ArrayList<DEAppliedStereotype>();
  	return replacedAppliedStereotypes;
	}

	public void setFactoryInterface(boolean factory)
	{
		factoryInterface = factory ? true : null; 
	}
	

  public void setRawRetired(boolean retired)
  {
  	this.retired = retired ? true : null;
  }
  
	@Override
	public boolean isRawRetired()
	{
		return retired != null ? retired : false;
	}

	@Override
	public boolean isRawAbstract()
	{
		// interfaces are never abstract
		return false;
	}
}