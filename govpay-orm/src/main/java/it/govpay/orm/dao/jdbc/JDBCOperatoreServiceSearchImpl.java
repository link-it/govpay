/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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
package it.govpay.orm.dao.jdbc;

import it.govpay.orm.IdOperatore;
import it.govpay.orm.Operatore;
import it.govpay.orm.OperatoreApplicazione;
import it.govpay.orm.OperatorePortale;
import it.govpay.orm.OperatoreUo;
import it.govpay.orm.dao.jdbc.converter.OperatoreFieldConverter;
import it.govpay.orm.dao.jdbc.fetch.OperatoreFetch;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
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

/**     
 * JDBCOperatoreServiceSearchImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCOperatoreServiceSearchImpl implements IJDBCServiceSearchWithId<Operatore, IdOperatore, JDBCServiceManager> {

	private OperatoreFieldConverter _operatoreFieldConverter = null;
	public OperatoreFieldConverter getOperatoreFieldConverter() {
		if(this._operatoreFieldConverter==null){
			this._operatoreFieldConverter = new OperatoreFieldConverter(this.jdbcServiceManager.getJdbcProperties().getDatabaseType());
		}		
		return this._operatoreFieldConverter;
	}
	@Override
	public ISQLFieldConverter getFieldConverter() {
		return this.getOperatoreFieldConverter();
	}
	
	private OperatoreFetch operatoreFetch = new OperatoreFetch();
	public OperatoreFetch getOperatoreFetch() {
		return this.operatoreFetch;
	}
	@Override
	public IJDBCFetch getFetch() {
		return getOperatoreFetch();
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
	public IdOperatore convertToId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Operatore operatore) throws NotImplementedException, ServiceException, Exception{
	
		IdOperatore idOperatore = new IdOperatore();
		idOperatore.setPrincipal(operatore.getPrincipal());
	
		return idOperatore;
	}
	
	@Override
	public Operatore get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdOperatore id, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException,Exception {
		Long id_operatore = ( (id!=null && id.getId()!=null && id.getId()>0) ? id.getId() : this.findIdOperatore(jdbcProperties, log, connection, sqlQueryObject, id, true));
		return this._get(jdbcProperties, log, connection, sqlQueryObject, id_operatore,idMappingResolutionBehaviour);
		
		
	}
	
	@Override
	public boolean exists(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdOperatore id) throws MultipleResultException, NotImplementedException, ServiceException,Exception {

		Long id_operatore = this.findIdOperatore(jdbcProperties, log, connection, sqlQueryObject, id, false);
		return id_operatore != null && id_operatore > 0;
		
	}
	
	@Override
	public List<IdOperatore> findAllIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {

		// default behaviour (id-mapping)
        if(idMappingResolutionBehaviour==null){
                idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
        }
		List<IdOperatore> list = new ArrayList<IdOperatore>();

		try{
			List<IField> fields = new ArrayList<IField>();
			fields.add(Operatore.model().PRINCIPAL);

			List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));
        
			for(Map<String, Object> map: returnMap) {
				list.add(this.convertToId(jdbcProperties, log, connection, sqlQueryObject, (Operatore)this.getOperatoreFetch().fetch(jdbcProperties.getDatabase(), Operatore.model(), map)));
	        }
		} catch(NotFoundException e) {}

        return list;
		
	}
	
	@Override
	public List<Operatore> findAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {

		// default behaviour (id-mapping)
        if(idMappingResolutionBehaviour==null){
                idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
        }
		List<Operatore> list = new ArrayList<Operatore>();

		try{
			List<IField> fields = new ArrayList<IField>();
			fields.add(new CustomField("id", Long.class, "id", this.getOperatoreFieldConverter().toTable(Operatore.model())));

			fields.add(Operatore.model().PRINCIPAL);
			fields.add(Operatore.model().NOME);
			fields.add(Operatore.model().PROFILO);
			fields.add(Operatore.model().ABILITATO);

			List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));
        
			org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
					new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		

			for(Map<String, Object> map: returnMap) {
				
				Operatore operatore = (Operatore)this.getOperatoreFetch().fetch(jdbcProperties.getDatabase(), Operatore.model(), map);
				
				// Object operatore_operatoreUo
				ISQLQueryObject sqlQueryObjectGet_operatore_operatoreUo = sqlQueryObject.newSQLQueryObject();
				sqlQueryObjectGet_operatore_operatoreUo.setANDLogicOperator(true);
				sqlQueryObjectGet_operatore_operatoreUo.addFromTable(this.getOperatoreFieldConverter().toTable(Operatore.model().OPERATORE_UO));
				sqlQueryObjectGet_operatore_operatoreUo.addSelectField("id");
				sqlQueryObjectGet_operatore_operatoreUo.addWhereCondition("id_operatore=?");

				// Get operatore_operatoreUo
				java.util.List<Object> operatore_operatoreUo_list = (java.util.List<Object>) jdbcUtilities.executeQuery(sqlQueryObjectGet_operatore_operatoreUo.createSQLQuery(), jdbcProperties.isShowSql(), Operatore.model().OPERATORE_UO, this.getOperatoreFetch(),
					new JDBCObject(operatore.getId(),Long.class));

				if(operatore_operatoreUo_list != null) {
					for (Object operatore_operatoreUo_object: operatore_operatoreUo_list) {
						OperatoreUo operatore_operatoreUo = (OperatoreUo) operatore_operatoreUo_object;


						if(idMappingResolutionBehaviour==null ||
							(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
						){
							// Object _operatore_operatoreUo_uo (recupero id)
							ISQLQueryObject sqlQueryObjectGet_operatore_operatoreUo_uo_readFkId = sqlQueryObject.newSQLQueryObject();
							sqlQueryObjectGet_operatore_operatoreUo_uo_readFkId.addFromTable(this.getOperatoreFieldConverter().toTable(it.govpay.orm.Operatore.model().OPERATORE_UO));
							sqlQueryObjectGet_operatore_operatoreUo_uo_readFkId.addSelectField("id_uo");
							sqlQueryObjectGet_operatore_operatoreUo_uo_readFkId.addWhereCondition("id=?");
							sqlQueryObjectGet_operatore_operatoreUo_uo_readFkId.setANDLogicOperator(true);
							Long idFK_operatore_operatoreUo_uo = (Long) jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet_operatore_operatoreUo_uo_readFkId.createSQLQuery(), jdbcProperties.isShowSql(),Long.class,
									new JDBCObject(operatore_operatoreUo.getId(),Long.class));
							
							it.govpay.orm.IdUo id_operatore_operatoreUo_uo = null;
							if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
								id_operatore_operatoreUo_uo = ((JDBCUoServiceSearch)(this.getServiceManager().getUoServiceSearch())).findId(idFK_operatore_operatoreUo_uo, false);
							}else{
								id_operatore_operatoreUo_uo = new it.govpay.orm.IdUo();
							}
							id_operatore_operatoreUo_uo.setId(idFK_operatore_operatoreUo_uo);
							operatore_operatoreUo.setIdUo(id_operatore_operatoreUo_uo);
						}

						operatore.addOperatoreUo(operatore_operatoreUo);
					}
				}

				// Object operatore_operatoreApplicazione
				ISQLQueryObject sqlQueryObjectGet_operatore_operatoreApplicazione = sqlQueryObject.newSQLQueryObject();
				sqlQueryObjectGet_operatore_operatoreApplicazione.setANDLogicOperator(true);
				sqlQueryObjectGet_operatore_operatoreApplicazione.addFromTable(this.getOperatoreFieldConverter().toTable(Operatore.model().OPERATORE_APPLICAZIONE));
				sqlQueryObjectGet_operatore_operatoreApplicazione.addSelectField("id");
				sqlQueryObjectGet_operatore_operatoreApplicazione.addWhereCondition("id_operatore=?");

				// Get operatore_operatoreApplicazione
				java.util.List<Object> operatore_operatoreApplicazione_list = (java.util.List<Object>) jdbcUtilities.executeQuery(sqlQueryObjectGet_operatore_operatoreApplicazione.createSQLQuery(), jdbcProperties.isShowSql(), Operatore.model().OPERATORE_APPLICAZIONE, this.getOperatoreFetch(),
					new JDBCObject(operatore.getId(),Long.class));

				if(operatore_operatoreApplicazione_list != null) {
					for (Object operatore_operatoreApplicazione_object: operatore_operatoreApplicazione_list) {
						OperatoreApplicazione operatore_operatoreApplicazione = (OperatoreApplicazione) operatore_operatoreApplicazione_object;


						if(idMappingResolutionBehaviour==null ||
							(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
						){
							// Object _operatore_operatoreApplicazione_applicazione (recupero id)
							ISQLQueryObject sqlQueryObjectGet_operatore_operatoreApplicazione_applicazione_readFkId = sqlQueryObject.newSQLQueryObject();
							sqlQueryObjectGet_operatore_operatoreApplicazione_applicazione_readFkId.addFromTable(this.getOperatoreFieldConverter().toTable(it.govpay.orm.Operatore.model().OPERATORE_APPLICAZIONE));
							sqlQueryObjectGet_operatore_operatoreApplicazione_applicazione_readFkId.addSelectField("id_applicazione");
							sqlQueryObjectGet_operatore_operatoreApplicazione_applicazione_readFkId.addWhereCondition("id=?");
							sqlQueryObjectGet_operatore_operatoreApplicazione_applicazione_readFkId.setANDLogicOperator(true);
							Long idFK_operatore_operatoreApplicazione_applicazione = (Long) jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet_operatore_operatoreApplicazione_applicazione_readFkId.createSQLQuery(), jdbcProperties.isShowSql(),Long.class,
									new JDBCObject(operatore_operatoreApplicazione.getId(),Long.class));
							
							it.govpay.orm.IdApplicazione id_operatore_operatoreApplicazione_applicazione = null;
							if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
								id_operatore_operatoreApplicazione_applicazione = ((JDBCApplicazioneServiceSearch)(this.getServiceManager().getApplicazioneServiceSearch())).findId(idFK_operatore_operatoreApplicazione_applicazione, false);
							}else{
								id_operatore_operatoreApplicazione_applicazione = new it.govpay.orm.IdApplicazione();
							}
							id_operatore_operatoreApplicazione_applicazione.setId(idFK_operatore_operatoreApplicazione_applicazione);
							operatore_operatoreApplicazione.setIdApplicazione(id_operatore_operatoreApplicazione_applicazione);
						}

						operatore.addOperatoreApplicazione(operatore_operatoreApplicazione);
					}
				}

				// Object operatore_operatorePortale
				ISQLQueryObject sqlQueryObjectGet_operatore_operatorePortale = sqlQueryObject.newSQLQueryObject();
				sqlQueryObjectGet_operatore_operatorePortale.setANDLogicOperator(true);
				sqlQueryObjectGet_operatore_operatorePortale.addFromTable(this.getOperatoreFieldConverter().toTable(Operatore.model().OPERATORE_PORTALE));
				sqlQueryObjectGet_operatore_operatorePortale.addSelectField("id");
				sqlQueryObjectGet_operatore_operatorePortale.addWhereCondition("id_operatore=?");

				// Get operatore_operatorePortale
				java.util.List<Object> operatore_operatorePortale_list = (java.util.List<Object>) jdbcUtilities.executeQuery(sqlQueryObjectGet_operatore_operatorePortale.createSQLQuery(), jdbcProperties.isShowSql(), Operatore.model().OPERATORE_PORTALE, this.getOperatoreFetch(),
					new JDBCObject(operatore.getId(),Long.class));

				if(operatore_operatorePortale_list != null) {
					for (Object operatore_operatorePortale_object: operatore_operatorePortale_list) {
						OperatorePortale operatore_operatorePortale = (OperatorePortale) operatore_operatorePortale_object;


						if(idMappingResolutionBehaviour==null ||
							(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
						){
							// Object _operatore_operatorePortale_portale (recupero id)
							ISQLQueryObject sqlQueryObjectGet_operatore_operatorePortale_portale_readFkId = sqlQueryObject.newSQLQueryObject();
							sqlQueryObjectGet_operatore_operatorePortale_portale_readFkId.addFromTable(this.getOperatoreFieldConverter().toTable(it.govpay.orm.Operatore.model().OPERATORE_PORTALE));
							sqlQueryObjectGet_operatore_operatorePortale_portale_readFkId.addSelectField("id_portale");
							sqlQueryObjectGet_operatore_operatorePortale_portale_readFkId.addWhereCondition("id=?");
							sqlQueryObjectGet_operatore_operatorePortale_portale_readFkId.setANDLogicOperator(true);
							Long idFK_operatore_operatorePortale_portale = (Long) jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet_operatore_operatorePortale_portale_readFkId.createSQLQuery(), jdbcProperties.isShowSql(),Long.class,
									new JDBCObject(operatore_operatorePortale.getId(),Long.class));
							
							it.govpay.orm.IdPortale id_operatore_operatorePortale_portale = null;
							if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
								id_operatore_operatorePortale_portale = ((JDBCPortaleServiceSearch)(this.getServiceManager().getPortaleServiceSearch())).findId(idFK_operatore_operatorePortale_portale, false);
							}else{
								id_operatore_operatorePortale_portale = new it.govpay.orm.IdPortale();
							}
							id_operatore_operatorePortale_portale.setId(idFK_operatore_operatorePortale_portale);
							operatore_operatorePortale.setIdPortale(id_operatore_operatorePortale_portale);
						}

						operatore.addOperatorePortale(operatore_operatorePortale);
					}
				}

				list.add(operatore);
	        }
		} catch(NotFoundException e) {}

        return list;     
		
	}
	
	@Override
	public Operatore find(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) 
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
												this.getOperatoreFieldConverter(), Operatore.model());
		
		sqlQueryObject.addSelectCountField(this.getOperatoreFieldConverter().toTable(Operatore.model())+".id","tot",true);
		
		_join(expression,sqlQueryObject);
		
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.count(jdbcProperties, log, connection, sqlQueryObject, expression,
																			this.getOperatoreFieldConverter(), Operatore.model(),listaQuery);
	}

	@Override
	public InUse inUse(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdOperatore id) throws NotFoundException, NotImplementedException, ServiceException,Exception {
		
		Long id_operatore = this.findIdOperatore(jdbcProperties, log, connection, sqlQueryObject, id, true);
        return this._inUse(jdbcProperties, log, connection, sqlQueryObject, id_operatore);
		
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
												this.getOperatoreFieldConverter(), field);

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
        						expression, this.getOperatoreFieldConverter(), Operatore.model(), 
        						listaQuery,listaParams);
		
		_join(expression,sqlQueryObject);
        
        List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.select(jdbcProperties, log, connection,
        								org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareSqlQueryObjectForSelectDistinct(sqlQueryObject,sqlQueryObjectDistinct), 
        								expression, this.getOperatoreFieldConverter(), Operatore.model(),
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
        						this.getOperatoreFieldConverter(), Operatore.model(), 
        						sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);
		
		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				_join(expression,sqlQueryObjectInnerList.get(i));
			}
		}
        
        List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.union(jdbcProperties, log, connection, sqlQueryObject, 
        								this.getOperatoreFieldConverter(), Operatore.model(), 
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
        						this.getOperatoreFieldConverter(), Operatore.model(), 
        						sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);
		
		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				_join(expression,sqlQueryObjectInnerList.get(i));
			}
		}
        
        NonNegativeNumber number = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.unionCount(jdbcProperties, log, connection, sqlQueryObject, 
        								this.getOperatoreFieldConverter(), Operatore.model(), 
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
			return new JDBCExpression(this.getOperatoreFieldConverter());
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}


	@Override
	public JDBCPaginatedExpression newPaginatedExpression(Logger log) throws NotImplementedException, ServiceException {
		try{
			return new JDBCPaginatedExpression(this.getOperatoreFieldConverter());
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
	public void mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdOperatore id, Operatore obj) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		_mappingTableIds(jdbcProperties,log,connection,sqlQueryObject,obj,
				this.get(jdbcProperties,log,connection,sqlQueryObject,id,null));
	}
	
	@Override
	public void mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Operatore obj) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		_mappingTableIds(jdbcProperties,log,connection,sqlQueryObject,obj,
				this.get(jdbcProperties,log,connection,sqlQueryObject,tableId,null));
	}
	private void _mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Operatore obj, Operatore imgSaved) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		if(imgSaved==null){
			return;
		}
		obj.setId(imgSaved.getId());
		if(obj.getOperatoreUoList()!=null){
			List<it.govpay.orm.OperatoreUo> listObj_ = obj.getOperatoreUoList();
			for(it.govpay.orm.OperatoreUo itemObj_ : listObj_){
				it.govpay.orm.OperatoreUo itemAlreadySaved_ = null;
				if(imgSaved.getOperatoreUoList()!=null){
					List<it.govpay.orm.OperatoreUo> listImgSaved_ = imgSaved.getOperatoreUoList();
					for(it.govpay.orm.OperatoreUo itemImgSaved_ : listImgSaved_){
						boolean objEqualsToImgSaved_ = false;
						objEqualsToImgSaved_ = org.openspcoop2.generic_project.utils.Utilities.equals(itemObj_.getIdUo(),itemImgSaved_.getIdUo());
						if(objEqualsToImgSaved_){
							itemAlreadySaved_=itemImgSaved_;
							break;
						}
					}
				}
				if(itemAlreadySaved_!=null){
					itemObj_.setId(itemAlreadySaved_.getId());
					if(itemObj_.getIdUo()!=null && 
							itemAlreadySaved_.getIdUo()!=null){
						itemObj_.getIdUo().setId(itemAlreadySaved_.getIdUo().getId());
						if(itemObj_.getIdUo().getIdDominio()!=null && 
								itemAlreadySaved_.getIdUo().getIdDominio()!=null){
							itemObj_.getIdUo().getIdDominio().setId(itemAlreadySaved_.getIdUo().getIdDominio().getId());
						}
					}
				}
			}
		}
		if(obj.getOperatoreApplicazioneList()!=null){
			List<it.govpay.orm.OperatoreApplicazione> listObj_ = obj.getOperatoreApplicazioneList();
			for(it.govpay.orm.OperatoreApplicazione itemObj_ : listObj_){
				it.govpay.orm.OperatoreApplicazione itemAlreadySaved_ = null;
				if(imgSaved.getOperatoreApplicazioneList()!=null){
					List<it.govpay.orm.OperatoreApplicazione> listImgSaved_ = imgSaved.getOperatoreApplicazioneList();
					for(it.govpay.orm.OperatoreApplicazione itemImgSaved_ : listImgSaved_){
						boolean objEqualsToImgSaved_ = false;
						objEqualsToImgSaved_ = org.openspcoop2.generic_project.utils.Utilities.equals(itemObj_.getIdApplicazione(),itemImgSaved_.getIdApplicazione());
						if(objEqualsToImgSaved_){
							itemAlreadySaved_=itemImgSaved_;
							break;
						}
					}
				}
				if(itemAlreadySaved_!=null){
					itemObj_.setId(itemAlreadySaved_.getId());
					if(itemObj_.getIdApplicazione()!=null && 
							itemAlreadySaved_.getIdApplicazione()!=null){
						itemObj_.getIdApplicazione().setId(itemAlreadySaved_.getIdApplicazione().getId());
					}
				}
			}
		}
		if(obj.getOperatorePortaleList()!=null){
			List<it.govpay.orm.OperatorePortale> listObj_ = obj.getOperatorePortaleList();
			for(it.govpay.orm.OperatorePortale itemObj_ : listObj_){
				it.govpay.orm.OperatorePortale itemAlreadySaved_ = null;
				if(imgSaved.getOperatorePortaleList()!=null){
					List<it.govpay.orm.OperatorePortale> listImgSaved_ = imgSaved.getOperatorePortaleList();
					for(it.govpay.orm.OperatorePortale itemImgSaved_ : listImgSaved_){
						boolean objEqualsToImgSaved_ = false;
						objEqualsToImgSaved_ = org.openspcoop2.generic_project.utils.Utilities.equals(itemObj_.getIdPortale(),itemImgSaved_.getIdPortale());
						if(objEqualsToImgSaved_){
							itemAlreadySaved_=itemImgSaved_;
							break;
						}
					}
				}
				if(itemAlreadySaved_!=null){
					itemObj_.setId(itemAlreadySaved_.getId());
					if(itemObj_.getIdPortale()!=null && 
							itemAlreadySaved_.getIdPortale()!=null){
						itemObj_.getIdPortale().setId(itemAlreadySaved_.getIdPortale().getId());
					}
				}
			}
		}

	}
	
	@Override
	public Operatore get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
		return this._get(jdbcProperties, log, connection, sqlQueryObject, Long.valueOf(tableId), idMappingResolutionBehaviour);
	}
	
	private Operatore _get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
	
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
					new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectGet = sqlQueryObject.newSQLQueryObject();
				
		Operatore operatore = new Operatore();
		

		// Object operatore
		ISQLQueryObject sqlQueryObjectGet_operatore = sqlQueryObjectGet.newSQLQueryObject();
		sqlQueryObjectGet_operatore.setANDLogicOperator(true);
		sqlQueryObjectGet_operatore.addFromTable(this.getOperatoreFieldConverter().toTable(Operatore.model()));
		sqlQueryObjectGet_operatore.addSelectField("id");
		sqlQueryObjectGet_operatore.addSelectField(this.getOperatoreFieldConverter().toColumn(Operatore.model().PRINCIPAL,true));
		sqlQueryObjectGet_operatore.addSelectField(this.getOperatoreFieldConverter().toColumn(Operatore.model().NOME,true));
		sqlQueryObjectGet_operatore.addSelectField(this.getOperatoreFieldConverter().toColumn(Operatore.model().PROFILO,true));
		sqlQueryObjectGet_operatore.addSelectField(this.getOperatoreFieldConverter().toColumn(Operatore.model().ABILITATO,true));
		sqlQueryObjectGet_operatore.addWhereCondition("id=?");

		// Get operatore
		operatore = (Operatore) jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet_operatore.createSQLQuery(), jdbcProperties.isShowSql(), Operatore.model(), this.getOperatoreFetch(),
			new JDBCObject(tableId,Long.class));



		// Object operatore_operatoreUo
		ISQLQueryObject sqlQueryObjectGet_operatore_operatoreUo = sqlQueryObjectGet.newSQLQueryObject();
		sqlQueryObjectGet_operatore_operatoreUo.setANDLogicOperator(true);
		sqlQueryObjectGet_operatore_operatoreUo.addFromTable(this.getOperatoreFieldConverter().toTable(Operatore.model().OPERATORE_UO));
		sqlQueryObjectGet_operatore_operatoreUo.addSelectField("id");
		sqlQueryObjectGet_operatore_operatoreUo.addWhereCondition("id_operatore=?");

		// Get operatore_operatoreUo
		java.util.List<Object> operatore_operatoreUo_list = (java.util.List<Object>) jdbcUtilities.executeQuery(sqlQueryObjectGet_operatore_operatoreUo.createSQLQuery(), jdbcProperties.isShowSql(), Operatore.model().OPERATORE_UO, this.getOperatoreFetch(),
			new JDBCObject(operatore.getId(),Long.class));

		if(operatore_operatoreUo_list != null) {
			for (Object operatore_operatoreUo_object: operatore_operatoreUo_list) {
				OperatoreUo operatore_operatoreUo = (OperatoreUo) operatore_operatoreUo_object;


				if(idMappingResolutionBehaviour==null ||
					(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
				){
					// Object _operatore_operatoreUo_uo (recupero id)
					ISQLQueryObject sqlQueryObjectGet_operatore_operatoreUo_uo_readFkId = sqlQueryObjectGet.newSQLQueryObject();
					sqlQueryObjectGet_operatore_operatoreUo_uo_readFkId.addFromTable(this.getOperatoreFieldConverter().toTable(it.govpay.orm.Operatore.model().OPERATORE_UO));
					sqlQueryObjectGet_operatore_operatoreUo_uo_readFkId.addSelectField("id_uo");
					sqlQueryObjectGet_operatore_operatoreUo_uo_readFkId.addWhereCondition("id=?");
					sqlQueryObjectGet_operatore_operatoreUo_uo_readFkId.setANDLogicOperator(true);
					Long idFK_operatore_operatoreUo_uo = (Long) jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet_operatore_operatoreUo_uo_readFkId.createSQLQuery(), jdbcProperties.isShowSql(),Long.class,
							new JDBCObject(operatore_operatoreUo.getId(),Long.class));
					
					it.govpay.orm.IdUo id_operatore_operatoreUo_uo = null;
					if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
						id_operatore_operatoreUo_uo = ((JDBCUoServiceSearch)(this.getServiceManager().getUoServiceSearch())).findId(idFK_operatore_operatoreUo_uo, false);
					}else{
						id_operatore_operatoreUo_uo = new it.govpay.orm.IdUo();
					}
					id_operatore_operatoreUo_uo.setId(idFK_operatore_operatoreUo_uo);
					operatore_operatoreUo.setIdUo(id_operatore_operatoreUo_uo);
				}

				operatore.addOperatoreUo(operatore_operatoreUo);
			}
		}

		// Object operatore_operatoreApplicazione
		ISQLQueryObject sqlQueryObjectGet_operatore_operatoreApplicazione = sqlQueryObjectGet.newSQLQueryObject();
		sqlQueryObjectGet_operatore_operatoreApplicazione.setANDLogicOperator(true);
		sqlQueryObjectGet_operatore_operatoreApplicazione.addFromTable(this.getOperatoreFieldConverter().toTable(Operatore.model().OPERATORE_APPLICAZIONE));
		sqlQueryObjectGet_operatore_operatoreApplicazione.addSelectField("id");
		sqlQueryObjectGet_operatore_operatoreApplicazione.addWhereCondition("id_operatore=?");

		// Get operatore_operatoreApplicazione
		java.util.List<Object> operatore_operatoreApplicazione_list = (java.util.List<Object>) jdbcUtilities.executeQuery(sqlQueryObjectGet_operatore_operatoreApplicazione.createSQLQuery(), jdbcProperties.isShowSql(), Operatore.model().OPERATORE_APPLICAZIONE, this.getOperatoreFetch(),
			new JDBCObject(operatore.getId(),Long.class));

		if(operatore_operatoreApplicazione_list != null) {
			for (Object operatore_operatoreApplicazione_object: operatore_operatoreApplicazione_list) {
				OperatoreApplicazione operatore_operatoreApplicazione = (OperatoreApplicazione) operatore_operatoreApplicazione_object;


				if(idMappingResolutionBehaviour==null ||
					(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
				){
					// Object _operatore_operatoreApplicazione_applicazione (recupero id)
					ISQLQueryObject sqlQueryObjectGet_operatore_operatoreApplicazione_applicazione_readFkId = sqlQueryObjectGet.newSQLQueryObject();
					sqlQueryObjectGet_operatore_operatoreApplicazione_applicazione_readFkId.addFromTable(this.getOperatoreFieldConverter().toTable(it.govpay.orm.Operatore.model().OPERATORE_APPLICAZIONE));
					sqlQueryObjectGet_operatore_operatoreApplicazione_applicazione_readFkId.addSelectField("id_applicazione");
					sqlQueryObjectGet_operatore_operatoreApplicazione_applicazione_readFkId.addWhereCondition("id=?");
					sqlQueryObjectGet_operatore_operatoreApplicazione_applicazione_readFkId.setANDLogicOperator(true);
					Long idFK_operatore_operatoreApplicazione_applicazione = (Long) jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet_operatore_operatoreApplicazione_applicazione_readFkId.createSQLQuery(), jdbcProperties.isShowSql(),Long.class,
							new JDBCObject(operatore_operatoreApplicazione.getId(),Long.class));
					
					it.govpay.orm.IdApplicazione id_operatore_operatoreApplicazione_applicazione = null;
					if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
						id_operatore_operatoreApplicazione_applicazione = ((JDBCApplicazioneServiceSearch)(this.getServiceManager().getApplicazioneServiceSearch())).findId(idFK_operatore_operatoreApplicazione_applicazione, false);
					}else{
						id_operatore_operatoreApplicazione_applicazione = new it.govpay.orm.IdApplicazione();
					}
					id_operatore_operatoreApplicazione_applicazione.setId(idFK_operatore_operatoreApplicazione_applicazione);
					operatore_operatoreApplicazione.setIdApplicazione(id_operatore_operatoreApplicazione_applicazione);
				}

				operatore.addOperatoreApplicazione(operatore_operatoreApplicazione);
			}
		}

		// Object operatore_operatorePortale
		ISQLQueryObject sqlQueryObjectGet_operatore_operatorePortale = sqlQueryObjectGet.newSQLQueryObject();
		sqlQueryObjectGet_operatore_operatorePortale.setANDLogicOperator(true);
		sqlQueryObjectGet_operatore_operatorePortale.addFromTable(this.getOperatoreFieldConverter().toTable(Operatore.model().OPERATORE_PORTALE));
		sqlQueryObjectGet_operatore_operatorePortale.addSelectField("id");
		sqlQueryObjectGet_operatore_operatorePortale.addWhereCondition("id_operatore=?");

		// Get operatore_operatorePortale
		java.util.List<Object> operatore_operatorePortale_list = (java.util.List<Object>) jdbcUtilities.executeQuery(sqlQueryObjectGet_operatore_operatorePortale.createSQLQuery(), jdbcProperties.isShowSql(), Operatore.model().OPERATORE_PORTALE, this.getOperatoreFetch(),
			new JDBCObject(operatore.getId(),Long.class));

		if(operatore_operatorePortale_list != null) {
			for (Object operatore_operatorePortale_object: operatore_operatorePortale_list) {
				OperatorePortale operatore_operatorePortale = (OperatorePortale) operatore_operatorePortale_object;


				if(idMappingResolutionBehaviour==null ||
					(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
				){
					// Object _operatore_operatorePortale_portale (recupero id)
					ISQLQueryObject sqlQueryObjectGet_operatore_operatorePortale_portale_readFkId = sqlQueryObjectGet.newSQLQueryObject();
					sqlQueryObjectGet_operatore_operatorePortale_portale_readFkId.addFromTable(this.getOperatoreFieldConverter().toTable(it.govpay.orm.Operatore.model().OPERATORE_PORTALE));
					sqlQueryObjectGet_operatore_operatorePortale_portale_readFkId.addSelectField("id_portale");
					sqlQueryObjectGet_operatore_operatorePortale_portale_readFkId.addWhereCondition("id=?");
					sqlQueryObjectGet_operatore_operatorePortale_portale_readFkId.setANDLogicOperator(true);
					Long idFK_operatore_operatorePortale_portale = (Long) jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet_operatore_operatorePortale_portale_readFkId.createSQLQuery(), jdbcProperties.isShowSql(),Long.class,
							new JDBCObject(operatore_operatorePortale.getId(),Long.class));
					
					it.govpay.orm.IdPortale id_operatore_operatorePortale_portale = null;
					if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
						id_operatore_operatorePortale_portale = ((JDBCPortaleServiceSearch)(this.getServiceManager().getPortaleServiceSearch())).findId(idFK_operatore_operatorePortale_portale, false);
					}else{
						id_operatore_operatorePortale_portale = new it.govpay.orm.IdPortale();
					}
					id_operatore_operatorePortale_portale.setId(idFK_operatore_operatorePortale_portale);
					operatore_operatorePortale.setIdPortale(id_operatore_operatorePortale_portale);
				}

				operatore.addOperatorePortale(operatore_operatorePortale);
			}
		}

        return operatore;  
	
	} 
	
	@Override
	public boolean exists(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId) throws MultipleResultException, NotImplementedException, ServiceException, Exception {
		return this._exists(jdbcProperties, log, connection, sqlQueryObject, Long.valueOf(tableId));
	}
	
	private boolean _exists(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long tableId) throws MultipleResultException, NotImplementedException, ServiceException, Exception {
	
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
					new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
				
		boolean existsOperatore = false;

		sqlQueryObject = sqlQueryObject.newSQLQueryObject();
		sqlQueryObject.setANDLogicOperator(true);

		sqlQueryObject.addFromTable(this.getOperatoreFieldConverter().toTable(Operatore.model()));
		sqlQueryObject.addSelectField(this.getOperatoreFieldConverter().toColumn(Operatore.model().PRINCIPAL,true));
		sqlQueryObject.addWhereCondition("id=?");


		// Exists operatore
		existsOperatore = jdbcUtilities.exists(sqlQueryObject.createSQLQuery(), jdbcProperties.isShowSql(),
			new JDBCObject(tableId,Long.class));

		
        return existsOperatore;
	
	}
	
	private void _join(IExpression expression, ISQLQueryObject sqlQueryObject) throws NotImplementedException, ServiceException, Exception{
	
		if(expression.inUseModel(Operatore.model().OPERATORE_UO,false)){
			String tableName1 = this.getOperatoreFieldConverter().toAliasTable(Operatore.model());
			String tableName2 = this.getOperatoreFieldConverter().toAliasTable(Operatore.model().OPERATORE_UO);
			sqlQueryObject.addWhereCondition(tableName1+".id="+tableName2+".id_operatore");
			
	        if(expression.inUseModel(Operatore.model().OPERATORE_UO.ID_UO,false)){
				if(expression.inUseModel(Operatore.model().OPERATORE_UO,false)==false){
					sqlQueryObject.addFromTable(this.getOperatoreFieldConverter().toTable(Operatore.model().OPERATORE_UO));
				}
				String tableName3 = this.getOperatoreFieldConverter().toAliasTable(Operatore.model().OPERATORE_UO.ID_UO);
				sqlQueryObject.addWhereCondition(tableName3+".id="+tableName2+".id_uo");

			}

		}
		
		if(expression.inUseModel(Operatore.model().OPERATORE_PORTALE,false)){
			String tableName1 = this.getOperatoreFieldConverter().toAliasTable(Operatore.model());
			String tableName2 = this.getOperatoreFieldConverter().toAliasTable(Operatore.model().OPERATORE_PORTALE);
			sqlQueryObject.addWhereCondition(tableName1+".id="+tableName2+".id_operatore");
			
	        if(expression.inUseModel(Operatore.model().OPERATORE_PORTALE.ID_PORTALE,false)){
				if(expression.inUseModel(Operatore.model().OPERATORE_PORTALE,false)==false){
					sqlQueryObject.addFromTable(this.getOperatoreFieldConverter().toTable(Operatore.model().OPERATORE_PORTALE));
				}
				String tableName3 = this.getOperatoreFieldConverter().toAliasTable(Operatore.model().OPERATORE_PORTALE.ID_PORTALE);
				sqlQueryObject.addWhereCondition(tableName3+".id="+tableName2+".id_portale");

			}

		}
		
		if(expression.inUseModel(Operatore.model().OPERATORE_APPLICAZIONE,false)){
			String tableName1 = this.getOperatoreFieldConverter().toAliasTable(Operatore.model());
			String tableName2 = this.getOperatoreFieldConverter().toAliasTable(Operatore.model().OPERATORE_APPLICAZIONE);
			sqlQueryObject.addWhereCondition(tableName1+".id="+tableName2+".id_operatore");
			
	        if(expression.inUseModel(Operatore.model().OPERATORE_APPLICAZIONE.ID_APPLICAZIONE,false)){
				if(expression.inUseModel(Operatore.model().OPERATORE_APPLICAZIONE,false)==false){
					sqlQueryObject.addFromTable(this.getOperatoreFieldConverter().toTable(Operatore.model().OPERATORE_APPLICAZIONE));
				}
				String tableName3 = this.getOperatoreFieldConverter().toAliasTable(Operatore.model().OPERATORE_APPLICAZIONE.ID_APPLICAZIONE);
				sqlQueryObject.addWhereCondition(tableName3+".id="+tableName2+".id_applicazione");

			}

		}
        
	}
	
	protected java.util.List<Object> _getRootTablePrimaryKeyValues(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdOperatore id) throws NotFoundException, ServiceException, NotImplementedException, Exception{
	    // Identificativi
        java.util.List<Object> rootTableIdValues = new java.util.ArrayList<Object>();
		Long longId = this.findIdOperatore(jdbcProperties, log, connection, sqlQueryObject.newSQLQueryObject(), id, true);
		rootTableIdValues.add(longId);
        
        return rootTableIdValues;
	}
	
	protected Map<String, List<IField>> _getMapTableToPKColumn() throws NotImplementedException, Exception{
	
		OperatoreFieldConverter converter = this.getOperatoreFieldConverter();
		Map<String, List<IField>> mapTableToPKColumn = new java.util.Hashtable<String, List<IField>>();
		UtilsTemplate<IField> utilities = new UtilsTemplate<IField>();

		//		  If a table doesn't have a primary key, don't add it to this map

		// Operatore.model()
		mapTableToPKColumn.put(converter.toTable(Operatore.model()),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Operatore.model()))
			));

		// Operatore.model().OPERATORE_UO
		mapTableToPKColumn.put(converter.toTable(Operatore.model().OPERATORE_UO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Operatore.model().OPERATORE_UO))
			));

		// Operatore.model().OPERATORE_UO.ID_UO
		mapTableToPKColumn.put(converter.toTable(Operatore.model().OPERATORE_UO.ID_UO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Operatore.model().OPERATORE_UO.ID_UO))
			));

		// Operatore.model().OPERATORE_UO.ID_UO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(Operatore.model().OPERATORE_UO.ID_UO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Operatore.model().OPERATORE_UO.ID_UO.ID_DOMINIO))
			));

		// Operatore.model().OPERATORE_APPLICAZIONE
		mapTableToPKColumn.put(converter.toTable(Operatore.model().OPERATORE_APPLICAZIONE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Operatore.model().OPERATORE_APPLICAZIONE))
			));

		// Operatore.model().OPERATORE_APPLICAZIONE.ID_APPLICAZIONE
		mapTableToPKColumn.put(converter.toTable(Operatore.model().OPERATORE_APPLICAZIONE.ID_APPLICAZIONE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Operatore.model().OPERATORE_APPLICAZIONE.ID_APPLICAZIONE))
			));

		// Operatore.model().OPERATORE_PORTALE
		mapTableToPKColumn.put(converter.toTable(Operatore.model().OPERATORE_PORTALE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Operatore.model().OPERATORE_PORTALE))
			));

		// Operatore.model().OPERATORE_PORTALE.ID_PORTALE
		mapTableToPKColumn.put(converter.toTable(Operatore.model().OPERATORE_PORTALE.ID_PORTALE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Operatore.model().OPERATORE_PORTALE.ID_PORTALE))
			));

        return mapTableToPKColumn;		
	}
	
	@Override
	public List<Long> findAllTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression paginatedExpression) throws ServiceException, NotImplementedException, Exception {
		
		List<Long> list = new ArrayList<Long>();

		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getOperatoreFieldConverter().toTable(Operatore.model())+".id");
		Class<?> objectIdClass = Long.class;
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFindAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
												this.getOperatoreFieldConverter(), Operatore.model());
		
		_join(paginatedExpression,sqlQueryObject);
		
		List<Object> listObjects = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.findAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
																			this.getOperatoreFieldConverter(), Operatore.model(), objectIdClass, listaQuery);
		for(Object object: listObjects) {
			list.add((Long)object);
		}

        return list;
		
	}
	
	@Override
	public long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws ServiceException, NotFoundException, MultipleResultException, NotImplementedException, Exception {
	
		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getOperatoreFieldConverter().toTable(Operatore.model())+".id");
		Class<?> objectIdClass = Long.class;
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFind(jdbcProperties, log, connection, sqlQueryObject, expression,
												this.getOperatoreFieldConverter(), Operatore.model());
		
		_join(expression,sqlQueryObject);

		Object res = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.find(jdbcProperties, log, connection, sqlQueryObject, expression,
														this.getOperatoreFieldConverter(), Operatore.model(), objectIdClass, listaQuery);
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
	public IdOperatore findId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, boolean throwNotFound)
			throws NotFoundException, ServiceException, NotImplementedException, Exception {
		
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);

		ISQLQueryObject sqlQueryObjectGet = sqlQueryObject.newSQLQueryObject();

		// Object _operatore
		sqlQueryObjectGet.addFromTable(this.getOperatoreFieldConverter().toTable(Operatore.model()));
		sqlQueryObjectGet.addSelectField(this.getOperatoreFieldConverter().toColumn(Operatore.model().PRINCIPAL,true));

		sqlQueryObjectGet.setANDLogicOperator(true);
		sqlQueryObjectGet.addWhereCondition("id=?");

		// Recupero _operatore
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] searchParams_operatore = new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] { 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tableId,Long.class)
		};
		List<Class<?>> listaFieldIdReturnType_operatore = new ArrayList<Class<?>>();
		listaFieldIdReturnType_operatore.add(Operatore.model().PRINCIPAL.getFieldType());

		it.govpay.orm.IdOperatore id_operatore = null;
		List<Object> listaFieldId_operatore = jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet.createSQLQuery(), jdbcProperties.isShowSql(),
				listaFieldIdReturnType_operatore, searchParams_operatore);
		if(listaFieldId_operatore==null || listaFieldId_operatore.size()<=0){
			if(throwNotFound){
				throw new NotFoundException("Not Found");
			}
		}
		else{
			id_operatore = new it.govpay.orm.IdOperatore();
			id_operatore.setPrincipal((String)listaFieldId_operatore.get(0));
		}
		
		return id_operatore;
		
	}

	@Override
	public Long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdOperatore id, boolean throwNotFound)
			throws NotFoundException, ServiceException, NotImplementedException, Exception {
	
		return this.findIdOperatore(jdbcProperties,log,connection,sqlQueryObject,id,throwNotFound);
			
	}
	
	@Override
	public List<List<Object>> nativeQuery(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
											String sql,List<Class<?>> returnClassTypes,Object ... param) throws ServiceException,NotFoundException,NotImplementedException,Exception{
		
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.nativeQuery(jdbcProperties, log, connection, sqlQueryObject,
																							sql,returnClassTypes,param);
														
	}
	
	protected Long findIdOperatore(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdOperatore id, boolean throwNotFound) throws NotFoundException, ServiceException, NotImplementedException, Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);

		ISQLQueryObject sqlQueryObjectGet = sqlQueryObject.newSQLQueryObject();

		// Object _operatore
		sqlQueryObjectGet.addFromTable(this.getOperatoreFieldConverter().toTable(Operatore.model()));
		sqlQueryObjectGet.addSelectField("id");
		sqlQueryObjectGet.setANDLogicOperator(true);
		sqlQueryObjectGet.setSelectDistinct(true);
		sqlQueryObjectGet.addWhereCondition(this.getOperatoreFieldConverter().toColumn(Operatore.model().PRINCIPAL,true)+"=?");

		// Recupero _operatore
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] searchParams_operatore = new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] { 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id.getPrincipal(),Operatore.model().PRINCIPAL.getFieldType()),
		};
		Long id_operatore = null;
		try{
			id_operatore = (Long) jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet.createSQLQuery(), jdbcProperties.isShowSql(),
						Long.class, searchParams_operatore);
		}catch(NotFoundException notFound){
			if(throwNotFound){
				throw new NotFoundException(notFound);
			}
		}
		if(id_operatore==null || id_operatore<=0){
			if(throwNotFound){
				throw new NotFoundException("Not Found");
			}
		}
		
		return id_operatore;
	}
}
