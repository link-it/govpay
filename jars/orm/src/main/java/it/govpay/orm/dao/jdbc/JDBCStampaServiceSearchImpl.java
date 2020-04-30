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
import it.govpay.orm.IdStampa;
import it.govpay.orm.IdVersamento;
import it.govpay.orm.Stampa;
import it.govpay.orm.dao.jdbc.converter.StampaFieldConverter;
import it.govpay.orm.dao.jdbc.fetch.StampaFetch;

/**     
 * JDBCStampaServiceSearchImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCStampaServiceSearchImpl implements IJDBCServiceSearchWithId<Stampa, IdStampa, JDBCServiceManager> {

	private StampaFieldConverter _stampaFieldConverter = null;
	public StampaFieldConverter getStampaFieldConverter() {
		if(this._stampaFieldConverter==null){
			this._stampaFieldConverter = new StampaFieldConverter(this.jdbcServiceManager.getJdbcProperties().getDatabaseType());
		}		
		return this._stampaFieldConverter;
	}
	@Override
	public ISQLFieldConverter getFieldConverter() {
		return this.getStampaFieldConverter();
	}
	
	private StampaFetch stampaFetch = new StampaFetch();
	public StampaFetch getStampaFetch() {
		return this.stampaFetch;
	}
	@Override
	public IJDBCFetch getFetch() {
		return getStampaFetch();
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
	public IdStampa convertToId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Stampa stampa) throws NotImplementedException, ServiceException, Exception{
		IdStampa idStampa = new IdStampa();
		idStampa.setTipo(stampa.getTipo());
		idStampa.setIdVersamento(stampa.getIdVersamento());
		idStampa.setIdDocumento(stampa.getIdDocumento());
		idStampa.setId(stampa.getId());
		return idStampa;
	}
	
	@Override
	public Stampa get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdStampa id, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException,Exception {
		Long id_stampa = ( (id!=null && id.getId()!=null && id.getId()>0) ? id.getId() : this.findIdStampa(jdbcProperties, log, connection, sqlQueryObject, id, true));
		return this._get(jdbcProperties, log, connection, sqlQueryObject, id_stampa,idMappingResolutionBehaviour);
		
		
	}
	
	@Override
	public boolean exists(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdStampa id) throws MultipleResultException, NotImplementedException, ServiceException,Exception {

		Long id_stampa = this.findIdStampa(jdbcProperties, log, connection, sqlQueryObject, id, false);
		return id_stampa != null && id_stampa > 0;
		
	}
	
	@Override
	public List<IdStampa> findAllIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {

		
     // default behaviour (id-mapping)
        if(idMappingResolutionBehaviour==null){
                idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
        }

        List<IdStampa> list = new ArrayList<IdStampa>();

		try{
			List<IField> fields = new ArrayList<>();
			fields.add(new CustomField("id_versamento", Long.class, "id_versamento", this.getStampaFieldConverter().toTable(Stampa.model())));
			fields.add(new CustomField("id_documento", Long.class, "id_documento", this.getStampaFieldConverter().toTable(Stampa.model())));
			fields.add(Stampa.model().TIPO);

			List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));
        
			for(Map<String, Object> map: returnMap) {
				IdStampa idStampa = new IdStampa();
				
				Long idVersamento = null;
				Object idVersamentoObject = map.remove("id_versamento");
				if(idVersamentoObject instanceof Long) {
					idVersamento = (Long) idVersamentoObject;
				}
				
				Long idDocumento = null;
				Object idDocumentoObject = map.remove("id_documento");
				if(idDocumentoObject instanceof Long) {
					idDocumento = (Long) idDocumentoObject;
				}

				if(idVersamento != null && idVersamento > 0) {
					it.govpay.orm.IdVersamento id_stampa_versamento = null;
					if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
						id_stampa_versamento = ((JDBCVersamentoServiceSearch)(this.getServiceManager().getVersamentoServiceSearch())).findId(idVersamento, false);
					}else{
						id_stampa_versamento = new it.govpay.orm.IdVersamento();
					}
					id_stampa_versamento.setId(idVersamento);
					idStampa.setIdVersamento(id_stampa_versamento);
				}
				
				if(idDocumento != null && idDocumento > 0) {
					it.govpay.orm.IdDocumento id_stampa_documento = null;
					if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
						id_stampa_documento = ((JDBCDocumentoServiceSearch)(this.getServiceManager().getDocumentoServiceSearch())).findId(idDocumento, false);
					}else{
						id_stampa_documento = new it.govpay.orm.IdDocumento();
					}
					id_stampa_documento.setId(idDocumento);
					idStampa.setIdDocumento(id_stampa_documento);
				}

				idStampa.setTipo((String) map.get("tipo"));
				
				list.add(idStampa);
			}
		} catch(NotFoundException e) {}

        return list;
	}
	
	@Override
	public List<Stampa> findAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {

		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
        List<Stampa> list = new ArrayList<Stampa>();
        
        
        try{
			List<IField> fields = new ArrayList<>();
			fields.add(new CustomField("id", Long.class, "id", this.getStampaFieldConverter().toTable(Stampa.model())));
			fields.add(Stampa.model().DATA_CREAZIONE);
			fields.add(Stampa.model().PDF);
			fields.add(Stampa.model().TIPO);
			fields.add(new CustomField("id_versamento", Long.class, "id_versamento", this.getStampaFieldConverter().toTable(Stampa.model())));
			fields.add(new CustomField("id_documento", Long.class, "id_documento", this.getStampaFieldConverter().toTable(Stampa.model())));
			
			List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));
			
			for(Map<String, Object> map: returnMap) {
				
				Long idVersamento = null;
				Object idVersamentoObject = map.remove("id_versamento");
				if(idVersamentoObject instanceof Long) {
					idVersamento = (Long) idVersamentoObject;
				}
				
				Long idDocumento = null;
				Object idDocumentoObject = map.remove("id_documento");
				if(idDocumentoObject instanceof Long) {
					idDocumento = (Long) idDocumentoObject;
				}
				
				Stampa stampa = (Stampa)this.getStampaFetch().fetch(jdbcProperties.getDatabase(), Stampa.model(), map);
				
				if(idVersamento != null && idVersamento > 0) {
					it.govpay.orm.IdVersamento id_stampa_versamento = null;
					if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
						id_stampa_versamento = ((JDBCVersamentoServiceSearch)(this.getServiceManager().getVersamentoServiceSearch())).findId(idVersamento, false);
					}else{
						id_stampa_versamento = new it.govpay.orm.IdVersamento();
					}
					id_stampa_versamento.setId(idVersamento);
					stampa.setIdVersamento(id_stampa_versamento);
				}
				
				if(idDocumento != null && idDocumento > 0) {
					it.govpay.orm.IdDocumento id_stampa_documento = null;
					if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
						id_stampa_documento = ((JDBCDocumentoServiceSearch)(this.getServiceManager().getDocumentoServiceSearch())).findId(idDocumento, false);
					}else{
						id_stampa_documento = new it.govpay.orm.IdDocumento();
					}
					id_stampa_documento.setId(idDocumento);
					stampa.setIdDocumento(id_stampa_documento);
				}

				list.add(stampa);
			}
        } catch(NotFoundException e) {}
	        
        return list;      
		
	}
	
	@Override
	public Stampa find(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) 
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
												this.getStampaFieldConverter(), Stampa.model());
		
		sqlQueryObject.addSelectCountField(this.getStampaFieldConverter().toTable(Stampa.model())+".id","tot");
		
		_join(expression,sqlQueryObject);
		
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.count(jdbcProperties, log, connection, sqlQueryObject, expression,
																			this.getStampaFieldConverter(), Stampa.model(),listaQuery);
	}

	@Override
	public InUse inUse(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdStampa id) throws NotFoundException, NotImplementedException, ServiceException,Exception {
		
		Long id_stampa = this.findIdStampa(jdbcProperties, log, connection, sqlQueryObject, id, true);
        return this._inUse(jdbcProperties, log, connection, sqlQueryObject, id_stampa);
		
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
												this.getStampaFieldConverter(), field);

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
        						expression, this.getStampaFieldConverter(), Stampa.model(), 
        						listaQuery,listaParams);
		
		_join(expression,sqlQueryObject);
        
        List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.select(jdbcProperties, log, connection,
        								org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareSqlQueryObjectForSelectDistinct(sqlQueryObject,sqlQueryObjectDistinct), 
        								expression, this.getStampaFieldConverter(), Stampa.model(),
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
        						this.getStampaFieldConverter(), Stampa.model(), 
        						sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);
		
		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				_join(expression,sqlQueryObjectInnerList.get(i));
			}
		}
        
        List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.union(jdbcProperties, log, connection, sqlQueryObject, 
        								this.getStampaFieldConverter(), Stampa.model(), 
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
        						this.getStampaFieldConverter(), Stampa.model(), 
        						sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);
		
		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				_join(expression,sqlQueryObjectInnerList.get(i));
			}
		}
        
        NonNegativeNumber number = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.unionCount(jdbcProperties, log, connection, sqlQueryObject, 
        								this.getStampaFieldConverter(), Stampa.model(), 
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
			return new JDBCExpression(this.getStampaFieldConverter());
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}


	@Override
	public JDBCPaginatedExpression newPaginatedExpression(Logger log) throws NotImplementedException, ServiceException {
		try{
			return new JDBCPaginatedExpression(this.getStampaFieldConverter());
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
	public void mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdStampa id, Stampa obj) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		_mappingTableIds(jdbcProperties,log,connection,sqlQueryObject,obj,
				this.get(jdbcProperties,log,connection,sqlQueryObject,id,null));
	}
	
	@Override
	public void mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Stampa obj) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		_mappingTableIds(jdbcProperties,log,connection,sqlQueryObject,obj,
				this.get(jdbcProperties,log,connection,sqlQueryObject,tableId,null));
	}
	private void _mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Stampa obj, Stampa imgSaved) throws NotFoundException,NotImplementedException,ServiceException,Exception{
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

	}
	
	@Override
	public Stampa get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
		return this._get(jdbcProperties, log, connection, sqlQueryObject, Long.valueOf(tableId), idMappingResolutionBehaviour);
	}
	
	private Stampa _get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
	
		IField idField = new CustomField("id", Long.class, "id", this.getStampaFieldConverter().toTable(Stampa.model()));
		JDBCPaginatedExpression expression = this.newPaginatedExpression(log);
		
		expression.equals(idField, tableId);
		List<Stampa> lst = this.findAll(jdbcProperties, log, connection, sqlQueryObject.newSQLQueryObject(), expression, idMappingResolutionBehaviour);
		
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
				
		boolean existsStampa = false;

		sqlQueryObject = sqlQueryObject.newSQLQueryObject();
		sqlQueryObject.setANDLogicOperator(true);

		sqlQueryObject.addFromTable(this.getStampaFieldConverter().toTable(Stampa.model()));
		sqlQueryObject.addSelectField(this.getStampaFieldConverter().toColumn(Stampa.model().TIPO,true));
		sqlQueryObject.addWhereCondition("id=?");


		// Exists stampa
		existsStampa = jdbcUtilities.exists(sqlQueryObject.createSQLQuery(), jdbcProperties.isShowSql(),
			new JDBCObject(tableId,Long.class));

		
        return existsStampa;
	
	}
	
	private void _join(IExpression expression, ISQLQueryObject sqlQueryObject) throws NotImplementedException, ServiceException, Exception{
		
		if(expression.inUseModel(Stampa.model().ID_DOCUMENTO,false)){
			String tableName1 = this.getStampaFieldConverter().toAliasTable(Stampa.model());
			String tableName2 = this.getStampaFieldConverter().toAliasTable(Stampa.model().ID_DOCUMENTO);
			sqlQueryObject.addWhereCondition(tableName1+".id_documento="+tableName2+".id");
		}
		
		if(expression.inUseModel(Stampa.model().ID_VERSAMENTO,false)){
			String tableName1 = this.getStampaFieldConverter().toAliasTable(Stampa.model());
			String tableName2 = this.getStampaFieldConverter().toAliasTable(Stampa.model().ID_VERSAMENTO);
			sqlQueryObject.addWhereCondition(tableName1+".id_versamento="+tableName2+".id");
		}
		
		if(expression.inUseModel(Stampa.model().ID_VERSAMENTO.ID_APPLICAZIONE,false)){
			
			if(!expression.inUseModel(Stampa.model().ID_VERSAMENTO,false)){
				String tableName1 = this.getStampaFieldConverter().toAliasTable(Stampa.model());
				String tableName2 = this.getStampaFieldConverter().toAliasTable(Stampa.model().ID_VERSAMENTO);
				sqlQueryObject.addFromTable(tableName2);
				sqlQueryObject.addWhereCondition(tableName1+".id_versamento="+tableName2+".id");
			}
			
			String tableName1 = this.getStampaFieldConverter().toAliasTable(Stampa.model().ID_VERSAMENTO);
			String tableName2 = this.getStampaFieldConverter().toAliasTable(Stampa.model().ID_VERSAMENTO.ID_APPLICAZIONE);
			sqlQueryObject.addWhereCondition(tableName1+".id_applicazione="+tableName2+".id");
		}
	
	}
	
	protected java.util.List<Object> _getRootTablePrimaryKeyValues(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdStampa id) throws NotFoundException, ServiceException, NotImplementedException, Exception{
	    // Identificativi
        java.util.List<Object> rootTableIdValues = new java.util.ArrayList<Object>();
		Long longId = this.findIdStampa(jdbcProperties, log, connection, sqlQueryObject.newSQLQueryObject(), id, true);
		rootTableIdValues.add(longId);
        return rootTableIdValues;
	}
	
	protected Map<String, List<IField>> _getMapTableToPKColumn() throws NotImplementedException, Exception{
	
		StampaFieldConverter converter = this.getStampaFieldConverter();
		Map<String, List<IField>> mapTableToPKColumn = new java.util.Hashtable<String, List<IField>>();
		UtilsTemplate<IField> utilities = new UtilsTemplate<IField>();

		//		  If a table doesn't have a primary key, don't add it to this map

		// Stampa.model()
		mapTableToPKColumn.put(converter.toTable(Stampa.model()),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Stampa.model()))
			));

		// Stampa.model().ID_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(Stampa.model().ID_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Stampa.model().ID_VERSAMENTO))
			));

		// Stampa.model().ID_VERSAMENTO.ID_APPLICAZIONE
		mapTableToPKColumn.put(converter.toTable(Stampa.model().ID_VERSAMENTO.ID_APPLICAZIONE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Stampa.model().ID_VERSAMENTO.ID_APPLICAZIONE))
			));

		// Stampa.model().ID_VERSAMENTO.ID_UO
		mapTableToPKColumn.put(converter.toTable(Stampa.model().ID_VERSAMENTO.ID_UO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Stampa.model().ID_VERSAMENTO.ID_UO))
			));

		// Stampa.model().ID_VERSAMENTO.ID_UO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(Stampa.model().ID_VERSAMENTO.ID_UO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Stampa.model().ID_VERSAMENTO.ID_UO.ID_DOMINIO))
			));

		// Stampa.model().ID_VERSAMENTO.ID_TIPO_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(Stampa.model().ID_VERSAMENTO.ID_TIPO_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Stampa.model().ID_VERSAMENTO.ID_TIPO_VERSAMENTO))
			));

        return mapTableToPKColumn;		
	}
	
	@Override
	public List<Long> findAllTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression paginatedExpression) throws ServiceException, NotImplementedException, Exception {
		
		List<Long> list = new ArrayList<Long>();

		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getStampaFieldConverter().toTable(Stampa.model())+".id");
		Class<?> objectIdClass = Long.class;
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFindAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
												this.getStampaFieldConverter(), Stampa.model());
		
		_join(paginatedExpression,sqlQueryObject);
		
		List<Object> listObjects = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.findAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
																			this.getStampaFieldConverter(), Stampa.model(), objectIdClass, listaQuery);
		for(Object object: listObjects) {
			list.add((Long)object);
		}

        return list;
		
	}
	
	@Override
	public long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws ServiceException, NotFoundException, MultipleResultException, NotImplementedException, Exception {
	
		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getStampaFieldConverter().toTable(Stampa.model())+".id");
		Class<?> objectIdClass = Long.class;
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFind(jdbcProperties, log, connection, sqlQueryObject, expression,
												this.getStampaFieldConverter(), Stampa.model());
		
		_join(expression,sqlQueryObject);

		Object res = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.find(jdbcProperties, log, connection, sqlQueryObject, expression,
														this.getStampaFieldConverter(), Stampa.model(), objectIdClass, listaQuery);
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
	public IdStampa findId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, boolean throwNotFound)
			throws NotFoundException, ServiceException, NotImplementedException, Exception {
		
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);

		ISQLQueryObject sqlQueryObjectGet = sqlQueryObject.newSQLQueryObject();

		sqlQueryObjectGet.addFromTable(this.getStampaFieldConverter().toTable(Stampa.model()));
		sqlQueryObjectGet.addFromTable(this.getStampaFieldConverter().toTable(Stampa.model().ID_VERSAMENTO));
		sqlQueryObjectGet.addFromTable(this.getStampaFieldConverter().toTable(Stampa.model().ID_VERSAMENTO.ID_APPLICAZIONE));
		sqlQueryObjectGet.addSelectField(this.getStampaFieldConverter().toColumn(Stampa.model().ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE,true));
		sqlQueryObjectGet.addSelectField(this.getStampaFieldConverter().toColumn(Stampa.model().ID_VERSAMENTO.COD_VERSAMENTO_ENTE,true));
		sqlQueryObjectGet.addSelectField(this.getStampaFieldConverter().toColumn(Stampa.model().TIPO,true));
		sqlQueryObjectGet.setANDLogicOperator(true);
		sqlQueryObjectGet.addWhereCondition(this.getStampaFieldConverter().toTable(Stampa.model())+".id=?");
		sqlQueryObjectGet.addWhereCondition(this.getStampaFieldConverter().toTable(Stampa.model())+".id_versamento="+this.getStampaFieldConverter().toTable(Stampa.model().ID_VERSAMENTO)+".id");
		sqlQueryObjectGet.addWhereCondition(this.getStampaFieldConverter().toTable(Stampa.model().ID_VERSAMENTO.ID_APPLICAZIONE)+".id="+this.getStampaFieldConverter().toTable(Stampa.model().ID_VERSAMENTO)+".id_applicazione");

		// Recupero _stampa
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] searchParams_stampa = new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] { 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tableId,Long.class)
		};
		List<Class<?>> listaFieldIdReturnType_stampa = new ArrayList<Class<?>>();
		listaFieldIdReturnType_stampa.add(Stampa.model().ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE.getFieldType());
		listaFieldIdReturnType_stampa.add(Stampa.model().ID_VERSAMENTO.COD_VERSAMENTO_ENTE.getFieldType());
		listaFieldIdReturnType_stampa.add(Stampa.model().TIPO.getFieldType());
		it.govpay.orm.IdStampa id_stampa = null;
		List<Object> listaFieldId_stampa = jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet.createSQLQuery(), jdbcProperties.isShowSql(),
				listaFieldIdReturnType_stampa, searchParams_stampa);
		if(listaFieldId_stampa==null || listaFieldId_stampa.size()<=0){
			if(throwNotFound){
				throw new NotFoundException("Not Found");
			}
		}
		else{
			// set _stampa
			id_stampa = new it.govpay.orm.IdStampa();
			IdVersamento idVersamento = new IdVersamento();
			IdApplicazione idApplicazione = new IdApplicazione();
			idApplicazione.setCodApplicazione((String)listaFieldId_stampa.get(0));
			
			idVersamento.setIdApplicazione(idApplicazione);
			
			idVersamento.setCodVersamentoEnte((String)listaFieldId_stampa.get(1));
			id_stampa.setIdVersamento(idVersamento);
			id_stampa.setTipo((String)listaFieldId_stampa.get(2));
		}
		
		return id_stampa;
		
	}

	@Override
	public Long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdStampa id, boolean throwNotFound)
			throws NotFoundException, ServiceException, NotImplementedException, Exception {
	
		return this.findIdStampa(jdbcProperties,log,connection,sqlQueryObject,id,throwNotFound);
			
	}
	
	@Override
	public List<List<Object>> nativeQuery(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
											String sql,List<Class<?>> returnClassTypes,Object ... param) throws ServiceException,NotFoundException,NotImplementedException,Exception{
		
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.nativeQuery(jdbcProperties, log, connection, sqlQueryObject,
																							sql,returnClassTypes,param);
														
	}
	
	protected Long findIdStampa(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdStampa id, boolean throwNotFound) throws NotFoundException, ServiceException, NotImplementedException, Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);

		ISQLQueryObject sqlQueryObjectGet = sqlQueryObject.newSQLQueryObject();
		
		if(id.getId() != null && id.getId() > 0) {
			return id.getId();
		}
		
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] searchParams_stampa = null;
		
		if(id.getIdVersamento() != null && id.getIdVersamento().getId() != null && id.getIdVersamento().getId() > 0) {
			sqlQueryObjectGet.addFromTable(this.getStampaFieldConverter().toTable(Stampa.model()));
			sqlQueryObjectGet.addSelectField("id");
			sqlQueryObjectGet.setANDLogicOperator(true);
//			sqlQueryObjectGet.setSelectDistinct(true);
			sqlQueryObjectGet.addWhereCondition(this.getStampaFieldConverter().toColumn(Stampa.model().TIPO,true)+"=?");
			sqlQueryObjectGet.addWhereCondition("id_versamento=?");

			searchParams_stampa = new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] { 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id.getTipo(),Stampa.model().TIPO.getFieldType()),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id.getIdVersamento().getId(),Long.class)
			};
		} else if(id.getIdDocumento() != null && id.getIdDocumento().getId() != null && id.getIdDocumento().getId() > 0) {
			sqlQueryObjectGet.addFromTable(this.getStampaFieldConverter().toTable(Stampa.model()));
			sqlQueryObjectGet.addSelectField("id");
			sqlQueryObjectGet.setANDLogicOperator(true);
//				sqlQueryObjectGet.setSelectDistinct(true);
			sqlQueryObjectGet.addWhereCondition(this.getStampaFieldConverter().toColumn(Stampa.model().TIPO,true)+"=?");
			sqlQueryObjectGet.addWhereCondition("id_documento=?");

			searchParams_stampa = new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] { 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id.getTipo(),Stampa.model().TIPO.getFieldType()),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id.getIdDocumento().getId(),Long.class)
			};			
		} else {
			sqlQueryObjectGet.addFromTable(this.getStampaFieldConverter().toTable(Stampa.model()));
			sqlQueryObjectGet.addFromTable(this.getStampaFieldConverter().toTable(Stampa.model().ID_VERSAMENTO));
			sqlQueryObjectGet.addFromTable(this.getStampaFieldConverter().toTable(Stampa.model().ID_VERSAMENTO.ID_APPLICAZIONE));
			sqlQueryObjectGet.addSelectField(this.getStampaFieldConverter().toTable(Stampa.model())+".id");
			sqlQueryObjectGet.setANDLogicOperator(true);
//			sqlQueryObjectGet.setSelectDistinct(true);
			sqlQueryObjectGet.addWhereCondition(this.getStampaFieldConverter().toColumn(Stampa.model().ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE,true)+"=?");
			sqlQueryObjectGet.addWhereCondition(this.getStampaFieldConverter().toColumn(Stampa.model().ID_VERSAMENTO.COD_VERSAMENTO_ENTE,true)+"=?");
			sqlQueryObjectGet.addWhereCondition(this.getStampaFieldConverter().toColumn(Stampa.model().TIPO,true)+"=?");
			sqlQueryObjectGet.addWhereCondition(this.getStampaFieldConverter().toTable(Stampa.model())+".id_versamento="+this.getStampaFieldConverter().toTable(Stampa.model().ID_VERSAMENTO)+".id");
			sqlQueryObjectGet.addWhereCondition(this.getStampaFieldConverter().toTable(Stampa.model().ID_VERSAMENTO)+".id_applicazione="+this.getStampaFieldConverter().toTable(Stampa.model().ID_VERSAMENTO.ID_APPLICAZIONE)+".id");

			searchParams_stampa = new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] { 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id.getIdVersamento().getIdApplicazione().getCodApplicazione(),Stampa.model().ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE.getFieldType()),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id.getIdVersamento().getCodVersamentoEnte(),Stampa.model().ID_VERSAMENTO.COD_VERSAMENTO_ENTE.getFieldType()),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id.getTipo(),Stampa.model().TIPO.getFieldType()),
			};
		}
		Long id_stampa = null;
		try{
			id_stampa = (Long) jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet.createSQLQuery(), jdbcProperties.isShowSql(),
						Long.class, searchParams_stampa);
		}catch(NotFoundException notFound){
			if(throwNotFound){
				throw new NotFoundException(notFound);
			}
		}
		if(id_stampa==null || id_stampa<=0){
			if(throwNotFound){
				throw new NotFoundException("Not Found");
			}
		}
		
		return id_stampa;
	}
}
