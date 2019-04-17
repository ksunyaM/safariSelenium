package com.wba.dataanalytics.api.test.common;

import cucumber.api.java.ca.Cal;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateHelper {
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
	
	/**
	 * generate sysdatime + 1 day
	 * @param format
	 * @param numDay
	 * @return
	 */
	public static String currentDateGenerator(String format, int numDay) {
//		Calendar c = Calendar.getInstance();
//		c.setTime(new Date());
//		c.add(Calendar.DATE, numDay);
//		Date date = c.getTime();
//		DateFormat formatDate = new SimpleDateFormat(format);
//		return formatDate.format(date);
		
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, numDay);
		Date date = c.getTime();
		DateFormat formatDate = new SimpleDateFormat(format);
		String today = formatDate.format(date);
		return formatDate.format(date);
		
		
	}

	public static long convertFromStringToTimestamp(String stringData, String format) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat(format);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date date = dateFormat.parse(stringData);
		return date.getTime();
	}

	@Deprecated
	public static String convertDataFormat(String oldFormat, String newFormat, String date) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat(oldFormat);
		Date newDate = df.parse(date);
		df.applyPattern(newFormat);
		return df.format(newDate);
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
	
	
	public static String setSpeficicDayDateFromCurrentDateString(String format, int amount){
		DateFormat formatDate = new SimpleDateFormat(format);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, amount);
		c.set(Calendar.HOUR_OF_DAY,10);
		return formatDate.format(c.getTime());
	}
	
	public static String lastTwoDigitCurrentYear(){
		DateFormat df = new SimpleDateFormat("yy"); // Just the year, with 2 digits
		String formattedDate = df.format(Calendar.getInstance().getTime());
		return formattedDate;
	}

	public static Map<String,String> convertSystemTimeToGmtAmbariTime(Date date )
	{
		Map<String, String> ambariSystemTime = new HashMap<>();
		Date currentDateTime = getDate(date);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd HH");
		String [] timestamp = sdf.format(currentDateTime).trim().split("\\s+");
		String year = timestamp[0];
		String month = timestamp[1];
		String day = timestamp[2];
		String hour = timestamp[3];

		ambariSystemTime.put("year", year);
		ambariSystemTime.put("month", month);
		ambariSystemTime.put("day", day);
		ambariSystemTime.put("hour", hour);

		return ambariSystemTime;
	}

	private static Date getDate(Date date) {
		TimeZone tz = TimeZone.getDefault();
		Date currentDateTime = new Date( date.getTime() - tz.getRawOffset() );
		// if we are now in DST, back off by the delta.  Note that we are checking the GMT date, this is the KEY.
		if ( tz.inDaylightTime( currentDateTime )) {
			Date dstDate = new Date( currentDateTime.getTime() - tz.getDSTSavings() );
			// check to make sure we have not crossed back into standard time
			if ( tz.inDaylightTime( dstDate )) {
				currentDateTime = dstDate;
			}
		}
		return currentDateTime;
	}

	public static String getTimestampForETLJobs(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		calendar.add(Calendar.DATE, 1);
		int day = calendar.get(Calendar.DATE);

		calendar.set(year,month,day,1,0,0);
		date = calendar.getTime();
		return new SimpleDateFormat("yyyyMMddHHmmss").format(date);
	}

	/**
	 * @author schandzk
	 * @param numberOfDays
	 * @return
	 * This method returns future/past date in the format of yyyyMMddHHmmss for ETL jobs
	 */
	public static String getTimestampForETLJobs(int numberOfDays){

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, numberOfDays);

		Date date = calendar.getTime();

		SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMddhhmmss");
		String dateCreated = format1.format(date);

		return dateCreated;

	}
}
