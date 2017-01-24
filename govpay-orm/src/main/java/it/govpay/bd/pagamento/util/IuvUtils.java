/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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
package it.govpay.bd.pagamento.util;

import java.math.BigInteger;

import org.openspcoop2.generic_project.exception.ServiceException;

public class IuvUtils {
	
	public static boolean checkISO11640(String iuv){
		if(iuv.length() <= 4) return false;
		
		String reference = iuv.substring(4);
		String check;
		try {
			check = IuvUtils.getCheckDigit(reference);
		} catch (ServiceException e) {
			return false;
		}
		return iuv.equals("RF" + check + reference);
	}

	public static String getCheckDigit(String reference) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		String referenceUpperCase = reference.toUpperCase();
		for (int i = 0; i < referenceUpperCase.length(); i++){
			char c = referenceUpperCase.charAt(i);        
			switch (c) {
			case '0': sb.append('0'); break;
			case '1': sb.append('1'); break;
			case '2': sb.append('2'); break;
			case '3': sb.append('3'); break;
			case '4': sb.append('4'); break;
			case '5': sb.append('5'); break;
			case '6': sb.append('6'); break;
			case '7': sb.append('7'); break;
			case '8': sb.append('8'); break;
			case '9': sb.append('9'); break;
			case 'A': sb.append("10"); break;
			case 'B': sb.append("11"); break;
			case 'C': sb.append("12"); break;
			case 'D': sb.append("13"); break;
			case 'E': sb.append("14"); break;
			case 'F': sb.append("15"); break;
			case 'G': sb.append("16"); break;
			case 'H': sb.append("17"); break;
			case 'I': sb.append("18"); break;
			case 'J': sb.append("19"); break;
			case 'K': sb.append("20"); break;
			case 'L': sb.append("21"); break;
			case 'M': sb.append("22"); break;
			case 'N': sb.append("23"); break;
			case 'O': sb.append("24"); break;
			case 'P': sb.append("25"); break;
			case 'Q': sb.append("26"); break;
			case 'R': sb.append("27"); break;
			case 'S': sb.append("28"); break;
			case 'T': sb.append("29"); break;
			case 'U': sb.append("30"); break;
			case 'V': sb.append("31"); break;
			case 'W': sb.append("32"); break;
			case 'X': sb.append("33"); break;
			case 'Y': sb.append("34"); break;
			case 'Z': sb.append("35"); break;
			default:
				throw new ServiceException("Carattere [" + c +"] non amesso nell'IUV");
			}
		}

		BigInteger base = new BigInteger(sb.toString() + "271500");
		BigInteger mod97 = base.mod(BigInteger.valueOf(97));
		int diff98 = 98 - mod97.intValue();
		return String.format("%02d", diff98);
	}
	
	public static String getCheckDigit93(String reference, int auxDigit, int code) {
		long resto93 = (Long.parseLong(String.valueOf(auxDigit) + String.format("%02d", code) + reference)) % 93;
		return String.format("%02d", resto93);
	}
}
