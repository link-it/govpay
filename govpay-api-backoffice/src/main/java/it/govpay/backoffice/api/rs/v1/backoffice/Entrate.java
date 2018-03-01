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
import it.govpay.rs.v1.controllers.base.EntrateController;
import it.govpay.rs.v1.costanti.Costanti;


@Path("/entrate")

public class Entrate extends BaseRsServiceV1{


	private EntrateController controller = null;

	public Entrate() {
		super("entrate");
		this.controller = new EntrateController(this.nomeServizio,this.log);
	}



    @PUT
    @Path("/{idEntrata}")
    @Consumes({ "application/json" })
    
    public Response entrateIdEntrataPUT(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idEntrata") String idEntrata, java.io.InputStream is){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.entrateIdEntrataPUT(this.getUser(), uriInfo, httpHeaders,  idEntrata, is);
    }

    @GET
    @Path("/{idEntrata}")
    
    @Produces({ "application/json" })
    public Response entrateIdEntrataGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idEntrata") String idEntrata){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.entrateIdEntrataGET(this.getUser(), uriInfo, httpHeaders,  idEntrata);
    }

    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response entrateGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.entrateGET(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi);
    }

}


