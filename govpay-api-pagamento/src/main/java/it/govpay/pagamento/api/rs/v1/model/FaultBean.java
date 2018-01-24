package it.govpay.pagamento.api.rs.pagamenti.v1.model;

public class FaultBean {

	public enum CATEGORIA {
		AUTORIZZAZIONE, RICHIESTA, OPERAZIONE, PAGOPA,INTERNO
	};
	
	private String categoria;
	private String codice;
	private String descrizione;
	private String dettaglio;
	
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(CATEGORIA categoria) {
		this.categoria = categoria.name();
	}
	public String getCodice() {
		return codice;
	}
	public void setCodice(String codice) {
		this.codice = codice;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getDettaglio() {
		return dettaglio;
	}
	public void setDettaglio(String dettaglio) {
		this.dettaglio = dettaglio;
	}
}
