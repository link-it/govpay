/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
package it.govpay.backoffice.v1.beans.converter;

import java.math.BigDecimal;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.backoffice.v1.beans.NotificaIndex;
import it.govpay.backoffice.v1.beans.StatoNotifica;
import it.govpay.backoffice.v1.beans.TipoNotifica;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Notifica;

public class NotificheConverter {
	
	private NotificheConverter() {}

	public static NotificaIndex toRsModelIndex(Notifica notifica) throws ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);

		NotificaIndex rsModel = new NotificaIndex();

		rsModel.setDataCreazione(notifica.getDataCreazione());
		rsModel.setDataProssimaSpedizione(notifica.getDataProssimaSpedizione());
		rsModel.setDataUltimoAggiornamento(notifica.getDataAggiornamento());
		rsModel.setDescrizioneStato(notifica.getDescrizioneStato());
		rsModel.setNumeroTentativi(new BigDecimal(notifica.getTentativiSpedizione()));
		if(notifica.getStato() != null) {
			switch (notifica.getStato()) {
			case ANNULLATA:
				rsModel.setStato(StatoNotifica.ANNULLATA);
				break;
			case DA_SPEDIRE:
				rsModel.setStato(StatoNotifica.DA_SPEDIRE);
				break;
			case SPEDITO:
				rsModel.setStato(StatoNotifica.SPEDITA);
				break;
			default:
				break;
			}
		}

		if(notifica.getTipo() != null) {
			switch (notifica.getTipo()) {
			case ANNULLAMENTO:
				rsModel.setTipo(TipoNotifica.ANNULLAMENTO);
				break;
			case ATTIVAZIONE:
				rsModel.setTipo(TipoNotifica.ATTIVAZIONE);
				break;
			case FALLIMENTO:
				rsModel.setTipo(TipoNotifica.FALLIMENTO);
				break;
			case RICEVUTA:
				rsModel.setTipo(TipoNotifica.RICEVUTA);
				break;
			}
		}

		rsModel.setIdA2A(notifica.getApplicazione(configWrapper).getCodApplicazione());

		return rsModel;
	}

}
