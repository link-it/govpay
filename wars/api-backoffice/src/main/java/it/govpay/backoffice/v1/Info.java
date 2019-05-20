package it.govpay.backoffice.v1;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.backoffice.v1.controllers.InfoController;
import it.govpay.core.utils.InitConstants;
import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/info")

public class Info extends BaseRsServiceV1{


	private InfoController controller = null;

	public Info() throws ServiceException {
		super("info");
		this.controller = new InfoController(this.nomeServizio,this.log);
		String commit = (InitConstants.GOVPAY_BUILD_NUMBER.length() > 7) ? InitConstants.GOVPAY_BUILD_NUMBER.substring(0, 7) : InitConstants.GOVPAY_BUILD_NUMBER;
		this.controller.setGovpayBuildNumber(commit);
		this.controller.setGovpayVersione(InitConstants.GOVPAY_VERSION);
	}

    @GET
    @Path("/")
    @Produces({ "application/json" })
    public Response infoGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders){
        this.controller.setContext(this.getContext());
        return this.controller.infoGET(this.getUser(), uriInfo, httpHeaders);
    }

}


