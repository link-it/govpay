package it.govpay.core.dao.pagamenti.dto;

import java.util.Date;

import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.model.IAutorizzato;
import it.govpay.model.Versamento.StatoVersamento;
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
	private StatoVersamento stato;
	private String idDominio;
	private String idPagamento;
	private String idDebitore;
	private String idA2A;
	
	public Date getDataA() {
		return dataA;
	}
	public void setDataA(Date dataA) {
		this.dataA = dataA;
	}
	public Date getDataDa() {
		return dataDa;
	}
	public void setDataDa(Date dataDa) {
		this.dataDa = dataDa;
	}
	public StatoVersamento getStato() {
		return stato;
	}
	public void setStato(StatoVersamento stato) {
		this.stato = stato;
	}
	public String getIdDominio() {
		return idDominio;
	}
	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}
	public String getIdPagamento() {
		return idPagamento;
	}
	public void setIdPagamento(String idPagamento) {
		this.idPagamento = idPagamento;
	}
	public String getIdDebitore() {
		return idDebitore;
	}
	public void setIdDebitore(String idDebitore) {
		this.idDebitore = idDebitore;
	}
	public String getIdA2A() {
		return idA2A;
	}
	public void setIdA2A(String idA2A) {
		this.idA2A = idA2A;
	}
}
