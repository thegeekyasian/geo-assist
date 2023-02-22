# Geo Assist

## What is it?
Geo assist is library that allows users to perform insertion and query operations on the spatial data. 

It enables users to create a spatial engine by loading objects with spatial references, such as latitude/longitude, in-memory.

The goal of this project is to enable the use of complex search algorithms, by tweaking them for geo-spatial operations.

## How to?

### Install:
Geo-assist is available on maven repository and can be imported to your project. 

```xml
<dependency>
    <groupId>com.thegeekyasian</groupId>
    <artifactId>geo-assist</artifactId>
    <version>1.0.2</version>
</dependency>
```

### K-d Tree:

K-d Tree, formally called K-Dimensional Trees, are one of the best options when storing and retrieving objects based on geospatial parameters.

I have provided an implementation of storing objects in a K-d tree using the coordinates and searching nearest neighbors for the provided location (latitude/longitude) and the distance.

#### Insert

Here is how to initialize your data:

``` java
KDTree<Long, Object> kdTree = new KDTree<>();
kdTree.insert(new KDTreeObject.Builder<Long, Object>()
				.id(5)
				.latitude(25.2002450)
				.longitude(55.2734184)
				.build());
```

#### Find Nearest Neighbors

Once you have inserted your object(s) in the tree, here is how you can search for the nearest neighbors for a provided location:

``` java
Point point = new Point.Builder()
				.latitude(25.2012544)
				.longitude(55.2569389)
				.build();
List<KDTreeObject<Long, Object>> nearestNeighbors = 
        kdTree.findNearestNeighbor(point, 2); // 2 kilometers based on haversine distance.
```

#### Find in Bounding Box (range)
You cna also find of objects in a bounding box for the provided range.
The `findInRange` method searches the k-d tree for all nodes whose coordinates fall within a given bounding box. This is useful for finding all points within a specific geographic region or for performing spatial queries on a set of points. The method takes in a BoundingBox object that defines the range to search within, and returns a list of KDTreeObject objects whose coordinates fall within the bounding box.

Here is how you can use `find in range`:
``` java
BoundingBox boundingBox = new BoundingBox.Builder()
				.lowerPoint(new Point.Builder()
						.latitude(24.836135)
						.longitude(66.976089)
						.build())
				.upperPoint(new Point.Builder()
						.latitude(24.951953)
						.longitude(67.157364)
						.build())
				.build();
				
List<KDTreeObject<String, Object>> objects = kdTree.findInRange(boundingBox);
```

#### Delete

You can delete the object based on the custom identifier `ID`:

``` java
boolean ok = kdTree.delete(5);
```

This is how simple it has been made to query your geo-spatial data.

Feel free to [contact me](https://thegeekyasian.com/contact/) or write me an email at [hello@thegeekyasian.com](mailto:hello@thegeekyasian.com)
