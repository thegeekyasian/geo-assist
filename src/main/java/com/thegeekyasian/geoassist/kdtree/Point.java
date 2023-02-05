package com.thegeekyasian.geoassist.kdtree;

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

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
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
			return new Point(this);
		}
	}
}
