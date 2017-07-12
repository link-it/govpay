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

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.model.Intermediario;

public class Rpt extends it.govpay.model.Rpt{
	
	private static final long serialVersionUID = 1L;
	 
	// Business
	
	private transient Versamento versamento;
	private transient Dominio dominio;
	private transient Canale canale;
	private transient Psp psp;
	private transient List<Pagamento> pagamenti;
	
	public Versamento getVersamento(BasicBD bd) throws ServiceException {
		if(this.versamento == null) {
			VersamentiBD versamentiBD = new VersamentiBD(bd);
			this.versamento = versamentiBD.getVersamento(getIdVersamento());
		}
		return this.versamento;
	}
	
	public void setVersamento(Versamento versamento) {
		this.versamento = versamento;
	}
	
	public Dominio getDominio(BasicBD bd) throws ServiceException {
		if(this.dominio == null) {
			try {
				this.dominio = AnagraficaManager.getDominio(bd, getCodDominio());
			} catch (NotFoundException e) {
				throw new ServiceException(e);
			}
		}
		return this.dominio;
	}
	
	public Canale getCanale(BasicBD bd) throws ServiceException {
		if(canale == null)
			canale = AnagraficaManager.getCanale(bd, getIdCanale());
		return canale;
	}
	public void setCanale(Canale canale) {
		this.canale = canale;
	}
	
	public Psp getPsp(BasicBD bd) throws ServiceException {
		if(psp == null) 
			psp = AnagraficaManager.getPsp(bd, getCanale(bd).getIdPsp());
		return psp;
	}
	public void setPsp(Psp psp) {
		this.psp = psp;
	}
	
	public List<Pagamento> getPagamenti(BasicBD bd) throws ServiceException {
		if(pagamenti == null) {
			PagamentiBD pagamentiBD = new PagamentiBD(bd);
			pagamenti = pagamentiBD.getPagamenti(getId());
		}
		return pagamenti;
	}
	
	public Pagamento getPagamento(String iur, BasicBD bd) throws ServiceException, NotFoundException {
		List<Pagamento> pagamenti = getPagamenti(bd);
		for(Pagamento pagamento : pagamenti) {
			if(pagamento.getIur().equals(iur))
				return pagamento;
		}
		throw new NotFoundException();
	}
	
	public void setPagamenti(List<Pagamento> pagamenti) {
		this.pagamenti = pagamenti;
	}

	public Stazione getStazione(BasicBD bd) throws ServiceException {
		return getDominio(bd).getStazione(bd);
	}


	public Intermediario getIntermediario(BasicBD bd) throws ServiceException {
		return getDominio(bd).getStazione(bd).getIntermediario(bd);
	}

}
