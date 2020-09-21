package it.govpay.pendenze.v2;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
import it.govpay.pendenze.v2.controller.PendenzeController;
import it.govpay.rs.v2.BaseRsServiceV2;


@Path("/pendenze")

public class Pendenze extends BaseRsServiceV2{


	private PendenzeController controller = null;

	public Pendenze() throws ServiceException {
		super("pendenze");
		this.controller = new PendenzeController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/{idA2A}/{idPendenza}")
    
    @Produces({ "application/json" })
    public Response getPendenza(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idA2A") String idA2A, @PathParam("idPendenza") String idPendenza){
        this.buildContext();
        return this.controller.pendenzeIdA2AIdPendenzaGET(this.getUser(), uriInfo, httpHeaders,  idA2A,  idPendenza);
    }

    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response findPendenze(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi,  @QueryParam("dataDa") String dataDa, @QueryParam("dataA") String dataA, @QueryParam("idDominio") String idDominio, @QueryParam("idA2A") String idA2A, @QueryParam("idDebitore") String idDebitore, @QueryParam("stato") String stato, @QueryParam("idPagamento") String idPagamento, @QueryParam("direzione") String direzione, @QueryParam("divisione") String divisione, @QueryParam("mostraSpontaneiNonPagati") @DefaultValue(value="false") Boolean mostraSpontaneiNonPagati, @QueryParam("metadatiPaginazione") @DefaultValue(value="true") Boolean metadatiPaginazione, @QueryParam("maxRisultati") @DefaultValue(value="true") Boolean maxRisultati){
        this.buildContext();
        return this.controller.pendenzeGET(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, dataDa, dataA, idDominio, idA2A, idDebitore, stato, idPagamento, direzione, divisione, mostraSpontaneiNonPagati, metadatiPaginazione, maxRisultati);
    }

    @GET
    @Path("/byAvviso/{idDominio}/{numeroAvviso}")
    
    @Produces({ "application/json" })
    public Response getPendenzaByAvviso(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("numeroAvviso") String numeroAvviso){
    	this.buildContext();
        return this.controller.getPendenzaByAvviso(this.getUser(), uriInfo, httpHeaders,  idDominio,  numeroAvviso);
    }

    @PATCH
    @Path("/{idA2A}/{idPendenza}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    public Response updatePendenza(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idA2A") String idA2A, @PathParam("idPendenza") String idPendenza, java.io.InputStream is){
        this.buildContext();
        return this.controller.pendenzeIdA2AIdPendenzaPATCH(this.getUser(), uriInfo, httpHeaders,  idA2A,  idPendenza, is);
    }
    
    @POST
    @Path("/{idA2A}/{idPendenza}")
    @Consumes({ "application/json" })
    public Response updatePendenza(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idA2A") String idA2A, @PathParam("idPendenza") String idPendenza, java.io.InputStream is, @QueryParam("stampaAvviso") @DefaultValue(value="false") Boolean stampaAvviso, @QueryParam("data_avvisatura") String dataAvvisatura){
        this.buildContext();
        if(httpHeaders.getRequestHeader("X-HTTP-Method-Override") != null && !httpHeaders.getRequestHeader("X-HTTP-Method-Override").isEmpty() && httpHeaders.getRequestHeader("X-HTTP-Method-Override").get(0).equals("PATCH"))
        	return this.controller.pendenzeIdA2AIdPendenzaPATCH(this.getUser(), uriInfo, httpHeaders,  idA2A,  idPendenza, is);
        if(httpHeaders.getRequestHeader("X-HTTP-Method-Override") != null && !httpHeaders.getRequestHeader("X-HTTP-Method-Override").isEmpty() && httpHeaders.getRequestHeader("X-HTTP-Method-Override").get(0).equals("PUT"))
            return this.controller.pendenzeIdA2AIdPendenzaPUT(this.getUser(), uriInfo, httpHeaders,  idA2A,  idPendenza, is, stampaAvviso, dataAvvisatura);
     
        String transactionId = this.getContext().getTransactionId();
        return this.controller.handleEventoFail(Response.status(405), transactionId, null, "Operazione non consentita").build();
    }

    @PUT
    @Path("/{idA2A}/{idPendenza}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    public Response addPendenza(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idA2A") String idA2A, @PathParam("idPendenza") String idPendenza, java.io.InputStream is, @QueryParam("stampaAvviso") @DefaultValue(value="false") Boolean stampaAvviso, @QueryParam("dataAvvisatura") String dataAvvisatura){
        this.buildContext();
        return this.controller.pendenzeIdA2AIdPendenzaPUT(this.getUser(), uriInfo, httpHeaders,  idA2A,  idPendenza, is, stampaAvviso, dataAvvisatura);
    }

}


