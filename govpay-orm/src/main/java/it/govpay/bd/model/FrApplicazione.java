/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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
package it.govpay.bd.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.pagamento.FrBD;
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.model.Applicazione;
import it.govpay.bd.model.Pagamento;

public class FrApplicazione extends it.govpay.model.FrApplicazione{
	private static final long serialVersionUID = 1L;
 
	
	// Business 
	
	private Applicazione applicazione;
	private Fr fr;
	private List<Pagamento> pagamenti;
	private List<RendicontazioneSenzaRpt> rendicontazioniSenzaRpt;
	
	public Applicazione getApplicazione(BasicBD bd) throws ServiceException {
		if(applicazione == null) {
			applicazione = AnagraficaManager.getApplicazione(bd, this.getIdApplicazione());
		}
		return applicazione;
	}
	public void setApplicazione(Applicazione applicazione) {
		this.applicazione = applicazione;
	}

	public Fr getFr(BasicBD bd) throws ServiceException {
		if(fr == null) {
			FrBD frBD = new FrBD(bd);
			fr = frBD.getFr(this.getIdFr());
		}
		return fr;
	}
	public void setFr(Fr fr) {
		this.fr = fr;
	}
	
	public List<Pagamento> getPagamenti(BasicBD bd) throws ServiceException {
		if(pagamenti == null) {
			PagamentiBD pagamentiBD = new PagamentiBD(bd);
			pagamenti = pagamentiBD.getPagamentiByFrApplicazione(this.getId());
		}
		return pagamenti;
	}
	
	public void setPagamenti(List<Pagamento> pagamenti) {
		this.pagamenti = pagamenti;
	}
	
	public void addPagamento(Pagamento pagamento) {
		if(this.pagamenti == null) this.pagamenti = new ArrayList<Pagamento>();
		this.pagamenti.add(pagamento);
		BigDecimal importoPagato = pagamento.getImportoPagato();
		this.addImporto(importoPagato);
		this.incrementaNumeroPagamenti();
	}
	
	public List<RendicontazioneSenzaRpt> getRendicontazioniSenzaRpt(BasicBD bd) throws ServiceException {
		if(rendicontazioniSenzaRpt == null) {
			PagamentiBD pagamentiBD = new PagamentiBD(bd);
			rendicontazioniSenzaRpt = pagamentiBD.getRendicontazioniSenzaRpt(this.getId());
		}
		return rendicontazioniSenzaRpt;
	}
	
	public void setRendicontazioniSenzaRpt(List<RendicontazioneSenzaRpt> rendicontazioniSenzaRpt) {
		this.rendicontazioniSenzaRpt = rendicontazioniSenzaRpt;
	}
	
	public void addRendicontazioneSenzaRpt(RendicontazioneSenzaRpt rendincontazione) {
		if(this.rendicontazioniSenzaRpt == null) this.rendicontazioniSenzaRpt = new ArrayList<RendicontazioneSenzaRpt>();
		this.rendicontazioniSenzaRpt.add(rendincontazione);
		BigDecimal importoPagato = rendincontazione.getImportoPagato();
		this.addImporto(importoPagato);
		this.incrementaNumeroPagamenti();
	}
	
}
