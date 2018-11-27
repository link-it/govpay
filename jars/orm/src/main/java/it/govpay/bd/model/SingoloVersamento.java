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
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.bd.pagamento.RendicontazioniBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.model.IbanAccredito;

public class SingoloVersamento extends it.govpay.model.SingoloVersamento{
	private static final long serialVersionUID = 1L;
	
	// Business
	
	private transient Versamento versamento;
	private transient Tributo tributo;
	private transient IbanAccredito ibanAccredito;
	private transient IbanAccredito ibanAppoggio;
	private transient List<Pagamento> pagamenti;
	private transient List<Rendicontazione> rendicontazioni;

	
	public Tributo getTributo(BasicBD bd) throws ServiceException {
		if(this.tributo == null && this.getIdTributo() != null) {
			this.tributo = AnagraficaManager.getTributo(bd, this.getIdTributo());
		}
		return this.tributo;
	}
	
	public void setTributo(Tributo tributo){
		this.tributo = tributo;
	}

	public void setTributo(String codTributo, BasicBD bd) throws ServiceException, NotFoundException {
		this.tributo = AnagraficaManager.getTributo(bd, this.versamento.getUo(bd).getIdDominio(), codTributo);
		this.setIdTributo(this.tributo.getId());
	}
	
	public void setVersamento(Versamento versamento) {
		this.versamento = versamento;
		if(versamento.getId() != null)
			this.setIdVersamento(versamento.getId());
	}
	
	public Versamento getVersamento(BasicBD bd) throws ServiceException {
		if(this.versamento == null) {
			VersamentiBD versamentiBD = new VersamentiBD(bd);
			this.versamento = versamentiBD.getVersamento(this.getIdVersamento());
		}
		return this.versamento;
	}
	
	public IbanAccredito getIbanAccredito(BasicBD bd) throws ServiceException {
		if(this.ibanAccredito == null && this.getIdIbanAccredito() != null) {
			this.ibanAccredito = AnagraficaManager.getIbanAccredito(bd, this.getIdIbanAccredito());
		}
		
		if(this.ibanAccredito == null && this.getIdIbanAccredito() == null) {
			this.ibanAccredito = this.getTributo(bd).getIbanAccredito();
		}
		
		return this.ibanAccredito;
	}
	
	public void setIbanAppoggio(IbanAccredito ibanAppoggio) {
		this.ibanAppoggio = ibanAppoggio;
		if(ibanAppoggio != null && ibanAppoggio.getId() != null)
			this.setIdIbanAppoggio(ibanAppoggio.getId());
	}
	
	public IbanAccredito getIbanAppoggio(BasicBD bd) throws ServiceException {
		if(this.ibanAppoggio == null && this.getIdIbanAppoggio() != null) {
			this.ibanAppoggio = AnagraficaManager.getIbanAccredito(bd, this.getIdIbanAppoggio());
		}
		
		if(this.ibanAppoggio == null && this.getTributo(bd) != null && this.getIdIbanAppoggio() == null) {
			this.ibanAppoggio = this.getTributo(bd).getIbanAppoggio();
		}
		
		return this.ibanAppoggio;
	}
	
	public void setIbanAccredito(IbanAccredito ibanAccredito) {
		this.ibanAccredito = ibanAccredito;
		if(ibanAccredito != null && ibanAccredito.getId() != null)
			this.setIdIbanAccredito(ibanAccredito.getId());
	}
	
	public Tributo.TipoContabilita getTipoContabilita(BasicBD bd) throws ServiceException {
		if(this.getTipoContabilita() != null)
			return this.getTipoContabilita();
		else
			return this.getTributo(bd).getTipoContabilita();
	}
	
	public String getCodContabilita(BasicBD bd) throws ServiceException {
		if(this.getCodContabilita() != null)
			return this.getCodContabilita();
		else
			return this.getTributo(bd).getCodContabilita();
	}

	public List<Pagamento> getPagamenti(BasicBD bd) throws ServiceException  {
		if(this.pagamenti == null) {
			PagamentiBD pagamentiBD = new PagamentiBD(bd);
			this.pagamenti = pagamentiBD.getPagamentiBySingoloVersamento(this.getId());
		}
		return pagamenti;
	}

	public void setPagamenti(List<Pagamento> pagamenti) {
		this.pagamenti = pagamenti;
	}

	public List<Rendicontazione> getRendicontazioni(BasicBD bd) throws ServiceException  {
		if(this.rendicontazioni == null) {
			RendicontazioniBD rendicontazioniBD = new RendicontazioniBD(bd);
			this.rendicontazioni = rendicontazioniBD.getRendicontazioniBySingoloVersamento(this.getId());
		}
		return rendicontazioni;
	}

	public void setRendicontazioni(List<Rendicontazione> rendicontazioni) {
		this.rendicontazioni = rendicontazioni;
	}
}

