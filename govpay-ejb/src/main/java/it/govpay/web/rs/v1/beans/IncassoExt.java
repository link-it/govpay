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
package it.govpay.web.rs.v1.beans;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;

public class IncassoExt extends Incasso{
	
	private List<Pagamento> pagamenti;
	
	public IncassoExt() {
		super();
	}
	
	public IncassoExt(it.govpay.bd.model.Incasso i, BasicBD bd) throws ServiceException {
		super(i);
		this.pagamenti = new ArrayList<Pagamento>();
		for(it.govpay.bd.model.Pagamento p : i.getPagamenti(bd)) {
			pagamenti.add(new Pagamento(p, bd));
		}
	}
	
	public List<Pagamento> getPagamenti() {
		return pagamenti;
	}
	public void setPagamenti(List<Pagamento> pagamenti) {
		this.pagamenti = pagamenti;
	}

}
