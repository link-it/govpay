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
package it.govpay.core.business;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Evento;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.pagamento.EventiBD;
import it.govpay.core.beans.EventoContext;
import it.govpay.model.Intermediario;
import it.govpay.model.eventi.DatiPagoPA;

public class GiornaleEventi {
	
	private static Logger log = LoggerWrapperFactory.getLogger(GiornaleEventi.class	);
	
	public GiornaleEventi() {
		//donothing
	}

	public void registraEvento(Evento evento) {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		try {
			EventiBD eventiBD = new EventiBD(configWrapper);
			eventiBD.insertEvento(evento);
		} catch (Exception e) {
			log.error("Errore nella registrazione degli eventi", e);
		}
	}
	
	public static void popolaEventoCooperazione(Rpt rpt, Intermediario intermediario, Stazione stazione, EventoContext eventoContext) {
		DatiPagoPA datiPagoPA = new DatiPagoPA();
		datiPagoPA.setCodCanale(rpt.getCodCanale());
		datiPagoPA.setCodPsp(rpt.getCodPsp());
		datiPagoPA.setCodStazione(stazione.getCodStazione());
		datiPagoPA.setCodIntermediario(intermediario.getCodIntermediario());
		datiPagoPA.setErogatore(it.govpay.model.Evento.NDP);
		datiPagoPA.setFruitore(intermediario.getCodIntermediario());
		datiPagoPA.setTipoVersamento(rpt.getTipoVersamento());
		datiPagoPA.setModelloPagamento(rpt.getModelloPagamento().getCodifica()+"");
		datiPagoPA.setCodIntermediarioPsp(rpt.getCodIntermediarioPsp());
		datiPagoPA.setCodDominio(rpt.getCodDominio());
		eventoContext.setDatiPagoPA(datiPagoPA);
	}
}
