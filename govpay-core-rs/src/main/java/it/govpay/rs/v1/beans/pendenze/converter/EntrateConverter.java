package it.govpay.rs.v1.beans.pendenze.converter;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.core.dao.anagrafica.dto.PutEntrataDTO;
import it.govpay.core.rs.v1.beans.pendenze.TipoEntrata;
import it.govpay.core.rs.v1.beans.pendenze.TipoEntrataPost;
import it.govpay.model.IAutorizzato;

public class EntrateConverter {

	public static PutEntrataDTO getPutEntrataDTO(TipoEntrataPost entrataPost, String idEntrata, IAutorizzato user) throws ServiceException {
		PutEntrataDTO entrataDTO = new PutEntrataDTO(user);
		
		it.govpay.model.TipoTributo tipoTributo = new it.govpay.model.TipoTributo();

		tipoTributo.setCodContabilitaDefault(entrataPost.getCodiceContabilita());
		if(entrataPost.getCodificaIUV()!=null)
			tipoTributo.setCodTributoIuvDefault(entrataPost.getCodificaIUV().toString());
		tipoTributo.setCodTributo(idEntrata);
		tipoTributo.setDescrizione(entrataPost.getDescrizione());
		if(entrataPost.getTipoContabilita() != null) {
			switch (entrataPost.getTipoContabilita()) {
			case ALTRO:
				tipoTributo.setTipoContabilitaDefault(it.govpay.model.Tributo.TipoContabilita.ALTRO);
				break;
			case ENTRATA:
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
				rsModel.tipoContabilita(TipoEntrata.TipoContabilitaEnum.ALTRO);
				break;
			case CAPITOLO:
				rsModel.tipoContabilita(TipoEntrata.TipoContabilitaEnum.ENTRATA);
				break;
			case SIOPE:
				rsModel.tipoContabilita(TipoEntrata.TipoContabilitaEnum.SIOPE);
				break;
			case SPECIALE:
				rsModel.tipoContabilita(TipoEntrata.TipoContabilitaEnum.SPECIALE);
				break;
			}
		}
		
		return rsModel;
	}
}
