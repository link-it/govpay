package it.govpay.pagamento.v2;

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
import it.govpay.pagamento.v2.controller.PendenzeController;
import it.govpay.rs.v2.BaseRsServiceV2;


@Path("/pendenze")

public class Pendenze extends BaseRsServiceV2{


	private PendenzeController controller = null;

	public Pendenze() throws ServiceException {
		super("pendenze");
		this.controller = new PendenzeController(this.nomeServizio,this.log);
	}

    @GET
    @Path("/")
    @Produces({ "application/json" })
    public Response findPendenze(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi,  @QueryParam("dataDa") String dataDa, @QueryParam("dataA") String dataA, @QueryParam("idDominio") String idDominio, @QueryParam("idA2A") String idA2A, @QueryParam("idDebitore") String idDebitore, @QueryParam("stato") String stato, @QueryParam("idPagamento") String idPagamento, @QueryParam("direzione") String direzione, @QueryParam("divisione") String divisione){
        this.controller.setContext(this.getContext());
        return this.controller.pendenzeGET(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, dataDa, dataA, idDominio, idA2A, idDebitore, stato, idPagamento, direzione, divisione);
    }

    @GET
    @Path("/{idA2A}/{idPendenza}")
    @Produces({ "application/json" })
    public Response getPendenza(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idA2A") String idA2A, @PathParam("idPendenza") String idPendenza){
        this.controller.setContext(this.getContext());
        return this.controller.pendenzeIdA2AIdPendenzaGET(this.getUser(), uriInfo, httpHeaders,  idA2A,  idPendenza);
    }

}


