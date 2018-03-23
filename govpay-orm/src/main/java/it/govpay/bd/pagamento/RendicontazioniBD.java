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

import java.util.List;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Rendicontazione;
import it.govpay.bd.model.converter.PagamentoConverter;
import it.govpay.bd.model.converter.RendicontazioneConverter;
import it.govpay.bd.pagamento.filters.RendicontazioneFilter;
import it.govpay.orm.IdPagamento;
import it.govpay.orm.IdRendicontazione;

public class RendicontazioniBD extends BasicBD {

	public RendicontazioniBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public RendicontazioneFilter newFilter() throws ServiceException {
		return new RendicontazioneFilter(this.getRendicontazioneService());
	}
	
	public RendicontazioneFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new RendicontazioneFilter(this.getRendicontazioneService(),simpleSearch);
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
	
	public void updateRendicontazione(Rendicontazione dto) throws ServiceException {
		try {
			it.govpay.orm.Rendicontazione vo = RendicontazioneConverter.toVO(dto);
			IdRendicontazione idRendicontazione = new IdRendicontazione();
			idRendicontazione.setId(dto.getId());
			idRendicontazione.setIdRendicontazione(dto.getId());
			this.getRendicontazioneService().update(idRendicontazione, vo);
			dto.setId(vo.getId());
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
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
	
	public long count(RendicontazioneFilter filter) throws ServiceException {
		try {
			return this.getRendicontazioneService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Recupera la rendicontazione identificata dalla chiave fisica
	 */
	public Rendicontazione getRendicontazione(long id) throws ServiceException {
		try {
			IdRendicontazione idRendicontazione = new IdRendicontazione();
			idRendicontazione.setId(id);
			it.govpay.orm.Rendicontazione rendicontazione = this.getRendicontazioneService().get(idRendicontazione);
			return RendicontazioneConverter.toDTO(rendicontazione);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}
	}
}
