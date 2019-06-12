package it.govpay.backoffice.v1;

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

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.backoffice.v1.controllers.EventiController;
import it.govpay.core.beans.Costanti;
import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/eventi")

public class Eventi extends BaseRsServiceV1{


	private EventiController controller = null;

	public Eventi() throws ServiceException {
		super("eventi");
		this.controller = new EventiController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/{id}")
    
    @Produces({ "application/json" })
    public Response getEvento(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("id") String id){
        this.controller.setContext(this.getContext());
        return this.controller.eventiIdGET(this.getUser(), uriInfo, httpHeaders,  id);
    }

    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response findEventi(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina,
    		@QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("idDominio") String idDominio, @QueryParam("iuv") String iuv, 
    		@QueryParam("idA2A") String idA2A, @QueryParam("idPendenza") String idPendenza, @QueryParam("idPagamento") String idPagamento, @QueryParam("esito") String esito, @QueryParam("dataDa") String dataDa, @QueryParam("dataA") String dataA,
		@QueryParam("categoriaEvento") String categoriaEvento, @QueryParam("tipoEvento") String tipoEvento, @QueryParam("componente") String componente, @QueryParam("ruolo") String ruolo, @QueryParam("messaggi") Boolean messaggi){
        this.controller.setContext(this.getContext());
        return this.controller.eventiGET(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, idDominio, iuv, idA2A, idPendenza, idPagamento, esito, dataDa, dataA, categoriaEvento, tipoEvento, componente, ruolo, messaggi);
    }

}


