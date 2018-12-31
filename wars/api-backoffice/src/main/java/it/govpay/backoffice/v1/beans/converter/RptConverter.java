package it.govpay.backoffice.v1.beans.converter;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.backoffice.v1.beans.Rpp;
import it.govpay.backoffice.v1.beans.RppIndex;
import it.govpay.core.utils.UriBuilderUtils;
import it.govpay.rs.v1.ConverterUtils;

public class RptConverter {


	public static Rpp toRsModel(it.govpay.bd.model.Rpt rpt) throws ServiceException {
		Rpp rsModel = new Rpp();

		rsModel.setStato(rpt.getStato().toString());
		rsModel.setDettaglioStato(rpt.getDescrizioneStato());
		rsModel.setPendenza(PendenzeConverter.toRsModelIndex(rpt.getVersamento(null)));
		rsModel.setRpt(ConverterUtils.getRptJson(rpt));
		rsModel.setRt(ConverterUtils.getRtJson(rpt));

		return rsModel;
	}

	public static RppIndex toRsModelIndex(it.govpay.bd.model.Rpt rpt) throws ServiceException {
		RppIndex rsModel = new RppIndex();

		rsModel.setStato(rpt.getStato().toString());
		rsModel.setDettaglioStato(rpt.getDescrizioneStato());
		rsModel.setPendenza(UriBuilderUtils.getPendenzaByIdA2AIdPendenza(rpt.getVersamento(null).getApplicazione(null).getCodApplicazione(), rpt.getVersamento(null).getCodVersamentoEnte()));
		rsModel.setRpt(ConverterUtils.getRptJson(rpt));
		rsModel.setRt(ConverterUtils.getRtJson(rpt));
		
		return rsModel;
	}
}
