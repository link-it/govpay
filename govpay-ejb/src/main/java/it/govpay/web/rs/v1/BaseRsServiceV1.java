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
package it.govpay.web.rs.v1;

import java.io.IOException;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.ThreadContext;
import org.codehaus.jackson.map.ObjectMapper;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.web.rs.BaseRsService;

public class BaseRsServiceV1 extends BaseRsService {
	
	@Override
	public int getVersione() {
		return 1;
	}
	
	public void setupContext(UriInfo uriInfo, HttpHeaders rsHttpHeaders,String nomeOperazione) throws ServiceException {
		GpContext ctx = new GpContext(uriInfo,rsHttpHeaders, request, nomeOperazione, nomeServizio, GpContext.TIPO_SERVIZIO_GOVPAY_JSON, getVersione());
		ThreadContext.put("op", ctx.getTransactionId());
		GpThreadLocal.set(ctx);
	}
	
	public void logResponse(UriInfo uriInfo, HttpHeaders rsHttpHeaders, String nomeOperazione, Object o) throws IOException {
		logResponse(uriInfo, rsHttpHeaders, nomeOperazione, o, null);
	}
	

	public void logResponse(UriInfo uriInfo, HttpHeaders rsHttpHeaders, String nomeOperazione, Object o, Integer responseCode) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(o);
		super.logResponse(uriInfo, rsHttpHeaders, nomeOperazione, json.getBytes(), responseCode);
	}
}
