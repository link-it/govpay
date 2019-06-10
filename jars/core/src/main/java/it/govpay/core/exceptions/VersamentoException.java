package it.govpay.core.exceptions;

public class VersamentoException extends Exception {

	private static final long serialVersionUID = 1L;

	private String codVersamentoEnte;
	private String codApplicazione;

	public VersamentoException() {
		super();
	}

	public VersamentoException(String codApplicazione, String codVersamentoEnte, String message) {
		super(message);
		this.codApplicazione = codApplicazione;
		this.codVersamentoEnte = codVersamentoEnte;
	}

	public VersamentoException(String codApplicazione, String codVersamentoEnte, Throwable t) {
		super(t);
		this.codApplicazione = codApplicazione;
		this.codVersamentoEnte = codVersamentoEnte;
	}

	public VersamentoException(String codApplicazione, String codVersamentoEnte, String message, Throwable t) {
		super(message,t);
		this.codApplicazione = codApplicazione;
		this.codVersamentoEnte = codVersamentoEnte;
	}


	public String getCodVersamentoEnte() {
		return codVersamentoEnte;
	}

	public void setCodVersamentoEnte(String codVersamentoEnte) {
		this.codVersamentoEnte = codVersamentoEnte;
	}

	public String getCodApplicazione() {
		return codApplicazione;
	}

	public void setCodApplicazione(String codApplicazione) {
		this.codApplicazione = codApplicazione;
	}


}
