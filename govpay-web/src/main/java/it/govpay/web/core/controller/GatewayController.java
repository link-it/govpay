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
package it.govpay.web.core.controller;

import it.govpay.ejb.core.ejb.AnagraficaEJB;
import it.govpay.ejb.core.exception.GovPayException;
import it.govpay.ejb.core.model.EnteCreditoreModel;
import it.govpay.ejb.core.model.GatewayPagamentoModel;
import it.govpay.ejb.core.model.ScadenzarioModel;
import it.govpay.ejb.core.model.GatewayPagamentoModel.EnumFornitoreGateway;
import it.govpay.ejb.core.model.GatewayPagamentoModel.EnumModalitaPagamento;
import it.govpay.ejb.core.utils.rs.EjbUtils;
import it.govpay.ejb.ndp.controller.PspController;
import it.govpay.ejb.ndp.ejb.AnagraficaDominioEJB;
import it.govpay.ejb.ndp.model.DominioEnteModel;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.ThreadContext;

@Stateless
public class GatewayController {

	@Inject
	AnagraficaEJB anagraficaEjb;

	@Inject
	AnagraficaDominioEJB anagraficaDominioEjb;

	@Inject
	PspController pspCtrl;

	public List<it.govpay.rs.Psp> listaPSP() {
		List<GatewayPagamentoModel> listaPsp = anagraficaEjb.getListaGatewayPagamento(EnumModalitaPagamento.ADDEBITO_DIRETTO, EnumModalitaPagamento.BONIFICO_BANCARIO_TESORERIA, EnumModalitaPagamento.BOLLETTINO_POSTALE, EnumModalitaPagamento.CARTA_PAGAMENTO, EnumModalitaPagamento.ATTIVATO_PRESSO_PSP);
		return EjbUtils.toWebPsp(listaPsp);
	}

	public void aggiornaListaPSP() throws GovPayException {
		ThreadContext.put("dom", null);
		ThreadContext.put("iuv", null);
		ThreadContext.put("ccp", null);

		List<EnteCreditoreModel> enti = anagraficaEjb.getEntiCreditori();
		List<GatewayPagamentoModel> listaPsp = new ArrayList<GatewayPagamentoModel>();
		for(EnteCreditoreModel ente : enti) {
			ThreadContext.put("dom", ente.getIdFiscale());
			List<ScadenzarioModel> scadenzari = anagraficaEjb.getScadenzari(ente.getIdEnteCreditore());
			for(ScadenzarioModel scadenzario : scadenzari) {
				DominioEnteModel dominioEnte = anagraficaDominioEjb.getDominioEnte(ente.getIdEnteCreditore(), scadenzario.getIdStazione());
			
				List<GatewayPagamentoModel> listaPspScadenzario = pspCtrl.chiediListaPsp(dominioEnte);
				for(GatewayPagamentoModel psp : listaPspScadenzario) {
					if(!listaPsp.contains(psp)) listaPsp.add(psp);
				}
			}
		}
		anagraficaEjb.aggiornaGatewayPagamento(listaPsp,EnumFornitoreGateway.NODO_PAGAMENTI_SPC);
	}		
}
