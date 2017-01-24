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
package it.govpay.bd.pagamento;

import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Rendicontazione;
import it.govpay.bd.pagamento.filters.RendicontazioneFilter;

public class RendicontazioniBD extends BasicBD {

	public RendicontazioniBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public RendicontazioneFilter newFilter() throws ServiceException {
		//TODO GP-439.3 Bussu 
		//return new RendicontazioneFilter(this.getRendicontazioneService());
		return null;
	}

	public Rendicontazione insert(Rendicontazione dto) {
		//TODO GP-439.3 Bussu
		return null;
	}
	
	public List<Rendicontazione> findAll(RendicontazioneFilter filter) {
		//TODO GP-439.3 Bussu
		return null;
	}
}
