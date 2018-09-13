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
	
	// Business
	
	public TipoContabilta getTipoContabilita() {
		if(getTipoContabilitaCustom() != null)
			return getTipoContabilitaCustom();
		else 
			return getTipoContabilitaDefault();
	}

	public String getCodContabilita() {
		if(getCodContabilitaCustom() != null)
			return getCodContabilitaCustom();
		else 
			return getCodContabilitaDefault();
	}
	
	public String getCodTributoIuv() {
		if(getCodTributoIuvCustom() != null)
			return getCodTributoIuvCustom();
		else 
			return getCodTributoIuvDefault();
	}
	
	public boolean isTipoContabilitaCustom(){return getTipoContabilitaCustom() != null;}
	public boolean isCodContabilitaCustom(){return getCodContabilitaCustom() != null;}
	public boolean isCodTributoIuvCustom(){return getCodTributoIuvCustom() != null;}
	
	private transient IbanAccredito ibanAccredito;
	
	public IbanAccredito getIbanAccredito(BasicBD bd) throws ServiceException {
		if(ibanAccredito == null && this.getIdIbanAccredito() != null) {
			ibanAccredito = AnagraficaManager.getIbanAccredito(bd, this.getIdIbanAccredito());
		}
		return ibanAccredito;
	}
	public void setIbanAccredito(IbanAccredito ibanAccredito) {
		this.ibanAccredito = ibanAccredito;
	}
	
	private transient IbanAccredito ibanAppoggio;
	public IbanAccredito getIbanAppoggio(BasicBD bd) throws ServiceException {
		if(ibanAppoggio == null && this.getIdIbanAppoggio() != null) {
			ibanAppoggio = AnagraficaManager.getIbanAccredito(bd, this.getIdIbanAppoggio());
		}
		return ibanAppoggio;
	}
	public void setIbanAccreditoPostale(IbanAccredito ibanAccreditoPostale) {
		this.ibanAppoggio = ibanAccreditoPostale;
	}

	
}
