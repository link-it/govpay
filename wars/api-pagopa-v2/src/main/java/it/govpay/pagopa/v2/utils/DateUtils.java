/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2022 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.pagopa.v2.utils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateUtils {

	/**
	 * Controlla se la data da verificare e' decorsa.
	 * 
	 * @param daVerificare
	 * @return
	 */
	public static boolean isDataDecorsa(LocalDateTime daVerificare) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		cal.add(Calendar.DATE, -1);
		LocalDateTime oggi = LocalDateTime.ofInstant(cal.toInstant(), cal.getTimeZone().toZoneId());
		return daVerificare.isBefore(oggi);
	}
	
	public static SimpleDateFormat newSimpleDateFormat(String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		sdf.setTimeZone(TimeZone.getTimeZone("Europe/Rome"));
		sdf.setLenient(false);
		return sdf;
	}
	
	public static Date fromLocalDateTime(LocalDateTime ldt) {
		if(ldt == null) return null;
		
		Calendar c = Calendar.getInstance();
		Instant instant = ldt.atZone(c.getTimeZone().toZoneId()).toInstant();
		Date dt = Date.from(instant);
		return dt;
	}
	
	public static LocalDateTime toLocalDateTime(Date dt) {
		if(dt == null) return null;
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
        LocalDateTime ldt = LocalDateTime.ofInstant(cal.toInstant(), cal.getTimeZone().toZoneId());
		return ldt;
	}
}
