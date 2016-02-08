/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
package it.govpay.web.wsclient;

import it.gov.spcoop.nodopagamentispc.servizi.pagamentitelematicirpt.PagamentiTelematiciRPT;
import it.gov.spcoop.nodopagamentispc.servizi.pagamentitelematicirpt.PagamentiTelematiciRPTservice;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;


public class PoolablePagamentiTelematiciRPTFactory extends BasePooledObjectFactory<PagamentiTelematiciRPT> {
	
	private static PagamentiTelematiciRPTservice service;
	
	public PoolablePagamentiTelematiciRPTFactory(PagamentiTelematiciRPTservice service) {
		PoolablePagamentiTelematiciRPTFactory.service = service;
	}

	@Override
	public PagamentiTelematiciRPT create() throws Exception {
		return service.getPagamentiTelematiciRPTPort();
	}

	@Override
	public PooledObject<PagamentiTelematiciRPT> wrap(PagamentiTelematiciRPT obj) {
		return new DefaultPooledObject<PagamentiTelematiciRPT>(obj);
	}

}
