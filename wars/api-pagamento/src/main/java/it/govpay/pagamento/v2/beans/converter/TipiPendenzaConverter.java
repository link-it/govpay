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
package it.govpay.pagamento.v2.beans.converter;

import org.openspcoop2.utils.jaxrs.RawObject;

import it.govpay.pagamento.v2.beans.TipoPendenza;
import it.govpay.pagamento.v2.beans.TipoPendenzaForm;

public class TipiPendenzaConverter {

	public static TipoPendenza toTipoPendenzaRsModel(it.govpay.model.TipoVersamento tipoVersamento) {
		TipoPendenza rsModel = new TipoPendenza();

		rsModel.descrizione(tipoVersamento.getDescrizione())
		.idTipoPendenza(tipoVersamento.getCodTipoVersamento());

		if(tipoVersamento.getCaricamentoPendenzePortalePagamentoFormTipoDefault() != null && tipoVersamento.getCaricamentoPendenzePortalePagamentoFormDefinizioneDefault() != null) {
			TipoPendenzaForm form = new TipoPendenzaForm();
			form.setTipo(tipoVersamento.getCaricamentoPendenzePortalePagamentoFormTipoDefault());
			form.setDefinizione(new RawObject(tipoVersamento.getCaricamentoPendenzePortalePagamentoFormDefinizioneDefault()));
			if(tipoVersamento.getCaricamentoPendenzePortalePagamentoFormImpaginazioneDefault() !=null)
				form.setImpaginazione(new RawObject(tipoVersamento.getCaricamentoPendenzePortalePagamentoFormImpaginazioneDefault()));
			rsModel.setForm(form);
		}

		rsModel.setVisualizzazione(new RawObject(tipoVersamento.getVisualizzazioneDefinizioneDefault()));


		return rsModel;
	}
}
