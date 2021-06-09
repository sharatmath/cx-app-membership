package com.dev.prepaid.util;

import java.util.UUID;

public class GUIDUtil {

	public static String generateGUID() {
		UUID uuid = UUID.randomUUID();
		return  uuid.toString();
	}
	
}
