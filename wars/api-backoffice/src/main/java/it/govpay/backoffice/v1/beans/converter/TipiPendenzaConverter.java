package it.govpay.backoffice.v1.beans.converter;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.jaxrs.RawObject;
import org.openspcoop2.utils.json.ValidationException;
import org.springframework.security.core.Authentication;

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
		
		if(entrataPost.getCodificaIUV()!=null)
			tipoVersamento.setCodificaIuvDefault(entrataPost.getCodificaIUV().toString());
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
		
		if(entrataPost.getForm() != null) {
			Object definizione = entrataPost.getForm().getDefinizione();
//			if(definizione instanceof String)
//				tipoVersamento.setFormDefinizioneDefault((String) definizione);
//			else
				tipoVersamento.setFormDefinizioneDefault(ConverterUtils.toJSON(definizione,null));
			tipoVersamento.setFormTipoDefault(entrataPost.getForm().getTipo());
		}
		
		tipoVersamento.setPromemoriaAvvisoAbilitatoDefault(false);
		if(entrataPost.getPromemoriaAvviso() != null) {
			tipoVersamento.setPromemoriaAvvisoAbilitatoDefault(entrataPost.getPromemoriaAvviso().Abilitato());
			
//			if(entrataPost.getPromemoriaAvviso().Abilitato() && entrataPost.getPromemoriaAvviso().getTipo() != null
//					&& entrataPost.getPromemoriaAvviso().getMessaggio() != null
//					&& entrataPost.getPromemoriaAvviso().getOggetto() != null) {
			if(entrataPost.getPromemoriaAvviso().getTipo() != null) {
				// valore tipo contabilita non valido
				if(it.govpay.backoffice.v1.beans.TipoPendenzaPromemoria.TipoEnum.fromValue(entrataPost.getPromemoriaAvviso().getTipo()) == null) {
					throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" +
							entrataPost.getPromemoriaAvviso().getTipo() + "] valori possibili " + ArrayUtils.toString(it.govpay.backoffice.v1.beans.TipoPendenzaPromemoria.TipoEnum.values()));
				}
			}
			
			
			tipoVersamento.setPromemoriaAvvisoTipoDefault(entrataPost.getPromemoriaAvviso().getTipo());
			tipoVersamento.setPromemoriaAvvisoMessaggioDefault(ConverterUtils.toJSON(entrataPost.getPromemoriaAvviso().getMessaggio(),null));
			tipoVersamento.setPromemoriaAvvisoOggettoDefault(ConverterUtils.toJSON(entrataPost.getPromemoriaAvviso().getOggetto(),null));
			tipoVersamento.setPromemoriaAvvisoPdfDefault(entrataPost.getPromemoriaAvviso().AllegaPdf());
//			}
		}
		
		tipoVersamento.setPromemoriaRicevutaAbilitatoDefault(false);
		if(entrataPost.getPromemoriaRicevuta() != null) {
			tipoVersamento.setPromemoriaRicevutaAbilitatoDefault(entrataPost.getPromemoriaRicevuta().Abilitato());
			
			if(entrataPost.getPromemoriaRicevuta().getTipo() != null) {
				// valore tipo contabilita non valido
				if(it.govpay.backoffice.v1.beans.TipoPendenzaPromemoria.TipoEnum.fromValue(entrataPost.getPromemoriaRicevuta().getTipo()) == null) {
					throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" +
							entrataPost.getPromemoriaRicevuta().getTipo() + "] valori possibili " + ArrayUtils.toString(it.govpay.backoffice.v1.beans.TipoPendenzaPromemoria.TipoEnum.values()));
				}
			}
			
//			if(entrataPost.getPromemoriaRicevuta().Abilitato()  && entrataPost.getPromemoriaRicevuta().getTipo() != null
//					&& entrataPost.getPromemoriaRicevuta().getMessaggio() != null
//					&& entrataPost.getPromemoriaRicevuta().getOggetto() != null) {
			tipoVersamento.setPromemoriaRicevutaTipoDefault(entrataPost.getPromemoriaRicevuta().getTipo());
			tipoVersamento.setPromemoriaRicevutaMessaggioDefault(ConverterUtils.toJSON(entrataPost.getPromemoriaRicevuta().getMessaggio(),null));
			tipoVersamento.setPromemoriaRicevutaOggettoDefault(ConverterUtils.toJSON(entrataPost.getPromemoriaRicevuta().getOggetto(),null));
			tipoVersamento.setPromemoriaRicevutaPdfDefault(entrataPost.getPromemoriaRicevuta().AllegaPdf());
//			}
		}
		
		if(entrataPost.getTrasformazione() != null) {
			if(entrataPost.getTrasformazione().getTipo() != null) {
				// valore tipo contabilita non valido
				if(TipoEnum.fromValue(entrataPost.getTrasformazione().getTipo()) == null) {
					throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" + entrataPost.getTrasformazione().getTipo() + "] valori possibili " + ArrayUtils.toString(TipoEnum.values()));
				}
			}
			
			Object definizione = entrataPost.getTrasformazione().getDefinizione();
//			if(definizione instanceof String)
//				tipoVersamento.setTrasformazioneDefinizioneDefault((String) definizione);
//			else
				tipoVersamento.setTrasformazioneDefinizioneDefault(ConverterUtils.toJSON(definizione,null));
			tipoVersamento.setTrasformazioneTipoDefault(entrataPost.getTrasformazione().getTipo());
		}
		if(entrataPost.getValidazione() != null)
			tipoVersamento.setValidazioneDefinizioneDefault(ConverterUtils.toJSON(entrataPost.getValidazione(),null));
		
		if(entrataPost.getInoltro() != null)
			tipoVersamento.setCodApplicazioneDefault(entrataPost.getInoltro());
		
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
		
		if(tipoVersamento.getFormDefinizioneDefault() != null && tipoVersamento.getFormTipoDefault() != null) {
			TipoPendenzaForm form = new TipoPendenzaForm();
			form.setTipo(tipoVersamento.getFormTipoDefault());
			form.setDefinizione(new RawObject(tipoVersamento.getFormDefinizioneDefault())); 
			rsModel.setForm(form);
		}
		
		TipoPendenzaPromemoria promemoria = new TipoPendenzaPromemoria();
		promemoria.setAbilitato(tipoVersamento.isPromemoriaAvvisoAbilitatoDefault());
		
		if(tipoVersamento.getPromemoriaAvvisoOggettoDefault() != null)
			promemoria.setOggetto(new RawObject(tipoVersamento.getPromemoriaAvvisoOggettoDefault()));
		if(tipoVersamento.getPromemoriaAvvisoMessaggioDefault() != null)
			promemoria.setMessaggio(new RawObject(tipoVersamento.getPromemoriaAvvisoMessaggioDefault()));
		promemoria.setAllegaPdf(tipoVersamento.getPromemoriaAvvisoPdfDefault());
		promemoria.setTipo(tipoVersamento.getPromemoriaAvvisoTipoDefault());
		
		rsModel.setPromemoriaAvviso(promemoria);
		
		TipoPendenzaPromemoria ricevuta = new TipoPendenzaPromemoria();
		ricevuta.setAbilitato(tipoVersamento.isPromemoriaRicevutaAbilitatoDefault());
		
		if(tipoVersamento.getPromemoriaRicevutaOggettoDefault() != null)
			ricevuta.setOggetto(new RawObject(tipoVersamento.getPromemoriaRicevutaOggettoDefault()));
		if(tipoVersamento.getPromemoriaRicevutaMessaggioDefault() != null)
			ricevuta.setMessaggio(new RawObject(tipoVersamento.getPromemoriaRicevutaMessaggioDefault()));
		ricevuta.setAllegaPdf(tipoVersamento.getPromemoriaRicevutaPdfDefault());
		ricevuta.setTipo(tipoVersamento.getPromemoriaRicevutaTipoDefault());
		
		
		rsModel.setPromemoriaRicevuta(ricevuta);
		
		
		if(tipoVersamento.getTrasformazioneTipoDefault() != null && tipoVersamento.getTrasformazioneDefinizioneDefault() != null) {
			TipoPendenzaTrasformazione trasformazione  = new TipoPendenzaTrasformazione();
			trasformazione.setTipo(tipoVersamento.getTrasformazioneTipoDefault());
			trasformazione.setDefinizione(new RawObject(tipoVersamento.getTrasformazioneDefinizioneDefault())); 
			rsModel.setTrasformazione(trasformazione);
		}
		if(tipoVersamento.getValidazioneDefinizioneDefault() != null)
			rsModel.setValidazione(new RawObject(tipoVersamento.getValidazioneDefinizioneDefault()));
		
		rsModel.setInoltro(tipoVersamento.getCodApplicazioneDefault());
		
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
