package it.govpay.core.loader.timers.model;

import org.openspcoop2.generic_project.exception.ValidationException;
import org.openspcoop2.utils.csv.Record;

import it.govpay.model.loader.Operazione.TipoOperazioneType;


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
}
