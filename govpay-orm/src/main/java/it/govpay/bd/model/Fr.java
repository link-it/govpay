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
package it.govpay.bd.model;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Rendicontazione;
import it.govpay.bd.pagamento.RendicontazioniBD;
import it.govpay.bd.pagamento.filters.RendicontazioneFilter;

public class Fr extends it.govpay.model.Fr {
	private static final long serialVersionUID = 1L;

	// Business
	private Dominio dominio;
	private Psp psp;
	private List<Rendicontazione> rendicontazioni;
	
	public Dominio getDominio(BasicBD bd) throws ServiceException, NotFoundException {
		if(dominio == null){
			dominio = AnagraficaManager.getDominio(bd, this.getCodDominio());
		}
		return dominio;
	}
	public void setDominio(Dominio dominio) {
		this.dominio = dominio;
	}

	public Psp getPsp(BasicBD bd) throws ServiceException, NotFoundException {
		if(psp == null){
			psp = AnagraficaManager.getPsp(bd, this.getCodPsp());
		}
		return psp;
	}
	public void setPsp(Psp psp) {
		this.psp = psp;
	}
	
	public List<Rendicontazione> getRendicontazioni(BasicBD bd) throws ServiceException {
		if(rendicontazioni == null) {
			RendicontazioniBD rendicontazioniBD = new RendicontazioniBD(bd);
			RendicontazioneFilter newFilter = rendicontazioniBD.newFilter();
			newFilter.setIdFr(getId());
			rendicontazioni = rendicontazioniBD.findAll(newFilter);
		}
		return rendicontazioni;
	}
	
	public void addRendicontazione(Rendicontazione rendicontazione) {
		if(rendicontazioni == null) {
			this.rendicontazioni = new ArrayList<Rendicontazione>();
		}
		this.rendicontazioni.add(rendicontazione);
	}
	
}
