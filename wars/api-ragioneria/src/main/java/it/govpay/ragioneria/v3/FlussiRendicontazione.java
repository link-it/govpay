package it.govpay.ragioneria.v3;


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

import it.govpay.core.beans.Costanti;
import it.govpay.ragioneria.v3.controller.FlussiRendicontazioneController;
import it.govpay.rs.v3.BaseRsServiceV3;



@Path("/flussiRendicontazione")

public class FlussiRendicontazione extends BaseRsServiceV3{


	private FlussiRendicontazioneController controller = null;

	public FlussiRendicontazione() throws ServiceException {
		super("flussiRendicontazione");
		this.controller = new FlussiRendicontazioneController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/{idFlusso}")
    
    @Produces({ "application/xml", "application/json" })
    public Response getFlussoRendicontazione(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idFlusso") String idFlusso){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.getFlussoRendicontazione(this.getUser(), uriInfo, httpHeaders,  idFlusso);
    }

    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response findFlussiRendicontazione(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("idDominio") String idDominio, @QueryParam("dataDa") String dataDa, @QueryParam("dataA") String dataA, @QueryParam("stato") String stato, @QueryParam("metadatiPaginazione") @DefaultValue(value="true") Boolean metadatiPaginazione, @QueryParam("maxRisultati") @DefaultValue(value="true") Boolean maxRisultati, @QueryParam("iuv") String iuv){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.findFlussiRendicontazione(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, idDominio, dataDa, dataA, stato, metadatiPaginazione, maxRisultati, iuv);
    }

    @GET
    @Path("/{idFlusso}/{dataOraFlusso}")
    
    @Produces({ "application/xml", "application/json" })
    public Response getFlussoRendicontazioneByIdEData(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idFlusso") String idFlusso, @PathParam("dataOraFlusso") String dataOraFlusso){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.getFlussoRendicontazioneByIdEData(this.getUser(), uriInfo, httpHeaders,  idFlusso,  dataOraFlusso);
    }

}


