package it.govpay.pagamento.api.rs.v1.pendenze;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import it.govpay.pagamento.api.rs.BaseRsService;

@Path("/pendenze")
public class Pendenze extends BaseRsService{


	public Pendenze() {
		super("pendenze");
	}
	
	
	@POST
	@Path("/")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public Response inserisciPendenza(InputStream is , @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam("idSessionePortale") String idSessionePortale) {
		return Response.ok().build();
	}
	
	
	@GET
	@Path("/")
	@Produces({MediaType.APPLICATION_JSON})
	public Response get(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders,
			@QueryParam("from") String from,  @QueryParam("size") String size,  
			@QueryParam("stato") String stato,@QueryParam("sort") String sort,
			@QueryParam("idDominio") String idDominio,@QueryParam("idDebitore") String idDebitore	) {
		return Response.ok().build();
	}
}
