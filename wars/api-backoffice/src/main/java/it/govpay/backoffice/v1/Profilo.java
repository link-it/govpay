package it.govpay.backoffice.v1;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.backoffice.v1.controllers.ProfiloController;
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
        this.buildContext();
        return this.controller.getProfilo(this.getUser(), uriInfo, httpHeaders);
    }

    @PATCH
    @Path("/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    public Response updateProfilo(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, java.io.InputStream is){
    	 this.buildContext();
        return this.controller.updateProfilo(this.getUser(), uriInfo, httpHeaders, is);
    }

}


