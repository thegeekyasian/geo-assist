package com.thegeekyasian.geoassist.kdtree;

/**
 * A helper class used to represent the result of a nearest neighbor search in a KDTree.
 * Contains a reference to the closest KDTreeObject found and the distance from the query point to that object.
 *
 * @param <T> the type of data stored in the KDTreeObject
 *
 * @param <O> the type of the object used as an identifier for the KDTreeObject
 *
 * @author The Geeky Asian
 */
public class KDTreeNearestNeighbor<T, O> {

	/**
	 * The closest KDTreeObject found in the KDTree.
	 */
	private KDTreeObject<T, O> kdTreeObject;

	/**
	 The distance from the query point to the closest KDTreeObject.
	 */
	private Double distance;

	/**
	 Get the closest KDTreeObject.
	 @return the closest KDTreeObject
	 */
	public KDTreeObject<T, O> getKdTreeObject() {
		return this.kdTreeObject;
	}

	/**
	 Set the closest KDTreeObject.
	 @param kdTreeObject the closest KDTreeObject to set
	 */
	public void setKdTreeObject(KDTreeObject<T, O> kdTreeObject) {
		this.kdTreeObject = kdTreeObject;
	}

	/**
	 Get the distance to the closest KDTreeObject.
	 @return the distance to the closest KDTreeObject
	 */
	public Double getDistance() {
		return this.distance;
	}

	/**
	 Set the distance to the closest KDTreeObject.
	 @param distance the distance to the closest KDTreeObject to set
	 */
	public void setDistance(Double distance) {
		this.distance = distance;
	}
}
