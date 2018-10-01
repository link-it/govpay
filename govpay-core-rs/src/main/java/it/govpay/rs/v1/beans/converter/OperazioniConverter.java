package it.govpay.rs.v1.beans.converter;

import it.govpay.core.dao.operazioni.dto.LeggiOperazioneDTOResponse;
import it.govpay.core.rs.v1.beans.base.Operazione;
import it.govpay.core.rs.v1.beans.base.Operazione.StatoEnum;
import it.govpay.core.rs.v1.beans.base.OperazioneIndex;
import it.govpay.core.utils.UriBuilderUtils;

public class OperazioniConverter {

	public static Operazione toRsModel(LeggiOperazioneDTOResponse leggiOperazioneDTOResponse) {
		Operazione rsModel = new Operazione();
		
		rsModel.setDescrizione(leggiOperazioneDTOResponse.getDescrizioneStato());
//		rsModel.setDettaglio(leggiOperazioneDTOResponse.getDettaglio()); //TODO dettaglio operazione
		rsModel.setEsito(leggiOperazioneDTOResponse.getEsito());
		rsModel.setStato(StatoEnum.fromValue(leggiOperazioneDTOResponse.getStato().toString()));
		rsModel.setIdOperazione(leggiOperazioneDTOResponse.getNome());
		return rsModel;
	}

	public static OperazioneIndex toRsModelIndex(LeggiOperazioneDTOResponse leggiOperazioneDTOResponse) {
		OperazioneIndex rsModel = new OperazioneIndex();
		rsModel.setDescrizione(leggiOperazioneDTOResponse.getDescrizioneStato());
		rsModel.setIdOperazione(leggiOperazioneDTOResponse.getNome());
		rsModel.setLocation(UriBuilderUtils.getFromOperazioni(leggiOperazioneDTOResponse.getNome()));
		
		return rsModel;
	}

}
