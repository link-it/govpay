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
package it.govpay.ragioneria.v1.controller;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.beans.Costanti;
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.dao.pagamenti.IncassiDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiIncassoDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiIncassoDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaIncassiDTO;
import it.govpay.core.dao.pagamenti.dto.ListaIncassiDTOResponse;
import it.govpay.core.dao.pagamenti.dto.RichiestaIncassoDTO;
import it.govpay.core.dao.pagamenti.dto.RichiestaIncassoDTOResponse;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.core.utils.validator.ValidatoreUtils;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Incasso.StatoIncasso;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.ragioneria.v1.beans.Incasso;
import it.govpay.ragioneria.v1.beans.IncassoIndex;
import it.govpay.ragioneria.v1.beans.IncassoPost;
import it.govpay.ragioneria.v1.beans.ListaIncassiIndex;
import it.govpay.ragioneria.v1.beans.converter.IncassiConverter;



public class IncassiController extends BaseController {

	public IncassiController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
	}


    public Response incassiGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String dataDa, String dataA, String idDominio, Boolean metadatiPaginazione, Boolean maxRisultati) {
    	String methodName = "incassiGET";
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName);
		String campi = null;
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_RAGIONERIA), Arrays.asList(Diritti.LETTURA));

			ValidatorFactory vf = ValidatorFactory.newInstance();
			ValidatoreUtils.validaRisultatiPerPagina(vf, Costanti.PARAMETRO_RISULTATI_PER_PAGINA, risultatiPerPagina);

			ListaIncassiDTO listaIncassoDTO = new ListaIncassiDTO(user);

			listaIncassoDTO.setLimit(risultatiPerPagina);
			listaIncassoDTO.setPagina(pagina);
			listaIncassoDTO.setIdDominio(idDominio);

			if(dataDa != null) {
				Date dataDaDate = SimpleDateFormatUtils.getDataDaConTimestamp(dataDa, "dataDa");
				listaIncassoDTO.setDataDa(dataDaDate);
			}
			if(dataA != null) {
				Date dataADate = SimpleDateFormatUtils.getDataAConTimestamp(dataA, "dataA");
				listaIncassoDTO.setDataA(dataADate);
			}

			listaIncassoDTO.setStato(StatoIncasso.ACQUISITO);

			// filtro sull'applicazione
			listaIncassoDTO.setIdA2A(AutorizzazioneUtils.getAuthenticationDetails(user).getApplicazione().getCodApplicazione());

			// autorizzazione sui domini
			List<String> domini = AuthorizationManager.getDominiAutorizzati(user);
			listaIncassoDTO.setCodDomini(domini);

			listaIncassoDTO.setEseguiCount(metadatiPaginazione);
			listaIncassoDTO.setEseguiCountConLimit(maxRisultati);

			IncassiDAO incassiDAO = new IncassiDAO();
			ListaIncassiDTOResponse listaIncassiDTOResponse = domini != null ? incassiDAO.listaIncassi(listaIncassoDTO) : new ListaIncassiDTOResponse(0L, new ArrayList<>());

			// CONVERT TO JSON DELLA RISPOSTA

			List<IncassoIndex> listaIncassi = new ArrayList<>();
			for(it.govpay.bd.model.Incasso i : listaIncassiDTOResponse.getResults()) {
				listaIncassi.add(IncassiConverter.toRsIndexModel(i));
			}

			ListaIncassiIndex response = new ListaIncassiIndex(listaIncassi, this.getServicePath(uriInfo),
					listaIncassiDTOResponse.getTotalResults(), pagina, risultatiPerPagina);

			this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName);
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();

		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.logContext(ContextThreadLocal.get());
		}
    }


    public Response incassiIdDominioIdIncassoGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String idIncasso) {
    	String methodName = "incassiIdDominioIdIncassoGET";
		String transactionId = ContextThreadLocal.get().getTransactionId();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName);
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_RAGIONERIA), Arrays.asList(Diritti.LETTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);

			// Parametri - > DTO Input

			LeggiIncassoDTO leggiIncassoDTO = new LeggiIncassoDTO(user);
			leggiIncassoDTO.setIdDominio(idDominio);
			leggiIncassoDTO.setIdRiconciliazione(idIncasso);

			if(!AuthorizationManager.isDominioAuthorized(leggiIncassoDTO.getUser(), leggiIncassoDTO.getIdDominio())) {
				throw AuthorizationManager.toNotAuthorizedException(leggiIncassoDTO.getUser(), leggiIncassoDTO.getIdDominio(), null);
			}

			IncassiDAO incassiDAO = new IncassiDAO();

			// CHIAMATA AL DAO

			LeggiIncassoDTOResponse leggiIncassoDTOResponse = incassiDAO.leggiIncasso(leggiIncassoDTO);

			// filtro sull'applicazione
			if(!AutorizzazioneUtils.getAuthenticationDetails(user).getApplicazione().getCodApplicazione().equals(leggiIncassoDTOResponse.getIncasso().getApplicazione(configWrapper).getCodApplicazione())) {
				throw AuthorizationManager.toNotAuthorizedException(user);
			}

			// CONVERT TO JSON DELLA RISPOSTA

			Incasso response = IncassiConverter.toRsModel(leggiIncassoDTOResponse.getIncasso());

			this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName);
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();

		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.logContext(ContextThreadLocal.get());
		}
    }

    public Response incassiIdDominioPOST(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, java.io.InputStream is) {
    	String methodName = "incassiIdDominioPOST";
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName);
		try(ByteArrayOutputStream baos = new ByteArrayOutputStream();){
			// salvo il json ricevuto
			IOUtils.copy(is, baos);

			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_RAGIONERIA), Arrays.asList(Diritti.SCRITTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);

			IncassoPost incasso = JSONSerializable.parse(baos.toString(), IncassoPost.class);
			incasso.validate();

			RichiestaIncassoDTO richiestaIncassoDTO = IncassiConverter.toRichiestaIncassoDTO(incasso, idDominio, user);

			IncassiDAO incassiDAO = new IncassiDAO();

			RichiestaIncassoDTOResponse richiestaIncassoDTOResponse = incassiDAO.richiestaIncasso(richiestaIncassoDTO);

			Incasso incassoExt = IncassiConverter.toRsModel(richiestaIncassoDTOResponse.getIncasso());

			Status responseStatus = richiestaIncassoDTOResponse.isCreated() ?  Status.CREATED : Status.OK;

			this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName);
			return this.handleResponseOk(Response.status(responseStatus).entity(incassoExt.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.logContext(ContextThreadLocal.get());
		}
    }
}
