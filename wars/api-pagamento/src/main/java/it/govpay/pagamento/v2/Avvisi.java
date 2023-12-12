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
package it.govpay.pagamento.v2;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import it.govpay.pagamento.v2.controller.AvvisiController;
import it.govpay.rs.v2.BaseRsServiceV2;


@Path("/avvisi")

public class Avvisi extends BaseRsServiceV2{


	private AvvisiController controller = null;

	public Avvisi() {
		super("avvisi");
		this.controller = new AvvisiController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/{idDominio}/{numeroAvviso}")

    @Produces({ "application/json", "application/pdf" })
    public Response getAvviso(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("numeroAvviso") String numeroAvviso, @QueryParam("idDebitore") String idDebitore, @QueryParam("UUID") String UUID, @QueryParam("gRecaptchaResponse") String gRecaptchaResponse, @QueryParam("linguaSecondaria") String linguaSecondaria){
        this.controller.setRequestResponse(this.request, this.response);
        this.buildContext();
        return this.controller.avvisiIdDominioIuvGET(this.getUser(), uriInfo, httpHeaders,  idDominio,  numeroAvviso, idDebitore, UUID, gRecaptchaResponse, linguaSecondaria);
    }

}


