package com.nec.edgedisplay.util.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.nec.edgedisplay.utils.AgeUtils;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UtilTest {

	@Test
	public void ageTest() {

		assertEquals("22-27", AgeUtils.parseAge("22 to 27"));
	}

}
