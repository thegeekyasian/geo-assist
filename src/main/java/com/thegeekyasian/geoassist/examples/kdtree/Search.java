package com.thegeekyasian.geoassist.examples.kdtree;

import java.util.List;

import com.thegeekyasian.geoassist.kdtree.KDTree;
import com.thegeekyasian.geoassist.kdtree.KDTreeObject;
import com.thegeekyasian.geoassist.kdtree.Point;

/**
 * @author thegeekyasian
 */
public class Search {

	public static void main(String... args) {

		KDTree<String, Object> kdTree = new KDTree<>();
		init(kdTree);
		List<KDTreeObject<String, Object>> nearestNeighbors = kdTree.findNearestNeighbor(
				new Point.Builder()
						.latitude(25.2012544)
						.longitude(55.2569389)
						.build(), 2); // 2 kilometers based on haversine distance.

		nearestNeighbors.forEach(neighbor -> System.out.printf("ID -> %s%n", neighbor.getId()));
	}

	public static void init(KDTree<String, Object> kdTree) {

		kdTree.insert(new KDTreeObject.Builder<String, Object>()
				.id("1")
				.latitude(25.1967512)
				.longitude(55.2732038)
				.build());


		kdTree.insert(new KDTreeObject.Builder<String, Object>()
				.id("2")
				.latitude(25.1962077)
				.longitude(55.2714443)
				.build());


		kdTree.insert(new KDTreeObject.Builder<String, Object>()
				.id("3")
				.latitude(25.1954312)
				.longitude(55.2811432)
				.build());


		kdTree.insert(new KDTreeObject.Builder<String, Object>()
				.id("4")
				.latitude(25.1903843)
				.longitude(55.2798557)
				.build());


		kdTree.insert(new KDTreeObject.Builder<String, Object>()
				.id("5")
				.latitude(25.2002450)
				.longitude(55.2734184)
				.build());


		kdTree.insert(new KDTreeObject.Builder<String, Object>()
				.id("6")
				.latitude(25.2028848)
				.longitude(55.2783966)
				.build());


		kdTree.insert(new KDTreeObject.Builder<String, Object>()
				.id("7")
				.latitude(25.2012544)
				.longitude(55.2569389)
				.build());


		kdTree.insert(new KDTreeObject.Builder<String, Object>()
				.id("8")
				.latitude(25.1644242)
				.longitude(55.2450943)
				.build());


		kdTree.insert(new KDTreeObject.Builder<String, Object>()
				.id("9")
				.latitude(25.0763827)
				.longitude(55.1616669)
				.build());
	}
}
