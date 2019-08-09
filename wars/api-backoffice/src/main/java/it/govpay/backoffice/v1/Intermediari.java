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

import it.govpay.backoffice.v1.controllers.IntermediariController;
import it.govpay.core.beans.Costanti;
import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/intermediari")

public class Intermediari extends BaseRsServiceV1{


	private IntermediariController controller = null;

	public Intermediari() throws ServiceException {
		super("intermediari");
		this.controller = new IntermediariController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/{idIntermediario}")
    
    @Produces({ "application/json" })
    public Response getIntermediario(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idIntermediario") String idIntermediario){
        this.controller.setContext(this.getContext());
        return this.controller.getIntermediario(this.getUser(), uriInfo, httpHeaders,  idIntermediario);
    }

    @PUT
    @Path("/{idIntermediario}/stazioni/{idStazione}")
    @Consumes({ "application/json" })
    
    public Response addStazione(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idIntermediario") String idIntermediario, @PathParam("idStazione") String idStazione, java.io.InputStream is){
        this.controller.setContext(this.getContext());
        return this.controller.addStazione(this.getUser(), uriInfo, httpHeaders,  idIntermediario,  idStazione, is);
    }

    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response findIntermediari(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato){
        this.controller.setContext(this.getContext());
        return this.controller.findIntermediari(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, abilitato);
    }

    @PUT
    @Path("/{idIntermediario}")
    @Consumes({ "application/json" })
    
    public Response addIntermediario(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idIntermediario") String idIntermediario, java.io.InputStream is){
        this.controller.setContext(this.getContext());
        return this.controller.addIntermediario(this.getUser(), uriInfo, httpHeaders,  idIntermediario, is);
    }

    @GET
    @Path("/{idIntermediario}/stazioni")
    
    @Produces({ "application/json" })
    public Response findStazioni(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idIntermediario") String idIntermediario, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato){
        this.controller.setContext(this.getContext());
        return this.controller.findStazioni(this.getUser(), uriInfo, httpHeaders,  idIntermediario, pagina, risultatiPerPagina, ordinamento, campi, abilitato);
    }

    @GET
    @Path("/{idIntermediario}/stazioni/{idStazione}")
    
    @Produces({ "application/json" })
    public Response getStazione(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idIntermediario") String idIntermediario, @PathParam("idStazione") String idStazione){
        this.controller.setContext(this.getContext());
        return this.controller.getStazione(this.getUser(), uriInfo, httpHeaders,  idIntermediario,  idStazione);
    }

}


