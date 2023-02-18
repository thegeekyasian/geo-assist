package com.thegeekyasian.geoassist.kdtree;

/**
 * @param <T> describes the identifier of the K-d Tree Object,
 *            that is being inserted in the tree. For example ID or UUID of the Object.
 * @param <O> describes the object that is inserted in the tree.
 *            For example Vendor, Restaurant, Franchise, etc.
 * @author thegeekyasian
 */
public class KDTreeObject<T, O> {

    private T id;

    private O data;

    private Point point;

    private KDTreeObject(final Builder<T, O> builder, Point point) {
        this.id = builder.id;
        this.data = builder.data;
        this.point = point;
    }

    public T getId() {
        return id;
    }

    public void setId(final T id) {
        this.id = id;
    }

    public O getData() {
        return data;
    }

    public void setData(final O data) {
        this.data = data;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(final Point point) {
        this.point = point;
    }

    public static class Builder<T, O> {
        private T id;

        private O data;

        private double latitude;

        private double longitude;

        public Builder<T, O> id(final T id) {
            this.id = id;
            return this;
        }

        public Builder<T, O> data(final O data) {
            this.data = data;
            return this;
        }

        public Builder<T, O> latitude(final double latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder<T, O> longitude(final double longitude) {
            this.longitude = longitude;
            return this;
        }

        public KDTreeObject<T, O> build() {
            final Point point = new Point.Builder()
                    .latitude(this.latitude)
                    .longitude(this.longitude)
                    .build();
            return new KDTreeObject<>(this, point);
        }
    }
}