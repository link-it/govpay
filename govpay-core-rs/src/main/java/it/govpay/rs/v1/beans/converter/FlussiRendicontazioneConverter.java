package it.govpay.rs.v1.beans.converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.govpay.bd.model.Rendicontazione;
import it.govpay.core.rs.v1.beans.FlussoRendicontazione;
import it.govpay.core.rs.v1.beans.base.FlussoRendicontazioneIndex;

public class FlussiRendicontazioneConverter {

	public static FlussoRendicontazione toRsModel(it.govpay.bd.model.Fr fr, List<Rendicontazione> rendicontazioni) {
		FlussoRendicontazione rsModel = new FlussoRendicontazione();
		rsModel.setIdFlusso(fr.getCodFlusso());
		rsModel.setDataFlusso(fr.getDataFlusso());
		rsModel.setTrn(fr.getIur());
		rsModel.setDataRegolamento(fr.getDataRegolamento());
		rsModel.setIdPsp(fr.getCodPsp());
		rsModel.setBicRiversamento(fr.getCodBicRiversamento());
		rsModel.setIdDominio(fr.getCodDominio());
		rsModel.setNumeroPagamenti(new BigDecimal(fr.getNumeroPagamenti()));
		rsModel.setImportoTotale(fr.getImportoTotalePagamenti().doubleValue());
//		rsModel.setSegnalazioni(fr.getSegnalazioni());

		List<it.govpay.core.rs.v1.beans.base.Rendicontazione> rendicontazioniLst = new ArrayList<>();
		for(Rendicontazione rendicontazione: rendicontazioni) {
			rendicontazioniLst.add(toRendicontazioneRsModel(rendicontazione));
		}
		rsModel.setRendicontazioni(rendicontazioniLst);

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
		rsModel.setNumeroPagamenti(new BigDecimal(fr.getNumeroPagamenti()));
		rsModel.setImportoTotale(fr.getImportoTotalePagamenti().doubleValue());
//		rsModel.setSegnalazioni(fr.getSegnalazioni());


		return rsModel;
	}

	public static it.govpay.core.rs.v1.beans.base.Rendicontazione toRendicontazioneRsModel(Rendicontazione rendicontazione) {
		it.govpay.core.rs.v1.beans.base.Rendicontazione rsModel = new it.govpay.core.rs.v1.beans.base.Rendicontazione();
		rsModel.setIuv(rendicontazione.getIuv());
		rsModel.setIur(rendicontazione.getIur());
		if(rendicontazione.getIndiceDati()!=null)
			rsModel.setIndice(new BigDecimal(rendicontazione.getIndiceDati()));
		
		rsModel.setImporto(rendicontazione.getImporto());
		
		rsModel.setEsito(new BigDecimal(rendicontazione.getEsito().toString()));
		rsModel.setData(rendicontazione.getData());
//		rsModel.setSegnalazioni(rendicontazione.getSegnalazioni());
//		rsModel.setRiscossione(rendicontazione.getRiscossione());
		return rsModel;
	}
}
