/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

public class UtilityDate {
	public static Date convertFromStringToData(String data, String format) throws ParseException {
		DateFormat df = new SimpleDateFormat(format);
		Date startDate;
		startDate = df.parse(data);
		return startDate;
	}

	public static String convertFromDateToString(Date data, String format) throws ParseException {
		DateFormat df = new SimpleDateFormat(format);
		return df.format(data);
	}

	public static String currentDateGenerator(String format) {
		DateFormat formatDate = new SimpleDateFormat(format);
		Date date;
		date = new Date();
		return formatDate.format(date);
	}

	public static long convertFromStringToTimestamp(String stringData, String format) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat(format);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date date = dateFormat.parse(stringData);
		return date.getTime();
	}

	public static String convertDateFormat(String oldFormat, String newFormat, String date) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat(oldFormat);
		Date newDate = df.parse(date);
		df.applyPattern(newFormat);
		return df.format(newDate);
	}

	public static String convertFromFeatureFileDateToJsonFileDate(Date data, String format) {
		DateFormat df2 = new SimpleDateFormat(format);
		return df2.format(data);
	}

	public static boolean isThisDateValid(String dateToValidate, String dateFromat) {
		if (dateToValidate == null) {
			return false;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
		sdf.setLenient(false);
		try {
			// if not valid, it will throw ParseException
			Date date = sdf.parse(dateToValidate);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	public static String setSpeficicDayDateFromCurrentDateString(String format, int amount) {
		DateFormat formatDate = new SimpleDateFormat(format);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, amount);
		c.set(Calendar.HOUR_OF_DAY, 10);
		return formatDate.format(c.getTime());
	}

	public static String lastTwoDigitCurrentYear() {
		DateFormat df = new SimpleDateFormat("yy"); // Just the year, with 2
													// digits
		String formattedDate = df.format(Calendar.getInstance().getTime());
		return formattedDate;
	}

	// in order to compare two date date1<date2, this method accepts in input a
	// list of date
	// in this format MM/dd/aaaa and return a list of date in this format
	// aaaammgg
	// (e.g. 04/20/1952 and 04/20/1972 -> 19520420 and 19720420 => 19520420 is <
	// 19720420)
	public static List<String> manageDateForCompare(List<String> dateForCompare) {
		List<String> listDobSortedemp = new LinkedList<String>();
		for (int i = 0; i < dateForCompare.size(); i++) {
			String yearOne = dateForCompare.get(i).substring(6, 10);
			String dayOne = dateForCompare.get(i).substring(3, 5);
			String monthOne = dateForCompare.get(i).substring(0, 2);
			String newDateForCompare = yearOne + monthOne + dayOne;
			listDobSortedemp.add(i, newDateForCompare);
		}
		return listDobSortedemp;
	}

	// Makes the future date with n. day from current date (MM/dd/yyyy)
	public static String dateFutureByDay(int numDay) {
		String dateFuture = "";

		Calendar c = Calendar.getInstance();

		c.setTime(new Date()); // Now use today date.

		c.add(Calendar.DAY_OF_YEAR, numDay);

		String tomorrow = c.getTime().toString();

		String day = tomorrow.substring(8, 10);
		String month = tomorrow.substring(4, 7);
		String anno = tomorrow.substring(25, 29);

		String monthNum = "";

		if (month.equalsIgnoreCase("JAN")) {
			monthNum = "01";
		} else if (month.equalsIgnoreCase("FEB")) {
			monthNum = "02";
		} else if (month.equalsIgnoreCase("MAR")) {
			monthNum = "03";
		} else if (month.equalsIgnoreCase("APR")) {
			monthNum = "04";
		} else if (month.equalsIgnoreCase("MAY")) {
			monthNum = "05";
		} else if (month.equalsIgnoreCase("JUN")) {
			monthNum = "06";
		} else if (month.equalsIgnoreCase("JUL")) {
			monthNum = "07";
		} else if (month.equalsIgnoreCase("AUG")) {
			monthNum = "08";
		} else if (month.equalsIgnoreCase("SET")) {
			monthNum = "09";
		} else if (month.equalsIgnoreCase("OCT")) {
			monthNum = "10";
		} else if (month.equalsIgnoreCase("NOV")) {
			monthNum = "11";
		} else if (month.equalsIgnoreCase("DEC")) {
			monthNum = "12";
		}

		dateFuture = "" + monthNum + "/" + day + "/" + anno;

		return dateFuture;
	}

	public static String yesterday(String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return sdf.format(cal.getTime());
	}

	public static String shiftDateInThePast(Date from, int days, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		final Calendar cal = Calendar.getInstance();
		cal.setTime(from);
		cal.add(Calendar.DATE, days);
		return sdf.format(cal.getTime());
	}

}

