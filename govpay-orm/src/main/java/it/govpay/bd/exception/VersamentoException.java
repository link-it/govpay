package it.govpay.bd.exception;

public class VersamentoException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    /**
     * Versamento inesistente
     * 
     */
    public static final String VER_008 = "VER_008";

    /**
     * Non è possibile annullare un versamento in stato diverso da NON_ESEGUITO
     * 
     */
    public static final String VER_009 = "VER_009";
	
	private String codEsito;
	private String codVersamentoEnte;

	public VersamentoException(){
		super();
	}
	
	public VersamentoException(String codVersamentoEnte, String codEsito){
		super();
		this.codEsito = codEsito;
		this.codVersamentoEnte = codVersamentoEnte;
	}
	
	public VersamentoException(String codVersamentoEnte, String codEsito, Throwable t){
		super(t);
		this.codEsito = codEsito;
		this.codVersamentoEnte = codVersamentoEnte;
	}
	
	public VersamentoException(String codVersamentoEnte, String codEsito, String message,Throwable t){
		super(message,t);
		this.codEsito = codEsito;
		this.codVersamentoEnte = codVersamentoEnte;
	}

	public String getCodEsito() {
		return this.codEsito;
	}

	public void setCodEsito(String codEsito) {
		this.codEsito = codEsito;
	}

	public String getCodVersamentoEnte() {
		return this.codVersamentoEnte;
	}

	public void setCodVersamentoEnte(String codVersamentoEnte) {
		this.codVersamentoEnte = codVersamentoEnte;
	}
	
}
