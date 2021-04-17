package com.holidu.interview.assignment.utils;

import org.springframework.stereotype.Service;

import com.holidu.interview.assignment.data.Circle;

@Service
public class Utilities {

	/**
	 * Convert meter to foot
	 * 
	 * 1 meter = 3.28084 ~ 3.281 feet
	 * 
	 * @param radius
	 * @return
	 */
	public double metersToFeet(double radius) {
		return radius * 3.281;
	}
	
	/**
	 * Method prepares $where parameter of the 3rd party service.
	 * This is used to limit the search area
	 * 
	 * @param circle
	 * @return 
	 */
	public String getWhereClause(Circle circle) {
		
		return 	"x_sp <= " + Double.toString(circle.getXMax()) + " AND " + 
				"x_sp >= " + Double.toString(circle.getXMin()) + " AND " + 
				"y_sp <= " + Double.toString(circle.getYMax()) + " AND " + 
				"y_sp >= " + Double.toString(circle.getYMin());
	}

}
