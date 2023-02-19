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

        final KDTree<Long, Object> kdTree = new KDTree<>();
        init(kdTree);

        final Point point = new Point.Builder()
                .latitude(25.2012544)
                .longitude(55.2569389)
                .build();
        final List<KDTreeObject<Long, Object>> nearestNeighbors =
                kdTree.findNearestNeighbor(point, 2); // 2 kilometers based on haversine distance.

        //output
        nearestNeighbors.forEach(System.out::println);
    }

    private static void init(final KDTree<Long, Object> kdTree) {

        kdTree.insert(new KDTreeObject.Builder<Long, Object>()
                .id(1L)
                .latitude(25.1967512)
                .longitude(55.2732038)
                .build());


        kdTree.insert(new KDTreeObject.Builder<Long, Object>()
                .id(2L)
                .latitude(25.1962077)
                .longitude(55.2714443)
                .build());


        kdTree.insert(new KDTreeObject.Builder<Long, Object>()
                .id(3L)
                .latitude(25.1954312)
                .longitude(55.2811432)
                .build());


        kdTree.insert(new KDTreeObject.Builder<Long, Object>()
                .id(4L)
                .latitude(25.1903843)
                .longitude(55.2798557)
                .build());


        kdTree.insert(new KDTreeObject.Builder<Long, Object>()
                .id(5L)
                .latitude(25.2002450)
                .longitude(55.2734184)
                .build());


        kdTree.insert(new KDTreeObject.Builder<Long, Object>()
                .id(6L)
                .latitude(25.2028848)
                .longitude(55.2783966)
                .build());


        kdTree.insert(new KDTreeObject.Builder<Long, Object>()
                .id(7L)
                .latitude(25.2012544)
                .longitude(55.2569389)
                .build());


        kdTree.insert(new KDTreeObject.Builder<Long, Object>()
                .id(8L)
                .latitude(25.1644242)
                .longitude(55.2450943)
                .build());


        kdTree.insert(new KDTreeObject.Builder<Long, Object>()
                .id(9L)
                .latitude(25.0763827)
                .longitude(55.1616669)
                .build());
    }
}
