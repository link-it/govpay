package it.govpay.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IncassoUtils {

	private static Pattern patternSingoloRFS = Pattern.compile("^.*RFS.(\\d\\d\\d\\d-\\d\\d-\\d\\d[^-]+-[^- \\/]+)[- \\/]?.*$");
	private static Pattern patternSingoloRFB = Pattern.compile("^.*RFB.(\\d\\d\\d\\d-\\d\\d-\\d\\d[^-]+-[^- \\/]+)[- \\/]?.*$");
	private static Pattern patternCumulativo = Pattern.compile("^.*PUR.LGPE-RIVERSAMENTO.URI.(\\d\\d\\d\\d-\\d\\d-\\d\\d[^-]+-[^- \\/]+)[- \\/]?.*$");


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
		if (matcher.find())
			return matcher.group(1);
		else
			return null;
	}

	public static String getRiferimentoIncasso(String causale) {
		String idf = getRiferimentoIncassoCumulativo(causale);
		if(idf!=null) return idf;
		else return getRiferimentoIncassoSingolo(causale);
	}

}
