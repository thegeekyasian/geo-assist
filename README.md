<h1 align="center">
   <a href="https://github.com/thegeekyasian/geo-assist">
   <img src="https://user-images.githubusercontent.com/30790884/221882235-459941b2-abaf-4790-9fc3-0d8bfa0239ac.png" alt="Logo" height=150>
   </a>
   <br />
   Geo Assist
</h1>
<p align="center">
   Manage and query your geo-spatial data efficiently.
   <br />
   <br />
   <a href="https://github.com/thegeekyasian/geo-assist/issues">Report a Bug</a>
   ·
   <a href="https://github.com/thegeekyasian/geo-assist/issues">Request a Feature</a>
</p>
<p align="center">
   <a href="https://discord.gg/8Xe2Ds4BWj" target="_blank">
   <img src="https://img.shields.io/badge/discord-geoassist-purple" alt="Logo">
   </a>
   <a href="https://www.twitter.com/thegeekyasian" target="_blank">
   <img src="https://img.shields.io/badge/twitter-thegeekyasian-9cf" alt="Logo">
   </a>
</p>

## ⚡️ What is it?
Geo assist is library that allows users to perform insertion and query operations on the spatial data. 

It enables users to create a spatial engine by loading objects with spatial references, such as latitude/longitude, in-memory.

The goal of this project is to enable the use of complex search algorithms, by tweaking them for geo-spatial operations.

## 📖 How to?

### Install:
Geo-assist is available on maven repository and can be imported to your project. 

```xml
<dependency>
    <groupId>com.thegeekyasian</groupId>
    <artifactId>geo-assist</artifactId>
    <version>1.0.2</version>
</dependency>
```

### 🌳 K-d Tree:

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

## ⭐️ Project assistance

If you want to say **thank you** or/and support active development of `Geo Assist`:

- Add a [GitHub Star](https://github.com/thegeekyasian/geo-assist) to the project.
- Tweet about project [on your Twitter](https://twitter.com/intent/tweet?text=Manage%20and%20query%20your%20%23geospatial%20data%20efficiently%20with%20%23GeoAssist%0A%0A%23java%20%23programming%20%23gis%20%23opensource%20%23coding&url=https%3A%2F%2Fgithub.com%2Fthegeekyasian%2Fgeo-assist%2F).
- Write interesting articles about project on [Dev.to](https://dev.to/), [Medium](https://medium.com/) or personal blog.

Together, we can make this project **better** every day! ❤️

Feel free to [contact me](https://thegeekyasian.com/contact/) or write me an email at [hello@thegeekyasian.com](mailto:hello@thegeekyasian.com)
