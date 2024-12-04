package it.govpay.backoffice.v1;


import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import it.govpay.backoffice.v1.controllers.RicevuteController;
import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/ricevute")

public class Ricevute extends BaseRsServiceV1 {


	private RicevuteController controller = null;

	public Ricevute() {
		super("ricevute");
		this.controller = new RicevuteController(this.nomeServizio,this.log);
	}



    @POST
    @Path("/")
    @Consumes({ "text/xml", "multipart/form-data" })
    @Produces({ "application/json" })
    public Response addRicevuta(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, java.io.InputStream is){
    	this.buildContext();
        return this.controller.addRicevuta(this.getUser(), uriInfo, httpHeaders, is);
    }

}


