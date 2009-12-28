/**
 * Copyright (C) 1998-2000 by University of Maryland, College Park, MD 20742, USA
 * All rights reserved.
 */
package edu.umd.cs.jazz.event;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.*;

import edu.umd.cs.jazz.*;
import edu.umd.cs.jazz.util.*;

/**
 * <b>ZMouseEvent</b> is an event which indicates that a mouse action occurred in a node.
 * <P>
 * This low-level event is generated by a node object for:
 * <ul>
 * <li>Mouse Events
 *     <ul>
 *     <li>a mouse button is pressed
 *     <li>a mouse button is released
 *     <li>a mouse button is clicked (pressed and released)
 *     <li>the mouse cursor enters a node
 *     <li>the mouse cursor exits a node
 *     </ul>
 * <P>
 * A ZMouseEvent object is passed to every <code>ZMouseListener</code>
 * or <code>ZMouseAdapter</code> object which registered to receive
 * the "interesting" mouse events using the component's
 * <code>addMouseListener</code> method.
 * (<code>ZMouseAdapter</code> objects implement the
 * <code>ZMouseListener</code> interface.) Each such listener object
 * gets a <code>ZMouseEvent</code> containing the mouse event.
 * <P>
 * <b>Warning:</b> Serialized and ZSerialized objects of this class will not be
 * compatible with future Jazz releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Jazz. A future release of Jazz will provide support for long
 * term persistence.
 *
 * @see ZMouseAdapter
 * @see ZMouseListener
 */
public class ZMouseEvent extends MouseEvent implements ZEvent, Serializable {
    private int id;                         // The id that specifies the event trigger (press, release, etc.)
    private ZSceneGraphPath grabPath;       // The path the event took from the ZCanvas to the grabbed node
    private ZSceneGraphPath currentPath;    // The the path to the object that the mouse is currently over
                                            // this may differ from the normal path in MOUSE_RELEASED
                                            // or MOUSE_DRAGGED events

    private Class listenerType = null; // This will be taken out in the future,

    /**
     * Constructs a new ZMouse event from a Java MouseEvent.
     *
     * @param id The event type (MOUSE_PRESSED, MOUSE_RELEASED, MOUSE_CLICKED, MOUSE_ENTERED, MOUSE_EXITED)
     * @param e The original Java mouse event
     * @param path The path to use for getNode() and getPath()
     * @param mouseOverPath The path to the current node under the mouse, this may be differnt then the normal path
     * when in MOUSE_RELEASED events.
     * @deprecated as of Jazz 1.1, use createMouseEvent() instead.
     */
    public ZMouseEvent(int id, MouseEvent e, ZSceneGraphPath aGrabPath, ZSceneGraphPath aCurrentPath) {
        super((Component)e.getSource(), e.getID(), e.getWhen(), e.getModifiers(), e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger());
        this.id = id;

        if (id == ZMouseEvent.MOUSE_MOVED ||
            id == ZMouseEvent.MOUSE_DRAGGED) {
            listenerType = ZMouseMotionListener.class;
        } else {
            listenerType = ZMouseListener.class;
        }

        grabPath = aGrabPath;
        currentPath = aCurrentPath;
    }

    /**
     * @deprecated as of Jazz 1.1, use createMouseEvent() instead.
     */
    public ZMouseEvent(int id, ZNode aNode, MouseEvent e, ZSceneGraphPath aGrabPath) {
        super((Component)e.getSource(), e.getID(), e.getWhen(), e.getModifiers(), e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger());

        if (id == ZMouseEvent.MOUSE_MOVED ||
            id == ZMouseEvent.MOUSE_DRAGGED) {
            listenerType = ZMouseMotionListener.class;
        } else {
            listenerType = ZMouseListener.class;
        }

        this.id = id;
        grabPath = aGrabPath;
        currentPath = aGrabPath;
    }

    protected ZMouseEvent(int id, MouseEvent e, ZSceneGraphPath aGrabPath, ZSceneGraphPath aCurrentPath, Object dummy) {
        super((Component)e.getSource(), e.getID(), e.getWhen(), e.getModifiers(), e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger());

        if (id == ZMouseEvent.MOUSE_MOVED ||
            id == ZMouseEvent.MOUSE_DRAGGED) {
            listenerType = ZMouseMotionListener.class;
        } else {
            listenerType = ZMouseListener.class;
        }

        this.id = id;
        grabPath = aGrabPath;
        currentPath = aCurrentPath;
    }

    /**
     * Creates and returns a new ZMouse event from a Java MouseEvent.
     *
     * @param id The event type (MOUSE_PRESSED, MOUSE_RELEASED, MOUSE_CLICKED, MOUSE_ENTERED, MOUSE_EXITED, MOUSE_MOVED, MOUSE_DRAGGED)
     * @param e The original Java mouse event
     * @param path The path to use for getNode() and getPath()
     * @param mouseOverPath The path to the current node under the mouse, this may be differnt then the normal path
     * when in MOUSE_DRAGGED and MOUSE_RELEASED events.
     */
    public static ZMouseEvent createMouseEvent(int id,
                                               MouseEvent e,
                                               ZSceneGraphPath path,
                                               ZSceneGraphPath mouseOverPath) {
        if (id == ZMouseEvent.MOUSE_MOVED ||
            id == ZMouseEvent.MOUSE_DRAGGED) {

            return new ZMouseMotionEvent(id, e, path, mouseOverPath);
        } else {
            return new ZMouseEvent(id, e, path, mouseOverPath, null);
        }
    }

    /**
     * Returns the x,y position of the event in the local coordinate system of the node
     * the event occurred on.
     * @return a Point2D object containing the x and y coordinates local to the node.
     */
    public Point2D getLocalPoint() {
        Point2D.Double point = new Point2D.Double();
        point.setLocation(getX(), getY());
        grabPath.screenToLocal(point);
        return point;
    }

    /**
     * Returns the horizontal x position of the event in the local coordinate system
     * of the node the event occurred on.
     * @return x a double indicating horizontal position local to the node.
     */
    public double getLocalX() {
        Point2D point = getLocalPoint();
        return point.getX();
    }

    /**
     * Returns the vertical y position of the event in the local coordinate system
     * of the node the event occurred on.
     * @return y a double indicating vertical position local to the node.
     */
    public double getLocalY() {
        Point2D point = getLocalPoint();
        return point.getY();
    }

    /**
     * Determine the event type.
     * @return the id
     */
    public int getID() {
        return id;
    }

    /**
     * Determine the node the event originated at.  If an event percolates
     * up the tree and is handled by an event listener higher up in the
     * tree than the original node that generated the event, this returns
     * the original node.  For mouse drag and release events, this is the
     * node that the original matching press event went to - in other words,
     * the event is 'grabbed' by the originating node.
     * @return the node
     */
    public ZNode getNode() {
        return getGrabNode();
    }

    /**
     * Determine the path the event took from the ZCanvas down to the visual component.
     * @return the path
     */
    public ZSceneGraphPath getPath() {
        return getGrabPath();
    }

    /**
     * Determine the node the event originated at.  If an event percolates
     * up the tree and is handled by an event listener higher up in the
     * tree than the original node that generated the event, this returns
     * the original node.  For mouse drag and release events, this is the
     * node that the original matching press event went to - in other words,
     * the event is 'grabbed' by the originating node.
     * @return the node
     */
    public ZNode getGrabNode() {
        return grabPath.getNode();
    }

    /**
     * Return the path from the ZCanvas down to the currently grabbed object.
     * @return the path
     */
    public ZSceneGraphPath getGrabPath() {
        return grabPath;
    }

    /**
     * Get the current node that is under the cursor. This may return a different result then getGrabNode() when
     * in a MOUSE_RELEASED or MOUSE_DRAGGED event.
     * @return the current node.
     */
    public ZNode getCurrentNode() {
        return currentPath.getNode();
    }

    /**
     * Get the path from the ZCanvas down to the visual component currently under the mouse.This may
     * give a different result then getGrabPath() durring a MOUSE_DRAGGED or MOUSE_RELEASED operation.
     * @return the current path.
     */
    public ZSceneGraphPath getCurrentPath() {
        return currentPath;
    }

    /**
     * Calls appropriate method on the listener based on this events ID.
     */
    public void dispatchTo(Object listener) {
        if (listenerType == ZMouseListener.class) {
            ZMouseListener mouseListener = (ZMouseListener) listener;
            switch (getID()) {
                case ZMouseEvent.MOUSE_CLICKED:
                    mouseListener.mouseClicked(this);
                    break;
                case ZMouseEvent.MOUSE_ENTERED:
                    mouseListener.mouseEntered(this);
                    break;
                case ZMouseEvent.MOUSE_EXITED:
                    mouseListener.mouseExited(this);
                    break;
                case ZMouseEvent.MOUSE_PRESSED:
                    mouseListener.mousePressed(this);
                    break;
                case ZMouseEvent.MOUSE_RELEASED:
                    mouseListener.mouseReleased(this);
                    break;
                default:
                    throw new RuntimeException("ZMouseEvent with bad ID");
            }
        } else {
            ZMouseMotionListener mouseMotionListener = (ZMouseMotionListener) listener;
            switch (getID()) {
                case ZMouseEvent.MOUSE_DRAGGED:
                    mouseMotionListener.mouseDragged(this);
                    break;
                case ZMouseEvent.MOUSE_MOVED:
                    mouseMotionListener.mouseMoved(this);
                    break;
                default:
                    throw new RuntimeException("ZMouseMotionEvent with bad ID");
            }
        }
    }

    /**
     * Returns the ZMouseLister class.
     */
    public Class getListenerType() {
        return listenerType;
    }

    /**
     * Set the souce of this event. As the event is fired up the tree the source of the
     * event will keep changing to reflect the scenegraph object that is firing the event.
     */
    public void setSource(Object aSource) {
        source = aSource;
    }
}