package it.govpay.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.service.fault.jaxrs.FaultCode;

import it.govpay.core.dao.commons.exception.RedirectException;
import it.govpay.core.exceptions.BaseExceptionV1;
import it.govpay.core.exceptions.GovPayException;

/**
 * Gestione automatica delle eccezioni lanciate dal DAO V1 in Problem V2.
 * 
 * @author pintori
 *
 */
@Provider
public class WebApplicationExceptionMapper extends org.openspcoop2.utils.service.fault.jaxrs.WebApplicationExceptionMapper {

	@Override
	public Response toResponse(javax.ws.rs.WebApplicationException e) {
		if(WebApplicationExceptionMapper.existsInnerException(e, RedirectException.class)) {
			RedirectException innerException = (RedirectException) WebApplicationExceptionMapper.getInnerException(e, RedirectException.class);
			return Response.seeOther(innerException.getURILocation()).build();
		}else {
			return super.toResponse(e);
		}
	}
	
	@Override
	public void updateProblem(org.openspcoop2.utils.service.fault.jaxrs.Problem problem, WebApplicationException e) {
	
		if(WebApplicationExceptionMapper.existsInnerException(e, BaseExceptionV1.class)) {
			BaseExceptionV1 innerException = (BaseExceptionV1) WebApplicationExceptionMapper.getInnerException(e, BaseExceptionV1.class);
			problem.setStatus(innerException.getTransportErrorCode());
			problem.setDetail(innerException.getMessage());
		} 
		
		if(WebApplicationExceptionMapper.existsInnerException(e, GovPayException.class)) {
			GovPayException innerException = (GovPayException) WebApplicationExceptionMapper.getInnerException(e, GovPayException.class);
			int statusCode = innerException.getStatusCode();
			if(innerException.getFaultBean()!=null) {
				statusCode = 502;
				problem.setDetail(innerException.getFaultBean().getDescription());
			} else {
				problem.setDetail(innerException.getMessageV3());
			}
			problem.setStatus(statusCode);
		} 
		if(WebApplicationExceptionMapper.existsInnerException(e, ValidationException.class)) {
			ValidationException innerException = (ValidationException) WebApplicationExceptionMapper.getInnerException(e, ValidationException.class);
			int statusCode = 400;
			problem.setStatus(statusCode);
			problem.setDetail(innerException.getMessage());
		}
	}

	
	public static WebApplicationException handleException(Throwable e) {

		if(e instanceof BaseExceptionV1 || e instanceof RedirectException || e instanceof GovPayException || e instanceof ValidationException) {
			return new WebApplicationException(e);
		}

		return FaultCode.ERRORE_INTERNO.toException(e);
	}
	
	public static boolean existsInnerException(Throwable e,Class<?> found){
		//System.out.println("ANALIZZO ["+e.getClass().getName()+"] ("+found+")");
		if(found.isAssignableFrom(e.getClass())){
			//System.out.println("OK ["+e.getClass().getName()+"]");
			return true;
		}else{
			if(e.getCause()!=null){
				//System.out.println("INNER ["+e.getClass().getName()+"]...");
				return existsInnerException(e.getCause(), found);
			}
			else{
				//System.out.println("ESCO ["+e.getClass().getName()+"]");
				return false;
			}
		}
	}

	public static Throwable getInnerException(Throwable e,Class<?> found){
		//System.out.println("ANALIZZO ["+e.getClass().getName()+"] ("+found+")");
		if(found.isAssignableFrom(e.getClass())){
			//System.out.println("OK ["+e.getClass().getName()+"]");
			return e;
		}else{
			if(e.getCause()!=null){
				//System.out.println("INNER ["+e.getClass().getName()+"]...");
				return getInnerException(e.getCause(), found);
			}
			else{
				//System.out.println("ESCO ["+e.getClass().getName()+"]");
				return null;
			}
		}
	}
}