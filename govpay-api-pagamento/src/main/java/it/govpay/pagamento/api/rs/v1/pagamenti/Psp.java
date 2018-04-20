package it.govpay.pagamento.api.rs.v1.pagamenti;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.rs.v1.controllers.base.PspController;
import it.govpay.core.rs.v1.costanti.Costanti;
import it.govpay.rs.v1.BaseRsServiceV1;


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


