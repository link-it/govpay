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
package it.govpay.rs.v1.beans;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonFilter;
import org.openspcoop2.generic_project.exception.ServiceException;

@JsonFilter(value="ruoli")  
public class Ruolo extends it.govpay.rs.v1.beans.base.Ruolo {

	@Override
	public String getJsonIdFilter() {
		return "ruoli";
	}
	
	public static Ruolo parse(String json) {
		return (Ruolo) parse(json, Ruolo.class);
	}
	
	public Ruolo(it.govpay.model.Ruolo ruolo) throws ServiceException {
		List<it.govpay.rs.v1.beans.base.ACL> acls = new ArrayList<>();
		
		ruolo.getAcls().stream().forEach(acl -> {acls.add(new ACL(acl));});
		this.idRuolo(ruolo.getCodRuolo())
		.descrizione(ruolo.getDescrizione())
		.acls(acls);
		
	}

}