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
import it.govpay.bd.model.Acl;
import it.govpay.bd.model.converter.AclConverter;
import it.govpay.orm.ACL;
import it.govpay.orm.IdApplicazione;
import it.govpay.orm.IdOperatore;
import it.govpay.orm.IdPortale;
import it.govpay.orm.dao.jdbc.converter.ACLFieldConverter;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;

public class AclBD extends BasicBD {

	public AclBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	private List<Acl> findAll(IPaginatedExpression exp) throws ServiceException, NotImplementedException, NotFoundException {
		List<ACL> findAll = this.getAclService().findAll(exp);
		List<Acl> findAllDTO = new ArrayList<Acl>(); 
		for(ACL acl : findAll) {
			findAllDTO.add(AclConverter.toDTO(acl, this));
		}
		return findAllDTO;
	}
	
	
	/*
	 * OPERATORE
	 */

	public void insertAclOperatore(Long id, List<Acl> acls) throws ServiceException {
		for(Acl acl: acls) {
			try{
				ACL aclVo = AclConverter.toVO(acl, this);
				IdOperatore idOperatore = new IdOperatore();
				idOperatore.setId(id);
				aclVo.setIdOperatore(idOperatore);
				this.getAclService().create(aclVo);
			} catch(NotImplementedException e) {
				throw new ServiceException(e);
			} catch (NotFoundException e) {
				throw new ServiceException(e);
			}
		}
	}
	
	public List<Acl> getAclOperatore(long idOperatore) throws ServiceException, NotFoundException {
		try{
			IPaginatedExpression exp = this.getAclService().newPaginatedExpression();
			ACLFieldConverter aclFC = new ACLFieldConverter(this.getJdbcProperties().getDatabase());
			exp.equals(new CustomField("id_operatore", Long.class, "id_operatore", aclFC.toAliasTable(ACL.model())), idOperatore);
			return findAll(exp);
		} catch(ExpressionException e) {
			throw new ServiceException(e);
		} catch(ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch(NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public void deleteAclOperatore(long idOperatore) throws ServiceException {
		try {
			IExpression exp = this.getAclService().newExpression();
			ACLFieldConverter aclFC = new ACLFieldConverter(this.getJdbcProperties().getDatabase());
			exp.equals(new CustomField("id_operatore", Long.class, "id_operatore", aclFC.toAliasTable(ACL.model())), idOperatore);
			this.getAclService().deleteAll(exp);
		} catch(ExpressionException e) {
			throw new ServiceException(e);
		} catch(ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch(NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	/*
	 * APPLICAZIONE
	 */
	
	public List<Acl> getAclApplicazione(long idApplicazione) throws ServiceException, NotFoundException {
		try {
			IPaginatedExpression exp = this.getAclService().newPaginatedExpression();
			ACLFieldConverter aclFC = new ACLFieldConverter(this.getJdbcProperties().getDatabase());
			exp.equals(new CustomField("id_applicazione", Long.class, "id_applicazione", aclFC.toAliasTable(ACL.model())), idApplicazione);
			return findAll(exp);
		} catch(ExpressionException e) {
			throw new ServiceException(e);
		} catch(ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch(NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	public List<Acl> getAclPortale(long idPortale) throws ServiceException, NotFoundException {
		try {
			IPaginatedExpression exp = this.getAclService().newPaginatedExpression();
			ACLFieldConverter aclFC = new ACLFieldConverter(this.getJdbcProperties().getDatabase());
			exp.equals(new CustomField("id_portale", Long.class, "id_portale", aclFC.toAliasTable(ACL.model())), idPortale);
			return findAll(exp);
		} catch(ExpressionException e) {
			throw new ServiceException(e);
		} catch(ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch(NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	public void insertAclPortale(Long id, List<Acl> acls) throws ServiceException {
		for(Acl acl: acls) {
			try{
				ACL aclVo = AclConverter.toVO(acl, this);
				IdPortale idPortale = new IdPortale();
				idPortale.setId(id);
				aclVo.setIdPortale(idPortale);
				this.getAclService().create(aclVo);
			} catch(NotImplementedException e) {
				throw new ServiceException(e);
			} catch (NotFoundException e) {
				throw new ServiceException(e);
			}
		}
	}
	
	/*
	 * PORTALE
	 */

	public void deleteAclPortale(long idPortale) throws ServiceException {
		try{
			IExpression exp = this.getAclService().newExpression();
			ACLFieldConverter aclFC = new ACLFieldConverter(this.getJdbcProperties().getDatabase());
			exp.equals(new CustomField("id_portale", Long.class, "id_portale", aclFC.toAliasTable(ACL.model())), idPortale);
			this.getAclService().deleteAll(exp);
		} catch(ExpressionException e) {
			throw new ServiceException(e);
		} catch(ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch(NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	public void insertAclApplicazione(Long id, List<Acl> acls) throws ServiceException {
		for(Acl acl: acls) {
			try{
				ACL aclVo = AclConverter.toVO(acl, this);
				IdApplicazione idApplicazione = new IdApplicazione();
				idApplicazione.setId(id);
				aclVo.setIdApplicazione(idApplicazione);
				this.getAclService().create(aclVo);
			} catch(NotImplementedException e) {
				throw new ServiceException(e);
			} catch (NotFoundException e) {
				throw new ServiceException(e);
			}
		}
	}
	
	public void deleteAclApplicazione(long idApplicazione) throws ServiceException {
		try {
			IExpression exp = this.getAclService().newExpression();
			ACLFieldConverter aclFC = new ACLFieldConverter(this.getJdbcProperties().getDatabase());
			exp.equals(new CustomField("id_applicazione", Long.class, "id_applicazione", aclFC.toAliasTable(ACL.model())), idApplicazione);
			this.getAclService().deleteAll(exp);
		} catch(ExpressionException e) {
			throw new ServiceException(e);
		} catch(ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch(NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	
	
}
