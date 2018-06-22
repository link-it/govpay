package it.govpay.rs.v1.beans.converter;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.core.rs.v1.beans.base.Rpp;
import it.govpay.core.rs.v1.beans.base.RppIndex;
import it.govpay.core.utils.UriBuilderUtils;
import it.govpay.rs.v1.beans.ConverterUtils;

public class RptConverter {


	public static Rpp toRsModel(it.govpay.bd.model.Rpt rpt, it.govpay.bd.model.Versamento versamento, it.govpay.bd.model.Applicazione applicazione) throws ServiceException {
		Rpp rsModel = new Rpp();

		rsModel.setStato(rpt.getStato().toString());
		rsModel.setDettaglioStato(rpt.getDescrizioneStato());
		rsModel.setPendenza(PendenzeConverter.toRsModelIndex(versamento));
		rsModel.setRpt(ConverterUtils.getRptJson(rpt));
		
		if(rpt.getXmlRt() != null) {
			rsModel.setRpt(ConverterUtils.getRtJson(rpt));
		}
		
		return rsModel;
	}

	public static RppIndex toRsModelIndex(it.govpay.bd.model.Rpt rpt, it.govpay.bd.model.Versamento versamento, it.govpay.bd.model.Applicazione applicazione) throws ServiceException {
		RppIndex rsModel = new RppIndex();

		rsModel.setStato(rpt.getStato().toString());
		rsModel.setDettaglioStato(rpt.getDescrizioneStato());
		rsModel.setPendenza(UriBuilderUtils.getPendenzaByIdA2AIdPendenza(applicazione.getCodApplicazione(), versamento.getCodVersamentoEnte()));
		rsModel.setRpt(ConverterUtils.getRptJson(rpt));
		
		if(rpt.getXmlRt() != null) {
			rsModel.setRpt(ConverterUtils.getRtJson(rpt));
		}
		
		return rsModel;
	}
}
