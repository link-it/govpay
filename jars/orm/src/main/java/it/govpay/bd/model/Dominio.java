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
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.IbanAccreditoBD;
import it.govpay.bd.anagrafica.TipiVersamentoDominiBD;
import it.govpay.bd.anagrafica.TributiBD;
import it.govpay.bd.anagrafica.UnitaOperativeBD;
import it.govpay.bd.anagrafica.filters.IbanAccreditoFilter;
import it.govpay.bd.anagrafica.filters.TipoVersamentoDominioFilter;
import it.govpay.bd.anagrafica.filters.TributoFilter;
import it.govpay.bd.anagrafica.filters.UnitaOperativaFilter;
import it.govpay.model.Anagrafica;

public class Dominio extends it.govpay.model.Dominio {
	private static final long serialVersionUID = 1L;

	public Dominio() {
		super();
	}

	// Business
	public Dominio(BDConfigWrapper configWrapper, long idDominio, long idStazione) throws ServiceException {
		super.setId(idDominio);
		super.setIdStazione(idStazione);

		try {
			this.anagrafica = AnagraficaManager.getUnitaOperativa(configWrapper, idDominio, EC).getAnagrafica();
			this.stazione = AnagraficaManager.getStazione(configWrapper, idStazione);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		}
	}

	// Business

	private transient Anagrafica anagrafica;
	private transient Stazione stazione;
	private transient List<UnitaOperativa> unitaOperative;
	private transient List<IbanAccredito> ibanAccredito;
	private transient List<Tributo> tributi;
	private transient List<TipoVersamentoDominio> tipiVersamento;

	public Stazione getStazione() throws ServiceException {
		return this.stazione;
	}

	public Anagrafica getAnagrafica() throws ServiceException {
		return this.anagrafica;
	}

	public void setAnagrafica(Anagrafica anagrafica) {
		this.anagrafica = anagrafica;
	}

	public Applicazione getApplicazioneDefault(BDConfigWrapper configWrapper) throws ServiceException {
		if(this.getIdApplicazioneDefault() != null) {
			try {
				return AnagraficaManager.getApplicazione(configWrapper, this.getIdApplicazioneDefault());
			} catch (NotFoundException e) {
			}
		} 
		return null;
	}

	public void setApplicazioneDefault(Applicazione applicazioneDefault) {
		this.setIdApplicazioneDefault(applicazioneDefault.getId());
	}

	// Business

	public List<UnitaOperativa> getUnitaOperative(BDConfigWrapper configWrapper) throws ServiceException {
		if(this.unitaOperative == null) { 
			UnitaOperativeBD uoBD = new UnitaOperativeBD(configWrapper);
			UnitaOperativaFilter filter = uoBD.newFilter();
			filter.setDominioFilter(this.getId());
			filter.setExcludeEC(true);
			this.unitaOperative = uoBD.findAll(filter);
		}
		return this.unitaOperative;
	}

	public void setUnitaOperative(List<UnitaOperativa> unitaOperative) {
		this.unitaOperative = unitaOperative;
	}
	
	public List<UnitaOperativa> getUnitaOperative() {
		return unitaOperative;
	}

	public UnitaOperativa getUnitaOperativa(BDConfigWrapper configWrapper, String codUnivoco) throws ServiceException, NotFoundException {
		return AnagraficaManager.getUnitaOperativa(configWrapper, this.getId(), codUnivoco);
	}

	public List<IbanAccredito> getIbanAccredito(BDConfigWrapper configWrapper) throws ServiceException {
		if(this.ibanAccredito == null) { 
			IbanAccreditoBD ibanAccreditoBD = new IbanAccreditoBD(configWrapper);
			IbanAccreditoFilter filter = ibanAccreditoBD.newFilter();
			filter.setIdDominio(this.getId());
			this.ibanAccredito = ibanAccreditoBD.findAll(filter);
		}
		return this.ibanAccredito;
	}

	public IbanAccredito getIban(BDConfigWrapper configWrapper, String iban) throws ServiceException, NotFoundException {
		return AnagraficaManager.getIbanAccredito(configWrapper, this.getId(), iban);
	}
 
	public List<Tributo> getTributi(BDConfigWrapper configWrapper) throws ServiceException {
		if(this.tributi == null) { 
			TributiBD tributiBD = new TributiBD(configWrapper);
			TributoFilter filter = tributiBD.newFilter();
			filter.setIdDominio(this.getId());
			this.tributi = tributiBD.findAll(filter);
		}
		return this.tributi;
	}

	public Tributo getTributo(BDConfigWrapper configWrapper, String codTributo) throws ServiceException, NotFoundException {
		return AnagraficaManager.getTributo(configWrapper, this.getId(), codTributo);
	}
	
	public List<TipoVersamentoDominio> getTipiVersamento(BDConfigWrapper configWrapper) throws ServiceException {
		if(this.tipiVersamento == null) { 
			TipiVersamentoDominiBD tvdBD = new TipiVersamentoDominiBD(configWrapper);
			TipoVersamentoDominioFilter filter = tvdBD.newFilter();
			filter.setIdDominio(this.getId());
			this.tipiVersamento = tvdBD.findAll(filter);
		}
		return this.tipiVersamento;
	}

	public TipoVersamentoDominio getTipoVersamentoDominio(BDConfigWrapper configWrapper, String codTipoVersamento) throws ServiceException, NotFoundException {
		return AnagraficaManager.getTipoVersamentoDominio(configWrapper, this.getId(), codTipoVersamento);
	}
	
	
}

