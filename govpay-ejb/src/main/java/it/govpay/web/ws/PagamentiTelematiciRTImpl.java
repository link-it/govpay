/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
package it.govpay.web.ws;

import gov.telematici.pagamenti.ws.ppthead.IntestazionePPT;
import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.ws.nodo.EsitoPaaInviaRT;
import it.gov.digitpa.schemas._2011.ws.nodo.PaaInviaRT;
import it.gov.digitpa.schemas._2011.ws.nodo.PaaInviaRTRisposta;
import it.gov.digitpa.schemas._2011.ws.nodo.TipoInviaEsitoStornoRisposta;
import it.gov.digitpa.schemas._2011.ws.nodo.FaultBean;
import it.gov.spcoop.nodopagamentispc.servizi.pagamentitelematicirt.PagamentiTelematiciRT;
import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Intermediario;
import it.govpay.bd.model.Rpt.FirmaRichiesta;
import it.govpay.bd.model.Rt.FaultPa;
import it.govpay.business.Pagamenti;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.exception.GovPayNdpException;
import it.govpay.utils.JaxbUtils;
import it.govpay.utils.NdpUtils;
import it.govpay.utils.ValidatoreRT;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

@WebService(serviceName = "PagamentiTelematiciRTservice",
endpointInterface = "it.gov.spcoop.nodopagamentispc.servizi.pagamentitelematicirt.PagamentiTelematiciRT",
targetNamespace = "http://NodoPagamentiSPC.spcoop.gov.it/servizi/PagamentiTelematiciRT",
portName = "PPTPort")

@HandlerChain(file="../../../../handler-chains/ws-ndp-handler-chain.xml")
public class PagamentiTelematiciRTImpl implements PagamentiTelematiciRT {

	@Resource
	WebServiceContext wsCtxt;

	private static Logger log = LogManager.getLogger();

	@Override
	public TipoInviaEsitoStornoRisposta paaInviaEsitoStorno(
			String identificativoIntermediarioPA,
			String identificativoStazioneIntermediarioPA,
			String identificativoDominio,
			String identificativoUnivocoVersamento,
			String codiceContestoPagamento, byte[] er) {
		return null;
	}

	@Override
	public PaaInviaRTRisposta paaInviaRT(PaaInviaRT bodyrichiesta, IntestazionePPT header) {
		String ccp = header.getCodiceContestoPagamento();
		String codDominio = header.getIdentificativoDominio();
		String iuv = header.getIdentificativoUnivocoVersamento();
		
		BasicBD bd = null;
		try {
			try {
				bd = BasicBD.newInstance();
				Intermediario intermediario = AnagraficaManager.getIntermediario(bd, header.getIdentificativoIntermediarioPA());
				NdpUtils.setThreadContextNdpParams(codDominio, iuv, ccp, null, null, header.getIdentificativoStazioneIntermediarioPA(), null, null, "paaInviaRT", intermediario.getDenominazione());
			} catch (ServiceException e) {
				throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_INTERNO, FaultPa.PAA_ERRORE_INTERNO, "Impossibile stabilire una connessione con db", e);
			} catch (NotFoundException e) {
				throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_INTERNO, FaultPa.PAA_ERRORE_INTERNO, "Impossibile trovare l'intermediario ["+header.getIdentificativoIntermediarioPA()+"] sul db", e);
			} catch (MultipleResultException e) {
				throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_INTERNO, FaultPa.PAA_ERRORE_INTERNO, "Impossibile trovare l'intermediario ["+header.getIdentificativoIntermediarioPA()+"] sul db", e);
			}
		} catch (GovPayNdpException gpndp) {
			PaaInviaRTRisposta risposta = new PaaInviaRTRisposta();
			EsitoPaaInviaRT esito = new EsitoPaaInviaRT();
			esito.setEsito("KO");
			FaultBean fault = new FaultBean();
			fault.setId(codDominio);
			fault.setFaultCode(gpndp.getFaultCode().toString());
			fault.setDescription(gpndp.getMessage());
			esito.setFault(fault);
			risposta.setPaaInviaRTRisposta(esito);
			return risposta;
		} finally {
			if(bd != null) bd.closeConnection();
		}
		
		log.info("Ricevuta richiesta di acquisizione RT [" + codDominio + "][" + iuv + "][" + ccp + "]");
		
		FirmaRichiesta firma = null;
		try {
			firma = FirmaRichiesta.toEnum(bodyrichiesta.getTipoFirma());
		} catch (ServiceException e) {
			log.error("Ricevuto TipoFirma non gestito [" + bodyrichiesta.getTipoFirma() + "]");
			PaaInviaRTRisposta risposta = new PaaInviaRTRisposta();
			EsitoPaaInviaRT esito = new EsitoPaaInviaRT();
			esito.setEsito("KO");
			FaultBean fault = new FaultBean();
			fault.setId(codDominio);
			fault.setFaultCode(FaultPa.PAA_FIRMA_ERRATA.toString());
			fault.setDescription("Ricevuto TipoFirma non gestito [" + bodyrichiesta.getTipoFirma() + "]");
			esito.setFault(fault);
			risposta.setPaaInviaRTRisposta(esito);
			return risposta;
		}
		
		try {
			ValidatoreRT validatoreRT = new ValidatoreRT();
			
			log.info("Verifica della firma della Ricevuta Telematica");
			byte[] ctRtByte = validatoreRT.validaFirma(firma, bodyrichiesta.getRt());
			
			log.info("Validazione della Ricevuta Telematica");
			CtRicevutaTelematica ctRt = null;
			try {
				ctRt = JaxbUtils.toRT(ctRtByte);
			} catch (Exception e) {
				log.error("Errore durante la validazione sintattica della Ricevuta Telematica.", e);
				throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, FaultPa.PAA_SINTASSI_XSD, e.getMessage());
			}
			try {
				bd = BasicBD.newInstance();
			} catch (ServiceException e) {
				throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_INTERNO, FaultPa.PAA_ERRORE_INTERNO, "Impossibile stabilire una connessione con db", e);
			}
			Pagamenti pagamenti = new Pagamenti(bd);
			pagamenti.acquisisciRT(codDominio, iuv, ccp, ctRt, bodyrichiesta.getRt());
		} catch (GovPayNdpException gpndp) {
			PaaInviaRTRisposta risposta = new PaaInviaRTRisposta();
			EsitoPaaInviaRT esito = new EsitoPaaInviaRT();
			esito.setEsito("KO");
			FaultBean fault = new FaultBean();
			fault.setId(codDominio);
			fault.setFaultCode(gpndp.getFaultCode().toString());
			fault.setDescription(gpndp.getMessage());
			esito.setFault(fault);
			risposta.setPaaInviaRTRisposta(esito);
			return risposta;
		} catch (GovPayException e) {
			PaaInviaRTRisposta risposta = new PaaInviaRTRisposta();
			EsitoPaaInviaRT esito = new EsitoPaaInviaRT();
			esito.setEsito("KO");
			FaultBean fault = new FaultBean();
			fault.setId(codDominio);
			fault.setFaultCode(FaultPa.PAA_ERRORE_INTERNO.toString());
			fault.setDescription(e.getDescrizione());
			esito.setFault(fault);
			risposta.setPaaInviaRTRisposta(esito);
			return risposta;
		} finally {
			if(bd!= null) bd.closeConnection();
		}
		
		PaaInviaRTRisposta risposta = new PaaInviaRTRisposta();
		EsitoPaaInviaRT esito = new EsitoPaaInviaRT();
		esito.setEsito("OK");
		risposta.setPaaInviaRTRisposta(esito);
		return risposta;
	}
}
