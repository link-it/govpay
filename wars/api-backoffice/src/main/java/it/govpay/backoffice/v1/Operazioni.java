package it.govpay.backoffice.v1;

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

import it.govpay.backoffice.v1.controllers.OperazioniController;
import it.govpay.core.beans.Costanti;
import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/operazioni")

public class Operazioni extends BaseRsServiceV1{


	private OperazioniController controller = null;

	public Operazioni() throws ServiceException {
		super("operazioni");
		this.controller = new OperazioniController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response findOperazioni(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina){
        this.buildContext();
        return this.controller.findOperazioni(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, null, null);
    }

    @GET
    @Path("/stato/{id}")
    
    @Produces({ "application/json" })
    public Response getStatoOperazione(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("id") String id){
        this.buildContext();
        return this.controller.getStatoOperazione(this.getUser(), uriInfo, httpHeaders,  id);
    }

    @GET
    @Path("/{idOperazione}")
    
    @Produces({ "application/json" })
    public Response getOperazione(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idOperazione") String idOperazione){
        this.buildContext();
        return this.controller.getOperazione(this.getUser(), uriInfo, httpHeaders,  idOperazione);
    }

}


