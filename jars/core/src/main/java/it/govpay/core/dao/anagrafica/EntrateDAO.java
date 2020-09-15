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
package it.govpay.core.dao.anagrafica;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.TipiTributoBD;
import it.govpay.bd.anagrafica.filters.TipoTributoFilter;
import it.govpay.core.dao.anagrafica.dto.FindEntrateDTO;
import it.govpay.core.dao.anagrafica.dto.FindEntrateDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetEntrataDTO;
import it.govpay.core.dao.anagrafica.dto.GetEntrataDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutEntrataDTO;
import it.govpay.core.dao.anagrafica.dto.PutEntrataDTOResponse;
import it.govpay.core.dao.anagrafica.exception.TipoTributoNonTrovatoException;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;

public class EntrateDAO extends BaseDAO{
	
	public EntrateDAO() {
		super();
	}

	public EntrateDAO(boolean useCacheData) {
		super(useCacheData);
	}

	public PutEntrataDTOResponse createOrUpdateEntrata(PutEntrataDTO putTipoTributoDTO) throws ServiceException,TipoTributoNonTrovatoException, NotAuthorizedException, NotAuthenticatedException{
		PutEntrataDTOResponse intermediarioDTOResponse = new PutEntrataDTOResponse();
		TipiTributoBD entrateBD = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		try {
			entrateBD = new TipiTributoBD(configWrapper);
			TipoTributoFilter filter = entrateBD.newFilter(false);
			filter.setCodTributo(putTipoTributoDTO.getCodTributo());
			filter.setSearchModeEquals(true);

			// flag creazione o update
			boolean isCreate = entrateBD.count(filter) == 0;
			intermediarioDTOResponse.setCreated(isCreate);
			if(isCreate) {
				entrateBD.insertTipoTributo(putTipoTributoDTO.getTipoTributo());
			} else {
				entrateBD.updateTipoTributo(putTipoTributoDTO.getTipoTributo());
			}
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new TipoTributoNonTrovatoException(e.getMessage());
		} finally {
			if(entrateBD != null)
				entrateBD.closeConnection();
		}
		return intermediarioDTOResponse;
	}

	public FindEntrateDTOResponse findEntrate(FindEntrateDTO findEntrateDTO) throws NotAuthorizedException, ServiceException, NotAuthenticatedException {
		TipiTributoBD entrateBD = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		try {
			entrateBD = new TipiTributoBD(configWrapper);
			TipoTributoFilter filter = null;
			if(findEntrateDTO.isSimpleSearch()) {
				filter = entrateBD.newFilter(true);
				filter.setSimpleSearchString(findEntrateDTO.getSimpleSearch());
			} else {
				filter = entrateBD.newFilter(false);
				filter.setSearchAbilitato(findEntrateDTO.getAbilitato());
			}

			filter.setOffset(findEntrateDTO.getOffset());
			filter.setLimit(findEntrateDTO.getLimit());
			filter.getFilterSortList().addAll(findEntrateDTO.getFieldSortList());

			return new FindEntrateDTOResponse(entrateBD.count(filter), entrateBD.findAll(filter));
		} finally {
			if(entrateBD != null)
				entrateBD.closeConnection();
		}
	}

	public GetEntrataDTOResponse getEntrata(GetEntrataDTO getEntrataDTO) throws NotAuthorizedException, TipoTributoNonTrovatoException, ServiceException, NotAuthenticatedException {
		GetEntrataDTOResponse response = null;
		try {
			BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
			response = new GetEntrataDTOResponse(AnagraficaManager.getTipoTributo(configWrapper, getEntrataDTO.getCodTipoTributo()));
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new TipoTributoNonTrovatoException("Entrata " + getEntrataDTO.getCodTipoTributo() + " non censita in Anagrafica");
		} finally {
		}
		return response;
	}

}