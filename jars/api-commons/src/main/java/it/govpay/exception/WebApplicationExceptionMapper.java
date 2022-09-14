package it.govpay.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import it.govpay.core.exceptions.ValidationException;
import org.openspcoop2.utils.service.fault.jaxrs.FaultCode;

import it.govpay.core.dao.commons.exception.RedirectException;
import it.govpay.core.exceptions.BaseExceptionV1;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.UnprocessableEntityException;
import it.govpay.core.utils.ExceptionUtils;

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
		if(ExceptionUtils.existsInnerException(e, RedirectException.class)) {
			RedirectException innerException = (RedirectException) ExceptionUtils.getInnerException(e, RedirectException.class);
			return Response.seeOther(innerException.getURILocation()).build();
		}else {
			return super.toResponse(e);
		}
	}
	
	@Override
	public void updateProblem(org.openspcoop2.utils.service.fault.jaxrs.Problem problem, WebApplicationException e) {
		
		if(ExceptionUtils.existsInnerException(e, UnprocessableEntityException.class)) {
			UnprocessableEntityException innerException = (UnprocessableEntityException) ExceptionUtils.getInnerException(e, UnprocessableEntityException.class);
			int statusCode = 422;
			problem.setStatus(statusCode);
			problem.setDetail(innerException.getMessage());
		}
	
		if(ExceptionUtils.existsInnerException(e, BaseExceptionV1.class)) {
			BaseExceptionV1 innerException = (BaseExceptionV1) ExceptionUtils.getInnerException(e, BaseExceptionV1.class);
			problem.setStatus(innerException.getTransportErrorCode());
			problem.setDetail(innerException.getMessage());
		} 
		
		if(ExceptionUtils.existsInnerException(e, GovPayException.class)) {
			GovPayException innerException = (GovPayException) ExceptionUtils.getInnerException(e, GovPayException.class);
			int statusCode = innerException.getStatusCode();
			if(innerException.getFaultBean()!=null) {
				statusCode = 502;
				problem.setDetail(innerException.getFaultBean().getDescription());
			} else {
				problem.setDetail(innerException.getMessageV3());
			}
			problem.setStatus(statusCode);
		} 
		if(ExceptionUtils.existsInnerException(e, ValidationException.class)) {
			ValidationException innerException = (ValidationException) ExceptionUtils.getInnerException(e, ValidationException.class);
			int statusCode = 400;
			problem.setStatus(statusCode);
			problem.setDetail(innerException.getMessage());
		}
	}

	
	public static WebApplicationException handleException(Throwable e) {

		if(e instanceof BaseExceptionV1 || e instanceof RedirectException || e instanceof GovPayException || e instanceof ValidationException || e instanceof UnprocessableEntityException) {
			return new WebApplicationException(e);
		}

		return FaultCode.ERRORE_INTERNO.toException(e);
	}
}