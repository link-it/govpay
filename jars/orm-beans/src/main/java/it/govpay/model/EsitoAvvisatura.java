package it.govpay.model;

import java.util.Date;

public class EsitoAvvisatura extends BasicModel {

	private static final long serialVersionUID = 1L;
	
	private String codDominio;
	public String getCodDominio() {
		return this.codDominio;
	}
	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}
	public String getIdentificativoAvvisatura() {
		return this.identificativoAvvisatura;
	}
	public void setIdentificativoAvvisatura(String identificativoAvvisatura) {
		this.identificativoAvvisatura = identificativoAvvisatura;
	}
	public Integer getTipoCanale() {
		return this.tipoCanale;
	}
	public void setTipoCanale(Integer tipoCanale) {
		this.tipoCanale = tipoCanale;
	}
	public String getCodCanale() {
		return this.codCanale;
	}
	public void setCodCanale(String codCanale) {
		this.codCanale = codCanale;
	}
	public Date getData() {
		return this.data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public Integer getCodEsito() {
		return this.codEsito;
	}
	public void setCodEsito(Integer codEsito) {
		this.codEsito = codEsito;
	}
	public String getDescrizioneEsito() {
		return this.descrizioneEsito;
	}
	public void setDescrizioneEsito(String descrizioneEsito) {
		this.descrizioneEsito = descrizioneEsito;
	}
	public long getIdTracciato() {
		return this.idTracciato;
	}
	public void setIdTracciato(long idTracciato) {
		this.idTracciato = idTracciato;
	}
	@Override
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	private String identificativoAvvisatura;
	private Integer tipoCanale;
	private String codCanale;
	private Date data;
	private Integer codEsito;
	private String descrizioneEsito;
	private long idTracciato;
	private Long id;

}
