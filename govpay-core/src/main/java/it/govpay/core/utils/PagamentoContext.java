package it.govpay.core.utils;

public class PagamentoContext {
	
	private String codSessionePortale;
	private boolean carrello;
	private String codCarrello;
	private String codDominio;
	private String iuv;
	private String ccp;
	private boolean pspRedirect;
	private String pspSessionId;
	
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
	public String getCcp() {
		return ccp;
	}
	public void setCcp(String ccp) {
		this.ccp = ccp;
	}
	public boolean isPspRedirect() {
		return pspRedirect;
	}
	public void setPspRedirect(boolean pspRedirect) {
		this.pspRedirect = pspRedirect;
	}
	public String getPspSessionId() {
		return pspSessionId;
	}
	public void setPspSessionId(String pspSessionId) {
		this.pspSessionId = pspSessionId;
	}
	public String getCodSessionePortale() {
		return codSessionePortale;
	}
	public void setCodSessionePortale(String codSessionePortale) {
		this.codSessionePortale = codSessionePortale;
	}
	public boolean isCarrello() {
		return carrello;
	}
	public void setCarrello(boolean carrello) {
		this.carrello = carrello;
	}
	public String getCodCarrello() {
		return codCarrello;
	}
	public void setCodCarrello(String codCarrello) {
		this.codCarrello = codCarrello;
	}

}
