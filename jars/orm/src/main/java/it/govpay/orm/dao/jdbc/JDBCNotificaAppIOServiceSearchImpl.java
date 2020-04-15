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

import it.govpay.orm.IdNotifica;
import it.govpay.orm.NotificaAppIO;
import it.govpay.orm.dao.jdbc.converter.NotificaAppIOFieldConverter;
import it.govpay.orm.dao.jdbc.fetch.NotificaAppIOFetch;

/**     
 * JDBCNotificaAppIOServiceSearchImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCNotificaAppIOServiceSearchImpl implements IJDBCServiceSearchWithId<NotificaAppIO, IdNotifica, JDBCServiceManager> {

	private NotificaAppIOFieldConverter _notificaAppIOFieldConverter = null;
	public NotificaAppIOFieldConverter getNotificaAppIOFieldConverter() {
		if(this._notificaAppIOFieldConverter==null){
			this._notificaAppIOFieldConverter = new NotificaAppIOFieldConverter(this.jdbcServiceManager.getJdbcProperties().getDatabaseType());
		}		
		return this._notificaAppIOFieldConverter;
	}
	@Override
	public ISQLFieldConverter getFieldConverter() {
		return this.getNotificaAppIOFieldConverter();
	}
	
	private NotificaAppIOFetch notificaAppIOFetch = new NotificaAppIOFetch();
	public NotificaAppIOFetch getNotificaAppIOFetch() {
		return this.notificaAppIOFetch;
	}
	@Override
	public IJDBCFetch getFetch() {
		return getNotificaAppIOFetch();
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
	public IdNotifica convertToId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, NotificaAppIO notificaAppIO) throws NotImplementedException, ServiceException, Exception{
	
		IdNotifica idNotificaAppIO = new IdNotifica();
		idNotificaAppIO.setIdNotifica(notificaAppIO.getId());
		return idNotificaAppIO;
	}
	
	@Override
	public NotificaAppIO get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdNotifica id, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException,Exception {
		Long id_notificaAppIO = ( (id!=null && id.getId()!=null && id.getId()>0) ? id.getId() : this.findIdNotificaAppIO(jdbcProperties, log, connection, sqlQueryObject, id, true));
		return this._get(jdbcProperties, log, connection, sqlQueryObject, id_notificaAppIO,idMappingResolutionBehaviour);
		
		
	}
	
	@Override
	public boolean exists(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdNotifica id) throws MultipleResultException, NotImplementedException, ServiceException,Exception {

		Long id_notificaAppIO = this.findIdNotificaAppIO(jdbcProperties, log, connection, sqlQueryObject, id, false);
		return id_notificaAppIO != null && id_notificaAppIO > 0;
		
	}
	
	@Override
	public List<IdNotifica> findAllIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {

        // default behaviour (id-mapping)
        if(idMappingResolutionBehaviour==null){
                idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
        }
		List<IdNotifica> list = new ArrayList<>();

		try{
			List<IField> fields = new ArrayList<>();
			fields.add(new CustomField("id", Long.class, "id", this.getNotificaAppIOFieldConverter().toTable(NotificaAppIO.model())));

			List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));
        
			for(Map<String, Object> map: returnMap) {
				list.add(this.convertToId(jdbcProperties, log, connection, sqlQueryObject, (NotificaAppIO)this.getNotificaAppIOFetch().fetch(jdbcProperties.getDatabase(), NotificaAppIO.model(), map)));
	        }
		} catch(NotFoundException e) {}

        return list;
		
	}
	
	@Override
	public List<NotificaAppIO> findAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {

        List<NotificaAppIO> list = new ArrayList<NotificaAppIO>();
        
     // default behaviour (id-mapping)
        if(idMappingResolutionBehaviour==null){
                idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
        }

		try{
			List<IField> fields = new ArrayList<>();
			fields.add(new CustomField("id", Long.class, "id", this.getNotificaAppIOFieldConverter().toTable(NotificaAppIO.model())));
			fields.add(new CustomField("id_versamento", Long.class, "id_versamento", this.getNotificaAppIOFieldConverter().toTable(NotificaAppIO.model())));
			fields.add(new CustomField("id_tipo_versamento_dominio", Long.class, "id_tipo_versamento_dominio", this.getNotificaAppIOFieldConverter().toTable(NotificaAppIO.model())));
			fields.add(NotificaAppIO.model().TIPO_ESITO);
			fields.add(NotificaAppIO.model().DATA_CREAZIONE);
			fields.add(NotificaAppIO.model().STATO);
			fields.add(NotificaAppIO.model().DESCRIZIONE_STATO);
			fields.add(NotificaAppIO.model().DATA_AGGIORNAMENTO_STATO);
			fields.add(NotificaAppIO.model().TENTATIVI_SPEDIZIONE);
			fields.add(NotificaAppIO.model().DATA_PROSSIMA_SPEDIZIONE);
			fields.add(NotificaAppIO.model().DEBITORE_IDENTIFICATIVO);
			fields.add(NotificaAppIO.model().COD_VERSAMENTO_ENTE);
			fields.add(NotificaAppIO.model().COD_APPLICAZIONE);
			fields.add(NotificaAppIO.model().COD_DOMINIO);
			fields.add(NotificaAppIO.model().IUV);
			fields.add(NotificaAppIO.model().ID_MESSAGGIO);
			fields.add(NotificaAppIO.model().STATO_MESSAGGIO);
			
			List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));
        
			for(Map<String, Object> map: returnMap) {
				Long id_versamento = (Long) map.remove("id_versamento");
				Long id_tipoVersamentoDominio = (Long)  map.remove("id_tipo_versamento_dominio");
				
				NotificaAppIO notifica = (NotificaAppIO)this.getNotificaAppIOFetch().fetch(jdbcProperties.getDatabase(), NotificaAppIO.model(), map);
				
				if(idMappingResolutionBehaviour==null ||
						(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
					){
					it.govpay.orm.IdVersamento id_notificaAppIo_versamento = null;
					if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
						id_notificaAppIo_versamento = ((JDBCVersamentoServiceSearch)(this.getServiceManager().getVersamentoServiceSearch())).findId(id_versamento, false);
					}else{
						id_notificaAppIo_versamento = new it.govpay.orm.IdVersamento();
					}
					id_notificaAppIo_versamento.setId(id_versamento);
					notifica.setIdVersamento(id_notificaAppIo_versamento);
					
					it.govpay.orm.IdTipoVersamentoDominio id_notificaAppIo_tipoVersamentoDominio = null;
					if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
						id_notificaAppIo_tipoVersamentoDominio = ((JDBCTipoVersamentoDominioServiceSearch)(this.getServiceManager().getTipoVersamentoDominioServiceSearch())).findId(id_tipoVersamentoDominio, false);
					}else{
						id_notificaAppIo_tipoVersamentoDominio = new it.govpay.orm.IdTipoVersamentoDominio();
					}
					id_notificaAppIo_tipoVersamentoDominio.setId(id_tipoVersamentoDominio);
					notifica.setIdTipoVersamentoDominio(id_notificaAppIo_tipoVersamentoDominio);
					}


				list.add(notifica);
	        }
		} catch(NotFoundException e) {}

        return list;      
		
	}
	
	@Override
	public NotificaAppIO find(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) 
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
												this.getNotificaAppIOFieldConverter(), NotificaAppIO.model());
		
		sqlQueryObject.addSelectCountField(this.getNotificaAppIOFieldConverter().toTable(NotificaAppIO.model())+".id","tot");
		
		_join(expression,sqlQueryObject);
		
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.count(jdbcProperties, log, connection, sqlQueryObject, expression,
																			this.getNotificaAppIOFieldConverter(), NotificaAppIO.model(),listaQuery);
	}

	@Override
	public InUse inUse(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdNotifica id) throws NotFoundException, NotImplementedException, ServiceException,Exception {
		
		Long id_notificaAppIO = this.findIdNotificaAppIO(jdbcProperties, log, connection, sqlQueryObject, id, true);
        return this._inUse(jdbcProperties, log, connection, sqlQueryObject, id_notificaAppIO);
		
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
												this.getNotificaAppIOFieldConverter(), field);

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
        						expression, this.getNotificaAppIOFieldConverter(), NotificaAppIO.model(), 
        						listaQuery,listaParams);
		
		_join(expression,sqlQueryObject);
        
        List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.select(jdbcProperties, log, connection,
        								org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareSqlQueryObjectForSelectDistinct(sqlQueryObject,sqlQueryObjectDistinct), 
        								expression, this.getNotificaAppIOFieldConverter(), NotificaAppIO.model(),
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
        						this.getNotificaAppIOFieldConverter(), NotificaAppIO.model(), 
        						sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);
		
		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				_join(expression,sqlQueryObjectInnerList.get(i));
			}
		}
        
        List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.union(jdbcProperties, log, connection, sqlQueryObject, 
        								this.getNotificaAppIOFieldConverter(), NotificaAppIO.model(), 
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
        						this.getNotificaAppIOFieldConverter(), NotificaAppIO.model(), 
        						sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);
		
		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				_join(expression,sqlQueryObjectInnerList.get(i));
			}
		}
        
        NonNegativeNumber number = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.unionCount(jdbcProperties, log, connection, sqlQueryObject, 
        								this.getNotificaAppIOFieldConverter(), NotificaAppIO.model(), 
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
			return new JDBCExpression(this.getNotificaAppIOFieldConverter());
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}


	@Override
	public JDBCPaginatedExpression newPaginatedExpression(Logger log) throws NotImplementedException, ServiceException {
		try{
			return new JDBCPaginatedExpression(this.getNotificaAppIOFieldConverter());
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
	public void mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdNotifica id, NotificaAppIO obj) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		_mappingTableIds(jdbcProperties,log,connection,sqlQueryObject,obj,
				this.get(jdbcProperties,log,connection,sqlQueryObject,id,null));
	}
	
	@Override
	public void mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, NotificaAppIO obj) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		_mappingTableIds(jdbcProperties,log,connection,sqlQueryObject,obj,
				this.get(jdbcProperties,log,connection,sqlQueryObject,tableId,null));
	}
	private void _mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, NotificaAppIO obj, NotificaAppIO imgSaved) throws NotFoundException,NotImplementedException,ServiceException,Exception{
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
		if(obj.getIdTipoVersamentoDominio()!=null && 
				imgSaved.getIdTipoVersamentoDominio()!=null){
			obj.getIdTipoVersamentoDominio().setId(imgSaved.getIdTipoVersamentoDominio().getId());
			if(obj.getIdTipoVersamentoDominio().getIdDominio()!=null && 
					imgSaved.getIdTipoVersamentoDominio().getIdDominio()!=null){
				obj.getIdTipoVersamentoDominio().getIdDominio().setId(imgSaved.getIdTipoVersamentoDominio().getIdDominio().getId());
			}
			if(obj.getIdTipoVersamentoDominio().getIdTipoVersamento()!=null && 
					imgSaved.getIdTipoVersamentoDominio().getIdTipoVersamento()!=null){
				obj.getIdTipoVersamentoDominio().getIdTipoVersamento().setId(imgSaved.getIdTipoVersamentoDominio().getIdTipoVersamento().getId());
			}
		}

	}
	
	@Override
	public NotificaAppIO get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
		return this._get(jdbcProperties, log, connection, sqlQueryObject, Long.valueOf(tableId), idMappingResolutionBehaviour);
	}
	
	private NotificaAppIO _get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
	
		IField idField = new CustomField("id", Long.class, "id", this.getNotificaAppIOFieldConverter().toTable(NotificaAppIO.model()));
		JDBCPaginatedExpression expression = this.newPaginatedExpression(log);
		
		expression.equals(idField, tableId);
		List<NotificaAppIO> lst = this.findAll(jdbcProperties, log, connection, sqlQueryObject.newSQLQueryObject(), expression, idMappingResolutionBehaviour);
		
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
				
		boolean existsNotificaAppIO = false;

		sqlQueryObject = sqlQueryObject.newSQLQueryObject();
		sqlQueryObject.setANDLogicOperator(true);

		sqlQueryObject.addFromTable(this.getNotificaAppIOFieldConverter().toTable(NotificaAppIO.model()));
		sqlQueryObject.addSelectField(this.getNotificaAppIOFieldConverter().toColumn(NotificaAppIO.model().DEBITORE_IDENTIFICATIVO,true));
		sqlQueryObject.addWhereCondition("id=?");


		// Exists notificaAppIO
		existsNotificaAppIO = jdbcUtilities.exists(sqlQueryObject.createSQLQuery(), jdbcProperties.isShowSql(),
			new JDBCObject(tableId,Long.class));

		
        return existsNotificaAppIO;
	
	}
	
	private void _join(IExpression expression, ISQLQueryObject sqlQueryObject) throws NotImplementedException, ServiceException, Exception{
	
		if(expression.inUseModel(NotificaAppIO.model().ID_VERSAMENTO,false)){
			String tableName1 = this.getNotificaAppIOFieldConverter().toAliasTable(NotificaAppIO.model());
			String tableName2 = this.getNotificaAppIOFieldConverter().toAliasTable(NotificaAppIO.model().ID_VERSAMENTO);
			sqlQueryObject.addWhereCondition(tableName1+".id_versamento="+tableName2+".id");
		}
		
		if(expression.inUseModel(NotificaAppIO.model().ID_VERSAMENTO.ID_APPLICAZIONE,false)){
			
			if(!expression.inUseModel(NotificaAppIO.model().ID_VERSAMENTO,false)){
				String tableName1 = this.getNotificaAppIOFieldConverter().toAliasTable(NotificaAppIO.model());
				String tableName2 = this.getNotificaAppIOFieldConverter().toAliasTable(NotificaAppIO.model().ID_VERSAMENTO);
				sqlQueryObject.addFromTable(tableName2);
				sqlQueryObject.addWhereCondition(tableName1+".id_versamento="+tableName2+".id");
			}
			
			String tableName1 = this.getNotificaAppIOFieldConverter().toAliasTable(NotificaAppIO.model().ID_VERSAMENTO);
			String tableName2 = this.getNotificaAppIOFieldConverter().toAliasTable(NotificaAppIO.model().ID_VERSAMENTO.ID_APPLICAZIONE);
			sqlQueryObject.addWhereCondition(tableName1+".id_applicazione="+tableName2+".id");
		}
		
		if(expression.inUseModel(NotificaAppIO.model().ID_TIPO_VERSAMENTO_DOMINIO,false)){
			String tableName1 = this.getNotificaAppIOFieldConverter().toAliasTable(NotificaAppIO.model());
			String tableName2 = this.getNotificaAppIOFieldConverter().toAliasTable(NotificaAppIO.model().ID_TIPO_VERSAMENTO_DOMINIO);
			sqlQueryObject.addWhereCondition(tableName1+".id_tipo_versamento_dominio="+tableName2+".id");
		}
		
		if(expression.inUseModel(NotificaAppIO.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO,false)){
			if(!expression.inUseModel(NotificaAppIO.model().ID_TIPO_VERSAMENTO_DOMINIO,false)){
				String tableName1 = this.getNotificaAppIOFieldConverter().toAliasTable(NotificaAppIO.model());
				String tableName2 = this.getNotificaAppIOFieldConverter().toAliasTable(NotificaAppIO.model().ID_TIPO_VERSAMENTO_DOMINIO);
				sqlQueryObject.addFromTable(tableName2);
				sqlQueryObject.addWhereCondition(tableName1+".id_tipo_versamento_dominio="+tableName2+".id");
			}

			String tableName1 = this.getNotificaAppIOFieldConverter().toAliasTable(NotificaAppIO.model().ID_TIPO_VERSAMENTO_DOMINIO);
			String tableName2 = this.getNotificaAppIOFieldConverter().toAliasTable(NotificaAppIO.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO);
			sqlQueryObject.addWhereCondition(tableName1+".id_dominio="+tableName2+".id");
		}
		
		if(expression.inUseModel(NotificaAppIO.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO,false)){
			if(!expression.inUseModel(NotificaAppIO.model().ID_TIPO_VERSAMENTO_DOMINIO,false)){
				String tableName1 = this.getNotificaAppIOFieldConverter().toAliasTable(NotificaAppIO.model());
				String tableName2 = this.getNotificaAppIOFieldConverter().toAliasTable(NotificaAppIO.model().ID_TIPO_VERSAMENTO_DOMINIO);
				sqlQueryObject.addFromTable(tableName2);
				sqlQueryObject.addWhereCondition(tableName1+".id_tipo_versamento_dominio="+tableName2+".id");
			}

			String tableName1 = this.getNotificaAppIOFieldConverter().toAliasTable(NotificaAppIO.model().ID_TIPO_VERSAMENTO_DOMINIO);
			String tableName2 = this.getNotificaAppIOFieldConverter().toAliasTable(NotificaAppIO.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO);
			sqlQueryObject.addWhereCondition(tableName1+".id_tipo_versamento="+tableName2+".id");
		}
	}
	
	protected java.util.List<Object> _getRootTablePrimaryKeyValues(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdNotifica id) throws NotFoundException, ServiceException, NotImplementedException, Exception{
	    // Identificativi
        java.util.List<Object> rootTableIdValues = new java.util.ArrayList<Object>();
		Long longId = this.findIdNotificaAppIO(jdbcProperties, log, connection, sqlQueryObject.newSQLQueryObject(), id, true);
		rootTableIdValues.add(longId);
        return rootTableIdValues;
	}
	
	protected Map<String, List<IField>> _getMapTableToPKColumn() throws NotImplementedException, Exception{
	
		NotificaAppIOFieldConverter converter = this.getNotificaAppIOFieldConverter();
		Map<String, List<IField>> mapTableToPKColumn = new java.util.Hashtable<String, List<IField>>();
		UtilsTemplate<IField> utilities = new UtilsTemplate<IField>();

		// NotificaAppIO.model()
		mapTableToPKColumn.put(converter.toTable(NotificaAppIO.model()),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(NotificaAppIO.model()))
			));

		// NotificaAppIO.model().ID_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(NotificaAppIO.model().ID_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(NotificaAppIO.model().ID_VERSAMENTO))
			));

		// NotificaAppIO.model().ID_VERSAMENTO.ID_APPLICAZIONE
		mapTableToPKColumn.put(converter.toTable(NotificaAppIO.model().ID_VERSAMENTO.ID_APPLICAZIONE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(NotificaAppIO.model().ID_VERSAMENTO.ID_APPLICAZIONE))
			));

		// NotificaAppIO.model().ID_VERSAMENTO.ID_UO
		mapTableToPKColumn.put(converter.toTable(NotificaAppIO.model().ID_VERSAMENTO.ID_UO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(NotificaAppIO.model().ID_VERSAMENTO.ID_UO))
			));

		// NotificaAppIO.model().ID_VERSAMENTO.ID_UO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(NotificaAppIO.model().ID_VERSAMENTO.ID_UO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(NotificaAppIO.model().ID_VERSAMENTO.ID_UO.ID_DOMINIO))
			));

		// NotificaAppIO.model().ID_VERSAMENTO.ID_TIPO_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(NotificaAppIO.model().ID_VERSAMENTO.ID_TIPO_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(NotificaAppIO.model().ID_VERSAMENTO.ID_TIPO_VERSAMENTO))
			));

		// NotificaAppIO.model().ID_TIPO_VERSAMENTO_DOMINIO
		mapTableToPKColumn.put(converter.toTable(NotificaAppIO.model().ID_TIPO_VERSAMENTO_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(NotificaAppIO.model().ID_TIPO_VERSAMENTO_DOMINIO))
			));

		// NotificaAppIO.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(NotificaAppIO.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(NotificaAppIO.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO))
			));

		// NotificaAppIO.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(NotificaAppIO.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(NotificaAppIO.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO))
			));
        return mapTableToPKColumn;		
	}
	
	@Override
	public List<Long> findAllTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression paginatedExpression) throws ServiceException, NotImplementedException, Exception {
		
		List<Long> list = new ArrayList<Long>();

		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getNotificaAppIOFieldConverter().toTable(NotificaAppIO.model())+".id");
		Class<?> objectIdClass = Long.class;
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFindAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
												this.getNotificaAppIOFieldConverter(), NotificaAppIO.model());
		
		_join(paginatedExpression,sqlQueryObject);
		
		List<Object> listObjects = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.findAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
																			this.getNotificaAppIOFieldConverter(), NotificaAppIO.model(), objectIdClass, listaQuery);
		for(Object object: listObjects) {
			list.add((Long)object);
		}

        return list;
		
	}
	
	@Override
	public long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws ServiceException, NotFoundException, MultipleResultException, NotImplementedException, Exception {
	
		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getNotificaAppIOFieldConverter().toTable(NotificaAppIO.model())+".id");
		Class<?> objectIdClass = Long.class;
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFind(jdbcProperties, log, connection, sqlQueryObject, expression,
												this.getNotificaAppIOFieldConverter(), NotificaAppIO.model());
		
		_join(expression,sqlQueryObject);

		Object res = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.find(jdbcProperties, log, connection, sqlQueryObject, expression,
														this.getNotificaAppIOFieldConverter(), NotificaAppIO.model(), objectIdClass, listaQuery);
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
	public IdNotifica findId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, boolean throwNotFound)
			throws NotFoundException, ServiceException, NotImplementedException, Exception {
		
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);

		ISQLQueryObject sqlQueryObjectGet = sqlQueryObject.newSQLQueryObject();

		// Object _notificaAppIO
		sqlQueryObjectGet.addFromTable(this.getNotificaAppIOFieldConverter().toTable(NotificaAppIO.model()));
		sqlQueryObjectGet.addSelectField("id");
		sqlQueryObjectGet.setANDLogicOperator(true);
		sqlQueryObjectGet.addWhereCondition("id=?");

		// Recupero _notificaAppIO
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] searchParams_notificaAppIO = new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] { 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tableId,Long.class)
		};
		List<Class<?>> listaFieldIdReturnType_notificaAppIO = new ArrayList<Class<?>>();
		listaFieldIdReturnType_notificaAppIO.add(Long.class);
		it.govpay.orm.IdNotifica id_notificaAppIO = null;
		List<Object> listaFieldId_notificaAppIO = jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet.createSQLQuery(), jdbcProperties.isShowSql(),
				listaFieldIdReturnType_notificaAppIO, searchParams_notificaAppIO);
		if(listaFieldId_notificaAppIO==null || listaFieldId_notificaAppIO.size()<=0){
			if(throwNotFound){
				throw new NotFoundException("Not Found");
			}
		}
		else{
			// set _notificaAppIO
			id_notificaAppIO = new it.govpay.orm.IdNotifica();
			id_notificaAppIO.setIdNotifica((Long)listaFieldId_notificaAppIO.get(0));
		}
		
		return id_notificaAppIO;
		
	}

	@Override
	public Long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdNotifica id, boolean throwNotFound)
			throws NotFoundException, ServiceException, NotImplementedException, Exception {
	
		return this.findIdNotificaAppIO(jdbcProperties,log,connection,sqlQueryObject,id,throwNotFound);
			
	}
	
	@Override
	public List<List<Object>> nativeQuery(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
											String sql,List<Class<?>> returnClassTypes,Object ... param) throws ServiceException,NotFoundException,NotImplementedException,Exception{
		
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.nativeQuery(jdbcProperties, log, connection, sqlQueryObject,
																							sql,returnClassTypes,param);
														
	}
	
	protected Long findIdNotificaAppIO(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdNotifica id, boolean throwNotFound) throws NotFoundException, ServiceException, NotImplementedException, Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);

		ISQLQueryObject sqlQueryObjectGet = sqlQueryObject.newSQLQueryObject();

		// Object _notificaAppIO
		sqlQueryObjectGet.addFromTable(this.getNotificaAppIOFieldConverter().toTable(NotificaAppIO.model()));
		sqlQueryObjectGet.addSelectField("id");
		sqlQueryObjectGet.setANDLogicOperator(true);
//		sqlQueryObjectGet.setSelectDistinct(true);
		sqlQueryObjectGet.addWhereCondition("id=?");

		// Recupero _notificaAppIO
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] searchParams_notificaAppIO = new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] { 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id.getIdNotifica(),Long.class),
		};
		Long id_notificaAppIO = null;
		try{
			id_notificaAppIO = (Long) jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet.createSQLQuery(), jdbcProperties.isShowSql(),
						Long.class, searchParams_notificaAppIO);
		}catch(NotFoundException notFound){
			if(throwNotFound){
				throw new NotFoundException(notFound);
			}
		}
		if(id_notificaAppIO==null || id_notificaAppIO<=0){
			if(throwNotFound){
				throw new NotFoundException("Not Found");
			}
		}
		
		return id_notificaAppIO;
	}
}
