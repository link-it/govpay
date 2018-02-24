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
import it.govpay.bd.anagrafica.ApplicazioniBD;
import it.govpay.bd.anagrafica.filters.ApplicazioneFilter;
import it.govpay.core.dao.anagrafica.dto.FindApplicazioniDTO;
import it.govpay.core.dao.anagrafica.dto.FindApplicazioniDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetApplicazioneDTO;
import it.govpay.core.dao.anagrafica.dto.GetApplicazioneDTOResponse;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.exceptions.NotFoundException;
import it.govpay.core.utils.GpThreadLocal;

public class ApplicazioniDAO {

	public FindApplicazioniDTOResponse findApplicazioni(FindApplicazioniDTO listaApplicazioniDTO) throws NotAuthorizedException, ServiceException {
		BasicBD bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
		try {
//			Set<Long> applicazioni = AclEngine.getIdApplicazioniAutorizzati(listaApplicazioniDTO.getUser(), Servizio.Anagrafica_PagoPa);
//			
//			if(applicazioni != null && applicazioni.size() == 0) {
//				throw new NotAuthorizedException("L'utente autenticato non e' autorizzato in lettura ai servizi " + Servizio.Anagrafica_PagoPa + " per alcun applicazione");
//			}
			
			ApplicazioniBD applicazioniBD = new ApplicazioniBD(bd);
			ApplicazioneFilter filter = null;
			if(listaApplicazioniDTO.isSimpleSearch()) {
				filter = applicazioniBD.newFilter(true);
				filter.setSimpleSearchString(listaApplicazioniDTO.getSimpleSearch());
			} else {
				filter = applicazioniBD.newFilter(false);
				filter.setSearchAbilitato(listaApplicazioniDTO.getAbilitato());
			}
//			filter.setListaIdApplicazioni(applicazioni.stream().collect(Collectors.toList()));
			filter.setOffset(listaApplicazioniDTO.getOffset());
			filter.setLimit(listaApplicazioniDTO.getLimit());
			filter.getFilterSortList().addAll(listaApplicazioniDTO.getFieldSortList());
			
			return new FindApplicazioniDTOResponse(applicazioniBD.count(filter), applicazioniBD.findAll(filter));
			
		} finally {
			bd.closeConnection();
		}
	}
	
	public GetApplicazioneDTOResponse getApplicazione(GetApplicazioneDTO getApplicazioneDTO) throws NotAuthorizedException, NotFoundException, ServiceException {
		BasicBD bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
		try {
//			Set<String> applicazioni = AclEngine.getApplicazioniAutorizzati(getApplicazioneDTO.getUser(), Servizio.Anagrafica_PagoPa);
//			
//			if(applicazioni != null && !applicazioni.contains(getApplicazioneDTO.getCodApplicazione())) {
//				throw new NotAuthorizedException("L'utente autenticato non e' autorizzato in lettura ai servizi " + Servizio.Anagrafica_PagoPa + " per l'applicazione " + getApplicazioneDTO.getCodApplicazione());
//			}
			
			GetApplicazioneDTOResponse response = new GetApplicazioneDTOResponse(AnagraficaManager.getApplicazione(bd, getApplicazioneDTO.getCodApplicazione()));
			return response;
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new NotFoundException("Applicazione " + getApplicazioneDTO.getCodApplicazione() + " non censita in Anagrafica");
		} finally {
			bd.closeConnection();
		}
	}
	
}