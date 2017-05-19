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
package it.govpay.bd.anagrafica;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.filters.UnitaOperativaFilter;
import it.govpay.bd.model.converter.UnitaOperativaConverter;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.orm.IdDominio;
import it.govpay.orm.IdUo;
import it.govpay.orm.dao.jdbc.JDBCUoServiceSearch;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.UtilsException;

public class UnitaOperativeBD extends BasicBD {

	public UnitaOperativeBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public UnitaOperativa getUnitaOperativa(Long id) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			it.govpay.orm.Uo uoVO = ((JDBCUoServiceSearch) this.getUoService()).get(id);
			UnitaOperativa uo = UnitaOperativaConverter.toDTO(uoVO);
			return uo;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	public UnitaOperativa getUnitaOperativa(Long idDominio, String codUnitaOperativa) throws NotFoundException, ServiceException {
		try {
			IdUo id = new IdUo();
			id.setCodUo(codUnitaOperativa);
			IdDominio idDominioOrm = new IdDominio();
			idDominioOrm.setId(idDominio);
			id.setIdDominio(idDominioOrm);
			it.govpay.orm.Uo uoVO = this.getUoService().get(id);
			return UnitaOperativaConverter.toDTO(uoVO);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}
	}
	
	public void updateUnitaOperativa(UnitaOperativa uo) throws NotFoundException, ServiceException {
		try {
			it.govpay.orm.Uo vo = UnitaOperativaConverter.toVO(uo);
			IdUo idUnitaOperativa = this.getUoService().convertToId(vo);
			if(!this.getUoService().exists(idUnitaOperativa)) {
				throw new NotFoundException("UnitaOperativa con id ["+idUnitaOperativa.toJson()+"] non trovato");
			}
			this.getUoService().update(idUnitaOperativa, vo);
			AnagraficaManager.removeFromCache(uo);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} catch (UtilsException e) {
			throw new ServiceException(e);
		}
	}
	
	public void insertUnitaOperativa(UnitaOperativa uo) throws ServiceException{
		try {
			it.govpay.orm.Uo vo = UnitaOperativaConverter.toVO(uo);
			this.getUoService().create(vo);
			uo.setId(vo.getId());
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	public UnitaOperativaFilter newFilter() throws ServiceException {
		return new UnitaOperativaFilter(this.getUoService(), this.getJdbcProperties().getDatabaseType());
	}
	
	public UnitaOperativaFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new UnitaOperativaFilter(this.getUoService(), this.getJdbcProperties().getDatabaseType(),simpleSearch);
	}

	public long count(UnitaOperativaFilter filter) throws ServiceException {
		try {
			return this.getUoService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<UnitaOperativa> findAll(UnitaOperativaFilter filter) throws ServiceException {
		try {
			List<UnitaOperativa> lst = new ArrayList<UnitaOperativa>();
			List<it.govpay.orm.Uo> lstuoVo = this.getUoService().findAll(filter.toPaginatedExpression());
			for(it.govpay.orm.Uo uoVO: lstuoVo) {
				lst.add(UnitaOperativaConverter.toDTO(uoVO));
			}
			return lst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

}
