/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
package it.govpay.backoffice.v1;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import it.govpay.backoffice.v1.controllers.EnumerazioniController;
import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/enumerazioni")

public class Enumerazioni extends BaseRsServiceV1{


	private EnumerazioniController controller = null;

	public Enumerazioni() {
		super("enumerazioni");
		this.controller = new EnumerazioniController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/componentiEvento")

    @Produces({ "application/json" })
    public Response findEnumerazioniComponentiEvento(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders){
        this.buildContext();
        return this.controller.findEnumerazioniComponentiEvento(this.getUser(), uriInfo, httpHeaders);
    }

    @GET
    @Path("/serviziACL")

    @Produces({ "application/json" })
    public Response findEnumerazioniServiziACL(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders){
        this.buildContext();
        return this.controller.findEnumerazioniServiziACL(this.getUser(), uriInfo, httpHeaders);
    }

    @GET
    @Path("/versioneConnettore")

    @Produces({ "application/json" })
    public Response findEnumerazioniVersioneConnettore(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders){
        this.buildContext();
        return this.controller.findEnumerazioniVersioneConnettore(this.getUser(), uriInfo, httpHeaders);
    }

    @GET
    @Path("/labelTipiEvento")

    @Produces({ "application/json" })
    public Response findEnumerazioniLabelTipiEvento(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders){
        this.buildContext();
        return this.controller.findEnumerazioniLabelTipiEvento(this.getUser(), uriInfo, httpHeaders);
    }

}


