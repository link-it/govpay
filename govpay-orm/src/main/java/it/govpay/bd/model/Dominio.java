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
import it.govpay.bd.anagrafica.AnagraficaManagerNoCache;
import it.govpay.bd.anagrafica.IbanAccreditoBD;
import it.govpay.bd.anagrafica.TributiBD;
import it.govpay.bd.anagrafica.UnitaOperativeBD;
import it.govpay.bd.anagrafica.filters.IbanAccreditoFilter;
import it.govpay.bd.anagrafica.filters.TributoFilter;
import it.govpay.bd.anagrafica.filters.UnitaOperativaFilter;
import it.govpay.model.Anagrafica;
import it.govpay.bd.model.Applicazione;

public class Dominio extends it.govpay.model.Dominio {
	private static final long serialVersionUID = 1L;

	public Dominio() {
		super();
	}
	
	// Business
	public Dominio(BasicBD bd, long idDominio, long idStazione) throws ServiceException {
		super.setId(idDominio);
		super.setIdStazione(idStazione);

		try {
			anagrafica = AnagraficaManager.getUnitaOperativa(bd, idDominio, EC).getAnagrafica();
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		}

		stazione = AnagraficaManager.getStazione(bd, idStazione);
	}
	
	// Business

	private transient Anagrafica anagrafica;
	private transient Stazione stazione;
	private transient Applicazione applicazioneDefault;
	private transient List<UnitaOperativa> unitaOperative;
	private transient List<IbanAccredito> ibanAccredito;
	private transient List<Tributo> tributi;

	public Stazione getStazione() throws ServiceException {
		return stazione;
	}

	public Anagrafica getAnagrafica() throws ServiceException {
		return anagrafica;
	}

	public void setAnagrafica(Anagrafica anagrafica) {
		this.anagrafica = anagrafica;
	}
	
	public Applicazione getApplicazioneDefault(BasicBD bd) throws ServiceException {
		return getApplicazioneDefault(bd, false);
	}

	public Applicazione getApplicazioneDefault(BasicBD bd, boolean useCacheData) throws ServiceException {
		if(applicazioneDefault == null && this.getIdApplicazioneDefault() != null) {
			applicazioneDefault = useCacheData ? AnagraficaManager.getApplicazione(bd, this.getIdApplicazioneDefault()) : AnagraficaManagerNoCache.getApplicazione(bd, this.getIdApplicazioneDefault());
		} 
		return applicazioneDefault;
	}

	public void setApplicazioneDefault(Applicazione applicazioneDefault) {
		this.applicazioneDefault = applicazioneDefault;
		this.setIdApplicazioneDefault(applicazioneDefault.getId());
	}

	// Business

	public List<UnitaOperativa> getUnitaOperative(BasicBD bd) throws ServiceException {
		if(unitaOperative == null) { 
			UnitaOperativeBD uoBD = new UnitaOperativeBD(bd);
			UnitaOperativaFilter filter = uoBD.newFilter();
			filter.setDominioFilter(this.getId());
			unitaOperative = uoBD.findAll(filter);
		}
		return unitaOperative;
	}

	
	public UnitaOperativa getUnitaOperativa(BasicBD bd, String codUnivoco) throws ServiceException, NotFoundException {
		return getUnitaOperativa(bd, codUnivoco, true);
	}
	
	public UnitaOperativa getUnitaOperativa(BasicBD bd, String codUnivoco, boolean useCacheData) throws ServiceException, NotFoundException {
		return useCacheData ? AnagraficaManager.getUnitaOperativa(bd, this.getId(), codUnivoco) : AnagraficaManagerNoCache.getUnitaOperativa(bd, this.getId(), codUnivoco);
	}

	public List<IbanAccredito> getIbanAccredito(BasicBD bd) throws ServiceException {
		if(ibanAccredito == null) { 
			IbanAccreditoBD ibanAccreditoBD = new IbanAccreditoBD(bd);
			IbanAccreditoFilter filter = ibanAccreditoBD.newFilter();
			filter.setIdDominio(this.getId());
			ibanAccredito = ibanAccreditoBD.findAll(filter);
		}
		return ibanAccredito;
	}
	
	public IbanAccredito getIban(BasicBD bd, String iban) throws ServiceException, NotFoundException {
		return getIban(bd, iban, true);
	}

	public IbanAccredito getIban(BasicBD bd, String iban, boolean useCacheData) throws ServiceException, NotFoundException {
		return useCacheData ? AnagraficaManager.getIbanAccredito(bd, this.getId(), iban) : AnagraficaManagerNoCache.getIbanAccredito(bd, this.getId(), iban);
	}

	public List<Tributo> getTributi(BasicBD bd) throws ServiceException {
		if(tributi == null) { 
			TributiBD tributiBD = new TributiBD(bd);
			TributoFilter filter = tributiBD.newFilter();
			filter.setIdDominio(this.getId());
			tributi = tributiBD.findAll(filter);
		}
		return tributi;
	}

	public Tributo getTributo(BasicBD bd, String codTributo) throws ServiceException, NotFoundException {
		return getTributo(bd, codTributo, true);
	}
	
	public Tributo getTributo(BasicBD bd, String codTributo, boolean useCacheData) throws ServiceException, NotFoundException {
		return useCacheData ? AnagraficaManager.getTributo(bd, this.getId(), codTributo) : AnagraficaManagerNoCache.getTributo(bd, this.getId(), codTributo);
	}
}

