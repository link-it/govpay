package it.govpay.rs.v1.beans.converter;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.core.dao.anagrafica.dto.PutEntrataDTO;
import it.govpay.model.IAutorizzato;
import it.govpay.model.TipoTributo;
import it.govpay.rs.v1.beans.base.TipoentrataPost;

public class EntrateConverter {

	public static PutEntrataDTO getPutEntrataDTO(TipoentrataPost entrataPost, String idEntrata, IAutorizzato user) throws ServiceException {
		PutEntrataDTO stazioneDTO = new PutEntrataDTO(user);
		
		TipoTributo tipoTributo = new TipoTributo();

		tipoTributo.setCodContabilitaDefault(entrataPost.getCodiceContabilita());
		tipoTributo.setCodTributoIuvDefault(entrataPost.getCodificaIUV().toString());
		tipoTributo.setCodTributo(idEntrata);
		tipoTributo.setDescrizione(entrataPost.getDescrizione());
		tipoTributo.setTipoContabilitaDefault(null);

		
		return stazioneDTO;		
	}
}
