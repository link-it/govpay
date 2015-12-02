/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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
package it.govpay.web.rs.gpv1;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.PortaliBD;
import it.govpay.bd.model.Portale;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.pagamento.RptBD;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

@Path("/pub")
public class GestioneRedirectGw {

	Logger log = LogManager.getLogger();

	@Context HttpServletRequest httpServletRequest;

	@GET
	@Path("/backUrl")
	public Response backUrl(
			@QueryParam(value = "codPortale") String codPortale, 
			@QueryParam(value = "idDominio") @DefaultValue(value="") String idDominio, 
			@QueryParam(value = "idSession") String idSession,
			@QueryParam(value = "esito") @DefaultValue(value="") String esito) {
		BasicBD bd = null;
		Rpt rpt = null;
		
		try {
			try {
				bd = BasicBD.getInstance();
				RptBD rptBD = new RptBD(bd);
				rpt = rptBD.getRptByCodSessione(idSession);
				URI uri = new URI(rpt.getCallbackURL().replace("{idSession}", idSession).replace("{esito}", esito).replace("{idDominio}", idDominio));
				return Response.seeOther(uri).build();
			} catch (ServiceException e) {
			} catch (NotFoundException e) {
			} catch (URISyntaxException e) {
				log.error("La CallbackURL configurata [idRpt: " + rpt.getId() + "] non e' valida.");
			} 
	
			try {
				if(codPortale != null) {
					URI uri = new URI(AnagraficaManager.getPortale(bd, codPortale).getDefaultCallbackURL().replace("{idSession}", idSession).replace("{esito}", esito).replace("{idDominio}", idDominio));
					return Response.seeOther(uri).build();
				} else {
					PortaliBD portaliBD = new PortaliBD(bd);
					List<Portale> portali = portaliBD.getPortali();
					if(portali.size() == 1) {
						URI uri = new URI(portali.get(0).getDefaultCallbackURL().replace("{idSession}", idSession).replace("{esito}", esito).replace("{idDominio}", idDominio));
						return Response.seeOther(uri).build();
					}
				}
			} catch (ServiceException e) {
			} catch (URISyntaxException e) {
				log.error("La DefaultCallbackURL configurata [CodPortale: " + codPortale + "] non e' valida.");
			} catch (NotFoundException e) {
			} catch (MultipleResultException e) {
			} 
	
			return Response.status(Response.Status.NOT_FOUND).build();
		} finally {
			if(bd!= null) bd.closeConnection();
		}
	}
}

