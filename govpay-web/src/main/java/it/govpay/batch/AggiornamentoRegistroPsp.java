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
package it.govpay.batch;

import it.govpay.bd.BasicBD;
import it.govpay.business.RegistroPSP;

import java.util.UUID;

import javax.ejb.Schedule;
import javax.ejb.Singleton;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

@Singleton
public class AggiornamentoRegistroPsp {
	
	Logger log = LogManager.getLogger();
	
	@Schedule(hour="*/24", persistent=true)
	public void doWork(){
		ThreadContext.put("cmd", "AggiornamentoRegistroPsp");
		ThreadContext.put("op", UUID.randomUUID().toString() );
	
		BasicBD bd = null;
		try {
			bd = BasicBD.getInstance();
			new RegistroPSP(bd).aggiornaRegistro();
		} catch (Exception e) {
			log.info("Aggiornamento della lista dei PSP fallito", e);
		} finally {
			if(bd != null) bd.closeConnection();
		}
	}
}