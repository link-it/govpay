package it.govpay.ragioneria.v3;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.ragioneria.v3.controller.ProfiloController;
import it.govpay.rs.v3.BaseRsServiceV3;



@Path("/profilo")

public class Profilo extends BaseRsServiceV3{


	private ProfiloController controller = null;

	public Profilo() throws ServiceException {
		super("profilo");
		this.controller = new ProfiloController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response getProfilo(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.getProfilo(this.getUser(), uriInfo, httpHeaders);
    }

}


