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
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.UtilsException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.filters.TipoVersamentoDominioFilter;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.TipoVersamentoDominio;
import it.govpay.bd.model.converter.TipoVersamentoDominioConverter;
import it.govpay.model.TipoVersamento;
import it.govpay.orm.IdTipoVersamentoDominio;
import it.govpay.orm.dao.jdbc.JDBCTipoVersamentoDominioServiceSearch;
import it.govpay.orm.dao.jdbc.converter.TipoVersamentoDominioFieldConverter;
import it.govpay.orm.model.TipoVersamentoDominioModel;

public class TipiVersamentoDominiBD extends BasicBD {

	public TipiVersamentoDominiBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public TipiVersamentoDominiBD(String idTransaction) {
		super(idTransaction);
	}
	
	public TipiVersamentoDominiBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}
	
	public TipiVersamentoDominiBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache());
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
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			return TipoVersamentoDominioConverter.toDTO(((JDBCTipoVersamentoDominioServiceSearch)this.getTipoVersamentoDominioService()).get(id));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}


	/**
	 * Recupera il tipoVersamento identificato dal codice
	 * 
	 * @param idDominio
	 * @return
	 * @throws ServiceException
	 */
	public List<TipoVersamentoDominio> getTipiVersamentoDominioPortalePagamentoForm(Long idDominio) throws ServiceException {
		TipoVersamentoDominioFilter filter = this.newFilter();
		filter.setIdDominio(idDominio);
		filter.setFormPortalePagamento(true);
		filter.setSearchAbilitato(true);
		filter.setOffset(0);
		filter.getFilterSortList().add(new FilterSortWrapper(it.govpay.orm.TipoVersamentoDominio.model().TIPO_VERSAMENTO.DESCRIZIONE, SortOrder.ASC));
		return this.findAll(filter);
	}

	/**
	 * Recupera i tipiversamento dominio con pagamento portale form abilitato e definito
	 * 
	 * @param codTipoVersamentoDominio
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public TipoVersamentoDominio getTipoVersamentoDominio(Long idDominio, String codTipoVersamento) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			TipoVersamentoDominioFieldConverter converter = new TipoVersamentoDominioFieldConverter(this.getJdbcProperties().getDatabaseType());
			
			IExpression expr = this.getTipoVersamentoDominioService().newExpression();
			expr.equals(it.govpay.orm.TipoVersamentoDominio.model().TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO, codTipoVersamento);
			expr.and();
			expr.equals(new CustomField("id_dominio", Long.class, "id_dominio", converter.toTable(it.govpay.orm.TipoVersamentoDominio.model())), idDominio);
			
//			IdTipoVersamentoDominio idTipoVersamentoDominio = new IdTipoVersamentoDominio();
//			IdDominio idDominioOrm = new IdDominio();
//			IdTipoVersamento idTipoVersamento = new IdTipoVersamento();
//			idDominioOrm.setId(idDominio);
//			idTipoVersamento.setCodTipoVersamento(codTipoVersamento);
//			idTipoVersamentoDominio.setIdDominio(idDominioOrm);
//			idTipoVersamentoDominio.setIdTipoVersamento(idTipoVersamento);
			
			return TipoVersamentoDominioConverter.toDTO( this.getTipoVersamentoDominioService().find(expr));
		} catch (NotImplementedException | ExpressionNotImplementedException | ExpressionException e) { 
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
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
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
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
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
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
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.TipoVersamentoDominio vo = TipoVersamentoDominioConverter.toVO(tipoVersamento);
			this.getTipoVersamentoDominioService().create(vo);
			tipoVersamento.setId(vo.getId());
			this.emitAudit(tipoVersamento);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
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
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getTipoVersamentoDominioService());
			}
			
			return this.getTipoVersamentoDominioService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public List<TipoVersamentoDominio> findAll(TipoVersamentoDominioFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getTipoVersamentoDominioService());
			}
			
			return TipoVersamentoDominioConverter.toDTOList(this.getTipoVersamentoDominioService().findAll(filter.toPaginatedExpression()));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public List<Long> getIdTipiVersamentoDefinitiPerDominio(Long idDominio) throws ServiceException {
		List<Long> lstIdTipiTributi = new ArrayList<>();

		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IPaginatedExpression pagExpr = this.getTipoVersamentoDominioService().newPaginatedExpression();

			TipoVersamentoDominioFieldConverter converter = new TipoVersamentoDominioFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			TipoVersamentoDominioModel model = it.govpay.orm.TipoVersamentoDominio.model();
			CustomField cfIdDominio = new CustomField("id_dominio", Long.class, "id_dominio", converter.toTable(model));
			pagExpr.equals(cfIdDominio, idDominio);

			CustomField cfIdTipoVersamento = new CustomField("id_tipo_versamento", Long.class, "id_tipo_versamento", converter.toTable(model));
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
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
		return lstIdTipiTributi;
	}
	
	public List<Long> getIdDominiConFormDefinita(TipoVersamentoDominioFilter filter) throws ServiceException{
		TipoVersamentoDominioFieldConverter converter = new TipoVersamentoDominioFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
		TipoVersamentoDominioModel model = it.govpay.orm.TipoVersamentoDominio.model();
		return this.getIdDominiConFormDefinita(filter, converter, model);
	}

	private List<Long> getIdDominiConFormDefinita(TipoVersamentoDominioFilter filter, TipoVersamentoDominioFieldConverter converter, TipoVersamentoDominioModel model) throws ServiceException{
		List<Long> idDomini = new ArrayList<>();
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getTipoVersamentoDominioService());
			}
			
			IPaginatedExpression pagExpr = filter.toPaginatedExpression();

			CustomField cfIdDominio = new CustomField("id_dominio", Long.class, "id_dominio", converter.toTable(model));
			List<Object> select = this.getTipoVersamentoDominioService().select(pagExpr, true, cfIdDominio);

			if(select != null && select.size() > 0)
				for (Object object : select) {
					if(object instanceof Long){
						idDomini.add((Long) object); 
					}
				}

		}catch(ServiceException e){
			throw e;
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			return new ArrayList<>();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}  finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}

		return idDomini;
	}
}
