/**
 * Copyright (C) 1998-2000 by University of Maryland, College Park, MD 20742, USA
 * All rights reserved.
 */

package edu.umd.cs.jazz.util;

import java.io.*;
import java.awt.geom.*;

import edu.umd.cs.jazz.*;
import edu.umd.cs.jazz.event.*;

/**
 * <b>ZSceneGraphPath</b> represents a unique path in a scene graph
 * from a top-level camera to a terminal node. The path is typically used
 * in event handlers to determine what object is under the mouse pointer,
 * and what camera(s) that object was rendered within.  The path is
 * typically generated by {@link ZDrawingSurface#pick}.  The terminal node
 * can be a node or visual component, but is typically a visual component,
 * or null.  One exception to this is that if a group's children are not
 * pickable than the group itself will be the terminal object if one
 * if its children was actually picked.  Also, cameras are not picked
 * as objects, and thus do not typically appear as a terminal object.
 *
 * <P>The path also encapsulates a transform, indicating the
 * composited transform from the top-level camera to the terminal
 * object, possibly going through zero or more internal cameras.  Even
 * when the terminal object is null, the transform contains the
 * complete transform through all the cameras that the mouse pointer
 * is over (for pick operations).
 *
 * <P>The path also holds a list of the cameras traversed by the path.
 * As with the transform, even if the terminal object is null, the
 * camea list contains all the cameras the mouse pointer is over (for
 * pick operations).
 *
 * <P>
 * NOTE: As of Jazz 1.1 {@link #getNode} does not return null but instead
 *       returns the bottom camera node on the picked path when no
 *       object is picked.  {@link #getObject} should be used instead
 *       to determine if an object has been picked.  Consequently,
 *       {@link #getObject} returns null when no object has been picked.
 *       Because {@link #getObject} returns a
 *       {@link edu.umd.cs.jazz.ZSceneGraphObject},
 *       applications can use {@link #getNode} to obtain the lowest picked
 *       node once the application has determined that an object has
 *       been picked.
 *
 *       Note also that {@link #screenToGlobal(java.awt.geom.Point2D)} and
 *       {@link #screenToGlobal(java.awt.geom.Rectangle2D)} now convert to
 *       global coordinates in the lowest camera.  However, this is only
 *       likely to affect an application if it uses internal cameras.
 *
 * <P>
 * <b>Warning:</b> Serialized and ZSerialized objects of this class will not be
 * compatible with future Jazz releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Jazz. A future release of Jazz will provide support for long
 * term persistence.
 *
 * @see ZDrawingSurface#pick
 * @author Jonathan Meyer, Aug 99
 * @author Ben Bederson
 * @author Lance Good
 */
public final class ZSceneGraphPath implements Serializable {
                                // Better to overallocate a bit, and reduce the possibility of dynamic re-allocation
    static private final int INITIAL_PATH_LIST_LENGTH = 10;
    static private final int INITIAL_CAMERA_LIST_LENGTH = 1;
    static private final int INITIAL_TRANSFORMER_LIST_LENGTH = 5;

    private ZRoot root = null;                      // Root of the scenegraph
    private ZCamera topCamera = null;               // The top-level camera.  Set even if the path is empty.
    private ZNode topCameraNode = null;             // The node of the top camera.  Set even if the path is empty.
    private ZList.ZSceneGraphObjectList parents;    // List of objects in path excluding the terminal object

    private ZList.ZCameraList cameras;              // List of cameras in path

    private ZList.ZTransformableList transformers;  // List of objects in path that transform

    private ZSceneGraphObject terminal = null;      // The terminal object
    private AffineTransform transform = null;       // The cumulative transform of the path, including internal cameras
    private AffineTransform cameraTransform = null; // The cumulative transformof the path, up to the bottom camera on the path
    private boolean cameraFound = false;            // True if camera has been found on return up recursive pick calls

    /**
     * Constructs a new ZSceneGraphPath.
     */
    public ZSceneGraphPath() {
        parents = new ZListImpl.ZSceneGraphObjectListImpl(INITIAL_PATH_LIST_LENGTH);
        cameras = new ZListImpl.ZCameraListImpl(INITIAL_CAMERA_LIST_LENGTH);
        transformers = new ZListImpl.ZTransformableListImpl(INITIAL_TRANSFORMER_LIST_LENGTH);
        transform = new AffineTransform();
        cameraTransform = new AffineTransform();
    }

    /**
     * Returns the terminal object in the path. This is
     * either a ZVisualComponent or a ZNode.
     */
    public ZSceneGraphObject getObject() {
        return terminal;
    }

    /**
     * Sets the terminal object in the path.
     */
    public void setObject(ZSceneGraphObject object) {
        terminal = object;
    }

    /**
     * Returns the node associated with the top-level camera in the path.
     * Even if the path is empty, this node is guaranteed to be set.
     */
    public ZNode getTopCameraNode() {
        return topCameraNode;
    }

    /**
     * Sets the node associated with the top-level camera in the path.
     */
    public void setTopCameraNode(ZNode node) {
        topCameraNode = node;
    }

    /**
     * Returns the top-level camera in the path.  This is the first camera that
     * the object was picked within.
     * Even if the path is empty, this camera is guaranteed to be set.
     */
    public ZCamera getTopCamera() {
        return topCamera;
    }

    /**
     * Sets the top-level camera in the path.
     */
    public void setTopCamera(ZCamera camera) {
        topCamera = camera;
    }

    /**
     * Returns the nearest ZNode to the picked object. If the
     * picked object is a ZNode, this simply returns that
     * object. If the picked object is a ZVisualComponent, this
     * returns the first ZNode parent of the component.  If no object
     * is picked, this returns the bottom camera node on the path.
     *
     * NOTE: As of Jazz 1.1 this method does not return null but instead
     *       returns the bottom camera node on the picked path when no
     *       object is picked.  {@link #getObject} should be used instead
     *       to determine if an object has been picked.
     * @return the node.
     */
    public ZNode getNode() {
        if (terminal instanceof ZVisualComponent || terminal instanceof ZHandle || terminal == null) {

            ZSceneGraphObject[] objectsRef = parents.getSceneGraphObjectsReference();
            for (int i = parents.size() - 1; i >= 0; i--) {
                if (objectsRef[i] instanceof ZNode) {
                    return (ZNode) objectsRef[i];
                }
            }
        }

        ZNode node;
        if (terminal == null) {
            node = topCameraNode;
        } else {
                // terminal is either null or a ZNode - safe
                // to simply cast it and return it.
            node = (ZNode)terminal;
        }

        return node;
    }

    /**
     * Returns the nearest ZCamera to the picked object.
     * That is, this returns the last camera on the path.
     * If the path is empty, it returns the top-level camera.
     * @return the camera.
     */
    public ZCamera getCamera() {
        if (terminal instanceof ZCamera) {
            return (ZCamera)terminal;
        }
        if (cameras.size() > 0) {
            return getCamera(cameras.size() - 1);
        } else {
            return topCamera;
        }
    }

    /**
     * Returns the root node for this path.
     */
    public ZRoot getRoot() { return root; }

    /**
     * Sets the root node for this path.
     */
    public void setRoot(ZRoot root) { this.root = root; }

    /**
     * Returns the transform for this path. This is formed by compositing
     * the all the transforms of the scene graph objects in this path.
     * This resulting transform represents the local coordinate system
     * of the terminal element of this path.  If the path is empty,
     * then the transform contains the concatenated transforms of the
     * cameras the mouse pointer is over (for pick operations).
     */
    public AffineTransform getTransform() { return (AffineTransform)transform.clone(); }

    /**
     * Sets the transform for this path.
     * @param tm The new transform
     */
    public void setTransform(AffineTransform tm) { transform.setTransform(tm); }

    /**
     * Returns the camera transform for this path.  This is formed by
     * compositing all the tranforms of the scene graph objects in this path
     * up to the lowest camera.  This resulting transform represents the
     * local coordinate system of the last camera in this path.  If the path
     * is empty, then the transform contains the concatenated transforms of
     * the cameras the mouse pointer is over, minus the final camera view
     * transform (for pick operations).
     */
    public AffineTransform getCameraTransform() {
        return (AffineTransform) cameraTransform.clone();
    }

    /**
     * Returns the camera transform for this camera in this path.  This is
     * formed by compositing all the tranforms of the scene graph objects in
     * this path up to the specified camera.  This resulting transform
     * represents the local coordinate system of the specified camera in this
     * path.   If the camera does not appear in the current
     * path, the transform for the terminal object is returned.
     */
    public AffineTransform getCameraTransform(ZCamera camera) {
        return transformers.collectiveCatTransformUntil(camera);
    }

    /**
     * Sets the camera transform of the bottom camera for this path.
     * @param tm The new camera transform
     */
    public void setCameraTransform(AffineTransform tm) { cameraTransform.setTransform(tm); }

    /**
     * Converts the specified point from screen coordinates to
     * global coordinates through the bottom camera, ie. camera closest
     * to the picked object, of this path.
     * @param pt The pt to be transformed
     */
    public void screenToGlobal(Point2D pt) {
        screenToCamera(pt);
        getCamera().cameraToLocal(pt,null);
    }

    /**
     * Converts the specified dimension from screen coordinates to
     * global coordinates through the bottom camera, ie. camera closest
     * to the picked object, of this path. The input dimension is modified by this method.
     * <P>
     * NOTE: Dimension2D's are abstract. When creating a new Dimension2D for use with Jazz
     * we recoment that you use edu.umd.cs.util.ZDimension instead of java.awt.Dimension.
     * ZDimension uses doubles internally, while java.awt.Dimension uses integers.
     * <p>
     * @param aDimension The dimension to be transformed
     */
    public void screenToGlobal(Dimension2D aDimension) {
        screenToCamera(aDimension);
        getCamera().cameraToLocal(aDimension, null);
    }

    /**
     * Converts the specified point from screen coordinates to
     * global coordinates through the bottom camera, ie. camera closest
     * to the picked object, of this path.
     * @param rect The rect to be transformed
     */
    public void screenToGlobal(Rectangle2D rect) {
        screenToCamera(rect);
        getCamera().cameraToLocal(rect,null);
    }

    /**
     * Converts the specified point from screen coordinates to the local
     * coordinate system of the bottom camera in this path.
     * @param pt The point to be transformed
     */
    public double screenToCamera(Point2D pt) {
        double dz = 0.0;

        try {
            cameraTransform.inverseTransform(pt, pt);
            dz = 1 / Math.max(cameraTransform.getScaleX(), cameraTransform.getScaleY());
        } catch (NoninvertibleTransformException e) {
            throw new ZNoninvertibleTransformException(e);
        }

        return dz;
    }

    /**
     * Converts the specified dimension from screen coordinates to the
     * coordinate system of the bottom camera. The input dimension is modified by this method.
     * <P>
     * NOTE: Dimension2D's are abstract. When creating a new Dimension2D for use with Jazz
     * we recoment that you use edu.umd.cs.util.ZDimension instead of java.awt.Dimension.
     * ZDimension uses doubles internally, while java.awt.Dimension uses integers.
     * <p>
     * @param aDimension The dimension to be transformed
     */
    public void screenToCamera(Dimension2D aDimension) {
        try {
            ZUtil.inverseTransformDimension(aDimension, cameraTransform);
        } catch (NoninvertibleTransformException e) {
            throw new ZNoninvertibleTransformException(e);
        }
    }

    /**
     * Converts the specified point from screen coordinates to the local
     * coordinate system of the bottom camera in this path.
     * @param rect The rect to be transformed
     */
    public double screenToCamera(Rectangle2D rect) {
        try {
            return ZUtil.inverseTransformRectangle(rect, cameraTransform);
        } catch (NoninvertibleTransformException e) {
            throw new ZNoninvertibleTransformException(e);
        }
    }

    /**
     * Converts the specified point from screen coordinates to the local
     * coordinate system of the specified camera, if the camera appears
     * on the current path.  If the camera does not appear in the current
     * path, the point is transformed to local coordinates for the terminal
     * object.
     * @param pt The point to be transformed
     * @param camera The camera for which the point is transformed
     */
    public double screenToCamera(Point2D pt, ZCamera camera) {
        ZCamera bottomCamera = getCamera();

        if (camera == bottomCamera) return screenToCamera(pt);

        double dz = 0.0;
        AffineTransform tmpTransform = transformers.collectiveCatTransformUntil(camera);

        try {
            tmpTransform.inverseTransform(pt, pt);
            dz = 1 / Math.max(tmpTransform.getScaleX(), tmpTransform.getScaleY());
        } catch (NoninvertibleTransformException e) {
            throw new ZNoninvertibleTransformException(e);
        }

        return dz;
    }

    /**
     * Converts the specified dimension from screen coordinates to the
     * coordinate system of the specified camera. The input dimension is modified by this method.
     * <P>
     * NOTE: Dimension2D's are abstract. When creating a new Dimension2D for use with Jazz
     * we recoment that you use edu.umd.cs.util.ZDimension instead of java.awt.Dimension.
     * ZDimension uses doubles internally, while java.awt.Dimension uses integers.
     * <p>
     * @param aDimension The dimension to be transformed
     * @param aCamera The camera for which the point is transformed
     */
    public void screenToCamera(Dimension2D aDimension, ZCamera aCamera) {
        ZCamera bottomCamera = getCamera();

        if (aCamera == bottomCamera) {
            screenToCamera(aDimension);
            return;
        }

        AffineTransform tmpTransform = transformers.collectiveCatTransformUntil(aCamera);
        try {
            ZUtil.inverseTransformDimension(aDimension, tmpTransform);
        } catch (NoninvertibleTransformException e) {
            throw new ZNoninvertibleTransformException(e);
        }
    }

    /**
     * Converts the specified point from screen coordinates to the local
     * coordinate system of the specified camera, if the camera appears
     * on the current path.  If the camera does not appear in the current
     * path, the rectangle is transformed to local coordinates for the
     * terminal object.
     * @param rect The rect to be transformed
     * @param camera The camera for which the point is transformed
     */
    public double screenToCamera(Rectangle2D rect, ZCamera camera) {
        ZCamera bottomCamera = getCamera();

        if (camera == bottomCamera) return screenToCamera(rect);

        AffineTransform tmpTransform = transformers.collectiveCatTransformUntil(camera);

        try {
            return ZUtil.inverseTransformRectangle(rect, tmpTransform);
        } catch (NoninvertibleTransformException e) {
            throw new ZNoninvertibleTransformException(e);
        }
    }


    /**
     * Converts the specified point from camera coordinates to the
     * coordinate system of the screen, if the camera appears
     * on the current path.  If the camera does not appear in the current
     * path, the point is transformed from the local coordinates for the
     * terminal object.
     * @param pt The point to be transformed
     * @param camera The camera for which the point is transformed
     */
    public double cameraToScreen(Point2D pt, ZCamera camera) {
        AffineTransform tmpTransform = transformers.collectiveCatTransformUntil(camera);

        tmpTransform.transform(pt,pt);

        return Math.max(tmpTransform.getScaleX(), tmpTransform.getScaleY());
    }

    /**
     * Converts the specified dimension from camera coordinates to the
     * coordinate system of the screen, if the camera appears
     * on the current path.  If the camera does not appear in the current
     * path, the dimension is transformed from the local coordinates for the
     * terminal object.
     * @param dimension The dimension to be transformed
     * @param camera The camera for which the point is transformed
     */
    public double cameraToScreen(Dimension2D dimension, ZCamera camera) {
        AffineTransform tmpTransform = transformers.collectiveCatTransformUntil(camera);

        return ZUtil.transformDimension(dimension,tmpTransform);
    }



    /**
     * Converts the specified rectangle from camera coordinates to the
     * coordinate system of the screen, if the camera appears
     * on the current path.  If the camera does not appear in the current
     * path, the rectangle is transformed from the local coordinates for the
     * terminal object.
     * @param rect The rectangle to be transformed
     * @param camera The camera for which the point is transformed
     */
    public double cameraToScreen(Rectangle2D rect, ZCamera camera) {
        AffineTransform tmpTransform = transformers.collectiveCatTransformUntil(camera);

        ZTransformGroup.transform(rect,tmpTransform);

        return Math.max(tmpTransform.getScaleX(), tmpTransform.getScaleY());
    }

    /**
     * Converts the specified point from screen coordinates to the
     * local coordinate system of the terminal scene graph object in
     * this path.
     * @param pt The pt to be transformed
     */
    public void screenToLocal(Point2D pt) {
        try {
            transform.inverseTransform(pt, pt);
        } catch (NoninvertibleTransformException e) {
            throw new ZNoninvertibleTransformException(e);
        }
    }

    /**
     * Converts the specified dimension from screen coordinates to the
     * local coordinate system of the terminal scene graph object in
     * this path. The input dimension is modified by this method.
     * <P>
     * NOTE: Dimension2D's are abstract. When creating a new Dimension2D for use with Jazz
     * we recoment that you use edu.umd.cs.util.ZDimension instead of java.awt.Dimension.
     * ZDimension uses doubles internally, while java.awt.Dimension uses integers.
     * <p>
     * @param aDimension The dimension to be transformed
     */
    public void screenToLocal(Dimension2D aDimension) {
        try {
            ZUtil.inverseTransformDimension(aDimension, transform);
        } catch (NoninvertibleTransformException e) {
            throw new ZNoninvertibleTransformException(e);
        }
    }

    /**
     * Converts the specified rectangle from screen coordinates to the
     * local coordinate system of the terminal scene graph object in
     * this path.
     * @param rect The rect to be transformed
     */
    public void screenToLocal(Rectangle2D rect) {
        try {
            ZUtil.inverseTransformRectangle(rect, transform);
        } catch (NoninvertibleTransformException e) {
            throw new ZNoninvertibleTransformException(e);
        }
    }

    /**
     * Returns the number of internal ZSceneGraphObjects between the root and the terminal object.
     */
    public int getNumParents() { return parents.size(); }

    /**
     * Returns the i'th scene graph object between the root and the terminal object. The parent
     * at position 0 is closest to the root, and the parent at the last
     * position is closest to the terminal object.
     * @param i The index of the path element to return
     * @return The scene graph object
     */
    public ZSceneGraphObject getParent(int i) {
        return (ZSceneGraphObject) parents.get(i);
    }

    /**
     * Adds a node to the end of the list of parent nodes.
     * This is used during picking
     * to construct a path from the root to the picked object.
     * @param sgo The scene graph object to be added to the path
     */
    public void push(ZSceneGraphObject sgo) {
        parents.add(sgo);
    }

    /**
     * Removes a node (and any nodes after it) from the list of parent nodes.
     * This is used during picking
     * to construct a path from the root to the picked object.
     * @param sgo The scene graph object to be removed from the path
     */
    public void pop(ZSceneGraphObject sgo) {
        parents.pop(sgo);
    }

    /**
     * Trims the capacity of the array that stores the parents list points to
     * the actual number of points.  Normally, the parents list arrays can be
     * slightly larger than the number of points in the parents list.
     * An application can use this operation to minimize the storage of a
     * parents list.
     */
    public void trimToSize() {
        parents.trimToSize();
        trimTransformersToSize();
        trimCamerasToSize();
    }

    /**
     * Returns the number of internal cameras between the root and the terminal object.
     */
    public int getNumCameras() { return cameras.size(); }

    /**
     * Returns the i'th camera between the root and the terminal object. The camera
     * at position 0 is closest to the root, and the camera at the last
     * position is closest to the terminal object.
     * @param i The index of the camera to return
     * @return The camera
     */
    public ZCamera getCamera(int i) {
        return (ZCamera) cameras.get(i);
    }

    /**
     * Adds a node to the end of the list of camera nodes.
     * This is used during picking
     * to construct a path from the root to the picked object.
     * @param camera The camera to be added to the path
     */
    public void pushCamera(ZCamera camera) {
        cameras.add(camera);
    }

    /**
     * Removes a camera (and any cameras after it) from the list of camera nodes.
     * This is used
     * to construct a path from the root to the picked object.
     * @param camera The camera to be removed from the path
     */
    public void popCamera(ZCamera camera) {
        cameras.pop(camera);
    }

    /**
     * Trims the capacity of the array that stores the cameras to
     * the actual number of points.  Normally, the cameras array can be
     * slightly larger than the number of cameras in the list.
     * An application can use this operation to minimize the storage of the
     * camera list.
     */
    public void trimCamerasToSize() {
        cameras.trimToSize();
    }

    /**
     * Internal method.
     * Adds a transformer to the end of the list of transformer nodes.
     * This is used to support updateTransform().
     * @param transformer The transformer to be added to the path
     */
    public void pushTransformer(ZTransformable transformer) {
        transformers.add(transformer);
    }

    /**
     * Removes a transformer (and any transformers after it) from the list of transformer nodes.
     * This is used to support updateTransform().
     * @param transformer The transformer to be removed from the path
     */
    public void popTransformer(ZTransformable transformer) {
        transformers.pop(transformer);
    }

    /**
     * Percolate this mouse event up the scene graph path until the event
     * is consumed or the top of the path is reached
     */
    public void processMouseEvent(ZMouseEvent e) {
        if (terminal != null) {
            terminal.processMouseEvent(e);
            if (e.isConsumed()) return;
        }

        ZSceneGraphObject[] parentsRef = parents.getSceneGraphObjectsReference();
        for (int i = parents.size() - 1; i >= 0; i--) {
            parentsRef[i].processMouseEvent(e);

            if (e.isConsumed())
                return;
        }

        getTopCameraNode().processMouseEvent(e);
    }

    /**
     * Trims the capacity of the array that stores the transformers to
     * the actual number of points.  Normally, the transformers array can be
     * slightly larger than the number of transformers in the list.
     * An application can use this operation to minimize the storage of the
     * transformer list.
     */
    public void trimTransformersToSize() {
        transformers.trimToSize();
    }

    /**
     * Internal method.  This is used by pick on the way back up
     * the recursive pick checking to say that a camera has been gone
     * through, and thus transforms should be kept from this point up.
     */
    public void setCameraFound(boolean cf) {
        cameraFound = cf;
    }

    /**
     * Internal method.  This is used by pick on the way back up
     * the recursive pick checking to say that a camera has been gone
     * through, and thus transforms should be kept from this point up.
     */
    public boolean getCameraFound() {
        return cameraFound;
    }

    /**
     * Update the path's transform.  This will modify the transform by
     * going through the path and rebuilding the transform based on
     * current values of ZCamera and ZTransformGroup objects.
     */
    public void updateTransform() {
        ZTransformable obj;
        ZCamera bottomCamera = getCamera();

        AffineTransform tmpTransform = new AffineTransform();
        AffineTransform catTransform = new AffineTransform();

        // XXX this could probably be using transformers.collectiveCatTransform()
        ZTransformable[] transformersRef = transformers.getTransformablesReference();
        for (int i=0; i<transformers.size(); i++) {
            obj = transformersRef[i];

            double[] matrix = new double[6];
            transformersRef[i].getMatrix(matrix);
            catTransform.setTransform(matrix[0],matrix[1],matrix[2],
                                      matrix[3],matrix[4],matrix[5]);
            if (bottomCamera != null && obj == bottomCamera) {
                setCameraTransform((AffineTransform)tmpTransform.clone());
            }
            tmpTransform.concatenate(catTransform);
        }

        setTransform(tmpTransform);
    }

    /**
     * Returns a string description of this path useful for debugging.
     * @return the string
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("ZSceneGraphPath[transform=" + transform
            + "; root=" + getRoot() + "; object=" + getObject() + "; parents=");
        for (int i = 0; i < parents.size(); i++) {
            sb.append(parents.get(i));
            if (i < parents.size() - 1) sb.append(", ");
        }
        sb.append("; cameras=");
        for (int i = 0; i < cameras.size(); i++) {
            sb.append(cameras.get(i));
            if (i < cameras.size() - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        trimToSize();   // Remove extra unused array elements
        out.defaultWriteObject();
    }
}