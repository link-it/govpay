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

import it.govpay.backoffice.v1.controllers.QuadratureController;
import it.govpay.core.beans.Costanti;
import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/quadrature")

public class Quadrature extends BaseRsServiceV1{


	private QuadratureController controller = null;

	public Quadrature() throws ServiceException{
		super("quadrature");
		this.controller = new QuadratureController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/riscossioni")
    
    @Produces({ "application/json" })
    public Response findQuadratureRiscossioni(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("dataDa") String dataDa, @QueryParam("dataA") String dataA, @QueryParam("idDominio") List<String> idDominio, @QueryParam("idUnita") List<String> idUnita, @QueryParam("idTipoPendenza") List<String> idTipoPendenza, @QueryParam("idA2A") List<String> idA2A, @QueryParam("direzione") List<String> direzione, @QueryParam("divisione") List<String> divisione, @QueryParam("tassonomia") List<String> tassonomia, @QueryParam("tipo") List<String> tipo, @QueryParam("gruppi") List<String> gruppi){
    	 this.buildContext();
        return this.controller.getQuadratureRiscossioni(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, dataDa, dataA, idDominio, idUnita, idTipoPendenza, idA2A, direzione, divisione, tassonomia, tipo, gruppi);
    }

    @GET
    @Path("/rendicontazioni")
    
    @Produces({ "application/json" })
    public Response findQuadratureRendicontazioni(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam("gruppi") List<String> gruppi, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("flussoRendicontazione.dataFlussoDa") String flussoRendicontazioneDataFlussoDa, @QueryParam("flussoRendicontazione.dataFlussoA") String flussoRendicontazioneDataFlussoA, @QueryParam("dataDa") String dataDa, @QueryParam("dataA") String dataA, @QueryParam("idFlusso") String idFlusso, @QueryParam("iuv") String iuv, @QueryParam("direzione") List<String> direzione, @QueryParam("divisione") List<String> divisione){
    	 this.buildContext();
        return this.controller.getQuadratureRendicontazioni(this.getUser(), uriInfo, httpHeaders, gruppi, pagina, risultatiPerPagina, flussoRendicontazioneDataFlussoDa, flussoRendicontazioneDataFlussoA, dataDa, dataA, idFlusso, iuv, direzione, divisione);
    }

}


