/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
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
package it.govpay.core.utils.adapter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;

public class DataTypeAdapter {

	public static BigDecimal parseImporto(String value) {
		return new BigDecimal(value);
	}

	public static String printImporto(BigDecimal value) {
		DecimalFormatSymbols custom=new DecimalFormatSymbols();
		custom.setDecimalSeparator('.');
		
		DecimalFormat format = new DecimalFormat();
		format.setDecimalFormatSymbols(custom);
		format.setGroupingUsed(false);
		format.setMaximumFractionDigits(2);
		return format.format(value);
	}

	public static Integer parseYear(String year) {
		if (year == null) {
			return null;
		}
		return DatatypeConverter.parseDate(year).get(Calendar.YEAR);
	}
	
	public static String printYear(Integer year) {
		if (year == null) {
			return null;
		}
		return year.toString();
	}
	
    public static Date parseDateTime(String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }
        return DatatypeConverter.parseDateTime(s).getTime();
    }
    
    public static String printDateTime(Date dt) {
        if (dt == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        String date = DatatypeConverter.printDateTime(c);
        if(date != null && date.contains("+"))
        	date = date.substring(0, date.indexOf("+"));
        
        if(date != null && date.length() > 19) {
        	date = date.substring(0, 19);
        }
        return date;
    }
    
    public static Date parseDate(String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }
        return DatatypeConverter.parseDateTime(s).getTime();
    }
    
    public static String printDate(Date dt) {
        if (dt == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        String date = DatatypeConverter.printDateTime(c);
        if(date != null && date.contains("+"))
        	date = date.substring(0, date.indexOf("+"));
        
        if(date != null && date.length() > 10) {
        	date = date.substring(0, 10);
        }
        return date;
    }

    public static Date parseTime(String s) {
        if (s == null) {
            return null;
        }
        return DatatypeConverter.parseTime(s).getTime();
    }
    public static String printTime(Date dt) {
        if (dt == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        return DatatypeConverter.printTime(c);
    }
}
