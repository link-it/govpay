package it.govpay.pagamento.api.rs.v1.pagamenti;

import javax.ws.rs.Path;

import it.govpay.rs.v1.BaseRsServiceV1;
import it.govpay.rs.v1.controllers.base.AvvisiController;


@Path("/avvisi")

public class Avvisi extends BaseRsServiceV1{


	private AvvisiController controller = null;

	public Avvisi() {
		super("avvisi");
		this.controller = new AvvisiController(this.nomeServizio,this.log);
	}



/*
    @GET
    @Path("/{idDominio}/{iuv}")
    
    @Produces({ "application/json", "application/pdf" })
    public Response avvisiIdDominioIuvGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("iuv") String iuv){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.avvisiIdDominioIuvGET(this.getUser(), uriInfo, httpHeaders,  idDominio,  iuv);
    }
*/

/*
    @GET
    @Path("/{idDominio}/{iuv}")
    
    @Produces({ "application/json", "application/pdf" })
    public Response avvisiIdDominioIuvGET_1(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("iuv") String iuv){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.avvisiIdDominioIuvGET_1(this.getUser(), uriInfo, httpHeaders,  idDominio,  iuv);
    }
*/

/*
    @POST
    @Path("/{idDominio}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    public Response avvisiIdDominioPOST(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, java.io.InputStream is){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.avvisiIdDominioPOST(this.getUser(), uriInfo, httpHeaders,  idDominio, is);
    }
*/

/*
    @POST
    @Path("/{idDominio}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    public Response avvisiIdDominioPOST_2(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, java.io.InputStream is){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.avvisiIdDominioPOST_2(this.getUser(), uriInfo, httpHeaders,  idDominio, is);
    }
*/

}
