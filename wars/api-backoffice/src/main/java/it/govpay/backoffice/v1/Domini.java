package it.govpay.backoffice.v1;

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

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.backoffice.v1.controllers.DominiController;
import it.govpay.core.beans.Costanti;
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
    public Response getUnitaOperativa(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("idUnitaOperativa") String idUnitaOperativa){
        this.controller.setContext(this.getContext());
        return this.controller.getUnitaOperativa(this.getUser(), uriInfo, httpHeaders,  idDominio,  idUnitaOperativa);
    }

    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response findDomini(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1")  Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25")  Integer risultatiPerPagina, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato, @QueryParam("ordinamento") String ordinamento, @QueryParam("idStazione") String idStazione, @QueryParam("associati") Boolean associati){
        this.controller.setContext(this.getContext());
        return this.controller.findDomini(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, campi, abilitato, ordinamento, idStazione, associati);
    }

    @GET
    @Path("/{idDominio}/contiAccredito")
    
    @Produces({ "application/json" })
    public Response findContiAccredito(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1")  Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25")  Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato){
        this.controller.setContext(this.getContext());
        return this.controller.findContiAccredito(this.getUser(), uriInfo, httpHeaders,  idDominio, pagina, risultatiPerPagina, ordinamento, campi, abilitato);
    }

    @GET
    @Path("/{idDominio}/entrate")
    
    @Produces({ "application/json" })
    public Response findEntrate(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1")  Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25")  Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato){
        this.controller.setContext(this.getContext());
        return this.controller.findEntrate(this.getUser(), uriInfo, httpHeaders,  idDominio, pagina, risultatiPerPagina, ordinamento, campi, abilitato);
    }

    @GET
    @Path("/{idDominio}/unitaOperative")
    
    @Produces({ "application/json" })
    public Response findUnitaOperative(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1")  Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25")  Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato, @QueryParam("associati") Boolean associati){
        this.controller.setContext(this.getContext());
        return this.controller.findUnitaOperative(this.getUser(), uriInfo, httpHeaders,  idDominio, pagina, risultatiPerPagina, ordinamento, campi, abilitato, associati);
    }

    @GET
    @Path("/{idDominio}/tipiPendenza")
    
    @Produces({ "application/json" })
    public Response findTipiPendenza(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1")  Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25")  Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato, @QueryParam("tipo") String tipo, @QueryParam("associati") Boolean associati, @QueryParam("form") Boolean form){
        this.controller.setContext(this.getContext());
        return this.controller.findTipiPendenza(this.getUser(), uriInfo, httpHeaders,  idDominio, pagina, risultatiPerPagina, ordinamento, campi, abilitato, tipo, associati, form);
    }

    @PUT
    @Path("/{idDominio}/unitaOperative/{idUnitaOperativa}")
    @Consumes({ "application/json" })
    
    public Response addUnitaOperativa(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("idUnitaOperativa") String idUnitaOperativa, java.io.InputStream is){
        this.controller.setContext(this.getContext());
        return this.controller.addUnitaOperativa(this.getUser(), uriInfo, httpHeaders,  idDominio,  idUnitaOperativa, is);
    }

    @PUT
    @Path("/{idDominio}/entrate/{idEntrata}")
    @Consumes({ "application/json" })
    
    public Response addEntrata(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("idEntrata") String idEntrata, java.io.InputStream is){
        this.controller.setContext(this.getContext());
        return this.controller.addEntrata(this.getUser(), uriInfo, httpHeaders,  idDominio,  idEntrata, is);
    }

    @PUT
    @Path("/{idDominio}")
    @Consumes({ "application/json" })
    
    public Response addDominio(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, java.io.InputStream is){
        this.controller.setContext(this.getContext());
        return this.controller.addDominio(this.getUser(), uriInfo, httpHeaders,  idDominio, is);
    }

    @PUT
    @Path("/{idDominio}/tipiPendenza/{idTipoPendenza}")
    @Consumes({ "application/json" })
    
    public Response addTipoPendenza(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("idTipoPendenza") String idTipoPendenza, java.io.InputStream is){
        this.controller.setContext(this.getContext());
        return this.controller.addTipoPendenza(this.getUser(), uriInfo, httpHeaders,  idDominio,  idTipoPendenza, is);
    }

    @PUT
    @Path("/{idDominio}/contiAccredito/{ibanAccredito}")
    @Consumes({ "application/json" })
    
    public Response addContiAccredito(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("ibanAccredito") String ibanAccredito, java.io.InputStream is){
        this.controller.setContext(this.getContext());
        return this.controller.addContiAccredito(this.getUser(), uriInfo, httpHeaders,  idDominio,  ibanAccredito, is);
    }

    @GET
    @Path("/{idDominio}/tipiPendenza/{idTipoPendenza}")
    
    @Produces({ "application/json" })
    public Response getTipoPendenza(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("idTipoPendenza") String idTipoPendenza){
        this.controller.setContext(this.getContext());
        return this.controller.getTipoPendenza(this.getUser(), uriInfo, httpHeaders,  idDominio,  idTipoPendenza);
    }

    @GET
    @Path("/{idDominio}/contiAccredito/{ibanAccredito}")
    
    @Produces({ "application/json" })
    public Response getContiAccredito(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("ibanAccredito") String ibanAccredito){
        this.controller.setContext(this.getContext());
        return this.controller.getContiAccredito(this.getUser(), uriInfo, httpHeaders,  idDominio,  ibanAccredito);
    }

    @GET
    @Path("/{idDominio}")
    
    @Produces({ "application/json" })
    public Response getDominio(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio){
        this.controller.setContext(this.getContext());
        return this.controller.getDominio(this.getUser(), uriInfo, httpHeaders,  idDominio);
    }

    @GET
    @Path("/{idDominio}/entrate/{idEntrata}")
    
    @Produces({ "application/json" })
    public Response getEntrata(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("idEntrata") String idEntrata){
        this.controller.setContext(this.getContext());
        return this.controller.getEntrata(this.getUser(), uriInfo, httpHeaders,  idDominio,  idEntrata);
    }

    @GET
    @Path("/{idDominio}/logo")
    @Produces({ "application/json" })
    public Response getLogo(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio){
            this.controller.setContext(this.getContext());
            return this.controller.getLogo(this.getUser(), uriInfo, httpHeaders,  idDominio);
    }

}


