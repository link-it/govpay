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

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.filters.AclFilter;
import it.govpay.bd.model.converter.AclConverter;
import it.govpay.model.Acl;
import it.govpay.orm.IdAcl;
import it.govpay.orm.dao.jdbc.JDBCACLServiceSearch;

public class AclBD extends BasicBD {

	public AclBD(BasicBD basicBD) {
		super(basicBD);
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
			it.govpay.orm.ACL applicazioneVO = ((JDBCACLServiceSearch)this.getAclService()).get(id);
			Acl applicazione = AclConverter.toDTO(applicazioneVO);

			return applicazione;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	public long count(AclFilter filter) throws ServiceException {
		try {
			return this.getAclService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<Acl> findAll(AclFilter filter) throws ServiceException {
		try {

			List<Acl> dtoList = new ArrayList<Acl>();
			for(it.govpay.orm.ACL vo: this.getAclService().findAll(filter.toPaginatedExpression())) {
				dtoList.add(AclConverter.toDTO(vo));
			}
			return dtoList;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	
	/**
	 * Aggiorna l'applicazione con i dati forniti
	 * @param ente
	 * @throws NotFoundException se non esiste
	 * @throws ServiceException in caso di errore DB.
	 */
	public void updateAcl(Acl applicazione) throws NotFoundException, ServiceException {
		try {
			it.govpay.orm.ACL vo = AclConverter.toVO(applicazione);
			IdAcl id = this.getAclService().convertToId(vo);

			if(!this.getAclService().exists(id)) {
				throw new NotFoundException("Acl con id ["+id+"] non esiste.");
			}

			this.getAclService().update(id, vo);
			applicazione.setId(vo.getId());
			emitAudit(applicazione);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Crea una nuova applicazione con i dati forniti
	 * @param applicazione
	 * @throws ServiceException in caso di errore DB.
	 */
	public void insertAcl(Acl applicazione) throws ServiceException{
		try {
			it.govpay.orm.ACL vo = AclConverter.toVO(applicazione);
			this.getAclService().create(vo);
			applicazione.setId(vo.getId());
			emitAudit(applicazione);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} 
	}
	
}
