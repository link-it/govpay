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

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.VersamentoFilter;
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

	private List<Versamento> getVersamenti(BDConfigWrapper configWrapper) throws ServiceException {
		if(this.versamenti == null) { 
			VersamentiBD versamentiBD = new VersamentiBD(configWrapper);
			VersamentoFilter filter = versamentiBD.newFilter();
			filter.setIdDocumento(this.getId()); 
			this.versamenti = versamentiBD.findAll(filter);
		}
		return this.versamenti;
	}
	
	public List<Versamento> getVersamentiPagabili(BDConfigWrapper configWrapper, List<String> numeriAvviso) throws ServiceException {
		List<Versamento> versamentiPagabili = new ArrayList<Versamento>();
		List<Versamento> versamenti = getVersamenti(configWrapper);
		for(Versamento v : versamenti) {
			if(v.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO)) {
				if(numeriAvviso != null && !numeriAvviso.isEmpty()) {
					if(numeriAvviso.contains(v.getNumeroAvviso())) {
						versamentiPagabili.add(v);
					}
				} else {
					versamentiPagabili.add(v);	
				}
			}
		}
		return versamentiPagabili;
	}

	public Dominio getDominio(BDConfigWrapper configWrapper) throws ServiceException {
		if(this.dominio == null) {
			try {
				this.dominio = AnagraficaManager.getDominio(configWrapper, this.getIdDominio());
			} catch (NotFoundException e) {
			}
		} 
		return this.dominio;
	}

	public Applicazione getApplicazione(BDConfigWrapper configWrapper) throws ServiceException {
		if(this.applicazione == null) {
			try {
				this.applicazione = AnagraficaManager.getApplicazione(configWrapper, this.getIdApplicazione());
			} catch (NotFoundException e) {
			}
		} 
		return this.applicazione;
	}
	
}

