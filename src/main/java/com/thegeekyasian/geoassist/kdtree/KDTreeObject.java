package com.thegeekyasian.geoassist.kdtree;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

import static java.util.Objects.isNull;

/**
 * @param <T> describes the identifier of the K-d Tree Object,
 *            that is being inserted in the tree. For example ID or UUID of the Object.
 * @param <O> describes the object that is inserted in the tree.
 *            For example Vendor, Restaurant, Franchise, etc.
 * @author thegeekyasian
 */
public class KDTreeObject<T, O> {

    @Nullable
    private T id;

    @Nullable
    private O data;

    @Nonnull
    private Point point;

    private KDTreeObject(@Nonnull final Builder<T, O> builder, @Nonnull final Point point) {
        this.id = builder.id;
        this.data = builder.data;
        this.point = point;
    }

    @Nullable
    public T getId() {
        return id;
    }

    public void setId(@Nullable final T id) {
        this.id = id;
    }

    @Nullable
    public O getData() {
        return data;
    }

    public void setData(@Nullable final O data) {
        this.data = data;
    }

    @Nonnull
    public Point getPoint() {
        return point;
    }

    public void setPoint(@Nonnull final Point point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "KDTreeObject{id=" + id + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (isNull(o) || getClass() != o.getClass()){
            return false;
        }
        final KDTreeObject<?, ?> that = (KDTreeObject<?, ?>) o;

        return Objects.equals(id, that.id)
                && Objects.equals(data, that.data)
                && point.equals(that.point);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, data, point);
    }

    public static class Builder<T, O> {
        @Nullable
        private T id;

        @Nullable
        private O data;

        private double latitude;

        private double longitude;

        public Builder<T, O> id(@Nullable final T id) {
            this.id = id;
            return this;
        }

        public Builder<T, O> data(@Nullable final O data) {
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