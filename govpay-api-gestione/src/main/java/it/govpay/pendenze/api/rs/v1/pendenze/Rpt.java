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
import it.govpay.rs.v1.controllers.base.RptController;
import it.govpay.rs.v1.costanti.Costanti;


@Path("/rpt")

public class Rpt extends BaseRsServiceV1{


	private RptController controller = null;

	public Rpt() {
		super("rpt");
		this.controller = new RptController(this.nomeServizio,this.log);
	}

	
	@GET
	@Path("/")

        @Produces({ "application/json" })
	public Response get(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders,
			@QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") int pagina,
			@QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") int risultatiPerPagina,
			@QueryParam("idDominio") String idDominio, @QueryParam("iuv") String iuv, @QueryParam("ccp") String ccp,
			@QueryParam("idA2A") String idA2A, @QueryParam("idPendenza") String idPendenza, @QueryParam("idPagamento") String idPagamento,   
			@QueryParam("dataDa") String dataDa,  @QueryParam("dataA") String dataA,  @QueryParam("esito") String esito, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi) {
		this.controller.setRequestResponse(this.request, this.response);
		return this.controller.rptGET(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, idDominio, iuv, ccp, idA2A, idPendenza, idPagamento, esito);
	}

/*
    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response rptGET_1(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam("pagina") Integer pagina, @QueryParam("risultatiPerPagina") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("idDominio") String idDominio, @QueryParam("iuv") String iuv, @QueryParam("ccp") String ccp, @QueryParam("idA2A") String idA2A, @QueryParam("idPendenza") String idPendenza, @QueryParam("esito") EsitoRpt esito){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.rptGET_1(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, idDominio, iuv, ccp, idA2A, idPendenza, esito);
    }
*/

/*
    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response rptGET_2(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam("pagina") Integer pagina, @QueryParam("risultatiPerPagina") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("idDominio") String idDominio, @QueryParam("iuv") String iuv, @QueryParam("ccp") String ccp, @QueryParam("idA2A") String idA2A, @QueryParam("idPendenza") String idPendenza, @QueryParam("esito") EsitoRpt esito){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.rptGET_2(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, idDominio, iuv, ccp, idA2A, idPendenza, esito);
    }
*/

	@GET
	@Path("/{idDominio}/{iuv}/{ccp}")
        @Produces({ "application/json" })
	public Response getRptByIdDominioIuvCcp(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders,
			@PathParam("idDominio") String idDominio, @PathParam("iuv") String iuv, @PathParam("ccp") String ccp) {
		this.controller.setRequestResponse(this.request, this.response);
		return this.controller.rptIdDominioIuvCcpGET(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders, idDominio, iuv, ccp);
	}
	



/*
    @GET
    @Path("/{idDominio}/{iuv}/{ccp}")
    
    @Produces({ "application/json" })
    public Response rptIdDominioIuvCcpGET_3(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("iuv") String iuv, @PathParam("ccp") String ccp){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.rptIdDominioIuvCcpGET_3(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders,  idDominio,  iuv,  ccp);
    }
*/

/*
    @GET
    @Path("/{idDominio}/{iuv}/{ccp}")
    
    @Produces({ "application/json" })
    public Response rptIdDominioIuvCcpGET_4(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("iuv") String iuv, @PathParam("ccp") String ccp){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.rptIdDominioIuvCcpGET_4(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders,  idDominio,  iuv,  ccp);
    }
*/

	@GET
	@Path("/{idDominio}/{iuv}/{ccp}/rt")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_OCTET_STREAM,MediaType.APPLICATION_XML,"application/pdf"})
	public Response getRtByIdDominioIuvCcp(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders,
			@PathParam("idDominio") String idDominio, @PathParam("iuv") String iuv, @PathParam("ccp") String ccp) {
		this.controller.setRequestResponse(this.request, this.response);
		return this.controller.rptIdDominioIuvCcpRtGET(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders, idDominio, iuv, ccp);
	}



/*
    @GET
    @Path("/{idDominio}/{iuv}/{ccp}/rt")
    
    @Produces({ "application/pdf", "application/xml", "application/json" })
    public Response rptIdDominioIuvCcpRtGET_5(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("iuv") String iuv, @PathParam("ccp") String ccp){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.rptIdDominioIuvCcpRtGET_5(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders,  idDominio,  iuv,  ccp);
    }
*/

/*
    @GET
    @Path("/{idDominio}/{iuv}/{ccp}/rt")
    
    @Produces({ "application/pdf", "application/xml", "application/json" })
    public Response rptIdDominioIuvCcpRtGET_6(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("iuv") String iuv, @PathParam("ccp") String ccp){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.rptIdDominioIuvCcpRtGET_6(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders,  idDominio,  iuv,  ccp);
    }
*/

}


