/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
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
package it.govpay.ragioneria.api.rs.v1.ragioneria;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import it.govpay.rs.v1.BaseRsServiceV1;
import it.govpay.rs.v1.controllers.base.IncassiController;
import it.govpay.rs.v1.costanti.Costanti;

@Path("/incassi")
public class Incassi extends BaseRsServiceV1{


	private IncassiController controller = null;

	public Incassi() {
		super("incassi");
		this.controller = new IncassiController(this.nomeServizio,this.log);
	}
	
	
	@GET
	@Path("/")
	@Produces({MediaType.APPLICATION_JSON})
	public Response cercaIncassi(InputStream is, @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders,
			@QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") int pagina,
			@QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") int risultatiPerPagina) {		
		this.controller.setRequestResponse(this.request, this.response);
		return this.controller.incassiGET(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders, pagina, risultatiPerPagina);
	}
	
	@GET
	@Path("/{id}")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response leggiIncasso(InputStream is, @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders,
			@PathParam(value="id") Long id) {
		this.controller.setRequestResponse(this.request, this.response);
		return this.controller.incassiIdGET(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders, id);
	}
	
	@POST
	@Path("/")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response inserisciIncasso(InputStream is, @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders){
		this.controller.setRequestResponse(this.request, this.response);
		return this.controller.incassiPOST(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders, is);
	}

}
