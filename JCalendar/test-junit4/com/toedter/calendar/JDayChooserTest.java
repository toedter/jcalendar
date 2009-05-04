/*
 *  Copyright (C) 2009 Kai Toedter
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package com.toedter.calendar;

import static org.junit.Assert.assertEquals;
import junit.framework.JUnit4TestAdapter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JDayChooserTest {

	JDayChooser jDayChooser;

	@Before
	public void setUp() throws Exception {
		jDayChooser = new JDayChooser();
	}

	@After
	public void tearDown() throws Exception {
		jDayChooser = null;
	}

	@Test
	public void testMonthJump() {
		jDayChooser = new JDayChooser();
		jDayChooser.setMonth(4); // May
		jDayChooser.setDay(31);
		jDayChooser.setMonth(5); // June
		assertEquals(30, jDayChooser.getDay());
		jDayChooser.setMonth(1); // February
		assertEquals(28, jDayChooser.getDay());
	}

	public static void main(String... args) {
		junit.textui.TestRunner.run(suite());
	}

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(JDayChooserTest.class);
	}
}
