// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.io.*;

/**
 * SetMetaRgn TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: SetMetaRgn.java,v 1.1 2009-03-04 22:46:50 andrew Exp $
 */
public class SetMetaRgn extends EMFTag
{

  public SetMetaRgn()
  {
    super(28, 1);
  }

  public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
  {

    return this;
  }

}
