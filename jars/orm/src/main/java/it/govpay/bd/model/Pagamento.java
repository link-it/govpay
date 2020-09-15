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
	}
	
	public Rpt getRpt(BasicBD bd) throws ServiceException {
		if(this.getIdRpt() != null) {
			if(this.rpt == null) {
				RptBD rptBD = new RptBD(bd);
				rptBD.setAtomica(false); // la connessione deve essere gia' aperta
				this.rpt = rptBD.getRpt(this.getIdRpt());
			}
		}
		return this.rpt;
	}

	public void setRpt(Rpt rpt) {
		this.rpt = rpt;
		this.setIdRpt(rpt.getId());
	}
	
	public Rr getRr(BasicBD bd) throws ServiceException {
		if(this.rr == null) {
			RrBD rrBD = new RrBD(bd);
			this.rr = rrBD.getRr(this.getIdRr());
		}
		return this.rr;
	}

	public void setRr(Rr rr) {
		this.rr = rr;
		this.setIdRr(rr.getId());
	}
	
	public SingoloVersamento getSingoloVersamento() {
		return this.singoloVersamento;
	}

	public SingoloVersamento getSingoloVersamento(BasicBD bd) throws ServiceException {
		if(this.singoloVersamento == null && bd != null) {
			VersamentiBD singoliVersamentiBD = new VersamentiBD(bd);
			singoliVersamentiBD.setAtomica(false); // la connessione deve essere gia' aperta
			this.singoloVersamento = singoliVersamentiBD.getSingoloVersamento(this.getIdSingoloVersamento());
		}
		return this.singoloVersamento;
	}

	public void setSingoloVersamento(SingoloVersamento singoloVersamento) {
		this.singoloVersamento = singoloVersamento;
		this.setIdSingoloVersamento(singoloVersamento.getId());
	}

	public List<Rendicontazione> getRendicontazioni(BasicBD bd) throws ServiceException {
		if(this.rendicontazioni == null){
			RendicontazioniBD rendicontazioniBD = new RendicontazioniBD(bd);
			rendicontazioniBD.setAtomica(false); // la connessione deve essere gia' aperta
			RendicontazioneFilter newFilter = rendicontazioniBD.newFilter();
			newFilter.setCodDominio(this.getCodDominio());
			newFilter.setIuv(this.getIuv());
			newFilter.setIur(this.getIur());
			newFilter.setIndiceDati(this.getIndiceDati());
			this.rendicontazioni = rendicontazioniBD.findAll(newFilter);
		}
		return this.rendicontazioni;
	}
	
	public Incasso getIncasso(BasicBD bd) throws ServiceException {
		if(this.getIdIncasso() != null) {
			if(this.incasso == null) {
				IncassiBD incassiBD = new IncassiBD(bd);
				this.incasso = incassiBD.getIncasso(this.getIdIncasso());
			}
		}
		return this.incasso;
	}

	public void setIncasso(Incasso incasso) {
		this.incasso = incasso;
		this.setIdIncasso(incasso.getId());
	}
	
	public boolean isPagamentoRendicontato(BasicBD bd) throws ServiceException {
		for(Rendicontazione r : this.getRendicontazioni(bd)) {
			if(r.getEsito().equals(EsitoRendicontazione.ESEGUITO) || r.getEsito().equals(EsitoRendicontazione.ESEGUITO_SENZA_RPT))
				return true;
		}
		return false;
	}
	
	public boolean isPagamentoRevocato(BasicBD bd) throws ServiceException {
		for(Rendicontazione r : this.getRendicontazioni(bd)) {
			if(r.getEsito().equals(EsitoRendicontazione.REVOCATO) || r.getEsito().equals(EsitoRendicontazione.ESEGUITO_SENZA_RPT))
				return true;
		}
		return false;
	}
	
	public Dominio getDominio(BDConfigWrapper configWrapper) throws ServiceException {
		if(this.dominio == null){
			try {
				this.dominio = AnagraficaManager.getDominio(configWrapper, this.getCodDominio());
			}catch(NotFoundException e) {}
		}
		return this.dominio;
	}
	public void setDominio(Dominio dominio) {
		this.dominio = dominio;
	}
}

