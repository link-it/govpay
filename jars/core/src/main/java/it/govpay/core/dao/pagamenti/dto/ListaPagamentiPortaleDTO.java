package it.govpay.core.dao.pagamenti.dto;

import java.util.Date;

import org.openspcoop2.generic_project.expression.SortOrder;
import org.springframework.security.core.Authentication;

import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.orm.PagamentoPortale;

public class ListaPagamentiPortaleDTO extends BasicFindRequestDTO{
	
	public ListaPagamentiPortaleDTO(Authentication user) {
		super(user);
		this.addSortField("dataRichiestaPagamento", PagamentoPortale.model().DATA_RICHIESTA);
		this.addSortField("stato", PagamentoPortale.model().STATO);
		this.addDefaultSort(PagamentoPortale.model().DATA_RICHIESTA,SortOrder.DESC);
	}
	private Date dataA;
	private Date dataDa;
	private String stato;
	private String versante;
	private Boolean verificato;
	private String idSessionePortale;
	private String idSessionePsp;
	
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
	public String getStato() {
		return this.stato;
	}
	public void setStato(String stato) {
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
}
