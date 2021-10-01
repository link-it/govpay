package it.govpay.ragioneria.v2;

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
import it.govpay.ragioneria.v2.controller.RiscossioniController;
import it.govpay.rs.v2.BaseRsServiceV2;


@Path("/riscossioni")

public class Riscossioni extends BaseRsServiceV2{


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
    public Response findRiscossioni(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("idDominio") String idDominio, @QueryParam("idA2A") String idA2A, @QueryParam("idPendenza") String idPendenza, @QueryParam("stato") String stato, @QueryParam("dataDa") String dataRiscossioneDa, @QueryParam("dataA") String dataRiscossioneA, @QueryParam("tipo") String tipo, @QueryParam("metadatiPaginazione") @DefaultValue(value="true") Boolean metadatiPaginazione, @QueryParam("maxRisultati") @DefaultValue(value="true") Boolean maxRisultati){
        this.buildContext();
        return this.controller.findRiscossioni(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, idDominio, idA2A, idPendenza, stato, dataRiscossioneDa, dataRiscossioneA, tipo, metadatiPaginazione, maxRisultati);
    }

}


