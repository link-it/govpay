/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2023 Link.it srl (http://www.link.it).
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
import it.govpay.bd.pagamento.FrBD;
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.bd.pagamento.VersamentiBD;

public class Rendicontazione extends it.govpay.model.Rendicontazione {

	private static final long serialVersionUID = 1L;
	
	private transient Fr fr;
	private transient Pagamento pagamento;
	private transient boolean pagamentoDaCreare = false;
	private transient Versamento versamento;
	private transient SingoloVersamento singoloVersamento;
	
	public Fr getFr(BasicBD bd) throws ServiceException {
		if(this.fr == null) {
			FrBD frBD = new FrBD(bd);
			this.fr = frBD.getFr(this.getIdFr());
		}
		return this.fr;
	}
	public void setFr(Fr fr) {
		this.fr = fr;
	}
	public Pagamento getPagamento(BasicBD bd) throws ServiceException {
		if(this.pagamento == null && this.getIdPagamento() != null) {
			PagamentiBD pagamentiBD = new PagamentiBD(bd);
			pagamentiBD.setAtomica(false); 
			this.pagamento = pagamentiBD.getPagamento(this.getIdPagamento());
		}
		return this.pagamento;
	}
	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}
	public boolean isPagamentoDaCreare() {
		return this.pagamentoDaCreare;
	}
	public void setPagamentoDaCreare(boolean pagamentoDaCreare) {
		this.pagamentoDaCreare = pagamentoDaCreare;
	}
	
	public Versamento getVersamento(BasicBD bd) throws ServiceException {
		if(this.versamento == null) {
			if(this.getPagamento(bd) != null)
				this.versamento = this.getPagamento(bd).getSingoloVersamento(bd).getVersamentoBD(bd);
		}
		return this.versamento;
	}
	
	public void setVersamento(Versamento versamento) {
		this.versamento = versamento;
	}
	
	public SingoloVersamento getSingoloVersamento(BasicBD bd) throws ServiceException {
		if(this.singoloVersamento == null) {
			VersamentiBD singoliVersamentiBD = new VersamentiBD(bd);
			this.singoloVersamento = singoliVersamentiBD.getSingoloVersamento(this.getIdSingoloVersamento());
		}
		return this.singoloVersamento;
	}

	public void setSingoloVersamento(SingoloVersamento singoloVersamento) {
		this.singoloVersamento = singoloVersamento;
		this.setIdSingoloVersamento(singoloVersamento.getId());
	}
}
