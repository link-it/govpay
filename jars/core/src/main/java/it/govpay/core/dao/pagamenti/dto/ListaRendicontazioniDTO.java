package it.govpay.core.dao.pagamenti.dto;

import java.util.Date;

import org.openspcoop2.generic_project.expression.SortOrder;
import org.springframework.security.core.Authentication;

import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.orm.FR;

public class ListaRendicontazioniDTO extends BasicFindRequestDTO{
	
	public ListaRendicontazioniDTO(Authentication user) {
		super(user);
		this.addSortField("data", FR.model().DATA_ACQUISIZIONE);
		this.addDefaultSort(FR.model().DATA_ACQUISIZIONE,SortOrder.DESC);
	}
	private String idDominio;
	private Date dataDa;
	private Date dataA;

	public String getIdDominio() {
		return this.idDominio;
	}
	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}
	public Date getDataDa() {
		return this.dataDa;
	}
	public void setDataDa(Date dataDa) {
		this.dataDa = dataDa;
	}
	public Date getDataA() {
		return this.dataA;
	}
	public void setDataA(Date dataA) {
		this.dataA = dataA;
	}
	
}
