package it.govpay.backoffice.v1.beans.converter;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.TipoContabilita;
import it.govpay.backoffice.v1.beans.TipoPendenza;
import it.govpay.backoffice.v1.beans.TipoPendenzaPost;
import it.govpay.core.dao.anagrafica.dto.PutTipoPendenzaDTO;

public class TipiPendenzaConverter {

	public static PutTipoPendenzaDTO getPutTipoPendenzaDTO(TipoPendenzaPost entrataPost, String idTipoPendenza, Authentication user) throws ServiceException, ValidationException {
		PutTipoPendenzaDTO entrataDTO = new PutTipoPendenzaDTO(user);
		
		it.govpay.model.TipoVersamento tipoVersamento = new it.govpay.model.TipoVersamento();

//		tipoVersamento.setCodContabilitaDefault(entrataPost.getCodiceContabilita());
//		if(entrataPost.getCodificaIUV()!=null)
//			tipoVersamento.setCodTributoIuvDefault(entrataPost.getCodificaIUV().toString());
		tipoVersamento.setCodTipoVersamento(idTipoPendenza);
		tipoVersamento.setDescrizione(entrataPost.getDescrizione());
//		if(entrataPost.getTipoContabilita() != null) {
//			
//			// valore tipo contabilita non valido
//			if(TipoContabilita.fromValue(entrataPost.getTipoContabilita()) == null) {
//				throw new ValidationException("Codifica inesistente per tipoContabilita. Valore fornito [" + entrataPost.getTipoContabilita() + "] valori possibili " + ArrayUtils.toString(TipoContabilita.values()));
//			}
//			
//			entrataPost.setTipoContabilitaEnum(TipoContabilita.fromValue(entrataPost.getTipoContabilita()));
//
//			switch (entrataPost.getTipoContabilitaEnum()) {
//			case ALTRO:
//				tipoVersamento.setTipoContabilitaDefault(it.govpay.model.Tributo.TipoContabilita.ALTRO);
//				break;
//			case CAPITOLO:
//				tipoVersamento.setTipoContabilitaDefault(it.govpay.model.Tributo.TipoContabilita.CAPITOLO);
//				break;
//			case SIOPE:
//				tipoVersamento.setTipoContabilitaDefault(it.govpay.model.Tributo.TipoContabilita.SIOPE);
//				break;
//			case SPECIALE:
//			default:
//				tipoVersamento.setTipoContabilitaDefault(it.govpay.model.Tributo.TipoContabilita.SPECIALE);
//				break;
//			}
//		}

		entrataDTO.setCodTipoVersamento(idTipoPendenza);
		entrataDTO.setTipoVersamento(tipoVersamento);
		
//		tipoVersamento.setOnlineDefault(entrataPost.Online());
//		tipoVersamento.setPagaTerziDefault(entrataPost.PagaTerzi());
		
		return entrataDTO;		
	}
	
	public static TipoPendenza toTipoPendenzaRsModel(it.govpay.model.TipoVersamento tipoVersamento) {
		TipoPendenza rsModel = new TipoPendenza();
		
//		rsModel.codiceContabilita(tipoVersamento.getCodContabilitaDefault())
//		.codificaIUV(tipoVersamento.getCodTributoIuvDefault())
		rsModel.descrizione(tipoVersamento.getDescrizione())
		.idTipoPendenza(tipoVersamento.getCodTipoVersamento());
		
//		if(tipoVersamento.getTipoContabilitaDefault() != null) {
//			switch (tipoVersamento.getTipoContabilitaDefault()) {
//			case ALTRO:
//				rsModel.tipoContabilita(TipoContabilita.ALTRO);
//				break;
//			case CAPITOLO:
//				rsModel.tipoContabilita(TipoContabilita.CAPITOLO);
//				break;
//			case SIOPE:
//				rsModel.tipoContabilita(TipoContabilita.SIOPE);
//				break;
//			case SPECIALE:
//				rsModel.tipoContabilita(TipoContabilita.SPECIALE);
//				break;
//			}
//		}
//		
//		rsModel.setOnline(tipoVersamento.getOnlineDefault());
//		rsModel.setPagaTerzi(tipoVersamento.getPagaTerziDefault());
		
		return rsModel;
	}
}
