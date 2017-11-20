package it.govpay.core.business.model;

import it.govpay.model.Operatore;
import it.govpay.model.Operazione.StatoOperazioneType;
import it.govpay.model.Operazione.TipoOperazioneType;

public class ListaOperazioniDTO {

	private long idTracciato;
	private StatoOperazioneType stato;
	private TipoOperazioneType tipo;
	private Operatore operatore;
	
	public Operatore getOperatore() {
		return operatore;
	}
	public void setOperatore(Operatore operatore) {
		this.operatore = operatore;
	}
	public long getIdTracciato() {
		return idTracciato;
	}
	public void setIdTracciato(long idTracciato) {
		this.idTracciato = idTracciato;
	}
	public StatoOperazioneType getStato() {
		return stato;
	}
	public void setStato(StatoOperazioneType stato) {
		this.stato = stato;
	}
	public TipoOperazioneType getTipo() {
		return tipo;
	}
	public void setTipo(TipoOperazioneType tipo) {
		this.tipo = tipo;
	}
	
}
