package it.govpay.model;

import java.math.BigDecimal;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.ServiceException;

public class TipoVersamento extends BasicModel {
	private static final long serialVersionUID = 1L;

	public enum Tipo {
		SPONTANEO("SPONTANEO"),
		DOVUTO("DOVUTO");

		private String codifica;

		Tipo(String codifica) {
			this.codifica = codifica;
		}
		public String getCodifica() {
			return this.codifica;
		}

		public static Tipo toEnum(String codifica) throws ServiceException {
			for(Tipo p : Tipo.values()){
				if(p.getCodifica().equals(codifica))
					return p;
			}
			throw new ServiceException("Codifica inesistente per Tipo. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(Tipo.values()));
		}
	}



	private Long id; 
	private String codTipoVersamento;
	private String descrizione;
	private String codificaIuvDefault;
	private Tipo tipoDefault;
	private boolean pagaTerziDefault;
	private boolean abilitatoDefault;
	
	/* Configurazione Caricamento pendenza da portale backoffice*/
	
	private String caricamentoPendenzePortaleBackofficeFormTipoDefault;
	private String caricamentoPendenzePortaleBackofficeFormDefinizioneDefault;
	private String caricamentoPendenzePortaleBackofficeValidazioneDefinizioneDefault;
	private String caricamentoPendenzePortaleBackofficeTrasformazioneTipoDefault;
	private String caricamentoPendenzePortaleBackofficeTrasformazioneDefinizioneDefault;
	private String caricamentoPendenzePortaleBackofficeCodApplicazioneDefault;
	private boolean caricamentoPendenzePortaleBackofficeAbilitatoDefault;
	
	/* Configurazione Caricamento pendenza da portale pagamento cittadino */
	
	private String caricamentoPendenzePortalePagamentoFormTipoDefault;
	private String caricamentoPendenzePortalePagamentoFormDefinizioneDefault;
	private String caricamentoPendenzePortalePagamentoFormImpaginazioneDefault;
	private String caricamentoPendenzePortalePagamentoValidazioneDefinizioneDefault;
	private String caricamentoPendenzePortalePagamentoTrasformazioneTipoDefault;
	private String caricamentoPendenzePortalePagamentoTrasformazioneDefinizioneDefault;
	private String caricamentoPendenzePortalePagamentoCodApplicazioneDefault;
	private boolean caricamentoPendenzePortalePagamentoAbilitatoDefault;
	
	/* Avvisatura Via Mail */
	
	private boolean avvisaturaMailPromemoriaAvvisoAbilitatoDefault;
	private String avvisaturaMailPromemoriaAvvisoTipoDefault;
	private Boolean avvisaturaMailPromemoriaAvvisoPdfDefault;
	private String avvisaturaMailPromemoriaAvvisoOggettoDefault;
	private String avvisaturaMailPromemoriaAvvisoMessaggioDefault;
	
	private boolean avvisaturaMailPromemoriaRicevutaAbilitatoDefault;
	private String avvisaturaMailPromemoriaRicevutaTipoDefault;
	private Boolean avvisaturaMailPromemoriaRicevutaPdfDefault;
	private String avvisaturaMailPromemoriaRicevutaOggettoDefault;
	private String avvisaturaMailPromemoriaRicevutaMessaggioDefault;
	private Boolean avvisaturaMailPromemoriaRicevutaInviaSoloEseguitiDefault;
	
	private boolean avvisaturaMailPromemoriaScadenzaAbilitatoDefault;
	private String avvisaturaMailPromemoriaScadenzaTipoDefault;
	private BigDecimal avvisaturaMailPromemoriaScadenzaPreavvisoDefault;
	private String avvisaturaMailPromemoriaScadenzaOggettoDefault;
	private String avvisaturaMailPromemoriaScadenzaMessaggioDefault;
	
	/* Visualizzazione Custom dettaglio pendenza */
	
	private String visualizzazioneDefinizioneDefault;
	
	/* Configurazione conversione pendenza  caricata con i tracciati CSV */
	
	private String tracciatoCsvTipoDefault;
	private String tracciatoCsvIntestazioneDefault;
	private String tracciatoCsvRichiestaDefault;
	private String tracciatoCsvRispostaDefault;
	
	/* Avvisatura Via AppIO */
	
	private boolean avvisaturaAppIoPromemoriaAvvisoAbilitatoDefault;
	private String avvisaturaAppIoPromemoriaAvvisoTipoDefault;
	private String avvisaturaAppIoPromemoriaAvvisoOggettoDefault;
	private String avvisaturaAppIoPromemoriaAvvisoMessaggioDefault;
	
	private boolean avvisaturaAppIoPromemoriaRicevutaAbilitatoDefault;
	private String avvisaturaAppIoPromemoriaRicevutaTipoDefault;
	private String avvisaturaAppIoPromemoriaRicevutaOggettoDefault;
	private String avvisaturaAppIoPromemoriaRicevutaMessaggioDefault;
	private Boolean avvisaturaAppIoPromemoriaRicevutaInviaSoloEseguitiDefault;
	
	private boolean avvisaturaAppIoPromemoriaScadenzaAbilitatoDefault;
	private String avvisaturaAppIoPromemoriaScadenzaTipoDefault;
	private BigDecimal avvisaturaAppIoPromemoriaScadenzaPreavvisoDefault;
	private String avvisaturaAppIoPromemoriaScadenzaOggettoDefault;
	private String avvisaturaAppIoPromemoriaScadenzaMessaggioDefault;
	

	public String getCodificaIuvDefault() {
		return codificaIuvDefault;
	}
	public void setCodificaIuvDefault(String codificaIuvDefault) {
		this.codificaIuvDefault = codificaIuvDefault;
	}
	public Tipo getTipoDefault() {
		return tipoDefault;
	}
	public void setTipoDefault(Tipo tipoDefault) {
		this.tipoDefault = tipoDefault;
	}
	public boolean getPagaTerziDefault() {
		return pagaTerziDefault;
	}
	public void setPagaTerziDefault(boolean pagaTerziDefault) {
		this.pagaTerziDefault = pagaTerziDefault;
	}
	@Override
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCodTipoVersamento() {
		return codTipoVersamento;
	}
	public void setCodTipoVersamento(String codTipoVersamento) {
		this.codTipoVersamento = codTipoVersamento;
	}
	public String getDescrizione() {
		return this.descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public boolean isAbilitatoDefault() {
		return abilitatoDefault;
	}
	public void setAbilitatoDefault(boolean abilitatoDefault) {
		this.abilitatoDefault = abilitatoDefault;
	}
	public String getCaricamentoPendenzePortaleBackofficeFormTipoDefault() {
		return caricamentoPendenzePortaleBackofficeFormTipoDefault;
	}
	public void setCaricamentoPendenzePortaleBackofficeFormTipoDefault(String caricamentoPendenzePortaleBackofficeFormTipoDefault) {
		this.caricamentoPendenzePortaleBackofficeFormTipoDefault = caricamentoPendenzePortaleBackofficeFormTipoDefault;
	}
	public String getCaricamentoPendenzePortaleBackofficeFormDefinizioneDefault() {
		return caricamentoPendenzePortaleBackofficeFormDefinizioneDefault;
	}
	public void setCaricamentoPendenzePortaleBackofficeFormDefinizioneDefault(String caricamentoPendenzePortaleBackofficeFormDefinizioneDefault) {
		this.caricamentoPendenzePortaleBackofficeFormDefinizioneDefault = caricamentoPendenzePortaleBackofficeFormDefinizioneDefault;
	}
	public String getCaricamentoPendenzePortaleBackofficeValidazioneDefinizioneDefault() {
		return caricamentoPendenzePortaleBackofficeValidazioneDefinizioneDefault;
	}
	public void setCaricamentoPendenzePortaleBackofficeValidazioneDefinizioneDefault(String caricamentoPendenzePortaleBackofficeValidazioneDefinizioneDefault) {
		this.caricamentoPendenzePortaleBackofficeValidazioneDefinizioneDefault = caricamentoPendenzePortaleBackofficeValidazioneDefinizioneDefault;
	}
	public String getCaricamentoPendenzePortaleBackofficeTrasformazioneTipoDefault() {
		return caricamentoPendenzePortaleBackofficeTrasformazioneTipoDefault;
	}
	public void setCaricamentoPendenzePortaleBackofficeTrasformazioneTipoDefault(String caricamentoPendenzePortaleBackofficeTrasformazioneTipoDefault) {
		this.caricamentoPendenzePortaleBackofficeTrasformazioneTipoDefault = caricamentoPendenzePortaleBackofficeTrasformazioneTipoDefault;
	}
	public String getCaricamentoPendenzePortaleBackofficeTrasformazioneDefinizioneDefault() {
		return caricamentoPendenzePortaleBackofficeTrasformazioneDefinizioneDefault;
	}
	public void setCaricamentoPendenzePortaleBackofficeTrasformazioneDefinizioneDefault(String caricamentoPendenzePortaleBackofficeTrasformazioneDefinizioneDefault) {
		this.caricamentoPendenzePortaleBackofficeTrasformazioneDefinizioneDefault = caricamentoPendenzePortaleBackofficeTrasformazioneDefinizioneDefault;
	}
	public String getCaricamentoPendenzePortaleBackofficeCodApplicazioneDefault() {
		return caricamentoPendenzePortaleBackofficeCodApplicazioneDefault;
	}
	public void setCaricamentoPendenzePortaleBackofficeCodApplicazioneDefault(String caricamentoPendenzePortaleBackofficeCodApplicazioneDefault) {
		this.caricamentoPendenzePortaleBackofficeCodApplicazioneDefault = caricamentoPendenzePortaleBackofficeCodApplicazioneDefault;
	}
	public boolean isCaricamentoPendenzePortaleBackofficeAbilitatoDefault() {
		return caricamentoPendenzePortaleBackofficeAbilitatoDefault;
	}
	public void setCaricamentoPendenzePortaleBackofficeAbilitatoDefault(boolean caricamentoPendenzePortaleBackofficeAbilitatoDefault) {
		this.caricamentoPendenzePortaleBackofficeAbilitatoDefault = caricamentoPendenzePortaleBackofficeAbilitatoDefault;
	}
	public String getCaricamentoPendenzePortalePagamentoFormTipoDefault() {
		return caricamentoPendenzePortalePagamentoFormTipoDefault;
	}
	public void setCaricamentoPendenzePortalePagamentoFormTipoDefault(String caricamentoPendenzePortalePagamentoFormTipoDefault) {
		this.caricamentoPendenzePortalePagamentoFormTipoDefault = caricamentoPendenzePortalePagamentoFormTipoDefault;
	}
	public String getCaricamentoPendenzePortalePagamentoFormDefinizioneDefault() {
		return caricamentoPendenzePortalePagamentoFormDefinizioneDefault;
	}
	public void setCaricamentoPendenzePortalePagamentoFormDefinizioneDefault(String caricamentoPendenzePortalePagamentoFormDefinizioneDefault) {
		this.caricamentoPendenzePortalePagamentoFormDefinizioneDefault = caricamentoPendenzePortalePagamentoFormDefinizioneDefault;
	}
	public String getCaricamentoPendenzePortalePagamentoFormImpaginazioneDefault() {
		return caricamentoPendenzePortalePagamentoFormImpaginazioneDefault;
	}
	public void setCaricamentoPendenzePortalePagamentoFormImpaginazioneDefault(String caricamentoPendenzePortalePagamentoFormImpaginazioneDefault) {
		this.caricamentoPendenzePortalePagamentoFormImpaginazioneDefault = caricamentoPendenzePortalePagamentoFormImpaginazioneDefault;
	}
	public String getCaricamentoPendenzePortalePagamentoValidazioneDefinizioneDefault() {
		return caricamentoPendenzePortalePagamentoValidazioneDefinizioneDefault;
	}
	public void setCaricamentoPendenzePortalePagamentoValidazioneDefinizioneDefault(String caricamentoPendenzePortalePagamentoValidazioneDefinizioneDefault) {
		this.caricamentoPendenzePortalePagamentoValidazioneDefinizioneDefault = caricamentoPendenzePortalePagamentoValidazioneDefinizioneDefault;
	}
	public String getCaricamentoPendenzePortalePagamentoTrasformazioneTipoDefault() {
		return caricamentoPendenzePortalePagamentoTrasformazioneTipoDefault;
	}
	public void setCaricamentoPendenzePortalePagamentoTrasformazioneTipoDefault(String caricamentoPendenzePortalePagamentoTrasformazioneTipoDefault) {
		this.caricamentoPendenzePortalePagamentoTrasformazioneTipoDefault = caricamentoPendenzePortalePagamentoTrasformazioneTipoDefault;
	}
	public String getCaricamentoPendenzePortalePagamentoTrasformazioneDefinizioneDefault() {
		return caricamentoPendenzePortalePagamentoTrasformazioneDefinizioneDefault;
	}
	public void setCaricamentoPendenzePortalePagamentoTrasformazioneDefinizioneDefault(String caricamentoPendenzePortalePagamentoTrasformazioneDefinizioneDefault) {
		this.caricamentoPendenzePortalePagamentoTrasformazioneDefinizioneDefault = caricamentoPendenzePortalePagamentoTrasformazioneDefinizioneDefault;
	}
	public String getCaricamentoPendenzePortalePagamentoCodApplicazioneDefault() {
		return caricamentoPendenzePortalePagamentoCodApplicazioneDefault;
	}
	public void setCaricamentoPendenzePortalePagamentoCodApplicazioneDefault(String caricamentoPendenzePortalePagamentoCodApplicazioneDefault) {
		this.caricamentoPendenzePortalePagamentoCodApplicazioneDefault = caricamentoPendenzePortalePagamentoCodApplicazioneDefault;
	}
	public boolean isCaricamentoPendenzePortalePagamentoAbilitatoDefault() {
		return caricamentoPendenzePortalePagamentoAbilitatoDefault;
	}
	public void setCaricamentoPendenzePortalePagamentoAbilitatoDefault(boolean caricamentoPendenzePortalePagamentoAbilitatoDefault) {
		this.caricamentoPendenzePortalePagamentoAbilitatoDefault = caricamentoPendenzePortalePagamentoAbilitatoDefault;
	}
	public boolean isAvvisaturaMailPromemoriaAvvisoAbilitatoDefault() {
		return avvisaturaMailPromemoriaAvvisoAbilitatoDefault;
	}
	public void setAvvisaturaMailPromemoriaAvvisoAbilitatoDefault(boolean avvisaturaMailPromemoriaAvvisoAbilitatoDefault) {
		this.avvisaturaMailPromemoriaAvvisoAbilitatoDefault = avvisaturaMailPromemoriaAvvisoAbilitatoDefault;
	}
	public String getAvvisaturaMailPromemoriaAvvisoTipoDefault() {
		return avvisaturaMailPromemoriaAvvisoTipoDefault;
	}
	public void setAvvisaturaMailPromemoriaAvvisoTipoDefault(String avvisaturaMailPromemoriaAvvisoTipoDefault) {
		this.avvisaturaMailPromemoriaAvvisoTipoDefault = avvisaturaMailPromemoriaAvvisoTipoDefault;
	}
	public Boolean getAvvisaturaMailPromemoriaAvvisoPdfDefault() {
		return avvisaturaMailPromemoriaAvvisoPdfDefault;
	}
	public void setAvvisaturaMailPromemoriaAvvisoPdfDefault(Boolean avvisaturaMailPromemoriaAvvisoPdfDefault) {
		this.avvisaturaMailPromemoriaAvvisoPdfDefault = avvisaturaMailPromemoriaAvvisoPdfDefault;
	}
	public String getAvvisaturaMailPromemoriaAvvisoOggettoDefault() {
		return avvisaturaMailPromemoriaAvvisoOggettoDefault;
	}
	public void setAvvisaturaMailPromemoriaAvvisoOggettoDefault(String avvisaturaMailPromemoriaAvvisoOggettoDefault) {
		this.avvisaturaMailPromemoriaAvvisoOggettoDefault = avvisaturaMailPromemoriaAvvisoOggettoDefault;
	}
	public String getAvvisaturaMailPromemoriaAvvisoMessaggioDefault() {
		return avvisaturaMailPromemoriaAvvisoMessaggioDefault;
	}
	public void setAvvisaturaMailPromemoriaAvvisoMessaggioDefault(String avvisaturaMailPromemoriaAvvisoMessaggioDefault) {
		this.avvisaturaMailPromemoriaAvvisoMessaggioDefault = avvisaturaMailPromemoriaAvvisoMessaggioDefault;
	}
	public boolean isAvvisaturaMailPromemoriaRicevutaAbilitatoDefault() {
		return avvisaturaMailPromemoriaRicevutaAbilitatoDefault;
	}
	public void setAvvisaturaMailPromemoriaRicevutaAbilitatoDefault(boolean avvisaturaMailPromemoriaRicevutaAbilitatoDefault) {
		this.avvisaturaMailPromemoriaRicevutaAbilitatoDefault = avvisaturaMailPromemoriaRicevutaAbilitatoDefault;
	}
	public String getAvvisaturaMailPromemoriaRicevutaTipoDefault() {
		return avvisaturaMailPromemoriaRicevutaTipoDefault;
	}
	public void setAvvisaturaMailPromemoriaRicevutaTipoDefault(String avvisaturaMailPromemoriaRicevutaTipoDefault) {
		this.avvisaturaMailPromemoriaRicevutaTipoDefault = avvisaturaMailPromemoriaRicevutaTipoDefault;
	}
	public Boolean getAvvisaturaMailPromemoriaRicevutaPdfDefault() {
		return avvisaturaMailPromemoriaRicevutaPdfDefault;
	}
	public void setAvvisaturaMailPromemoriaRicevutaPdfDefault(Boolean avvisaturaMailPromemoriaRicevutaPdfDefault) {
		this.avvisaturaMailPromemoriaRicevutaPdfDefault = avvisaturaMailPromemoriaRicevutaPdfDefault;
	}
	public String getAvvisaturaMailPromemoriaRicevutaOggettoDefault() {
		return avvisaturaMailPromemoriaRicevutaOggettoDefault;
	}
	public void setAvvisaturaMailPromemoriaRicevutaOggettoDefault(String avvisaturaMailPromemoriaRicevutaOggettoDefault) {
		this.avvisaturaMailPromemoriaRicevutaOggettoDefault = avvisaturaMailPromemoriaRicevutaOggettoDefault;
	}
	public String getAvvisaturaMailPromemoriaRicevutaMessaggioDefault() {
		return avvisaturaMailPromemoriaRicevutaMessaggioDefault;
	}
	public void setAvvisaturaMailPromemoriaRicevutaMessaggioDefault(String avvisaturaMailPromemoriaRicevutaMessaggioDefault) {
		this.avvisaturaMailPromemoriaRicevutaMessaggioDefault = avvisaturaMailPromemoriaRicevutaMessaggioDefault;
	}
	public Boolean getAvvisaturaMailPromemoriaRicevutaInviaSoloEseguitiDefault() {
		return avvisaturaMailPromemoriaRicevutaInviaSoloEseguitiDefault;
	}
	public void setAvvisaturaMailPromemoriaRicevutaInviaSoloEseguitiDefault(Boolean avvisaturaMailPromemoriaRicevutaInviaSoloEseguitiDefault) {
		this.avvisaturaMailPromemoriaRicevutaInviaSoloEseguitiDefault = avvisaturaMailPromemoriaRicevutaInviaSoloEseguitiDefault;
	}
	public boolean isAvvisaturaMailPromemoriaScadenzaAbilitatoDefault() {
		return avvisaturaMailPromemoriaScadenzaAbilitatoDefault;
	}
	public void setAvvisaturaMailPromemoriaScadenzaAbilitatoDefault(boolean avvisaturaMailPromemoriaScadenzaAbilitatoDefault) {
		this.avvisaturaMailPromemoriaScadenzaAbilitatoDefault = avvisaturaMailPromemoriaScadenzaAbilitatoDefault;
	}
	public String getAvvisaturaMailPromemoriaScadenzaTipoDefault() {
		return avvisaturaMailPromemoriaScadenzaTipoDefault;
	}
	public void setAvvisaturaMailPromemoriaScadenzaTipoDefault(String avvisaturaMailPromemoriaScadenzaTipoDefault) {
		this.avvisaturaMailPromemoriaScadenzaTipoDefault = avvisaturaMailPromemoriaScadenzaTipoDefault;
	}
	public BigDecimal getAvvisaturaMailPromemoriaScadenzaPreavvisoDefault() {
		return avvisaturaMailPromemoriaScadenzaPreavvisoDefault;
	}
	public void setAvvisaturaMailPromemoriaScadenzaPreavvisoDefault(BigDecimal avvisaturaMailPromemoriaScadenzaPreavvisoDefault) {
		this.avvisaturaMailPromemoriaScadenzaPreavvisoDefault = avvisaturaMailPromemoriaScadenzaPreavvisoDefault;
	}
	public String getAvvisaturaMailPromemoriaScadenzaOggettoDefault() {
		return avvisaturaMailPromemoriaScadenzaOggettoDefault;
	}
	public void setAvvisaturaMailPromemoriaScadenzaOggettoDefault(String avvisaturaMailPromemoriaScadenzaOggettoDefault) {
		this.avvisaturaMailPromemoriaScadenzaOggettoDefault = avvisaturaMailPromemoriaScadenzaOggettoDefault;
	}
	public String getAvvisaturaMailPromemoriaScadenzaMessaggioDefault() {
		return avvisaturaMailPromemoriaScadenzaMessaggioDefault;
	}
	public void setAvvisaturaMailPromemoriaScadenzaMessaggioDefault(
			String avvisaturaMailPromemoriaScadenzaMessaggioDefault) {
		this.avvisaturaMailPromemoriaScadenzaMessaggioDefault = avvisaturaMailPromemoriaScadenzaMessaggioDefault;
	}
	public String getVisualizzazioneDefinizioneDefault() {
		return visualizzazioneDefinizioneDefault;
	}
	public void setVisualizzazioneDefinizioneDefault(String visualizzazioneDefinizioneDefault) {
		this.visualizzazioneDefinizioneDefault = visualizzazioneDefinizioneDefault;
	}
	public String getTracciatoCsvTipoDefault() {
		return tracciatoCsvTipoDefault;
	}
	public void setTracciatoCsvTipoDefault(String tracciatoCsvTipoDefault) {
		this.tracciatoCsvTipoDefault = tracciatoCsvTipoDefault;
	}
	public String getTracciatoCsvIntestazioneDefault() {
		return tracciatoCsvIntestazioneDefault;
	}
	public void setTracciatoCsvIntestazioneDefault(String tracciatoCsvIntestazioneDefault) {
		this.tracciatoCsvIntestazioneDefault = tracciatoCsvIntestazioneDefault;
	}
	public String getTracciatoCsvRichiestaDefault() {
		return tracciatoCsvRichiestaDefault;
	}
	public void setTracciatoCsvRichiestaDefault(String tracciatoCsvRichiestaDefault) {
		this.tracciatoCsvRichiestaDefault = tracciatoCsvRichiestaDefault;
	}
	public String getTracciatoCsvRispostaDefault() {
		return tracciatoCsvRispostaDefault;
	}
	public void setTracciatoCsvRispostaDefault(String tracciatoCsvRispostaDefault) {
		this.tracciatoCsvRispostaDefault = tracciatoCsvRispostaDefault;
	}
	public boolean isAvvisaturaAppIoPromemoriaAvvisoAbilitatoDefault() {
		return avvisaturaAppIoPromemoriaAvvisoAbilitatoDefault;
	}
	public void setAvvisaturaAppIoPromemoriaAvvisoAbilitatoDefault(boolean avvisaturaAppIoPromemoriaAvvisoAbilitatoDefault) {
		this.avvisaturaAppIoPromemoriaAvvisoAbilitatoDefault = avvisaturaAppIoPromemoriaAvvisoAbilitatoDefault;
	}
	public String getAvvisaturaAppIoPromemoriaAvvisoTipoDefault() {
		return avvisaturaAppIoPromemoriaAvvisoTipoDefault;
	}
	public void setAvvisaturaAppIoPromemoriaAvvisoTipoDefault(String avvisaturaAppIoPromemoriaAvvisoTipoDefault) {
		this.avvisaturaAppIoPromemoriaAvvisoTipoDefault = avvisaturaAppIoPromemoriaAvvisoTipoDefault;
	}
	public String getAvvisaturaAppIoPromemoriaAvvisoOggettoDefault() {
		return avvisaturaAppIoPromemoriaAvvisoOggettoDefault;
	}
	public void setAvvisaturaAppIoPromemoriaAvvisoOggettoDefault(String avvisaturaAppIoPromemoriaAvvisoOggettoDefault) {
		this.avvisaturaAppIoPromemoriaAvvisoOggettoDefault = avvisaturaAppIoPromemoriaAvvisoOggettoDefault;
	}
	public String getAvvisaturaAppIoPromemoriaAvvisoMessaggioDefault() {
		return avvisaturaAppIoPromemoriaAvvisoMessaggioDefault;
	}
	public void setAvvisaturaAppIoPromemoriaAvvisoMessaggioDefault(String avvisaturaAppIoPromemoriaAvvisoMessaggioDefault) {
		this.avvisaturaAppIoPromemoriaAvvisoMessaggioDefault = avvisaturaAppIoPromemoriaAvvisoMessaggioDefault;
	}
	public boolean isAvvisaturaAppIoPromemoriaRicevutaAbilitatoDefault() {
		return avvisaturaAppIoPromemoriaRicevutaAbilitatoDefault;
	}
	public void setAvvisaturaAppIoPromemoriaRicevutaAbilitatoDefault(boolean avvisaturaAppIoPromemoriaRicevutaAbilitatoDefault) {
		this.avvisaturaAppIoPromemoriaRicevutaAbilitatoDefault = avvisaturaAppIoPromemoriaRicevutaAbilitatoDefault;
	}
	public String getAvvisaturaAppIoPromemoriaRicevutaTipoDefault() {
		return avvisaturaAppIoPromemoriaRicevutaTipoDefault;
	}
	public void setAvvisaturaAppIoPromemoriaRicevutaTipoDefault(String avvisaturaAppIoPromemoriaRicevutaTipoDefault) {
		this.avvisaturaAppIoPromemoriaRicevutaTipoDefault = avvisaturaAppIoPromemoriaRicevutaTipoDefault;
	}
	public String getAvvisaturaAppIoPromemoriaRicevutaOggettoDefault() {
		return avvisaturaAppIoPromemoriaRicevutaOggettoDefault;
	}
	public void setAvvisaturaAppIoPromemoriaRicevutaOggettoDefault(String avvisaturaAppIoPromemoriaRicevutaOggettoDefault) {
		this.avvisaturaAppIoPromemoriaRicevutaOggettoDefault = avvisaturaAppIoPromemoriaRicevutaOggettoDefault;
	}
	public String getAvvisaturaAppIoPromemoriaRicevutaMessaggioDefault() {
		return avvisaturaAppIoPromemoriaRicevutaMessaggioDefault;
	}
	public void setAvvisaturaAppIoPromemoriaRicevutaMessaggioDefault(String avvisaturaAppIoPromemoriaRicevutaMessaggioDefault) {
		this.avvisaturaAppIoPromemoriaRicevutaMessaggioDefault = avvisaturaAppIoPromemoriaRicevutaMessaggioDefault;
	}
	public Boolean getAvvisaturaAppIoPromemoriaRicevutaInviaSoloEseguitiDefault() {
		return avvisaturaAppIoPromemoriaRicevutaInviaSoloEseguitiDefault;
	}
	public void setAvvisaturaAppIoPromemoriaRicevutaInviaSoloEseguitiDefault(Boolean avvisaturaAppIoPromemoriaRicevutaInviaSoloEseguitiDefault) {
		this.avvisaturaAppIoPromemoriaRicevutaInviaSoloEseguitiDefault = avvisaturaAppIoPromemoriaRicevutaInviaSoloEseguitiDefault;
	}
	public boolean isAvvisaturaAppIoPromemoriaScadenzaAbilitatoDefault() {
		return avvisaturaAppIoPromemoriaScadenzaAbilitatoDefault;
	}
	public void setAvvisaturaAppIoPromemoriaScadenzaAbilitatoDefault(boolean avvisaturaAppIoPromemoriaScadenzaAbilitatoDefault) {
		this.avvisaturaAppIoPromemoriaScadenzaAbilitatoDefault = avvisaturaAppIoPromemoriaScadenzaAbilitatoDefault;
	}
	public String getAvvisaturaAppIoPromemoriaScadenzaTipoDefault() {
		return avvisaturaAppIoPromemoriaScadenzaTipoDefault;
	}
	public void setAvvisaturaAppIoPromemoriaScadenzaTipoDefault(String avvisaturaAppIoPromemoriaScadenzaTipoDefault) {
		this.avvisaturaAppIoPromemoriaScadenzaTipoDefault = avvisaturaAppIoPromemoriaScadenzaTipoDefault;
	}
	public BigDecimal getAvvisaturaAppIoPromemoriaScadenzaPreavvisoDefault() {
		return avvisaturaAppIoPromemoriaScadenzaPreavvisoDefault;
	}
	public void setAvvisaturaAppIoPromemoriaScadenzaPreavvisoDefault(BigDecimal avvisaturaAppIoPromemoriaScadenzaPreavvisoDefault) {
		this.avvisaturaAppIoPromemoriaScadenzaPreavvisoDefault = avvisaturaAppIoPromemoriaScadenzaPreavvisoDefault;
	}
	public String getAvvisaturaAppIoPromemoriaScadenzaOggettoDefault() {
		return avvisaturaAppIoPromemoriaScadenzaOggettoDefault;
	}
	public void setAvvisaturaAppIoPromemoriaScadenzaOggettoDefault(String avvisaturaAppIoPromemoriaScadenzaOggettoDefault) {
		this.avvisaturaAppIoPromemoriaScadenzaOggettoDefault = avvisaturaAppIoPromemoriaScadenzaOggettoDefault;
	}
	public String getAvvisaturaAppIoPromemoriaScadenzaMessaggioDefault() {
		return avvisaturaAppIoPromemoriaScadenzaMessaggioDefault;
	}
	public void setAvvisaturaAppIoPromemoriaScadenzaMessaggioDefault(String avvisaturaAppIoPromemoriaScadenzaMessaggioDefault) {
		this.avvisaturaAppIoPromemoriaScadenzaMessaggioDefault = avvisaturaAppIoPromemoriaScadenzaMessaggioDefault;
	}	
}
