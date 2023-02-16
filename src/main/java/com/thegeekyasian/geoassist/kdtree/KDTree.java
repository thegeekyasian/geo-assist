package com.thegeekyasian.geoassist.kdtree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.thegeekyasian.geoassist.core.GeoAssistException;

/**
 * @author thegeekyasian
 *
 * This is an implementation of a two-dimensional KD-Tree that enables
 * efficient range searching and lookup for point data. Points can be
 * dynamically inserted into the tree to build it. The KD-Tree supports
 * point equality queries and range queries, providing fast retrieval of the data.
 *
 * It is important to note that the structure of a KD-Tree is dependent on the
 * order insertion. If points are inserted in a coherent manner
 * (such as being monotonic in one or both dimensions), the tree may become unbalanced.
 * This can greatly impact the efficiency of queries.
 *
 * @param <T>
 *     describes the identifier of the K-d Tree Object,
 *     that is being inserted in the tree. For example ID or UUID of the Object.
 *
 * @param <O>
 *     describes the object that is inserted in the tree.
 *     For example Vendor, Restaurant, Franchise, etc.
 */
public class KDTree<T, O> implements Serializable {

	private static final long serialVersionUID = 5020274653621814765L;

	private KDTreeNode<T, O> root;

	private final Map<T, KDTreeNode<T, O>> map = new HashMap<>();

	/**
	 * Creates a new instance of KDTree.
	 * */
	public KDTree() {
		this.root = null;
	}


	/**
	 * Inserts the provided KDTreeObject on the tree,
	 * based on the provided Point (coordinates).
	 *
	 * @param kdTreeObject KDTreeObject holds the custom object,
	 * identifier along with the Point (latitude/longitude coordinates) of the object.
	 * */
	public void insert(KDTreeObject<T, O> kdTreeObject) {
		insertObject(kdTreeObject);
	}

	private void insertObject(KDTreeObject<T, O> object) {

		if (root == null) {
			KDTreeNode<T, O> node = new KDTreeNode<>(object, null);
			root = node;
			map.put(object.getId(), node);
			return;
		}

		KDTreeNode<T, O> current = root;
		KDTreeNode<T, O> parent = root;
		Point point = object.getPoint();
		double[] coordinates = { point.getLatitude(), point.getLongitude() };
		boolean isLatitude = true;
		boolean isLessThanCurrentValue = true;

		while (current != null) {
			// Store the current node as the parent for the next iteration
			parent = current;

			// Determine whether to move to the left or right subtree based on the comparison of the current dimension's coordinate value
			isLessThanCurrentValue = Double.compare(coordinates[isLatitude ? 0 : 1], current.value(isLatitude)) < 0;
			current = isLessThanCurrentValue ? current.getLeft() : current.getRight();

			// If the next node in the tree has the same coordinate value along the current dimension, move to the next dimension and repeat the process
			if (current != null && Double.compare(coordinates[isLatitude ? 0 : 1], current.value(isLatitude)) == 0) {
				isLatitude = !isLatitude;
				isLessThanCurrentValue = Double.compare(coordinates[isLatitude ? 0 : 1], current.value(isLatitude)) < 0;
				current = isLessThanCurrentValue ? current.getLeft() : current.getRight();

				// If the next node in the tree has the same coordinate value along the alternate dimension, it already exists in the tree and should be returned
				if (current != null && Double.compare(coordinates[isLatitude ? 0 : 1], current.value(isLatitude)) == 0) {
					return;
				}
			}

			// Move to the alternate dimension and continue the iteration
			isLatitude = !isLatitude;
		}

		KDTreeNode<T, O> node = new KDTreeNode<>(object, parent);
		map.put(object.getId(), node);

		if (isLessThanCurrentValue) {
			parent.setLeft(node);
			return;
		}
		parent.setRight(node);
	}

	/**
	 * Finds the nearest neighbor to a given point
	 * with the provided distance in the k-d tree.
	 *
	 * @param point Location (latitude/longitude) coordinates
	 * of the reference point to find the nearest neighbors for.
	 *
	 * @param distance Maximum distance to find the nearest neighbors in.
	 *
	 * @return Returns the list of KDTreeObjects
	 * that are nearby the provided point for the provided distance.
	 * */
	public List<KDTreeObject<T, O>> findNearestNeighbor(Point point, double distance) {
		// Initialize a list to store the closest points
		List<KDTreeObject<T, O>> closestPoints = new ArrayList<>();
		// Call the recursive helper method with the root node, point, distance, and closestPoints list
		findNearestNeighbor(root, point, distance, closestPoints, 0);
		// Return the list of closest points
		return closestPoints;
	}

	private void findNearestNeighbor(KDTreeNode<T, O> node, Point point, double distance, List<KDTreeObject<T, O>> closestPoints, int depth) {
		if (node == null) {
			return;
		}

		double currentDistance = getHaversineDistance(node.getKdTreeObject().getPoint(), point);

		if (currentDistance <= distance) {
			closestPoints.add(node.getKdTreeObject());
		}

		// Determine which dimension to compare based on the depth of the current node in the K-d tree
		int dim = depth % 2;

		// If the `dim`-th coordinate of the input point is less than the `dim`-th coordinate of the current node's point,
		// continue searching in the left subtree and check if the absolute difference between the `dim`-th coordinates
		// is less than or equal to the maximum distance
		if (dim == 0) {
			if (point.getLatitude() < node.getKdTreeObject().getPoint().getLatitude()) {
				findNearestNeighbor(node.getLeft(), point, distance, closestPoints, depth + 1);

				// check if the difference in latitudes is less than or equal to the provided distance
				// if so, also search the right subtree
				if (node.getRight() != null && Math.abs(node.getKdTreeObject().getPoint().getLatitude() - point.getLatitude()) <= distance) {
					findNearestNeighbor(node.getRight(), point, distance, closestPoints, depth + 1);
				}
			}
			else {
				// If the `dim`-th coordinate of the input point is greater than or equal to the `dim`-th coordinate of the current node's point,
				// continue searching in the right subtree and check if the absolute difference between the `dim`-th coordinates
				// is less than or equal to the maximum distance
				findNearestNeighbor(node.getRight(), point, distance, closestPoints, depth + 1);

				// check if the difference in latitudes is less than or equal to the provided distance
				// if so, also search the left subtree
				if (node.getLeft() != null && Math.abs(node.getKdTreeObject().getPoint().getLatitude() - point.getLatitude()) <= distance) {
					findNearestNeighbor(node.getLeft(), point, distance, closestPoints, depth + 1);
				}
			}
		}
		else {
			// using the same logic as above but for longitude i.e. the second dimension.
			if (point.getLongitude() < node.getKdTreeObject().getPoint().getLongitude()) {
				findNearestNeighbor(node.getLeft(), point, distance, closestPoints, depth + 1);
				if (node.getRight() != null && Math.abs(node.getKdTreeObject().getPoint().getLongitude() - point.getLongitude()) <= distance) {
					findNearestNeighbor(node.getRight(), point, distance, closestPoints, depth + 1);
				}
			}
			else {
				findNearestNeighbor(node.getRight(), point, distance, closestPoints, depth + 1);
				if (node.getLeft() != null && Math.abs(node.getKdTreeObject().getPoint().getLongitude() - point.getLongitude()) <= distance) {
					findNearestNeighbor(node.getLeft(), point, distance, closestPoints, depth + 1);
				}
			}
		}
	}

	/**
	 * Returns the size i.e. number of objects present in the tree at a provided moment
	 *
	 * @return an integer value of size is returned.
	 * */
	public int getSize() {
		return map.size();
	}

	/**
	 * Find the KDTreeObject for the provided ID.
	 *
	 * @param id ID of the KDTreeObject that you are looking.
	 *
	 * @return The method returns the KDTreeObject for the provided ID.
	 * If not found, the method returns `null` otherwise.
	 * */
	public KDTreeObject<T, O> getById(T id) {
		return Optional.ofNullable(map.get(id))
				.map(KDTreeNode::getKdTreeObject)
				.orElse(null);
	}

	/**
	 * Updates the custom data by replacing it in the KDTreeObject with the provided data.
	 *
	 * @param id ID of the custom object that is desired to be deleted.
	 *
	 * @param data Custom data to be updated in the KDTreeObject of provided ID.
	 *
	 * @throws GeoAssistException is thrown an object with provided ID is not found.
	 * */
	public void update(T id, O data) {
		Optional.ofNullable(map.get(id))
				.map(node -> {
					node.getKdTreeObject().setData(data);
					return node.getKdTreeObject();
				}).orElseThrow(() -> new GeoAssistException("No object found for provided ID"));
	}

	/**
	 * Deletes the object with the provided custom ID.
	 *
	 * @param id ID of the custom object that is desired to be deleted.
	 *
	 * @return Returns a boolean flag, `true` if the object is successfully deleted
	 * and `false` otherwise.
	 * */
	public boolean delete(T id) {
		return Optional.ofNullable(map.get(id)).map(node -> {
			deleteNode(node);
			map.remove(id);
			return true;
		}).orElse(false);
	}

	private void deleteNode(KDTreeNode<T, O> node) {
		if (node.getLeft() == null && node.getRight() == null) {
			if (node.getParent().getLeft() == node) {
				node.getParent().setLeft(null);
			}
			else {
				node.getParent().setRight(null);
			}
		}
		else if (node.getLeft() == null) {
			if (node.getParent().getLeft() == node) {
				node.getParent().setLeft(node.getRight());
			}
			else {
				node.getParent().setRight(node.getRight());
			}
			node.getRight().setParent(node.getParent());
		}
		else if (node.getRight() == null) {
			if (node.getParent().getLeft() == node) {
				node.getParent().setLeft(node.getLeft());
			}
			else {
				node.getParent().setRight(node.getLeft());
			}
			node.getLeft().setParent(node.getParent());
		}
		else {
			KDTreeNode<T, O> minNode = node.getRight();
			while (minNode.getLeft() != null) {
				minNode = minNode.getLeft();
			}
			node.setKdTreeObject(minNode.getKdTreeObject());
			deleteNode(minNode);
		}
	}

	private double getHaversineDistance(Point point1, Point point2) {
		double lat1 = point1.getLatitude();
		double lon1 = point1.getLongitude();
		double lat2 = point2.getLatitude();
		double lon2 = point2.getLongitude();

		double R = 6371; // radius of Earth in kilometers
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);

		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
				Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return R * c;
	}
}