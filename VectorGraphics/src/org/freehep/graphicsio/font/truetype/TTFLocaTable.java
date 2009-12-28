// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.font.truetype;

import java.io.*;

/**
 * LOCA Table.
 * 
 * @author Simon Fischer
 * @version $Id: TTFLocaTable.java,v 1.1 2009-03-04 22:46:54 andrew Exp $
 */
public class TTFLocaTable extends TTFTable
{

  public long offset[];

  public String getTag()
  {
    return "loca";
  }

  public void readTable() throws IOException
  {
    short format = ((TTFHeadTable) getTable("head")).indexToLocFormat;
    int numGlyphs = ((TTFMaxPTable) getTable("maxp")).numGlyphs + 1;
    offset = new long[numGlyphs];
    for (int i = 0; i < numGlyphs; i++)
    {
      offset[i] = (format == TTFHeadTable.ITLF_LONG ? ttf.readULong() : ttf.readUShort() * 2);
    }
  }

  public String toString()
  {
    String str = super.toString();
    for (int i = 0; i < offset.length; i++)
    {
      if (i % 16 == 0)
        str += "\n  ";
      str += offset[i] + " ";
    }
    return str;
  }
}
