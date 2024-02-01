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
package it.govpay.core.business;

import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.pagamento.NotificheAppIoBD;

public class NotificaAppIo {
	
	public NotificaAppIo( ) {
	}

	public List<it.govpay.bd.model.NotificaAppIo> findNotificheDaSpedire(Integer offset, Integer limit) throws ServiceException{
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		NotificheAppIoBD notificheBD = new NotificheAppIoBD(configWrapper);
		return notificheBD.findNotificheDaSpedire(offset, limit);
	}
}
