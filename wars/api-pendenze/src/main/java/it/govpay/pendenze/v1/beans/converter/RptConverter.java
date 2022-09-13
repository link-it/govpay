package it.govpay.pendenze.v1.beans.converter;


import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.core.exceptions.IOException;
import it.govpay.core.utils.UriBuilderUtils;
import it.govpay.pendenze.v1.beans.Rpp;
import it.govpay.pendenze.v1.beans.RppIndex;
import it.govpay.rs.v1.ConverterUtils;

public class RptConverter {


	public static Rpp toRsModel(it.govpay.bd.model.Rpt rpt, it.govpay.bd.model.Versamento versamento, it.govpay.bd.model.Applicazione applicazione) throws IOException, ServiceException {
		Rpp rsModel = new Rpp();

		rsModel.setStato(rpt.getStato().toString());
		rsModel.setDettaglioStato(rpt.getDescrizioneStato());
		rsModel.setPendenza(PendenzeConverter.toRsIndexModel(versamento));
		rsModel.setRpt(ConverterUtils.getRptJson(rpt));
		rsModel.setRt(ConverterUtils.getRtJson(rpt));
		
		return rsModel;
	}

	public static RppIndex toRsModelIndex(it.govpay.bd.model.Rpt rpt, it.govpay.bd.model.Versamento versamento, it.govpay.bd.model.Applicazione applicazione) throws IOException {
		RppIndex rsModel = new RppIndex();

		rsModel.setStato(rpt.getStato().toString());
		rsModel.setDettaglioStato(rpt.getDescrizioneStato());
		rsModel.setPendenza(UriBuilderUtils.getPendenzaByIdA2AIdPendenza(applicazione.getCodApplicazione(), versamento.getCodVersamentoEnte()));
		rsModel.setRpt(ConverterUtils.getRptJson(rpt));
		rsModel.setRt(ConverterUtils.getRtJson(rpt));
		
		return rsModel;
	}
}
