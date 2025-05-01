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
import java.util.Date;
import java.util.List;

import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;

import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.ListaRendicontazioni;
import it.govpay.backoffice.v1.beans.RendicontazioneConFlussoEVocePendenza;
import it.govpay.backoffice.v1.beans.converter.RendicontazioniConverter;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.IdUnitaOperativa;
import it.govpay.bd.viste.model.Rendicontazione;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.beans.Costanti;
import it.govpay.core.dao.pagamenti.RendicontazioniDAO;
import it.govpay.core.dao.pagamenti.dto.ListaRendicontazioniDTO;
import it.govpay.core.dao.pagamenti.dto.ListaRendicontazioniDTOResponse;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreUtils;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;

public class RendicontazioniController extends BaseController {

	public RendicontazioniController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response findRendicontazioni(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi,
    		String flussoRendicontazioneDataFlussoDa, String flussoRendicontazioneDataFlussoA, String dataRendicontazioneDa, String dataRendicontazioneA,
    		String idDominio, String idFlusso, String iuv, List<String> direzione, List<String> divisione, Boolean metadatiPaginazione, Boolean maxRisultati, Boolean escludiObsoleti) {
    	String methodName = "findRendicontazioni";
    	String transactionId = ContextThreadLocal.get().getTransactionId();
		this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName);
		this.setMaxRisultati(maxRisultati);
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.RENDICONTAZIONI_E_INCASSI), Arrays.asList(Diritti.LETTURA));

			ValidatorFactory vf = ValidatorFactory.newInstance();
			ValidatoreUtils.validaRisultatiPerPagina(vf, Costanti.PARAMETRO_RISULTATI_PER_PAGINA, risultatiPerPagina);

			// Parametri - > DTO Input
			ListaRendicontazioniDTO findRendicontazioniDTO = new ListaRendicontazioniDTO(user);
			findRendicontazioniDTO.setLimit(risultatiPerPagina);
			findRendicontazioniDTO.setPagina(pagina);
			findRendicontazioniDTO.setOrderBy(ordinamento);
			findRendicontazioniDTO.setEseguiCount(metadatiPaginazione);
			findRendicontazioniDTO.setEseguiCountConLimit(maxRisultati);

			Date dataFlussoDaDate = null;
			if(flussoRendicontazioneDataFlussoDa!=null) {
				dataFlussoDaDate = SimpleDateFormatUtils.getDataDaConTimestamp(flussoRendicontazioneDataFlussoDa, Costanti.PARAM_FLUSSO_RENDICONTAZIONE_DATA_FLUSSO_DA, true);
				findRendicontazioniDTO.setDataFlussoDa(dataFlussoDaDate);
			}

			Date dataFlussoADate = null;
			if(flussoRendicontazioneDataFlussoA!=null) {
				dataFlussoADate = SimpleDateFormatUtils.getDataAConTimestamp(flussoRendicontazioneDataFlussoA, Costanti.PARAM_FLUSSO_RENDICONTAZIONE_DATA_FLUSSO_A, true);
				findRendicontazioniDTO.setDataFlussoA(dataFlussoADate);
			}

			Date dataRendicontazioneDaDate = null;
			if(dataRendicontazioneDa!=null) {
				dataRendicontazioneDaDate = SimpleDateFormatUtils.getDataDaConTimestamp(dataRendicontazioneDa, Costanti.PARAM_DATA_DA, true);
				findRendicontazioniDTO.setDataRendicontazioneDa(dataRendicontazioneDaDate);
			}

			Date dataRendicontazioneADate = null;
			if(dataRendicontazioneA!=null) {
				dataRendicontazioneADate =SimpleDateFormatUtils.getDataAConTimestamp(dataRendicontazioneA, Costanti.PARAM_DATA_A, true);
				findRendicontazioniDTO.setDataRendicontazioneA(dataRendicontazioneADate);
			}

			findRendicontazioniDTO.setIdDominio(idDominio);
			findRendicontazioniDTO.setCodFlusso(idFlusso);
			findRendicontazioniDTO.setRicercaIdFlussoCaseInsensitive(true);
			findRendicontazioniDTO.setIuv(iuv);
			findRendicontazioniDTO.setDirezione(direzione);
			findRendicontazioniDTO.setDivisione(divisione);
			if(escludiObsoleti != null && escludiObsoleti.booleanValue()) {
				findRendicontazioniDTO.setFrObsoleto(!escludiObsoleti);
			}

			// Autorizzazione sulle uo
			List<IdUnitaOperativa> uo = AuthorizationManager.getUoAutorizzate(user);
			findRendicontazioniDTO.setUnitaOperative(uo);

			RendicontazioniDAO rendicontazioniDAO = new RendicontazioniDAO();

			// CHIAMATA AL DAO

			ListaRendicontazioniDTOResponse findRendicontazioniDTOResponse =  uo != null ? rendicontazioniDAO.listaRendicontazioni(findRendicontazioniDTO)
					: new ListaRendicontazioniDTOResponse(0L, new ArrayList<>());

			// CONVERT TO JSON DELLA RISPOSTA
			List<RendicontazioneConFlussoEVocePendenza> risultati = new ArrayList<>();

			BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
			for (Rendicontazione rendicontazione : findRendicontazioniDTOResponse.getResults()) {
				risultati.add(RendicontazioniConverter.toRsModel(rendicontazione, configWrapper));
			}

			ListaRendicontazioni response = new ListaRendicontazioni(risultati,	this.getServicePath(uriInfo),
					findRendicontazioniDTOResponse.getTotalResults(), pagina, risultatiPerPagina, this.maxRisultatiBigDecimal);

			this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName);
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();

		}catch (Exception e) {
			return this.handleException(methodName, e, transactionId);
		} finally {
			this.logContext(ContextThreadLocal.get());
		}
    }


}
