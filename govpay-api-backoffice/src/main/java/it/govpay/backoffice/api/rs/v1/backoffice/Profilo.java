package it.govpay.backoffice.api.rs.v1.backoffice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import it.govpay.rs.v1.BaseRsServiceV1;
import it.govpay.rs.v1.controllers.base.ProfiloController;


@Path("/profilo")

public class Profilo extends BaseRsServiceV1{


	private ProfiloController controller = null;

	public Profilo() {
		super("profilo");
		this.controller = new ProfiloController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response profiloGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam("pagina") Integer pagina, @QueryParam("risultatiPerPagina") Integer risultatiPerPagina){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.profiloGET(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders, pagina, risultatiPerPagina);
    }

}
