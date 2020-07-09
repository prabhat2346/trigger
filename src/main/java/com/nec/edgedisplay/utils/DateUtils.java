package com.nec.edgedisplay.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	
	private DateUtils() {}

	public static String getCurrentDay() {
		Date now = new Date();

		SimpleDateFormat simpleDateformat = new SimpleDateFormat("EE"); // the day of the week spelled out completely
		return simpleDateformat.format(now);
	}

	public static Calendar getCurrentTime() {
		return Calendar.getInstance();
	}

	public static boolean checkCurrentTime(final int startHour, final int startMin, final int endHour,
			final int endMin) {
		int currentHour = getCurrentTime().get(Calendar.HOUR_OF_DAY);
		int currentMin = getCurrentTime().get(Calendar.MINUTE);
		if ((startHour < currentHour && endHour > currentHour)) {
			return true;
		} else if (startHour == currentHour && endHour == currentHour) {
				return startMin <= currentMin && endMin >= currentMin;
		} else if (startHour == currentHour) {
			if (startMin <= currentMin) {
				return true;
			}
		} else if (endHour == currentHour && endMin >= currentMin) {
				return true;
		}
		return false;
	}

}
