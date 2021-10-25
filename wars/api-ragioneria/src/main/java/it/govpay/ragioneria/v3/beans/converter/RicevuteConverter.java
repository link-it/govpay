package it.govpay.ragioneria.v3.beans.converter;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentRes;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTReq;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Rpt;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.ragioneria.v3.beans.Ricevuta;
import it.govpay.ragioneria.v3.beans.RicevutaRpt;
import it.govpay.ragioneria.v3.beans.RicevutaRt;
import it.govpay.ragioneria.v3.beans.RicevuteRisultati;
import it.govpay.ragioneria.v3.beans.RicevutaRt.TipoEnum;

public class RicevuteConverter {

	public static RicevuteRisultati toRsModelIndex(Rpt dto) throws ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		RicevuteRisultati rsModel = new RicevuteRisultati();
		rsModel.setData(dto.getDataMsgRicevuta());
		rsModel.setDominio(DominiConverter.toRsModelIndex(dto.getDominio(configWrapper)));
		rsModel.setIdRicevuta(dto.getCcp());
		rsModel.setIuv(dto.getIuv());

		return rsModel;
	}


	public static Ricevuta toRsModel(Rpt rpt) throws ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		Ricevuta rsModel = new Ricevuta();
		rsModel.setAttestante(null);
		rsModel.setData(rpt.getDataMsgRicevuta());
		rsModel.setDataPagamento(null);
		rsModel.setDominio(DominiConverter.toRsModelIndex(rpt.getDominio(configWrapper)));
		rsModel.setIdRicevuta(rpt.getCcp());
		rsModel.setIuv(rpt.getIuv());


		rsModel.setPendenza(PendenzeConverter.toRsModel(rpt.getVersamento()));
		rsModel.setRiscossioni(null);
		RicevutaRpt ricevutaRpt = new RicevutaRpt();

		try {
			ricevutaRpt.setXml(rpt.getXmlRpt());
			switch (rpt.getVersione()) {
			case SANP_240:
				PaGetPaymentRes paGetPaymentRes_RPT = JaxbUtils.toPaGetPaymentRes_RPT(rpt.getXmlRpt(), false);
				ricevutaRpt.setTipo(it.govpay.ragioneria.v3.beans.RicevutaRpt.TipoEnum.CTPAYMENTPA);
				ricevutaRpt.setJson(paGetPaymentRes_RPT);
				break;
			case SANP_230:
				CtRichiestaPagamentoTelematico ctRpt = JaxbUtils.toRPT(rpt.getXmlRpt(), false);
				ricevutaRpt.setTipo(it.govpay.ragioneria.v3.beans.RicevutaRpt.TipoEnum.CTRICHIESTAPAGAMENTOTELEMATICO);
				ricevutaRpt.setJson(ctRpt);

				rsModel.setVersante(PendenzeConverter.toSoggettoRsModel(ctRpt.getSoggettoVersante())); 
				break;

			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}


		rsModel.setRpt(ricevutaRpt);

		if(rpt.getXmlRt() != null) {
			RicevutaRt ricevutaRt = new RicevutaRt();
			ricevutaRt.setXml(rpt.getXmlRt());

			try {
				switch (rpt.getVersione()) {
				case SANP_240:
					PaSendRTReq paSendRTReq_RT = JaxbUtils.toPaSendRTReq_RT(rpt.getXmlRt(), false);
					ricevutaRt.setTipo(TipoEnum.CTRECEIPT);
					ricevutaRt.setJson(paSendRTReq_RT);
					break;
				case SANP_230:
					CtRicevutaTelematica ctRt = JaxbUtils.toRT(rpt.getXmlRt(), false);
					ricevutaRt.setTipo(TipoEnum.CTRICEVUTATELEMATICA);
					ricevutaRt.setJson(ctRt);
					break;
				}
			} catch (Exception e) {
				throw new ServiceException(e);
			}
			rsModel.setRt(ricevutaRt);

		}

		return rsModel;
	}

}
