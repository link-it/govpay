package it.govpay.pagamento.api.rs.v1.model;

public class Canale {

	private String idPsp = null;
	private String nomePsp = null;
	private String idCanale = null;
	private String nomeCanale = null;
	private String tipoVersamento = null;
	public String getIdPsp() {
		return idPsp;
	}
	public void setIdPsp(String idPsp) {
		this.idPsp = idPsp;
	}
	public String getNomePsp() {
		return nomePsp;
	}
	public void setNomePsp(String nomePsp) {
		this.nomePsp = nomePsp;
	}
	public String getIdCanale() {
		return idCanale;
	}
	public void setIdCanale(String idCanale) {
		this.idCanale = idCanale;
	}
	public String getNomeCanale() {
		return nomeCanale;
	}
	public void setNomeCanale(String nomeCanale) {
		this.nomeCanale = nomeCanale;
	}
	public String getTipoVersamento() {
		return tipoVersamento;
	}
	public void setTipoVersamento(String tipoVersamento) {
		this.tipoVersamento = tipoVersamento;
	}
	
}
