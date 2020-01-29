package it.govpay.core.dao.pagamenti.dto;

import java.util.Date;

import org.openspcoop2.generic_project.expression.SortOrder;
import org.springframework.security.core.Authentication;

import it.govpay.bd.model.PagamentoPortale.STATO;
import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;

public class ListaPagamentiPortaleDTO extends BasicFindRequestDTO{
	
	public ListaPagamentiPortaleDTO(Authentication user) {
		super(user);
		this.addSortField("dataRichiestaPagamento", it.govpay.orm.VistaPagamentoPortale.model().DATA_RICHIESTA);
		this.addSortField("stato", it.govpay.orm.VistaPagamentoPortale.model().STATO);
		this.addDefaultSort(it.govpay.orm.VistaPagamentoPortale.model().DATA_RICHIESTA,SortOrder.DESC);
	}
	private Date dataA;
	private Date dataDa;
	private STATO stato;
	private String versante;
	private Boolean verificato;
	private String idSessionePortale;
	private String idSessionePsp;
	private String cfCittadino;
	private String codApplicazione;
	private String idSessione;
	
	public String getIdSessione() {
		return idSessione;
	}
	public void setIdSessione(String idSessione) {
		this.idSessione = idSessione;
	}
	public Date getDataA() {
		return this.dataA;
	}
	public void setDataA(Date dataA) {
		this.dataA = dataA;
	}
	public Date getDataDa() {
		return this.dataDa;
	}
	public void setDataDa(Date dataDa) {
		this.dataDa = dataDa;
	}
	public STATO getStato() {
		return this.stato;
	}
	public void setStato(STATO stato) {
		this.stato = stato;
	}
	public String getVersante() {
		return this.versante;
	}
	public void setVersante(String versante) {
		this.versante = versante;
	}
	public Boolean getVerificato() {
		return this.verificato;
	}
	public void setVerificato(Boolean verificato) {
		this.verificato = verificato;
	}
	public String getIdSessionePortale() {
		return this.idSessionePortale;
	}
	public void setIdSessionePortale(String idSessionePortale) {
		this.idSessionePortale = idSessionePortale;
	}
	public String getIdSessionePsp() {
		return idSessionePsp;
	}
	public void setIdSessionePsp(String idSessionePsp) {
		this.idSessionePsp = idSessionePsp;
	}
	public String getCfCittadino() {
		return cfCittadino;
	}
	public void setCfCittadino(String cfCittadino) {
		this.cfCittadino = cfCittadino;
	}
	public String getCodApplicazione() {
		return codApplicazione;
	}
	public void setCodApplicazione(String codApplicazione) {
		this.codApplicazione = codApplicazione;
	}
	
}
