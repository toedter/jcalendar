/*
 *  JDateChooser.java  - A bean for choosing a date
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
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A date chooser containig a date editor and a button, that makes a JCalendar
 * visible for choosing a date. If no date editor is specified, a
 * JTextFieldDateEditor is used as default.
 * 
 * @author Kai Toedter
 * @version $LastChangedRevision: 18 $ $LastChangedDate: 2004-12-20 07:58:43
 *          +0100 (Mo, 20 Dez 2004) $
 */
public class JDateChooser extends JPanel implements ActionListener, PropertyChangeListener,
		FocusListener {

	private static final long serialVersionUID = -4306412745720670722L;

	protected IDateEditor dateEditor;

	protected JButton calendarButton;

	protected JCalendar jcalendar;

	protected JPopupMenu popup;

	protected boolean isInitialized;

	protected boolean dateSelected;

	protected Date lastSelectedDate;

	protected ImageIcon icon;

	/**
	 * Creates a new JDateChooser object. By default, no date is set and the
	 * textfield is empty.
	 */
	public JDateChooser() {
		this(null, null, null, null);
	}

	/**
	 * Creates a new JDateChooser object.
	 * 
	 * @param dateEditor
	 *            the dateEditor to be used used to display the date. if null, a
	 *            JTextFieldDateEditor is used.
	 */
	public JDateChooser(IDateEditor dateEditor) {
		this(null, null, null, dateEditor);
	}

	/**
	 * Creates a new JDateChooser.
	 * 
	 * @param date
	 *            the date or null
	 */
	public JDateChooser(Date date) {
		this(date, null);
	}

	/**
	 * Creates a new JDateChooser.
	 * 
	 * @param date
	 *            the date or null
	 * @param dateFormatString
	 *            the date format string or null (then MEDIUM SimpleDateFormat
	 *            format is used)
	 */
	public JDateChooser(Date date, String dateFormatString) {
		this(date, dateFormatString, null);
	}

	/**
	 * Creates a new JDateChooser.
	 * 
	 * @param date
	 *            the date or null
	 * @param dateFormatString
	 *            the date format string or null (then MEDIUM SimpleDateFormat
	 *            format is used)
	 * @param dateEditor
	 *            the dateEditor to be used used to display the date. if null, a
	 *            JTextFieldDateEditor is used.
	 */
	public JDateChooser(Date date, String dateFormatString, IDateEditor dateEditor) {
		this(null, date, dateFormatString, dateEditor);
	}

	/**
	 * Creates a new JDateChooser. If the JDateChooser is created with this
	 * constructor, the mask will be always visible in the date editor. Please
	 * note that the date pattern and the mask will not be changed if the locale
	 * of the JDateChooser is changed.
	 * 
	 * @param datePattern
	 *            the date pattern, e.g. "MM/dd/yy"
	 * @param maskPattern
	 *            the mask pattern, e.g. "##/##/##"
	 * @param placeholder
	 *            the placeholer charachter, e.g. '_'
	 */
	public JDateChooser(String datePattern, String maskPattern, char placeholder) {
		this(null, null, datePattern, new JTextFieldDateEditor(datePattern, maskPattern,
				placeholder));
	}

	/**
	 * Creates a new JDateChooser.
	 * 
	 * @param jcal
	 *            the JCalendar to be used
	 * @param date
	 *            the date or null
	 * @param dateFormatString
	 *            the date format string or null (then MEDIUM Date format is
	 *            used)
	 * @param dateEditor
	 *            the dateEditor to be used used to display the date. if null, a
	 *            JTextFieldDateEditor is used.
	 */
	public JDateChooser(JCalendar jcal, Date date, String dateFormatString, IDateEditor dateEditor) {
		setName("JDateChooser");

		this.dateEditor = dateEditor;
		if (this.dateEditor == null) {
			this.dateEditor = new JTextFieldDateEditor();
		}

		if (jcal == null) {
			jcalendar = new JCalendar(date);
		} else {
			jcalendar = jcal;
			jcalendar.setDate(date);
		}

		setLayout(new BorderLayout());

		jcalendar.getDayChooser().addPropertyChangeListener(this);
		// always fire"day" property even if the user selects
		// the already selected day again
		jcalendar.getDayChooser().setAlwaysFireDayProperty(true);

		setDateFormatString(dateFormatString);
		setDate(date);

		// Display a calendar button with an icon
		if (icon == null) {
			URL iconURL = getClass().getResource(
					"/com/toedter/calendar/images/JDateChooserIcon.gif");
			icon = new ImageIcon(iconURL);
		}

		calendarButton = new JButton(icon);
		calendarButton.setMargin(new Insets(0, 0, 0, 0));
		calendarButton.addActionListener(this);

		// Alt + 'C' selects the calendar.
		calendarButton.setMnemonic(KeyEvent.VK_C);

		add(calendarButton, BorderLayout.EAST);
		add(this.dateEditor.getUiComponent(), BorderLayout.CENTER);

		calendarButton.setMargin(new Insets(0, 0, 0, 0));
		calendarButton.addFocusListener(this);
		
		popup = new JPopupMenu() {
			private static final long serialVersionUID = -6078272560337577761L;

			public void setVisible(boolean b) {
				Boolean isCanceled = (Boolean) getClientProperty("JPopupMenu.firePopupMenuCanceled");
				if (b || (!b && dateSelected)
						|| ((isCanceled != null) && !b && isCanceled.booleanValue())) {
					super.setVisible(b);
				}
			}
		};

		popup.setLightWeightPopupEnabled(true);

		popup.add(jcalendar);

		lastSelectedDate = date;

		// Corrects a problem that occured when the JMonthChooser's combobox is
		// displayed, and a click outside the popup does not close it.

		// The following code was provided by forum user podiatanapraia:
		MenuSelectionManager.defaultManager().addChangeListener(new ChangeListener() {
			boolean hasListened = false;

			public void stateChanged(ChangeEvent e) {
				if (hasListened) {
					hasListened = false;
					return;
				}
				if (popup.isVisible()
						&& JDateChooser.this.jcalendar.monthChooser.getComboBox().hasFocus()) {

					System.out.println(".stateChanged(): 2");

					MenuElement[] me = MenuSelectionManager.defaultManager().getSelectedPath();
					MenuElement[] newMe = new MenuElement[me.length + 1];
					newMe[0] = popup;
					for (int i = 0; i < me.length; i++) {
						newMe[i + 1] = me[i];
					}
					hasListened = true;
					MenuSelectionManager.defaultManager().setSelectedPath(newMe);
				}
			}
		});
		// end of code provided by forum user podiatanapraia

		isInitialized = true;
	}

	/**
	 * Called when the jalendar button was pressed.
	 * 
	 * @param e
	 *            the action event
	 */
	public void actionPerformed(ActionEvent e) {
		int x = calendarButton.getWidth() - (int) popup.getPreferredSize().getWidth();
		int y = calendarButton.getY() + calendarButton.getHeight();

		Calendar calendar = Calendar.getInstance();
		Date date = dateEditor.getDate();
		if (date != null) {
			calendar.setTime(date);
		}
		jcalendar.setCalendar(calendar);
		popup.show(calendarButton, x, y);
		dateSelected = false;
	}

	/**
	 * Listens for a "date" property change or a "day" property change event
	 * from the JCalendar. Updates the dateSpinner and closes the popup.
	 * 
	 * @param evt
	 *            the event
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("day")) {
			if (popup.isVisible()) {
				dateSelected = true;
				popup.setVisible(false);
				setDate(jcalendar.getCalendar().getTime());
			}
		} else if (evt.getPropertyName().equals("date")) {
			setDate((Date) evt.getNewValue());
		}
	}

	/**
	 * Updates the UI of itself and the popup.
	 */
	public void updateUI() {
		super.updateUI();

		if (jcalendar != null) {
			SwingUtilities.updateComponentTreeUI(popup);
		}
	}

	/**
	 * Sets the locale.
	 * 
	 * @param l
	 *            The new locale value
	 */
	public void setLocale(Locale l) {
		dateEditor.setLocale(l);
		jcalendar.setLocale(l);
	}

	/**
	 * Gets the date format string.
	 * 
	 * @return Returns the dateFormatString.
	 */
	public String getDateFormatString() {
		return dateEditor.getDateFormatString();
	}

	/**
	 * Sets the date format string. E.g "MMMMM d, yyyy" will result in "July 21,
	 * 2004" if this is the selected date and locale is English.
	 * 
	 * @param dfString
	 *            The dateFormatString to set.
	 */
	public void setDateFormatString(String dfString) {
		dateEditor.setDateFormatString(dfString);
		invalidate();
	}

	/**
	 * Returns the date. If the JDateChooser is started with a null date and no
	 * date was set by the user, null is returned.
	 * 
	 * @return the current date
	 */
	public Date getDate() {
		return dateEditor.getDate();
	}

	/**
	 * Sets the date. Fires the property change "date" if date != null.
	 * 
	 * @param date
	 *            the new date.
	 */
	public void setDate(Date date) {
		dateEditor.setDate(date);
		if (getParent() != null) {
			getParent().invalidate();
		}
	}

	/**
	 * Returns the calendar. If the JDateChooser is started with a null date (or
	 * null calendar) and no date was set by the user, null is returned.
	 * 
	 * @return the current calendar
	 */
	public Calendar getCalendar() {
		Date date = getDate();
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	/**
	 * Sets the calendar. Value null will set the null date on the date editor.
	 * 
	 * @param calendar
	 *            the calendar.
	 */
	public void setCalendar(Calendar calendar) {
		if (calendar == null) {
			dateEditor.setDate(null);
		} else {
			dateEditor.setDate(calendar.getTime());
		}
	}

	/**
	 * Enable or disable the JDateChooser.
	 * 
	 * @param enabled
	 *            the new enabled value
	 */
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		dateEditor.setEnabled(enabled);
		calendarButton.setEnabled(enabled);
	}

	/**
	 * Returns true, if enabled.
	 * 
	 * @return true, if enabled.
	 */
	public boolean isEnabled() {
		return super.isEnabled();
	}

	/**
	 * @param icon
	 *            The icon to set.
	 */
	public void setIcon(ImageIcon icon) {
		this.icon = icon;
	}

	/**
	 * Creates a JFrame with a JDateChooser inside and can be used for testing.
	 * 
	 * @param s
	 *            The command line arguments
	 */
	public static void main(String[] s) {
		JFrame frame = new JFrame("JDateChooser");
		// JDateChooser dateChooser = new JDateChooser();
		JDateChooser dateChooser = new JDateChooser(null, new Date(), null, null);
		// dateChooser.setLocale(new Locale("de"));
		// dateChooser.setDateFormatString("dd. MMMM yyyy");
		frame.getContentPane().add(dateChooser);
		frame.pack();
		frame.setVisible(true);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		dateEditor.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		dateEditor.removePropertyChangeListener(listener);
	}

	/**
	 * Delegates the focus of the button to the next component.
	 * 
	 * @param event
	 *            the focus event
	 */
	public void focusGained(FocusEvent event) {
		KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
	}

	public void focusLost(FocusEvent e) {
		// do nothing
	}

}
