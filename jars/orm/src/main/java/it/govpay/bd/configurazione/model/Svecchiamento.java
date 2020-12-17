package it.govpay.bd.configurazione.model;

import java.io.Serializable;

public class Svecchiamento implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer stampeAvvisi = null;
	private Integer stampeRicevute = null;
	
	public Integer getStampeAvvisi() {
		return stampeAvvisi;
	}
	public void setStampeAvvisi(Integer stampeAvvisi) {
		this.stampeAvvisi = stampeAvvisi;
	}
	public Integer getStampeRicevute() {
		return stampeRicevute;
	}
	public void setStampeRicevute(Integer stampeRicevute) {
		this.stampeRicevute = stampeRicevute;
	}

	
}
