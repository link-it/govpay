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
import org.xml.sax.SAXException;

import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtReceipt;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtReceiptV2;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTReq;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTV2Request;
import it.govpay.bd.model.Rpt;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTO.FormatoRicevuta;
import it.govpay.model.exception.CodificaInesistenteException;
import it.govpay.pagopa.beans.utils.JaxbUtils;

public class MessaggiPagoPARtUtils {

	private static final String FORMATO_0_NON_DISPONIBILE = "Formato [{0}] non disponibile";
	private static final String VERSIONE_0_NON_VALIDA = "Versione [{0}] non valida";

	private MessaggiPagoPARtUtils() {}
	
	public static Object getMessaggioRT(Rpt rpt, FormatoRicevuta formato, boolean retrocompatibilitaMessaggiPagoPAV1) throws CodificaInesistenteException, JAXBException, SAXException, ServiceException {
		switch (rpt.getVersione()) {
		case SANP_230:
			return getMessaggioRTSANP230(rpt,formato);
		case SANP_240:
		case RPTV2_RTV1:
			return getMessaggioRTSANP240(rpt, formato, retrocompatibilitaMessaggiPagoPAV1);
		case SANP_321_V2:
		case RPTV1_RTV2:
		case RPTSANP230_RTV2:
			return getMessaggioRTSANP321(rpt, formato, retrocompatibilitaMessaggiPagoPAV1);
		}
		
		throw new CodificaInesistenteException(MessageFormat.format(VERSIONE_0_NON_VALIDA, rpt.getVersione()));
	}

	private static Object getMessaggioRTSANP230(Rpt rpt, FormatoRicevuta formato) throws CodificaInesistenteException, JAXBException, SAXException {
		switch (formato) {
		case JSON:
			return JaxbUtils.toRT(rpt.getXmlRt(), false);
		case RAW:
		case XML:
			return rpt.getXmlRt();
		case PDF: break;
		}
		
		throw new CodificaInesistenteException(MessageFormat.format(FORMATO_0_NON_DISPONIBILE, formato)); 
	}
	
	private static Object getMessaggioRTSANP240(Rpt rpt, FormatoRicevuta formato, boolean retrocompatibilitaMessaggiPagoPAV1) throws CodificaInesistenteException, JAXBException, SAXException, ServiceException {
		PaSendRTReq paSendRTReq = JaxbUtils.toPaSendRTReqRT(rpt.getXmlRt(), false);
		
		if(paSendRTReq == null) return null;
		
		switch (formato) {
		case JSON:
			if(retrocompatibilitaMessaggiPagoPAV1) {
				return MessaggiPagoPAUtils.toCtRicevutaTelematica(paSendRTReq, rpt);
			}
			
			return paSendRTReq.getReceipt();
		case RAW:
		case XML:
			if(retrocompatibilitaMessaggiPagoPAV1) {
				CtRicevutaTelematica ctRt2 = MessaggiPagoPAUtils.toCtRicevutaTelematica(paSendRTReq, rpt);
				return JaxbUtils.toByte(ctRt2);
			}
			
			return rpt.getXmlRt();
		case PDF: break;
		}
		
		throw new CodificaInesistenteException(MessageFormat.format(FORMATO_0_NON_DISPONIBILE, formato)); 
	}
	
	private static Object getMessaggioRTSANP321(Rpt rpt, FormatoRicevuta formato, boolean retrocompatibilitaMessaggiPagoPAV1) throws CodificaInesistenteException, JAXBException, SAXException, ServiceException {
		PaSendRTV2Request paSendRTReq = JaxbUtils.toPaSendRTV2RequestRT(rpt.getXmlRt(), false);
		
		if(paSendRTReq == null) return null;

		switch (formato) {
		case JSON:
			if(retrocompatibilitaMessaggiPagoPAV1) {
				return MessaggiPagoPAUtils.toCtRicevutaTelematica(paSendRTReq, rpt);
			}
			
			return paSendRTReq.getReceipt();
		case RAW:
		case XML:
			if(retrocompatibilitaMessaggiPagoPAV1) {
				CtRicevutaTelematica ctRt2 = MessaggiPagoPAUtils.toCtRicevutaTelematica(paSendRTReq, rpt);
				return JaxbUtils.toByte(ctRt2);
			}
			
			return rpt.getXmlRt();
		case PDF: break;
		}
		
		throw new CodificaInesistenteException(MessageFormat.format(FORMATO_0_NON_DISPONIBILE, formato)); 
	}
	
	public static BigDecimal getImportoRT(Rpt rpt) throws CodificaInesistenteException, JAXBException, SAXException, ServiceException {
		if(rpt.getXmlRt() == null) return null;
		
		switch (rpt.getVersione()) {
		case SANP_230:
			CtRicevutaTelematica ctRicevutaTelematica = (CtRicevutaTelematica) getMessaggioRTSANP230(rpt,FormatoRicevuta.JSON);
			return ctRicevutaTelematica.getDatiPagamento().getImportoTotalePagato();
		case SANP_240:
		case RPTV1_RTV2:
			CtReceipt ctReceipt = (CtReceipt) getMessaggioRTSANP240(rpt, FormatoRicevuta.JSON, false);
			return ctReceipt != null ? ctReceipt.getPaymentAmount() : null;
		case SANP_321_V2:
		case RPTV2_RTV1:
		case RPTSANP230_RTV2:
			CtReceiptV2 ctReceiptV2 = (CtReceiptV2) getMessaggioRTSANP321(rpt, FormatoRicevuta.JSON, false);
			return ctReceiptV2 != null ? ctReceiptV2.getPaymentAmount() : null;
		}
		
		throw new CodificaInesistenteException(MessageFormat.format(VERSIONE_0_NON_VALIDA, rpt.getVersione()));
		
	}
}
