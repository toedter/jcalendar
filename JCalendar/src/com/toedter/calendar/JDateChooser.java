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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A date chooser containig a date spinner and a button, that makes a JCalendar
 * visible for choosing a date.
 * 
 * @author Kai Toedter
 * @version $LastChangedRevision: 18 $ $LastChangedDate: 2004-12-20 07:58:43
 *          +0100 (Mo, 20 Dez 2004) $
 */
public class JDateChooser extends JPanel implements ActionListener,
        PropertyChangeListener, ChangeListener {
    protected JButton calendarButton;

    protected JTextField textField;

    protected boolean hasSpinner;

    protected JSpinner dateSpinner;

    protected JSpinner.DateEditor editor;

    protected JCalendar jcalendar;

    protected JPopupMenu popup;

    protected SpinnerDateModel model;

    protected SimpleDateFormat dateFormater;

    protected String dateFormatString;

    protected boolean isInitialized;

    protected boolean dateSelected;

    protected Date lastSelectedDate;

    protected ImageIcon icon;

    protected Date selectedDate;

    /**
     * Creates a new JDateChooser object.
     */
    public JDateChooser() {
        this(null, null, null, false);
    }

    /**
     * Creates a new JDateChooser object.
     * 
     * @param spinner
     *            if true, a JSpinner is used to display the date. Otherwise a
     *            textfield is used.
     */
    public JDateChooser(boolean spinner) {
        this(null, null, null, spinner);
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
     *            the date format string or null (then MEDIUM Date format is
     *            used)
     */
    public JDateChooser(Date date, String dateFormatString) {
        this(date, dateFormatString, false);
    }

    /**
     * Creates a new JDateChooser.
     * 
     * @param date
     *            the date or null
     * @param dateFormatString
     *            the date format string or null (then MEDIUM Date format is
     *            used)
     * @param spinner
     *            if true, a JSpinner is used to display the date. Otherwise a
     *            textfield is used.
     */
    public JDateChooser(Date date, String dateFormatString, boolean spinner) {
        this(null, date, dateFormatString, spinner);
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
     * @param spinner
     *            if true, a JSpinner is used to display the date. Otherwise a
     *            textfield is used.
     */
    public JDateChooser(JCalendar jcal, Date date, String dateFormatString,
            boolean spinner) {
        setName("JDateChooser");

        this.hasSpinner = spinner;

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

        dateFormater = (SimpleDateFormat) DateFormat
                .getDateInstance(DateFormat.MEDIUM);

        if (hasSpinner) {
            setModel(new SpinnerDateModel());
            dateSpinner = new JSpinner(model) {
                public void setEnabled(boolean enabled) {
                    super.setEnabled(enabled);
                    calendarButton.setEnabled(enabled);
                }
            };

            editor = (JSpinner.DateEditor) dateSpinner.getEditor();
            dateSpinner.setEditor(editor);
            add(dateSpinner, BorderLayout.CENTER);
        } else {
            textField = new JTextField("");
            add(textField, BorderLayout.CENTER);
        }
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

        calendarButton.setMargin(new Insets(0, 0, 0, 0));
        popup = new JPopupMenu() {
            public void setVisible(boolean b) {
                Boolean isCanceled = (Boolean) getClientProperty("JPopupMenu.firePopupMenuCanceled");

                if (b
                        || (!b && dateSelected)
                        || ((isCanceled != null) && !b && isCanceled
                                .booleanValue())) {
                    super.setVisible(b);
                }
            }
        };

        popup.setLightWeightPopupEnabled(true);

        popup.add(jcalendar);

        lastSelectedDate = date;
        isInitialized = true;
    }

    /**
     * Called when the jalendar button was pressed.
     * 
     * @param e
     *            the action event
     */
    public void actionPerformed(ActionEvent e) {
        int x = calendarButton.getWidth()
                - (int) popup.getPreferredSize().getWidth();
        int y = calendarButton.getY() + calendarButton.getHeight();

        Calendar calendar = Calendar.getInstance();
        if (selectedDate != null) {
            calendar.setTime(selectedDate);
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
        if (hasSpinner) {
            dateSpinner.setLocale(l);
            String tmpDateFormatString = "";
            if (dateFormatString == null) {
                // TODO Localize date format string
                dateFormatString = "MMMMM d, yyyy";
            }

            if (selectedDate != null) {
                tmpDateFormatString = dateFormatString;
            }

            editor = new JSpinner.DateEditor(dateSpinner, tmpDateFormatString);
            dateSpinner.setEditor(editor);
        }
        jcalendar.setLocale(l);
    }

    /**
     * Gets the date format string.
     * 
     * @return Returns the dateFormatString.
     */
    public String getDateFormatString() {
        return dateFormatString;
    }

    /**
     * Sets the date format string. E.g "MMMMM d, yyyy" will result in "July 21,
     * 2004" if this is the selected date and locale is English.
     * 
     * @param dfString
     *            The dateFormatString to set.
     */
    public void setDateFormatString(String dfString) {
        if (dfString == null) {
            dateFormatString = dateFormater.toPattern();
        } else {
            dateFormatString = dfString;
            dateFormater.applyPattern(dateFormatString);
        }
        if (hasSpinner) {
            if (selectedDate != null) {
                editor.getFormat().applyPattern(dateFormatString);
            } else {
                editor.getFormat().applyPattern("");
            }
        }
        invalidate();
    }

    /**
     * Returns the date. If the JDateChooser is started with an empty date and
     * no date is set by the user, null is returned.
     * 
     * @return the current date
     */
    public Date getDate() {
        return selectedDate;
    }

    /**
     * Sets the date. Fires the property change "date" if date != null.
     * 
     * @param date
     *            the new date.
     */
    public void setDate(Date date) {
        this.selectedDate = date;
        if (date != null) {
            if (hasSpinner) {
                editor.getFormat().applyPattern(dateFormatString);
                model.setValue(date);
            } else {
                String formattedDate = dateFormater.format(date);
                textField.setText(formattedDate);
            }
        } else {
            if (hasSpinner) {
                editor.getTextField().setText("");
            } else {
                textField.setText("");
            }
        }
        if (getParent() != null) {
            getParent().invalidate();
        }
    }

    /**
     * Fires property "date" changes, recting on the spinner's state changes.
     * 
     * @param e
     *            the change event
     */
    public void stateChanged(ChangeEvent e) {
        if (isInitialized) {
            firePropertyChange("date", lastSelectedDate, model.getDate());
            lastSelectedDate = model.getDate();
        }
    }

    /*
     * The methods: public JSpinner getSpinner() public SpinnerDateModel
     * getModel() public void setModel(SpinnerDateModel mdl)
     * 
     * were added by Mark Brown on 24 Aug 2004. They were added to allow the
     * setting of the SpinnerDateModel from a source outside the JDateChooser
     * control. This was necessary in order to allow the JDateChooser to be
     * integrated with applications using persistence frameworks like Oracle's
     * ADF/BC4J.
     */

    /**
     * Returns this control's JSpinner control.
     * 
     * @return the JSpinner control
     */
    public JSpinner getSpinner() {
        return dateSpinner;
    }

    /**
     * Returns the SpinnerDateModel associated with this control.
     * 
     * @return the SpinnerDateModel
     */
    public SpinnerDateModel getModel() {
        return model;
    }

    /**
     * Set the SpinnerDateModel for this control. This method allows the
     * JDateChooser control to be used with some persistence frameworks (ie.
     * Oracle ADF) to bind the control to the database Date value.
     * 
     * @param mdl
     *            the SpinnerDateModel
     */
    public void setModel(SpinnerDateModel mdl) {
        model = mdl;
        model.setCalendarField(java.util.Calendar.WEEK_OF_MONTH);
        model.addChangeListener(this);
        if (dateSpinner != null) {
            dateSpinner.setModel(model);
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
        dateSpinner.setEnabled(enabled);
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
        JDateChooser dateChooser = new JDateChooser(null, new Date(), null,
                true);
        // dateChooser.setLocale(new Locale("de"));
        // dateChooser.setDateFormatString("dd. MMMM yyyy");
        frame.getContentPane().add(dateChooser);
        frame.pack();
        frame.setVisible(true);
    }
}
