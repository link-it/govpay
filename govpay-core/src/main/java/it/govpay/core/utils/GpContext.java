package it.govpay.core.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.logger.ILogger;
import org.openspcoop2.utils.logger.LoggerFactory;
import org.openspcoop2.utils.logger.beans.Message;
import org.openspcoop2.utils.logger.beans.proxy.Actor;
import org.openspcoop2.utils.logger.beans.proxy.Client;
import org.openspcoop2.utils.logger.beans.proxy.Operation;
import org.openspcoop2.utils.logger.beans.proxy.ProxyContext;
import org.openspcoop2.utils.logger.beans.proxy.Request;
import org.openspcoop2.utils.logger.beans.proxy.Role;
import org.openspcoop2.utils.logger.beans.proxy.Server;
import org.openspcoop2.utils.logger.beans.proxy.Service;
import org.openspcoop2.utils.logger.beans.proxy.Transaction;
import org.openspcoop2.utils.logger.constants.proxy.FlowMode;
import org.openspcoop2.utils.logger.constants.proxy.Result;
import org.openspcoop2.utils.transport.http.HttpServletCredential;

import it.gov.spcoop.nodopagamentispc.servizi.pagamentitelematicirpt.PagamentiTelematiciRPTservice;
import it.govpay.bd.model.Utenza;
import it.govpay.core.exceptions.NdpException.FaultPa;
import it.govpay.core.utils.client.NodoClient.Azione;
import it.govpay.core.utils.client.handler.IntegrationContext;
import it.govpay.model.Rpt;
import it.govpay.model.Versionabile.Versione;
import it.govpay.servizi.PagamentiTelematiciPAService;
import it.govpay.servizi.commons.GpResponse;

public class GpContext {

	private List<ILogger> loggers;
	private List<Context> contexts;
	
	private PagamentoContext pagamentoCtx;
	private IntegrationContext integrationCtx;
	
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
		try {
			this.loggers = new ArrayList<>();
			ILogger logger = LoggerFactory.newLogger(new Context());	
			this.loggers.add(logger);
			
			this.contexts = new ArrayList<>();
			Context context = (Context) logger.getContext();
			context.getTransaction().setProtocol("govpay");
			this.contexts.add(context);
			
			Transaction transaction = context.getTransaction();
			transaction.setRole(Role.SERVER);
			
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
			Client client = new Client();
			client.setInvocationEndpoint(servletRequest.getRequestURI());
			
			if(msgCtx.get(MessageContext.WSDL_INTERFACE) != null)
				client.setInterfaceName(((QName) msgCtx.get(MessageContext.WSDL_INTERFACE)).getLocalPart());
			else 
				client.setInterfaceName("<Unknown>");
			
			Utenza user = CredentialUtils.getUser(new HttpServletCredential((HttpServletRequest) msgCtx.get(MessageContext.SERVLET_REQUEST), null));
			if(user != null)
				client.setPrincipal(user.getPrincipal());
			
			transaction.setClient(client);
			
			Server server = new Server();
			server.setName(GovPay);
			
			Actor to = new Actor();
			to.setName(GovPay);
			to.setType(TIPO_SOGGETTO_GOVPAY);
			transaction.setTo(to);
			
			transaction.setServer(server);
		} catch (UtilsException e) {
			throw new ServiceException(e);
		}
	}
	
	public GpContext(UriInfo uriInfo, HttpHeaders rsHttpHeaders, HttpServletRequest request,
			String nomeOperazione, String nomeServizio, String tipoServizio, int versioneServizio) throws ServiceException {
		try {
			this.loggers = new ArrayList<>();
			ILogger logger = LoggerFactory.newLogger(new Context());	
			this.loggers.add(logger);
			
			this.contexts = new ArrayList<>();
			Context context = (Context) logger.getContext();
			context.getTransaction().setProtocol("govpay");
			this.contexts.add(context);
			
			Transaction transaction = context.getTransaction();
			transaction.setRole(Role.SERVER);
			
			Service service = new Service();
			service.setName(nomeServizio);
			service.setVersion(versioneServizio);
			service.setType(tipoServizio);
			
			transaction.setService(service);
			
			Operation operation = new Operation();
			operation.setMode(FlowMode.INPUT_OUTPUT);
			operation.setName(nomeOperazione);
			transaction.setOperation(operation);
			
			Client client = new Client();
			client.setInvocationEndpoint(request.getRequestURI());
			
			client.setInterfaceName(nomeServizio);
			
			Utenza user = CredentialUtils.getUser(new HttpServletCredential(request, null));
			if(user != null)
				client.setPrincipal(user.getPrincipal());
			
			transaction.setClient(client);
			
			Server server = new Server();
			server.setName(GovPay);
			
			Actor to = new Actor();
			to.setName(GovPay);
			to.setType(TIPO_SOGGETTO_GOVPAY);
			transaction.setTo(to);
			
			transaction.setServer(server);
		} catch (UtilsException e) {
			throw new ServiceException(e);
		}
	}
	
	public GpContext() throws ServiceException {
		this.loggers = new ArrayList<>();
		this.contexts = new ArrayList<>();
		this.openTransaction();
	}
	
	public GpContext(String correlationId) throws ServiceException {
		this();
		this.setCorrelationId(correlationId);
	}
	
	public String openTransaction() throws ServiceException {
		try {
			ILogger logger = LoggerFactory.newLogger(new Context());	
			this.loggers.add(logger);
			
			Context context = (Context) logger.getContext();
			context.getTransaction().setProtocol("govpay");
			if(!this.contexts.isEmpty())
				context.getRequest().setCorrelationIdentifier(this.contexts.get(0).getIdTransaction());
			this.contexts.add(context);
			
			Request request = context.getRequest();
			request.setInDate(new Date());
			
			Transaction transaction = context.getTransaction();
			transaction.setRole(Role.CLIENT);
			
			return context.getIdTransaction();
		} catch (UtilsException e) {
			throw new ServiceException(e);
		}
	}
	
	public void closeTransaction(String idTransaction) {
		if(idTransaction == null) return;
		
		Context c = this.getContext(idTransaction);
		if(c != null) c.setActive(false);
	}
	
	public Context getContext(){
		for(int i=this.contexts.size() -1; i>=0; i--) {
			if(this.contexts.get(i).isActive) return this.contexts.get(i);
		}
		return null;
	}
	
	private Context getContext(String idTransaction){
		for(Context c : this.contexts) {
			if(c.getIdTransaction().equals(idTransaction))
					return c;
		}
		return null;
	}
	
	public void setupNodoClient(String codStazione, String codDominio, Azione azione) {
		Actor to = new Actor();
		to.setName(NodoDeiPagamentiSPC);
		to.setType(TIPO_SOGGETTO_NDP);
		GpThreadLocal.get().getTransaction().setTo(to);
		
		Actor from = new Actor();
		from.setName(codStazione);
		from.setType(TIPO_SOGGETTO_STAZIONE);
		GpThreadLocal.get().getTransaction().setFrom(from);
		
		GpThreadLocal.get().setInfoFruizione(TIPO_SERVIZIO_NDP, PagamentiTelematiciRPTservice.SERVICE.getLocalPart(), azione.toString(), Rpt.VERSIONE_ENCODED);
		
		Server server = new Server();
		server.setName(NodoDeiPagamentiSPC);
		GpThreadLocal.get().getTransaction().setServer(server);
		
		if(codDominio != null) {
			Client client = new Client();
			client.setName(codDominio);
			GpThreadLocal.get().getTransaction().setClient(client);
		}
	}
	
	public void setupPaClient(String codApplicazione, String azione, String url, Versione versione) {
		Actor to = new Actor();
		to.setName(codApplicazione);
		to.setType(TIPO_SOGGETTO_APP);
		GpThreadLocal.get().getTransaction().setTo(to);
		
		Actor from = new Actor();
		from.setName(GovPay);
		from.setType(TIPO_SERVIZIO_GOVPAY);
		GpThreadLocal.get().getTransaction().setFrom(from);
		
		GpThreadLocal.get().setInfoFruizione(TIPO_SERVIZIO_GOVPAY_WS, PagamentiTelematiciPAService.SERVICE.getLocalPart(), azione, versione.getVersione());
		
		Server server = new Server();
		server.setName(codApplicazione);
		server.setEndpoint(url);
		GpThreadLocal.get().getTransaction().setServer(server);
		
		Client client = new Client();
		client.setName(GovPay);
		GpThreadLocal.get().getTransaction().setClient(client);
	}
	
	private ILogger getActiveLogger(){
		for(int i=this.contexts.size()-1; i>=0; i--) {
			if(this.contexts.get(i).isActive) return this.loggers.get(i);
		}
		return null;
	}
	
	public void setInfoFruizione(String tipoServizio, String servizio, String operazione, int version) {
		Service service = new Service();
		service.setName(servizio);
		service.setVersion(version);
		service.setType(tipoServizio);
		this.getContext().getTransaction().setService(service);
		
		Operation operation = new Operation();
		operation.setMode(FlowMode.INPUT_OUTPUT);
		operation.setName(operazione);
		this.getContext().getTransaction().setOperation(operation);
	}
	
	public Transaction getTransaction() {
		return this.getContext().getTransaction();
	}
	
	public String getTransactionId() {
		return this.getContext().getIdTransaction();
	}
	
	public boolean hasCorrelationId() {
		try {
			return this.contexts.get(0).getRequest().getCorrelationIdentifier() != null;
		} catch (Throwable t) {
			return false;
		}
	}
	
	public void setCorrelationId(String id) {
		this.contexts.get(0).getRequest().setCorrelationIdentifier(id);
	}

	public void setResult(GpResponse response) {
		if(response == null || response.getCodEsitoOperazione() == null) {
			this.getContext().getTransaction().setResult(Result.INTERNAL_ERROR);
			return;
		}
		switch (response.getCodEsitoOperazione()) {
		case OK:
			this.getContext().getTransaction().setResult(Result.SUCCESS);
			break;
		case INTERNAL:
			this.getContext().getTransaction().setResult(Result.INTERNAL_ERROR);
			break;
		default:
			this.getContext().getTransaction().setResult(Result.PROCESSING_ERROR);
			break;
		}
	}
	
	public void setResult(it.govpay.servizi.v2_3.commons.GpResponse response) {
		if(response == null || response.getCodEsito() == null) {
			this.getContext().getTransaction().setResult(Result.INTERNAL_ERROR);
			return;
		}
		if(response.getCodEsito().equals("OK")) 
			this.getContext().getTransaction().setResult(Result.SUCCESS);
		else if(response.getCodEsito().equals("INTERNAL"))
			this.getContext().getTransaction().setResult(Result.INTERNAL_ERROR);
		else
			this.getContext().getTransaction().setResult(Result.PROCESSING_ERROR);
	}
	
	public void setResult(String faultCode) {
		if(faultCode == null) {
			this.getContext().getTransaction().setResult(Result.SUCCESS);
			return;
		}
			
		if(faultCode.equals(FaultPa.PAA_SYSTEM_ERROR.name())) {
			this.getContext().getTransaction().setResult(Result.INTERNAL_ERROR);
			return; 
		}
		
		this.getContext().getTransaction().setResult(Result.PROCESSING_ERROR);
	}
	
	public void log(String string, String...params) {
		try {
			ILogger activeLogger = this.getActiveLogger();
			if(activeLogger != null)
				activeLogger.log(string, params);
		} catch (Exception e) {
			LoggerWrapperFactory.getLogger(GpContext.class).error("Errore nell'emissione del diagnostico", e);
		}
	}
	
	public void log() {
		for(ILogger l : this.loggers) {
			try {
				l.log();
			} catch (UtilsException e) {
				LoggerWrapperFactory.getLogger(GpContext.class).error("Errore nell'emissione della transazione", e);
			}
		}
	}
	
	public void log(Message m) {
		try {
			ILogger activeLogger = this.getActiveLogger();
			if(activeLogger != null)
				activeLogger.log(m);
		} catch (Exception e) {
			LoggerWrapperFactory.getLogger(GpContext.class).error("Errore nell'emissione della transazione", e);
		}
	}
	
	public IntegrationContext getIntegrationCtx() {
		if(this.integrationCtx == null) 
			this.integrationCtx=new IntegrationContext();
		
		return this.integrationCtx;
	}

	public PagamentoContext getPagamentoCtx() {
		if(this.pagamentoCtx == null) 
			this.pagamentoCtx = new PagamentoContext();
		return this.pagamentoCtx;
	}


	public class Context extends ProxyContext {
		private static final long serialVersionUID = 1L;
		
		private boolean isActive = true;
		
		public boolean isActive() {
			return this.isActive;
		}

		public void setActive(boolean isActive) {
			this.isActive = isActive;
		}
	}
}