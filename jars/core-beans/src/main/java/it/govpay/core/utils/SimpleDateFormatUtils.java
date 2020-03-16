package it.govpay.core.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang.time.DateFormatUtils;

public class SimpleDateFormatUtils {
	
	private static final String PATTERN_DATA_JSON_YYYY_MM_DD_T_HH_MM_SS_SSS = "yyyy-MM-dd'T'HH:mm:ss.SSS";
	private static final String PATTERN_DATA_JSON_YYYY_MM_DD_T_HH_MM = "yyyy-MM-dd'T'HH:mm";
	private static final String PATTERN_DATA_JSON_YYYY_MM_DD_T_HH_MM_SS = "yyyy-MM-dd'T'HH:mm:ss";
	private static final String PATTERN_DATA_JSON_YYYY_MM_DD = "yyyy-MM-dd";
	private static final String PATTERN_DATA_DD_MM_YYYY_HH_MM_SS_SSS = "ddMMyyyyHHmmSSsss";
	private static final String PATTERN_DATA_YYYY = "yyyy";
	
	public static List<String> datePatterns = null;
	static {
		datePatterns = new ArrayList<>();
		datePatterns.add(DateFormatUtils.ISO_DATE_FORMAT.getPattern());
		datePatterns.add(DateFormatUtils.ISO_DATETIME_FORMAT.getPattern());
		datePatterns.add(DateFormatUtils.ISO_DATE_TIME_ZONE_FORMAT.getPattern());
		datePatterns.add(DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.getPattern());
	}
	
	public static List<String> datePatternsRest = null;
	static {
		datePatternsRest = new ArrayList<>();
		datePatternsRest.addAll(datePatterns);
		datePatternsRest.add(PATTERN_DATA_JSON_YYYY_MM_DD_T_HH_MM_SS_SSS);
		datePatternsRest.add(PATTERN_DATA_JSON_YYYY_MM_DD_T_HH_MM);
		datePatternsRest.add(PATTERN_DATA_JSON_YYYY_MM_DD_T_HH_MM_SS);
		datePatternsRest.add(PATTERN_DATA_JSON_YYYY_MM_DD);
		datePatternsRest.add(PATTERN_DATA_DD_MM_YYYY_HH_MM_SS_SSS);
		datePatternsRest.add(PATTERN_DATA_YYYY);
	}
	
	public static List<String> onlyDatePatternsRest = null;
	static {
		onlyDatePatternsRest = new ArrayList<>();
		onlyDatePatternsRest.add(DateFormatUtils.ISO_DATE_FORMAT.getPattern());
		onlyDatePatternsRest.add(PATTERN_DATA_JSON_YYYY_MM_DD);
	}
	
	public static SimpleDateFormat newSimpleDateFormat() {
		return newSimpleDateFormat(SimpleDateFormatUtils.PATTERN_DATA_JSON_YYYY_MM_DD_T_HH_MM_SS_SSS);
	}
	
	public static SimpleDateFormat newSimpleDateFormatDataOreMinuti() {
		return newSimpleDateFormat(SimpleDateFormatUtils.PATTERN_DATA_JSON_YYYY_MM_DD_T_HH_MM);
	}
	
	public static SimpleDateFormat newSimpleDateFormatDataOreMinutiSecondi() {
		return newSimpleDateFormat(SimpleDateFormatUtils.PATTERN_DATA_JSON_YYYY_MM_DD_T_HH_MM_SS);
	}
	
	public static SimpleDateFormat newSimpleDateFormatSoloData() {
		return newSimpleDateFormat(SimpleDateFormatUtils.PATTERN_DATA_JSON_YYYY_MM_DD);
	}
	
	public static SimpleDateFormat newSimpleDateFormatIuvUtils() {
		SimpleDateFormat sdf = new SimpleDateFormat(SimpleDateFormatUtils.PATTERN_DATA_DD_MM_YYYY_HH_MM_SS_SSS);
		return sdf;
	}
	
	public static SimpleDateFormat newSimpleDateFormatSoloAnno() {
		SimpleDateFormat sdf = new SimpleDateFormat(SimpleDateFormatUtils.PATTERN_DATA_YYYY);
		return sdf;
	}
	
	public static SimpleDateFormat newSimpleDateFormat(String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		sdf.setTimeZone(TimeZone.getTimeZone("CET"));
		sdf.setLenient(false);
		return sdf;
	}
}
