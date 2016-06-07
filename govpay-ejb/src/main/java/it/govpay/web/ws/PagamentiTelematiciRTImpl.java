/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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

import java.util.Date;
import gov.telematici.pagamenti.ws.ppthead.IntestazionePPT;
import it.gov.digitpa.schemas._2011.ws.nodo.EsitoPaaInviaRT;
import it.gov.digitpa.schemas._2011.ws.nodo.PaaInviaRT;
import it.gov.digitpa.schemas._2011.ws.nodo.PaaInviaRTRisposta;
import it.gov.digitpa.schemas._2011.ws.nodo.TipoInviaEsitoStornoRisposta;
import it.gov.digitpa.schemas._2011.ws.nodo.FaultBean;
import it.gov.spcoop.nodopagamentispc.servizi.pagamentitelematicirt.PagamentiTelematiciRT;
import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Evento;
import it.govpay.bd.model.Intermediario;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rr;
import it.govpay.bd.model.Evento.TipoEvento;
import it.govpay.bd.model.Stazione;
import it.govpay.core.business.GiornaleEventi;
import it.govpay.core.exceptions.NdpException;
import it.govpay.core.exceptions.NdpException.FaultPa;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.RrUtils;
import it.govpay.core.utils.RtUtils;
import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import org.apache.cxf.annotations.SchemaValidation.SchemaValidationType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.logger.beans.proxy.Actor;

@WebService(serviceName = "PagamentiTelematiciRTservice",
endpointInterface = "it.gov.spcoop.nodopagamentispc.servizi.pagamentitelematicirt.PagamentiTelematiciRT",
targetNamespace = "http://NodoPagamentiSPC.spcoop.gov.it/servizi/PagamentiTelematiciRT",
portName = "PPTPort",
wsdlLocation = "classpath:wsdl/PaPerNodo.wsdl")

@org.apache.cxf.annotations.SchemaValidation(type = SchemaValidationType.IN)

@HandlerChain(file="../../../../handler-chains/handler-chain-ndp.xml")

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
		
		GpContext ctx = GpThreadLocal.get();
		
		ctx.setCorrelationId(identificativoDominio + identificativoUnivocoVersamento + codiceContestoPagamento);
		
		Actor from = new Actor();
		from.setName("NodoDeiPagamentiSPC");
		GpThreadLocal.get().getTransaction().setFrom(from);
		
		Actor to = new Actor();
		to.setName(identificativoStazioneIntermediarioPA);
		GpThreadLocal.get().getTransaction().setTo(to);
		
		ctx.getContext().getRequest().addGenericProperty(new Property("ccp", codiceContestoPagamento));
		ctx.getContext().getRequest().addGenericProperty(new Property("codDominio", identificativoDominio));
		ctx.getContext().getRequest().addGenericProperty(new Property("iuv", identificativoUnivocoVersamento));
		ctx.log("er.ricezione");
		
		log.info("Ricevuta richiesta di acquisizione ER [" + identificativoDominio + "][" + identificativoUnivocoVersamento + "][" + codiceContestoPagamento + "]");
		
		TipoInviaEsitoStornoRisposta response = new TipoInviaEsitoStornoRisposta();
		
		BasicBD bd = null;
		
		Evento evento = new Evento();
		evento.setCodStazione(identificativoStazioneIntermediarioPA);
		evento.setCodDominio(identificativoDominio);
		evento.setIuv(identificativoUnivocoVersamento);
		evento.setCcp(codiceContestoPagamento);
		evento.setTipoEvento(TipoEvento.paaInviaEsitoStorno);
		evento.setFruitore("NodoDeiPagamentiSPC");
		
		try {
			bd = BasicBD.newInstance();
			
			Dominio dominio = null;
			try {
				dominio = AnagraficaManager.getDominio(bd, identificativoDominio);
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_ID_DOMINIO_ERRATO, identificativoDominio);
			}
			
			Intermediario intermediario = null;
			try {
				intermediario = AnagraficaManager.getIntermediario(bd, identificativoIntermediarioPA);
				evento.setErogatore(intermediario.getDenominazione());
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_ID_INTERMEDIARIO_ERRATO, identificativoDominio);
			}
			
			Stazione stazione = null;
			try {
				stazione = AnagraficaManager.getStazione(bd, identificativoStazioneIntermediarioPA);
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_STAZIONE_INT_ERRATA, identificativoDominio);
			}
			
			if(stazione.getIdIntermediario() != intermediario.getId()) {
				throw new NdpException(FaultPa.PAA_ID_INTERMEDIARIO_ERRATO, identificativoDominio);
			}
			
			if(dominio.getIdStazione() != stazione.getId()) {
				throw new NdpException(FaultPa.PAA_STAZIONE_INT_ERRATA, identificativoDominio);
			}
			
			Rr rr = RrUtils.acquisisciEr(identificativoDominio, identificativoUnivocoVersamento, codiceContestoPagamento, er, bd);
			evento.setCodCanale(rr.getRpt(bd).getCanale(bd).getCodCanale());
			evento.setTipoVersamento(rr.getRpt(bd).getCanale(bd).getTipoVersamento());
			response.setEsito("OK");
			ctx.log("er.ricezioneOk");
		} catch (NdpException e) {
			if(bd != null) bd.rollback();
			response = buildRisposta(e, response);
			String faultDescription = response.getFault().getDescription() == null ? "<Nessuna descrizione>" : response.getFault().getDescription(); 
			ctx.log("er.ricezioneKo", response.getFault().getFaultCode(), response.getFault().getFaultString(), faultDescription);
		} catch (Exception e) {
			if(bd != null) bd.rollback();
			response = buildRisposta(new NdpException(FaultPa.PAA_SYSTEM_ERROR, identificativoDominio, e.getMessage(), e), response);
			String faultDescription = response.getFault().getDescription() == null ? "<Nessuna descrizione>" : response.getFault().getDescription(); 
			ctx.log("er.ricezioneKo", response.getFault().getFaultCode(), response.getFault().getFaultString(), faultDescription);
		} finally {
			GiornaleEventi ge = new GiornaleEventi(bd);
			evento.setEsito(response.getEsito());
			evento.setDataRisposta(new Date());
			ge.registraEvento(evento);
			
			if(bd != null) bd.closeConnection();
		}
		return response;
	}
	
	@Override
	public PaaInviaRTRisposta paaInviaRT(PaaInviaRT bodyrichiesta, IntestazionePPT header) {
		
		String ccp = header.getCodiceContestoPagamento();
		String codDominio = header.getIdentificativoDominio();
		String iuv = header.getIdentificativoUnivocoVersamento();
		
		GpContext ctx = GpThreadLocal.get();
		
		ctx.setCorrelationId(codDominio + iuv + ccp);
		
		Actor from = new Actor();
		from.setName("NodoDeiPagamentiSPC");
		ctx.getTransaction().setFrom(from);
		
		Actor to = new Actor();
		to.setName(header.getIdentificativoStazioneIntermediarioPA());
		ctx.getTransaction().setTo(to);
		
		ctx.getContext().getRequest().addGenericProperty(new Property("ccp", ccp));
		ctx.getContext().getRequest().addGenericProperty(new Property("codDominio", codDominio));
		ctx.getContext().getRequest().addGenericProperty(new Property("iuv", iuv));
		ctx.log("rt.ricezione");
		
		log.info("Ricevuta richiesta di acquisizione RT [" + codDominio + "][" + iuv + "][" + ccp + "]");
		PaaInviaRTRisposta response = new PaaInviaRTRisposta();
		
		BasicBD bd = null;
		
		Evento evento = new Evento();
		evento.setCodStazione(header.getIdentificativoStazioneIntermediarioPA());
		evento.setCodDominio(codDominio);
		evento.setIuv(iuv);
		evento.setCcp(ccp);
		evento.setTipoEvento(TipoEvento.paaInviaRT);
		evento.setFruitore("NodoDeiPagamentiSPC");
		
		try {
			bd = BasicBD.newInstance();
			
			Dominio dominio = null;
			try {
				dominio = AnagraficaManager.getDominio(bd, codDominio);
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_ID_DOMINIO_ERRATO, codDominio);
			}
			
			Intermediario intermediario = null;
			try {
				intermediario = AnagraficaManager.getIntermediario(bd, header.getIdentificativoIntermediarioPA());
				evento.setErogatore(intermediario.getDenominazione());
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_ID_INTERMEDIARIO_ERRATO, codDominio);
			}
			
			Stazione stazione = null;
			try {
				stazione = AnagraficaManager.getStazione(bd, header.getIdentificativoStazioneIntermediarioPA());
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_STAZIONE_INT_ERRATA, codDominio);
			}
			
			if(stazione.getIdIntermediario() != intermediario.getId()) {
				throw new NdpException(FaultPa.PAA_ID_INTERMEDIARIO_ERRATO, codDominio);
			}
			
			if(dominio.getIdStazione() != stazione.getId()) {
				throw new NdpException(FaultPa.PAA_STAZIONE_INT_ERRATA, codDominio);
			}
			
			Rpt rpt = RtUtils.acquisisciRT(codDominio, iuv, ccp, bodyrichiesta.getTipoFirma(), bodyrichiesta.getRt(), bd);
			evento.setCodCanale(rpt.getCanale(bd).getCodCanale());
			evento.setTipoVersamento(rpt.getCanale(bd).getTipoVersamento());
			
			EsitoPaaInviaRT esito = new EsitoPaaInviaRT();
			esito.setEsito("OK");
			response.setPaaInviaRTRisposta(esito);
			ctx.log("rt.ricezioneOk");
		} catch (NdpException e) {
			if(bd != null) bd.rollback();
			response = buildRisposta(e, response);
			String faultDescription = response.getPaaInviaRTRisposta().getFault().getDescription() == null ? "<Nessuna descrizione>" : response.getPaaInviaRTRisposta().getFault().getDescription(); 
			ctx.log("rt.ricezioneKo", response.getPaaInviaRTRisposta().getFault().getFaultCode(), response.getPaaInviaRTRisposta().getFault().getFaultString(), faultDescription);
		} catch (Exception e) {
			if(bd != null) bd.rollback();
			response = buildRisposta(new NdpException(FaultPa.PAA_SYSTEM_ERROR, codDominio, e.getMessage(), e), response);
			String faultDescription = response.getPaaInviaRTRisposta().getFault().getDescription() == null ? "<Nessuna descrizione>" : response.getPaaInviaRTRisposta().getFault().getDescription(); 
			ctx.log("rt.ricezioneKo", response.getPaaInviaRTRisposta().getFault().getFaultCode(), response.getPaaInviaRTRisposta().getFault().getFaultString(), faultDescription);
		} finally {
			GiornaleEventi ge = new GiornaleEventi(bd);
			evento.setEsito(response.getPaaInviaRTRisposta().getEsito());
			evento.setDataRisposta(new Date());
			ge.registraEvento(evento);
			
			if(bd != null) bd.closeConnection();
		}
		return response;
	}

	private <T> T buildRisposta(NdpException e, T r) {
		if(r instanceof PaaInviaRTRisposta) {
			if(e.getFault().equals(FaultPa.PAA_SYSTEM_ERROR))
				log.error("Rifiutata RT con Fault " + e.getFault().toString() + ( e.getDescrizione() != null ? (": " + e.getDescrizione()) : ""), e);
			else
				log.error("Rifiutata RT con Fault " + e.getFault().toString() + ( e.getDescrizione() != null ? (": " + e.getDescrizione()) : ""));
			
			PaaInviaRTRisposta risposta = (PaaInviaRTRisposta) r;
			EsitoPaaInviaRT esito = new EsitoPaaInviaRT();
			esito.setEsito("KO");
			FaultBean fault = new FaultBean();
			fault.setId(e.getCodDominio());
			fault.setFaultCode(e.getFault().toString());
			fault.setDescription(e.getDescrizione());
			fault.setFaultString(e.getFault().getFaultString());
			esito.setFault(fault);
			risposta.setPaaInviaRTRisposta(esito);
		}

		if(r instanceof TipoInviaEsitoStornoRisposta) {
			if(e.getFault().equals(FaultPa.PAA_SYSTEM_ERROR))
				log.error("Rifiutata ER con Fault " + e.getFault().toString() + ( e.getDescrizione() != null ? (": " + e.getDescrizione()) : ""), e);
			else
				log.error("Rifiutata ER con Fault " + e.getFault().toString() + ( e.getDescrizione() != null ? (": " + e.getDescrizione()) : ""));
			
			TipoInviaEsitoStornoRisposta risposta = (TipoInviaEsitoStornoRisposta) r;
			risposta.setEsito("KO");
			FaultBean fault = new FaultBean();
			fault.setId(e.getCodDominio());
			fault.setFaultCode(e.getFault().toString());
			fault.setDescription(e.getDescrizione());
			fault.setFaultString(e.getFault().getFaultString());
			risposta.setFault(fault);
		}

		return r;
	}
}
