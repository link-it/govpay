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
import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.bd.pagamento.PagamentiPortaleBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.model.Intermediario;

public class Rpt extends it.govpay.model.Rpt{
	
	private static final long serialVersionUID = 1L;
	 
	// Business
	
	private transient Versamento versamento;
	private transient Dominio dominio;
	private transient List<Pagamento> pagamenti;
	private transient PagamentoPortale pagamentoPortale;
	
	public Versamento getVersamento(BDConfigWrapper configWrapper) throws ServiceException {
		if(this.versamento == null && this.getIdVersamento() > 0) {
			VersamentiBD versamentiBD = new VersamentiBD(configWrapper);
			this.versamento = versamentiBD.getVersamento(this.getIdVersamento());
		}
		return this.versamento;
	}
	
	public Versamento getVersamento(BasicBD bd) throws ServiceException {
		if(this.versamento == null && bd != null && this.getIdVersamento() > 0) {
			VersamentiBD versamentiBD = new VersamentiBD(bd);
			versamentiBD.setAtomica(false); // connessione gestita fuori
			this.versamento = versamentiBD.getVersamento(this.getIdVersamento());
		}
		return this.versamento;
	}
	
	public void setVersamento(Versamento versamento) {
		this.versamento = versamento;
		if(this.versamento != null)
			this.setIdVersamento(this.versamento.getId());
	}
	
	public Versamento getVersamento() {
		return this.versamento;
	}

	public Dominio getDominio(BDConfigWrapper configWrapper) throws ServiceException {
		if(this.dominio == null) {
			try {
				this.dominio = AnagraficaManager.getDominio(configWrapper, this.getCodDominio());
			} catch (NotFoundException e) {
				throw new ServiceException(e);
			}
		}
		return this.dominio;
	}
	
	public List<Pagamento> getPagamenti()  {
		return this.pagamenti;
	}
	
	public List<Pagamento> getPagamenti(BDConfigWrapper configWrapper) throws ServiceException {
		if(this.pagamenti == null) {
			PagamentiBD pagamentiBD = new PagamentiBD(configWrapper);
			this.pagamenti = pagamentiBD.getPagamenti(this.getId(), true);
		}
		return this.pagamenti;
	}
	
//	public List<Pagamento> getPagamenti(BasicBD bd) throws ServiceException {
//		if(this.pagamenti == null) {
//			PagamentiBD pagamentiBD = new PagamentiBD(bd);
//			pagamentiBD.setAtomica(false);
//			this.pagamenti = pagamentiBD.getPagamenti(this.getId());
//		}
//		return this.pagamenti;
//	}
	
	public Pagamento getPagamento(String iur) throws ServiceException, NotFoundException {
		List<Pagamento> pagamenti = this.getPagamenti();
		for(Pagamento pagamento : pagamenti) {
			if(pagamento.getIur().equals(iur))
				return pagamento;
		}
		throw new NotFoundException();
	}
	
//	public Pagamento getPagamento(String iur, BasicBD bd) throws ServiceException, NotFoundException {
//		List<Pagamento> pagamenti = this.getPagamenti(bd);
//		for(Pagamento pagamento : pagamenti) {
//			if(pagamento.getIur().equals(iur))
//				return pagamento;
//		}
//		throw new NotFoundException();
//	}
	
	public void setPagamenti(List<Pagamento> pagamenti) {
		this.pagamenti = pagamenti;
	}

	public Stazione getStazione(BDConfigWrapper configWrapper) throws ServiceException {
		return this.getDominio(configWrapper).getStazione();
	}

	public Intermediario getIntermediario(BDConfigWrapper configWrapper) throws ServiceException {
		return this.getDominio(configWrapper).getStazione().getIntermediario(configWrapper);
	}

	public PagamentoPortale getPagamentoPortale()  {
		return this.pagamentoPortale;
	}
	
	public PagamentoPortale getPagamentoPortale(BDConfigWrapper configWrapper) throws ServiceException  {
		if(this.pagamentoPortale == null && this.getIdPagamentoPortale() != null) {
			PagamentiPortaleBD versamentiBD = new PagamentiPortaleBD(configWrapper);
			try {
				this.pagamentoPortale = versamentiBD.getPagamento(this.getIdPagamentoPortale());
			} catch (NotFoundException e) {
			}
		}
		return this.pagamentoPortale;
	}
	
	public void setPagamentoPortale(PagamentoPortale pagamentoPortale) {
		this.pagamentoPortale = pagamentoPortale;
	}

}
