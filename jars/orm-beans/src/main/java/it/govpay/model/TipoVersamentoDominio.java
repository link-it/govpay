package it.govpay.model;

public class TipoVersamentoDominio extends TipoVersamento {
	private static final long serialVersionUID = 1L;
	
	private long idTipoVersamento;
	private long idDominio;
	private String codificaIuvCustom;
	private Tipo tipoCustom;
	private Boolean pagaTerziCustom;
	private Boolean abilitatoCustom;
	private String formTipoCustom;
	private String formDefinizioneCustom;
	private String validazioneDefinizioneCustom;
	private String trasformazioneTipoCustom;
	private String trasformazioneDefinizioneCustom;
	private String codApplicazioneCustom;
	private Boolean promemoriaAvvisoCustom;
	private String promemoriaOggettoCustom;
	private String promemoriaMessaggioCustom;
	
	public String getFormTipoCustom() {
		return formTipoCustom;
	}
	public void setFormTipoCustom(String formTipoCustom) {
		this.formTipoCustom = formTipoCustom;
	}
	public String getFormDefinizioneCustom() {
		return formDefinizioneCustom;
	}
	public void setFormDefinizioneCustom(String formDefinizioneCustom) {
		this.formDefinizioneCustom = formDefinizioneCustom;
	}
	public String getValidazioneDefinizioneCustom() {
		return validazioneDefinizioneCustom;
	}
	public void setValidazioneDefinizioneCustom(String validazioneDefinizioneCustom) {
		this.validazioneDefinizioneCustom = validazioneDefinizioneCustom;
	}
	public String getTrasformazioneTipoCustom() {
		return trasformazioneTipoCustom;
	}
	public void setTrasformazioneTipoCustom(String trasformazioneTipoCustom) {
		this.trasformazioneTipoCustom = trasformazioneTipoCustom;
	}
	public String getTrasformazioneDefinizioneCustom() {
		return trasformazioneDefinizioneCustom;
	}
	public void setTrasformazioneDefinizioneCustom(String trasformazioneDefinizioneCustom) {
		this.trasformazioneDefinizioneCustom = trasformazioneDefinizioneCustom;
	}
	public String getCodApplicazioneCustom() {
		return codApplicazioneCustom;
	}
	public void setCodApplicazioneCustom(String codApplicazioneCustom) {
		this.codApplicazioneCustom = codApplicazioneCustom;
	}
	public Boolean getPromemoriaAvvisoCustom() {
		return promemoriaAvvisoCustom;
	}
	public void setPromemoriaAvvisoCustom(Boolean promemoriaAvvisoCustom) {
		this.promemoriaAvvisoCustom = promemoriaAvvisoCustom;
	}
	public String getPromemoriaOggettoCustom() {
		return promemoriaOggettoCustom;
	}
	public void setPromemoriaOggettoCustom(String promemoriaOggettoCustom) {
		this.promemoriaOggettoCustom = promemoriaOggettoCustom;
	}
	public String getPromemoriaMessaggioCustom() {
		return promemoriaMessaggioCustom;
	}
	public void setPromemoriaMessaggioCustom(String promemoriaMessaggioCustom) {
		this.promemoriaMessaggioCustom = promemoriaMessaggioCustom;
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
