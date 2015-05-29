/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
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
package it.govpay.ejb.core.builder;

import it.govpay.ejb.core.model.EnteCreditoreModel;
import it.govpay.ejb.core.model.OperatoreModel;
import it.govpay.orm.profilazione.IntestatarioOperatore;
import it.govpay.orm.profilazione.Operatore;

import java.util.ArrayList;
import java.util.List;

public class OperatoreBuilder {
	
	
	public static OperatoreModel fromOperatore(Operatore operatore) {
		
		if(operatore == null) {
			return null;
		}
		OperatoreModel operatoreModel = new OperatoreModel();
		operatoreModel.setUsername(operatore.getUsername());
		operatoreModel.setPassword(operatore.getPassword()); // encrypted
		operatoreModel.setIdOperatore(operatore.getOperatore());
		List<EnteCreditoreModel> entiModel = new ArrayList<EnteCreditoreModel>();
		
		for(IntestatarioOperatore intestOper : operatore.getIntestatarioperatori()) {
			entiModel.add(CreditoreBuilder.fromEnte(intestOper.getIntestatario().getEnte()));
		}
		operatoreModel.setEnti(entiModel);
		return operatoreModel;
	}

}
