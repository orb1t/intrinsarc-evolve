// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.*;

/**
 * Escape TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: Escape.java,v 1.1 2009-03-04 22:46:47 andrew Exp $
 */
public class Escape extends CGMTag
{

  private int id;
  private byte[] data;

  public Escape()
  {
    super(6, 1, 1);
  }

  public Escape(int id, byte[] data)
  {
    this();
    this.id = id;
    this.data = data;
  }

  public void write(int tagID, CGMOutputStream cgm) throws IOException
  {
    cgm.writeInteger(id);
    cgm.writeData(data);
  }

  public void write(int tagID, CGMWriter cgm) throws IOException
  {
    cgm.print("ESCAPE ");
    cgm.writeInteger(id);
    cgm.println();
    cgm.writeData(data);
  }
}
