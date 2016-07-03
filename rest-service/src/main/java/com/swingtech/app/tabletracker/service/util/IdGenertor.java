package com.swingtech.app.tabletracker.service.util;

import org.joda.time.LocalDateTime;

public class IdGenertor {
	public static String generateNewId() {
		LocalDateTime now = new LocalDateTime();
		
		return Long.toString(now.toDateTime().getMillis());
	}
}
