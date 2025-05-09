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
package it.govpay.bd.model;

import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.orm.IdUtenza;

public class Acl extends it.govpay.model.Acl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 

	public Acl() {
		super();
	}

	// business
	private transient Utenza utenza;
	private transient IdUtenza idUtenzaObj;

	public Utenza getUtenza(BDConfigWrapper configWrapper) throws ServiceException {
		if(this.getIdUtenza() != null && this.utenza == null)
			try {
				this.setUtenza(AnagraficaManager.getUtenza(configWrapper, this.getIdUtenza()));
			} catch (NotFoundException e) {
				// donothing
			}

		return this.utenza;
	}


	public void setUtenza(Utenza utenza) {
		this.utenza = utenza;
	}

	public String getUtenzaPrincipal() {
		return this.utenza != null ? this.utenza.getPrincipal() : null;
	}

	public String getUtenzaPrincipalOriginale() {
		return this.utenza != null ? this.utenza.getPrincipalOriginale() : null;
	}

	public boolean isUtenzaAbilitato() {
		return this.utenza != null ? this.utenza.isAbilitato() : false;
	}


	public IdUtenza getIdUtenza(BDConfigWrapper configWrapper) throws ServiceException {
		Utenza u = this.getUtenza(configWrapper);
		if(u != null && this.idUtenzaObj == null) {
			this.idUtenzaObj = new IdUtenza();
			this.idUtenzaObj.setId(u.getId());
			this.idUtenzaObj.setPrincipal(u.getPrincipal());
			this.idUtenzaObj.setPrincipalOriginale(u.getPrincipalOriginale());
			this.idUtenzaObj.setAbilitato(u.isAbilitato());
		}

		return this.idUtenzaObj;
	}

	public static Acl mergeAcls(List<Acl> listaServizio) {
		Acl aclR = null;
		Acl aclW = null;

		for (Acl acl : listaServizio) {
			if(acl.getListaDiritti().contains(Diritti.SCRITTURA) && acl.getListaDiritti().contains(Diritti.LETTURA)) {
				return acl;
			} else if(acl.getListaDiritti().contains(Diritti.SCRITTURA)) {
				aclW = acl;
			} else if(acl.getListaDiritti().contains(Diritti.LETTURA)) {
				aclR = acl;
			} else { 
				// donothing
			}
		}

		if(aclW != null)
			return aclW;

		if(aclR != null)
			return aclR;

		return null;
	}
}
