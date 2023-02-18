package com.thegeekyasian.geoassist.kdtree;

import com.thegeekyasian.geoassist.core.GeoAssistException;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @param <T> describes the identifier of the K-d Tree Object,
 *            that is being inserted in the tree. For example ID or UUID of the Object.
 * @param <O> describes the object that is inserted in the tree.
 *            For example Vendor, Restaurant, Franchise, etc.
 * @author thegeekyasian
 * <p>
 * This is an implementation of a two-dimensional KD-Tree that enables
 * efficient range searching and lookup for point data. Points can be
 * dynamically inserted into the tree to build it. The KD-Tree supports
 * point equality queries and range queries, providing fast retrieval of the data.
 * <p>
 * It is important to note that the structure of a KD-Tree is dependent on the
 * order insertion. If points are inserted in a coherent manner
 * (such as being monotonic in one or both dimensions), the tree may become unbalanced.
 * This can greatly impact the efficiency of queries.
 */
public class KDTree<T, O> implements Serializable {

    private static final long serialVersionUID = 5020274653621814765L;

    private KDTreeNode<T, O> root;

    private final Map<T, KDTreeNode<T, O>> nodeMap = new HashMap<>();

    /**
     * Creates a new instance of KDTree.
     */
    public KDTree() {
        this.root = null;
    }


    /**
     * Inserts the provided KDTreeObject on the tree,
     * based on the provided Point (coordinates).
     *
     * @param kdTreeObject KDTreeObject holds the custom object,
     *                     identifier along with the Point (latitude/longitude coordinates) of the object.
     */
    public void insert(final KDTreeObject<T, O> kdTreeObject) {
        insertObject(kdTreeObject);
    }

    @SuppressWarnings({"checkstyle:NPathComplexity", "checkstyle:CyclomaticComplexity"})
    private void insertObject(final KDTreeObject<T, O> object) {

        int depth = 0;
        if (root == null) {
            KDTreeNode<T, O> node = new KDTreeNode<>(object, depth, null);
            root = node;
            nodeMap.put(object.getId(), node);
            return;
        }

        KDTreeNode<T, O> current = root;
        KDTreeNode<T, O> parent = root;
        final Point point = object.getPoint();
        final double[] coordinates = {point.getLatitude(), point.getLongitude()};
        boolean isLatitude = true;
        boolean isLessThanCurrentValue = true;

        while (current != null) {
            // Store the current node as the parent for the next iteration
            parent = current;

            // Determine whether to move to the left or right subtree based on
            // the comparison of the current dimension's coordinate value
            isLessThanCurrentValue = Double.compare(coordinates[isLatitude ? 0 : 1], current.value(isLatitude)) < 0;
            current = isLessThanCurrentValue ? current.getLeft() : current.getRight();

            // If the next node in the tree has the same coordinate value along the current dimension,
            // move to the next dimension and repeat the process
            if (current != null
                    && Double.compare(coordinates[isLatitude ? 0 : 1], current.value(isLatitude)) == 0) {
                isLatitude = !isLatitude;
                isLessThanCurrentValue = Double.compare(coordinates[isLatitude ? 0 : 1], current.value(isLatitude)) < 0;
                current = isLessThanCurrentValue ? current.getLeft() : current.getRight();

                // If the next node in the tree has the same coordinate value along the alternate dimension,
                // it already exists in the tree and should be returned
                if (current != null
                        && Double.compare(coordinates[isLatitude ? 0 : 1], current.value(isLatitude)) == 0) {
                    return;
                }
            }

            // Move to the alternate dimension and continue the iteration
            isLatitude = !isLatitude;
            depth++;
        }

        final KDTreeNode<T, O> node = new KDTreeNode<>(object, depth, parent);
        nodeMap.put(object.getId(), node);

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
     * @param point    Location (latitude/longitude) coordinates
     *                 of the reference point to find the nearest neighbors for.
     * @param distance Maximum distance to find the nearest neighbors in.
     * @return Returns the list of KDTreeObjects
     * that are nearby the provided point for the provided distance.
     */
    public List<KDTreeObject<T, O>> findNearestNeighbor(final Point point, final double distance) {
        // Initialize a list to store the closest points
        List<KDTreeObject<T, O>> closestPoints = new ArrayList<>();
        // Call the recursive helper method with the root node, point, distance, and closestPoints list
        findNearestNeighbor(root, point, distance, closestPoints, 0);
        // Return the list of the closest points
        return closestPoints;
    }

    @SuppressWarnings("checkstyle:CyclomaticComplexity")
    private void findNearestNeighbor(final KDTreeNode<T, O> node,
                                     final Point point,
                                     final double distance,
                                     final List<KDTreeObject<T, O>> closestPoints,
                                     final int depth) {
        if (node == null) {
            return;
        }

        double currentDistance = getHaversineDistance(node.getKdTreeObject().getPoint(), point);

        if (currentDistance <= distance) {
            closestPoints.add(node.getKdTreeObject());
        }

        // Determine which dimension to compare based on the depth of the current node in the K-d tree
        final int dim = depth % 2;

        // If the `dim`-th coordinate of the input point is less than the `dim`-th coordinate
        // of the current node's point,
        // continue searching in the left subtree and check if the absolute difference between the `dim`-th coordinates
        // is less than or equal to the maximum distance
        if (dim == 0) {
            if (point.getLatitude() < node.getKdTreeObject().getPoint().getLatitude()) {
                findNearestNeighbor(node.getLeft(), point, distance, closestPoints, depth + 1);

                // check if the difference in latitudes is less than or equal to the provided distance
                // if so, also search the right subtree
                if (node.getRight() != null
                        && Math.abs(node.getKdTreeObject().getPoint().getLatitude() - point.getLatitude())
                        <= distance) {
                    findNearestNeighbor(node.getRight(), point, distance, closestPoints, depth + 1);
                }
            } else {
                // If the `dim`-th coordinate of the input point is greater than or equal to the `dim`-th
                // coordinate of the current node's point,
                // continue searching in the right subtree and check if the absolute difference between
                // the `dim`-th coordinates
                // is less than or equal to the maximum distance
                findNearestNeighbor(node.getRight(), point, distance, closestPoints, depth + 1);

                // check if the difference in latitudes is less than or equal to the provided distance
                // if so, also search the left subtree
                if (node.getLeft() != null
                        && Math.abs(node.getKdTreeObject().getPoint().getLatitude() - point.getLatitude())
                        <= distance) {
                    findNearestNeighbor(node.getLeft(), point, distance, closestPoints, depth + 1);
                }
            }
        } else {
            // using the same logic as above but for longitude i.e. the second dimension.
            if (point.getLongitude() < node.getKdTreeObject().getPoint().getLongitude()) {
                findNearestNeighbor(node.getLeft(), point, distance, closestPoints, depth + 1);
                if (node.getRight() != null
                        && Math.abs(node.getKdTreeObject().getPoint().getLongitude() - point.getLongitude())
                        <= distance) {
                    findNearestNeighbor(node.getRight(), point, distance, closestPoints, depth + 1);
                }
            } else {
                findNearestNeighbor(node.getRight(), point, distance, closestPoints, depth + 1);
                if (node.getLeft() != null
                        && Math.abs(node.getKdTreeObject().getPoint().getLongitude() - point.getLongitude())
                        <= distance) {
                    findNearestNeighbor(node.getLeft(), point, distance, closestPoints, depth + 1);
                }
            }
        }
    }

    /**
     * Returns the size i.e. number of objects present in the tree at a provided moment
     *
     * @return an integer value of size is returned.
     */
    public int getSize() {
        return nodeMap.size();
    }

    /**
     * Find the KDTreeObject for the provided ID.
     *
     * @param id ID of the KDTreeObject that you are looking.
     * @return The method returns the KDTreeObject for the provided ID.
     * If not found, the method returns `null` otherwise.
     */
    @Nullable
    public KDTreeObject<T, O> getById(final T id) {
        return Optional.ofNullable(nodeMap.get(id))
                .map(KDTreeNode::getKdTreeObject)
                .orElse(null);
    }

    /**
     * Updates the custom data by replacing it in the KDTreeObject with the provided data.
     *
     * @param id   ID of the custom object that is desired to be deleted.
     * @param data Custom data to be updated in the KDTreeObject of provided ID.
     * @throws GeoAssistException is thrown an object with provided ID is not found.
     */
    public void update(final T id, final O data) {
        Optional.ofNullable(nodeMap.get(id))
                .map(node -> {
                    node.getKdTreeObject().setData(data);
                    return node.getKdTreeObject();
                }).orElseThrow(() -> new GeoAssistException("No object found for provided ID"));
    }

    /**
     * Deletes the object with the provided custom ID.
     *
     * @param id ID of the custom object that is desired to be deleted.
     * @return Returns a boolean flag, `true` if the object is successfully deleted
     * and `false` otherwise.
     */
    public boolean delete(final T id) {
        return Optional.ofNullable(nodeMap.get(id)).map(node -> {
            deleteNode(node);
            nodeMap.remove(id);
            return true;
        }).orElse(false);
    }

    private void deleteNode(final KDTreeNode<T, O> node) {
        if (node.getLeft() == null && node.getRight() == null) {
            if (node.getParent().getLeft() == node) {
                node.getParent().setLeft(null);
            } else {
                node.getParent().setRight(null);
            }
        } else if (node.getLeft() == null) {
            if (node.getParent().getLeft() == node) {
                node.getParent().setLeft(node.getRight());
            } else {
                node.getParent().setRight(node.getRight());
            }
            node.getRight().setParent(node.getParent());
        } else if (node.getRight() == null) {
            if (node.getParent().getLeft() == node) {
                node.getParent().setLeft(node.getLeft());
            } else {
                node.getParent().setRight(node.getLeft());
            }
            node.getLeft().setParent(node.getParent());
        } else {
            KDTreeNode<T, O> minNode = node.getRight();
            while (minNode.getLeft() != null) {
                minNode = minNode.getLeft();
            }
            node.setKdTreeObject(minNode.getKdTreeObject());
            deleteNode(minNode);
        }
    }

    /**
     * <p>
     * Returns whether the KDTree is balanced. A KDTree is considered balanced if
     * the depth of any two leaf nodes differs by no more than 1. The method
     * determines if the tree is balanced by recursively traversing the tree and
     * checking the depths of each subtree.
     * </p>
     *
     * <p>
     * The method has a time complexity of O(N) in the best case scenario,
     * where the tree is perfectly balanced, and O(N log N) in the worst case scenario,
     * where the tree is completely unbalanced.
     * </p>
     *
     * @return true if the tree is balanced, false otherwise.
     */
    public boolean isBalanced() {
        return isBalanced(root) != -1;
    }

    private int isBalanced(final KDTreeNode<T, O> node) {
        // If the current node is null, return 0.
        if (node == null) {
            return 0;
        }

        // Recursively call isBalanced on the left subtree, obtaining its depth.
        int leftDepth = isBalanced(node.getLeft());

        // If the left subtree is unbalanced (depth of -1),
        // return -1 to indicate that the current subtree is also unbalanced.
        if (leftDepth == -1) {
            return -1;
        }

        // Recursively call isBalanced on the right subtree, obtaining its depth.
        int rightDepth = isBalanced(node.getRight());

        // If the right subtree is unbalanced (depth of -1),
        // return -1 to indicate that the current subtree is also unbalanced.
        if (rightDepth == -1) {
            return -1;
        }

        // Check if the current subtree is balanced by comparing the depths of the left and right subtrees.
        if (Math.abs(leftDepth - rightDepth) > 1) {
            // If the current subtree is unbalanced,
            // return -1 to indicate that the current subtree is unbalanced.
            return -1;
        }

        // If the current subtree is balanced, return its depth.
        return Math.max(leftDepth, rightDepth) + 1;
    }

    /**
     * <p>
     * Balances the k-d tree to ensure efficient query performance.
     * A k-d tree can become unbalanced if the order of
     * point insertion is coherent (e.g. monotonic in one or both dimensions).
     * An unbalanced tree may have a much deeper
     * depth than a balanced tree, which can lead to slower query performance.
     * This method reorders the nodes in the tree to improve balance and reduce depth,
     * improving query performance.
     * This method does not affect the values or coordinates of the nodes in the tree.
     * </p>
     * <p>
     * This method should be called after all nodes have been inserted into the tree,
     * and can be called multiple times if the tree becomes unbalanced after
     * subsequent insertions or removals.
     * This method has a time complexity of O(N log N),
     * where N is the number of nodes in the tree.
     * </p>
     */
    public void balance() {
        // If the tree is empty, there's nothing to balance
        if (root == null) {
            return;
        }

        // Build an array of all nodes in the tree
        final List<KDTreeNode<T, O>> nodeList = new ArrayList<>();
        collectNodes(nodeList);

        // Sort the nodes by their x-coordinate and y-coordinate, alternately
        nodeList.sort(new NodeComparator<>());

        // Rebuild the tree by recursively dividing the array and creating sub-trees
        root = buildTree(nodeList, null, 0, nodeList.size() - 1, 0);
    }

    private void collectNodes(final List<KDTreeNode<T, O>> nodeList) {
        if (nodeMap.isEmpty()) {
            return;
        }
        nodeList.addAll(nodeMap.values());
    }

    @Nullable
    private KDTreeNode<T, O> buildTree(final List<KDTreeNode<T, O>> nodeList,
                                       final KDTreeNode<T, O> parent,
                                       final int start,
                                       final int end,
                                       final int depth) {
        if (start > end) {
            return null;
        }

        final int mid = start + (end - start) / 2;
        final KDTreeNode<T, O> node = nodeList.get(mid);

        final int nextDepth = depth + 1;

        node.setDepth(depth);
        node.setParent(parent);
        node.setLeft(buildTree(nodeList, node, start, mid - 1, nextDepth));
        node.setRight(buildTree(nodeList, node, mid + 1, end, nextDepth));

        return node;
    }

    private double getHaversineDistance(final Point point1, final Point point2) {
        final double lat1 = point1.getLatitude();
        final double lon1 = point1.getLongitude();
        final double lat2 = point2.getLatitude();
        final double lon2 = point2.getLongitude();

        final double R = 6378.13; // radius of Earth in kilometer WGS84
        final double dLat = Math.toRadians(lat2 - lat1);
        final double dLon = Math.toRadians(lon2 - lon1);

        final double lat1Radios = Math.toRadians(lat1);
        final double lat2Radios = Math.toRadians(lat2);

        final double latDelta = Math.sin(dLat / 2);
        final double lonDelta = Math.sin(dLon / 2);

        final double a = (latDelta * latDelta) + (lonDelta * lonDelta * Math.cos(lat1Radios) * Math.cos(lat2Radios));
        final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}
