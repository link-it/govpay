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
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.beans.UpdateField;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.GovpayConfig;
import it.govpay.bd.model.Notifica;
import it.govpay.bd.model.Promemoria;
import it.govpay.bd.model.converter.PromemoriaConverter;
import it.govpay.bd.pagamento.filters.PromemoriaFilter;
import it.govpay.model.Promemoria.StatoSpedizione;
import it.govpay.orm.dao.jdbc.JDBCPromemoriaService;
import it.govpay.orm.dao.jdbc.converter.PromemoriaFieldConverter;
import it.govpay.orm.model.PromemoriaModel;

public class PromemoriaBD extends BasicBD {

	public PromemoriaBD(BasicBD basicBD) {
		super(basicBD);
	}

	public Promemoria insertPromemoria(Promemoria dto) throws ServiceException {
		it.govpay.orm.Promemoria vo = PromemoriaConverter.toVO(dto);
		try {
			this.getPromemoriaService().create(vo);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
		dto.setId(vo.getId());
		return dto;
	}

	public List<Promemoria> findPromemoriaDaSpedire(Integer offset, Integer limit) throws ServiceException {
		try {
			IPaginatedExpression exp = this.getPromemoriaService().newPaginatedExpression();
			exp.lessThan(it.govpay.orm.Promemoria.model().DATA_PROSSIMA_SPEDIZIONE, new Date());
			exp.equals(it.govpay.orm.Promemoria.model().STATO, Promemoria.StatoSpedizione.DA_SPEDIRE.toString());
			
			if(offset != null) {
				exp.offset(offset);
			}
			
			if(limit != null) {
				exp.limit(limit);
			}
			
			exp.addOrder(it.govpay.orm.Promemoria.model().DATA_PROSSIMA_SPEDIZIONE, SortOrder.DESC);
			
			List<it.govpay.orm.Promemoria> findAll = this.getPromemoriaService().findAll(exp);
			return PromemoriaConverter.toDTOList(findAll);
		} catch(NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}

	public long countPromemoriaDaSpedire() throws ServiceException {
		PromemoriaFilter newFilter = this.newFilter();
		newFilter.setStato(Notifica.StatoSpedizione.DA_SPEDIRE.toString());
		newFilter.setDataProssimaSpedizioneFine(new Date());
		return this.count(newFilter);
	}
	
	public long countPromemoriaInAttesa() throws ServiceException {
		PromemoriaFilter newFilter = this.newFilter();
		newFilter.setStato(Notifica.StatoSpedizione.DA_SPEDIRE.toString());
		return this.count(newFilter);
	}

	public void updateSpedito(long id) throws ServiceException {
		this.update(id,  StatoSpedizione.SPEDITO, null, null, null);
	}

	public void updateDaSpedire(Long id, String message, long tentativi, Date prossima) throws ServiceException {
		// Non aggiorno il campo a DA_SPEDIRE. Se lo e' gia' tutto bene, se per concorrenza e' a spedito, non voglio sovrascriverlo. 
		this.update(id, null, message, tentativi, prossima);
	}
	
	public void updateAnnullata(Long id, String message, long tentativi, Date prossima) throws ServiceException {
		this.update(id, StatoSpedizione.ANNULLATO, message, tentativi, prossima);
	}
	
	public void updateFallita(Long id, String message) throws ServiceException {
		this.update(id, StatoSpedizione.FALLITO, message, null, null);
	}

	private void update(long id, StatoSpedizione stato, String descrizione, Long tentativi, Date prossimaSpedizione) throws ServiceException {
		try {
//			IdPromemoria idVO = ((JDBCPromemoriaServiceSearch)this.getPromemoriaService()).findId(id, true);
			List<UpdateField> lstUpdateFields = new ArrayList<>();
			if(stato != null)
				lstUpdateFields.add(new UpdateField(it.govpay.orm.Promemoria.model().STATO, stato.toString()));
//			if(descrizione != null)
			if(descrizione != null && descrizione.length() > 1024)
				descrizione = descrizione.substring(0, 1021)+ "...";
			
				lstUpdateFields.add(new UpdateField(it.govpay.orm.Promemoria.model().DESCRIZIONE_STATO, descrizione));
			if(tentativi != null)
				lstUpdateFields.add(new UpdateField(it.govpay.orm.Promemoria.model().TENTATIVI_SPEDIZIONE, tentativi));
			if(prossimaSpedizione != null) 
				lstUpdateFields.add(new UpdateField(it.govpay.orm.Promemoria.model().DATA_PROSSIMA_SPEDIZIONE, prossimaSpedizione));
			lstUpdateFields.add(new UpdateField(it.govpay.orm.Promemoria.model().DATA_AGGIORNAMENTO_STATO, new Date()));

			((JDBCPromemoriaService)this.getPromemoriaService()).updateFields(id, lstUpdateFields.toArray(new UpdateField[]{}));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		}
	}
	
	public void updateMessaggioPromemoria(long id, String oggetto, String messaggio, String contentType) throws ServiceException {
		try {
//			IdPromemoria idVO = ((JDBCPromemoriaServiceSearch)this.getPromemoriaService()).findId(id, true);
			List<UpdateField> lstUpdateFields = new ArrayList<>();
			if(oggetto != null)
				lstUpdateFields.add(new UpdateField(it.govpay.orm.Promemoria.model().OGGETTO, oggetto));
			if(messaggio != null)
				lstUpdateFields.add(new UpdateField(it.govpay.orm.Promemoria.model().MESSAGGIO, messaggio));
			if(contentType != null) 
				lstUpdateFields.add(new UpdateField(it.govpay.orm.Promemoria.model().MESSAGGIO_CONTENT_TYPE, contentType));
			lstUpdateFields.add(new UpdateField(it.govpay.orm.Promemoria.model().DATA_AGGIORNAMENTO_STATO, new Date()));

			((JDBCPromemoriaService)this.getPromemoriaService()).updateFields(id, lstUpdateFields.toArray(new UpdateField[]{}));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		}
	}
	
	
	public PromemoriaFilter newFilter() throws ServiceException {
		return new PromemoriaFilter(this.getPromemoriaService());
	}

	public PromemoriaFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new PromemoriaFilter(this.getPromemoriaService(),simpleSearch);
	}

	public long count(PromemoriaFilter filter) throws ServiceException {
		try {
			int limitInterno = GovpayConfig.getInstance().getMaxRisultati();
			
			ISQLQueryObject sqlQueryObjectInterno = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			ISQLQueryObject sqlQueryObjectDistinctID = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			
			PromemoriaFieldConverter converter = new PromemoriaFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			PromemoriaModel model = it.govpay.orm.Promemoria.model();
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
				);
			*/
			
			sqlQueryObjectInterno.addFromTable(converter.toTable(model.STATO));
			sqlQueryObjectInterno.addSelectField(converter.toTable(model.STATO), "id");
			sqlQueryObjectInterno.setANDLogicOperator(true);
			
			// creo condizioni
			sqlQueryObjectInterno = filter.toWhereCondition(sqlQueryObjectInterno);
			// preparo parametri
			Object[] parameters = filter.getParameters(sqlQueryObjectInterno);
			
			sqlQueryObjectInterno.addOrderBy(converter.toColumn(model.DATA_CREAZIONE, true), false);
			sqlQueryObjectInterno.setLimit(limitInterno);
			
			sqlQueryObjectDistinctID.addFromTable(sqlQueryObjectInterno);
			sqlQueryObjectDistinctID.addSelectCountField("id","id",true);
			
			String sql = sqlQueryObjectDistinctID.createSQLQuery();
			List<Class<?>> returnTypes = new ArrayList<>();
			returnTypes.add(Long.class); // Count
			
			List<List<Object>> nativeQuery = this.getPromemoriaService().nativeQuery(sql, returnTypes, parameters);
			
			Long count = 0L;
			for (List<Object> row : nativeQuery) {
				int pos = 0;
				count = BasicBD.getValueOrNull(row.get(pos++), Long.class);
			}
			
			return count.longValue();
		} catch (NotImplementedException | SQLQueryObjectException | ExpressionException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			return 0;
		}
	}
	
	public List<Promemoria> findAll(PromemoriaFilter filter) throws ServiceException {
		try {
			List<Promemoria> notificaLst = new ArrayList<>();
			List<it.govpay.orm.Promemoria> notificaVOLst = this.getPromemoriaService().findAll(filter.toPaginatedExpression()); 
			for(it.govpay.orm.Promemoria notificaVO: notificaVOLst) {
				notificaLst.add(PromemoriaConverter.toDTO(notificaVO));
			}
			return notificaLst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
}
