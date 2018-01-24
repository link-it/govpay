package it.govpay.pagamento.api.rs.v1.model;

import java.util.List;

public class PagamentoPortale {
	
	private String id= null;
	private String idPsp = null;
	private String nomePsp = null;
	private String idCanale = null;
	private String nomeCanale = null;
	private String tipoVersamento = null;
	private String modelloPagamento = null;
	private String stato = null;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	public String getModelloPagamento() {
		return modelloPagamento;
	}
	public void setModelloPagamento(String modelloPagamento) {
		this.modelloPagamento = modelloPagamento;
	}
	public String getStato() {
		return stato;
	}
	public void setStato(String stato) {
		this.stato = stato;
	}
	
	// [TODO]
	private List<Rpt> rpts = null;

}
