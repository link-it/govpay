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

import org.slf4j.Logger;
import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.beans.FunctionField;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.InUse;
import org.openspcoop2.generic_project.beans.NonNegativeNumber;
import org.openspcoop2.generic_project.beans.Union;
import org.openspcoop2.generic_project.beans.UnionExpression;
import org.openspcoop2.generic_project.dao.jdbc.IJDBCServiceSearchWithoutId;
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

import it.govpay.orm.PagamentoPortaleVersamento;
import it.govpay.orm.dao.jdbc.converter.PagamentoPortaleVersamentoFieldConverter;
import it.govpay.orm.dao.jdbc.fetch.PagamentoPortaleVersamentoFetch;

/**     
 * JDBCPagamentoPortaleVersamentoServiceSearchImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCPagamentoPortaleVersamentoServiceSearchImpl implements IJDBCServiceSearchWithoutId<PagamentoPortaleVersamento, JDBCServiceManager> {

	private PagamentoPortaleVersamentoFieldConverter _pagamentoPortaleVersamentoFieldConverter = null;
	public PagamentoPortaleVersamentoFieldConverter getPagamentoPortaleVersamentoFieldConverter() {
		if(this._pagamentoPortaleVersamentoFieldConverter==null){
			this._pagamentoPortaleVersamentoFieldConverter = new PagamentoPortaleVersamentoFieldConverter(this.jdbcServiceManager.getJdbcProperties().getDatabaseType());
		}		
		return this._pagamentoPortaleVersamentoFieldConverter;
	}
	@Override
	public ISQLFieldConverter getFieldConverter() {
		return this.getPagamentoPortaleVersamentoFieldConverter();
	}

	private PagamentoPortaleVersamentoFetch pagamentoPortaleVersamentoFetch = new PagamentoPortaleVersamentoFetch();
	public PagamentoPortaleVersamentoFetch getPagamentoPortaleVersamentoFetch() {
		return this.pagamentoPortaleVersamentoFetch;
	}
	@Override
	public IJDBCFetch getFetch() {
		return this.getPagamentoPortaleVersamentoFetch();
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
	public List<PagamentoPortaleVersamento> findAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {

		List<PagamentoPortaleVersamento> list = new ArrayList<>();

		try {

			List<IField> fields = new ArrayList<>();
			fields.add(new CustomField("id", Long.class, "id", this.getFieldConverter().toTable(PagamentoPortaleVersamento.model())));
			fields.add(new CustomField("id_pagamento_portale", Long.class, "id_pagamento_portale", this.getFieldConverter().toTable(PagamentoPortaleVersamento.model())));
			fields.add(new CustomField("id_versamento", Long.class, "id_versamento", this.getFieldConverter().toTable(PagamentoPortaleVersamento.model())));

			List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));

			for(Map<String, Object> map: returnMap) {


				PagamentoPortaleVersamento pagamentoPortaleVersamento = (PagamentoPortaleVersamento)this.getFetch().fetch(jdbcProperties.getDatabase(), PagamentoPortaleVersamento.model(), map);

				if(idMappingResolutionBehaviour==null ||
						(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
						){
					Long idFK_pagamentoPortaleVersamento_pagamentoPortale = (Long) map.remove("id_pagamento_portale");

					it.govpay.orm.IdPagamentoPortale id_pagamentoPortaleVersamento_pagamentoPortale = null;
					if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
						id_pagamentoPortaleVersamento_pagamentoPortale = ((JDBCPagamentoPortaleServiceSearch)(this.getServiceManager().getPagamentoPortaleServiceSearch())).findId(idFK_pagamentoPortaleVersamento_pagamentoPortale, false);
					}else{
						id_pagamentoPortaleVersamento_pagamentoPortale = new it.govpay.orm.IdPagamentoPortale();
					}
					id_pagamentoPortaleVersamento_pagamentoPortale.setId(idFK_pagamentoPortaleVersamento_pagamentoPortale);
					pagamentoPortaleVersamento.setIdPagamentoPortale(id_pagamentoPortaleVersamento_pagamentoPortale);
				}

				if(idMappingResolutionBehaviour==null ||
						(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
						){
					Long idFK_pagamentoPortaleVersamento_versamento = (Long) map.remove("id_versamento");

					it.govpay.orm.IdVersamento id_pagamentoPortaleVersamento_versamento = null;
					if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
						id_pagamentoPortaleVersamento_versamento = ((JDBCVersamentoServiceSearch)(this.getServiceManager().getVersamentoServiceSearch())).findId(idFK_pagamentoPortaleVersamento_versamento, false);
					}else{
						id_pagamentoPortaleVersamento_versamento = new it.govpay.orm.IdVersamento();
					}
					id_pagamentoPortaleVersamento_versamento.setId(idFK_pagamentoPortaleVersamento_versamento);
					pagamentoPortaleVersamento.setIdVersamento(id_pagamentoPortaleVersamento_versamento);
				}

				list.add(pagamentoPortaleVersamento);
			}
		} catch(NotFoundException e) {}
		return list;      

	}

	@Override
	public PagamentoPortaleVersamento find(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) 
			throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException,Exception {

		long id = this.findTableId(jdbcProperties, log, connection, sqlQueryObject, expression);
		if(id>0){
			return this.get(jdbcProperties, log, connection, sqlQueryObject, id, idMappingResolutionBehaviour);
		}else{
			throw new NotFoundException("Entry with id["+id+"] not found");
		}

	}

	@Override
	public NonNegativeNumber count(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws NotImplementedException, ServiceException,Exception {

		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareCount(jdbcProperties, log, connection, sqlQueryObject, expression,
				this.getPagamentoPortaleVersamentoFieldConverter(), PagamentoPortaleVersamento.model());

		sqlQueryObject.addSelectCountField(this.getPagamentoPortaleVersamentoFieldConverter().toTable(PagamentoPortaleVersamento.model())+".id","tot",true);

		this._join(expression,sqlQueryObject);

		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.count(jdbcProperties, log, connection, sqlQueryObject, expression,
				this.getPagamentoPortaleVersamentoFieldConverter(), PagamentoPortaleVersamento.model(),listaQuery);
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
							this.getPagamentoPortaleVersamentoFieldConverter(), field);

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
				expression, this.getPagamentoPortaleVersamentoFieldConverter(), PagamentoPortaleVersamento.model(), 
				listaQuery,listaParams);

		this._join(expression,sqlQueryObject);

		List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.select(jdbcProperties, log, connection,
				org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareSqlQueryObjectForSelectDistinct(sqlQueryObject,sqlQueryObjectDistinct), 
				expression, this.getPagamentoPortaleVersamentoFieldConverter(), PagamentoPortaleVersamento.model(),
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
				this.getPagamentoPortaleVersamentoFieldConverter(), PagamentoPortaleVersamento.model(), 
				sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);

		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				this._join(expression,sqlQueryObjectInnerList.get(i));
			}
		}

		List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.union(jdbcProperties, log, connection, sqlQueryObject, 
				this.getPagamentoPortaleVersamentoFieldConverter(), PagamentoPortaleVersamento.model(), 
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
				this.getPagamentoPortaleVersamentoFieldConverter(), PagamentoPortaleVersamento.model(), 
				sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);

		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				this._join(expression,sqlQueryObjectInnerList.get(i));
			}
		}

		NonNegativeNumber number = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.unionCount(jdbcProperties, log, connection, sqlQueryObject, 
				this.getPagamentoPortaleVersamentoFieldConverter(), PagamentoPortaleVersamento.model(), 
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
			return new JDBCExpression(this.getPagamentoPortaleVersamentoFieldConverter());
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}


	@Override
	public JDBCPaginatedExpression newPaginatedExpression(Logger log) throws NotImplementedException, ServiceException {
		try{
			return new JDBCPaginatedExpression(this.getPagamentoPortaleVersamentoFieldConverter());
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
	public PagamentoPortaleVersamento get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
		return this._get(jdbcProperties, log, connection, sqlQueryObject, Long.valueOf(tableId), idMappingResolutionBehaviour);
	}

	private PagamentoPortaleVersamento _get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {

		IField idField = new CustomField("id", Long.class, "id", this.getFieldConverter().toTable(PagamentoPortaleVersamento.model()));
		JDBCPaginatedExpression expression = this.newPaginatedExpression(log);

		expression.equals(idField, tableId);
		List<PagamentoPortaleVersamento> lst = this.findAll(jdbcProperties, log, connection, sqlQueryObject.newSQLQueryObject(), expression, idMappingResolutionBehaviour);

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

		boolean existsPagamentoPortaleVersamento = false;

		sqlQueryObject = sqlQueryObject.newSQLQueryObject();
		sqlQueryObject.setANDLogicOperator(true);

		sqlQueryObject.addWhereCondition("id=?");


		// Exists pagamentoPortaleVersamento
		existsPagamentoPortaleVersamento = jdbcUtilities.exists(sqlQueryObject.createSQLQuery(), jdbcProperties.isShowSql(),
				new JDBCObject(tableId,Long.class));


		return existsPagamentoPortaleVersamento;

	}

	private void _join(IExpression expression, ISQLQueryObject sqlQueryObject) throws NotImplementedException, ServiceException, Exception{

		if(expression.inUseModel(PagamentoPortaleVersamento.model().ID_PAGAMENTO_PORTALE,false)){
			String tableName1 = this.getPagamentoPortaleVersamentoFieldConverter().toAliasTable(PagamentoPortaleVersamento.model());
			String tableName2 = this.getPagamentoPortaleVersamentoFieldConverter().toAliasTable(PagamentoPortaleVersamento.model().ID_PAGAMENTO_PORTALE);
			sqlQueryObject.addWhereCondition(tableName1+".id_pagamento_portale="+tableName2+".id");
		}
		if(expression.inUseModel(PagamentoPortaleVersamento.model().ID_VERSAMENTO,false)){
			String tableName1 = this.getPagamentoPortaleVersamentoFieldConverter().toAliasTable(PagamentoPortaleVersamento.model());
			String tableName2 = this.getPagamentoPortaleVersamentoFieldConverter().toAliasTable(PagamentoPortaleVersamento.model().ID_VERSAMENTO);
			sqlQueryObject.addWhereCondition(tableName1+".id_versamento="+tableName2+".id");
		}

	}

	protected java.util.List<Object> _getRootTablePrimaryKeyValues(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, PagamentoPortaleVersamento pagamentoPortaleVersamento) throws NotFoundException, ServiceException, NotImplementedException, Exception{
		// Identificativi
		java.util.List<Object> rootTableIdValues = new java.util.ArrayList<>();
		rootTableIdValues.add(pagamentoPortaleVersamento.getId());

		return rootTableIdValues;
	}

	protected Map<String, List<IField>> _getMapTableToPKColumn() throws NotImplementedException, Exception{

		PagamentoPortaleVersamentoFieldConverter converter = this.getPagamentoPortaleVersamentoFieldConverter();
		Map<String, List<IField>> mapTableToPKColumn = new java.util.Hashtable<>();
		UtilsTemplate<IField> utilities = new UtilsTemplate<>();

		// PagamentoPortaleVersamento.model()
		mapTableToPKColumn.put(converter.toTable(PagamentoPortaleVersamento.model()),
				utilities.newList(
						new CustomField("id", Long.class, "id", converter.toTable(PagamentoPortaleVersamento.model()))
						));

		// PagamentoPortaleVersamento.model().ID_PAGAMENTO_PORTALE
		mapTableToPKColumn.put(converter.toTable(PagamentoPortaleVersamento.model().ID_PAGAMENTO_PORTALE),
				utilities.newList(
						new CustomField("id", Long.class, "id", converter.toTable(PagamentoPortaleVersamento.model().ID_PAGAMENTO_PORTALE))
						));

		// PagamentoPortaleVersamento.model().ID_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(PagamentoPortaleVersamento.model().ID_VERSAMENTO),
				utilities.newList(
						new CustomField("id", Long.class, "id", converter.toTable(PagamentoPortaleVersamento.model().ID_VERSAMENTO))
						));

		// PagamentoPortaleVersamento.model().ID_VERSAMENTO.ID_APPLICAZIONE
		mapTableToPKColumn.put(converter.toTable(PagamentoPortaleVersamento.model().ID_VERSAMENTO.ID_APPLICAZIONE),
				utilities.newList(
						new CustomField("id", Long.class, "id", converter.toTable(PagamentoPortaleVersamento.model().ID_VERSAMENTO.ID_APPLICAZIONE))
						));

		return mapTableToPKColumn;		
	}

	@Override
	public List<Long> findAllTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression paginatedExpression) throws ServiceException, NotImplementedException, Exception {

		List<Long> list = new ArrayList<>();

		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getPagamentoPortaleVersamentoFieldConverter().toTable(PagamentoPortaleVersamento.model())+".id");
		Class<?> objectIdClass = Long.class;

		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFindAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
				this.getPagamentoPortaleVersamentoFieldConverter(), PagamentoPortaleVersamento.model());

		this._join(paginatedExpression,sqlQueryObject);

		List<Object> listObjects = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.findAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
				this.getPagamentoPortaleVersamentoFieldConverter(), PagamentoPortaleVersamento.model(), objectIdClass, listaQuery);
		for(Object object: listObjects) {
			list.add((Long)object);
		}

		return list;

	}

	@Override
	public long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws ServiceException, NotFoundException, MultipleResultException, NotImplementedException, Exception {

		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getPagamentoPortaleVersamentoFieldConverter().toTable(PagamentoPortaleVersamento.model())+".id");
		Class<?> objectIdClass = Long.class;

		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFind(jdbcProperties, log, connection, sqlQueryObject, expression,
				this.getPagamentoPortaleVersamentoFieldConverter(), PagamentoPortaleVersamento.model());

		this._join(expression,sqlQueryObject);

		Object res = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.find(jdbcProperties, log, connection, sqlQueryObject, expression,
				this.getPagamentoPortaleVersamentoFieldConverter(), PagamentoPortaleVersamento.model(), objectIdClass, listaQuery);
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

		return inUse;

	}



	@Override
	public List<List<Object>> nativeQuery(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
			String sql,List<Class<?>> returnClassTypes,Object ... param) throws ServiceException,NotFoundException,NotImplementedException,Exception{

		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.nativeQuery(jdbcProperties, log, connection, sqlQueryObject,
				sql,returnClassTypes,param);

	}

}
