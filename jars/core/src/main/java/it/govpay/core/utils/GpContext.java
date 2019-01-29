package it.govpay.core.utils;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.logger.beans.context.application.ApplicationContext;
import org.openspcoop2.utils.logger.beans.context.application.ApplicationTransaction;
import org.openspcoop2.utils.logger.beans.context.core.AbstractTransaction;
import org.openspcoop2.utils.logger.beans.context.core.Actor;
import org.openspcoop2.utils.logger.beans.context.core.BaseClient;
import org.openspcoop2.utils.logger.beans.context.core.BaseServer;
import org.openspcoop2.utils.logger.beans.context.core.Operation;
import org.openspcoop2.utils.logger.beans.context.core.Request;
import org.openspcoop2.utils.logger.beans.context.core.Role;
import org.openspcoop2.utils.logger.beans.context.core.Service;
import org.openspcoop2.utils.logger.constants.context.FlowMode;
import org.openspcoop2.utils.logger.constants.context.Result;
import org.springframework.security.core.context.SecurityContextHolder;

import it.gov.spcoop.nodopagamentispc.servizi.pagamentitelematicirpt.PagamentiTelematiciRPTservice;
import it.gov.spcoop.puntoaccessopa.servizi.avvisidigitali.NodoInviaAvvisoDigitaleService;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.beans.GpResponse;
import it.govpay.core.exceptions.NdpException.FaultPa;
import it.govpay.core.utils.client.NodoClient.Azione;
import it.govpay.model.Rpt;
import it.govpay.model.Versionabile.Versione;

public class GpContext extends ApplicationContext {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 
	private PagamentoContext pagamentoCtx;
	
	public static final String NOT_SET = "<Non valorizzato>";
	
	public static final String NodoDeiPagamentiSPC = "NodoDeiPagamentiSPC";
	public static final String GovPay = "GovPay";
	public static final String TIPO_SOGGETTO_NDP = "NDP";
	public static final String TIPO_SERVIZIO_NDP = "NDP";
	
	public static final String TIPO_SOGGETTO_APP = "APP";
	public static final String TIPO_SOGGETTO_PRT = "PRT";
	public static final String TIPO_SOGGETTO_STAZIONE = "STZ";
	public static final String TIPO_SOGGETTO_GOVPAY = "GP";
	
	public static final String TIPO_SERVIZIO_GOVPAY = "GP";
	public static final String TIPO_SERVIZIO_GOVPAY_RS = "GPRS";
	public static final String TIPO_SERVIZIO_GOVPAY_JSON = "GPJSON";
	public static final String TIPO_SERVIZIO_GOVPAY_WS = "GPWS";
	public static final String TIPO_SERVIZIO_GOVPAY_OPT = "GPO";
	
	
	public GpContext(MessageContext msgCtx, String tipoServizio, int versioneServizio) throws ServiceException {
			ApplicationTransaction transaction = (ApplicationTransaction) this.getTransaction();
			transaction.setRole(Role.SERVER);
			transaction.setProtocol("govpay");
			
			Service service = new Service();
			if(msgCtx.get(MessageContext.WSDL_SERVICE) != null)
				service.setName(((QName) msgCtx.get(MessageContext.WSDL_SERVICE)).getLocalPart());
			else 
				service.setName("<Unknown>");
			service.setVersion(versioneServizio);
			service.setType(tipoServizio);
			
			transaction.setService(service);
			
			Operation operation = new Operation();
			operation.setMode(FlowMode.INPUT_OUTPUT);
			if(msgCtx.get(MessageContext.WSDL_OPERATION) != null)
				operation.setName(((QName) msgCtx.get(MessageContext.WSDL_OPERATION)).getLocalPart());
			else 
				operation.setName("<Unknown>");
			transaction.setOperation(operation);
			
			HttpServletRequest servletRequest = (HttpServletRequest) msgCtx.get(MessageContext.SERVLET_REQUEST);
			BaseClient client = new BaseClient();
			client.setInvocationEndpoint(servletRequest.getRequestURI());
			
			if(msgCtx.get(MessageContext.WSDL_INTERFACE) != null)
				client.setInterfaceName(((QName) msgCtx.get(MessageContext.WSDL_INTERFACE)).getLocalPart());
			else 
				client.setInterfaceName("<Unknown>");
			
			String user = AutorizzazioneUtils.getPrincipal(SecurityContextHolder.getContext().getAuthentication()); 
			
			if(user != null)
				client.setPrincipal(user);
			transaction.setClient(client);
			
			BaseServer server = new BaseServer();
			server.setName(GovPay);
			
			Actor to = new Actor();
			to.setName(GovPay);
			to.setType(TIPO_SOGGETTO_GOVPAY);
			transaction.setTo(to);
			transaction.addServer(server);
	}
	
	public GpContext(UriInfo uriInfo, HttpHeaders rsHttpHeaders, HttpServletRequest request,
			String nomeOperazione, String nomeServizio, String tipoServizio, int versioneServizio) throws ServiceException {

			ApplicationTransaction transaction = (ApplicationTransaction) this.getTransaction();
			transaction.setRole(Role.SERVER);
			transaction.setProtocol("REST");
			
			Service service = new Service();
			service.setName(nomeServizio);
			service.setVersion(versioneServizio);
			service.setType(tipoServizio);
			transaction.setService(service);
			
			Operation operation = new Operation();
			operation.setMode(FlowMode.INPUT_OUTPUT);
			operation.setName(nomeOperazione);
			transaction.setOperation(operation);
			
			BaseClient client = new BaseClient();
			client.setInvocationEndpoint(request.getRequestURI());
			client.setInterfaceName(nomeServizio);
			
			String user = AutorizzazioneUtils.getPrincipal(SecurityContextHolder.getContext().getAuthentication()); 
			
			if(user != null)
				client.setPrincipal(user);
			transaction.setClient(client);
			
			BaseServer server = new BaseServer();
			server.setName(GovPay);
			
			Actor to = new Actor();
			to.setName(GovPay);
			to.setType(TIPO_SOGGETTO_GOVPAY);
			transaction.setTo(to);
			transaction.addServer(server);
	}
	
	public GpContext(String requestUri,	String nomeServizio, String nomeOperazione, String httpMethod, int versioneServizio, String user) throws ServiceException {
			ApplicationTransaction transaction = (ApplicationTransaction) this.getTransaction();
			transaction.setRole(Role.SERVER);
			transaction.setProtocol("REST");
			
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
			
			BaseServer server = new BaseServer();
			server.setName(GovPay);
			
			Actor to = new Actor();
			to.setName(GovPay);
			transaction.setTo(to);
			transaction.addServer(server);
	}
	public GpContext() throws ServiceException {
		ApplicationTransaction transaction = (ApplicationTransaction) this.getTransaction();
		transaction.setRole(Role.SERVER);
		transaction.setProtocol("REST");
		
		BaseServer server = new BaseServer();
		server.setName(GovPay);
		
		Actor to = new Actor();
		to.setName(GovPay);
		to.setType(TIPO_SOGGETTO_GOVPAY);
		transaction.setTo(to);
		transaction.addServer(server);
		
		Request request = this.getRequest();
		request.setDate(new Date());
	}
	
	public GpContext(String correlationId) throws ServiceException {
		this();
		this.setCorrelationId(correlationId);
	}
	
	public void setupNodoClient(String codStazione, String codDominio, Azione azione) {
		this._setupNodoClient(codStazione, codDominio, PagamentiTelematiciRPTservice.SERVICE.getLocalPart(), azione.toString(), Rpt.VERSIONE_ENCODED);
	}
	
	public void setupNodoClient(String codStazione, String codDominio, it.govpay.core.utils.client.AvvisaturaClient.Azione azione) {
		this._setupNodoClient(codStazione, codDominio, NodoInviaAvvisoDigitaleService.SERVICE.getLocalPart(), azione.toString(), 1);
	}
	
	private void _setupNodoClient(String codStazione, String codDominio, String servizio, String azione, int versione) {
		Actor to = new Actor();
		to.setName(NodoDeiPagamentiSPC);
		to.setType(TIPO_SOGGETTO_NDP);
		GpThreadLocal.get().getApplicationContext().getTransaction().setTo(to);
		
		Actor from = new Actor();
		from.setName(codStazione);
		from.setType(TIPO_SOGGETTO_STAZIONE);
		GpThreadLocal.get().getApplicationContext().getTransaction().setFrom(from);
		
		this.setInfoFruizione(TIPO_SERVIZIO_NDP, servizio, azione, versione);
		
		BaseServer server = new BaseServer();
		server.setName(NodoDeiPagamentiSPC);
		this.getTransaction().addServer(server); 
		
//		if(codDominio != null) {
//			BaseClient client = new BaseClient();
//			client.setName(codDominio);
//			this.getTransaction().setClient(client);
//		}
	}
	
	public void setupPaClient(String codApplicazione, String azione, String url, Versione versione) {
		Actor to = new Actor();
		to.setName(codApplicazione);
		to.setType(TIPO_SOGGETTO_APP);
		GpThreadLocal.get().getApplicationContext().getTransaction().setTo(to);
		
		Actor from = new Actor();
		from.setName(GovPay);
		from.setType(TIPO_SERVIZIO_GOVPAY);
		GpThreadLocal.get().getApplicationContext().getTransaction().setFrom(from);
		
		this.setInfoFruizione(TIPO_SERVIZIO_GOVPAY_WS, "", azione, versione.getVersione());
		
		BaseServer server = new BaseServer();
		server.setName(codApplicazione);
		server.setEndpoint(url);
		this.getTransaction().addServer(server); 
		
//		BaseClient client = new BaseClient();
//		client.setName(GovPay);
//		this.getTransaction().setClient(client);
	}
	
	private void setInfoFruizione(String tipoServizio, String servizio, String operazione, int version) {
		Service service = new Service();
		service.setName(servizio);
		service.setVersion(version);
		service.setType(tipoServizio);
		this.getTransaction().setService(service);
		
		Operation operation = new Operation();
		operation.setMode(FlowMode.INPUT_OUTPUT);
		operation.setName(operazione);
		this.getTransaction().setOperation(operation);
	}
	
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
}