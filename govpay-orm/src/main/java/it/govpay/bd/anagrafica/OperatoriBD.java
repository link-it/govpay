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
import it.govpay.bd.anagrafica.filters.OperatoreFilter;
import it.govpay.bd.model.Acl;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.converter.AclConverter;
import it.govpay.bd.model.converter.OperatoreConverter;
import it.govpay.orm.ACL;
import it.govpay.orm.IdOperatore;
import it.govpay.orm.dao.jdbc.JDBCOperatoreServiceSearch;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.UtilsException;

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
			return getOperatore(operatoreVO);
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
	public Operatore getOperatore(String principal) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			IdOperatore id = new IdOperatore();
			id.setPrincipal(principal);
			it.govpay.orm.Operatore operatoreVO = this.getOperatoreService().get(id);
			return getOperatore(operatoreVO);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	
	private Operatore getOperatore(it.govpay.orm.Operatore operatoreVO) throws ServiceException, NotFoundException, MultipleResultException, NotImplementedException {
		
		AclBD aclBD = new AclBD(this);
		try{
			List<Acl> acls = aclBD.getAclOperatore(operatoreVO.getId());

			Operatore operatore = OperatoreConverter.toDTO(operatoreVO, acls);
			return operatore;

		} catch(NotFoundException e) {
			throw new ServiceException(e);
		}

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

			it.govpay.orm.Operatore vo = OperatoreConverter.toVO(operatore);
			IdOperatore idOperatore = this.getOperatoreService().convertToId(vo);
			if(!this.getOperatoreService().exists(idOperatore)) {
				throw new NotFoundException("Operatore con id ["+idOperatore.toJson()+"] non trovato");
			}
			this.getOperatoreService().update(idOperatore, vo);

			AclBD aclBD = new AclBD(this);
			aclBD.deleteAclOperatore(operatore.getId());
			
			if(operatore.getAcls() != null && !operatore.getAcls().isEmpty()) {
				 
				for(Acl acl: operatore.getAcls()) {
					try{
						ACL aclVo = AclConverter.toVO(acl, this);
						IdOperatore idOperatoreACL = new IdOperatore();
						idOperatoreACL.setId(operatore.getId());
						aclVo.setIdOperatore(idOperatoreACL);
						this.getAclService().create(aclVo);
					} catch(NotFoundException e) {
						throw new ServiceException(e);
					}
				}
			}

			
			
			operatore.setId(vo.getId());
			AnagraficaManager.removeFromCache(operatore);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} catch (UtilsException e) {
			throw new ServiceException(e);
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

			it.govpay.orm.Operatore vo = OperatoreConverter.toVO(operatore);

			this.getOperatoreService().create(vo);
			operatore.setId(vo.getId());
			
			if(operatore.getAcls() != null && !operatore.getAcls().isEmpty()) {
				 
				for(Acl acl: operatore.getAcls()) {
					try{
						ACL aclVo = AclConverter.toVO(acl, this);
						IdOperatore idOperatoreACL = new IdOperatore();
						idOperatoreACL.setId(operatore.getId());
						aclVo.setIdOperatore(idOperatoreACL);
						this.getAclService().create(aclVo);
					} catch(NotFoundException e) {
						throw new ServiceException(e);
					}
				}
			}
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public OperatoreFilter newFilter() throws ServiceException {
		return new OperatoreFilter(this.getOperatoreService());
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
			List<Operatore> lst = new ArrayList<Operatore>();
			List<it.govpay.orm.Operatore> lstVO = this.getOperatoreService().findAll(filter.toPaginatedExpression());

			for(it.govpay.orm.Operatore operatoreVO : lstVO) {
				lst.add(getOperatore(operatoreVO));
			}
			return lst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}
	}

}
