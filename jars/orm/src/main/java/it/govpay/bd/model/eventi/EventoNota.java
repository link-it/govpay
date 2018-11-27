package it.govpay.bd.model.eventi;

public class EventoNota extends EventoGenerico {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String UTENTE_SISTEMA = "SISTEMA";

	public enum TipoNota {
		NotaUtente, SistemaFatal, SistemaWarning, SistemaInfo, Anomalia
	}


	private String principal = null;
	private String autore = null;
	private String testo = null;
	private String oggetto = null;

	public EventoNota() {
		super();
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getAutore() {
		return autore;
	}

	public void setAutore(String autore) {
		this.autore = autore;
	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public void setTipoEvento(TipoNota tipo) {
		if(tipo != null)
		super.setTipoEvento(tipo.name());
	}

	public String getOggetto() {
		return oggetto;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}
}
