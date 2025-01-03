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
package it.govpay.pendenze.v2.controller;


import java.text.MessageFormat;
import java.util.Arrays;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.bd.model.Allegato;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.dao.pagamenti.AllegatiDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiAllegatoDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiAllegatoDTOResponse;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;

public class AllegatiController extends BaseController {

	public AllegatiController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
	}



	public Response getAllegatoPendenza(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Long id) {
		String methodName = "getAllegatoPendenza";  
		String transactionId = ContextThreadLocal.get().getTransactionId();

		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 

		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PENDENZE), Arrays.asList(Diritti.LETTURA));

			AllegatiDAO allegatiDAO = new AllegatiDAO();

			LeggiAllegatoDTO leggiAllegatoDTO = new LeggiAllegatoDTO(user);
			leggiAllegatoDTO.setId(id);
			leggiAllegatoDTO.setIncludiRawContenuto(false);

			LeggiAllegatoDTOResponse leggiAllegatoDTOResponse = allegatiDAO.leggiAllegato(leggiAllegatoDTO);

			Allegato allegato = leggiAllegatoDTOResponse.getAllegato();

			// filtro sull'applicazione			
			if(!AutorizzazioneUtils.getAuthenticationDetails(user).getApplicazione().getCodApplicazione().equals(leggiAllegatoDTOResponse.getApplicazione().getCodApplicazione())) {
				throw AuthorizationManager.toNotAuthorizedException(user);
			}

			String allegatoFileName = allegato.getNome();
			String mediaType = allegato.getTipo() != null? allegato.getTipo() : MediaType.APPLICATION_OCTET_STREAM;

			StreamingOutput contenutoStream = allegatiDAO.leggiBlobContenuto(allegato.getId());

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).type(mediaType).entity(contenutoStream).header("content-disposition", "attachment; filename=\""+allegatoFileName+"\""),transactionId).build();

		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.logContext(ContextThreadLocal.get());
		}
	}


}


