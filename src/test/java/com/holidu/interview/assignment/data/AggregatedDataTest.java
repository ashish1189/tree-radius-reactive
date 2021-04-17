package com.holidu.interview.assignment.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class AggregatedDataTest {

	@Test
	void testObject() {
		AggregatedData data = new AggregatedData("maple", 300);
		
		assertNotNull(data);
		assertEquals(300, data.getCount());
		assertEquals("maple", data.getSpcCommon());
	}
	
	@Test
	void testSetterGetters() {
		AggregatedData data = new AggregatedData();
		
		data.setSpcCommon("willow oak");
		data.setCount(336);

		assertEquals(336, data.getCount());
		assertEquals("willow oak", data.getSpcCommon());
	}
}
