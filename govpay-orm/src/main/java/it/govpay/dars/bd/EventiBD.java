/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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
package it.govpay.dars.bd;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Evento;
import it.govpay.bd.registro.EventoFilter;
import it.govpay.dars.model.ListaEventiEntry;

public class EventiBD extends BasicBD {
	
	private it.govpay.bd.registro.EventiBD  eventiBd = null;

	public EventiBD(BasicBD basicBD) {
		super(basicBD);
		this.eventiBd = new it.govpay.bd.registro.EventiBD(basicBD);
	}
	
	public List<ListaEventiEntry> findAll(EventoFilter filter) throws ServiceException {
		try {
			List<ListaEventiEntry> eventiLst = new ArrayList<ListaEventiEntry>();
			
			List<it.govpay.bd.model.Evento> eventoDTOLst = this.eventiBd.findAll(filter);
			for (it.govpay.bd.model.Evento eventoDTO : eventoDTOLst) {
				ListaEventiEntry entry = new ListaEventiEntry();
				entry.setDataRegistrazione(eventoDTO.getDataOraEvento());
				entry.setId(eventoDTO.getId());
				entry.setDominio(eventoDTO.getCodDominio());
				entry.setEsito(eventoDTO.getEsito());
				entry.setIuv(eventoDTO.getIuv());
				entry.setCcp(eventoDTO.getCcp());
				entry.setCategoria(eventoDTO.getCategoriaEvento().name());
				entry.setSottotipo(eventoDTO.getSottotipoEvento());
				entry.setTipo(eventoDTO.getTipoEvento().name());
				
				eventiLst.add(entry);
			}
			return eventiLst;
		} catch (ServiceException e) {
			throw e;
		}
	}
	
	public Evento get(long idEvento) throws ServiceException, NotFoundException {
		Evento evento = null;
		try {
			evento = this.eventiBd.getEvento(idEvento);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}
		
		return evento;
		
	}
	
	public EventoFilter newFilter() throws ServiceException {
		try {
			return new EventoFilter(this.getServiceManager().getEventoServiceSearch());
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
}
