package it.govpay.backoffice.v1;


import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.backoffice.v1.controllers.RendicontazioniController;
import it.govpay.core.beans.Costanti;
import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/rendicontazioni")

public class Rendicontazioni extends BaseRsServiceV1{


	private RendicontazioniController controller = null;

	public Rendicontazioni() throws ServiceException {
		super("rendicontazioni");
		this.controller = new RendicontazioniController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response findRendicontazioni(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("dataOraFlussoDa") String dataOraFlussoDa, @QueryParam("dataOraFlussoA") String dataOraFlussoA, @QueryParam("dataRendicontazioneDa") String dataRendicontazioneDa, @QueryParam("dataRendicontazioneA") String dataRendicontazioneA, @QueryParam("idFlusso") String idFlusso, @QueryParam("iuv") String iuv, @QueryParam("direzione") List<String> direzione, @QueryParam("divisione") List<String> divisione){
    	this.controller.setContext(this.getContext());
        return this.controller.findRendicontazioni(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, dataOraFlussoDa, dataOraFlussoA, dataRendicontazioneDa, dataRendicontazioneA, idFlusso, iuv, direzione, divisione);
    }

}


