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
package it.govpay.orm.dao.jdbc;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.beans.FunctionField;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.InUse;
import org.openspcoop2.generic_project.beans.NonNegativeNumber;
import org.openspcoop2.generic_project.beans.Union;
import org.openspcoop2.generic_project.beans.UnionExpression;
import org.openspcoop2.generic_project.dao.jdbc.IJDBCServiceSearchWithId;
import org.openspcoop2.generic_project.dao.jdbc.JDBCExpression;
import org.openspcoop2.generic_project.dao.jdbc.JDBCPaginatedExpression;
import org.openspcoop2.generic_project.dao.jdbc.JDBCServiceManagerProperties;
import org.openspcoop2.generic_project.dao.jdbc.utils.IJDBCFetch;
import org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.impl.sql.ISQLFieldConverter;
import org.openspcoop2.generic_project.utils.UtilsTemplate;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.slf4j.Logger;

import it.govpay.orm.IdApplicazione;
import it.govpay.orm.IdSingoloVersamento;
import it.govpay.orm.IdVersamento;
import it.govpay.orm.SingoloVersamento;
import it.govpay.orm.dao.jdbc.converter.SingoloVersamentoFieldConverter;
import it.govpay.orm.dao.jdbc.fetch.SingoloVersamentoFetch;

/**     
 * JDBCSingoloVersamentoServiceSearchImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCSingoloVersamentoServiceSearchImpl implements IJDBCServiceSearchWithId<SingoloVersamento, IdSingoloVersamento, JDBCServiceManager> {

	private SingoloVersamentoFieldConverter _singoloVersamentoFieldConverter = null;
	public SingoloVersamentoFieldConverter getSingoloVersamentoFieldConverter() {
		if(this._singoloVersamentoFieldConverter==null){
			this._singoloVersamentoFieldConverter = new SingoloVersamentoFieldConverter(this.jdbcServiceManager.getJdbcProperties().getDatabaseType());
		}		
		return this._singoloVersamentoFieldConverter;
	}
	@Override
	public ISQLFieldConverter getFieldConverter() {
		return this.getSingoloVersamentoFieldConverter();
	}
	
	private SingoloVersamentoFetch singoloVersamentoFetch = new SingoloVersamentoFetch();
	public SingoloVersamentoFetch getSingoloVersamentoFetch() {
		return this.singoloVersamentoFetch;
	}
	@Override
	public IJDBCFetch getFetch() {
		return this.getSingoloVersamentoFetch();
	}
	
	
	private JDBCServiceManager jdbcServiceManager = null;

	@Override
	public void setServiceManager(JDBCServiceManager serviceManager) throws ServiceException{
		this.jdbcServiceManager = serviceManager;
	}
	
	@Override
	public JDBCServiceManager getServiceManager() throws ServiceException{
		return this.jdbcServiceManager;
	}
	

	@Override
	public IdSingoloVersamento convertToId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, SingoloVersamento singoloVersamento) throws NotImplementedException, ServiceException, Exception{
	
		IdSingoloVersamento idSingoloVersamento = new IdSingoloVersamento();
		idSingoloVersamento.setIdVersamento(singoloVersamento.getIdVersamento());
		idSingoloVersamento.setCodSingoloVersamentoEnte(singoloVersamento.getCodSingoloVersamentoEnte());
		idSingoloVersamento.setIndiceDati(singoloVersamento.getIndiceDati());
	
		return idSingoloVersamento;
	}
	
	@Override
	public SingoloVersamento get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdSingoloVersamento id, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException,Exception {
		Long id_singoloVersamento = ( (id!=null && id.getId()!=null && id.getId()>0) ? id.getId() : this.findIdSingoloVersamento(jdbcProperties, log, connection, sqlQueryObject, id, true));
		return this._get(jdbcProperties, log, connection, sqlQueryObject, id_singoloVersamento,idMappingResolutionBehaviour);
		
		
	}
	
	@Override
	public boolean exists(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdSingoloVersamento id) throws MultipleResultException, NotImplementedException, ServiceException,Exception {

		Long id_singoloVersamento = this.findIdSingoloVersamento(jdbcProperties, log, connection, sqlQueryObject, id, false);
		return id_singoloVersamento != null && id_singoloVersamento > 0;
		
	}
	
	@Override
	public List<IdSingoloVersamento> findAllIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {
        // default behaviour (id-mapping)
        if(idMappingResolutionBehaviour==null){
                idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
        }

		List<IdSingoloVersamento> list = new ArrayList<>();

		try{
			List<IField> fields = new ArrayList<>();
			fields.add(new CustomField("id_versamento", Long.class, "id_versamento", this.getSingoloVersamentoFieldConverter().toTable(SingoloVersamento.model())));
			fields.add(SingoloVersamento.model().COD_SINGOLO_VERSAMENTO_ENTE);
			fields.add(SingoloVersamento.model().INDICE_DATI);

			List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));
        
			for(Map<String, Object> map: returnMap) {
				IdSingoloVersamento idSingoloVersamento = new IdSingoloVersamento();

				
				Long idFK_singoloVersamento_versamento = (Long) map.get("id_versamento");
				it.govpay.orm.IdVersamento id_singoloVersamento_versamento = new it.govpay.orm.IdVersamento();
				id_singoloVersamento_versamento.setId(idFK_singoloVersamento_versamento);
				idSingoloVersamento.setIdVersamento(id_singoloVersamento_versamento);

				idSingoloVersamento.setCodSingoloVersamentoEnte((String) map.get("cod_singolo_versamento_ente"));
				idSingoloVersamento.setIndiceDati((Integer) map.get("indice_dati"));
        	list.add(idSingoloVersamento);
        }
		} catch(NotFoundException e) {}

        return list;
		
	}
	
	@Override
	public List<SingoloVersamento> findAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {

        // default behaviour (id-mapping)
        if(idMappingResolutionBehaviour==null){
                idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
        }
        List<SingoloVersamento> list = new ArrayList<>();
        
        try{
			List<IField> fields = new ArrayList<>();
			fields.add(new CustomField("id", Long.class, "id", this.getSingoloVersamentoFieldConverter().toTable(SingoloVersamento.model())));
			fields.add(SingoloVersamento.model().TIPO_BOLLO);
			fields.add(SingoloVersamento.model().COD_SINGOLO_VERSAMENTO_ENTE);
			fields.add(SingoloVersamento.model().IMPORTO_SINGOLO_VERSAMENTO);
			fields.add(SingoloVersamento.model().HASH_DOCUMENTO);
			fields.add(SingoloVersamento.model().TIPO_CONTABILITA);
			fields.add(SingoloVersamento.model().CODICE_CONTABILITA);
			fields.add(SingoloVersamento.model().STATO_SINGOLO_VERSAMENTO);
			fields.add(SingoloVersamento.model().PROVINCIA_RESIDENZA);
			fields.add(SingoloVersamento.model().DESCRIZIONE);
			fields.add(SingoloVersamento.model().DATI_ALLEGATI);
			fields.add(SingoloVersamento.model().INDICE_DATI);
			fields.add(SingoloVersamento.model().DESCRIZIONE_CAUSALE_RPT);
			fields.add(SingoloVersamento.model().CONTABILITA);
			
			fields.add(new CustomField("id_iban_accredito", Long.class, "id_iban_accredito", this.getSingoloVersamentoFieldConverter().toTable(SingoloVersamento.model())));
			fields.add(new CustomField("id_iban_appoggio", Long.class, "id_iban_appoggio", this.getSingoloVersamentoFieldConverter().toTable(SingoloVersamento.model())));
			fields.add(new CustomField("id_tributo", Long.class, "id_tributo", this.getSingoloVersamentoFieldConverter().toTable(SingoloVersamento.model())));
			fields.add(new CustomField("id_versamento", Long.class, "id_versamento", this.getSingoloVersamentoFieldConverter().toTable(SingoloVersamento.model())));
			fields.add(new CustomField("id_dominio", Long.class, "id_dominio", this.getSingoloVersamentoFieldConverter().toTable(SingoloVersamento.model())));
        
			List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));

			for(Map<String, Object> map: returnMap) {
				
				Object idTributoObj = map.remove("id_tributo");
				Long idTributo = null;
				if(idTributoObj instanceof Long) {
					idTributo = (Long) idTributoObj;
				}
				
				Object idIbanAccreditoObj = map.remove("id_iban_accredito");
				Long idIbanAccredito = null;
				if(idIbanAccreditoObj instanceof Long) {
					idIbanAccredito = (Long) idIbanAccreditoObj;
				}
				
				Object idIbanAppoggioObj = map.remove("id_iban_appoggio");
				Long idIbanAppoggio = null;
				if(idIbanAppoggioObj instanceof Long) {
					idIbanAppoggio = (Long) idIbanAppoggioObj;
				}
				
				Object idDominioObj = map.remove("id_dominio");
				Long idDominio = null;
				if(idDominioObj instanceof Long) {
					idDominio = (Long) idDominioObj;
				}
				
				Long idVersamento = (Long)map.remove("id_versamento");

				SingoloVersamento singoloVersamento = (SingoloVersamento)this.getSingoloVersamentoFetch().fetch(jdbcProperties.getDatabase(), SingoloVersamento.model(), map);

				if(idTributo != null) {
					it.govpay.orm.IdTributo id_singoloVersamento_tributo = null;
					id_singoloVersamento_tributo = new it.govpay.orm.IdTributo();
					id_singoloVersamento_tributo.setId(idTributo);
					singoloVersamento.setIdTributo(id_singoloVersamento_tributo);
				}

				if(idIbanAccredito != null) {
					it.govpay.orm.IdIbanAccredito id_singoloVersamento_ibanAccredito = null;
					id_singoloVersamento_ibanAccredito = new it.govpay.orm.IdIbanAccredito();
					id_singoloVersamento_ibanAccredito.setId(idIbanAccredito);
					singoloVersamento.setIdIbanAccredito(id_singoloVersamento_ibanAccredito);
				}

				if(idIbanAppoggio != null) {
					it.govpay.orm.IdIbanAccredito id_singoloVersamento_ibanAccredito = null;
					id_singoloVersamento_ibanAccredito = new it.govpay.orm.IdIbanAccredito();
					id_singoloVersamento_ibanAccredito.setId(idIbanAppoggio);
					singoloVersamento.setIdIbanAppoggio(id_singoloVersamento_ibanAccredito);
				}
				
				if(idDominio != null) {
					it.govpay.orm.IdDominio id_singoloVersamento_dominio = null;
					id_singoloVersamento_dominio = new it.govpay.orm.IdDominio();
					id_singoloVersamento_dominio.setId(idDominio);
					singoloVersamento.setIdDominio(id_singoloVersamento_dominio);
				}

				it.govpay.orm.IdVersamento id_singoloVersamento_versamento = null;
				id_singoloVersamento_versamento = new it.govpay.orm.IdVersamento();
				id_singoloVersamento_versamento.setId(idVersamento);
				singoloVersamento.setIdVersamento(id_singoloVersamento_versamento);
				
				list.add(singoloVersamento);
			}
		} catch(NotFoundException e) {}

        return list;      
		
	}
	
	@Override
	public SingoloVersamento find(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) 
		throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException,Exception {

		JDBCPaginatedExpression pagExpr = this.toPaginatedExpression(expression,log);
		
		List<SingoloVersamento> lst = this.findAll(jdbcProperties, log, connection, sqlQueryObject, pagExpr, idMappingResolutionBehaviour);

		if(lst.size() <=0)
			throw new NotFoundException("Nessuna entry corrisponde ai criteri indicati.");

		if(lst.size() > 1)
			throw new MultipleResultException("I criteri indicati individuano piu' entry.");

		return lst.get(0);
		
	}
	
	@Override
	public NonNegativeNumber count(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws NotImplementedException, ServiceException,Exception {
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareCount(jdbcProperties, log, connection, sqlQueryObject, expression,
												this.getSingoloVersamentoFieldConverter(), SingoloVersamento.model());
		
		sqlQueryObject.addSelectCountField(this.getSingoloVersamentoFieldConverter().toTable(SingoloVersamento.model())+".id","tot");
		
		this._join(expression,sqlQueryObject);
		
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.count(jdbcProperties, log, connection, sqlQueryObject, expression,
																			this.getSingoloVersamentoFieldConverter(), SingoloVersamento.model(),listaQuery);
	}

	@Override
	public InUse inUse(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdSingoloVersamento id) throws NotFoundException, NotImplementedException, ServiceException,Exception {
		
		Long id_singoloVersamento = this.findIdSingoloVersamento(jdbcProperties, log, connection, sqlQueryObject, id, true);
        return this._inUse(jdbcProperties, log, connection, sqlQueryObject, id_singoloVersamento);
		
	}

	@Override
	public List<Object> select(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
													JDBCPaginatedExpression paginatedExpression, IField field) throws ServiceException,NotFoundException,NotImplementedException,Exception {
		return this.select(jdbcProperties, log, connection, sqlQueryObject,
								paginatedExpression, false, field);
	}
	
	@Override
	public List<Object> select(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
													JDBCPaginatedExpression paginatedExpression, boolean distinct, IField field) throws ServiceException,NotFoundException,NotImplementedException,Exception {
		List<Map<String,Object>> map = 
			this.select(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression, distinct, new IField[]{field});
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.selectSingleObject(map);
	}
	
	@Override
	public List<Map<String,Object>> select(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
													JDBCPaginatedExpression paginatedExpression, IField ... field) throws ServiceException,NotFoundException,NotImplementedException,Exception {
		return this.select(jdbcProperties, log, connection, sqlQueryObject,
								paginatedExpression, false, field);
	}
	
	@Override
	public List<Map<String,Object>> select(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
													JDBCPaginatedExpression paginatedExpression, boolean distinct, IField ... field) throws ServiceException,NotFoundException,NotImplementedException,Exception {
		
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.setFields(sqlQueryObject,paginatedExpression,field);
		try{
		
			ISQLQueryObject sqlQueryObjectDistinct = 
						org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareSqlQueryObjectForSelectDistinct(distinct,sqlQueryObject, paginatedExpression, log,
												this.getSingoloVersamentoFieldConverter(), field);

			return this._select(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression, sqlQueryObjectDistinct);
			
		}finally{
			org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.removeFields(sqlQueryObject,paginatedExpression,field);
		}
	}

	@Override
	public Object aggregate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
													JDBCExpression expression, FunctionField functionField) throws ServiceException,NotFoundException,NotImplementedException,Exception {
		Map<String,Object> map = 
			this.aggregate(jdbcProperties, log, connection, sqlQueryObject, expression, new FunctionField[]{functionField});
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.selectAggregateObject(map,functionField);
	}
	
	@Override
	public Map<String,Object> aggregate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
													JDBCExpression expression, FunctionField ... functionField) throws ServiceException,NotFoundException,NotImplementedException,Exception {													
		
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.setFields(sqlQueryObject,expression,functionField);
		try{
			List<Map<String,Object>> list = this._select(jdbcProperties, log, connection, sqlQueryObject, expression);
			return list.get(0);
		}finally{
			org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.removeFields(sqlQueryObject,expression,functionField);
		}
	}

	@Override
	public List<Map<String,Object>> groupBy(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
													JDBCExpression expression, FunctionField ... functionField) throws ServiceException,NotFoundException,NotImplementedException,Exception {
		
		if(expression.getGroupByFields().size()<=0){
			throw new ServiceException("GroupBy conditions not found in expression");
		}
		
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.setFields(sqlQueryObject,expression,functionField);
		try{
			return this._select(jdbcProperties, log, connection, sqlQueryObject, expression);
		}finally{
			org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.removeFields(sqlQueryObject,expression,functionField);
		}
	}
	

	@Override
	public List<Map<String,Object>> groupBy(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
													JDBCPaginatedExpression paginatedExpression, FunctionField ... functionField) throws ServiceException,NotFoundException,NotImplementedException,Exception {
		
		if(paginatedExpression.getGroupByFields().size()<=0){
			throw new ServiceException("GroupBy conditions not found in expression");
		}
		
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.setFields(sqlQueryObject,paginatedExpression,functionField);
		try{
			return this._select(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression);
		}finally{
			org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.removeFields(sqlQueryObject,paginatedExpression,functionField);
		}
	}
	
	protected List<Map<String,Object>> _select(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
												IExpression expression) throws ServiceException,NotFoundException,NotImplementedException,Exception {
		return this._select(jdbcProperties, log, connection, sqlQueryObject, expression, null);
	}
	protected List<Map<String,Object>> _select(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
												IExpression expression, ISQLQueryObject sqlQueryObjectDistinct) throws ServiceException,NotFoundException,NotImplementedException,Exception {
		
		List<Object> listaQuery = new ArrayList<>();
		List<JDBCObject> listaParams = new ArrayList<>();
		List<Object> returnField = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareSelect(jdbcProperties, log, connection, sqlQueryObject, 
        						expression, this.getSingoloVersamentoFieldConverter(), SingoloVersamento.model(), 
        						listaQuery,listaParams);
		
		this._join(expression,sqlQueryObject);
        
        List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.select(jdbcProperties, log, connection,
        								org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareSqlQueryObjectForSelectDistinct(sqlQueryObject,sqlQueryObjectDistinct), 
        								expression, this.getSingoloVersamentoFieldConverter(), SingoloVersamento.model(),
        								listaQuery,listaParams,returnField);
		if(list!=null && list.size()>0){
			return list;
		}
		else{
			throw new NotFoundException("Not Found");
		}
	}
	
	@Override
	public List<Map<String,Object>> union(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
												Union union, UnionExpression ... unionExpression) throws ServiceException,NotFoundException,NotImplementedException,Exception {		
		
		List<ISQLQueryObject> sqlQueryObjectInnerList = new ArrayList<>();
		List<JDBCObject> jdbcObjects = new ArrayList<>();
		List<Class<?>> returnClassTypes = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareUnion(jdbcProperties, log, connection, sqlQueryObject, 
        						this.getSingoloVersamentoFieldConverter(), SingoloVersamento.model(), 
        						sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);
		
		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				this._join(expression,sqlQueryObjectInnerList.get(i));
			}
		}
        
        List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.union(jdbcProperties, log, connection, sqlQueryObject, 
        								this.getSingoloVersamentoFieldConverter(), SingoloVersamento.model(), 
        								sqlQueryObjectInnerList, jdbcObjects, returnClassTypes, union, unionExpression);
        if(list!=null && list.size()>0){
			return list;
		}
		else{
			throw new NotFoundException("Not Found");
		}								
	}
	
	@Override
	public NonNegativeNumber unionCount(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
												Union union, UnionExpression ... unionExpression) throws ServiceException,NotFoundException,NotImplementedException,Exception {		
		
		List<ISQLQueryObject> sqlQueryObjectInnerList = new ArrayList<>();
		List<JDBCObject> jdbcObjects = new ArrayList<>();
		List<Class<?>> returnClassTypes = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareUnionCount(jdbcProperties, log, connection, sqlQueryObject, 
        						this.getSingoloVersamentoFieldConverter(), SingoloVersamento.model(), 
        						sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);
		
		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				this._join(expression,sqlQueryObjectInnerList.get(i));
			}
		}
        
        NonNegativeNumber number = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.unionCount(jdbcProperties, log, connection, sqlQueryObject, 
        								this.getSingoloVersamentoFieldConverter(), SingoloVersamento.model(), 
        								sqlQueryObjectInnerList, jdbcObjects, returnClassTypes, union, unionExpression);
        if(number!=null && number.longValue()>=0){
			return number;
		}
		else{
			throw new NotFoundException("Not Found");
		}
	}



	// -- ConstructorExpression	

	@Override
	public JDBCExpression newExpression(Logger log) throws NotImplementedException, ServiceException {
		try{
			return new JDBCExpression(this.getSingoloVersamentoFieldConverter());
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}


	@Override
	public JDBCPaginatedExpression newPaginatedExpression(Logger log) throws NotImplementedException, ServiceException {
		try{
			return new JDBCPaginatedExpression(this.getSingoloVersamentoFieldConverter());
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}
	
	@Override
	public JDBCExpression toExpression(JDBCPaginatedExpression paginatedExpression, Logger log) throws NotImplementedException, ServiceException {
		try{
			return new JDBCExpression(paginatedExpression);
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}

	@Override
	public JDBCPaginatedExpression toPaginatedExpression(JDBCExpression expression, Logger log) throws NotImplementedException, ServiceException {
		try{
			return new JDBCPaginatedExpression(expression);
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}
	
	
	
	// -- DB

	@Override
	public void mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdSingoloVersamento id, SingoloVersamento obj) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		this._mappingTableIds(jdbcProperties,log,connection,sqlQueryObject,obj,
				this.get(jdbcProperties,log,connection,sqlQueryObject,id,null));
	}
	
	@Override
	public void mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, SingoloVersamento obj) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		this._mappingTableIds(jdbcProperties,log,connection,sqlQueryObject,obj,
				this.get(jdbcProperties,log,connection,sqlQueryObject,tableId,null));
	}
	private void _mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, SingoloVersamento obj, SingoloVersamento imgSaved) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		if(imgSaved==null){
			return;
		}
		obj.setId(imgSaved.getId());
		if(obj.getIdVersamento()!=null && 
				imgSaved.getIdVersamento()!=null){
			obj.getIdVersamento().setId(imgSaved.getIdVersamento().getId());
			if(obj.getIdVersamento().getIdApplicazione()!=null && 
					imgSaved.getIdVersamento().getIdApplicazione()!=null){
				obj.getIdVersamento().getIdApplicazione().setId(imgSaved.getIdVersamento().getIdApplicazione().getId());
			}
			if(obj.getIdVersamento().getIdUo()!=null && 
					imgSaved.getIdVersamento().getIdUo()!=null){
				obj.getIdVersamento().getIdUo().setId(imgSaved.getIdVersamento().getIdUo().getId());
				if(obj.getIdVersamento().getIdUo().getIdDominio()!=null && 
						imgSaved.getIdVersamento().getIdUo().getIdDominio()!=null){
					obj.getIdVersamento().getIdUo().getIdDominio().setId(imgSaved.getIdVersamento().getIdUo().getIdDominio().getId());
				}
			}
			if(obj.getIdVersamento().getIdTipoVersamento()!=null && 
					imgSaved.getIdVersamento().getIdTipoVersamento()!=null){
				obj.getIdVersamento().getIdTipoVersamento().setId(imgSaved.getIdVersamento().getIdTipoVersamento().getId());
			}
		}
		if(obj.getIdTributo()!=null && 
				imgSaved.getIdTributo()!=null){
			obj.getIdTributo().setId(imgSaved.getIdTributo().getId());
			if(obj.getIdTributo().getIdDominio()!=null && 
					imgSaved.getIdTributo().getIdDominio()!=null){
				obj.getIdTributo().getIdDominio().setId(imgSaved.getIdTributo().getIdDominio().getId());
			}
			if(obj.getIdTributo().getIdTipoTributo()!=null && 
					imgSaved.getIdTributo().getIdTipoTributo()!=null){
				obj.getIdTributo().getIdTipoTributo().setId(imgSaved.getIdTributo().getIdTipoTributo().getId());
			}
		}
		if(obj.getIdIbanAccredito()!=null && 
				imgSaved.getIdIbanAccredito()!=null){
			obj.getIdIbanAccredito().setId(imgSaved.getIdIbanAccredito().getId());
			if(obj.getIdIbanAccredito().getIdDominio()!=null && 
					imgSaved.getIdIbanAccredito().getIdDominio()!=null){
				obj.getIdIbanAccredito().getIdDominio().setId(imgSaved.getIdIbanAccredito().getIdDominio().getId());
			}
		}
		if(obj.getIdIbanAppoggio()!=null && 
				imgSaved.getIdIbanAppoggio()!=null){
			obj.getIdIbanAppoggio().setId(imgSaved.getIdIbanAppoggio().getId());
			if(obj.getIdIbanAppoggio().getIdDominio()!=null && 
					imgSaved.getIdIbanAppoggio().getIdDominio()!=null){
				obj.getIdIbanAppoggio().getIdDominio().setId(imgSaved.getIdIbanAppoggio().getIdDominio().getId());
			}
		}
		if(obj.getIdDominio()!=null && 
				imgSaved.getIdDominio()!=null){
			obj.getIdDominio().setId(imgSaved.getIdDominio().getId());
		}

	}
	
	@Override
	public SingoloVersamento get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
		return this._get(jdbcProperties, log, connection, sqlQueryObject, Long.valueOf(tableId), idMappingResolutionBehaviour);
	}
	
	private SingoloVersamento _get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
	
		
		IField idField = new CustomField("id", Long.class, "id", this.getSingoloVersamentoFieldConverter().toTable(SingoloVersamento.model()));
		JDBCPaginatedExpression expression = this.newPaginatedExpression(log);
		
		expression.equals(idField, tableId);
		List<SingoloVersamento> lst = this.findAll(jdbcProperties, log, connection, sqlQueryObject.newSQLQueryObject(), expression, idMappingResolutionBehaviour);
		
		if(lst.size() <=0)
			throw new NotFoundException("Id ["+tableId+"]");

		if(lst.size() > 1)
			throw new MultipleResultException("Id ["+tableId+"]");

		return lst.get(0);
	
	} 
	
	@Override
	public boolean exists(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId) throws MultipleResultException, NotImplementedException, ServiceException, Exception {
		return this._exists(jdbcProperties, log, connection, sqlQueryObject, Long.valueOf(tableId));
	}
	
	private boolean _exists(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long tableId) throws MultipleResultException, NotImplementedException, ServiceException, Exception {
	
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
					new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
				
		boolean existsSingoloVersamento = false;

		sqlQueryObject = sqlQueryObject.newSQLQueryObject();
		sqlQueryObject.setANDLogicOperator(true);

		sqlQueryObject.addFromTable(this.getSingoloVersamentoFieldConverter().toTable(SingoloVersamento.model()));
		sqlQueryObject.addSelectField(this.getSingoloVersamentoFieldConverter().toColumn(SingoloVersamento.model().COD_SINGOLO_VERSAMENTO_ENTE,true));
		sqlQueryObject.addWhereCondition("id=?");


		// Exists singoloVersamento
		existsSingoloVersamento = jdbcUtilities.exists(sqlQueryObject.createSQLQuery(), jdbcProperties.isShowSql(),
			new JDBCObject(tableId,Long.class));

		
        return existsSingoloVersamento;
	
	}
	
	private void _join(IExpression expression, ISQLQueryObject sqlQueryObject) throws NotImplementedException, ServiceException, Exception{
		
		if(expression.inUseModel(SingoloVersamento.model().ID_DOMINIO,false)){
			String tableName1 = this.getSingoloVersamentoFieldConverter().toAliasTable(SingoloVersamento.model());
			String tableName2 = this.getSingoloVersamentoFieldConverter().toAliasTable(SingoloVersamento.model().ID_DOMINIO);
			sqlQueryObject.addWhereCondition(tableName1+".id_dominio="+tableName2+".id");
		}
		
		if(expression.inUseModel(SingoloVersamento.model().ID_TRIBUTO,false)){
			String tableName1 = this.getSingoloVersamentoFieldConverter().toAliasTable(SingoloVersamento.model());
			String tableName2 = this.getSingoloVersamentoFieldConverter().toAliasTable(SingoloVersamento.model().ID_TRIBUTO);
			sqlQueryObject.addWhereCondition(tableName1+".id_tributo="+tableName2+".id");
		}
		
		if(expression.inUseModel(SingoloVersamento.model().ID_TRIBUTO.ID_TIPO_TRIBUTO,false)){

			if(!expression.inUseModel(SingoloVersamento.model().ID_TRIBUTO,false)){
				String tableName1 = this.getSingoloVersamentoFieldConverter().toAliasTable(SingoloVersamento.model());
				String tableName2 = this.getSingoloVersamentoFieldConverter().toAliasTable(SingoloVersamento.model().ID_TRIBUTO);
				sqlQueryObject.addFromTable(tableName2);
				sqlQueryObject.addWhereCondition(tableName1+".id_tributo="+tableName2+".id");
			}

			String tableName1 = this.getSingoloVersamentoFieldConverter().toAliasTable(SingoloVersamento.model().ID_TRIBUTO);
			String tableName2 = this.getSingoloVersamentoFieldConverter().toAliasTable(SingoloVersamento.model().ID_TRIBUTO.ID_TIPO_TRIBUTO);
			sqlQueryObject.addWhereCondition(tableName1+".id_tipo_tributo="+tableName2+".id");
			
		}
		
		if(expression.inUseModel(SingoloVersamento.model().ID_TRIBUTO.ID_DOMINIO,false)){
			if(!expression.inUseModel(SingoloVersamento.model().ID_TRIBUTO,false)){
				String tableName1 = this.getSingoloVersamentoFieldConverter().toAliasTable(SingoloVersamento.model());
				String tableName2 = this.getSingoloVersamentoFieldConverter().toAliasTable(SingoloVersamento.model().ID_TRIBUTO);
				sqlQueryObject.addFromTable(tableName2);
				sqlQueryObject.addWhereCondition(tableName1+".id_tributo="+tableName2+".id");
			}

			String tableName1 = this.getSingoloVersamentoFieldConverter().toAliasTable(SingoloVersamento.model().ID_TRIBUTO.ID_DOMINIO);
			String tableName2 = this.getSingoloVersamentoFieldConverter().toAliasTable(SingoloVersamento.model().ID_TRIBUTO);
			sqlQueryObject.addWhereCondition(tableName1+".id="+tableName2+".id_dominio");
		}

		if(expression.inUseModel(SingoloVersamento.model().ID_VERSAMENTO,false)){
			String tableName1 = this.getSingoloVersamentoFieldConverter().toAliasTable(SingoloVersamento.model());
			String tableName2 = this.getSingoloVersamentoFieldConverter().toAliasTable(SingoloVersamento.model().ID_VERSAMENTO);
			sqlQueryObject.addWhereCondition(tableName1+".id_versamento="+tableName2+".id");
		}
		
		if(expression.inUseModel(SingoloVersamento.model().ID_IBAN_ACCREDITO,false)){
			String tableName1 = this.getSingoloVersamentoFieldConverter().toAliasTable(SingoloVersamento.model());
			String tableName2 = this.getSingoloVersamentoFieldConverter().toAliasTable(SingoloVersamento.model().ID_IBAN_ACCREDITO);
			sqlQueryObject.addWhereCondition(tableName1+".id_iban_accredito="+tableName2+".id");
		}
		
		if(expression.inUseModel(SingoloVersamento.model().ID_IBAN_APPOGGIO,false)){
			String tableName1 = this.getSingoloVersamentoFieldConverter().toAliasTable(SingoloVersamento.model());
			String tableName2 = this.getSingoloVersamentoFieldConverter().toAliasTable(SingoloVersamento.model().ID_IBAN_APPOGGIO);
			sqlQueryObject.addWhereCondition(tableName1+".id_iban_appoggio="+tableName2+".id");
		}
		
		if(expression.inUseModel(SingoloVersamento.model().ID_VERSAMENTO.ID_APPLICAZIONE,false)){
			
			if(!expression.inUseModel(SingoloVersamento.model().ID_VERSAMENTO,false)){
				String tableName1 = this.getSingoloVersamentoFieldConverter().toAliasTable(SingoloVersamento.model());
				String tableName2 = this.getSingoloVersamentoFieldConverter().toAliasTable(SingoloVersamento.model().ID_VERSAMENTO);
				sqlQueryObject.addFromTable(tableName2);
				sqlQueryObject.addWhereCondition(tableName1+".id_versamento="+tableName2+".id");
			}
			
			String tableName1 = this.getSingoloVersamentoFieldConverter().toAliasTable(SingoloVersamento.model().ID_VERSAMENTO);
			String tableName2 = this.getSingoloVersamentoFieldConverter().toAliasTable(SingoloVersamento.model().ID_VERSAMENTO.ID_APPLICAZIONE);
			sqlQueryObject.addWhereCondition(tableName1+".id_applicazione="+tableName2+".id");
		}
		
	}
	
	protected java.util.List<Object> _getRootTablePrimaryKeyValues(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdSingoloVersamento id) throws NotFoundException, ServiceException, NotImplementedException, Exception{
	    // Identificativi
        java.util.List<Object> rootTableIdValues = new java.util.ArrayList<>();
		Long longId = this.findIdSingoloVersamento(jdbcProperties, log, connection, sqlQueryObject.newSQLQueryObject(), id, true);
		rootTableIdValues.add(longId);
        
        
        return rootTableIdValues;
	}
	
	protected Map<String, List<IField>> _getMapTableToPKColumn() throws NotImplementedException, Exception{
	
		SingoloVersamentoFieldConverter converter = this.getSingoloVersamentoFieldConverter();
		Map<String, List<IField>> mapTableToPKColumn = new java.util.Hashtable<>();
		UtilsTemplate<IField> utilities = new UtilsTemplate<>();

		//		  If a table doesn't have a primary key, don't add it to this map

		// SingoloVersamento.model()
		mapTableToPKColumn.put(converter.toTable(SingoloVersamento.model()),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(SingoloVersamento.model()))
			));

		// SingoloVersamento.model().ID_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(SingoloVersamento.model().ID_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(SingoloVersamento.model().ID_VERSAMENTO))
			));

		// SingoloVersamento.model().ID_VERSAMENTO.ID_APPLICAZIONE
		mapTableToPKColumn.put(converter.toTable(SingoloVersamento.model().ID_VERSAMENTO.ID_APPLICAZIONE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(SingoloVersamento.model().ID_VERSAMENTO.ID_APPLICAZIONE))
			));

		// SingoloVersamento.model().ID_VERSAMENTO.ID_UO
		mapTableToPKColumn.put(converter.toTable(SingoloVersamento.model().ID_VERSAMENTO.ID_UO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(SingoloVersamento.model().ID_VERSAMENTO.ID_UO))
			));

		// SingoloVersamento.model().ID_VERSAMENTO.ID_UO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(SingoloVersamento.model().ID_VERSAMENTO.ID_UO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(SingoloVersamento.model().ID_VERSAMENTO.ID_UO.ID_DOMINIO))
			));

		// SingoloVersamento.model().ID_VERSAMENTO.ID_TIPO_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(SingoloVersamento.model().ID_VERSAMENTO.ID_TIPO_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(SingoloVersamento.model().ID_VERSAMENTO.ID_TIPO_VERSAMENTO))
			));

		// SingoloVersamento.model().ID_TRIBUTO
		mapTableToPKColumn.put(converter.toTable(SingoloVersamento.model().ID_TRIBUTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(SingoloVersamento.model().ID_TRIBUTO))
			));

		// SingoloVersamento.model().ID_TRIBUTO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(SingoloVersamento.model().ID_TRIBUTO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(SingoloVersamento.model().ID_TRIBUTO.ID_DOMINIO))
			));

		// SingoloVersamento.model().ID_TRIBUTO.ID_TIPO_TRIBUTO
		mapTableToPKColumn.put(converter.toTable(SingoloVersamento.model().ID_TRIBUTO.ID_TIPO_TRIBUTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(SingoloVersamento.model().ID_TRIBUTO.ID_TIPO_TRIBUTO))
			));

		// SingoloVersamento.model().ID_IBAN_ACCREDITO
		mapTableToPKColumn.put(converter.toTable(SingoloVersamento.model().ID_IBAN_ACCREDITO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(SingoloVersamento.model().ID_IBAN_ACCREDITO))
			));

		// SingoloVersamento.model().ID_IBAN_ACCREDITO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(SingoloVersamento.model().ID_IBAN_ACCREDITO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(SingoloVersamento.model().ID_IBAN_ACCREDITO.ID_DOMINIO))
			));

		// SingoloVersamento.model().ID_IBAN_APPOGGIO
		mapTableToPKColumn.put(converter.toTable(SingoloVersamento.model().ID_IBAN_APPOGGIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(SingoloVersamento.model().ID_IBAN_APPOGGIO))
			));

		// SingoloVersamento.model().ID_IBAN_APPOGGIO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(SingoloVersamento.model().ID_IBAN_APPOGGIO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(SingoloVersamento.model().ID_IBAN_APPOGGIO.ID_DOMINIO))
			));

		// SingoloVersamento.model().ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(SingoloVersamento.model().ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(SingoloVersamento.model().ID_DOMINIO))
			));

        return mapTableToPKColumn;		
	}
	
	@Override
	public List<Long> findAllTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression paginatedExpression) throws ServiceException, NotImplementedException, Exception {
		
		List<Long> list = new ArrayList<>();

		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getSingoloVersamentoFieldConverter().toTable(SingoloVersamento.model())+".id");
		Class<?> objectIdClass = Long.class;
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFindAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
												this.getSingoloVersamentoFieldConverter(), SingoloVersamento.model());
		
		this._join(paginatedExpression,sqlQueryObject);
		
		List<Object> listObjects = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.findAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
																			this.getSingoloVersamentoFieldConverter(), SingoloVersamento.model(), objectIdClass, listaQuery);
		for(Object object: listObjects) {
			list.add((Long)object);
		}

        return list;
		
	}
	
	@Override
	public long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws ServiceException, NotFoundException, MultipleResultException, NotImplementedException, Exception {
	
		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getSingoloVersamentoFieldConverter().toTable(SingoloVersamento.model())+".id");
		Class<?> objectIdClass = Long.class;
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFind(jdbcProperties, log, connection, sqlQueryObject, expression,
												this.getSingoloVersamentoFieldConverter(), SingoloVersamento.model());
		
		this._join(expression,sqlQueryObject);

		Object res = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.find(jdbcProperties, log, connection, sqlQueryObject, expression,
														this.getSingoloVersamentoFieldConverter(), SingoloVersamento.model(), objectIdClass, listaQuery);
		if(res!=null && (((Long) res).longValue()>0) ){
			return ((Long) res).longValue();
		}
		else{
			throw new NotFoundException("Not Found");
		}
		
	}

	@Override
	public InUse inUse(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId) throws ServiceException, NotFoundException, NotImplementedException, Exception {
		return this._inUse(jdbcProperties, log, connection, sqlQueryObject, Long.valueOf(tableId));
	}

	private InUse _inUse(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long tableId) throws ServiceException, NotFoundException, NotImplementedException, Exception {

		InUse inUse = new InUse();
		inUse.setInUse(false);
		
		// Delete this line when you have implemented the method
		int throwNotImplemented = 1;
		if(throwNotImplemented==1){
		        throw new NotImplementedException("NotImplemented");
		}
		// Delete this line when you have implemented the method

        return inUse;

	}
	
	@Override
	public IdSingoloVersamento findId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, boolean throwNotFound)
			throws NotFoundException, ServiceException, NotImplementedException, Exception {
		
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);

		ISQLQueryObject sqlQueryObjectGet = sqlQueryObject.newSQLQueryObject();


		// Object _singoloVersamento
		sqlQueryObjectGet.addFromTable(this.getSingoloVersamentoFieldConverter().toTable(SingoloVersamento.model()));
		sqlQueryObjectGet.addFromTable(this.getSingoloVersamentoFieldConverter().toTable(SingoloVersamento.model().ID_VERSAMENTO));
		sqlQueryObjectGet.addFromTable(this.getSingoloVersamentoFieldConverter().toTable(SingoloVersamento.model().ID_VERSAMENTO.ID_APPLICAZIONE));
		sqlQueryObjectGet.addSelectField(this.getSingoloVersamentoFieldConverter().toColumn(SingoloVersamento.model().ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE,true));
		sqlQueryObjectGet.addSelectField(this.getSingoloVersamentoFieldConverter().toColumn(SingoloVersamento.model().ID_VERSAMENTO.COD_VERSAMENTO_ENTE,true));
		sqlQueryObjectGet.addSelectField(this.getSingoloVersamentoFieldConverter().toColumn(SingoloVersamento.model().COD_SINGOLO_VERSAMENTO_ENTE,true));
		sqlQueryObjectGet.addSelectField(this.getSingoloVersamentoFieldConverter().toColumn(SingoloVersamento.model().INDICE_DATI,true));
		sqlQueryObjectGet.setANDLogicOperator(true);
		sqlQueryObjectGet.addWhereCondition(this.getSingoloVersamentoFieldConverter().toTable(SingoloVersamento.model())+".id=?");
		sqlQueryObjectGet.addWhereCondition(this.getSingoloVersamentoFieldConverter().toTable(SingoloVersamento.model())+".id_versamento="+this.getSingoloVersamentoFieldConverter().toTable(SingoloVersamento.model().ID_VERSAMENTO)+".id");
		sqlQueryObjectGet.addWhereCondition(this.getSingoloVersamentoFieldConverter().toTable(SingoloVersamento.model().ID_VERSAMENTO.ID_APPLICAZIONE)+".id="+this.getSingoloVersamentoFieldConverter().toTable(SingoloVersamento.model().ID_VERSAMENTO)+".id_applicazione");

		// Recupero _singoloVersamento
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] searchParams_singoloVersamento = new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] { 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tableId,Long.class)
		};
		List<Class<?>> listaFieldIdReturnType_singoloVersamento = new ArrayList<>();
		listaFieldIdReturnType_singoloVersamento.add(SingoloVersamento.model().ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE.getFieldType());
		listaFieldIdReturnType_singoloVersamento.add(SingoloVersamento.model().ID_VERSAMENTO.COD_VERSAMENTO_ENTE.getFieldType());
		listaFieldIdReturnType_singoloVersamento.add(SingoloVersamento.model().COD_SINGOLO_VERSAMENTO_ENTE.getFieldType());
		listaFieldIdReturnType_singoloVersamento.add(SingoloVersamento.model().INDICE_DATI.getFieldType());
		it.govpay.orm.IdSingoloVersamento id_singoloVersamento = null;
		List<Object> listaFieldId_singoloVersamento = jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet.createSQLQuery(), jdbcProperties.isShowSql(),
				listaFieldIdReturnType_singoloVersamento, searchParams_singoloVersamento);
		if(listaFieldId_singoloVersamento==null || listaFieldId_singoloVersamento.size()<=0){
			if(throwNotFound){
				throw new NotFoundException("Not Found");
			}
		}
		else{
			id_singoloVersamento = new it.govpay.orm.IdSingoloVersamento();
			IdVersamento idVersamento = new IdVersamento();
			IdApplicazione idApplicazione = new IdApplicazione();
			idApplicazione.setCodApplicazione((String)listaFieldId_singoloVersamento.get(0));
			
			idVersamento.setIdApplicazione(idApplicazione);
			
			idVersamento.setCodVersamentoEnte((String)listaFieldId_singoloVersamento.get(1));
			id_singoloVersamento.setIdVersamento(idVersamento);
			
			id_singoloVersamento.setCodSingoloVersamentoEnte((String) listaFieldId_singoloVersamento.get(2));
			id_singoloVersamento.setIndiceDati((Integer) listaFieldId_singoloVersamento.get(3));
		}
		
		return id_singoloVersamento;
		
	}

	@Override
	public Long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdSingoloVersamento id, boolean throwNotFound)
			throws NotFoundException, ServiceException, NotImplementedException, Exception {
	
		return this.findIdSingoloVersamento(jdbcProperties,log,connection,sqlQueryObject,id,throwNotFound);
			
	}
	
	@Override
	public List<List<Object>> nativeQuery(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
											String sql,List<Class<?>> returnClassTypes,Object ... param) throws ServiceException,NotFoundException,NotImplementedException,Exception{
		
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.nativeQuery(jdbcProperties, log, connection, sqlQueryObject,
																							sql,returnClassTypes,param);
														
	}
	
	protected Long findIdSingoloVersamento(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdSingoloVersamento id, boolean throwNotFound) throws NotFoundException, ServiceException, NotImplementedException, Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);

		ISQLQueryObject sqlQueryObjectGet = sqlQueryObject.newSQLQueryObject();

		if(id.getId() != null && id.getId() > 0) {
			return id.getId();
		}
		
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] searchParams_singoloVersamento = null;
		if(id.getIdVersamento().getId() != null && id.getIdVersamento().getId() > 0) {
			// Object _singoloVersamento
			sqlQueryObjectGet.addFromTable(this.getSingoloVersamentoFieldConverter().toTable(SingoloVersamento.model()));
			sqlQueryObjectGet.addSelectField("id");
			sqlQueryObjectGet.setANDLogicOperator(true);
//			sqlQueryObjectGet.setSelectDistinct(true);
			sqlQueryObjectGet.addWhereCondition(this.getSingoloVersamentoFieldConverter().toColumn(SingoloVersamento.model().COD_SINGOLO_VERSAMENTO_ENTE,true)+"=?");
			sqlQueryObjectGet.addWhereCondition(this.getSingoloVersamentoFieldConverter().toColumn(SingoloVersamento.model().INDICE_DATI,true)+"=?");
			sqlQueryObjectGet.addWhereCondition("id_versamento=?");

			// Recupero _singoloVersamento
			searchParams_singoloVersamento = new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] { 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id.getCodSingoloVersamentoEnte(),SingoloVersamento.model().COD_SINGOLO_VERSAMENTO_ENTE.getFieldType()),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id.getIndiceDati(),SingoloVersamento.model().INDICE_DATI.getFieldType()),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id.getIdVersamento().getId(),Long.class)
			};
		} else {
			// Object _singoloVersamento
			sqlQueryObjectGet.addFromTable(this.getSingoloVersamentoFieldConverter().toTable(SingoloVersamento.model()));
			sqlQueryObjectGet.addFromTable(this.getSingoloVersamentoFieldConverter().toTable(SingoloVersamento.model().ID_VERSAMENTO));
			sqlQueryObjectGet.addFromTable(this.getSingoloVersamentoFieldConverter().toTable(SingoloVersamento.model().ID_VERSAMENTO.ID_APPLICAZIONE));
			sqlQueryObjectGet.addSelectField(this.getSingoloVersamentoFieldConverter().toTable(SingoloVersamento.model())+".id");
			sqlQueryObjectGet.setANDLogicOperator(true);
//			sqlQueryObjectGet.setSelectDistinct(true);
			sqlQueryObjectGet.addWhereCondition(this.getSingoloVersamentoFieldConverter().toColumn(SingoloVersamento.model().ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE,true)+"=?");
			sqlQueryObjectGet.addWhereCondition(this.getSingoloVersamentoFieldConverter().toColumn(SingoloVersamento.model().ID_VERSAMENTO.COD_VERSAMENTO_ENTE,true)+"=?");
			sqlQueryObjectGet.addWhereCondition(this.getSingoloVersamentoFieldConverter().toColumn(SingoloVersamento.model().COD_SINGOLO_VERSAMENTO_ENTE,true)+"=?");
			sqlQueryObjectGet.addWhereCondition(this.getSingoloVersamentoFieldConverter().toColumn(SingoloVersamento.model().INDICE_DATI,true)+"=?");
			sqlQueryObjectGet.addWhereCondition(this.getSingoloVersamentoFieldConverter().toTable(SingoloVersamento.model())+".id_versamento="+this.getSingoloVersamentoFieldConverter().toTable(SingoloVersamento.model().ID_VERSAMENTO)+".id");
			sqlQueryObjectGet.addWhereCondition(this.getSingoloVersamentoFieldConverter().toTable(SingoloVersamento.model().ID_VERSAMENTO)+".id_applicazione="+this.getSingoloVersamentoFieldConverter().toTable(SingoloVersamento.model().ID_VERSAMENTO.ID_APPLICAZIONE)+".id");

			// Recupero _singoloVersamento
			searchParams_singoloVersamento = new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] { 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id.getIdVersamento().getIdApplicazione().getCodApplicazione(),SingoloVersamento.model().ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE.getFieldType()),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id.getIdVersamento().getCodVersamentoEnte(),SingoloVersamento.model().ID_VERSAMENTO.COD_VERSAMENTO_ENTE.getFieldType()),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id.getCodSingoloVersamentoEnte(),SingoloVersamento.model().COD_SINGOLO_VERSAMENTO_ENTE.getFieldType()),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id.getIndiceDati(),SingoloVersamento.model().INDICE_DATI.getFieldType())
			};
		}

		Long id_singoloVersamento = null;
		try{
			id_singoloVersamento = (Long) jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet.createSQLQuery(), jdbcProperties.isShowSql(),
						Long.class, searchParams_singoloVersamento);
		}catch(NotFoundException notFound){
			if(throwNotFound){
				throw new NotFoundException(notFound);
			}
		}
		if(id_singoloVersamento==null || id_singoloVersamento<=0){
			if(throwNotFound){
				throw new NotFoundException("Not Found");
			}
		}
		
		return id_singoloVersamento;
	}
}
