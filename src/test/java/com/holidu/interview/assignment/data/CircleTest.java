package com.holidu.interview.assignment.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class CircleTest {

	@Test
	void testCircleObject() {
		Circle circle = new Circle(1, 1, 2);
		
		assertNotNull(circle);
		assertEquals(3, circle.getXMax());
		assertEquals(-1, circle.getXMin());
		assertEquals(3, circle.getYMax());
		assertEquals(-1, circle.getYMin());
	}
	
	@Test
	void testSetterGetters() {
		Circle circle = new Circle(1, 1, 2);
		
		circle.setXMax(20);
		circle.setXMin(-20);
		circle.setYMax(20);
		circle.setYMin(-20);
		
		assertEquals(20, circle.getXMax());
		assertEquals(-20, circle.getXMin());
		assertEquals(20, circle.getYMax());
		assertEquals(-20, circle.getYMin());
	}
}
