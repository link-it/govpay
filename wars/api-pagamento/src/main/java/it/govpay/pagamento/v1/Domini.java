package it.govpay.pagamento.v1;

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
import it.govpay.pagamento.v1.controller.DominiController;
import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/domini")

public class Domini extends BaseRsServiceV1{


	private DominiController controller = null;

	public Domini() throws ServiceException {
		super("domini");
		this.controller = new DominiController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/{idDominio}/unitaOperative/{idUnitaOperativa}")
    
    @Produces({ "application/json" })
    public Response dominiIdDominioUnitaOperativeIdUnitaOperativaGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("idUnitaOperativa") String idUnitaOperativa){
        this.controller.setContext(this.getContext());
        return this.controller.dominiIdDominioUnitaOperativeIdUnitaOperativaGET(this.getUser(), uriInfo, httpHeaders,  idDominio,  idUnitaOperativa);
    }

    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response dominiGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato, @QueryParam("idStazione") String idStazione){
        this.controller.setContext(this.getContext());
        return this.controller.dominiGET(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, abilitato, idStazione);
    }

    @GET
    @Path("/{idDominio}/contiAccredito")
    
    @Produces({ "application/json" })
    public Response dominiIdDominioContiAccreditoGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato, @QueryParam("ordinamento") String ordinamento){
        this.controller.setContext(this.getContext());
        return this.controller.dominiIdDominioContiAccreditoGET(this.getUser(), uriInfo, httpHeaders,  idDominio, pagina, risultatiPerPagina, campi, abilitato, ordinamento);
    }

    @GET
    @Path("/{idDominio}")
    
    @Produces({ "application/json" })
    public Response dominiIdDominioGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio){
        this.controller.setContext(this.getContext());
        return this.controller.dominiIdDominioGET(this.getUser(), uriInfo, httpHeaders,  idDominio);
    }

    @GET
    @Path("/{idDominio}/entrate")
    
    @Produces({ "application/json" })
    public Response dominiIdDominioEntrateGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25")Integer risultatiPerPagina, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato, @QueryParam("ordinamento") String ordinamento){
        this.controller.setContext(this.getContext());
        return this.controller.dominiIdDominioEntrateGET(this.getUser(), uriInfo, httpHeaders,  idDominio, pagina, risultatiPerPagina, ordinamento, campi, abilitato);
    }

    @GET
    @Path("/{idDominio}/unitaOperative")
    
    @Produces({ "application/json" })
    public Response dominiIdDominioUnitaOperativeGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato){
        this.controller.setContext(this.getContext());
        return this.controller.dominiIdDominioUnitaOperativeGET(this.getUser(), uriInfo, httpHeaders,  idDominio, pagina, risultatiPerPagina, ordinamento, campi, abilitato);
    }

    @GET
    @Path("/{idDominio}/entrate/{idEntrata}")
    
    @Produces({ "application/json" })
    public Response dominiIdDominioEntrateIdEntrataGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("idEntrata") String idEntrata){
        this.controller.setContext(this.getContext());
        return this.controller.dominiIdDominioEntrateIdEntrataGET(this.getUser(), uriInfo, httpHeaders,  idDominio,  idEntrata);
    }

    @GET
    @Path("/{idDominio}/contiAccredito/{iban}")
    
    @Produces({ "application/json" })
    public Response dominiIdDominioContiAccreditoIbanGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("iban") String iban){
        this.controller.setContext(this.getContext());
        return this.controller.dominiIdDominioContiAccreditoIbanGET(this.getUser(), uriInfo, httpHeaders,  idDominio,  iban);
    }
    
    @GET
    @Path("/{idDominio}/logo")
    
    @Produces({ "application/json" })
    public Response dominiIdDominioLogoGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio){
        this.controller.setContext(this.getContext());
        return this.controller.dominiIdDominioLogoGET(this.getUser(), uriInfo, httpHeaders,  idDominio);
    }



}


