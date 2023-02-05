# Geo Assist

---

## What is it?
Geo assist is library that allows users to perform insertion and query operations on the spatial data. 

It enables users to create a spatial engine by loading objects with spatial references, such as latitude/longitude, in-memory.

The goal of this project is to enable the use of complex search algorithms, by tweaking them for geo-spatial operations.

## How to?

### K-d Tree:

K-d Tree, formally called K-Dimensional Trees, are one of the best options when storing and retrieving objects based on geospatial parameters.

I have provided an implementation of storing objects in a K-d tree using the coordinates and searching nearest neighbors for the provided location (latitude/longitude) and the distance.

Here is how to initialize your data:

``` java
KDTree<String, Object> kdTree = new KDTree<>();
kdTree.insert(new KDTreeObject.Builder<String, Object>()
				.id("5")
				.latitude(25.2002450)
				.longitude(55.2734184)
				.build());
```

Once you have inserted your object(s) in the tree, here is how you can search for the nearest neighbors for a provided location:

``` java
Point point = new Point.Builder()
				.latitude(25.2012544)
				.longitude(55.2569389)
				.build();
List<KDTreeObject<String, Object>> nearestNeighbors = 
        kdTree.findNearestNeighbor(point, 2); // 2 kilometers based on haversine distance.
```

This is how simple it has been made to query your geo-spatial data.

The project is yet in progress to be made available through the central repository though.

Feel free to [contact me](https://thegeekyasian.com/contact/) or write me an email at [hello@thegeekyasian.com](mailto:hello@thegeekyasian.com)