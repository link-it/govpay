package it.govpay.model;

public class TipoVersamentoDominio extends TipoVersamento {
	private static final long serialVersionUID = 1L;
	
	private long idTipoVersamento;
	private long idDominio;
	private String codificaIuvCustom;
	private Tipo tipoCustom;
	private Boolean pagaTerziCustom;
	private Boolean abilitatoCustom;

	
	/* Configurazione Caricamento pendenza da portale backoffice*/
	
	private String caricamentoPendenzePortaleBackofficeFormTipoCustom;
	private String caricamentoPendenzePortaleBackofficeFormDefinizioneCustom;
	private String caricamentoPendenzePortaleBackofficeValidazioneDefinizioneCustom;
	private String caricamentoPendenzePortaleBackofficeTrasformazioneTipoCustom;
	private String caricamentoPendenzePortaleBackofficeTrasformazioneDefinizioneCustom;
	private String caricamentoPendenzePortaleBackofficeCodApplicazioneCustom;
	private Boolean caricamentoPendenzePortaleBackofficeAbilitatoCustom;
	
	/* Configurazione Caricamento pendenza da portale pagamento cittadino */
	
	private String caricamentoPendenzePortalePagamentoFormTipoCustom;
	private String caricamentoPendenzePortalePagamentoFormDefinizioneCustom;
	private String caricamentoPendenzePortalePagamentoValidazioneDefinizioneCustom;
	private String caricamentoPendenzePortalePagamentoTrasformazioneTipoCustom;
	private String caricamentoPendenzePortalePagamentoTrasformazioneDefinizioneCustom;
	private String caricamentoPendenzePortalePagamentoCodApplicazioneCustom;
	private Boolean caricamentoPendenzePortalePagamentoAbilitatoCustom;
	
	/* Avvisatura Via Mail */
	
	private Boolean avvisaturaMailPromemoriaAvvisoAbilitatoCustom;
	private String avvisaturaMailPromemoriaAvvisoTipoCustom;
	private Boolean avvisaturaMailPromemoriaAvvisoPdfCustom;
	private String avvisaturaMailPromemoriaAvvisoOggettoCustom;
	private String avvisaturaMailPromemoriaAvvisoMessaggioCustom;
	
	private Boolean avvisaturaMailPromemoriaRicevutaAbilitatoCustom;
	private String avvisaturaMailPromemoriaRicevutaTipoCustom;
	private Boolean avvisaturaMailPromemoriaRicevutaPdfCustom;
	private String avvisaturaMailPromemoriaRicevutaOggettoCustom;
	private String avvisaturaMailPromemoriaRicevutaMessaggioCustom;
	private Boolean avvisaturaMailPromemoriaRicevutaInviaSoloEseguitiCustom;
	
	private Boolean avvisaturaMailPromemoriaScadenzaAbilitatoCustom;
	private String avvisaturaMailPromemoriaScadenzaTipoCustom;
	private Integer avvisaturaMailPromemoriaScadenzaPreavvisoCustom;
	private String avvisaturaMailPromemoriaScadenzaOggettoCustom;
	private String avvisaturaMailPromemoriaScadenzaMessaggioCustom;
	
	/* Visualizzazione Custom dettaglio pendenza */
	
	private String visualizzazioneDefinizioneCustom;
	
	/* Configurazione conversione pendenza  caricata con i tracciati CSV */
	
	private String tracciatoCsvTipoCustom;
	private String tracciatoCsvIntestazioneCustom;
	private String tracciatoCsvRichiestaCustom;
	private String tracciatoCsvRispostaCustom;
	
	/* Avvisatura Via AppIO */
	
	private String appIOAPIKey;
	
	private Boolean avvisaturaAppIoPromemoriaAvvisoAbilitatoCustom;
	private String avvisaturaAppIoPromemoriaAvvisoTipoCustom;
	private String avvisaturaAppIoPromemoriaAvvisoOggettoCustom;
	private String avvisaturaAppIoPromemoriaAvvisoMessaggioCustom;
	
	private Boolean avvisaturaAppIoPromemoriaRicevutaAbilitatoCustom;
	private String avvisaturaAppIoPromemoriaRicevutaTipoCustom;
	private String avvisaturaAppIoPromemoriaRicevutaOggettoCustom;
	private String avvisaturaAppIoPromemoriaRicevutaMessaggioCustom;
	private Boolean avvisaturaAppIoPromemoriaRicevutaInviaSoloEseguitiCustom;
	
	private Boolean avvisaturaAppIoPromemoriaScadenzaAbilitatoCustom;
	private String avvisaturaAppIoPromemoriaScadenzaTipoCustom;
	private Integer avvisaturaAppIoPromemoriaScadenzaPreavvisoCustom;
	private String avvisaturaAppIoPromemoriaScadenzaOggettoCustom;
	private String avvisaturaAppIoPromemoriaScadenzaMessaggioCustom;
	
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
		return abilitatoCustom;
	}
	public void setAbilitatoCustom(Boolean abilitatoCustom) {
		this.abilitatoCustom = abilitatoCustom;
	}
	public String getCaricamentoPendenzePortaleBackofficeFormTipoCustom() {
		return caricamentoPendenzePortaleBackofficeFormTipoCustom;
	}
	public void setCaricamentoPendenzePortaleBackofficeFormTipoCustom(String caricamentoPendenzePortaleBackofficeFormTipoCustom) {
		this.caricamentoPendenzePortaleBackofficeFormTipoCustom = caricamentoPendenzePortaleBackofficeFormTipoCustom;
	}
	public String getCaricamentoPendenzePortaleBackofficeFormDefinizioneCustom() {
		return caricamentoPendenzePortaleBackofficeFormDefinizioneCustom;
	}
	public void setCaricamentoPendenzePortaleBackofficeFormDefinizioneCustom(String caricamentoPendenzePortaleBackofficeFormDefinizioneCustom) {
		this.caricamentoPendenzePortaleBackofficeFormDefinizioneCustom = caricamentoPendenzePortaleBackofficeFormDefinizioneCustom;
	}
	public String getCaricamentoPendenzePortaleBackofficeValidazioneDefinizioneCustom() {
		return caricamentoPendenzePortaleBackofficeValidazioneDefinizioneCustom;
	}
	public void setCaricamentoPendenzePortaleBackofficeValidazioneDefinizioneCustom(String caricamentoPendenzePortaleBackofficeValidazioneDefinizioneCustom) {
		this.caricamentoPendenzePortaleBackofficeValidazioneDefinizioneCustom = caricamentoPendenzePortaleBackofficeValidazioneDefinizioneCustom;
	}
	public String getCaricamentoPendenzePortaleBackofficeTrasformazioneTipoCustom() {
		return caricamentoPendenzePortaleBackofficeTrasformazioneTipoCustom;
	}
	public void setCaricamentoPendenzePortaleBackofficeTrasformazioneTipoCustom(String caricamentoPendenzePortaleBackofficeTrasformazioneTipoCustom) {
		this.caricamentoPendenzePortaleBackofficeTrasformazioneTipoCustom = caricamentoPendenzePortaleBackofficeTrasformazioneTipoCustom;
	}
	public String getCaricamentoPendenzePortaleBackofficeTrasformazioneDefinizioneCustom() {
		return caricamentoPendenzePortaleBackofficeTrasformazioneDefinizioneCustom;
	}
	public void setCaricamentoPendenzePortaleBackofficeTrasformazioneDefinizioneCustom(String caricamentoPendenzePortaleBackofficeTrasformazioneDefinizioneCustom) {
		this.caricamentoPendenzePortaleBackofficeTrasformazioneDefinizioneCustom = caricamentoPendenzePortaleBackofficeTrasformazioneDefinizioneCustom;
	}
	public String getCaricamentoPendenzePortaleBackofficeCodApplicazioneCustom() {
		return caricamentoPendenzePortaleBackofficeCodApplicazioneCustom;
	}
	public void setCaricamentoPendenzePortaleBackofficeCodApplicazioneCustom(String caricamentoPendenzePortaleBackofficeCodApplicazioneCustom) {
		this.caricamentoPendenzePortaleBackofficeCodApplicazioneCustom = caricamentoPendenzePortaleBackofficeCodApplicazioneCustom;
	}
	public Boolean getCaricamentoPendenzePortaleBackofficeAbilitatoCustom() {
		return caricamentoPendenzePortaleBackofficeAbilitatoCustom;
	}
	public void setCaricamentoPendenzePortaleBackofficeAbilitatoCustom(Boolean caricamentoPendenzePortaleBackofficeAbilitatoCustom) {
		this.caricamentoPendenzePortaleBackofficeAbilitatoCustom = caricamentoPendenzePortaleBackofficeAbilitatoCustom;
	}
	public String getCaricamentoPendenzePortalePagamentoFormTipoCustom() {
		return caricamentoPendenzePortalePagamentoFormTipoCustom;
	}
	public void setCaricamentoPendenzePortalePagamentoFormTipoCustom(String caricamentoPendenzePortalePagamentoFormTipoCustom) {
		this.caricamentoPendenzePortalePagamentoFormTipoCustom = caricamentoPendenzePortalePagamentoFormTipoCustom;
	}
	public String getCaricamentoPendenzePortalePagamentoFormDefinizioneCustom() {
		return caricamentoPendenzePortalePagamentoFormDefinizioneCustom;
	}
	public void setCaricamentoPendenzePortalePagamentoFormDefinizioneCustom(String caricamentoPendenzePortalePagamentoFormDefinizioneCustom) {
		this.caricamentoPendenzePortalePagamentoFormDefinizioneCustom = caricamentoPendenzePortalePagamentoFormDefinizioneCustom;
	}
	public String getCaricamentoPendenzePortalePagamentoValidazioneDefinizioneCustom() {
		return caricamentoPendenzePortalePagamentoValidazioneDefinizioneCustom;
	}
	public void setCaricamentoPendenzePortalePagamentoValidazioneDefinizioneCustom(String caricamentoPendenzePortalePagamentoValidazioneDefinizioneCustom) {
		this.caricamentoPendenzePortalePagamentoValidazioneDefinizioneCustom = caricamentoPendenzePortalePagamentoValidazioneDefinizioneCustom;
	}
	public String getCaricamentoPendenzePortalePagamentoTrasformazioneTipoCustom() {
		return caricamentoPendenzePortalePagamentoTrasformazioneTipoCustom;
	}
	public void setCaricamentoPendenzePortalePagamentoTrasformazioneTipoCustom(String caricamentoPendenzePortalePagamentoTrasformazioneTipoCustom) {
		this.caricamentoPendenzePortalePagamentoTrasformazioneTipoCustom = caricamentoPendenzePortalePagamentoTrasformazioneTipoCustom;
	}
	public String getCaricamentoPendenzePortalePagamentoTrasformazioneDefinizioneCustom() {
		return caricamentoPendenzePortalePagamentoTrasformazioneDefinizioneCustom;
	}
	public void setCaricamentoPendenzePortalePagamentoTrasformazioneDefinizioneCustom(String caricamentoPendenzePortalePagamentoTrasformazioneDefinizioneCustom) {
		this.caricamentoPendenzePortalePagamentoTrasformazioneDefinizioneCustom = caricamentoPendenzePortalePagamentoTrasformazioneDefinizioneCustom;
	}
	public String getCaricamentoPendenzePortalePagamentoCodApplicazioneCustom() {
		return caricamentoPendenzePortalePagamentoCodApplicazioneCustom;
	}
	public void setCaricamentoPendenzePortalePagamentoCodApplicazioneCustom(String caricamentoPendenzePortalePagamentoCodApplicazioneCustom) {
		this.caricamentoPendenzePortalePagamentoCodApplicazioneCustom = caricamentoPendenzePortalePagamentoCodApplicazioneCustom;
	}
	public Boolean getCaricamentoPendenzePortalePagamentoAbilitatoCustom() {
		return caricamentoPendenzePortalePagamentoAbilitatoCustom;
	}
	public void setCaricamentoPendenzePortalePagamentoAbilitatoCustom(Boolean caricamentoPendenzePortalePagamentoAbilitatoCustom) {
		this.caricamentoPendenzePortalePagamentoAbilitatoCustom = caricamentoPendenzePortalePagamentoAbilitatoCustom;
	}
	public Boolean getAvvisaturaMailPromemoriaAvvisoAbilitatoCustom() {
		return avvisaturaMailPromemoriaAvvisoAbilitatoCustom;
	}
	public void setAvvisaturaMailPromemoriaAvvisoAbilitatoCustom(Boolean avvisaturaMailPromemoriaAvvisoAbilitatoCustom) {
		this.avvisaturaMailPromemoriaAvvisoAbilitatoCustom = avvisaturaMailPromemoriaAvvisoAbilitatoCustom;
	}
	public String getAvvisaturaMailPromemoriaAvvisoTipoCustom() {
		return avvisaturaMailPromemoriaAvvisoTipoCustom;
	}
	public void setAvvisaturaMailPromemoriaAvvisoTipoCustom(String avvisaturaMailPromemoriaAvvisoTipoCustom) {
		this.avvisaturaMailPromemoriaAvvisoTipoCustom = avvisaturaMailPromemoriaAvvisoTipoCustom;
	}
	public Boolean getAvvisaturaMailPromemoriaAvvisoPdfCustom() {
		return avvisaturaMailPromemoriaAvvisoPdfCustom;
	}
	public void setAvvisaturaMailPromemoriaAvvisoPdfCustom(Boolean avvisaturaMailPromemoriaAvvisoPdfCustom) {
		this.avvisaturaMailPromemoriaAvvisoPdfCustom = avvisaturaMailPromemoriaAvvisoPdfCustom;
	}
	public String getAvvisaturaMailPromemoriaAvvisoOggettoCustom() {
		return avvisaturaMailPromemoriaAvvisoOggettoCustom;
	}
	public void setAvvisaturaMailPromemoriaAvvisoOggettoCustom(String avvisaturaMailPromemoriaAvvisoOggettoCustom) {
		this.avvisaturaMailPromemoriaAvvisoOggettoCustom = avvisaturaMailPromemoriaAvvisoOggettoCustom;
	}
	public String getAvvisaturaMailPromemoriaAvvisoMessaggioCustom() {
		return avvisaturaMailPromemoriaAvvisoMessaggioCustom;
	}
	public void setAvvisaturaMailPromemoriaAvvisoMessaggioCustom(String avvisaturaMailPromemoriaAvvisoMessaggioCustom) {
		this.avvisaturaMailPromemoriaAvvisoMessaggioCustom = avvisaturaMailPromemoriaAvvisoMessaggioCustom;
	}
	public Boolean getAvvisaturaMailPromemoriaRicevutaAbilitatoCustom() {
		return avvisaturaMailPromemoriaRicevutaAbilitatoCustom;
	}
	public void setAvvisaturaMailPromemoriaRicevutaAbilitatoCustom(Boolean avvisaturaMailPromemoriaRicevutaAbilitatoCustom) {
		this.avvisaturaMailPromemoriaRicevutaAbilitatoCustom = avvisaturaMailPromemoriaRicevutaAbilitatoCustom;
	}
	public String getAvvisaturaMailPromemoriaRicevutaTipoCustom() {
		return avvisaturaMailPromemoriaRicevutaTipoCustom;
	}
	public void setAvvisaturaMailPromemoriaRicevutaTipoCustom(String avvisaturaMailPromemoriaRicevutaTipoCustom) {
		this.avvisaturaMailPromemoriaRicevutaTipoCustom = avvisaturaMailPromemoriaRicevutaTipoCustom;
	}
	public Boolean getAvvisaturaMailPromemoriaRicevutaPdfCustom() {
		return avvisaturaMailPromemoriaRicevutaPdfCustom;
	}
	public void setAvvisaturaMailPromemoriaRicevutaPdfCustom(Boolean avvisaturaMailPromemoriaRicevutaPdfCustom) {
		this.avvisaturaMailPromemoriaRicevutaPdfCustom = avvisaturaMailPromemoriaRicevutaPdfCustom;
	}
	public String getAvvisaturaMailPromemoriaRicevutaOggettoCustom() {
		return avvisaturaMailPromemoriaRicevutaOggettoCustom;
	}
	public void setAvvisaturaMailPromemoriaRicevutaOggettoCustom(String avvisaturaMailPromemoriaRicevutaOggettoCustom) {
		this.avvisaturaMailPromemoriaRicevutaOggettoCustom = avvisaturaMailPromemoriaRicevutaOggettoCustom;
	}
	public String getAvvisaturaMailPromemoriaRicevutaMessaggioCustom() {
		return avvisaturaMailPromemoriaRicevutaMessaggioCustom;
	}
	public void setAvvisaturaMailPromemoriaRicevutaMessaggioCustom(String avvisaturaMailPromemoriaRicevutaMessaggioCustom) {
		this.avvisaturaMailPromemoriaRicevutaMessaggioCustom = avvisaturaMailPromemoriaRicevutaMessaggioCustom;
	}
	public Boolean getAvvisaturaMailPromemoriaRicevutaInviaSoloEseguitiCustom() {
		return avvisaturaMailPromemoriaRicevutaInviaSoloEseguitiCustom;
	}
	public void setAvvisaturaMailPromemoriaRicevutaInviaSoloEseguitiCustom(Boolean avvisaturaMailPromemoriaRicevutaInviaSoloEseguitiCustom) {
		this.avvisaturaMailPromemoriaRicevutaInviaSoloEseguitiCustom = avvisaturaMailPromemoriaRicevutaInviaSoloEseguitiCustom;
	}
	public Boolean getAvvisaturaMailPromemoriaScadenzaAbilitatoCustom() {
		return avvisaturaMailPromemoriaScadenzaAbilitatoCustom;
	}
	public void setAvvisaturaMailPromemoriaScadenzaAbilitatoCustom(Boolean avvisaturaMailPromemoriaScadenzaAbilitatoCustom) {
		this.avvisaturaMailPromemoriaScadenzaAbilitatoCustom = avvisaturaMailPromemoriaScadenzaAbilitatoCustom;
	}
	public String getAvvisaturaMailPromemoriaScadenzaTipoCustom() {
		return avvisaturaMailPromemoriaScadenzaTipoCustom;
	}
	public void setAvvisaturaMailPromemoriaScadenzaTipoCustom(String avvisaturaMailPromemoriaScadenzaTipoCustom) {
		this.avvisaturaMailPromemoriaScadenzaTipoCustom = avvisaturaMailPromemoriaScadenzaTipoCustom;
	}
	public Integer getAvvisaturaMailPromemoriaScadenzaPreavvisoCustom() {
		return avvisaturaMailPromemoriaScadenzaPreavvisoCustom;
	}
	public void setAvvisaturaMailPromemoriaScadenzaPreavvisoCustom(Integer avvisaturaMailPromemoriaScadenzaPreavvisoCustom) {
		this.avvisaturaMailPromemoriaScadenzaPreavvisoCustom = avvisaturaMailPromemoriaScadenzaPreavvisoCustom;
	}
	public String getAvvisaturaMailPromemoriaScadenzaOggettoCustom() {
		return avvisaturaMailPromemoriaScadenzaOggettoCustom;
	}
	public void setAvvisaturaMailPromemoriaScadenzaOggettoCustom(String avvisaturaMailPromemoriaScadenzaOggettoCustom) {
		this.avvisaturaMailPromemoriaScadenzaOggettoCustom = avvisaturaMailPromemoriaScadenzaOggettoCustom;
	}
	public String getAvvisaturaMailPromemoriaScadenzaMessaggioCustom() {
		return avvisaturaMailPromemoriaScadenzaMessaggioCustom;
	}
	public void setAvvisaturaMailPromemoriaScadenzaMessaggioCustom(String avvisaturaMailPromemoriaScadenzaMessaggioCustom) {
		this.avvisaturaMailPromemoriaScadenzaMessaggioCustom = avvisaturaMailPromemoriaScadenzaMessaggioCustom;
	}
	public String getVisualizzazioneDefinizioneCustom() {
		return visualizzazioneDefinizioneCustom;
	}
	public void setVisualizzazioneDefinizioneCustom(String visualizzazioneDefinizioneCustom) {
		this.visualizzazioneDefinizioneCustom = visualizzazioneDefinizioneCustom;
	}
	public String getTracciatoCsvTipoCustom() {
		return tracciatoCsvTipoCustom;
	}
	public void setTracciatoCsvTipoCustom(String tracciatoCsvTipoCustom) {
		this.tracciatoCsvTipoCustom = tracciatoCsvTipoCustom;
	}
	public String getTracciatoCsvIntestazioneCustom() {
		return tracciatoCsvIntestazioneCustom;
	}
	public void setTracciatoCsvIntestazioneCustom(String tracciatoCsvIntestazioneCustom) {
		this.tracciatoCsvIntestazioneCustom = tracciatoCsvIntestazioneCustom;
	}
	public String getTracciatoCsvRichiestaCustom() {
		return tracciatoCsvRichiestaCustom;
	}
	public void setTracciatoCsvRichiestaCustom(String tracciatoCsvRichiestaCustom) {
		this.tracciatoCsvRichiestaCustom = tracciatoCsvRichiestaCustom;
	}
	public String getTracciatoCsvRispostaCustom() {
		return tracciatoCsvRispostaCustom;
	}
	public void setTracciatoCsvRispostaCustom(String tracciatoCsvRispostaCustom) {
		this.tracciatoCsvRispostaCustom = tracciatoCsvRispostaCustom;
	}
	public String getAppIOAPIKey() {
		return appIOAPIKey;
	}
	public void setAppIOAPIKey(String appIOAPIKey) {
		this.appIOAPIKey = appIOAPIKey;
	}
	public Boolean getAvvisaturaAppIoPromemoriaAvvisoAbilitatoCustom() {
		return avvisaturaAppIoPromemoriaAvvisoAbilitatoCustom;
	}
	public void setAvvisaturaAppIoPromemoriaAvvisoAbilitatoCustom(Boolean avvisaturaAppIoPromemoriaAvvisoAbilitatoCustom) {
		this.avvisaturaAppIoPromemoriaAvvisoAbilitatoCustom = avvisaturaAppIoPromemoriaAvvisoAbilitatoCustom;
	}
	public String getAvvisaturaAppIoPromemoriaAvvisoTipoCustom() {
		return avvisaturaAppIoPromemoriaAvvisoTipoCustom;
	}
	public void setAvvisaturaAppIoPromemoriaAvvisoTipoCustom(String avvisaturaAppIoPromemoriaAvvisoTipoCustom) {
		this.avvisaturaAppIoPromemoriaAvvisoTipoCustom = avvisaturaAppIoPromemoriaAvvisoTipoCustom;
	}
	public String getAvvisaturaAppIoPromemoriaAvvisoOggettoCustom() {
		return avvisaturaAppIoPromemoriaAvvisoOggettoCustom;
	}
	public void setAvvisaturaAppIoPromemoriaAvvisoOggettoCustom(String avvisaturaAppIoPromemoriaAvvisoOggettoCustom) {
		this.avvisaturaAppIoPromemoriaAvvisoOggettoCustom = avvisaturaAppIoPromemoriaAvvisoOggettoCustom;
	}
	public String getAvvisaturaAppIoPromemoriaAvvisoMessaggioCustom() {
		return avvisaturaAppIoPromemoriaAvvisoMessaggioCustom;
	}
	public void setAvvisaturaAppIoPromemoriaAvvisoMessaggioCustom(String avvisaturaAppIoPromemoriaAvvisoMessaggioCustom) {
		this.avvisaturaAppIoPromemoriaAvvisoMessaggioCustom = avvisaturaAppIoPromemoriaAvvisoMessaggioCustom;
	}
	public Boolean getAvvisaturaAppIoPromemoriaRicevutaAbilitatoCustom() {
		return avvisaturaAppIoPromemoriaRicevutaAbilitatoCustom;
	}
	public void setAvvisaturaAppIoPromemoriaRicevutaAbilitatoCustom(Boolean avvisaturaAppIoPromemoriaRicevutaAbilitatoCustom) {
		this.avvisaturaAppIoPromemoriaRicevutaAbilitatoCustom = avvisaturaAppIoPromemoriaRicevutaAbilitatoCustom;
	}
	public String getAvvisaturaAppIoPromemoriaRicevutaTipoCustom() {
		return avvisaturaAppIoPromemoriaRicevutaTipoCustom;
	}
	public void setAvvisaturaAppIoPromemoriaRicevutaTipoCustom(String avvisaturaAppIoPromemoriaRicevutaTipoCustom) {
		this.avvisaturaAppIoPromemoriaRicevutaTipoCustom = avvisaturaAppIoPromemoriaRicevutaTipoCustom;
	}
	public String getAvvisaturaAppIoPromemoriaRicevutaOggettoCustom() {
		return avvisaturaAppIoPromemoriaRicevutaOggettoCustom;
	}
	public void setAvvisaturaAppIoPromemoriaRicevutaOggettoCustom(String avvisaturaAppIoPromemoriaRicevutaOggettoCustom) {
		this.avvisaturaAppIoPromemoriaRicevutaOggettoCustom = avvisaturaAppIoPromemoriaRicevutaOggettoCustom;
	}
	public String getAvvisaturaAppIoPromemoriaRicevutaMessaggioCustom() {
		return avvisaturaAppIoPromemoriaRicevutaMessaggioCustom;
	}
	public void setAvvisaturaAppIoPromemoriaRicevutaMessaggioCustom(String avvisaturaAppIoPromemoriaRicevutaMessaggioCustom) {
		this.avvisaturaAppIoPromemoriaRicevutaMessaggioCustom = avvisaturaAppIoPromemoriaRicevutaMessaggioCustom;
	}
	public Boolean getAvvisaturaAppIoPromemoriaRicevutaInviaSoloEseguitiCustom() {
		return avvisaturaAppIoPromemoriaRicevutaInviaSoloEseguitiCustom;
	}
	public void setAvvisaturaAppIoPromemoriaRicevutaInviaSoloEseguitiCustom(Boolean avvisaturaAppIoPromemoriaRicevutaInviaSoloEseguitiCustom) {
		this.avvisaturaAppIoPromemoriaRicevutaInviaSoloEseguitiCustom = avvisaturaAppIoPromemoriaRicevutaInviaSoloEseguitiCustom;
	}
	public Boolean getAvvisaturaAppIoPromemoriaScadenzaAbilitatoCustom() {
		return avvisaturaAppIoPromemoriaScadenzaAbilitatoCustom;
	}
	public void setAvvisaturaAppIoPromemoriaScadenzaAbilitatoCustom(Boolean avvisaturaAppIoPromemoriaScadenzaAbilitatoCustom) {
		this.avvisaturaAppIoPromemoriaScadenzaAbilitatoCustom = avvisaturaAppIoPromemoriaScadenzaAbilitatoCustom;
	}
	public String getAvvisaturaAppIoPromemoriaScadenzaTipoCustom() {
		return avvisaturaAppIoPromemoriaScadenzaTipoCustom;
	}
	public void setAvvisaturaAppIoPromemoriaScadenzaTipoCustom(String avvisaturaAppIoPromemoriaScadenzaTipoCustom) {
		this.avvisaturaAppIoPromemoriaScadenzaTipoCustom = avvisaturaAppIoPromemoriaScadenzaTipoCustom;
	}
	public Integer getAvvisaturaAppIoPromemoriaScadenzaPreavvisoCustom() {
		return avvisaturaAppIoPromemoriaScadenzaPreavvisoCustom;
	}
	public void setAvvisaturaAppIoPromemoriaScadenzaPreavvisoCustom(Integer avvisaturaAppIoPromemoriaScadenzaPreavvisoCustom) {
		this.avvisaturaAppIoPromemoriaScadenzaPreavvisoCustom = avvisaturaAppIoPromemoriaScadenzaPreavvisoCustom;
	}
	public String getAvvisaturaAppIoPromemoriaScadenzaOggettoCustom() {
		return avvisaturaAppIoPromemoriaScadenzaOggettoCustom;
	}
	public void setAvvisaturaAppIoPromemoriaScadenzaOggettoCustom(String avvisaturaAppIoPromemoriaScadenzaOggettoCustom) {
		this.avvisaturaAppIoPromemoriaScadenzaOggettoCustom = avvisaturaAppIoPromemoriaScadenzaOggettoCustom;
	}
	public String getAvvisaturaAppIoPromemoriaScadenzaMessaggioCustom() {
		return avvisaturaAppIoPromemoriaScadenzaMessaggioCustom;
	}
	public void setAvvisaturaAppIoPromemoriaScadenzaMessaggioCustom(String avvisaturaAppIoPromemoriaScadenzaMessaggioCustom) {
		this.avvisaturaAppIoPromemoriaScadenzaMessaggioCustom = avvisaturaAppIoPromemoriaScadenzaMessaggioCustom;
	} 
}