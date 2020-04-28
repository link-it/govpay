package it.govpay.bd.reportistica.statistiche.model;

import it.govpay.bd.model.Fr;

public class StatisticaRendicontazione extends it.govpay.model.reportistica.statistiche.StatisticaRendicontazione {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Fr flusso;
	
	public StatisticaRendicontazione() {
		super();
	}
	
	public Fr getFlusso() {
		return flusso;
	}
	public void setFlusso(Fr flusso) {
		this.flusso = flusso;
	}
	
	
}
