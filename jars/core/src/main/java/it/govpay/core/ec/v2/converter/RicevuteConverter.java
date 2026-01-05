/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.core.ec.v2.converter;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.jaxrs.RawObject;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentRes;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentV2Response;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTReq;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTV2Request;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.PagamentoPortale;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Versamento;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.utils.rawutils.ConverterUtils;
import it.govpay.ec.v2.beans.EsitoRpp;
import it.govpay.ec.v2.beans.ModelloPagamento;
import it.govpay.ec.v2.beans.Ricevuta;
import it.govpay.ec.v2.beans.RicevutaIstitutoAttestante;
import it.govpay.ec.v2.beans.RicevutaRpt;
import it.govpay.ec.v2.beans.RicevutaRt;
import it.govpay.ec.v2.beans.RicevutaRt.TipoEnum;
import it.govpay.model.Notifica;
import it.govpay.pagopa.beans.utils.JaxbUtils;

public class RicevuteConverter {
	
	private RicevuteConverter() {}

	public static Ricevuta toRsModel(Notifica notifica, Rpt rpt, Applicazione applicazione, Versamento versamento, List<Pagamento> pagamenti) throws ServiceException, IOException, UnsupportedEncodingException {
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
		
		if(rpt.getIdPagamentoPortale() != null) {
			PagamentoPortale pagamentoPortale = rpt.getPagamentoPortale(configWrapper);
			rsModel.setIdPagamento(pagamentoPortale.getIdSessione());
			rsModel.setIdSessionePsp(pagamentoPortale.getIdSessionePsp());
		}
		
		rsModel.setPendenza(PendenzePagateConverter.toRsModel(rpt));
		
		rsModel.setDataPagamento(rpt.getDataMsgRicevuta());
		
		RicevutaRpt ricevutaRpt = new RicevutaRpt();

		try {
			ricevutaRpt.setXml(rpt.getXmlRpt());
			switch (rpt.getVersione()) {
			case SANP_240:
			case RPTV1_RTV2:
				PaGetPaymentRes paGetPaymentResRPT = JaxbUtils.toPaGetPaymentResRPT(rpt.getXmlRpt(), false);
				ricevutaRpt.setTipo(it.govpay.ec.v2.beans.RicevutaRpt.TipoEnum.CTPAYMENTPA);
				ricevutaRpt.setJson(new RawObject(ConverterUtils.getRptJson(rpt)));
				
				rsModel.setImporto(paGetPaymentResRPT.getData().getPaymentAmount());
				break;
			case SANP_230:
			case RPTSANP230_RTV2:
				CtRichiestaPagamentoTelematico ctRpt = JaxbUtils.toRPT(rpt.getXmlRpt(), false);
				ricevutaRpt.setTipo(it.govpay.ec.v2.beans.RicevutaRpt.TipoEnum.CTRICHIESTAPAGAMENTOTELEMATICO);
				ricevutaRpt.setJson(new RawObject(ConverterUtils.getRptJson(rpt)));

				rsModel.setVersante(PendenzeConverter.toSoggettoRsModel(ctRpt.getSoggettoVersante()));
				
				rsModel.setImporto(ctRpt.getDatiVersamento().getImportoTotaleDaVersare());
				break;
			case SANP_321_V2:
			case RPTV2_RTV1:
				PaGetPaymentV2Response paGetPaymentV2Response = JaxbUtils.toPaGetPaymentV2ResponseRPT(rpt.getXmlRpt(), false);
				ricevutaRpt.setTipo(it.govpay.ec.v2.beans.RicevutaRpt.TipoEnum.CTPAYMENTPA);
				ricevutaRpt.setJson(new RawObject(ConverterUtils.getRptJson(rpt)));
				
				rsModel.setImporto(paGetPaymentV2Response.getData().getPaymentAmount());
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
				case RPTV2_RTV1:
					PaSendRTReq paSendRTReqRT = JaxbUtils.toPaSendRTReqRT(rpt.getXmlRt(), false);
					ricevutaRt.setTipo(TipoEnum.CTRECEIPT);
					ricevutaRt.setJson(new RawObject(ConverterUtils.getRtJson(rpt)));
					rsModel.setImporto(paSendRTReqRT.getReceipt().getPaymentAmount());
					break;
				case SANP_230:
					CtRicevutaTelematica ctRt = JaxbUtils.toRT(rpt.getXmlRt(), false);
					ricevutaRt.setTipo(TipoEnum.CTRICEVUTATELEMATICA);
					ricevutaRt.setJson(new RawObject(ConverterUtils.getRtJson(rpt)));
					rsModel.setImporto(ctRt.getDatiPagamento().getImportoTotalePagato());
					
					rsModel.setVersante(PendenzeConverter.toSoggettoRsModel(ctRt.getSoggettoVersante()));
					break;
				case SANP_321_V2:
				case RPTV1_RTV2:
				case RPTSANP230_RTV2:
					PaSendRTV2Request paSendRTV2Request = JaxbUtils.toPaSendRTV2RequestRT(rpt.getXmlRt(), false);
					ricevutaRt.setTipo(TipoEnum.CTRECEIPT);
					ricevutaRt.setJson(new RawObject(ConverterUtils.getRtJson(rpt)));
					rsModel.setImporto(paSendRTV2Request.getReceipt().getPaymentAmount());
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
