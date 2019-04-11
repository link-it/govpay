package it.govpay.core.dao.pagamenti.dto;

public class RedirectDaPspDTO {

	private String idSession = null;
	private String esito = null;

	public String getIdSession() {
		return this.idSession;
	}
	public void setIdSession(String idSession) {
		this.idSession = idSession;
	}
	public String getEsito() {
		return this.esito;
	}
	public void setEsito(String esito) {
		this.esito = esito;
	}
}
