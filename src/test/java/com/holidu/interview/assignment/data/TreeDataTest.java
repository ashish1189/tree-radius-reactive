package com.holidu.interview.assignment.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class TreeDataTest {

	@Test
	void testObject() {
		TreeData data = new TreeData("maple", "100", "100");
		
		assertNotNull(data);
		assertEquals("100", data.getXSp());
		assertEquals("100", data.getYSp());
		assertEquals("maple", data.getSpcCommon());
	}
	
	@Test
	void testSetterGetters() {
		TreeData data = new TreeData();

		data.setXSp("100");
		data.setYSp("100");
		data.setSpcCommon("willow oak");

		assertEquals("100", data.getXSp());
		assertEquals("100", data.getYSp());
		assertEquals("willow oak", data.getSpcCommon());
	}
	
	@Test
	void testDefaultTreeName() {
		TreeData data = new TreeData();

		data.setSpcCommon("Default");

		assertEquals("Default", data.getSpcCommon());
	}
}
