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

import org.openspcoop2.utils.LoggerWrapperFactory;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.model.BasicModel;
import it.govpay.orm.Audit;
import it.govpay.orm.IdOperatore;

public class AuditBD extends BasicBD {

	public AuditBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public AuditBD(String idTransaction) {
		super(idTransaction);
	}
	
	public AuditBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}
	
	public AuditBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache());
	}
	
	public void insertAudit(long idOperatore, BasicModel model) {
		
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			Audit audit = new Audit();
			audit.setData(new Date());
			audit.setIdOggetto(model.getId());
			audit.setOggetto(model.toString());
			audit.setTipoOggetto(model.getClass().getSimpleName());
			IdOperatore idOp = new IdOperatore();
			idOp.setId(idOperatore);
			audit.setIdOperatore(idOp);
			
			this.getAuditService().create(audit);
		} catch(Throwable e) {
			LoggerWrapperFactory.getLogger(AuditBD.class).error("Errore durante la registrazione dell'audit: " + e.getMessage(),e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
}
