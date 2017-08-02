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
package it.govpay.core.business;

import java.util.Set;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.UnitaOperativeBD;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.core.business.model.GetDominioDTO;
import it.govpay.core.business.model.GetDominioDTOResponse;
import it.govpay.core.business.model.FindDominiDTO;
import it.govpay.core.business.model.FindDominiDTOResponse;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.exceptions.NotFoundException;
import it.govpay.core.utils.AclEngine;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Applicazione;


public class Domini extends BasicBD {

	public Domini(BasicBD basicBD) {
		super(basicBD);
	}

	public FindDominiDTOResponse findDomini(FindDominiDTO listaDominiDTO) throws NotAuthorizedException, ServiceException {
		Set<Long> domini = null;
		try {
			Applicazione applicazione = AnagraficaManager.getApplicazioneByPrincipal(this, listaDominiDTO.getPrincipal());
			domini = AclEngine.getIdDominiAutorizzati(applicazione, Servizio.Anagrafica_PagoPa);
			if(domini != null && domini.size() == 0) {
				throw new NotAuthorizedException();
			}
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			try {
				Operatore operatore = AnagraficaManager.getOperatore(this, listaDominiDTO.getPrincipal());
				domini = AclEngine.getIdDominiAutorizzati(operatore.getRuoli(this), Servizio.Anagrafica_PagoPa, 1);
				if(domini != null && domini.size() == 0) {
					throw new NotAuthorizedException();
				}
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e2) {
				throw new NotAuthorizedException();
			} 
		} 
		DominiBD dominiBD = new DominiBD(this);
		FindDominiDTOResponse response = new FindDominiDTOResponse();
		response.setDomini(dominiBD.findAll(listaDominiDTO.getFilter()));
		response.setTotalCount(dominiBD.count(listaDominiDTO.getFilter()));
		return response;
	}
	
	public GetDominioDTOResponse getDominio(GetDominioDTO leggiDominioDTO) throws NotAuthorizedException, NotFoundException, ServiceException {
		try {
			Set<String> domini = null;
			try {
				Applicazione applicazione = AnagraficaManager.getApplicazioneByPrincipal(this, leggiDominioDTO.getPrincipal());
				domini = AclEngine.getDominiAutorizzati(applicazione, Servizio.Anagrafica_PagoPa);
				if(domini != null && domini.size() == 0) {
					throw new NotAuthorizedException();
				}
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
				try {
					Operatore operatore = AnagraficaManager.getOperatore(this, leggiDominioDTO.getPrincipal());
					domini = AclEngine.getDominiAutorizzati(operatore.getRuoli(this), Servizio.Anagrafica_PagoPa, 1);
					if(domini != null && domini.size() == 0) {
						throw new NotAuthorizedException();
					}
				} catch (org.openspcoop2.generic_project.exception.NotFoundException e2) {
					throw new NotAuthorizedException();
				} 
			} 
			if(domini != null && !domini.contains(leggiDominioDTO.getCodDominio())) {
				throw new NotAuthorizedException();
			}
			GetDominioDTOResponse response = new GetDominioDTOResponse();
			Dominio dominio = AnagraficaManager.getDominio(this, leggiDominioDTO.getCodDominio());
			response.setDominio(dominio);
			return response;
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new NotFoundException("Dominio " + leggiDominioDTO.getCodDominio() + " non censito in Anagrafica");
		}
	}
	
	public GetUnitaOrganizzativaDTOResponse getUnitaOrganizzativa(GetUnitaOrganizzativaDTO leggiUnitaOrganizzativaDTO) throws NotAuthorizedException, NotFoundException, ServiceException {
		try {
			Set<String> domini = null;
			try {
				Applicazione applicazione = AnagraficaManager.getApplicazioneByPrincipal(this, leggiUnitaOrganizzativaDTO.getPrincipal());
				domini = AclEngine.getDominiAutorizzati(applicazione, Servizio.Anagrafica_PagoPa);
				if(domini != null && domini.size() == 0) {
					throw new NotAuthorizedException();
				}
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
				try {
					Operatore operatore = AnagraficaManager.getOperatore(this, leggiUnitaOrganizzativaDTO.getPrincipal());
					domini = AclEngine.getDominiAutorizzati(operatore.getRuoli(this), Servizio.Anagrafica_PagoPa, 1);
					if(domini != null && domini.size() == 0) {
						throw new NotAuthorizedException();
					}
				} catch (org.openspcoop2.generic_project.exception.NotFoundException e2) {
					throw new NotAuthorizedException();
				} 
			} 
			if(domini != null && !domini.contains(leggiUnitaOrganizzativaDTO.getCodDominio())) {
				throw new NotAuthorizedException();
			}
			GetDominioDTOResponse response = new GetDominioDTOResponse();
			Dominio dominio = AnagraficaManager.getDominio(this, leggiUnitaOrganizzativaDTO.getCodDominio());
			UnitaOperativa unitaOperativa = AnagraficaManager.getDominio(this, leggiUnitaOrganizzativaDTO.getCodUnitaOperativa());
			response.setDominio(dominio);
			return response;
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new NotFoundException("Dominio " + leggiUnitaOrganizzativaDTO.getCodDominio() + " non censito in Anagrafica");
		}
	}
	
	public FindUnitaOrganizzativeDTOResponse findUnitaOrganizzative(FindUnitaOrganizzativeDTO listaUnitaOrganizzativeDTO) throws NotAuthorizedException, ServiceException {
		Set<Long> domini = null;
		try {
			Applicazione applicazione = AnagraficaManager.getApplicazioneByPrincipal(this, listaDominiDTO.getPrincipal());
			domini = AclEngine.getIdDominiAutorizzati(applicazione, Servizio.Anagrafica_PagoPa);
			if(domini != null && domini.size() == 0) {
				throw new NotAuthorizedException();
			}
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			try {
				Operatore operatore = AnagraficaManager.getOperatore(this, listaDominiDTO.getPrincipal());
				domini = AclEngine.getIdDominiAutorizzati(operatore.getRuoli(this), Servizio.Anagrafica_PagoPa, 1);
				if(domini != null && domini.size() == 0) {
					throw new NotAuthorizedException();
				}
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e2) {
				throw new NotAuthorizedException();
			} 
		} 
		DominiBD dominiBD = new DominiBD(this);
		FindDominiDTOResponse response = new FindDominiDTOResponse();
		response.setDomini(dominiBD.findAll(listaDominiDTO.getFilter()));
		response.setTotalCount(dominiBD.count(listaDominiDTO.getFilter()));
		return response;
	}
}


