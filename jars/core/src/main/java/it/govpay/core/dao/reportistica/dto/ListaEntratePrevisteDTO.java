package it.govpay.core.dao.reportistica.dto;

import java.util.Date;

import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.model.IAutorizzato;
import it.govpay.orm.VistaRiscossioni;

public class ListaEntratePrevisteDTO extends BasicFindRequestDTO{
	
	public enum FormatoRichiesto {JSON,PDF}
	
	
	public ListaEntratePrevisteDTO(IAutorizzato user) {
		super(user);
		this.addSortField("data", VistaRiscossioni.model().DATA);
		this.addDefaultSort(VistaRiscossioni.model().DATA,SortOrder.ASC);
	}
	private Date dataA;
	private Date dataDa;
	private String idDominio;
	private String idA2A;
	private FormatoRichiesto formato;
	
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
	public String getIdDominio() {
		return this.idDominio;
	}
	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}
	public String getIdA2A() {
		return this.idA2A;
	}
	public void setIdA2A(String idA2A) {
		this.idA2A = idA2A;
	}
	public FormatoRichiesto getFormato() {
		return formato;
	}
	public void setFormato(FormatoRichiesto formato) {
		this.formato = formato;
	}
	
}
