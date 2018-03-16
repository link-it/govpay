package it.govpay.wc.ecsp.v1.gw;

import java.io.InputStream;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import it.govpay.rs.v1.BaseRsServiceV1;
import it.govpay.rs.v1.controllers.gateway.GatewayController;

@Path("/")
public class Gateway extends BaseRsServiceV1{
	
	private GatewayController controller = null;
	
	public Gateway() {
		super("gateway");
		this.controller = new GatewayController(this.nomeServizio,this.log);
	}

	@POST
	@Path("/v1/gw/{id}")
	@Produces({MediaType.TEXT_HTML})
	public Response post_GW(InputStream is, @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("id") String idSessione, @QueryParam("action") String action) {
		this.controller.setRequestResponse(this.request, this.response);
        return this.controller.post_GW(this.getUser(),is, uriInfo, httpHeaders, idSessione, action);
	}
	
	@GET
	@Path("/v1/gw/{id}")
	@Produces({MediaType.TEXT_HTML})
	public Response get_GW(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("id") String idSessione, @QueryParam("action") String action, @QueryParam("idDominio") String idDominio,
			@QueryParam("keyPA") String keyPA, @QueryParam("keyWISP") String keyWISP ,@QueryParam("type") String type) {
		 	this.controller.setRequestResponse(this.request, this.response);
		 	return this.controller.get_GW(this.getUser(), uriInfo, httpHeaders, idSessione, action, idDominio, keyPA, keyWISP, type);
	}
}
