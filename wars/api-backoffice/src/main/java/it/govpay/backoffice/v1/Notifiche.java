/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import it.govpay.backoffice.v1.controllers.NotificheController;
import it.govpay.core.beans.Costanti;
import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/notifiche")

public class Notifiche extends BaseRsServiceV1{


	private NotificheController controller = null;

	public Notifiche()  {
		super("notifiche");
		this.controller = new NotificheController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/")

    @Produces({ "application/json" })
    public Response findNotifiche(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina,
    		@QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("dataDa") String dataDa, @QueryParam("dataA") String dataA,
    		@QueryParam("stato") String stato, @QueryParam("tipo") String tipo, @QueryParam("metadatiPaginazione") @DefaultValue(value="true") Boolean metadatiPaginazione, @QueryParam("maxRisultati") @DefaultValue(value="true") Boolean maxRisultati){
    	 this.buildContext();
        return this.controller.findNotifiche(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, dataDa, dataA, stato, tipo, metadatiPaginazione, maxRisultati);
    }

}


