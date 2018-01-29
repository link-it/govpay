package it.govpay.pagamento.api.rs.v1.model;

import java.util.Date;
import java.util.List;

public class PagamentiPortaleRequest {

	private String urlRitorno = null;
	private TokenWISP tokenWISP = null;
	private DatiAddebito datiAddebito = null;
	private Date dataEsecuzionePagamento = null;
	private String credenzialiPagatore= null;
	private Anagrafica soggettoVersante = null;
	private String lingua = null;
	private String autenticazioneSoggetto= null;
	
	public String getUrlRitorno() {
		return urlRitorno;
	}
	public void setUrlRitorno(String urlRitorno) {
		this.urlRitorno = urlRitorno;
	}
	public TokenWISP getTokenWISP() {
		return tokenWISP;
	}
	public void setTokenWISP(TokenWISP tokenWISP) {
		this.tokenWISP = tokenWISP;
	}
	public DatiAddebito getDatiAddebito() {
		return datiAddebito;
	}
	public void setDatiAddebito(DatiAddebito datiAddebito) {
		this.datiAddebito = datiAddebito;
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
	public Anagrafica getSoggettoVersante() {
		return soggettoVersante;
	}
	public void setSoggettoVersante(Anagrafica soggettoVersante) {
		this.soggettoVersante = soggettoVersante;
	}
	public String getLingua() {
		return lingua;
	}
	public void setLingua(String lingua) {
		this.lingua = lingua;
	}
	public List<Object> getPendenze() {
		return pendenze;
	}
	public void setPendenze(List<Object> pendenze) {
		this.pendenze = pendenze;
	}
	public String getAutenticazioneSoggetto() {
		return autenticazioneSoggetto;
	}
	public void setAutenticazioneSoggetto(String autenticazioneSoggetto) {
		this.autenticazioneSoggetto = autenticazioneSoggetto;
	}
	private List<Object> pendenze = null;
	
	
}
