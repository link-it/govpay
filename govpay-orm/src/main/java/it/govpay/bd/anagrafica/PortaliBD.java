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
import it.govpay.bd.anagrafica.filters.PortaleFilter;
import it.govpay.model.Portale;

public class PortaliBD extends BasicBD {

	public PortaliBD(BasicBD basicBD) {
		super(basicBD);
	}

	/**
	 * Recupera l'portale tramite l'id fisico
	 * 
	 * @param idPortale
	 * @return
	 * @throws NotFoundException se non esiste.
	 * @throws MultipleResultException in caso di duplicati.
	 * @throws ServiceException in caso di errore DB.
	 */
	public Portale getPortale(Long id) throws NotFoundException, ServiceException, MultipleResultException {
		return null;
	}

	/**
	 * Recupera l'portale tramite l'id logico
	 * 
	 * @param codPortale
	 * @return
	 * @throws NotFoundException se l'ente non esiste.
	 * @throws MultipleResultException in caso di duplicati.
	 * @throws ServiceException in caso di errore DB.
	 */
	public Portale getPortale(String codPortale) throws NotFoundException, MultipleResultException, ServiceException {
		return null;
	}



	/**
	 * Recupera l'portale identificata dal Principal fornito
	 * 
	 * @param principal
	 * @return
	 * @throws NotFoundException se l'ente non esiste.
	 * @throws ServiceException in caso di errore DB.
	 */
	public Portale getPortaleByPrincipal(String principal) throws NotFoundException, MultipleResultException, ServiceException {
		return null;
	}

	/**
	 * Aggiorna l'portale con i dati forniti
	 * @param portale
	 * @throws NotFoundException se non esiste
	 * @throws ServiceException in caso di errore DB.
	 */
	public void updatePortale(Portale portale) throws NotFoundException, ServiceException {
		return;
	}

	/**
	 * Crea una nuova portale con i dati forniti
	 * @param portale
	 * @throws ServiceException in caso di errore DB.
	 */
	public void insertPortale(Portale portale) throws ServiceException{
		return;
	}

	public PortaleFilter newFilter() throws ServiceException {
		return null;
	}
	
	public PortaleFilter newFilter(boolean simpleSearch) throws ServiceException {
		return null;
	}

	public long count(PortaleFilter filter) throws ServiceException {
		return -1;
	}

	public List<Portale> findAll(PortaleFilter filter) throws ServiceException {
		return null;
	}

}
