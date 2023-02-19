package com.thegeekyasian.geoassist.kdtree;

import java.util.Objects;

import static java.util.Objects.isNull;

/**
 * @author thegeekyasian
 */
public class Point {
    private double latitude;

    private double longitude;

    public Point() {
    }

    public Point(Builder builder) {
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(final double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(final double longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (isNull(o) || getClass() != o.getClass()) {
            return false;
        }
        final Point point = (Point) o;
        return Double.compare(point.latitude, latitude) == 0
                && Double.compare(point.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

    @Override
    public String toString() {
        return "Point{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    public static class Builder {
        private double latitude;

        private double longitude;

        public Builder latitude(final double latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder longitude(final double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Point build() {
            return new Point(this);
        }
    }
}
