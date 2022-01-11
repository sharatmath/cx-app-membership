/**
 * 
 */
package com.dev.prepaid.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Saket
 *
 * 
 */
@Slf4j
public class OperationUtil extends StringUtils {
	private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(OperationUtil.class);

//	String Null Check
	public static String nullStringCheck(String value) {
		return value == null ? "" : value;
	}

//	Empty check
	public static boolean isZeroOrEmpty(String s) {
		int length;

		if ((s == null) || (s.length() == 0) || s.equals("null") || s.equals("0")) {
			return true;
		}
		length = s.length();
		for (int i = 0; i < length; i++) {
			if (!Character.isWhitespace(s.charAt(i))) {
				return false;
			}
		}

		return true;
	}

//	Convert Integer
	public static Integer toInt(String value, Integer defaultValue) {
		if (value == null || value.trim().length() <= 0)
			return defaultValue;

		if (value.indexOf('.') >= 0) {
			try {
				Double d = Double.valueOf(value);
				return d.intValue();
			} catch (Exception e) {
				log.warn("Can't convert \"" + value + "\" into Integer, use default value: " + defaultValue, e);
				return defaultValue;
			}
		}

		try {
			return Integer.valueOf(value);
		} catch (Exception e) {
			log.warn("Can't convert \"" + value + "\" into Integer, use default value: " + defaultValue, e);
			return defaultValue;
		}
	}

//	Convert Long
	public static Long toLong(String value, Long defaultValue) {
		if (value == null || value.trim().length() <= 0)
			return defaultValue;

		if (value.indexOf('.') >= 0) {
			try {
				Double d = Double.valueOf(value);
				return d.longValue();
			} catch (Exception e) {
				log.warn("Can't convert \"" + value + "\" into Long, use default value: " + defaultValue, e);
				return defaultValue;
			}
		}

		try {
			return Long.valueOf(value);
		} catch (Exception e) {
			log.warn("Can't convert \"" + value + "\" into Long, use default value: " + defaultValue, e);
			return defaultValue;
		}
	}

}
