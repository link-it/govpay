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

import it.govpay.orm.RendicontazioneSenzaRPT;
import it.govpay.orm.dao.jdbc.converter.RendicontazioneSenzaRPTFieldConverter;
import it.govpay.orm.dao.jdbc.fetch.RendicontazioneSenzaRPTFetch;

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

/**     
 * JDBCRendicontazioneSenzaRPTServiceSearchImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCRendicontazioneSenzaRPTServiceSearchImpl implements IJDBCServiceSearchWithoutId<RendicontazioneSenzaRPT, JDBCServiceManager> {

	private RendicontazioneSenzaRPTFieldConverter _rendicontazioneSenzaRPTFieldConverter = null;
	public RendicontazioneSenzaRPTFieldConverter getRendicontazioneSenzaRPTFieldConverter() {
		if(this._rendicontazioneSenzaRPTFieldConverter==null){
			this._rendicontazioneSenzaRPTFieldConverter = new RendicontazioneSenzaRPTFieldConverter(this.jdbcServiceManager.getJdbcProperties().getDatabaseType());
		}		
		return this._rendicontazioneSenzaRPTFieldConverter;
	}
	@Override
	public ISQLFieldConverter getFieldConverter() {
		return this.getRendicontazioneSenzaRPTFieldConverter();
	}
	
	private RendicontazioneSenzaRPTFetch rendicontazioneSenzaRPTFetch = new RendicontazioneSenzaRPTFetch();
	public RendicontazioneSenzaRPTFetch getRendicontazioneSenzaRPTFetch() {
		return this.rendicontazioneSenzaRPTFetch;
	}
	@Override
	public IJDBCFetch getFetch() {
		return getRendicontazioneSenzaRPTFetch();
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
	public List<RendicontazioneSenzaRPT> findAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {

		 // default behaviour (id-mapping)
        if(idMappingResolutionBehaviour==null){
                idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
        }

        List<RendicontazioneSenzaRPT> list = new ArrayList<RendicontazioneSenzaRPT>();
        
		try{
			List<IField> fields = new ArrayList<IField>();
			fields.add(new CustomField("id", Long.class, "id", this.getRendicontazioneSenzaRPTFieldConverter().toTable(RendicontazioneSenzaRPT.model())));
			fields.add(new CustomField("id_fr_applicazione", Long.class, "id_fr_applicazione", this.getRendicontazioneSenzaRPTFieldConverter().toTable(RendicontazioneSenzaRPT.model())));
			fields.add(new CustomField("id_iuv", Long.class, "id_iuv", this.getRendicontazioneSenzaRPTFieldConverter().toTable(RendicontazioneSenzaRPT.model())));
			fields.add(new CustomField("id_singolo_versamento", Long.class, "id_singolo_versamento", this.getRendicontazioneSenzaRPTFieldConverter().toTable(RendicontazioneSenzaRPT.model())));
			fields.add(RendicontazioneSenzaRPT.model().IMPORTO_PAGATO);
			fields.add(RendicontazioneSenzaRPT.model().IUR);
			fields.add(RendicontazioneSenzaRPT.model().RENDICONTAZIONE_DATA);

			List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));

			for(Map<String, Object> map: returnMap) {

				Long idFrApplicazione = (Long) map.remove("id_fr_applicazione");
				Long idIuv = (Long) map.remove("id_iuv");
				
				Object idSingoloVersamentoObject = map.remove("id_singolo_versamento");
						
				RendicontazioneSenzaRPT rendicontazioneSenzaRPT = (RendicontazioneSenzaRPT)this.getRendicontazioneSenzaRPTFetch().fetch(jdbcProperties.getDatabase(), RendicontazioneSenzaRPT.model(), map);
				
				if(idMappingResolutionBehaviour==null ||
						(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
					){
						
						it.govpay.orm.IdFrApplicazione id_rendicontazioneSenzaRPT_frApplicazione = null;
						if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
							id_rendicontazioneSenzaRPT_frApplicazione = ((JDBCFrApplicazioneServiceSearch)(this.getServiceManager().getFrApplicazioneServiceSearch())).findId(idFrApplicazione, false);
						}else{
							id_rendicontazioneSenzaRPT_frApplicazione = new it.govpay.orm.IdFrApplicazione();
						}
						id_rendicontazioneSenzaRPT_frApplicazione.setId(idFrApplicazione);
						rendicontazioneSenzaRPT.setIdFrApplicazione(id_rendicontazioneSenzaRPT_frApplicazione);
					}

					if(idMappingResolutionBehaviour==null ||
						(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
					){
						
						it.govpay.orm.IdIuv id_rendicontazioneSenzaRPT_iuv = null;
						if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
							id_rendicontazioneSenzaRPT_iuv = ((JDBCIUVServiceSearch)(this.getServiceManager().getIUVServiceSearch())).findId(idIuv, false);
						}else{
							id_rendicontazioneSenzaRPT_iuv = new it.govpay.orm.IdIuv();
						}
						id_rendicontazioneSenzaRPT_iuv.setId(idIuv);
						rendicontazioneSenzaRPT.setIdIuv(id_rendicontazioneSenzaRPT_iuv);
					}

					if(idSingoloVersamentoObject instanceof Long) {

						Long idSingoloVersamento = (Long) idSingoloVersamentoObject; 
						
						if(idMappingResolutionBehaviour==null ||
								(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
							){
								it.govpay.orm.IdSingoloVersamento id_rendicontazioneSenzaRPT_singoloVersamento = null;
								if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
									id_rendicontazioneSenzaRPT_singoloVersamento = ((JDBCSingoloVersamentoServiceSearch)(this.getServiceManager().getSingoloVersamentoServiceSearch())).findId(idSingoloVersamento, false);
								}else{
									id_rendicontazioneSenzaRPT_singoloVersamento = new it.govpay.orm.IdSingoloVersamento();
								}
								id_rendicontazioneSenzaRPT_singoloVersamento.setId(idSingoloVersamento);
								rendicontazioneSenzaRPT.setIdSingoloVersamento(id_rendicontazioneSenzaRPT_singoloVersamento);
							}
					}

				list.add(rendicontazioneSenzaRPT);
			}
		} catch(NotFoundException e) {}

        return list;     
     
		
	}
	
	@Override
	public RendicontazioneSenzaRPT find(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) 
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
												this.getRendicontazioneSenzaRPTFieldConverter(), RendicontazioneSenzaRPT.model());
		
		sqlQueryObject.addSelectCountField(this.getRendicontazioneSenzaRPTFieldConverter().toTable(RendicontazioneSenzaRPT.model())+".id","tot",true);
		
		_join(expression,sqlQueryObject);
		
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.count(jdbcProperties, log, connection, sqlQueryObject, expression,
																			this.getRendicontazioneSenzaRPTFieldConverter(), RendicontazioneSenzaRPT.model(),listaQuery);
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
												this.getRendicontazioneSenzaRPTFieldConverter(), field);

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
        						expression, this.getRendicontazioneSenzaRPTFieldConverter(), RendicontazioneSenzaRPT.model(), 
        						listaQuery,listaParams);
		
		_join(expression,sqlQueryObject);
        
        List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.select(jdbcProperties, log, connection,
        								org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareSqlQueryObjectForSelectDistinct(sqlQueryObject,sqlQueryObjectDistinct), 
        								expression, this.getRendicontazioneSenzaRPTFieldConverter(), RendicontazioneSenzaRPT.model(),
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
        						this.getRendicontazioneSenzaRPTFieldConverter(), RendicontazioneSenzaRPT.model(), 
        						sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);
		
		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				_join(expression,sqlQueryObjectInnerList.get(i));
			}
		}
        
        List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.union(jdbcProperties, log, connection, sqlQueryObject, 
        								this.getRendicontazioneSenzaRPTFieldConverter(), RendicontazioneSenzaRPT.model(), 
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
        						this.getRendicontazioneSenzaRPTFieldConverter(), RendicontazioneSenzaRPT.model(), 
        						sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);
		
		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				_join(expression,sqlQueryObjectInnerList.get(i));
			}
		}
        
        NonNegativeNumber number = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.unionCount(jdbcProperties, log, connection, sqlQueryObject, 
        								this.getRendicontazioneSenzaRPTFieldConverter(), RendicontazioneSenzaRPT.model(), 
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
			return new JDBCExpression(this.getRendicontazioneSenzaRPTFieldConverter());
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}


	@Override
	public JDBCPaginatedExpression newPaginatedExpression(Logger log) throws NotImplementedException, ServiceException {
		try{
			return new JDBCPaginatedExpression(this.getRendicontazioneSenzaRPTFieldConverter());
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
	public RendicontazioneSenzaRPT get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
		return this._get(jdbcProperties, log, connection, sqlQueryObject, Long.valueOf(tableId), idMappingResolutionBehaviour);
	}
	
	private RendicontazioneSenzaRPT _get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
	
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
					new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectGet = sqlQueryObject.newSQLQueryObject();
				
		RendicontazioneSenzaRPT rendicontazioneSenzaRPT = new RendicontazioneSenzaRPT();
		

		// Object rendicontazioneSenzaRPT
		ISQLQueryObject sqlQueryObjectGet_rendicontazioneSenzaRPT = sqlQueryObjectGet.newSQLQueryObject();
		sqlQueryObjectGet_rendicontazioneSenzaRPT.setANDLogicOperator(true);
		sqlQueryObjectGet_rendicontazioneSenzaRPT.addFromTable(this.getRendicontazioneSenzaRPTFieldConverter().toTable(RendicontazioneSenzaRPT.model()));
		sqlQueryObjectGet_rendicontazioneSenzaRPT.addSelectField("id");
		sqlQueryObjectGet_rendicontazioneSenzaRPT.addSelectField(this.getRendicontazioneSenzaRPTFieldConverter().toColumn(RendicontazioneSenzaRPT.model().IMPORTO_PAGATO,true));
		sqlQueryObjectGet_rendicontazioneSenzaRPT.addSelectField(this.getRendicontazioneSenzaRPTFieldConverter().toColumn(RendicontazioneSenzaRPT.model().IUR,true));
		sqlQueryObjectGet_rendicontazioneSenzaRPT.addSelectField(this.getRendicontazioneSenzaRPTFieldConverter().toColumn(RendicontazioneSenzaRPT.model().RENDICONTAZIONE_DATA,true));
		sqlQueryObjectGet_rendicontazioneSenzaRPT.addWhereCondition("id=?");

		// Get rendicontazioneSenzaRPT
		rendicontazioneSenzaRPT = (RendicontazioneSenzaRPT) jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet_rendicontazioneSenzaRPT.createSQLQuery(), jdbcProperties.isShowSql(), RendicontazioneSenzaRPT.model(), this.getRendicontazioneSenzaRPTFetch(),
			new JDBCObject(tableId,Long.class));


		if(idMappingResolutionBehaviour==null ||
			(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
		){
			// Object _rendicontazioneSenzaRPT_frApplicazione (recupero id)
			ISQLQueryObject sqlQueryObjectGet_rendicontazioneSenzaRPT_frApplicazione_readFkId = sqlQueryObjectGet.newSQLQueryObject();
			sqlQueryObjectGet_rendicontazioneSenzaRPT_frApplicazione_readFkId.addFromTable(this.getRendicontazioneSenzaRPTFieldConverter().toTable(it.govpay.orm.RendicontazioneSenzaRPT.model()));
			sqlQueryObjectGet_rendicontazioneSenzaRPT_frApplicazione_readFkId.addSelectField("id_fr_applicazione");
			sqlQueryObjectGet_rendicontazioneSenzaRPT_frApplicazione_readFkId.addWhereCondition("id=?");
			sqlQueryObjectGet_rendicontazioneSenzaRPT_frApplicazione_readFkId.setANDLogicOperator(true);
			Long idFK_rendicontazioneSenzaRPT_frApplicazione = (Long) jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet_rendicontazioneSenzaRPT_frApplicazione_readFkId.createSQLQuery(), jdbcProperties.isShowSql(),Long.class,
					new JDBCObject(rendicontazioneSenzaRPT.getId(),Long.class));
			
			it.govpay.orm.IdFrApplicazione id_rendicontazioneSenzaRPT_frApplicazione = null;
			if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
				id_rendicontazioneSenzaRPT_frApplicazione = ((JDBCFrApplicazioneServiceSearch)(this.getServiceManager().getFrApplicazioneServiceSearch())).findId(idFK_rendicontazioneSenzaRPT_frApplicazione, false);
			}else{
				id_rendicontazioneSenzaRPT_frApplicazione = new it.govpay.orm.IdFrApplicazione();
			}
			id_rendicontazioneSenzaRPT_frApplicazione.setId(idFK_rendicontazioneSenzaRPT_frApplicazione);
			rendicontazioneSenzaRPT.setIdFrApplicazione(id_rendicontazioneSenzaRPT_frApplicazione);
		}

		if(idMappingResolutionBehaviour==null ||
			(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
		){
			// Object _rendicontazioneSenzaRPT_iuv (recupero id)
			ISQLQueryObject sqlQueryObjectGet_rendicontazioneSenzaRPT_iuv_readFkId = sqlQueryObjectGet.newSQLQueryObject();
			sqlQueryObjectGet_rendicontazioneSenzaRPT_iuv_readFkId.addFromTable(this.getRendicontazioneSenzaRPTFieldConverter().toTable(it.govpay.orm.RendicontazioneSenzaRPT.model()));
			sqlQueryObjectGet_rendicontazioneSenzaRPT_iuv_readFkId.addSelectField("id_iuv");
			sqlQueryObjectGet_rendicontazioneSenzaRPT_iuv_readFkId.addWhereCondition("id=?");
			sqlQueryObjectGet_rendicontazioneSenzaRPT_iuv_readFkId.setANDLogicOperator(true);
			Long idFK_rendicontazioneSenzaRPT_iuv = (Long) jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet_rendicontazioneSenzaRPT_iuv_readFkId.createSQLQuery(), jdbcProperties.isShowSql(),Long.class,
					new JDBCObject(rendicontazioneSenzaRPT.getId(),Long.class));
			
			it.govpay.orm.IdIuv id_rendicontazioneSenzaRPT_iuv = null;
			if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
				id_rendicontazioneSenzaRPT_iuv = ((JDBCIUVServiceSearch)(this.getServiceManager().getIUVServiceSearch())).findId(idFK_rendicontazioneSenzaRPT_iuv, false);
			}else{
				id_rendicontazioneSenzaRPT_iuv = new it.govpay.orm.IdIuv();
			}
			id_rendicontazioneSenzaRPT_iuv.setId(idFK_rendicontazioneSenzaRPT_iuv);
			rendicontazioneSenzaRPT.setIdIuv(id_rendicontazioneSenzaRPT_iuv);
		}

		if(idMappingResolutionBehaviour==null ||
			(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
		){
			// Object _rendicontazioneSenzaRPT_singoloVersamento (recupero id)
			ISQLQueryObject sqlQueryObjectGet_rendicontazioneSenzaRPT_singoloVersamento_readFkId = sqlQueryObjectGet.newSQLQueryObject();
			sqlQueryObjectGet_rendicontazioneSenzaRPT_singoloVersamento_readFkId.addFromTable(this.getRendicontazioneSenzaRPTFieldConverter().toTable(it.govpay.orm.RendicontazioneSenzaRPT.model()));
			sqlQueryObjectGet_rendicontazioneSenzaRPT_singoloVersamento_readFkId.addSelectField("id_singolo_versamento");
			sqlQueryObjectGet_rendicontazioneSenzaRPT_singoloVersamento_readFkId.addWhereCondition("id=?");
			sqlQueryObjectGet_rendicontazioneSenzaRPT_singoloVersamento_readFkId.setANDLogicOperator(true);
			Long idFK_rendicontazioneSenzaRPT_singoloVersamento = (Long) jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet_rendicontazioneSenzaRPT_singoloVersamento_readFkId.createSQLQuery(), jdbcProperties.isShowSql(),Long.class,
					new JDBCObject(rendicontazioneSenzaRPT.getId(),Long.class));
			
			it.govpay.orm.IdSingoloVersamento id_rendicontazioneSenzaRPT_singoloVersamento = null;
			if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
				id_rendicontazioneSenzaRPT_singoloVersamento = ((JDBCSingoloVersamentoServiceSearch)(this.getServiceManager().getSingoloVersamentoServiceSearch())).findId(idFK_rendicontazioneSenzaRPT_singoloVersamento, false);
			}else{
				id_rendicontazioneSenzaRPT_singoloVersamento = new it.govpay.orm.IdSingoloVersamento();
			}
			id_rendicontazioneSenzaRPT_singoloVersamento.setId(idFK_rendicontazioneSenzaRPT_singoloVersamento);
			rendicontazioneSenzaRPT.setIdSingoloVersamento(id_rendicontazioneSenzaRPT_singoloVersamento);
		}

        return rendicontazioneSenzaRPT;  
	
	} 
	
	@Override
	public boolean exists(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId) throws MultipleResultException, NotImplementedException, ServiceException, Exception {
		return this._exists(jdbcProperties, log, connection, sqlQueryObject, Long.valueOf(tableId));
	}
	
	private boolean _exists(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long tableId) throws MultipleResultException, NotImplementedException, ServiceException, Exception {
	
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
					new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
				
		boolean existsRendicontazioneSenzaRPT = false;

		sqlQueryObject = sqlQueryObject.newSQLQueryObject();
		sqlQueryObject.setANDLogicOperator(true);

		sqlQueryObject.addFromTable(this.getRendicontazioneSenzaRPTFieldConverter().toTable(RendicontazioneSenzaRPT.model()));
		sqlQueryObject.addSelectField(this.getRendicontazioneSenzaRPTFieldConverter().toColumn(RendicontazioneSenzaRPT.model().IMPORTO_PAGATO,true));
		sqlQueryObject.addWhereCondition("id=?");


		// Exists rendicontazioneSenzaRPT
		existsRendicontazioneSenzaRPT = jdbcUtilities.exists(sqlQueryObject.createSQLQuery(), jdbcProperties.isShowSql(),
			new JDBCObject(tableId,Long.class));

		
        return existsRendicontazioneSenzaRPT;
	
	}
	
	private void _join(IExpression expression, ISQLQueryObject sqlQueryObject) throws NotImplementedException, ServiceException, Exception{
		
		if(expression.inUseModel(RendicontazioneSenzaRPT.model().ID_FR_APPLICAZIONE,false)){
			String tableName1 = this.getRendicontazioneSenzaRPTFieldConverter().toAliasTable(RendicontazioneSenzaRPT.model());
			String tableName2 = this.getRendicontazioneSenzaRPTFieldConverter().toAliasTable(RendicontazioneSenzaRPT.model().ID_FR_APPLICAZIONE);
			sqlQueryObject.addWhereCondition(tableName1+".id_fr_applicazione="+tableName2+".id");
		}
		
		if(expression.inUseModel(RendicontazioneSenzaRPT.model().ID_FR_APPLICAZIONE.ID_APPLICAZIONE,false)){
			if(!expression.inUseModel(RendicontazioneSenzaRPT.model().ID_FR_APPLICAZIONE,false)){
				String tableName1 = this.getRendicontazioneSenzaRPTFieldConverter().toAliasTable(RendicontazioneSenzaRPT.model());
				String tableName2 = this.getRendicontazioneSenzaRPTFieldConverter().toAliasTable(RendicontazioneSenzaRPT.model().ID_FR_APPLICAZIONE);
				sqlQueryObject.addFromTable(tableName2);
				sqlQueryObject.addWhereCondition(tableName1+".id_fr_applicazione="+tableName2+".id");
			}

			String tableName1 = this.getRendicontazioneSenzaRPTFieldConverter().toAliasTable(RendicontazioneSenzaRPT.model().ID_FR_APPLICAZIONE);
			String tableName2 = this.getRendicontazioneSenzaRPTFieldConverter().toAliasTable(RendicontazioneSenzaRPT.model().ID_FR_APPLICAZIONE.ID_APPLICAZIONE);
			sqlQueryObject.addWhereCondition(tableName1+".id_applicazione="+tableName2+".id");
		}
		
		if(expression.inUseModel(RendicontazioneSenzaRPT.model().ID_FR_APPLICAZIONE.ID_FR,false)){
			if(!expression.inUseModel(RendicontazioneSenzaRPT.model().ID_FR_APPLICAZIONE,false)){
				String tableName1 = this.getRendicontazioneSenzaRPTFieldConverter().toAliasTable(RendicontazioneSenzaRPT.model());
				String tableName2 = this.getRendicontazioneSenzaRPTFieldConverter().toAliasTable(RendicontazioneSenzaRPT.model().ID_FR_APPLICAZIONE);
				sqlQueryObject.addFromTable(tableName2);
				sqlQueryObject.addWhereCondition(tableName1+".id_fr_applicazione="+tableName2+".id");
			}

			String tableName1 = this.getRendicontazioneSenzaRPTFieldConverter().toAliasTable(RendicontazioneSenzaRPT.model().ID_FR_APPLICAZIONE);
			String tableName2 = this.getRendicontazioneSenzaRPTFieldConverter().toAliasTable(RendicontazioneSenzaRPT.model().ID_FR_APPLICAZIONE.ID_FR);
			sqlQueryObject.addWhereCondition(tableName1+".id_fr="+tableName2+".id");
		}
		
		if(expression.inUseModel(RendicontazioneSenzaRPT.model().ID_IUV,false)){
			String tableName1 = this.getRendicontazioneSenzaRPTFieldConverter().toAliasTable(RendicontazioneSenzaRPT.model());
			String tableName2 = this.getRendicontazioneSenzaRPTFieldConverter().toAliasTable(RendicontazioneSenzaRPT.model().ID_IUV);
			sqlQueryObject.addWhereCondition(tableName1+".id_iuv="+tableName2+".id");
		}
		
		if(expression.inUseModel(RendicontazioneSenzaRPT.model().ID_IUV.ID_DOMINIO,false)){
			if(!expression.inUseModel(RendicontazioneSenzaRPT.model().ID_IUV,false)){
				String tableName1 = this.getRendicontazioneSenzaRPTFieldConverter().toAliasTable(RendicontazioneSenzaRPT.model());
				String tableName2 = this.getRendicontazioneSenzaRPTFieldConverter().toAliasTable(RendicontazioneSenzaRPT.model().ID_IUV);
				sqlQueryObject.addFromTable(tableName2);
				sqlQueryObject.addWhereCondition(tableName1+".id_iuv="+tableName2+".id");
			}
			
			String tableName1 = this.getRendicontazioneSenzaRPTFieldConverter().toAliasTable(RendicontazioneSenzaRPT.model().ID_IUV);
			String tableName2 = this.getRendicontazioneSenzaRPTFieldConverter().toAliasTable(RendicontazioneSenzaRPT.model().ID_IUV.ID_DOMINIO);
			sqlQueryObject.addWhereCondition(tableName1+".id_dominio="+tableName2+".id");
		}
		
        
	}
	
	protected java.util.List<Object> _getRootTablePrimaryKeyValues(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, RendicontazioneSenzaRPT rendicontazioneSenzaRPT) throws NotFoundException, ServiceException, NotImplementedException, Exception{
	    // Identificativi
        java.util.List<Object> rootTableIdValues = new java.util.ArrayList<Object>();
		rootTableIdValues.add(rendicontazioneSenzaRPT.getId());
        
        return rootTableIdValues;
	}
	
	protected Map<String, List<IField>> _getMapTableToPKColumn() throws NotImplementedException, Exception{
	
		RendicontazioneSenzaRPTFieldConverter converter = this.getRendicontazioneSenzaRPTFieldConverter();
		Map<String, List<IField>> mapTableToPKColumn = new java.util.Hashtable<String, List<IField>>();
		UtilsTemplate<IField> utilities = new UtilsTemplate<IField>();

		//		  If a table doesn't have a primary key, don't add it to this map

		// RendicontazioneSenzaRPT.model()
		mapTableToPKColumn.put(converter.toTable(RendicontazioneSenzaRPT.model()),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazioneSenzaRPT.model()))
			));

		// RendicontazioneSenzaRPT.model().ID_FR_APPLICAZIONE
		mapTableToPKColumn.put(converter.toTable(RendicontazioneSenzaRPT.model().ID_FR_APPLICAZIONE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazioneSenzaRPT.model().ID_FR_APPLICAZIONE))
			));

		// RendicontazioneSenzaRPT.model().ID_FR_APPLICAZIONE.ID_FR
		mapTableToPKColumn.put(converter.toTable(RendicontazioneSenzaRPT.model().ID_FR_APPLICAZIONE.ID_FR),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazioneSenzaRPT.model().ID_FR_APPLICAZIONE.ID_FR))
			));

		// RendicontazioneSenzaRPT.model().ID_FR_APPLICAZIONE.ID_APPLICAZIONE
		mapTableToPKColumn.put(converter.toTable(RendicontazioneSenzaRPT.model().ID_FR_APPLICAZIONE.ID_APPLICAZIONE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazioneSenzaRPT.model().ID_FR_APPLICAZIONE.ID_APPLICAZIONE))
			));

		// RendicontazioneSenzaRPT.model().ID_IUV
		mapTableToPKColumn.put(converter.toTable(RendicontazioneSenzaRPT.model().ID_IUV),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazioneSenzaRPT.model().ID_IUV))
			));

		// RendicontazioneSenzaRPT.model().ID_IUV.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(RendicontazioneSenzaRPT.model().ID_IUV.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazioneSenzaRPT.model().ID_IUV.ID_DOMINIO))
			));

        return mapTableToPKColumn;		
	}
	
	@Override
	public List<Long> findAllTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression paginatedExpression) throws ServiceException, NotImplementedException, Exception {
		
		List<Long> list = new ArrayList<Long>();

		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getRendicontazioneSenzaRPTFieldConverter().toTable(RendicontazioneSenzaRPT.model())+".id");
		Class<?> objectIdClass = Long.class;
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFindAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
												this.getRendicontazioneSenzaRPTFieldConverter(), RendicontazioneSenzaRPT.model());
		
		_join(paginatedExpression,sqlQueryObject);
		
		List<Object> listObjects = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.findAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
																			this.getRendicontazioneSenzaRPTFieldConverter(), RendicontazioneSenzaRPT.model(), objectIdClass, listaQuery);
		for(Object object: listObjects) {
			list.add((Long)object);
		}

        return list;
		
	}
	
	@Override
	public long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws ServiceException, NotFoundException, MultipleResultException, NotImplementedException, Exception {
	
		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getRendicontazioneSenzaRPTFieldConverter().toTable(RendicontazioneSenzaRPT.model())+".id");
		Class<?> objectIdClass = Long.class;
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFind(jdbcProperties, log, connection, sqlQueryObject, expression,
												this.getRendicontazioneSenzaRPTFieldConverter(), RendicontazioneSenzaRPT.model());
		
		_join(expression,sqlQueryObject);

		Object res = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.find(jdbcProperties, log, connection, sqlQueryObject, expression,
														this.getRendicontazioneSenzaRPTFieldConverter(), RendicontazioneSenzaRPT.model(), objectIdClass, listaQuery);
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
	public List<List<Object>> nativeQuery(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
											String sql,List<Class<?>> returnClassTypes,Object ... param) throws ServiceException,NotFoundException,NotImplementedException,Exception{
		
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.nativeQuery(jdbcProperties, log, connection, sqlQueryObject,
																							sql,returnClassTypes,param);
														
	}
	
}
