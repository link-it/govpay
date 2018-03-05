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

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.OperatoriBD;
import it.govpay.bd.anagrafica.filters.OperatoreFilter;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Operatore;
import it.govpay.core.dao.anagrafica.dto.FindOperatoriDTO;
import it.govpay.core.dao.anagrafica.dto.FindOperatoriDTOResponse;
import it.govpay.core.dao.anagrafica.dto.LeggiOperatoreDTO;
import it.govpay.core.dao.anagrafica.dto.LeggiOperatoreDTOResponse;
import it.govpay.core.dao.anagrafica.exception.OperatoreNonTrovatoException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.IAutorizzato;


public class UtentiDAO {

	public enum TipoUtenza {
		PORTALE, OPERATORE, APPLICAZIONE;
	}

	public IAutorizzato getUser(String principal) throws NotAuthenticatedException, ServiceException {
		BasicBD bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
		try {
			IAutorizzato user = null;
			boolean autenticated = false;

			try {
				Applicazione applicazione = AnagraficaManager.getApplicazioneByPrincipal(bd, principal);
				user = applicazione.getUtenza();
				autenticated = true;
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) { }

			try {
				Operatore operatore = AnagraficaManager.getOperatore(bd, principal);
				user = operatore.getUtenza();
				autenticated = true;
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) { }
			
			if(!autenticated) throw new NotAuthenticatedException();
			
			return user;
		} finally {
			bd.closeConnection();
		}
	}

	public void populateUser(IAutorizzato user) throws NotAuthenticatedException, ServiceException {
		BasicBD bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
		try {
			try {
				Applicazione applicazione = AnagraficaManager.getApplicazioneByPrincipal(bd, user.getPrincipal());
				user = applicazione.getUtenza();
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) { 
				try {
					Operatore operatore = AnagraficaManager.getOperatore(bd, user.getPrincipal());
					user = operatore.getUtenza();
				} catch (org.openspcoop2.generic_project.exception.NotFoundException ex) {
					throw new NotAuthenticatedException();					
				}
				
			}

			
		} finally {
			bd.closeConnection();
		}
	}
	
	public Applicazione getApplicazione(String principal) throws NotAuthenticatedException, ServiceException {
		BasicBD bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
		try {
			Applicazione applicazione = AnagraficaManager.getApplicazioneByPrincipal(bd, principal);
			return applicazione;
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new NotAuthenticatedException();
		} finally {
			bd.closeConnection();
		}
	}
	
	public Operatore getOperatore(String principal) throws NotAuthenticatedException, ServiceException {
		BasicBD bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
		try {
			Operatore operatore = AnagraficaManager.getOperatore(bd, principal);
			return operatore;
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e3) {
			throw new NotAuthenticatedException();
		} finally {
			bd.closeConnection();
		}
	}
	
	public LeggiOperatoreDTOResponse getOperatore(LeggiOperatoreDTO leggiOperatore) throws NotAuthenticatedException, ServiceException, OperatoreNonTrovatoException {
		BasicBD bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
		try {
			Operatore operatore = AnagraficaManager.getOperatore(bd, leggiOperatore.getPrincipal());
			LeggiOperatoreDTOResponse response = new LeggiOperatoreDTOResponse();
			response.setOperatore(operatore);
			return response;
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e3) {
			throw new OperatoreNonTrovatoException("Operatore " + leggiOperatore.getPrincipal() + " non censito in Anagrafica");
		} finally {
			bd.closeConnection();
		}
	}
	
	public FindOperatoriDTOResponse findOperatori(FindOperatoriDTO listaOperatoriDTO) throws NotAuthorizedException, ServiceException {
		BasicBD bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
		try {
//			Set<Long> applicazioni = AclEngine.getIdOperatoriAutorizzati(listaOperatoriDTO.getUser(), Servizio.ANAGRAFICA_PAGOPA);
//			
//			if(applicazioni != null && applicazioni.size() == 0) {
//				throw new NotAuthorizedException("L'utente autenticato non e' autorizzato in lettura ai servizi " + Servizio.ANAGRAFICA_PAGOPA + " per alcun applicazione");
//			}
			
			OperatoriBD applicazioniBD = new OperatoriBD(bd);
			OperatoreFilter filter = null;
			if(listaOperatoriDTO.isSimpleSearch()) {
				filter = applicazioniBD.newFilter(true);
				filter.setSimpleSearchString(listaOperatoriDTO.getSimpleSearch());
			} else {
				filter = applicazioniBD.newFilter(false);
				filter.setSearchAbilitato(listaOperatoriDTO.getAbilitato());
			}
//			filter.setListaIdOperatori(applicazioni.stream().collect(Collectors.toList()));
			filter.setOffset(listaOperatoriDTO.getOffset());
			filter.setLimit(listaOperatoriDTO.getLimit());
			filter.getFilterSortList().addAll(listaOperatoriDTO.getFieldSortList());
			
			return new FindOperatoriDTOResponse(applicazioniBD.count(filter), applicazioniBD.findAll(filter));
			
		} finally {
			bd.closeConnection();
		}
	}
}



