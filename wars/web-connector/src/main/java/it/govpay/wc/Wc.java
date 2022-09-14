package it.govpay.wc;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import it.govpay.rs.v1.BaseRsServiceV1;
import it.govpay.wc.controller.WcController;

@Path("/")
public class Wc extends BaseRsServiceV1 {
	
	private WcController controller = null;
	
	public Wc() {
		super("psp");
		this.controller = new WcController(this.nomeServizio,this.log);
	}

	@GET
	@Path("/psp")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getPsp(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam("idSession") String idSession, @QueryParam("esito") String esito) {
		this.controller.setContext(this.getContext());
        return this.controller.getPsp(null, uriInfo, httpHeaders, idSession, esito);
	}
}
