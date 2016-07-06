package it.govpay.web.rs.model;

import java.util.Date;

import it.govpay.bd.model.BasicModel;

public class EstrattoContoRequest extends BasicModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EstrattoContoRequest(){super();}
	
	
	private String codiceCreditore;
	private Date dataInizio;
	private Date dataFine;
	private Integer pagina;
	
	public String getCodiceCreditore() {
		return codiceCreditore;
	}
	public void setCodiceCreditore(String codiceCreditore) {
		this.codiceCreditore = codiceCreditore;
	}
	public Date getDataInizio() {
		return dataInizio;
	}
	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}
	public Date getDataFine() {
		return dataFine;
	}
	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}
	public Integer getPagina() {
		return pagina;
	}
	public void setPagina(Integer pagina) {
		this.pagina = pagina;
	}
	
	

}
