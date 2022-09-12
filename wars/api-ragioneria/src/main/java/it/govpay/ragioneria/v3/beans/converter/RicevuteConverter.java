package it.govpay.ragioneria.v3.beans.converter;

import java.io.IOException;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.jaxrs.RawObject;
import it.govpay.core.exceptions.ValidationException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentRes;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTReq;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Versamento;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.ragioneria.v3.beans.EsitoRpp;
import it.govpay.ragioneria.v3.beans.ModelloPagamento;
import it.govpay.ragioneria.v3.beans.Ricevuta;
import it.govpay.ragioneria.v3.beans.RicevutaIstitutoAttestante;
import it.govpay.ragioneria.v3.beans.RicevutaRpt;
import it.govpay.ragioneria.v3.beans.RicevutaRt;
import it.govpay.ragioneria.v3.beans.RicevutaRt.TipoEnum;
import it.govpay.ragioneria.v3.beans.RicevuteRisultati;
import it.govpay.rs.v1.ConverterUtils;

public class RicevuteConverter {

	public static RicevuteRisultati toRsModelIndex(Rpt dto) throws ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		RicevuteRisultati rsModel = new RicevuteRisultati();
		rsModel.setData(dto.getDataMsgRicevuta());
		rsModel.setDominio(DominiConverter.toRsModelIndex(dto.getDominio(configWrapper)));
		rsModel.setIdRicevuta(dto.getCcp());
		rsModel.setIuv(dto.getIuv());
		if(dto.getEsitoPagamento() != null)
			rsModel.setEsito(EsitoRpp.fromRptEsitoPagamento(dto.getEsitoPagamento().name()));
		
		return rsModel;
	}


	public static Ricevuta toRsModel(Rpt rpt) throws ServiceException, IOException, ValidationException {
		return toRsModel(rpt, rpt.getVersamento());
	}


	public static Ricevuta toRsModel(Rpt rpt, Versamento versamento) throws ServiceException, IOException, ValidationException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		Ricevuta rsModel = new Ricevuta();
		
		if(rpt.getIdentificativoAttestante() != null) {
			RicevutaIstitutoAttestante istitutoAttestante = new RicevutaIstitutoAttestante();
			istitutoAttestante.setDenominazione(rpt.getDenominazioneAttestante());
			istitutoAttestante.setIdPSP(rpt.getIdentificativoAttestante());
			istitutoAttestante.setIdCanale(rpt.getCodCanale());
			rsModel.setIstitutoAttestante(istitutoAttestante);	
		}
		
		rsModel.setData(rpt.getDataMsgRicevuta());
		rsModel.setDominio(DominiConverter.toRsModelIndex(rpt.getDominio(configWrapper)));
		rsModel.setIdRicevuta(rpt.getCcp());
		rsModel.setIuv(rpt.getIuv());
		if(rpt.getEsitoPagamento() != null)
			rsModel.setEsito(EsitoRpp.fromRptEsitoPagamento(rpt.getEsitoPagamento().name()));
		
		if(rpt.getPagamentoPortale(configWrapper) != null) {
			rsModel.setIdPagamento(rpt.getPagamentoPortale(configWrapper).getIdSessione());
			rsModel.setIdSessionePsp(rpt.getPagamentoPortale(configWrapper).getIdSessionePsp());
		}

		rsModel.setPendenza(PendenzeConverter.toPendenzaPagataRsModel(rpt));
		
		rsModel.setDataPagamento(rpt.getDataMsgRicevuta());
		
		RicevutaRpt ricevutaRpt = new RicevutaRpt();

		try {
			ricevutaRpt.setXml(rpt.getXmlRpt());
			switch (rpt.getVersione()) {
			case SANP_240:
				PaGetPaymentRes paGetPaymentRes_RPT = JaxbUtils.toPaGetPaymentRes_RPT(rpt.getXmlRpt(), false);
				ricevutaRpt.setTipo(it.govpay.ragioneria.v3.beans.RicevutaRpt.TipoEnum.CTPAYMENTPA);
				ricevutaRpt.setJson(new RawObject(ConverterUtils.getRptJson(rpt)));
				
				rsModel.setImporto(paGetPaymentRes_RPT.getData().getPaymentAmount());
				break;
			case SANP_230:
				CtRichiestaPagamentoTelematico ctRpt = JaxbUtils.toRPT(rpt.getXmlRpt(), false);
				ricevutaRpt.setTipo(it.govpay.ragioneria.v3.beans.RicevutaRpt.TipoEnum.CTRICHIESTAPAGAMENTOTELEMATICO);
				ricevutaRpt.setJson(new RawObject(ConverterUtils.getRptJson(rpt)));

				rsModel.setVersante(PendenzeConverter.toSoggettoRsModel(ctRpt.getSoggettoVersante()));
				
				rsModel.setImporto(ctRpt.getDatiVersamento().getImportoTotaleDaVersare());
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
					ricevutaRt.setJson(new RawObject(ConverterUtils.getRtJson(rpt)));
					rsModel.setImporto(paSendRTReq_RT.getReceipt().getPaymentAmount());
					break;
				case SANP_230:
					CtRicevutaTelematica ctRt = JaxbUtils.toRT(rpt.getXmlRt(), false);
					ricevutaRt.setTipo(TipoEnum.CTRICEVUTATELEMATICA);
					ricevutaRt.setJson(new RawObject(ConverterUtils.getRtJson(rpt)));
					rsModel.setImporto(ctRt.getDatiPagamento().getImportoTotalePagato());
					break;
				}
			} catch (Exception e) {
				throw new ServiceException(e);
			}
			rsModel.setRt(ricevutaRt);

		}
		
		if(rpt.getPagamentoPortale() != null) {
			if(rpt.getPagamentoPortale().getTipo() == 1) {
				rsModel.setModello(ModelloPagamento.ENTE);	
			} else if(rpt.getPagamentoPortale().getTipo() == 3) {
				rsModel.setModello(ModelloPagamento.PSP);
			}
		}

		return rsModel;
	}

}
