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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.LikeMode;
import org.openspcoop2.utils.Utilities;
import org.openspcoop2.utils.UtilsException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.filters.OperatoreFilter;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.Utenza;
import it.govpay.bd.model.converter.OperatoreConverter;
import it.govpay.orm.IdOperatore;
import it.govpay.orm.dao.jdbc.JDBCOperatoreServiceSearch;

public class OperatoriBD extends BasicBD {

	public OperatoriBD(BasicBD basicBD) {
		super(basicBD);
	}

	/**
	 * Recupera l'operatore identificato dalla chiave fisica
	 * 
	 * @param idOperatore
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Operatore getOperatore(Long idOperatore) throws NotFoundException, MultipleResultException, ServiceException {

		if(idOperatore== null) {
			throw new ServiceException("Parameter 'id' cannot be NULL");
		}

		long id = idOperatore.longValue();


		try {
			it.govpay.orm.Operatore operatoreVO = ((JDBCOperatoreServiceSearch)this.getOperatoreService()).get(id);
			return this.getOperatore(operatoreVO);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Recupera l'operatore identificato dalla chiave logica
	 * 
	 * @param principal
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Operatore getOperatoreByPrincipal(String principal) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			IExpression expr = this.getOperatoreService().newExpression();
			expr.equals(it.govpay.orm.Operatore.model().ID_UTENZA.PRINCIPAL, principal);
			
			it.govpay.orm.Operatore operatoreVO = this.getOperatoreService().find(expr);
			return this.getOperatore(operatoreVO);
		} catch (NotImplementedException | MultipleResultException | ExpressionNotImplementedException | ExpressionException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Recupera l'operatore identificato dalla chiave logica
	 * 
	 * @param principal
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Operatore getOperatoreBySubject(String principal) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			IExpression expr = this.getOperatoreService().newExpression();

			Hashtable<String, String> hashSubject = null;
			try {
			  hashSubject = Utilities.getSubjectIntoHashtable(principal);
			}catch(UtilsException e) {
				throw new NotFoundException("Utenza" + principal + "non autorizzata");
			}
			Enumeration<String> keys = hashSubject.keys();
			while(keys.hasMoreElements()){
				String key = keys.nextElement();
				String value = hashSubject.get(key);
				expr.like(it.govpay.orm.Operatore.model().ID_UTENZA.PRINCIPAL, "/"+Utilities.formatKeySubject(key)+"="+Utilities.formatValueSubject(value)+"/", LikeMode.ANYWHERE);
			}
			
			it.govpay.orm.Operatore operatoreVO = this.getOperatoreService().find(expr);
			return this.getOperatore(operatoreVO);
		} catch (NotImplementedException | MultipleResultException | ExpressionNotImplementedException | ExpressionException e) {
			throw new ServiceException(e);
		}
	}
	
	
	/**
	 * Recupera l'operatore identificato dalla chiave logica
	 * 
	 * @param principal
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Operatore getOperatore(String principal) throws NotFoundException, ServiceException {
		return this.getOperatore(principal,false);
	}
	
	/**
	 * Recupera l'operatore identificato dalla chiave logica
	 * 
	 * @param principal
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Operatore getOperatore(String principal, boolean checkIgnoreCase) throws NotFoundException, ServiceException {
		try {
			IExpression expr = this.getOperatoreService().newExpression();
			if(checkIgnoreCase)
				expr.ilike(it.govpay.orm.Operatore.model().ID_UTENZA.PRINCIPAL_ORIGINALE, principal, LikeMode.EXACT);
			else 
				expr.equals(it.govpay.orm.Operatore.model().ID_UTENZA.PRINCIPAL_ORIGINALE, principal);
			
			it.govpay.orm.Operatore operatoreVO = this.getOperatoreService().find(expr);
			return this.getOperatore(operatoreVO);
		} catch (NotImplementedException | MultipleResultException | ExpressionNotImplementedException | ExpressionException e) {
			throw new ServiceException(e);
		}
	}


	private Operatore getOperatore(it.govpay.orm.Operatore operatoreVO) throws ServiceException, NotFoundException, MultipleResultException, NotImplementedException {
		Operatore operatore = OperatoreConverter.toDTO(operatoreVO);
		operatore.setUtenza(new UtenzeBD(this).getUtenza(operatoreVO.getIdUtenza().getId()));
		return operatore;
	}

	/**
	 * Recupera tutti gli operatori censiti sul sistema
	 * 
	 * @param principal
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public List<Operatore> getOperatori() throws ServiceException {
		return this.findAll(this.newFilter());
	}

	/**
	 * Aggiorna il operatore
	 * 
	 * @param operatore
	 * @throws NotFoundException
	 * @throws ServiceException
	 */
	public void updateOperatore(Operatore operatore) throws NotFoundException, ServiceException {
		try {
			
			UtenzeBD utenzeBD = new UtenzeBD(this);
			// autocommit false		
			this.setAutoCommit(false);
			
			if(!utenzeBD.exists(operatore.getUtenza())) {
				utenzeBD.insertUtenza(operatore.getUtenza());
			} else {
				try {
					utenzeBD.updateUtenza(operatore.getUtenza());
				} catch(NotFoundException e) {
					throw new ServiceException(e);
				}
			}
			operatore.setIdUtenza(operatore.getUtenza().getId());

			it.govpay.orm.Operatore vo = OperatoreConverter.toVO(operatore);
			IdOperatore idOperatore = this.getOperatoreService().convertToId(vo);
			if(!this.getOperatoreService().exists(idOperatore)) {
				throw new NotFoundException("Operatore con id ["+idOperatore.toJson()+"] non trovato");
			}
			
			this.getOperatoreService().update(idOperatore, vo);
			operatore.setId(vo.getId());
			
			this.emitAudit(operatore);
			this.commit();
		} catch (NotImplementedException | MultipleResultException | UtilsException e) {
			this.rollback();
			throw new ServiceException(e);
		} finally {
			// ripristino l'autocommit.
			this.setAutoCommit(true); 
		}
	}

	/**
	 * Crea un nuovo operatore.
	 * 
	 * @param ente
	 * @throws NotPermittedException
	 * @throws ServiceException
	 */
	public void insertOperatore(Operatore operatore) throws  ServiceException{
		try {
			UtenzeBD utenzeBD = new UtenzeBD(this);
			// autocommit false		
			this.setAutoCommit(false); 
			utenzeBD.insertUtenza(operatore.getUtenza());
			operatore.setIdUtenza(operatore.getUtenza().getId());
			it.govpay.orm.Operatore vo = OperatoreConverter.toVO(operatore);
			this.getOperatoreService().create(vo);
			operatore.setId(vo.getId());
			this.commit();
			// ripristino l'autocommit.
			this.setAutoCommit(true); 
			this.emitAudit(operatore);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public OperatoreFilter newFilter() throws ServiceException {
		return new OperatoreFilter(this.getOperatoreService());
	}

	public OperatoreFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new OperatoreFilter(this.getOperatoreService(),simpleSearch);
	}

	public long count(OperatoreFilter filter) throws ServiceException {
		try {
			return this.getOperatoreService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<Operatore> findAll(OperatoreFilter filter) throws ServiceException {
		try {
			List<Operatore> lst = new ArrayList<>();
			List<it.govpay.orm.Operatore> lstVO = this.getOperatoreService().findAll(filter.toPaginatedExpression());

			for(it.govpay.orm.Operatore operatoreVO : lstVO) {
				lst.add(this.getOperatore(operatoreVO));
			}
			return lst;
		} catch (NotImplementedException | NotFoundException | MultipleResultException e) {
			throw new ServiceException(e);
		}
	}

	public void deleteOperatore(String principal) throws ServiceException, NotFoundException {
		boolean oldAutoCommit = this.isAutoCommit();
		try {
			
			this.setAutoCommit(false); 
			
			Operatore operatore = this.getOperatore(principal);
			
			if(!this.exists(operatore )) {
				throw new NotFoundException();
			}
			
			Utenza utenza = operatore.getUtenza();
			this.getOperatoreService().delete(OperatoreConverter.toVO(operatore));
			
			// chiama utenza bd. delete
			new UtenzeBD(this).deleteUtenza(utenza, true);
			
			this.commit();
		} catch (NotImplementedException e) {
			this.rollback(); 
			throw new ServiceException(e);
		} catch (ServiceException e) {
			this.rollback();
			throw e;
		} finally {
			this.setAutoCommit(oldAutoCommit); 
		} 
	}
	
	/**
	 * Recupera operatore identificata dalla chiave fisica
	 * 
	 * @param operatore
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public boolean exists(Operatore operatore) throws ServiceException {
		try {
			IExpression expr = this.getOperatoreService().newExpression();
			expr.equals(it.govpay.orm.Operatore.model().ID_UTENZA.PRINCIPAL_ORIGINALE,operatore.getPrincipal());
			return this.getOperatoreService().count(expr).longValue() > 0;
		} catch (NotImplementedException | ExpressionNotImplementedException | ExpressionException e) {
			throw new ServiceException(e);
		}
	}
}
