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

import it.govpay.rs.v1.controllers.base.AvvisiController;

import it.govpay.rs.v1.BaseRsServiceV1;
import it.govpay.rs.v1.costanti.Costanti;


@Path("/avvisi")

public class Avvisi extends BaseRsServiceV1{


	private AvvisiController controller = null;

	public Avvisi() {
		super("avvisi");
		this.controller = new AvvisiController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/{idDominio}/{iuv}")
    
    @Produces({ "application/json", "application/pdf" })
    public Response avvisiIdDominioIuvGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("iuv") String iuv){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.avvisiIdDominioIuvGET(this.getUser(), uriInfo, httpHeaders,  idDominio,  iuv);
    }

    @POST
    @Path("/{idDominio}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    public Response avvisiIdDominioPOST(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, java.io.InputStream is){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.avvisiIdDominioPOST(this.getUser(), uriInfo, httpHeaders,  idDominio, is);
    }

}


