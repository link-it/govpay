package it.govpay.pendenze.v2.beans.converter;


import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.jaxrs.RawObject;

import it.govpay.core.exceptions.IOException;
import it.govpay.core.utils.rawutils.ConverterUtils;
import it.govpay.pendenze.v2.beans.Rpp;
import it.govpay.pendenze.v2.beans.RppIndex;

public class RptConverter {


	public static Rpp toRsModel(it.govpay.bd.model.Rpt rpt, it.govpay.bd.model.Versamento versamento, it.govpay.bd.model.Applicazione applicazione) throws ServiceException, IOException {
		Rpp rsModel = new Rpp();

		rsModel.setStato(rpt.getStato().toString());
		rsModel.setDettaglioStato(rpt.getDescrizioneStato());
		rsModel.setPendenza(PendenzeConverter.toRsIndexModel(versamento));
		rsModel.setRpt(new RawObject(ConverterUtils.getRptJson(rpt)));
		rsModel.setRt(new RawObject(ConverterUtils.getRtJson(rpt)));
		
		return rsModel;
	}

	public static RppIndex toRsModelIndex(it.govpay.bd.model.Rpt rpt, it.govpay.bd.model.Versamento versamento, it.govpay.bd.model.Applicazione applicazione) throws ServiceException, IOException {
		RppIndex rsModel = new RppIndex();

		rsModel.setStato(rpt.getStato().toString());
		rsModel.setDettaglioStato(rpt.getDescrizioneStato());
		rsModel.setPendenza(PendenzeConverter.toRsIndexModel(versamento));
		rsModel.setRpt(new RawObject(ConverterUtils.getRptJson(rpt)));
		rsModel.setRt(new RawObject(ConverterUtils.getRtJson(rpt)));
		
		return rsModel;
	}
}
