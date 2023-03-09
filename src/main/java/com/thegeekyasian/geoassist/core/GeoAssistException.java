package com.thegeekyasian.geoassist.core;

/**
 * This is a custom runtime exception for any issues that might arise.
 *
 * @author The Geeky Asian
 */
public class GeoAssistException extends RuntimeException {
	public GeoAssistException(String message) {
		super(message);
	}
}
