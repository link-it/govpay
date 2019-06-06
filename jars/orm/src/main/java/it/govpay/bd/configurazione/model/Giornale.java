package it.govpay.bd.configurazione.model;

import java.io.Serializable;

public class Giornale implements Serializable{

	private static final long serialVersionUID = 1L;

	private GdeInterfaccia apiEnte;
	private GdeInterfaccia apiPagamento;
	private GdeInterfaccia apiRagioneria;
	private GdeInterfaccia apiBackoffice;
	private GdeInterfaccia apiPagoPA;
	
	public GdeInterfaccia getApiEnte() {
		return apiEnte;
	}
	public void setApiEnte(GdeInterfaccia apiEnte) {
		this.apiEnte = apiEnte;
	}
	public GdeInterfaccia getApiPagamento() {
		return apiPagamento;
	}
	public void setApiPagamento(GdeInterfaccia apiPagamento) {
		this.apiPagamento = apiPagamento;
	}
	public GdeInterfaccia getApiRagioneria() {
		return apiRagioneria;
	}
	public void setApiRagioneria(GdeInterfaccia apiRagioneria) {
		this.apiRagioneria = apiRagioneria;
	}
	public GdeInterfaccia getApiBackoffice() {
		return apiBackoffice;
	}
	public void setApiBackoffice(GdeInterfaccia apiBackoffice) {
		this.apiBackoffice = apiBackoffice;
	}
	public GdeInterfaccia getApiPagoPA() {
		return apiPagoPA;
	}
	public void setApiPagoPA(GdeInterfaccia apiPagoPA) {
		this.apiPagoPA = apiPagoPA;
	}

}
