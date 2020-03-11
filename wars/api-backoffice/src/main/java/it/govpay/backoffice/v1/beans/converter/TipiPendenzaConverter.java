package it.govpay.backoffice.v1.beans.converter;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.jaxrs.RawObject;
import org.openspcoop2.utils.json.ValidationException;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.ConfigurazioneGenerazioneMessageAppIO;
import it.govpay.backoffice.v1.beans.TipoPendenza;
import it.govpay.backoffice.v1.beans.TipoPendenzaForm;
import it.govpay.backoffice.v1.beans.TipoPendenzaIndex;
import it.govpay.backoffice.v1.beans.TipoPendenzaPost;
import it.govpay.backoffice.v1.beans.TipoPendenzaPromemoria;
import it.govpay.backoffice.v1.beans.TipoPendenzaTipologia;
import it.govpay.backoffice.v1.beans.TipoPendenzaTrasformazione;
import it.govpay.backoffice.v1.beans.TipoPendenzaTrasformazione.TipoEnum;
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
		if(entrataPost.getTipo() != null) {
			
			// valore tipo contabilita non valido
			if(TipoPendenzaTipologia.fromValue(entrataPost.getTipo()) == null) {
				throw new ValidationException("Codifica inesistente per tipo. Valore fornito [" + entrataPost.getTipo() + "] valori possibili " + ArrayUtils.toString(TipoPendenzaTipologia.values()));
			}
			
			entrataPost.setTipoEnum(TipoPendenzaTipologia.fromValue(entrataPost.getTipo()));

			switch (entrataPost.getTipoEnum()) {
			case DOVUTO:
				tipoVersamento.setTipoDefault(it.govpay.model.TipoVersamento.Tipo.DOVUTO);
				break;
			case SPONTANEO:
				tipoVersamento.setTipoDefault(it.govpay.model.TipoVersamento.Tipo.SPONTANEO);
				break;
			}
		}

		entrataDTO.setCodTipoVersamento(idTipoPendenza);
		entrataDTO.setTipoVersamento(tipoVersamento);
		
		tipoVersamento.setPagaTerziDefault(entrataPost.PagaTerzi());
		tipoVersamento.setAbilitatoDefault(entrataPost.Abilitato());
		
		if(entrataPost.getForm() != null && entrataPost.getForm().getDefinizione() != null && entrataPost.getForm().getTipo() != null) {
			Object definizione = entrataPost.getForm().getDefinizione();
			tipoVersamento.setCaricamentoPendenzePortaleBackofficeFormDefinizioneDefault(ConverterUtils.toJSON(definizione,null));
			tipoVersamento.setCaricamentoPendenzePortaleBackofficeFormTipoDefault(entrataPost.getForm().getTipo());
		}
		
		tipoVersamento.setAvvisaturaMailPromemoriaAvvisoAbilitatoDefault(false);
		if(entrataPost.getPromemoriaAvviso() != null) {
			
			if(entrataPost.getPromemoriaAvviso().Abilitato() != null) {
				tipoVersamento.setAvvisaturaMailPromemoriaAvvisoAbilitatoDefault(entrataPost.getPromemoriaAvviso().Abilitato());
			}else {
				tipoVersamento.setAvvisaturaMailPromemoriaAvvisoAbilitatoDefault(false);
			}

			if(entrataPost.getPromemoriaAvviso().getMessaggio() != null) {
				tipoVersamento.setAvvisaturaMailPromemoriaAvvisoMessaggioDefault(ConverterUtils.toJSON(entrataPost.getPromemoriaAvviso().getMessaggio(),null));
			}else {
				tipoVersamento.setAvvisaturaMailPromemoriaAvvisoMessaggioDefault(null);
			}
			if(entrataPost.getPromemoriaAvviso().getOggetto() != null) {
				tipoVersamento.setAvvisaturaMailPromemoriaAvvisoOggettoDefault(ConverterUtils.toJSON(entrataPost.getPromemoriaAvviso().getOggetto(),null));
			}else {
				tipoVersamento.setAvvisaturaMailPromemoriaAvvisoOggettoDefault(null);
			}
			if(entrataPost.getPromemoriaAvviso().AllegaPdf() != null) {
				tipoVersamento.setAvvisaturaMailPromemoriaAvvisoPdfDefault(entrataPost.getPromemoriaAvviso().AllegaPdf());
			}else {
				tipoVersamento.setAvvisaturaMailPromemoriaAvvisoPdfDefault(null);
			}
			if(entrataPost.getPromemoriaAvviso().getTipo() != null) {
				tipoVersamento.setAvvisaturaMailPromemoriaAvvisoTipoDefault(entrataPost.getPromemoriaAvviso().getTipo());
			}else {
				tipoVersamento.setAvvisaturaMailPromemoriaAvvisoTipoDefault(null);
			}
			
			
			if(entrataPost.getPromemoriaAvviso().getTipo() != null) {
				// valore tipo contabilita non valido
				if(it.govpay.backoffice.v1.beans.TipoPendenzaPromemoria.TipoEnum.fromValue(entrataPost.getPromemoriaAvviso().getTipo()) == null) {
					throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" +
							entrataPost.getPromemoriaAvviso().getTipo() + "] valori possibili " + ArrayUtils.toString(it.govpay.backoffice.v1.beans.TipoPendenzaPromemoria.TipoEnum.values()));
				}
			}
		}
		
		tipoVersamento.setAvvisaturaMailPromemoriaRicevutaAbilitatoDefault(false);
		if(entrataPost.getPromemoriaRicevuta() != null) {
			
			if(entrataPost.getPromemoriaRicevuta().Abilitato() != null) {
				tipoVersamento.setAvvisaturaMailPromemoriaRicevutaAbilitatoDefault(entrataPost.getPromemoriaRicevuta().Abilitato());
			}else {
				tipoVersamento.setAvvisaturaMailPromemoriaRicevutaAbilitatoDefault(false);
			}

			if(entrataPost.getPromemoriaRicevuta().getMessaggio() != null) {
				tipoVersamento.setAvvisaturaMailPromemoriaRicevutaMessaggioDefault(ConverterUtils.toJSON(entrataPost.getPromemoriaRicevuta().getMessaggio(),null));
			}else {
				tipoVersamento.setAvvisaturaMailPromemoriaRicevutaMessaggioDefault(null);
			}

			if(entrataPost.getPromemoriaRicevuta().getOggetto() != null) {
				tipoVersamento.setAvvisaturaMailPromemoriaRicevutaOggettoDefault(ConverterUtils.toJSON(entrataPost.getPromemoriaRicevuta().getOggetto(),null));
			}else {
				tipoVersamento.setAvvisaturaMailPromemoriaRicevutaOggettoDefault(null);
			}

			if(entrataPost.getPromemoriaRicevuta().AllegaPdf() != null) {
				tipoVersamento.setAvvisaturaMailPromemoriaRicevutaPdfDefault(entrataPost.getPromemoriaRicevuta().AllegaPdf());
			}else {
				tipoVersamento.setAvvisaturaMailPromemoriaRicevutaPdfDefault(null);
			}

			if(entrataPost.getPromemoriaRicevuta().getTipo() != null) {
				tipoVersamento.setAvvisaturaMailPromemoriaRicevutaTipoDefault(entrataPost.getPromemoriaRicevuta().getTipo());
			}else {
				tipoVersamento.setAvvisaturaMailPromemoriaRicevutaTipoDefault(null);
			}
			
			
			if(entrataPost.getPromemoriaRicevuta().getTipo() != null) {
				// valore tipo contabilita non valido
				if(it.govpay.backoffice.v1.beans.TipoPendenzaPromemoria.TipoEnum.fromValue(entrataPost.getPromemoriaRicevuta().getTipo()) == null) {
					throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" +
							entrataPost.getPromemoriaRicevuta().getTipo() + "] valori possibili " + ArrayUtils.toString(it.govpay.backoffice.v1.beans.TipoPendenzaPromemoria.TipoEnum.values()));
				}
			}
		}
		
		if(entrataPost.getTrasformazione() != null  && entrataPost.getTrasformazione().getDefinizione() != null && entrataPost.getTrasformazione().getTipo() != null) {
			if(entrataPost.getTrasformazione().getTipo() != null) {
				// valore tipo contabilita non valido
				if(TipoEnum.fromValue(entrataPost.getTrasformazione().getTipo()) == null) {
					throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" + entrataPost.getTrasformazione().getTipo() + "] valori possibili " + ArrayUtils.toString(TipoEnum.values()));
				}
			}
			
			Object definizione = entrataPost.getTrasformazione().getDefinizione();
			tipoVersamento.setCaricamentoPendenzePortaleBackofficeTrasformazioneDefinizioneDefault(ConverterUtils.toJSON(definizione,null));
			tipoVersamento.setCaricamentoPendenzePortaleBackofficeTrasformazioneTipoDefault(entrataPost.getTrasformazione().getTipo());
		}
		if(entrataPost.getValidazione() != null)
			tipoVersamento.setCaricamentoPendenzePortaleBackofficeValidazioneDefinizioneDefault(ConverterUtils.toJSON(entrataPost.getValidazione(),null));
		
		if(entrataPost.getInoltro() != null)
			tipoVersamento.setCaricamentoPendenzePortaleBackofficeCodApplicazioneDefault(entrataPost.getInoltro());
		
		if(entrataPost.getVisualizzazione() != null)
			tipoVersamento.setVisualizzazioneDefinizioneDefault(ConverterUtils.toJSON(entrataPost.getVisualizzazione(),null));
		
		if(entrataPost.getTracciatoCsv() != null
				&& entrataPost.getTracciatoCsv().getTipo() != null
				&& entrataPost.getTracciatoCsv().getIntestazione() != null
				&& entrataPost.getTracciatoCsv().getRichiesta() != null
				&& entrataPost.getTracciatoCsv().getRisposta() != null) {
			tipoVersamento.setTracciatoCsvTipoDefault(entrataPost.getTracciatoCsv().getTipo());
			
			// valore tipo contabilita non valido
			if(it.govpay.backoffice.v1.beans.TracciatoCsv.TipoEnum.fromValue(entrataPost.getTracciatoCsv().getTipo()) == null) {
				throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" +
						entrataPost.getTracciatoCsv().getTipo() + "] valori possibili " + ArrayUtils.toString(it.govpay.backoffice.v1.beans.TracciatoCsv.TipoEnum.values()));
			}
			
			tipoVersamento.setTracciatoCsvIntestazioneDefault(entrataPost.getTracciatoCsv().getIntestazione());
			tipoVersamento.setTracciatoCsvRichiestaDefault(ConverterUtils.toJSON(entrataPost.getTracciatoCsv().getRichiesta(),null));
			tipoVersamento.setTracciatoCsvRispostaDefault(ConverterUtils.toJSON(entrataPost.getTracciatoCsv().getRisposta(),null));
		}
		
		if(entrataPost.getAppIO() != null &&  entrataPost.getAppIO().getTipo() != null && entrataPost.getAppIO().getSubject() != null && 
				entrataPost.getAppIO().getBody() != null ) {
			
			tipoVersamento.setAvvisaturaAppIoPromemoriaAvvisoTipoDefault(entrataPost.getAppIO().getTipo());
			
			// valore tipo contabilita non valido
			if(it.govpay.backoffice.v1.beans.ConfigurazioneGenerazioneMessageAppIO.TipoEnum.fromValue(entrataPost.getAppIO().getTipo()) == null) {
				throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" +
						entrataPost.getAppIO().getTipo() + "] valori possibili " + ArrayUtils.toString(it.govpay.backoffice.v1.beans.ConfigurazioneGenerazioneMessageAppIO.TipoEnum.values()));
			}
						
			tipoVersamento.setAvvisaturaAppIoPromemoriaAvvisoMessaggioDefault(ConverterUtils.toJSON(entrataPost.getAppIO().getBody(),null));
			tipoVersamento.setAvvisaturaAppIoPromemoriaAvvisoOggettoDefault(ConverterUtils.toJSON(entrataPost.getAppIO().getSubject(),null));
		}
		
		return entrataDTO;		
	}
	
	public static TipoPendenza toTipoPendenzaRsModel(it.govpay.model.TipoVersamento tipoVersamento) {
		TipoPendenza rsModel = new TipoPendenza();
		
		rsModel.descrizione(tipoVersamento.getDescrizione())
		.idTipoPendenza(tipoVersamento.getCodTipoVersamento()).codificaIUV(tipoVersamento.getCodificaIuvDefault())
		.abilitato(tipoVersamento.isAbilitatoDefault());
		
		if(tipoVersamento.getTipoDefault() != null) {
			switch (tipoVersamento.getTipoDefault()) {
			case DOVUTO:
				rsModel.setTipo(it.govpay.backoffice.v1.beans.TipoPendenzaTipologia.DOVUTO);
				break;
			case SPONTANEO:
				rsModel.setTipo(it.govpay.backoffice.v1.beans.TipoPendenzaTipologia.SPONTANEO);
				break;
			}
		}
		
		rsModel.setPagaTerzi(tipoVersamento.getPagaTerziDefault());
		
		if(tipoVersamento.getCaricamentoPendenzePortaleBackofficeFormDefinizioneDefault() != null && tipoVersamento.getCaricamentoPendenzePortaleBackofficeFormTipoDefault() != null) {
			TipoPendenzaForm form = new TipoPendenzaForm();
			form.setTipo(tipoVersamento.getCaricamentoPendenzePortaleBackofficeFormTipoDefault());
			form.setDefinizione(new RawObject(tipoVersamento.getCaricamentoPendenzePortaleBackofficeFormDefinizioneDefault())); 
			rsModel.setForm(form);
		}
		
		TipoPendenzaPromemoria avvisaturaMailPromemoriaAvviso = new TipoPendenzaPromemoria();
		avvisaturaMailPromemoriaAvviso.setAbilitato(tipoVersamento.isAvvisaturaMailPromemoriaAvvisoAbilitatoDefault());
		
		if(tipoVersamento.getAvvisaturaMailPromemoriaAvvisoOggettoDefault() != null)
			avvisaturaMailPromemoriaAvviso.setOggetto(new RawObject(tipoVersamento.getAvvisaturaMailPromemoriaAvvisoOggettoDefault()));
		if(tipoVersamento.getAvvisaturaMailPromemoriaAvvisoMessaggioDefault() != null)
			avvisaturaMailPromemoriaAvviso.setMessaggio(new RawObject(tipoVersamento.getAvvisaturaMailPromemoriaAvvisoMessaggioDefault()));
		avvisaturaMailPromemoriaAvviso.setAllegaPdf(tipoVersamento.getAvvisaturaMailPromemoriaAvvisoPdfDefault());
		avvisaturaMailPromemoriaAvviso.setTipo(tipoVersamento.getAvvisaturaMailPromemoriaAvvisoTipoDefault());
		
		rsModel.setPromemoriaAvviso(avvisaturaMailPromemoriaAvviso);
		
		TipoPendenzaPromemoria avvisaturaMailPromemoriaRicevuta = new TipoPendenzaPromemoria();
		avvisaturaMailPromemoriaRicevuta.setAbilitato(tipoVersamento.isAvvisaturaMailPromemoriaRicevutaAbilitatoDefault());
		
		if(tipoVersamento.getAvvisaturaMailPromemoriaRicevutaOggettoDefault() != null)
			avvisaturaMailPromemoriaRicevuta.setOggetto(new RawObject(tipoVersamento.getAvvisaturaMailPromemoriaRicevutaOggettoDefault()));
		if(tipoVersamento.getAvvisaturaMailPromemoriaRicevutaMessaggioDefault() != null)
			avvisaturaMailPromemoriaRicevuta.setMessaggio(new RawObject(tipoVersamento.getAvvisaturaMailPromemoriaRicevutaMessaggioDefault()));
		avvisaturaMailPromemoriaRicevuta.setAllegaPdf(tipoVersamento.getAvvisaturaMailPromemoriaRicevutaPdfDefault());
		avvisaturaMailPromemoriaRicevuta.setTipo(tipoVersamento.getAvvisaturaMailPromemoriaRicevutaTipoDefault());
		
		
		rsModel.setPromemoriaRicevuta(avvisaturaMailPromemoriaRicevuta);
		
		
		if(tipoVersamento.getCaricamentoPendenzePortaleBackofficeTrasformazioneTipoDefault() != null && tipoVersamento.getCaricamentoPendenzePortaleBackofficeTrasformazioneDefinizioneDefault() != null) {
			TipoPendenzaTrasformazione trasformazione  = new TipoPendenzaTrasformazione();
			trasformazione.setTipo(tipoVersamento.getCaricamentoPendenzePortaleBackofficeTrasformazioneTipoDefault());
			trasformazione.setDefinizione(new RawObject(tipoVersamento.getCaricamentoPendenzePortaleBackofficeTrasformazioneDefinizioneDefault())); 
			rsModel.setTrasformazione(trasformazione);
		}
		if(tipoVersamento.getCaricamentoPendenzePortaleBackofficeValidazioneDefinizioneDefault() != null)
			rsModel.setValidazione(new RawObject(tipoVersamento.getCaricamentoPendenzePortaleBackofficeValidazioneDefinizioneDefault()));
		
		rsModel.setInoltro(tipoVersamento.getCaricamentoPendenzePortaleBackofficeCodApplicazioneDefault());
		
		if(tipoVersamento.getVisualizzazioneDefinizioneDefault() != null)
			rsModel.setVisualizzazione(new RawObject(tipoVersamento.getVisualizzazioneDefinizioneDefault()));
		
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
		
		if(tipoVersamento.getAvvisaturaAppIoPromemoriaAvvisoTipoDefault() != null && tipoVersamento.getAvvisaturaAppIoPromemoriaAvvisoMessaggioDefault() != null && 
				tipoVersamento.getAvvisaturaAppIoPromemoriaAvvisoOggettoDefault() != null ) {
			
			ConfigurazioneGenerazioneMessageAppIO appIO = new ConfigurazioneGenerazioneMessageAppIO();
			appIO.setTipo(tipoVersamento.getAvvisaturaAppIoPromemoriaAvvisoTipoDefault());
			appIO.setBody(tipoVersamento.getAvvisaturaAppIoPromemoriaAvvisoMessaggioDefault());
			appIO.setSubject(tipoVersamento.getAvvisaturaAppIoPromemoriaAvvisoOggettoDefault());
			rsModel.setAppIO(appIO );
		}
		
		return rsModel;
	}
	
	public static TipoPendenzaIndex toTipoPendenzaRsModelIndex(TipoVersamentoDominio tipoVersamentoDominio) {
		TipoPendenzaIndex rsModel = new TipoPendenzaIndex();
		
		rsModel.descrizione(tipoVersamentoDominio.getDescrizione())
		.idTipoPendenza(tipoVersamentoDominio.getCodTipoVersamento());
		
		if(tipoVersamentoDominio.getTipo() != null) {
			switch (tipoVersamentoDominio.getTipo()) {
			case DOVUTO:
				rsModel.setTipo(it.govpay.backoffice.v1.beans.TipoPendenzaTipologia.DOVUTO);
				break;
			case SPONTANEO:
				rsModel.setTipo(it.govpay.backoffice.v1.beans.TipoPendenzaTipologia.SPONTANEO);
				break;
			}
		}
		
		rsModel.setVisualizzazione(new RawObject(tipoVersamentoDominio.getVisualizzazioneDefinizione()));
		
		return rsModel;
	}
	
	public static TipoPendenzaIndex toTipoPendenzaRsModelIndex(it.govpay.model.TipoVersamento tipoVersamento) {
		TipoPendenzaIndex rsModel = new TipoPendenzaIndex();
		
		rsModel.descrizione(tipoVersamento.getDescrizione())
		.idTipoPendenza(tipoVersamento.getCodTipoVersamento());
		
		if(tipoVersamento.getTipoDefault() != null) {
			switch (tipoVersamento.getTipoDefault()) {
			case DOVUTO:
				rsModel.setTipo(it.govpay.backoffice.v1.beans.TipoPendenzaTipologia.DOVUTO);
				break;
			case SPONTANEO:
				rsModel.setTipo(it.govpay.backoffice.v1.beans.TipoPendenzaTipologia.SPONTANEO);
				break;
			}
		}
		
		rsModel.setVisualizzazione(new RawObject(tipoVersamento.getVisualizzazioneDefinizioneDefault()));
		
		return rsModel;
	}
}
