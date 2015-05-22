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

import it.govpay.ejb.controller.AnagraficaEJB;
import it.govpay.ndp.ejb.AnagraficaDominioEJB;
import it.govpay.ndp.ejb.RendicontazioneEJB;
import it.govpay.ndp.model.RendicontazioneModel;
import it.govpay.rs.ListaRendicontazioni;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.apache.logging.log4j.ThreadContext;

@Path("/rendicontazionì")
public class GestioneRendicontazioniRsService {

	@Inject
	RendicontazioneEJB rendicontazioneEJB;
	
	@Inject
	AnagraficaEJB anagraficaEJB;
	
	@Inject
	AnagraficaDominioEJB anagraficaDominioEJB;
	
	@GET
	@Path("/listaRendicontazioni")
	public ListaRendicontazioni getListaRendicontazioni(@QueryParam(value = "identificativoBeneficiario") String identificativoBeneficiario) {
		ThreadContext.put("proc", "ListaRendicontazioni");
		
		return new ListaRendicontazioni();
	}
	
	@GET
	@Path("/rendicontazione")
	public RendicontazioneModel getRendicontazioni(
			@QueryParam(value = "identificativoBeneficiario") String identificativoBeneficiario,
			@QueryParam(value = "idGatewayPagamento") String idGatewayPagamento,
			@QueryParam(value = "idFlusso") String idFlusso) {
		ThreadContext.put("proc", "Rendicontazione");
		
		return rendicontazioneEJB.getRendicontazione(identificativoBeneficiario, idGatewayPagamento, idFlusso);
	}
}

