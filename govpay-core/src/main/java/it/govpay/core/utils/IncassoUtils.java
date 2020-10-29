package it.govpay.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IncassoUtils {

	private static Pattern patternSingoloRFS = Pattern.compile("^.*RFS.(\\d\\d\\d\\d-\\d\\d-\\d\\d[^-]+-[^ \\/]+)[- \\/]?.*$");
	private static Pattern patternSingoloRFB = Pattern.compile("^.*RFB.(\\d\\d\\d\\d-\\d\\d-\\d\\d[^-]+-[^ \\/]+)[- \\/]?.*$");
	private static List<Pattern> patternCumulativo;
	
	static {
		patternCumulativo = new ArrayList<Pattern>();
		patternCumulativo.add(Pattern.compile("^.*PUR.LGPE-RIVERSAMENTO.URI.(\\d\\d\\d\\d-\\d\\d-\\d\\d[^-]+-[^ \\/]+)[- \\/]?.*$"));
		patternCumulativo.add(Pattern.compile("^.*PUR.LGPE-RIVERSAMENTO.URI.(\\d\\d\\d\\d-\\d\\d-\\d\\d[^-]+-[^ \\/-]+)[- \\/]?.*$"));
	}
	
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

	public static List<String> getRiferimentoIncassoCumulativo(String causale) {
		List<String> matches = new ArrayList<String>();
		for(Pattern p : patternCumulativo) {
			Matcher matcher = p.matcher(causale);
			if (matcher.find())
				matches.add(matcher.group(1));
		}
		
		if(matches.size() > 0)
			return matches;
		else 
			return null;
	}
	
}
