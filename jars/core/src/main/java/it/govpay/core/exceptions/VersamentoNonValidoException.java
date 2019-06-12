package it.govpay.core.exceptions;

public class VersamentoNonValidoException extends VersamentoException {

	private static final long serialVersionUID = 1L;

	public VersamentoNonValidoException() {
		super();
	}
	
	public VersamentoNonValidoException(String codApplicazione, String codVersamentoEnte, String message) {
		super(codApplicazione, codVersamentoEnte, message);
	}

}
