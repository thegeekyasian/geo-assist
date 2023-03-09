package com.thegeekyasian.geoassist.kdtree.geometry;

import com.thegeekyasian.geoassist.core.GeoAssistException;

/**
 * The Point class represents a point in the geographic coordinate system, with a latitude and longitude.
 * It provides a Builder class for constructing instances of the Point class.
 *
 * @author The Geeky Asian
 */
public class Point {
	private double latitude;

	private double longitude;

	public Point(Builder builder) {
		this.latitude = builder.latitude;
		this.longitude = builder.longitude;
	}

	public double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public static class Builder {
		private double latitude;

		private double longitude;

		public Builder latitude(double latitude) {
			this.latitude = latitude;
			return this;
		}

		public Builder longitude(double longitude) {
			this.longitude = longitude;
			return this;
		}

		public Point build() {
			if (this.latitude < -90 || this.latitude > 90) {
				throw new GeoAssistException("not a valid latitude value");
			}
			if (this.longitude < -180 || this.longitude > 180) {
				throw new GeoAssistException("not a valid longitude value");
			}

			return new Point(this);
		}
	}
}
