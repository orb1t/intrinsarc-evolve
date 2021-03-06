// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.io.*;

/**
 * EndPath TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: EndPath.java,v 1.1 2009-03-04 22:46:50 andrew Exp $
 */
public class EndPath extends EMFTag
{

  public EndPath()
  {
    super(60, 1);
  }

  public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
  {

    return this;
  }

}
