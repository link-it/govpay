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
//package it.govpay.core.dao.pagamenti.utils;
//
//import it.govpay.servizi.commons.Anagrafica;
//import net.sf.json.JSONNull;
//import net.sf.json.JSONObject;
//
//public class JsonUtils {
//	
//	
//	public static String eliminaJSONNull(JSONObject jsonObject,String key) {
//		if(jsonObject.containsKey(key)) {
//			Object object = jsonObject.get(key);
//			if(object instanceof JSONNull) {
//				return null;
//			}
//			
//			return jsonObject.getString(key);
//		}
//		return null;
//	}
//
//	public static Anagrafica getAnagraficaFromJson(String jsonRequest) {
//		Anagrafica anagrafica = null;
//
//		try {
//			JSONObject jsonObjectPagamentiPortaleRequest = JSONObject.fromObject( jsonRequest );  
//
//			if(jsonObjectPagamentiPortaleRequest.containsKey("soggettoVersante")) {
//				JSONObject jsonObjectSoggettoVersante = jsonObjectPagamentiPortaleRequest.getJSONObject("soggettoVersante");
//				if(jsonObjectSoggettoVersante.isNullObject()) {
//					return null;
//				}
//				anagrafica = new Anagrafica();
//				anagrafica.setCap(eliminaJSONNull(jsonObjectSoggettoVersante,"cap"));
//				anagrafica.setCellulare(eliminaJSONNull(jsonObjectSoggettoVersante,"cellulare"));
//				anagrafica.setCivico(eliminaJSONNull(jsonObjectSoggettoVersante,"civico"));
//				anagrafica.setCodUnivoco(eliminaJSONNull(jsonObjectSoggettoVersante,"identificativo"));
//				anagrafica.setEmail(eliminaJSONNull(jsonObjectSoggettoVersante,"email"));
//				anagrafica.setIndirizzo(eliminaJSONNull(jsonObjectSoggettoVersante,"indirizzo"));
//				anagrafica.setLocalita(eliminaJSONNull(jsonObjectSoggettoVersante,"localita"));
//				anagrafica.setNazione(eliminaJSONNull(jsonObjectSoggettoVersante,"nazione"));
//				anagrafica.setProvincia(eliminaJSONNull(jsonObjectSoggettoVersante,"provincia"));
//				anagrafica.setRagioneSociale(eliminaJSONNull(jsonObjectSoggettoVersante,"anagrafica"));
//			}
//		}catch(Exception ee) {	}
//
//		return anagrafica;
//	}
//
//	public static String getIbanAddebitoFromJson(String jsonRequest) {
//		String ibanAddebito = null;
//
//		try {
//			JSONObject jsonObjectPagamentiPortaleRequest = JSONObject.fromObject( jsonRequest );  
//			
//			if(jsonObjectPagamentiPortaleRequest.containsKey("datiAddebito")) {
//				Object object = jsonObjectPagamentiPortaleRequest.get("datiAddebito");
//				if(object instanceof JSONNull) {
//					return null;
//				}
//				
//				JSONObject jsonObjectDatiAddebito = jsonObjectPagamentiPortaleRequest.getJSONObject("datiAddebito");
//				ibanAddebito = eliminaJSONNull(jsonObjectDatiAddebito,"ibanAddebito");
//			}
//		}catch(Exception ee) {	}
//
//		return ibanAddebito;
//	}
//
//	public static String getCredenzialiPagatoreFromJson(String jsonRequest) {
//		String autenticazione = null;
//
//		try {
//			JSONObject jsonObjectPagamentiPortaleRequest = JSONObject.fromObject( jsonRequest );
//			autenticazione = eliminaJSONNull(jsonObjectPagamentiPortaleRequest, "credenzialiPagatore");
//		}catch(Exception ee) {	}
//
//		return autenticazione;
//	}
//
//	public static String getAutenticazioneFromJson(String jsonRequest) {
//		String autenticazione = null;
//
//		try {
//			JSONObject jsonObjectPagamentiPortaleRequest = JSONObject.fromObject( jsonRequest );  
//			autenticazione = eliminaJSONNull(jsonObjectPagamentiPortaleRequest, "autenticazioneSoggetto");
//		}catch(Exception ee) {	}
//
//		return autenticazione;
//	}
//}
