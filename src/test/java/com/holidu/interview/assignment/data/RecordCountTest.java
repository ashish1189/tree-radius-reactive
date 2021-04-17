package com.holidu.interview.assignment.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class RecordCountTest {

	@Test
	void testObject() {
		RecordCount data = new RecordCount(1000);
		
		assertNotNull(data);
		assertEquals(1000, data.getCount());
	}
	
	@Test
	void testSetterGetters() {
		RecordCount data = new RecordCount();

		data.setCount(5555);

		assertEquals(5555, data.getCount());
	}
}
