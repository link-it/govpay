package it.govpay.core.utils.tracciati.operazioni;

import java.util.List;

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
	protected byte[] createDati() {
		StringBuilder sb = new StringBuilder();

		for(String dato: this.listDati()) {
			if(sb.length() > 0) {
				sb.append(this.delim);
			}
			sb.append(dato);
		}
		
		return sb.toString().getBytes();
	}
	
	protected abstract List<String> listDati();
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
