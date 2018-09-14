package it.govpay.core.utils.client.handler;

import it.govpay.core.utils.client.BasicClient.TipoConnettore;
import it.govpay.core.utils.client.BasicClient.TipoDestinatario;
import it.govpay.model.Applicazione;
import it.govpay.model.Intermediario;


public class IntegrationContext {

	private byte[] msg;
	private Applicazione applicazione;
	private Intermediario intermediario;
	private TipoConnettore tipoConnettore;
	private TipoDestinatario tipoDestinatario;
	
	public byte[] getMsg() {
		return msg;
	}
	public void setMsg(byte[] msg) {
		this.msg = msg;
	}
	public Applicazione getApplicazione() {
		return applicazione;
	}
	public void setApplicazione(Applicazione applicazione) {
		this.applicazione = applicazione;
	}
	public Intermediario getIntermediario() {
		return intermediario;
	}
	public void setIntermediario(Intermediario intermediario) {
		this.intermediario = intermediario;
	}
	public TipoConnettore getTipoConnettore() {
		return tipoConnettore;
	}
	public void setTipoConnettore(TipoConnettore tipoConnettore) {
		this.tipoConnettore = tipoConnettore;
	}
	public TipoDestinatario getTipoDestinatario() {
		return tipoDestinatario;
	}
	public void setTipoDestinatario(TipoDestinatario tipoDestinatario) {
		this.tipoDestinatario = tipoDestinatario;
	}
}
