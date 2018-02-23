package it.govpay.pagamento.api.rs.v1.pagamenti;

import it.govpay.rs.v1.beans.base.Dominio;
import it.govpay.rs.v1.beans.base.DominioPost;
import it.govpay.rs.v1.beans.base.Entrata;
import it.govpay.rs.v1.beans.base.EntrataPost;
import it.govpay.rs.v1.beans.base.IbanAccredito;
import it.govpay.rs.v1.beans.base.IbanAccreditoPost;
import it.govpay.rs.v1.beans.base.UnitaOperativa;
import it.govpay.rs.v1.beans.base.UnitaOperativaPost;


import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;

import it.govpay.rs.v1.controllers.base.DominiController;

import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/domini")

public class Domini extends BaseRsServiceV1{


	private DominiController controller = null;

	public Domini() {
		super("domini");
		this.controller = new DominiController(this.nomeServizio,this.log);
	}



/*
    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response dominiGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam("pagina") Integer pagina, @QueryParam("risultatiPerPagina") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato, @QueryParam("idStazione") String idStazione){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.dominiGET(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, abilitato, idStazione);
    }
*/

/*
    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response dominiGET_1(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam("pagina") Integer pagina, @QueryParam("risultatiPerPagina") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato, @QueryParam("idStazione") String idStazione){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.dominiGET_1(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, abilitato, idStazione);
    }
*/

/*
    @GET
    @Path("/{idDominio}/entrate")
    
    @Produces({ "application/json" })
    public Response dominiIdDominioEntrateGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @QueryParam("pagina") Integer pagina, @QueryParam("risultatiPerPagina") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.dominiIdDominioEntrateGET(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders,  idDominio, pagina, risultatiPerPagina, ordinamento, campi, abilitato);
    }
*/

/*
    @GET
    @Path("/{idDominio}/entrate/{idEntrata}")
    
    @Produces({ "application/json" })
    public Response dominiIdDominioEntrateIdEntrataGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("idEntrata") String idEntrata){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.dominiIdDominioEntrateIdEntrataGET(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders,  idDominio,  idEntrata);
    }
*/

/*
    @PUT
    @Path("/{idDominio}/entrate/{idEntrata}")
    @Consumes({ "application/json" })
    
    public Response dominiIdDominioEntrateIdEntrataPUT(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("idEntrata") String idEntrata, java.io.InputStream is){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.dominiIdDominioEntrateIdEntrataPUT(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders,  idDominio,  idEntrata, is);
    }
*/

/*
    @GET
    @Path("/{idDominio}")
    
    @Produces({ "application/json" })
    public Response dominiIdDominioGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.dominiIdDominioGET(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders,  idDominio);
    }
*/

/*
    @GET
    @Path("/{idDominio}")
    
    @Produces({ "application/json" })
    public Response dominiIdDominioGET_2(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.dominiIdDominioGET_2(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders,  idDominio);
    }
*/

/*
    @GET
    @Path("/{idDominio}/ibanAccredito")
    
    @Produces({ "application/json" })
    public Response dominiIdDominioIbanAccreditoGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @QueryParam("pagina") Integer pagina, @QueryParam("risultatiPerPagina") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.dominiIdDominioIbanAccreditoGET(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders,  idDominio, pagina, risultatiPerPagina, ordinamento, campi, abilitato);
    }
*/

/*
    @GET
    @Path("/{idDominio}/ibanAccredito/{ibanAccredito}")
    
    @Produces({ "application/json" })
    public Response dominiIdDominioIbanAccreditoIbanAccreditoGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("ibanAccredito") String ibanAccredito){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.dominiIdDominioIbanAccreditoIbanAccreditoGET(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders,  idDominio,  ibanAccredito);
    }
*/

/*
    @PUT
    @Path("/{idDominio}/ibanAccredito/{ibanAccredito}")
    @Consumes({ "application/json" })
    
    public Response dominiIdDominioIbanAccreditoIbanAccreditoPUT(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("ibanAccredito") String ibanAccredito, java.io.InputStream is){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.dominiIdDominioIbanAccreditoIbanAccreditoPUT(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders,  idDominio,  ibanAccredito, is);
    }
*/

/*
    @PUT
    @Path("/{idDominio}")
    @Consumes({ "application/json" })
    
    public Response dominiIdDominioPUT(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, java.io.InputStream is){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.dominiIdDominioPUT(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders,  idDominio, is);
    }
*/

/*
    @GET
    @Path("/{idDominio}/unitaOperative")
    
    @Produces({ "application/json" })
    public Response dominiIdDominioUnitaOperativeGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @QueryParam("pagina") Integer pagina, @QueryParam("risultatiPerPagina") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.dominiIdDominioUnitaOperativeGET(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders,  idDominio, pagina, risultatiPerPagina, ordinamento, campi, abilitato);
    }
*/

/*
    @GET
    @Path("/{idDominio}/unitaOperative")
    
    @Produces({ "application/json" })
    public Response dominiIdDominioUnitaOperativeGET_3(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @QueryParam("pagina") Integer pagina, @QueryParam("risultatiPerPagina") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.dominiIdDominioUnitaOperativeGET_3(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders,  idDominio, pagina, risultatiPerPagina, ordinamento, campi, abilitato);
    }
*/

/*
    @GET
    @Path("/{idDominio}/unitaOperative/{idUnitaOperativa}")
    
    @Produces({ "application/json" })
    public Response dominiIdDominioUnitaOperativeIdUnitaOperativaGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("idUnitaOperativa") String idUnitaOperativa){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.dominiIdDominioUnitaOperativeIdUnitaOperativaGET(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders,  idDominio,  idUnitaOperativa);
    }
*/

/*
    @GET
    @Path("/{idDominio}/unitaOperative/{idUnitaOperativa}")
    
    @Produces({ "application/json" })
    public Response dominiIdDominioUnitaOperativeIdUnitaOperativaGET_4(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("idUnitaOperativa") String idUnitaOperativa){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.dominiIdDominioUnitaOperativeIdUnitaOperativaGET_4(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders,  idDominio,  idUnitaOperativa);
    }
*/

/*
    @PUT
    @Path("/{idDominio}/unitaOperative/{idUnitaOperativa}")
    @Consumes({ "application/json" })
    
    public Response dominiIdDominioUnitaOperativeIdUnitaOperativaPUT(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("idUnitaOperativa") String idUnitaOperativa, java.io.InputStream is){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.dominiIdDominioUnitaOperativeIdUnitaOperativaPUT(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders,  idDominio,  idUnitaOperativa, is);
    }
*/

}


