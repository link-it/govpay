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
import java.util.List;

import org.openspcoop2.generic_project.beans.Function;
import org.openspcoop2.generic_project.beans.FunctionField;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.filters.AclFilter;
import it.govpay.bd.model.Acl;
import it.govpay.bd.model.converter.AclConverter;
import it.govpay.model.Acl.Servizio;
import it.govpay.orm.ACL;
import it.govpay.orm.IdAcl;
import it.govpay.orm.IdUtenza;
import it.govpay.orm.dao.IDBACLService;
import it.govpay.orm.dao.jdbc.JDBCACLServiceSearch;

public class AclBD extends BasicBD {

	public AclBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public AclBD(String idTransaction) {
		super(idTransaction);
	}
	
	public AclBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}
	
	public AclBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache());
	}
	
	public AclFilter newFilter() throws ServiceException {
		return new AclFilter(this.getAclService());
	}

	public AclFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new AclFilter(this.getAclService(),simpleSearch);
	}
	
	/**
	 * Recupera l'applicazione tramite l'id fisico
	 * 
	 * @param idAcl
	 * @return
	 * @throws NotFoundException se non esiste.
	 * @throws MultipleResultException in caso di duplicati.
	 * @throws ServiceException in caso di errore DB.
	 */
	public Acl getAcl(Long idAcl) throws NotFoundException, ServiceException, MultipleResultException {

		if(idAcl == null) {
			throw new ServiceException("Parameter 'id' cannot be NULL");
		}

		long id = idAcl.longValue();

		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.ACL applicazioneVO = ((JDBCACLServiceSearch)this.getAclService()).get(id);
			Acl applicazione = AclConverter.toDTO(applicazioneVO);

			return applicazione;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public long count(AclFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getAclService());
			}
			
			return this.getAclService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public List<Acl> findAll(AclFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getAclService());
			}

			List<Acl> dtoList = new ArrayList<>();
			for(it.govpay.orm.ACL vo: this.getAclService().findAll(filter.toPaginatedExpression())) {
				dtoList.add(AclConverter.toDTO(vo));
			}
			return dtoList;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public List<String> findAllRuoli(AclFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getAclService());
			}
			
			List<Object> select = this.getAclService().select(filter.toPaginatedExpression(), true, ACL.model().RUOLO);
			
			List<String> ruoli = new ArrayList<>();
			for(Object obj: select) {
				if(obj instanceof String)
					ruoli.add((String) obj);
			}
			
			return ruoli;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public long countRuoli(AclFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getAclService());
			}
			
			Object cntDistinct = this.getAclService().aggregate(filter.toExpression(), new FunctionField(ACL.model().RUOLO, Function.COUNT_DISTINCT, "cnt"));
			return Long.parseLong(((String) cntDistinct));
			
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	
	/**
	 * Aggiorna l'applicazione con i dati forniti
	 * @param ente
	 * @throws NotFoundException se non esiste
	 * @throws ServiceException in caso di errore DB.
	 */
	public void updateAcl(Acl acl) throws NotFoundException, ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.ACL vo = AclConverter.toVO(acl);
			IdAcl id = this.getAclService().convertToId(vo);

			if(!this.getAclService().exists(id)) {
				throw new NotFoundException("Acl con id ["+id+"] non esiste.");
			}

			this.getAclService().update(id, vo);
			acl.setId(vo.getId());
			this.emitAudit(acl);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	/**
	 * Crea una nuova applicazione con i dati forniti
	 * @param acl
	 * @throws ServiceException in caso di errore DB.
	 */
	public void insertAcl(Acl acl) throws ServiceException{
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.ACL vo = AclConverter.toVO(acl);
			this.getAclService().create(vo);
			acl.setId(vo.getId());
			this.emitAudit(acl);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public void deleteAcl(Long id) throws ServiceException, NotFoundException{
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}

			if(!((IDBACLService)this.getAclService()).exists(id)) {
				throw new NotFoundException();
			}
			
			((IDBACLService)this.getAclService()).deleteById(id);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public void deleteAcl(Acl acl) throws ServiceException{
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.ACL vo = AclConverter.toVO(acl);
			this.getAclService().delete(vo);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public boolean existsAcl(String ruolo, IdUtenza idUtenza, Servizio servizio) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IdAcl id = new IdAcl();
			id.setRuolo(ruolo);
			id.setIdUtenza(idUtenza);
			id.setServizio(servizio.getCodifica());
			
			return this.getAclService().exists(id);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
}
