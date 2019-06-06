package utils.java;

public class NumeroAvvisoFactory {
	
	public class IuvFormatError extends Exception {
		private static final long serialVersionUID = 1L;
		
		public IuvFormatError(String message) {
			super(message);
		}
	}

	public String build_aux0(int application_code, String prefix, long reference) throws IuvFormatError {
		if(prefix == null) prefix = "";
		int prefixLength = prefix.length();
		if(String.valueOf(reference).length() > (13 - prefixLength))
			throw new IuvFormatError("Reference fornito per la generazione del numero avviso AUX 0 troppo lungo [" + reference + "]. Consentite al piu' 13 cifre.");
		String ref = String.format("%0" + (13 - prefixLength) + "d", reference);
		ref = prefix + ref;
		String check = getCheckDigit93("0" + String.format("%02d", application_code) + ref); 
		return "0" +  String.format("%02d", application_code) + ref + check;
	}
	
	public String build_aux1(String prefix, long reference) throws IuvFormatError {
		if(prefix == null) prefix = "";
		int prefixLength = prefix.length();
		if(String.valueOf(reference).length() > (17 - prefixLength))
			throw new IuvFormatError("Reference fornito per la generazione del numero avviso AUX 1 troppo lungo [" + reference + "]. Consentite al piu' 17 cifre.");
		String ref = String.format("%0" + (17 - prefixLength) + "d", reference);
		ref = prefix + ref;
		return "1" + reference;
	}
	
	public String build_aux2(String prefix, long reference) throws IuvFormatError {
		if(prefix == null) prefix = "";
		int prefixLength = prefix.length();
		if(String.valueOf(reference).length() > (15 - prefixLength))
			throw new IuvFormatError("Reference fornito per la generazione del numero avviso AUX 2 troppo lungo [" + reference + "]. Consentite al piu' 15 cifre.");
		String ref = String.format("%0" + (15 - prefixLength) + "d", reference);
		ref = prefix + ref;
		String check = getCheckDigit93("2" + ref); 
		return "2" + ref + check;
	}
	
	public String build_aux3(int segregation_code, String prefix, long reference) throws IuvFormatError {
		if(prefix == null) prefix = "";
		int prefixLength = prefix.length();
		if(String.valueOf(reference).length() > (13 - prefixLength))
			throw new IuvFormatError("Reference fornito per la generazione del numero avviso AUX 3 troppo lungo [" + reference + "]. Consentite al piu' 13 cifre.");
		String ref = String.format("%0" + (13 - prefixLength) + "d", reference);
		ref = prefix + ref;
		String check = getCheckDigit93("3" + String.format("%02d", segregation_code) + ref); 
		return "3" +  String.format("%02d", segregation_code) + ref + check;
	}
	
	public static String getCheckDigit93(String reference) {
		long resto93 = (Long.parseLong(reference)) % 93;
		return String.format("%02d", resto93);
	}
	
	public String getIuvFromNumeroAvviso(String numeroAvviso) throws IuvFormatError {
		if(numeroAvviso == null)
			return null;
		
		if(numeroAvviso.length() != 18)
			throw new IuvFormatError("Numero Avviso [" + numeroAvviso + "] fornito non valido: Consentite 18 cifre trovate ["+numeroAvviso.length()+"].");
		
		try {
			Long.parseLong(numeroAvviso);
		}catch(Exception e) {
			throw new IuvFormatError("Numero Avviso [" + numeroAvviso + "] fornito non valido: non e' in formato numerico.");
		}
		
		if(numeroAvviso.startsWith("0")) // '0' + applicationCode(2) + ref(13) + check(2)
			return numeroAvviso.substring(3);
		else if(numeroAvviso.startsWith("1")) // '1' + reference(17)
			return numeroAvviso.substring(1);
		else if(numeroAvviso.startsWith("2")) // '2' + ref(15) + check(2)
			return numeroAvviso.substring(1);
		else if(numeroAvviso.startsWith("3")) // '3' + segregationCode(2) +  ref(13) + check(2) 
			return numeroAvviso.substring(1);
		else 
			throw new IuvFormatError("Numero Avviso [" + numeroAvviso + "] fornito non valido: prima cifra non e' [0|1|2|3]");
	}
}
