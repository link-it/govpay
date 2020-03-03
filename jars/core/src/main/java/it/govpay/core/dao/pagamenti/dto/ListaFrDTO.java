package it.govpay.core.dao.pagamenti.dto;

import java.util.Date;

import org.openspcoop2.generic_project.expression.SortOrder;
import org.springframework.security.core.Authentication;

import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.model.Fr;
import it.govpay.orm.FR;

public class ListaFrDTO extends BasicFindRequestDTO{
	
	public ListaFrDTO(Authentication user) {
		super(user);
		this.addSortField("data", FR.model().DATA_ACQUISIZIONE);
		this.addSortField("idFlusso", FR.model().COD_FLUSSO);
		this.addDefaultSort(FR.model().DATA_ACQUISIZIONE,SortOrder.DESC);
	}
	private String idDominio;
	private Date dataDa;
	private Date dataA;
	private Boolean incassato = null;
	private String idFlusso;
	private Fr.StatoFr stato;

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
	public Boolean getIncassato() {
		return incassato;
	}
	public void setIncassato(Boolean incassato) {
		this.incassato = incassato;
	}
	public String getIdFlusso() {
		return idFlusso;
	}
	public void setIdFlusso(String idFlusso) {
		this.idFlusso = idFlusso;
	}
	public Fr.StatoFr getStato() {
		return stato;
	}
	public void setStato(Fr.StatoFr stato) {
		this.stato = stato;
	}
	
}
