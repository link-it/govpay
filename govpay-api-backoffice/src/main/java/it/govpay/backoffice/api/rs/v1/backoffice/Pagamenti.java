package it.govpay.backoffice.api.rs.v1.backoffice;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
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
import it.govpay.rs.v1.controllers.base.PagamentiController;
import it.govpay.rs.v1.costanti.Costanti;


@Path("/pagamenti")

public class Pagamenti extends BaseRsServiceV1{


	private PagamentiController controller = null;

	public Pagamenti() {
		super("pagamenti");
		this.controller = new PagamentiController(this.nomeServizio,this.log);
	}
	@GET
	@Path("/")
	@Produces({MediaType.APPLICATION_JSON})
	public Response get(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders,
			@QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") int pagina,
			@QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") int risultatiPerPagina,@QueryParam("idSessionePortale") String idSessionePortale,
			@QueryParam("stato") String stato,@QueryParam("versante") String versante,@QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi) {
		this.controller.setRequestResponse(this.request, this.response);
		return this.controller.pagamentiGET(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, stato, versante, idSessionePortale);
	}

/*
    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response pagamentiGET_1(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam("pagina") Integer pagina, @QueryParam("risultatiPerPagina") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("stato") StatoPagamento stato, @QueryParam("versante") String versante, @QueryParam("idSessionePortale") String idSessionePortale){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.pagamentiGET_1(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, stato, versante, idSessionePortale);
    }
*/

/*
    @POST
    @Path("/{idDominio}/{iuv}")
    @Consumes({ "application/json" })
    
    public Response pagamentiIdDominioIuvPOST(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam("idDominio") String idDominio, @QueryParam("iuv") String iuv, java.io.InputStream is){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.pagamentiIdDominioIuvPOST(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders, idDominio, iuv, is);
    }
*/

	@GET
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response pagamentiIdGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("id") String id) {
		this.controller.setRequestResponse(this.request, this.response);
		return this.controller.pagamentiIdGET(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders, id);
	}


/*
    @GET
    @Path("/{id}")
    
    @Produces({ "application/json" })
    public Response pagamentiIdGET_2(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("id") String id){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.pagamentiIdGET_2(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders,  id);
    }
*/

	@POST
	@Path("/")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public Response pagamentiPOST(InputStream is , @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam("idSessionePortale") String idSessionePortale) {
		this.controller.setRequestResponse(this.request, this.response);
		return this.controller.pagamentiPOST(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders, is, idSessionePortale);
	}
	

}


