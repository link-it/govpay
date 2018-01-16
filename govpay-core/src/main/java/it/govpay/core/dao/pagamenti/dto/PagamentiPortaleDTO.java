package it.govpay.core.dao.pagamenti.dto;

import java.util.Date;

public class PagamentiPortaleDTO {
	
	private String principal = null; 
	private String returnUrl = null; 
	private String identificativiVersamenti = null;
	private String tokenWISP = null;
	private Date dataEsecuzione =null;
	private String datiAddebito = null;
	private String credenzialiPagatore = null;
	private String anagraficaVersante = null;
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public String getReturnUrl() {
		return returnUrl;
	}
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
	public String getIdentificativiVersamenti() {
		return identificativiVersamenti;
	}
	public void setIdentificativiVersamenti(String identificativiVersamenti) {
		this.identificativiVersamenti = identificativiVersamenti;
	}
	public String getTokenWISP() {
		return tokenWISP;
	}
	public void setTokenWISP(String tokenWISP) {
		this.tokenWISP = tokenWISP;
	}
	public Date getDataEsecuzione() {
		return dataEsecuzione;
	}
	public void setDataEsecuzione(Date dataEsecuzione) {
		this.dataEsecuzione = dataEsecuzione;
	}
	public String getDatiAddebito() {
		return datiAddebito;
	}
	public void setDatiAddebito(String datiAddebito) {
		this.datiAddebito = datiAddebito;
	}
	public String getCredenzialiPagatore() {
		return credenzialiPagatore;
	}
	public void setCredenzialiPagatore(String credenzialiPagatore) {
		this.credenzialiPagatore = credenzialiPagatore;
	}
	public String getAnagraficaVersante() {
		return anagraficaVersante;
	}
	public void setAnagraficaVersante(String anagraficaVersante) {
		this.anagraficaVersante = anagraficaVersante;
	}

	
}
