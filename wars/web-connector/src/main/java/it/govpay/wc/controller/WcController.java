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
package it.govpay.wc.controller;

import java.net.URI;

import org.slf4j.Logger;

import it.govpay.core.beans.Costanti;
import it.govpay.core.dao.pagamenti.WebControllerDAO;
import it.govpay.core.dao.pagamenti.dto.RedirectDaPspDTO;
import it.govpay.core.dao.pagamenti.dto.RedirectDaPspDTOResponse;
import it.govpay.core.utils.validator.CostantiValidazione;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import jakarta.ws.rs.core.Response;

public class WcController  extends BaseController {

	public WcController(String nomeServizio, Logger log) {
		super(nomeServizio, log);
	}

	public Response getPsp(String idSession, String esito) {
		String methodName = "getPsp";  
		String transactionId = this.context.getTransactionId();
		this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName); 
		try{
			ValidatoreIdentificativi validatoreIdentificativi = ValidatoreIdentificativi.newInstance();
			validatoreIdentificativi.validaParametroObbligatorio(Costanti.PARAM_ID_SESSION, idSession, CostantiValidazione.PATTERN_GENERICO, 1, 512);
			validatoreIdentificativi.validaParametroObbligatorio(Costanti.PARAM_ESITO, esito, CostantiValidazione.PATTERN_GENERICO, 1, 512);
			
			RedirectDaPspDTO redirectDaPspDTO = new RedirectDaPspDTO();
			redirectDaPspDTO.setEsito(esito);
			redirectDaPspDTO.setIdSession(idSession);
			WebControllerDAO webControllerDAO = new WebControllerDAO();
			
			RedirectDaPspDTOResponse redirectDaPspDTOResponse = webControllerDAO.gestisciRedirectPsp(redirectDaPspDTO);
			
			this.logInfo("Esecuzione " + methodName + " completata con redirect verso la URL ["+ redirectDaPspDTOResponse.getLocation() +"].");	
			return this.handleResponseOk(Response.seeOther(new URI(redirectDaPspDTOResponse.getLocation())),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(methodName, e, transactionId);
		} finally {
			this.logContext(this.context);
		}
	}
}
