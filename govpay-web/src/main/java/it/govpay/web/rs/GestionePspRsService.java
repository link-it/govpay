/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
package it.govpay.web.rs;

import it.govpay.web.controller.GatewayController;
import it.govpay.rs.ListaPsp;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.apache.logging.log4j.ThreadContext;

@Path("/psp")
public class GestionePspRsService {

	@Inject
	GatewayController pspCtrl;
	
	@GET
	@Path("/listaPsp")
	public ListaPsp listaPSP(@QueryParam(value = "identificativoBeneficiario") String identificativoBeneficiario) {
		ThreadContext.put("proc", "ListaPSP");
		ListaPsp listaPsp = new ListaPsp();
		listaPsp.getPsps().addAll(pspCtrl.listaPSP());
		return listaPsp;
	}
}

