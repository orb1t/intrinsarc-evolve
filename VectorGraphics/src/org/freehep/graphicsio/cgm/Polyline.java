// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.awt.geom.*;
import java.io.*;

/**
 * Polyline TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: Polyline.java,v 1.1 2009-03-04 22:46:47 andrew Exp $
 */
public class Polyline extends CGMTag
{

  protected Point2D[] p;

  public Polyline()
  {
    super(4, 1, 1);
  }

  public Polyline(Point2D[] p)
  {
    this();
    this.p = p;
  }

  protected Polyline(int elementClass, int elementID, int version)
  {
    super(elementClass, elementID, version);
  }

  public void write(int tagID, CGMOutputStream cgm) throws IOException
  {
    for (int i = 0; i < p.length; i++)
    {
      cgm.writePoint(p[i]);
    }
  }

  public void write(int tagID, CGMWriter cgm) throws IOException
  {
    cgm.println("LINE");
    writePointList(cgm);
  }

  protected void writePointList(CGMWriter cgm) throws IOException
  {
    cgm.indent();
    cgm.writePoint(p[0]);
    cgm.println();
    for (int i = 1; i < p.length; i++)
    {
      cgm.print(", ");
      cgm.writePoint(p[i]);
      cgm.println();
    }
    cgm.outdent();
  }
}