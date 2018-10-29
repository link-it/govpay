package it.govpay.backoffice.api.rs.v1.backoffice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.backoffice.api.listener.InitListener;
import it.govpay.rs.v1.BaseRsServiceV1;
import it.govpay.rs.v1.controllers.base.InfoController;


@Path("/info")

public class Info extends BaseRsServiceV1{


	private InfoController controller = null;

	public Info() throws ServiceException {
		super("info");
		this.controller = new InfoController(this.nomeServizio,this.log);
		String commit = (InitListener.GOVPAY_BUILD_NUMBER.length() > 7) ? InitListener.GOVPAY_BUILD_NUMBER.substring(0, 7) : InitListener.GOVPAY_BUILD_NUMBER;
		this.controller.setGovpayBuildNumber(commit);
		this.controller.setGovpayVersione(InitListener.GOVPAY_VERSION);
	}

    @GET
    @Path("/")
    @Produces({ "application/json" })
    public Response infoGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.infoGET(this.getUser(), uriInfo, httpHeaders);
    }

}


