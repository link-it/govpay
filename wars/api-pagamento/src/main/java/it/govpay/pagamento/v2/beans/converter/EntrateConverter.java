package it.govpay.pagamento.v2.beans.converter;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.core.dao.anagrafica.dto.PutEntrataDTO;
import it.govpay.model.IAutorizzato;
import it.govpay.pagamento.v2.beans.TipoContabilita;
import it.govpay.pagamento.v2.beans.TipoEntrata;
import it.govpay.pagamento.v2.beans.TipoEntrataPost;

public class EntrateConverter {

	public static TipoEntrata toTipoEntrataRsModel(it.govpay.model.TipoTributo tributo) {
		TipoEntrata rsModel = new TipoEntrata();
		
		rsModel.setAbilitato();
		rsModel.setCodiceContabilita(codiceContabilita);
		rsModel.setIdEntrata(idEntrata);
		
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
		
		return rsModel;
	}
}
