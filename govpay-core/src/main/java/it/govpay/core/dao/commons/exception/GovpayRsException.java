package it.govpay.core.dao.commons.exception;

import it.govpay.core.rs.v1.beans.base.FaultBean.CategoriaEnum;

public class GovpayRsException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer status;
	private CategoriaEnum categoria;
	private String codice;
	
	public GovpayRsException(String codice, Integer status, CategoriaEnum categoria) {
		super();
		this.codice = codice;
		this.status = status;
		this.categoria = categoria;
	}
	
	public GovpayRsException(String codice, Integer status, CategoriaEnum categoria, String message) {
		super(message);
		this.codice = codice;
		this.status = status;
		this.categoria = categoria;
	}
	
	public GovpayRsException(String codice, Integer status, CategoriaEnum categoria,Throwable t) {
		super(t);
		this.codice = codice;
		this.status = status;
		this.categoria = categoria;
	}
	
	public GovpayRsException(String codice, Integer status, CategoriaEnum categoria, String message ,Throwable t) {
		super(message,t);
		this.codice = codice;
		this.status = status;
		this.categoria = categoria;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public CategoriaEnum getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaEnum categoria) {
		this.categoria = categoria;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}
}
