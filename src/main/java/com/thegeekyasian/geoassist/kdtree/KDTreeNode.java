package com.thegeekyasian.geoassist.kdtree;

/**
 * @author thegeekyasian
 */
public class KDTreeNode<T, O> {

    private KDTreeObject<T, O> kdTreeObject;

    private KDTreeNode<T, O> parent;

    private KDTreeNode<T, O> left;

    private KDTreeNode<T, O> right;

    private int depth;

    public KDTreeNode<T, O> getParent() {
        return parent;
    }

    public void setParent(final KDTreeNode<T, O> parent) {
        this.parent = parent;
    }

    public KDTreeNode<T, O> getLeft() {
        return left;
    }

    public void setLeft(final KDTreeNode<T, O> left) {
        this.left = left;
    }

    public KDTreeNode<T, O> getRight() {
        return right;
    }

    public void setRight(final KDTreeNode<T, O> right) {
        this.right = right;
    }

    public KDTreeNode(final KDTreeObject<T, O> kdTreeObject, final int depth, final KDTreeNode<T, O> parent) {
        this.kdTreeObject = kdTreeObject;
        this.parent = parent;
        this.left = null;
        this.right = null;
        this.depth = depth;
    }

    public double value(final boolean isLatitude) {
        if (isLatitude) {
            return getKdTreeObject().getPoint().getLatitude();
        }
        return getKdTreeObject().getPoint().getLongitude();
    }

    public KDTreeObject<T, O> getKdTreeObject() {
        return kdTreeObject;
    }

    public void setKdTreeObject(final KDTreeObject<T, O> kdTreeObject) {
        this.kdTreeObject = kdTreeObject;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(final int depth) {
        this.depth = depth;
    }
}
