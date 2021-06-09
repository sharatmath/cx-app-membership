package com.dev.prepaid.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateUtil {
	static final long ONE_MINUTE_IN_MILLIS=60000;//millisecs
	
	public static Date stringToDate(String dateStr,String format) throws ParseException {
			return new SimpleDateFormat(format).parse(dateStr);//yyyy-MM-dd'T'HH:mm:ss'Z'
	}
	public static String dateToString(Date date,String format) throws ParseException {
			return new SimpleDateFormat(format).format(date);//yyyy-MM-dd'T'HH:mm:ss'Z'
	}
	public static Date dateToLocaleDateTimeStamp(Date date,String zoneId) throws ParseException {
		TimeZone etTimeZone = TimeZone.getTimeZone(zoneId);// Europe/London
		
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatTime = new SimpleDateFormat("'T'HH:mm:ss'Z'");
		
		formatTime.setTimeZone(etTimeZone);
		log.debug(formatDate.format(date)+formatTime.format(new Date()));
		return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(formatDate.format(date)+formatTime.format(new Date()));
	}
	
	public static Date addDayDate(Date date, Integer addDays) throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, addDays); 
		long t= cal.getTimeInMillis();
		Date afterAddingDay=new Date(t);
		
		return afterAddingDay;
	}
	public static Date addMonthDate(Date date, Integer addMonths) throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, addMonths); 
		long t= cal.getTimeInMillis();
		Date afterAddingMonth=new Date(t);
		
		return afterAddingMonth;
	}
	
	public static String dayName(Date inputDate){
        
        return new SimpleDateFormat("EEEE", Locale.ENGLISH).format(inputDate);
    }
	
	public static String getDay(Date inputDate) {
		LocalDate localDate = inputDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return String.format("%02d", localDate.getDayOfMonth());
	}
	public static String getMonth(Date inputDate) {
		LocalDate localDate = inputDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return String.format("%02d", localDate.getMonthValue());
	}
	public static String getYear(Date inputDate) {
		LocalDate localDate = inputDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return String.valueOf(localDate.getYear());
	}
	public static String getWeek(Date inputDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(inputDate);
		Integer week = ((cal.get(Calendar.WEEK_OF_MONTH)-1)>4) ? 4 : (cal.get(Calendar.WEEK_OF_MONTH));  
		return String.valueOf(week);
	}
	public static String getLastDateOfMonth(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
//        return cal.getTime();
        return new SimpleDateFormat("dd").format(cal.getTime());
    }
}
