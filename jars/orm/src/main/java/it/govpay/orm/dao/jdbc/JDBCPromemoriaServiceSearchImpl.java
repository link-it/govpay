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

import it.govpay.orm.IdPromemoria;
import it.govpay.orm.Promemoria;
import it.govpay.orm.dao.jdbc.converter.PromemoriaFieldConverter;
import it.govpay.orm.dao.jdbc.fetch.PromemoriaFetch;

/**     
 * JDBCPromemoriaServiceSearchImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCPromemoriaServiceSearchImpl implements IJDBCServiceSearchWithId<Promemoria, IdPromemoria, JDBCServiceManager> {

	private PromemoriaFieldConverter _promemoriaFieldConverter = null;
	public PromemoriaFieldConverter getPromemoriaFieldConverter() {
		if(this._promemoriaFieldConverter==null){
			this._promemoriaFieldConverter = new PromemoriaFieldConverter(this.jdbcServiceManager.getJdbcProperties().getDatabaseType());
		}		
		return this._promemoriaFieldConverter;
	}
	@Override
	public ISQLFieldConverter getFieldConverter() {
		return this.getPromemoriaFieldConverter();
	}
	
	private PromemoriaFetch promemoriaFetch = new PromemoriaFetch();
	public PromemoriaFetch getPromemoriaFetch() {
		return this.promemoriaFetch;
	}
	@Override
	public IJDBCFetch getFetch() {
		return getPromemoriaFetch();
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
	public IdPromemoria convertToId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Promemoria promemoria) throws NotImplementedException, ServiceException, Exception{
	
		IdPromemoria idPromemoria = new IdPromemoria();
		idPromemoria.setIdPromemoria(promemoria.getId());
		return idPromemoria;
	}
	
	@Override
	public Promemoria get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPromemoria id, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException,Exception {
		Long id_promemoria = ( (id!=null && id.getId()!=null && id.getId()>0) ? id.getId() : this.findIdPromemoria(jdbcProperties, log, connection, sqlQueryObject, id, true));
		return this._get(jdbcProperties, log, connection, sqlQueryObject, id_promemoria,idMappingResolutionBehaviour);
		
		
	}
	
	@Override
	public boolean exists(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPromemoria id) throws MultipleResultException, NotImplementedException, ServiceException,Exception {

		Long id_promemoria = this.findIdPromemoria(jdbcProperties, log, connection, sqlQueryObject, id, false);
		return id_promemoria != null && id_promemoria > 0;
		
	}
	
	@Override
	public List<IdPromemoria> findAllIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {

        // default behaviour (id-mapping)
        if(idMappingResolutionBehaviour==null){
                idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
        }
		List<IdPromemoria> list = new ArrayList<IdPromemoria>();
		
		try{
			List<IField> fields = new ArrayList<>();
			fields.add(new CustomField("id", Long.class, "id", this.getFieldConverter().toTable(Promemoria.model())));

			List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));
        
			for(Map<String, Object> map: returnMap) {
				list.add(this.convertToId(jdbcProperties, log, connection, sqlQueryObject, (Promemoria)this.getPromemoriaFetch().fetch(jdbcProperties.getDatabase(), Promemoria.model(), map)));
	        }
		} catch(NotFoundException e) {}
        return list;
		
	}
	
	@Override
	public List<Promemoria> findAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {

        // default behaviour (id-mapping)
        if(idMappingResolutionBehaviour==null){
                idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
        }
        List<Promemoria> list = new ArrayList<Promemoria>();
        
        try{
			List<IField> fields = new ArrayList<>();
			fields.add(new CustomField("id", Long.class, "id", this.getFieldConverter().toTable(Promemoria.model())));
			fields.add(new CustomField("id_versamento", Long.class, "id_versamento", this.getFieldConverter().toTable(Promemoria.model())));
			fields.add(new CustomField("id_rpt", Long.class, "id_rpt", this.getFieldConverter().toTable(Promemoria.model())));
			fields.add(new CustomField("id_documento", Long.class, "id_documento", this.getFieldConverter().toTable(Promemoria.model())));
			fields.add(Promemoria.model().TIPO);
			fields.add(Promemoria.model().DATA_CREAZIONE);
			fields.add(Promemoria.model().STATO);
			fields.add(Promemoria.model().DESCRIZIONE_STATO);
			fields.add(Promemoria.model().DATA_AGGIORNAMENTO_STATO);
			fields.add(Promemoria.model().TENTATIVI_SPEDIZIONE);
			fields.add(Promemoria.model().DATA_PROSSIMA_SPEDIZIONE);
			fields.add(Promemoria.model().DESTINATARIO_TO);
			fields.add(Promemoria.model().DESTINATARIO_CC);
			fields.add(Promemoria.model().OGGETTO);
			fields.add(Promemoria.model().MESSAGGIO);
			fields.add(Promemoria.model().ALLEGA_PDF);
			fields.add(Promemoria.model().MESSAGGIO_CONTENT_TYPE);
			
			List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));
        
			for(Map<String, Object> map: returnMap) {
				Long id_versamento = null;
				Object idVersamentoObject = map.remove("id_versamento");
				if(idVersamentoObject instanceof Long) {
					id_versamento = (Long) idVersamentoObject;
				}
				
				
				Object id_rptObj = map.remove("id_rpt");
				Long id_rpt = null;
				if(id_rptObj instanceof Long)
					id_rpt = (Long) id_rptObj;  
				
				Long idDocumento = null;
				Object idDocumentoObject = map.remove("id_documento");
				if(idDocumentoObject instanceof Long) {
					idDocumento = (Long) idDocumentoObject;
				}
				
				Promemoria notifica = (Promemoria)this.getPromemoriaFetch().fetch(jdbcProperties.getDatabase(), Promemoria.model(), map);
				
				if(id_versamento != null && id_versamento > 0) {
					it.govpay.orm.IdVersamento id_promemoria_versamento = new it.govpay.orm.IdVersamento();
					id_promemoria_versamento.setId(id_versamento);
					notifica.setIdVersamento(id_promemoria_versamento);
				}
				
				if(id_rpt != null) {
					it.govpay.orm.IdRpt id_promemoria_rpt = new it.govpay.orm.IdRpt();
					id_promemoria_rpt.setId(id_rpt);
					notifica.setIdRPT(id_promemoria_rpt);
				}
				
				if(idDocumento != null && idDocumento > 0) {
					it.govpay.orm.IdDocumento id_stampa_documento = new it.govpay.orm.IdDocumento();
					id_stampa_documento.setId(idDocumento);
					notifica.setIdDocumento(id_stampa_documento);
				}

				list.add(notifica);
	        }
		} catch(NotFoundException e) {}

        return list;
	}
	
	@Override
	public Promemoria find(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) 
		throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException,Exception {

		JDBCPaginatedExpression pagExpr = this.toPaginatedExpression(expression,log);
		
		List<Promemoria> lst = this.findAll(jdbcProperties, log, connection, sqlQueryObject, pagExpr, idMappingResolutionBehaviour);

		if(lst.size() <=0)
			throw new NotFoundException("Nessuna entry corrisponde ai criteri indicati.");

		if(lst.size() > 1)
			throw new MultipleResultException("I criteri indicati individuano piu' entry.");

		return lst.get(0);
		
	}
	
	@Override
	public NonNegativeNumber count(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws NotImplementedException, ServiceException,Exception {
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareCount(jdbcProperties, log, connection, sqlQueryObject, expression,
												this.getPromemoriaFieldConverter(), Promemoria.model());
		
		sqlQueryObject.addSelectCountField(this.getPromemoriaFieldConverter().toTable(Promemoria.model())+".id","tot");
		
		_join(expression,sqlQueryObject);
		
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.count(jdbcProperties, log, connection, sqlQueryObject, expression,
																			this.getPromemoriaFieldConverter(), Promemoria.model(),listaQuery);
	}

	@Override
	public InUse inUse(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPromemoria id) throws NotFoundException, NotImplementedException, ServiceException,Exception {
		
		Long id_promemoria = this.findIdPromemoria(jdbcProperties, log, connection, sqlQueryObject, id, true);
        return this._inUse(jdbcProperties, log, connection, sqlQueryObject, id_promemoria);
		
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
												this.getPromemoriaFieldConverter(), field);

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
        						expression, this.getPromemoriaFieldConverter(), Promemoria.model(), 
        						listaQuery,listaParams);
		
		_join(expression,sqlQueryObject);
        
        List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.select(jdbcProperties, log, connection,
        								org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareSqlQueryObjectForSelectDistinct(sqlQueryObject,sqlQueryObjectDistinct), 
        								expression, this.getPromemoriaFieldConverter(), Promemoria.model(),
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
        						this.getPromemoriaFieldConverter(), Promemoria.model(), 
        						sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);
		
		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				_join(expression,sqlQueryObjectInnerList.get(i));
			}
		}
        
        List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.union(jdbcProperties, log, connection, sqlQueryObject, 
        								this.getPromemoriaFieldConverter(), Promemoria.model(), 
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
        						this.getPromemoriaFieldConverter(), Promemoria.model(), 
        						sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);
		
		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				_join(expression,sqlQueryObjectInnerList.get(i));
			}
		}
        
        NonNegativeNumber number = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.unionCount(jdbcProperties, log, connection, sqlQueryObject, 
        								this.getPromemoriaFieldConverter(), Promemoria.model(), 
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
			return new JDBCExpression(this.getPromemoriaFieldConverter());
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}


	@Override
	public JDBCPaginatedExpression newPaginatedExpression(Logger log) throws NotImplementedException, ServiceException {
		try{
			return new JDBCPaginatedExpression(this.getPromemoriaFieldConverter());
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
	public void mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPromemoria id, Promemoria obj) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		_mappingTableIds(jdbcProperties,log,connection,sqlQueryObject,obj,
				this.get(jdbcProperties,log,connection,sqlQueryObject,id,null));
	}
	
	@Override
	public void mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Promemoria obj) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		_mappingTableIds(jdbcProperties,log,connection,sqlQueryObject,obj,
				this.get(jdbcProperties,log,connection,sqlQueryObject,tableId,null));
	}
	private void _mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Promemoria obj, Promemoria imgSaved) throws NotFoundException,NotImplementedException,ServiceException,Exception{
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
		}
		if(obj.getIdRPT()!=null && 
				imgSaved.getIdRPT()!=null){
			obj.getIdRPT().setId(imgSaved.getIdRPT().getId());
		}

	}
	
	@Override
	public Promemoria get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
		return this._get(jdbcProperties, log, connection, sqlQueryObject, Long.valueOf(tableId), idMappingResolutionBehaviour);
	}
	
	private Promemoria _get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
	
		IField idField = new CustomField("id", Long.class, "id", this.getPromemoriaFieldConverter().toTable(Promemoria.model()));
		JDBCPaginatedExpression expression = this.newPaginatedExpression(log);
		
		expression.equals(idField, tableId);
		List<Promemoria> lst = this.findAll(jdbcProperties, log, connection, sqlQueryObject.newSQLQueryObject(), expression, idMappingResolutionBehaviour);
		
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
				
		boolean existsPromemoria = false;

		sqlQueryObject = sqlQueryObject.newSQLQueryObject();
		sqlQueryObject.setANDLogicOperator(true);

		sqlQueryObject.addFromTable(this.getPromemoriaFieldConverter().toTable(Promemoria.model()));
		sqlQueryObject.addSelectField(this.getPromemoriaFieldConverter().toColumn(Promemoria.model().TIPO,true));
		sqlQueryObject.addWhereCondition("id=?");


		// Exists promemoria
		existsPromemoria = jdbcUtilities.exists(sqlQueryObject.createSQLQuery(), jdbcProperties.isShowSql(),
			new JDBCObject(tableId,Long.class));

		
        return existsPromemoria;
	
	}
	
	private void _join(IExpression expression, ISQLQueryObject sqlQueryObject) throws NotImplementedException, ServiceException, Exception{
		
		if(expression.inUseModel(Promemoria.model().ID_DOCUMENTO,false)){
			String tableName1 = this.getPromemoriaFieldConverter().toAliasTable(Promemoria.model());
			String tableName2 = this.getPromemoriaFieldConverter().toAliasTable(Promemoria.model().ID_DOCUMENTO);
			sqlQueryObject.addWhereCondition(tableName1+".id_documento="+tableName2+".id");
		}
	
		if(expression.inUseModel(Promemoria.model().ID_VERSAMENTO,false)){
			String tableName1 = this.getPromemoriaFieldConverter().toAliasTable(Promemoria.model());
			String tableName2 = this.getPromemoriaFieldConverter().toAliasTable(Promemoria.model().ID_VERSAMENTO);
			sqlQueryObject.addWhereCondition(tableName1+".id_versamento="+tableName2+".id");
		}
		
		if(expression.inUseModel(Promemoria.model().ID_RPT,false)){
			String tableName1 = this.getPromemoriaFieldConverter().toAliasTable(Promemoria.model());
			String tableName2 = this.getPromemoriaFieldConverter().toAliasTable(Promemoria.model().ID_RPT);
			sqlQueryObject.addWhereCondition(tableName1+".id_rpt="+tableName2+".id");
		}
	}
	
	protected java.util.List<Object> _getRootTablePrimaryKeyValues(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPromemoria id) throws NotFoundException, ServiceException, NotImplementedException, Exception{
	    // Identificativi
        java.util.List<Object> rootTableIdValues = new java.util.ArrayList<Object>();
		Long longId = this.findIdPromemoria(jdbcProperties, log, connection, sqlQueryObject.newSQLQueryObject(), id, true);
		rootTableIdValues.add(longId);
        return rootTableIdValues;
	}
	
	protected Map<String, List<IField>> _getMapTableToPKColumn() throws NotImplementedException, Exception{
	
		PromemoriaFieldConverter converter = this.getPromemoriaFieldConverter();
		Map<String, List<IField>> mapTableToPKColumn = new java.util.Hashtable<String, List<IField>>();
		UtilsTemplate<IField> utilities = new UtilsTemplate<IField>();

		// Promemoria.model()
		mapTableToPKColumn.put(converter.toTable(Promemoria.model()),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Promemoria.model()))
			));

		// Promemoria.model().ID_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(Promemoria.model().ID_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Promemoria.model().ID_VERSAMENTO))
			));

		// Promemoria.model().ID_VERSAMENTO.ID_APPLICAZIONE
		mapTableToPKColumn.put(converter.toTable(Promemoria.model().ID_VERSAMENTO.ID_APPLICAZIONE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Promemoria.model().ID_VERSAMENTO.ID_APPLICAZIONE))
			));

		// Promemoria.model().ID_VERSAMENTO.ID_UO
		mapTableToPKColumn.put(converter.toTable(Promemoria.model().ID_VERSAMENTO.ID_UO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Promemoria.model().ID_VERSAMENTO.ID_UO))
			));

		// Promemoria.model().ID_VERSAMENTO.ID_UO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(Promemoria.model().ID_VERSAMENTO.ID_UO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Promemoria.model().ID_VERSAMENTO.ID_UO.ID_DOMINIO))
			));

		// Promemoria.model().ID_VERSAMENTO.ID_TIPO_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(Promemoria.model().ID_VERSAMENTO.ID_TIPO_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Promemoria.model().ID_VERSAMENTO.ID_TIPO_VERSAMENTO))
			));

		// Promemoria.model().ID_RPT
		mapTableToPKColumn.put(converter.toTable(Promemoria.model().ID_RPT),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Promemoria.model().ID_RPT))
			));

        return mapTableToPKColumn;		
	}
	
	@Override
	public List<Long> findAllTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression paginatedExpression) throws ServiceException, NotImplementedException, Exception {
		
		List<Long> list = new ArrayList<Long>();

		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getPromemoriaFieldConverter().toTable(Promemoria.model())+".id");
		Class<?> objectIdClass = Long.class;
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFindAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
												this.getPromemoriaFieldConverter(), Promemoria.model());
		
		_join(paginatedExpression,sqlQueryObject);
		
		List<Object> listObjects = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.findAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
																			this.getPromemoriaFieldConverter(), Promemoria.model(), objectIdClass, listaQuery);
		for(Object object: listObjects) {
			list.add((Long)object);
		}

        return list;
		
	}
	
	@Override
	public long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws ServiceException, NotFoundException, MultipleResultException, NotImplementedException, Exception {
	
		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getPromemoriaFieldConverter().toTable(Promemoria.model())+".id");
		Class<?> objectIdClass = Long.class;
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFind(jdbcProperties, log, connection, sqlQueryObject, expression,
												this.getPromemoriaFieldConverter(), Promemoria.model());
		
		_join(expression,sqlQueryObject);

		Object res = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.find(jdbcProperties, log, connection, sqlQueryObject, expression,
														this.getPromemoriaFieldConverter(), Promemoria.model(), objectIdClass, listaQuery);
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
	public IdPromemoria findId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, boolean throwNotFound)
			throws NotFoundException, ServiceException, NotImplementedException, Exception {
		
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);

		ISQLQueryObject sqlQueryObjectGet = sqlQueryObject.newSQLQueryObject();
		// Object _promemoria
		sqlQueryObjectGet.addFromTable(this.getPromemoriaFieldConverter().toTable(Promemoria.model()));
		sqlQueryObjectGet.addSelectField("id");
		sqlQueryObjectGet.setANDLogicOperator(true);
		sqlQueryObjectGet.addWhereCondition("id=?");

		// Recupero _promemoria
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] searchParams_promemoria = new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] { 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tableId,Long.class)
		};
		List<Class<?>> listaFieldIdReturnType_promemoria = new ArrayList<Class<?>>();
		listaFieldIdReturnType_promemoria.add(Long.class);
		it.govpay.orm.IdPromemoria id_promemoria = null;
		List<Object> listaFieldId_promemoria = jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet.createSQLQuery(), jdbcProperties.isShowSql(),
				listaFieldIdReturnType_promemoria, searchParams_promemoria);
		if(listaFieldId_promemoria==null || listaFieldId_promemoria.size()<=0){
			if(throwNotFound){
				throw new NotFoundException("Not Found");
			}
		}
		else{
			// set _promemoria
			id_promemoria = new it.govpay.orm.IdPromemoria();
			id_promemoria.setIdPromemoria((Long)listaFieldId_promemoria.get(0));
		}
		
		return id_promemoria;
		
	}

	@Override
	public Long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPromemoria id, boolean throwNotFound)
			throws NotFoundException, ServiceException, NotImplementedException, Exception {
	
		return this.findIdPromemoria(jdbcProperties,log,connection,sqlQueryObject,id,throwNotFound);
			
	}
	
	@Override
	public List<List<Object>> nativeQuery(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
											String sql,List<Class<?>> returnClassTypes,Object ... param) throws ServiceException,NotFoundException,NotImplementedException,Exception{
		
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.nativeQuery(jdbcProperties, log, connection, sqlQueryObject,
																							sql,returnClassTypes,param);
														
	}
	
	protected Long findIdPromemoria(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPromemoria id, boolean throwNotFound) throws NotFoundException, ServiceException, NotImplementedException, Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);

		ISQLQueryObject sqlQueryObjectGet = sqlQueryObject.newSQLQueryObject();

		// Object _promemoria
		sqlQueryObjectGet.addFromTable(this.getPromemoriaFieldConverter().toTable(Promemoria.model()));
		sqlQueryObjectGet.addSelectField("id");
		// Devono essere mappati nella where condition i metodi dell'oggetto id.getXXX
		sqlQueryObjectGet.setANDLogicOperator(true);
//		sqlQueryObjectGet.setSelectDistinct(true);
		sqlQueryObjectGet.addWhereCondition("id=?");
		// Recupero _promemoria
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] searchParams_promemoria = new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] { 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id.getIdPromemoria(),Long.class),
		};
		Long id_promemoria = null;
		try{
			id_promemoria = (Long) jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet.createSQLQuery(), jdbcProperties.isShowSql(),
						Long.class, searchParams_promemoria);
		}catch(NotFoundException notFound){
			if(throwNotFound){
				throw new NotFoundException(notFound);
			}
		}
		if(id_promemoria==null || id_promemoria<=0){
			if(throwNotFound){
				throw new NotFoundException("Not Found");
			}
		}
		
		return id_promemoria;
	}
}
