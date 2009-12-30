package com.hopstepjump.backbone.nodes.simple;

import java.util.*;

import com.hopstepjump.backbone.exceptions.*;
import com.hopstepjump.deltaengine.base.*;
import com.thoughtworks.xstream.annotations.*;

@XStreamAlias("Port")
public class BBSimplePort extends BBSimpleObject
{
	@XStreamAsAttribute
	private String name;
	private transient String rawName;
  @XStreamConverter(SimpleReferencesConverter.class)
	private List<BBSimpleInterface> provides;
  @XStreamConverter(SimpleReferencesConverter.class)
	private List<BBSimpleInterface> requires;
  private int upperBound;
  private int lowerBound;
	private transient DEPort complex;
	private transient boolean resolved;
	private transient BBSimpleComponent owner;
	
	public BBSimplePort(BBSimpleElementRegistry registry, DEComponent component, DEPort port, BBSimpleComponent owner)
	{
		rawName = port.getName();
		name = registry.makeName(rawName);
		upperBound = port.getUpperBound();
		lowerBound = port.getLowerBound();
		this.owner = owner;
		Set<? extends DEInterface> provided = component.getProvidedInterfaces(registry.getPerspective(), port);
		if (!provided.isEmpty())
		{
			provides = new ArrayList<BBSimpleInterface>();
			for (DEInterface prov : provided)
				provides.add(registry.retrieveInterface(prov));
		}
		Set<? extends DEInterface> required = component.getRequiredInterfaces(registry.getPerspective(), port);
		if (!required.isEmpty())
		{
			requires = new ArrayList<BBSimpleInterface>();
			for (DEInterface req : component.getRequiredInterfaces(registry.getPerspective(), port))
				requires.add(registry.retrieveInterface(req));
		}
		complex = port;
	}

	public void resolveImplementation(BBSimpleElementRegistry registry) throws BBImplementationInstantiationException
	{
		if (resolved)
			return;
		resolved = true;
		
		if (provides != null)
			for (BBSimpleInterface prov : provides)
				prov.resolveImplementation(registry);
		if (requires != null)
			for (BBSimpleInterface req : requires)
				req.resolveImplementation(registry);
	}

	public DEPort getComplexPort()
	{
		return complex;
	}

	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public String getRawName()
	{
		return rawName;
	}

	public List<BBSimpleInterface> getProvides()
	{
		if (provides == null)
			return new ArrayList<BBSimpleInterface>();
		return provides;
	}

	public List<BBSimpleInterface> getRequires()
	{
		if (requires == null)
			return new ArrayList<BBSimpleInterface>();
		return requires;
	}

	public int getUpperBound()
	{
		return upperBound;
	}

	public int getLowerBound()
	{
		return lowerBound;
	}

	public BBSimpleComponent getOwner()
	{
		return owner;
	}
	
	public boolean isIndexed()
	{
		return upperBound > 1 || upperBound == -1;
	}
	
	public boolean suppressGeneration()
	{
		return false;
	}

	public boolean isHyperportStart()
	{
		return complex.getPortKind() == PortKindEnum.HYPERPORT_START;
	}

	public boolean isHyperportEnd()
	{
		return complex.getPortKind() == PortKindEnum.HYPERPORT_END;
	}
}