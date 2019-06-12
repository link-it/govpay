package it.govpay.bd.model.eventi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.JsonFormat;

public class DettaglioRisposta implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	private Date dataOraRisposta;
	private Integer status;
//	private Map<String, String> headers;
	private List<Entry<String, String>> headers;
	
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
	public List<Entry<String, String>> getHeaders() {
		return headers;
	}
	public void setHeaders(List<Entry<String, String>> headers) {
		this.headers = headers;
	}
	public void setHeadersFromMap(Map<String, String> headers) {
		if(headers != null) {
			this.headers = new ArrayList<>();
			this.headers.addAll(headers.entrySet());
		}
	}
	public String getPayload() {
		return payload;
	}
	public void setPayload(String payload) {
		this.payload = payload;
	}
}
