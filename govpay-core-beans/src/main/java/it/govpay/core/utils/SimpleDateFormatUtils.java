package it.govpay.core.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang.time.DateFormatUtils;

public class SimpleDateFormatUtils {
	
	public static List<String> datePatterns = null;
	static {

		datePatterns = new ArrayList<String>();
		datePatterns.add(DateFormatUtils.ISO_DATE_FORMAT.getPattern());
		datePatterns.add(DateFormatUtils.ISO_DATETIME_FORMAT.getPattern());
		datePatterns.add(DateFormatUtils.ISO_DATE_TIME_ZONE_FORMAT.getPattern());
		datePatterns.add(DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.getPattern());
	}
	
	private static final String PATTERN_DATA_JSON_YYYY_MM_DD_T_HH_MM_SS_SSS = "yyyy-MM-dd'T'HH:mm:ss.SSS";
	public static SimpleDateFormat newSimpleDateFormat() {
		SimpleDateFormat sdf = new SimpleDateFormat(SimpleDateFormatUtils.PATTERN_DATA_JSON_YYYY_MM_DD_T_HH_MM_SS_SSS);
		sdf.setTimeZone(TimeZone.getTimeZone("CET"));
		sdf.setLenient(false);
		return sdf;
	}

	private static final String PATTERN_DATA_JSON_YYYY_MM_DD = "yyyy-MM-dd";
	public static SimpleDateFormat newSimpleDateFormatSoloData() {
		SimpleDateFormat sdf = new SimpleDateFormat(SimpleDateFormatUtils.PATTERN_DATA_JSON_YYYY_MM_DD);
		sdf.setTimeZone(TimeZone.getTimeZone("CET"));
		sdf.setLenient(false);
		return sdf;
	}
}
