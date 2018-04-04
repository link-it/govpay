package it.govpay.core.dao.pagamenti.dto;

import java.util.Date;

import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.model.IAutorizzato;
import it.govpay.orm.FR;

public class ListaRendicontazioniDTO extends BasicFindRequestDTO{
	
	public ListaRendicontazioniDTO(IAutorizzato user) {
		super(user);
		this.addSortField("data", FR.model().DATA_ACQUISIZIONE);
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
