package it.govpay.backoffice.v1;


import it.govpay.backoffice.v1.controllers.RicevuteController;
import it.govpay.rs.v1.BaseRsServiceV1;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;


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


