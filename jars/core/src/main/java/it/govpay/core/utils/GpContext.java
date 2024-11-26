/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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

import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
//import javax.ws.rs.core.HttpHeaders;
//import javax.ws.rs.core.UriInfo;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;

import org.openspcoop2.utils.logger.beans.context.application.ApplicationContext;
import org.openspcoop2.utils.logger.beans.context.application.ApplicationTransaction;
import org.openspcoop2.utils.logger.beans.context.core.AbstractTransaction;
import org.openspcoop2.utils.logger.beans.context.core.Actor;
import org.openspcoop2.utils.logger.beans.context.core.BaseClient;
import org.openspcoop2.utils.logger.beans.context.core.BaseServer;
import org.openspcoop2.utils.logger.beans.context.core.HttpClient;
import org.openspcoop2.utils.logger.beans.context.core.HttpServer;
import org.openspcoop2.utils.logger.beans.context.core.Operation;
import org.openspcoop2.utils.logger.beans.context.core.Request;
import org.openspcoop2.utils.logger.beans.context.core.Role;
import org.openspcoop2.utils.logger.beans.context.core.Service;
import org.openspcoop2.utils.logger.constants.context.FlowMode;
import org.openspcoop2.utils.logger.constants.context.Result;
import org.springframework.security.core.context.SecurityContextHolder;

import it.gov.spcoop.nodopagamentispc.servizi.pagamentitelematicirpt.PagamentiTelematiciRPTservice;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.beans.EventoContext;
import it.govpay.core.beans.EventoContext.Categoria;
import it.govpay.core.beans.EventoContext.Componente;
import it.govpay.core.beans.GpResponse;
import it.govpay.core.exceptions.NdpException.FaultPa;
import it.govpay.model.Evento.RuoloEvento;
import it.govpay.model.Versionabile.Versione;

public class GpContext extends ApplicationContext {

	public static final String UNKNOWN = "<Unknown>";
	public static final String TIPO_PROTOCOLLO_REST = "REST";
	public static final String TIPO_PROTOCOLLO_WS = "WS";
	public static final String TIPO_PROTOCOLLO_TASK = "TASK";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 
	private PagamentoContext pagamentoCtx;
	private EventoContext eventoCtx;
	
	// Mappa da utilizzare all'interno delle trasformazioni si puo' utilizzare per salvare dei dati all'interno dei template e utilizzarli dopo la fine della trasformazione.
	private Hashtable<String, Object> ctx = new Hashtable<String, Object>();
	
	public Hashtable<String, Object> getContext() {
		return this.ctx;
	}

	public static final String NOT_SET = "<Non valorizzato>";

	public static final String NodoDeiPagamentiSPC = "NodoDeiPagamentiSPC";
	public static final String GOVPAY = "GovPay";
	public static final String APPIO = "AppIO";
	public static final String TIPO_SOGGETTO_NDP = "NDP";
	public static final String TIPO_SERVIZIO_NDP = "NDP";
	public static final String CHECKOUT = "Checkout";

	public static final String TIPO_SOGGETTO_APP = "APP";
	public static final String TIPO_SOGGETTO_PRT = "PRT";
	public static final String TIPO_SOGGETTO_STAZIONE = "STZ";
	public static final String TIPO_SOGGETTO_GOVPAY = "GP";

	public static final String TIPO_SERVIZIO_GOVPAY = "GP";
	public static final String TIPO_SERVIZIO_GOVPAY_RS = "GPRS";
	public static final String TIPO_SERVIZIO_GOVPAY_JSON = "GPJSON";
	public static final String TIPO_SERVIZIO_GOVPAY_WS = "GPWS";
	public static final String TIPO_SERVIZIO_GOVPAY_OPT = "GPO";
	
	public static final String MYPIVOT = "MyPivot";
	public static final String MAGGIOLIJPPA = "MaggioliJPPA";
	public static final String TIPO_SOGGETTO_MAGGIOLI_JPPA = "MaggioliJPPA";


	public GpContext() {
		super();
	}

	public GpContext(MessageContext msgCtx, String tipoServizio, int versioneServizio, Componente componente) {
		this();
		popolaGpContext(this, msgCtx, tipoServizio, versioneServizio, componente);
	}
	
	public static void popolaGpContext(GpContext ctx, MessageContext msgCtx, String tipoServizio, int versioneServizio, Componente componente) {
		ApplicationTransaction transaction = ctx.getTransaction();
		transaction.setRole(Role.SERVER);
		transaction.setProtocol(TIPO_PROTOCOLLO_WS);

		Service service = new Service();
		if(msgCtx.get(MessageContext.WSDL_SERVICE) != null)
			service.setName(((QName) msgCtx.get(MessageContext.WSDL_SERVICE)).getLocalPart());
		else 
			service.setName(UNKNOWN);
		service.setVersion(versioneServizio);
		service.setType(tipoServizio);

		transaction.setService(service);

		Operation operation = new Operation();
		operation.setMode(FlowMode.INPUT_OUTPUT);
		if(msgCtx.get(MessageContext.WSDL_OPERATION) != null)
			operation.setName(((QName) msgCtx.get(MessageContext.WSDL_OPERATION)).getLocalPart());
		else 
			operation.setName(UNKNOWN);
		transaction.setOperation(operation);

		HttpServletRequest servletRequest = (HttpServletRequest) msgCtx.get(MessageContext.SERVLET_REQUEST);
		BaseClient client = new HttpClient();
		client.setInvocationEndpoint(servletRequest.getRequestURI());

		if(msgCtx.get(MessageContext.WSDL_INTERFACE) != null)
			client.setInterfaceName(((QName) msgCtx.get(MessageContext.WSDL_INTERFACE)).getLocalPart());
		else 
			client.setInterfaceName(UNKNOWN);

		String user = AutorizzazioneUtils.getPrincipal(SecurityContextHolder.getContext().getAuthentication());

		if(user != null)
			client.setPrincipal(user);
		transaction.setClient(client);

		ctx.getEventoCtx().setCategoriaEvento(Categoria.INTERFACCIA);
		ctx.getEventoCtx().setRole(RuoloEvento.SERVER);
		ctx.getEventoCtx().setDataRichiesta(new Date());
		ctx.getEventoCtx().setMethod((String) msgCtx.get(MessageContext.HTTP_REQUEST_METHOD));
		ctx.getEventoCtx().setComponente(componente);
		ctx.getEventoCtx().setUrl(servletRequest.getRequestURI());
		ctx.getEventoCtx().setPrincipal(user);
		ctx.getEventoCtx().setTipoEvento(operation.getName());
	}

	public GpContext(String requestUri,	String nomeServizio, String nomeOperazione, String httpMethod, int versioneServizio, String user, Componente componente) {
		this();
		ApplicationTransaction transaction = this.getTransaction();
		transaction.setRole(Role.SERVER);
		transaction.setProtocol(TIPO_PROTOCOLLO_REST);

		Service service = new Service();
		service.setName(nomeServizio);
		service.setVersion(versioneServizio);
		service.setType(httpMethod);
		transaction.setService(service);

		Operation operation = new Operation();
		operation.setMode(FlowMode.INPUT_OUTPUT);
		operation.setName(nomeOperazione);
		transaction.setOperation(operation);

		BaseClient client = new BaseClient();
		client.setInvocationEndpoint(requestUri);
		client.setInterfaceName(nomeServizio);
		if(user != null) client.setPrincipal(user);
		transaction.setClient(client);

		HttpServer server = new HttpServer();
		server.setName(GOVPAY);

		Actor to = new Actor();
		to.setName(GOVPAY);
		to.setType(TIPO_SOGGETTO_GOVPAY);
		transaction.setTo(to);
		transaction.addServer(server);
		
		this.getEventoCtx().setCategoriaEvento(Categoria.INTERFACCIA);
		this.getEventoCtx().setRole(RuoloEvento.SERVER);
		this.getEventoCtx().setDataRichiesta(new Date());
		this.getEventoCtx().setMethod(httpMethod);
		this.getEventoCtx().setComponente(componente);
		this.getEventoCtx().setUrl(requestUri);
		this.getEventoCtx().setPrincipal(user);
		this.getEventoCtx().setTipoEvento(nomeOperazione);
	}

	public static GpContext newContext() {
		GpContext context = new GpContext();
		
		ApplicationTransaction transaction = context.getTransaction();
		transaction.setRole(Role.SERVER);
		transaction.setProtocol(TIPO_PROTOCOLLO_REST);

		Actor to = new Actor();
		to.setName(GOVPAY);
		to.setType(TIPO_SOGGETTO_GOVPAY);
		transaction.setTo(to);

		HttpServer server = new HttpServer();
		server.setName(GOVPAY);
		transaction.addServer(server);

		Request request = context.getRequest();
		request.setDate(new Date());
		
		context.getEventoCtx().setCategoriaEvento(Categoria.INTERFACCIA);
		context.getEventoCtx().setRole(RuoloEvento.SERVER);
		context.getEventoCtx().setDataRichiesta(new Date());

		return context;
	}

	public static GpContext newBatchContext() {
		GpContext context = new GpContext();
		
		ApplicationTransaction transaction = context.getTransaction();
		transaction.setRole(Role.CLIENT);
		transaction.setProtocol(TIPO_PROTOCOLLO_TASK);
		
		HttpServer server = new HttpServer();
		server.setName(GOVPAY);
		transaction.addServer(server);

		Request request = context.getRequest();
		request.setDate(new Date());
		
		context.getEventoCtx().setCategoriaEvento(Categoria.INTERNO);
		context.getEventoCtx().setRole(RuoloEvento.CLIENT);
		context.getEventoCtx().setDataRichiesta(new Date());
		
		return context;
	}
	
	public String setupNodoClient(String codDominio, String azione) {
		return this.setupNodoClient(null, codDominio, null, azione);
	}
	
	public String setupNodoClient(String codStazione, String codDominio, String azione) {
		return this.setupNodoClient(codStazione, codDominio, null, azione);
	}

	public String setupNodoClient(String codStazione, String codDominio, EventoContext.Azione azione) {
		return this.setupNodoClient(codStazione, codDominio, PagamentiTelematiciRPTservice.SERVICE.getLocalPart(), azione.toString());
	}

	public synchronized String setupNodoClient(String codStazione, String codDominio, String servizio, String azione) {
		HttpServer server = new HttpServer();
		server.setName(NodoDeiPagamentiSPC);
		server.setIdOperation(UUID.randomUUID().toString());
		this.getTransaction().addServer(server); 

		return server.getIdOperation();
	}

	public synchronized String setupPaClient(String codApplicazione, String azione, String url, Versione versione) {
		HttpServer server = new HttpServer();
		server.setName(codApplicazione);
		server.setEndpoint(url);
		server.setIdOperation(UUID.randomUUID().toString());
		this.getTransaction().addServer(server); 

		return server.getIdOperation();
	}
	
	public synchronized String setupAppIOClient(String azione, String url) {
		HttpServer server = new HttpServer();
		server.setName(APPIO);
		server.setEndpoint(url);
		server.setIdOperation(UUID.randomUUID().toString());
		this.getTransaction().addServer(server); 

		return server.getIdOperation();
	}
	
	public synchronized String setupNotificaPagamentiClient(String azione, String url) {
		HttpServer server = new HttpServer();
		server.setName(MYPIVOT);
		server.setEndpoint(url);
		server.setIdOperation(UUID.randomUUID().toString());
		this.getTransaction().addServer(server); 

		return server.getIdOperation();
	}
	
	public synchronized String setupInvioNotificaPagamentiMaggioliJPPAClient(String azione, String url) {
		HttpServer server = new HttpServer();
		server.setName(MAGGIOLIJPPA);
		server.setEndpoint(url);
		server.setIdOperation(UUID.randomUUID().toString());
		this.getTransaction().addServer(server); 

		return server.getIdOperation();
	}
	
	public synchronized String setupcheckoutClient(String azione, String url) {
		HttpServer server = new HttpServer();
		server.setName(CHECKOUT);
		server.setEndpoint(url);
		server.setIdOperation(UUID.randomUUID().toString());
		this.getTransaction().addServer(server); 

		return server.getIdOperation();
	}

	@Override
	public ApplicationTransaction getTransaction() {
		return (ApplicationTransaction) super.getTransaction(); 
	}

	public boolean hasCorrelationId() {
		try {
			return this.getRequest().getCorrelationIdentifier() != null;
		} catch (Throwable t) {
			return false;
		}
	}

	public void setCorrelationId(String id) {
		this.getRequest().setCorrelationIdentifier(id);
	}

	public static void setResult(AbstractTransaction transaction, GpResponse response) {
		if(response == null || response.getCodEsito() == null) {
			transaction.setResult(Result.INTERNAL_ERROR);
			return;
		}
		switch (response.getCodEsito()) {
		case "OK":
			transaction.setResult(Result.SUCCESS);
			break;
		case "INTERNAL":
			transaction.setResult(Result.INTERNAL_ERROR);
			break;
		default:
			transaction.setResult(Result.PROCESSING_ERROR);
			break;
		}
	}

	public static void setResult(AbstractTransaction transaction, String faultCode) {
		if(faultCode == null) {
			transaction.setResult(Result.SUCCESS);
			return;
		}

		if(faultCode.equals(FaultPa.PAA_SYSTEM_ERROR.name())) {
			transaction.setResult(Result.INTERNAL_ERROR);
			return; 
		}

		transaction.setResult(Result.PROCESSING_ERROR);
	}

	public PagamentoContext getPagamentoCtx() {
		if(this.pagamentoCtx == null) 
			this.pagamentoCtx = new PagamentoContext();
		return this.pagamentoCtx;
	}
	
	public EventoContext getEventoCtx() {
		if(this.eventoCtx == null) 
			this.eventoCtx = new EventoContext();
		return this.eventoCtx;
	}

	public synchronized BaseServer getServerByOperationId(String operationID) {
		List<BaseServer> servers = this.getTransaction().getServers();
		for (BaseServer baseServer : servers) {
			if(operationID.equals(baseServer.getIdOperation())) {
				return baseServer;
			}
		}
		return null;
	}
}
