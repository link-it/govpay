package it.govpay.core.utils.rawutils;

import java.text.SimpleDateFormat;

public class DateFormatUtils {

	private static final String PATTERN_DATA_JSON_YYYY_MM_DD_T_HH_MM_SS = "yyyy-MM-dd'T'HH:mm:ss";
	private static final String PATTERN_DATA_JSON_YYYY_MM_DD = "yyyy-MM-dd";


	public static SimpleDateFormat newSimpleDateFormatNoMillis() {
		return newSimpleDateFormat(DateFormatUtils.PATTERN_DATA_JSON_YYYY_MM_DD_T_HH_MM_SS);
	}

	public static SimpleDateFormat newSimpleDateFormatSoloData() {
		return newSimpleDateFormat(DateFormatUtils.PATTERN_DATA_JSON_YYYY_MM_DD);
	}

	public static SimpleDateFormat newSimpleDateFormat(String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		sdf.setLenient(false);
		return sdf;
	}

}

