package it.govpay.pagamento.v2;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.pagamento.v2.controller.LogoutController;
import it.govpay.rs.v2.BaseRsServiceV2; 


@Path("/logout")

public class Logout extends BaseRsServiceV2{


	private LogoutController controller = null;

	public Logout() throws ServiceException {
		super("logout");
		this.controller = new LogoutController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/")
    
    
    public Response logout(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.logout(this.getUser(), uriInfo, httpHeaders);
    }

}


