package it.govpay.core.dao.pagamenti.dto;

import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.expression.SortOrder;
import org.springframework.security.core.Authentication;

import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.orm.VistaRendicontazione;

public class ListaRendicontazioniDTO extends BasicFindRequestDTO{
	
	public ListaRendicontazioniDTO(Authentication user) {
		super(user);
		this.addSortField("data", VistaRendicontazione.model().RND_DATA);
		this.addDefaultSort(VistaRendicontazione.model().RND_DATA,SortOrder.DESC);
	}
	
	private Date dataFlussoDa;
	private Date dataFlussoA;
	private Date dataRendicontazioneDa;
	private Date dataRendicontazioneA;
	private String codFlusso;
	private String iuv;
	private List<String> direzione;
	private List<String> divisione;
	private Boolean frObsoleto;
	private boolean ricercaIdFlussoCaseInsensitive = false;
	
	public Date getDataFlussoDa() {
		return dataFlussoDa;
	}
	public void setDataFlussoDa(Date dataFlussoDa) {
		this.dataFlussoDa = dataFlussoDa;
	}
	public Date getDataFlussoA() {
		return dataFlussoA;
	}
	public void setDataFlussoA(Date dataFlussoA) {
		this.dataFlussoA = dataFlussoA;
	}
	public Date getDataRendicontazioneDa() {
		return dataRendicontazioneDa;
	}
	public void setDataRendicontazioneDa(Date dataRendicontazioneDa) {
		this.dataRendicontazioneDa = dataRendicontazioneDa;
	}
	public Date getDataRendicontazioneA() {
		return dataRendicontazioneA;
	}
	public void setDataRendicontazioneA(Date dataRendicontazioneA) {
		this.dataRendicontazioneA = dataRendicontazioneA;
	}
	public String getCodFlusso() {
		return codFlusso;
	}
	public void setCodFlusso(String codFlusso) {
		this.codFlusso = codFlusso;
	}
	public String getIuv() {
		return iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public List<String> getDirezione() {
		return direzione;
	}
	public void setDirezione(List<String> direzione) {
		this.direzione = direzione;
	}
	public List<String> getDivisione() {
		return divisione;
	}
	public void setDivisione(List<String> divisione) {
		this.divisione = divisione;
	}
	public Boolean getFrObsoleto() {
		return frObsoleto;
	}
	public void setFrObsoleto(Boolean frObsoleto) {
		this.frObsoleto = frObsoleto;
	}
	public boolean isRicercaIdFlussoCaseInsensitive() {
		return ricercaIdFlussoCaseInsensitive;
	}
	public void setRicercaIdFlussoCaseInsensitive(boolean ricercaIdFlussoCaseInsensitive) {
		this.ricercaIdFlussoCaseInsensitive = ricercaIdFlussoCaseInsensitive;
	}
	
}
