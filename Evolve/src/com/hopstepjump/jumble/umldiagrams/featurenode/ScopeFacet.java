package com.hopstepjump.jumble.umldiagrams.featurenode;

import com.hopstepjump.gem.*;

/**
 * @author Andrew
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public interface ScopeFacet extends Facet
{
  public Object setScope(boolean classifierScope);
  public void unSetScope(Object memento);
}