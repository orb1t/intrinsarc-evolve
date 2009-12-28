package com.hopstepjump.gem;

/**
 *
 * (c) Andrew McVeigh 05-Aug-02
 *
 */
public interface MainFacet extends Facet
{
	/** provide a way to retrieve a facet in a dynamic fashion */
	public Facet getDynamicFacet(Class facetClass);
	public boolean hasDynamicFacet(Class facetClass);
	/** register a dynamic facet */
	public void registerDynamicFacet(Facet facet, Class facetInterface);
}
