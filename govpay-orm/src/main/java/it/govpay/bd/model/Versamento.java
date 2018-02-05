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
import java.util.Collections;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.pagamento.IuvBD;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.RptFilter;
import it.govpay.model.Applicazione;
import it.govpay.model.Iuv;
import it.govpay.model.Iuv.TipoIUV;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.UnitaOperativa;

public class Versamento extends it.govpay.model.Versamento {

	private static final long serialVersionUID = 1L;
	// BUSINESS
	
	private transient List<SingoloVersamento> singoliVersamenti;
	private transient List<Rpt> rpts;
	private transient Applicazione applicazione;
	private transient Dominio dominio;
	private transient UnitaOperativa uo;
	private transient Iuv iuv;
	
	public void addSingoloVersamento(it.govpay.bd.model.SingoloVersamento singoloVersamento) throws ServiceException {
		if(this.singoliVersamenti == null) {
			this.singoliVersamenti = new ArrayList<SingoloVersamento>();
		}
		
		this.singoliVersamenti.add(singoloVersamento);
		
		if(singoloVersamento.getTipoBollo() != null) {
			this.setBolloTelematico(true);
		}
	}
	
	public List<it.govpay.bd.model.SingoloVersamento> getSingoliVersamenti(BasicBD bd) throws ServiceException {
		if(this.singoliVersamenti == null && getId() != null) {
			VersamentiBD versamentiBD = new VersamentiBD(bd);
			this.singoliVersamenti = versamentiBD.getSingoliVersamenti(getId());
		}
		
		if(this.singoliVersamenti != null)
			Collections.sort(this.singoliVersamenti);
		
		return this.singoliVersamenti;
	}

	public Applicazione getApplicazione(BasicBD bd) throws ServiceException {
		if(applicazione == null) {
			applicazione = AnagraficaManager.getApplicazione(bd, getIdApplicazione());
		} 
		return applicazione;
	}
	
	public void setApplicazione(String codApplicazione, BasicBD bd) throws ServiceException, NotFoundException {
		applicazione = AnagraficaManager.getApplicazione(bd, codApplicazione);
		this.setIdApplicazione(applicazione.getId());
	}

	public UnitaOperativa getUo(BasicBD bd) throws ServiceException {
		if(this.getIdUo() != null && uo == null) {
			uo = AnagraficaManager.getUnitaOperativa(bd, getIdUo());
		}
		return uo;
	}

	public Dominio getDominio(BasicBD bd) throws ServiceException {
		if(dominio == null) {
			dominio = AnagraficaManager.getDominio(bd, getIdUo());
		} 
		return dominio;
	}
	
	public UnitaOperativa setUo(long idDominio, String codUo, BasicBD bd) throws ServiceException, NotFoundException {
		uo = AnagraficaManager.getUnitaOperativa(bd, idDominio, codUo);
		this.setIdUo(uo.getId());
		return uo;
	}
	
	public List<Rpt> getRpt(BasicBD bd) throws ServiceException {
		if(rpts == null) {
			RptBD rptBD = new RptBD(bd);
			RptFilter filter = rptBD.newFilter();
			filter.setIdVersamento(this.getId());
			rpts = rptBD.findAll(filter);
		}
		return rpts;
	}
	
	public Iuv getIuv(BasicBD bd) throws ServiceException {
		if(iuv == null) {
			IuvBD iuvBD = new IuvBD(bd);
			try {
				iuv = iuvBD.getIuv(this.getIdApplicazione(), this.getCodVersamentoEnte(), TipoIUV.NUMERICO);
			} catch (NotFoundException e) {
				// Iuv non assegnato.
			}
		}
		return iuv;
	}
}
