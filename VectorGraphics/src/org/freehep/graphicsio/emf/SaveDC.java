// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.io.*;

/**
 * SaveDC TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: SaveDC.java,v 1.1 2009-03-04 22:46:50 andrew Exp $
 */
public class SaveDC extends EMFTag
{

  public SaveDC()
  {
    super(33, 1);
  }

  public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
  {

    return this;
  }
}
