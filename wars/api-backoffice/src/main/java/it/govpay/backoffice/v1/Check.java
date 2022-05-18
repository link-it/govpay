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
package it.govpay.backoffice.v1;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.backoffice.v1.controllers.SondeController;
import it.govpay.rs.v1.BaseRsServiceV1;

@Path("/sonde")
public class Check extends BaseRsServiceV1{

	private SondeController controller = null;
	
	public Check() throws ServiceException {
		super("sonde");
		this.controller = new SondeController(this.nomeServizio,this.log);
	}

	@GET
	@Path("/")
	@Produces({MediaType.APPLICATION_JSON})
	public Response verificaSonde(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders) {
		 this.buildContext();
		 return this.controller.findSonde(this.getUser(), uriInfo, httpHeaders);
	}

	@GET
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response verificaSonda(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam(value = "id") String id) {
		this.buildContext();
		 return this.controller.getSonda(this.getUser(), uriInfo, httpHeaders, id);
	}

}

