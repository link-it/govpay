/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
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
package it.govpay.core.utils.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.transport.http.HttpRequestMethod;
import org.slf4j.Logger;

import gov.telematici.pagamenti.ws.rpt.ObjectFactory;
import it.govpay.bd.configurazione.model.Giornale;
import it.govpay.bd.model.Dominio;
import it.govpay.core.utils.EventoContext.Componente;
import it.govpay.model.ConnettoreNotificaPagamenti;

public class EnteRendicontazioniClient extends BasicClient {

	public enum Azione {
		
		inviaFlussoRendicontazione, inviaRpp, inviaSintesiFlussiRendicontazione, inviaSintesiPagamenti
	}

	private static ObjectFactory objectFactory;
	private static Logger log = LoggerWrapperFactory.getLogger(EnteRendicontazioniClient.class);
	private Dominio dominio;
	private it.govpay.bd.model.TracciatoNotificaPagamenti tracciato;
	
	public EnteRendicontazioniClient(Dominio dominio, it.govpay.bd.model.TracciatoNotificaPagamenti tracciato, ConnettoreNotificaPagamenti connettore, String operationID, Giornale giornale) throws ClientException, ServiceException {
		super(dominio, TipoConnettore.GOVPAY, connettore); 
		if(objectFactory == null || log == null ){
			objectFactory = new ObjectFactory();
		}
		this.operationID = operationID;
		this.componente = Componente.API_GOVPAY;
		this.dominio = dominio;
		this.tracciato = tracciato;
		this.setGiornale(giornale);
		
		this.getEventoCtx().setComponente(this.componente); 
	}
	
	@Override
	public String getOperationId() {
		return this.operationID;
	}
	
	public void setOperationId(String operationID) {
		this.operationID = operationID;
	}
	
	public String getSwaggerOperationId(ConnettoreNotificaPagamenti.Contenuti contenuto) {
		String swaggerOperationID = "";
		
		switch (contenuto) {
		case FLUSSI_RENDICONTAZIONE:
			swaggerOperationID = Azione.inviaFlussoRendicontazione.toString();
			break;
		case RPP:
			swaggerOperationID = Azione.inviaRpp.toString();
			break;
		case SINTESI_FLUSSI_RENDICONTAZIONE:
			swaggerOperationID = Azione.inviaSintesiFlussiRendicontazione.toString();
			break;
		case SINTESI_PAGAMENTI:
			swaggerOperationID = Azione.inviaSintesiPagamenti.toString();
			break;
		}
		
		return swaggerOperationID;
	}
	
	public String getContentType(ConnettoreNotificaPagamenti.Contenuti contenuto) {
		String swaggerOperationID = "";
		
		switch (contenuto) {
		case FLUSSI_RENDICONTAZIONE:
			swaggerOperationID = "application/xml";
			break;
		case RPP:
			swaggerOperationID = "application/json";
			break;
		case SINTESI_FLUSSI_RENDICONTAZIONE:
			swaggerOperationID = "text/csv";
			break;
		case SINTESI_PAGAMENTI:
			swaggerOperationID = "text/csv";
			break;
		}
		
		return swaggerOperationID;
	}
	
	public byte[] inviaFile(byte[] body, Map<String, String> queryParams, ConnettoreNotificaPagamenti.Contenuti contenuto, String idFile) throws ClientException { 
		List<Property> headerProperties = new ArrayList<>();
		headerProperties.add(new Property("Accept", "application/json"));
		StringBuilder sb = new StringBuilder();
		HttpRequestMethod httpMethod = HttpRequestMethod.POST;
		String swaggerOperationID = this.getSwaggerOperationId(contenuto);
		
		switch (contenuto) {
		case FLUSSI_RENDICONTAZIONE:
			sb.append("/flussiRendicontazione/" + idFile);
			break;
		case RPP:
			sb.append("/rpp/" + idFile);
			break;
		case SINTESI_FLUSSI_RENDICONTAZIONE:
			sb.append("/flussiRendicontazione/" + this.dominio.getCodDominio() + "/"+ this.tracciato.getIdentificativo());
			break;
		case SINTESI_PAGAMENTI:
			sb.append("/rpp/" + this.dominio.getCodDominio() + "/"+ this.tracciato.getIdentificativo());
			break;
		}
		
		// composizione URL
		if(queryParams != null) {
			boolean amp = false;
			for (String key : queryParams.keySet()) {
				if(amp) {
					sb.append("&");
				} else {
					sb.append("?");
					amp = true;
				}
	
				sb.append(key).append("=").append(queryParams.get(key));
			}
		}
		
		log.debug("Spedisco il contentuto " + contenuto.toString() + " del Tracciato (Dominio: " + this.dominio.getCodDominio() + ", Identificativo" + this.tracciato.getIdentificativo() + ") alla URL ("+sb.toString()+")");
		
		String contentType = this.getContentType(contenuto);
		
		return this.sendJson(sb.toString(), body, headerProperties, httpMethod, contentType, swaggerOperationID);
	}

}
