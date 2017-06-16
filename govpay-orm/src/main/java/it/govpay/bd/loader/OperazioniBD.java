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
package it.govpay.bd.loader;

import it.govpay.bd.BasicBD;
import it.govpay.bd.loader.converter.OperazioneConverter;
import it.govpay.bd.loader.filters.OperazioneFilter;
import it.govpay.orm.Operazione;
import it.govpay.orm.dao.IDBOperazioneService;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;

public class OperazioniBD extends BasicBD {

	public OperazioniBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	private List<it.govpay.bd.loader.model.Operazione> findAll(IPaginatedExpression exp) throws ServiceException, NotImplementedException {
		List<Operazione> findAll = this.getOperazioneService().findAll(exp);
		List<it.govpay.bd.loader.model.Operazione> findAllDTO = new ArrayList<it.govpay.bd.loader.model.Operazione>(); 
		for(Operazione caricamento : findAll) {
			findAllDTO.add(OperazioneConverter.toDTO(caricamento));
		}
		return findAllDTO;
	}
	
	
	public void insertOperazione(it.govpay.bd.loader.model.Operazione caricamento) throws ServiceException {
		try{
			Operazione caricamentoVo = OperazioneConverter.toVO(caricamento);
			this.getOperazioneService().create(caricamentoVo);
		} catch(NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	public void updateOperazione(it.govpay.bd.loader.model.Operazione caricamento) throws ServiceException {
		try{
			Operazione caricamentoVo = OperazioneConverter.toVO(caricamento);
			this.getOperazioneService().update(caricamentoVo);
		} catch(NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		}
	}
	
	public OperazioneFilter newFilter() throws ServiceException {
		return new OperazioneFilter(this.getOperazioneService());
	}
	
	public OperazioneFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new OperazioneFilter(this.getOperazioneService(),simpleSearch);
	}
	
	public long count(OperazioneFilter filter) throws ServiceException {
		try {
			return this.getOperazioneService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<it.govpay.bd.loader.model.Operazione> findAll(OperazioneFilter filter) throws ServiceException {
		try {
			return this.findAll(filter.toPaginatedExpression());
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	public it.govpay.bd.loader.model.Operazione getOperazione(long id) throws ServiceException, NotFoundException {
		try {
			it.govpay.orm.Operazione operazione = ((IDBOperazioneService)this.getOperazioneService()).get(id);
			return OperazioneConverter.toDettaglioDTO(operazione);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}
	}
}
