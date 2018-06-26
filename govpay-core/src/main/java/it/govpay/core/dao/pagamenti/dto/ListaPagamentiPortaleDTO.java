package it.govpay.core.dao.pagamenti.dto;

import java.util.Date;

import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.model.PagamentoPortale.STATO;
import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.model.IAutorizzato;
import it.govpay.orm.PagamentoPortale;

public class ListaPagamentiPortaleDTO extends BasicFindRequestDTO{
	
	public ListaPagamentiPortaleDTO(IAutorizzato user) {
		super(user);
		this.addSortField("dataRichiestaPagamento", PagamentoPortale.model().DATA_RICHIESTA);
		this.addSortField("stato", PagamentoPortale.model().STATO);
		this.setDefaultSort(PagamentoPortale.model().DATA_RICHIESTA,SortOrder.DESC);
	}
	private Date dataA;
	private Date dataDa;
	private String stato;
	private String versante;
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
	public String getStato() {
		return stato;
	}
	public void setStato(String stato) {
		this.stato = stato;
	}
	public String getVersante() {
		return versante;
	}
	public void setVersante(String versante) {
		this.versante = versante;
	}
}
