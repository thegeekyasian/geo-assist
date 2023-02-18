# Geo Assist Benchmarks

## Insertion

### JTS Insert
```
Benchmark            (size)  Mode  Cnt           Score           Error  Units
JTS-K-DTree.insert        1  avgt    5          18.019 ±        12.871  ns/op
JTS-K-DTree.insert       10  avgt    5         424.379 ±       217.984  ns/op
JTS-K-DTree.insert      100  avgt    5        5696.540 ±       726.186  ns/op
JTS-K-DTree.insert     1000  avgt    5      160424.511 ±     43723.511  ns/op
JTS-K-DTree.insert    10000  avgt    5     2781463.138 ±    609730.798  ns/op
JTS-K-DTree.insert   100000  avgt    5    59601450.462 ±  13733688.834  ns/op
JTS-K-DTree.insert  1000000  avgt    5  1140432240.000 ± 265797631.855  ns/op
```

### Geo Assist
```
Benchmark                  (size)  Mode  Cnt          Score           Error  Units
GeoAssist-K-DTree.insert        1  avgt    5         33.646 ±         0.522  ns/op
GeoAssist-K-DTree.insert       10  avgt    5        285.876 ±        19.412  ns/op
GeoAssist-K-DTree.insert      100  avgt    5       3826.130 ±       615.843  ns/op
GeoAssist-K-DTree.insert     1000  avgt    5     137003.708 ±      9701.671  ns/op
GeoAssist-K-DTree.insert    10000  avgt    5    2134120.788 ±    176302.011  ns/op
GeoAssist-K-DTree.insert   100000  avgt    5   37465374.497 ±   1639938.460  ns/op
GeoAssist-K-DTree.insert  1000000  avgt    5  867845480.000 ± 250250975.846  ns/op
```

### JTS Shuffle
```
Benchmark                    (size)  Mode  Cnt           Score           Error  Units
JTS-K-DTree_Shuffle.insert        1  avgt    5          19.530 ±        16.995  ns/op
JTS-K-DTree_Shuffle.insert       10  avgt    5         413.295 ±        74.594  ns/op
JTS-K-DTree_Shuffle.insert      100  avgt    5        5868.343 ±      1252.581  ns/op
JTS-K-DTree_Shuffle.insert     1000  avgt    5      164805.687 ±     39825.609  ns/op
JTS-K-DTree_Shuffle.insert    10000  avgt    5     2843804.242 ±    629854.856  ns/op
JTS-K-DTree_Shuffle.insert   100000  avgt    5    65883758.696 ±  14574586.296  ns/op
JTS-K-DTree_Shuffle.insert  1000000  avgt    5  1160544820.000 ± 273094738.479  ns/op
```

### Geo Assist Shuffle
```
Benchmark                          (size)  Mode  Cnt          Score           Error  Units
GeoAssist-K-DTree_Shuffle.insert        1  avgt    5         39.305 ±         8.926  ns/op
GeoAssist-K-DTree_Shuffle.insert       10  avgt    5        302.071 ±        59.980  ns/op
GeoAssist-K-DTree_Shuffle.insert      100  avgt    5       3915.696 ±       782.513  ns/op
GeoAssist-K-DTree_Shuffle.insert     1000  avgt    5     130643.646 ±     19244.618  ns/op
GeoAssist-K-DTree_Shuffle.insert    10000  avgt    5    2170827.989 ±    163835.329  ns/op
GeoAssist-K-DTree_Shuffle.insert   100000  avgt    5   37120607.381 ±   1234417.680  ns/op
GeoAssist-K-DTree_Shuffle.insert  1000000  avgt    5  818519030.000 ± 140793765.408  ns/op
```

### Summary
![image](https://user-images.githubusercontent.com/30790884/219821529-f4b02729-c772-497a-be57-1d8a17bf2f45.png)

## Find Nearest Neighbor

### Geo Assist (Unbalanced Tree - Unordered Insertion)

```
Benchmark                                          (size)  Mode  Cnt         Score          Error  Units
GeoAssist-K-DTree_Unbalanced.findNearestNeighbor        1  avgt    5       172.428 ±       10.924  ns/op
GeoAssist-K-DTree_Unbalanced.findNearestNeighbor       10  avgt    5       917.868 ±      140.696  ns/op
GeoAssist-K-DTree_Unbalanced.findNearestNeighbor      100  avgt    5      4429.680 ±      523.224  ns/op
GeoAssist-K-DTree_Unbalanced.findNearestNeighbor     1000  avgt    5     26142.709 ±     3375.036  ns/op
GeoAssist-K-DTree_Unbalanced.findNearestNeighbor    10000  avgt    5    204298.346 ±    23939.167  ns/op
GeoAssist-K-DTree_Unbalanced.findNearestNeighbor   100000  avgt    5   2447769.418 ±   122831.088  ns/op
GeoAssist-K-DTree_Unbalanced.findNearestNeighbor  1000000  avgt    5  28057768.147 ± 21224821.849  ns/op
```

### Geo Assist (Balanced Tree)

```
Benchmark                                        (size)  Mode  Cnt    Score    Error  Units
GeoAssist-K-DTree_Balanced.findNearestNeighbor        1  avgt    5  170.756 ± 11.267  ns/op
GeoAssist-K-DTree_Balanced.findNearestNeighbor       10  avgt    5  169.294 ±  1.702  ns/op
GeoAssist-K-DTree_Balanced.findNearestNeighbor      100  avgt    5  173.019 ±  6.780  ns/op
GeoAssist-K-DTree_Balanced.findNearestNeighbor     1000  avgt    5  170.490 ±  6.104  ns/op
GeoAssist-K-DTree_Balanced.findNearestNeighbor    10000  avgt    5  170.172 ±  5.985  ns/op
GeoAssist-K-DTree_Balanced.findNearestNeighbor   100000  avgt    5  169.036 ±  3.656  ns/op
GeoAssist-K-DTree_Balanced.findNearestNeighbor  1000000  avgt    5  175.640 ± 10.803  ns/op
```

### Summary

![image](https://user-images.githubusercontent.com/30790884/219823261-1da8a378-547e-42b3-a6a6-e49f6c7e4189.png)
