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
package it.govpay.ejb.ndp.controller;

import it.govpay.ejb.core.exception.GovPayException;
import it.govpay.ejb.core.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.ejb.ndp.ejb.EventoEJB;
import it.govpay.ejb.ndp.model.EventiInterfacciaModel;
import it.govpay.ejb.ndp.model.EventiInterfacciaModel.Evento;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.logging.log4j.Logger;

@Stateless
public class GdeController {
	
	@Inject
	EventoEJB eventoEjb;
	
    @Inject  
    private transient Logger log;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void registraEventi(EventiInterfacciaModel eventiInterfaccia) throws GovPayException {
		log.info("Richiesta inserimento eventi interfaccia:");
		for(Evento evento : eventiInterfaccia.getEventi()) {
			log.info("Evento con chiave [" + evento.getData() + "][" + evento.getDominio() + "][" + evento.getIuv() + "][" + evento.getCcp() + "]");
			log.debug("Psp: " + evento.getPsp());
			log.debug("Canale: " + evento.getCanalePagamento());
			log.debug("Tipo Versamento: " + evento.getTipoVersamento());
			log.debug("Esito: " + evento.getEsito());
			if(eventiInterfaccia.getInfospcoop() != null)
				log.debug("IdEgov: " + eventiInterfaccia.getInfospcoop().getIdEgov());
		}
		
		try {
			eventoEjb.inserisciEvento(eventiInterfaccia);
		} catch (Exception e) {
			log.error("Errore durante la registrazione dell'evento", e);
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Errore durante la registrazione dell'evento nel Giornale degli Eventi", e);
		}
	}
}