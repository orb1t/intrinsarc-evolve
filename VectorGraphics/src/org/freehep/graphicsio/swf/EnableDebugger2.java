// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import java.io.*;
import java.security.*;

/**
 * EnableDebugger TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: EnableDebugger2.java,v 1.1 2009-03-04 22:46:52 andrew Exp $
 */
public class EnableDebugger2 extends ControlTag
{

  private String password;

  public EnableDebugger2(String password)
  {
    this();

    try
    {
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      byte[] b = md5.digest(password.getBytes());
      this.password = new String(b);
    }
    catch (NoSuchAlgorithmException nsae)
    {
      this.password = password;
    }
  }

  public EnableDebugger2()
  {
    super(64, 6);
  }

  public SWFTag read(int tagID, SWFInputStream swf, int len) throws IOException
  {

    EnableDebugger2 tag = new EnableDebugger2();
    swf.readUnsignedShort();
    tag.password = swf.readString();
    return tag;
  }

  public void write(int tagID, SWFOutputStream swf) throws IOException
  {
    swf.writeUnsignedShort(0);
    swf.writeString(password);
  }
}
