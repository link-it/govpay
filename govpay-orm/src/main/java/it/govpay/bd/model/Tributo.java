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

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.model.IbanAccredito;

public class Tributo extends it.govpay.model.Tributo {
	private static final long serialVersionUID = 1L;
	
	public Tributo() {	}
	
	// Business
	
	public TipoContabilita getTipoContabilita() {
		if(this.getTipoContabilitaCustom() != null)
			return this.getTipoContabilitaCustom();
		else 
			return this.getTipoContabilitaDefault();
	}

	public String getCodContabilita() {
		if(this.getCodContabilitaCustom() != null)
			return this.getCodContabilitaCustom();
		else 
			return this.getCodContabilitaDefault();
	}
	
	public String getCodTributoIuv() {
		if(this.getCodTributoIuvCustom() != null)
			return this.getCodTributoIuvCustom();
		else 
			return this.getCodTributoIuvDefault();
	}
	
	public boolean isTipoContabilitaCustom(){return this.getTipoContabilitaCustom() != null;}
	public boolean isCodContabilitaCustom(){return this.getCodContabilitaCustom() != null;}
	public boolean isCodTributoIuvCustom(){return this.getCodTributoIuvCustom() != null;}
	
	private transient IbanAccredito ibanAccredito;
	public IbanAccredito getIbanAccredito() throws ServiceException {
		return this.ibanAccredito;
	}
	public void setIbanAccredito(IbanAccredito ibanAccredito) {
		super.setIdIbanAccredito(ibanAccredito.getId());
		this.ibanAccredito = ibanAccredito;
	}
	public void setIbanAccredito(BasicBD bd, long idIbanAccredito) throws ServiceException {
		super.setIdIbanAccredito(idIbanAccredito);
		this.ibanAccredito = AnagraficaManager.getIbanAccredito(bd, idIbanAccredito);
	}
	
	private transient IbanAccredito ibanAppoggio;
	public IbanAccredito getIbanAppoggio() throws ServiceException {
		return this.ibanAppoggio;
	}
	public void setIbanAppoggio(IbanAccredito ibanAppoggio) {
		super.setIdIbanAppoggio(ibanAppoggio.getId());
		this.ibanAppoggio = ibanAppoggio;
	}
	public void setIbanAppoggio(BasicBD bd, long idIbanAppoggio) throws ServiceException {
		super.setIdIbanAppoggio(idIbanAppoggio);
		this.ibanAccredito = AnagraficaManager.getIbanAccredito(bd, idIbanAppoggio);
	}

	
	private transient Dominio dominio;
	
	public Dominio getDominio(BasicBD bd) throws ServiceException {
		if(this.dominio == null) {
			this.dominio = AnagraficaManager.getDominio(bd, this.getIdDominio());
		} 
		return this.dominio;
	}
	
}
