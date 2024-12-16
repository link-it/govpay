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
/**
 * 
 */
package it.govpay.core.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 31 gen 2018 $
 * 
 */
public class UriBuilderUtils {
	
	public static String getFromOperazioni(String idOperazione) {
		return getListOperazioni().path(idOperazione).build().toString();
	}

	public static String getFromPagamenti(String idPagamento) {
		return getListPagamenti().path(idPagamento).build().toString();
	}
	
	public static String getRppByDominioIuvCcp(String idDominio,String iuv, String ccp) {
		return getListRpp().path(idDominio).path(iuv).path(ccp).build().toString(); 
	}

	public static String getDominio(String codCominio) {
		return getFromDomini().buildFromEncoded(codCominio).toString();
	}

	public static String getLogoDominio(String codCominio) {
		return getFromDomini().path("logo").buildFromEncoded(codCominio).toString();
	}
	
	public static String getPendenzaByIdA2AIdPendenza(String idA2A, String idPendenza) {
		return getListPendenze().path("{idA2A}").path("{idPendenza}").buildFromEncoded(idA2A,idPendenza).toString();
	}
	
	public static String getPendenzeByPagamento(String idPagamento) {
		return getByPagamento(getListPendenze(), idPagamento);
	}

	public static String getPagamentiByIdA2AIdPendenza(String idA2A, String idPendenza) {
		return getListPagamenti().queryParam("idA2A", idA2A).queryParam("idPendenza", idPendenza).build().toString();
	}

	public static String getRptsByPagamento(String idPagamento) {
		return getByPagamento(getListRpp(), idPagamento);
	}

	public static String getRppsByIdA2AIdPendenza(String idA2A, String idPendenza) {
		return getListRpp().queryParam("idA2A", idA2A).queryParam("idPendenza", idPendenza).build().toString();
	}

	public static String getListUoByDominio(String idDominio) {
		return getFromDomini().path("unitaOperative").buildFromEncoded(idDominio).toString();
	}

	public static String getEntrateByDominio(String idDominio) {
		return getFromDomini().path("entrate").buildFromEncoded(idDominio).toString();
	}
	
	public static String getTipiPendenzaByDominio(String idDominio) {
		return getFromDomini().path("tipiPendenza").buildFromEncoded(idDominio).toString();
	}

	public static String getContiAccreditoByDominio(String idDominio) {
		return getFromDomini().path("contiAccredito").buildFromEncoded(idDominio).toString();
	}

	public static String getByPagamento(UriBuilder base, String idPagamento) {
		return base.queryParam("idPagamento", idPagamento).build().toString();
	}

	public static String getByPendenza(UriBuilder base, String idPendenza) {
		return base.queryParam("idPendenza", idPendenza).build().toString();
	}

	public static UriBuilder getFromIntermediari() {
		return getListIntermediari().path("{idIntermediario}");
	}
	
	private static UriBuilder getFromDomini() {
		return getListDomini().path("{idDominio}");
	}
	
	private static UriBuilder getListIntermediari() {
		return getBaseList("intermediari");
	}
	
	private static UriBuilder getListDomini() {
		return getBaseList("domini");
	}
	
	private static UriBuilder getListPagamenti() {
		return getBaseList("pagamenti");
	}

	private static UriBuilder getListRpp() {
		return getBaseList("rpp");
	}
	
	private static UriBuilder getListIncassi() {
		return getBaseList("incassi");
	}
	
	private static UriBuilder getListRiconciliazioni() {
		return getBaseList("riconciliazioni");
	}
	
	private static UriBuilder getListPendenze() {
		return getBaseList("pendenze");
	}
	
	private static UriBuilder getListOperazioni() {
		return getBaseList("operazioni");
	}
	
	public static UriBuilder getListRendicontazioni() {
		return getBaseList("rendicontazioni");
	}
	
	private static UriBuilder getBaseList(String type) {
		return getList(getBasePath(), type);
	}
	
	private static UriBuilder getBasePath() {
		UriBuilder fromPath = UriBuilder.fromPath("/");
		return fromPath;
	}

	private static UriBuilder getList(UriBuilder base, String type) {
		return base.path(type);
	}

	/**
	 * @param codStazione
	 * @return
	 */
	public static String getListDomini(String codStazione) {
		return getListDomini().queryParam("idStazione", codStazione).build().toString();
	}

	public static String getIncassiByIdDominioIdIncasso(String idDominio, String idIncasso) {
		return getListIncassi().path("{idDominio}").path("{idIncasso}").buildFromEncoded(idDominio,idIncasso).toString();
	}
	
	public static String getRiconciliazioniByIdDominioIdIncasso(String idDominio, String idIncasso) {
		return getListRiconciliazioni().path("{idDominio}").path("{idIncasso}").buildFromEncoded(idDominio,idIncasso).toString();
	}
	
	public static boolean isRisorseCustomBaseUrlEnabled() {
		Properties risorseCustomBaseURLProperties = GovpayConfig.getInstance().getRisorseCustomBaseURLProperties();
		Object object = risorseCustomBaseURLProperties.get("enabled");
		
	    if (object instanceof String) {
	        return Boolean.parseBoolean((String) object);
	    }
	    
	    return object instanceof Boolean && (Boolean) object;
	}
	
	public static URI getRisorseCustomBaseUrl(HttpHeaders httpHeaders) {
		
		Properties risorseCustomBaseURLProperties = GovpayConfig.getInstance().getRisorseCustomBaseURLProperties();
		String nomeHeaderProtocollo = (String) risorseCustomBaseURLProperties.get("nomeHeaderProtocollo");
		String nomeHeaderHost = (String) risorseCustomBaseURLProperties.get("nomeHeaderHost");
		String nomeHeaderPorta = (String) risorseCustomBaseURLProperties.get("nomeHeaderPorta");
		String nomeHeaderContesto = (String) risorseCustomBaseURLProperties.get("nomeHeaderContesto");
		
		String protocollo = StringUtils.isNotBlank(nomeHeaderProtocollo) ? httpHeaders.getHeaderString(nomeHeaderProtocollo) : null;
		String host = StringUtils.isNotBlank(nomeHeaderHost) ? httpHeaders.getHeaderString(nomeHeaderHost) : null;
		String porta = StringUtils.isNotBlank(nomeHeaderPorta) ? httpHeaders.getHeaderString(nomeHeaderPorta) : null;
		String contesto = StringUtils.isNotBlank(nomeHeaderContesto) ? httpHeaders.getHeaderString(nomeHeaderContesto): null;
		
	    // Validazione del protocollo
		if(protocollo == null) {
			protocollo = "https";
		}
		
	    if (!protocollo.equals("http") && !protocollo.equals("https")) {
	        return null; // Restituisci null se il protocollo è invalido
	    }

	    // Validazione dell'host
	    if (host == null || host.isEmpty()) {
	        return null; // Restituisci null se l'host è vuoto o null
	    }

	    // Validazione della porta
	    if (porta != null && !porta.isEmpty()) {
	        try {
	            int portaInt = Integer.parseInt(porta);
	            if (portaInt < 1 || portaInt > 65535) {
	                return null; // Restituisci null se la porta non è valida
	            }
	        } catch (NumberFormatException e) {
	            return null; // Restituisci null se la porta non è un numero valido
	        }
	    }

	    // Se la porta non è fornita, imposta un valore di default
	    if (porta == null || porta.isEmpty()) {
	        porta = protocollo.equals("https") ? "443" : "80"; // Porta di default per https è 443, per http è 80
	    }

	    // Validazione del contesto
	    if (contesto == null || contesto.isEmpty()) {
	        contesto = ""; // Lascia vuoto se non c'è contesto
	    } else {
	        // Verifica che il contesto inizi con uno slash
	        if (!contesto.startsWith("/")) {
	            contesto = "/" + contesto;
	        }
	    }

	    // Costruzione della URL
	    StringBuilder urlBuilder = new StringBuilder();
	    urlBuilder.append(protocollo)
	              .append("://")
	              .append(host)
	              .append(":")
	              .append(porta)
	              .append(contesto);

	    // Validazione della URL costruita
	    try {
	        URI uri = new URI(urlBuilder.toString());
	        if (!uri.isAbsolute()) {
	            return null; // Restituisci null se l'URL non è assoluta
	        }
	        return uri; // restistuisco la URI valida
	    } catch (URISyntaxException e) {
	        return null; // Restituisci null se l'URL è malformata
	    }
	}
	
	public static URI getServicePathConURIAssoluta(Logger log, UriInfo uriInfo, HttpHeaders httpHeaders) throws URISyntaxException {
		URI customURI = null;
		if(isRisorseCustomBaseUrlEnabled()) {
			customURI = getRisorseCustomBaseUrl(httpHeaders);
			log.debug("Custom URI {}", customURI);
		}

		if(customURI != null) {
			String baseUri = uriInfo.getBaseUri().toString();
			String requestUri = uriInfo.getRequestUri().toString();
			int idxOfBaseUri = requestUri.indexOf(baseUri);

			String servicePathwithParameters = requestUri.substring((idxOfBaseUri + baseUri.length()) - 1);
			
			String uriCompleta = customURI.toString() + servicePathwithParameters; 
			log.debug("URI completa {}", uriCompleta);
					
			return new URI(uriCompleta);
		}
		
		return uriInfo.getRequestUri();
	}
}
