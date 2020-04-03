package it.govpay.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.openspcoop2.utils.json.ValidationException;

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
	
	public static Date getDataDaConTimestamp(String dataInput, String dataInputName) throws ValidationException{
		return getDataDaConTimestamp(dataInput, dataInputName, false, true);
	}
	
	public static Date getDataDaConTimestamp(String dataInput, String dataInputName, boolean azzeraOraMinutiTimestamp) throws ValidationException{
		return getDataDaConTimestamp(dataInput, dataInputName, azzeraOraMinutiTimestamp, true);
	}
	
	public static Date getDataDaConTimestamp(String dataInput, String dataInputName, boolean azzeraOraMinutiTimestamp, boolean throwException) throws ValidationException{
		Date dataOutput = null;
		
		try {
			dataOutput = DateUtils.parseDate(dataInput, SimpleDateFormatUtils.onlyDatePatternsRest.toArray(new String[0]));
			Calendar c = Calendar.getInstance();
			c.setTime(dataOutput);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			dataOutput = c.getTime();
		}catch(ParseException e) {
			try {
				dataOutput = DateUtils.parseDate(dataInput, SimpleDateFormatUtils.datePatternsRest.toArray(new String[0]));
				if(azzeraOraMinutiTimestamp) {
					Calendar c = Calendar.getInstance();
					c.setTime(dataOutput);
					c.set(Calendar.HOUR_OF_DAY, 0);
					c.set(Calendar.MINUTE, 0);
					c.set(Calendar.SECOND, 0);
					c.set(Calendar.MILLISECOND, 0);
					dataOutput = c.getTime();
				}
			} catch (ParseException e1) {
				if(throwException)
					throw new ValidationException("Il formato della data indicata ["+dataInput+"] per il parametro ["+dataInputName+"] non e' valido.");
			}
		}
		return dataOutput;
	}
	
	public static Date getDataAConTimestamp(String dataInput, String dataInputName) throws ValidationException{
		return getDataAConTimestamp(dataInput, dataInputName, false, true);
	}
	
	public static Date getDataAConTimestamp(String dataInput, String dataInputName, boolean azzeraOraMinutiTimestamp) throws ValidationException{
		return getDataAConTimestamp(dataInput, dataInputName, azzeraOraMinutiTimestamp, true);
	}
	
	public static Date getDataAConTimestamp(String dataInput, String dataInputName, boolean azzeraOraMinutiTimestamp, boolean throwException) throws ValidationException{
		Date dataOutput = null;
		
		try {
			dataOutput = DateUtils.parseDate(dataInput, SimpleDateFormatUtils.onlyDatePatternsRest.toArray(new String[0]));
			Calendar c = Calendar.getInstance();
			c.setTime(dataOutput);
			c.set(Calendar.HOUR_OF_DAY, 23); 
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			c.set(Calendar.MILLISECOND, 999);
			dataOutput = c.getTime();
		}catch(ParseException e) {
			try {
				dataOutput = DateUtils.parseDate(dataInput, SimpleDateFormatUtils.datePatternsRest.toArray(new String[0]));
				if(azzeraOraMinutiTimestamp) {
					Calendar c = Calendar.getInstance();
					c.setTime(dataOutput);
					c.set(Calendar.HOUR_OF_DAY, 23); 
					c.set(Calendar.MINUTE, 59);
					c.set(Calendar.SECOND, 59);
					c.set(Calendar.MILLISECOND, 999);
					dataOutput = c.getTime();
				}
			} catch (ParseException e1) {
				if(throwException)
					throw new ValidationException("Il formato della data indicata ["+dataInput+"] per il parametro ["+dataInputName+"] non e' valido.");
			}
		}
		return dataOutput;
	}
}
