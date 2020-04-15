package it.govpay.bd.model;

import java.math.BigDecimal;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.model.TipoVersamento;

public class TipoVersamentoDominio extends it.govpay.model.TipoVersamentoDominio{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient Dominio dominio;
	private transient TipoVersamento tipoVersamento;
	
	public String getCodificaIuv() {
		if(this.getCodificaIuvCustom() != null)
			return this.getCodificaIuvCustom();
		else 
			return this.getCodificaIuvDefault();
	}
	
	public Tipo getTipo() {
		if(this.getTipoCustom() != null)
			return this.getTipoCustom();
		else 
			return this.getTipoDefault();
	} 
	
	public Boolean getPagaTerzi() {
		if(this.getPagaTerziCustom() != null)
			return this.getPagaTerziCustom();
		else 
			return this.getPagaTerziDefault();
	} 
	
	public Boolean getAbilitato() {
		if(this.getAbilitatoCustom() != null)
			return this.getAbilitatoCustom();
		else 
			return this.isAbilitatoDefault();
	} 

	public boolean isTipoCustom(){return this.getTipoCustom() != null;}
	public boolean isPagaTerzi(){return this.getPagaTerzi();}
	
	public TipoVersamento getTipoVersamento(BasicBD bd) throws ServiceException {
		if(this.tipoVersamento == null) {
			try {
				this.tipoVersamento = AnagraficaManager.getTipoVersamento(bd, this.getIdTipoVersamento());
			} catch (NotFoundException e) {
			}
		} 
		return this.tipoVersamento;
	}

	public Dominio getDominio(BasicBD bd) throws ServiceException {
		if(this.dominio == null) {
			try {
				this.dominio = AnagraficaManager.getDominio(bd, this.getIdDominio());
			} catch (NotFoundException e) {
			}
		} 
		return this.dominio;
	}
	
	/* Configurazione Caricamento pendenza da portale backoffice*/
	
	public String getCaricamentoPendenzePortaleBackofficeFormTipo() {
		if(this.getCaricamentoPendenzePortaleBackofficeFormTipoCustom() != null)
			return this.getCaricamentoPendenzePortaleBackofficeFormTipoCustom();
		else 
			return this.getCaricamentoPendenzePortaleBackofficeFormTipoDefault();
	}
	public String getCaricamentoPendenzePortaleBackofficeFormDefinizione() {
		if(this.getCaricamentoPendenzePortaleBackofficeFormDefinizioneCustom() != null)
			return this.getCaricamentoPendenzePortaleBackofficeFormDefinizioneCustom();
		else 
			return this.getCaricamentoPendenzePortaleBackofficeFormDefinizioneDefault();
	}
	public String getCaricamentoPendenzePortaleBackofficeValidazioneDefinizione() {
		if(this.getCaricamentoPendenzePortaleBackofficeValidazioneDefinizioneCustom() != null)
			return this.getCaricamentoPendenzePortaleBackofficeValidazioneDefinizioneCustom();
		else 
			return this.getCaricamentoPendenzePortaleBackofficeValidazioneDefinizioneDefault();
	}
	public String getCaricamentoPendenzePortaleBackofficeTrasformazioneTipo() {
		if(this.getCaricamentoPendenzePortaleBackofficeTrasformazioneTipoCustom() != null)
			return this.getCaricamentoPendenzePortaleBackofficeTrasformazioneTipoCustom();
		else 
			return this.getCaricamentoPendenzePortaleBackofficeTrasformazioneTipoDefault();
	}
	public String getCaricamentoPendenzePortaleBackofficeTrasformazioneDefinizione() {
		if(this.getCaricamentoPendenzePortaleBackofficeTrasformazioneDefinizioneCustom() != null)
			return this.getCaricamentoPendenzePortaleBackofficeTrasformazioneDefinizioneCustom();
		else 
			return this.getCaricamentoPendenzePortaleBackofficeTrasformazioneDefinizioneDefault();
	}
	public String getCaricamentoPendenzePortaleBackofficeCodApplicazione() {
		if(this.getCaricamentoPendenzePortaleBackofficeCodApplicazioneCustom() != null)
			return this.getCaricamentoPendenzePortaleBackofficeCodApplicazioneCustom();
		else 
			return this.getCaricamentoPendenzePortaleBackofficeCodApplicazioneDefault();
	}
	public Boolean getCaricamentoPendenzePortaleBackofficeAbilitato() {
		if(this.getCaricamentoPendenzePortaleBackofficeAbilitatoCustom() != null)
			return this.getCaricamentoPendenzePortaleBackofficeAbilitatoCustom();
		else 
			return this.isCaricamentoPendenzePortaleBackofficeAbilitatoDefault();
	}
	
	
	/* Configurazione Caricamento pendenza da portale pagamento cittadino */
	
	public String getCaricamentoPendenzePortalePagamentoFormTipo() {
		if(this.getCaricamentoPendenzePortalePagamentoFormTipoCustom() != null)
			return this.getCaricamentoPendenzePortalePagamentoFormTipoCustom();
		else 
			return this.getCaricamentoPendenzePortalePagamentoFormTipoDefault();
	}
	public String getCaricamentoPendenzePortalePagamentoFormDefinizione() {
		if(this.getCaricamentoPendenzePortalePagamentoFormDefinizioneCustom() != null)
			return this.getCaricamentoPendenzePortalePagamentoFormDefinizioneCustom();
		else 
			return this.getCaricamentoPendenzePortalePagamentoFormDefinizioneDefault();
	}
	public String getCaricamentoPendenzePortalePagamentoFormImpaginazione() {
		if(this.getCaricamentoPendenzePortalePagamentoFormImpaginazioneCustom() != null)
			return this.getCaricamentoPendenzePortalePagamentoFormImpaginazioneCustom();
		else 
			return this.getCaricamentoPendenzePortalePagamentoFormImpaginazioneDefault();
	}
	public String getCaricamentoPendenzePortalePagamentoValidazioneDefinizione() {
		if(this.getCaricamentoPendenzePortalePagamentoValidazioneDefinizioneCustom() != null)
			return this.getCaricamentoPendenzePortalePagamentoValidazioneDefinizioneCustom();
		else 
			return this.getCaricamentoPendenzePortalePagamentoValidazioneDefinizioneDefault();
	}
	public String getCaricamentoPendenzePortalePagamentoTrasformazioneTipo() {
		if(this.getCaricamentoPendenzePortalePagamentoTrasformazioneTipoCustom() != null)
			return this.getCaricamentoPendenzePortalePagamentoTrasformazioneTipoCustom();
		else 
			return this.getCaricamentoPendenzePortalePagamentoTrasformazioneTipoDefault();
	}
	public String getCaricamentoPendenzePortalePagamentoTrasformazioneDefinizione() {
		if(this.getCaricamentoPendenzePortalePagamentoTrasformazioneDefinizioneCustom() != null)
			return this.getCaricamentoPendenzePortalePagamentoTrasformazioneDefinizioneCustom();
		else 
			return this.getCaricamentoPendenzePortalePagamentoTrasformazioneDefinizioneDefault();
	}
	public String getCaricamentoPendenzePortalePagamentoCodApplicazione() {
		if(this.getCaricamentoPendenzePortalePagamentoCodApplicazioneCustom() != null)
			return this.getCaricamentoPendenzePortalePagamentoCodApplicazioneCustom();
		else 
			return this.getCaricamentoPendenzePortalePagamentoCodApplicazioneDefault();
	}
	public Boolean getCaricamentoPendenzePortalePagamentoAbilitato() {
		if(this.getCaricamentoPendenzePortalePagamentoAbilitatoCustom() != null)
			return this.getCaricamentoPendenzePortalePagamentoAbilitatoCustom();
		else 
			return this.isCaricamentoPendenzePortalePagamentoAbilitatoDefault();
	}
	
	
	/* Avvisatura Via Mail */
	
	public Boolean getAvvisaturaMailPromemoriaAvvisoAbilitato() {
		if(this.getAvvisaturaMailPromemoriaAvvisoAbilitatoCustom() != null)
			return this.getAvvisaturaMailPromemoriaAvvisoAbilitatoCustom();
		else 
			return this.isAvvisaturaMailPromemoriaAvvisoAbilitatoDefault();
	}
	public String getAvvisaturaMailPromemoriaAvvisoTipo() {
		if(this.getAvvisaturaMailPromemoriaAvvisoTipoCustom() != null)
			return this.getAvvisaturaMailPromemoriaAvvisoTipoCustom();
		else 
			return this.getAvvisaturaMailPromemoriaAvvisoTipoDefault();
	}
	public Boolean getAvvisaturaMailPromemoriaAvvisoPdf() {
		if(this.getAvvisaturaMailPromemoriaAvvisoPdfCustom() != null)
			return this.getAvvisaturaMailPromemoriaAvvisoPdfCustom();
		else 
			return this.getAvvisaturaMailPromemoriaAvvisoPdfDefault();
	}
	public String getAvvisaturaMailPromemoriaAvvisoOggetto() {
		if(this.getAvvisaturaMailPromemoriaAvvisoOggettoCustom() != null)
			return this.getAvvisaturaMailPromemoriaAvvisoOggettoCustom();
		else 
			return this.getAvvisaturaMailPromemoriaAvvisoOggettoDefault();
	}
	public String getAvvisaturaMailPromemoriaAvvisoMessaggio() {
		if(this.getAvvisaturaMailPromemoriaAvvisoMessaggioCustom() != null)
			return this.getAvvisaturaMailPromemoriaAvvisoMessaggioCustom();
		else 
			return this.getAvvisaturaMailPromemoriaAvvisoMessaggioDefault();
	}
	public Boolean getAvvisaturaMailPromemoriaRicevutaAbilitato() {
		if(this.getAvvisaturaMailPromemoriaRicevutaAbilitatoCustom() != null)
			return this.getAvvisaturaMailPromemoriaRicevutaAbilitatoCustom();
		else 
			return this.isAvvisaturaMailPromemoriaRicevutaAbilitatoDefault();
	}
	public String getAvvisaturaMailPromemoriaRicevutaTipo() {
		if(this.getAvvisaturaMailPromemoriaRicevutaTipoCustom() != null)
			return this.getAvvisaturaMailPromemoriaRicevutaTipoCustom();
		else 
			return this.getAvvisaturaMailPromemoriaRicevutaTipoDefault();
	}
	public Boolean getAvvisaturaMailPromemoriaRicevutaPdf() {
		if(this.getAvvisaturaMailPromemoriaRicevutaPdfCustom() != null)
			return this.getAvvisaturaMailPromemoriaRicevutaPdfCustom();
		else 
			return this.getAvvisaturaMailPromemoriaRicevutaPdfDefault();
	}
	public String getAvvisaturaMailPromemoriaRicevutaOggetto() {
		if(this.getAvvisaturaMailPromemoriaRicevutaOggettoCustom() != null)
			return this.getAvvisaturaMailPromemoriaRicevutaOggettoCustom();
		else 
			return this.getAvvisaturaMailPromemoriaRicevutaOggettoDefault();
	}
	public String getAvvisaturaMailPromemoriaRicevutaMessaggio() {
		if(this.getAvvisaturaMailPromemoriaRicevutaMessaggioCustom() != null)
			return this.getAvvisaturaMailPromemoriaRicevutaMessaggioCustom();
		else 
			return this.getAvvisaturaMailPromemoriaRicevutaMessaggioDefault();
	}
	public Boolean getAvvisaturaMailPromemoriaRicevutaInviaSoloEseguiti() {
		if(this.getAvvisaturaMailPromemoriaRicevutaInviaSoloEseguitiCustom() != null)
			return this.getAvvisaturaMailPromemoriaRicevutaInviaSoloEseguitiCustom();
		else 
			return this.getAvvisaturaMailPromemoriaRicevutaInviaSoloEseguitiDefault();
	}
	public Boolean getAvvisaturaMailPromemoriaScadenzaAbilitato() {
		if(this.getAvvisaturaMailPromemoriaScadenzaAbilitatoCustom() != null)
			return this.getAvvisaturaMailPromemoriaScadenzaAbilitatoCustom();
		else 
			return this.isAvvisaturaMailPromemoriaScadenzaAbilitatoDefault();
	}
	public String getAvvisaturaMailPromemoriaScadenzaTipo() {
		if(this.getAvvisaturaMailPromemoriaScadenzaTipoCustom() != null)
			return this.getAvvisaturaMailPromemoriaScadenzaTipoCustom();
		else 
			return this.getAvvisaturaMailPromemoriaScadenzaTipoDefault();
	}
	public BigDecimal getAvvisaturaMailPromemoriaScadenzaPreavviso() {
		if(this.getAvvisaturaMailPromemoriaScadenzaPreavvisoCustom() != null)
			return this.getAvvisaturaMailPromemoriaScadenzaPreavvisoCustom();
		else 
			return this.getAvvisaturaMailPromemoriaScadenzaPreavvisoDefault();
	}
	public String getAvvisaturaMailPromemoriaScadenzaOggetto() {
		if(this.getAvvisaturaMailPromemoriaScadenzaOggettoCustom() != null)
			return this.getAvvisaturaMailPromemoriaScadenzaOggettoCustom();
		else 
			return this.getAvvisaturaMailPromemoriaScadenzaOggettoDefault();
	}
	public String getAvvisaturaMailPromemoriaScadenzaMessaggio() {
		if(this.getAvvisaturaMailPromemoriaScadenzaMessaggioCustom() != null)
			return this.getAvvisaturaMailPromemoriaScadenzaMessaggioCustom();
		else 
			return this.getAvvisaturaMailPromemoriaScadenzaMessaggioDefault();
	}
	
	
	/* Visualizzazione Custom dettaglio pendenza */
	
	public String getVisualizzazioneDefinizione() {
		if(this.getVisualizzazioneDefinizioneCustom() != null)
			return this.getVisualizzazioneDefinizioneCustom();
		else 
			return this.getVisualizzazioneDefinizioneDefault();
	}
	
	/* Configurazione conversione pendenza  caricata con i tracciati CSV */
	
	public String getTracciatoCsvTipo() {
		if(this.getTracciatoCsvTipoCustom() != null)
			return this.getTracciatoCsvTipoCustom();
		else 
			return this.getTracciatoCsvTipoDefault();
	}
	
	public String getTracciatoCsvIntestazione() {
		if(this.getTracciatoCsvIntestazioneCustom() != null)
			return this.getTracciatoCsvIntestazioneCustom();
		else 
			return this.getTracciatoCsvIntestazioneDefault();
	}
	
	public String getTracciatoCsvRichiesta() {
		if(this.getTracciatoCsvRichiestaCustom() != null)
			return this.getTracciatoCsvRichiestaCustom();
		else 
			return this.getTracciatoCsvRichiestaDefault();
	}
	
	public String getTracciatoCsvRisposta() {
		if(this.getTracciatoCsvRispostaCustom() != null)
			return this.getTracciatoCsvRispostaCustom();
		else 
			return this.getTracciatoCsvRispostaDefault();
	}
	
	/* Avvisatura Via AppIO */
	
	public Boolean getAvvisaturaAppIoPromemoriaAvvisoAbilitato() {
		if(this.getAvvisaturaAppIoPromemoriaAvvisoAbilitatoCustom() != null)
			return this.getAvvisaturaAppIoPromemoriaAvvisoAbilitatoCustom();
		else 
			return this.isAvvisaturaAppIoPromemoriaAvvisoAbilitatoDefault();
	}
	public String getAvvisaturaAppIoPromemoriaAvvisoTipo() {
		if(this.getAvvisaturaAppIoPromemoriaAvvisoTipoCustom() != null)
			return this.getAvvisaturaAppIoPromemoriaAvvisoTipoCustom();
		else 
			return this.getAvvisaturaAppIoPromemoriaAvvisoTipoDefault();
	}
	public String getAvvisaturaAppIoPromemoriaAvvisoOggetto() {
		if(this.getAvvisaturaAppIoPromemoriaAvvisoOggettoCustom() != null)
			return this.getAvvisaturaAppIoPromemoriaAvvisoOggettoCustom();
		else 
			return this.getAvvisaturaAppIoPromemoriaAvvisoOggettoDefault();
	}
	public String getAvvisaturaAppIoPromemoriaAvvisoMessaggio() {
		if(this.getAvvisaturaAppIoPromemoriaAvvisoMessaggioCustom() != null)
			return this.getAvvisaturaAppIoPromemoriaAvvisoMessaggioCustom();
		else 
			return this.getAvvisaturaAppIoPromemoriaAvvisoMessaggioDefault();
	}
	public Boolean getAvvisaturaAppIoPromemoriaRicevutaAbilitato() {
		if(this.getAvvisaturaAppIoPromemoriaRicevutaAbilitatoCustom() != null)
			return this.getAvvisaturaAppIoPromemoriaRicevutaAbilitatoCustom();
		else 
			return this.isAvvisaturaAppIoPromemoriaRicevutaAbilitatoDefault();
	}
	public String getAvvisaturaAppIoPromemoriaRicevutaTipo() {
		if(this.getAvvisaturaAppIoPromemoriaRicevutaTipoCustom() != null)
			return this.getAvvisaturaAppIoPromemoriaRicevutaTipoCustom();
		else 
			return this.getAvvisaturaAppIoPromemoriaRicevutaTipoDefault();
	}
	public String getAvvisaturaAppIoPromemoriaRicevutaOggetto() {
		if(this.getAvvisaturaAppIoPromemoriaRicevutaOggettoCustom() != null)
			return this.getAvvisaturaAppIoPromemoriaRicevutaOggettoCustom();
		else 
			return this.getAvvisaturaAppIoPromemoriaRicevutaOggettoDefault();
	}
	public String getAvvisaturaAppIoPromemoriaRicevutaMessaggio() {
		if(this.getAvvisaturaAppIoPromemoriaRicevutaMessaggioCustom() != null)
			return this.getAvvisaturaAppIoPromemoriaRicevutaMessaggioCustom();
		else 
			return this.getAvvisaturaAppIoPromemoriaRicevutaMessaggioDefault();
	}
	public Boolean getAvvisaturaAppIoPromemoriaRicevutaInviaSoloEseguiti() {
		if(this.getAvvisaturaAppIoPromemoriaRicevutaInviaSoloEseguitiCustom() != null) 
			return this.getAvvisaturaAppIoPromemoriaRicevutaInviaSoloEseguitiCustom();
		else 
			return this.getAvvisaturaAppIoPromemoriaRicevutaInviaSoloEseguitiDefault();
	}
	public Boolean getAvvisaturaAppIoPromemoriaScadenzaAbilitato() {
		if(this.getAvvisaturaAppIoPromemoriaScadenzaAbilitatoCustom() != null)
			return this.getAvvisaturaAppIoPromemoriaScadenzaAbilitatoCustom();
		else 
			return this.isAvvisaturaAppIoPromemoriaScadenzaAbilitatoDefault();
	}
	public String getAvvisaturaAppIoPromemoriaScadenzaTipo() {
		if(this.getAvvisaturaAppIoPromemoriaScadenzaTipoCustom() != null)
			return this.getAvvisaturaAppIoPromemoriaScadenzaTipoCustom();
		else 
			return this.getAvvisaturaAppIoPromemoriaScadenzaTipoDefault();
	}
	public BigDecimal getAvvisaturaAppIoPromemoriaScadenzaPreavviso() {
		if(this.getAvvisaturaAppIoPromemoriaScadenzaPreavvisoCustom() != null)
			return this.getAvvisaturaAppIoPromemoriaScadenzaPreavvisoCustom();
		else 
			return this.getAvvisaturaAppIoPromemoriaScadenzaPreavvisoDefault();
	}
	public String getAvvisaturaAppIoPromemoriaScadenzaOggetto() {
		if(this.getAvvisaturaAppIoPromemoriaScadenzaOggettoCustom() != null)
			return this.getAvvisaturaAppIoPromemoriaScadenzaOggettoCustom();
		else 
			return this.getAvvisaturaAppIoPromemoriaScadenzaOggettoDefault();
	}
	public String getAvvisaturaAppIoPromemoriaScadenzaMessaggio() {
		if(this.getAvvisaturaAppIoPromemoriaScadenzaMessaggioCustom() != null)
			return this.getAvvisaturaAppIoPromemoriaScadenzaMessaggioCustom();
		else 
			return this.getAvvisaturaAppIoPromemoriaScadenzaMessaggioDefault();
	}
	
}
