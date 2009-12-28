// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import java.io.*;

/**
 * SoundStreamHead2 TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: SoundStreamHead2.java,v 1.1 2009-03-04 22:46:51 andrew Exp $
 */
public class SoundStreamHead2 extends SoundStreamHead
{

  public SoundStreamHead2(int playbackSoundRate, boolean playbackSoundStereo, int streamSoundRate,
      boolean streamSoundStereo, int samples)
  {
    this();
    this.playbackSoundRate = playbackSoundRate;
    this.playbackSoundSize16 = true;
    this.playbackSoundStereo = playbackSoundStereo;
    this.streamSoundCompression = 1;
    this.streamSoundRate = streamSoundRate;
    this.streamSoundSize16 = true;
    this.streamSoundStereo = streamSoundStereo;
    this.samples = samples;
  }

  public SoundStreamHead2()
  {
    super(45, 3);
  }

  public SWFTag read(int tagID, SWFInputStream swf, int len) throws IOException
  {

    SoundStreamHead2 tag = new SoundStreamHead2();
    tag.read(swf, len);
    return tag;
  }
}
