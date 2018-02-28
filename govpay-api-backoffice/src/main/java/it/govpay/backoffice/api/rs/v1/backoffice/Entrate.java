package it.govpay.pagamento.api.rs.v1.pagamenti;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;

import it.govpay.rs.v1.controllers.base.EntrateController;

import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/entrate")

public class Entrate extends BaseRsServiceV1{


	private EntrateController controller = null;

	public Entrate() {
		super("entrate");
		this.controller = new EntrateController(this.nomeServizio,this.log);
	}



/*
    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response entrateGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam("pagina") Integer pagina, @QueryParam("risultatiPerPagina") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.entrateGET(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi);
    }
*/

/*
    @GET
    @Path("/{idEntrata}")
    
    @Produces({ "application/json" })
    public Response entrateIdEntrataGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idEntrata") String idEntrata){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.entrateIdEntrataGET(this.getUser(), uriInfo, httpHeaders,  idEntrata);
    }
*/

/*
    @PUT
    @Path("/{idEntrata}")
    @Consumes({ "application/json" })
    
    public Response entrateIdEntrataPUT(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idEntrata") String idEntrata, java.io.InputStream is){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.entrateIdEntrataPUT(this.getUser(), uriInfo, httpHeaders,  idEntrata, is);
    }
*/

}


