package com.hopstepjump.backbone;

import java.io.*;
import java.util.*;

import com.hopstepjump.backbone.exceptions.*;
import com.hopstepjump.backbone.nodes.*;
import com.hopstepjump.backbone.nodes.converters.*;
import com.hopstepjump.backbone.nodes.simple.*;
import com.hopstepjump.backbone.nodes.simple.internal.*;
import com.hopstepjump.backbone.readers.*;
import com.hopstepjump.backbone.runtime.api.*;
import com.hopstepjump.deltaengine.base.*;
import com.hopstepjump.deltaengine.errorchecking.*;
import com.thoughtworks.xstream.*;

public class BackboneInterpreter
{
	List<String> x = new ArrayList<String>();

	public static void main(String args[])
	{
		System.out.println("> Backbone v2.0");
		
		// ensure we have enough arguments
		if (args.length < 4)
		{
			System.err.println("Usage: backbone [-nocheck] load-list-file stratum component port");
			System.exit(-1);
		}		
		run(args);
	}

	private static void run(String[] args)
	{
		boolean nocheck = args[0].equals("-nocheck");
		int offset = nocheck ? 1 : 0;
		String loadListFile = args[0 + offset];
		String stratum = args[1 + offset];
		String component = args[2 + offset];
		String port = args[3 + offset];
		
		String tab = "      |  ";
		System.out.println(tab + "loading system from " + loadListFile);
		XStream x = new XStream();
		BBXStreamConverters.registerConverters(x);
		LoadListReader reader = new LoadListReader(x, new File(loadListFile));
		
		// install the delta engine
		GlobalDeltaEngine.engine = new BBDeltaEngine();
		
		try
		{
			long start = System.currentTimeMillis();
			List<BBStratum> system = reader.readSystem();
			GlobalNodeRegistry.registry.resolveLazyReferences();
			long end = System.currentTimeMillis();
			System.out.println(tab + "loaded " + system.size() + " packages succesfully in " + (end - start) + "ms");
			
			// tell each package about their parent
			// anything that doesn't have a loaded parent gets the model
			BBStratum root = GlobalNodeRegistry.registry.getRoot();
			for (BBStratum pkg : system)
			{
				DEObject parent = GlobalNodeRegistry.registry.getNode(pkg.getParentUuid());
				if (parent == null)
					parent = root;
				pkg.setParentAndTellChildren((BBStratum) parent);
			}
			
			// perform the error check
			if (nocheck)
				System.out.println("Skipping checking phase");
			else
			{
				ErrorRegister errors = new ErrorRegister();
				StratumErrorDetector detector = new StratumErrorDetector(errors);
				system.add(root);
				
				int size = system.size();
	    	final int totalPermutations = detector.calculatePerspectivePermutations(system, -1, true);
	    	int extraSize = size > 1 ? size - 2 : 0;
				System.out.println(tab + "total package combinations to check is " + totalPermutations + " + " + (extraSize + 1) + " at home");
	    	start = System.currentTimeMillis();
	    	
	    	// check all at home
	    	if (size > 1)
	    		detector.checkAllAtHome(system, 0, extraSize, null);
	      
	    	// now check at the top level perspective
	      detector.checkAllInOrder(system, -1, true, null);
	      
				end = System.currentTimeMillis();
				if (errors.countErrors() > 0)
				{
					System.out.println(tab + "found " + errors.countErrors() + " errors in " + (end - start) + "ms");
					for (ErrorLocation loc : errors.getAllErrors().keySet())
					{
						for (ErrorDescription desc : errors.getAllErrors().get(loc))
							System.err.println("Error: " + loc + " | " + desc + " at " + loc.getFirstUuid() + " |");
					}
					System.exit(-1);
				}
				else
					System.out.println(tab + "check took " + (end - start) + "ms; found no errors");
			}
			
			String fullName = stratum + "::" + component + "." + port;
			System.out.println("> running " + fullName);
			
			// find the correct stratum
			DEStratum runStratum = findNamedStratum(fullName, new StringTokenizer(stratum, "::"), root);
			DEComponent runComponent = findNamedComponent(fullName, runStratum, component);

			// flatten out
			BBSimpleElementRegistry registry = new BBSimpleElementRegistry(root, runComponent);
			BBSimpleComponent top = new BBSimpleComponent(registry, runComponent);

			// this must have only one port, which is the runner
			List<BBSimplePort> ports = top.getPorts();
			if (ports == null || ports.size() != 1)
				throw new BBImplementationInstantiationException("Top component must have a single provided run port.  " + ports.size() + " ports found", top);
			BBSimplePort provider = ports.get(0);
			if (provider.getRequires() != null && provider.getRequires().size() != 0 || provider.getProvides() == null || provider.getProvides().size() != 1)
				throw new BBImplementationInstantiationException("Top component run port must provide only IRun", top);
			BBSimpleInterface iface = provider.getProvides().get(0);
			if (iface.getImplementationClassName() == null || !iface.getImplementationClassName().equals(IRun.class.getName()))
				throw new BBImplementationInstantiationException("Top component run port must provide only IRun", top);

			BBSimpleInstantiatedFactory instance = top.instantiate(registry);
			instance.runViaPort(provider, args);
		}
		catch (BBNodeNotFoundException e)
		{
			System.err.println("Problem finding reference: " + e.getMessage());
			System.exit(-1);
		}
		catch (StratumLoadingException e)
		{
			System.err.println("Problem loading stratum: " + e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
		catch (BBVariableNotFoundException e)
		{
			System.err.println("Problem loading stratum: " + e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
		catch (BBBadRunPointException e)
		{
			System.err.println("Bad run point specified: " + e.getMessage());
			System.exit(-1);
		}
		catch (BBImplementationInstantiationException e)
		{
			if (e.getElement() != null)
				System.err.println(e.getElement().getName() + " | " + e.getMessage() + " at " + e.getElement().getUuid() + " |");
			e.printStackTrace(System.err);
			System.exit(-1);
		}
		catch (BBRuntimeException e)
		{
			if (e.getElement() == null)
			{
				System.err.println("Runtime problem: " + e.getMessage());
			}
			else
				System.err.println(e.getElement().getName() + " | " + e.getMessage() + " at " + e.getElement().getUuid() + " |");
			if (e.getCause() != null)
				e.getCause().printStackTrace(System.err);

			System.exit(-1);
		}
	}
	
	private static DEPort findNamedPort(String fullName, DEStratum perspective, DEComponent runComponent, String portName) throws BBBadRunPointException
	{
		Set<DeltaPair> pairs = runComponent.getDeltas(ConstituentTypeEnum.DELTA_PORT).getConstituents(perspective);
		for (DeltaPair pair : pairs)
		{
			if (pair.getConstituent().getName().equals(portName))
				return pair.getConstituent().asPort();
		}
		throw new BBBadRunPointException("Cannot find port: " + portName);
	}

	private static DEComponent findNamedComponent(String fullName, DEStratum runStratum, String componentName) throws BBBadRunPointException
	{
		for (DEElement elem : runStratum.getChildElements())
		{
			if (elem.asComponent() != null && elem.asComponent().getName().equals(componentName))
				return elem.asComponent(); 
		}
		throw new BBBadRunPointException("Cannot find component: " + fullName);
	}

	private static DEStratum findNamedStratum(String fullName, StringTokenizer tokens, DEStratum start) throws BBBadRunPointException
	{
		// are we the match?
		if (!tokens.hasMoreTokens())
			return start;

		String next = tokens.nextToken();
		for (DEStratum child : start.getDirectlyNestedPackages())
		{
			if (child.getName().equals(next))
			{
				DEStratum match = findNamedStratum(fullName, tokens, child);
				if (match != null)
					return match;
			}
		}
		// if we got here, we couldn't find a named match
		throw new BBBadRunPointException("Cannot find stratum: " + fullName);
	}
}