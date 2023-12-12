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
package it.govpay.backoffice.v1.beans.converter;

import java.math.BigDecimal;

import it.govpay.backoffice.v1.beans.PromemoriaIndex;
import it.govpay.backoffice.v1.beans.StatoPromemoria;
import it.govpay.backoffice.v1.beans.TipoPromemoria;
import it.govpay.bd.model.Promemoria;

public class PromemoriaConverter {

	public static PromemoriaIndex toRsModelIndex(Promemoria promemoria) {
		PromemoriaIndex rsModel = new PromemoriaIndex();

		rsModel.setDataCreazione(promemoria.getDataCreazione());
		rsModel.setDataProssimaSpedizione(promemoria.getDataProssimaSpedizione());
		rsModel.setDataUltimoAggiornamento(promemoria.getDataAggiornamento());
		rsModel.setDescrizioneStato(promemoria.getDescrizioneStato());
		rsModel.setNumeroTentativi(new BigDecimal(promemoria.getTentativiSpedizione()));
		if(promemoria.getStato() != null) {
			switch (promemoria.getStato()) {
			case ANNULLATO:
				rsModel.setStato(StatoPromemoria.ANNULLATO);
				break;
			case DA_SPEDIRE:
				rsModel.setStato(StatoPromemoria.DA_SPEDIRE);
				break;
			case FALLITO:
				rsModel.setStato(StatoPromemoria.FALLITO);
				break;
			case SPEDITO:
				rsModel.setStato(StatoPromemoria.SPEDITO);
				break;
			}
		}

		if(promemoria.getTipo() != null) {
			switch (promemoria.getTipo()) {
			case AVVISO:
				rsModel.setTipo(TipoPromemoria.AVVISO_PAGAMENTO);
				break;
			case SCADENZA:
				rsModel.setTipo(TipoPromemoria.SCADENZA_AVVISO_PAGAMENTO);
				break;
			case RICEVUTA:
			case RICEVUTA_NO_RPT:
				rsModel.setTipo(TipoPromemoria.RICEVUTA_TELEMATICA);
				break;
			}
		}

		return rsModel;
	}

}
