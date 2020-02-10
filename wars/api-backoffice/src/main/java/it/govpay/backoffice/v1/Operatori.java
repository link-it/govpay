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

import it.govpay.backoffice.v1.controllers.OperatoriController;
import it.govpay.core.beans.Costanti;
import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/operatori")

public class Operatori extends BaseRsServiceV1{


	private OperatoriController controller = null;

	public Operatori() throws ServiceException {
		super("operatori");
		this.controller = new OperatoriController(this.nomeServizio,this.log);
	}



    @PUT
    @Path("/{principal}")
    @Consumes({ "application/json" })
    public Response addOperatore(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("principal") String principal, java.io.InputStream is){
        this.buildContext();
        return this.controller.addOperatore(this.getUser(), uriInfo, httpHeaders,  principal, is);
    }

    @PATCH
    @Path("/{principal}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    public Response updateOperatore(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, java.io.InputStream is, @PathParam("principal") String principal){
        this.buildContext();
        return this.controller.updateOperatore(this.getUser(), uriInfo, httpHeaders, is,  principal);
    }

    @GET
    @Path("/{principal}")
    @Produces({ "application/json" })
    public Response getOperatore(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("principal") String principal){
        this.buildContext();
        return this.controller.getOperatore(this.getUser(), uriInfo, httpHeaders,  principal);
    }

    @GET
    @Path("/")
    @Produces({ "application/json" })
    public Response findOperatori(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato){
        this.buildContext();
        return this.controller.findOperatori(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, abilitato);
    }

}


