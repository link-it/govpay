/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
import it.govpay.bd.pagamento.IuvBD;
import it.govpay.bd.pagamento.SingoliVersamentiBD;
import it.govpay.model.Iuv;

public class RendicontazioneSenzaRpt extends it.govpay.model.RendicontazioneSenzaRpt {

	private static final long serialVersionUID = 1L;
	
	// Business
	
	private SingoloVersamento singoloVersamento;
	
	public SingoloVersamento getSingoloVersamento(BasicBD bd) throws ServiceException {
		if(singoloVersamento == null) {
			SingoliVersamentiBD singoliVersamentiBD = new SingoliVersamentiBD(bd);
			singoloVersamento = singoliVersamentiBD.getSingoloVersamento(this.getIdSingoloVersamento());
		}
		return singoloVersamento;
	}
	
	private Iuv iuv;
	
	public Iuv getIuv(BasicBD bd) throws ServiceException {
		if(iuv == null) {
			IuvBD iuvBD = new IuvBD(bd);
			iuv = iuvBD.getIuv(this.getIdIuv());
		}
		return iuv;
	}
}
