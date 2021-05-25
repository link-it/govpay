package it.govpay.backoffice.v1;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
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

import it.govpay.backoffice.v1.controllers.RuoliController;
import it.govpay.core.beans.Costanti;
import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/ruoli")

public class Ruoli extends BaseRsServiceV1{


	private RuoliController controller = null;

	public Ruoli() throws ServiceException {
		super("ruoli");
		this.controller = new RuoliController(this.nomeServizio,this.log);
	}



    @PATCH
    @Path("/{idRuolo}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    public Response updateRuolo(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, java.io.InputStream is, @PathParam("idRuolo") String idRuolo){
    	this.buildContext();
        return this.controller.updateRuolo(this.getUser(), uriInfo, httpHeaders, is,  idRuolo);
    }

    @PUT
    @Path("/{idRuolo}")
    @Consumes({ "application/json" })
    
    public Response addRuolo(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idRuolo") String idRuolo, java.io.InputStream is){
    	this.buildContext();
        return this.controller.addRuolo(this.getUser(), uriInfo, httpHeaders,  idRuolo, is);
    }

    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response findRuoli(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) Integer risultatiPerPagina){
    	this.buildContext();
        return this.controller.findRuoli(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina);
    }

    @GET
    @Path("/{idRuolo}")
    
    @Produces({ "application/json" })
    public Response getRuolo(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idRuolo") String idRuolo){
    	this.buildContext();
        return this.controller.getRuolo(this.getUser(), uriInfo, httpHeaders,  idRuolo);
    }

}


