package it.govpay.pagamento.api.rs.v1.pagamenti;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
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
import it.govpay.rs.v1.controllers.base.PendenzeController;
import it.govpay.rs.v1.costanti.Costanti;

@Path("/pendenze")
public class Pendenze extends BaseRsServiceV1{

	private PendenzeController controller = null;

	public Pendenze() {
		super("pendenze");
		this.controller = new PendenzeController(this.nomeServizio,this.log);
	}

	@GET
	@Path("/")
	@Produces({MediaType.APPLICATION_JSON})
	public Response pendenzeGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders,
			@QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") int pagina,
			@QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") int risultatiPerPagina,
			@QueryParam("idDominio") String idDominio, @QueryParam("idDebitore") String idDebitore,
			@QueryParam("idA2A") String idA2A, @QueryParam("idPagamento") String idPagamento,   
			@QueryParam("stato") String stato, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi) {
		this.controller.setRequestResponse(this.request, this.response);
		return this.controller.pendenzeGET(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, idDominio, idA2A, idDebitore, idPagamento, stato);
	}


	@GET
	@Path("/{idA2A}/{idPendenza}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getByIda2aIdPendenza(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders,
			@PathParam("idA2A") String idA2A, @PathParam("idPendenza") String idPendenza) {
		this.controller.setRequestResponse(this.request, this.response);
		return this.controller.pendenzeIdA2AIdPendenzaGET(this.getUser(), uriInfo, httpHeaders, idA2A, idPendenza);
	}
}
