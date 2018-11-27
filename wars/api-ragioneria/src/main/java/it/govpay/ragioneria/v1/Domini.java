package it.govpay.ragioneria.v1;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.ragioneria.v1.controller.DominiController;
import it.govpay.rs.v1.BaseRsServiceV1;

@Path("/domini")

public class Domini extends BaseRsServiceV1 {


	private DominiController controller = null;

	public Domini() throws ServiceException {
		super("domini");
		this.controller = new DominiController(this.nomeServizio,this.log);
	}


    @GET
    @Path("/{idDominio}/logo")
    
    @Produces({ "application/json" })
    public Response dominiIdDominioLogoGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.dominiIdDominioLogoGET(this.getUser(), uriInfo, httpHeaders,  idDominio);
    }

}


