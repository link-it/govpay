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
package it.govpay.core.utils;

import java.math.BigDecimal;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/***
 * Utility per la formattazione degli importi
 * 
 * @author pintori
 *
 */
public class CurrencyUtils {

	public static final String PATTERN_CON_DECIMALI_E_VIRGOLA = "%,.2f";
	public static final String PATTERN_CON_DECIMALI_SENZA_VIRGOLA = "%.2f";
	public static final String PATTERN_SENZA_DECIMALI_E_VIRGOLA = "%,.0f";
	public static final String PATTERN_SENZA_DECIMALI_SENZA_VIRGOLA = "%.0f";
	
	public static final String EURO = "€"; 
	public static final String DOLLARO = "$";
	
	private Logger log = null;
	private boolean mostraVirgole = false;
	private Locale locale = null;
	
	public static CurrencyUtils getInstance() {
		return newInstance(LogManager.getLogger());
	}
	 
	public static CurrencyUtils newInstance(Logger log) {
		return new CurrencyUtils(log,null);
	}
	
	public static CurrencyUtils newInstance(Logger log,Locale locale) {
		return new CurrencyUtils(log,locale);
	}
	
	public CurrencyUtils(Logger log,Locale locale) {
		this.log = log;
		this.locale = locale;
	}
	
	public String getCurrencyAsEuro(Double value) {
		StringBuilder sb = new StringBuilder();
		String currencyAsString = getCurrencyAsString(value,this.locale);
		sb.append(currencyAsString);
		if(StringUtils.isNotEmpty(currencyAsString));
			sb.append(EURO);
			
		return sb.toString();
	}
	
	public String getCurrencyAsString(Double value) {
		return getCurrencyAsString(value,this.locale);
	}
	
	public String getCurrencyAsString(Double value, Locale locale) {
		return getCurrencyAsStringWithStringFormat(value, locale, this.mostraVirgole);
	}

	public String getCurrencyAsString(Double value, boolean mostraVirgole) {
		return getCurrencyAsStringWithStringFormat(value, this.locale, mostraVirgole);
	}

	public String getCurrencyAsStringWithStringFormat(Double value, Locale locale, boolean mostraVirgole) {
		if(value == null)
			return "";

		try {
		if(mostraVirgole)
			return getCurrencyAsStringWithStringFormatConVirgole(value,locale);
		else 
			return getCurrencyAsStringWithStringFormatSenzaVirgole(value,locale);
		}catch(Exception e) {
			this.log.error("Si e' verificato un errore durante la conversione del valore ["+value+"]: "+ e.getMessage(),e); 
			return "";
		}
		
	}

	public String getCurrencyAsStringWithStringFormatConVirgole(Double value, Locale locale)  throws Exception{
		if(value == null)
			return "";
		
		if(locale == null) {
			if (Math.round(value.doubleValue()) != value.doubleValue()) {
				return String.format(PATTERN_CON_DECIMALI_E_VIRGOLA, value.doubleValue());
			} else {
				return String.format(PATTERN_SENZA_DECIMALI_E_VIRGOLA, value.doubleValue());
			}
		} else {
			if (Math.round(value.doubleValue()) != value.doubleValue()) {
				return String.format(locale, PATTERN_CON_DECIMALI_E_VIRGOLA, value.doubleValue());
			} else {
				return String.format(locale, PATTERN_SENZA_DECIMALI_E_VIRGOLA, value.doubleValue());
			}
		}
	}

	public String getCurrencyAsStringWithStringFormatSenzaVirgole(Double value, Locale locale) throws Exception{
		if(value == null)
			return "";

		if(locale == null) {
			if (Math.round(value.doubleValue()) != value.doubleValue()) {
				return String.format(PATTERN_CON_DECIMALI_SENZA_VIRGOLA, value.doubleValue());
			} else {
				return String.format(PATTERN_SENZA_DECIMALI_SENZA_VIRGOLA, value.doubleValue());
			}
		} else {
			if (Math.round(value.doubleValue()) != value.doubleValue()) {
				return String.format(locale, PATTERN_CON_DECIMALI_SENZA_VIRGOLA, value.doubleValue());
			} else {
				return String.format(locale, PATTERN_SENZA_DECIMALI_SENZA_VIRGOLA, value.doubleValue());
			}
		}
	}
	
	/* Metodi con input BigDecimal */
	
	public String getCurrencyAsEuro(BigDecimal value) {
		if(value == null)
			return "";
		
		return getCurrencyAsEuro(value.doubleValue());
	}
	
	public String getCurrencyAsString(BigDecimal value) {
		if(value == null)
			return "";
		
		return getCurrencyAsString(value.doubleValue(),this.locale);
	}
	
	public String getCurrencyAsString(BigDecimal value, Locale locale) {
		if(value == null)
			return "";
		
		return getCurrencyAsStringWithStringFormat(value.doubleValue(), locale, this.mostraVirgole);
	}

	public String getCurrencyAsString(BigDecimal value, boolean mostraVirgole) {
		if(value == null)
			return "";
		
		return getCurrencyAsStringWithStringFormat(value.doubleValue(), this.locale, mostraVirgole);
	}
	
	
	/* TEST **/
	public static void main(String[] args) {
		Double d1 = null;
		Double d2 = 100D;
		Double d3 = 100.01D;
		Double d4 = 10000000000.01D;
		Logger log = LogManager.getLogger();
		CurrencyUtils currencyUtils = CurrencyUtils.newInstance(log,Locale.ITALIAN);

		System.out.println("=======D1=======");
		System.out.println("D1 C1: "+ currencyUtils.getCurrencyAsString(d1));
		System.out.println("D1 C2: "+ currencyUtils.getCurrencyAsStringWithStringFormat(d1,null,true));
		System.out.println("=======D1=======");

		System.out.println("=======D2=======");
		System.out.println("D2 C1: "+ currencyUtils.getCurrencyAsString(d2));
		System.out.println("D2 C2: "+ currencyUtils.getCurrencyAsStringWithStringFormat(d2,null,true));
		System.out.println("=======D2=======");

		System.out.println("=======D3=======");
		System.out.println("D3 C1: "+ currencyUtils.getCurrencyAsString(d3));
		System.out.println("D3 C2: "+ currencyUtils.getCurrencyAsStringWithStringFormat(d3,null,false));
		System.out.println("=======D3=======");

		System.out.println("=======D4=======");
		System.out.println("D4 C1: "+ currencyUtils.getCurrencyAsString(d4));
		System.out.println("D4 C2: "+ currencyUtils.getCurrencyAsString(d4,true));
		System.out.println("=======D4=======");
	}
}
