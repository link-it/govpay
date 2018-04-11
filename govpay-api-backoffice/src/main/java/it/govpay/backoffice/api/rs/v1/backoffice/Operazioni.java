package it.govpay.backoffice.api.rs.v1.backoffice;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import it.govpay.core.rs.v1.costanti.Costanti;
import it.govpay.rs.v1.BaseRsServiceV1;
import it.govpay.rs.v1.controllers.base.OperazioniController;

@Path("/eventi")

public class Operazioni extends BaseRsServiceV1{


	private OperazioniController controller = null;

	public Operazioni() {
		super("eventi");
		this.controller = new OperazioniController(this.nomeServizio,this.log); 
	}



    @GET
    @Path("/{id}")
    
    @Produces({ "application/json" })
    public Response operazioniIdGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("id") String id){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.operazioniIdGET(this.getUser(), uriInfo, httpHeaders,  id);
    }

}