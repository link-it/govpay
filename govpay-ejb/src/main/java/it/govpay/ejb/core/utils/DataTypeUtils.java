/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
package it.govpay.ejb.core.utils;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

public class DataTypeUtils {

	private static DatatypeFactory datatypeFactory;

	public static void init() throws DatatypeConfigurationException {
		datatypeFactory = DatatypeFactory.newInstance();
	}
	
	public static XMLGregorianCalendar dateToXmlGregorianCalendar(Date d) {

		if (d==null) {
			return null;
		}

		GregorianCalendar gregory = new GregorianCalendar();
		gregory.setTime(d);
		XMLGregorianCalendar returnValue;
		returnValue = datatypeFactory.newXMLGregorianCalendar(gregory);
		return  returnValue;

	}


	public static XMLGregorianCalendar getXMLGregorianCalendarAdesso() {
		return datatypeFactory.newXMLGregorianCalendar(new GregorianCalendar());
	}

	public static XMLGregorianCalendar getGiorniDopo(XMLGregorianCalendar date, int giorni, boolean aggiungi) {
		return getGiorniDopoPrima(date, giorni, true);
	}
	
	public static XMLGregorianCalendar getGiorniPrima(XMLGregorianCalendar date, int giorni) {
		return getGiorniDopoPrima(date, giorni, false);
	}
	
	private static XMLGregorianCalendar getGiorniDopoPrima(XMLGregorianCalendar date, int giorni, boolean aggiungi) {
		Duration duration = datatypeFactory.newDuration(aggiungi, 0, 0, giorni, 0, 0, 0);
		date.add(duration);
		return date;
	}

	public static Timestamp getSqlTimeStamp(XMLGregorianCalendar xgc) {
		if (xgc == null) {
			return null;
		} else {
			return new Timestamp(xgc.toGregorianCalendar().getTime().getTime());
		}
	}

	public static XMLGregorianCalendar toData(XMLGregorianCalendar dataOra) {
		if (dataOra==null) {
			return null;
		}
		XMLGregorianCalendar cal = datatypeFactory.newXMLGregorianCalendar();
		cal.setYear(dataOra.getYear());
		cal.setDay(dataOra.getDay());
		cal.setMonth(dataOra.getMonth());
		return cal;
	}



	public static Timestamp dateToSQLTimestamp(Date d) {
		if (d==null) {
			return null;
		}

		return new Timestamp(d.getTime());
	}



	public static Date getGiorniDopo(Date d, int piuGiorni){
		if (d==null) {
			return null;
		}
		Calendar date = Calendar.getInstance();
		date.setTime(d);
		date.add(Calendar.DATE, piuGiorni);
		return date.getTime();
	}
	
	
    /*
     * Converts XMLGregorianCalendar to java.util.Date 
     */
    public static Date xmlGregorianCalendartoDate(XMLGregorianCalendar calendar){
        if(calendar == null) {
            return null;
        }
        return calendar.toGregorianCalendar().getTime();
    }



}
