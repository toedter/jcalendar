package com.toedter.calendar;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for com.toedter.calendar");
		//$JUnit-BEGIN$
		suite.addTestSuite(JDayChooserTest.class);
		suite.addTestSuite(JMonthChooserTest.class);
		suite.addTestSuite(JYearChooserTest.class);
		suite.addTestSuite(JCalendarTest.class);
		suite.addTestSuite(JSpinnerDateEditorTest.class);
		suite.addTestSuite(JTextFieldDateEditorTest.class);
		suite.addTestSuite(JDateChooserTest.class);
		//$JUnit-END$
		return suite;
	}

}
