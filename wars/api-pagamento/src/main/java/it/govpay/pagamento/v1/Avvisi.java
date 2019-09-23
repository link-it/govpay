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

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.pagamento.v1.controller.AvvisiController;
import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/avvisi")

public class Avvisi extends BaseRsServiceV1{


	private AvvisiController controller = null;

	public Avvisi() throws ServiceException {
		super("avvisi");
		this.controller = new AvvisiController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/{idDominio}/{numeroAvviso}")
    
    @Produces({ "application/json", "application/pdf" })
    public Response getAvviso(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("numeroAvviso") String numeroAvviso, @QueryParam("idDebitore") String idDebitore){
        this.controller.setContext(this.getContext());
        return this.controller.avvisiIdDominioNumeroAvvisoGET(this.getUser(), uriInfo, httpHeaders,  idDominio,  numeroAvviso, idDebitore);
    }

}


