package it.govpay.ragioneria.v1.beans.converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.Rendicontazione;
import it.govpay.ragioneria.v1.beans.FlussoRendicontazione;
import it.govpay.ragioneria.v1.beans.FlussoRendicontazioneIndex;
import it.govpay.ragioneria.v1.beans.Segnalazione;
import it.govpay.model.Fr.Anomalia;

public class FlussiRendicontazioneConverter {

	public static FlussoRendicontazione toRsModel(it.govpay.bd.model.Fr fr) throws ServiceException, NotFoundException {
		FlussoRendicontazione rsModel = new FlussoRendicontazione();
		rsModel.setIdFlusso(fr.getCodFlusso());
		rsModel.setDataFlusso(fr.getDataFlusso());
		rsModel.setTrn(fr.getIur());
		rsModel.setDataRegolamento(fr.getDataRegolamento());
		rsModel.setBicRiversamento(fr.getCodBicRiversamento());
		rsModel.setIdDominio(fr.getCodDominio());
		rsModel.setNumeroPagamenti(new BigDecimal(fr.getNumeroPagamenti()));
		rsModel.setImportoTotale(fr.getImportoTotalePagamenti().doubleValue());
		rsModel.setIdPsp(fr.getCodPsp());
		rsModel.setIdDominio(fr.getCodDominio());
		
		if(fr.getAnomalie() != null) {
			List<Segnalazione> segnalazioni = new ArrayList<>();
			for(Anomalia anomalia: fr.getAnomalie()) {
				segnalazioni.add(new Segnalazione().codice(anomalia.getCodice()).descrizione(anomalia.getDescrizione()));
			}
			rsModel.setSegnalazioni(segnalazioni);
		}

		List<it.govpay.ragioneria.v1.beans.Rendicontazione> rendicontazioniLst = new ArrayList<>();
		for(Rendicontazione rendicontazione: fr.getRendicontazioni(null)) {
			rendicontazioniLst.add(toRendicontazioneRsModel(rendicontazione));
		}
		rsModel.setRendicontazioni(rendicontazioniLst);

		return rsModel;
	}

	public static FlussoRendicontazioneIndex toRsIndexModel(it.govpay.bd.model.Fr fr) throws ServiceException, NotFoundException {
		FlussoRendicontazioneIndex rsModel = new FlussoRendicontazioneIndex();
		rsModel.setIdFlusso(fr.getCodFlusso());
		rsModel.setDataFlusso(fr.getDataFlusso());
		rsModel.setTrn(fr.getIur());
		rsModel.setDataRegolamento(fr.getDataRegolamento());
		rsModel.setBicRiversamento(fr.getCodBicRiversamento());
		rsModel.setIdDominio(fr.getCodDominio());
		rsModel.setNumeroPagamenti(new BigDecimal(fr.getNumeroPagamenti()));
		rsModel.setImportoTotale(fr.getImportoTotalePagamenti().doubleValue());
		rsModel.setIdPsp(fr.getCodPsp());
		if(fr.getAnomalie() != null) {
			List<Segnalazione> segnalazioni = new ArrayList<>();
			for(Anomalia anomalia: fr.getAnomalie()) {
				segnalazioni.add(new Segnalazione().codice(anomalia.getCodice()).descrizione(anomalia.getDescrizione()));
			}
			rsModel.setSegnalazioni(segnalazioni);
		}


		return rsModel;
	}

	public static it.govpay.ragioneria.v1.beans.Rendicontazione toRendicontazioneRsModel(Rendicontazione rendicontazione) throws ServiceException, NotFoundException {
		it.govpay.ragioneria.v1.beans.Rendicontazione rsModel = new it.govpay.ragioneria.v1.beans.Rendicontazione();
		rsModel.setIuv(rendicontazione.getIuv());
		rsModel.setIur(rendicontazione.getIur());
		if(rendicontazione.getIndiceDati()!=null)
			rsModel.setIndice(new BigDecimal(rendicontazione.getIndiceDati()));
		
		rsModel.setImporto(rendicontazione.getImporto());
		
		if(rendicontazione.getEsito() != null)
			rsModel.setEsito(new BigDecimal(rendicontazione.getEsito().getCodifica()));
		rsModel.setData(rendicontazione.getData());
		if(rendicontazione.getAnomalie() != null) {
			List<Segnalazione> segnalazioni = new ArrayList<>();
			for(it.govpay.model.Rendicontazione.Anomalia anomalia: rendicontazione.getAnomalie()) {
				segnalazioni.add(new Segnalazione().codice(anomalia.getCodice()).descrizione(anomalia.getDescrizione()));
			}
			rsModel.setSegnalazioni(segnalazioni);
		}
		
		if(rendicontazione.getPagamento(null) != null)
			rsModel.setRiscossione(RiscossioniConverter.toRsModelIndex(rendicontazione.getPagamento(null)));
		return rsModel;
	}
}
