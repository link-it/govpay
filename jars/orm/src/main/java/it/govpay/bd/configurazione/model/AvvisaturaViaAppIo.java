package it.govpay.bd.configurazione.model;

import java.io.Serializable;

public class AvvisaturaViaAppIo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private PromemoriaAvvisoBase promemoriaAvviso;
	private PromemoriaRicevutaBase promemoriaRicevuta;
	private PromemoriaScadenza promemoriaScadenza;
	
	public PromemoriaAvvisoBase getPromemoriaAvviso() {
		return promemoriaAvviso;
	}
	public void setPromemoriaAvviso(PromemoriaAvvisoBase promemoriaAvviso) {
		this.promemoriaAvviso = promemoriaAvviso;
	}
	public PromemoriaRicevutaBase getPromemoriaRicevuta() {
		return promemoriaRicevuta;
	}
	public void setPromemoriaRicevuta(PromemoriaRicevutaBase promemoriaRicevuta) {
		this.promemoriaRicevuta = promemoriaRicevuta;
	}
	public PromemoriaScadenza getPromemoriaScadenza() {
		return promemoriaScadenza;
	}
	public void setPromemoriaScadenza(PromemoriaScadenza promemoriaScadenza) {
		this.promemoriaScadenza = promemoriaScadenza;
	}
}
