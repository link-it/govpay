package it.govpay.pendenze.v2;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import it.govpay.pendenze.v2.controller.DominiController;
import it.govpay.rs.v2.BaseRsServiceV2;


@Path("/domini")

public class Domini extends BaseRsServiceV2{


	private DominiController controller = null;

	public Domini() {
		super("domini");
		this.controller = new DominiController(this.nomeServizio, this.log);
	}


    @GET
    @Path("/{idDominio}/logo")

    @Produces({ "application/json" })
    public Response getLogo(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio){
    	this.buildContext();
        return this.controller.dominiIdDominioLogoGET(this.getUser(), uriInfo, httpHeaders,  idDominio);
    }

}


