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
package it.govpay.backoffice.v1.beans.converter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map.Entry;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.backoffice.v1.beans.StatisticaQuadratura;
import it.govpay.backoffice.v1.beans.StatisticaQuadraturaRendicontazione;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.core.utils.UriBuilderUtils;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;

public class StatisticaQuadraturaConverter {
	
	private static final String PARAM_IUV = "iuv";
	private static final String PARAM_DATA_RENDICONTAZIONE_A = "dataRendicontazioneA";
	private static final String PARAM_DATA_RENDICONTAZIONE_DA = "dataRendicontazioneDa";
	private static final String PARAM_DATA_ORA_FLUSSO_A = "dataOraFlussoA";
	private static final String PARAM_DATA_ORA_FLUSSO_DA = "dataOraFlussoDa";
	private static final String PARAM_DIVISIONE = "divisione";
	private static final String PARAM_DIREZIONE = "direzione";
	private static final String PARAM_ID_FLUSSO = "idFlusso";

	private StatisticaQuadraturaConverter() {}

	public static StatisticaQuadratura toRsModelIndex(it.govpay.bd.reportistica.statistiche.model.StatisticaRiscossione statistica) throws ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		StatisticaQuadratura rsModel = new StatisticaQuadratura();

		if(statistica.getApplicazione(configWrapper) != null)
			rsModel.setApplicazione(ApplicazioniConverter.toRsModelIndex(statistica.getApplicazione(configWrapper)));
		rsModel.setDettaglio(null);
		rsModel.setDirezione(statistica.getDirezione());
		rsModel.setDivisione(statistica.getDivisione());
		rsModel.setTassonomia(statistica.getTassonomia());
		if(statistica.getDominio(configWrapper) != null)
			rsModel.setDominio(DominiConverter.toRsModelIndex(statistica.getDominio(configWrapper)));
		if(statistica.getImporto() != null)
			rsModel.setImporto(statistica.getImporto());
		if(statistica.getNumeroPagamenti() != null)
			rsModel.setNumeroPagamenti(new BigDecimal(statistica.getNumeroPagamenti()));
		if(statistica.getTipoVersamento(configWrapper) != null)
			rsModel.setTipoPendenza(TipiPendenzaConverter.toTipoPendenzaRsModelIndex(statistica.getTipoVersamento(configWrapper)));
		if(statistica.getUo(configWrapper) != null)
			rsModel.setUnitaOperativa(DominiConverter.toUnitaOperativaRsModelIndex(statistica.getUo(configWrapper)));

		return rsModel;
	}

	public static StatisticaQuadraturaRendicontazione toRsModelIndex(it.govpay.bd.reportistica.statistiche.model.StatisticaRendicontazione statistica, UriInfo uriInfo) {
		StatisticaQuadraturaRendicontazione rsModel = new StatisticaQuadraturaRendicontazione();
		if(statistica.getFlusso() != null) {
			rsModel.setFlussoRendicontazione(FlussiRendicontazioneConverter.toRsIndexModel(statistica.getFlusso()));
		}
		rsModel.setDirezione(statistica.getDirezione());
		rsModel.setDivisione(statistica.getDivisione());
		if(statistica.getImporto() != null)
			rsModel.setImporto(statistica.getImporto());
		if(statistica.getNumeroPagamenti() != null)
			rsModel.setNumeroRendicontazioni(new BigDecimal(statistica.getNumeroPagamenti()));

		creaURLDettaglio(statistica, uriInfo, rsModel);

		return rsModel;
	}

	private static void creaURLDettaglio(it.govpay.model.reportistica.statistiche.StatisticaRendicontazione statistica,
			UriInfo uriInfo, StatisticaQuadraturaRendicontazione rsModel) {
		// URL di dettaglio composta da tutti i filtri e da i valori dei gruppi trovati
		UriBuilder uriBuilder = UriBuilderUtils.getListRendicontazioni();
		MultivaluedMap<String, String> parametri = new MultivaluedHashMap<>();

		MultivaluedMap<String,String> queryParameters = uriInfo.getQueryParameters();

		/*
		 @QueryParam("dataOraFlussoDa") String dataOraFlussoDa,
		 @QueryParam("dataOraFlussoA") String dataOraFlussoA,
		 @QueryParam("dataRendicontazioneDa") String dataRendicontazioneDa,
		 @QueryParam("dataRendicontazioneA") String dataRendicontazioneA,
		 @QueryParam("idFlusso") String idFlusso,
		 @QueryParam("iuv") String iuv,
		 @QueryParam("direzione") List<String> direzione,
		 @QueryParam("divisione") List<String> divisione

		*/


		if(statistica.getCodFlusso() != null) {
			parametri.add(PARAM_ID_FLUSSO, statistica.getCodFlusso());
		} else {
			if(queryParameters.containsKey(PARAM_ID_FLUSSO))
				parametri.addAll(PARAM_ID_FLUSSO, queryParameters.get(PARAM_ID_FLUSSO));
		}

		if(statistica.getDirezione() != null) {
			parametri.add(PARAM_DIREZIONE, statistica.getDirezione());
		} else {
			if(queryParameters.containsKey(PARAM_DIREZIONE))
				parametri.addAll(PARAM_DIREZIONE, queryParameters.get(PARAM_DIREZIONE));
		}

		if(statistica.getDivisione() != null) {
			parametri.add(PARAM_DIVISIONE, statistica.getDivisione());
		} else {
			if(queryParameters.containsKey(PARAM_DIVISIONE))
				parametri.addAll(PARAM_DIVISIONE, queryParameters.get(PARAM_DIVISIONE));
		}

		if(queryParameters.containsKey(PARAM_DATA_ORA_FLUSSO_DA))
			parametri.addAll(PARAM_DATA_ORA_FLUSSO_DA, queryParameters.get(PARAM_DATA_ORA_FLUSSO_DA));
		if(queryParameters.containsKey(PARAM_DATA_ORA_FLUSSO_A))
			parametri.addAll(PARAM_DATA_ORA_FLUSSO_A, queryParameters.get(PARAM_DATA_ORA_FLUSSO_A));
		if(queryParameters.containsKey(PARAM_DATA_RENDICONTAZIONE_DA))
			parametri.addAll(PARAM_DATA_RENDICONTAZIONE_DA, queryParameters.get(PARAM_DATA_RENDICONTAZIONE_DA));
		if(queryParameters.containsKey(PARAM_DATA_RENDICONTAZIONE_A))
			parametri.addAll(PARAM_DATA_RENDICONTAZIONE_A, queryParameters.get(PARAM_DATA_RENDICONTAZIONE_A));
		if(queryParameters.containsKey(PARAM_IUV))
			parametri.addAll(PARAM_IUV, queryParameters.get(PARAM_IUV));

		// aggiungo tutti i parametri
		for (Entry<String, List<String>> entry : parametri.entrySet()) {
			String key = entry.getKey();
			List<String> list = entry.getValue();
			uriBuilder = uriBuilder.queryParam(key, list.toArray(new Object[list.size()]));
		}

		rsModel.setDettaglio(uriBuilder.build().toString());
	}




}

