package it.govpay.pagamento.v2;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import it.govpay.pagamento.v2.controller.PagamentiController;
import it.govpay.rs.v2.BaseRsServiceV2;


@Path("/pagamenti")

public class Pagamenti extends BaseRsServiceV2{


	private PagamentiController controller = null;

	public Pagamenti() throws ServiceException {
		super("pagamenti");
		this.controller = new PagamentiController(this.nomeServizio,this.log);
	}



    @POST
    @Path("/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    public Response addPagamento(@Context UriInfo uriInfo, 
    		@Context HttpHeaders httpHeaders, 
    		java.io.InputStream is, 
    		@QueryParam("idSessionePortale") String idSessionePortale, 
    		@QueryParam("gRecaptchaResponse") String gRecaptchaResponse, 
    		@QueryParam("codiceConvenzione") String codiceConvenzione,
    		@QueryParam("identificativoPSP") String identificativoPSP, 
    		@QueryParam("identificativoIntermediarioPSP") String identificativoIntermediarioPSP, 
    		@QueryParam("identificativoCanale") String identificativoCanale){
        this.buildContext();
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.addPagamento(this.getUser(), 
        		uriInfo, 
        		httpHeaders, 
        		is, 
        		idSessionePortale, 
        		gRecaptchaResponse, 
        		codiceConvenzione,
        		identificativoPSP,
        		identificativoIntermediarioPSP,
        		identificativoCanale);
    }
    
    @GET
    @Path("/")
    @Produces({ "application/json" })
    public Response findPagamenti(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, 
    		@QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento,
    		@QueryParam("campi") String campi, @QueryParam("dataDa") String dataDa, @QueryParam("dataA") String dataA,
    		@QueryParam("stato") String stato, @QueryParam("idVersante") String idVersante, @QueryParam("idDebitore") String idDebitore,  @QueryParam("idSessionePortale") String idSessionePortale, @QueryParam("idSessione") String idSessionePsp,
		@QueryParam("id") String id, @QueryParam("metadatiPaginazione") @DefaultValue(value="true") Boolean metadatiPaginazione, @QueryParam("maxRisultati") @DefaultValue(value="true") Boolean maxRisultati){
        this.buildContext();
        return this.controller.pagamentiGET(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, dataDa, dataA, stato, idVersante, idDebitore, idSessionePortale, idSessionePsp, id, metadatiPaginazione, maxRisultati);
    }
    
    @GET
    @Path("/byIdSession/{idSession}")
    @Produces({ "application/json" })
    public Response getPagamentoByIdSession(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idSession") String idSession){
        this.buildContext();
        return this.controller.pagamentiIdSessionGET(this.getUser(), uriInfo, httpHeaders,  idSession);
    }

    @GET
    @Path("/{id}")
    @Produces({ "application/json" })
    public Response getPagamento(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("id") String id){
        this.buildContext();
        return this.controller.pagamentiIdGET(this.getUser(), uriInfo, httpHeaders,  id);
    }



}


