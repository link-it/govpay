package it.govpay.ragioneria.api.rs.v1.ragioneria;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import it.govpay.core.rs.v1.costanti.Costanti;
import it.govpay.rs.v1.BaseRsServiceV1;
import it.govpay.rs.v1.controllers.base.FlussiRendicontazioneController;


@Path("/flussiRendicontazione")

public class FlussiRendicontazione extends BaseRsServiceV1{


	private FlussiRendicontazioneController controller = null;

	public FlussiRendicontazione() {
		super("flussiRendicontazione");
		this.controller = new FlussiRendicontazioneController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/{idFlusso}")
    
    @Produces({ "application/json" })
    public Response flussiRendicontazioneIdFlussoGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idFlusso") String idFlusso){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.flussiRendicontazioneIdFlussoGET(this.getUser(), uriInfo, httpHeaders,  idFlusso);
    }

    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response flussiRendicontazioneGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("idDominio") String idDominio){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.flussiRendicontazioneGET(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, idDominio);
    }

}


