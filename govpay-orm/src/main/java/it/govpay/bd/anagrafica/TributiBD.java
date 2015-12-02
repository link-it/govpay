/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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
package it.govpay.bd.anagrafica;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.filters.TributoFilter;
import it.govpay.bd.model.Tributo;
import it.govpay.bd.model.converter.TributoConverter;
import it.govpay.orm.IdEnte;
import it.govpay.orm.IdTributo;
import it.govpay.orm.dao.jdbc.JDBCTributoServiceSearch;
import it.govpay.orm.dao.jdbc.converter.ApplicazioneFieldConverter;

import java.util.List;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;
import org.openspcoop2.utils.UtilsException;

public class TributiBD extends BasicBD {

	public TributiBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	/**
	 * Recupera il tributo identificato dalla chiave fisica
	 * 
	 * @param idTributo
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Tributo getTributo(Long idTributo) throws NotFoundException, MultipleResultException, ServiceException {
		if(idTributo == null) {
			throw new ServiceException("Parameter 'id' cannot be NULL");
		}
		
		long id = idTributo.longValue();

		try {
			return TributoConverter.toDTO(((JDBCTributoServiceSearch)this.getServiceManager().getTributoService()).get(id));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Recupera il tributo identificato dalla chiave logica
	 * 
	 * @param idApplicazione
	 * @param codTributo
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Tributo getTributo(Long longIdEnte, String codTributo) throws NotFoundException, MultipleResultException, ServiceException {
		if(longIdEnte == null) {
			throw new ServiceException("Parameter 'idEnte' cannot be NULL");
		}
		
		long idEnte = longIdEnte.longValue();
		
		try {
			IdTributo idTributo = new IdTributo();
			IdEnte logicIdEnte = new IdEnte();
			logicIdEnte.setId(idEnte);
			idTributo.setIdEnte(logicIdEnte);
			idTributo.setCodTributo(codTributo);
			
			return TributoConverter.toDTO(this.getServiceManager().getTributoService().get(idTributo));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * recupera i tributi afferenti ad una applicazione
	 * @param idApplicazione
	 * @return
	 * @throws NotFoundException
	 * @throws ServiceException
	 */
	public List<Tributo> getTributi(long idApplicazione) throws NotFoundException, ServiceException {
		try {
			
			IPaginatedExpression exp = this.getServiceManager().getTributoServiceSearch().newPaginatedExpression();
			ApplicazioneFieldConverter fieldConverter = new ApplicazioneFieldConverter(this.getServiceManager().getJdbcProperties().getDatabase());
			exp.equals(new CustomField("id_applicazione", Long.class, "id_applicazione", fieldConverter.toTable(it.govpay.orm.Applicazione.model().APPLICAZIONE_TRIBUTO)), idApplicazione);
			
			return TributoConverter.toDTOList(this.getServiceManager().getTributoService().findAll(exp));
			
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Aggiorna il tributo
	 * 
	 * @param tributo
	 * @throws NotFoundException
	 * @throws ServiceException
	 */
	public void updateTributo(Tributo tributo) throws NotFoundException, ServiceException {
		try {
			it.govpay.orm.Tributo vo = TributoConverter.toVO(tributo);
			
			IdTributo idVO = this.getServiceManager().getTributoServiceSearch().convertToId(vo);
			
			if(!this.getServiceManager().getTributoServiceSearch().exists(idVO)) {
				throw new NotFoundException("Tributo con id ["+idVO.toJson()+"] non trovato.");
			}
			
			this.getServiceManager().getTributoService().update(idVO, vo);
			tributo.setId(vo.getId());
			
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (UtilsException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}

	}
	
	
	/**
	 * Crea un nuovo tributo
	 * @param ente
	 * @throws NotPermittedException
	 * @throws ServiceException
	 */
	public void insertTributo(Tributo tributo) throws ServiceException {
		try {
			it.govpay.orm.Tributo vo = TributoConverter.toVO(tributo);
			
			this.getServiceManager().getTributoService().create(vo);
			tributo.setId(vo.getId());

		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}

	}
	
	
	public TributoFilter newFilter() throws ServiceException {
		try {
			return new TributoFilter(this.getServiceManager().getTributoServiceSearch());
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public long count(TributoFilter filter) throws ServiceException {
		try {
			return this.getServiceManager().getTributoServiceSearch().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<Tributo> findAll(TributoFilter filter) throws ServiceException {
		try {
			return TributoConverter.toDTOList(this.getServiceManager().getTributoServiceSearch().findAll(filter.toPaginatedExpression()));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	
}
