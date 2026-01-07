/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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
package it.govpay.user.v1;


import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import it.govpay.rs.v1.BaseRsServiceV1;
import it.govpay.user.v1.controller.LogoutController;


@Path("/logout")

public class Logout extends BaseRsServiceV1{


	private LogoutController controller = null;

	public Logout() {
		super("logout");
		this.controller = new LogoutController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/{urlID}")


    public Response logout(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("urlID") String urlID){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.logout(uriInfo, urlID);
    }

    @GET
    @Path("/")


    public Response logoutSenzaRedirect(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.logout(uriInfo, null);
    }

}


