package com.personal.rents.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtil {

	private static final DateFormat standardFormat = 
			new SimpleDateFormat(GeneralConstants.DATE_FORMAT);
	
	private DateUtil() {
	}

	public static final String standardFormat(Date date) {
		return standardFormat.format(date);
	}
}
