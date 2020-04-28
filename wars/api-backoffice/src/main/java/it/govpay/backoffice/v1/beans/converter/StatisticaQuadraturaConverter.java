package it.govpay.backoffice.v1.beans.converter;

import java.math.BigDecimal;
import java.util.List;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.backoffice.v1.beans.StatisticaQuadratura;
import it.govpay.backoffice.v1.beans.StatisticaQuadraturaRendicontazione;
import it.govpay.core.utils.UriBuilderUtils;

public class StatisticaQuadraturaConverter {

	
	public static StatisticaQuadratura toRsModelIndex(it.govpay.bd.reportistica.statistiche.model.StatisticaRiscossione statistica) throws ServiceException {
		StatisticaQuadratura rsModel = new StatisticaQuadratura();
		
		if(statistica.getApplicazione(null) != null)
			rsModel.setApplicazione(ApplicazioniConverter.toRsModelIndex(statistica.getApplicazione(null)));
		rsModel.setDettaglio(null);
		rsModel.setDirezione(statistica.getDirezione());
		rsModel.setDivisione(statistica.getDivisione());
		rsModel.setTassonomia(statistica.getTassonomia());
		if(statistica.getDominio(null) != null)
			rsModel.setDominio(DominiConverter.toRsModelIndex(statistica.getDominio(null)));
		if(statistica.getImporto() != null)
			rsModel.setImporto(statistica.getImporto());
		if(statistica.getNumeroPagamenti() != null)
			rsModel.setNumeroPagamenti(new BigDecimal(statistica.getNumeroPagamenti()));
		if(statistica.getTipoVersamento(null) != null)
			rsModel.setTipoPendenza(TipiPendenzaConverter.toTipoPendenzaRsModelIndex(statistica.getTipoVersamento(null)));
		if(statistica.getUo(null) != null)
			rsModel.setUnitaOperativa(DominiConverter.toUnitaOperativaRsModelIndex(statistica.getUo(null)));
		
//		if(statistica.getTipo() != null) {
//			if(statistica.getTipo().equals(TipoPagamento.ENTRATA)) {
//				rsModel.setTipo(TipoRiscossione.ENTRATA);
//			} else {
//				rsModel.setTipo(TipoRiscossione.MBT);
//			} 
//		}
		
		return rsModel;
	} 
	
	public static StatisticaQuadraturaRendicontazione toRsModelIndex(it.govpay.bd.reportistica.statistiche.model.StatisticaRendicontazione statistica, UriInfo uriInfo) throws ServiceException {
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
		MultivaluedMap<String, String> parametri = new MultivaluedHashMap<String, String>();
		
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
			parametri.add("idFlusso", statistica.getCodFlusso());
		} else {
			if(queryParameters.containsKey("idFlusso"))
				parametri.addAll("idFlusso", queryParameters.get("idFlusso"));
		}
		
		if(statistica.getDirezione() != null) {
			parametri.add("direzione", statistica.getDirezione());
		} else {
			if(queryParameters.containsKey("direzione"))
				parametri.addAll("direzione", queryParameters.get("direzione"));
		}
		
		if(statistica.getDivisione() != null) {
			parametri.add("divisione", statistica.getDivisione());
		} else {
			if(queryParameters.containsKey("divisione"))
				parametri.addAll("divisione", queryParameters.get("divisione"));
		}
		
		if(queryParameters.containsKey("dataOraFlussoDa"))
			parametri.addAll("dataOraFlussoDa", queryParameters.get("dataOraFlussoDa"));
		if(queryParameters.containsKey("dataOraFlussoA"))
			parametri.addAll("dataOraFlussoA", queryParameters.get("dataOraFlussoA"));
		if(queryParameters.containsKey("dataRendicontazioneDa"))
			parametri.addAll("dataRendicontazioneDa", queryParameters.get("dataRendicontazioneDa"));
		if(queryParameters.containsKey("dataRendicontazioneA"))
			parametri.addAll("dataRendicontazioneA", queryParameters.get("dataRendicontazioneA"));
		if(queryParameters.containsKey("iuv"))
			parametri.addAll("iuv", queryParameters.get("iuv"));

		// aggiungo tutti i parametri 
		for (String key : parametri.keySet()) {
			List<String> list = parametri.get(key);
			uriBuilder = uriBuilder.queryParam(key, list.toArray(new Object[list.size()]));
		}
		
		rsModel.setDettaglio(uriBuilder.build().toString());
	} 
	
	
	
	
}

