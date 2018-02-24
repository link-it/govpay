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

import it.govpay.rs.v1.BaseRsServiceV1;
import it.govpay.rs.v1.controllers.base.PspController;
import it.govpay.rs.v1.costanti.Costanti;



@Path("/psp")

public class Psp extends BaseRsServiceV1{


	private PspController controller = null;

	public Psp() {
		super("psp");
		this.controller = new PspController(this.nomeServizio,this.log);
	}

    @GET
    @Path("/")
    @Produces({ "application/json" })
    public Response pspGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, 
    		@QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") int pagina,
			@QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") int risultatiPerPagina,
			@QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi,
			@QueryParam("abilitato") Boolean abilitato, @QueryParam("bollo") Boolean bollo, @QueryParam("storno") Boolean storno){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.pspGET(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, abilitato, bollo, storno);
    }

    /*
    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response pspGET_1(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam("pagina") Integer pagina, @QueryParam("risultatiPerPagina") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato, @QueryParam("bollo") Boolean bollo, @QueryParam("storno") Boolean storno){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.pspGET_1(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, abilitato, bollo, storno);
    }

 */
    @GET
    @Path("/{idPsp}/canali")
    
    @Produces({ "application/json" })
    public Response pspIdPspCanaliGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam("pagina") Integer pagina, @QueryParam("risultatiPerPagina") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato, @QueryParam("modello") String modello, @QueryParam("abilitato2") String abilitato2){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.pspIdPspCanaliGET(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, abilitato, modello, abilitato2);
    }

    /*

    @GET
    @Path("/{idPsp}/canali")
    
    @Produces({ "application/json" })
    public Response pspIdPspCanaliGET_2(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam("pagina") Integer pagina, @QueryParam("risultatiPerPagina") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato, @QueryParam("modello") ModelloPagamento modello, @QueryParam("abilitato2") TipoVersamento abilitato2){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.pspIdPspCanaliGET_2(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, abilitato, modello, abilitato2);
    }


    @GET
    @Path("/{idPsp}/canali/{idCanale}/{tipoVersamento}")
    
    @Produces({ "application/json" })
    public Response pspIdPspCanaliIdCanaleTipoVersamentoGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idPsp") String idPsp, @PathParam("idCanale") String idCanale, @PathParam("tipoVersamento") TipoVersamento tipoVersamento){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.pspIdPspCanaliIdCanaleTipoVersamentoGET(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders,  idPsp,  idCanale,  tipoVersamento);
    }


    @GET
    @Path("/{idPsp}/canali/{idCanale}/{tipoVersamento}")
    
    @Produces({ "application/json" })
    public Response pspIdPspCanaliIdCanaleTipoVersamentoGET_3(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idPsp") String idPsp, @PathParam("idCanale") String idCanale, @PathParam("tipoVersamento") TipoVersamento tipoVersamento){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.pspIdPspCanaliIdCanaleTipoVersamentoGET_3(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders,  idPsp,  idCanale,  tipoVersamento);
    }

*/
  
    @GET
    @Path("/{idPsp}")
    @Produces({ "application/json" })
    public Response pspIdPspGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idPsp") String idPsp){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.pspIdPspGET(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders,  idPsp);
    }
/*
    @GET
    @Path("/{idPsp}")
    
    @Produces({ "application/json" })
    public Response pspIdPspGET_4(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idPsp") String idPsp){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.pspIdPspGET_4(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders,  idPsp);
    }
*/

}


