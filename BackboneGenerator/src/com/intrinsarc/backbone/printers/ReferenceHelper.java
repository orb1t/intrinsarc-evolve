package com.intrinsarc.backbone.printers;

import java.util.regex.*;

import com.intrinsarc.deltaengine.base.*;

public class ReferenceHelper
{
	private static Pattern WHITE = Pattern.compile("\\s", Pattern.MULTILINE);
	private DEStratum current;
	private BackbonePrinterMode mode;
	
	public ReferenceHelper(DEStratum current, BackbonePrinterMode mode)
	{
		this.current = current;
		this.mode = mode;
	}
	
	public String name(DEObject obj)
	{
		return objectId(obj);
	}
	
	public String reference(DEObject obj)
	{
		return objectId(obj);
	}
	
	public String safeName(String name)
	{
		return WHITE.matcher(name).replaceAll("_");
	}
	
	private String objectId(DEObject obj)
	{
		if (obj == null)
			throw new IllegalStateException("Found no object for name");
		String uuid = obj.getUuid();
		if (obj.getName() == null || obj.getName().length() == 0 || isReadable(uuid))
			return uuid;
		else
		{
			// if a reference, use full path
			if (mode == BackbonePrinterMode.REAL_NAMES)
			{
				String name = obj.getName();
				return WHITE.matcher(name).replaceAll("_");
			}
			else
			{
				String name = obj.getName();
				return uuid + "/" + name + "/";
			}
		}
	}

	public String name(DEElement obj)
	{
		return elementId(obj, false);
	}
	
	public String reference(DEElement obj)
	{
		return elementId(obj, true);
	}
	
	private String elementId(DEElement obj, boolean reference)
	{
		if (obj == null)
			throw new IllegalStateException("Found no object for name");
		String uuid = obj.getUuid();
		if (obj.getName() == null || obj.getName().length() == 0 || isReadable(uuid))
			return uuid;
		else
		{
			// if a reference, use full path
			if (mode == BackbonePrinterMode.REAL_NAMES)
			{
				// if the uuid is readable, don't use the name
				String name = reference && current != obj.getHomeStratum() ?
						obj.getFullyQualifiedName("::") : obj.getName();
				return WHITE.matcher(name).replaceAll("_").replace('/', '_');
			}
			else
			{
				String name = obj.getName();
				if (isReadable(uuid))
					return uuid;
				return uuid + "/" + name.replace('/', '_') + "/";
			}
		}
	}

	private Pattern NON_READABLE_UUID = Pattern.compile("[a-fA-F0-9\\-]+");
	/**
	 * returns true if the pattern is readable -- i.e. isn't a match for a non-readable UUID
	 */
	private boolean isReadable(String uuid)
	{
		return !NON_READABLE_UUID.matcher(uuid).matches();
	}
}
