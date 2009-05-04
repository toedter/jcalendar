package com.toedter.components;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for com.toedter.components");
		//$JUnit-BEGIN$
		suite.addTestSuite(JSpinFieldTest.class);
		//$JUnit-END$
		return suite;
	}

}
