package it.govpay.core.exceptions;

public class VersamentoException extends Exception {

	private static final long serialVersionUID = 1L;

	private String codVersamentoEnte;
	private String codApplicazione;
	private String bundlekey;
	private String codUnivocoDebitore;
	private String codDominio;
	private String iuv;

	public VersamentoException() {
		super();
	}

	public VersamentoException(String codApplicazione, String codVersamentoEnte, String bundlekey, String codUnivocoDebitore,
			String codDominio, String iuv, String message) {
		super(message);
		this.codApplicazione = codApplicazione;
		this.codVersamentoEnte = codVersamentoEnte;
		this.bundlekey = bundlekey;
		this.codUnivocoDebitore = codUnivocoDebitore;
		this.codDominio = codDominio;
		this.iuv = iuv;
	}

	public VersamentoException(String codApplicazione, String codVersamentoEnte, String bundlekey, String codUnivocoDebitore,
			String codDominio, String iuv, Throwable t) {
		super(t);
		this.codApplicazione = codApplicazione;
		this.codVersamentoEnte = codVersamentoEnte;
		this.bundlekey = bundlekey;
		this.codUnivocoDebitore = codUnivocoDebitore;
		this.codDominio = codDominio;
		this.iuv = iuv;
	}

	public VersamentoException(String codApplicazione, String codVersamentoEnte, String bundlekey, String codUnivocoDebitore,
			String codDominio, String iuv, String message, Throwable t) {
		super(message,t);
		this.codApplicazione = codApplicazione;
		this.codVersamentoEnte = codVersamentoEnte;
		this.bundlekey = bundlekey;
		this.codUnivocoDebitore = codUnivocoDebitore;
		this.codDominio = codDominio;
		this.iuv = iuv;
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

	public String getBundlekey() {
		return bundlekey;
	}

	public void setBundlekey(String bundlekey) {
		this.bundlekey = bundlekey;
	}

	public String getCodUnivocoDebitore() {
		return codUnivocoDebitore;
	}

	public void setCodUnivocoDebitore(String codUnivocoDebitore) {
		this.codUnivocoDebitore = codUnivocoDebitore;
	}

	public String getCodDominio() {
		return codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public String getIuv() {
		return iuv;
	}

	public void setIuv(String iuv) {
		this.iuv = iuv;
	}

}
