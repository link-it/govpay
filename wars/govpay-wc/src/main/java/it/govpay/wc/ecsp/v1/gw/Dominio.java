package it.govpay.wc.ecsp.v1.gw;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.rs.v1.BaseRsServiceV1;
import it.govpay.rs.v1.controllers.base.DominiController;

@Path("/")
public class Dominio extends BaseRsServiceV1{
	
	private DominiController controller = null;
	
	public Dominio() throws ServiceException {
		super("dominio");
		this.controller = new DominiController(this.nomeServizio,this.log);
	}

	@GET
	@Path("/domini/{idDominio}/logo")
	@Produces({MediaType.WILDCARD})
	public Response getLogo(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio) {
		this.controller.setRequestResponse(this.request, this.response);
        return this.controller.getLogo(this.getUser(),uriInfo, httpHeaders, idDominio);
	}
}
