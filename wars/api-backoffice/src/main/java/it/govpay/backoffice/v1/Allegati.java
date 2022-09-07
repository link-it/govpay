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

import it.govpay.backoffice.v1.controllers.AllegatiController;
import it.govpay.rs.v1.BaseRsServiceV1;



@Path("/allegati")

public class Allegati extends BaseRsServiceV1 {

	public static final String DETTAGLIO_PATH_PATTERN = "/allegati/{0}";

	private AllegatiController controller = null;

	public Allegati() throws ServiceException {
		super("allegati");
		this.controller = new AllegatiController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/{id}")
    
    @Produces({ "*/*" })
    public Response getAllegatoPendenza(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("id") Long id){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.getAllegatoPendenza(this.getUser(), uriInfo, httpHeaders,  id);
    }

}


