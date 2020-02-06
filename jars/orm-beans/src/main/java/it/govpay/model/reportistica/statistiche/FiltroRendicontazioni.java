package it.govpay.model.reportistica.statistiche;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class FiltroRendicontazioni implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Date dataFlussoDa;
	private Date dataFlussoA;
	private Date dataRendicontazioneDa;
	private Date dataRendicontazioneA;
	private String codFlusso;
	private String iuv;
	private List<String> direzione;
	private List<String> divisione;
	
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
}