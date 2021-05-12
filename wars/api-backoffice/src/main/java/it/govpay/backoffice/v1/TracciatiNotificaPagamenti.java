package it.govpay.backoffice.v1;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.backoffice.v1.controllers.TracciatiNotificaPagamentiController;
import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/tracciatiNotificaPagamenti")

public class TracciatiNotificaPagamenti extends BaseRsServiceV1{


	private TracciatiNotificaPagamentiController controller = null;

	public TracciatiNotificaPagamenti() throws ServiceException {
		super("tracciatiNotificaPagamenti");
		this.controller = new TracciatiNotificaPagamentiController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/{id}")
    
    @Produces({ "application/zip" })
    public Response getTracciatoNotificaPagamenti(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("id") String id){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.getTracciatoNotificaPagamenti(this.getUser(), uriInfo, httpHeaders,  id);
    }

}


