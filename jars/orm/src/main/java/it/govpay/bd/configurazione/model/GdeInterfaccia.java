package it.govpay.bd.configurazione.model;

import java.io.Serializable;

public class GdeInterfaccia implements Serializable{

	private static final long serialVersionUID = 1L;

	private GdeEvento letture;
	
	private GdeEvento scritture;

	public GdeEvento getLetture() {
		return letture;
	}

	public void setLetture(GdeEvento letture) {
		this.letture = letture;
	}

	public GdeEvento getScritture() {
		return scritture;
	}

	public void setScritture(GdeEvento scritture) {
		this.scritture = scritture;
	}
	
}
