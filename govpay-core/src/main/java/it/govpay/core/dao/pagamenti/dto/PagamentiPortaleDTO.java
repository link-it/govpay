package it.govpay.core.dao.pagamenti.dto;

import java.util.Date;
import java.util.List;

import it.govpay.core.dao.anagrafica.dto.BasicCreateRequestDTO;
import it.govpay.core.dao.commons.Anagrafica;
import it.govpay.model.IAutorizzato;

public class PagamentiPortaleDTO  extends BasicCreateRequestDTO{

	public PagamentiPortaleDTO(IAutorizzato user) {
		super(user);
	}

	private String idSessione = null;
	private String idSessionePortale =null;
	private String jsonRichiesta = null;
	private String urlRitorno = null;
	private String ibanAddebito =null;
	private String bicAddebito = null;
	private Date dataEsecuzionePagamento = null;
	private String credenzialiPagatore = null;
	private String lingua = null;
	private Anagrafica versante = null;
	private List<Object> pendenzeOrPendenzeRef = null;
	private String idDominio = null;
	private String keyPA = null;
	private String keyWISP = null;
	private String autenticazioneSoggetto = null;

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
	public Anagrafica getVersante() {
		return versante;
	}
	public void setVersante(Anagrafica versante) {
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
	public String getAutenticazioneSoggetto() {
		return autenticazioneSoggetto;
	}
	public void setAutenticazioneSoggetto(String autenticazioneSoggetto) {
		this.autenticazioneSoggetto = autenticazioneSoggetto;
	}


	public class RefVersamentoAvviso {

		private String idDominio;
		private String numeroAvviso;
		
		public String getIdDominio() {
			return idDominio;
		}
		public void setIdDominio(String idDominio) {
			this.idDominio = idDominio;
		}
		public String getNumeroAvviso() {
			return numeroAvviso;
		}
		public void setNumeroAvviso(String numeroAvviso) {
			this.numeroAvviso = numeroAvviso;
		}
	}

	public class RefVersamentoPendenza {

		private String idA2A;
		private String idPendenza;
		public String getIdA2A() {
			return idA2A;
		}
		public void setIdA2A(String idA2A) {
			this.idA2A = idA2A;
		}
		public String getIdPendenza() {
			return idPendenza;
		}
		public void setIdPendenza(String idPendenza) {
			this.idPendenza = idPendenza;
		}
	}

}
