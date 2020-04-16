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
import it.govpay.model.Versamento.StatoVersamento;

public class Documento extends it.govpay.model.Documento {
	private static final long serialVersionUID = 1L;

	public Documento() {
		super();
	}

	// Business

	private transient List<Versamento> versamenti;
	private transient Dominio dominio;
	private transient Applicazione applicazione;

	public List<Versamento> getVersamenti(BasicBD bd) throws ServiceException {
		if(this.versamenti == null) { 
			//TODO GIULIANO cercare i versamenti che hanno idDominio = this.id
			this.versamenti = null;
		}
		return this.versamenti;
	}
	
	public List<Versamento> getVersamentiPagabili(BasicBD bd) throws ServiceException {
		List<Versamento> versamentiPagabili = new ArrayList<Versamento>();
		List<Versamento> versamenti = getVersamenti(bd);
		for(Versamento v : versamenti) {
			if(v.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO))
				versamentiPagabili.add(v);
		}
		return versamentiPagabili;
	}

	public Dominio getDominio(BasicBD bd) throws ServiceException {
		if(this.dominio == null) {
			try {
				this.dominio = AnagraficaManager.getDominio(bd, this.getIdDominio());
			} catch (NotFoundException e) {
			}
		} 
		return this.dominio;
	}

	public Applicazione getApplicazione(BasicBD bd) throws ServiceException {
		if(this.applicazione == null) {
			try {
				this.applicazione = AnagraficaManager.getApplicazione(bd, this.getIdApplicazione());
			} catch (NotFoundException e) {
			}
		} 
		return this.applicazione;
	}
	
}

