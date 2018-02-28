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
package it.govpay.rs.v1.beans;

import java.math.BigDecimal;

import org.codehaus.jackson.map.annotate.JsonFilter;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.model.IbanAccredito;

@JsonFilter(value="entrate")  
public class Entrata extends it.govpay.rs.v1.beans.base.Entrata{

	public Entrata(it.govpay.bd.model.Tributo tributo, IbanAccredito ibanAccredito) throws ServiceException {
		this.codiceContabilita(tributo.getCodContabilita())
		.codificaIUV(new BigDecimal(tributo.getCodTributoIuv()))
		.abilitato(tributo.isAbilitato())
		.idEntrata(tributo.getCodTributo())
		.tipoEntrata(new Tipoentrata(tributo))
		.ibanAccreditoBancario(tributo.getIbanAccredito().getCodIban())
		.ibanAccreditoPostale(ibanAccredito.getCodIban())
		.tipoContabilita(TipoContabilitaEnum.fromValue(tributo.getTipoContabilita().toString()));
	}
	
	@Override
	public String getJsonIdFilter() {
		return "entrate";
	}
}

