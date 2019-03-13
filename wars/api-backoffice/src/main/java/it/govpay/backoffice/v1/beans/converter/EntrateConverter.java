package it.govpay.backoffice.v1.beans.converter;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.TipoContabilita;
import it.govpay.backoffice.v1.beans.TipoEntrata;
import it.govpay.backoffice.v1.beans.TipoEntrataPost;
import it.govpay.core.dao.anagrafica.dto.PutEntrataDTO;

public class EntrateConverter {

	public static PutEntrataDTO getPutEntrataDTO(TipoEntrataPost entrataPost, String idEntrata, Authentication user) throws ServiceException, ValidationException {
		PutEntrataDTO entrataDTO = new PutEntrataDTO(user);
		
		it.govpay.model.TipoTributo tipoTributo = new it.govpay.model.TipoTributo();

		tipoTributo.setCodContabilitaDefault(entrataPost.getCodiceContabilita());
		if(entrataPost.getCodificaIUV()!=null)
			tipoTributo.setCodTributoIuvDefault(entrataPost.getCodificaIUV().toString());
		tipoTributo.setCodTributo(idEntrata);
		tipoTributo.setDescrizione(entrataPost.getDescrizione());
		if(entrataPost.getTipoContabilita() != null) {
			
			// valore tipo contabilita non valido
			if(TipoContabilita.fromValue(entrataPost.getTipoContabilita()) == null) {
				throw new ValidationException("Codifica inesistente per tipoContabilita. Valore fornito [" + entrataPost.getTipoContabilita() + "] valori possibili " + ArrayUtils.toString(TipoContabilita.values()));
			}
			
			entrataPost.setTipoContabilitaEnum(TipoContabilita.fromValue(entrataPost.getTipoContabilita()));

			switch (entrataPost.getTipoContabilitaEnum()) {
			case ALTRO:
				tipoTributo.setTipoContabilitaDefault(it.govpay.model.Tributo.TipoContabilita.ALTRO);
				break;
			case CAPITOLO:
				tipoTributo.setTipoContabilitaDefault(it.govpay.model.Tributo.TipoContabilita.CAPITOLO);
				break;
			case SIOPE:
				tipoTributo.setTipoContabilitaDefault(it.govpay.model.Tributo.TipoContabilita.SIOPE);
				break;
			case SPECIALE:
			default:
				tipoTributo.setTipoContabilitaDefault(it.govpay.model.Tributo.TipoContabilita.SPECIALE);
				break;
			}
		}

		entrataDTO.setCodTributo(idEntrata);
		entrataDTO.setTipoTributo(tipoTributo);
		
		tipoTributo.setOnlineDefault(entrataPost.Online());
		tipoTributo.setPagaTerziDefault(entrataPost.PagaTerzi());
		
		return entrataDTO;		
	}
	
	public static TipoEntrata toTipoEntrataRsModel(it.govpay.model.TipoTributo tributo) {
		TipoEntrata rsModel = new TipoEntrata();
		
		rsModel.codiceContabilita(tributo.getCodContabilitaDefault())
		.codificaIUV(tributo.getCodTributoIuvDefault())
		.descrizione(tributo.getDescrizione())
		.idEntrata(tributo.getCodTributo());
		
		if(tributo.getTipoContabilitaDefault() != null) {
			switch (tributo.getTipoContabilitaDefault()) {
			case ALTRO:
				rsModel.tipoContabilita(TipoContabilita.ALTRO);
				break;
			case CAPITOLO:
				rsModel.tipoContabilita(TipoContabilita.CAPITOLO);
				break;
			case SIOPE:
				rsModel.tipoContabilita(TipoContabilita.SIOPE);
				break;
			case SPECIALE:
				rsModel.tipoContabilita(TipoContabilita.SPECIALE);
				break;
			}
		}
		
		rsModel.setOnline(tributo.getOnlineDefault());
		rsModel.setPagaTerzi(tributo.getPagaTerziDefault());
		
		return rsModel;
	}
}
