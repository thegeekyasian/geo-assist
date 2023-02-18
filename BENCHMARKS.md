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

![image](https://user-images.githubusercontent.com/30790884/219818462-06643c77-07c8-4c27-b08a-02381e461363.png)
