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
package it.govpay.ejb.builder;

import it.govpay.ejb.model.CondizionePagamentoModel;
import it.govpay.ejb.model.PendenzaModel;
import it.govpay.ejb.model.PendenzaModel.EnumModalitaPagamento;
import it.govpay.ejb.model.PendenzaModel.EnumStatoPendenza;
import it.govpay.orm.posizionedebitoria.Condizione;
import it.govpay.orm.posizionedebitoria.DestinatarioPendenza;
import it.govpay.orm.posizionedebitoria.Pendenza;

import java.util.ArrayList;
import java.util.Set;

public class PendenzaBuilder {
	
	
	public static PendenzaModel fromPendenza(Pendenza pend) {
		
		PendenzaModel pendenzaModel = new PendenzaModel();

		pendenzaModel.setIdPendenza(pend.getIdPendenza());
		pendenzaModel.setImportoTotale(pend.getImTotale());
		pendenzaModel.setIdEnteCreditore(pend.getTributoEnte().getIdEnte());
		pendenzaModel.setIdTributo(pend.getTributoEnte().getIdTributo());
		pendenzaModel.setCodiceTributo(pend.getTributoEnte().getCdTrbEnte());
		pendenzaModel.setIdDebitoEnte(null); // TODO:MINO manca
		pendenzaModel.setDataCreazione(pend.getTsCreazioneente());
		pendenzaModel.setDataDecorrenza(pend.getTsDecorrenza());
		pendenzaModel.setDataEmissione(pend.getTsEmissioneente());
		pendenzaModel.setDataPrescrizione(pend.getTsPrescrizione());
		pendenzaModel.setAnnoRiferimento(pend.getAnnoRiferimento());
		pendenzaModel.setCausale(pend.getDeCausale());
		pendenzaModel.setStatoPendenza(EnumStatoPendenza.valueOf(pend.getStPendenza()));
		pendenzaModel.setModalitaPagamento(EnumModalitaPagamento.valueOf(pend.getFlModPagamento()));
		
		Set<DestinatarioPendenza> destinatari = pend.getDestinatari();
		if(destinatari != null && !destinatari.isEmpty()) {
			DestinatarioPendenza destinatario = destinatari.iterator().next();
			pendenzaModel.setDebitore(SoggettoBuilder.fromAnagraficaDestinatario(destinatario.getDatiAnagraficiDestinatario(), destinatario.getCoDestinatario()));
		}

		pendenzaModel.setCondizioniPagamento(new ArrayList<CondizionePagamentoModel>());
		for(Condizione cond : pend.getCondizioni()) {
			CondizionePagamentoModel condizionePagamentoModel = CondizionePagamentoBuilder.fromCondizione(cond, pend.getTributoEnte().getIdEnte());
			pendenzaModel.getCondizioniPagamento().add(condizionePagamentoModel);
		}
		
		return pendenzaModel;
	}

}
