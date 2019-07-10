package it.govpay.ragioneria.v2.beans.converter;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.ragioneria.v2.beans.RppIndex;
import it.govpay.rs.v1.ConverterUtils;

public class RptConverter {

	public static RppIndex toRsModelIndex(it.govpay.bd.model.Rpt rpt, it.govpay.bd.model.Versamento versamento, it.govpay.bd.model.Applicazione applicazione) throws ServiceException {
		RppIndex rsModel = new RppIndex();

		rsModel.setStato(rpt.getStato().toString());
		rsModel.setDettaglioStato(rpt.getDescrizioneStato());
		rsModel.setRpt(ConverterUtils.getRptJson(rpt));
		rsModel.setRt(ConverterUtils.getRtJson(rpt));

		return rsModel;
	}
}
