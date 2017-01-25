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

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.model.IbanAccredito;

public class SingoloVersamento extends it.govpay.model.SingoloVersamento{
	private static final long serialVersionUID = 1L;
	
	// Business
	
	private Versamento versamento;
	private Tributo tributo;
	private IbanAccredito ibanAccredito;
	
	public Tributo getTributo(BasicBD bd) throws ServiceException {
		if(tributo == null && this.getIdTributo() != null) {
			tributo = AnagraficaManager.getTributo(bd, getIdTributo());
		}
		return tributo;
	}
	
	public void setTributo(Tributo tributo){
		this.tributo = tributo;
	}

	public void setTributo(String codTributo, BasicBD bd) throws ServiceException, NotFoundException {
		this.tributo = AnagraficaManager.getTributo(bd, versamento.getUo(bd).getIdDominio(), codTributo);
		this.setIdTributo(tributo.getId());
	}
	
	public void setVersamento(Versamento versamento) {
		this.versamento = versamento;
		if(versamento.getId() != null)
			this.setIdVersamento(versamento.getId());
	}
	
	public Versamento getVersamento(BasicBD bd) throws ServiceException {
		if(this.versamento == null) {
			VersamentiBD versamentiBD = new VersamentiBD(bd);
			this.versamento = versamentiBD.getVersamento(getIdVersamento());
		}
		return this.versamento;
	}
	
	public IbanAccredito getIbanAccredito(BasicBD bd) throws ServiceException {
		if(ibanAccredito == null && this.getIdIbanAccredito() != null) {
			ibanAccredito = AnagraficaManager.getIbanAccredito(bd, this.getIdIbanAccredito());
		}
		
		if(ibanAccredito == null && this.getIdIbanAccredito() == null) {
			ibanAccredito = getTributo(bd).getIbanAccredito(bd);
		}
		
		return ibanAccredito;
	}
	
	public void setIbanAccredito(IbanAccredito ibanAccredito) {
		this.ibanAccredito = ibanAccredito;
		if(ibanAccredito.getId() != null)
			this.setIdIbanAccredito(ibanAccredito.getId());
	}
	
	public Tributo.TipoContabilta getTipoContabilita(BasicBD bd) throws ServiceException {
		if(this.getTipoContabilita() != null)
			return this.getTipoContabilita();
		else
			return getTributo(bd).getTipoContabilita();
	}
	
	public String getCodContabilita(BasicBD bd) throws ServiceException {
		if(this.getCodContabilita() != null)
			return this.getCodContabilita();
		else
			return getTributo(bd).getCodContabilita();
	}


}

