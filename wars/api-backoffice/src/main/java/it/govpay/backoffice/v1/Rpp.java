package it.govpay.backoffice.v1;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.backoffice.v1.controllers.RppController;
import it.govpay.core.beans.Costanti;
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
    public Response findRpps(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("idDominio") String idDominio, @QueryParam("iuv") String iuv, @QueryParam("ccp") String ccp, @QueryParam("idA2A") String idA2A, @QueryParam("idPendenza") String idPendenza, @QueryParam("esito") String esito, @QueryParam("idPagamento") String idPagamento, @QueryParam("idDebitore") String idDebitore, @QueryParam("dataRptDa") String dataRptDa, @QueryParam("dataRptA") String dataRptA, @QueryParam("dataRtDa") String dataRtDa, @QueryParam("dataRtA") String dataRtA, @QueryParam("direzione") String direzione, @QueryParam("divisione") String divisione, @QueryParam("tassonomia") String tassonomia, @QueryParam("idUnita") String idUnita, @QueryParam("idTipoPendenza") String idTipoPendenza, @QueryParam("anagraficaDebitore") String anagraficaDebitore){
        this.controller.setContext(this.getContext());
        return this.controller.findRpps(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, idDominio, iuv, ccp, idA2A, idPendenza, esito, idPagamento, idDebitore, dataRptDa, dataRptA, dataRtDa, dataRtA, direzione, divisione, tassonomia, idUnita, idTipoPendenza, anagraficaDebitore);
    }
    
    @GET
    @Path("/{idDominio}/{iuv}/n/a")
    @Produces({ "application/json" })
    public Response getRpp(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("iuv") String iuv){
        this.controller.setContext(this.getContext());
        return this.controller.getRpp(this.getUser(), uriInfo, httpHeaders,  idDominio,  iuv,  "n/a");
    }
    
    @GET
    @Path("/{idDominio}/{iuv}/{ccp}")
    @Produces({ "application/json" })
    public Response getRpp(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("iuv") String iuv, @PathParam("ccp") String ccp){
        this.controller.setContext(this.getContext());
        return this.controller.getRpp(this.getUser(), uriInfo, httpHeaders,  idDominio,  iuv,  ccp);
    }
    
    @GET
    @Path("/{idDominio}/{iuv}/n/a/rt")
    @Produces({ "application/pdf", "application/xml", "application/json" })
    public Response getRt(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("iuv") String iuv, @QueryParam("visualizzaSoggettoDebitore") Boolean visualizzaSoggettoDebitore){
        this.controller.setContext(this.getContext());
        return this.controller.getRt(this.getUser(), uriInfo, httpHeaders,  idDominio,  iuv,  "n/a", visualizzaSoggettoDebitore);
    }

    @GET
    @Path("/{idDominio}/{iuv}/{ccp}/rt")
    @Produces({ "application/pdf", "application/xml", "application/json" })
    public Response getRt(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("iuv") String iuv, @PathParam("ccp") String ccp, @QueryParam("visualizzaSoggettoDebitore") Boolean visualizzaSoggettoDebitore){
        this.controller.setContext(this.getContext());
        return this.controller.getRt(this.getUser(), uriInfo, httpHeaders,  idDominio,  iuv,  ccp, visualizzaSoggettoDebitore);
    }
    
    @GET
    @Path("/{idDominio}/{iuv}/n/a/rpt")
    @Produces({ "application/xml", "application/json" })
    public Response getRpt(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("iuv") String iuv){
        this.controller.setContext(this.getContext());
        return this.controller.getRpt(this.getUser(), uriInfo, httpHeaders,  idDominio,  iuv,  "n/a");
    }
    
    @GET
    @Path("/{idDominio}/{iuv}/{ccp}/rpt")
    
    @Produces({ "application/xml", "application/json" })
    public Response getRpt(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("iuv") String iuv, @PathParam("ccp") String ccp){
        this.controller.setContext(this.getContext());
        return this.controller.getRpt(this.getUser(), uriInfo, httpHeaders,  idDominio,  iuv,  ccp);
    }
    
    @POST
    @Path("/{idDominio}/{iuv}/{ccp}")
    @Consumes({ "application/json" })
    public Response updateRppPOST(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, java.io.InputStream is, @PathParam("idDominio") String idDominio, @PathParam("iuv") String iuv, @PathParam("ccp") String ccp){
        this.controller.setContext(this.getContext());
        if(httpHeaders.getRequestHeader("X-HTTP-Method-Override") != null && !httpHeaders.getRequestHeader("X-HTTP-Method-Override").isEmpty() && httpHeaders.getRequestHeader("X-HTTP-Method-Override").get(0).equals("PATCH"))
        	return this.controller.updateRpp(this.getUser(), uriInfo, httpHeaders, is,  idDominio,  iuv,  ccp);
       
        String transactionId = this.getContext().getTransactionId();
        return this.controller.handleEventoFail(Response.status(405), transactionId, null, "Operazione non consentita").build();
    }
    
    @POST
    @Path("/{idDominio}/{iuv}/n/a")
    @Consumes({ "application/json" })
    public Response updateRppPOST(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, java.io.InputStream is, @PathParam("idDominio") String idDominio, @PathParam("iuv") String iuv){
        this.controller.setContext(this.getContext());
        if(httpHeaders.getRequestHeader("X-HTTP-Method-Override") != null && !httpHeaders.getRequestHeader("X-HTTP-Method-Override").isEmpty() && httpHeaders.getRequestHeader("X-HTTP-Method-Override").get(0).equals("PATCH"))
        	return this.controller.updateRpp(this.getUser(), uriInfo, httpHeaders, is,  idDominio,  iuv,  "n/a");
      
        String transactionId = this.getContext().getTransactionId();
        return this.controller.handleEventoFail(Response.status(405), transactionId, null, "Operazione non consentita").build();
    }

    @PATCH
    @Path("/{idDominio}/{iuv}/{ccp}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    public Response updateRpp(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, java.io.InputStream is, @PathParam("idDominio") String idDominio, @PathParam("iuv") String iuv, @PathParam("ccp") String ccp){
        this.controller.setContext(this.getContext());
        return this.controller.updateRpp(this.getUser(), uriInfo, httpHeaders, is,  idDominio,  iuv,  ccp);
    }

    @PATCH
    @Path("/{idDominio}/{iuv}/n/a")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    public Response updateRpp(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, java.io.InputStream is, @PathParam("idDominio") String idDominio, @PathParam("iuv") String iuv){
        this.controller.setContext(this.getContext());
        return this.controller.updateRpp(this.getUser(), uriInfo, httpHeaders, is,  idDominio,  iuv,  "n/a");
    }
}


