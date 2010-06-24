package com.hopstepjump.backbone.parser;

import java.util.*;

import com.hopstepjump.backbone.nodes.*;
import com.hopstepjump.backbone.nodes.converters.*;
import com.hopstepjump.backbone.parserbase.*;
import com.hopstepjump.deltaengine.base.*;

public class ParserUtilities
{
	public static <T> void parseUUIDs(final Expect ex, final LazyObjects<T> references)
	{
		ex.oneOrMore(
				",",
				new LiteralMatch(
						new IAction()
						{
							public void act()
							{
								UuidReference reference = new UuidReference();
								ex.uuid(reference);
								references.addReference(reference);
							}
						}));		
	}
	
	public static <T> void parseUUIDs(final Expect ex, final List<String> references)
	{
		ex.oneOrMore(
				",",
				new LiteralMatch(
						new IAction()
						{
							public void act()
							{
								UuidReference reference = new UuidReference();
								ex.uuid(reference);
								references.add(reference.getUuid());
							}
						}));		
	}
	
	public static List<BBAppliedStereotype> parseAppliedStereotype(final Expect ex)
	{
		List<BBAppliedStereotype> stereos = new ArrayList<BBAppliedStereotype>();
		ex.guard("\u00ab",
			new IAction()
			{
				final UuidReference stereo = new UuidReference();
				public void act()
				{
					ex.oneOrMore(",",
							new LiteralMatch(
									new IAction()
									{
										public void act()
										{
											BBAppliedStereotype applied = new BBAppliedStereotype();
											ex.uuid(stereo);
											ex.
												guard(":",
													new IAction()
													{
														public void act()
														{
															ex.oneOrMore(",",
																	new LiteralMatch(
																			new IAction()
																			{
																				public void act()
																				{
																					parseAppliedValue(ex);
																				}
																			}));
														}
													});
										}
									}));
					ex.literal("\u00bb");
				}
			});
		return stereos;
	}
	

	private static void parseAppliedValue(final Expect ex)
	{
		UuidReference attr = new UuidReference();
		ex.
			uuid(attr).
			guard("=",
				new IAction()
				{
					public void act()
					{
						ex.parameter();
					}
				});					
	}
}
