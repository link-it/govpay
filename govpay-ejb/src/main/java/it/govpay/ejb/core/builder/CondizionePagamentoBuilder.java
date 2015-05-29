/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
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
package it.govpay.ejb.core.builder;

import it.govpay.ejb.core.model.CondizionePagamentoModel;
import it.govpay.orm.posizionedebitoria.Condizione;

public class CondizionePagamentoBuilder {
	
	public static CondizionePagamentoModel fromCondizione(Condizione cond, String idEnte) {
		
		if (cond == null)
			return null;
		
		CondizionePagamentoModel condizionePagamentoModel = new CondizionePagamentoModel();
		
		condizionePagamentoModel.setCausale(cond.getCausalePagamento());
		condizionePagamentoModel.setCodiceTributo(cond.getCdTrbEnte());
		condizionePagamentoModel.setDataDecorrenza(cond.getDtPagamento());
		condizionePagamentoModel.setDataFineValidita(cond.getDtFinevalidita());
		condizionePagamentoModel.setDataInizioValidita(cond.getDtIniziovalidita());
		condizionePagamentoModel.setDataScadenza(cond.getDtScadenza());
		condizionePagamentoModel.setIbanBeneficiario(cond.getIbanBeneficiario());
		condizionePagamentoModel.setIdCondizione(cond.getIdCondizione());
		// per limitare gli accessi al db passo l'idente. ma se manca ...
		condizionePagamentoModel.setIdCreditore(idEnte != null ? idEnte : cond.getPendenza().getTributoEnte().getIdEnte());
		condizionePagamentoModel.setIdPagamentoEnte(cond.getIdPagamento());
		condizionePagamentoModel.setImportoTotale(cond.getImTotale());
		condizionePagamentoModel.setRagioneSocaleBeneficiario(cond.getRagioneSocaleBeneficiario());
		condizionePagamentoModel.setTipoPagamento(CondizionePagamentoModel.EnumTipoPagamento.valueOf(cond.getTiPagamento()));

		return condizionePagamentoModel;
		
	}

}
