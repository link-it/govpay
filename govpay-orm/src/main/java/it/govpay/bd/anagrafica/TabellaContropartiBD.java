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
import it.govpay.bd.IFilter;
import it.govpay.bd.model.TabellaControparti;
import it.govpay.bd.model.converter.TabellaContropartiConverter;
import it.govpay.orm.IdTabellaControparti;
import it.govpay.orm.dao.jdbc.JDBCTabellaContropartiServiceSearch;
import it.govpay.orm.dao.jdbc.converter.TabellaContropartiFieldConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.dao.IDBServiceUtilities;
import org.openspcoop2.generic_project.dao.jdbc.utils.IJDBCFetch;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.generic_project.expression.impl.sql.ISQLFieldConverter;

public class TabellaContropartiBD extends BasicBD {

	public TabellaContropartiBD(BasicBD basicBD) {
		super(basicBD);
	}

	/**
	 * Recupera l'tabellaControparti tramite l'id fisico
	 * 
	 * @param idTabellaControparti
	 * @return
	 * @throws NotFoundException se non esiste.
	 * @throws MultipleResultException in caso di duplicati.
	 * @throws ServiceException in caso di errore DB.
	 */
	public TabellaControparti getTabellaControparti(long idTabellaControparti) throws NotFoundException, ServiceException, MultipleResultException {
		try {
			it.govpay.orm.TabellaControparti tabellaContropartiVO = ((JDBCTabellaContropartiServiceSearch)this.getServiceManager().getTabellaContropartiServiceSearch()).get(idTabellaControparti);
			TabellaControparti tabellaControparti = getTabellaControparti(tabellaContropartiVO);
			
			return tabellaControparti;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}

	}
	
	/**
	 * Recupera l'tabellaControparti tramite l'id logico
	 * 
	 * @param codEnte
	 * @return
	 * @throws NotFoundException se l'ente non esiste.
	 * @throws MultipleResultException in caso di duplicati.
	 * @throws ServiceException in caso di errore DB.
	 */
	public TabellaControparti getLastTabellaControparti(long idDominio) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			IPaginatedExpression exp = this.getServiceManager().getTabellaContropartiServiceSearch().newPaginatedExpression();
			
			TabellaContropartiFieldConverter converter = new TabellaContropartiFieldConverter(this.getServiceManager().getJdbcProperties().getDatabase()); 
			
			exp.equals(new CustomField("id_dominio", Long.class, "id_dominio", converter.toTable(it.govpay.orm.TabellaControparti.model())));
			exp.addOrder(it.govpay.orm.TabellaControparti.model().DATA_ORA_PUBBLICAZIONE, SortOrder.DESC);
			exp.limit(1);
			List<it.govpay.orm.TabellaControparti> lstTabellaControparti = this.getServiceManager().getTabellaContropartiServiceSearch().findAll(exp);
			
			if(lstTabellaControparti == null || lstTabellaControparti.isEmpty()) {
				throw new NotFoundException("Nessuna TabellaControparti con idDominio ["+idDominio+"] trovata");
			}
			
			if(lstTabellaControparti.size() != 1) {
				throw new MultipleResultException("Errore nella ricerca dell'ultima TabellaControparti con idDominio ["+idDominio+"]");
			}
			
			TabellaControparti tabellaControparti = getTabellaControparti(lstTabellaControparti.get(0));
			
			return tabellaControparti;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}

	private TabellaControparti getTabellaControparti(it.govpay.orm.TabellaControparti tabellaContropartiVO) throws ServiceException,
			NotImplementedException, ExpressionNotImplementedException,
			ExpressionException {
		return TabellaContropartiConverter.toDTO(tabellaContropartiVO);
	}
	
	/**
	 * Aggiorna l'tabellaControparti con i dati forniti
	 * @param ente
	 * @throws NotFoundException se non esiste
	 * @throws ServiceException in caso di errore DB.
	 */
	public void updateTabellaControparti(TabellaControparti tabellaControparti) throws NotFoundException, ServiceException {
		try {
			it.govpay.orm.TabellaControparti vo = TabellaContropartiConverter.toVO(tabellaControparti);
			IdTabellaControparti id = this.getServiceManager().getTabellaContropartiServiceSearch().convertToId(vo);
			
			if(!this.getServiceManager().getTabellaContropartiServiceSearch().exists(id)) {
				throw new NotFoundException("TabellaControparti con id ["+id+"] non esiste.");
			}
			
			this.getServiceManager().getTabellaContropartiService().update(id, vo);
			tabellaControparti.setId(vo.getId());
			
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}

	}
	
	/**
	 * Crea una nuova tabellaControparti con i dati forniti
	 * @param tabellaControparti
	 * @throws ServiceException in caso di errore DB.
	 */
	public void insertTabellaControparti(TabellaControparti tabellaControparti) throws ServiceException{
		try {
			it.govpay.orm.TabellaControparti vo = TabellaContropartiConverter.toVO(tabellaControparti);
			this.getServiceManager().getTabellaContropartiService().create(vo);
			tabellaControparti.setId(vo.getId());

		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	public IFilter newFilter() throws ServiceException {
		try {
			return new it.govpay.bd.AbstractFilter(this.getServiceManager().getTabellaContropartiServiceSearch()) {
				
				@Override
				public IExpression toExpression() throws ServiceException {
					try {
						return newExpression();
					} catch (NotImplementedException e) {
						throw new ServiceException(e);
					}
				}
			};
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public long count(IFilter filter) throws ServiceException {
		try {
			return this.getServiceManager().getTabellaContropartiServiceSearch().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<TabellaControparti> findAll(IFilter filter) throws ServiceException {
		List<TabellaControparti> tabelleControparti = new ArrayList<TabellaControparti>();
		try {
			
			List<IField> fields = new ArrayList<IField>();
			ISQLFieldConverter converter = ((IDBServiceUtilities<?>)this.getServiceManager().getTabellaContropartiServiceSearch()).getFieldConverter();
			IJDBCFetch fetch = ((IDBServiceUtilities<?>)this.getServiceManager().getTabellaContropartiServiceSearch()).getFetch();

			fields.add(new CustomField("id", Long.class, "id", converter.toTable(it.govpay.orm.TabellaControparti.model())));
			fields.add(it.govpay.orm.TabellaControparti.model().ID_FLUSSO);
			fields.add(it.govpay.orm.TabellaControparti.model().DATA_ORA_PUBBLICAZIONE);
			fields.add(it.govpay.orm.TabellaControparti.model().DATA_ORA_INIZIO_VALIDITA);

			List<Map<String,Object>> select = this.getServiceManager().getTabellaContropartiServiceSearch().select(filter.toPaginatedExpression(), fields.toArray(new IField[]{}));
			
			if(select != null && !select.isEmpty()) {
				for (Map<String, Object> map : select) {
					tabelleControparti.add(TabellaContropartiConverter.toDTO((it.govpay.orm.TabellaControparti) fetch.fetch(this.getServiceManager().getJdbcProperties().getDatabase(), it.govpay.orm.TabellaControparti.model(), map)));
				}
			}
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
		return tabelleControparti;
	}


	
}
