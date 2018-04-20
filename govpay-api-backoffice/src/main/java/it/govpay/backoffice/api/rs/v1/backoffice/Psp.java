package it.govpay.backoffice.api.rs.v1.backoffice;

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

import it.govpay.core.rs.v1.costanti.Costanti;
import it.govpay.rs.v1.BaseRsServiceV1;
import it.govpay.rs.v1.controllers.base.PspController;


@Path("/psp")

public class Psp extends BaseRsServiceV1{


	private PspController controller = null;

	public Psp() throws ServiceException {
		super("psp");
		this.controller = new PspController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/{idPsp}/canali")
    
    @Produces({ "application/json" })
    public Response pspIdPspCanaliGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idPsp") String idPsp, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato, @QueryParam("modello") String modello, @QueryParam("tipoVersamento") String tipoVersamento){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.pspIdPspCanaliGET(this.getUser(), uriInfo, httpHeaders,  idPsp, pagina, risultatiPerPagina, ordinamento, campi, abilitato, modello, tipoVersamento);
    }

    @GET
    @Path("/{idPsp}/canali/{idCanale}/{tipoVersamento}")
    
    @Produces({ "application/json" })
    public Response pspIdPspCanaliIdCanaleTipoVersamentoGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idPsp") String idPsp, @PathParam("idCanale") String idCanale, @PathParam("tipoVersamento") String tipoVersamento){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.pspIdPspCanaliIdCanaleTipoVersamentoGET(this.getUser(), uriInfo, httpHeaders,  idPsp,  idCanale,  tipoVersamento);
    }

    @GET
    @Path("/{idPsp}")
    
    @Produces({ "application/json" })
    public Response pspIdPspGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idPsp") String idPsp){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.pspIdPspGET(this.getUser(), uriInfo, httpHeaders,  idPsp);
    }

    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response pspGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato, @QueryParam("bollo") Boolean bollo, @QueryParam("storno") Boolean storno){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.pspGET(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, abilitato, bollo, storno);
    }

}


