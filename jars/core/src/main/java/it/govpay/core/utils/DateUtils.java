/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
/**
 * 
 */
package it.govpay.core.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 30 mag 2018 $
 * 
 */
public class DateUtils {
	
	public static final String CONTROLLO_SCADENZA = "scadenza";
	public static final String CONTROLLO_VALIDITA = "validita'";

	private static Logger log = LoggerWrapperFactory.getLogger(DateUtils.class);
	
	public static boolean isDataDecorsa(Date daVerificare, String tipoControllo) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		cal.add(Calendar.DATE, -1);
		Date oggi = cal.getTime();
		
		boolean esito = daVerificare.before(oggi);
		log.debug("Controllo " +tipoControllo + ": data da verificare ["+daVerificare+"] is before oggi ["+oggi+"]: " + esito + ".");
		return esito;

	}

	public static Date toJavaDate(LocalDateTime dateTime) {
		if (dateTime == null)
			return null;
		ZonedDateTime zdt = dateTime.atZone(ZoneId.systemDefault());
		return Date.from(zdt.toInstant());
	}

	public static Date toJavaDate(LocalDate date) {
		if (date == null)
			return null;
		return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	public static LocalDateTime toLocalDateTime(Date date) {
		if (date == null)
			return null;
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}

	public static LocalDateTime toOffsetDateTime(Calendar c) {
		if (c == null)
			return null;
		return toLocalDateTime( c.getTime() );
	}

	public static LocalDate toLocalDate(Date javaDate) {
		if (javaDate == null)
			return null;
		return javaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static LocalDate toLocalDate(LocalDateTime dateTime) {
		if (dateTime == null)
			return null;
		return dateTime.toLocalDate();
	}
}
