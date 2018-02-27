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

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;

import it.govpay.bd.BasicBD;
import it.govpay.model.Acl;

public class AclBD extends BasicBD {

	public AclBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	/*
	 * OPERATORE
	 */

	public void insertAclOperatore(Long id, List<Acl> acls) throws ServiceException {
	}
	
	public List<Acl> getAclOperatore(long idOperatore) throws ServiceException, NotFoundException {
		return null;
	}

	public void deleteAclOperatore(long idOperatore) throws ServiceException {
	}
	
	/*
	 * APPLICAZIONE
	 */
	
	public List<Acl> getAclApplicazione(long idApplicazione) throws ServiceException, NotFoundException {
		return null;
	}
	
	public List<Acl> getAclPortale(long idPortale) throws ServiceException, NotFoundException {
		return null;
	}
	
	public void insertAclApplicazione(Long id, List<Acl> acls) throws ServiceException {
	}
	
	public void deleteAclApplicazione(long idApplicazione) throws ServiceException {
	}

	
	/*
	 * RUOLO
	 */

	public void insertAclRuolo(Long id, List<Acl> acls) throws ServiceException {
	}
	
	public List<Acl> getAclRuolo(long idRuolo) throws ServiceException, NotFoundException {
		return null;
	}

	public void deleteAclRuolo(long idRuolo) throws ServiceException {
	}
}
