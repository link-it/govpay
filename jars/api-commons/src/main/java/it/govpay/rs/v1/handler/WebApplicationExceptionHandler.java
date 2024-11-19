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
package it.govpay.rs.v1.handler;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class WebApplicationExceptionHandler implements ExceptionMapper<WebApplicationException>
{
	private boolean excludeFaultBean;

	@Override
	public Response toResponse(WebApplicationException e)
	{
		if(this.excludeFaultBean) {
			ResponseBuilder responseBuilder = Response.status(e.getResponse().getStatus());
			if(e.getResponse().getHeaders()!=null) {
				MultivaluedMap<String, Object> map = e.getResponse().getHeaders();
				if(!map.isEmpty()) {
					map.keySet().stream().forEach(k -> {
						responseBuilder.header(k, map.get(k));
					});
				}
			}
			return responseBuilder.build();
		} else {
			return e.getResponse();
		}

	}

	public boolean isExcludeFaultBean() {
		return this.excludeFaultBean;
	}

	public void setExcludeFaultBean(boolean excludeFaultBean) {
		this.excludeFaultBean = excludeFaultBean;
	}

}