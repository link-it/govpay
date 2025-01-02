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
package it.govpay.wc;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import it.govpay.rs.v1.BaseRsServiceV1;
import it.govpay.wc.controller.WcController;

@Path("/")
public class Wc extends BaseRsServiceV1 {
	
	private WcController controller = null;
	
	public Wc() {
		super("psp");
		this.controller = new WcController(this.nomeServizio,this.log);
	}

	@GET
	@Path("/psp")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getPsp(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam("idSession") String idSession, @QueryParam("esito") String esito) {
		this.controller.setContext(this.getContext());
        return this.controller.getPsp(null, uriInfo, httpHeaders, idSession, esito);
	}
}
