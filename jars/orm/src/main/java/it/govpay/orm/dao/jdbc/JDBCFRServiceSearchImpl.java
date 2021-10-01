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
import java.util.Date;
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
import org.openspcoop2.utils.TipiDatabase;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.slf4j.Logger;

import it.govpay.bd.ConnectionManager;
import it.govpay.orm.FR;
import it.govpay.orm.IdFr;
import it.govpay.orm.dao.jdbc.converter.FRFieldConverter;
import it.govpay.orm.dao.jdbc.fetch.FRFetch;

/**     
 * JDBCFRServiceSearchImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCFRServiceSearchImpl implements IJDBCServiceSearchWithId<FR, IdFr, JDBCServiceManager> {

	private FRFieldConverter _frFieldConverter = null;
	public FRFieldConverter getFRFieldConverter() {
		if(this._frFieldConverter==null){
			this._frFieldConverter = new FRFieldConverter(this.jdbcServiceManager.getJdbcProperties().getDatabaseType());
		}		
		return this._frFieldConverter;
	}
	@Override
	public ISQLFieldConverter getFieldConverter() {
		return this.getFRFieldConverter();
	}
	
	private FRFetch frFetch = new FRFetch();
	public FRFetch getFRFetch() {
		return this.frFetch;
	}
	@Override
	public IJDBCFetch getFetch() {
		return this.getFRFetch();
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
	public IdFr convertToId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, FR fr) throws NotImplementedException, ServiceException, Exception{
	
		IdFr idFR = new IdFr();
		idFR.setCodFlusso(fr.getCodFlusso());
		idFR.setDataOraFlusso(fr.getDataOraFlusso());
	
		return idFR;
	}
	
	@Override
	public FR get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdFr id, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException,Exception {
		Long id_fr = ( (id!=null && id.getId()!=null && id.getId()>0) ? id.getId() : this.findIdFR(jdbcProperties, log, connection, sqlQueryObject, id, true));
		return this._get(jdbcProperties, log, connection, sqlQueryObject, id_fr,idMappingResolutionBehaviour);
		
		
	}
	
	@Override
	public boolean exists(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdFr id) throws MultipleResultException, NotImplementedException, ServiceException,Exception {

		Long id_fr = this.findIdFR(jdbcProperties, log, connection, sqlQueryObject, id, false);
		return id_fr != null && id_fr > 0;
		
	}
	
	@Override
	public List<IdFr> findAllIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {
        // default behaviour (id-mapping)
        if(idMappingResolutionBehaviour==null){
                idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
        }

		List<IdFr> list = new ArrayList<>();

		try{
			List<IField> fields = new ArrayList<>();

			fields.add(FR.model().COD_FLUSSO);
			fields.add(FR.model().DATA_ORA_FLUSSO);
        
			List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));

			for(Map<String, Object> map: returnMap) {
				list.add(this.convertToId(jdbcProperties, log, connection, sqlQueryObject, (FR)this.getFRFetch().fetch(jdbcProperties.getDatabase(), FR.model(), map)));
        }
		} catch(NotFoundException e) {}

        return list;
		
	}
	
	@Override
	public List<FR> findAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {

		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		List<FR> list = new ArrayList<>();

		TipiDatabase tipoDatabase = ConnectionManager.getJDBCServiceManagerProperties().getDatabase();
		
		switch (tipoDatabase) {
		case DB2:
		case DEFAULT:
		case DERBY:
		case HSQL:
		case MYSQL:
		case POSTGRESQL:
		case SQLSERVER:
			try{
				List<IField> fields = new ArrayList<>();
				fields.add(new CustomField("id", Long.class, "id", this.getFRFieldConverter().toTable(FR.model())));
				fields.add(FR.model().COD_FLUSSO);
				fields.add(FR.model().COD_PSP);
				fields.add(FR.model().COD_DOMINIO);
				fields.add(FR.model().STATO);
				fields.add(FR.model().DESCRIZIONE_STATO);
				fields.add(FR.model().DATA_ACQUISIZIONE);
				fields.add(FR.model().DATA_ORA_FLUSSO);
				fields.add(FR.model().DATA_REGOLAMENTO);
				fields.add(FR.model().NUMERO_PAGAMENTI);
				fields.add(FR.model().IMPORTO_TOTALE_PAGAMENTI);
				fields.add(FR.model().XML);
				fields.add(FR.model().IUR);
				fields.add(FR.model().COD_BIC_RIVERSAMENTO);
				fields.add(FR.model().RAGIONE_SOCIALE_DOMINIO);
				fields.add(FR.model().RAGIONE_SOCIALE_PSP);
				fields.add(FR.model().OBSOLETO);
				fields.add(new CustomField("id_incasso", Long.class, "id_incasso", this.getFRFieldConverter().toTable(FR.model())));
                List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));

                for(Map<String, Object> map: returnMap) {
                        
                    Long idIncasso = null;
                    
                    Object idIncassoObj = map.remove("id_incasso");

                    if(idIncassoObj instanceof Long)
                            idIncasso = (Long) idIncassoObj;
                    
                    
                    FR fr = (FR)this.getFRFetch().fetch(jdbcProperties.getDatabase(), FR.model(), map);
                    
                    if(idIncasso != null) {
                        it.govpay.orm.IdIncasso id_pagamento_incasso = new it.govpay.orm.IdIncasso();
                        id_pagamento_incasso.setId(idIncasso);
                        fr.setIdIncasso(id_pagamento_incasso);
                    }
                    
                    list.add(fr);
                }
			} catch(NotFoundException e) {}
			break;
		case ORACLE:
			try{
				List<IField> fieldsEsterni = new ArrayList<>();
				fieldsEsterni.add(new CustomField("id", Long.class, "id", this.getFRFieldConverter().toTable(FR.model())));
				fieldsEsterni.add(FR.model().DATA_ACQUISIZIONE);
	 			List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fieldsEsterni.toArray(new IField[1]));
	 			
	 			org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
						new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
			
	 			for(Map<String, Object> map: returnMap) {
	 				Object idObj = map.remove("id");
	 				Long id = (Long) idObj;
	 				
	 				ISQLQueryObject sqlQueryObjectGet = sqlQueryObject.newSQLQueryObject();
					
	 				FR fr = new FR();
	 				
	 				// Object fr
	 				ISQLQueryObject sqlQueryObjectGet_fr = sqlQueryObjectGet.newSQLQueryObject();
	 				sqlQueryObjectGet_fr.setANDLogicOperator(true);
	 				sqlQueryObjectGet_fr.addFromTable(this.getFRFieldConverter().toTable(FR.model()));
	 				sqlQueryObjectGet_fr.addSelectField("id");
	 				sqlQueryObjectGet_fr.addSelectField(this.getFRFieldConverter().toColumn(FR.model().COD_PSP,true));
	 				sqlQueryObjectGet_fr.addSelectField(this.getFRFieldConverter().toColumn(FR.model().COD_DOMINIO,true));
	 				sqlQueryObjectGet_fr.addSelectField(this.getFRFieldConverter().toColumn(FR.model().COD_FLUSSO,true));
	 				sqlQueryObjectGet_fr.addSelectField(this.getFRFieldConverter().toColumn(FR.model().STATO,true));
	 				sqlQueryObjectGet_fr.addSelectField(this.getFRFieldConverter().toColumn(FR.model().DESCRIZIONE_STATO,true));
	 				sqlQueryObjectGet_fr.addSelectField(this.getFRFieldConverter().toColumn(FR.model().IUR,true));
	 				sqlQueryObjectGet_fr.addSelectField(this.getFRFieldConverter().toColumn(FR.model().DATA_ORA_FLUSSO,true));
	 				sqlQueryObjectGet_fr.addSelectField(this.getFRFieldConverter().toColumn(FR.model().DATA_REGOLAMENTO,true));
	 				sqlQueryObjectGet_fr.addSelectField(this.getFRFieldConverter().toColumn(FR.model().DATA_ACQUISIZIONE,true));
	 				sqlQueryObjectGet_fr.addSelectField(this.getFRFieldConverter().toColumn(FR.model().NUMERO_PAGAMENTI,true));
	 				sqlQueryObjectGet_fr.addSelectField(this.getFRFieldConverter().toColumn(FR.model().IMPORTO_TOTALE_PAGAMENTI,true));
	 				sqlQueryObjectGet_fr.addSelectField(this.getFRFieldConverter().toColumn(FR.model().COD_BIC_RIVERSAMENTO,true));
	 				sqlQueryObjectGet_fr.addSelectField(this.getFRFieldConverter().toColumn(FR.model().XML,true));
	 				sqlQueryObjectGet_fr.addSelectField(this.getFRFieldConverter().toColumn(FR.model().RAGIONE_SOCIALE_PSP,true));
	 				sqlQueryObjectGet_fr.addSelectField(this.getFRFieldConverter().toColumn(FR.model().RAGIONE_SOCIALE_DOMINIO,true));
	 				sqlQueryObjectGet_fr.addSelectField(this.getFRFieldConverter().toColumn(FR.model().OBSOLETO,true));
	 				sqlQueryObjectGet_fr.addWhereCondition("id=?");
	
	 				// Get fr
	 				fr = (FR) jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet_fr.createSQLQuery(), jdbcProperties.isShowSql(), FR.model(), this.getFRFetch(),
	 					new JDBCObject(id,Long.class));
	
	
	 				if(idMappingResolutionBehaviour==null ||
	 					(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
	 				){
	 					try {
		 					// Object _fr_incasso (recupero id)
		 					ISQLQueryObject sqlQueryObjectGet_fr_incasso_readFkId = sqlQueryObjectGet.newSQLQueryObject();
		 					sqlQueryObjectGet_fr_incasso_readFkId.addFromTable(this.getFRFieldConverter().toTable(it.govpay.orm.FR.model()));
		 					sqlQueryObjectGet_fr_incasso_readFkId.addSelectField("id_incasso");
		 					sqlQueryObjectGet_fr_incasso_readFkId.addWhereCondition("id=?");
		 					sqlQueryObjectGet_fr_incasso_readFkId.setANDLogicOperator(true);
		 					Long idFK_fr_incasso = (Long) jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet_fr_incasso_readFkId.createSQLQuery(), jdbcProperties.isShowSql(),Long.class,
		 							new JDBCObject(fr.getId(),Long.class));
		 					
		 					it.govpay.orm.IdIncasso id_fr_incasso = new it.govpay.orm.IdIncasso();
		 					id_fr_incasso.setId(idFK_fr_incasso);
		 					fr.setIdIncasso(id_fr_incasso);
		 				} catch(NotFoundException e) {}
	 				}
	 				
		        	list.add(fr);
	 				
	 			}
			} catch(NotFoundException e) {}
		break;
		}
		
		return list;      
		
	}
	
	@Override
	public FR find(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) 
		throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException,Exception {

		JDBCPaginatedExpression pagExpr = this.toPaginatedExpression(expression,log);
		
		List<FR> lst = this.findAll(jdbcProperties, log, connection, sqlQueryObject, pagExpr, idMappingResolutionBehaviour);

		if(lst.size() <=0)
			throw new NotFoundException("Nessuna entry corrisponde ai criteri indicati.");

		if(lst.size() > 1)
			throw new MultipleResultException("I criteri indicati individuano piu' entry.");

		return lst.get(0);
	}
	
	@Override
	public NonNegativeNumber count(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws NotImplementedException, ServiceException,Exception {
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareCount(jdbcProperties, log, connection, sqlQueryObject, expression,
												this.getFRFieldConverter(), FR.model());
		
		sqlQueryObject.addSelectCountField(this.getFRFieldConverter().toTable(FR.model())+".id","tot",true);
		
		this._join(expression,sqlQueryObject);
		
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.count(jdbcProperties, log, connection, sqlQueryObject, expression,
																			this.getFRFieldConverter(), FR.model(),listaQuery);
	}

	@Override
	public InUse inUse(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdFr id) throws NotFoundException, NotImplementedException, ServiceException,Exception {
		
		Long id_fr = this.findIdFR(jdbcProperties, log, connection, sqlQueryObject, id, true);
        return this._inUse(jdbcProperties, log, connection, sqlQueryObject, id_fr);
		
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
												this.getFRFieldConverter(), field);

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
        						expression, this.getFRFieldConverter(), FR.model(), 
        						listaQuery,listaParams);
		
		this._join(expression,sqlQueryObject);
        
        List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.select(jdbcProperties, log, connection,
        								org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareSqlQueryObjectForSelectDistinct(sqlQueryObject,sqlQueryObjectDistinct), 
        								expression, this.getFRFieldConverter(), FR.model(),
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
        						this.getFRFieldConverter(), FR.model(), 
        						sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);
		
		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				this._join(expression,sqlQueryObjectInnerList.get(i));
			}
		}
        
        List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.union(jdbcProperties, log, connection, sqlQueryObject, 
        								this.getFRFieldConverter(), FR.model(), 
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
        						this.getFRFieldConverter(), FR.model(), 
        						sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);
		
		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				this._join(expression,sqlQueryObjectInnerList.get(i));
			}
		}
        
        NonNegativeNumber number = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.unionCount(jdbcProperties, log, connection, sqlQueryObject, 
        								this.getFRFieldConverter(), FR.model(), 
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
			return new JDBCExpression(this.getFRFieldConverter());
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}


	@Override
	public JDBCPaginatedExpression newPaginatedExpression(Logger log) throws NotImplementedException, ServiceException {
		try{
			return new JDBCPaginatedExpression(this.getFRFieldConverter());
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
	public void mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdFr id, FR obj) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		this._mappingTableIds(jdbcProperties,log,connection,sqlQueryObject,obj,
				this.get(jdbcProperties,log,connection,sqlQueryObject,id,null));
	}
	
	@Override
	public void mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, FR obj) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		this._mappingTableIds(jdbcProperties,log,connection,sqlQueryObject,obj,
				this.get(jdbcProperties,log,connection,sqlQueryObject,tableId,null));
	}
	private void _mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, FR obj, FR imgSaved) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		if(imgSaved==null){
			return;
		}
		obj.setId(imgSaved.getId());
		if(obj.getIdSingoloVersamento()!=null && 
				imgSaved.getIdSingoloVersamento()!=null){
			obj.getIdSingoloVersamento().setId(imgSaved.getIdSingoloVersamento().getId());
			if(obj.getIdSingoloVersamento().getIdVersamento()!=null && 
					imgSaved.getIdSingoloVersamento().getIdVersamento()!=null){
				obj.getIdSingoloVersamento().getIdVersamento().setId(imgSaved.getIdSingoloVersamento().getIdVersamento().getId());
				if(obj.getIdSingoloVersamento().getIdVersamento().getIdApplicazione()!=null && 
						imgSaved.getIdSingoloVersamento().getIdVersamento().getIdApplicazione()!=null){
					obj.getIdSingoloVersamento().getIdVersamento().getIdApplicazione().setId(imgSaved.getIdSingoloVersamento().getIdVersamento().getIdApplicazione().getId());
				}
				if(obj.getIdSingoloVersamento().getIdVersamento().getIdUo()!=null && 
						imgSaved.getIdSingoloVersamento().getIdVersamento().getIdUo()!=null){
					obj.getIdSingoloVersamento().getIdVersamento().getIdUo().setId(imgSaved.getIdSingoloVersamento().getIdVersamento().getIdUo().getId());
					if(obj.getIdSingoloVersamento().getIdVersamento().getIdUo().getIdDominio()!=null && 
							imgSaved.getIdSingoloVersamento().getIdVersamento().getIdUo().getIdDominio()!=null){
						obj.getIdSingoloVersamento().getIdVersamento().getIdUo().getIdDominio().setId(imgSaved.getIdSingoloVersamento().getIdVersamento().getIdUo().getIdDominio().getId());
					}
				}
				if(obj.getIdSingoloVersamento().getIdVersamento().getIdTipoVersamento()!=null && 
						imgSaved.getIdSingoloVersamento().getIdVersamento().getIdTipoVersamento()!=null){
					obj.getIdSingoloVersamento().getIdVersamento().getIdTipoVersamento().setId(imgSaved.getIdSingoloVersamento().getIdVersamento().getIdTipoVersamento().getId());
				}
			}
			if(obj.getIdSingoloVersamento().getIdTributo()!=null && 
					imgSaved.getIdSingoloVersamento().getIdTributo()!=null){
				obj.getIdSingoloVersamento().getIdTributo().setId(imgSaved.getIdSingoloVersamento().getIdTributo().getId());
				if(obj.getIdSingoloVersamento().getIdTributo().getIdDominio()!=null && 
						imgSaved.getIdSingoloVersamento().getIdTributo().getIdDominio()!=null){
					obj.getIdSingoloVersamento().getIdTributo().getIdDominio().setId(imgSaved.getIdSingoloVersamento().getIdTributo().getIdDominio().getId());
				}
				if(obj.getIdSingoloVersamento().getIdTributo().getIdTipoTributo()!=null && 
						imgSaved.getIdSingoloVersamento().getIdTributo().getIdTipoTributo()!=null){
					obj.getIdSingoloVersamento().getIdTributo().getIdTipoTributo().setId(imgSaved.getIdSingoloVersamento().getIdTributo().getIdTipoTributo().getId());
				}
			}
		}
		if(obj.getIdIncasso()!=null && 
				imgSaved.getIdIncasso()!=null){
			obj.getIdIncasso().setId(imgSaved.getIdIncasso().getId());
		}

	}
	
	@Override
	public FR get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
		return this._get(jdbcProperties, log, connection, sqlQueryObject, Long.valueOf(tableId), idMappingResolutionBehaviour);
	}
	
	private FR _get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
		IField idField = new CustomField("id", Long.class, "id", this.getFRFieldConverter().toTable(FR.model()));
		JDBCPaginatedExpression expression = this.newPaginatedExpression(log);
		
		expression.equals(idField, tableId);
		List<FR> lst = this.findAll(jdbcProperties, log, connection, sqlQueryObject.newSQLQueryObject(), expression, idMappingResolutionBehaviour);
		
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
				
		boolean existsFR = false;

		sqlQueryObject = sqlQueryObject.newSQLQueryObject();
		sqlQueryObject.setANDLogicOperator(true);

		sqlQueryObject.addFromTable(this.getFRFieldConverter().toTable(FR.model()));
		sqlQueryObject.addSelectField(this.getFRFieldConverter().toColumn(FR.model().COD_FLUSSO,true));
		sqlQueryObject.addWhereCondition("id=?");


		// Exists fr
		existsFR = jdbcUtilities.exists(sqlQueryObject.createSQLQuery(), jdbcProperties.isShowSql(),
			new JDBCObject(tableId,Long.class));

		
        return existsFR;
	
	}
	
	private void _join(IExpression expression, ISQLQueryObject sqlQueryObject) throws NotImplementedException, ServiceException, Exception{
		
		boolean addRendicontazioni = false;
		
		String tableNameFr = this.getFieldConverter().toAliasTable(FR.model());
		String tableNameRendicontazioni = this.getFieldConverter().toAliasTable(FR.model().ID_RENDICONTAZIONE);
		
		if(expression.inUseModel(FR.model().ID_RENDICONTAZIONE,false)){
			sqlQueryObject.addWhereCondition(tableNameFr+".id="+tableNameRendicontazioni+".id_fr");
			addRendicontazioni = true;
		}
		
		
		if(expression.inUseModel(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO,false)){
			String tableNameSingoliVersamenti = this.getFieldConverter().toAliasTable(FR.model().ID_SINGOLO_VERSAMENTO);
			String tableNameVersamenti = this.getFieldConverter().toAliasTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO);
			
			sqlQueryObject.setSelectDistinct(true);
			if(!addRendicontazioni) {
				sqlQueryObject.addFromTable(tableNameRendicontazioni);
				sqlQueryObject.addWhereCondition(tableNameFr+".id="+tableNameRendicontazioni+".id_fr");
			}
			
			sqlQueryObject.addFromTable(tableNameSingoliVersamenti);
			sqlQueryObject.addWhereCondition(tableNameRendicontazioni+".id_singolo_versamento="+tableNameSingoliVersamenti+".id");
			
			// sqlQueryObject.addFromTable(tableNameVersamenti);
			
			sqlQueryObject.addWhereCondition(tableNameSingoliVersamenti+".id_versamento="+tableNameVersamenti+".id");
			
			addRendicontazioni = true;
		}
		
		if(expression.inUseModel(FR.model().ID_INCASSO,false)){
			String tableName2 = this.getFieldConverter().toAliasTable(FR.model().ID_INCASSO);
			sqlQueryObject.addWhereCondition(tableNameFr+".id_incasso="+tableName2+".id");

		} 
		
		
	}
	
	protected java.util.List<Object> _getRootTablePrimaryKeyValues(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdFr id) throws NotFoundException, ServiceException, NotImplementedException, Exception{
	    // Identificativi
        java.util.List<Object> rootTableIdValues = new java.util.ArrayList<>();
		Long longId = this.findIdFR(jdbcProperties, log, connection, sqlQueryObject.newSQLQueryObject(), id, true);
		rootTableIdValues.add(longId);
        
        return rootTableIdValues;
	}
	
	protected Map<String, List<IField>> _getMapTableToPKColumn() throws NotImplementedException, Exception{
	
		FRFieldConverter converter = this.getFRFieldConverter();
		Map<String, List<IField>> mapTableToPKColumn = new java.util.Hashtable<>();
		UtilsTemplate<IField> utilities = new UtilsTemplate<>();

		//		  If a table doesn't have a primary key, don't add it to this map

		// FR.model()
		mapTableToPKColumn.put(converter.toTable(FR.model()),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(FR.model()))
			));

		// FR.model().ID_SINGOLO_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(FR.model().ID_SINGOLO_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(FR.model().ID_SINGOLO_VERSAMENTO))
			));

		// FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO))
			));

		// FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE
		mapTableToPKColumn.put(converter.toTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE))
			));

		// FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO
		mapTableToPKColumn.put(converter.toTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO))
			));

		// FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO))
			));

		// FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO))
			));

		// FR.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO
		mapTableToPKColumn.put(converter.toTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO))
			));

		// FR.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO))
			));

		// FR.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO
		mapTableToPKColumn.put(converter.toTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO))
			));

		// FR.model().ID_INCASSO
		mapTableToPKColumn.put(converter.toTable(FR.model().ID_INCASSO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(FR.model().ID_INCASSO))
			));

        
        return mapTableToPKColumn;		
	}
	
	@Override
	public List<Long> findAllTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression paginatedExpression) throws ServiceException, NotImplementedException, Exception {
		
		List<Long> list = new ArrayList<>();

		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getFRFieldConverter().toTable(FR.model())+".id");
		Class<?> objectIdClass = Long.class;
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFindAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
												this.getFRFieldConverter(), FR.model());
		
		this._join(paginatedExpression,sqlQueryObject);
		
		List<Object> listObjects = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.findAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
																			this.getFRFieldConverter(), FR.model(), objectIdClass, listaQuery);
		for(Object object: listObjects) {
			list.add((Long)object);
		}

        return list;
		
	}
	
	@Override
	public long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws ServiceException, NotFoundException, MultipleResultException, NotImplementedException, Exception {
	
		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getFRFieldConverter().toTable(FR.model())+".id");
		Class<?> objectIdClass = Long.class;
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFind(jdbcProperties, log, connection, sqlQueryObject, expression,
												this.getFRFieldConverter(), FR.model());
		
		this._join(expression,sqlQueryObject);

		Object res = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.find(jdbcProperties, log, connection, sqlQueryObject, expression,
														this.getFRFieldConverter(), FR.model(), objectIdClass, listaQuery);
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
	public IdFr findId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, boolean throwNotFound)
			throws NotFoundException, ServiceException, NotImplementedException, Exception {
		
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);

		ISQLQueryObject sqlQueryObjectGet = sqlQueryObject.newSQLQueryObject();


		// Object _fr
		sqlQueryObjectGet.addFromTable(this.getFRFieldConverter().toTable(FR.model()));
		sqlQueryObjectGet.addSelectField(this.getFRFieldConverter().toColumn(FR.model().COD_FLUSSO,true));
		sqlQueryObjectGet.addSelectField(this.getFRFieldConverter().toColumn(FR.model().DATA_ORA_FLUSSO,true));
		sqlQueryObjectGet.setANDLogicOperator(true);
		sqlQueryObjectGet.addWhereCondition("id=?");

		// Recupero _fr
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] searchParams_fr = new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] { 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tableId,Long.class)
		};
		List<Class<?>> listaFieldIdReturnType_fr = new ArrayList<>();
		listaFieldIdReturnType_fr.add(FR.model().COD_FLUSSO.getFieldType());
		listaFieldIdReturnType_fr.add(FR.model().DATA_ORA_FLUSSO.getFieldType());
		it.govpay.orm.IdFr id_fr = null;
		List<Object> listaFieldId_fr = jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet.createSQLQuery(), jdbcProperties.isShowSql(),
				listaFieldIdReturnType_fr, searchParams_fr);
		if(listaFieldId_fr==null || listaFieldId_fr.size()<=0){
			if(throwNotFound){
				throw new NotFoundException("Not Found");
			}
		}
		else{
			id_fr = new it.govpay.orm.IdFr();
			id_fr.setCodFlusso((String)listaFieldId_fr.get(0));
			id_fr.setDataOraFlusso((Date)listaFieldId_fr.get(1));
		}
		
		return id_fr;
		
	}

	@Override
	public Long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdFr id, boolean throwNotFound)
			throws NotFoundException, ServiceException, NotImplementedException, Exception {
	
		return this.findIdFR(jdbcProperties,log,connection,sqlQueryObject,id,throwNotFound);
			
	}
	
	@Override
	public List<List<Object>> nativeQuery(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
											String sql,List<Class<?>> returnClassTypes,Object ... param) throws ServiceException,NotFoundException,NotImplementedException,Exception{
		
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.nativeQuery(jdbcProperties, log, connection, sqlQueryObject,
																							sql,returnClassTypes,param);
														
	}
	
	protected Long findIdFR(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdFr id, boolean throwNotFound) throws NotFoundException, ServiceException, NotImplementedException, Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);

		ISQLQueryObject sqlQueryObjectGet = sqlQueryObject.newSQLQueryObject();

		if((id!=null && id.getId()!=null && id.getId() >0))
			return id.getId();
		
		// Object _fr
		sqlQueryObjectGet.addFromTable(this.getFRFieldConverter().toTable(FR.model()));
		sqlQueryObjectGet.addSelectField("id");
		sqlQueryObjectGet.setANDLogicOperator(true);
//		sqlQueryObjectGet.setSelectDistinct(true);
		sqlQueryObjectGet.addWhereCondition(this.getFRFieldConverter().toColumn(FR.model().COD_FLUSSO,true)+"=?");
		sqlQueryObjectGet.addWhereCondition(this.getFRFieldConverter().toColumn(FR.model().DATA_ORA_FLUSSO,true)+"=?");

		// Recupero _fr
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] searchParams_fr = new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] { 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id.getCodFlusso(),FR.model().COD_FLUSSO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id.getDataOraFlusso(),FR.model().DATA_ORA_FLUSSO.getFieldType()),
		};
		Long id_fr = null;
		try{
			id_fr = (Long) jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet.createSQLQuery(), jdbcProperties.isShowSql(),
						Long.class, searchParams_fr);
		}catch(NotFoundException notFound){
			if(throwNotFound){
				throw new NotFoundException(notFound);
			}
		}
		if(id_fr==null || id_fr<=0){
			if(throwNotFound){
				throw new NotFoundException("Not Found");
			}
		}
		
		return id_fr;
	}
}
