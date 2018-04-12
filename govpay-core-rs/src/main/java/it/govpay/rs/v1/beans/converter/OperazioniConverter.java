package it.govpay.rs.v1.beans.converter;

import it.govpay.core.dao.operazioni.dto.LeggiOperazioneDTOResponse;
import it.govpay.core.rs.v1.beans.Operazione;
import it.govpay.core.rs.v1.beans.base.OperazioneIndex;
import it.govpay.core.utils.UriBuilderUtils;

public class OperazioniConverter {

	public static Operazione toRsModel(LeggiOperazioneDTOResponse leggiOperazioneDTOResponse) {
		Operazione rsModel = new Operazione();
		
		rsModel.setDescrizione(leggiOperazioneDTOResponse.getDescrizioneStato());
		rsModel.setDettaglio(leggiOperazioneDTOResponse.getEsito());
		rsModel.setNome(leggiOperazioneDTOResponse.getNome());
		rsModel.setStato(leggiOperazioneDTOResponse.getStato() + "");
		
		return rsModel;
	}

	public static OperazioneIndex toRsModelIndex(LeggiOperazioneDTOResponse leggiOperazioneDTOResponse) {
		OperazioneIndex rsModel = new OperazioneIndex();
		rsModel.setNome(leggiOperazioneDTOResponse.getNome());
		rsModel.setLocation(UriBuilderUtils.getFromOperazioni(leggiOperazioneDTOResponse.getNome()));
		
		return rsModel;
	}

}
