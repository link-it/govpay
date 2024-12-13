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
package it.govpay.core.dao.pagamenti;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Notifica;
import it.govpay.bd.pagamento.NotificheBD;
import it.govpay.bd.pagamento.filters.NotificaFilter;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.pagamenti.dto.ListaNotificheDTO;
import it.govpay.core.dao.pagamenti.dto.ListaNotificheDTOResponse;

public class NotificheDAO extends BaseDAO{


	public ListaNotificheDTOResponse listaNotifiche(ListaNotificheDTO listaNotificheDTO) throws ServiceException { 
		NotificheBD notificheBD = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		try {
			notificheBD = new NotificheBD(configWrapper);
			NotificaFilter filter = notificheBD.newFilter();
			
			filter.setOffset(listaNotificheDTO.getOffset());
			filter.setLimit(listaNotificheDTO.getLimit());
			filter.setDataInizio(listaNotificheDTO.getDataDa());
			filter.setDataFine(listaNotificheDTO.getDataA());
			filter.setEseguiCountConLimit(listaNotificheDTO.isEseguiCountConLimit());
			
			if(listaNotificheDTO.getStato() != null) {
				try {
					it.govpay.model.Notifica.StatoSpedizione statoSpedizione = listaNotificheDTO.getStato();
					filter.setStato(statoSpedizione.toString());
				} catch(Exception e) {
					return new ListaNotificheDTOResponse(0L, new ArrayList<>());
				}
			}
			
			if(listaNotificheDTO.getTipo() != null) {
				try {
					it.govpay.model.Notifica.TipoNotifica tipoNotifica =  listaNotificheDTO.getTipo();
					filter.setTipo(tipoNotifica.toString());
				} catch(Exception e) {
					return new ListaNotificheDTOResponse(0L, new ArrayList<>());
				}
			}
			
			Long count = null;
			
			if(listaNotificheDTO.isEseguiCount()) {
				 count = notificheBD.count(filter);
			}

			if(listaNotificheDTO.isEseguiFindAll()) {
				List<Notifica> lst = notificheBD.findAll(filter);
				
				for (Notifica notifica : lst) {
					this.populateNotifica(notifica, configWrapper);
				}
				
				return new ListaNotificheDTOResponse(count, lst);
			} else {
				return new ListaNotificheDTOResponse(count, new ArrayList<>());
			}
		}finally {
			notificheBD.closeConnection();
		}
	}
	
	private void populateNotifica(Notifica notifica, BDConfigWrapper configWrapper) throws ServiceException {
		notifica.getApplicazione(configWrapper);
	}
}
