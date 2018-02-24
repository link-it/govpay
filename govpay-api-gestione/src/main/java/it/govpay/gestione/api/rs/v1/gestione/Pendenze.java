package it.govpay.gestione.api.rs.v1.gestione;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import it.govpay.rs.v1.BaseRsServiceV1;
import it.govpay.rs.v1.controllers.base.PendenzeController;

@Path("/pendenze")
public class Pendenze extends BaseRsServiceV1{

	private PendenzeController controller = null;

	public Pendenze() {
		super("pendenze");
		this.controller = new PendenzeController(this.nomeServizio,this.log);
	}

    @GET
    @Path("/{idDominio}/{iuv}")
    
    @Produces({ "application/json" })
    public Response pendenzeIdDominioIuvGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam("idDominio") String idDominio, @QueryParam("iuv") String iuv){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.pendenzeIdDominioIuvGET(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders, idDominio, iuv);
    }
    
    
}
