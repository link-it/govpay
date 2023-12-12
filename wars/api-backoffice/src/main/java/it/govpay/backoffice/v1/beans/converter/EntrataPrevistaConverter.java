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

import it.govpay.backoffice.v1.beans.EntrataPrevistaIndex;

public class EntrataPrevistaConverter {


	public static EntrataPrevistaIndex toRsModelIndex(it.govpay.bd.viste.model.EntrataPrevista entrataPrevista) {
		EntrataPrevistaIndex rsModel = new EntrataPrevistaIndex();

		rsModel.setDataPagamento(entrataPrevista.getDataPagamento());
		rsModel.setDataRegolamento(entrataPrevista.getDataRegolamento());
		rsModel.setIdA2A(entrataPrevista.getCodApplicazione());
		rsModel.setIdDominio(entrataPrevista.getCodDominio());
		rsModel.setIdFlusso(entrataPrevista.getCodFlusso());
		rsModel.setIdPendenza(entrataPrevista.getCodVersamentoEnte());
		rsModel.setIdVocePendenza(entrataPrevista.getCodSingoloVersamentoEnte());
		rsModel.setImportoPagato(entrataPrevista.getImportoPagato());
		if(entrataPrevista.getImportoTotalePagamenti() != null)
			rsModel.setImportoTotale(entrataPrevista.getImportoTotalePagamenti().doubleValue());
		if(entrataPrevista.getIndiceDati() != null)
			rsModel.setIndice(new BigDecimal(entrataPrevista.getIndiceDati()));
		rsModel.setIur(entrataPrevista.getIur());
		rsModel.setIuv(entrataPrevista.getIuv());
		rsModel.setNumeroPagamenti(new BigDecimal(entrataPrevista.getNumeroPagamenti()));
		rsModel.setTrn(entrataPrevista.getFrIur());

		if(entrataPrevista.getAnno() != null)
			rsModel.setAnno(new BigDecimal(entrataPrevista.getAnno()));

		rsModel.setIdTipoPendenza(entrataPrevista.getCodTipoVersamento());
		rsModel.setIdEntrata(entrataPrevista.getCodEntrata());
		rsModel.setIdentificativoDebitore(entrataPrevista.getIdentificativoDebitore());


		return rsModel;
	}
}
