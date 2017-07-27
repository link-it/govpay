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
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Operatore;
import it.govpay.core.business.model.LeggiDominioDTO;
import it.govpay.core.business.model.LeggiDominioDTOResponse;
import it.govpay.core.business.model.ListaDominiDTO;
import it.govpay.core.business.model.ListaDominiDTOResponse;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.exceptions.NotFoundException;
import it.govpay.core.utils.AclEngine;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Applicazione;


public class Domini extends BasicBD {

	public Domini(BasicBD basicBD) {
		super(basicBD);
	}

	public ListaDominiDTOResponse listaDomini(ListaDominiDTO listaDominiDTO) throws NotAuthorizedException, ServiceException {
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
		ListaDominiDTOResponse response = new ListaDominiDTOResponse();
		response.setDomini(dominiBD.findAll(listaDominiDTO.getFilter()));
		response.setTotalCount(dominiBD.count(listaDominiDTO.getFilter()));
		return response;
	}
	
	public LeggiDominioDTOResponse leggiDominio(LeggiDominioDTO leggiDominioDTO) throws NotAuthorizedException, NotFoundException, ServiceException {
		DominiBD dominiBD = new DominiBD(this);
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
			LeggiDominioDTOResponse response = new LeggiDominioDTOResponse();
			Dominio dominio = dominiBD.getDominio(leggiDominioDTO.getCodDominio());
			response.setDominio(dominio);
			return response;
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new NotFoundException("Dominio " + leggiDominioDTO.getCodDominio() + " non censito in Anagrafica");
		}
	}
}


