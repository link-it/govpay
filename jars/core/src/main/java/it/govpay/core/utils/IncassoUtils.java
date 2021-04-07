package it.govpay.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IncassoUtils {

	private static Pattern patternSingoloRFS = Pattern.compile("^.*RFS[ \\/]([^ \\/]+)[ \\/]?.*$");
	private static Pattern patternSingoloRFB = Pattern.compile("^.*RFB[ \\/]([^ \\/]+)[ \\/]?.*$");
	private static Pattern patternCumulativo = Pattern.compile("PUR.LGPE-RIVERSAMENTO.URI.([0-9A-Za-z\\-_]+)");
	private static Pattern patternCausale = Pattern.compile("^.*TXT[ \\/]([ \\/]?.*)$");
	private static Pattern patternIDF = Pattern.compile("\\d\\d\\d\\d-\\d\\d-\\d\\d[0-9A-Za-z_]*-\\S*");

	public static String getRiferimentoIncassoSingolo(String causale) {
		Matcher matcher = patternSingoloRFS.matcher(causale);
		if (matcher.find())
			return matcher.group(1);
		else {
			matcher = patternSingoloRFB.matcher(causale);
			if (matcher.find())
				return matcher.group(1);
			else
				return null;
		}
	}

	public static String getRiferimentoIncassoCumulativo(String causale) {
		Matcher matcher = patternCumulativo.matcher(causale);
		while (matcher.find()) {
			for(int i=1; i<=matcher.groupCount(); i++) {
				String match = matcher.group(i);
				System.out.println(match);
				if(patternIDF.matcher(match).find()) {
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

	public static String getCausaleDaDescrizioneIncasso(String descrizione) {
		if(descrizione != null) {
			Matcher matcher = patternCausale.matcher(descrizione);
			if (matcher.find())
				return matcher.group(1);
		}

		return "";
	}
}
