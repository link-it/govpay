package it.govpay.core.utils;

public class IncassoUtils {

	public static String getRiferimentoIncassoSingolo(String causale) {
		if(causale.startsWith("/RFS") || causale.startsWith("/RFB")) {
			int indexSlash = causale.indexOf("/", 6);
			int indexBlank = causale.indexOf(" ", 6);
			
			if(indexSlash == -1 && indexBlank == -1)
				return causale.substring(5);
			
			if(indexSlash == -1)
				return causale.substring(5, indexBlank);
			
			if(indexBlank == -1)
				return causale.substring(5, indexSlash);
			
			return causale.substring(5, indexSlash < indexBlank ? indexSlash : indexBlank);
		}
		
		return null;
	}
	
	public static String getRiferimentoIncassoCumulativo(String causale) {
		if(causale.startsWith("/PUR")) {
			int indexSlash = causale.indexOf("/", 28);
			int indexBlank = causale.indexOf(" ", 28);
			
			if(indexSlash == -1 && indexBlank == -1)
				return causale.substring(27);
			
			if(indexSlash == -1)
				return causale.substring(27, indexBlank);
			
			if(indexBlank == -1)
				return causale.substring(27, indexSlash);
			
			return causale.substring(27, indexSlash < indexBlank ? indexSlash : indexBlank);
		}
		return null;
	}

	public static String getRiferimentoIncasso(String causale) {
		String idf = getRiferimentoIncassoCumulativo(causale);
		if(idf!=null) return idf;
		else return getRiferimentoIncassoSingolo(causale);
	}
	
}
