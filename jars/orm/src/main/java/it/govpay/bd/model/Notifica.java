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

import java.util.Date;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.RrBD;
import it.govpay.bd.model.Applicazione;

public class Notifica extends it.govpay.model.Notifica {
	
	public Notifica() {
		
	}
	
	public Notifica(Rpt rpt, TipoNotifica tipoNotifica, BasicBD bd) throws ServiceException {
		this.setApplicazione(rpt.getVersamento(bd).getApplicazione(bd));
		long adesso = new Date().getTime();
		this.setDataAggiornamento(new Date(adesso));
		this.setDataCreazione(new Date(adesso));
		this.setDataProssimaSpedizione(new Date(adesso + 10000 ));
		this.setDescrizioneStato(null);
		this.setRpt(rpt);
		this.setStato(StatoSpedizione.DA_SPEDIRE);
		this.setTentativiSpedizione(0l);
		this.setTipo(tipoNotifica);
	}
	
	public Notifica(Rr rr, TipoNotifica tipoNotifica, BasicBD bd) throws ServiceException {
		this.setApplicazione(rr.getRpt(bd).getVersamento(bd).getApplicazione(bd));
		long adesso = new Date().getTime();
		this.setDataAggiornamento(new Date(adesso));
		this.setDataCreazione(new Date(adesso));
		this.setDataProssimaSpedizione(new Date(adesso + 60000 ));
		this.setDescrizioneStato(null);
		this.setRr(rr);
		this.setStato(StatoSpedizione.DA_SPEDIRE);
		this.setTentativiSpedizione(0l);
		this.setTipo(tipoNotifica);
	}
	
	private static final long serialVersionUID = 1L;
	
	// Business 
	private transient Applicazione applicazione;
	private transient Rpt rpt;
	private transient Rr rr;
	
	public void setApplicazione(Applicazione applicazione) {
		this.applicazione = applicazione;
		this.setIdApplicazione(applicazione.getId());
	}
	
	public Applicazione getApplicazione(BasicBD bd) throws ServiceException {
		if(this.applicazione == null)
			try {
				this.applicazione = AnagraficaManager.getApplicazione(bd, this.getIdApplicazione());
			} catch (NotFoundException e) {
			}
		return this.applicazione;
	}
	
	public void setRpt(Rpt rpt) {
		this.rpt = rpt;
		this.setIdRpt(rpt.getId());
	}
	
	public Rpt getRpt(BasicBD bd) throws ServiceException {
		if(this.rpt == null && this.getIdRpt() != null) {
			RptBD rptBD = new RptBD(bd);
			this.rpt = rptBD.getRpt(this.getIdRpt());
		}
			
		return this.rpt;
	}

	public Rr getRr(BasicBD bd) throws ServiceException {
		if(this.rr == null && this.getIdRr() != null) {
			RrBD rrBD = new RrBD(bd);
			this.rr = rrBD.getRr(this.getIdRr());
		}
		return this.rr;
	}

	public void setRr(Rr rr) {
		this.rr = rr;
		this.setIdRr(rr.getId());
	}
	
	public String getRptKey(BasicBD bd) throws ServiceException {
		if(this.getRpt(bd) != null)
			return rpt.getCodDominio() + "@" + rpt.getIuv() + "@" + rpt.getCcp();
		return "";
	}
	
}
