/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2023 Link.it srl (http://www.link.it).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.pagamento.v1;

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

import it.govpay.core.beans.Costanti;
import it.govpay.pagamento.v1.controller.DominiController;
import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/domini")

public class Domini extends BaseRsServiceV1{


	private DominiController controller = null;

	public Domini() {
		super("domini");
		this.controller = new DominiController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/{idDominio}/unitaOperative/{idUnitaOperativa}")

    @Produces({ "application/json" })
    public Response getUnitaOperativa(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("idUnitaOperativa") String idUnitaOperativa){
        this.buildContext();
        return this.controller.dominiIdDominioUnitaOperativeIdUnitaOperativaGET(this.getUser(), uriInfo, httpHeaders,  idDominio,  idUnitaOperativa);
    }

    @GET
    @Path("/")

    @Produces({ "application/json" })
    public Response findDomini(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato, @QueryParam("idStazione") String idStazione){
        this.buildContext();
        return this.controller.dominiGET(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, abilitato, idStazione);
    }

    @GET
    @Path("/{idDominio}/contiAccredito")

    @Produces({ "application/json" })
    public Response findContiAccredito(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato, @QueryParam("ordinamento") String ordinamento){
        this.buildContext();
        return this.controller.dominiIdDominioContiAccreditoGET(this.getUser(), uriInfo, httpHeaders,  idDominio, pagina, risultatiPerPagina, campi, abilitato, ordinamento);
    }

    @GET
    @Path("/{idDominio}")

    @Produces({ "application/json" })
    public Response getDominio(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio){
        this.buildContext();
        return this.controller.dominiIdDominioGET(this.getUser(), uriInfo, httpHeaders,  idDominio);
    }

    @GET
    @Path("/{idDominio}/entrate")

    @Produces({ "application/json" })
    public Response findEntrate(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25")Integer risultatiPerPagina, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato, @QueryParam("ordinamento") String ordinamento){
        this.buildContext();
        return this.controller.dominiIdDominioEntrateGET(this.getUser(), uriInfo, httpHeaders,  idDominio, pagina, risultatiPerPagina, ordinamento, campi, abilitato);
    }

    @GET
    @Path("/{idDominio}/unitaOperative")

    @Produces({ "application/json" })
    public Response findUnitaOperative(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato){
        this.buildContext();
        return this.controller.dominiIdDominioUnitaOperativeGET(this.getUser(), uriInfo, httpHeaders,  idDominio, pagina, risultatiPerPagina, ordinamento, campi, abilitato);
    }

    @GET
    @Path("/{idDominio}/entrate/{idEntrata}")

    @Produces({ "application/json" })
    public Response getEntrata(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("idEntrata") String idEntrata){
        this.buildContext();
        return this.controller.dominiIdDominioEntrateIdEntrataGET(this.getUser(), uriInfo, httpHeaders,  idDominio,  idEntrata);
    }

    @GET
    @Path("/{idDominio}/contiAccredito/{iban}")

    @Produces({ "application/json" })
    public Response getContiAccredito(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("iban") String iban){
        this.buildContext();
        return this.controller.dominiIdDominioContiAccreditoIbanGET(this.getUser(), uriInfo, httpHeaders,  idDominio,  iban);
    }

    @GET
    @Path("/{idDominio}/logo")

    @Produces({ "application/json" })
    public Response getLogo(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio){
        this.buildContext();
        return this.controller.dominiIdDominioLogoGET(this.getUser(), uriInfo, httpHeaders,  idDominio);
    }



}


