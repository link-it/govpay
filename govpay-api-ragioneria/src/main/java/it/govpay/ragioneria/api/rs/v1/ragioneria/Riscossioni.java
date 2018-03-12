package it.govpay.ragioneria.api.rs.v1.ragioneria;

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

import it.govpay.rs.v1.controllers.base.RiscossioniController;
import it.govpay.core.rs.v1.costanti.Costanti;
import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/riscossioni")

public class Riscossioni extends BaseRsServiceV1{


	private RiscossioniController controller = null;

	public Riscossioni() {
		super("riscossioni");
		this.controller = new RiscossioniController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/{idDominio}/{iuv}/{iur}/{indice}")
    
    @Produces({ "application/json" })
    public Response riscossioniIdDominioIuvIurIndiceGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("iuv") String iuv, @PathParam("iur") String iur, @PathParam("indice") String indice){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.riscossioniIdDominioIuvIurIndiceGET(this.getUser(), uriInfo, httpHeaders,  idDominio,  iuv,  iur,  indice);
    }

    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response riscossioniGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("idDominio") String idDominio, @QueryParam("idA2A") String idA2A, @QueryParam("idPendenza") String idPendenza, @QueryParam("stato") String stato, @QueryParam("dataRiscossioneDa") String dataRiscossioneDa, @QueryParam("dataRiscossioneA") String dataRiscossioneA, @QueryParam("tipo") String tipo){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.riscossioniGET(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, idDominio, idA2A, idPendenza, stato, dataRiscossioneDa, dataRiscossioneA, tipo);
    }

}


