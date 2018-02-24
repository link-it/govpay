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

//import java.util.Set;
//
//import org.openspcoop2.generic_project.exception.ServiceException;
//
//import it.govpay.bd.BasicBD;
//import it.govpay.bd.anagrafica.AnagraficaManager;
//import it.govpay.bd.anagrafica.IntermediariBD;
//import it.govpay.bd.anagrafica.StazioniBD;
//import it.govpay.bd.anagrafica.filters.IntermediarioFilter;
//import it.govpay.bd.anagrafica.filters.StazioneFilter;
//import it.govpay.core.dao.anagrafica.dto.FindIntermediariDTO;
//import it.govpay.core.dao.anagrafica.dto.FindIntermediariDTOResponse;
//import it.govpay.core.dao.anagrafica.dto.FindStazioniDTO;
//import it.govpay.core.dao.anagrafica.dto.FindStazioniDTOResponse;
//import it.govpay.core.dao.anagrafica.dto.GetIntermediarioDTO;
//import it.govpay.core.dao.anagrafica.dto.GetIntermediarioDTOResponse;
//import it.govpay.core.dao.anagrafica.dto.GetStazioneDTO;
//import it.govpay.core.dao.anagrafica.dto.GetStazioneDTOResponse;
//import it.govpay.core.dao.anagrafica.dto.PutIntermediarioDTO;
//import it.govpay.core.dao.anagrafica.dto.PutIntermediarioDTOResponse;
//import it.govpay.core.dao.anagrafica.exception.IntermediarioNonTrovatoException;
//import it.govpay.core.dao.anagrafica.exception.StazioneNonTrovataException;
//import it.govpay.core.exceptions.NotAuthorizedException;
//import it.govpay.core.exceptions.NotFoundException;
//import it.govpay.core.utils.AclEngine;
//import it.govpay.core.utils.GpThreadLocal;
//import it.govpay.model.Acl.Servizio;
//import it.govpay.model.Intermediario;

public class IntermediariDAO {
	
//	public PutIntermediarioDTOResponse createOrUpdate(PutIntermediarioDTO putIntermediarioDTO) throws ServiceException,IntermediarioNonTrovatoException,StazioneNonTrovataException{
//		PutIntermediarioDTOResponse intermediarioDTOResponse = new PutIntermediarioDTOResponse();
//		BasicBD bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
//		try {
//			IntermediariBD intermediariBD = new IntermediariBD(bd);
//			IntermediarioFilter filter = intermediariBD.newFilter(false);
//			filter.setCodIntermediario(putIntermediarioDTO.getIdIntermediario());
//			
//			// flag creazione o update
//			boolean isCreate = intermediariBD.count(filter) == 0;
//			intermediarioDTOResponse.setCreated(isCreate);
//			if(isCreate) {
//				intermediariBD.insertIntermediario(putIntermediarioDTO.getIntermediario());
//			} else {
//				intermediariBD.updateIntermediario(putIntermediarioDTO.getIntermediario());
//			}
//		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
//			throw new IntermediarioNonTrovatoException(e.getMessage());
//		} finally {
//			bd.closeConnection();
//		}
//		return intermediarioDTOResponse;
//	}
//
//	public FindIntermediariDTOResponse findIntermediari(FindIntermediariDTO listaIntermediariDTO) throws NotAuthorizedException, ServiceException {
//		BasicBD bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
//		try {
//			Set<Long> intermediari = AclEngine.getIdIntermediariAutorizzati(listaIntermediariDTO.getUser(), Servizio.Anagrafica_PagoPa);
//			
//			if(intermediari != null && intermediari.size() == 0) {
//				throw new NotAuthorizedException("L'utente autenticato non e' autorizzato in lettura ai servizi " + Servizio.Anagrafica_PagoPa + " per alcun intermediario");
//			}
//			
//			IntermediariBD intermediariBD = new IntermediariBD(bd);
//			IntermediarioFilter filter = null;
//			if(listaIntermediariDTO.isSimpleSearch()) {
//				filter = intermediariBD.newFilter(true);
//				filter.setSimpleSearchString(listaIntermediariDTO.getSimpleSearch());
//			} else {
//				filter = intermediariBD.newFilter(false);
//				filter.setCodStazione(listaIntermediariDTO.getCodStazione());
//				filter.setCodIntermediario(listaIntermediariDTO.getCodIntermediario());
//				filter.setRagioneSociale(listaIntermediariDTO.getRagioneSociale());
//				filter.setAbilitato(listaIntermediariDTO.getAbilitato());
//			}
//			filter.setIdIntermediari(intermediari);
//			filter.setOffset(listaIntermediariDTO.getOffset());
//			filter.setLimit(listaIntermediariDTO.getLimit());
//			filter.getFilterSortList().addAll(listaIntermediariDTO.getFieldSortList());
//			
//			return new FindIntermediariDTOResponse(intermediariBD.count(filter), intermediariBD.findAll(filter));
//			
//		} finally {
//			bd.closeConnection();
//		}
//	}
//	
//	public GetIntermediarioDTOResponse getIntermediario(GetIntermediarioDTO getIntermediarioDTO) throws NotAuthorizedException, NotFoundException, ServiceException {
//		BasicBD bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
//		try {
//			Set<String> intermediari = AclEngine.getIntermediariAutorizzati(getIntermediarioDTO.getUser(), Servizio.Anagrafica_PagoPa);
//			
//			if(intermediari != null && !intermediari.contains(getIntermediarioDTO.getCodIntermediario())) {
//				throw new NotAuthorizedException("L'utente autenticato non e' autorizzato in lettura ai servizi " + Servizio.Anagrafica_PagoPa + " per il intermediario " + getIntermediarioDTO.getCodIntermediario());
//			}
//			
//			GetIntermediarioDTOResponse response = new GetIntermediarioDTOResponse(AnagraficaManager.getIntermediario(bd, getIntermediarioDTO.getCodIntermediario()));
//			return response;
//		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
//			throw new NotFoundException("Intermediario " + getIntermediarioDTO.getCodIntermediario() + " non censito in Anagrafica");
//		} finally {
//			bd.closeConnection();
//		}
//	}
//	
//	
//	public FindStazioniDTOResponse findStazioni(FindStazioniDTO findStazioniDTO) throws NotAuthorizedException, NotFoundException, ServiceException {
//		BasicBD bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
//		try {
//			Set<Long> intermediari = AclEngine.getIdIntermediariAutorizzati(findStazioniDTO.getUser(), Servizio.Anagrafica_PagoPa);
//			
//			if(intermediari != null && intermediari.size() == 0) {
//				throw new NotAuthorizedException("L'utente autenticato non e' autorizzato in lettura ai servizi " + Servizio.Anagrafica_PagoPa + " per alcun intermediario");
//			}
//			
//			StazioniBD stazioneBD = new StazioniBD(bd);
//			StazioneFilter filter = null;
//			if(findStazioniDTO.isSimpleSearch()) {
//				filter = stazioneBD.newFilter(true);
//				filter.setSimpleSearchString(findStazioniDTO.getSimpleSearch());
//			} else {
//				filter = stazioneBD.newFilter(false);
//				filter.setCodStazione(findStazioniDTO.getCodStazione());
//				filter.setDescrizione(findStazioniDTO.getDescrizione());
//				filter.setSearchAbilitato(findStazioniDTO.getAbilitato());
//			}
//			try {
//				filter.setIdIntermediario(AnagraficaManager.getIntermediario(bd, findStazioniDTO.getCodIntermediario()).getId());
//			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
//				throw new NotFoundException("Intermediario " + findStazioniDTO.getCodIntermediario() + " non censito in Anagrafica");
//			}
//			filter.setOffset(findStazioniDTO.getOffset());
//			filter.setLimit(findStazioniDTO.getLimit());
//			filter.getFilterSortList().addAll(findStazioniDTO.getFieldSortList());
//
//			return new FindStazioniDTOResponse(stazioneBD.count(filter), stazioneBD.findAll(filter));
//		} finally {
//			bd.closeConnection();
//		}
//	}
//	
//	public GetStazioneDTOResponse getStazione(GetStazioneDTO getStazioneDTO) throws NotAuthorizedException, NotFoundException, ServiceException {
//		BasicBD bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
//		try {
//			Set<String> intermediari = AclEngine.getIntermediariAutorizzati(getStazioneDTO.getUser(), Servizio.Anagrafica_PagoPa);
//			
//			if(intermediari != null && !intermediari.contains(getStazioneDTO.getCodIntermediario())) {
//				throw new NotAuthorizedException("L'utente autenticato non e' autorizzato in lettura ai servizi " + Servizio.Anagrafica_PagoPa + " per il intermediario " + getStazioneDTO.getCodIntermediario());
//			}
//			
//			Intermediario intermediario = null;
//			try {
//				intermediario = AnagraficaManager.getIntermediario(bd, getStazioneDTO.getCodIntermediario());
//			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
//				throw new NotFoundException("Intermediario " + getStazioneDTO.getCodIntermediario() + " non censito in Anagrafica");
//			}
//			GetStazioneDTOResponse response = new GetStazioneDTOResponse(AnagraficaManager.getStazione(bd, intermediario.getId(), getStazioneDTO.getCodStazione()));
//			return response;
//		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
//			throw new NotFoundException("Stazione " + getStazioneDTO.getCodStazione() + " non censito in Anagrafica per il intermediario " + getStazioneDTO.getCodIntermediario());
//		} finally {
//			bd.closeConnection();
//		}
//	}
	
}