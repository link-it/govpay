package it.govpay.model;

public class TipoVersamentoDominio extends TipoVersamento {
	private static final long serialVersionUID = 1L;
	
	private long idTipoVersamento;
	private long idDominio;
	private String codificaIuvCustom;
	private Tipo tipoCustom;
	private Boolean pagaTerziCustom;
	private Boolean abilitatoCustom;
	private String jsonSchemaCustom;
	private String datiAllegatiCustom;
	
	public String getJsonSchemaCustom() {
		return jsonSchemaCustom;
	}
	public void setJsonSchemaCustom(String jsonSchemaCustom) {
		this.jsonSchemaCustom = jsonSchemaCustom;
	}
	public String getDatiAllegatiCustom() {
		return datiAllegatiCustom;
	}
	public void setDatiAllegatiCustom(String datiAllegatiCustom) {
		this.datiAllegatiCustom = datiAllegatiCustom;
	}
	public long getIdTipoVersamento() {
		return idTipoVersamento;
	}
	public void setIdTipoVersamento(long idTipoVersamento) {
		this.idTipoVersamento = idTipoVersamento;
	}
	public long getIdDominio() {
		return idDominio;
	}
	public void setIdDominio(long idDominio) {
		this.idDominio = idDominio;
	}
	public String getCodificaIuvCustom() {
		return codificaIuvCustom;
	}
	public void setCodificaIuvCustom(String codificaIuvCustom) {
		this.codificaIuvCustom = codificaIuvCustom;
	}
	public Tipo getTipoCustom() {
		return tipoCustom;
	}
	public void setTipoCustom(Tipo tipoCustom) {
		this.tipoCustom = tipoCustom;
	}
	public Boolean getPagaTerziCustom() {
		return pagaTerziCustom;
	}
	public void setPagaTerziCustom(Boolean pagaTerziCustom) {
		this.pagaTerziCustom = pagaTerziCustom;
	}
	public Boolean getAbilitatoCustom() {
		return this.abilitatoCustom;
	}
	public void setAbilitatoCustom(Boolean abilitatoCustom) {
		this.abilitatoCustom = abilitatoCustom;
	}
}
