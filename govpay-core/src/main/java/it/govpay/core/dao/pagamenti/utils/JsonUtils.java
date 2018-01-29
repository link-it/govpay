package it.govpay.core.dao.pagamenti.utils;

import it.govpay.servizi.commons.Anagrafica;
import net.sf.json.JSONObject;

public class JsonUtils {

	public static Anagrafica getAnagraficaFromJson(String jsonRequest) {
		Anagrafica anagrafica = new Anagrafica();

		try {
			JSONObject jsonObjectPagamentiPortaleRequest = JSONObject.fromObject( jsonRequest );  

			JSONObject jsonObjectSoggettoVersante = jsonObjectPagamentiPortaleRequest.getJSONObject("soggettoVersante");

			if(jsonObjectSoggettoVersante != null) {
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
		String ibanAddebito = "";

		try {
			JSONObject jsonObjectPagamentiPortaleRequest = JSONObject.fromObject( jsonRequest );  
			JSONObject jsonObjectDatiAddebito = jsonObjectPagamentiPortaleRequest.getJSONObject("datiAddebito");

			if(jsonObjectDatiAddebito != null) {
				ibanAddebito = jsonObjectDatiAddebito.getString("ibanAddebito");
			}
		}catch(Exception ee) {	}

		return ibanAddebito;
	}

	public static String getAutenticazioneFromJson(String jsonRequest) {
		String autenticazione = "";

		try {
			JSONObject jsonObjectPagamentiPortaleRequest = JSONObject.fromObject( jsonRequest );  
			autenticazione = jsonObjectPagamentiPortaleRequest.getString("autenticazioneSoggetto");
		}catch(Exception ee) {	}

		return autenticazione;
	}
}
