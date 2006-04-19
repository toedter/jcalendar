package com.toedter.calendar;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

/**
 * JSpinnerDateEditor is a date editor based on a JSpinner.
 * 
 * @author Kai Toedter
 * @version $LastChangedRevision$ $LastChangedDate$
 */
public class JSpinnerDateEditor extends JSpinner implements IDateEditor, FocusListener {

	private static final long serialVersionUID = 5692204052306085316L;
	protected Date date;
	protected String dateFormatString;
	protected SimpleDateFormat dateFormatter;

	public JSpinnerDateEditor() {
		super(new SpinnerDateModel());
		dateFormatter = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.MEDIUM);
		((JSpinner.DateEditor) getEditor()).getTextField().addFocusListener(this);
	}

	public Date getDate() {
		if (date == null) {
			return null;
		}
		return ((SpinnerDateModel) getModel()).getDate();
	}

	public void setDate(Date date) {
		this.date = date;
		if (date == null) {
			((JSpinner.DateEditor) getEditor()).getFormat().applyPattern("");
			((JSpinner.DateEditor) getEditor()).getTextField().setText("");
		} else {
			if (dateFormatString != null) {
				((JSpinner.DateEditor) getEditor()).getFormat().applyPattern(dateFormatString);
			}
			((SpinnerDateModel) getModel()).setValue(date);
		}
	}

	public void setDateFormatString(String dateFormatString) {
		try {
			dateFormatter.applyPattern(dateFormatString);
		} catch (RuntimeException e) {
			dateFormatter = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.MEDIUM);
			dateFormatter.setLenient(false);
		}
		this.dateFormatString = dateFormatter.toPattern();
		setToolTipText(this.dateFormatString);

		if (date != null) {
			((JSpinner.DateEditor) getEditor()).getFormat().applyPattern(this.dateFormatString);
		} else {
			((JSpinner.DateEditor) getEditor()).getFormat().applyPattern("");
		}

		if (date != null) {
			String text = dateFormatter.format(date);
			((JSpinner.DateEditor) getEditor()).getTextField().setText(text);
		}
	}

	public String getDateFormatString() {
		return dateFormatString;
	}

	public JComponent getUiComponent() {
		return this;
	}

	public void setLocale(Locale locale) {
		super.setLocale(locale);
		dateFormatter = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
		setDateFormatString(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
	 */
	public void focusLost(FocusEvent focusEvent) {
		String text = ((JSpinner.DateEditor) getEditor()).getTextField().getText();
		if (text.length() == 0) {
			setDate(null);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
	public void focusGained(FocusEvent e) {
	}

}
