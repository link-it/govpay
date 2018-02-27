package it.govpay.core.dao.anagrafica.exception;

import it.govpay.core.dao.commons.exception.RedirectException;

public class TipoTributoNonTrovatoException extends RedirectException{

	public TipoTributoNonTrovatoException(String location) {
		super(location);
	}
	
	public TipoTributoNonTrovatoException(String location, String message) {
		super(location,message);
	}
	
	public TipoTributoNonTrovatoException(String location,Throwable t) {
		super(location, t);
	}
	
	public TipoTributoNonTrovatoException(String location, String message ,Throwable t) {
		super(location,message,t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
