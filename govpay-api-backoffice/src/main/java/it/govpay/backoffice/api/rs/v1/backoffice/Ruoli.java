package it.govpay.backoffice.api.rs.v1.backoffice;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import it.govpay.rs.v1.BaseRsServiceV1;
import it.govpay.rs.v1.controllers.base.RuoliController;
import it.govpay.rs.v1.costanti.Costanti;


@Path("/ruoli")

public class Ruoli extends BaseRsServiceV1{


	private RuoliController controller = null;

	public Ruoli() {
		super("ruoli");
		this.controller = new RuoliController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response ruoliGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") int pagina,
			@QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") int risultatiPerPagina, @QueryParam("abilitato") Boolean abilitato){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.ruoliGET(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders, pagina, risultatiPerPagina, abilitato);
    }

    
    @GET
    @Path("/{idRuolo}")
    
    @Produces({ "application/json" })
    public Response ruoliIdRuoloGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idRuolo") String idRuolo){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.ruoliIdRuoloGET(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders,  idRuolo);
    }

    
    @PUT
    @Path("/{idRuolo}")
    @Consumes({ "application/json" })
    
    public Response ruoliIdRuoloPUT(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idRuolo") String idRuolo, java.io.InputStream is){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.ruoliIdRuoloPUT(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders,  idRuolo, is);
    }


}
