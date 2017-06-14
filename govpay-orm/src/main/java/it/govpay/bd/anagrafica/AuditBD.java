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
package it.govpay.bd.anagrafica;

import java.util.Date;

import it.govpay.bd.BasicBD;
import it.govpay.model.BasicModel;

public class AuditBD extends BasicBD {

	public AuditBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public void insertAudit(long idOperatore, BasicModel model) {
		
		// insert con data attuale;
		// In caso di errore loggare
		
		
		
		System.out.println("<<<< START AUDIT >>>> " );
		if(idOperatore == 0) {
			System.err.println("Operatore non fornito!!!!");
		} else {
			System.out.println("Operatore " + idOperatore);
		}
		System.out.println("Tipo " + model.getClass().getSimpleName());
		System.out.println("Data " + new Date());
		System.out.println("Bean " + model.toString());
		System.out.println("<<<< STOP  AUDIT >>>> " );
	}
	
	
}
