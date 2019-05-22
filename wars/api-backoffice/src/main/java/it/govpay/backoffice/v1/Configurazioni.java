package it.govpay.backoffice.v1;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.backoffice.v1.controllers.ConfigurazioniController;
import it.govpay.rs.v1.BaseRsServiceV1;

@Path("/configurazioni")

public class Configurazioni extends BaseRsServiceV1{


	private ConfigurazioniController controller = null;

	public Configurazioni() throws ServiceException {
		super("configurazioni");
		this.controller = new ConfigurazioniController(this.nomeServizio,this.log);
	}



	@GET
	@Path("/giornale")

	@Produces({ "application/json" })
	public Response configurazioniGiornaleGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders){
		this.controller.setContext(this.getContext());
		return this.controller.configurazioniGiornaleGET(this.getUser(), uriInfo, httpHeaders);
	}

	@POST
	@Path("/giornale")
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	public Response configurazioniGiornalePOST(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, java.io.InputStream is){
		this.controller.setContext(this.getContext());
		return this.controller.configurazioniGiornalePOST(this.getUser(), uriInfo, httpHeaders, is);
	}

}


