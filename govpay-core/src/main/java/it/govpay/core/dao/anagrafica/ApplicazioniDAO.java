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

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.exception.ValidationException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.AnagraficaManagerNoCache;
import it.govpay.bd.anagrafica.ApplicazioniBD;
import it.govpay.bd.anagrafica.filters.ApplicazioneFilter;
import it.govpay.bd.model.Applicazione;
import it.govpay.core.dao.anagrafica.dto.FindApplicazioniDTO;
import it.govpay.core.dao.anagrafica.dto.FindApplicazioniDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetApplicazioneDTO;
import it.govpay.core.dao.anagrafica.dto.GetApplicazioneDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutApplicazioneDTO;
import it.govpay.core.dao.anagrafica.dto.PutApplicazioneDTOResponse;
import it.govpay.core.dao.anagrafica.exception.ApplicazioneNonTrovataException;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.pagamenti.dto.ApplicazionePatchDTO;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.rs.v1.beans.base.PatchOp;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;

public class ApplicazioniDAO extends BaseDAO {
	
	public ApplicazioniDAO() {
		super();
	}
	
	public ApplicazioniDAO(boolean useCacheData) {
		super(useCacheData);
	}

	public FindApplicazioniDTOResponse findApplicazioni(FindApplicazioniDTO listaApplicazioniDTO) throws NotAuthorizedException, ServiceException, NotAuthenticatedException {
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			this.autorizzaRichiesta(listaApplicazioniDTO.getUser(), Servizio.ANAGRAFICA_APPLICAZIONI, Diritti.LETTURA,bd);

			ApplicazioniBD applicazioniBD = new ApplicazioniBD(bd);
			ApplicazioneFilter filter = null;
			if(listaApplicazioniDTO.isSimpleSearch()) {
				filter = applicazioniBD.newFilter(true);
				filter.setSimpleSearchString(listaApplicazioniDTO.getSimpleSearch());
			} else {
				filter = applicazioniBD.newFilter(false);
				filter.setSearchAbilitato(listaApplicazioniDTO.getAbilitato());
			}
			filter.setOffset(listaApplicazioniDTO.getOffset());
			filter.setLimit(listaApplicazioniDTO.getLimit());
			filter.getFilterSortList().addAll(listaApplicazioniDTO.getFieldSortList());

			return new FindApplicazioniDTOResponse(applicazioniBD.count(filter), applicazioniBD.findAll(filter));

		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public GetApplicazioneDTOResponse getApplicazione(GetApplicazioneDTO getApplicazioneDTO) throws NotAuthorizedException, ApplicazioneNonTrovataException, ServiceException, NotAuthenticatedException {
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			this.autorizzaRichiesta(getApplicazioneDTO.getUser(), Servizio.ANAGRAFICA_APPLICAZIONI, Diritti.LETTURA,bd);
			return new GetApplicazioneDTOResponse(this.useCacheData ? AnagraficaManager.getApplicazione(bd, getApplicazioneDTO.getCodApplicazione()) : AnagraficaManagerNoCache.getApplicazione(bd, getApplicazioneDTO.getCodApplicazione()));
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new ApplicazioneNonTrovataException("Applicazione " + getApplicazioneDTO.getCodApplicazione() + " non censita in Anagrafica");
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}


	public PutApplicazioneDTOResponse createOrUpdate(PutApplicazioneDTO putApplicazioneDTO) throws ServiceException,
	ApplicazioneNonTrovataException, NotAuthorizedException, NotAuthenticatedException { 
		PutApplicazioneDTOResponse applicazioneDTOResponse = new PutApplicazioneDTOResponse();
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			this.autorizzaRichiesta(putApplicazioneDTO.getUser(), Servizio.ANAGRAFICA_APPLICAZIONI, Diritti.SCRITTURA,bd);

			ApplicazioniBD applicazioniBD = new ApplicazioniBD(bd);
			ApplicazioneFilter filter = applicazioniBD.newFilter(false);
			filter.setCodApplicazione(putApplicazioneDTO.getIdApplicazione());

			if(putApplicazioneDTO.getIdDomini() != null) {
				List<Long> idDomini = new ArrayList<>();
				for (String codDominio : putApplicazioneDTO.getIdDomini()) {
					idDomini.add(AnagraficaManager.getDominio(bd, codDominio).getId());
				}

				putApplicazioneDTO.getApplicazione().getUtenza().setIdDomini(idDomini );
			}

			if(putApplicazioneDTO.getIdTributi() != null) {
				List<Long> idTributi = new ArrayList<>();
				for (String codTributo : putApplicazioneDTO.getIdTributi()) {
					idTributi.add(AnagraficaManager.getTipoTributo(bd, codTributo).getId());
				}

				putApplicazioneDTO.getApplicazione().getUtenza().setIdTributi(idTributi);
			}


			// flag creazione o update
			boolean isCreate = applicazioniBD.count(filter) == 0;
			applicazioneDTOResponse.setCreated(isCreate);
			if(isCreate) {
				applicazioniBD.insertApplicazione(putApplicazioneDTO.getApplicazione());
			} else {
				applicazioniBD.updateApplicazione(putApplicazioneDTO.getApplicazione());
			}
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new ApplicazioneNonTrovataException(e.getMessage());
		} finally {
			if(bd != null) 
				bd.closeConnection();
		}
		return applicazioneDTOResponse;
	}

	public GetApplicazioneDTOResponse patch(ApplicazionePatchDTO patchDTO) throws ServiceException,ApplicazioneNonTrovataException, NotAuthorizedException, NotAuthenticatedException, ValidationException{
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			ApplicazioniBD applicazioniBD = new ApplicazioniBD(bd);

			this.autorizzaRichiesta(patchDTO.getUser(), Servizio.ANAGRAFICA_APPLICAZIONI, Diritti.SCRITTURA,bd);

			Applicazione applicazione = applicazioniBD.getApplicazione(patchDTO.getCodApplicazione());
			
			GetApplicazioneDTOResponse getApplicazioneDTOResponse = new GetApplicazioneDTOResponse(applicazione);

			for(PatchOp op: patchDTO.getOp()) {
				UtenzaPatchUtils.patchUtenza(op, getApplicazioneDTOResponse.getApplicazione().getUtenza(), bd);
			}

			applicazioniBD.updateApplicazione(getApplicazioneDTOResponse.getApplicazione());
			
			AnagraficaManager.removeFromCache(getApplicazioneDTOResponse.getApplicazione());
			AnagraficaManager.removeFromCache(getApplicazioneDTOResponse.getApplicazione().getUtenza()); 
			
			applicazione = applicazioniBD.getApplicazione(patchDTO.getCodApplicazione());
			getApplicazioneDTOResponse.setApplicazione(applicazione);

			return getApplicazioneDTOResponse;
		}catch(NotFoundException | MultipleResultException e) {
			throw new ApplicazioneNonTrovataException("Non esiste un'applicazione associata all'ID ["+patchDTO.getCodApplicazione()+"]");
		}finally {
			if(bd != null)
				bd.closeConnection();
		}

	}


}