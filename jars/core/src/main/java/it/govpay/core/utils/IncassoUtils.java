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
package it.govpay.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IncassoUtils {
	
	private IncassoUtils() {}
	
	private static Pattern patternSingolo = Pattern.compile("RF[SB].([0-9A-Za-z\\-_]+)");
	private static Pattern patternCumulativo = Pattern.compile("PUR.LGPE-RIVERSAMENTO.(TXT.[0-9]{1}.)?URI.([0-9A-Za-z\\-_]+)");
	private static Pattern patternIDF = Pattern.compile("\\d\\d\\d\\d-\\d\\d-\\d\\d[0-9A-Za-z_]*-\\S*");
	private static Pattern patternIUV = Pattern.compile("[0-9]{15,17}|^RF.*");

	public static String getRiferimentoIncassoSingolo(String causale) {
		Matcher matcher = patternSingolo.matcher(causale);
		while (matcher.find()) {
			for(int i=1; i<=matcher.groupCount(); i++) {
				String match = matcher.group(i);
				if(patternIUV.matcher(match).find()) {
					return match;
				}
			}
		} 
		return null;
	}

	public static String getRiferimentoIncassoCumulativo(String causale) {
		Matcher matcher = patternCumulativo.matcher(causale);
		while (matcher.find()) {
			for(int i=1; i<=matcher.groupCount(); i++) {
				String match = matcher.group(i);
				if(match != null && patternIDF.matcher(match).find()) {
					return match;
				}
			}
		} 
		return null;
	}

	public static String getRiferimentoIncasso(String causale) {
		String idf = getRiferimentoIncassoCumulativo(causale);
		if(idf!=null) return idf;
		else return getRiferimentoIncassoSingolo(causale);
	}
}
