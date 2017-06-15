package it.govpay.core.loader.timers.model;

import it.govpay.orm.constants.StatoOperazioneType;


public abstract class AbstractOperazioneResponse {
	
	private StatoOperazioneType stato;
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

}
