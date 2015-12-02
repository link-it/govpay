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
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.filters.ApplicazioneFilter;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Stazione;
import it.govpay.dars.model.ApplicazioneExt;
import it.govpay.dars.model.ListaApplicazioniEntry;
import it.govpay.dars.model.ListaTributiEntry;

public class ApplicazioniBD extends BasicBD {

	private it.govpay.bd.anagrafica.ApplicazioniBD applicazioniBD = null;
	private TributiBD tributiBD = null;
	
	public ApplicazioniBD(BasicBD basicBD) {
		super(basicBD);
		this.applicazioniBD = new it.govpay.bd.anagrafica.ApplicazioniBD(basicBD);
		this.tributiBD = new TributiBD(basicBD);
	}
	
	public List<ListaApplicazioniEntry> findAll(ApplicazioneFilter filter) throws ServiceException {
		
		try {
			List<it.govpay.bd.model.Applicazione> lstapplicazione = this.applicazioniBD.findAll(filter);
			List<ListaApplicazioniEntry> applicazioniLst = new ArrayList<ListaApplicazioniEntry>();
			
			for(it.govpay.bd.model.Applicazione applicazioneDTO: lstapplicazione) {
				ListaApplicazioniEntry entry = new ListaApplicazioniEntry();
				entry.setId(applicazioneDTO.getId());
				entry.setAbilitato(applicazioneDTO.isAbilitato());
				entry.setCodApplicazione(applicazioneDTO.getCodApplicazione());
				
				Stazione stazione = AnagraficaManager.getStazione(this, applicazioneDTO.getIdStazione());
				
				entry.setStazione(stazione.getCodStazione());
				applicazioniLst.add(entry);
			}
			return applicazioniLst;
		}    catch (MultipleResultException e) {
			throw new ServiceException(e);
		}  catch (NotFoundException e) {
			throw new ServiceException(e);
		}
	}

	public ApplicazioneExt getApplicazione(long idApplicazione) throws NotFoundException, MultipleResultException, ServiceException {
		Applicazione applicazione = this.applicazioniBD.getApplicazione(idApplicazione);
		
		ApplicazioneExt applicazioneExt = new ApplicazioneExt();
		
		applicazioneExt.setApplicazione(applicazione);
		
		applicazioneExt.setStazione(AnagraficaManager.getStazione(this.tributiBD, applicazione.getIdStazione()));
		
		
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
		Applicazione applicazione = this.applicazioniBD.getApplicazione(idApplicazione);
		ListaApplicazioniEntry entry = new ListaApplicazioniEntry();
		
		
		entry.setId(applicazione.getId());
		entry.setAbilitato(applicazione.isAbilitato());
		entry.setCodApplicazione(applicazione.getCodApplicazione());
		
		Stazione stazione = AnagraficaManager.getStazione(this.tributiBD, applicazione.getIdStazione());
		
		entry.setStazione(stazione.getCodStazione());
		
		return entry;
	}
	
	public void updateApplicazione(ApplicazioneExt applicazioneExt) throws NotFoundException, ServiceException {
		this.applicazioniBD.updateApplicazione(applicazioneExt.getApplicazione());
	}
	
	public void insertApplicazione(ApplicazioneExt applicazioneExt) throws ServiceException{
		this.applicazioniBD.insertApplicazione(applicazioneExt.getApplicazione()); 
	}
	
	public ApplicazioneFilter newFilter() throws ServiceException {
		return this.applicazioniBD.newFilter();
	}

}
