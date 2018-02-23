package it.govpay.backoffice.api.rs.v1.backoffice;

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
		return this.controller.pendenzeGET(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, idDominio, idA2A, idDebitore, idPagamento, stato);
	}


/*
    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response pendenzeGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam("pagina") Integer pagina, @QueryParam("risultatiPerPagina") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("idDominio") String idDominio, @QueryParam("idA2A") String idA2A, @QueryParam("idDebitore") String idDebitore, @QueryParam("stato") StatoPendenza stato, @QueryParam("idPagamento") String idPagamento){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.pendenzeGET(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, idDominio, idA2A, idDebitore, stato, idPagamento);
    }
*/

/*
    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response pendenzeGET_1(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam("pagina") Integer pagina, @QueryParam("risultatiPerPagina") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("idDominio") String idDominio, @QueryParam("idA2A") String idA2A, @QueryParam("idDebitore") String idDebitore, @QueryParam("stato") StatoPendenza stato, @QueryParam("idPagamento") String idPagamento){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.pendenzeGET_1(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, idDominio, idA2A, idDebitore, stato, idPagamento);
    }
*/

/*
    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response pendenzeGET_2(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam("pagina") Integer pagina, @QueryParam("risultatiPerPagina") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("idDominio") String idDominio, @QueryParam("idA2A") String idA2A, @QueryParam("idDebitore") String idDebitore, @QueryParam("stato") StatoPendenza stato, @QueryParam("idPagamento") String idPagamento){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.pendenzeGET_2(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, idDominio, idA2A, idDebitore, stato, idPagamento);
    }
*/


    @GET
    @Path("/{idA2A}/{idPendenza}")
    
    @Produces({ "application/json" })
    public Response pendenzeIdA2AIdPendenzaGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idA2A") String idA2A, @PathParam("idPendenza") String idPendenza){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.pendenzeIdA2AIdPendenzaGET(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders,  idA2A,  idPendenza);
    }


/*
    @GET
    @Path("/{idA2A}/{idPendenza}")
    
    @Produces({ "application/json" })
    public Response pendenzeIdA2AIdPendenzaGET_3(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idA2A") String idA2A, @PathParam("idPendenza") String idPendenza){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.pendenzeIdA2AIdPendenzaGET_3(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders,  idA2A,  idPendenza);
    }
*/

/*
    @GET
    @Path("/{idA2A}/{idPendenza}")
    
    @Produces({ "application/json" })
    public Response pendenzeIdA2AIdPendenzaGET_4(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idA2A") String idA2A, @PathParam("idPendenza") String idPendenza){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.pendenzeIdA2AIdPendenzaGET_4(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders,  idA2A,  idPendenza);
    }
*/

/*
    @PATCH
    @Path("/{idA2A}/{idPendenza}")
    @Consumes({ "application/json" })
    
    public Response pendenzeIdA2AIdPendenzaPATCH(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idA2A") String idA2A, @PathParam("idPendenza") String idPendenza, java.io.InputStream is){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.pendenzeIdA2AIdPendenzaPATCH(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders,  idA2A,  idPendenza, is);
    }
*/

/*
    @PATCH
    @Path("/{idA2A}/{idPendenza}")
    @Consumes({ "application/json" })
    
    public Response pendenzeIdA2AIdPendenzaPATCH_5(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idA2A") String idA2A, @PathParam("idPendenza") String idPendenza, java.io.InputStream is){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.pendenzeIdA2AIdPendenzaPATCH_5(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders,  idA2A,  idPendenza, is);
    }
*/

/*
    @PUT
    @Path("/{idA2A}/{idPendenza}")
    @Consumes({ "application/json" })
    @Produces({ "application/json""application/json" })
    public Response pendenzeIdA2AIdPendenzaPUT(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idA2A") String idA2A, @PathParam("idPendenza") String idPendenza, java.io.InputStream is){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.pendenzeIdA2AIdPendenzaPUT(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders,  idA2A,  idPendenza, is);
    }
*/

/*
    @GET
    @Path("/{idDominio}/{iuv}")
    
    @Produces({ "application/json" })
    public Response pendenzeIdDominioIuvGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam("idDominio") String idDominio, @QueryParam("iuv") String iuv){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.pendenzeIdDominioIuvGET(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders, idDominio, iuv);
    }
*/

}
