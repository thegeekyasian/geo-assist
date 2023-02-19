package com.thegeekyasian.geoassist.kdtree;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

import static java.util.Objects.isNull;

/**
 * @author thegeekyasian
 */
public class KDTreeNode<T, O> {

    @Nonnull
    private KDTreeObject<T, O> kdTreeObject;

    @Nullable
    private KDTreeNode<T, O> parent;

    @Nullable
    private KDTreeNode<T, O> left;

    @Nullable
    private KDTreeNode<T, O> right;

    private int depth;

    @Nullable
    public KDTreeNode<T, O> getParent() {
        return parent;
    }

    public void setParent(@Nullable final KDTreeNode<T, O> parent) {
        this.parent = parent;
    }

    @Nullable
    public KDTreeNode<T, O> getLeft() {
        return left;
    }

    public void setLeft(@Nullable final KDTreeNode<T, O> left) {
        this.left = left;
    }

    @Nullable
    public KDTreeNode<T, O> getRight() {
        return right;
    }

    public void setRight(@Nullable final KDTreeNode<T, O> right) {
        this.right = right;
    }

    public KDTreeNode(@Nonnull final KDTreeObject<T, O> kdTreeObject,
                      @Nullable final KDTreeNode<T, O> parent,
                      final int depth) {

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

    @Nonnull
    public KDTreeObject<T, O> getKdTreeObject() {
        return kdTreeObject;
    }

    public void setKdTreeObject(@Nonnull final KDTreeObject<T, O> kdTreeObject) {
        this.kdTreeObject = kdTreeObject;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(final int depth) {
        this.depth = depth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (isNull(o) || getClass() != o.getClass()) {
            return false;
        }
        final KDTreeNode<?, ?> that = (KDTreeNode<?, ?>) o;

        return depth == that.depth
                && kdTreeObject.equals(that.kdTreeObject)
                && Objects.equals(parent, that.parent)
                && Objects.equals(left, that.left)
                && Objects.equals(right, that.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kdTreeObject, parent, left, right, depth);
    }

    @Override
    public String toString() {
        return "KDTreeNode{" +
                "kdTreeObject=" + kdTreeObject +
                ", depth=" + depth +
                '}';
    }
}
