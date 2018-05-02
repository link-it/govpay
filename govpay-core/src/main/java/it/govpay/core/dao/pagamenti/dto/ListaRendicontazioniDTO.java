package it.govpay.core.dao.pagamenti.dto;

import java.util.Date;

import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.model.IAutorizzato;
import it.govpay.orm.FR;

public class ListaRendicontazioniDTO extends BasicFindRequestDTO{
	
	public ListaRendicontazioniDTO(IAutorizzato user) {
		super(user);
		this.addSortField("data", FR.model().DATA_ACQUISIZIONE);
		this.setDefaultSort(FR.model().DATA_ACQUISIZIONE,SortOrder.DESC);
	}
	private String idDominio;
	private Date dataDa;
	private Date dataA;

	public String getIdDominio() {
		return idDominio;
	}
	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}
	public Date getDataDa() {
		return dataDa;
	}
	public void setDataDa(Date dataDa) {
		this.dataDa = dataDa;
	}
	public Date getDataA() {
		return dataA;
	}
	public void setDataA(Date dataA) {
		this.dataA = dataA;
	}
	
}
