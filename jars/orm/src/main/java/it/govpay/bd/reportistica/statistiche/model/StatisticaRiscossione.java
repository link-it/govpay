/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
package it.govpay.bd.reportistica.statistiche.model;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.model.TipoVersamento;

public class StatisticaRiscossione extends it.govpay.model.reportistica.statistiche.StatisticaRiscossione{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 
	
	public StatisticaRiscossione() {
		super();
	}
	
	private transient Dominio dominio;
	private transient UnitaOperativa uo;
	private transient TipoVersamento tipoVersamento;
	private transient Applicazione applicazione;
	
	public UnitaOperativa getUo(BDConfigWrapper configWrapper) throws ServiceException {
		if(this.getCodUo() != null && this.getDominio(configWrapper) != null && this.uo == null) {
			try {
				this.uo = AnagraficaManager.getUnitaOperativa(configWrapper, this.getDominio(configWrapper).getId(), this.getCodUo());
			} catch (NotFoundException e) {
			}
		}
		return this.uo;
	}

	public Dominio getDominio(BDConfigWrapper configWrapper) throws ServiceException {
		if(this.getCodDominio() != null && this.dominio == null) {
			try {
				this.dominio = AnagraficaManager.getDominio(configWrapper, this.getCodDominio());
			} catch (NotFoundException e) {
			}
		}
		return this.dominio;
	}
	
	public TipoVersamento getTipoVersamento(BDConfigWrapper configWrapper) throws ServiceException {
		if(this.getCodTipoVersamento() != null && this.tipoVersamento == null) {
			try {
				this.tipoVersamento = AnagraficaManager.getTipoVersamento(configWrapper, this.getCodTipoVersamento()); 
			} catch (NotFoundException e) {
			}
		} 
		return this.tipoVersamento;
	}
	
	public Applicazione getApplicazione(BDConfigWrapper configWrapper) throws ServiceException {
		if(this.getCodApplicazione() != null && this.applicazione == null) {
			try {
				this.applicazione = AnagraficaManager.getApplicazione(configWrapper, this.getCodApplicazione());
			} catch (NotFoundException e) {
			}
		} 
		return this.applicazione;
	}
	
}
