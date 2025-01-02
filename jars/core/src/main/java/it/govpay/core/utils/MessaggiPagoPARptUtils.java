/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
package it.govpay.core.utils;

import java.math.BigDecimal;
import java.text.MessageFormat;

import javax.xml.bind.JAXBException;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.xml.sax.SAXException;

import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtPaymentPA;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtPaymentPAV2;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentRes;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentV2Response;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTReq;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTV2Request;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Versamento;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTO.FormatoRicevuta;
import it.govpay.model.exception.CodificaInesistenteException;
import it.govpay.pagopa.beans.utils.JaxbUtils;

public class MessaggiPagoPARptUtils {

	private static final String FORMATO_0_NON_DISPONIBILE = "Formato [{0}] non disponibile";
	private static final String VERSIONE_0_NON_VALIDA = "Versione [{0}] non valida";

	private MessaggiPagoPARptUtils() {}
	
	public static Object getMessaggioRPT(Rpt rpt, FormatoRicevuta formato, boolean retrocompatibilitaMessaggiPagoPAV1) throws CodificaInesistenteException, JAXBException, SAXException, ServiceException {
		switch (rpt.getVersione()) {
		case SANP_230:
		case RPTSANP230_RTV2:
			return getMessaggioRPTSANP230(rpt,formato);
		case SANP_240:
		case RPTV1_RTV2:
			return getMessaggioRPTSANP240(rpt, formato, retrocompatibilitaMessaggiPagoPAV1);
		case SANP_321_V2:
		case RPTV2_RTV1:
			return getMessaggioRPTSANP321(rpt, formato, retrocompatibilitaMessaggiPagoPAV1);
		}
		
		throw new CodificaInesistenteException(MessageFormat.format(VERSIONE_0_NON_VALIDA, rpt.getVersione()));
	}

	private static Object getMessaggioRPTSANP230(Rpt rpt, FormatoRicevuta formato) throws CodificaInesistenteException, JAXBException, SAXException {
		switch (formato) {
		case JSON:
			return JaxbUtils.toRPT(rpt.getXmlRpt(), false);
		case RAW:
		case XML:
			return rpt.getXmlRpt();
		case PDF: break;
		}
		
		throw new CodificaInesistenteException(MessageFormat.format(FORMATO_0_NON_DISPONIBILE, formato)); 
	}
	
	private static Object getMessaggioRPTSANP240(Rpt rpt, FormatoRicevuta formato, boolean retrocompatibilitaMessaggiPagoPAV1) throws CodificaInesistenteException, JAXBException, SAXException, ServiceException {
		byte[] xmlRpt = rpt.getXmlRpt();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		
		PaGetPaymentRes paGetPaymentRes;
		// per lo standin il messaggio XML della richiesta potrebbe non esserci. lo ricostruisco
		if(xmlRpt == null) {
			PaSendRTReq paSendRTReq = JaxbUtils.toPaSendRTReqRT(rpt.getXmlRt(), false);
			Versamento versamento = rpt.getVersamento(configWrapper);
			paGetPaymentRes = MessaggiPagoPAUtils.ricostruisciPaGetPaymentRes(paSendRTReq, rpt, versamento);
			
		} else {
			paGetPaymentRes = JaxbUtils.toPaGetPaymentResRPT(xmlRpt, false);
		}
		
		
		switch (formato) {
		case JSON:
			if(retrocompatibilitaMessaggiPagoPAV1) {
				return MessaggiPagoPAUtils.toCtRichiestaPagamentoTelematico(paGetPaymentRes, rpt);
			}
			
			return paGetPaymentRes.getData();
		case RAW:
		case XML:
			if(retrocompatibilitaMessaggiPagoPAV1) {
				CtRichiestaPagamentoTelematico ctRpt2 = MessaggiPagoPAUtils.toCtRichiestaPagamentoTelematico(paGetPaymentRes, rpt);
				return JaxbUtils.toByte(ctRpt2);
			}
			
			return JaxbUtils.toByte(paGetPaymentRes);
		case PDF: break;
		}
		
		throw new CodificaInesistenteException(MessageFormat.format(FORMATO_0_NON_DISPONIBILE, formato)); 
	}
	
	private static Object getMessaggioRPTSANP321(Rpt rpt, FormatoRicevuta formato, boolean retrocompatibilitaMessaggiPagoPAV1) throws CodificaInesistenteException, JAXBException, SAXException, ServiceException {
		byte[] xmlRpt = rpt.getXmlRpt();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		
		PaGetPaymentV2Response paGetPaymentRes;
		// per lo standin il messaggio XML della richiesta potrebbe non esserci. lo ricostruisco
		if(xmlRpt == null) {
			PaSendRTV2Request paSendRTReq = JaxbUtils.toPaSendRTV2RequestRT(rpt.getXmlRt(), false);
			Versamento versamento = rpt.getVersamento(configWrapper);
			paGetPaymentRes = MessaggiPagoPAUtils.ricostruisciPaGetPaymentV2Response(paSendRTReq, rpt, versamento);
			
		} else {
			paGetPaymentRes = JaxbUtils.toPaGetPaymentV2ResponseRPT(xmlRpt, false);
		}
		
		
		switch (formato) {
		case JSON:
			if(retrocompatibilitaMessaggiPagoPAV1) {
				return MessaggiPagoPAUtils.toCtRichiestaPagamentoTelematico(paGetPaymentRes, rpt);
			}
			
			return paGetPaymentRes.getData();
		case RAW:
		case XML:
			if(retrocompatibilitaMessaggiPagoPAV1) {
				CtRichiestaPagamentoTelematico ctRpt2 = MessaggiPagoPAUtils.toCtRichiestaPagamentoTelematico(paGetPaymentRes, rpt);
				return JaxbUtils.toByte(ctRpt2);
			}
			
			return JaxbUtils.toByte(paGetPaymentRes);
		case PDF: break;
		}
		
		throw new CodificaInesistenteException(MessageFormat.format(FORMATO_0_NON_DISPONIBILE, formato)); 
	}
	
	public static BigDecimal getImportoRPT(Rpt rpt) throws CodificaInesistenteException, JAXBException, SAXException, ServiceException {
		switch (rpt.getVersione()) {
		case SANP_230:
		case RPTSANP230_RTV2:
			CtRichiestaPagamentoTelematico ctRichiestaPagamentoTelematico = (CtRichiestaPagamentoTelematico) getMessaggioRPTSANP230(rpt,FormatoRicevuta.JSON);
			return ctRichiestaPagamentoTelematico.getDatiVersamento().getImportoTotaleDaVersare();
		case SANP_240:
		case RPTV1_RTV2:
			CtPaymentPA ctPaymentPA = (CtPaymentPA) getMessaggioRPTSANP240(rpt, FormatoRicevuta.JSON, false);
			return ctPaymentPA.getPaymentAmount();
		case SANP_321_V2:
		case RPTV2_RTV1:
			CtPaymentPAV2 ctPaymentPAV2 = (CtPaymentPAV2) getMessaggioRPTSANP321(rpt, FormatoRicevuta.JSON, false);
			return ctPaymentPAV2.getPaymentAmount();
		}
		
		throw new CodificaInesistenteException(MessageFormat.format(VERSIONE_0_NON_VALIDA, rpt.getVersione()));
		
	}
}
