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

import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.IntermediariBD;
import it.govpay.bd.anagrafica.StazioniBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.anagrafica.filters.IntermediarioFilter;
import it.govpay.bd.anagrafica.filters.StazioneFilter;
import it.govpay.bd.model.Dominio;
import it.govpay.core.dao.anagrafica.dto.FindIntermediariDTO;
import it.govpay.core.dao.anagrafica.dto.FindIntermediariDTOResponse;
import it.govpay.core.dao.anagrafica.dto.FindStazioniDTO;
import it.govpay.core.dao.anagrafica.dto.FindStazioniDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetIntermediarioDTO;
import it.govpay.core.dao.anagrafica.dto.GetIntermediarioDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetStazioneDTO;
import it.govpay.core.dao.anagrafica.dto.GetStazioneDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutIntermediarioDTO;
import it.govpay.core.dao.anagrafica.dto.PutIntermediarioDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutStazioneDTO;
import it.govpay.core.dao.anagrafica.dto.PutStazioneDTOResponse;
import it.govpay.core.dao.anagrafica.exception.IntermediarioNonTrovatoException;
import it.govpay.core.dao.anagrafica.exception.StazioneNonTrovataException;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;

public class IntermediariDAO extends BaseDAO{

	public IntermediariDAO() {
		super();
	}

	public IntermediariDAO(boolean useCacheData) {
		super(useCacheData);
	}
	
	public PutIntermediarioDTOResponse createOrUpdateIntermediario(PutIntermediarioDTO putIntermediarioDTO) throws ServiceException,IntermediarioNonTrovatoException, NotAuthorizedException, NotAuthenticatedException{
		PutIntermediarioDTOResponse intermediarioDTOResponse = new PutIntermediarioDTOResponse();
		IntermediariBD intermediariBD = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		try {
			intermediariBD = new IntermediariBD(configWrapper);
			IntermediarioFilter filter = intermediariBD.newFilter(false);
			filter.setCodIntermediario(putIntermediarioDTO.getIdIntermediario());

			// flag creazione o update
			boolean isCreate = intermediariBD.count(filter) == 0;
			intermediarioDTOResponse.setCreated(isCreate);
			if(isCreate) {
				intermediariBD.insertIntermediario(putIntermediarioDTO.getIntermediario());
			} else {
				intermediariBD.updateIntermediario(putIntermediarioDTO.getIntermediario());
			}
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new IntermediarioNonTrovatoException(e.getMessage());
		} finally {
			if(intermediariBD != null)
				intermediariBD.closeConnection();
		}
		return intermediarioDTOResponse;
	}

	public PutStazioneDTOResponse createOrUpdateStazione(PutStazioneDTO putStazioneDTO) throws ServiceException,IntermediarioNonTrovatoException, NotAuthorizedException, NotAuthenticatedException{
		PutStazioneDTOResponse stazioneDTOResponse = new PutStazioneDTOResponse();
		StazioniBD stazioniBD = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		try {
			try {
				// inserisco l'iddominio
				putStazioneDTO.getStazione().setIdIntermediario(AnagraficaManager.getIntermediario(configWrapper, putStazioneDTO.getIdIntermediario()).getId());
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
				throw new IntermediarioNonTrovatoException(e.getMessage());
			}

			stazioniBD = new StazioniBD(configWrapper);
			StazioneFilter filter = stazioniBD.newFilter(false);
			filter.setCodIntermediario(putStazioneDTO.getIdIntermediario());
			filter.setCodStazione(putStazioneDTO.getIdStazione());

			// flag creazione o update
			boolean isCreate = stazioniBD.count(filter) == 0;
			stazioneDTOResponse.setCreated(isCreate);
			if(isCreate) {
				stazioniBD.insertStazione(putStazioneDTO.getStazione());
			} else {
				stazioniBD.updateStazione(putStazioneDTO.getStazione());
			}
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new IntermediarioNonTrovatoException(e.getMessage());
		} finally {
			if(stazioniBD != null)
				stazioniBD.closeConnection();
		}
		return stazioneDTOResponse;
	}

	public FindIntermediariDTOResponse findIntermediari(FindIntermediariDTO listaIntermediariDTO) throws NotAuthorizedException, ServiceException, NotAuthenticatedException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		IntermediariBD intermediariBD = null;
		try {
			intermediariBD = new IntermediariBD(configWrapper);
			IntermediarioFilter filter = null;
			if(listaIntermediariDTO.isSimpleSearch()) {
				filter = intermediariBD.newFilter(true);
				filter.setSimpleSearchString(listaIntermediariDTO.getSimpleSearch());
			} else {
				filter = intermediariBD.newFilter(false);
				filter.setIdIntermediario(listaIntermediariDTO.getCodIntermediario());
			}
			filter.setOffset(listaIntermediariDTO.getOffset());
			filter.setLimit(listaIntermediariDTO.getLimit());
			filter.getFilterSortList().addAll(listaIntermediariDTO.getFieldSortList());

			return new FindIntermediariDTOResponse(intermediariBD.count(filter), intermediariBD.findAll(filter));

		} finally {
			if(intermediariBD != null)
				intermediariBD.closeConnection();
		}
	}

	public GetIntermediarioDTOResponse getIntermediario(GetIntermediarioDTO getIntermediarioDTO) throws NotAuthorizedException, IntermediarioNonTrovatoException, ServiceException, NotAuthenticatedException {
		try {
			BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
			GetIntermediarioDTOResponse response = new GetIntermediarioDTOResponse(AnagraficaManager.getIntermediario(configWrapper, getIntermediarioDTO.getCodIntermediario()));
			return response;
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new IntermediarioNonTrovatoException("Intermediario " + getIntermediarioDTO.getCodIntermediario() + " non censito in Anagrafica");
		} finally {
		}
	}


	public FindStazioniDTOResponse findStazioni(FindStazioniDTO findStazioniDTO) throws NotAuthorizedException, ServiceException, NotAuthenticatedException {
		StazioniBD stazioneBD = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		
		try {
			stazioneBD = new StazioniBD(configWrapper);
			StazioneFilter filter = null;
			if(findStazioniDTO.isSimpleSearch()) {
				filter = stazioneBD.newFilter(true);
				filter.setSimpleSearchString(findStazioniDTO.getSimpleSearch());
			} else {
				filter = stazioneBD.newFilter(false);
				filter.setCodIntermediario(findStazioniDTO.getCodIntermediario());
				filter.setSearchAbilitato(findStazioniDTO.getAbilitato());
			}

			filter.setOffset(findStazioniDTO.getOffset());
			filter.setLimit(findStazioniDTO.getLimit());
			filter.getFilterSortList().addAll(findStazioniDTO.getFieldSortList());

			return new FindStazioniDTOResponse(stazioneBD.count(filter), stazioneBD.findAll(filter));
		} finally {
			if(stazioneBD != null)
				stazioneBD.closeConnection();
		}
	}

	public GetStazioneDTOResponse getStazione(GetStazioneDTO getStazioneDTO) throws NotAuthorizedException, IntermediarioNonTrovatoException, StazioneNonTrovataException, ServiceException, NotAuthenticatedException { 
		try {
			BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
			try {
				AnagraficaManager.getIntermediario(configWrapper, getStazioneDTO.getCodIntermediario());
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
				throw new IntermediarioNonTrovatoException("Intermediario " + getStazioneDTO.getCodIntermediario() + " non censito in Anagrafica");
			}
			GetStazioneDTOResponse response = new GetStazioneDTOResponse(AnagraficaManager.getStazione(configWrapper, getStazioneDTO.getCodStazione())); 
			
			DominiBD dominiBD = new DominiBD(ContextThreadLocal.get().getTransactionId());
			DominioFilter dominioFilter = dominiBD.newFilter();
			dominioFilter.setCodStazione(getStazioneDTO.getCodStazione());
			List<Dominio> findAll = dominiBD.findAll(dominioFilter);
			response.setDomini(findAll);
			
			return response;
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new StazioneNonTrovataException("Stazione " + getStazioneDTO.getCodStazione() + " non censita in Anagrafica per l'intermediario " + getStazioneDTO.getCodIntermediario());
		} 
	}

}