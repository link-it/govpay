package it.govpay.model.configurazione;

public class PromemoriaAvviso extends PromemoriaAvvisoBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private boolean allegaPdf;

	public boolean isAllegaPdf() {
		return allegaPdf;
	}

	public void setAllegaPdf(boolean allegaPdf) {
		this.allegaPdf = allegaPdf;
	}
}
