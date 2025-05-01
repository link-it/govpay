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

import it.govpay.backoffice.v1.beans.ListaPromemoria;
import it.govpay.backoffice.v1.beans.PromemoriaIndex;
import it.govpay.backoffice.v1.beans.StatoPromemoria;
import it.govpay.backoffice.v1.beans.TipoPromemoria;
import it.govpay.backoffice.v1.beans.converter.PromemoriaConverter;
import it.govpay.bd.model.Promemoria;
import it.govpay.core.beans.Costanti;
import it.govpay.core.dao.pagamenti.PromemoriaDAO;
import it.govpay.core.dao.pagamenti.dto.ListaPromemoriaDTO;
import it.govpay.core.dao.pagamenti.dto.ListaPromemoriaDTOResponse;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreUtils;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;



public class PromemoriaController extends BaseController {

	public PromemoriaController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
	}

	public Response findPromemoria(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String dataDa, String dataA, String stato, String tipo, Boolean metadatiPaginazione, Boolean maxRisultati) {
		String methodName = "findPromemoria";
		String transactionId = ContextThreadLocal.get().getTransactionId();
		try{
			this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName);
			this.setMaxRisultati(maxRisultati);
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.CONFIGURAZIONE_E_MANUTENZIONE), Arrays.asList(Diritti.LETTURA));

			ValidatorFactory vf = ValidatorFactory.newInstance();
			ValidatoreUtils.validaRisultatiPerPagina(vf, Costanti.PARAMETRO_RISULTATI_PER_PAGINA, risultatiPerPagina);

			// Parametri - > DTO Input
			ListaPromemoriaDTO listaPromemoriaDTO = new ListaPromemoriaDTO(user);

			listaPromemoriaDTO.setLimit(risultatiPerPagina);
			listaPromemoriaDTO.setPagina(pagina);
			listaPromemoriaDTO.setEseguiCount(metadatiPaginazione);
			listaPromemoriaDTO.setEseguiCountConLimit(maxRisultati);

			if(stato != null) {
				StatoPromemoria statoPromemoria = StatoPromemoria.fromValue(stato);
				if(statoPromemoria != null) {
					switch (statoPromemoria) {
					case ANNULLATO:
						listaPromemoriaDTO.setStato(it.govpay.model.Promemoria.StatoSpedizione.ANNULLATO);
						break;
					case DA_SPEDIRE:
						listaPromemoriaDTO.setStato(it.govpay.model.Promemoria.StatoSpedizione.DA_SPEDIRE);
						break;
					case FALLITO:
						listaPromemoriaDTO.setStato(it.govpay.model.Promemoria.StatoSpedizione.FALLITO);
						break;
					case SPEDITO:
						listaPromemoriaDTO.setStato(it.govpay.model.Promemoria.StatoSpedizione.SPEDITO);
						break;
					}
				}
			}

			if(tipo != null) {
				TipoPromemoria tipoPromemoria = TipoPromemoria.fromValue(tipo);
				if(tipoPromemoria != null)
					switch (tipoPromemoria) {
					case AVVISO_PAGAMENTO:
						listaPromemoriaDTO.setTipo(it.govpay.model.Promemoria.TipoPromemoria.AVVISO);
						break;
					case RICEVUTA_TELEMATICA:
						listaPromemoriaDTO.setTipo(it.govpay.model.Promemoria.TipoPromemoria.RICEVUTA);
						break;
					case SCADENZA_AVVISO_PAGAMENTO:
						listaPromemoriaDTO.setTipo(it.govpay.model.Promemoria.TipoPromemoria.SCADENZA);
						break;
					}
			}

			if(dataDa!=null) {
				Date dataDaDate = SimpleDateFormatUtils.getDataDaConTimestamp(dataDa, Costanti.PARAM_DATA_DA);
				listaPromemoriaDTO.setDataDa(dataDaDate);
			}

			if(dataA!=null) {
				Date dataADate = SimpleDateFormatUtils.getDataAConTimestamp(dataA, Costanti.PARAM_DATA_A);
				listaPromemoriaDTO.setDataA(dataADate);
			}

			// INIT DAO

			PromemoriaDAO notificheDAO = new PromemoriaDAO();

			// CHIAMATA AL DAO

			ListaPromemoriaDTOResponse listaPromemoriaDTOResponse = notificheDAO.listaPromemoria(listaPromemoriaDTO);

			// CONVERT TO JSON DELLA RISPOSTA

			List<PromemoriaIndex> results = new ArrayList<>();
			for(Promemoria notifica: listaPromemoriaDTOResponse.getResults()) {
				results.add(PromemoriaConverter.toRsModelIndex(notifica));
			}

			ListaPromemoria response = new ListaPromemoria(results, this.getServicePath(uriInfo),
					listaPromemoriaDTOResponse.getTotalResults(), pagina, risultatiPerPagina, this.maxRisultatiBigDecimal);

			this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName);
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();

		}catch (Exception e) {
			return this.handleException(methodName, e, transactionId);
		} finally {
			this.logContext(ContextThreadLocal.get());
		}
	}
}
