package it.govpay.core.dao.pagamenti.exception;

import it.govpay.core.dao.commons.exception.RedirectException;

public class PagamentoPortaleNonTrovatoException extends RedirectException{

	public PagamentoPortaleNonTrovatoException(String location) {
		super(location);
	}
	
	public PagamentoPortaleNonTrovatoException(String location, String message) {
		super(location,message);
	}
	
	public PagamentoPortaleNonTrovatoException(String location,Throwable t) {
		super(location, t);
	}
	
	public PagamentoPortaleNonTrovatoException(String location, String message ,Throwable t) {
		super(location,message,t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
