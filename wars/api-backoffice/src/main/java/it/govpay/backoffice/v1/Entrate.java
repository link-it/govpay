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

import it.govpay.backoffice.v1.controllers.EntrateController;
import it.govpay.core.beans.Costanti;
import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/entrate")

public class Entrate extends BaseRsServiceV1{


	private EntrateController controller = null;

	public Entrate() throws ServiceException {
		super("entrate");
		this.controller = new EntrateController(this.nomeServizio,this.log);
	}



    @PUT
    @Path("/{idEntrata}")
    @Consumes({ "application/json" })
    
    public Response addEntrata(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idEntrata") String idEntrata, java.io.InputStream is){
        this.buildContext();
        return this.controller.addEntrata(this.getUser(), uriInfo, httpHeaders,  idEntrata, is);
    }

    @GET
    @Path("/{idEntrata}")
    
    @Produces({ "application/json" })
    public Response getEntrata(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idEntrata") String idEntrata){
        this.buildContext();
        return this.controller.getEntrata(this.getUser(), uriInfo, httpHeaders,  idEntrata);
    }

    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response findEntrate(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi){
        this.buildContext();
        return this.controller.findEntrate(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi);
    }

}


