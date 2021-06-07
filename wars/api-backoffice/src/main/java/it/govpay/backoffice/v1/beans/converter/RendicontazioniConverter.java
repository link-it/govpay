package it.govpay.backoffice.v1.beans.converter;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import it.govpay.backoffice.v1.beans.RendicontazioneConFlussoEVocePendenza;
import it.govpay.backoffice.v1.beans.Segnalazione;
import it.govpay.model.Rendicontazione.Anomalia;

public class RendicontazioniConverter {

	public static RendicontazioneConFlussoEVocePendenza toRsModel(it.govpay.bd.viste.model.Rendicontazione rendicontazione) throws ServiceException, IOException, ValidationException {
		
		RendicontazioneConFlussoEVocePendenza rsModel = new RendicontazioneConFlussoEVocePendenza();
		
		rsModel.setFlussoRendicontazione(FlussiRendicontazioneConverter.toRsIndexModel(rendicontazione.getFr()));
		
		rsModel.setVocePendenza(PendenzeConverter.toVocePendenzaRendicontazioneRsModel(rendicontazione.getSingoloVersamento(), rendicontazione.getVersamento()));
		
		rsModel.setData(rendicontazione.getRendicontazione().getData());
		if(rendicontazione.getRendicontazione().getEsito() != null)
			rsModel.setEsito(new BigDecimal(rendicontazione.getRendicontazione().getEsito().getCodifica()));
		rsModel.setImporto(rendicontazione.getRendicontazione().getImporto());
		if(rendicontazione.getRendicontazione().getIndiceDati()!=null)
			rsModel.setIndice(new BigDecimal(rendicontazione.getRendicontazione().getIndiceDati()));
		
		rsModel.setIur(rendicontazione.getRendicontazione().getIur());
		rsModel.setIuv(rendicontazione.getRendicontazione().getIuv());
		
		if(rendicontazione.getRendicontazione().getAnomalie() != null) {
			List<Segnalazione> segnalazioni = new ArrayList<>();
			for(Anomalia anomalia: rendicontazione.getRendicontazione().getAnomalie()) {
				segnalazioni.add(new Segnalazione().codice(anomalia.getCodice()).descrizione(anomalia.getDescrizione()));
			}
			rsModel.setSegnalazioni(segnalazioni);
		}
		
		return rsModel;
	}
}
