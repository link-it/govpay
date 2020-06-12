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

import it.govpay.orm.IdVistaRiscossione;
import it.govpay.orm.VistaRiscossioni;
import it.govpay.orm.dao.jdbc.converter.VistaRiscossioniFieldConverter;
import it.govpay.orm.dao.jdbc.fetch.VistaRiscossioniFetch;

/**     
 * JDBCVistaRiscossioniServiceSearchImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCVistaRiscossioniServiceSearchImpl implements IJDBCServiceSearchWithId<VistaRiscossioni, IdVistaRiscossione, JDBCServiceManager> {

	private VistaRiscossioniFieldConverter _vistaRiscossioniFieldConverter = null;
	public VistaRiscossioniFieldConverter getVistaRiscossioniFieldConverter() {
		if(this._vistaRiscossioniFieldConverter==null){
			this._vistaRiscossioniFieldConverter = new VistaRiscossioniFieldConverter(this.jdbcServiceManager.getJdbcProperties().getDatabaseType());
		}		
		return this._vistaRiscossioniFieldConverter;
	}
	@Override
	public ISQLFieldConverter getFieldConverter() {
		return this.getVistaRiscossioniFieldConverter();
	}
	
	private VistaRiscossioniFetch vistaRiscossioniFetch = new VistaRiscossioniFetch();
	public VistaRiscossioniFetch getVistaRiscossioniFetch() {
		return this.vistaRiscossioniFetch;
	}
	@Override
	public IJDBCFetch getFetch() {
		return getVistaRiscossioniFetch();
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
	public IdVistaRiscossione convertToId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, VistaRiscossioni vistaRiscossioni) throws NotImplementedException, ServiceException, Exception{
	
		IdVistaRiscossione idVistaRiscossioni = new IdVistaRiscossione();
		
		idVistaRiscossioni.setCodDominio(vistaRiscossioni.getCodDominio());
		idVistaRiscossioni.setIndiceDati(vistaRiscossioni.getIndiceDati());
		idVistaRiscossioni.setIuv(vistaRiscossioni.getIuv());
	
		return idVistaRiscossioni;
	}

	protected IdVistaRiscossione convertObjectIdToId(Object objectId) throws ServiceException{
		if(!(objectId instanceof IdVistaRiscossione)) 
			throw new ServiceException("objectId has wrong type. Expected type: IdVistaRiscossione");
		return (IdVistaRiscossione) objectId;
	}
	
	@Override
	public VistaRiscossioni get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdVistaRiscossione id, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException,Exception {
		Object id_vistaRiscossioni = this.findIdVistaRiscossioni(jdbcProperties, log, connection, sqlQueryObject, id, true);
		return this._get(jdbcProperties, log, connection, sqlQueryObject, id_vistaRiscossioni,idMappingResolutionBehaviour);
		
		
	}
	
	@Override
	public boolean exists(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdVistaRiscossione id) throws MultipleResultException, NotImplementedException, ServiceException,Exception {

		Object id_vistaRiscossioni = this.findIdVistaRiscossioni(jdbcProperties, log, connection, sqlQueryObject, id, false);
		return id_vistaRiscossioni != null;	
		
	}
	
	@Override
	public List<IdVistaRiscossione> findAllIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {

		List<IdVistaRiscossione> list = new ArrayList<IdVistaRiscossione>();

		try{
			List<IField> fields = new ArrayList<>();
			fields.add(VistaRiscossioni.model().COD_DOMINIO);
			fields.add(VistaRiscossioni.model().INDICE_DATI);
			fields.add(VistaRiscossioni.model().IUV);

			List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));

			for(Map<String, Object> map: returnMap) {
				list.add(this.convertToId(jdbcProperties, log, connection, sqlQueryObject, (VistaRiscossioni)this.getVistaRiscossioniFetch().fetch(jdbcProperties.getDatabase(), VistaRiscossioni.model(), map)));
			}
		} catch(NotFoundException e) {}

        return list;
		
	}
	
	@Override
	public List<VistaRiscossioni> findAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {

        List<VistaRiscossioni> list = new ArrayList<VistaRiscossioni>();

        try{
 			List<IField> fields = new ArrayList<>();
 			fields.add(VistaRiscossioni.model().COD_APPLICAZIONE);
 			fields.add(VistaRiscossioni.model().COD_DOMINIO);
 			fields.add(VistaRiscossioni.model().COD_FLUSSO);
 			fields.add(VistaRiscossioni.model().COD_SINGOLO_VERSAMENTO_ENTE);
 			fields.add(VistaRiscossioni.model().COD_VERSAMENTO_ENTE);
 			fields.add(VistaRiscossioni.model().DATA_REGOLAMENTO);
 			fields.add(VistaRiscossioni.model().FR_IUR);
 			fields.add(VistaRiscossioni.model().IMPORTO_PAGATO);
 			fields.add(VistaRiscossioni.model().IMPORTO_TOTALE_PAGAMENTI);
 			fields.add(VistaRiscossioni.model().INDICE_DATI);
 			fields.add(VistaRiscossioni.model().IUR);
 			fields.add(VistaRiscossioni.model().IUV);
 			fields.add(VistaRiscossioni.model().NUMERO_PAGAMENTI);
 			fields.add(VistaRiscossioni.model().DATA_PAGAMENTO);
 			fields.add(VistaRiscossioni.model().COD_TIPO_VERSAMENTO);
 			fields.add(VistaRiscossioni.model().COD_ENTRATA);
 			fields.add(VistaRiscossioni.model().IDENTIFICATIVO_DEBITORE);
 			fields.add(VistaRiscossioni.model().ANNO);
 	
 			List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));
 	
 			for(Map<String, Object> map: returnMap) {
 				VistaRiscossioni riscossione = (VistaRiscossioni)this.getFetch().fetch(jdbcProperties.getDatabase(), VistaRiscossioni.model(), map);
 				list.add(riscossione);
 			}
 		} catch(NotFoundException e) {}

        return list;      
		
	}
	
	@Override
	public VistaRiscossioni find(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) 
		throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException,Exception {

		JDBCPaginatedExpression pagExpr = this.toPaginatedExpression(expression,log);
		
		List<VistaRiscossioni> lst = this.findAll(jdbcProperties, log, connection, sqlQueryObject, pagExpr, idMappingResolutionBehaviour);

		if(lst.size() <=0)
			throw new NotFoundException("Nessuna entry corrisponde ai criteri indicati.");

		if(lst.size() > 1)
			throw new MultipleResultException("I criteri indicati individuano piu' entry.");

		return lst.get(0);
		
	}
	
	@Override
	public NonNegativeNumber count(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws NotImplementedException, ServiceException,Exception {
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareCount(jdbcProperties, log, connection, sqlQueryObject, expression,
												this.getVistaRiscossioniFieldConverter(), VistaRiscossioni.model());
		
		sqlQueryObject.addSelectCountField("tot");
		
		_join(expression,sqlQueryObject);
		
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.count(jdbcProperties, log, connection, sqlQueryObject, expression,
																			this.getVistaRiscossioniFieldConverter(), VistaRiscossioni.model(),listaQuery);
	}

	@Override
	public InUse inUse(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdVistaRiscossione id) throws NotFoundException, NotImplementedException, ServiceException,Exception {
		
		Object id_vistaRiscossioni = this.findIdVistaRiscossioni(jdbcProperties, log, connection, sqlQueryObject, id, true);
        return this._inUse(jdbcProperties, log, connection, sqlQueryObject, id_vistaRiscossioni);
		
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
												this.getVistaRiscossioniFieldConverter(), field);

			return _select(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression, sqlQueryObjectDistinct);
			
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
			List<Map<String,Object>> list = _select(jdbcProperties, log, connection, sqlQueryObject, expression);
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
			return _select(jdbcProperties, log, connection, sqlQueryObject, expression);
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
			return _select(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression);
		}finally{
			org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.removeFields(sqlQueryObject,paginatedExpression,functionField);
		}
	}
	
	protected List<Map<String,Object>> _select(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
												IExpression expression) throws ServiceException,NotFoundException,NotImplementedException,Exception {
		return _select(jdbcProperties, log, connection, sqlQueryObject, expression, null);
	}
	protected List<Map<String,Object>> _select(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
												IExpression expression, ISQLQueryObject sqlQueryObjectDistinct) throws ServiceException,NotFoundException,NotImplementedException,Exception {
		
		List<Object> listaQuery = new ArrayList<Object>();
		List<JDBCObject> listaParams = new ArrayList<JDBCObject>();
		List<Object> returnField = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareSelect(jdbcProperties, log, connection, sqlQueryObject, 
        						expression, this.getVistaRiscossioniFieldConverter(), VistaRiscossioni.model(), 
        						listaQuery,listaParams);
		
		_join(expression,sqlQueryObject);
        
        List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.select(jdbcProperties, log, connection,
        								org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareSqlQueryObjectForSelectDistinct(sqlQueryObject,sqlQueryObjectDistinct), 
        								expression, this.getVistaRiscossioniFieldConverter(), VistaRiscossioni.model(),
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
		
		List<ISQLQueryObject> sqlQueryObjectInnerList = new ArrayList<ISQLQueryObject>();
		List<JDBCObject> jdbcObjects = new ArrayList<JDBCObject>();
		List<Class<?>> returnClassTypes = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareUnion(jdbcProperties, log, connection, sqlQueryObject, 
        						this.getVistaRiscossioniFieldConverter(), VistaRiscossioni.model(), 
        						sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);
		
		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				_join(expression,sqlQueryObjectInnerList.get(i));
			}
		}
        
        List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.union(jdbcProperties, log, connection, sqlQueryObject, 
        								this.getVistaRiscossioniFieldConverter(), VistaRiscossioni.model(), 
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
		
		List<ISQLQueryObject> sqlQueryObjectInnerList = new ArrayList<ISQLQueryObject>();
		List<JDBCObject> jdbcObjects = new ArrayList<JDBCObject>();
		List<Class<?>> returnClassTypes = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareUnionCount(jdbcProperties, log, connection, sqlQueryObject, 
        						this.getVistaRiscossioniFieldConverter(), VistaRiscossioni.model(), 
        						sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);
		
		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				_join(expression,sqlQueryObjectInnerList.get(i));
			}
		}
        
        NonNegativeNumber number = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.unionCount(jdbcProperties, log, connection, sqlQueryObject, 
        								this.getVistaRiscossioniFieldConverter(), VistaRiscossioni.model(), 
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
			return new JDBCExpression(this.getVistaRiscossioniFieldConverter());
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}


	@Override
	public JDBCPaginatedExpression newPaginatedExpression(Logger log) throws NotImplementedException, ServiceException {
		try{
			return new JDBCPaginatedExpression(this.getVistaRiscossioniFieldConverter());
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
	public void mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdVistaRiscossione id, VistaRiscossioni obj) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		_mappingTableIds(jdbcProperties,log,connection,sqlQueryObject,obj,
				this.get(jdbcProperties,log,connection,sqlQueryObject,id,null));
	}
	
	@Override
	public void mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, VistaRiscossioni obj) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		_mappingTableIds(jdbcProperties,log,connection,sqlQueryObject,obj,
				this.get(jdbcProperties,log,connection,sqlQueryObject,tableId,null));
	}
	private void _mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, VistaRiscossioni obj, VistaRiscossioni imgSaved) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		if(imgSaved==null){
			return;
		}
		obj.setId(imgSaved.getId());

	}
	
	@Override
	public VistaRiscossioni get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
		throw new NotImplementedException("Table without long id column PK");
	}
	
	protected VistaRiscossioni _get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Object objectId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
	
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
					new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectGet = sqlQueryObject.newSQLQueryObject();
				
		VistaRiscossioni vistaRiscossioni = new VistaRiscossioni();
		
		IdVistaRiscossione convertObjectIdToId = this.convertObjectIdToId(objectId);

		// Object vistaRiscossioni
		ISQLQueryObject sqlQueryObjectGet_vistaRiscossioni = sqlQueryObjectGet.newSQLQueryObject();
		sqlQueryObjectGet_vistaRiscossioni.setANDLogicOperator(true);
		sqlQueryObjectGet_vistaRiscossioni.addFromTable(this.getVistaRiscossioniFieldConverter().toTable(VistaRiscossioni.model()));
		sqlQueryObjectGet_vistaRiscossioni.addSelectField(this.getVistaRiscossioniFieldConverter().toColumn(VistaRiscossioni.model().COD_DOMINIO,true));
		sqlQueryObjectGet_vistaRiscossioni.addSelectField(this.getVistaRiscossioniFieldConverter().toColumn(VistaRiscossioni.model().IUV,true));
		sqlQueryObjectGet_vistaRiscossioni.addSelectField(this.getVistaRiscossioniFieldConverter().toColumn(VistaRiscossioni.model().IUR,true));
		sqlQueryObjectGet_vistaRiscossioni.addSelectField(this.getVistaRiscossioniFieldConverter().toColumn(VistaRiscossioni.model().COD_FLUSSO,true));
		sqlQueryObjectGet_vistaRiscossioni.addSelectField(this.getVistaRiscossioniFieldConverter().toColumn(VistaRiscossioni.model().FR_IUR,true));
		sqlQueryObjectGet_vistaRiscossioni.addSelectField(this.getVistaRiscossioniFieldConverter().toColumn(VistaRiscossioni.model().DATA_REGOLAMENTO,true));
		sqlQueryObjectGet_vistaRiscossioni.addSelectField(this.getVistaRiscossioniFieldConverter().toColumn(VistaRiscossioni.model().NUMERO_PAGAMENTI,true));
		sqlQueryObjectGet_vistaRiscossioni.addSelectField(this.getVistaRiscossioniFieldConverter().toColumn(VistaRiscossioni.model().IMPORTO_TOTALE_PAGAMENTI,true));
		sqlQueryObjectGet_vistaRiscossioni.addSelectField(this.getVistaRiscossioniFieldConverter().toColumn(VistaRiscossioni.model().IMPORTO_PAGATO,true));
		sqlQueryObjectGet_vistaRiscossioni.addSelectField(this.getVistaRiscossioniFieldConverter().toColumn(VistaRiscossioni.model().COD_SINGOLO_VERSAMENTO_ENTE,true));
		sqlQueryObjectGet_vistaRiscossioni.addSelectField(this.getVistaRiscossioniFieldConverter().toColumn(VistaRiscossioni.model().INDICE_DATI,true));
		sqlQueryObjectGet_vistaRiscossioni.addSelectField(this.getVistaRiscossioniFieldConverter().toColumn(VistaRiscossioni.model().COD_VERSAMENTO_ENTE,true));
		sqlQueryObjectGet_vistaRiscossioni.addSelectField(this.getVistaRiscossioniFieldConverter().toColumn(VistaRiscossioni.model().COD_APPLICAZIONE,true));
		sqlQueryObjectGet_vistaRiscossioni.addSelectField(this.getVistaRiscossioniFieldConverter().toColumn(VistaRiscossioni.model().DATA_PAGAMENTO,true));
		sqlQueryObjectGet_vistaRiscossioni.addSelectField(this.getVistaRiscossioniFieldConverter().toColumn(VistaRiscossioni.model().COD_TIPO_VERSAMENTO,true));
		sqlQueryObjectGet_vistaRiscossioni.addSelectField(this.getVistaRiscossioniFieldConverter().toColumn(VistaRiscossioni.model().COD_ENTRATA,true));
		sqlQueryObjectGet_vistaRiscossioni.addSelectField(this.getVistaRiscossioniFieldConverter().toColumn(VistaRiscossioni.model().IDENTIFICATIVO_DEBITORE,true));
		sqlQueryObjectGet_vistaRiscossioni.addSelectField(this.getVistaRiscossioniFieldConverter().toColumn(VistaRiscossioni.model().ANNO,true));
		
		sqlQueryObjectGet_vistaRiscossioni.addWhereCondition(this.getVistaRiscossioniFieldConverter().toColumn(VistaRiscossioni.model().COD_DOMINIO,true) + "=?");
		sqlQueryObjectGet_vistaRiscossioni.addWhereCondition(this.getVistaRiscossioniFieldConverter().toColumn(VistaRiscossioni.model().IUV,true) + "=?");
		sqlQueryObjectGet_vistaRiscossioni.addWhereCondition(this.getVistaRiscossioniFieldConverter().toColumn(VistaRiscossioni.model().INDICE_DATI,true) + "=?");

		// Get vistaRiscossioni
		vistaRiscossioni = (VistaRiscossioni) jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet_vistaRiscossioni.createSQLQuery(), jdbcProperties.isShowSql(), VistaRiscossioni.model(), this.getVistaRiscossioniFetch(),
			new JDBCObject(convertObjectIdToId.getCodDominio(),convertObjectIdToId.getCodDominio().getClass()),
			new JDBCObject(convertObjectIdToId.getIuv(),convertObjectIdToId.getIuv().getClass()),
			new JDBCObject(convertObjectIdToId.getIndiceDati(),convertObjectIdToId.getIndiceDati().getClass()));

        return vistaRiscossioni;  
	
	} 
	
	@Override
	public boolean exists(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId) throws MultipleResultException, NotImplementedException, ServiceException, Exception {
		throw new NotImplementedException("Table without long id column PK");
	}
	
	protected boolean _exists(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Object objectId) throws MultipleResultException, NotImplementedException, ServiceException, Exception {
	
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
					new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
				
		boolean existsVistaRiscossioni = false;
		
		IdVistaRiscossione convertObjectIdToId = this.convertObjectIdToId(objectId);

		sqlQueryObject = sqlQueryObject.newSQLQueryObject();
		sqlQueryObject.setANDLogicOperator(true);

		sqlQueryObject.addFromTable(this.getVistaRiscossioniFieldConverter().toTable(VistaRiscossioni.model()));
		sqlQueryObject.addSelectField(this.getVistaRiscossioniFieldConverter().toColumn(VistaRiscossioni.model().COD_DOMINIO,true));
		
		sqlQueryObject.addWhereCondition(this.getVistaRiscossioniFieldConverter().toColumn(VistaRiscossioni.model().COD_DOMINIO,true) + "=?");
		sqlQueryObject.addWhereCondition(this.getVistaRiscossioniFieldConverter().toColumn(VistaRiscossioni.model().IUV,true) + "=?");
		sqlQueryObject.addWhereCondition(this.getVistaRiscossioniFieldConverter().toColumn(VistaRiscossioni.model().INDICE_DATI,true) + "=?");

		// Exists vistaRiscossioni
		existsVistaRiscossioni = jdbcUtilities.exists(sqlQueryObject.createSQLQuery(), jdbcProperties.isShowSql(),
				new JDBCObject(convertObjectIdToId.getCodDominio(),convertObjectIdToId.getCodDominio().getClass()),
				new JDBCObject(convertObjectIdToId.getIuv(),convertObjectIdToId.getIuv().getClass()),
				new JDBCObject(convertObjectIdToId.getIndiceDati(),convertObjectIdToId.getIndiceDati().getClass()));

        return existsVistaRiscossioni;
	
	}
	
	private void _join(IExpression expression, ISQLQueryObject sqlQueryObject) throws NotImplementedException, ServiceException, Exception{
        
	}
	
	protected java.util.List<Object> _getRootTablePrimaryKeyValues(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdVistaRiscossione id) throws NotFoundException, ServiceException, NotImplementedException, Exception{
	    // Identificativi
        java.util.List<Object> rootTableIdValues = new java.util.ArrayList<Object>();
		Object objectId = this.findIdVistaRiscossioni(jdbcProperties, log, connection, sqlQueryObject.newSQLQueryObject(), id, true);
		rootTableIdValues.add(objectId);
        return rootTableIdValues;
	}
	
	protected Map<String, List<IField>> _getMapTableToPKColumn() throws NotImplementedException, Exception{
	
		VistaRiscossioniFieldConverter converter = this.getVistaRiscossioniFieldConverter();
		Map<String, List<IField>> mapTableToPKColumn = new java.util.Hashtable<String, List<IField>>();
		UtilsTemplate<IField> utilities = new UtilsTemplate<IField>();

		// VistaRiscossioni.model()
		mapTableToPKColumn.put(converter.toTable(VistaRiscossioni.model()),
			utilities.newList(VistaRiscossioni.model().COD_DOMINIO, VistaRiscossioni.model().IUV, VistaRiscossioni.model().INDICE_DATI));
        
        return mapTableToPKColumn;		
	}
	
	@Override
	public List<Long> findAllTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression paginatedExpression) throws ServiceException, NotImplementedException, Exception {
		
		throw new NotImplementedException("Table without long id column PK");
		
	}
	public List<Object> _findAllObjectIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression paginatedExpression) throws ServiceException, NotImplementedException, Exception {
		

		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);

		sqlQueryObject.addSelectField(this.getVistaRiscossioniFieldConverter().toColumn(VistaRiscossioni.model().COD_DOMINIO,true));
		sqlQueryObject.addSelectField(this.getVistaRiscossioniFieldConverter().toColumn(VistaRiscossioni.model().IUV,true));
		sqlQueryObject.addSelectField(this.getVistaRiscossioniFieldConverter().toColumn(VistaRiscossioni.model().INDICE_DATI,true));
		
		List<Class<?>> objectIdClass = new ArrayList<>();
		objectIdClass.add(VistaRiscossioni.model().COD_DOMINIO.getClassType());
		objectIdClass.add(VistaRiscossioni.model().IUV.getClassType());
		objectIdClass.add(VistaRiscossioni.model().INDICE_DATI.getClassType());
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFindAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
												this.getVistaRiscossioniFieldConverter(), VistaRiscossioni.model());
		
		_join(paginatedExpression,sqlQueryObject);
		
		List<List<Object>> listObjects = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.findAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
																			this.getVistaRiscossioniFieldConverter(), VistaRiscossioni.model(), objectIdClass, listaQuery);
		
		List<Object> ids = new ArrayList<>();
		for (List<Object> id_vistaRiscossioni : listObjects) {
			IdVistaRiscossione idToRet = new IdVistaRiscossione();
			idToRet.setCodDominio((String) id_vistaRiscossioni.get(0));
			idToRet.setIuv((String) id_vistaRiscossioni.get(1));
			idToRet.setIndiceDati((Integer) id_vistaRiscossioni.get(2));
			
			ids.add(idToRet);
		}
		
        return ids;
		
	}
	
	@Override
	public long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws ServiceException, NotFoundException, MultipleResultException, NotImplementedException, Exception {
	
		throw new NotImplementedException("Table without long id column PK");
		
	}
	
	public Object _findObjectId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws ServiceException, NotFoundException, MultipleResultException, NotImplementedException, Exception {
		
		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		
		sqlQueryObject.addSelectField(this.getVistaRiscossioniFieldConverter().toColumn(VistaRiscossioni.model().COD_DOMINIO,true));
		sqlQueryObject.addSelectField(this.getVistaRiscossioniFieldConverter().toColumn(VistaRiscossioni.model().IUV,true));
		sqlQueryObject.addSelectField(this.getVistaRiscossioniFieldConverter().toColumn(VistaRiscossioni.model().INDICE_DATI,true));
		
		List<Class<?>> objectIdClass = new ArrayList<>();
		objectIdClass.add(VistaRiscossioni.model().COD_DOMINIO.getClassType());
		objectIdClass.add(VistaRiscossioni.model().IUV.getClassType());
		objectIdClass.add(VistaRiscossioni.model().INDICE_DATI.getClassType());
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFind(jdbcProperties, log, connection, sqlQueryObject, expression,
												this.getVistaRiscossioniFieldConverter(), VistaRiscossioni.model());
		
		_join(expression,sqlQueryObject);

		List<Object> id_vistaRiscossioni = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.find(jdbcProperties, log, connection, sqlQueryObject, expression,
														this.getVistaRiscossioniFieldConverter(), VistaRiscossioni.model(), objectIdClass, listaQuery);
		if(id_vistaRiscossioni !=null){
			IdVistaRiscossione idToRet = new IdVistaRiscossione();
			idToRet.setCodDominio((String) id_vistaRiscossioni.get(0));
			idToRet.setIuv((String) id_vistaRiscossioni.get(1));
			idToRet.setIndiceDati((Integer) id_vistaRiscossioni.get(2));
			return idToRet;
		}
		else{
			throw new NotFoundException("Not Found");
		}
		
	}

	@Override
	public InUse inUse(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId) throws ServiceException, NotFoundException, NotImplementedException, Exception {
		throw new NotImplementedException("Table without long id column PK");
	}

	protected InUse _inUse(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Object objectId) throws ServiceException, NotFoundException, NotImplementedException, Exception {

		InUse inUse = new InUse();
		inUse.setInUse(false);

        return inUse;

	}
	
	@Override
	public IdVistaRiscossione findId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, boolean throwNotFound)
			throws NotFoundException, ServiceException, NotImplementedException, Exception {
		
		throw new NotImplementedException("Table without long id column PK");
		
	}

	@Override
	public Long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdVistaRiscossione id, boolean throwNotFound)
			throws NotFoundException, ServiceException, NotImplementedException, Exception {
	
		throw new NotImplementedException("Table without long id column PK");
			
	}
	
	@Override
	public List<List<Object>> nativeQuery(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
											String sql,List<Class<?>> returnClassTypes,Object ... param) throws ServiceException,NotFoundException,NotImplementedException,Exception{
		
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.nativeQuery(jdbcProperties, log, connection, sqlQueryObject,
																							sql,returnClassTypes,param);
														
	}
	
	protected Object findIdVistaRiscossioni(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdVistaRiscossione id, boolean throwNotFound) throws NotFoundException, ServiceException, NotImplementedException, Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);

		ISQLQueryObject sqlQueryObjectGet = sqlQueryObject.newSQLQueryObject();

		// Object _vistaRiscossioni
		sqlQueryObjectGet.addFromTable(this.getVistaRiscossioniFieldConverter().toTable(VistaRiscossioni.model()));

		sqlQueryObjectGet.addSelectField(this.getVistaRiscossioniFieldConverter().toColumn(VistaRiscossioni.model().COD_DOMINIO,true));
		sqlQueryObjectGet.addSelectField(this.getVistaRiscossioniFieldConverter().toColumn(VistaRiscossioni.model().IUV,true));
		sqlQueryObjectGet.addSelectField(this.getVistaRiscossioniFieldConverter().toColumn(VistaRiscossioni.model().INDICE_DATI,true));

		// Devono essere mappati nella where condition i metodi dell'oggetto id.getXXX
		sqlQueryObjectGet.setANDLogicOperator(true);
		sqlQueryObjectGet.setSelectDistinct(true);
		sqlQueryObjectGet.addWhereCondition(this.getVistaRiscossioniFieldConverter().toColumn(VistaRiscossioni.model().COD_DOMINIO,true)+"=?");
		sqlQueryObjectGet.addWhereCondition(this.getVistaRiscossioniFieldConverter().toColumn(VistaRiscossioni.model().IUV,true)+"=?");
		sqlQueryObjectGet.addWhereCondition(this.getVistaRiscossioniFieldConverter().toColumn(VistaRiscossioni.model().INDICE_DATI,true)+"=?");

		// Recupero _vistaRiscossioni
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] searchParams_vistaRiscossioni = new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] { 
				new JDBCObject(id.getCodDominio(),id.getCodDominio().getClass()),
				new JDBCObject(id.getIuv(),id.getIuv().getClass()),
				new JDBCObject(id.getIndiceDati(),id.getIndiceDati().getClass()) 
		};
	
		List<Class<?>> id_vistaRiscossioni_classType = new ArrayList<>();
		id_vistaRiscossioni_classType.add(VistaRiscossioni.model().COD_DOMINIO.getClassType());
		id_vistaRiscossioni_classType.add(VistaRiscossioni.model().IUV.getClassType());
		id_vistaRiscossioni_classType.add(VistaRiscossioni.model().INDICE_DATI.getClassType());
		
		List<Object> id_vistaRiscossioni = jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet.createSQLQuery(), jdbcProperties.isShowSql(), id_vistaRiscossioni_classType, searchParams_vistaRiscossioni);
		
		if(id_vistaRiscossioni==null){
			if(throwNotFound){
				throw new NotFoundException("Not Found");
			}
		}
		
		IdVistaRiscossione idToRet = new IdVistaRiscossione();
		idToRet.setCodDominio((String) id_vistaRiscossioni.get(0));
		idToRet.setIuv((String) id_vistaRiscossioni.get(1));
		idToRet.setIndiceDati((Integer) id_vistaRiscossioni.get(2));
		
		return idToRet;
	}
}
