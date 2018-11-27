package it.govpay.bd.model.eventi;

import java.io.Serializable;
import java.util.Date;

import it.govpay.model.Evento.CategoriaEvento;

public class EventoGenerico implements Serializable{

	private static final long serialVersionUID = 1L;

	private Date data;
	private String codDominio;
	private String iuv;
	private String ccp;
	private CategoriaEvento categoriaEvento;
	private String tipoEvento;
	private String sottotipoEvento;
	private String esito;
	private String descrizioneEsito;
	private Long idVersamento;
	private Long idPagamentoPortale;

	public EventoGenerico() {
		this.categoriaEvento = CategoriaEvento.INTERNO;
		this.data = new Date();
	}

	public Date getData() {
		return this.data;
	}
	public void setData(Date data_richiesta) {
		this.data = data_richiesta;
	}
	public String getCodDominio() {
		return this.codDominio;
	}
	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}
	public String getIuv() {
		return this.iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public String getCcp() {
		return this.ccp;
	}
	public void setCcp(String ccp) {
		this.ccp = ccp;
	}
	public CategoriaEvento getCategoriaEvento() {
		return this.categoriaEvento;
	}
	public void setCategoriaEvento(CategoriaEvento categoriaEvento) {
		this.categoriaEvento = categoriaEvento;
	}
	public String getTipoEvento() {
		return this.tipoEvento;
	}
	public void setTipoEvento(String tipoEvento) {
		this.tipoEvento = tipoEvento;
	}
	public String getSottotipoEvento() {
		return this.sottotipoEvento;
	}
	public void setSottotipoEvento(String sottotipoEvento) {
		this.sottotipoEvento = sottotipoEvento;
	}
	public String getEsito() {
		return this.esito;
	}
	public void setEsito(String esito) {
		this.esito = esito;
	}
	public String getDescrizioneEsito() {
		return this.descrizioneEsito;
	}
	public void setDescrizioneEsito(String descrizioneEsito) {
		this.descrizioneEsito = descrizioneEsito;
	}
	public Long getIdVersamento() {
		return idVersamento;
	}
	public void setIdVersamento(Long idVersamento) {
		this.idVersamento = idVersamento;
	}
	public Long getIdPagamentoPortale() {
		return idPagamentoPortale;
	}
	public void setIdPagamentoPortale(Long idPagamentoPortale) {
		this.idPagamentoPortale = idPagamentoPortale;
	}
}
