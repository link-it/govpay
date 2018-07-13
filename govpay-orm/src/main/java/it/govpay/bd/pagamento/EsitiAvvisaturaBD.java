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
package it.govpay.bd.pagamento;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.converter.EsitoAvvisaturaConverter;
import it.govpay.bd.pagamento.filters.EsitoAvvisaturaFilter;
import it.govpay.model.EsitoAvvisatura;
import it.govpay.orm.dao.jdbc.JDBCEsitoAvvisaturaServiceSearch;

public class EsitiAvvisaturaBD extends BasicBD {

	public EsitiAvvisaturaBD(BasicBD basicBD) {
		super(basicBD);
	}

	/**
	 * Recupera l'esitoAvvisatura tramite l'id fisico
	 * 
	 * @param idEnte
	 * @return
	 * @throws NotFoundException se l'ente non esiste.
	 * @throws MultipleResultException in caso di duplicati.
	 * @throws ServiceException in caso di errore DB.
	 */
	public EsitoAvvisatura getEsitoAvvisatura(Long idEsitoAvvisatura) throws NotFoundException, ServiceException {
		if(idEsitoAvvisatura == null) {
			throw new ServiceException("Parameter 'id' cannot be NULL");
		}

		long id = idEsitoAvvisatura.longValue();

		try {
			it.govpay.orm.EsitoAvvisatura esitoAvvisaturaVO = ((JDBCEsitoAvvisaturaServiceSearch)this.getEsitoAvvisaturaService()).get(id);
			return EsitoAvvisaturaConverter.toDTO(esitoAvvisaturaVO);

		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}
	}

	public void insertEsitoAvvisatura(EsitoAvvisatura esitoAvvisatura) throws ServiceException{
		try {
			it.govpay.orm.EsitoAvvisatura vo = EsitoAvvisaturaConverter.toVO(esitoAvvisatura);

			this.getEsitoAvvisaturaService().create(vo);
			esitoAvvisatura.setId(vo.getId());
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}

	}

	public void insertEsitoAvvisaturaBatch(List<EsitoAvvisatura> esitoAvvisaturaLst) throws ServiceException{
		try {
			for(EsitoAvvisatura dto: esitoAvvisaturaLst) {
				it.govpay.orm.EsitoAvvisatura vo = EsitoAvvisaturaConverter.toVO(dto);
				this.getEsitoAvvisaturaService().create(vo);
				dto.setId(vo.getId());
			}
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}

	}


	public EsitoAvvisaturaFilter newFilter() throws ServiceException {
		return new EsitoAvvisaturaFilter(this.getEsitoAvvisaturaService());
	}
	
	public EsitoAvvisaturaFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new EsitoAvvisaturaFilter(this.getEsitoAvvisaturaService(),simpleSearch);
	}

	public long count(EsitoAvvisaturaFilter filter) throws ServiceException {
		try {
			return this.getEsitoAvvisaturaService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<EsitoAvvisatura> findAll(EsitoAvvisaturaFilter filter) throws ServiceException {
		try {
			List<EsitoAvvisatura> lst = new ArrayList<EsitoAvvisatura>();
			List<it.govpay.orm.EsitoAvvisatura> lstEsitoAvvisaturaVO = this.getEsitoAvvisaturaService().findAll(filter.toPaginatedExpression());
			for(it.govpay.orm.EsitoAvvisatura esitoAvvisaturaVO: lstEsitoAvvisaturaVO) {
				lst.add(EsitoAvvisaturaConverter.toDTO(esitoAvvisaturaVO));
			}
			return lst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public void deleteAll(EsitoAvvisaturaFilter filter) throws ServiceException {
		try {
			this.getEsitoAvvisaturaService().deleteAll(filter.toExpression());
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public void update(EsitoAvvisatura esitoAvvisatura) throws ServiceException {
		try {
			it.govpay.orm.EsitoAvvisatura vo = EsitoAvvisaturaConverter.toVO(esitoAvvisatura);
			this.getEsitoAvvisaturaService().update(this.getEsitoAvvisaturaService().convertToId(vo), vo);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		}
	}

}
