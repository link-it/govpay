/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
package it.govpay.core.dao.anagrafica;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.AclBD;
import it.govpay.bd.anagrafica.filters.AclFilter;
import it.govpay.core.dao.anagrafica.dto.DeleteAclDTO;
import it.govpay.core.dao.anagrafica.dto.ListaAclDTO;
import it.govpay.core.dao.anagrafica.dto.ListaAclDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PostAclDTO;
import it.govpay.core.dao.anagrafica.dto.PostAclDTOResponse;
import it.govpay.core.dao.anagrafica.exception.AclNonTrovatoException;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;

public class AclDAO extends BaseDAO{

	public AclDAO() {
		super();
	}

	public AclDAO(boolean useCacheData) {
		super(useCacheData);
	}

	public ListaAclDTOResponse leggiAclRuoloRegistratoSistema(String ruolo) throws ServiceException {
		ListaAclDTO listaAclDTO = new ListaAclDTO(null);
		listaAclDTO.setForceRuolo(true);
		listaAclDTO.setRuolo(ruolo);
		return this._listaAcl(listaAclDTO);
	}

	public ListaAclDTOResponse leggiAclRuoloRegistrateSistema() throws ServiceException {
		ListaAclDTO listaAclDTO = new ListaAclDTO(null);
		listaAclDTO.setForceRuolo(true);
		return this._listaAcl(listaAclDTO);
	}

	private ListaAclDTOResponse _listaAcl(ListaAclDTO listaAclDTO) throws ServiceException {
		AclBD aclBD = null;
		try {
			aclBD = new AclBD(ContextThreadLocal.get().getTransactionId(), useCacheData);
			AclFilter filter = aclBD.newFilter();

			if(listaAclDTO.getForceRuolo() != null)
				filter.setForceRuolo(listaAclDTO.getForceRuolo());

			if(listaAclDTO.getForcePrincipal() != null)
				filter.setForcePrincipal(listaAclDTO.getForcePrincipal());

			if(listaAclDTO.getPrincipal() != null)
				filter.setPrincipal(listaAclDTO.getPrincipal());

			if(listaAclDTO.getRuolo() != null)
				filter.setRuolo(listaAclDTO.getRuolo());

			if(listaAclDTO.getServizio() != null)
				filter.setServizio(listaAclDTO.getServizio());

			filter.setOffset(listaAclDTO.getOffset());
			filter.setLimit(listaAclDTO.getLimit());
			filter.getFilterSortList().addAll(listaAclDTO.getFieldSortList());

			return new ListaAclDTOResponse(aclBD.count(filter), aclBD.findAll(filter));
		} finally {
			if(aclBD != null) {
				aclBD.closeConnection();
			}
		}
	}

	public ListaAclDTOResponse leggiAclPrincipalRegistrateSistema() throws ServiceException {
		ListaAclDTO listaAclDTO = new ListaAclDTO(null);
		listaAclDTO.setForcePrincipal(true);
		return this._listaAcl(listaAclDTO);
	}

	/**
	 * @param putDominioDTO
	 * @return
	 * @throws NotAuthenticatedException 
	 */
	public PostAclDTOResponse create(PostAclDTO postAclDTO) throws NotAuthorizedException, ServiceException, NotAuthenticatedException { 
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData, postAclDTO.getIdOperatore());
		AclBD aclBD = null;
		try {
			aclBD = new AclBD(configWrapper);
			PostAclDTOResponse leggiAclDTOResponse = new PostAclDTOResponse();
			boolean exists = aclBD.existsAcl(postAclDTO.getAcl().getRuolo(), postAclDTO.getAcl().getIdUtenza(configWrapper), postAclDTO.getAcl().getServizio());
			leggiAclDTOResponse.setCreated(!exists);
			if(exists) {
				try {
					aclBD.updateAcl(postAclDTO.getAcl());
				} catch (NotFoundException e) {
					throw new ServiceException(e);
				}
			} else {
				aclBD.insertAcl(postAclDTO.getAcl());
			}

			leggiAclDTOResponse.setCreated(true);
			return leggiAclDTOResponse;
		} finally {
			if(aclBD != null) {
				aclBD.closeConnection();
			}
		}
	}

	/**
	 * @param deleteAclDTO
	 * @throws NotAuthenticatedException 
	 */
	public void deleteAcl(DeleteAclDTO deleteAclDTO) throws NotAuthorizedException, AclNonTrovatoException, ServiceException, NotAuthenticatedException { 
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData, deleteAclDTO.getIdOperatore());
		AclBD aclBD = null;
		try {
			aclBD = new AclBD(configWrapper);
			aclBD.deleteAcl(deleteAclDTO.getIdAcl());
		} catch (NotFoundException e) {
			throw new AclNonTrovatoException(e.getMessage());
		} finally {
			if(aclBD != null) {
				aclBD.closeConnection();
			}
		}
	}

}
