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
package it.govpay.pendenze.v1.controller;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import eu.medsea.mimeutil.MimeUtil;
import it.govpay.core.dao.anagrafica.DominiDAO;
import it.govpay.core.dao.anagrafica.dto.GetDominioDTO;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;

public class DominiController extends BaseController {

     public DominiController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }

    public Response dominiIdDominioLogoGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders, String idDominio) {
    	String methodName = "getLogo";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PENDENZE), Arrays.asList(Diritti.LETTURA));
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);
			
			// Parametri - > DTO Input
			
			GetDominioDTO getDominioDTO = new GetDominioDTO(user, idDominio);

			// INIT DAO
			
			DominiDAO dominiDAO = new DominiDAO(false);
			
			// CHIAMATA AL DAO
			
			byte[] logo = dominiDAO.getLogo(getDominioDTO);
			
			MimeUtil.registerMimeDetector(eu.medsea.mimeutil.detector.MagicMimeMimeDetector.class.getName());
			
			Collection<?> mimeTypes = MimeUtil.getMimeTypes(logo);
			
			String mimeType = MimeUtil.getFirstMimeType(mimeTypes.toString()).toString();
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			ResponseBuilder entity = Response.status(Status.OK).entity(logo);
			entity.header("CacheControl", "max-age: "+ GovpayConfig.getInstance().getCacheLogo().intValue());
			entity.header("Content-Type", mimeType);
			return this.handleResponseOk(entity,transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.logContext(ContextThreadLocal.get());
		}
    }
}