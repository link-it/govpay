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
package it.govpay.bd.pagamento;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.IFilter;
import it.govpay.bd.model.Esito;
import it.govpay.bd.model.EsitoBase;
import it.govpay.bd.model.converter.EsitoConverter;
import it.govpay.orm.IdEsito;
import it.govpay.orm.dao.jdbc.JDBCEsitoServiceSearch;
import it.govpay.orm.dao.jdbc.converter.EsitoFieldConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.SortOrder;

public class EsitiBD extends BasicBD {

	public EsitiBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	/**
	 * Recupera l'Esito identificato dalla chiave fisica
	 * 
	 * @param idTributo
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Esito getEsito(long idEsito) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			
			it.govpay.orm.Esito rptVO = ((JDBCEsitoServiceSearch)this.getEsitoService()).get(idEsito);
			return getEsito(rptVO);
			
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}

	}

	private Esito getEsito(it.govpay.orm.Esito rptVO) throws ServiceException, NotFoundException,
			MultipleResultException, NotImplementedException {
		return EsitoConverter.toDTO(rptVO);
	}
	
	/**
	 * Inserisce l'Esito.
	 * 
	 * @param esito
	 * @param documentoXml
	 * @throws NotFoundException
	 * @throws ServiceException
	 */
	public void insertEsito(Esito esito) throws NotFoundException, ServiceException {
		try {
			
			it.govpay.orm.Esito esitoVo = EsitoConverter.toVO(esito);
			this.getEsitoService().create(esitoVo);
			
			esito.setId(esitoVo.getId());
			
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}

	}

	public boolean exists(Esito esito) throws ServiceException {
		try {
			return ((JDBCEsitoServiceSearch)this.getEsitoService()).exists(esito.getId());
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Aggiorna lo stato di una Esito identificata dall'id
	 * @param idEsito
	 * @param stato
	 * @throws NotPermittedException
	 * @throws ServiceException
	 */
	public void updateEsito(Esito esito) throws NotFoundException, ServiceException{
		try {
			if(!this.exists(esito)) {
				throw new NotFoundException("Esito con id ["+esito.getId()+"] non trovato.");
			}
			IdEsito idVO = ((JDBCEsitoServiceSearch)this.getEsitoService()).findId(esito.getId(), true);

			this.getEsitoService().update(idVO, EsitoConverter.toVO(esito));
			
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}

	}
	
	public IFilter newFilter() throws ServiceException {
		return new EsitoFilter(this.getEsitoService());
	}

	private IFilter newBatchSpedizioneFilter(int offset, int limit) throws ServiceException {
		AbstractFilter filter = new AbstractFilter(this.getEsitoService()) {
			
			@Override
			public IExpression toExpression() throws ServiceException {
				try {
					IExpression exp = this.newExpression();
					exp.lessEquals(it.govpay.orm.Esito.model().DATA_ORA_PROSSIMA_SPEDIZIONE, new Date());
					IExpression exp2 = this.newExpression();
					exp2.isNull(it.govpay.orm.Esito.model().DATA_ORA_PROSSIMA_SPEDIZIONE);
					
					IExpression exp3 = this.newExpression().or(exp, exp2);
					
					IExpression exp4 = this.newExpression();
					exp4.equals(it.govpay.orm.Esito.model().STATO_SPEDIZIONE, Esito.StatoSpedizione.DA_SPEDIRE.toString());
					
					return exp4.and(exp3);
				}catch(NotImplementedException e) {
					throw new ServiceException(e);
				} catch (ExpressionNotImplementedException e) {
					throw new ServiceException(e);
				} catch (ExpressionException e) {
					throw new ServiceException(e);
				}
			}
		};

		filter.setOffset(offset);
		filter.setLimit(limit);
		List<FilterSortWrapper> filterSortList = new ArrayList<FilterSortWrapper>();
		FilterSortWrapper wrapper = new FilterSortWrapper();
		wrapper.setField(it.govpay.orm.Esito.model().DATA_ORA_PROSSIMA_SPEDIZIONE);
		wrapper.setSortOrder(SortOrder.ASC);
		filterSortList.add(wrapper);
		filter.setFilterSortList(filterSortList);
		return filter;
	}

	public long count(IFilter filter) throws ServiceException {
		try {
			return this.getEsitoService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<Esito> findAll(IFilter filter) throws ServiceException {
		try {
			List<Esito> esitoLst = new ArrayList<Esito>();
			List<it.govpay.orm.Esito> esitoVOLst = this.getEsitoService().findAll(filter.toPaginatedExpression()); 
			for(it.govpay.orm.Esito esitoVO: esitoVOLst) {
				esitoLst.add(getEsito(esitoVO));
			}
			return esitoLst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}
	}

	public List<EsitoBase> findEsitiDaSpedire(int offset, int limit) throws ServiceException {
		IFilter filter = newBatchSpedizioneFilter(offset, limit);
		try {
			List<EsitoBase> esitoLst = new ArrayList<EsitoBase>();
			List<IField> fields = new ArrayList<IField>();
			EsitoFieldConverter converter = new EsitoFieldConverter(this.getJdbcProperties().getDatabase());
			
			fields.add(new CustomField("id", Long.class, "id", converter.toTable(it.govpay.orm.Esito.model())));
			fields.add(it.govpay.orm.Esito.model().IUV);
			fields.add(it.govpay.orm.Esito.model().COD_DOMINIO);
			fields.add(new CustomField("id_applicazione", Long.class, "id_applicazione", converter.toTable(it.govpay.orm.Esito.model())));
			try {
				List<Map<String, Object>> esitoVOLst = this.getEsitoService().select(filter.toPaginatedExpression(), fields.toArray(new IField[]{}));
				for(Map<String, Object> map: esitoVOLst) {
					EsitoBase esito = new EsitoBase();
					esito.setId((Long)map.get("id"));
					esito.setCodDominio((String)map.get(it.govpay.orm.Esito.model().COD_DOMINIO.getFieldName()));
					esito.setIuv(((String)map.get(it.govpay.orm.Esito.model().IUV.getFieldName())));
					esito.setIdApplicazione((Long)map.get("id_applicazione"));
					esitoLst.add(esito);
				}
			} catch(NotFoundException e) {}
			
			return esitoLst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}



}
