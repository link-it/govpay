/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
package it.govpay.dars.bd;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.filters.ApplicazioneFilter;
import it.govpay.bd.model.Applicazione;
import it.govpay.dars.model.ApplicazioneExt;
import it.govpay.dars.model.ListaApplicazioniEntry;
import it.govpay.dars.model.ListaTributiEntry;

public class ApplicazioniBD extends it.govpay.bd.anagrafica.ApplicazioniBD {

	private TributiBD tributiBD = null;

	public ApplicazioniBD(BasicBD basicBD) {
		super(basicBD);
		this.tributiBD = new TributiBD(basicBD);
	}

	public List<ListaApplicazioniEntry> findAllEntry(ApplicazioneFilter filter) throws ServiceException {
		return this.findAll(filter, null);
	}

	public List<ListaApplicazioniEntry> findAll(ApplicazioneFilter filter,List<Long> idApplicazioni) throws ServiceException {
		if(idApplicazioni != null){
			filter.setListaIdApplicazioni(idApplicazioni);
		}
		List<it.govpay.bd.model.Applicazione> lstapplicazione = super.findAll(filter);
		List<ListaApplicazioniEntry> applicazioniLst = new ArrayList<ListaApplicazioniEntry>();
		for(it.govpay.bd.model.Applicazione applicazioneDTO: lstapplicazione) {
			ListaApplicazioniEntry entry = new ListaApplicazioniEntry();
			entry.setId(applicazioneDTO.getId());
			entry.setAbilitato(applicazioneDTO.isAbilitato());
			entry.setCodApplicazione(applicazioneDTO.getCodApplicazione());
			applicazioniLst.add(entry);
		}
		return applicazioniLst;
	}

	public List<ListaApplicazioniEntry> findAllApplicazioniNonInLista(ApplicazioneFilter filter,List<Long> idApplicazioni) throws ServiceException {

		List<it.govpay.bd.model.Applicazione> lstapplicazione = super.findAll(filter);
		List<ListaApplicazioniEntry> applicazioniLst = new ArrayList<ListaApplicazioniEntry>();

		for(it.govpay.bd.model.Applicazione applicazioneDTO: lstapplicazione) {
			boolean add = false;
			if(idApplicazioni != null && idApplicazioni.size() > 0){
				if(!idApplicazioni.contains(applicazioneDTO.getId())){
					add = true;
				}
			}
			if(add){
				ListaApplicazioniEntry entry = new ListaApplicazioniEntry();
				entry.setId(applicazioneDTO.getId());
				entry.setAbilitato(applicazioneDTO.isAbilitato());
				entry.setCodApplicazione(applicazioneDTO.getCodApplicazione());
				applicazioniLst.add(entry);
			}
		}
		return applicazioniLst;
	}

	public ApplicazioneExt getApplicazioneExt(long idApplicazione) throws NotFoundException, MultipleResultException, ServiceException {
		Applicazione applicazione = super.getApplicazione(idApplicazione);
		ApplicazioneExt applicazioneExt = new ApplicazioneExt();
		applicazioneExt.setApplicazione(applicazione);
		List<ListaTributiEntry> tributi = new ArrayList<ListaTributiEntry>();
		if(applicazione.getIdTributi() != null && applicazione.getIdTributi().size() >0){
			for (Long idTributo : applicazione.getIdTributi()) {
				ListaTributiEntry entry = this.tributiBD.getListaTributiEntry(idTributo);
				tributi.add(entry);
			}
		}
		applicazioneExt.setTributi(tributi);
		return applicazioneExt;
	}

	public ListaApplicazioniEntry getListaApplicazioniEntry(long idApplicazione) throws NotFoundException, MultipleResultException, ServiceException {
		Applicazione applicazione = super.getApplicazione(idApplicazione);
		ListaApplicazioniEntry entry = new ListaApplicazioniEntry();
		entry.setId(applicazione.getId());
		entry.setAbilitato(applicazione.isAbilitato());
		entry.setCodApplicazione(applicazione.getCodApplicazione());
		return entry;
	}

	public void updateApplicazione(ApplicazioneExt applicazioneExt) throws NotFoundException, ServiceException {
		super.updateApplicazione(applicazioneExt.getApplicazione());
	}

	public void insertApplicazione(ApplicazioneExt applicazioneExt) throws ServiceException{
		super.insertApplicazione(applicazioneExt.getApplicazione()); 
	}
}
