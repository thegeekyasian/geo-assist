package com.thegeekyasian.geoassist.kdtree;

/**
 * The KDTreeNode class represents a node in a KD-Tree, which is a binary search tree that is optimized for searching
 * in k-dimensional space. Each node in the tree represents a point in the k-dimensional space.
 *
 * @param <T> the type of object used to represent the point in the k-dimensional space.
 *
 * @param <O> the type of object associated with the point in the k-dimensional space.
 *
 * @author The Geeky Asian
 */
public class KDTreeNode<T, O> {

	private KDTreeObject<T, O> kdTreeObject;

	private KDTreeNode<T, O> parent;

	private KDTreeNode<T, O> left;

	private KDTreeNode<T, O> right;

	public KDTreeNode<T, O> getParent() {
		return this.parent;
	}

	public void setParent(KDTreeNode<T, O> parent) {
		this.parent = parent;
	}

	public KDTreeNode<T, O> getLeft() {
		return this.left;
	}

	public void setLeft(KDTreeNode<T, O> left) {
		this.left = left;
	}

	public KDTreeNode<T, O> getRight() {
		return this.right;
	}

	public void setRight(KDTreeNode<T, O> right) {
		this.right = right;
	}

	public KDTreeNode(KDTreeObject<T, O> kdTreeObject, KDTreeNode<T, O> parent) {
		this.kdTreeObject = kdTreeObject;
		this.parent = parent;
		this.left = null;
		this.right = null;
	}

	public double value(boolean isLatitude) {
		if (isLatitude) {
			return getKdTreeObject().getPoint().getLatitude();
		}
		return getKdTreeObject().getPoint().getLongitude();
	}

	public KDTreeObject<T, O> getKdTreeObject() {
		return this.kdTreeObject;
	}

	public void setKdTreeObject(KDTreeObject<T, O> kdTreeObject) {
		this.kdTreeObject = kdTreeObject;
	}
}
