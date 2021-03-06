package com.dev.prepaid.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;

@Slf4j
public class DateUtil {
	static final long ONE_MINUTE_IN_MILLIS=60000;//millisecs

	public static Date stringToDate(String data){
		ZoneId zoneId = ZoneId.of( "Asia/Singapore" );
		String concat = ":10.000Z";
		String format ="yyyy-MM-ddTHH:mm";
//        String data = "2021-09-02T13:15:10.249Z";
//		String data = "2021-09-02T13:15";
		Instant instant = Instant.parse(data.concat(concat));
		ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, zoneId );
		Date date = Date.from(zdt.toInstant());
		return date;
	}

	public static String fromDate(Date date) throws ParseException {
		String f  = DateUtil.dateToString(date, "yyyy-MM-dd'T'HH:mm:ss'Z'");
		return  f.substring(0, 16);
	}

	public static LocalDateTime stringToLocalDateTime(String data){
		String zero = ":00";
		String dateConvert = data.concat(zero);
		ZoneId timeZone = ZoneId.systemDefault();
//		ZoneId zoneId = ZoneId.of( "Asia/Singapore" );
		ZonedDateTime zonedDateTime = LocalDateTime.parse(dateConvert,
				DateTimeFormatter.ISO_DATE_TIME).atZone(timeZone);

		log.info("zonedDateTime |{}|", zonedDateTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
		LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();

		return localDateTime;
	}

	public static String fromLocalDateTime(LocalDateTime localDateTime) throws ParseException {
		DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
		ZoneId timeZone = ZoneId.systemDefault();
		ZonedDateTime zonedDateTime =localDateTime.atZone(timeZone);
		log.info("zonedDateTime |{}|", zonedDateTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
		String formattedDateTime = localDateTime.format(formatter);
		log.info("formattedDateTime |{}|", formattedDateTime);
		return  formattedDateTime.substring(0, 16);
	}
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
	
	public static Date add(Date date, int field, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        calendar.add(field, amount);
        return calendar.getTime();
    }
	public static String formatCommonYMD(Date date) {
        String pattern = "dd/MM/yyyy";
        if (date == null)
            return "";
        if (OperationUtil.isEmpty(pattern))
            return date.toString();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.format(date);
        } catch (Throwable e) {
            log.error("can not format date, date: " + date.toString() + ", pattern:" + pattern + ".", e);
            throw new BusinessException("Unable to format date and time???" + date.toString() + ", Format???" + pattern);
        }
    }
	
}
