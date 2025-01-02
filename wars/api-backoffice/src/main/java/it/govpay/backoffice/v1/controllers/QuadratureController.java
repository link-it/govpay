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

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang3.ArrayUtils;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.ListaStatisticheQuadrature;
import it.govpay.backoffice.v1.beans.ListaStatisticheQuadratureRendicontazioni;
import it.govpay.backoffice.v1.beans.RaggruppamentoStatistica;
import it.govpay.backoffice.v1.beans.RaggruppamentoStatisticaRendicontazione;
import it.govpay.backoffice.v1.beans.StatisticaQuadratura;
import it.govpay.backoffice.v1.beans.StatisticaQuadraturaRendicontazione;
import it.govpay.backoffice.v1.beans.TipoRiscossione;
import it.govpay.backoffice.v1.beans.converter.StatisticaQuadraturaConverter;
import it.govpay.bd.reportistica.statistiche.model.StatisticaRiscossione;
import it.govpay.core.beans.Costanti;
import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.core.dao.reportistica.StatisticaRendicontazioniDAO;
import it.govpay.core.dao.reportistica.StatisticaRiscossioniDAO;
import it.govpay.core.dao.reportistica.dto.ListaRendicontazioniDTO;
import it.govpay.core.dao.reportistica.dto.ListaRendicontazioniDTOResponse;
import it.govpay.core.dao.reportistica.dto.ListaRiscossioniDTO;
import it.govpay.core.dao.reportistica.dto.ListaRiscossioniDTO.GROUP_BY;
import it.govpay.core.dao.reportistica.dto.ListaRiscossioniDTOResponse;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreUtils;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Pagamento.TipoPagamento;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.model.reportistica.statistiche.FiltroRendicontazioni;
import it.govpay.model.reportistica.statistiche.FiltroRiscossioni;


public class QuadratureController extends BaseController {

     public QuadratureController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response getQuadratureRendicontazioni(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , List<String> gruppi, Integer pagina, Integer risultatiPerPagina,
    		String flussoRendicontazioneDataFlussoDa, String flussoRendicontazioneDataFlussoA, String dataRendicontazioneDa, String dataRendicontazioneA, String idFlusso, String iuv, List<String> direzione, List<String> divisione) {
    	String methodName = "getQuadratureRendicontazioni";
    	String transactionId = ContextThreadLocal.get().getTransactionId();

		try{
			this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName);

			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.RENDICONTAZIONI_E_INCASSI), Arrays.asList(Diritti.LETTURA));

			// Parametri - > DTO Input

			ListaRendicontazioniDTO listaRendicontazioniDTO = new ListaRendicontazioniDTO(user);

			FiltroRendicontazioni filtro = new FiltroRendicontazioni();

			if(risultatiPerPagina == null) {
				risultatiPerPagina = BasicFindRequestDTO.DEFAULT_LIMIT;
			}

			ValidatorFactory vf = ValidatorFactory.newInstance();
			ValidatoreUtils.validaRisultatiPerPagina(vf, Costanti.PARAMETRO_RISULTATI_PER_PAGINA, risultatiPerPagina);

			listaRendicontazioniDTO.setLimit(risultatiPerPagina);

			if(pagina == null) {
				pagina = 1;
			}
			listaRendicontazioniDTO.setPagina(pagina);


			Date dataFlussoDaDate = null;
			if(flussoRendicontazioneDataFlussoDa!=null) {
				dataFlussoDaDate = SimpleDateFormatUtils.getDataDaConTimestamp(flussoRendicontazioneDataFlussoDa, "flussoRendicontazione.dataFlussoDa", true);
				filtro.setDataFlussoDa(dataFlussoDaDate);
			}

			Date dataFlussoADate = null;
			if(flussoRendicontazioneDataFlussoA!=null) {
				dataFlussoADate = SimpleDateFormatUtils.getDataAConTimestamp(flussoRendicontazioneDataFlussoA, "flussoRendicontazione.dataFlussoA", true);
				filtro.setDataFlussoA(dataFlussoADate);
			}

			Date dataRendicontazioneDaDate = null;
			if(dataRendicontazioneDa!=null) {
				dataRendicontazioneDaDate = SimpleDateFormatUtils.getDataDaConTimestamp(dataRendicontazioneDa, "dataDa", true);
				filtro.setDataRendicontazioneDa(dataRendicontazioneDaDate);
			}

			Date dataRendicontazioneADate = null;
			if(dataRendicontazioneA!=null) {
				dataRendicontazioneADate = SimpleDateFormatUtils.getDataAConTimestamp(dataRendicontazioneA, "dataA", true);
				filtro.setDataRendicontazioneA(dataRendicontazioneADate);
			}

			filtro.setCodFlusso(idFlusso);
			filtro.setIuv(iuv);
			filtro.setDirezione(direzione);
			filtro.setDivisione(divisione);

			if(gruppi != null && gruppi.size() >0) {
				List<ListaRendicontazioniDTO.GROUP_BY> groupBy = new ArrayList<>();
				for (String gruppoString : gruppi) {
					RaggruppamentoStatisticaRendicontazione gruppo = RaggruppamentoStatisticaRendicontazione.fromValue(gruppoString);
					if(gruppo != null) {
						ListaRendicontazioniDTO.GROUP_BY gruppoToAdd = null;

						switch (gruppo) {
						case FLUSSO_RENDICONTAZIONE:
							gruppoToAdd = ListaRendicontazioniDTO.GROUP_BY.FLUSSO_RENDICONTAZIONE;
							break;
						case DIREZIONE:
							gruppoToAdd = ListaRendicontazioniDTO.GROUP_BY.DIR;
							break;
						case DIVISIONE:
							gruppoToAdd = ListaRendicontazioniDTO.GROUP_BY.DIV;
							break;
						}

						if(groupBy.contains(gruppoToAdd))
							throw new ValidationException("Il gruppo [" + gruppoString + "] e' stato indicato piu' di una volta.");

						groupBy.add(gruppoToAdd);

					} else {
						throw new ValidationException("Codifica inesistente per gruppo. Valore fornito [" + gruppoString + "] valori possibili " + ArrayUtils.toString(RaggruppamentoStatisticaRendicontazione.values()));
					}
				}
				listaRendicontazioniDTO.setGroupBy(groupBy);
			} else {
				throw new ValidationException("Indicare almeno un gruppo");
			}

			listaRendicontazioniDTO.setFiltro(filtro);

			// INIT DAO

			StatisticaRendicontazioniDAO statisticaRendicontazioniDAO = new StatisticaRendicontazioniDAO();

			// CHIAMATA AL DAO

			ListaRendicontazioniDTOResponse listaRendicontazioniDTOResponse = statisticaRendicontazioniDAO.listaRendicontazioni(listaRendicontazioniDTO);

			// CONVERT TO JSON DELLA RISPOSTA

			List<StatisticaQuadraturaRendicontazione> results = new ArrayList<>();
			for(it.govpay.bd.reportistica.statistiche.model.StatisticaRendicontazione entrataPrevista: listaRendicontazioniDTOResponse.getResults()) {
				StatisticaQuadraturaRendicontazione rsModel = StatisticaQuadraturaConverter.toRsModelIndex(entrataPrevista, uriInfo);
				results.add(rsModel);
			}

			ListaStatisticheQuadratureRendicontazioni response = new ListaStatisticheQuadratureRendicontazioni(results, this.getServicePath(uriInfo), listaRendicontazioniDTOResponse.getTotalResults(), pagina, risultatiPerPagina);

			this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName);
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.logContext(ContextThreadLocal.get());
		}
    }



	public Response getQuadratureRiscossioni(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina,
			String dataDa, String dataA, List<String> idDominio, List<String> idUnita, List<String> idTipoPendenza, List<String> idA2A, List<String> direzione, List<String> divisione, List<String> tassonomia, List<String> tipo, List<String> gruppi) {
		String methodName = "getQuadratureRiscossioni";
		String transactionId = ContextThreadLocal.get().getTransactionId();

		try{
			this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName);

			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.RENDICONTAZIONI_E_INCASSI), Arrays.asList(Diritti.LETTURA));

			// Parametri - > DTO Input

			ListaRiscossioniDTO listaRiscossioniDTO = new ListaRiscossioniDTO(user);

			FiltroRiscossioni filtro = new FiltroRiscossioni();

			if(risultatiPerPagina == null) {
				risultatiPerPagina = BasicFindRequestDTO.DEFAULT_LIMIT;
			}

			ValidatorFactory vf = ValidatorFactory.newInstance();
			ValidatoreUtils.validaRisultatiPerPagina(vf, Costanti.PARAMETRO_RISULTATI_PER_PAGINA, risultatiPerPagina);

			listaRiscossioniDTO.setLimit(risultatiPerPagina);

			if(pagina == null) {
				pagina = 1;
			}
			listaRiscossioniDTO.setPagina(pagina);

			Date dataDaDate = null;
			if(dataDa!=null) {
				dataDaDate = SimpleDateFormatUtils.getDataDaConTimestamp(dataDa, "dataDa");
				filtro.setDataDa(dataDaDate);
			}

			Date dataADate = null;
			if(dataA!=null) {
				dataADate = SimpleDateFormatUtils.getDataAConTimestamp(dataA, "dataA");
				filtro.setDataA(dataADate);
			}

			filtro.setCodDominio(idDominio);
			filtro.setCodUo(idUnita);
			filtro.setCodTipoVersamento(idTipoPendenza);
			filtro.setDirezione(direzione);
			filtro.setDivisione(divisione);
			filtro.setTassonomia(tassonomia);
			filtro.setCodApplicazione(idA2A);

			if(tipo!=null) {
				List<TipoPagamento> tipi = new ArrayList<>();
				for (String tipoS : tipo) {
					TipoRiscossione tipoRiscossione = TipoRiscossione.fromValue(tipoS);
					if(tipoRiscossione != null) {
						tipi.add(TipoPagamento.valueOf(tipoRiscossione.toString()));
					}
				}

				if(tipi.size()> 0) {
					filtro.setTipo(tipi);
				}
			}

			if(gruppi != null && gruppi.size() >0) {
				List<GROUP_BY> groupBy = new ArrayList<>();
				for (String gruppoString : gruppi) {
					RaggruppamentoStatistica gruppo = RaggruppamentoStatistica.fromValue(gruppoString);
					if(gruppo != null) {
						GROUP_BY gruppoToAdd = null;

						switch (gruppo) {
						case APPLICAZIONE:
							gruppoToAdd = GROUP_BY.APPLICAZIONE;
							break;
						case DIREZIONE:
							gruppoToAdd = GROUP_BY.DIR;
							break;
						case DIVISIONE:
							gruppoToAdd = GROUP_BY.DIV;
							break;
						case DOMINIO:
							gruppoToAdd = GROUP_BY.DOMINIO;
							break;
						case TIPO_PENDENZA:
							gruppoToAdd = GROUP_BY.TIPO_PENDENZA;
							break;
						case UNITA_OPERATIVA:
							gruppoToAdd = GROUP_BY.UO;
							break;
						case TASSONOMIA:
							gruppoToAdd = GROUP_BY.TASSONOMIA;
							break;
						}

						if(groupBy.contains(gruppoToAdd))
							throw new ValidationException("Il gruppo [" + gruppoString + "] e' stato indicato piu' di una volta.");

						groupBy.add(gruppoToAdd);

					} else {
						throw new ValidationException("Codifica inesistente per gruppo. Valore fornito [" + gruppoString + "] valori possibili " + ArrayUtils.toString(RaggruppamentoStatistica.values()));
					}
				}
				listaRiscossioniDTO.setGroupBy(groupBy);
			} else {
				throw new ValidationException("Indicare almeno un gruppo");
			}

			listaRiscossioniDTO.setFiltro(filtro);

			// INIT DAO

			StatisticaRiscossioniDAO statisticaRiscossioniDAO = new StatisticaRiscossioniDAO();

			// CHIAMATA AL DAO

			ListaRiscossioniDTOResponse listaRiscossioniDTOResponse = statisticaRiscossioniDAO.listaRiscossioni(listaRiscossioniDTO);

			// CONVERT TO JSON DELLA RISPOSTA

			List<StatisticaQuadratura> results = new ArrayList<>();
			for(StatisticaRiscossione entrataPrevista: listaRiscossioniDTOResponse.getResults()) {
				StatisticaQuadratura rsModel = StatisticaQuadraturaConverter.toRsModelIndex(entrataPrevista);
				results.add(rsModel);
			}

			ListaStatisticheQuadrature response = new ListaStatisticheQuadrature(results, this.getServicePath(uriInfo), listaRiscossioniDTOResponse.getTotalResults(), pagina, risultatiPerPagina);

			this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName);
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.logContext(ContextThreadLocal.get());
		}
	}
}
