package it.govpay.core.utils;

public class IncassoUtils {

	public static String getRiferimentoIncassoSingolo(String causale) {
		if(causale.startsWith("/RFS/") || causale.startsWith("/RFB/")) {
			if(causale.indexOf("/", 6) != -1) 
				return causale.substring(5, causale.indexOf("/", 6));
			else
				return causale.substring(5);
		}
		
		return null;
	}
	
	public static String getRiferimentoIncassoCumulativo(String causale) {
		if(causale.startsWith("/PUR/LGPE-RIVERSAMENTO/URI/")) {
			if(causale.indexOf("/", 28) != -1) 
				return causale.substring(27, causale.indexOf("/", 28));
			else
				return causale.substring(27);
		}
		
		if(causale.startsWith("PUR LGPE-RIVERSAMENTO URI ")) {
			if(causale.indexOf("/", 27) != -1) 
				return causale.substring(26, causale.indexOf("/", 27));
			else
				return causale.substring(26);
		}
		
		return null;
	}

	public static String getRiferimentoIncasso(String causale) {
		String idf = getRiferimentoIncassoCumulativo(causale);
		if(idf!=null) return idf;
		else return getRiferimentoIncassoSingolo(causale);
	}
	
}
