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
			tipoVersamento.setFormDefinizioneDefault(ConverterUtils.toJSON(entrataPost.getForm().getDefinizione(),null));
			tipoVersamento.setFormTipoDefault(entrataPost.getForm().getTipo());
		}
		
		if(entrataPost.getPromemoria() != null) {
			tipoVersamento.setPromemoriaMessaggioDefault(entrataPost.getPromemoria().getMessaggio());
			tipoVersamento.setPromemoriaOggettoDefault(entrataPost.getPromemoria().getOggetto());
			tipoVersamento.setPromemoriaAvvisoDefault(entrataPost.getPromemoria().AllegaAvviso());
		}
		
		if(entrataPost.getTrasformazione() != null) {
			if(entrataPost.getTrasformazione().getTipo() != null) {
				// valore tipo contabilita non valido
				if(TipoEnum.fromValue(entrataPost.getTrasformazione().getTipo()) == null) {
					throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" + entrataPost.getTrasformazione().getTipo() + "] valori possibili " + ArrayUtils.toString(TipoEnum.values()));
				}
			}
			
			tipoVersamento.setTrasformazioneDefinizioneDefault(ConverterUtils.toJSON(entrataPost.getTrasformazione().getDefinizione(),null));
			tipoVersamento.setTrasformazioneTipoDefault(entrataPost.getTrasformazione().getTipo());
		}
		if(entrataPost.getValidazione() != null)
			tipoVersamento.setValidazioneDefinizioneDefault(ConverterUtils.toJSON(entrataPost.getValidazione(),null));
		
		if(entrataPost.getInoltro() != null)
			tipoVersamento.setCodApplicazioneDefault(entrataPost.getInoltro());
		
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
		
		if(tipoVersamento.getPromemoriaMessaggioDefault() != null && tipoVersamento.getPromemoriaOggettoDefault() != null) {
			TipoPendenzaPromemoria promemoria = new TipoPendenzaPromemoria();
			promemoria.setOggetto(tipoVersamento.getPromemoriaOggettoDefault());
			promemoria.setMessaggio(tipoVersamento.getPromemoriaMessaggioDefault());
			promemoria.setAllegaAvviso(tipoVersamento.getPromemoriaAvvisoDefault());
			rsModel.setPromemoria(promemoria);
		}
		
		if(tipoVersamento.getTrasformazioneTipoDefault() != null && tipoVersamento.getTrasformazioneDefinizioneDefault() != null) {
			TipoPendenzaTrasformazione trasformazione  = new TipoPendenzaTrasformazione();
			trasformazione.setTipo(tipoVersamento.getTrasformazioneTipoDefault());
			trasformazione.setDefinizione(new RawObject(tipoVersamento.getTrasformazioneDefinizioneDefault())); 
			rsModel.setTrasformazione(trasformazione);
		}
		if(tipoVersamento.getValidazioneDefinizioneDefault() != null)
			rsModel.setValidazione(new RawObject(tipoVersamento.getValidazioneDefinizioneDefault()));
		
		rsModel.setInoltro(tipoVersamento.getCodApplicazioneDefault());
		
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
		
		return rsModel;
	}
}
