package it.govpay.backoffice.api.rs.v1.backoffice;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import it.govpay.rs.v1.BaseRsServiceV1;
import it.govpay.rs.v1.controllers.base.DominiController;
import it.govpay.rs.v1.costanti.Costanti;


@Path("/domini")

public class Domini extends BaseRsServiceV1{


	private DominiController controller = null;

	public Domini() {
		super("domini");
		this.controller = new DominiController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/{idDominio}/ibanAccredito/{ibanAccredito}")
    
    @Produces({ "application/json" })
    public Response dominiIdDominioIbanAccreditoIbanAccreditoGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("ibanAccredito") String ibanAccredito){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.dominiIdDominioIbanAccreditoIbanAccreditoGET(this.getUser(), uriInfo, httpHeaders,  idDominio,  ibanAccredito);
    }

    @GET
    @Path("/{idDominio}/unitaOperative/{idUnitaOperativa}")
    
    @Produces({ "application/json" })
    public Response dominiIdDominioUnitaOperativeIdUnitaOperativaGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("idUnitaOperativa") String idUnitaOperativa){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.dominiIdDominioUnitaOperativeIdUnitaOperativaGET(this.getUser(), uriInfo, httpHeaders,  idDominio,  idUnitaOperativa);
    }

    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response dominiGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato, @QueryParam("idStazione") String idStazione){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.dominiGET(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, abilitato, idStazione);
    }

    @GET
    @Path("/{idDominio}")
    
    @Produces({ "application/json" })
    public Response dominiIdDominioGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.dominiIdDominioGET(this.getUser(), uriInfo, httpHeaders,  idDominio);
    }

    @GET
    @Path("/{idDominio}/entrate")
    
    @Produces({ "application/json" })
    public Response dominiIdDominioEntrateGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.dominiIdDominioEntrateGET(this.getUser(), uriInfo, httpHeaders,  idDominio, pagina, risultatiPerPagina, ordinamento, campi, abilitato);
    }

    @GET
    @Path("/{idDominio}/unitaOperative")
    
    @Produces({ "application/json" })
    public Response dominiIdDominioUnitaOperativeGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.dominiIdDominioUnitaOperativeGET(this.getUser(), uriInfo, httpHeaders,  idDominio, pagina, risultatiPerPagina, ordinamento, campi, abilitato);
    }

    @GET
    @Path("/{idDominio}/entrate/{idEntrata}")
    
    @Produces({ "application/json" })
    public Response dominiIdDominioEntrateIdEntrataGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("idEntrata") String idEntrata){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.dominiIdDominioEntrateIdEntrataGET(this.getUser(), uriInfo, httpHeaders,  idDominio,  idEntrata);
    }

    @PUT
    @Path("/{idDominio}/ibanAccredito/{ibanAccredito}")
    @Consumes({ "application/json" })
    
    public Response dominiIdDominioIbanAccreditoIbanAccreditoPUT(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("ibanAccredito") String ibanAccredito, java.io.InputStream is){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.dominiIdDominioIbanAccreditoIbanAccreditoPUT(this.getUser(), uriInfo, httpHeaders,  idDominio,  ibanAccredito, is);
    }

    @PUT
    @Path("/{idDominio}/unitaOperative/{idUnitaOperativa}")
    @Consumes({ "application/json" })
    
    public Response dominiIdDominioUnitaOperativeIdUnitaOperativaPUT(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("idUnitaOperativa") String idUnitaOperativa, java.io.InputStream is){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.dominiIdDominioUnitaOperativeIdUnitaOperativaPUT(this.getUser(), uriInfo, httpHeaders,  idDominio,  idUnitaOperativa, is);
    }

    @PUT
    @Path("/{idDominio}/entrate/{idEntrata}")
    @Consumes({ "application/json" })
    
    public Response dominiIdDominioEntrateIdEntrataPUT(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("idEntrata") String idEntrata, java.io.InputStream is){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.dominiIdDominioEntrateIdEntrataPUT(this.getUser(), uriInfo, httpHeaders,  idDominio,  idEntrata, is);
    }

    @PUT
    @Path("/{idDominio}")
    @Consumes({ "application/json" })
    
    public Response dominiIdDominioPUT(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, java.io.InputStream is){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.dominiIdDominioPUT(this.getUser(), uriInfo, httpHeaders,  idDominio, is);
    }

    @GET
    @Path("/{idDominio}/ibanAccredito")
    
    @Produces({ "application/json" })
    public Response dominiIdDominioIbanAccreditoGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.dominiIdDominioIbanAccreditoGET(this.getUser(), uriInfo, httpHeaders,  idDominio, pagina, risultatiPerPagina, ordinamento, campi, abilitato);
    }

}


