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

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.pagamento.StampeBD;
import it.govpay.model.Stampa;

public class Operazione extends it.govpay.model.Operazione{

	// BUSINESS
	private transient Applicazione applicazione;
	private transient Dominio dominio;
	private transient Stampa stampa;
	
	public Dominio getDominio(BDConfigWrapper configWrapper) throws ServiceException, NotFoundException {
		if(this.dominio == null && this.getCodDominio() != null) {
			this.dominio = AnagraficaManager.getDominio(configWrapper, this.getCodDominio());
		} 
		return this.dominio;
	}

	public Applicazione getApplicazione(BDConfigWrapper configWrapper) throws ServiceException {
		if(this.applicazione == null && this.getIdApplicazione() != null) {
			try {
				this.applicazione = AnagraficaManager.getApplicazione(configWrapper, this.getIdApplicazione());
			} catch (NotFoundException e) {
			}
		} 
		return this.applicazione;
	}
	
	public void setApplicazione(String codApplicazione, BDConfigWrapper configWrapper) throws ServiceException, NotFoundException {
		this.applicazione = AnagraficaManager.getApplicazione(configWrapper, codApplicazione);
		this.setIdApplicazione(this.applicazione.getId());
	}
	
	public Stampa getStampa(BasicBD bd) throws ServiceException {
		if(this.stampa == null && this.getIdStampa() != null && bd != null) {
			try {
				StampeBD stampeBD = new StampeBD(bd);
				this.stampa = stampeBD.getStampa(this.getIdStampa());
			} catch (NotFoundException e) {
			}
		} 
		return this.stampa;
	}
	
	public void setStampa(Stampa stampa) throws ServiceException, NotFoundException {
		this.stampa = stampa;
		if(stampa != null)
			this.setIdStampa(this.stampa.getId());
	}
}
