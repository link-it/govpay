package it.govpay.backoffice.v1;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.backoffice.v1.controllers.FlussiRendicontazioneController;
import it.govpay.core.beans.Costanti;
import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/flussiRendicontazione")

public class FlussiRendicontazione extends BaseRsServiceV1{


	private FlussiRendicontazioneController controller = null;

	public FlussiRendicontazione() throws ServiceException {
		super("flussiRendicontazione");
		this.controller = new FlussiRendicontazioneController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/{idFlusso}")
    
    @Produces({ "application/json", MediaType.APPLICATION_XML })
    public Response flussiRendicontazioneIdFlussoGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idFlusso") String idFlusso){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.flussiRendicontazioneIdFlussoGET(this.getUser(), uriInfo, httpHeaders,  idFlusso);
    }

    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response flussiRendicontazioneGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("idDominio") String idDominio, @QueryParam("dataDa") String dataDa, @QueryParam("dataA") String dataA){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.flussiRendicontazioneGET(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, idDominio, ordinamento, dataDa, dataA);
    }

}


