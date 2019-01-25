package it.govpay.core.utils.service.context;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
import javax.xml.ws.handler.MessageContext;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.logger.ILogger;
import org.openspcoop2.utils.logger.LoggerFactory;
import org.openspcoop2.utils.service.context.Context;
import org.openspcoop2.utils.service.context.ContextFactory;
import org.openspcoop2.utils.service.context.IContextFactory;

import it.govpay.core.utils.GpContext;

public class GpContextFactory extends ContextFactory implements IContextFactory {
	
	@Override
	public Context newContext() throws UtilsException {
		
		ILogger logger = LoggerFactory.newLogger();
		
		GpContext context;
		try {
			context = new GpContext();
			logger.initLogger(context);
			return new Context(logger, this.isLoggerPrefixEnabled());
		} catch (ServiceException e) {
			throw new UtilsException(e);
		}
	}
	
	public Context newContext(String correlationId) throws UtilsException { 
		
		ILogger logger = LoggerFactory.newLogger();
		
		GpContext context;
		try {
			context = new GpContext(correlationId);
			logger.initLogger(context);
			return new Context(logger, this.isLoggerPrefixEnabled());
		} catch (ServiceException e) {
			throw new UtilsException(e);
		}
	}
	
	public Context newContext(MessageContext msgCtx, String tipoServizio, int versioneServizio) throws UtilsException {
		
		ILogger logger = LoggerFactory.newLogger();
		
		GpContext context;
		try {
			context = new GpContext(msgCtx, tipoServizio, versioneServizio);
			logger.initLogger(context);
			return new Context(logger, this.isLoggerPrefixEnabled());
		} catch (ServiceException e) {
			throw new UtilsException(e);
		}
	}
	
	public Context newContext(UriInfo uriInfo, HttpHeaders rsHttpHeaders, HttpServletRequest request,
				String nomeOperazione, String nomeServizio, String tipoServizio, int versioneServizio) throws UtilsException {
		
		ILogger logger = LoggerFactory.newLogger();
		
		GpContext context;
		try {
			context = new GpContext(uriInfo, rsHttpHeaders, request, nomeOperazione, nomeServizio, tipoServizio, versioneServizio);
			logger.initLogger(context);
			return new Context(logger, this.isLoggerPrefixEnabled());
		} catch (ServiceException e) {
			throw new UtilsException(e);
		}
	}
}
