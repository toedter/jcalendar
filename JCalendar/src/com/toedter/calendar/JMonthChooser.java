/*
 *  JMonthChooser.java  - A bean for choosing a month
 *  Copyright (C) 2004 Kai Toedter
 *  kai@toedter.com
 *  www.toedter.com
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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * JMonthChooser is a bean for choosing a month.
 * 
 * @author Kai Toedter
 * @version 1.2
 */
public class JMonthChooser extends JPanel implements ItemListener, ChangeListener {
	/** true, if the month chooser has a spinner component */
	protected boolean hasSpinner;
	private Locale locale;
	private int month;
	private int oldSpinnerValue = 0;

	// needed for comparison
	private JDayChooser dayChooser;
	private JYearChooser yearChooser;
	private JComboBox comboBox;
	private JSpinner spinner;
	private boolean initialized;
	private boolean localInitialize;

	/**
	 * Default JMonthChooser constructor.
	 */
	public JMonthChooser() {
		this(true);
	}

	/**
	 * JMonthChooser constructor with month spinner parameter.
	 * 
	 * @param hasSpinner
	 *            true, if the month chooser should have a spinner component
	 */
	public JMonthChooser(boolean hasSpinner) {
		super();

		this.hasSpinner = hasSpinner;

		setLayout(new BorderLayout());

		comboBox = new JComboBox();
		comboBox.addItemListener(this);

		// comboBox.addPopupMenuListener(this);
		locale = Locale.getDefault();
		initNames();

		if (hasSpinner) {
			spinner = new JSpinner();
			spinner.addChangeListener(this);
			comboBox.setBorder(new EmptyBorder(0, 0, 0, 0));
			spinner.setEditor(comboBox);
			add(spinner, BorderLayout.WEST);
		} else {
			add(comboBox, BorderLayout.WEST);
		}

		initialized = true;
		setMonth(Calendar.getInstance().get(Calendar.MONTH));
	}

	/**
	 * Initializes the locale specific month names.
	 */
	public void initNames() {
		localInitialize = true;

		DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
		String[] monthNames = dateFormatSymbols.getMonths();

		if (comboBox.getItemCount() == 12) {
			comboBox.removeAllItems();
		}

		for (int i = 0; i < 12; i++) {
			comboBox.addItem(monthNames[i]);
		}

		localInitialize = false;
		comboBox.setSelectedIndex(month);
	}

	/**
	 * Is invoked if the state of the spnner changes.
	 * 
	 * @param e
	 *            the change event.
	 */
	public void stateChanged(ChangeEvent e) {
		SpinnerNumberModel model = (SpinnerNumberModel) ((JSpinner) e.getSource()).getModel();
		int value = model.getNumber().intValue();
		boolean increase = (value > oldSpinnerValue) ? true : false;
		oldSpinnerValue = value;

		int month = getMonth();

		if (increase) {
			month += 1;

			if (month == 12) {
				month = 0;

				if (yearChooser != null) {
					int year = yearChooser.getYear();
					year += 1;
					yearChooser.setYear(year);
				}
			}
		} else {
			month -= 1;

			if (month == -1) {
				month = 11;

				if (yearChooser != null) {
					int year = yearChooser.getYear();
					year -= 1;
					yearChooser.setYear(year);
				}
			}
		}

		setMonth(month);
	}

	/**
	 * The ItemListener for the months.
	 * 
	 * @param e
	 *            the item event
	 */
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			int index = comboBox.getSelectedIndex();

			if ((index >= 0) && (index != month)) {
				setMonth(index, false);
			}
		}
	}

	/**
	 * Sets the month attribute of the JMonthChooser object. Fires a property change "month".
	 * 
	 * @param newMonth
	 *            the new month value
	 * @param select
	 *            true, if the month should be selcted in the combo box.
	 */
	private void setMonth(int newMonth, boolean select) {
		if (!initialized || localInitialize) {
			return;
		}

		int oldMonth = month;
		month = newMonth;

		if (select) {
			comboBox.setSelectedIndex(month);
		}

		if (dayChooser != null) {
			dayChooser.setMonth(month);
		}

		firePropertyChange("month", oldMonth, month);
	}

	/**
	 * Sets the month. This is a bound property.
	 * 
	 * @param newMonth
	 *            the new month value
	 * 
	 * @see #getMonth
	 */
	public void setMonth(int newMonth) {
	    if(newMonth < 0 || newMonth> 11)
	        return;
		setMonth(newMonth, true);
	}

	/**
	 * Returns the month.
	 * 
	 * @return the month value
	 */
	public int getMonth() {
		return month;
	}

	/**
	 * Convenience method set a day chooser.
	 * 
	 * @param dayChooser
	 *            the day chooser
	 */
	public void setDayChooser(JDayChooser dayChooser) {
		this.dayChooser = dayChooser;
	}

	/**
	 * Convenience method set a year chooser. If set, the spin for the month buttons will spin
	 * the year as well
	 * 
	 * @param yearChooser
	 *            the new yearChooser value
	 */
	public void setYearChooser(JYearChooser yearChooser) {
		this.yearChooser = yearChooser;
	}

	/**
	 * Returns the locale.
	 * 
	 * @return the locale value
	 * 
	 * @see #setLocale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * Set the locale and initializes the new month names.
	 * 
	 * @param l
	 *            the new locale value
	 * 
	 * @see #getLocale
	 */
	public void setLocale(Locale l) {
		if (!initialized) {
			super.setLocale(l);
		} else {
			locale = l;
			initNames();
		}
	}

	/**
	 * Enable or disable the JMonthChooser.
	 * 
	 * @param enabled
	 *            the new enabled value
	 */
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		comboBox.setEnabled(enabled);

		if (spinner != null) {
			spinner.setEnabled(enabled);
		}
	}

	/**
	 * Returns the month chooser's comboBox text area (which allow the focus to
	 * be set to it).
	 * 
	 * @return the combo box
	 */
	public Component getComboBox() {
		return this.comboBox;
	}

	/**
	 * Returns the month chooser's comboBox bar (which allow the focus to be set
	 * to it).
	 * 
	 * @return Component the spinner or null, if the month chooser has no
	 *         spinner
	 */
	public Component getSpinner() {
		// Returns <null> if there is no spinner.
		return spinner;
	}

	/**
	 * Returns the type of spinner the month chooser is using.
	 * 
	 * @return true, if the month chooser has a spinner
	 */
	public boolean hasSpinner() {
		return hasSpinner;
	}

    /**
     * Returns "JMonthChooser".
     *
     * @return the name value
     */
    public String getName() {
        return "JMonthChooser";
    }

	/**
	 * Creates a JFrame with a JMonthChooser inside and can be used for testing.
	 * 
	 * @param s
	 *            The command line arguments
	 */
	public static void main(String[] s) {
		JFrame frame = new JFrame("MonthChooser");
		frame.getContentPane().add(new JMonthChooser());
		frame.pack();
		frame.setVisible(true);
	}
}