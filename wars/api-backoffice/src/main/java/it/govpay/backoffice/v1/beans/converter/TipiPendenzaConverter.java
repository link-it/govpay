package it.govpay.backoffice.v1.beans.converter;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.jaxrs.RawObject;
import org.openspcoop2.utils.json.ValidationException;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.TipoPendenza;
import it.govpay.backoffice.v1.beans.TipoPendenzaAvvisaturaAppIO;
import it.govpay.backoffice.v1.beans.TipoPendenzaAvvisaturaMail;
import it.govpay.backoffice.v1.beans.TipoPendenzaAvvisaturaMailPromemoriaAvviso;
import it.govpay.backoffice.v1.beans.TipoPendenzaAvvisaturaMailPromemoriaRicevuta;
import it.govpay.backoffice.v1.beans.TipoPendenzaAvvisaturaPromemoriaAvvisoBase;
import it.govpay.backoffice.v1.beans.TipoPendenzaAvvisaturaPromemoriaRicevutaBase;
import it.govpay.backoffice.v1.beans.TipoPendenzaAvvisaturaPromemoriaScadenza;
import it.govpay.backoffice.v1.beans.TipoPendenzaFormPortaleBackoffice;
import it.govpay.backoffice.v1.beans.TipoPendenzaFormPortalePagamenti;
import it.govpay.backoffice.v1.beans.TipoPendenzaIndex;
import it.govpay.backoffice.v1.beans.TipoPendenzaPortaleBackofficeCaricamentoPendenze;
import it.govpay.backoffice.v1.beans.TipoPendenzaPortalePagamentiCaricamentoPendenze;
import it.govpay.backoffice.v1.beans.TipoPendenzaPost;
import it.govpay.backoffice.v1.beans.TipoPendenzaTrasformazione;
import it.govpay.backoffice.v1.beans.TipoTemplateTrasformazione;
import it.govpay.backoffice.v1.beans.TracciatoCsv;
import it.govpay.bd.model.TipoVersamentoDominio;
import it.govpay.core.dao.anagrafica.dto.PutTipoPendenzaDTO;
import it.govpay.core.utils.rawutils.ConverterUtils;

public class TipiPendenzaConverter {

	public static PutTipoPendenzaDTO getPutTipoPendenzaDTO(TipoPendenzaPost entrataPost, String idTipoPendenza, Authentication user) throws ServiceException, ValidationException {
		PutTipoPendenzaDTO entrataDTO = new PutTipoPendenzaDTO(user);
		
		it.govpay.model.TipoVersamento tipoVersamento = new it.govpay.model.TipoVersamento();
		
		tipoVersamento.setCodificaIuvDefault(entrataPost.getCodificaIUV());
		tipoVersamento.setCodTipoVersamento(idTipoPendenza);
		tipoVersamento.setDescrizione(entrataPost.getDescrizione());

		entrataDTO.setCodTipoVersamento(idTipoPendenza);
		entrataDTO.setTipoVersamento(tipoVersamento);
		
		tipoVersamento.setPagaTerziDefault(entrataPost.PagaTerzi());
		tipoVersamento.setAbilitatoDefault(entrataPost.Abilitato());
		
		// Configurazione Caricamento Pendenze Portale Backoffice
		tipoVersamento.setCaricamentoPendenzePortaleBackofficeAbilitatoDefault(false);
		if(entrataPost.getPortaleBackoffice() != null) {
			tipoVersamento.setCaricamentoPendenzePortaleBackofficeAbilitatoDefault(entrataPost.getPortaleBackoffice().Abilitato());
			
			if(tipoVersamento.isCaricamentoPendenzePortaleBackofficeAbilitatoDefault()) {
				if(entrataPost.getPortaleBackoffice().getForm() != null && entrataPost.getPortaleBackoffice().getForm().getDefinizione() != null && entrataPost.getPortaleBackoffice().getForm().getTipo() != null) {
					Object definizione = entrataPost.getPortaleBackoffice().getForm().getDefinizione();
					tipoVersamento.setCaricamentoPendenzePortaleBackofficeFormDefinizioneDefault(ConverterUtils.toJSON(definizione,null));
					tipoVersamento.setCaricamentoPendenzePortaleBackofficeFormTipoDefault(entrataPost.getPortaleBackoffice().getForm().getTipo());
				}
				
				if(entrataPost.getPortaleBackoffice().getTrasformazione() != null  && entrataPost.getPortaleBackoffice().getTrasformazione().getDefinizione() != null && entrataPost.getPortaleBackoffice().getTrasformazione().getTipo() != null) {
					if(entrataPost.getPortaleBackoffice().getTrasformazione().getTipo() != null) {
						// valore tipo template trasformazione non valido
						if(TipoTemplateTrasformazione.fromValue(entrataPost.getPortaleBackoffice().getTrasformazione().getTipo()) == null) {
							throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" + entrataPost.getPortaleBackoffice().getTrasformazione().getTipo() + "] valori possibili " + ArrayUtils.toString(TipoTemplateTrasformazione.values()));
						}
					}
					
					Object definizione = entrataPost.getPortaleBackoffice().getTrasformazione().getDefinizione();
					tipoVersamento.setCaricamentoPendenzePortaleBackofficeTrasformazioneDefinizioneDefault(ConverterUtils.toJSON(definizione,null));
					tipoVersamento.setCaricamentoPendenzePortaleBackofficeTrasformazioneTipoDefault(entrataPost.getPortaleBackoffice().getTrasformazione().getTipo());
				}
				if(entrataPost.getPortaleBackoffice().getValidazione() != null)
					tipoVersamento.setCaricamentoPendenzePortaleBackofficeValidazioneDefinizioneDefault(ConverterUtils.toJSON(entrataPost.getPortaleBackoffice().getValidazione(),null));
				
				if(entrataPost.getPortaleBackoffice().getInoltro() != null)
					tipoVersamento.setCaricamentoPendenzePortaleBackofficeCodApplicazioneDefault(entrataPost.getPortaleBackoffice().getInoltro());
			}
		}
		
		// Configurazione Caricamento Pendenze Portale Backoffice
		tipoVersamento.setCaricamentoPendenzePortalePagamentoAbilitatoDefault(false);
		if(entrataPost.getPortalePagamento() != null) {
			tipoVersamento.setCaricamentoPendenzePortalePagamentoAbilitatoDefault(entrataPost.getPortalePagamento().Abilitato());
			
			if(tipoVersamento.isCaricamentoPendenzePortalePagamentoAbilitatoDefault()) {
				if(entrataPost.getPortalePagamento().getForm() != null && entrataPost.getPortalePagamento().getForm().getDefinizione() != null && entrataPost.getPortalePagamento().getForm().getTipo() != null) {
					Object definizione = entrataPost.getPortalePagamento().getForm().getDefinizione();
					tipoVersamento.setCaricamentoPendenzePortalePagamentoFormDefinizioneDefault(ConverterUtils.toJSON(definizione,null));
					tipoVersamento.setCaricamentoPendenzePortalePagamentoFormTipoDefault(entrataPost.getPortalePagamento().getForm().getTipo());
					Object impaginazione = entrataPost.getPortalePagamento().getForm().getImpaginazione();
					tipoVersamento.setCaricamentoPendenzePortalePagamentoFormImpaginazioneDefault(ConverterUtils.toJSON(impaginazione,null));
				}
				
				if(entrataPost.getPortalePagamento().getTrasformazione() != null  && entrataPost.getPortalePagamento().getTrasformazione().getDefinizione() != null && entrataPost.getPortalePagamento().getTrasformazione().getTipo() != null) {
					if(entrataPost.getPortalePagamento().getTrasformazione().getTipo() != null) {
						// valore tipo template trasformazione non valido
						if(TipoTemplateTrasformazione.fromValue(entrataPost.getPortalePagamento().getTrasformazione().getTipo()) == null) {
							throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" + entrataPost.getPortalePagamento().getTrasformazione().getTipo() + "] valori possibili " + ArrayUtils.toString(TipoTemplateTrasformazione.values()));
						}
					}
					
					Object definizione = entrataPost.getPortalePagamento().getTrasformazione().getDefinizione();
					tipoVersamento.setCaricamentoPendenzePortalePagamentoTrasformazioneDefinizioneDefault(ConverterUtils.toJSON(definizione,null));
					tipoVersamento.setCaricamentoPendenzePortalePagamentoTrasformazioneTipoDefault(entrataPost.getPortalePagamento().getTrasformazione().getTipo());
				}
				if(entrataPost.getPortalePagamento().getValidazione() != null)
					tipoVersamento.setCaricamentoPendenzePortalePagamentoValidazioneDefinizioneDefault(ConverterUtils.toJSON(entrataPost.getPortalePagamento().getValidazione(),null));
				
				if(entrataPost.getPortalePagamento().getInoltro() != null)
					tipoVersamento.setCaricamentoPendenzePortalePagamentoCodApplicazioneDefault(entrataPost.getPortalePagamento().getInoltro());
			}
		}
		
		// Avvisatura Via Mail
		tipoVersamento.setAvvisaturaMailPromemoriaAvvisoAbilitatoDefault(false);
		tipoVersamento.setAvvisaturaMailPromemoriaScadenzaAbilitatoDefault(false);
		tipoVersamento.setAvvisaturaMailPromemoriaRicevutaAbilitatoDefault(false);
		if(entrataPost.getAvvisaturaMail() != null) {
			if(entrataPost.getAvvisaturaMail().getPromemoriaAvviso() != null) {
				if(entrataPost.getAvvisaturaMail().getPromemoriaAvviso().Abilitato() != null) {
					
					tipoVersamento.setAvvisaturaMailPromemoriaAvvisoAbilitatoDefault(entrataPost.getAvvisaturaMail().getPromemoriaAvviso().Abilitato());
				}else {
					tipoVersamento.setAvvisaturaMailPromemoriaAvvisoAbilitatoDefault(false);
				}

				if(entrataPost.getAvvisaturaMail().getPromemoriaAvviso().getMessaggio() != null) {
					tipoVersamento.setAvvisaturaMailPromemoriaAvvisoMessaggioDefault(ConverterUtils.toJSON(entrataPost.getAvvisaturaMail().getPromemoriaAvviso().getMessaggio(),null));
				}else {
					tipoVersamento.setAvvisaturaMailPromemoriaAvvisoMessaggioDefault(null);
				}
				if(entrataPost.getAvvisaturaMail().getPromemoriaAvviso().getOggetto() != null) {
					tipoVersamento.setAvvisaturaMailPromemoriaAvvisoOggettoDefault(ConverterUtils.toJSON(entrataPost.getAvvisaturaMail().getPromemoriaAvviso().getOggetto(),null));
				}else {
					tipoVersamento.setAvvisaturaMailPromemoriaAvvisoOggettoDefault(null);
				}
				if(entrataPost.getAvvisaturaMail().getPromemoriaAvviso().AllegaPdf() != null) {
					tipoVersamento.setAvvisaturaMailPromemoriaAvvisoPdfDefault(entrataPost.getAvvisaturaMail().getPromemoriaAvviso().AllegaPdf());
				}else {
					tipoVersamento.setAvvisaturaMailPromemoriaAvvisoPdfDefault(null);
				}
				if(entrataPost.getAvvisaturaMail().getPromemoriaAvviso().getTipo() != null) {
					tipoVersamento.setAvvisaturaMailPromemoriaAvvisoTipoDefault(entrataPost.getAvvisaturaMail().getPromemoriaAvviso().getTipo());
				}else {
					tipoVersamento.setAvvisaturaMailPromemoriaAvvisoTipoDefault(null);
				}
				
				if(entrataPost.getAvvisaturaMail().getPromemoriaAvviso().getTipo() != null) {
					// valore tipo contabilita non valido
					if(TipoTemplateTrasformazione.fromValue(entrataPost.getAvvisaturaMail().getPromemoriaAvviso().getTipo()) == null) {
						throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" +
								entrataPost.getAvvisaturaMail().getPromemoriaAvviso().getTipo() + "] valori possibili " + ArrayUtils.toString(TipoTemplateTrasformazione.values()));
					}
				}
			}
			
			if(entrataPost.getAvvisaturaMail().getPromemoriaRicevuta() != null) {
				if(entrataPost.getAvvisaturaMail().getPromemoriaRicevuta().Abilitato() != null) {
					
					tipoVersamento.setAvvisaturaMailPromemoriaRicevutaAbilitatoDefault(entrataPost.getAvvisaturaMail().getPromemoriaRicevuta().Abilitato());
				}else {
					tipoVersamento.setAvvisaturaMailPromemoriaRicevutaAbilitatoDefault(false);
				}

				if(entrataPost.getAvvisaturaMail().getPromemoriaRicevuta().getMessaggio() != null) {
					tipoVersamento.setAvvisaturaMailPromemoriaRicevutaMessaggioDefault(ConverterUtils.toJSON(entrataPost.getAvvisaturaMail().getPromemoriaRicevuta().getMessaggio(),null));
				}else {
					tipoVersamento.setAvvisaturaMailPromemoriaRicevutaMessaggioDefault(null);
				}
				if(entrataPost.getAvvisaturaMail().getPromemoriaRicevuta().getOggetto() != null) {
					tipoVersamento.setAvvisaturaMailPromemoriaRicevutaOggettoDefault(ConverterUtils.toJSON(entrataPost.getAvvisaturaMail().getPromemoriaRicevuta().getOggetto(),null));
				}else {
					tipoVersamento.setAvvisaturaMailPromemoriaRicevutaOggettoDefault(null);
				}
				if(entrataPost.getAvvisaturaMail().getPromemoriaRicevuta().AllegaPdf() != null) {
					tipoVersamento.setAvvisaturaMailPromemoriaRicevutaPdfDefault(entrataPost.getAvvisaturaMail().getPromemoriaRicevuta().AllegaPdf());
				}else {
					tipoVersamento.setAvvisaturaMailPromemoriaRicevutaPdfDefault(null);
				}
				if(entrataPost.getAvvisaturaMail().getPromemoriaRicevuta().getTipo() != null) {
					tipoVersamento.setAvvisaturaMailPromemoriaRicevutaTipoDefault(entrataPost.getAvvisaturaMail().getPromemoriaRicevuta().getTipo());
				}else {
					tipoVersamento.setAvvisaturaMailPromemoriaRicevutaTipoDefault(null);
				}
				if(entrataPost.getAvvisaturaMail().getPromemoriaRicevuta().SoloEseguiti() != null) {
					tipoVersamento.setAvvisaturaMailPromemoriaRicevutaInviaSoloEseguitiDefault(entrataPost.getAvvisaturaMail().getPromemoriaRicevuta().SoloEseguiti());
				}else {
					tipoVersamento.setAvvisaturaMailPromemoriaRicevutaInviaSoloEseguitiDefault(null);
				}
				
				if(entrataPost.getAvvisaturaMail().getPromemoriaRicevuta().getTipo() != null) {
					// valore tipo contabilita non valido
					if(TipoTemplateTrasformazione.fromValue(entrataPost.getAvvisaturaMail().getPromemoriaRicevuta().getTipo()) == null) {
						throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" +
								entrataPost.getAvvisaturaMail().getPromemoriaRicevuta().getTipo() + "] valori possibili " + ArrayUtils.toString(TipoTemplateTrasformazione.values()));
					}
				}
			}
			
			if(entrataPost.getAvvisaturaMail().getPromemoriaScadenza() != null) {
				if(entrataPost.getAvvisaturaMail().getPromemoriaScadenza().Abilitato() != null) {
					
					tipoVersamento.setAvvisaturaMailPromemoriaScadenzaAbilitatoDefault(entrataPost.getAvvisaturaMail().getPromemoriaScadenza().Abilitato());
				}else {
					tipoVersamento.setAvvisaturaMailPromemoriaScadenzaAbilitatoDefault(false);
				}

				if(entrataPost.getAvvisaturaMail().getPromemoriaScadenza().getMessaggio() != null) {
					tipoVersamento.setAvvisaturaMailPromemoriaScadenzaMessaggioDefault(ConverterUtils.toJSON(entrataPost.getAvvisaturaMail().getPromemoriaScadenza().getMessaggio(),null));
				}else {
					tipoVersamento.setAvvisaturaMailPromemoriaScadenzaMessaggioDefault(null);
				}
				if(entrataPost.getAvvisaturaMail().getPromemoriaScadenza().getOggetto() != null) {
					tipoVersamento.setAvvisaturaMailPromemoriaScadenzaOggettoDefault(ConverterUtils.toJSON(entrataPost.getAvvisaturaMail().getPromemoriaScadenza().getOggetto(),null));
				}else {
					tipoVersamento.setAvvisaturaMailPromemoriaScadenzaOggettoDefault(null);
				}
				if(entrataPost.getAvvisaturaMail().getPromemoriaScadenza().getPreavviso() != null) {
					tipoVersamento.setAvvisaturaMailPromemoriaScadenzaPreavvisoDefault(entrataPost.getAvvisaturaMail().getPromemoriaScadenza().getPreavviso());
				}else {
					tipoVersamento.setAvvisaturaMailPromemoriaScadenzaPreavvisoDefault(null);
				}
				if(entrataPost.getAvvisaturaMail().getPromemoriaScadenza().getTipo() != null) {
					tipoVersamento.setAvvisaturaMailPromemoriaScadenzaTipoDefault(entrataPost.getAvvisaturaMail().getPromemoriaScadenza().getTipo());
				}else {
					tipoVersamento.setAvvisaturaMailPromemoriaScadenzaTipoDefault(null);
				}
				
				
				if(entrataPost.getAvvisaturaMail().getPromemoriaScadenza().getTipo() != null) {
					// valore tipo contabilita non valido
					if(TipoTemplateTrasformazione.fromValue(entrataPost.getAvvisaturaMail().getPromemoriaScadenza().getTipo()) == null) {
						throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" +
								entrataPost.getAvvisaturaMail().getPromemoriaScadenza().getTipo() + "] valori possibili " + ArrayUtils.toString(TipoTemplateTrasformazione.values()));
					}
				}
			}
		}
		
		// Visualizzazione custom del dettaglio pendenza
		if(entrataPost.getVisualizzazione() != null)
			tipoVersamento.setVisualizzazioneDefinizioneDefault(ConverterUtils.toJSON(entrataPost.getVisualizzazione(),null));
		
		// trasformazione csv pendenze
		if(entrataPost.getTracciatoCsv() != null
				&& entrataPost.getTracciatoCsv().getTipo() != null
				&& entrataPost.getTracciatoCsv().getIntestazione() != null
				&& entrataPost.getTracciatoCsv().getRichiesta() != null
				&& entrataPost.getTracciatoCsv().getRisposta() != null) {
			tipoVersamento.setTracciatoCsvTipoDefault(entrataPost.getTracciatoCsv().getTipo());
			
			// valore tipo contabilita non valido
			if(TipoTemplateTrasformazione.fromValue(entrataPost.getTracciatoCsv().getTipo()) == null) {
				throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" +
						entrataPost.getTracciatoCsv().getTipo() + "] valori possibili " + ArrayUtils.toString(TipoTemplateTrasformazione.values()));
			}
			
			tipoVersamento.setTracciatoCsvIntestazioneDefault(entrataPost.getTracciatoCsv().getIntestazione());
			tipoVersamento.setTracciatoCsvRichiestaDefault(ConverterUtils.toJSON(entrataPost.getTracciatoCsv().getRichiesta(),null));
			tipoVersamento.setTracciatoCsvRispostaDefault(ConverterUtils.toJSON(entrataPost.getTracciatoCsv().getRisposta(),null));
		}
		
		// Avvisatura Via AppIO
		tipoVersamento.setAvvisaturaAppIoPromemoriaAvvisoAbilitatoDefault(false);
		tipoVersamento.setAvvisaturaAppIoPromemoriaScadenzaAbilitatoDefault(false);
		tipoVersamento.setAvvisaturaAppIoPromemoriaRicevutaAbilitatoDefault(false);
		if(entrataPost.getAvvisaturaAppIO() != null) {
			if(entrataPost.getAvvisaturaAppIO().getPromemoriaAvviso() != null) {
				if(entrataPost.getAvvisaturaAppIO().getPromemoriaAvviso().Abilitato() != null) {
					
					tipoVersamento.setAvvisaturaAppIoPromemoriaAvvisoAbilitatoDefault(entrataPost.getAvvisaturaAppIO().getPromemoriaAvviso().Abilitato());
				}else {
					tipoVersamento.setAvvisaturaAppIoPromemoriaAvvisoAbilitatoDefault(false);
				}

				if(entrataPost.getAvvisaturaAppIO().getPromemoriaAvviso().getMessaggio() != null) {
					tipoVersamento.setAvvisaturaAppIoPromemoriaAvvisoMessaggioDefault(ConverterUtils.toJSON(entrataPost.getAvvisaturaAppIO().getPromemoriaAvviso().getMessaggio(),null));
				}else {
					tipoVersamento.setAvvisaturaAppIoPromemoriaAvvisoMessaggioDefault(null);
				}
				if(entrataPost.getAvvisaturaAppIO().getPromemoriaAvviso().getOggetto() != null) {
					tipoVersamento.setAvvisaturaAppIoPromemoriaAvvisoOggettoDefault(ConverterUtils.toJSON(entrataPost.getAvvisaturaAppIO().getPromemoriaAvviso().getOggetto(),null));
				}else {
					tipoVersamento.setAvvisaturaAppIoPromemoriaAvvisoOggettoDefault(null);
				}
				if(entrataPost.getAvvisaturaAppIO().getPromemoriaAvviso().getTipo() != null) {
					tipoVersamento.setAvvisaturaAppIoPromemoriaAvvisoTipoDefault(entrataPost.getAvvisaturaAppIO().getPromemoriaAvviso().getTipo());
				}else {
					tipoVersamento.setAvvisaturaAppIoPromemoriaAvvisoTipoDefault(null);
				}
				
				if(entrataPost.getAvvisaturaAppIO().getPromemoriaAvviso().getTipo() != null) {
					// valore tipo contabilita non valido
					if(TipoTemplateTrasformazione.fromValue(entrataPost.getAvvisaturaAppIO().getPromemoriaAvviso().getTipo()) == null) {
						throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" +
								entrataPost.getAvvisaturaAppIO().getPromemoriaAvviso().getTipo() + "] valori possibili " + ArrayUtils.toString(TipoTemplateTrasformazione.values()));
					}
				}
			}
			
			if(entrataPost.getAvvisaturaAppIO().getPromemoriaRicevuta() != null) {
				if(entrataPost.getAvvisaturaAppIO().getPromemoriaRicevuta().Abilitato() != null) {
					
					tipoVersamento.setAvvisaturaAppIoPromemoriaRicevutaAbilitatoDefault(entrataPost.getAvvisaturaAppIO().getPromemoriaRicevuta().Abilitato());
				}else {
					tipoVersamento.setAvvisaturaAppIoPromemoriaRicevutaAbilitatoDefault(false);
				}

				if(entrataPost.getAvvisaturaAppIO().getPromemoriaRicevuta().getMessaggio() != null) {
					tipoVersamento.setAvvisaturaAppIoPromemoriaRicevutaMessaggioDefault(ConverterUtils.toJSON(entrataPost.getAvvisaturaAppIO().getPromemoriaRicevuta().getMessaggio(),null));
				}else {
					tipoVersamento.setAvvisaturaAppIoPromemoriaRicevutaMessaggioDefault(null);
				}
				if(entrataPost.getAvvisaturaAppIO().getPromemoriaRicevuta().getOggetto() != null) {
					tipoVersamento.setAvvisaturaAppIoPromemoriaRicevutaOggettoDefault(ConverterUtils.toJSON(entrataPost.getAvvisaturaAppIO().getPromemoriaRicevuta().getOggetto(),null));
				}else {
					tipoVersamento.setAvvisaturaAppIoPromemoriaRicevutaOggettoDefault(null);
				}
				if(entrataPost.getAvvisaturaAppIO().getPromemoriaRicevuta().getTipo() != null) {
					tipoVersamento.setAvvisaturaAppIoPromemoriaRicevutaTipoDefault(entrataPost.getAvvisaturaAppIO().getPromemoriaRicevuta().getTipo());
				}else {
					tipoVersamento.setAvvisaturaAppIoPromemoriaRicevutaTipoDefault(null);
				}
				if(entrataPost.getAvvisaturaAppIO().getPromemoriaRicevuta().SoloEseguiti() != null) {
					tipoVersamento.setAvvisaturaAppIoPromemoriaRicevutaInviaSoloEseguitiDefault(entrataPost.getAvvisaturaAppIO().getPromemoriaRicevuta().SoloEseguiti());
				}else {
					tipoVersamento.setAvvisaturaAppIoPromemoriaRicevutaInviaSoloEseguitiDefault(null);
				}
				
				if(entrataPost.getAvvisaturaAppIO().getPromemoriaRicevuta().getTipo() != null) {
					// valore tipo contabilita non valido
					if(TipoTemplateTrasformazione.fromValue(entrataPost.getAvvisaturaAppIO().getPromemoriaRicevuta().getTipo()) == null) {
						throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" +
								entrataPost.getAvvisaturaAppIO().getPromemoriaRicevuta().getTipo() + "] valori possibili " + ArrayUtils.toString(TipoTemplateTrasformazione.values()));
					}
				}
			}
			
			if(entrataPost.getAvvisaturaAppIO().getPromemoriaScadenza() != null) {
				if(entrataPost.getAvvisaturaAppIO().getPromemoriaScadenza().Abilitato() != null) {
					
					tipoVersamento.setAvvisaturaAppIoPromemoriaScadenzaAbilitatoDefault(entrataPost.getAvvisaturaAppIO().getPromemoriaScadenza().Abilitato());
				}else {
					tipoVersamento.setAvvisaturaAppIoPromemoriaScadenzaAbilitatoDefault(false);
				}

				if(entrataPost.getAvvisaturaAppIO().getPromemoriaScadenza().getMessaggio() != null) {
					tipoVersamento.setAvvisaturaAppIoPromemoriaScadenzaMessaggioDefault(ConverterUtils.toJSON(entrataPost.getAvvisaturaAppIO().getPromemoriaScadenza().getMessaggio(),null));
				}else {
					tipoVersamento.setAvvisaturaAppIoPromemoriaScadenzaMessaggioDefault(null);
				}
				if(entrataPost.getAvvisaturaAppIO().getPromemoriaScadenza().getOggetto() != null) {
					tipoVersamento.setAvvisaturaAppIoPromemoriaScadenzaOggettoDefault(ConverterUtils.toJSON(entrataPost.getAvvisaturaAppIO().getPromemoriaScadenza().getOggetto(),null));
				}else {
					tipoVersamento.setAvvisaturaAppIoPromemoriaScadenzaOggettoDefault(null);
				}
				if(entrataPost.getAvvisaturaAppIO().getPromemoriaScadenza().getPreavviso() != null) {
					tipoVersamento.setAvvisaturaAppIoPromemoriaScadenzaPreavvisoDefault(entrataPost.getAvvisaturaAppIO().getPromemoriaScadenza().getPreavviso());
				}else {
					tipoVersamento.setAvvisaturaAppIoPromemoriaScadenzaPreavvisoDefault(null);
				}
				if(entrataPost.getAvvisaturaAppIO().getPromemoriaScadenza().getTipo() != null) {
					tipoVersamento.setAvvisaturaAppIoPromemoriaScadenzaTipoDefault(entrataPost.getAvvisaturaAppIO().getPromemoriaScadenza().getTipo());
				}else {
					tipoVersamento.setAvvisaturaAppIoPromemoriaScadenzaTipoDefault(null);
				}
				
				
				if(entrataPost.getAvvisaturaAppIO().getPromemoriaScadenza().getTipo() != null) {
					// valore tipo contabilita non valido
					if(TipoTemplateTrasformazione.fromValue(entrataPost.getAvvisaturaAppIO().getPromemoriaScadenza().getTipo()) == null) {
						throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" +
								entrataPost.getAvvisaturaAppIO().getPromemoriaScadenza().getTipo() + "] valori possibili " + ArrayUtils.toString(TipoTemplateTrasformazione.values()));
					}
				}
			}
		}
		
		return entrataDTO;		
	}
	
	public static TipoPendenza toTipoPendenzaRsModel(it.govpay.model.TipoVersamento tipoVersamento) {
		TipoPendenza rsModel = new TipoPendenza();
		
		rsModel.descrizione(tipoVersamento.getDescrizione())
		.idTipoPendenza(tipoVersamento.getCodTipoVersamento()).codificaIUV(tipoVersamento.getCodificaIuvDefault())
		.abilitato(tipoVersamento.isAbilitatoDefault());
		
		rsModel.setPagaTerzi(tipoVersamento.getPagaTerziDefault());
		
		// Caricamento Pendenze Portale Backoffice
		TipoPendenzaPortaleBackofficeCaricamentoPendenze portaleBackoffice = new TipoPendenzaPortaleBackofficeCaricamentoPendenze();
		portaleBackoffice.setAbilitato(tipoVersamento.isCaricamentoPendenzePortaleBackofficeAbilitatoDefault());
		
		if(tipoVersamento.getCaricamentoPendenzePortaleBackofficeFormDefinizioneDefault() != null && tipoVersamento.getCaricamentoPendenzePortaleBackofficeFormTipoDefault() != null) {
			TipoPendenzaFormPortaleBackoffice form = new TipoPendenzaFormPortaleBackoffice();
			form.setTipo(tipoVersamento.getCaricamentoPendenzePortaleBackofficeFormTipoDefault());
			form.setDefinizione(new RawObject(tipoVersamento.getCaricamentoPendenzePortaleBackofficeFormDefinizioneDefault())); 
			portaleBackoffice.setForm(form);
		}
		
		if(tipoVersamento.getCaricamentoPendenzePortaleBackofficeTrasformazioneTipoDefault() != null && tipoVersamento.getCaricamentoPendenzePortaleBackofficeTrasformazioneDefinizioneDefault() != null) {
			TipoPendenzaTrasformazione trasformazione  = new TipoPendenzaTrasformazione();
			trasformazione.setTipo(tipoVersamento.getCaricamentoPendenzePortaleBackofficeTrasformazioneTipoDefault());
			trasformazione.setDefinizione(new RawObject(tipoVersamento.getCaricamentoPendenzePortaleBackofficeTrasformazioneDefinizioneDefault())); 
			portaleBackoffice.setTrasformazione(trasformazione);
		}
		if(tipoVersamento.getCaricamentoPendenzePortaleBackofficeValidazioneDefinizioneDefault() != null)
			portaleBackoffice.setValidazione(new RawObject(tipoVersamento.getCaricamentoPendenzePortaleBackofficeValidazioneDefinizioneDefault()));
		
		portaleBackoffice.setInoltro(tipoVersamento.getCaricamentoPendenzePortaleBackofficeCodApplicazioneDefault());
		
		rsModel.setPortaleBackoffice(portaleBackoffice);
		
		// Caricamento Pendenze Portale Pagamento
		TipoPendenzaPortalePagamentiCaricamentoPendenze portalePagamento = new TipoPendenzaPortalePagamentiCaricamentoPendenze();
		portalePagamento.setAbilitato(tipoVersamento.isCaricamentoPendenzePortalePagamentoAbilitatoDefault());
		
		if(tipoVersamento.getCaricamentoPendenzePortalePagamentoFormDefinizioneDefault() != null && tipoVersamento.getCaricamentoPendenzePortalePagamentoFormTipoDefault() != null) {
			TipoPendenzaFormPortalePagamenti form = new TipoPendenzaFormPortalePagamenti();
			form.setTipo(tipoVersamento.getCaricamentoPendenzePortalePagamentoFormTipoDefault());
			form.setDefinizione(new RawObject(tipoVersamento.getCaricamentoPendenzePortalePagamentoFormDefinizioneDefault())); 
			if(tipoVersamento.getCaricamentoPendenzePortalePagamentoFormImpaginazioneDefault() !=null)
				form.setImpaginazione(new RawObject(tipoVersamento.getCaricamentoPendenzePortalePagamentoFormImpaginazioneDefault()));
			portalePagamento.setForm(form);
		}
		
		if(tipoVersamento.getCaricamentoPendenzePortalePagamentoTrasformazioneTipoDefault() != null && tipoVersamento.getCaricamentoPendenzePortalePagamentoTrasformazioneDefinizioneDefault() != null) {
			TipoPendenzaTrasformazione trasformazione  = new TipoPendenzaTrasformazione();
			trasformazione.setTipo(tipoVersamento.getCaricamentoPendenzePortalePagamentoTrasformazioneTipoDefault());
			trasformazione.setDefinizione(new RawObject(tipoVersamento.getCaricamentoPendenzePortalePagamentoTrasformazioneDefinizioneDefault())); 
			portalePagamento.setTrasformazione(trasformazione);
		}
		if(tipoVersamento.getCaricamentoPendenzePortalePagamentoValidazioneDefinizioneDefault() != null)
			portalePagamento.setValidazione(new RawObject(tipoVersamento.getCaricamentoPendenzePortalePagamentoValidazioneDefinizioneDefault()));
		
		portalePagamento.setInoltro(tipoVersamento.getCaricamentoPendenzePortalePagamentoCodApplicazioneDefault());
		
		rsModel.setPortalePagamento(portalePagamento);
		
		// Avvisatura via mail 
		
		TipoPendenzaAvvisaturaMail avvisaturaMail = new TipoPendenzaAvvisaturaMail();
		
		TipoPendenzaAvvisaturaMailPromemoriaAvviso avvisaturaMailPromemoriaAvviso = new TipoPendenzaAvvisaturaMailPromemoriaAvviso();
		avvisaturaMailPromemoriaAvviso.setAbilitato(tipoVersamento.isAvvisaturaMailPromemoriaAvvisoAbilitatoDefault());
		
		if(tipoVersamento.getAvvisaturaMailPromemoriaAvvisoOggettoDefault() != null)
			avvisaturaMailPromemoriaAvviso.setOggetto(new RawObject(tipoVersamento.getAvvisaturaMailPromemoriaAvvisoOggettoDefault()));
		if(tipoVersamento.getAvvisaturaMailPromemoriaAvvisoMessaggioDefault() != null)
			avvisaturaMailPromemoriaAvviso.setMessaggio(new RawObject(tipoVersamento.getAvvisaturaMailPromemoriaAvvisoMessaggioDefault()));
		avvisaturaMailPromemoriaAvviso.setAllegaPdf(tipoVersamento.getAvvisaturaMailPromemoriaAvvisoPdfDefault());
		avvisaturaMailPromemoriaAvviso.setTipo(tipoVersamento.getAvvisaturaMailPromemoriaAvvisoTipoDefault());
		
		avvisaturaMail.setPromemoriaAvviso(avvisaturaMailPromemoriaAvviso);
		
		TipoPendenzaAvvisaturaMailPromemoriaRicevuta avvisaturaMailPromemoriaRicevuta = new TipoPendenzaAvvisaturaMailPromemoriaRicevuta();
		avvisaturaMailPromemoriaRicevuta.setAbilitato(tipoVersamento.isAvvisaturaMailPromemoriaRicevutaAbilitatoDefault());
		
		if(tipoVersamento.getAvvisaturaMailPromemoriaRicevutaOggettoDefault() != null)
			avvisaturaMailPromemoriaRicevuta.setOggetto(new RawObject(tipoVersamento.getAvvisaturaMailPromemoriaRicevutaOggettoDefault()));
		if(tipoVersamento.getAvvisaturaMailPromemoriaRicevutaMessaggioDefault() != null)
			avvisaturaMailPromemoriaRicevuta.setMessaggio(new RawObject(tipoVersamento.getAvvisaturaMailPromemoriaRicevutaMessaggioDefault()));
		avvisaturaMailPromemoriaRicevuta.setAllegaPdf(tipoVersamento.getAvvisaturaMailPromemoriaRicevutaPdfDefault());
		avvisaturaMailPromemoriaRicevuta.setSoloEseguiti(tipoVersamento.getAvvisaturaMailPromemoriaRicevutaInviaSoloEseguitiDefault());
		avvisaturaMailPromemoriaRicevuta.setTipo(tipoVersamento.getAvvisaturaMailPromemoriaRicevutaTipoDefault());
		
		avvisaturaMail.setPromemoriaRicevuta(avvisaturaMailPromemoriaRicevuta);

		TipoPendenzaAvvisaturaPromemoriaScadenza avvisaturaMailPromemoriaScadenza = new TipoPendenzaAvvisaturaPromemoriaScadenza();
		avvisaturaMailPromemoriaScadenza.setAbilitato(tipoVersamento.isAvvisaturaMailPromemoriaScadenzaAbilitatoDefault());
		
		if(tipoVersamento.getAvvisaturaMailPromemoriaScadenzaOggettoDefault() != null)
			avvisaturaMailPromemoriaScadenza.setOggetto(new RawObject(tipoVersamento.getAvvisaturaMailPromemoriaScadenzaOggettoDefault()));
		if(tipoVersamento.getAvvisaturaMailPromemoriaScadenzaMessaggioDefault() != null)
			avvisaturaMailPromemoriaScadenza.setMessaggio(new RawObject(tipoVersamento.getAvvisaturaMailPromemoriaScadenzaMessaggioDefault()));
		avvisaturaMailPromemoriaScadenza.setPreavviso(tipoVersamento.getAvvisaturaMailPromemoriaScadenzaPreavvisoDefault());
		avvisaturaMailPromemoriaScadenza.setTipo(tipoVersamento.getAvvisaturaMailPromemoriaScadenzaTipoDefault());
		
		avvisaturaMail.setPromemoriaScadenza(avvisaturaMailPromemoriaScadenza);
		
		rsModel.setAvvisaturaMail(avvisaturaMail);
		
		

		
		

		// Visualizzazione Custom della pendenza
		if(tipoVersamento.getVisualizzazioneDefinizioneDefault() != null)
			rsModel.setVisualizzazione(new RawObject(tipoVersamento.getVisualizzazioneDefinizioneDefault()));
		
		// Configurazione conversione CSV
		if(tipoVersamento.getTracciatoCsvTipoDefault() != null &&  
				tipoVersamento.getTracciatoCsvIntestazioneDefault() != null && 
				tipoVersamento.getTracciatoCsvRichiestaDefault() != null && 
				tipoVersamento.getTracciatoCsvRispostaDefault() != null) {
			TracciatoCsv tracciatoCsv = new TracciatoCsv();
			tracciatoCsv.setTipo(tipoVersamento.getTracciatoCsvTipoDefault());
			tracciatoCsv.setIntestazione(tipoVersamento.getTracciatoCsvIntestazioneDefault());
			tracciatoCsv.setRichiesta(new RawObject(tipoVersamento.getTracciatoCsvRichiestaDefault()));
			tracciatoCsv.setRisposta(new RawObject(tipoVersamento.getTracciatoCsvRispostaDefault()));
			rsModel.setTracciatoCsv(tracciatoCsv);
		}
		
		// Avvisatura via AppIO 
		
		TipoPendenzaAvvisaturaAppIO avvisaturaAppIO = new TipoPendenzaAvvisaturaAppIO();
		
		TipoPendenzaAvvisaturaPromemoriaAvvisoBase avvisaturaAppIOPromemoriaAvviso = new TipoPendenzaAvvisaturaPromemoriaAvvisoBase();
		avvisaturaAppIOPromemoriaAvviso.setAbilitato(tipoVersamento.isAvvisaturaAppIoPromemoriaAvvisoAbilitatoDefault());
		
		if(tipoVersamento.getAvvisaturaAppIoPromemoriaAvvisoOggettoDefault() != null)
			avvisaturaAppIOPromemoriaAvviso.setOggetto(new RawObject(tipoVersamento.getAvvisaturaAppIoPromemoriaAvvisoOggettoDefault()));
		if(tipoVersamento.getAvvisaturaAppIoPromemoriaAvvisoMessaggioDefault() != null)
			avvisaturaAppIOPromemoriaAvviso.setMessaggio(new RawObject(tipoVersamento.getAvvisaturaAppIoPromemoriaAvvisoMessaggioDefault()));
		avvisaturaAppIOPromemoriaAvviso.setTipo(tipoVersamento.getAvvisaturaAppIoPromemoriaAvvisoTipoDefault());
		
		avvisaturaAppIO.setPromemoriaAvviso(avvisaturaAppIOPromemoriaAvviso);
		
		TipoPendenzaAvvisaturaPromemoriaRicevutaBase avvisaturaAppIOPromemoriaRicevuta = new TipoPendenzaAvvisaturaPromemoriaRicevutaBase();
		avvisaturaAppIOPromemoriaRicevuta.setAbilitato(tipoVersamento.isAvvisaturaAppIoPromemoriaRicevutaAbilitatoDefault());
		
		if(tipoVersamento.getAvvisaturaAppIoPromemoriaRicevutaOggettoDefault() != null)
			avvisaturaAppIOPromemoriaRicevuta.setOggetto(new RawObject(tipoVersamento.getAvvisaturaAppIoPromemoriaRicevutaOggettoDefault()));
		if(tipoVersamento.getAvvisaturaAppIoPromemoriaRicevutaMessaggioDefault() != null)
			avvisaturaAppIOPromemoriaRicevuta.setMessaggio(new RawObject(tipoVersamento.getAvvisaturaAppIoPromemoriaRicevutaMessaggioDefault()));
		avvisaturaAppIOPromemoriaRicevuta.setSoloEseguiti(tipoVersamento.getAvvisaturaAppIoPromemoriaRicevutaInviaSoloEseguitiDefault());
		avvisaturaAppIOPromemoriaRicevuta.setTipo(tipoVersamento.getAvvisaturaAppIoPromemoriaRicevutaTipoDefault());
		
		avvisaturaAppIO.setPromemoriaRicevuta(avvisaturaAppIOPromemoriaRicevuta);

		TipoPendenzaAvvisaturaPromemoriaScadenza avvisaturaAppIOPromemoriaScadenza = new TipoPendenzaAvvisaturaPromemoriaScadenza();
		avvisaturaAppIOPromemoriaScadenza.setAbilitato(tipoVersamento.isAvvisaturaAppIoPromemoriaScadenzaAbilitatoDefault());
		
		if(tipoVersamento.getAvvisaturaAppIoPromemoriaScadenzaOggettoDefault() != null)
			avvisaturaAppIOPromemoriaScadenza.setOggetto(new RawObject(tipoVersamento.getAvvisaturaAppIoPromemoriaScadenzaOggettoDefault()));
		if(tipoVersamento.getAvvisaturaAppIoPromemoriaScadenzaMessaggioDefault() != null)
			avvisaturaAppIOPromemoriaScadenza.setMessaggio(new RawObject(tipoVersamento.getAvvisaturaAppIoPromemoriaScadenzaMessaggioDefault()));
		avvisaturaAppIOPromemoriaScadenza.setPreavviso(tipoVersamento.getAvvisaturaAppIoPromemoriaScadenzaPreavvisoDefault());
		avvisaturaAppIOPromemoriaScadenza.setTipo(tipoVersamento.getAvvisaturaAppIoPromemoriaScadenzaTipoDefault());
		
		avvisaturaAppIO.setPromemoriaScadenza(avvisaturaAppIOPromemoriaScadenza);
		
		rsModel.setAvvisaturaAppIO(avvisaturaAppIO);
		
		return rsModel;
	}
	
	public static TipoPendenzaIndex toTipoPendenzaRsModelIndex(TipoVersamentoDominio tipoVersamentoDominio) {
		TipoPendenzaIndex rsModel = new TipoPendenzaIndex();
		
		rsModel.descrizione(tipoVersamentoDominio.getDescrizione())
		.idTipoPendenza(tipoVersamentoDominio.getCodTipoVersamento());
		
		rsModel.setVisualizzazione(new RawObject(tipoVersamentoDominio.getVisualizzazioneDefinizione()));
		
		return rsModel;
	}
	
	public static TipoPendenzaIndex toTipoPendenzaRsModelIndex(it.govpay.model.TipoVersamento tipoVersamento) {
		TipoPendenzaIndex rsModel = new TipoPendenzaIndex();
		
		rsModel.descrizione(tipoVersamento.getDescrizione())
		.idTipoPendenza(tipoVersamento.getCodTipoVersamento());
		
		rsModel.setVisualizzazione(new RawObject(tipoVersamento.getVisualizzazioneDefinizioneDefault()));
		
		return rsModel;
	}
}
