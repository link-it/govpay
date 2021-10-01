package it.govpay.pendenze.v1;

import javax.ws.rs.DefaultValue;
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

import it.govpay.core.beans.Costanti;
import it.govpay.pendenze.v1.controller.RppController;
import it.govpay.rs.v1.BaseRsServiceV1;

@Path("/rpp")

public class Rpp extends BaseRsServiceV1{


	private RppController controller = null;

	public Rpp() throws ServiceException {
		super("rpp");
		this.controller = new RppController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response findRpps(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("dataDa") String dataDa, @QueryParam("dataA") String dataA, @QueryParam("idDominio") String idDominio, @QueryParam("iuv") String iuv, @QueryParam("ccp") String ccp, @QueryParam("idA2A") String idA2A, @QueryParam("idPendenza") String idPendenza, @QueryParam("esito") String esito, @QueryParam("idPagamento") String idPagamento, @QueryParam("metadatiPaginazione") @DefaultValue(value="true") Boolean metadatiPaginazione, @QueryParam("maxRisultati") @DefaultValue(value="true") Boolean maxRisultati){
        this.buildContext();
        return this.controller.rppGET(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, dataDa, dataA, idDominio, iuv, ccp, idA2A, idPendenza, esito, idPagamento, metadatiPaginazione, maxRisultati);
    }
    
    @GET
    @Path("/{idDominio}/{iuv}/n/a")
    @Produces({ "application/json" })
    public Response getRpp(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("iuv") String iuv){
        this.buildContext();
        return this.controller.rppIdDominioIuvCcpGET(this.getUser(), uriInfo, httpHeaders,  idDominio,  iuv,  "n/a");
    }
    
    @GET
    @Path("/{idDominio}/{iuv}/{ccp}")
    @Produces({ "application/json" })
    public Response getRpp(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("iuv") String iuv, @PathParam("ccp") String ccp){
        this.buildContext();
        return this.controller.rppIdDominioIuvCcpGET(this.getUser(), uriInfo, httpHeaders,  idDominio,  iuv,  ccp);
    }
    
    @GET
    @Path("/{idDominio}/{iuv}/n/a/rt")
    @Produces({ "application/pdf", "application/xml", "application/json" })
    public Response getRt(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("iuv") String iuv, @QueryParam("visualizzaSoggettoDebitore") Boolean visualizzaSoggettoDebitore){
        this.buildContext();
        return this.controller.rppIdDominioIuvCcpRtGET(this.getUser(), uriInfo, httpHeaders,  idDominio,  iuv,  "n/a", visualizzaSoggettoDebitore);
    }

    @GET
    @Path("/{idDominio}/{iuv}/{ccp}/rt")
    @Produces({ "application/pdf", "application/xml", "application/json" })
    public Response getRt(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("iuv") String iuv, @PathParam("ccp") String ccp, @QueryParam("visualizzaSoggettoDebitore") Boolean visualizzaSoggettoDebitore){
        this.buildContext();
        return this.controller.rppIdDominioIuvCcpRtGET(this.getUser(), uriInfo, httpHeaders,  idDominio,  iuv,  ccp, visualizzaSoggettoDebitore);
    }
    
    @GET
    @Path("/{idDominio}/{iuv}/n/a/rpt")
    
    @Produces({ "application/xml", "application/json" })
    public Response getRpt(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("iuv") String iuv){
        this.buildContext();
        return this.controller.rppIdDominioIuvCcpRptGET(this.getUser(), uriInfo, httpHeaders,  idDominio,  iuv,  "n/a");
    }
    
    @GET
    @Path("/{idDominio}/{iuv}/{ccp}/rpt")
    
    @Produces({ "application/xml", "application/json" })
    public Response getRpt(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("iuv") String iuv, @PathParam("ccp") String ccp){
        this.buildContext();
        return this.controller.rppIdDominioIuvCcpRptGET(this.getUser(), uriInfo, httpHeaders,  idDominio,  iuv,  ccp);
    }
}


