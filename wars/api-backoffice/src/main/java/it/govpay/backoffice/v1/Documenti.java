package it.govpay.backoffice.v1;


import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.backoffice.v1.controllers.DocumentiController;
import it.govpay.rs.v1.BaseRsServiceV1;



@Path("/documenti")

public class Documenti extends BaseRsServiceV1{


	private DocumentiController controller = null;

	public Documenti() throws ServiceException {
		super("documenti");
		this.controller = new DocumentiController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/{idA2A}/{idDominio}/{numeroDocumento}/avvisi")
    
    @Produces({ "application/pdf" })
    public Response getAvvisiDocumento(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idA2A") String idA2A, @PathParam("idDominio") String idDominio, @PathParam("numeroDocumento") String numeroDocumento, @QueryParam("linguaSecondaria") String linguaSecondaria, @QueryParam("numeriAvviso") List<String> numeriAvviso){
    	this.buildContext();
        return this.controller.getAvvisiDocumento(this.getUser(), uriInfo, httpHeaders,  idA2A,  idDominio,  numeroDocumento, linguaSecondaria, numeriAvviso);
    }

}


