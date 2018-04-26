package it.govpay.wc.ecsp.psp;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.rs.v1.BaseRsServiceV1;
import it.govpay.rs.v1.controllers.gateway.PspController;

@Path("/")
public class Psp extends BaseRsServiceV1 {
	
	private PspController controller = null;
	
	public Psp() throws ServiceException {
		super("psp");
		this.controller = new PspController(this.nomeServizio,this.log);
	}


	@GET
	@Path("/psp")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getPsp(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam("idSession") String idSession, @QueryParam("esito") String esito) {
		this.controller.setRequestResponse(this.request, this.response);
        return this.controller.getPsp(null, uriInfo, httpHeaders, idSession, esito);
	}
}
