package com.toedter;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for com.toedter");
		//$JUnit-BEGIN$
		suite.addTest(com.toedter.components.AllTests.suite());
		suite.addTest(com.toedter.calendar.AllTests.suite());
		//$JUnit-END$
		return suite;
	}

}
