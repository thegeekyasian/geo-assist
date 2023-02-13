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
    <version>1.0.1</version>
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

#### Delete

You can delete the object based on the custom identifier `ID`:

``` java
boolean ok = kdTree.delete(5);
```

This is how simple it has been made to query your geo-spatial data.

Feel free to [contact me](https://thegeekyasian.com/contact/) or write me an email at [hello@thegeekyasian.com](mailto:hello@thegeekyasian.com)
