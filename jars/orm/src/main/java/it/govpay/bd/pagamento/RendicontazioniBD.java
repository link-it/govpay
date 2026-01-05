/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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
import java.util.Calendar;
import java.util.Date;
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
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.GovpayConfig;
import it.govpay.bd.model.Rendicontazione;
import it.govpay.bd.model.converter.RendicontazioneConverter;
import it.govpay.bd.pagamento.filters.RendicontazioneFilter;
import it.govpay.model.Rendicontazione.StatoRendicontazione;
import it.govpay.model.exception.CodificaInesistenteException;
import it.govpay.orm.IdRendicontazione;
import it.govpay.orm.dao.jdbc.JDBCRendicontazioneServiceSearch;
import it.govpay.orm.dao.jdbc.converter.RendicontazioneFieldConverter;
import it.govpay.orm.model.RendicontazioneModel;

public class RendicontazioniBD extends BasicBD {

	private static final String CF_ID_SINGOLO_VERSAMENTO = "id_singolo_versamento";
	private static final String CF_ID_PAGAMENTO = "id_pagamento";

	public RendicontazioniBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public RendicontazioniBD(String idTransaction) {
		super(idTransaction);
	}
	
	public RendicontazioniBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}
	
	public RendicontazioniBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache());
	}
	
	public RendicontazioneFilter newFilter() {
		return new RendicontazioneFilter(this.getRendicontazioneService());
	}
	
	public RendicontazioneFilter newFilter(boolean simpleSearch) {
		return new RendicontazioneFilter(this.getRendicontazioneService(),simpleSearch);
	}

	public Rendicontazione insert(Rendicontazione dto) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.Rendicontazione vo = RendicontazioneConverter.toVO(dto);
			this.getRendicontazioneService().create(vo);
			dto.setId(vo.getId());
			return dto;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public void updateRendicontazione(Rendicontazione dto) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.Rendicontazione vo = RendicontazioneConverter.toVO(dto);
			IdRendicontazione idRendicontazione = new IdRendicontazione();
			idRendicontazione.setId(dto.getId());
			idRendicontazione.setIdRendicontazione(dto.getId());
			this.getRendicontazioneService().update(idRendicontazione, vo);
			dto.setId(vo.getId());
		} catch (NotImplementedException | NotFoundException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public List<Rendicontazione> findAll(RendicontazioneFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getRendicontazioneService());
			}
			
			List<it.govpay.orm.Rendicontazione> rendicontazioneVOLst = this
					.getRendicontazioneService().findAll(
							filter.toPaginatedExpression());
			return RendicontazioneConverter.toDTO(rendicontazioneVOLst);
		} catch (NotImplementedException | CodificaInesistenteException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public long count(RendicontazioneFilter filter) throws ServiceException {
		return filter.isEseguiCountConLimit() ? this.countConLimitEngine(filter) : this.countSenzaLimitEngine(filter);
	}
	
	private long countSenzaLimitEngine(RendicontazioneFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getRendicontazioneService());
			}
			
			return this.getRendicontazioneService().count(filter.toExpression()).longValue();
	
		} catch (NotImplementedException e) {
			return 0;
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	private long countConLimitEngine(RendicontazioneFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			int limitInterno = GovpayConfig.getInstance().getMaxRisultati();
			
			ISQLQueryObject sqlQueryObjectInterno = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			ISQLQueryObject sqlQueryObjectDistinctID = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			
			RendicontazioneFieldConverter converter = new RendicontazioneFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			RendicontazioneModel model = it.govpay.orm.Rendicontazione.model();
			/*
			SELECT count(distinct id) 
				FROM
				  (
				  SELECT versamenti.id
				  FROM versamenti
				  WHERE ...restrizioni di autorizzazione o ricerca...
				  ORDER BY data_richiesta 
				  LIMIT K
				  ) a
				)
			*/
			
			sqlQueryObjectInterno.addFromTable(converter.toTable(model.STATO));
			sqlQueryObjectInterno.addSelectField(converter.toTable(model.STATO), "id");

			sqlQueryObjectInterno.setANDLogicOperator(true);
			
			// creo condizioni
			sqlQueryObjectInterno = filter.toWhereCondition(sqlQueryObjectInterno);
			// preparo parametri
			Object[] parameters = filter.getParameters(sqlQueryObjectInterno);
			
			sqlQueryObjectInterno.setLimit(limitInterno);
			
			sqlQueryObjectDistinctID.addFromTable(sqlQueryObjectInterno);
			sqlQueryObjectDistinctID.addSelectCountField("id","id",true);
			
			String sql = sqlQueryObjectDistinctID.createSQLQuery();
			List<Class<?>> returnTypes = new ArrayList<>();
			returnTypes.add(Long.class); // Count
			
			List<List<Object>> nativeQuery = this.getRendicontazioneService().nativeQuery(sql, returnTypes, parameters);
			
			Long count = 0L;
			for (List<Object> row : nativeQuery) {
				count = BasicBD.getValueOrNull(row.get(0), Long.class);
			}
			
			return count.longValue();
		} catch (NotImplementedException | SQLQueryObjectException | ExpressionException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			return 0;
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	/**
	 * Recupera la rendicontazione identificata dalla chiave fisica
	 */
	public Rendicontazione getRendicontazione(long id) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.Rendicontazione rendicontazione = ((JDBCRendicontazioneServiceSearch)this.getRendicontazioneService()).get(id);
			return RendicontazioneConverter.toDTO(rendicontazione);
		} catch (NotImplementedException | NotFoundException | MultipleResultException | CodificaInesistenteException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public List<Rendicontazione> getRendicontazioniBySingoloVersamento(long idSingoloVersamento) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IPaginatedExpression exp = this.getRendicontazioneService()
					.newPaginatedExpression();
			RendicontazioneFieldConverter fieldConverter = new RendicontazioneFieldConverter(this.getJdbcProperties().getDatabaseType());
			exp.equals(new CustomField(CF_ID_SINGOLO_VERSAMENTO, Long.class, CF_ID_SINGOLO_VERSAMENTO,fieldConverter.toTable(it.govpay.orm.Rendicontazione.model())),	idSingoloVersamento);
			List<it.govpay.orm.Rendicontazione> singoliPagamenti = this.getRendicontazioneService().findAll(exp);
			return RendicontazioneConverter.toDTO(singoliPagamenti);
		} catch (NotImplementedException | ExpressionNotImplementedException | ExpressionException | CodificaInesistenteException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	
	public Rendicontazione getRendicontazione(String codDominio, String iuv, String iur, Integer indiceDati, StatoRendicontazione stato, boolean pagamentoNull) throws ServiceException, NotFoundException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IExpression exp = this.getRendicontazioneService().newExpression();
			
			RendicontazioneFieldConverter fieldConverter = new RendicontazioneFieldConverter(this.getJdbcProperties().getDatabaseType());
			RendicontazioneModel model = it.govpay.orm.Rendicontazione.model();
			
			exp.equals(model.ID_FR.COD_DOMINIO, codDominio);
			exp.and();
			exp.equals(model.IUV, iuv);
			exp.equals(model.IUR, iur);
			
			if(indiceDati != null) {
				IExpression orIndiceDati = this.getRendicontazioneService().newExpression();
				orIndiceDati.equals(model.INDICE_DATI, indiceDati).or().isNull(model.INDICE_DATI);
				exp.and(orIndiceDati);
			}
			
			exp.equals(model.STATO, stato.toString());
			
			CustomField cf = new  CustomField(CF_ID_PAGAMENTO, Long.class, CF_ID_PAGAMENTO,fieldConverter.toTable(it.govpay.orm.Rendicontazione.model()));
			if(pagamentoNull) {
				exp.isNull(cf);
			} else {
				exp.isNotNull(cf);
			}
			
			it.govpay.orm.Rendicontazione rendicontazione = this.getRendicontazioneService().find(exp);
			return RendicontazioneConverter.toDTO(rendicontazione);
		} catch (NotImplementedException | MultipleResultException | ExpressionNotImplementedException | ExpressionException | CodificaInesistenteException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public List<Rendicontazione> getRendicontazioniSenzaPagamento(String codDominio, Integer numeroMassimoGiorniSogliaRicerca) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			// -- Rendicontazioni di cui recuperare la RT
			// select * from rendicontazioni where id_singolo_versamento is not null and id_pagamento is null and data > now() - interval '8 weeks' order by data desc
			RendicontazioneFieldConverter fieldConverter = new RendicontazioneFieldConverter(this.getJdbcProperties().getDatabaseType());
			RendicontazioneModel model = it.govpay.orm.Rendicontazione.model();
			
			IPaginatedExpression exp = this.getRendicontazioneService().newPaginatedExpression();
			exp.equals(model.ID_FR.COD_DOMINIO, codDominio);
			
			CustomField cfIdSV = new  CustomField(CF_ID_SINGOLO_VERSAMENTO, Long.class, CF_ID_SINGOLO_VERSAMENTO,fieldConverter.toTable(it.govpay.orm.Rendicontazione.model()));
			exp.isNotNull(cfIdSV);
			
			CustomField cfIdPag = new  CustomField(CF_ID_PAGAMENTO, Long.class, CF_ID_PAGAMENTO,fieldConverter.toTable(it.govpay.orm.Rendicontazione.model()));
			exp.isNull(cfIdPag);
			
			// filtro temporale sui giorni
			Date now = new Date();
			Calendar c = Calendar.getInstance();
			c.setTime(now);
			c.add(Calendar.DATE, - numeroMassimoGiorniSogliaRicerca);
			
			exp.and();
			exp.greaterThan(model.DATA, c.getTime());
			
			List<it.govpay.orm.Rendicontazione> rendicontazioneVOLst = this.getRendicontazioneService().findAll(exp);
			return RendicontazioneConverter.toDTO(rendicontazioneVOLst);
		} catch(NotImplementedException | ExpressionNotImplementedException | ExpressionException | CodificaInesistenteException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
}
