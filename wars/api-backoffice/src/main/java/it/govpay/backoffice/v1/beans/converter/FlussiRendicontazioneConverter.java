package it.govpay.backoffice.v1.beans.converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.backoffice.v1.beans.FlussoRendicontazione;
import it.govpay.backoffice.v1.beans.FlussoRendicontazioneIndex;
import it.govpay.backoffice.v1.beans.Segnalazione;
import it.govpay.backoffice.v1.beans.StatoFlussoRendicontazione;
import it.govpay.bd.model.Rendicontazione;
import it.govpay.model.Fr.Anomalia;
import it.govpay.model.Fr.StatoFr;

public class FlussiRendicontazioneConverter {

	public static FlussoRendicontazione toRsModel(it.govpay.bd.model.Fr fr) throws ServiceException {
		FlussoRendicontazione rsModel = new FlussoRendicontazione();
		rsModel.setIdFlusso(fr.getCodFlusso());
		rsModel.setDataFlusso(fr.getDataFlusso());
		rsModel.setTrn(fr.getIur());
		rsModel.setDataRegolamento(fr.getDataRegolamento());
		rsModel.setIdPsp(fr.getCodPsp());
		rsModel.setBicRiversamento(fr.getCodBicRiversamento());
		rsModel.setIdDominio(fr.getCodDominio());
		rsModel.setNumeroPagamenti(BigDecimal.valueOf(fr.getNumeroPagamenti()));
		rsModel.setImportoTotale(fr.getImportoTotalePagamenti().doubleValue());
		
		if(fr.getAnomalie() != null) {
			List<Segnalazione> segnalazioni = new ArrayList<>();
			for(Anomalia anomalia: fr.getAnomalie()) {
				segnalazioni.add(new Segnalazione().codice(anomalia.getCodice()).descrizione(anomalia.getDescrizione()));
			}
			rsModel.setSegnalazioni(segnalazioni);
		}
		
		rsModel.setRagioneSocialeDominio(fr.getRagioneSocialeDominio());
		rsModel.setRagioneSocialePsp(fr.getRagioneSocialePsp());

		List<it.govpay.backoffice.v1.beans.Rendicontazione> rendicontazioniLst = new ArrayList<>();
		for(Rendicontazione rendicontazione: fr.getRendicontazioni(null)) {
			rendicontazioniLst.add(toRendicontazioneRsModel(rendicontazione));
		}
		rsModel.setRendicontazioni(rendicontazioniLst);
		
		StatoFr stato = fr.getStato();
		if(stato != null) {
			switch (stato) {
			case ACCETTATA:
				rsModel.setStato(StatoFlussoRendicontazione.ACQUISITO);
				break;
			case ANOMALA:
				rsModel.setStato(StatoFlussoRendicontazione.ANOMALO);
				break;
			case RIFIUTATA:
				rsModel.setStato(StatoFlussoRendicontazione.RIFIUTATO);
				break;
			}
		}

		return rsModel;
	}

	public static FlussoRendicontazioneIndex toRsIndexModel(it.govpay.bd.model.Fr fr) {
		FlussoRendicontazioneIndex rsModel = new FlussoRendicontazioneIndex();
		rsModel.setIdFlusso(fr.getCodFlusso());
		rsModel.setDataFlusso(fr.getDataFlusso());
		rsModel.setTrn(fr.getIur());
		rsModel.setDataRegolamento(fr.getDataRegolamento());
		rsModel.setIdPsp(fr.getCodPsp());
		rsModel.setBicRiversamento(fr.getCodBicRiversamento());
		rsModel.setIdDominio(fr.getCodDominio());
		rsModel.setNumeroPagamenti(BigDecimal.valueOf(fr.getNumeroPagamenti()));
		rsModel.setImportoTotale(fr.getImportoTotalePagamenti().doubleValue());
		if(fr.getAnomalie() != null) {
			List<Segnalazione> segnalazioni = new ArrayList<>();
			for(Anomalia anomalia: fr.getAnomalie()) {
				segnalazioni.add(new Segnalazione().codice(anomalia.getCodice()).descrizione(anomalia.getDescrizione()));
			}
			rsModel.setSegnalazioni(segnalazioni);
		}

		rsModel.setRagioneSocialeDominio(fr.getRagioneSocialeDominio());
		rsModel.setRagioneSocialePsp(fr.getRagioneSocialePsp());
		
		StatoFr stato = fr.getStato();
		if(stato != null) {
			switch (stato) {
			case ACCETTATA:
				rsModel.setStato(StatoFlussoRendicontazione.ACQUISITO);
				break;
			case ANOMALA:
				rsModel.setStato(StatoFlussoRendicontazione.ANOMALO);
				break;
			case RIFIUTATA:
				rsModel.setStato(StatoFlussoRendicontazione.RIFIUTATO);
				break;
			}
		}

		return rsModel;
	}

	public static it.govpay.backoffice.v1.beans.Rendicontazione toRendicontazioneRsModel(Rendicontazione rendicontazione) throws ServiceException {
		it.govpay.backoffice.v1.beans.Rendicontazione rsModel = new it.govpay.backoffice.v1.beans.Rendicontazione();
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
			rsModel.setRiscossione(RiscossioniConverter.toRsModel(rendicontazione.getPagamento(null)));
		return rsModel;
	}
}
