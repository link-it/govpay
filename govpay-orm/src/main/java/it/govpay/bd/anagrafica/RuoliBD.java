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

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.filters.RuoloFilter;
import it.govpay.bd.model.converter.RuoloConverter;
import it.govpay.model.Ruolo;
import it.govpay.orm.IdRuolo;
import it.govpay.orm.dao.jdbc.JDBCRuoloServiceSearch;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;

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

		if(idRuolo == null) {
			throw new ServiceException("Parameter 'id' cannot be NULL");
		}

		long id = idRuolo.longValue();

		try {
			it.govpay.orm.Ruolo ruoloVO = ((JDBCRuoloServiceSearch)this.getRuoloService()).get(id);
			Ruolo ruolo = RuoloConverter.toDTO(ruoloVO);

			return ruolo;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
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
		try {
			IdRuolo id = new IdRuolo();
			id.setCodRuolo(codRuolo);
			it.govpay.orm.Ruolo ruoloVO = this.getRuoloService().get(id);
			Ruolo ruolo = RuoloConverter.toDTO(ruoloVO);
			return ruolo;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Aggiorna l'ruolo con i dati forniti
	 * @param ente
	 * @throws NotFoundException se non esiste
	 * @throws ServiceException in caso di errore DB.
	 */
	public void updateRuolo(Ruolo ruolo) throws NotFoundException, ServiceException {
		try {
			it.govpay.orm.Ruolo vo = RuoloConverter.toVO(ruolo);
			IdRuolo id = this.getRuoloService().convertToId(vo);

			if(!this.getRuoloService().exists(id)) {
				throw new NotFoundException("Ruolo con id ["+id+"] non esiste.");
			}

			this.getRuoloService().update(id, vo);
			ruolo.setId(vo.getId());

		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}

	}

	/**
	 * Crea una nuova ruolo con i dati forniti
	 * @param ruolo
	 * @throws ServiceException in caso di errore DB.
	 */
	public void insertRuolo(Ruolo ruolo) throws ServiceException{
		try {
			it.govpay.orm.Ruolo vo = RuoloConverter.toVO(ruolo);
			this.getRuoloService().create(vo);
			ruolo.setId(vo.getId());
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}



	public RuoloFilter newFilter() throws ServiceException {
		return new RuoloFilter(this.getRuoloService());
	}
	
	public RuoloFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new RuoloFilter(this.getRuoloService(),simpleSearch);
	}

	public long count(RuoloFilter filter) throws ServiceException {
		try {
			return this.getRuoloService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<Ruolo> findAll(RuoloFilter filter) throws ServiceException {
		try {

			List<Ruolo> dtoList = new ArrayList<Ruolo>();
			for(it.govpay.orm.Ruolo vo: this.getRuoloService().findAll(filter.toPaginatedExpression())) {
				dtoList.add(RuoloConverter.toDTO(vo));
			}
			return dtoList;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

//	private Ruolo getRuolo(it.govpay.orm.Ruolo ruoloVO) throws ServiceException {
//		return RuoloConverter.toDTO(ruoloVO);
//	}
}
