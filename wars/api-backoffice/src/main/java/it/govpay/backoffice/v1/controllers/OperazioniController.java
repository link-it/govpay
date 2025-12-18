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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.ListaOperazioni;
import it.govpay.backoffice.v1.beans.converter.OperazioniConverter;
import it.govpay.core.beans.Costanti;
import it.govpay.core.dao.operazioni.OperazioniDAO;
import it.govpay.core.dao.operazioni.dto.LeggiOperazioneDTO;
import it.govpay.core.dao.operazioni.dto.LeggiOperazioneDTOResponse;
import it.govpay.core.dao.operazioni.dto.ListaOperazioniDTOResponse;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreUtils;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;

public class OperazioniController extends BaseController {

    public OperazioniController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
    }

    public Response findOperazioni(Authentication user, UriInfo uriInfo, Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi) {
    	String methodName = "findOperazioni";
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName);
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.CONFIGURAZIONE_E_MANUTENZIONE), Arrays.asList(Diritti.LETTURA));

			ValidatorFactory vf = ValidatorFactory.newInstance();
			ValidatoreUtils.validaRisultatiPerPagina(vf, Costanti.PARAMETRO_RISULTATI_PER_PAGINA, risultatiPerPagina);

			// INIT DAO
			OperazioniDAO operazioniDAO = new OperazioniDAO();

			// CHIAMATA AL DAO

			ListaOperazioniDTOResponse listaOperazioniDTOResponse = operazioniDAO.listaOperazioni();

			// CONVERT TO JSON DELLA RISPOSTA

			List<it.govpay.backoffice.v1.beans.OperazioneIndex> results = new ArrayList<>();
			for(LeggiOperazioneDTOResponse pagamentoPortale: listaOperazioniDTOResponse.getResults()) {
				results.add(OperazioniConverter.toRsModelIndex(pagamentoPortale));
			}

			ListaOperazioni response = new ListaOperazioni(results, this.getServicePath(uriInfo),
					listaOperazioniDTOResponse.getTotalResults(), pagina, risultatiPerPagina);

			this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName);
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(methodName, e, transactionId);
		} finally {
			this.logContext(ContextThreadLocal.get());
		}
    }

    public Response getOperazione(Authentication user, String id, Boolean forzaEsecuzione) {
    	String methodName = "getOperazione";
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName + ": " + id);
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.CONFIGURAZIONE_E_MANUTENZIONE), Arrays.asList(Diritti.SCRITTURA));

			LeggiOperazioneDTO leggiOperazioneDTO = new LeggiOperazioneDTO(user);
			leggiOperazioneDTO.setIdOperazione(id);
			leggiOperazioneDTO.setForzaEsecuzione(forzaEsecuzione != null && forzaEsecuzione);

			OperazioniDAO operazioniDAO = new OperazioniDAO();

			LeggiOperazioneDTOResponse leggiOperazioneDTOResponse = operazioniDAO.eseguiOperazione(leggiOperazioneDTO);

			this.logTrace(BaseController.LOG_MSG_ESECUZIONE_OPERAZIONE_COMPLETATA, methodName + ": " + id, leggiOperazioneDTOResponse.getStato(), leggiOperazioneDTOResponse.getDescrizioneStato());

			it.govpay.backoffice.v1.beans.Operazione response = OperazioniConverter.toRsModel(leggiOperazioneDTOResponse);

			this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName + ": " + id);
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(methodName, e, transactionId);
		} finally {
			this.logContext(ContextThreadLocal.get());
		}
    }

    public Response getStatoOperazione() {
    	String methodName = "getStatoOperazione";
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName);
		try{
			throw new NotImplementedException("Not implemented");
		}catch (Exception e) {
			return this.handleException(methodName, e, transactionId);
		} finally {
			this.logContext(ContextThreadLocal.get());
		}
    }

}
