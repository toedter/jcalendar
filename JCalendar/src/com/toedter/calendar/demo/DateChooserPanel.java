 /*
 * Copyright (C) Siemens AG 2004
 *
 * Transmittal, reproduction, dissemination and/or editing of this
 * source code as well as utilization of its contents and communication
 * thereof to others without express authorization are prohibited.
 * Offenders will be held liable for payment of damages.  All rights
 * created by patent grant or registration of a utility model or design
 * patent are reserved.
 *
 * Created on Nov 2, 2005
 *
 */
 
package com.toedter.calendar.demo;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.toedter.calendar.JDateChooser;

/**
 * A demonstration panel includeing several JDateChoosers.
 *
 * @author Kai Toedter
 * @version $LastChangedRevision: 12 $ $LastChangedDate: 2004-10-17 22:27:20 +0200 (So, 17 Okt 2004) $
 */
@SuppressWarnings("serial")
public class DateChooserPanel extends JPanel implements PropertyChangeListener {
    private JComponent[] components;
    
    public DateChooserPanel() {
        setName("JDateChooser");
        
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;

        setLayout(gridbag);

        components = new JComponent[5];
        components[0] = new JDateChooser();
        components[1] = new JDateChooser(new Date());
        components[2] = new JDateChooser(null, null, null, true);
        components[3] = new JDateChooser(null, new Date(), null, true);
        components[4] = new DemoTable();
        
        addEntry("Default", components[0], gridbag);
        addEntry("Date was set", components[1], gridbag);
        addEntry("With spinner", components[2], gridbag);
        addEntry("Date set with spinner", components[3], gridbag);
        addEntry("Table with date editors", components[4], gridbag);
        
        
    }
    
    private void addEntry(String text, JComponent component,
            GridBagLayout grid) {
            JLabel label = new JLabel(text + ": ", null, JLabel.RIGHT);
            GridBagConstraints c = new GridBagConstraints();
            c.weightx = 1.0;
            c.fill = GridBagConstraints.BOTH;
            grid.setConstraints(label, c);
            add(label);
            c.gridwidth = GridBagConstraints.REMAINDER;
            grid.setConstraints(component, c);
            add(component);
        }

    /**
     * Gets the date format string.
     * 
     * @return Returns the dateFormatString.
     */
    public String getDateFormatString() {
        return ((JDateChooser)components[0]).getDateFormatString();
    }

    /**
     * Sets the date format string. E.g "MMMMM d, yyyy" will result in "July 21,
     * 2004" if this is the selected date and locale is English.
     * 
     * @param dfString
     *            The dateFormatString to set.
     */
    public void setDateFormatString(String dfString) {
    }

    /**
     * Returns the date. If the JDateChooser is started with an empty date and
     * no date is set by the user, null is returned.
     * 
     * @return the current date
     */
    public Date getDate() {
        return ((JDateChooser)components[0]).getDate();
    }

    /**
     * Sets the date. Fires the property change "date" if date != null.
     * 
     * @param date
     *            the new date.
     */
    public void setDate(Date date) {
    }

    public void propertyChange(PropertyChangeEvent evt) {
        // TODO Auto-generated method stub
        
    }

}
