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

import it.govpay.wc.ecsp.BaseRsService;

@Path("/")
public class Psp extends BaseRsService {

	@GET
	@Path("/psp")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getPsp(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam("idSessione") String idSessione, @QueryParam("esito") String esito) {
		return Response.ok().build();
	}
}
