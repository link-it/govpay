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

import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;

public class Incasso extends it.govpay.model.Incasso {

	private static final long serialVersionUID = 1L;
	// Business
	private transient List<Pagamento> pagamenti;
	private transient Applicazione applicazione;
	private transient Operatore operatore;
	private transient Dominio dominio;

	public List<Pagamento> getPagamenti() {
		return this.pagamenti;
	}
	
	public void setPagamenti(List<Pagamento> pagamenti) {
		this.pagamenti = pagamenti;
	}
	
	public Applicazione getApplicazione(BDConfigWrapper configWrapper) throws ServiceException {
		if(this.getIdApplicazione() != null && this.applicazione == null) {
			try {
				this.applicazione = AnagraficaManager.getApplicazione (configWrapper, this.getIdApplicazione());
			} catch (NotFoundException e) {
			}
		} 
		return this.applicazione;
	}

	public void setApplicazione(long idApplicazione, BDConfigWrapper configWrapper) throws ServiceException {
		try {
			this.applicazione = AnagraficaManager.getApplicazione(configWrapper, this.getIdApplicazione());
			this.setIdApplicazione(this.applicazione.getId());
		} catch (NotFoundException e) {
		}
		
	}
	
	public void setApplicazione(Applicazione applicazione) {
		this.applicazione = applicazione;
		if(this.applicazione != null) {
			this.setIdApplicazione(this.applicazione.getId());
		}
	}
	
	public Operatore getOperatore(BDConfigWrapper configWrapper) throws ServiceException {
		if(this.getIdOperatore() != null && this.operatore == null) {
			try {
				this.operatore = AnagraficaManager.getOperatore(configWrapper, this.getIdOperatore());
			} catch (NotFoundException e) {
			}
		} 
		return this.operatore;
	}

	public void setOperatore(long idOperatore, BDConfigWrapper configWrapper) throws ServiceException {
		try {
			this.operatore = AnagraficaManager.getOperatore(configWrapper, idOperatore);
			this.setIdOperatore(this.operatore.getId());
		} catch (NotFoundException e) {
		}
		
	}
	
	public void setOperatore(Operatore operatore) {
		this.operatore = operatore;
		if(this.operatore != null) {
			this.setIdOperatore(this.operatore.getId());
		}
	}

	public Dominio getDominio(BDConfigWrapper configWrapper) throws ServiceException {
		if(this.dominio == null) {
			try{
				this.dominio = AnagraficaManager.getDominio(configWrapper, this.getCodDominio());
			}catch (NotFoundException e) {
				this.dominio = null;
			}
		} 
		return this.dominio;
	}
}

