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

import java.util.ArrayList;
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

import it.govpay.bd.BasicBD;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.anagrafica.filters.TipoVersamentoDominioFilter;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.TipoVersamentoDominio;
import it.govpay.bd.model.converter.TipoVersamentoDominioConverter;
import it.govpay.model.TipoVersamento;
import it.govpay.orm.IdDominio;
import it.govpay.orm.IdTipoVersamento;
import it.govpay.orm.IdTipoVersamentoDominio;
import it.govpay.orm.dao.jdbc.JDBCTipoVersamentoDominioServiceSearch;
import it.govpay.orm.dao.jdbc.converter.TipoVersamentoDominioFieldConverter;

public class TipiVersamentoDominiBD extends BasicBD {

	public TipiVersamentoDominiBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	/**
	 * Recupera il tipoVersamento identificato dalla chiave fisica
	 * 
	 * @param idTipoVersamento
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public TipoVersamentoDominio getTipoVersamentoDominio(Long idTipoVersamentoDominio) throws NotFoundException, MultipleResultException, ServiceException {
		if(idTipoVersamentoDominio == null) {
			throw new ServiceException("Parameter 'id' cannot be NULL");
		}
		
		long id = idTipoVersamentoDominio.longValue();

		try {
			return TipoVersamentoDominioConverter.toDTO(((JDBCTipoVersamentoDominioServiceSearch)this.getTipoVersamentoDominioService()).get(id));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	
	/**
	 * Recupera il tipoVersamento identificato dal codice
	 * 
	 * @param codTipoVersamentoDominio
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public TipoVersamentoDominio getTipoVersamentoDominio(Long idDominio, String codTipoVersamento) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			IdTipoVersamentoDominio idTipoVersamentoDominio = new IdTipoVersamentoDominio();
			IdDominio idDominioOrm = new IdDominio();
			IdTipoVersamento idTipoVersamento = new IdTipoVersamento();
			idDominioOrm.setId(idDominio);
			idTipoVersamento.setCodTipoVersamento(codTipoVersamento);
			idTipoVersamentoDominio.setIdDominio(idDominioOrm);
			idTipoVersamentoDominio.setIdTipoVersamento(idTipoVersamento);
			return TipoVersamentoDominioConverter.toDTO( this.getTipoVersamentoDominioService().get(idTipoVersamentoDominio));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Aggiorna il tipoVersamento
	 * 
	 * @param tipoVersamento
	 * @throws NotFoundException
	 * @throws ServiceException
	 */
	public void updateTipoVersamentoDominio(TipoVersamentoDominio tipoVersamento) throws NotFoundException, ServiceException {
		try {
			it.govpay.orm.TipoVersamentoDominio vo = TipoVersamentoDominioConverter.toVO(tipoVersamento);
			IdTipoVersamentoDominio idVO = this.getTipoVersamentoDominioService().convertToId(vo);
			if(!this.getTipoVersamentoDominioService().exists(idVO)) {
				throw new NotFoundException("TipoVersamentoDominio con id ["+idVO.toJson()+"] non trovato.");
			}
			this.getTipoVersamentoDominioService().update(idVO, vo);
			tipoVersamento.setId(vo.getId());
			this.emitAudit(tipoVersamento);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (UtilsException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}

	}
	
	/**
	 * Crea un nuovo tipoVersamento
	 * @param ente
	 * @throws NotPermittedException
	 * @throws ServiceException
	 */
	public TipoVersamentoDominio autoCensimentoTipoVersamentoDominio(TipoVersamento tipoVersamento, Dominio dominio) throws ServiceException {
		TipoVersamentoDominio tipoVersamentoDominio = new TipoVersamentoDominio();
		tipoVersamentoDominio.setCodTipoVersamento(tipoVersamento.getCodTipoVersamento());
		tipoVersamentoDominio.setIdTipoVersamento(tipoVersamento.getId());
		tipoVersamentoDominio.setDescrizione(tipoVersamento.getDescrizione());
		tipoVersamentoDominio.setIdDominio(dominio.getId());
		this.insertTipoVersamentoDominio(tipoVersamentoDominio);
		return tipoVersamentoDominio;
	}
	
	/**
	 * Crea un nuovo tipoVersamento
	 * @param ente
	 * @throws NotPermittedException
	 * @throws ServiceException
	 */
	public void insertTipoVersamentoDominio(TipoVersamentoDominio tipoVersamento) throws ServiceException {
		try {
			it.govpay.orm.TipoVersamentoDominio vo = TipoVersamentoDominioConverter.toVO(tipoVersamento);
			this.getTipoVersamentoDominioService().create(vo);
			tipoVersamento.setId(vo.getId());
			this.emitAudit(tipoVersamento);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	public TipoVersamentoDominioFilter newFilter() throws ServiceException {
		return new TipoVersamentoDominioFilter(this.getTipoVersamentoDominioService());
	}
	
	public TipoVersamentoDominioFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new TipoVersamentoDominioFilter(this.getTipoVersamentoDominioService(),simpleSearch);
	}

	public long count(TipoVersamentoDominioFilter filter) throws ServiceException {
		try {
			return this.getTipoVersamentoDominioService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<TipoVersamentoDominio> findAll(TipoVersamentoDominioFilter filter) throws ServiceException {
		try {
			return TipoVersamentoDominioConverter.toDTOList(this.getTipoVersamentoDominioService().findAll(filter.toPaginatedExpression()));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	public List<Long> getIdTipiVersamentoDefinitiPerDominio(Long idDominio) throws ServiceException {
		List<Long> lstIdTipiTributi = new ArrayList<>();

		try {
			IPaginatedExpression pagExpr = this.getTipoVersamentoDominioService().newPaginatedExpression();

			TipoVersamentoDominioFieldConverter converter = new TipoVersamentoDominioFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			CustomField cfIdDominio = new CustomField("id_dominio", Long.class, "id_dominio", converter.toTable(it.govpay.orm.TipoVersamentoDominio.model()));
			pagExpr.equals(cfIdDominio, idDominio);

			CustomField cfIdTipoVersamento = new CustomField("id_tipo_versamento", Long.class, "id_tipo_versamento", converter.toTable(it.govpay.orm.TipoVersamentoDominio.model()));
			List<Object> select = this.getTipoVersamentoDominioService().select(pagExpr, true, cfIdTipoVersamento);

			if(select != null && select.size() > 0)
				for (Object object : select) {
					if(object instanceof Long){
						lstIdTipiTributi.add((Long) object); 
					}
				}
		}catch(ServiceException e){
			throw e;
		} catch (NotFoundException e) {
			return new ArrayList<>();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		}
		return lstIdTipiTributi;
	}
}
