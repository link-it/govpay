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
import it.govpay.bd.model.converter.PortaleConverter;
import it.govpay.model.Acl;
import it.govpay.model.Portale;
import it.govpay.orm.IdPortale;
import it.govpay.orm.dao.jdbc.JDBCPortaleServiceSearch;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.utils.UtilsException;

import it.govpay.bd.anagrafica.filters.PortaleFilter;

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
	public Portale getPortale(long id) throws NotFoundException, ServiceException, MultipleResultException {
		try {
			it.govpay.orm.Portale portaleVO = ((JDBCPortaleServiceSearch)this.getPortaleService()).get(id);
			AclBD aclBD = new AclBD(this);
			List<Acl> acls = aclBD.getAclPortale(portaleVO.getId());

			Portale ente = PortaleConverter.toDTO(portaleVO, acls);
			return ente;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
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
		try {
			IdPortale id = new IdPortale();
			id.setCodPortale(codPortale);
			it.govpay.orm.Portale portaleVO = this.getPortaleService().get(id);
			AclBD aclBD = new AclBD(this);
			List<Acl> acls = aclBD.getAclPortale(portaleVO.getId());

			Portale ente = PortaleConverter.toDTO(portaleVO, acls);
			return ente;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
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
		try {
			IExpression exp = this.getPortaleService().newExpression();
			exp.equals(it.govpay.orm.Portale.model().PRINCIPAL, principal);
			it.govpay.orm.Portale portaleVO = this.getPortaleService().find(exp);

			AclBD aclBD = new AclBD(this);
			List<Acl> acls = aclBD.getAclPortale(portaleVO.getId());

			Portale ente = PortaleConverter.toDTO(portaleVO, acls);
			return ente;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Aggiorna l'portale con i dati forniti
	 * @param portale
	 * @throws NotFoundException se non esiste
	 * @throws ServiceException in caso di errore DB.
	 */
	public void updatePortale(Portale portale) throws NotFoundException, ServiceException {
		try {

			it.govpay.orm.Portale vo = PortaleConverter.toVO(portale);
			IdPortale idPortale = this.getPortaleService().convertToId(vo);

			if(!this.getPortaleService().exists(idPortale)) {
				throw new NotFoundException("Portale con id ["+idPortale.toJson()+"] non trovato");
			}

			AclBD aclBD = new AclBD(this);
			aclBD.deleteAclPortale(portale.getId());
			aclBD.insertAclPortale(portale.getId(), portale.getAcls());
			this.getPortaleService().update(idPortale, vo);
			portale.setId(vo.getId());
			AnagraficaManager.removeFromCache(portale);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} catch (UtilsException e) {
			throw new ServiceException(e);
		} 

	}

	/**
	 * Crea una nuova portale con i dati forniti
	 * @param portale
	 * @throws ServiceException in caso di errore DB.
	 */
	public void insertPortale(Portale portale) throws ServiceException{
		try {
			it.govpay.orm.Portale vo = PortaleConverter.toVO(portale);
			this.getPortaleService().create(vo);
			portale.setId(vo.getId());

			AclBD aclBD = new AclBD(this);
			aclBD.insertAclPortale(portale.getId(), portale.getAcls());	
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public PortaleFilter newFilter() throws ServiceException {
		return new PortaleFilter(this.getPortaleService());
	}

	public long count(PortaleFilter filter) throws ServiceException {
		try {
			return this.getPortaleService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<Portale> findAll(PortaleFilter filter) throws ServiceException {
		try {
			List<it.govpay.orm.Portale> findAll = this.getPortaleService().findAll(filter.toPaginatedExpression());
			List<Portale> lst = new ArrayList<Portale>();
			
			for (it.govpay.orm.Portale portale : findAll) {
				
				AclBD aclBD = new AclBD(this);
				List<Acl> acls = new ArrayList<Acl>();
				try {
					acls = aclBD.getAclPortale(portale.getId());
				} catch (NotFoundException e) {

				}

				Portale ente = PortaleConverter.toDTO(portale, acls);
				lst.add(ente);
			}
			
			return lst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

}
