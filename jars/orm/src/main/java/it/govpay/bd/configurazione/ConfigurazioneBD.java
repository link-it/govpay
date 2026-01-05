/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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

import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Configurazione;
import it.govpay.bd.model.converter.ConfigurazioneConverter;
import it.govpay.bd.model.converter.ConnettoreConverter;
import it.govpay.core.exceptions.IOException;
import it.govpay.model.Connettore;
import it.govpay.model.exception.CodificaInesistenteException;

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

			if(voList == null || voList.isEmpty())
				throw new NotFoundException("Configurazione Vuota");

			Configurazione dto = ConfigurazioneConverter.toDTO(voList);

			IPaginatedExpression exp = this.getConnettoreService().newPaginatedExpression();
			exp.equals(it.govpay.orm.Connettore.model().COD_CONNETTORE, Configurazione.COD_CONNETTORE_GDE);

			List<it.govpay.orm.Connettore> connettori = this.getConnettoreService().findAll(exp);
			Connettore connettoreGde = ConnettoreConverter.toDTO(connettori);
			if (connettoreGde.getIdConnettore() == null) {
				connettoreGde.setIdConnettore(it.govpay.bd.model.Configurazione.COD_CONNETTORE_GDE);
			}
			dto.setServizioGDE(connettoreGde);

			return dto;
		} catch (NotImplementedException | CodificaInesistenteException | ExpressionNotImplementedException | ExpressionException  e) {
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
				log.debug("Salvataggio configurazione entry [{}]...", vo.getNome());
				this.getConfigurazioneService().create(vo);
				log.debug("Salvataggio configurazione entry [{}] completato.", vo.getNome());
			}

			if(configurazione.getServizioGDE() != null) {
				List<it.govpay.orm.Connettore> voConnettoreLst = ConnettoreConverter.toVOList(configurazione.getServizioGDE());
				this.insertConnettore(voConnettoreLst, Configurazione.COD_CONNETTORE_GDE);
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

	private void insertConnettore(List<it.govpay.orm.Connettore> voConnettoreLst, String idConnettore) throws ServiceException {
		try {
			IExpression expDelete = this.getConnettoreService().newExpression();
			expDelete.equals(it.govpay.orm.Connettore.model().COD_CONNETTORE, idConnettore);
			this.getConnettoreService().deleteAll(expDelete);

			for (it.govpay.orm.Connettore connettore : voConnettoreLst) {
				this.getConnettoreService().create(connettore);
			}
		} catch (NotImplementedException | ExpressionNotImplementedException | ExpressionException e) {
			throw new ServiceException(e);
		}

	}
}
