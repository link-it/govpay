/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.core.utils.GpContext;
import it.govpay.model.Rpt;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;

@Path("/pub")
public class GestioneRedirectGw {
	
	Logger log = LogManager.getLogger(this.getClass());

	@GET
	@Path("/backUrl")
	public Response backUrl(
			@QueryParam(value = "idDominio") String codDominio, 
			@QueryParam(value = "idSession") String codSessione,
			@QueryParam(value = "esito") @DefaultValue("ERROR") String esito) {
		
		log.debug("Ricevuta richiesta di gw [Dominio:"+codDominio+" Sessione:"+codSessione+"]");
		
		BasicBD bd = null;
		Rpt rpt = null;
		GpContext gpContext = null;

		try {
			gpContext = new GpContext();
			gpContext.log("gw.redirect", codDominio, codSessione);
			bd = BasicBD.newInstance(gpContext.getTransactionId());
			RptBD rptBD = new RptBD(bd);
			rpt = rptBD.getRptByCodSessione(codDominio, codSessione);
		
			if(rpt.getCallbackURL() != null) {
				UriBuilder ub = UriBuilder.fromUri(rpt.getCallbackURL());
				if(codDominio != null) ub.queryParam("idDominio", codDominio);
				ub.queryParam("idSession", codSessione);
				ub.queryParam("esito", esito);
				gpContext.log("gw.redirectCustom", codDominio, codSessione, ub.build().toString());
				log.info("Gw custom [Dominio:"+codDominio+" Sessione:"+codSessione+"] > [Url:"+ub.build().toString()+"]");
				return Response.seeOther(ub.build()).build();
			} else {
				UriBuilder ub = UriBuilder.fromUri(AnagraficaManager.getPortale(bd, rpt.getIdPortale()).getDefaultCallbackURL());
				if(codDominio != null) ub.queryParam("idDominio", codDominio);
				ub.queryParam("idSession", codSessione);
				ub.queryParam("esito", esito);
				gpContext.log("gw.redirectDefault", codDominio, codSessione, ub.build().toString());
				log.info("Gw standard [Dominio:"+codDominio+" Sessione:"+codSessione+"] > [Url:"+ub.build().toString()+"]");
				return Response.seeOther(ub.build()).build();
			}
		} catch (NotFoundException e) {
			log.debug("Gw [Dominio:"+codDominio+" Sessione:"+codSessione+"] > Not found");
			if(gpContext != null) gpContext.log("gw.redirectNotFound", codDominio, codSessione);
			return Response.status(Response.Status.NOT_FOUND).build();
		} catch (MultipleResultException e) {
			log.error("Gw [Dominio:"+codDominio+" Sessione:"+codSessione+"] > Multiple Result");
			if(gpContext != null) gpContext.log("gw.redirectFail", codDominio, codSessione, "Individuati piu' processi di pagamento per l'identificativo sessione fornito.");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		} catch (Exception e) {
			log.error("Gw [Dominio:"+codDominio+" Sessione:"+codSessione+"] > Internal", e);
			if(gpContext != null) gpContext.log("gw.redirectFail", codDominio, codSessione, e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			if(bd!= null) bd.closeConnection();
			if(gpContext != null) gpContext.log();
		}
	}
}

