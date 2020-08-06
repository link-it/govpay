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

import it.govpay.orm.IbanAccredito;
import it.govpay.orm.IdDominio;
import it.govpay.orm.IdIbanAccredito;
import it.govpay.orm.dao.IDBDominioServiceSearch;
import it.govpay.orm.dao.jdbc.converter.IbanAccreditoFieldConverter;
import it.govpay.orm.dao.jdbc.fetch.IbanAccreditoFetch;

/**     
 * JDBCIbanAccreditoServiceSearchImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCIbanAccreditoServiceSearchImpl implements IJDBCServiceSearchWithId<IbanAccredito, IdIbanAccredito, JDBCServiceManager> {

	private IbanAccreditoFieldConverter _ibanAccreditoFieldConverter = null;
	public IbanAccreditoFieldConverter getIbanAccreditoFieldConverter() {
		if(this._ibanAccreditoFieldConverter==null){
			this._ibanAccreditoFieldConverter = new IbanAccreditoFieldConverter(this.jdbcServiceManager.getJdbcProperties().getDatabaseType());
		}		
		return this._ibanAccreditoFieldConverter;
	}
	@Override
	public ISQLFieldConverter getFieldConverter() {
		return this.getIbanAccreditoFieldConverter();
	}
	
	private IbanAccreditoFetch ibanAccreditoFetch = new IbanAccreditoFetch();
	public IbanAccreditoFetch getIbanAccreditoFetch() {
		return this.ibanAccreditoFetch;
	}
	@Override
	public IJDBCFetch getFetch() {
		return this.getIbanAccreditoFetch();
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
	public IdIbanAccredito convertToId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IbanAccredito ibanAccredito) throws NotImplementedException, ServiceException, Exception{
	
		IdIbanAccredito idIbanAccredito = new IdIbanAccredito();
		idIbanAccredito.setCodIban(ibanAccredito.getCodIban());
		idIbanAccredito.setIdDominio(ibanAccredito.getIdDominio());
	
		return idIbanAccredito;
	}
	
	@Override
	public IbanAccredito get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdIbanAccredito id, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException,Exception {
		Long id_ibanAccredito = ( (id!=null && id.getId()!=null && id.getId()>0) ? id.getId() : this.findIdIbanAccredito(jdbcProperties, log, connection, sqlQueryObject, id, true));
		return this._get(jdbcProperties, log, connection, sqlQueryObject, id_ibanAccredito,idMappingResolutionBehaviour);
		
		
	}
	
	@Override
	public boolean exists(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdIbanAccredito id) throws MultipleResultException, NotImplementedException, ServiceException,Exception {

		Long id_ibanAccredito = this.findIdIbanAccredito(jdbcProperties, log, connection, sqlQueryObject, id, false);
		return id_ibanAccredito != null && id_ibanAccredito > 0;
		
	}
	
	@Override
	public List<IdIbanAccredito> findAllIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {

        // default behaviour (id-mapping)
        if(idMappingResolutionBehaviour==null){
                idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
        }
		List<IdIbanAccredito> list = new ArrayList<>();
		try{
			List<IField> fields = new ArrayList<>();

			fields.add(IbanAccredito.model().COD_IBAN);
			fields.add(new CustomField("id_dominio", Long.class, "id_dominio", this.getFieldConverter().toTable(IbanAccredito.model())));
			

			List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));
        
			for(Map<String, Object> map: returnMap) {
				
				IdIbanAccredito idIbanAccredito = new IdIbanAccredito();
				idIbanAccredito.setCodIban((String) map.remove(IbanAccredito.model().COD_IBAN.getFieldName()));
				
				
				Long idDominio = (Long) map.remove("id_dominio");

				if(idMappingResolutionBehaviour==null ||
						(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
						){
					it.govpay.orm.IdDominio id_ibanAccredito_dominio = null;
					if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
						id_ibanAccredito_dominio = ((JDBCDominioServiceSearch)(this.getServiceManager().getDominioServiceSearch())).findId(idDominio, false);
					}else{
						id_ibanAccredito_dominio = new it.govpay.orm.IdDominio();
					}
					id_ibanAccredito_dominio.setId(idDominio);
					idIbanAccredito.setIdDominio(id_ibanAccredito_dominio);
				}

				
				list.add(this.convertToId(jdbcProperties, log, connection, sqlQueryObject, (IbanAccredito)this.getIbanAccreditoFetch().fetch(jdbcProperties.getDatabase(), IbanAccredito.model(), map)));
        }
		} catch(NotFoundException e) {}

        return list;
		
	}
	
	@Override
	public List<IbanAccredito> findAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {

        // default behaviour (id-mapping)
        if(idMappingResolutionBehaviour==null){
                idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
        }
        List<IbanAccredito> list = new ArrayList<>();
        
		try{
			List<IField> fields = new ArrayList<>();

			fields.add(new CustomField("id", Long.class, "id", this.getFieldConverter().toTable(IbanAccredito.model())));
			fields.add(IbanAccredito.model().COD_IBAN);
			fields.add(IbanAccredito.model().POSTALE);
			fields.add(IbanAccredito.model().BIC_ACCREDITO);
			fields.add(IbanAccredito.model().DESCRIZIONE);
			fields.add(IbanAccredito.model().ABILITATO);
			fields.add(IbanAccredito.model().INTESTATARIO);

			fields.add(new CustomField("id_dominio", Long.class, "id_dominio", this.getFieldConverter().toTable(IbanAccredito.model())));

			List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));

			for(Map<String, Object> map: returnMap) {
				Long idDominio = (Long) map.remove("id_dominio");
				
				IbanAccredito ibanAccredito = (IbanAccredito)this.getIbanAccreditoFetch().fetch(jdbcProperties.getDatabase(), IbanAccredito.model(), map);
				if(idMappingResolutionBehaviour==null ||
						(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
					){
						it.govpay.orm.IdDominio id_ibanAccredito_dominio = null;
						if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
							id_ibanAccredito_dominio = ((JDBCDominioServiceSearch)(this.getServiceManager().getDominioServiceSearch())).findId(idDominio, false);
						}else{
							id_ibanAccredito_dominio = new it.govpay.orm.IdDominio();
        }
						id_ibanAccredito_dominio.setId(idDominio);
						ibanAccredito.setIdDominio(id_ibanAccredito_dominio);
					}

				list.add(ibanAccredito);
			}
		} catch(NotFoundException e) {}

        return list;      
		
	}
	
	@Override
	public IbanAccredito find(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) 
		throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException,Exception {

		JDBCPaginatedExpression pagExpr = this.toPaginatedExpression(expression,log);
		
		List<IbanAccredito> lst = this.findAll(jdbcProperties, log, connection, sqlQueryObject, pagExpr, idMappingResolutionBehaviour);

		if(lst.size() <=0)
			throw new NotFoundException("Nessuna entry corrisponde ai criteri indicati.");

		if(lst.size() > 1)
			throw new MultipleResultException("I criteri indicati individuano piu' entry.");

		return lst.get(0);
		
	}
	
	@Override
	public NonNegativeNumber count(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws NotImplementedException, ServiceException,Exception {
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareCount(jdbcProperties, log, connection, sqlQueryObject, expression,
												this.getIbanAccreditoFieldConverter(), IbanAccredito.model());
		
		sqlQueryObject.addSelectCountField(this.getIbanAccreditoFieldConverter().toTable(IbanAccredito.model())+".id","tot");
		
		this._join(expression,sqlQueryObject);
		
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.count(jdbcProperties, log, connection, sqlQueryObject, expression,
																			this.getIbanAccreditoFieldConverter(), IbanAccredito.model(),listaQuery);
	}

	@Override
	public InUse inUse(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdIbanAccredito id) throws NotFoundException, NotImplementedException, ServiceException,Exception {
		
		Long id_ibanAccredito = this.findIdIbanAccredito(jdbcProperties, log, connection, sqlQueryObject, id, true);
        return this._inUse(jdbcProperties, log, connection, sqlQueryObject, id_ibanAccredito);
		
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
												this.getIbanAccreditoFieldConverter(), field);

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
        						expression, this.getIbanAccreditoFieldConverter(), IbanAccredito.model(), 
        						listaQuery,listaParams);
		
		this._join(expression,sqlQueryObject);
        
        List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.select(jdbcProperties, log, connection,
        								org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareSqlQueryObjectForSelectDistinct(sqlQueryObject,sqlQueryObjectDistinct), 
        								expression, this.getIbanAccreditoFieldConverter(), IbanAccredito.model(),
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
        						this.getIbanAccreditoFieldConverter(), IbanAccredito.model(), 
        						sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);
		
		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				this._join(expression,sqlQueryObjectInnerList.get(i));
			}
		}
        
        List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.union(jdbcProperties, log, connection, sqlQueryObject, 
        								this.getIbanAccreditoFieldConverter(), IbanAccredito.model(), 
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
        						this.getIbanAccreditoFieldConverter(), IbanAccredito.model(), 
        						sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);
		
		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				this._join(expression,sqlQueryObjectInnerList.get(i));
			}
		}
        
        NonNegativeNumber number = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.unionCount(jdbcProperties, log, connection, sqlQueryObject, 
        								this.getIbanAccreditoFieldConverter(), IbanAccredito.model(), 
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
			return new JDBCExpression(this.getIbanAccreditoFieldConverter());
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}


	@Override
	public JDBCPaginatedExpression newPaginatedExpression(Logger log) throws NotImplementedException, ServiceException {
		try{
			return new JDBCPaginatedExpression(this.getIbanAccreditoFieldConverter());
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
	public void mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdIbanAccredito id, IbanAccredito obj) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		this._mappingTableIds(jdbcProperties,log,connection,sqlQueryObject,obj,
				this.get(jdbcProperties,log,connection,sqlQueryObject,id,null));
	}
	
	@Override
	public void mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IbanAccredito obj) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		this._mappingTableIds(jdbcProperties,log,connection,sqlQueryObject,obj,
				this.get(jdbcProperties,log,connection,sqlQueryObject,tableId,null));
	}
	private void _mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IbanAccredito obj, IbanAccredito imgSaved) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		if(imgSaved==null){
			return;
		}
		obj.setId(imgSaved.getId());
		if(obj.getIdDominio()!=null && 
				imgSaved.getIdDominio()!=null){
			obj.getIdDominio().setId(imgSaved.getIdDominio().getId());
		}

	}
	
	@Override
	public IbanAccredito get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
		return this._get(jdbcProperties, log, connection, sqlQueryObject, Long.valueOf(tableId), idMappingResolutionBehaviour);
	}
	
	private IbanAccredito _get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
	
		IField idField = new CustomField("id", Long.class, "id", this.getFieldConverter().toTable(this.getFieldConverter().getRootModel()));
		JDBCPaginatedExpression expression = this.newPaginatedExpression(log);
		
		expression.equals(idField, tableId);
		List<IbanAccredito> lst = this.findAll(jdbcProperties, log, connection, sqlQueryObject.newSQLQueryObject(), expression, idMappingResolutionBehaviour);
		
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
				
		boolean existsIbanAccredito = false;

		sqlQueryObject = sqlQueryObject.newSQLQueryObject();
		sqlQueryObject.setANDLogicOperator(true);

		sqlQueryObject.addFromTable(this.getIbanAccreditoFieldConverter().toTable(IbanAccredito.model()));
		sqlQueryObject.addSelectField(this.getIbanAccreditoFieldConverter().toColumn(IbanAccredito.model().COD_IBAN,true));
		sqlQueryObject.addWhereCondition("id=?");


		// Exists ibanAccredito
		existsIbanAccredito = jdbcUtilities.exists(sqlQueryObject.createSQLQuery(), jdbcProperties.isShowSql(),
			new JDBCObject(tableId,Long.class));

		
        return existsIbanAccredito;
	
	}
	
	private void _join(IExpression expression, ISQLQueryObject sqlQueryObject) throws NotImplementedException, ServiceException, Exception{

		if(expression.inUseModel(IbanAccredito.model().ID_DOMINIO,false)){
			String tableName1 = this.getIbanAccreditoFieldConverter().toAliasTable(IbanAccredito.model());
			String tableName2 = this.getIbanAccreditoFieldConverter().toAliasTable(IbanAccredito.model().ID_DOMINIO);
			sqlQueryObject.addWhereCondition(tableName1+".id_dominio="+tableName2+".id");
		}
        
	}
	
	protected java.util.List<Object> _getRootTablePrimaryKeyValues(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdIbanAccredito id) throws NotFoundException, ServiceException, NotImplementedException, Exception{
	    // Identificativi
        java.util.List<Object> rootTableIdValues = new java.util.ArrayList<>();
		Long longId = this.findIdIbanAccredito(jdbcProperties, log, connection, sqlQueryObject.newSQLQueryObject(), id, true);
		rootTableIdValues.add(longId);
        
        return rootTableIdValues;
	}
	
	protected Map<String, List<IField>> _getMapTableToPKColumn() throws NotImplementedException, Exception{
	
		IbanAccreditoFieldConverter converter = this.getIbanAccreditoFieldConverter();
		Map<String, List<IField>> mapTableToPKColumn = new java.util.Hashtable<>();
		UtilsTemplate<IField> utilities = new UtilsTemplate<>();

		//		  If a table doesn't have a primary key, don't add it to this map

		// IbanAccredito.model()
		mapTableToPKColumn.put(converter.toTable(IbanAccredito.model()),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(IbanAccredito.model()))
			));

		// IbanAccredito.model().ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(IbanAccredito.model().ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(IbanAccredito.model().ID_DOMINIO))
			));

        
        return mapTableToPKColumn;		
	}
	
	@Override
	public List<Long> findAllTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression paginatedExpression) throws ServiceException, NotImplementedException, Exception {
		
		List<Long> list = new ArrayList<>();

		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getIbanAccreditoFieldConverter().toTable(IbanAccredito.model())+".id");
		Class<?> objectIdClass = Long.class;
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFindAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
												this.getIbanAccreditoFieldConverter(), IbanAccredito.model());
		
		this._join(paginatedExpression,sqlQueryObject);
		
		List<Object> listObjects = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.findAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
																			this.getIbanAccreditoFieldConverter(), IbanAccredito.model(), objectIdClass, listaQuery);
		for(Object object: listObjects) {
			list.add((Long)object);
		}

        return list;
		
	}
	
	@Override
	public long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws ServiceException, NotFoundException, MultipleResultException, NotImplementedException, Exception {
	
		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getIbanAccreditoFieldConverter().toTable(IbanAccredito.model())+".id");
		Class<?> objectIdClass = Long.class;
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFind(jdbcProperties, log, connection, sqlQueryObject, expression,
												this.getIbanAccreditoFieldConverter(), IbanAccredito.model());
		
		this._join(expression,sqlQueryObject);

		Object res = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.find(jdbcProperties, log, connection, sqlQueryObject, expression,
														this.getIbanAccreditoFieldConverter(), IbanAccredito.model(), objectIdClass, listaQuery);
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
	public IdIbanAccredito findId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, boolean throwNotFound)
			throws NotFoundException, ServiceException, NotImplementedException, Exception {
		
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);

		ISQLQueryObject sqlQueryObjectGet = sqlQueryObject.newSQLQueryObject();


		// Object _ibanAccredito
		sqlQueryObjectGet.addFromTable(this.getIbanAccreditoFieldConverter().toTable(IbanAccredito.model()));
		sqlQueryObjectGet.addSelectField(this.getIbanAccreditoFieldConverter().toColumn(IbanAccredito.model().COD_IBAN,true));
		sqlQueryObjectGet.addSelectField("id_dominio");
		sqlQueryObjectGet.setANDLogicOperator(true);
		sqlQueryObjectGet.addWhereCondition("id=?");

		// Recupero _ibanAccredito
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] searchParams_ibanAccredito = new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] { 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tableId,Long.class)
		};
		List<Class<?>> listaFieldIdReturnType_ibanAccredito = new ArrayList<>();
		listaFieldIdReturnType_ibanAccredito.add(IbanAccredito.model().COD_IBAN.getFieldType());
		listaFieldIdReturnType_ibanAccredito.add(Long.class);
		
		it.govpay.orm.IdIbanAccredito id_ibanAccredito = null;
		List<Object> listaFieldId_ibanAccredito = jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet.createSQLQuery(), jdbcProperties.isShowSql(),
				listaFieldIdReturnType_ibanAccredito, searchParams_ibanAccredito);
		if(listaFieldId_ibanAccredito==null || listaFieldId_ibanAccredito.size()<=0){
			if(throwNotFound){
				throw new NotFoundException("Not Found");
			}
		}
		else{
			id_ibanAccredito = new it.govpay.orm.IdIbanAccredito();
			id_ibanAccredito.setCodIban((String)listaFieldId_ibanAccredito.get(0));
			IdDominio idDominio = new IdDominio();
			idDominio.setId((Long)listaFieldId_ibanAccredito.get(1));
			id_ibanAccredito.setIdDominio(idDominio);
		}
		
		return id_ibanAccredito;
		
	}

	@Override
	public Long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdIbanAccredito id, boolean throwNotFound)
			throws NotFoundException, ServiceException, NotImplementedException, Exception {
	
		return this.findIdIbanAccredito(jdbcProperties,log,connection,sqlQueryObject,id,throwNotFound);
			
	}
	
	@Override
	public List<List<Object>> nativeQuery(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
											String sql,List<Class<?>> returnClassTypes,Object ... param) throws ServiceException,NotFoundException,NotImplementedException,Exception{
		
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.nativeQuery(jdbcProperties, log, connection, sqlQueryObject,
																							sql,returnClassTypes,param);
														
	}
	
	protected Long findIdIbanAccredito(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdIbanAccredito id, boolean throwNotFound) throws NotFoundException, ServiceException, NotImplementedException, Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);

		ISQLQueryObject sqlQueryObjectGet = sqlQueryObject.newSQLQueryObject();

		if(id.getId() != null && id.getId().longValue() > 0)
			return id.getId(); 

		Long idDominio = null;
		
		if(id.getIdDominio().getId() != null && id.getIdDominio().getId().longValue() > 0) {
			idDominio = id.getIdDominio().getId();
		} else {
			idDominio = ((IDBDominioServiceSearch)this.getServiceManager().getDominioServiceSearch()).findTableId(id.getIdDominio(), throwNotFound);
		}
		
		// Object _ibanAccredito
		sqlQueryObjectGet.addFromTable(this.getIbanAccreditoFieldConverter().toTable(IbanAccredito.model()));
		sqlQueryObjectGet.addSelectField("id");
		// Devono essere mappati nella where condition i metodi dell'oggetto id.getXXX
		sqlQueryObjectGet.setANDLogicOperator(true);
//		sqlQueryObjectGet.setSelectDistinct(true);
		sqlQueryObjectGet.addWhereCondition(this.getIbanAccreditoFieldConverter().toColumn(IbanAccredito.model().COD_IBAN,true)+"=?");
		sqlQueryObjectGet.addWhereCondition("id_dominio=?");

		// Recupero _ibanAccredito
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] searchParams_ibanAccredito = new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] { 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id.getCodIban(),IbanAccredito.model().COD_IBAN.getFieldType()),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(idDominio,Long.class)
		};
		Long id_ibanAccredito = null;
		try{
			id_ibanAccredito = (Long) jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet.createSQLQuery(), jdbcProperties.isShowSql(),
						Long.class, searchParams_ibanAccredito);
		}catch(NotFoundException notFound){
			if(throwNotFound){
				throw new NotFoundException(notFound);
			}
		}
		if(id_ibanAccredito==null || id_ibanAccredito<=0){
			if(throwNotFound){
				throw new NotFoundException("Not Found");
			}
		}
		
		return id_ibanAccredito;
	}
}
