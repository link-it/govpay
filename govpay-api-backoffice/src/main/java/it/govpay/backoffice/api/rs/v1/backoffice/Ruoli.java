package it.govpay.backoffice.api.rs.v1.backoffice;

import javax.ws.rs.Consumes;
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

import it.govpay.rs.v1.BaseRsServiceV1;
import it.govpay.rs.v1.controllers.base.RuoliController;


@Path("/ruoli")

public class Ruoli extends BaseRsServiceV1{


	private RuoliController controller = null;

	public Ruoli() throws ServiceException {
		super("ruoli");
		this.controller = new RuoliController(this.nomeServizio,this.log);
	}



    @PUT
    @Path("/{idRuolo}")
    @Consumes({ "application/json" })
    
    public Response ruoliIdRuoloPUT(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idRuolo") String idRuolo, java.io.InputStream is){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.ruoliIdRuoloPUT(this.getUser(), uriInfo, httpHeaders,  idRuolo, is);
    }

    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response ruoliGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam("pagina") Integer pagina, @QueryParam("risultatiPerPagina") Integer risultatiPerPagina){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.ruoliGET(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina);
    }

    @GET
    @Path("/{idRuolo}")
    
    @Produces({ "application/json" })
    public Response ruoliIdRuoloGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idRuolo") String idRuolo){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.ruoliIdRuoloGET(this.getUser(), uriInfo, httpHeaders,  idRuolo);
    }

}


