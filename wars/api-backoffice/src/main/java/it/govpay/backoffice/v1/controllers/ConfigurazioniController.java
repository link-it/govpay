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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.Configurazione;
import it.govpay.backoffice.v1.beans.PatchOp;
import it.govpay.backoffice.v1.beans.PatchOp.OpEnum;
import it.govpay.backoffice.v1.beans.converter.ConfigurazioniConverter;
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.dao.configurazione.ConfigurazioneDAO;
import it.govpay.core.dao.configurazione.dto.LeggiConfigurazioneDTO;
import it.govpay.core.dao.configurazione.dto.LeggiConfigurazioneDTOResponse;
import it.govpay.core.dao.configurazione.dto.PatchConfigurazioneDTO;
import it.govpay.core.dao.configurazione.dto.PutConfigurazioneDTO;
import it.govpay.core.dao.configurazione.dto.PutConfigurazioneDTOResponse;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;


public class ConfigurazioniController extends BaseController {

     public ConfigurazioniController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response getConfigurazioni(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders ) {
    	String methodName = "getConfigurazioni";
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName);
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.CONFIGURAZIONE_E_MANUTENZIONE), Arrays.asList(Diritti.LETTURA));

			LeggiConfigurazioneDTO leggiConfigurazioneDTO = new LeggiConfigurazioneDTO(user);
			ConfigurazioneDAO configurazioneDAO = new ConfigurazioneDAO(false);

			LeggiConfigurazioneDTOResponse configurazioneDTOResponse = configurazioneDAO.getConfigurazione(leggiConfigurazioneDTO);

			Configurazione response = ConfigurazioniConverter.toRsModel(configurazioneDTOResponse.getConfigurazione());
			this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName);
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).header(this.transactionIdHeaderName, transactionId).build();

		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.logContext(ContextThreadLocal.get());
		}
    }



    @SuppressWarnings("unchecked")
	public Response aggiornaConfigurazioni(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , java.io.InputStream is) {
    	String methodName = "aggiornaConfigurazioni";
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName);
		try(ByteArrayOutputStream baos= new ByteArrayOutputStream();){
			// salvo il json ricevuto
			IOUtils.copy(is, baos);

			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.CONFIGURAZIONE_E_MANUTENZIONE), Arrays.asList(Diritti.SCRITTURA));

			String jsonRequest = baos.toString();

			List<PatchOp> lstOp = new ArrayList<>();

			PatchConfigurazioneDTO patchConfigurazioneDTO = new PatchConfigurazioneDTO(user);
			try {
				List<java.util.LinkedHashMap<?,?>> lst = JSONSerializable.parse(jsonRequest, List.class);
				for(java.util.LinkedHashMap<?,?> map: lst) {
					PatchOp op = new PatchOp();
					String opText = (String) map.get("op");
					OpEnum opFromValue = OpEnum.fromValue(opText);

					if(StringUtils.isNotEmpty(opText) && opFromValue == null)
						throw new ValidationException("Il campo op non e' valido.");

					op.setOp(opFromValue);
					op.setPath((String) map.get("path"));
					op.setValue(map.get("value"));
					op.validate();
					lstOp.add(op);
				}
			} catch (ValidationException e) {
				throw e;
			} catch (Exception e) {

				lstOp = JSONSerializable.parse(jsonRequest, List.class);

				if(lstOp != null && !lstOp.isEmpty()) {
					for (PatchOp patchOp : lstOp) {
						patchOp.validate();
					}
				}
			}

			if(lstOp != null)
				patchConfigurazioneDTO.setOp(ConfigurazioniConverter.toModel(lstOp));

			ConfigurazioneDAO configurazioneDAO = new ConfigurazioneDAO(false);

			LeggiConfigurazioneDTOResponse leggiConfigurazioneDTOResponse = configurazioneDAO.patchConfigurazione(patchConfigurazioneDTO);
			Configurazione response = ConfigurazioniConverter.toRsModel(leggiConfigurazioneDTOResponse.getConfigurazione());

			this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName);
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).header(this.transactionIdHeaderName, transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.logContext(ContextThreadLocal.get());
		}
    }



    public Response addConfigurazioni(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , java.io.InputStream is) {
    	String methodName = "addConfigurazioni";
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName);
		try(ByteArrayOutputStream baos= new ByteArrayOutputStream();){
			// salvo il json ricevuto
			IOUtils.copy(is, baos);

			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.CONFIGURAZIONE_E_MANUTENZIONE), Arrays.asList(Diritti.SCRITTURA));

			String jsonRequest = baos.toString();
			Configurazione giornaleRequest= JSONSerializable.parse(jsonRequest, Configurazione.class);

			giornaleRequest.validate();

			PutConfigurazioneDTO putConfigurazioneDTO = ConfigurazioniConverter.getPutConfigurazioneDTO(giornaleRequest, user);

			ConfigurazioneDAO configurazioneDAO = new ConfigurazioneDAO(false);

			PutConfigurazioneDTOResponse putConfigurazioneDTOResponse = configurazioneDAO.salvaConfigurazione(putConfigurazioneDTO);
			Status responseStatus = putConfigurazioneDTOResponse.isCreated() ?  Status.CREATED : Status.OK;

			this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName);
			return this.handleResponseOk(Response.status(responseStatus),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.logContext(ContextThreadLocal.get());
		}
    }
}
