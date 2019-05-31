/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
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
package it.govpay.web.ws;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import org.apache.cxf.annotations.SchemaValidation.SchemaValidationType;
import org.openspcoop2.generic_project.exception.NotAuthorizedException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.logger.beans.context.core.Actor;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import gov.telematici.pagamenti.ws.ppthead.IntestazionePPT;
import gov.telematici.pagamenti.ws.rt.EsitoPaaInviaRT;
import gov.telematici.pagamenti.ws.rt.FaultBean;
import gov.telematici.pagamenti.ws.rt.PaaInviaRT;
import gov.telematici.pagamenti.ws.rt.PaaInviaRTRisposta;
import gov.telematici.pagamenti.ws.rt.TipoInviaEsitoStornoRisposta;
import gov.telematici.pagamenti.ws.rt.TipoInviaRichiestaRevocaRisposta;
import it.gov.spcoop.nodopagamentispc.servizi.pagamentitelematicirt.PagamentiTelematiciRT;
import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rr;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.model.eventi.Controparte;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.exceptions.NdpException;
import it.govpay.core.exceptions.NdpException.FaultPa;
import it.govpay.core.utils.EventoContext.Esito;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.RrUtils;
import it.govpay.core.utils.RtUtils;
import it.govpay.model.Intermediario;

@WebService(serviceName = "PagamentiTelematiciRTservice",
endpointInterface = "it.gov.spcoop.nodopagamentispc.servizi.pagamentitelematicirt.PagamentiTelematiciRT",
targetNamespace = "http://NodoPagamentiSPC.spcoop.gov.it/servizi/PagamentiTelematiciRT",
portName = "PPTPort",
wsdlLocation = "/wsdl/PaPerNodo.wsdl")

@org.apache.cxf.annotations.SchemaValidation(type = SchemaValidationType.IN)

//@HandlerChain(file="../../../../handler-chains/handler-chain-ndp.xml")

public class PagamentiTelematiciRTImpl implements PagamentiTelematiciRT {

	@Resource
	WebServiceContext wsCtxt;

	private static Logger log = LoggerWrapperFactory.getLogger(PagamentiTelematiciRTImpl.class);

	@Override
	public TipoInviaEsitoStornoRisposta paaInviaEsitoStorno(
			String identificativoIntermediarioPA,
			String identificativoStazioneIntermediarioPA,
			String identificativoDominio,
			String identificativoUnivocoVersamento,
			String codiceContestoPagamento, byte[] er) {

		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();

		appContext.setCorrelationId(identificativoDominio + identificativoUnivocoVersamento + codiceContestoPagamento);

		Actor from = new Actor();
		from.setName(GpContext.NodoDeiPagamentiSPC);
		from.setType(GpContext.TIPO_SOGGETTO_NDP);
		appContext.getTransaction().setFrom(from);

		Actor to = new Actor();
		to.setName(identificativoStazioneIntermediarioPA);
		from.setType(GpContext.TIPO_SOGGETTO_STAZIONE);
		appContext.getTransaction().setTo(to);

		appContext.getRequest().addGenericProperty(new Property("ccp", codiceContestoPagamento));
		appContext.getRequest().addGenericProperty(new Property("codDominio", identificativoDominio));
		appContext.getRequest().addGenericProperty(new Property("iuv", identificativoUnivocoVersamento));
		try {
			ctx.getApplicationLogger().log("er.ricezione");
		} catch (UtilsException e) {
			log.error("Errore durante il log dell'operazione: " + e.getMessage(),e);
		}

		log.info("Ricevuta richiesta di acquisizione ER [" + identificativoDominio + "][" + identificativoUnivocoVersamento + "][" + codiceContestoPagamento + "]");

		TipoInviaEsitoStornoRisposta response = new TipoInviaEsitoStornoRisposta();

		BasicBD bd = null;

		Controparte controparte = new Controparte();
		controparte.setCodStazione(identificativoStazioneIntermediarioPA);
		controparte.setFruitore(GpContext.NodoDeiPagamentiSPC);
		appContext.getEventoCtx().setControparte(controparte);

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if(GovpayConfig.getInstance().isPddAuthEnable() && authentication == null) {
				ctx.getApplicationLogger().log("er.erroreNoAutorizzazione");
				throw new NotAuthorizedException("Autorizzazione fallita: principal non fornito");
			}

			Intermediario intermediario = null;
			try {
				intermediario = AnagraficaManager.getIntermediario(bd, identificativoIntermediarioPA);

				// Controllo autorizzazione
				if(GovpayConfig.getInstance().isPddAuthEnable()){
					boolean authOk = AuthorizationManager.checkPrincipal(authentication, intermediario.getPrincipal()); 
					
					if(!authOk) {
						GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(authentication);
						String principal = details.getIdentificativo(); 
						ctx.getApplicationLogger().log("er.erroreAutorizzazione", principal);
						throw new NotAuthorizedException("Autorizzazione fallita: principal fornito (" + principal + ") non valido per l'intermediario (" + identificativoIntermediarioPA + ").");
					}
				}

				controparte.setErogatore(intermediario.getDenominazione());
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_ID_INTERMEDIARIO_ERRATO, identificativoDominio);
			}


			Dominio dominio = null;
			try {
				dominio = AnagraficaManager.getDominio(bd, identificativoDominio);
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_ID_DOMINIO_ERRATO, identificativoDominio);
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
			
			
			appContext.getEventoCtx().setIdRpt(rr.getRpt(bd).getId());
			appContext.getEventoCtx().setIdVersamento(rr.getRpt(bd).getVersamento(bd).getId());
			appContext.getEventoCtx().setIdPagamentoPortale(rr.getRpt(bd).getIdPagamentoPortale());
			controparte.setCodCanale(rr.getRpt(bd).getCodCanale());
			controparte.setTipoVersamento(rr.getRpt(bd).getTipoVersamento());
			response.setEsito("OK");
			appContext.getEventoCtx().setEsito(Esito.OK);
			ctx.getApplicationLogger().log("er.ricezioneOk");
		} catch (NdpException e) {
			if(bd != null) bd.rollback();
			response = this.buildRisposta(e, response);
			String faultDescription = response.getFault().getDescription() == null ? "<Nessuna descrizione>" : response.getFault().getDescription(); 
			try {
				ctx.getApplicationLogger().log("er.ricezioneKo", response.getFault().getFaultCode(), response.getFault().getFaultString(), faultDescription);
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
			appContext.getEventoCtx().setDescrizioneEsito(faultDescription);
			appContext.getEventoCtx().setEsito(Esito.FAIL);
		} catch (Exception e) {
			if(bd != null) bd.rollback();
			response = this.buildRisposta(new NdpException(FaultPa.PAA_SYSTEM_ERROR, identificativoDominio, e.getMessage(), e), response);
			String faultDescription = response.getFault().getDescription() == null ? "<Nessuna descrizione>" : response.getFault().getDescription(); 
			try {
				ctx.getApplicationLogger().log("er.ricezioneKo", response.getFault().getFaultCode(), response.getFault().getFaultString(), faultDescription);
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
			appContext.getEventoCtx().setDescrizioneEsito(faultDescription);
			appContext.getEventoCtx().setEsito(Esito.FAIL);
		} finally {
			
			GpContext.setResult(appContext.getTransaction(),response.getFault() == null ? null : response.getFault().getFaultCode());
			if(bd != null) bd.closeConnection();
		}
		return response;
	}

	@Override
	public PaaInviaRTRisposta paaInviaRT(PaaInviaRT bodyrichiesta, IntestazionePPT header) {

		String ccp = header.getCodiceContestoPagamento();
		String codDominio = header.getIdentificativoDominio();
		String iuv = header.getIdentificativoUnivocoVersamento();
		
		Long idVersamentoLong = null;
		Long idPagamentoPortaleLong = null;
		Long idRptLong = null;

		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();

		appContext.setCorrelationId(codDominio + iuv + ccp);

		Actor from = new Actor();
		from.setName(GpContext.NodoDeiPagamentiSPC);
		from.setType(GpContext.TIPO_SOGGETTO_NDP);
		appContext.getTransaction().setFrom(from);

		Actor to = new Actor();
		to.setName(header.getIdentificativoStazioneIntermediarioPA());
		from.setType(GpContext.TIPO_SOGGETTO_STAZIONE);
		appContext.getTransaction().setTo(to);

		appContext.getRequest().addGenericProperty(new Property("ccp", ccp));
		appContext.getRequest().addGenericProperty(new Property("codDominio", codDominio));
		appContext.getRequest().addGenericProperty(new Property("iuv", iuv));
		try {
			ctx.getApplicationLogger().log("pagamento.ricezioneRt");
		} catch (UtilsException e) {
			log.error("Errore durante il log dell'operazione: " + e.getMessage(),e);
		}
		
		log.info("Ricevuta richiesta di acquisizione RT [" + codDominio + "][" + iuv + "][" + ccp + "]");
		PaaInviaRTRisposta response = new PaaInviaRTRisposta();

		BasicBD bd = null;

		Controparte controparte = new Controparte();
		controparte.setCodStazione(header.getIdentificativoStazioneIntermediarioPA());
		controparte.setFruitore(GpContext.NodoDeiPagamentiSPC);
		appContext.getEventoCtx().setControparte(controparte);

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if(GovpayConfig.getInstance().isPddAuthEnable() && authentication == null) {
				ctx.getApplicationLogger().log("rt.erroreNoAutorizzazione");
				throw new NotAuthorizedException("Autorizzazione fallita: principal non fornito");
			}

			Intermediario intermediario = null;
			try {
				intermediario = AnagraficaManager.getIntermediario(bd, header.getIdentificativoIntermediarioPA());

				// Controllo autorizzazione
				if(GovpayConfig.getInstance().isPddAuthEnable()){
					boolean authOk = AuthorizationManager.checkPrincipal(authentication, intermediario.getPrincipal()); 
					
					if(!authOk) {
						GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(authentication);
						String principal = details.getIdentificativo(); 
						ctx.getApplicationLogger().log("rt.erroreAutorizzazione", principal);
						throw new NotAuthorizedException("Autorizzazione fallita: principal fornito (" + principal + ") non valido per l'intermediario (" + header.getIdentificativoIntermediarioPA() + ").");
					}
				}

				controparte.setErogatore(intermediario.getDenominazione());
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_ID_INTERMEDIARIO_ERRATO, codDominio);
			}

			Dominio dominio = null;
			try {
				dominio = AnagraficaManager.getDominio(bd, codDominio);
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_ID_DOMINIO_ERRATO, codDominio);
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

			Rpt rpt = RtUtils.acquisisciRT(codDominio, iuv, ccp, bodyrichiesta.getTipoFirma(), bodyrichiesta.getRt(), false, bd);
			
			idVersamentoLong = rpt.getVersamento(bd).getId();
			idPagamentoPortaleLong = rpt.getIdPagamentoPortale();
			idRptLong = rpt.getId();
			
			appContext.getEventoCtx().setIdVersamento(idVersamentoLong);
			appContext.getEventoCtx().setIdPagamentoPortale(idPagamentoPortaleLong);
			appContext.getEventoCtx().setIdRpt(idRptLong);
			
			appContext.getResponse().addGenericProperty(new Property("esitoPagamento", rpt.getEsitoPagamento().toString()));
			ctx.getApplicationLogger().log("pagamento.acquisizioneRtOk");
			
			controparte.setCodCanale(rpt.getCodCanale());
			controparte.setTipoVersamento(rpt.getTipoVersamento());

			EsitoPaaInviaRT esito = new EsitoPaaInviaRT();
			esito.setEsito("OK");
			response.setPaaInviaRTRisposta(esito);
			ctx.getApplicationLogger().log("rt.ricezioneOk");
			appContext.getEventoCtx().setDescrizioneEsito("Acquisita ricevuta di pagamento [IUV: " + rpt.getIuv() + " CCP:" + rpt.getCcp() + "] emessa da " + rpt.getDenominazioneAttestante());
			appContext.getEventoCtx().setEsito(Esito.OK);
		} catch (NdpException e) {
			if(bd != null) bd.rollback();
			response = this.buildRisposta(e, response);
			String faultDescription = response.getPaaInviaRTRisposta().getFault().getDescription() == null ? "<Nessuna descrizione>" : response.getPaaInviaRTRisposta().getFault().getDescription(); 
			try {
				ctx.getApplicationLogger().log("rt.ricezioneKo", response.getPaaInviaRTRisposta().getFault().getFaultCode(), response.getPaaInviaRTRisposta().getFault().getFaultString(), faultDescription);
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
			appContext.getEventoCtx().setEsito(Esito.FAIL);
			appContext.getEventoCtx().setDescrizioneEsito(faultDescription);
		} catch (Exception e) {
			if(bd != null) bd.rollback();
			response = this.buildRisposta(new NdpException(FaultPa.PAA_SYSTEM_ERROR, codDominio, e.getMessage(), e), response);
			String faultDescription = response.getPaaInviaRTRisposta().getFault().getDescription() == null ? "<Nessuna descrizione>" : response.getPaaInviaRTRisposta().getFault().getDescription(); 
			try {
				ctx.getApplicationLogger().log("rt.ricezioneKo", response.getPaaInviaRTRisposta().getFault().getFaultCode(), response.getPaaInviaRTRisposta().getFault().getFaultString(), faultDescription);
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
			appContext.getEventoCtx().setEsito(Esito.FAIL);
			appContext.getEventoCtx().setDescrizioneEsito(faultDescription);
		} finally {
			
			GpContext.setResult(appContext.getTransaction(), response.getPaaInviaRTRisposta().getFault() == null ? null : response.getPaaInviaRTRisposta().getFault().getFaultCode());
			if(bd != null) bd.closeConnection();
		}
		return response;
	}

	private <T> T buildRisposta(NdpException e, T r) {
		if(r instanceof PaaInviaRTRisposta) {
			if(e.getFaultCode().equals(FaultPa.PAA_SYSTEM_ERROR.name())) 
				log.error("Rifiutata RT con Fault " + e.getFaultString() + ( e.getDescrizione() != null ? (": " + e.getDescrizione()) : ""), e);
			else
				log.warn("Rifiutata RT con Fault " + e.getFaultString() + ( e.getDescrizione() != null ? (": " + e.getDescrizione()) : ""));

			PaaInviaRTRisposta risposta = (PaaInviaRTRisposta) r;
			EsitoPaaInviaRT esito = new EsitoPaaInviaRT();
			esito.setEsito("KO");
			FaultBean fault = new FaultBean();
			fault.setId(e.getCodDominio());
			fault.setFaultCode(e.getFaultCode());
			fault.setFaultString(e.getFaultString());
			fault.setDescription(e.getDescrizione());
			esito.setFault(fault);
			risposta.setPaaInviaRTRisposta(esito);
		}

		if(r instanceof TipoInviaEsitoStornoRisposta) {
			if(e.getFaultCode().equals(FaultPa.PAA_SYSTEM_ERROR.name())) 
				log.error("Rifiutata ER con Fault " + e.getFaultString() + ( e.getDescrizione() != null ? (": " + e.getDescrizione()) : ""), e);
			else
				log.warn("Rifiutata ER con Fault " + e.getFaultString() + ( e.getDescrizione() != null ? (": " + e.getDescrizione()) : ""));

			TipoInviaEsitoStornoRisposta risposta = (TipoInviaEsitoStornoRisposta) r;
			risposta.setEsito("KO");
			FaultBean fault = new FaultBean();
			fault.setId(e.getCodDominio());
			fault.setFaultCode(e.getFaultCode());
			fault.setFaultString(e.getFaultString());
			fault.setDescription(e.getDescrizione());
			risposta.setFault(fault);
		}

		return r;
	}

	@Override
	public TipoInviaRichiestaRevocaRisposta paaInviaRichiestaRevoca(String identificativoDominio, String identificativoUnivocoVersamento, String codiceContestoPagamento, byte[] rr) {
		TipoInviaRichiestaRevocaRisposta risposta = new TipoInviaRichiestaRevocaRisposta();
		
		FaultBean fault = new FaultBean();
		fault.setId(identificativoDominio);
		fault.setFaultCode(FaultPa.PAA_SYSTEM_ERROR.name());
		fault.setFaultString(FaultPa.PAA_SYSTEM_ERROR.getFaultString());
		fault.setDescription("Non implementato");
		
		risposta.setFault(fault);
		return risposta;
	}
}
