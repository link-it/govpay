package it.govpay.pendenze.v1.beans.converter;


import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.pendenze.v1.beans.Rpp;
import it.govpay.pendenze.v1.beans.RppIndex;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.UriBuilderUtils;
import it.govpay.core.utils.rawutils.ConverterUtils;

public class RptConverter {


	public static Rpp toRsModel(it.govpay.bd.model.Rpt rpt, it.govpay.bd.model.Versamento versamento, it.govpay.bd.model.Applicazione applicazione) throws ServiceException {
		Rpp rsModel = new Rpp();
		boolean convertiMessaggioPagoPAV2InPagoPAV1 = GovpayConfig.getInstance().isConversioneMessaggiPagoPAV2NelFormatoV1();

		rsModel.setStato(rpt.getStato().toString());
		rsModel.setDettaglioStato(rpt.getDescrizioneStato());
		rsModel.setPendenza(PendenzeConverter.toRsIndexModel(versamento));
		rsModel.setRpt(ConverterUtils.getRptJson(rpt, convertiMessaggioPagoPAV2InPagoPAV1));
		rsModel.setRt(ConverterUtils.getRtJson(rpt, convertiMessaggioPagoPAV2InPagoPAV1));
		
		return rsModel;
	}

	public static RppIndex toRsModelIndex(it.govpay.bd.model.Rpt rpt, it.govpay.bd.model.Versamento versamento, it.govpay.bd.model.Applicazione applicazione) throws ServiceException {
		RppIndex rsModel = new RppIndex();
		boolean convertiMessaggioPagoPAV2InPagoPAV1 = GovpayConfig.getInstance().isConversioneMessaggiPagoPAV2NelFormatoV1();

		rsModel.setStato(rpt.getStato().toString());
		rsModel.setDettaglioStato(rpt.getDescrizioneStato());
		rsModel.setPendenza(UriBuilderUtils.getPendenzaByIdA2AIdPendenza(applicazione.getCodApplicazione(), versamento.getCodVersamentoEnte()));
		rsModel.setRpt(ConverterUtils.getRptJson(rpt, convertiMessaggioPagoPAV2InPagoPAV1));
		rsModel.setRt(ConverterUtils.getRtJson(rpt, convertiMessaggioPagoPAV2InPagoPAV1));
		
		return rsModel;
	}
}
