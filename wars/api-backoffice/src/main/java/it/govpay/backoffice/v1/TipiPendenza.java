package it.govpay.backoffice.v1;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.backoffice.v1.controllers.TipiPendenzaController;
import it.govpay.rs.v1.BaseRsServiceV1; 


@Path("/tipiPendenza")

public class TipiPendenza extends BaseRsServiceV1{


	private TipiPendenzaController controller = null;

	public TipiPendenza() throws ServiceException{
		super("tipiPendenza");
		this.controller = new TipiPendenzaController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/{idTipoPendenza}")
    
    @Produces({ "application/json" })
    public Response tipiPendenzaIdTipoPendenzaGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idTipoPendenza") String idTipoPendenza){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.tipiPendenzaIdTipoPendenzaGET(this.getUser(), uriInfo, httpHeaders,  idTipoPendenza);
    }

    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response tipiPendenzaGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam("pagina") Integer pagina, @QueryParam("risultatiPerPagina") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.tipiPendenzaGET(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi);
    }

    @PUT
    @Path("/{idTipoPendenza}")
    @Consumes({ "application/json" })
    
    public Response tipiPendenzaIdTipoPendenzaPUT(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idTipoPendenza") String idTipoPendenza, java.io.InputStream is){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.tipiPendenzaIdTipoPendenzaPUT(this.getUser(), uriInfo, httpHeaders,  idTipoPendenza, is);
    }

}


