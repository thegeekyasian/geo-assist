package com.thegeekyasian.geoassist.kdtree.geometry;

import com.thegeekyasian.geoassist.core.GeoAssistException;

/**
 * @author thegeekyasian
 */
public class BoundingBox {

	private Point lowerPoint;

	private Point upperPoint;

	public BoundingBox(Builder builder) {
		this.lowerPoint = builder.lowerPoint;
		this.upperPoint = builder.upperPoint;
	}

	public Point getLowerPoint() {
		return lowerPoint;
	}

	public void setLowerPoint(Point lowerPoint) {
		this.lowerPoint = lowerPoint;
	}

	public Point getUpperPoint() {
		return upperPoint;
	}

	public void setUpperPoint(Point upperPoint) {
		this.upperPoint = upperPoint;
	}

	public static class Builder {
		private Point lowerPoint;

		private Point upperPoint;

		public Builder lowerPoint(Point point) {
			this.lowerPoint = point;
			return this;
		}

		public Builder upperPoint(Point point) {
			this.upperPoint = point;
			return this;
		}

		public BoundingBox build() {
			if (this.lowerPoint == null) {
				throw new GeoAssistException("lowerPoint can not be null");
			}
			if (this.upperPoint == null) {
				throw new GeoAssistException("upperPoint can not be null");
			}
			return new BoundingBox(this);
		}
	}
}
