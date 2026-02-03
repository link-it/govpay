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

import org.apache.cxf.annotations.SchemaValidation.SchemaValidationType;
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
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.beans.EventoContext.Esito;
import it.govpay.core.exceptions.NdpException;
import it.govpay.core.exceptions.NdpException.FaultPa;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.logger.MessaggioDiagnosticoCostanti;
import jakarta.annotation.Resource;
import jakarta.jws.WebService;
import jakarta.xml.ws.WebServiceContext;

@WebService(serviceName = "PagamentiTelematiciRTservice",
endpointInterface = "it.gov.spcoop.nodopagamentispc.servizi.pagamentitelematicirt.PagamentiTelematiciRT",
targetNamespace = "http://NodoPagamentiSPC.spcoop.gov.it/servizi/PagamentiTelematiciRT",
portName = "PPTPort",
wsdlLocation = "/wsdl/PaPerNodo.wsdl")

@org.apache.cxf.annotations.SchemaValidation(type = SchemaValidationType.IN)

public class PagamentiTelematiciRTImpl implements PagamentiTelematiciRT {
	
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
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		appContext.getEventoCtx().setPrincipal(AutorizzazioneUtils.getPrincipal(authentication));

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
		appContext.getRequest().addGenericProperty(new Property(MessaggioDiagnosticoCostanti.PROPERTY_COD_DOMINIO, codDominio));
		appContext.getRequest().addGenericProperty(new Property(MessaggioDiagnosticoCostanti.PROPERTY_IUV, iuv));

		appContext.getEventoCtx().setCodDominio(codDominio);
		appContext.getEventoCtx().setIuv(iuv);
		appContext.getEventoCtx().setCcp(ccp);
		
		PaaInviaRTRisposta response = new PaaInviaRTRisposta();
		
		PagamentiTelematiciRTImpl.buildRisposta(new NdpException(FaultPa.PAA_SYSTEM_ERROR, codDominio, OPERAZIONE_NON_IMPLEMENTATA), response, log);

		appContext.getEventoCtx().setSottotipoEsito(FaultPa.PAA_SYSTEM_ERROR.name());
		appContext.getEventoCtx().setDescrizioneEsito(OPERAZIONE_NON_IMPLEMENTATA);
		appContext.getEventoCtx().setEsito(Esito.FAIL);

		GpContext.setResult(appContext.getTransaction(), response.getPaaInviaRTRisposta().getFault().getFaultCode());

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
