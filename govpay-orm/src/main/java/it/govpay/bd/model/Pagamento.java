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
import it.govpay.bd.pagamento.IncassiBD;
import it.govpay.bd.pagamento.RendicontazioniBD;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.RrBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.RendicontazioneFilter;
import it.govpay.bd.model.Rendicontazione;
import it.govpay.model.Rendicontazione.EsitoRendicontazione;

public class Pagamento extends it.govpay.model.Pagamento {

	private static final long serialVersionUID = 1L;
	// Business
	private transient Dominio dominio;
	private transient Rpt rpt;
	private transient SingoloVersamento singoloVersamento;
	private transient Rr rr;
	private transient Incasso incasso;
	private transient List<Rendicontazione> rendicontazioni;

	public Pagamento() {
		// TODO Auto-generated constructor stub
	}
	public Rpt getRpt(BasicBD bd) throws ServiceException {
		if(this.getIdRpt() != null) {
			if(rpt == null) {
				RptBD rptBD = new RptBD(bd);
				rpt = rptBD.getRpt(this.getIdRpt());
			}
		}
		return rpt;
	}

	public void setRpt(Rpt rpt) {
		this.rpt = rpt;
		this.setIdRpt(rpt.getId());
	}
	
	public Rr getRr(BasicBD bd) throws ServiceException {
		if(rr == null) {
			RrBD rrBD = new RrBD(bd);
			rr = rrBD.getRr(this.getIdRr());
		}
		return rr;
	}

	public void setRr(Rr rr) {
		this.rr = rr;
		this.setIdRr(rr.getId());
	}

	public SingoloVersamento getSingoloVersamento(BasicBD bd) throws ServiceException {
		if(singoloVersamento == null) {
			VersamentiBD singoliVersamentiBD = new VersamentiBD(bd);
			singoloVersamento = singoliVersamentiBD.getSingoloVersamento(this.getIdSingoloVersamento());
		}
		return singoloVersamento;
	}

	public void setSingoloVersamento(SingoloVersamento singoloVersamento) {
		this.singoloVersamento = singoloVersamento;
		this.setIdSingoloVersamento(singoloVersamento.getId());
	}

	public List<Rendicontazione> getRendicontazioni(BasicBD bd) throws ServiceException {
		if(rendicontazioni == null){
			RendicontazioniBD rendicontazioniBD = new RendicontazioniBD(bd);
			RendicontazioneFilter newFilter = rendicontazioniBD.newFilter();
			newFilter.setCodDominio(getCodDominio());
			newFilter.setIuv(getIuv());
			newFilter.setIur(getIur());
			rendicontazioni = rendicontazioniBD.findAll(newFilter);
		}
		return rendicontazioni;
	}
	
	public Incasso getIncasso(BasicBD bd) throws ServiceException {
		if(this.getIdIncasso() != null) {
			if(incasso == null) {
				IncassiBD incassiBD = new IncassiBD(bd);
				incasso = incassiBD.getIncasso(this.getIdIncasso());
			}
		}
		return incasso;
	}

	public void setIncasso(Incasso incasso) {
		this.incasso = incasso;
		this.setIdIncasso(incasso.getId());
	}
	
	public boolean isPagamentoRendicontato(BasicBD bd) throws ServiceException {
		for(Rendicontazione r : getRendicontazioni(bd)) {
			if(r.getEsito().equals(EsitoRendicontazione.ESEGUITO) || r.getEsito().equals(EsitoRendicontazione.ESEGUITO_SENZA_RPT))
				return true;
		}
		return false;
	}
	
	public boolean isPagamentoRevocato(BasicBD bd) throws ServiceException {
		for(Rendicontazione r : getRendicontazioni(bd)) {
			if(r.getEsito().equals(EsitoRendicontazione.REVOCATO) || r.getEsito().equals(EsitoRendicontazione.ESEGUITO_SENZA_RPT))
				return true;
		}
		return false;
	}
	
	public Dominio getDominio(BasicBD bd) throws ServiceException, NotFoundException {
		if(dominio == null){
			dominio = AnagraficaManager.getDominio(bd, this.getCodDominio());
		}
		return dominio;
	}
	public void setDominio(Dominio dominio) {
		this.dominio = dominio;
	}
}

