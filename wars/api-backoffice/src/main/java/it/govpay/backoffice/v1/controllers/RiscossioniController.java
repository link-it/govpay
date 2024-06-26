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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;

import org.apache.commons.lang3.ArrayUtils;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.ListaRiscossioni;
import it.govpay.backoffice.v1.beans.Riscossione;
import it.govpay.backoffice.v1.beans.StatoRiscossione;
import it.govpay.backoffice.v1.beans.TipoRiscossione;
import it.govpay.backoffice.v1.beans.converter.RiscossioniConverter;
import it.govpay.bd.model.Incasso;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.beans.Costanti;
import it.govpay.core.dao.pagamenti.RiscossioniDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiRiscossioneDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRiscossioneDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaRiscossioniDTO;
import it.govpay.core.dao.pagamenti.dto.ListaRiscossioniDTOResponse;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.core.utils.validator.ValidatoreUtils;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Pagamento.Stato;
import it.govpay.model.Pagamento.TipoPagamento;
import it.govpay.model.Utenza.TIPO_UTENZA;



public class RiscossioniController extends BaseController {

     public RiscossioniController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response getRiscossione(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String iuv, String iur, Integer indice) {
    	String methodName = "getRiscossione";
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName));
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.RENDICONTAZIONI_E_INCASSI), Arrays.asList(Diritti.LETTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);

			// Parametri - > DTO Input

			LeggiRiscossioneDTO getRiscossioneDTO = new LeggiRiscossioneDTO(user, idDominio, iuv, iur, indice);

			// INIT DAO

			RiscossioniDAO applicazioniDAO = new RiscossioniDAO();

			// CHIAMATA AL DAO

			LeggiRiscossioneDTOResponse getRiscossioneDTOResponse = applicazioniDAO.leggiRiscossione(getRiscossioneDTO);

			// controllo che il dominio sia autorizzato
			if(!AuthorizationManager.isDominioAuthorized(user, getRiscossioneDTOResponse.getDominio().getCodDominio())) {
				throw AuthorizationManager.toNotAuthorizedException(user,getRiscossioneDTOResponse.getDominio().getCodDominio(), null);
			}
			// CONVERT TO JSON DELLA RISPOSTA

			SingoloVersamento singoloVersamento = getRiscossioneDTOResponse.getPagamento().getSingoloVersamento(null);
			Versamento versamento = singoloVersamento.getVersamentoBD(null);
			Rpt rpt = getRiscossioneDTOResponse.getPagamento().getRpt(null);
			Incasso incasso = getRiscossioneDTOResponse.getPagamento().getIncasso(null);
			Riscossione response = RiscossioniConverter.toRsModel(getRiscossioneDTOResponse.getPagamento(), singoloVersamento, versamento, rpt, incasso);

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.logContext(ContextThreadLocal.get());
		}
    }



    public Response findRiscossioni(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, String idDominio, String idA2A, String idPendenza, String idUnita, String idTipoPendenza, String stato, String dataDa, String dataA, List<String> tipo, String iuv, List<String> direzione, List<String> divisione, List<String> tassonomia, Boolean metadatiPaginazione, Boolean maxRisultati, String iur) {
    	String methodName = "findRiscossioni";
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName));
		this.setMaxRisultati(maxRisultati);
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.RENDICONTAZIONI_E_INCASSI), Arrays.asList(Diritti.LETTURA));

			ValidatorFactory vf = ValidatorFactory.newInstance();
			ValidatoreUtils.validaRisultatiPerPagina(vf, Costanti.PARAMETRO_RISULTATI_PER_PAGINA, risultatiPerPagina);

			// Parametri - > DTO Input

			ListaRiscossioniDTO findRiscossioniDTO = new ListaRiscossioniDTO(user);
			findRiscossioniDTO.setIdDominio(idDominio);
			findRiscossioniDTO.setLimit(risultatiPerPagina);
			findRiscossioniDTO.setPagina(pagina);
			findRiscossioniDTO.setEseguiCount(metadatiPaginazione);
			findRiscossioniDTO.setEseguiCountConLimit(maxRisultati);

			if(dataDa != null) {
				Date dataDaDate = SimpleDateFormatUtils.getDataDaConTimestamp(dataDa, "dataDa");
				findRiscossioniDTO.setDataRiscossioneDa(dataDaDate);
			}
			if(dataA != null) {
				Date dataADate =  SimpleDateFormatUtils.getDataAConTimestamp(dataA, "dataA");
				findRiscossioniDTO.setDataRiscossioneA(dataADate);
			}

			findRiscossioniDTO.setIdA2A(idA2A);
			findRiscossioniDTO.setIdPendenza(idPendenza);
			findRiscossioniDTO.setOrderBy(ordinamento);
			if(stato != null) {
				StatoRiscossione statoRisc = StatoRiscossione.fromValue(stato);
				if(statoRisc != null) {
					switch(statoRisc) {
					case INCASSATA: findRiscossioniDTO.setStato(Stato.INCASSATO);
						break;
					case RISCOSSA: findRiscossioniDTO.setStato(Stato.PAGATO);
						break;
					default:
						break;
					}
				} else {
					throw new ValidationException("Codifica inesistente per stato. Valore fornito [" + stato
							+ "] valori possibili " + ArrayUtils.toString(StatoRiscossione.values()));
				}
			}

			List<TipoPagamento> tipoEnum = new ArrayList<>();
			if(tipo == null || tipo.isEmpty()) { // valori di default
				tipoEnum.add(TipoPagamento.ENTRATA);
				tipoEnum.add(TipoPagamento.MBT);
			}

			if(tipo!=null) {
				for (String tipoS : tipo) {
					TipoRiscossione tipoRiscossione = TipoRiscossione.fromValue(tipoS);
					if(tipoRiscossione != null) {
						tipoEnum.add(TipoPagamento.valueOf(tipoRiscossione.toString()));
					} else {
						throw new ValidationException("Codifica inesistente per tipo. Valore fornito [" + tipo + "] valori possibili " + ArrayUtils.toString(TipoRiscossione.values()));
					}
				}
			}

			findRiscossioniDTO.setTipo(tipoEnum);
			findRiscossioniDTO.setIuv(iuv);
			findRiscossioniDTO.setIdUnita(idUnita);
			findRiscossioniDTO.setIdTipoPendenza(idTipoPendenza);
			findRiscossioniDTO.setDirezione(direzione);
			findRiscossioniDTO.setDivisione(divisione);
			findRiscossioniDTO.setTassonomia(tassonomia);
			findRiscossioniDTO.setIur(iur);

			// Autorizzazione sui domini
			List<String> domini = AuthorizationManager.getDominiAutorizzati(user);
//			if(domini == null) {
//				throw AuthorizationManager.toNotAuthorizedExceptionNessunDominioAutorizzato(user);
//			}
			findRiscossioniDTO.setCodDomini(domini);

			// INIT DAO

			RiscossioniDAO rendicontazioniDAO = new RiscossioniDAO();

			// CHIAMATA AL DAO

			ListaRiscossioniDTOResponse findRiscossioniDTOResponse = domini != null ? rendicontazioniDAO.listaRiscossioni(findRiscossioniDTO) : new ListaRiscossioniDTOResponse(0L, new ArrayList<>());

			// CONVERT TO JSON DELLA RISPOSTA

			ListaRiscossioni response = new ListaRiscossioni(findRiscossioniDTOResponse.getResults().stream().map(t -> RiscossioniConverter.toRsModelIndex(t)).collect(Collectors.toList()),
					 this.getServicePath(uriInfo), findRiscossioniDTOResponse.getTotalResults(), pagina, risultatiPerPagina, this.maxRisultatiBigDecimal);

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();

		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.logContext(ContextThreadLocal.get());
		}
    }


}


