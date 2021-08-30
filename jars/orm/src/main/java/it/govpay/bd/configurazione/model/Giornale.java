package it.govpay.bd.configurazione.model;

import java.io.Serializable;

public class Giornale implements Serializable{

	private static final long serialVersionUID = 1L;

	private GdeInterfaccia apiEnte;
	private GdeInterfaccia apiPagamento;
	private GdeInterfaccia apiRagioneria;
	private GdeInterfaccia apiBackoffice;
	private GdeInterfaccia apiPagoPA;
	private GdeInterfaccia apiPendenze;
	private GdeInterfaccia apiBackendIO;
	private GdeInterfaccia apiMaggioliJPPA;
	
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
	public GdeInterfaccia getApiPendenze() {
		return apiPendenze;
	}
	public void setApiPendenze(GdeInterfaccia apiPendenze) {
		this.apiPendenze = apiPendenze;
	}
	public GdeInterfaccia getApiBackendIO() {
		return apiBackendIO;
	}
	public void setApiBackendIO(GdeInterfaccia apiBackendIO) {
		this.apiBackendIO = apiBackendIO;
	}
	public GdeInterfaccia getApiMaggioliJPPA() {
		return apiMaggioliJPPA;
	}
	public void setApiMaggioliJPPA(GdeInterfaccia apiMaggioliJPPA) {
		this.apiMaggioliJPPA = apiMaggioliJPPA;
	}

}
