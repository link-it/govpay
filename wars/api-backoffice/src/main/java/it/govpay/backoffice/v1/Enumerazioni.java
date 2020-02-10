package it.govpay.backoffice.v1;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.backoffice.v1.controllers.EnumerazioniController;
import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/enumerazioni")

public class Enumerazioni extends BaseRsServiceV1{


	private EnumerazioniController controller = null;

	public Enumerazioni() throws ServiceException {
		super("enumerazioni");
		this.controller = new EnumerazioniController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/componentiEvento")
    
    @Produces({ "application/json" })
    public Response findEnumerazioniComponentiEvento(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders){
        this.buildContext();
        return this.controller.findEnumerazioniComponentiEvento(this.getUser(), uriInfo, httpHeaders);
    }

    @GET
    @Path("/serviziACL")
    
    @Produces({ "application/json" })
    public Response findEnumerazioniServiziACL(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders){
        this.buildContext();
        return this.controller.findEnumerazioniServiziACL(this.getUser(), uriInfo, httpHeaders);
    }

    @GET
    @Path("/versioneConnettore")
    
    @Produces({ "application/json" })
    public Response findEnumerazioniVersioneConnettore(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders){
        this.buildContext();
        return this.controller.findEnumerazioniVersioneConnettore(this.getUser(), uriInfo, httpHeaders);
    }

    @GET
    @Path("/labelTipiEvento")
    
    @Produces({ "application/json" })
    public Response findEnumerazioniLabelTipiEvento(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders){
        this.buildContext();
        return this.controller.findEnumerazioniLabelTipiEvento(this.getUser(), uriInfo, httpHeaders);
    }

}


