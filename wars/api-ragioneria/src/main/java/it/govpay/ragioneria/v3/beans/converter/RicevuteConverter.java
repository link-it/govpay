/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2023 Link.it srl (http://www.link.it).
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
package it.govpay.ragioneria.v3.beans.converter;

import java.io.UnsupportedEncodingException;

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
import it.govpay.bd.model.PagamentoPortale;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Versamento;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.utils.rawutils.ConverterUtils;
import it.govpay.pagopa.beans.utils.JaxbUtils;
import it.govpay.ragioneria.v3.beans.EsitoRpp;
import it.govpay.ragioneria.v3.beans.ModelloPagamento;
import it.govpay.ragioneria.v3.beans.Ricevuta;
import it.govpay.ragioneria.v3.beans.RicevutaIstitutoAttestante;
import it.govpay.ragioneria.v3.beans.RicevutaRpt;
import it.govpay.ragioneria.v3.beans.RicevutaRt;
import it.govpay.ragioneria.v3.beans.RicevutaRt.TipoEnum;
import it.govpay.ragioneria.v3.beans.RicevuteRisultati;

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


	public static Ricevuta toRsModel(Rpt rpt) throws ServiceException, IOException, UnsupportedEncodingException {
		return toRsModel(rpt, rpt.getVersamento());
	}


	public static Ricevuta toRsModel(Rpt rpt, Versamento versamento) throws ServiceException, UnsupportedEncodingException, IOException {
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
			case SANP_321_V2:
				PaGetPaymentV2Response paGetPaymentV2Response = JaxbUtils.toPaGetPaymentV2Response_RPT(rpt.getXmlRpt(), false);
				ricevutaRpt.setTipo(it.govpay.ragioneria.v3.beans.RicevutaRpt.TipoEnum.CTPAYMENTPA);
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
				case SANP_321_V2:
					PaSendRTV2Request paSendRTV2Request = JaxbUtils.toPaSendRTV2Request_RT(rpt.getXmlRt(), false);
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
