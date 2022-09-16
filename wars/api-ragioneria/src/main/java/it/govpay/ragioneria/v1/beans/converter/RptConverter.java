package it.govpay.ragioneria.v1.beans.converter;

import it.govpay.core.exceptions.IOException;
import it.govpay.core.utils.rawutils.ConverterUtils;
import it.govpay.ragioneria.v1.beans.RppIndex;

public class RptConverter {

	public static RppIndex toRsModelIndex(it.govpay.bd.model.Rpt rpt, it.govpay.bd.model.Versamento versamento, it.govpay.bd.model.Applicazione applicazione) throws IOException {
		RppIndex rsModel = new RppIndex();

		rsModel.setStato(rpt.getStato().toString());
		rsModel.setDettaglioStato(rpt.getDescrizioneStato());
		rsModel.setRpt(ConverterUtils.getRptJson(rpt));
		rsModel.setRt(ConverterUtils.getRtJson(rpt));

		return rsModel;
	}
}
