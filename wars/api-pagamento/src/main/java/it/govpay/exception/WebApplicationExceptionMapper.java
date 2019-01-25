package it.govpay.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.openspcoop2.utils.Utilities;

import com.fasterxml.jackson.databind.JsonMappingException;

import it.govpay.core.exceptions.GovPayException;
import it.govpay.pagamento.v2.beans.Problem;

@Provider
public class WebApplicationExceptionMapper extends org.openspcoop2.utils.service.fault.jaxrs.WebApplicationExceptionMapper {

	@Override
	public void updateProblem(org.openspcoop2.utils.service.fault.jaxrs.Problem problem, WebApplicationException e) {
	
//		Utilities.existsInnerException(e, found);
		
//		Utilities.getInnerException(e, found)
		
		super.updateProblem(problem, e);;
	}

	@Override
	public Response toResponse(WebApplicationException exception) {
		Response.Status errorStatus = Response.Status.BAD_REQUEST;
		
		Problem problem = new Problem();
		problem.setStatus(errorStatus.getStatusCode());
		problem.setTitle(Response.Status.BAD_REQUEST.name());
		problem.setDetail(exception.getMessage());
		
		if(exception.getCause() instanceof JsonMappingException) {
			errorStatus = Response.Status.BAD_REQUEST;
			JsonMappingException jsonMappingException = (JsonMappingException) exception.getCause();
			problem = new ProblemValidation();
			((ProblemValidation) problem).addInvalidParam(jsonMappingException.getPathReference(), jsonMappingException.getMessage(), null);
		}  
		
		if(exception.getCause() instanceof GovPayException) {
			errorStatus = Response.Status.INTERNAL_SERVER_ERROR;
			problem = new Problem();
			problem.setType("xxx");
		}  
		

		return Response.status(errorStatus).entity(problem).build();
	}

}

