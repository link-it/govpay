package it.govpay.rs.handler;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class WebApplicationExceptionHandler implements ExceptionMapper<WebApplicationException> 
{
	@Override
	public Response toResponse(WebApplicationException exception) 
	{
		return exception.getResponse();  
	}
}