package com.pool;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtils {

	private static String _DATE_FORMAT = "yyyy-MMM-dd HH:mm:ss";

	public static Date convertGMTDateToOtherTimeZone(Date date,
			TimeZone timezone) {
		Date todayDate = null;
		// GMT time zone
		SimpleDateFormat dateFormatGmt = new SimpleDateFormat(_DATE_FORMAT);
		dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
		// Local time zone
		SimpleDateFormat dateFormatLocal = new SimpleDateFormat(_DATE_FORMAT);
		dateFormatLocal.setTimeZone(timezone);
		try {
			todayDate = dateFormatGmt.parse(dateFormatLocal.format(date));
		} catch (ParseException e) {
		}
		return todayDate;
	}

	public static Date convertLocaleDateToGMT(Date date) {
		Date todayDate = null;
		// GMT time zone
		SimpleDateFormat dateFormatGmt = new SimpleDateFormat(_DATE_FORMAT);
		dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
		// Local time zone
		SimpleDateFormat dateFormatLocal = new SimpleDateFormat(_DATE_FORMAT);

		try {
			todayDate = dateFormatLocal.parse(dateFormatGmt.format(date));
		} catch (ParseException e) {
		}
		return todayDate;
	}

	/**
	 * This method uses the same date format as used by project Explorer
	 * 
	 * @param date
	 * @param locale
	 * @param timeZone
	 * @return
	 */
	public static String formatDate(Date date, Locale locale) {
		DateFormat formatter = safeNewDefaultDateFormat(DateFormat.SHORT,
				DateFormat.MEDIUM, locale);
		return formatter.format(date);
	}

	public static Date getCurrentDateInGMT() {
		return convertLocaleDateToGMT(new Date());
	}

	/**
	 * A method to construct a date format
	 */
	public static DateFormat safeNewDefaultDateFormat(int dateStyle,
			int timeStyle, Locale locale) {
		DateFormat dateFormat = DateFormat.getDateTimeInstance(dateStyle,
				timeStyle, locale);

		return dateFormat;
	}

}
