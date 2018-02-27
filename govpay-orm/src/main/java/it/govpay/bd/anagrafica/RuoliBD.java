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
package it.govpay.bd.anagrafica;

import java.util.List;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.filters.RuoloFilter;
import it.govpay.model.Ruolo;

public class RuoliBD extends BasicBD {

	public RuoliBD(BasicBD basicBD) {
		super(basicBD);
	}

	/**
	 * Recupera l'ruolo tramite l'id fisico
	 * 
	 * @param idRuolo
	 * @return
	 * @throws NotFoundException se non esiste.
	 * @throws MultipleResultException in caso di duplicati.
	 * @throws ServiceException in caso di errore DB.
	 */
	public Ruolo getRuolo(Long idRuolo) throws NotFoundException, ServiceException, MultipleResultException {
		return null;
	}

	/**
	 * Recupera l'ruolo tramite l'id logico
	 * 
	 * @param codEnte
	 * @return
	 * @throws NotFoundException se l'ente non esiste.
	 * @throws MultipleResultException in caso di duplicati.
	 * @throws ServiceException in caso di errore DB.
	 */
	public Ruolo getRuolo(String codRuolo) throws NotFoundException, MultipleResultException, ServiceException {
		return null;
	}
	

	/**
	 * Aggiorna l'ruolo con i dati forniti
	 * @param ente
	 * @throws NotFoundException se non esiste
	 * @throws ServiceException in caso di errore DB.
	 */
	public void updateRuolo(Ruolo ruolo) throws NotFoundException, ServiceException {
	}

	/**
	 * Crea una nuova ruolo con i dati forniti
	 * @param ruolo
	 * @throws ServiceException in caso di errore DB.
	 */
	public void insertRuolo(Ruolo ruolo) throws ServiceException{
	}



	public RuoloFilter newFilter() throws ServiceException {
		return null;
	}
	
	public RuoloFilter newFilter(boolean simpleSearch) throws ServiceException {
		return null;
	}

	public long count(RuoloFilter filter) throws ServiceException {
		return -1;
	}

	public List<Ruolo> findAll(RuoloFilter filter) throws ServiceException {
		return null;
	}
}
