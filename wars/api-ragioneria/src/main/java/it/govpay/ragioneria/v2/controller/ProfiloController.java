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
package it.govpay.ragioneria.v2.controller;

import java.text.MessageFormat;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.core.dao.anagrafica.UtentiDAO;
import it.govpay.core.dao.anagrafica.dto.LeggiProfiloDTOResponse;
import it.govpay.ragioneria.v2.beans.Profilo;
import it.govpay.ragioneria.v2.beans.converter.ProfiloConverter;

public class ProfiloController extends BaseController {

     public ProfiloController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



     public Response getProfilo(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders) {
     	String methodName = "getProfilo";
 		String transactionId = ContextThreadLocal.get().getTransactionId();
 		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName));
 		try{
 			UtentiDAO utentiDAO = new UtentiDAO();

 			LeggiProfiloDTOResponse leggiProfilo = utentiDAO.getProfilo(user);

 			Profilo profilo = ProfiloConverter.getProfilo(leggiProfilo);

 			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
 			return this.handleResponseOk(Response.status(Status.OK).entity(profilo.toJSON(null)),transactionId).build();

 		}catch (Exception e) {
 			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
 		} finally {
 			this.logContext(ContextThreadLocal.get());
 		}
     }

}


