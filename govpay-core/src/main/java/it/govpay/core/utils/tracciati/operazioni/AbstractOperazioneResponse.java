package it.govpay.core.utils.tracciati.operazioni;

import it.govpay.model.Operazione.StatoOperazioneType;


public abstract class AbstractOperazioneResponse {
	
	private String delim;
	private StatoOperazioneType stato;
	private String esito;
	private String descrizioneEsito;
	private byte[] dati;
	
	public StatoOperazioneType getStato() {
		return stato;
	}
	public void setStato(StatoOperazioneType stato) {
		this.stato = stato;
	}

	public String getDescrizioneEsito() {
		return descrizioneEsito;
	}
	public void setDescrizioneEsito(String descrizioneEsito) {
		this.descrizioneEsito = descrizioneEsito;
	}
	public byte[] getDati() {
		if(this.dati == null) {
			this.dati = createDati();
		}
		return dati;
	}
	protected abstract byte[] createDati();
	public String getEsito() {
		return esito;
	}
	public void setEsito(String esito) {
		this.esito = esito;
	}
	public String getDelim() {
		return delim;
	}
	public void setDelim(String delim) {
		this.delim = delim;
	}

}
