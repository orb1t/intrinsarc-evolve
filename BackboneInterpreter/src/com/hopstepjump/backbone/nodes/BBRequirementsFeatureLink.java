package com.hopstepjump.backbone.nodes;

import java.io.*;
import java.util.*;

import com.hopstepjump.backbone.nodes.converters.*;
import com.hopstepjump.backbone.nodes.insides.*;
import com.hopstepjump.backbone.parserbase.*;
import com.hopstepjump.deltaengine.base.*;

public class BBRequirementsFeatureLink extends DERequirementsFeatureLink implements INode, Serializable
{
  private transient DEObject parent;
  private String name;
  private String uuid = BBUidGenerator.newUuid(getClass());
	private SubfeatureKindEnum kind;
	private DERequirementsFeature subfeature;
	private List<DEAppliedStereotype> appliedStereotypes;
  
  public BBRequirementsFeatureLink(UUIDReference reference)
  {
  	this.uuid = reference.getUUID();
  	this.name = reference.getName();
  	GlobalNodeRegistry.registry.addNode(this);
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

  public void setParent(DEObject parent)
  {
    this.parent = parent;
  }

	public void setAppliedStereotypes(List<DEAppliedStereotype> appliedStereotypes)
	{
		this.appliedStereotypes = appliedStereotypes.isEmpty() ? null : appliedStereotypes;
	}

	@Override
	public List<DEAppliedStereotype> getAppliedStereotypes()
	{
		return appliedStereotypes == null ? new ArrayList<DEAppliedStereotype>() : appliedStereotypes;
	}

	@Override
	public SubfeatureKindEnum getKind()
	{
		return kind;
	}

	@Override
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	@Override
	public DERequirementsFeature getSubfeature()
	{
		return subfeature;
	}
}
