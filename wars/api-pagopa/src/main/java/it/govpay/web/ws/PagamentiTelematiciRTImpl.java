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
package it.govpay.web.ws;

import jakarta.annotation.Resource;
import jakarta.jws.WebService;
import jakarta.xml.ws.WebServiceContext;

import org.apache.cxf.annotations.SchemaValidation.SchemaValidationType;
import org.openspcoop2.generic_project.exception.NotAuthorizedException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.utils.LoggerWrapperFactory;
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
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Stazione;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.beans.EventoContext.Esito;
import it.govpay.core.exceptions.NdpException;
import it.govpay.core.exceptions.NdpException.FaultPa;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.RtUtils;
import it.govpay.core.utils.logger.MessaggioDiagnosticoCostanti;
import it.govpay.core.utils.logger.MessaggioDiagnosticoUtils;
import it.govpay.model.Intermediario;
import it.govpay.model.eventi.DatiPagoPA;

@WebService(serviceName = "PagamentiTelematiciRTservice",
endpointInterface = "it.gov.spcoop.nodopagamentispc.servizi.pagamentitelematicirt.PagamentiTelematiciRT",
targetNamespace = "http://NodoPagamentiSPC.spcoop.gov.it/servizi/PagamentiTelematiciRT",
portName = "PPTPort",
wsdlLocation = "/wsdl/PaPerNodo.wsdl")

@org.apache.cxf.annotations.SchemaValidation(type = SchemaValidationType.IN)

public class PagamentiTelematiciRTImpl implements PagamentiTelematiciRT {
	
	private static final String MSG_NESSUNA_DESCRIZIONE = "<Nessuna descrizione>";

	private static final String OPERAZIONE_NON_IMPLEMENTATA = "Non implementato";

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

		TipoInviaEsitoStornoRisposta risposta = new TipoInviaEsitoStornoRisposta();
		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		appContext.getEventoCtx().setPrincipal(AutorizzazioneUtils.getPrincipal(authentication));

		appContext.getRequest().addGenericProperty(new Property(MessaggioDiagnosticoCostanti.PROPERTY_CCP, codiceContestoPagamento));
		appContext.getRequest().addGenericProperty(new Property(MessaggioDiagnosticoCostanti.PROPERTY_COD_DOMINIO, identificativoDominio));
		appContext.getRequest().addGenericProperty(new Property(MessaggioDiagnosticoCostanti.PROPERTY_IUV, identificativoUnivocoVersamento));

		appContext.getEventoCtx().setCodDominio(identificativoDominio);
		appContext.getEventoCtx().setIuv(identificativoUnivocoVersamento);
		appContext.getEventoCtx().setCcp(codiceContestoPagamento);

		FaultBean fault = new FaultBean();
		fault.setId(identificativoDominio);
		fault.setFaultCode(FaultPa.PAA_SYSTEM_ERROR.name());
		fault.setFaultString(FaultPa.PAA_SYSTEM_ERROR.getFaultString());
		fault.setDescription(OPERAZIONE_NON_IMPLEMENTATA);

		risposta.setFault(fault);
		risposta.setEsito("KO");
		appContext.getEventoCtx().setSottotipoEsito(FaultPa.PAA_SYSTEM_ERROR.name());
		appContext.getEventoCtx().setDescrizioneEsito(OPERAZIONE_NON_IMPLEMENTATA);
		appContext.getEventoCtx().setEsito(Esito.FAIL);

		GpContext.setResult(appContext.getTransaction(), risposta.getFault().getFaultCode());

		return risposta;
	}

	@Override
	public PaaInviaRTRisposta paaInviaRT(PaaInviaRT bodyrichiesta, IntestazionePPT header) {
		return paaInviaRTImpl(bodyrichiesta, header, log);
	}

	public static PaaInviaRTRisposta paaInviaRTImpl(PaaInviaRT bodyrichiesta, IntestazionePPT header, Logger log) {
		String ccp = header.getCodiceContestoPagamento();
		String codDominio = header.getIdentificativoDominio();
		String iuv = header.getIdentificativoUnivocoVersamento();

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

		appContext.getRequest().addGenericProperty(new Property(MessaggioDiagnosticoCostanti.PROPERTY_CCP, ccp));
		appContext.getRequest().addGenericProperty(new Property(codDominio, codDominio));
		appContext.getRequest().addGenericProperty(new Property(MessaggioDiagnosticoCostanti.PROPERTY_IUV, iuv));

		appContext.getEventoCtx().setCodDominio(codDominio);
		appContext.getEventoCtx().setIuv(iuv);
		appContext.getEventoCtx().setCcp(ccp);

		MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_PAGAMENTO_RICEZIONE_RT);

		log.info("Ricevuta richiesta di acquisizione RT [{}][{}][{}]",codDominio,iuv,ccp);
		PaaInviaRTRisposta response = new PaaInviaRTRisposta();

		DatiPagoPA datiPagoPA = new DatiPagoPA();
		datiPagoPA.setCodStazione(header.getIdentificativoStazioneIntermediarioPA());
		datiPagoPA.setFruitore(GpContext.NodoDeiPagamentiSPC);
		datiPagoPA.setCodDominio(codDominio);
		datiPagoPA.setErogatore(header.getIdentificativoIntermediarioPA());
		datiPagoPA.setCodIntermediario(header.getIdentificativoIntermediarioPA());
		appContext.getEventoCtx().setDatiPagoPA(datiPagoPA);

		try {
			BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			appContext.getEventoCtx().setPrincipal(AutorizzazioneUtils.getPrincipal(authentication));
			if(GovpayConfig.getInstance().isPddAuthEnable() && authentication == null) {
				MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_RT_ERRORE_NO_AUTORIZZAZIONE);
				throw new NotAuthorizedException("Autorizzazione fallita: principal non fornito");
			}

			Intermediario intermediario = null;
			try {
				intermediario = AnagraficaManager.getIntermediario(configWrapper, header.getIdentificativoIntermediarioPA());

				// Controllo autorizzazione
				if(GovpayConfig.getInstance().isPddAuthEnable()){
					boolean authOk = AuthorizationManager.checkPrincipal(authentication, intermediario.getPrincipal());

					if(!authOk) {
						GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(authentication);
						String principal = details.getIdentificativo();
						MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_RT_ERRORE_AUTORIZZAZIONE, principal);
						throw new NotAuthorizedException("Autorizzazione fallita: principal fornito (" + principal + ") non valido per l'intermediario (" + header.getIdentificativoIntermediarioPA() + ").");
					}
				}
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_ID_INTERMEDIARIO_ERRATO, codDominio);
			}

			Dominio dominio = null;
			try {
				dominio = AnagraficaManager.getDominio(configWrapper, codDominio);
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_ID_DOMINIO_ERRATO, codDominio);
			}

			Stazione stazione = null;
			try {
				stazione = AnagraficaManager.getStazione(configWrapper, header.getIdentificativoStazioneIntermediarioPA());
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_STAZIONE_INT_ERRATA, codDominio);
			}

			if(stazione.getIdIntermediario() != intermediario.getId()) {
				throw new NdpException(FaultPa.PAA_ID_INTERMEDIARIO_ERRATO, codDominio);
			}

			if(dominio.getIdStazione().compareTo(stazione.getId())!=0) {
				throw new NdpException(FaultPa.PAA_STAZIONE_INT_ERRATA, codDominio);
			}

			Rpt rpt = RtUtils.acquisisciRT(codDominio, iuv, ccp, bodyrichiesta.getRt(), false);

			appContext.getEventoCtx().setIdA2A(rpt.getVersamento(configWrapper).getApplicazione(configWrapper).getCodApplicazione());
			appContext.getEventoCtx().setIdPendenza(rpt.getVersamento(configWrapper).getCodVersamentoEnte());
			if(rpt.getIdPagamentoPortale() != null)
				appContext.getEventoCtx().setIdPagamento(rpt.getPagamentoPortale(configWrapper).getIdSessione());

			appContext.getResponse().addGenericProperty(new Property(MessaggioDiagnosticoCostanti.PROPERTY_ESITO_PAGAMENTO, rpt.getEsitoPagamento().toString()));
			MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_PAGAMENTO_ACQUISIZIONE_RT_OK);

			datiPagoPA.setCodPsp(rpt.getCodPsp());
			datiPagoPA.setCodCanale(rpt.getCodCanale());
			datiPagoPA.setTipoVersamento(rpt.getTipoVersamento());
			datiPagoPA.setModelloPagamento(rpt.getModelloPagamento().getCodifica() + "");

			EsitoPaaInviaRT esito = new EsitoPaaInviaRT();
			esito.setEsito("OK");
			response.setPaaInviaRTRisposta(esito);
			MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_RT_RICEZIONE_OK);
			appContext.getEventoCtx().setDescrizioneEsito("Acquisita ricevuta di pagamento [IUV: " + rpt.getIuv() + " CCP:" + rpt.getCcp() + "] emessa da " + rpt.getDenominazioneAttestante());
			appContext.getEventoCtx().setEsito(Esito.OK);
		} catch (NdpException e) {
			response = PagamentiTelematiciRTImpl.buildRisposta(e, response, log);
			String faultDescription = response.getPaaInviaRTRisposta().getFault().getDescription() == null ? MSG_NESSUNA_DESCRIZIONE : response.getPaaInviaRTRisposta().getFault().getDescription();
			MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_RT_RICEZIONE_KO, response.getPaaInviaRTRisposta().getFault().getFaultCode(), response.getPaaInviaRTRisposta().getFault().getFaultString(), faultDescription);
			if(e.getFaultCode().equals(FaultPa.PAA_SYSTEM_ERROR.name()))
				appContext.getEventoCtx().setEsito(Esito.FAIL);
			else
				appContext.getEventoCtx().setEsito(Esito.KO);
			appContext.getEventoCtx().setDescrizioneEsito(faultDescription);
			appContext.getEventoCtx().setSottotipoEsito(e.getFaultCode());
		} catch (Exception e) {
			response = PagamentiTelematiciRTImpl.buildRisposta(new NdpException(FaultPa.PAA_SYSTEM_ERROR, codDominio, e.getMessage(), e), response, log);
			String faultDescription = response.getPaaInviaRTRisposta().getFault().getDescription() == null ? MSG_NESSUNA_DESCRIZIONE : response.getPaaInviaRTRisposta().getFault().getDescription();
			MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_RT_RICEZIONE_KO, response.getPaaInviaRTRisposta().getFault().getFaultCode(), response.getPaaInviaRTRisposta().getFault().getFaultString(), faultDescription);
			appContext.getEventoCtx().setSottotipoEsito(response.getPaaInviaRTRisposta().getFault().getFaultCode());
			appContext.getEventoCtx().setEsito(Esito.FAIL);
			appContext.getEventoCtx().setDescrizioneEsito(faultDescription);
		} finally {
			GpContext.setResult(appContext.getTransaction(), response.getPaaInviaRTRisposta().getFault() == null ? null : response.getPaaInviaRTRisposta().getFault().getFaultCode());
		}
		return response;
	}

	private static PaaInviaRTRisposta buildRisposta(NdpException e, PaaInviaRTRisposta risposta, Logger log) {
		if(e.getFaultCode().equals(FaultPa.PAA_SYSTEM_ERROR.name()))
			log.error("Rifiutata RT con Fault " + e.getFaultString() + ( e.getDescrizione() != null ? (": " + e.getDescrizione()) : ""), e);
		else
			log.warn("Rifiutata RT con Fault {}" ,(e.getFaultString() + ( e.getDescrizione() != null ? (": " + e.getDescrizione()) : "")));

		EsitoPaaInviaRT esito = new EsitoPaaInviaRT();
		esito.setEsito("KO");
		FaultBean fault = new FaultBean();
		fault.setId(e.getCodDominio());
		fault.setFaultCode(e.getFaultCode());
		fault.setFaultString(e.getFaultString());
		fault.setDescription(e.getDescrizione());
		esito.setFault(fault);
		risposta.setPaaInviaRTRisposta(esito);

		return risposta;
	}

	@Override
	public TipoInviaRichiestaRevocaRisposta paaInviaRichiestaRevoca(String identificativoDominio, String identificativoUnivocoVersamento, String codiceContestoPagamento, byte[] rr) {
		TipoInviaRichiestaRevocaRisposta risposta = new TipoInviaRichiestaRevocaRisposta();

		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		appContext.getEventoCtx().setPrincipal(AutorizzazioneUtils.getPrincipal(authentication));

		appContext.getRequest().addGenericProperty(new Property(MessaggioDiagnosticoCostanti.PROPERTY_CCP, codiceContestoPagamento));
		appContext.getRequest().addGenericProperty(new Property(MessaggioDiagnosticoCostanti.PROPERTY_COD_DOMINIO, identificativoDominio));
		appContext.getRequest().addGenericProperty(new Property(MessaggioDiagnosticoCostanti.PROPERTY_IUV, identificativoUnivocoVersamento));

		appContext.getEventoCtx().setCodDominio(identificativoDominio);
		appContext.getEventoCtx().setIuv(identificativoUnivocoVersamento);
		appContext.getEventoCtx().setCcp(codiceContestoPagamento);

		FaultBean fault = new FaultBean();
		fault.setId(identificativoDominio);
		fault.setFaultCode(FaultPa.PAA_SYSTEM_ERROR.name());
		fault.setFaultString(FaultPa.PAA_SYSTEM_ERROR.getFaultString());
		fault.setDescription(OPERAZIONE_NON_IMPLEMENTATA);

		risposta.setFault(fault);
		risposta.setEsito("KO");
		appContext.getEventoCtx().setSottotipoEsito(FaultPa.PAA_SYSTEM_ERROR.name());
		appContext.getEventoCtx().setDescrizioneEsito(OPERAZIONE_NON_IMPLEMENTATA);
		appContext.getEventoCtx().setEsito(Esito.FAIL);

		GpContext.setResult(appContext.getTransaction(), risposta.getFault().getFaultCode());

		return risposta;
	}
}
