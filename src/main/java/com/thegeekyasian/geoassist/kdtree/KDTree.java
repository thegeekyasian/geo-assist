package com.thegeekyasian.geoassist.kdtree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.thegeekyasian.geoassist.core.GeoAssistException;
import com.thegeekyasian.geoassist.kdtree.geometry.BoundingBox;
import com.thegeekyasian.geoassist.kdtree.geometry.Point;

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

	private final Map<T, KDTreeNode<T, O>> map = new ConcurrentHashMap<>();

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
	 * <p>
	 *     Searches the k-d tree for all nodes whose
	 *     coordinates fall within the given bounding box.
	 * </p>
	 *
	 * @param boundingBox the bounding box that defines the range to search within
	 * @return a list of KDTreeObject whose coordinates fall within the bounding box
	 */
	public List<KDTreeObject<T, O>> findInRange(BoundingBox boundingBox) {
		List<KDTreeObject<T, O>> result = new ArrayList<>();
		search(root, boundingBox, true, result);
		return result;
	}

	private void search(KDTreeNode<T, O> node, BoundingBox boundingBox, boolean isLatitude,
			List<KDTreeObject<T, O>> result) {
		if (node == null) {
			return;
		}
		KDTreeObject<T, O> kdTreeObject = node.getKdTreeObject();
		double latitude = kdTreeObject.getPoint().getLatitude();
		double longitude = kdTreeObject.getPoint().getLongitude();
		Point lowerPoint = boundingBox.getLowerPoint();
		Point upperPoint = boundingBox.getUpperPoint();
		if (latitude >= lowerPoint.getLatitude() && latitude <= upperPoint.getLatitude()
				&& longitude >= lowerPoint.getLongitude() && longitude <= upperPoint.getLongitude()) {
			result.add(kdTreeObject);
		}
		if (isLatitude) {
			if (latitude >= lowerPoint.getLatitude() && latitude <= upperPoint.getLatitude()) {
				search(node.getLeft(), boundingBox, false, result);
				search(node.getRight(), boundingBox, false, result);
			}
			else if (latitude < lowerPoint.getLatitude()) {
				search(node.getRight(), boundingBox, false, result);
			}
			else {
				search(node.getLeft(), boundingBox, false, result);
			}
		}
		else {
			if (longitude >= lowerPoint.getLongitude() && longitude <= upperPoint.getLongitude()) {
				search(node.getLeft(), boundingBox, true, result);
				search(node.getRight(), boundingBox, true, result);
			}
			else if (longitude < lowerPoint.getLongitude()) {
				search(node.getRight(), boundingBox, true, result);
			}
			else {
				search(node.getLeft(), boundingBox, true, result);
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

	/**
	 *
	 * <p>
	 *     Returns whether the KDTree is balanced. A KDTree is considered balanced if
	 *     the depth of any two leaf nodes differs by no more than 1. The method
	 *     determines if the tree is balanced by recursively traversing the tree and
	 *     checking the depths of each subtree.
	 * </p>
	 *
	 * <p>
	 *     The method has a time complexity of O(N) in the best case scenario,
	 *     where the tree is perfectly balanced, and O(N log N) in the worst case scenario,
	 *     where the tree is completely unbalanced.
	 * </p>
	 *
	 * @return true if the tree is balanced, false otherwise.
	 */
	public boolean isBalanced() {
		return isBalanced(root) != -1;
	}

	private int isBalanced(KDTreeNode<T, O> node) {
		// If the current node is null, return 0.
		if (node == null) {
			return 0;
		}

		// Recursively call isBalanced on the left subtree, obtaining its depth.
		int leftDepth = isBalanced(node.getLeft());

		// If the left subtree is unbalanced (depth of -1), return -1 to indicate that the current subtree is also unbalanced.
		if (leftDepth == -1) {
			return -1;
		}

		// Recursively call isBalanced on the right subtree, obtaining its depth.
		int rightDepth = isBalanced(node.getRight());

		// If the right subtree is unbalanced (depth of -1), return -1 to indicate that the current subtree is also unbalanced.
		if (rightDepth == -1) {
			return -1;
		}

		// Check if the current subtree is balanced by comparing the depths of the left and right subtrees.
		if (Math.abs(leftDepth - rightDepth) > 1) {
			// If the current subtree is unbalanced, return -1 to indicate that the current subtree is unbalanced.
			return -1;
		}

		// If the current subtree is balanced, return its depth.
		return Math.max(leftDepth, rightDepth) + 1;
	}

	/**
	 * <p>
	 *     Balances the k-d tree to ensure efficient query performance.
	 *     A k-d tree can become unbalanced if the order of
	 *     point insertion is coherent (e.g. monotonic in one or both dimensions).
	 *     An unbalanced tree may have a much deeper
	 *     depth than a balanced tree, which can lead to slower query performance.
	 *     This method reorders the nodes in the tree to improve balance and reduce depth,
	 *     improving query performance.
	 *     This method does not affect the values or coordinates of the nodes in the tree.
	 *</p>
	 * <p>
	 *     This method should be called after all nodes have been inserted into the tree,
	 *     and can be called multiple times if the tree becomes unbalanced after
	 *     subsequent insertions or removals.
	 *     This method has a time complexity of O(N log N),
	 *     where N is the number of nodes in the tree.
	 * </p>
	 *
	 */
	public void balance() {
		root = buildBalancedTree(getNodes(), null, true);
	}

	private List<KDTreeNode<T, O>> getNodes() {
		if (map.isEmpty()) {
			return Collections.emptyList();
		}

		return new ArrayList<>(map.values());
	}

	private KDTreeNode<T, O> buildBalancedTree(List<KDTreeNode<T, O>> nodes,
			KDTreeNode<T, O> parent, boolean isLatitude) {
		if (nodes.isEmpty()) {
			return null;
		}

		// Sort the nodes along the splitting dimension (latitude or longitude)
		nodes.sort((o1, o2) -> {
					Point o1Point = o1.getKdTreeObject().getPoint();
					Point o2Point = o2.getKdTreeObject().getPoint();
					if (isLatitude) {
						return Double.compare(o1Point.getLatitude(),
								o2Point.getLatitude());
					}
					return Double.compare(o1Point.getLongitude(),
							o2Point.getLongitude());
				}
		);

		// Find the index of the median node in the list of nodes
		int medianIndex = nodes.size() / 2;
		KDTreeNode<T, O> medianNode = nodes.get(medianIndex);

		medianNode.setParent(parent);
		medianNode.setLeft(buildBalancedTree(nodes.subList(0, medianIndex), medianNode, !isLatitude));
		medianNode.setRight(buildBalancedTree(nodes.subList(medianIndex + 1, nodes.size()), medianNode, !isLatitude));
		return medianNode;
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