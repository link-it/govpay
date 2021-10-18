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

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.VersamentiBD;

public class NotificaAppIo extends it.govpay.model.NotificaAppIo {
	
	public NotificaAppIo() {
		
	}
	
	public NotificaAppIo(Versamento versamento, TipoNotifica tipoNotifica, BDConfigWrapper configWrapper) throws ServiceException {
		if(versamento == null)
			throw new ServiceException("Il versamento associato alla Notifica e' vuoto.");
		
		this.setVersamento(versamento);
		this.setTipoVersamentoDominio(versamento.getTipoVersamentoDominio(configWrapper));
		this.setCodApplicazione(versamento.getApplicazione(configWrapper).getCodApplicazione());
		this.setCodVersamentoEnte(versamento.getCodVersamentoEnte());
		this.setCodDominio(versamento.getDominio(configWrapper).getCodDominio());
		this.setIuv(versamento.getIuvVersamento());
		this.setDebitoreIdentificativo(versamento.getAnagraficaDebitore().getCodUnivoco());
		long adesso = new Date().getTime(); 
		this.setDataAggiornamento(new Date(adesso));
		this.setDataCreazione(new Date(adesso));
		this.setDataProssimaSpedizione(new Date(adesso + 10000 ));
		this.setDescrizioneStato(null);
		this.setStato(StatoSpedizione.DA_SPEDIRE);
		this.setTentativiSpedizione(0l);
		this.setTipo(tipoNotifica);
	}
	
	public NotificaAppIo(Rpt rpt, Versamento versamento, TipoNotifica tipoNotifica, BDConfigWrapper configWrapper) throws ServiceException {
		if(versamento == null)
			throw new ServiceException("Il versamento associato alla Notifica e' vuoto.");
		
		if(rpt == null)
			throw new ServiceException("Rpt associato alla Notifica e' vuoto.");
		
		this.setVersamento(versamento);
		this.setRpt(rpt);
		this.setTipoVersamentoDominio(versamento.getTipoVersamentoDominio(configWrapper));
		this.setCodApplicazione(versamento.getApplicazione(configWrapper).getCodApplicazione());
		this.setCodVersamentoEnte(versamento.getCodVersamentoEnte());
		this.setCodDominio(versamento.getDominio(configWrapper).getCodDominio());
		this.setIuv(versamento.getIuvVersamento());
		this.setDebitoreIdentificativo(versamento.getAnagraficaDebitore().getCodUnivoco());
		long adesso = new Date().getTime(); 
		this.setDataAggiornamento(new Date(adesso));
		this.setDataCreazione(new Date(adesso));
		this.setDataProssimaSpedizione(new Date(adesso + 10000 ));
		this.setDescrizioneStato(null);
		this.setStato(StatoSpedizione.DA_SPEDIRE);
		this.setTentativiSpedizione(0l);
		this.setTipo(tipoNotifica);
	}
	
	private static final long serialVersionUID = 1L;
	
	// Business 
	private transient Versamento versamento;
	private transient TipoVersamentoDominio tipoVersamentoDominio;
	private transient Rpt rpt;
	
	public Versamento getVersamento(BDConfigWrapper configWrapper) throws ServiceException {
		if(this.versamento == null && this.getIdVersamento() > 0) {
			VersamentiBD versamentiBD = new VersamentiBD(configWrapper);
			this.versamento = versamentiBD.getVersamento(this.getIdVersamento()); 
		}
		return versamento;
	}
	
	public Versamento getVersamento() {
		return versamento;
	}

	public void setVersamento(Versamento versamento) {
		this.versamento = versamento;
		if(this.versamento.getId() != null)
			this.setIdVersamento(this.versamento.getId());
	}

	public TipoVersamentoDominio getTipoVersamentoDominio(BDConfigWrapper configWrapper) throws ServiceException {
		if(this.tipoVersamentoDominio == null && this.getIdTipoVersamentoDominio() > 0) {
			try {
				this.tipoVersamentoDominio = AnagraficaManager.getTipoVersamentoDominio(configWrapper, this.getIdTipoVersamentoDominio());
			} catch (NotFoundException e) {
			}
		}
		return tipoVersamentoDominio;
	}

	public void setTipoVersamentoDominio(TipoVersamentoDominio tipoVersamentoDominio) {
		this.tipoVersamentoDominio = tipoVersamentoDominio;
		if(this.tipoVersamentoDominio.getId() != null)
			this.setIdTipoVersamentoDominio(this.tipoVersamentoDominio.getId());
	}
	
	public void setRpt(Rpt rpt) {
		this.rpt = rpt;
		if(rpt != null)
			this.setIdRpt(rpt.getId());
	}
	
	public Rpt getRpt() {
		return this.rpt;
	}
	
	public Rpt getRpt(BasicBD bd) throws ServiceException {
		if(this.rpt == null && this.getIdRpt() != null) {
			RptBD rptBD = new RptBD(bd);
			this.rpt = rptBD.getRpt(this.getIdRpt());
		}
			
		return this.rpt;
	}
	
	public Rpt getRpt(BDConfigWrapper configWrapper) throws ServiceException {
		if(this.rpt == null && this.getIdRpt() != null) {
			RptBD rptBD = new RptBD(configWrapper);
			this.rpt = rptBD.getRpt(this.getIdRpt());
		}
		return rpt;
	}
}
