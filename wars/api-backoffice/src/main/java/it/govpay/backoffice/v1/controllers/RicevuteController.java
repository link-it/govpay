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
package it.govpay.backoffice.v1.controllers;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Arrays;

import javax.mail.BodyPart;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.openspcoop2.utils.mime.MimeMultipart;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.PendenzaIndex;
import it.govpay.backoffice.v1.beans.Rpp;
import it.govpay.backoffice.v1.beans.converter.PendenzeConverter;
import it.govpay.backoffice.v1.beans.converter.RptConverter;
import it.govpay.bd.model.Operatore;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.dao.pagamenti.RptDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiRptDTOResponse;
import it.govpay.core.dao.pagamenti.dto.PostRicevutaDTO;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;

public class RicevuteController extends BaseController {

	public RicevuteController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
	}

	public Response addRicevuta(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , java.io.InputStream is) {
		String methodName = "addRicevuta";
		String transactionId = ContextThreadLocal.get().getTransactionId();

		this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName);

		try(ByteArrayOutputStream baos= new ByteArrayOutputStream();){

			String contentTypeBody = null;
			if(httpHeaders.getRequestHeaders().containsKey("Content-Type")) {
				contentTypeBody = httpHeaders.getRequestHeaders().get("Content-Type").get(0);
			}

			this.logDebug("Content-Type della richiesta: {}.", contentTypeBody);

			String fileName = null;
			InputStream fileInputStream = null;
			try{
				// controllo se sono in una richiesta multipart
				if(contentTypeBody != null && contentTypeBody.startsWith("multipart")) {
					javax.mail.internet.ContentType cType = new javax.mail.internet.ContentType(contentTypeBody);
					this.logDebug(MessageFormat.format("Content-Type Boundary: [{0}]", cType.getParameter("boundary")));

					MimeMultipart mimeMultipart = new MimeMultipart(is,contentTypeBody);

					for(int i = 0 ; i < mimeMultipart.countBodyParts() ;  i ++) {
						BodyPart bodyPart = mimeMultipart.getBodyPart(i);
						fileName = PendenzeController.getBodyPartFileName(bodyPart);

						if(fileName != null) {
							fileInputStream = bodyPart.getInputStream();
							break;
						}
					}

					if(fileInputStream != null) {
						IOUtils.copy(fileInputStream, baos);
					}
				}
			}catch(Exception e) {
				this.log.error(e.getMessage(),e);
			}

			if(httpHeaders.getRequestHeaders().containsKey("X-GOVPAY-FILENAME")) {
				fileName = httpHeaders.getRequestHeaders().get("X-GOVPAY-FILENAME").get(0);
			}

			if(fileInputStream == null) {
				// salvo il file ricevuto
				IOUtils.copy(is, baos);
			}

			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.PAGAMENTI), Arrays.asList(Diritti.SCRITTURA));

			RptDAO rptDAO = new RptDAO();

			PostRicevutaDTO postRicevutaDTO = new PostRicevutaDTO(user);

			postRicevutaDTO.setNomeFile(fileName);
			postRicevutaDTO.setContenuto(baos.size() > 0 ? baos.toByteArray() : null);

			// operatore opzionale
			GovpayLdapUserDetails userDetails = AutorizzazioneUtils.getAuthenticationDetails(user);
			Operatore operatore = userDetails.getOperatore();
			postRicevutaDTO.setOperatore(operatore);

			LeggiRptDTOResponse leggiRptDTOResponse = rptDAO.addRicevuta(postRicevutaDTO);

			Rpp response =  RptConverter.toRsModel(leggiRptDTOResponse.getRpt());

			PendenzaIndex pendenza = PendenzeConverter.toRsModelIndex(leggiRptDTOResponse.getVersamento());

			response.setPendenza(pendenza);

			Status responseStatus = Status.CREATED;
			this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName);
			return this.handleResponseOk(Response.status(responseStatus).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.logContext(ContextThreadLocal.get());
		}
	}
}