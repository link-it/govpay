/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
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
import org.slf4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Evento;
import it.govpay.bd.model.converter.EventoConverter;
import it.govpay.bd.model.eventi.EventoCooperazione;
import it.govpay.bd.model.eventi.EventoIntegrazione;
import it.govpay.bd.model.eventi.EventoNota;
import it.govpay.bd.pagamento.EventiBD;

public class GiornaleEventi extends BasicBD {
	
	private static Logger log = LoggerWrapperFactory.getLogger(GiornaleEventi.class	);
	
	public GiornaleEventi(BasicBD basicBD) {
		super(basicBD);
	}

	private void _registraEvento(Evento evento) {
		try {
			EventiBD eventiBD = new EventiBD(this);
			eventiBD.insertEvento(evento);
		} catch (Exception e) {
			log.error("Errore nella registrazione degli eventi", e);
		}
	}
	
	public void registraEventoCooperazione(EventoCooperazione eventoCooperazione) {
		try {
			this._registraEvento(EventoConverter.fromEventoCooperazioneToEvento(eventoCooperazione));
		} catch (Exception e) {
			log.error("Errore nella registrazione degli eventi", e);
		}
	}
	
	public void registraEventoNota(EventoNota eventoNota) {
		try {
			this._registraEvento(EventoConverter.fromEventoNotaToEvento(eventoNota));
		} catch (Exception e) {
			log.error("Errore nella registrazione degli eventi", e);
		}
	}
	
	public void registraEventointegrazione(EventoIntegrazione eventoIntegrazione) {
		try {
			this._registraEvento(EventoConverter.fromEventointegrazioneToEvento(eventoIntegrazione));
		} catch (Exception e) {
			log.error("Errore nella registrazione degli eventi", e);
		}
	}
}
