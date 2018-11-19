package it.govpay.ragioneria.api.rs.v1.ragioneria;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
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

import it.govpay.core.rs.v1.costanti.Costanti;
import it.govpay.rs.v1.BaseRsServiceV1;
import it.govpay.rs.v1.controllers.ragioneria.IncassiController;


@Path("/incassi")

public class Incassi extends BaseRsServiceV1{


	private IncassiController controller = null;

	public Incassi() throws ServiceException {
		super("incassi");
		this.controller = new IncassiController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response incassiGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.incassiGET(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina);
    }

    @GET
    @Path("/{idDominio}/{idIncasso}")
    
    @Produces({ "application/json" })
    public Response incassiIdDominioIdIncassoGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("idIncasso") String idIncasso){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.incassiIdDominioIdIncassoGET(this.getUser(), uriInfo, httpHeaders,  idDominio,  idIncasso);
    }

    @POST
    @Path("/{idDominio}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    public Response incassiIdDominioPOST(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, java.io.InputStream is){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.incassiIdDominioPOST(this.getUser(), uriInfo, httpHeaders,  idDominio, is);
    }

}


