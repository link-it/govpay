package it.govpay.pagamento.v1;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import it.govpay.pagamento.v1.controller.AvvisiController;
import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/avvisi")

public class Avvisi extends BaseRsServiceV1{


	private AvvisiController controller = null;

	public Avvisi() {
		super("avvisi");
		this.controller = new AvvisiController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/{idDominio}/{numeroAvviso}")

    @Produces({ "application/json", "application/pdf" })
    public Response getAvviso(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("numeroAvviso") String numeroAvviso, @QueryParam("idDebitore") String idDebitore, @QueryParam("UUID") String UUID, @QueryParam("gRecaptchaResponse") String gRecaptchaResponse){
    	this.controller.setRequestResponse(this.request, this.response);
    	this.buildContext();
        return this.controller.avvisiIdDominioNumeroAvvisoGET(this.getUser(), uriInfo, httpHeaders,  idDominio,  numeroAvviso, idDebitore, UUID, gRecaptchaResponse);
    }

}


