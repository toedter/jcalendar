package com.toedter.calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.ImageIcon;

import junit.framework.JUnit4TestAdapter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JDateChooserTest {

	JDateChooser jDateChooser;

	@Before
	public void setUp() throws Exception {
		jDateChooser = new JDateChooser();
	}

	@After
	public void tearDown() throws Exception {
		jDateChooser = null;
	}

	@Test
	public void testDateConstructor() {
		jDateChooser = new JDateChooser((Date) null); // null date
		assertEquals(null, jDateChooser.getDate());

		Date date = new Date();
		jDateChooser = new JDateChooser(date);
		assertEquals(date, jDateChooser.getDate());
	}

	@Test
	public void testDateEditorConstructor() {
		jDateChooser = new JDateChooser((IDateEditor) null); // null date
		assertEquals(null, jDateChooser.getDate());

		IDateEditor jSpinnerDateEditor = new JSpinnerDateEditor();
		jDateChooser = new JDateChooser(jSpinnerDateEditor);
		assertEquals(jSpinnerDateEditor, jDateChooser.getDateEditor());
	}

	@Test
	public void testDateDateFormateStringConstructor() {
		jDateChooser = new JDateChooser(null, null);
		String defaultFormat = ((SimpleDateFormat) DateFormat.getDateInstance(DateFormat.MEDIUM))
				.toPattern();

		assertEquals(null, jDateChooser.getDate());
		assertEquals(defaultFormat, jDateChooser.getDateFormatString());

		Date date = new Date();
		String dateFormatString = "dd/MM/yy";
		jDateChooser = new JDateChooser(date, dateFormatString);
		assertEquals(date, jDateChooser.getDate());
		assertEquals(dateFormatString, jDateChooser.getDateFormatString());
	}

	@Test
	public void testDatePatternConstructor() {
		String datePattern = "dd/MM/yy";
		String dateMask = "##/##/##";
		jDateChooser = new JDateChooser(datePattern, dateMask, '_');

		assertEquals(datePattern, jDateChooser.getDateFormatString());
		assertEquals(datePattern, jDateChooser.getDateEditor().getDateFormatString());
	}

	@Test
	public void testBigConstructor() {
		String datePattern = "dd/MM/yy";
		Date date = new Date();
		JCalendar jcalendar = new JCalendar();
		IDateEditor jSpinnerDateEditor = new JSpinnerDateEditor();

		jDateChooser = new JDateChooser(jcalendar, date, datePattern, jSpinnerDateEditor);

		assertEquals(datePattern, jDateChooser.getDateFormatString());
		assertEquals(datePattern, jDateChooser.getDateEditor().getDateFormatString());
		assertEquals(date, jDateChooser.getDate());
		assertEquals(jcalendar, jDateChooser.getJCalendar());
		assertEquals(date, jcalendar.getDate());
		assertEquals(jSpinnerDateEditor, jDateChooser.getDateEditor());
	}

	@Test
	public void testSetGetDateFormatString() throws Exception {
		String[] tests = { "dd.MM.yyyy", "MM/dd/yy", new String() };

		for (int i = 0; i < tests.length; i++) {
			jDateChooser.setDateFormatString(tests[i]);
			assertEquals(tests[i], jDateChooser.getDateFormatString());
		}

		String[] tests2 = { null, "MM/xdd/yy" };
		String defaultFormat = ((SimpleDateFormat) DateFormat.getDateInstance(DateFormat.MEDIUM))
				.toPattern();

		for (int i = 0; i < tests2.length; i++) {
			jDateChooser.setDateFormatString(tests2[i]);
			assertEquals(defaultFormat, jDateChooser.getDateFormatString());
		}
	}

	@Test
	public void testSetGetDate()  {
		Date[] tests = { new Date(), null };

		for (int i = 0; i < tests.length; i++) {
			jDateChooser.setDate(tests[i]);
			assertEquals(tests[i], jDateChooser.getDate());
		}
	}

	@Test
	public void testSetGetFont()  {
		Font[] tests = { Font.decode(""), Font.decode("Arial"), null };

		for (int i = 0; i < tests.length; i++) {
			jDateChooser.setFont(tests[i]);
			assertEquals(tests[i], jDateChooser.getFont());
		}
	}

	@Test
	public void testSetGetCalendar() throws Exception {
		Calendar[] tests = { Calendar.getInstance(), null };

		for (int i = 0; i < tests.length; i++) {
			jDateChooser.setCalendar(tests[i]);
			assertEquals(tests[i], jDateChooser.getCalendar());
		}
	}

	@Test
	public void testSetIsEnabled() throws Exception {
		boolean[] tests = { true, false };

		for (int i = 0; i < tests.length; i++) {
			jDateChooser.setEnabled(tests[i]);
			assertEquals(tests[i], jDateChooser.isEnabled());
		}
	}

	@Test
	public void testSetIcon() throws Exception {
		URL iconURL = jDateChooser.getClass().getResource(
				"/com/toedter/calendar/images/JMonthChooserColor32.gif");
		ImageIcon icon = new ImageIcon(iconURL);
		jDateChooser.setIcon(icon);
		assertEquals(icon, jDateChooser.getCalendarButton().getIcon());
	}

	@Test(expected = NullPointerException.class)
	public void testSetGetLocale()  {
		Locale[] tests = { Locale.getDefault(), Locale.GERMAN, Locale.US };

		for (int i = 0; i < tests.length; i++) {
			jDateChooser.setLocale(tests[i]);
			assertEquals(tests[i], jDateChooser.getLocale());
		}

		jDateChooser.setLocale(null);
	}

	@Test
	public void testAddPropertyChangeListener() throws Exception {
		class MyListener implements PropertyChangeListener {
			boolean called = false;

			public void propertyChange(PropertyChangeEvent event) {
				called = true;
			}
		}
		MyListener listener = new MyListener();
		JDateChooser dateChooser = new JDateChooser();
		dateChooser.addPropertyChangeListener("date", listener);
		dateChooser.setDate(new Date());
		assertTrue("Listener was not called", listener.called);

		dateChooser.removePropertyChangeListener("date", listener);
		dateChooser.addPropertyChangeListener(listener);
		listener.called = false;
		dateChooser.setDate(new Date(System.currentTimeMillis() - 100));
		assertTrue("Listener was not called", listener.called);
	}

	@Test
	public void testRemovePropertyChangeListener() throws Exception {
		class MyListener implements PropertyChangeListener {
			boolean called = false;

			public void propertyChange(PropertyChangeEvent event) {
				called = true;
			}
		}
		MyListener listener = new MyListener();
		JDateChooser dateChooser = new JDateChooser();
		dateChooser.addPropertyChangeListener(listener);
		dateChooser.removePropertyChangeListener(listener);
		dateChooser.addPropertyChangeListener("date", listener);
		dateChooser.removePropertyChangeListener("date", listener);
		dateChooser.setDate(new Date());
		assertFalse("Listener was called", listener.called);
	}

	@Test
	public void testNullDateWithSpinnerEditor() {
		new JDateChooser(new JCalendar(), null, "", new JSpinnerDateEditor());
	}

	@Test
	public void testNullDate() {
		JDateChooser jDateChooser = new JDateChooser(new JSpinnerDateEditor());
		jDateChooser.setDate(null);
		assertNull("Date is not null", jDateChooser.getDate());
	}

	public static void main(String... args) {
		junit.textui.TestRunner.run(suite());
	}

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(JDateChooserTest.class);
	}
}
