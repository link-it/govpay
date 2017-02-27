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

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.RrBD;
import it.govpay.model.Applicazione;

public class Notifica extends it.govpay.model.Notifica {
	
	public Notifica() {
		
	}
	
	public Notifica(Rpt rpt, TipoNotifica tipoNotifica, BasicBD bd) throws ServiceException {
		setApplicazione(rpt.getVersamento(bd).getApplicazione(bd));
		setDataAggiornamento(new Date());
		setDataCreazione(new Date());
		setDataProssimaSpedizione(new Date());
		setDescrizioneStato(null);
		setRpt(rpt);
		setStato(StatoSpedizione.DA_SPEDIRE);
		setTentativiSpedizione(0l);
		setTipo(tipoNotifica);
	}
	
	public Notifica(Rr rr, TipoNotifica tipoNotifica, BasicBD bd) throws ServiceException {
		setApplicazione(rr.getRpt(bd).getVersamento(bd).getApplicazione(bd));
		setDataAggiornamento(new Date());
		setDataCreazione(new Date());
		setDataProssimaSpedizione(new Date());
		setDescrizioneStato(null);
		setRr(rr);
		setStato(StatoSpedizione.DA_SPEDIRE);
		setTentativiSpedizione(0l);
		setTipo(tipoNotifica);
	}
	
	private static final long serialVersionUID = 1L;
	
	// Business 
	private Applicazione applicazione;
	private Rpt rpt;
	private Rr rr;
	
	public void setApplicazione(Applicazione applicazione) {
		this.applicazione = applicazione;
		this.setIdApplicazione(applicazione.getId());
	}
	
	public Applicazione getApplicazione(BasicBD bd) throws ServiceException {
		if(applicazione == null)
			applicazione = AnagraficaManager.getApplicazione(bd, this.getIdApplicazione());
		return applicazione;
	}
	
	public void setRpt(Rpt rpt) {
		this.rpt = rpt;
		this.setIdRpt(rpt.getId());
	}
	
	public Rpt getRpt(BasicBD bd) throws ServiceException {
		if(rpt == null) {
			if(this.getIdRpt() != null) { 
				RptBD rptBD = new RptBD(bd);
				rpt = rptBD.getRpt(this.getIdRpt());
			} else {
				rpt = getRr(bd).getRpt(bd);
			}
		}
			
		return rpt;
	}

	public Rr getRr(BasicBD bd) throws ServiceException {
		if(rr == null && this.getIdRr() != null) {
			RrBD rrBD = new RrBD(bd);
			rr = rrBD.getRr(this.getIdRr());
		}
		return rr;
	}

	public void setRr(Rr rr) {
		this.rr = rr;
		this.setIdRr(rr.getId());
	}
	
	
	
}
