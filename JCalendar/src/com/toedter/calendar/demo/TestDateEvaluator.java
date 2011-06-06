package com.toedter.calendar.demo;

import java.awt.Color;
import java.util.Calendar;
import java.util.Date;

import com.toedter.calendar.IDateEvaluator;

public class TestDateEvaluator implements IDateEvaluator {

	private Calendar calendar = Calendar.getInstance();
	private Calendar calendar2 = Calendar.getInstance();
	private Color darkGreen = new Color(0x007F00);
	private Color lightGreen = new Color(0xbbebc8);
	private Color darkRed = new Color(0xa60007);
	private Color lightRed = new Color(0xffb1b5);

	public boolean isSpecial(Date date) {
		calendar2.setTime(date);
		for (int i = 2; i < 5; i++) {
			if (calendar.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
					&& calendar.get(Calendar.DAY_OF_MONTH) == calendar2
							.get(Calendar.DAY_OF_MONTH) + i) {
				return true;
			}
		}
		return false;
	}

	public Color getSpecialForegroundColor() {
		return darkGreen;
	}

	public Color getSpecialBackroundColor() {
		return lightGreen;
	}

	public String getSpecialTooltip() {
		return "Special Day!";
	}

	public boolean isInvalid(Date date) {
		calendar2.setTime(date);
		for (int i = 4; i < 6; i++) {
			if (calendar.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
					&& calendar.get(Calendar.DAY_OF_MONTH) == calendar2
					.get(Calendar.DAY_OF_MONTH) + i) {
				return true;
			}
		}
		return false;
	}
	
	public Color getInvalidForegroundColor() {
		return darkRed;
	}

	public Color getInvalidBackroundColor() {
		return null;
	}

	public String getInvalidTooltip() {
		return "You cannot select this date...";
	}


}
