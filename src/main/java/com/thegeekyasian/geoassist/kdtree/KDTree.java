package com.thegeekyasian.geoassist.kdtree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.thegeekyasian.geoassist.core.GeoAssistException;

/**
 * @author thegeekyasian
 *
 * @param <T>
 *     describes the identifier of the K-d Tree Object,
 *     that is being inserted in the tree. For example ID or UUID of the Object.
 *
 * @param <O>
 *     describes the object that is inserted in the tree.
 *     For example Vendor, Restaurant, Franchise, etc.
 */
public class KDTree<T, O> {
	private Node root;

	private final Map<T, Node> map = new HashMap<>();

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
		root = insert(root, kdTreeObject, null, 0);
	}

	private Node insert(Node current, KDTreeObject<T, O> kdTreeObject, Node parent, int level) {
		// base case: if current node is null, return new node with point
		if (current == null) {
			Node node = new Node(kdTreeObject, parent);
			map.put(kdTreeObject.getId(), node);
			return node;
		}
		// compare latitude if level is even, else compare longitude
		if (level % 2 == 0) {
			if (kdTreeObject.getPoint().getLatitude() < current.getKdTreeObject().getPoint().getLatitude()) {
				current.left = insert(current.left, kdTreeObject, current, level + 1);
			}
			else {
				current.right = insert(current.right, kdTreeObject, current, level + 1);
			}
		}
		else {
			if (kdTreeObject.getPoint().getLongitude() < current.getKdTreeObject().getPoint().getLongitude()) {
				current.left = insert(current.left, kdTreeObject, current, level + 1);
			}
			else {
				current.right = insert(current.right, kdTreeObject, current, level + 1);
			}
		}
		return current;
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
		findNearestNeighbor(root, point, distance, closestPoints);
		// Return the list of closest points
		return closestPoints;
	}

	private void findNearestNeighbor(Node node, Point point, double distance, List<KDTreeObject<T, O>> closestPoints) {
		if (node == null) {
			return;
		}

		// calculate the haversine distance between the current node's point and the given point
		double currentDistance = getHaversineDistance(node.getKdTreeObject().getPoint(), point);

		// if the current distance is less than or equal to the provided distance
		// add the current node's point to the closestPoints list
		if (currentDistance <= distance) {
			closestPoints.add(node.getKdTreeObject());
		}

		// compare the latitude of the given point and the current node's point
		// if the latitude of the given point is less than the current node's point, search the left subtree
		if (point.getLatitude() < node.getKdTreeObject().getPoint().getLatitude()) {
			findNearestNeighbor(node.left, point, distance, closestPoints);

			// check if the difference in latitudes is less than or equal to the provided distance
			// if so, also search the right subtree
			if (Math.abs(point.getLatitude() - node.getKdTreeObject().getPoint().getLatitude()) <= distance) {
				findNearestNeighbor(node.right, point, distance, closestPoints);
			}
		}
		// if the latitude of the given point is greater than or equal to the current node's point, search the right subtree
		else {
			findNearestNeighbor(node.right, point, distance, closestPoints);

			// check if the difference in latitudes is less than or equal to the provided distance
			// if so, also search the left subtree
			if (Math.abs(point.getLatitude() - node.getKdTreeObject().getPoint().getLatitude()) <= distance) {
				findNearestNeighbor(node.left, point, distance, closestPoints);
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
				.map(Node::getKdTreeObject)
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

	private void deleteNode(Node node) {
		if (node.left == null && node.right == null) {
			if (node.parent.left == node) {
				node.parent.left = null;
			}
			else {
				node.parent.right = null;
			}
		}
		else if (node.left == null) {
			if (node.parent.left == node) {
				node.parent.left = node.right;
			}
			else {
				node.parent.right = node.right;
			}
			node.right.parent = node.parent;
		}
		else if (node.right == null) {
			if (node.parent.left == node) {
				node.parent.left = node.left;
			}
			else {
				node.parent.right = node.left;
			}
			node.left.parent = node.parent;
		}
		else {
			Node minNode = node.right;
			while (minNode.left != null) {
				minNode = minNode.left;
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

	private class Node {
		private KDTreeObject<T, O> kdTreeObject;

		private Node parent;

		private Node left;

		private Node right;

		Node(KDTreeObject<T, O> kdTreeObject, Node parent) {
			this.kdTreeObject = kdTreeObject;
			this.parent = parent;
			this.left = null;
			this.right = null;
		}

		public KDTreeObject<T, O> getKdTreeObject() {
			return kdTreeObject;
		}

		public void setKdTreeObject(KDTreeObject<T, O> kdTreeObject) {
			this.kdTreeObject = kdTreeObject;
		}
	}

}