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

import it.govpay.backoffice.v1.controllers.PromemoriaController;
import it.govpay.core.beans.Costanti;
import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/promemoria")

public class Promemoria extends BaseRsServiceV1{


	private PromemoriaController controller = null;

	public Promemoria() throws ServiceException {
		super("promemoria");
		this.controller = new PromemoriaController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response findPromemoria(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, 
    		 @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, 
    		@QueryParam("dataDa") String dataDa, @QueryParam("dataA") String dataA, @QueryParam("stato") String stato, @QueryParam("tipo") String tipo){
    	 this.buildContext();
        return this.controller.findPromemoria(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, dataDa, dataA, stato, tipo);
    }

}


