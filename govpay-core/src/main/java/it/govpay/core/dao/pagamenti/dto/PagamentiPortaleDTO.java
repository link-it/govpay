package it.govpay.core.dao.pagamenti.dto;

import java.util.Date;
import java.util.List;

public class PagamentiPortaleDTO {
	
	private String idSessione = null;
	private String idSessionePortale =null;
	private String principal = null; 
	private String jsonRichiesta = null;
	private String urlRitorno = null;
	private String ibanAddebito =null;
	private String bicAddebito = null;
	private Date dataEsecuzionePagamento = null;
	private String credenzialiPagatore = null;
	private String lingua = null;
	private it.govpay.servizi.commons.Anagrafica versante = null;
	private List<Object> pendenzeOrPendenzeRef = null;
	private String idDominio = null;
	private String keyPA = null;
	private String keyWISP = null;
	
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public String getJsonRichiesta() {
		return jsonRichiesta;
	}
	public void setJsonRichiesta(String jsonRichiesta) {
		this.jsonRichiesta = jsonRichiesta;
	}
	public String getUrlRitorno() {
		return urlRitorno;
	}
	public void setUrlRitorno(String urlRitorno) {
		this.urlRitorno = urlRitorno;
	}
	public String getIbanAddebito() {
		return ibanAddebito;
	}
	public void setIbanAddebito(String ibanAddebito) {
		this.ibanAddebito = ibanAddebito;
	}
	public String getBicAddebito() {
		return bicAddebito;
	}
	public void setBicAddebito(String bicAddebito) {
		this.bicAddebito = bicAddebito;
	}
	public Date getDataEsecuzionePagamento() {
		return dataEsecuzionePagamento;
	}
	public void setDataEsecuzionePagamento(Date dataEsecuzionePagamento) {
		this.dataEsecuzionePagamento = dataEsecuzionePagamento;
	}
	public String getCredenzialiPagatore() {
		return credenzialiPagatore;
	}
	public void setCredenzialiPagatore(String credenzialiPagatore) {
		this.credenzialiPagatore = credenzialiPagatore;
	}
	public String getLingua() {
		return lingua;
	}
	public void setLingua(String lingua) {
		this.lingua = lingua;
	}
	public it.govpay.servizi.commons.Anagrafica getVersante() {
		return versante;
	}
	public void setVersante(it.govpay.servizi.commons.Anagrafica versante) {
		this.versante = versante;
	}
	public List<Object> getPendenzeOrPendenzeRef() {
		return pendenzeOrPendenzeRef;
	}
	public void setPendenzeOrPendenzeRef(List<Object> pendenzeOrPendenzeRef) {
		this.pendenzeOrPendenzeRef = pendenzeOrPendenzeRef;
	}
	public String getIdDominio() {
		return idDominio;
	}
	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}
	public String getKeyPA() {
		return keyPA;
	}
	public void setKeyPA(String keyPA) {
		this.keyPA = keyPA;
	}
	public String getKeyWISP() {
		return keyWISP;
	}
	public void setKeyWISP(String keyWISP) {
		this.keyWISP = keyWISP;
	}
	public String getIdSessione() {
		return idSessione;
	}
	public void setIdSessione(String idSessione) {
		this.idSessione = idSessione;
	}
	public String getIdSessionePortale() {
		return idSessionePortale;
	}
	public void setIdSessionePortale(String idSessionePortale) {
		this.idSessionePortale = idSessionePortale;
	}
	
}
