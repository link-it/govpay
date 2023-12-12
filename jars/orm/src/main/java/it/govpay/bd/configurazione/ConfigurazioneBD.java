/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2023 Link.it srl (http://www.link.it).
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
package it.govpay.bd.configurazione;

import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Configurazione;
import it.govpay.bd.model.converter.ConfigurazioneConverter;
import it.govpay.core.exceptions.IOException;

public class ConfigurazioneBD extends BasicBD {

	public ConfigurazioneBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public ConfigurazioneBD(String idTransaction) {
		super(idTransaction);
	}
	
	public ConfigurazioneBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}
	
	public ConfigurazioneBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache(), configWrapper.getIdOperatore());
	}

	public Configurazione getConfigurazione() throws NotFoundException, ServiceException{
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			List<it.govpay.orm.Configurazione> voList = this.getConfigurazioneService().findAll(this.getConfigurazioneService().newPaginatedExpression());
			
			if(voList == null || voList.size() == 0)
				throw new NotFoundException("Configurazione Vuota");
			
			return ConfigurazioneConverter.toDTO(voList);
		} catch (NotImplementedException  e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public void salvaConfigurazione(Configurazione configurazione) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			log.debug("Salvataggio configurazione...");
			
			log.debug("Salvataggio configurazione cancellazione entries obsolete...");
			this.getConfigurazioneService().deleteAll();
			log.debug("Salvataggio configurazione cancellazione entries obsolete completata");
			
			List<it.govpay.orm.Configurazione> voList = ConfigurazioneConverter.toVOList(configurazione);
			
			for (it.govpay.orm.Configurazione vo : voList) {
				log.debug("Salvataggio configurazione entry ["+vo.getNome()+"]...");
				this.getConfigurazioneService().create(vo);
				log.debug("Salvataggio configurazione entry ["+vo.getNome()+"] completato...");
			}
			
			this.emitAudit(configurazione);
			log.debug("Salvataggio configurazione completato.");
		} catch (NotImplementedException | IOException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
}
