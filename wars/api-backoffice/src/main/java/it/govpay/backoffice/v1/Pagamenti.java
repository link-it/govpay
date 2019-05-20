package it.govpay.backoffice.v1;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.backoffice.v1.controllers.PagamentiController;
import it.govpay.core.beans.Costanti;
import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/pagamenti")

public class Pagamenti extends BaseRsServiceV1{


	private PagamentiController controller = null;

	public Pagamenti() throws ServiceException {
		super("pagamenti");
		this.controller = new PagamentiController(this.nomeServizio,this.log);
	}

    @GET
    @Path("/")
    @Produces({ "application/json" })
    public Response pagamentiGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("stato") String stato, @QueryParam("versante") String versante, @QueryParam("idSessionePortale") String idSessionePortale, @QueryParam("verificato") Boolean verificato, @QueryParam("dataDa") String dataDa, @QueryParam("dataA") String dataA){
        this.controller.setContext(this.getContext());
        return this.controller.pagamentiGET(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, stato, versante, idSessionePortale, verificato, dataDa, dataA);
    }

    @GET
    @Path("/{id}")
    @Produces({ "application/json" })
    public Response pagamentiIdGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("id") String id){
        this.controller.setContext(this.getContext());
        return this.controller.pagamentiIdGET(this.getUser(), uriInfo, httpHeaders,  id);
    }

    @PATCH
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    public Response pagamentiIdPATCH(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, java.io.InputStream is, @PathParam("id") String id){
        this.controller.setContext(this.getContext());
        return this.controller.pagamentiIdPATCH(this.getUser(), uriInfo, httpHeaders, is,  id);
    }

}


