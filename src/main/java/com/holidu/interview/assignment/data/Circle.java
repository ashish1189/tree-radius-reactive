package com.holidu.interview.assignment.data;

import lombok.Data;

@Data
public class Circle {

	private double xMax;
	private double xMin;
	private double yMax;
	private double yMin;
	
	/**
	 * A circle is represented in cartesian coordinates as: (x − a)^2 + (y − b)^2 = r^2
	 * (a,b) is the center point.
	 * 
	 * Ref. https://en.wikipedia.org/wiki/Cartesian_coordinate_system#/media/File:Cartesian-coordinate-system-with-circle.svg
	 *
	 * @param x
	 * @param y
	 * @param radius
	 */
	public Circle (double x, double y, double radius) {
		this.xMax = x + radius;
		this.xMin = x - radius;
		this.yMax = y + radius;
		this.yMin = y - radius;
	}
	
}
