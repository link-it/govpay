package it.govpay.pagamento.v2;

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
import it.govpay.pagamento.v2.controller.DominiController;
import it.govpay.rs.v2.BaseRsServiceV2;


@Path("/domini")

public class Domini extends BaseRsServiceV2{


	private DominiController controller = null;

	public Domini() throws ServiceException {
		super("domini");
		this.controller = new DominiController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/{idDominio}/tipiPendenza/{idTipoPendenza}")
    
    @Produces({ "application/json" })
    public Response getTipoPendenza(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("idTipoPendenza") String idTipoPendenza){
    	this.buildContext();
        return this.controller.dominiIdDominioTipiPendenzaIdTipoPendenzaGET(this.getUser(), uriInfo, httpHeaders,  idDominio,  idTipoPendenza);
    }

    @GET
    @Path("/{idDominio}/unitaOperative/{idUnitaOperativa}")
    
    @Produces({ "application/json" })
    public Response getUnitaOperativa(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("idUnitaOperativa") String idUnitaOperativa){
        this.buildContext();
        return this.controller.dominiIdDominioUnitaOperativeIdUnitaOperativaGET(this.getUser(), uriInfo, httpHeaders,  idDominio,  idUnitaOperativa);
    }

    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response findDomini(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato, @QueryParam("idStazione") String idStazione){
        this.buildContext();
        return this.controller.dominiGET(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, abilitato, idStazione);
    }

    @GET
    @Path("/{idDominio}")
    
    @Produces({ "application/json" })
    public Response getDominio(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio){
        this.buildContext();
        return this.controller.dominiIdDominioGET(this.getUser(), uriInfo, httpHeaders,  idDominio);
    }

    @GET
    @Path("/{idDominio}/unitaOperative")
    
    @Produces({ "application/json" })
    public Response findUnitaOperative(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato){
        this.buildContext();
        return this.controller.dominiIdDominioUnitaOperativeGET(this.getUser(), uriInfo, httpHeaders,  idDominio, pagina, risultatiPerPagina, ordinamento, campi, abilitato);
    }

    @GET
    @Path("/{idDominio}/tipiPendenza")
    
    @Produces({ "application/json" })
    public Response findTipiPendenza(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato, @QueryParam("tipo") String tipo, @QueryParam("associati") Boolean associati, @QueryParam("form") Boolean form){
    	this.buildContext();
        return this.controller.dominiIdDominioTipiPendenzaGET(this.getUser(), uriInfo, httpHeaders,  idDominio, pagina, risultatiPerPagina, ordinamento, campi, abilitato, tipo, associati, form);
    }
    
    @GET
    @Path("/{idDominio}/logo")
    
    @Produces({ "application/json" })
    public Response getLogo(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio){
        this.buildContext();
        return this.controller.dominiIdDominioLogoGET(this.getUser(), uriInfo, httpHeaders,  idDominio);
    }



}


