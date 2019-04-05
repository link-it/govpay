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

import it.govpay.backoffice.v1.beans.StatoTracciatoPendenza;
import it.govpay.backoffice.v1.controllers.PendenzeController;
import it.govpay.core.beans.Costanti;
import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/pendenze")

public class Pendenze extends BaseRsServiceV1{


	private PendenzeController controller = null;

	public Pendenze() throws ServiceException {
		super("pendenze");
		this.controller = new PendenzeController(this.nomeServizio,this.log);
	}
	
    @POST
    @Path("/")
    @Consumes({ "application/json", "multipart/form-data" })
    public Response pendenzePOST(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, java.io.InputStream is){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.pendenzePOST(this.getUser(), uriInfo, httpHeaders, is);
    }

	@GET
    @Path("/")
    @Produces({ "application/json" })
    public Response pendenzeGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("idDominio") String idDominio, @QueryParam("idA2A") String idA2A, @QueryParam("idDebitore") String idDebitore, @QueryParam("stato") String stato, @QueryParam("idPagamento") String idPagamento, @QueryParam("idPendenza") String idPendenza, @QueryParam("dataDa") String dataDa, @QueryParam("dataA") String dataA){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.pendenzeGET(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, idDominio, idA2A, idDebitore, stato, idPagamento, idPendenza, dataDa, dataA);
    }

    @GET
    @Path("/{idA2A}/{idPendenza}")
    @Produces({ "application/json" })
    public Response pendenzeIdA2AIdPendenzaGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idA2A") String idA2A, @PathParam("idPendenza") String idPendenza){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.pendenzeIdA2AIdPendenzaGET(this.getUser(), uriInfo, httpHeaders,  idA2A,  idPendenza, true);
    }
    
    @PATCH
    @Path("/{idA2A}/{idPendenza}")
    @Consumes({ "application/json" })
    public Response pendenzeIdA2AIdPendenzaPATCH(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idA2A") String idA2A, @PathParam("idPendenza") String idPendenza, java.io.InputStream is){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.pendenzeIdA2AIdPendenzaPATCH(this.getUser(), uriInfo, httpHeaders,  idA2A,  idPendenza, is,true);
    }

    @GET
    @Path("/tracciati")
    @Produces({ "application/json" })
    public Response pendenzeTracciatiGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("idDominio") String idDominio, @QueryParam("statoTracciatoPendenza") StatoTracciatoPendenza stato){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.pendenzeTracciatiGET(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, idDominio, stato);
    }
    
    @GET
    @Path("/tracciati/{id}")
    @Produces({ "application/json" })
    public Response pendenzeTracciatiIdGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("id") Integer id){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.pendenzeTracciatiIdGET(this.getUser(), uriInfo, httpHeaders,  id);
    }

    @GET
    @Path("/tracciati/{id}/esito")
    @Produces({ "application/json" })
    public Response pendenzeTracciatiIdEsitoGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("id") Integer id){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.pendenzeTracciatiIdEsitoGET(this.getUser(), uriInfo, httpHeaders,  id);
    }

    @GET
    @Path("/tracciati/{id}/stampe")
    @Produces({ "application/zip" })
    public Response pendenzeTracciatiIdStampeGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("id") Integer id){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.pendenzeTracciatiIdStampeGET(this.getUser(), uriInfo, httpHeaders,  id);
    }

    @GET
    @Path("/tracciati/{id}/operazioni")
    @Produces({ "application/json" })
    public Response pendenzeTracciatiIdOperazioniGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("id") Integer id, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.pendenzeTracciatiIdOperazioniGET(this.getUser(), uriInfo, httpHeaders,  id, pagina, risultatiPerPagina);
    }

}


