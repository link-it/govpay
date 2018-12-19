package it.govpay.rs.v1.handler;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class WebApplicationExceptionHandler implements ExceptionMapper<WebApplicationException> 
{
	private boolean excludeFaultBean;

	@Override
	public Response toResponse(WebApplicationException e) 
	{ 
		if(this.excludeFaultBean) {
			ResponseBuilder responseBuilder = Response.status(e.getResponse().getStatus());
			if(e.getResponse().getHeaders()!=null) {
				MultivaluedMap<String, Object> map = e.getResponse().getHeaders();
				if(!map.isEmpty()) {
					map.keySet().stream().forEach(k -> {
						responseBuilder.header(k, map.get(k));
					});
				}
			}
			return responseBuilder.build();
		} else {
			return e.getResponse();
		}

	}

	public boolean isExcludeFaultBean() {
		return this.excludeFaultBean;
	}

	public void setExcludeFaultBean(boolean excludeFaultBean) {
		this.excludeFaultBean = excludeFaultBean;
	}

}