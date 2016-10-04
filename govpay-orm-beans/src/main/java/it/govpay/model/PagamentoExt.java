package it.govpay.model;

public class PagamentoExt extends Pagamento{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 
	
	private String codDominio;
	private String iuv;
	private String debitoreIdentificativo;
	private String debitoreAnagrafica;
	private String codAnnoTributario;
	private String codVersamentoLotto;
	private String codTributo;
	private String note;
		
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
	public String getDebitoreIdentificativo() {
		return debitoreIdentificativo;
	}
	public void setDebitoreIdentificativo(String debitoreIdentificativo) {
		this.debitoreIdentificativo = debitoreIdentificativo;
	}
	public String getCodAnnoTributario() {
		return codAnnoTributario;
	}
	public void setCodAnnoTributario(String codAnnoTributario) {
		this.codAnnoTributario = codAnnoTributario;
	}
	public String getCodVersamentoLotto() {
		return codVersamentoLotto;
	}
	public void setCodVersamentoLotto(String codVersamentoLotto) {
		this.codVersamentoLotto = codVersamentoLotto;
	}
	public String getCodTributo() {
		return codTributo;
	}
	public void setCodTributo(String codTributo) {
		this.codTributo = codTributo;
	}
	public String getDebitoreAnagrafica() {
		return debitoreAnagrafica;
	}
	public void setDebitoreAnagrafica(String debitoreAnagrafica) {
		this.debitoreAnagrafica = debitoreAnagrafica;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}

}
