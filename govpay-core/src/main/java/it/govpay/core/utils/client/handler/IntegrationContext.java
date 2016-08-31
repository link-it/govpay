package it.govpay.core.utils.client.handler;

import it.govpay.bd.model.Applicazione;


public class IntegrationContext {

	private byte[] msg;
	private Applicazione applicazione;
	
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
}
