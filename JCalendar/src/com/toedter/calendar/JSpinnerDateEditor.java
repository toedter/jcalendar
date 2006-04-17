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

public class JSpinnerDateEditor extends JSpinner implements IDateEditor,
		FocusListener {

	protected Date date;

	protected String dateFormatString;

	protected SimpleDateFormat dateFormatter;

	public JSpinnerDateEditor() {
		super(new SpinnerDateModel());
		dateFormatter = (SimpleDateFormat) DateFormat
				.getDateInstance(DateFormat.MEDIUM);
		((JSpinner.DateEditor) getEditor()).getTextField().addFocusListener(
				this);
	}

	public Date getDate() {
		return ((SpinnerDateModel) getModel()).getDate();
	}

	public void setDate(Date date) {
		this.date = date;
		if (date == null) {
			((JSpinner.DateEditor) getEditor()).getFormat().applyPattern("");
			((JSpinner.DateEditor) getEditor()).getTextField().setText("");
		} else {
			((JSpinner.DateEditor) getEditor()).getFormat().applyPattern(
					dateFormatString);
			((SpinnerDateModel) getModel()).setValue(date);
		}
	}

	public void setDateFormatString(String dateFormatString) {
		if (dateFormatString == null) {
			this.dateFormatString = dateFormatter.toPattern();
		} else {
			this.dateFormatString = dateFormatString;
			dateFormatter.applyPattern(this.dateFormatString);
		}
		if (date != null) {
			((JSpinner.DateEditor) getEditor()).getFormat().applyPattern(
					this.dateFormatString);
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
		dateFormatter = (SimpleDateFormat) DateFormat.getDateInstance(
				DateFormat.MEDIUM, locale);
		setDateFormatString(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
	 */
	public void focusLost(FocusEvent focusEvent) {
		String text = ((JSpinner.DateEditor) getEditor()).getTextField()
				.getText();
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
