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

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Incasso;
import it.govpay.bd.model.converter.IncassoConverter;
import it.govpay.bd.pagamento.filters.IncassoFilter;
import it.govpay.orm.IdIncasso;

public class IncassiBD extends BasicBD {
	
	public IncassiBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public Incasso getIncasso(long id) throws ServiceException {
		try {
			IdIncasso idIncasso = new IdIncasso();
			idIncasso.setId(id);
			it.govpay.orm.Incasso pagamentoVO = this.getIncassoService().get(idIncasso);
			return IncassoConverter.toDTO(pagamentoVO);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		}
	}
	
	public Incasso getIncasso(String codDominio, String trn) throws ServiceException, NotFoundException {
		try {
			IdIncasso idIncasso = new IdIncasso();
			//TODO idIncasso.setCodDominio(codDominio)
			idIncasso.setTrn(trn);
			it.govpay.orm.Incasso pagamentoVO = this.getIncassoService().get(idIncasso);
			return IncassoConverter.toDTO(pagamentoVO);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}
	}
	
	public void insertIncasso(Incasso incasso) throws ServiceException {
		try {
			it.govpay.orm.Incasso vo = IncassoConverter.toVO(incasso);
			this.getIncassoService().create(vo);
			incasso.setId(vo.getId());
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public IncassoFilter newFilter() throws ServiceException {
		return new IncassoFilter(this.getIncassoService());
	}

	public IncassoFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new IncassoFilter(this.getIncassoService(),simpleSearch);
	}

	public long count(IncassoFilter filter) throws ServiceException {
		try {
			return this.getIncassoService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<Incasso> findAll(IncassoFilter filter) throws ServiceException {
		try {
			List<Incasso> incassoLst = new ArrayList<Incasso>();

			if(filter.getCodDomini() != null && filter.getCodDomini().isEmpty()) return incassoLst;
			List<it.govpay.orm.Incasso> incassoVOLst = this.getIncassoService().findAll(filter.toPaginatedExpression()); 
			for(it.govpay.orm.Incasso incassoVO: incassoVOLst) {
				incassoLst.add(IncassoConverter.toDTO(incassoVO));
			}
			return incassoLst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
}
