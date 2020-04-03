package it.govpay.backoffice.v1;

import java.util.List;

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

import it.govpay.backoffice.v1.controllers.RiscossioniController;
import it.govpay.core.beans.Costanti;
import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/riscossioni")

public class Riscossioni extends BaseRsServiceV1{


	private RiscossioniController controller = null;

	public Riscossioni() throws ServiceException {
		super("riscossioni");
		this.controller = new RiscossioniController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/{idDominio}/{iuv}/{iur}/{indice}")
    
    @Produces({ "application/json" })
    public Response getRiscossione(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("iuv") String iuv, @PathParam("iur") String iur, @PathParam("indice") Integer indice){
        this.buildContext();
        return this.controller.getRiscossione(this.getUser(), uriInfo, httpHeaders,  idDominio,  iuv,  iur,  indice);
    }

    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response findRiscossioni(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("idDominio") String idDominio, @QueryParam("idA2A") String idA2A, @QueryParam("idPendenza") String idPendenza, @QueryParam("idUnita") String idUnita, @QueryParam("idTipoPendenza") String idTipoPendenza, @QueryParam("stato") String stato, @QueryParam("dataDa") String dataDa, @QueryParam("dataA") String dataA, @QueryParam("tipo") String tipo, @QueryParam("iuv") String iuv, @QueryParam("direzione") List<String> direzione, @QueryParam("divisione") List<String> divisione, @QueryParam("tassonomia") List<String> tassonomia){
        this.buildContext();
        return this.controller.findRiscossioni(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, idDominio, idA2A, idPendenza, idUnita, idTipoPendenza, stato, dataDa, dataA, tipo, iuv, direzione, divisione, tassonomia);
    }

}


