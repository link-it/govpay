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
package it.govpay.bd.pagamento;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Rendicontazione;
import it.govpay.bd.model.converter.RendicontazioneConverter;
import it.govpay.bd.pagamento.filters.RendicontazioneFilter;

import java.util.List;

import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;

public class RendicontazioniBD extends BasicBD {

	public RendicontazioniBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public RendicontazioneFilter newFilter() throws ServiceException {
		return new RendicontazioneFilter(this.getRendicontazioneService());
	}

	public Rendicontazione insert(Rendicontazione dto) throws ServiceException {
		try {
			it.govpay.orm.Rendicontazione vo = RendicontazioneConverter.toVO(dto);
			this.getRendicontazioneService().create(vo);
			dto.setId(vo.getId());
			return dto;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	public List<Rendicontazione> findAll(RendicontazioneFilter filter) throws ServiceException {
		try {
			List<it.govpay.orm.Rendicontazione> rendicontazioneVOLst = this
					.getRendicontazioneService().findAll(
							filter.toPaginatedExpression());
			return RendicontazioneConverter.toDTO(rendicontazioneVOLst);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
}
