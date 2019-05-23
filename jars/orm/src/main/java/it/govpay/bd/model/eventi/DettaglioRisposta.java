package it.govpay.bd.model.eventi;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class DettaglioRisposta implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Date dataOraRisposta;
	private Integer status;
	private Map<String, String> headers;
	private String payload;
	
	public Date getDataOraRisposta() {
		return dataOraRisposta;
	}
	public void setDataOraRisposta(Date dataOraRisposta) {
		this.dataOraRisposta = dataOraRisposta;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
}
