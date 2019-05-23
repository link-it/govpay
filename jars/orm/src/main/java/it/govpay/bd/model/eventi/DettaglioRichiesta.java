package it.govpay.bd.model.eventi;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class DettaglioRichiesta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 
	private String principal;
	private String utente;
	private Date dataOraRichiesta;
	private String url;
	private Map<String, String> headers;
	private String payload; 
	private String method;
	
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public Date getDataOraRichiesta() {
		return dataOraRichiesta;
	}
	public void setDataOraRichiesta(Date dataOraRichiesta) {
		this.dataOraRichiesta = dataOraRichiesta;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Map<String, String> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	public String getPayload() {
		return payload;
	}
	public void setPayload(String payload) {
		this.payload = payload;
	}
	public String getUtente() {
		return utente;
	}
	public void setUtente(String utente) {
		this.utente = utente;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}

}
