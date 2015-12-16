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
import it.govpay.bd.anagrafica.filters.EnteFilter;
import it.govpay.bd.model.Anagrafica;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Ente;
import it.govpay.dars.model.EnteExt;
import it.govpay.dars.model.ListaDominiEntry;
import it.govpay.dars.model.ListaEntiEntry;

public class EntiBD extends BasicBD{
	
	private it.govpay.bd.anagrafica.EntiBD entiBD = null;
	private it.govpay.dars.bd.DominiBD dominiBD  = null;

	public EntiBD(BasicBD basicBD) {
		super(basicBD);
		this.entiBD = new it.govpay.bd.anagrafica.EntiBD(basicBD);
		this.dominiBD = new it.govpay.dars.bd.DominiBD(basicBD);
	}
	
	public List<ListaEntiEntry> findAll(EnteFilter filter) throws ServiceException {
		try {
			List<it.govpay.bd.model.Ente> lstEnteDTO = entiBD.findAll(filter); 
			List<ListaEntiEntry> entiLst = new ArrayList<ListaEntiEntry>();
			
			for(it.govpay.bd.model.Ente enteDTO: lstEnteDTO) {
				ListaEntiEntry entry = new ListaEntiEntry();
				entry.setId(enteDTO.getId());
				entry.setAbilitato(enteDTO.isAttivo());
				entry.setCodEnte(enteDTO.getCodEnte());
				
				// Ragione sociale
				Anagrafica anagraficaEnte = enteDTO.getAnagraficaEnte();
				//it.govpay.orm.Anagrafica anagraficaVO = this.getAnagraficaService().get(anagraficaEnte);
				entry.setRagioneSociale(anagraficaEnte.getRagioneSociale());
				
				Dominio dominio = AnagraficaManager.getDominio(this, enteDTO.getIdDominio());
				
				entry.setDominio(dominio.getCodDominio());
				
				entiLst.add(entry);
			}
			return entiLst;
		}  catch (NotFoundException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}
	}

	public Ente getEnte(long idEnte) throws NotFoundException, MultipleResultException, ServiceException {
		return this.entiBD.getEnte(idEnte);
	}
	
	public EnteExt getEnteExt(long idEnte) throws NotFoundException, MultipleResultException, ServiceException {
		Ente ente = this.entiBD.getEnte(idEnte);
		EnteExt enteExt = null;
		
		if(ente != null){
			ListaDominiEntry dominio = this.dominiBD.getListaEntryDominio(ente.getIdDominio());
			enteExt = new EnteExt(ente, dominio);
		}
		
		return enteExt;
	}
	
	public void updateEnte(Ente ente) throws NotFoundException, ServiceException {
		this.entiBD.updateEnte(ente);
	}
	
	public void updateEnteExt(EnteExt ente) throws NotFoundException, ServiceException {
		this.entiBD.updateEnte(ente.getEnte());
	}
	
	public void insertEnte(Ente ente) throws ServiceException{
		this.entiBD.insertEnte(ente); 
	}
	
	public void insertEnteExt(EnteExt ente) throws ServiceException{
		this.entiBD.insertEnte(ente.getEnte()); 
	}
	
	public EnteFilter newFilter() throws ServiceException {
		return this.entiBD.newFilter();
	}
	
	public List<Long> getListaIdDominiByIdEnti(List<Long> listaIdEnti)throws ServiceException {
		List<Long> listaIdDomini = null;
		
		// [TODO] ottimizzare query
		
		if(listaIdEnti != null && listaIdEnti.size() > 0){
			listaIdDomini = new ArrayList<Long>();
			
			for (Long idEnte : listaIdEnti) {
				try {
					EnteExt enteExt = this.getEnteExt(idEnte);
					long id = enteExt.getDominio().getId();
					
					if(!listaIdDomini.contains(id))
						listaIdDomini.add(id);
				} catch (NotFoundException e) {
					
				} catch (MultipleResultException e) {
					throw new ServiceException(e);
				}
			}
		}
		
		return listaIdDomini;
	}

	public List<String> getListaCodDominiByIdEnti(List<Long> listaIdEnti)throws ServiceException {
		List<String> listaIdDomini = null;
		
		// [TODO] ottimizzare query
		
		if(listaIdEnti != null && listaIdEnti.size() > 0){
			listaIdDomini = new ArrayList<String>();
			
			for (Long idEnte : listaIdEnti) {
				try {
					EnteExt enteExt = this.getEnteExt(idEnte);
					String id = enteExt.getDominio().getCodDominio();
					
					if(!listaIdDomini.contains(id))
						listaIdDomini.add(id);
				} catch (NotFoundException e) {
					
				} catch (MultipleResultException e) {
					throw new ServiceException(e);
				}
			}
		}
		
		return listaIdDomini;
	}

}
