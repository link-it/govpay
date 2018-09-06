package it.govpay.core.dao.pagamenti.dto;

import java.util.Date;

import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.model.IAutorizzato;
import it.govpay.orm.Versamento;

public class ListaPendenzeDTO extends BasicFindRequestDTO{
	
	public ListaPendenzeDTO(IAutorizzato user) {
		super(user);
		this.addSortField("dataCaricamento", Versamento.model().DATA_CREAZIONE);
		this.addSortField("dataValidita", Versamento.model().DATA_VALIDITA);
		this.addSortField("dataSacadenza", Versamento.model().DATA_SCADENZA);
		this.addSortField("stato", Versamento.model().STATO_VERSAMENTO);
		this.setDefaultSort(Versamento.model().DATA_CREAZIONE,SortOrder.DESC);
	}
	private Date dataA;
	private Date dataDa;
	private String stato;
	private String idDominio;
	private String idPagamento;
	private String idDebitore;
	private String idA2A;
	
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
	public String getIdDominio() {
		return this.idDominio;
	}
	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}
	public String getIdPagamento() {
		return this.idPagamento;
	}
	public void setIdPagamento(String idPagamento) {
		this.idPagamento = idPagamento;
	}
	public String getIdDebitore() {
		return this.idDebitore;
	}
	public void setIdDebitore(String idDebitore) {
		this.idDebitore = idDebitore;
	}
	public String getIdA2A() {
		return this.idA2A;
	}
	public void setIdA2A(String idA2A) {
		this.idA2A = idA2A;
	}
}
