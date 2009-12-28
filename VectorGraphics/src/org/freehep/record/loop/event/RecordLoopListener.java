/*
 * interface: RecordLoopListener
 *
 * Version $Id: RecordLoopListener.java,v 1.1 2009-03-04 22:46:57 andrew Exp $
 *
 * Date: March 7 2003
 *
 * (c) 2003 IceCube Collaboration
 */

package org.freehep.record.loop.event;

import java.util.*;

/**
 * This interface is, when added to a <code>{@link
 org.freehep.record.loop.SequentialRecordLoop SequentialRecordLoop}</code>
 * object, notified of any <code>{@link LoopEvent}</code> that occurs in that
 * object.
 * 
 * @version $Id: RecordLoopListener.java,v 1.1 2009-03-04 22:46:57 andrew Exp $
 * @author patton
 */
public interface RecordLoopListener extends EventListener
{

  // public static final member data

  // instance member function (alphabetic)

  /**
   * Tells this object that a loop is about to begin.
   */
  public void loopBeginning(LoopEvent loopEvent);

  /**
   * Tells this object that a loop has ended.
   */
  public void loopEnded(LoopEvent loopEvent);

  /**
   * Tells this object that a loop has been reset.
   */
  public void loopReset(LoopEvent loopEvent);

  /**
   * Tells this object the state of progress through a record loop.
   */
  public void progress(LoopProgressEvent loopProgressEvent);

  // static member functions (alphabetic)

}
