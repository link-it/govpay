/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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
package it.govpay.bd.anagrafica;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.filters.IntermediarioFilter;
import it.govpay.bd.model.converter.ConnettoreConverter;
import it.govpay.bd.model.converter.IntermediarioConverter;
import it.govpay.model.Connettore;
import it.govpay.model.Intermediario;
import it.govpay.orm.IdIntermediario;
import it.govpay.orm.dao.jdbc.JDBCIntermediarioServiceSearch;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;

public class IntermediariBD extends BasicBD {

	public IntermediariBD(BasicBD basicBD) {
		super(basicBD);
	}

	/**
	 * Recupera l'intermediario tramite l'id fisico
	 * 
	 * @param idEnte
	 * @return
	 * @throws NotFoundException se l'ente non esiste.
	 * @throws MultipleResultException in caso di duplicati.
	 * @throws ServiceException in caso di errore DB.
	 */
	public Intermediario getIntermediario(Long idIntermediario) throws NotFoundException, MultipleResultException, ServiceException {
		if(idIntermediario == null) {
			throw new ServiceException("Parameter 'id' cannot be NULL");
		}

		long id = idIntermediario.longValue();

		try {
			it.govpay.orm.Intermediario intermediarioVO = ((JDBCIntermediarioServiceSearch)this.getIntermediarioService()).get(id);
			return getIntermediario(intermediarioVO);

		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Recupera l'intermediario tramite l'id logico
	 * 
	 * @param codEnte
	 * @return
	 * @throws NotFoundException se l'ente non esiste.
	 * @throws MultipleResultException in caso di duplicati.
	 * @throws ServiceException in caso di errore DB.
	 */
	public Intermediario getIntermediario(String codIntermediario) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			IdIntermediario id = new IdIntermediario();
			id.setCodIntermediario(codIntermediario);
			it.govpay.orm.Intermediario intermediarioVO = this.getIntermediarioService().get(id);

			return getIntermediario(intermediarioVO);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}

	public Intermediario getIntermediario(it.govpay.orm.Intermediario intermediarioVO) throws ExpressionNotImplementedException, ExpressionException, ServiceException, NotImplementedException {
		Intermediario intermediario = IntermediarioConverter.toDTO(intermediarioVO);

		if(intermediarioVO.getCodConnettorePdd() != null) {
			IPaginatedExpression exp = this.getConnettoreService().newPaginatedExpression();
			exp.equals(it.govpay.orm.Connettore.model().COD_CONNETTORE, intermediarioVO.getCodConnettorePdd());

			List<it.govpay.orm.Connettore> connettori = this.getConnettoreService().findAll(exp);
			Connettore connettorePdd = ConnettoreConverter.toDTO(connettori);
			intermediario.setConnettorePdd(connettorePdd);
		}
		return intermediario;

	}

	/**
	 * Recupera la lista degli intermediari censiti sul sistema
	 * 
	 * @return
	 * @throws ServiceException in caso di errore DB.
	 */
	public List<Intermediario> getIntermediari() throws ServiceException{
		return this.findAll(this.newFilter());
	}

	/**
	 * Aggiorna l'intermediari con i dati forniti
	 * @param intermediari
	 * @throws NotFoundException se l'intermediari non esiste
	 * @throws ServiceException in caso di errore DB.
	 */
	public void updateIntermediario(Intermediario intermediario) throws NotFoundException, ServiceException {

		try {
			it.govpay.orm.Intermediario vo = IntermediarioConverter.toVO(intermediario);
			IdIntermediario id = this.getIntermediarioService().convertToId(vo);

			if(!this.getIntermediarioService().exists(id)) {
				throw new NotFoundException("Intermediario con id ["+id+"] non esiste.");
			}

			this.getIntermediarioService().update(id, vo);
			intermediario.setId(vo.getId());

			if(intermediario.getConnettorePdd() != null) {

				List<it.govpay.orm.Connettore> voConnettoreLst = ConnettoreConverter.toVOList(intermediario.getConnettorePdd());


				IExpression expDelete = this.getConnettoreService().newExpression();
				expDelete.equals(it.govpay.orm.Connettore.model().COD_CONNETTORE, intermediario.getConnettorePdd().getIdConnettore());
				this.getConnettoreService().deleteAll(expDelete);

				for(it.govpay.orm.Connettore connettore: voConnettoreLst) {
					this.getConnettoreService().create(connettore);
				}
			}

			AnagraficaManager.removeFromCache(intermediario);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Aggiorna l'intermediari con i dati forniti
	 * @param intermediari
	 * @throws NotPermittedException se si inserisce un intermediari gia' censito
	 * @throws ServiceException in caso di errore DB.
	 */
	public void insertIntermediario(Intermediario intermediario) throws ServiceException{
		try {
			it.govpay.orm.Intermediario vo = IntermediarioConverter.toVO(intermediario);

			this.getIntermediarioService().create(vo);
			intermediario.setId(vo.getId());

			if(intermediario.getConnettorePdd() != null) {

				List<it.govpay.orm.Connettore> voConnettoreLst = ConnettoreConverter.toVOList(intermediario.getConnettorePdd());

				IExpression expDelete = this.getConnettoreService().newExpression();
				expDelete.equals(it.govpay.orm.Connettore.model().COD_CONNETTORE, intermediario.getConnettorePdd().getIdConnettore());
				this.getConnettoreService().deleteAll(expDelete);

				for(it.govpay.orm.Connettore connettore: voConnettoreLst) {

					this.getConnettoreService().create(connettore);
				}
			}

		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}

	}


	public IntermediarioFilter newFilter() throws ServiceException {
		return new IntermediarioFilter(this.getIntermediarioService());
	}

	public long count(IntermediarioFilter filter) throws ServiceException {
		try {
			return this.getIntermediarioService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<Intermediario> findAll(IntermediarioFilter filter) throws ServiceException {
		try {
			List<Intermediario> lst = new ArrayList<Intermediario>();
			List<it.govpay.orm.Intermediario> lstIntermediarioVO = this.getIntermediarioService().findAll(filter.toPaginatedExpression());
			for(it.govpay.orm.Intermediario intermediarioVO: lstIntermediarioVO) {
				lst.add(getIntermediario(intermediarioVO));
			}
			return lst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}

}
