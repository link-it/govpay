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

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;

import it.govpay.bd.BasicBD;
import it.govpay.bd.operazioni.filters.TracciatoFilter;
import it.govpay.bd.model.converter.TracciatoConverter;
import it.govpay.orm.Tracciato;

public class TracciatiBD extends BasicBD {

	public TracciatiBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	private List<it.govpay.bd.model.Tracciato> findAll(IPaginatedExpression exp) throws ServiceException, NotImplementedException {
		List<Tracciato> findAll = this.getTracciatoService().findAll(exp);
		List<it.govpay.bd.model.Tracciato> findAllDTO = new ArrayList<it.govpay.bd.model.Tracciato>(); 
		for(Tracciato tracciato : findAll) {
			findAllDTO.add(TracciatoConverter.toDTO(tracciato));
		}
		return findAllDTO;
	}
	
	
	public void insertTracciato(it.govpay.bd.model.Tracciato tracciato) throws ServiceException {
		try{
			Tracciato tracciatoVo = TracciatoConverter.toVO(tracciato);
			this.getTracciatoService().create(tracciatoVo);
			
			tracciato.setId(tracciatoVo.getId());
		} catch(NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	public void updateTracciato(it.govpay.bd.model.Tracciato tracciato) throws ServiceException {
		try{
			Tracciato tracciatoVo = TracciatoConverter.toVO(tracciato);
			this.getTracciatoService().update(this.getTracciatoService().convertToId(tracciatoVo), tracciatoVo);
		} catch(NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		}
	}
	
	public TracciatoFilter newFilter() throws ServiceException {
		return new TracciatoFilter(this.getTracciatoService());
	}
	
	public TracciatoFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new TracciatoFilter(this.getTracciatoService(),simpleSearch);
	}
	
	public long count(TracciatoFilter filter) throws ServiceException {
		try {
			return this.getTracciatoService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<it.govpay.bd.model.Tracciato> findAll(TracciatoFilter filter) throws ServiceException {
		try {
			return this.findAll(filter.toPaginatedExpression());
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	public it.govpay.bd.model.Tracciato getTracciato(long id) throws ServiceException, NotFoundException {
		try {
			it.govpay.orm.IdTracciato idTracciato = new it.govpay.orm.IdTracciato();
			idTracciato.setId(id);
			it.govpay.orm.Tracciato versamento = this.getTracciatoService().get(idTracciato);
			return TracciatoConverter.toDTO(versamento);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}
	}
}
