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
import it.govpay.bd.anagrafica.filters.PortaleFilter;
import it.govpay.bd.model.Portale;
import it.govpay.dars.model.ListaApplicazioniEntry;
import it.govpay.dars.model.ListaPortaliEntry;
import it.govpay.dars.model.PortaleExt;

public class PortaliBD extends BasicBD {

	private it.govpay.dars.bd.ApplicazioniBD applicazioniBD = null;
	private it.govpay.bd.anagrafica.PortaliBD portaliBD = null;

	public PortaliBD(BasicBD basicBD) {
		super(basicBD);
		this.applicazioniBD = new it.govpay.dars.bd.ApplicazioniBD(basicBD);
		this.portaliBD = new it.govpay.bd.anagrafica.PortaliBD(basicBD);
	}

	public List<ListaPortaliEntry> findAll(PortaleFilter filter) throws ServiceException {
		List<it.govpay.bd.model.Portale> lstportale = this.portaliBD.findAll(filter);
		List<ListaPortaliEntry> portaliLst = new ArrayList<ListaPortaliEntry>();

		for(it.govpay.bd.model.Portale portaleDTO: lstportale) {
			ListaPortaliEntry entry = new ListaPortaliEntry();
			entry.setId(portaleDTO.getId());
			entry.setAbilitato(portaleDTO.isAbilitato());
			entry.setCodPortale(portaleDTO.getCodPortale());
			portaliLst.add(entry);
		}
		return portaliLst;
	}

	public PortaleExt getPortale(long idPortale) throws NotFoundException, MultipleResultException, ServiceException {
		Portale portale = this.portaliBD.getPortale(idPortale);

		PortaleExt portaleExt = new PortaleExt();
		portaleExt.setPortale(portale);

		List<ListaApplicazioniEntry> applicazioni = new ArrayList<ListaApplicazioniEntry>();
		if(portale.getIdApplicazioni() != null && portale.getIdApplicazioni().size() >0){
			for (Long idApplicazione : portale.getIdApplicazioni()) {
				ListaApplicazioniEntry entry = this.applicazioniBD.getListaApplicazioniEntry(idApplicazione);
				applicazioni.add(entry);
			}
		}
		portaleExt.setApplicazioni(applicazioni);

		return portaleExt;
	}

	public ListaPortaliEntry getListaPortaliEntry(long idPortale) throws NotFoundException, MultipleResultException, ServiceException {
		Portale portale = this.portaliBD.getPortale(idPortale);
		ListaPortaliEntry entry = new ListaPortaliEntry();
		entry.setId(portale.getId());
		entry.setAbilitato(portale.isAbilitato());
		entry.setCodPortale(portale.getCodPortale());
		return entry;
	}

	public void updatePortale(PortaleExt portaleExt) throws NotFoundException, ServiceException {
		this.portaliBD.updatePortale(portaleExt.getPortale());
	}

	public void insertPortale(PortaleExt portaleExt) throws ServiceException{
		this.portaliBD.insertPortale(portaleExt.getPortale()); 
	}

	public PortaleFilter newFilter() throws ServiceException {
		return this.portaliBD.newFilter();
	}

}
