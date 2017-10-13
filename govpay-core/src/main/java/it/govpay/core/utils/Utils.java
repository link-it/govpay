package it.govpay.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openspcoop2.generic_project.exception.ValidationException;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.csv.Record;

public class Utils {

	public static String validaESettaRecord(Record record, String nomeCampo, Integer lunghezzaMax, Integer lunghezzaMin, boolean nullable) throws ValidationException {
		try{
			if(record == null){
				throw new ValidationException("Record nullo"); 
			}
			return Utils.validaESetta(nomeCampo, record.getMap().get(nomeCampo), lunghezzaMax, lunghezzaMin, nullable);
		} catch(UtilsException e){
			throw new ValidationException(e);
		}
	}
	
	public static String validaESetta(String nomeCampo, String campo, Integer lunghezzaMax, Integer lunghezzaMin, boolean nullable) throws ValidationException {
		String nomeValoreCampo = nomeCampo + " - valore["+campo+"]"; 

		if(campo == null) {
			if(!nullable) {
				throw new ValidationException("Campo "+nomeValoreCampo+" null");
			} else {
				return null;
			}
		}
		
		if(lunghezzaMax != null) {
			if(campo.length() > lunghezzaMax) {
				throw new ValidationException("Lunghezza del campo "+nomeValoreCampo+" maggiore di " + lunghezzaMax);
			}
		}
		
		if(lunghezzaMin != null) {
			if(campo.length() < lunghezzaMin) {
				throw new ValidationException("Lunghezza del campo "+nomeValoreCampo+" minore di " + lunghezzaMin);
			}
		}
		
		return campo.trim();
	}
	
	public static Double validaESettaDouble(String nomeCampo, String campo, Double max, Double min, boolean nullable) throws ValidationException {
		
		String nomeValoreCampo = nomeCampo + " - valore["+campo+"]"; 

		if(campo == null) {
			if(!nullable) {
				throw new ValidationException("Campo "+nomeValoreCampo+" null");
			} else {
				return null;
			}
		}
		
		Double campoDouble = null;
		try {
			campoDouble = Double.parseDouble(validaESetta(nomeCampo, campo, null, null, nullable));
		} catch(NumberFormatException e) {
			throw new ValidationException("Campo "+nomeValoreCampo+" non e' un double");
		}
		if(max != null) {
			if(campoDouble > max) {
				throw new ValidationException("Campo "+nomeValoreCampo+" maggiore di " + max);
			}
		}
		
		if(min != null) {
			if(campoDouble < min) {
				throw new ValidationException("Campo "+nomeValoreCampo+" minore di " + min);
			}
		}
		
		return campoDouble;
	}
	
	public static Date validaESettaDate(String nomeCampo, String campo, boolean nullable) throws ValidationException {
		
		
		String nomeValoreCampo = nomeCampo + " - valore["+campo+"]"; 

		if(campo == null) {
			if(!nullable) {
				throw new ValidationException("Campo "+nomeValoreCampo+" null");
			} else {
				return null;
			}
		}

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			return sdf.parse(validaESetta(nomeCampo, campo, null, null, nullable));
		} catch (ParseException e) {
			throw new ValidationException("Campo "+nomeValoreCampo+" non e' una data espressa in formato dd/MM/YYYY");
		}
	}
}
