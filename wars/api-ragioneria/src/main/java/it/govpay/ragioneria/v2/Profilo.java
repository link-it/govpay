package it.govpay.ragioneria.v2;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.ragioneria.v2.controller.ProfiloController;
import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/profilo")

public class Profilo extends BaseRsServiceV1{


	private ProfiloController controller = null;

	public Profilo() throws ServiceException {
		super("profilo");
		this.controller = new ProfiloController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response getProfilo(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders){
        this.controller.setContext(this.getContext());
        return this.controller.profiloGET(this.getUser(), uriInfo, httpHeaders);
    }

}


