package it.govpay.core.dao.pagamenti.utils;

import it.govpay.servizi.commons.Anagrafica;
import net.sf.json.JSONObject;

public class JsonUtils {

	public static Anagrafica getAnagraficaFromJson(String jsonRequest) {
		Anagrafica anagrafica = null;

		try {
			JSONObject jsonObjectPagamentiPortaleRequest = JSONObject.fromObject( jsonRequest );  

			if(jsonObjectPagamentiPortaleRequest.containsKey("soggettoVersante")) {
				JSONObject jsonObjectSoggettoVersante = jsonObjectPagamentiPortaleRequest.getJSONObject("soggettoVersante");
				anagrafica = new Anagrafica();
				anagrafica.setCap(jsonObjectSoggettoVersante.getString("cap"));
				anagrafica.setCellulare(jsonObjectSoggettoVersante.getString("cellulare"));
				anagrafica.setCivico(jsonObjectSoggettoVersante.getString("civico"));
				anagrafica.setCodUnivoco(jsonObjectSoggettoVersante.getString("identificativo"));
				anagrafica.setEmail(jsonObjectSoggettoVersante.getString("email"));
				anagrafica.setIndirizzo(jsonObjectSoggettoVersante.getString("indirizzo"));
				anagrafica.setLocalita(jsonObjectSoggettoVersante.getString("localita"));
				anagrafica.setNazione(jsonObjectSoggettoVersante.getString("nazione"));
				anagrafica.setProvincia(jsonObjectSoggettoVersante.getString("provincia"));
				anagrafica.setRagioneSociale(jsonObjectSoggettoVersante.getString("anagrafica"));
			}
		}catch(Exception ee) {	}

		return anagrafica;
	}

	public static String getIbanAddebitoFromJson(String jsonRequest) {
		String ibanAddebito = null;

		try {
			JSONObject jsonObjectPagamentiPortaleRequest = JSONObject.fromObject( jsonRequest );  

			if(jsonObjectPagamentiPortaleRequest.containsKey("datiAddebito")) {
				JSONObject jsonObjectDatiAddebito = jsonObjectPagamentiPortaleRequest.getJSONObject("datiAddebito");
				ibanAddebito = jsonObjectDatiAddebito.getString("ibanAddebito");
			}
		}catch(Exception ee) {	}

		return ibanAddebito;
	}

	public static String getCredenzialiPagatoreFromJson(String jsonRequest) {
		String autenticazione = null;

		try {
			JSONObject jsonObjectPagamentiPortaleRequest = JSONObject.fromObject( jsonRequest );  
			autenticazione = jsonObjectPagamentiPortaleRequest.getString("credenzialiPagatore");
		}catch(Exception ee) {	}

		return autenticazione;
	}

	public static String getAutenticazioneFromJson(String jsonRequest) {
		String autenticazione = null;

		try {
			JSONObject jsonObjectPagamentiPortaleRequest = JSONObject.fromObject( jsonRequest );  
			autenticazione = jsonObjectPagamentiPortaleRequest.getString("autenticazioneSoggetto");
		}catch(Exception ee) {	}

		return autenticazione;
	}
}
