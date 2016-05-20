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

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.RrBD;

import java.util.Date;

import org.openspcoop2.generic_project.exception.ServiceException;

public class Notifica extends BasicModel {
	
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
	
	public enum TipoNotifica {ATTIVAZIONE, RICEVUTA}
	public enum StatoSpedizione {DA_SPEDIRE, SPEDITO}
	
	private Long id;
	private long idApplicazione;
	private Long idRpt;
	private Long idRr;
	private TipoNotifica tipo;
	private Date dataCreazione;
	private StatoSpedizione stato;
	private String descrizioneStato;
	private Long tentativiSpedizione;
	private Date dataProssimaSpedizione;
	private Date dataAggiornamento;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public long getIdApplicazione() {
		return idApplicazione;
	}
	public void setIdApplicazione(long idApplicazione) {
		this.idApplicazione = idApplicazione;
	}
	public TipoNotifica getTipo() {
		return tipo;
	}
	public void setTipo(TipoNotifica tipo) {
		this.tipo = tipo;
	}
	public Date getDataCreazione() {
		return dataCreazione;
	}
	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}
	public StatoSpedizione getStato() {
		return stato;
	}
	public void setStato(StatoSpedizione stato) {
		this.stato = stato;
	}
	public String getDescrizioneStato() {
		return descrizioneStato;
	}
	public void setDescrizioneStato(String descrizioneStato) {
		this.descrizioneStato = descrizioneStato;
	}
	public Long getTentativiSpedizione() {
		return tentativiSpedizione;
	}
	public void setTentativiSpedizione(Long tentativiSpedizione) {
		this.tentativiSpedizione = tentativiSpedizione;
	}
	public Date getDataAggiornamento() {
		return dataAggiornamento;
	}
	public void setDataAggiornamento(Date dataAggiornamento) {
		this.dataAggiornamento = dataAggiornamento;
	}
	public Long getIdRpt() {
		return idRpt;
	}
	public void setIdRpt(Long idRpt) {
		this.idRpt = idRpt;
	}
	public Long getIdRr() {
		return idRr;
	}
	public void setIdRr(Long idRr) {
		this.idRr = idRr;	
	}
	public Date getDataProssimaSpedizione() {
		return dataProssimaSpedizione;
	}
	public void setDataProssimaSpedizione(Date dataProssimaSpedizione) {
		this.dataProssimaSpedizione = dataProssimaSpedizione;
	}
	
	// Business 
	private Applicazione applicazione;
	private Rpt rpt;
	private Rr rr;
	
	public void setApplicazione(Applicazione applicazione) {
		this.applicazione = applicazione;
		this.idApplicazione = applicazione.getId();
	}
	
	public Applicazione getApplicazione(BasicBD bd) throws ServiceException {
		if(applicazione == null)
			applicazione = AnagraficaManager.getApplicazione(bd, idApplicazione);
		return applicazione;
	}
	
	public void setRpt(Rpt rpt) {
		this.rpt = rpt;
		this.idRpt = rpt.getId();
	}
	
	public Rpt getRpt(BasicBD bd) throws ServiceException {
		if(rpt == null) {
			if(idRpt != null) { 
				RptBD rptBD = new RptBD(bd);
				rpt = rptBD.getRpt(idRpt);
			} else {
				rpt = getRr(bd).getRpt(bd);
			}
		}
			
		return rpt;
	}

	public Rr getRr(BasicBD bd) throws ServiceException {
		if(rr == null && idRr != null) {
			RrBD rrBD = new RrBD(bd);
			rr = rrBD.getRr(idRr);
		}
		return rr;
	}

	public void setRr(Rr rr) {
		this.rr = rr;
		this.idRr = rr.getId();
	}
	
	
	
}
