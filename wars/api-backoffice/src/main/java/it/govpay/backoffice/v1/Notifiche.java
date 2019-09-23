package it.govpay.backoffice.v1;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.backoffice.v1.controllers.NotificheController;
import it.govpay.core.beans.Costanti;
import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/notifiche")

public class Notifiche extends BaseRsServiceV1{


	private NotificheController controller = null;

	public Notifiche() throws ServiceException  {
		super("notifiche");
		this.controller = new NotificheController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response findNotifiche(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, 
    		@QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("dataDa") String dataDa, @QueryParam("dataA") String dataA, 
    		@QueryParam("stato") String stato, @QueryParam("tipo") String tipo){
    	 this.controller.setContext(this.getContext());
        return this.controller.findNotifiche(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, dataDa, dataA, stato, tipo);
    }

}


