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
package it.govpay.backoffice.v1.controllers;


import java.util.List;

import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.bd.model.TracciatoNotificaPagamenti;
import it.govpay.core.beans.Costanti;
import it.govpay.core.dao.commons.exception.NonTrovataException;
import it.govpay.core.dao.pagamenti.TracciatiNotificaPagamentiDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiTracciatoNotificaPagamentiDTO;
import it.govpay.model.TracciatoNotificaPagamenti.STATO_ELABORAZIONE;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.StreamingOutput;



public class TracciatiNotificaPagamentiController extends BaseController {

     public TracciatiNotificaPagamentiController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response getTracciatoNotificaPagamenti(Authentication user, Long id, String secID) {
    	String methodName = "getTracciatoNotificaPagamenti";
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName);
		try{
			// l'accesso a questa risorsa e' libero

			LeggiTracciatoNotificaPagamentiDTO leggiTracciatoDTO = new LeggiTracciatoNotificaPagamentiDTO(user);
			leggiTracciatoDTO.setId(id);
			leggiTracciatoDTO.setIdentificativo(secID);
			leggiTracciatoDTO.setIncludiRaw(false);

			List<Long> idDomini = null;

			TracciatiNotificaPagamentiDAO tracciatiDAO = new TracciatiNotificaPagamentiDAO();
			TracciatoNotificaPagamenti tracciato = tracciatiDAO.leggiTracciato(leggiTracciatoDTO);

			if(!(tracciato.getStato().equals(STATO_ELABORAZIONE.FILE_CARICATO) || tracciato.getStato().equals(STATO_ELABORAZIONE.FILE_NUOVO)))
				throw new NonTrovataException("Il tracciato richiesto non e' disponibile: elaborazione ancora in corso");

			String zipFileName = (tracciato.getNomeFile().contains(".") ? tracciato.getNomeFile().substring(0, tracciato.getNomeFile().lastIndexOf(".")) : tracciato.getNomeFile()) + ".zip";

			StreamingOutput zipStream = tracciatiDAO.leggiBlobTracciato(id, secID, idDomini, it.govpay.orm.TracciatoNotificaPagamenti.model().RAW_CONTENUTO);

			this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName);
			return this.handleResponseOk(Response.status(Status.OK).type(MediaType.APPLICATION_OCTET_STREAM).entity(zipStream).header(Costanti.HEADER_NAME_CONTENT_DISPOSITION, Costanti.PREFIX_CONTENT_DISPOSITION_ATTACHMENT_FILENAME+zipFileName+Costanti.SUFFIX_CONTENT_DISPOSITION),transactionId).build();
		}catch (Exception e) {
			return this.handleException(methodName, e, transactionId);
		} finally {
			this.logContext(ContextThreadLocal.get());
		}
    }


}
