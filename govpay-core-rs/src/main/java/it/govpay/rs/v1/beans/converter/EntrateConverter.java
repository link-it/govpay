package it.govpay.rs.v1.beans.converter;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.core.dao.anagrafica.dto.PutEntrataDTO;
import it.govpay.model.IAutorizzato;
import it.govpay.model.TipoTributo;
import it.govpay.rs.v1.beans.base.TipoEntrataPost;
import it.govpay.model.Tributo.TipoContabilita;

public class EntrateConverter {

	public static PutEntrataDTO getPutEntrataDTO(TipoEntrataPost entrataPost, String idEntrata, IAutorizzato user) throws ServiceException {
		PutEntrataDTO entrataDTO = new PutEntrataDTO(user);
		
		TipoTributo tipoTributo = new TipoTributo();

		tipoTributo.setCodContabilitaDefault(entrataPost.getCodiceContabilita());
		if(entrataPost.getCodificaIUV()!=null)
			tipoTributo.setCodTributoIuvDefault(entrataPost.getCodificaIUV().toString());
		tipoTributo.setCodTributo(idEntrata);
		tipoTributo.setDescrizione(entrataPost.getDescrizione());
		if(entrataPost.getTipoContabilitaEnum() != null) {
			switch (entrataPost.getTipoContabilitaEnum()) {
			case ALTRO:
				tipoTributo.setTipoContabilitaDefault(TipoContabilita.ALTRO);
				break;
			case ENTRATA:
				tipoTributo.setTipoContabilitaDefault(TipoContabilita.CAPITOLO);
				break;
			case SIOPE:
				tipoTributo.setTipoContabilitaDefault(TipoContabilita.SIOPE);
				break;
			case SPECIALE:
			default:
				tipoTributo.setTipoContabilitaDefault(TipoContabilita.SPECIALE);
				break;
			}
		}

		entrataDTO.setCodTributo(idEntrata);
		entrataDTO.setTipoTributo(tipoTributo);
		return entrataDTO;		
	}
}
