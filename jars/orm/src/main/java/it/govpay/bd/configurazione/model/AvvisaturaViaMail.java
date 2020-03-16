package it.govpay.bd.configurazione.model;

import java.io.Serializable;

public class AvvisaturaViaMail implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private PromemoriaAvviso promemoriaAvviso;
	private PromemoriaRicevuta promemoriaRicevuta;
	private PromemoriaScadenza promemoriaScadenza;
	
	public PromemoriaAvviso getPromemoriaAvviso() {
		return promemoriaAvviso;
	}
	public void setPromemoriaAvviso(PromemoriaAvviso promemoriaAvviso) {
		this.promemoriaAvviso = promemoriaAvviso;
	}
	public PromemoriaRicevuta getPromemoriaRicevuta() {
		return promemoriaRicevuta;
	}
	public void setPromemoriaRicevuta(PromemoriaRicevuta promemoriaRicevuta) {
		this.promemoriaRicevuta = promemoriaRicevuta;
	}
	public PromemoriaScadenza getPromemoriaScadenza() {
		return promemoriaScadenza;
	}
	public void setPromemoriaScadenza(PromemoriaScadenza promemoriaScadenza) {
		this.promemoriaScadenza = promemoriaScadenza;
	}
}
