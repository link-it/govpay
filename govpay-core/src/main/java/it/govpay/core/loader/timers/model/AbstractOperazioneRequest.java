package it.govpay.core.loader.timers.model;

import it.govpay.model.loader.Operazione.TipoOperazioneType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openspcoop2.generic_project.exception.ValidationException;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.csv.Record;


public abstract class AbstractOperazioneRequest {

	private Long idTracciato;
	private Long linea;
	private byte[] dati;
	private TipoOperazioneType tipoOperazione;
	
	public AbstractOperazioneRequest(TipoOperazioneType tipoOperazione, Record record) throws ValidationException {
		this.tipoOperazione = tipoOperazione;
	}

	public Long getLinea() {
		return linea;
	}
	public void setLinea(Long linea) {
		this.linea = linea;
	}
	public byte[] getDati() {
		return dati;
	}
	public void setDati(byte[] dati) {
		this.dati = dati;
	}
	public Long getIdTracciato() {
		return idTracciato;
	}
	public void setIdTracciato(Long idTracciato) {
		this.idTracciato = idTracciato;
	}

	public TipoOperazioneType getTipoOperazione() {
		return tipoOperazione;
	}

	public void setTipoOperazione(TipoOperazioneType tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}
	
	
	protected String validaESettaRecord(Record record, String nomeCampo, Integer lunghezzaMax, Integer lunghezzaMin, boolean nullable) throws ValidationException {
		try{
			if(record == null){
				throw new ValidationException("Record nullo"); 
			}
			return this.validaESetta(nomeCampo, record.getMap().get(nomeCampo), lunghezzaMax, lunghezzaMin, nullable);
		} catch(UtilsException e){
			throw new ValidationException(e);
		}
	}
	
	protected String validaESetta(String nomeCampo, String campo, Integer lunghezzaMax, Integer lunghezzaMin, boolean nullable) throws ValidationException {
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
	
	protected Double validaESettaDouble(String nomeCampo, String campo, Double max, Double min, boolean nullable) throws ValidationException {
		
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
	
	protected Date validaESettaDate(String nomeCampo, String campo, boolean nullable) throws ValidationException {
		
		
		String nomeValoreCampo = nomeCampo + " - valore["+campo+"]"; 

		if(campo == null) {
			if(!nullable) {
				throw new ValidationException("Campo "+nomeValoreCampo+" null");
			} else {
				return null;
			}
		}

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
			return sdf.parse(validaESetta(nomeCampo, campo, null, null, nullable));
		} catch (ParseException e) {
			throw new ValidationException("Campo "+nomeValoreCampo+" non e' una data espressa in formato dd/MM/YYYY");
		}
	}

}
