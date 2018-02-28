package it.govpay.ragioneria.api.rs.v1.ragioneria;



import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;

import it.govpay.rs.v1.controllers.base.RendicontazioneController;

import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/rendicontazione")

public class Rendicontazione extends BaseRsServiceV1{


	private RendicontazioneController controller = null;

	public Rendicontazione() {
		super("rendicontazione");
		this.controller = new RendicontazioneController(this.nomeServizio,this.log);
	}



/*
    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response rendicontazioneGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") int pagina,
			@QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") int risultatiPerPagina, @QueryParam("idDominio") String idDominio){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.rendicontazioneGET(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, idDominio);
    }
*/

/*
    @GET
    @Path("/{idFlusso}")
    
    @Produces({ "application/json" })
    public Response rendicontazioneIdFlussoGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idFlusso") String idFlusso){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.rendicontazioneIdFlussoGET(this.getUser(), uriInfo, httpHeaders,  idFlusso);
    }
*/

}


