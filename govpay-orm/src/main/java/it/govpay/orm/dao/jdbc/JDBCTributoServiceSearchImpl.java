/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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

import it.govpay.orm.IdEnte;
import it.govpay.orm.IdTributo;
import it.govpay.orm.Tributo;
import it.govpay.orm.dao.jdbc.converter.TributoFieldConverter;
import it.govpay.orm.dao.jdbc.fetch.TributoFetch;

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
 * JDBCTributoServiceSearchImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCTributoServiceSearchImpl implements IJDBCServiceSearchWithId<Tributo, IdTributo, JDBCServiceManager> {

	private TributoFieldConverter _tributoFieldConverter = null;
	public TributoFieldConverter getTributoFieldConverter() {
		if(this._tributoFieldConverter==null){
			this._tributoFieldConverter = new TributoFieldConverter(this.jdbcServiceManager.getJdbcProperties().getDatabaseType());
		}		
		return this._tributoFieldConverter;
	}
	@Override
	public ISQLFieldConverter getFieldConverter() {
		return this.getTributoFieldConverter();
	}
	
	private TributoFetch tributoFetch = new TributoFetch();
	public TributoFetch getTributoFetch() {
		return this.tributoFetch;
	}
	@Override
	public IJDBCFetch getFetch() {
		return getTributoFetch();
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
	public IdTributo convertToId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Tributo tributo) throws NotImplementedException, ServiceException, Exception{
	
		IdTributo idTributo = new IdTributo();
		idTributo.setCodTributo(tributo.getCodTributo());
		idTributo.setIdEnte(tributo.getIdEnte());
	
		return idTributo;
	}
	
	@Override
	public Tributo get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTributo id, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException,Exception {
		Long id_tributo = ( (id!=null && id.getId()!=null && id.getId()>0) ? id.getId() : this.findIdTributo(jdbcProperties, log, connection, sqlQueryObject, id, true));
		return this._get(jdbcProperties, log, connection, sqlQueryObject, id_tributo,idMappingResolutionBehaviour);
		
		
	}
	
	@Override
	public boolean exists(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTributo id) throws MultipleResultException, NotImplementedException, ServiceException,Exception {

		Long id_tributo = this.findIdTributo(jdbcProperties, log, connection, sqlQueryObject, id, false);
		return id_tributo != null && id_tributo > 0;
		
	}
	
	@Override
	public List<IdTributo> findAllIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {

        // default behaviour (id-mapping)
        if(idMappingResolutionBehaviour==null){
                idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
        }

		List<IdTributo> list = new ArrayList<IdTributo>();

		try{
			List<IField> fields = new ArrayList<IField>();
			fields.add(Tributo.model().COD_TRIBUTO);
			fields.add(new CustomField("id_ente", Long.class, "id_ente", this.getTributoFieldConverter().toTable(Tributo.model())));

			List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));

			for(Map<String, Object> map: returnMap) {
				IdTributo idtributo = new IdTributo();
				idtributo.setCodTributo((String) map.get("codTributo"));
				Long idEnte = (Long) map.get("id_ente");

				if(idMappingResolutionBehaviour==null ||
						(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
					){
						it.govpay.orm.IdEnte id_tributo_ente = null;
						if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
							id_tributo_ente = ((JDBCEnteServiceSearch)(this.getServiceManager().getEnteServiceSearch())).findId(idEnte, false);
						}else{
							id_tributo_ente = new it.govpay.orm.IdEnte();
						}
						id_tributo_ente.setId(idEnte);
						idtributo.setIdEnte(id_tributo_ente);
					}

				list.add(idtributo);
			}
		} catch(NotFoundException e) {}

        return list;
		
	}
	
	@Override
	public List<Tributo> findAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {

        // default behaviour (id-mapping)
        if(idMappingResolutionBehaviour==null){
                idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
        }

        List<Tributo> list = new ArrayList<Tributo>();
		try{
			List<IField> fields = new ArrayList<IField>();
			fields.add(new CustomField("id", Long.class, "id", this.getTributoFieldConverter().toTable(Tributo.model())));
			fields.add(Tributo.model().COD_TRIBUTO);
			fields.add(Tributo.model().ABILITATO);
			fields.add(Tributo.model().DESCRIZIONE);
			fields.add(Tributo.model().TIPO_CONTABILITA);
			fields.add(Tributo.model().CODICE_CONTABILITA);
			fields.add(new CustomField("id_ente", Long.class, "id_ente", this.getTributoFieldConverter().toTable(Tributo.model())));
			fields.add(new CustomField("id_iban_accredito", Long.class, "id_iban_accredito", this.getTributoFieldConverter().toTable(Tributo.model())));

			List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));

			for(Map<String, Object> map: returnMap) {
				Long idEnte = (Long) map.remove("id_ente");
				Long idIbanAccredito = (Long) map.remove("id_iban_accredito");
				Tributo tributo = (Tributo)this.getTributoFetch().fetch(jdbcProperties.getDatabase(), Tributo.model(), map);
				if(idMappingResolutionBehaviour==null ||
						(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
					){
						it.govpay.orm.IdEnte id_tributo_ente = null;
						if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
							id_tributo_ente = ((JDBCEnteServiceSearch)(this.getServiceManager().getEnteServiceSearch())).findId(idEnte, false);
						}else{
							id_tributo_ente = new it.govpay.orm.IdEnte();
						}
						id_tributo_ente.setId(idEnte);
						tributo.setIdEnte(id_tributo_ente);
						
						it.govpay.orm.IdIbanAccredito id_tributo_ibanAccredito = null;
						if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
							id_tributo_ibanAccredito = ((JDBCIbanAccreditoServiceSearch)(this.getServiceManager().getIbanAccreditoServiceSearch())).findId(idIbanAccredito, false);
						}else{
							id_tributo_ibanAccredito = new it.govpay.orm.IdIbanAccredito();
						}
						id_tributo_ibanAccredito.setId(idIbanAccredito);
						tributo.setIbanAccredito(id_tributo_ibanAccredito);

					}

				list.add(tributo);
			}
		} catch(NotFoundException e) {}

        return list;      
		
	}
	
	@Override
	public Tributo find(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) 
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
												this.getTributoFieldConverter(), Tributo.model());
		
		sqlQueryObject.addSelectCountField(this.getTributoFieldConverter().toTable(Tributo.model())+".id","tot",true);
		
		_join(expression,sqlQueryObject);
		
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.count(jdbcProperties, log, connection, sqlQueryObject, expression,
																			this.getTributoFieldConverter(), Tributo.model(),listaQuery);
	}

	@Override
	public InUse inUse(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTributo id) throws NotFoundException, NotImplementedException, ServiceException,Exception {
		
		Long id_tributo = this.findIdTributo(jdbcProperties, log, connection, sqlQueryObject, id, true);
        return this._inUse(jdbcProperties, log, connection, sqlQueryObject, id_tributo);
		
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
												this.getTributoFieldConverter(), field);

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
        						expression, this.getTributoFieldConverter(), Tributo.model(), 
        						listaQuery,listaParams);
		
		_join(expression,sqlQueryObject);
        
        List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.select(jdbcProperties, log, connection,
        								org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareSqlQueryObjectForSelectDistinct(sqlQueryObject,sqlQueryObjectDistinct), 
        								expression, this.getTributoFieldConverter(), Tributo.model(),
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
        						this.getTributoFieldConverter(), Tributo.model(), 
        						sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);
		
		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				_join(expression,sqlQueryObjectInnerList.get(i));
			}
		}
        
        List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.union(jdbcProperties, log, connection, sqlQueryObject, 
        								this.getTributoFieldConverter(), Tributo.model(), 
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
        						this.getTributoFieldConverter(), Tributo.model(), 
        						sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);
		
		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				_join(expression,sqlQueryObjectInnerList.get(i));
			}
		}
        
        NonNegativeNumber number = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.unionCount(jdbcProperties, log, connection, sqlQueryObject, 
        								this.getTributoFieldConverter(), Tributo.model(), 
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
			return new JDBCExpression(this.getTributoFieldConverter());
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}


	@Override
	public JDBCPaginatedExpression newPaginatedExpression(Logger log) throws NotImplementedException, ServiceException {
		try{
			return new JDBCPaginatedExpression(this.getTributoFieldConverter());
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
	public void mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTributo id, Tributo obj) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		_mappingTableIds(jdbcProperties,log,connection,sqlQueryObject,obj,
				this.get(jdbcProperties,log,connection,sqlQueryObject,id,null));
	}
	
	@Override
	public void mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Tributo obj) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		_mappingTableIds(jdbcProperties,log,connection,sqlQueryObject,obj,
				this.get(jdbcProperties,log,connection,sqlQueryObject,tableId,null));
	}
	private void _mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Tributo obj, Tributo imgSaved) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		if(imgSaved==null){
			return;
		}
		obj.setId(imgSaved.getId());
		if(obj.getIdEnte()!=null && 
				imgSaved.getIdEnte()!=null){
			obj.getIdEnte().setId(imgSaved.getIdEnte().getId());
		}
		if(obj.getIbanAccredito()!=null && 
				imgSaved.getIbanAccredito()!=null){
			obj.getIbanAccredito().setId(imgSaved.getIbanAccredito().getId());
		}

	}
	
	@Override
	public Tributo get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
		return this._get(jdbcProperties, log, connection, sqlQueryObject, Long.valueOf(tableId), idMappingResolutionBehaviour);
	}
	
	private Tributo _get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
	
//		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
//					new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
//		
//		// default behaviour (id-mapping)
//		if(idMappingResolutionBehaviour==null){
//			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
//		}
//		
//		ISQLQueryObject sqlQueryObjectGet = sqlQueryObject.newSQLQueryObject();
//				
//		Tributo tributo = new Tributo();
//		
//
//		// Object tributo
//		ISQLQueryObject sqlQueryObjectGet_tributo = sqlQueryObjectGet.newSQLQueryObject();
//		sqlQueryObjectGet_tributo.setANDLogicOperator(true);
//		sqlQueryObjectGet_tributo.addFromTable(this.getTributoFieldConverter().toTable(Tributo.model()));
//		sqlQueryObjectGet_tributo.addSelectField("id");
//		sqlQueryObjectGet_tributo.addSelectField(this.getTributoFieldConverter().toColumn(Tributo.model().COD_TRIBUTO,true));
//		sqlQueryObjectGet_tributo.addSelectField(this.getTributoFieldConverter().toColumn(Tributo.model().ABILITATO,true));
//		sqlQueryObjectGet_tributo.addSelectField(this.getTributoFieldConverter().toColumn(Tributo.model().DESCRIZIONE,true));
//		sqlQueryObjectGet_tributo.addSelectField(this.getTributoFieldConverter().toColumn(Tributo.model().TIPO_CONTABILITA,true));
//		sqlQueryObjectGet_tributo.addSelectField(this.getTributoFieldConverter().toColumn(Tributo.model().CODICE_CONTABILITA,true));
//		sqlQueryObjectGet_tributo.addWhereCondition("id=?");
//
//		// Get tributo
//		tributo = (Tributo) jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet_tributo.createSQLQuery(), jdbcProperties.isShowSql(), Tributo.model(), this.getTributoFetch(),
//			new JDBCObject(tableId,Long.class));
//
//
//		if(idMappingResolutionBehaviour==null ||
//			(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
//		){
//			// Object _tributo_ente (recupero id)
//			ISQLQueryObject sqlQueryObjectGet_tributo_ente_readFkId = sqlQueryObjectGet.newSQLQueryObject();
//			sqlQueryObjectGet_tributo_ente_readFkId.addFromTable(this.getTributoFieldConverter().toTable(it.govpay.orm.Tributo.model()));
//			sqlQueryObjectGet_tributo_ente_readFkId.addSelectField("id_ente");
//			sqlQueryObjectGet_tributo_ente_readFkId.addWhereCondition("id=?");
//			sqlQueryObjectGet_tributo_ente_readFkId.setANDLogicOperator(true);
//			Long idFK_tributo_ente = (Long) jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet_tributo_ente_readFkId.createSQLQuery(), jdbcProperties.isShowSql(),Long.class,
//					new JDBCObject(tributo.getId(),Long.class));
//			
//			it.govpay.orm.IdEnte id_tributo_ente = null;
//			if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
//				id_tributo_ente = ((JDBCEnteServiceSearch)(this.getServiceManager().getEnteServiceSearch())).findId(idFK_tributo_ente, false);
//			}else{
//				id_tributo_ente = new it.govpay.orm.IdEnte();
//			}
//			id_tributo_ente.setId(idFK_tributo_ente);
//			tributo.setIdEnte(id_tributo_ente);
//		}
//		if(idMappingResolutionBehaviour==null ||
//			(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
//		){
//			// Object _tributo_ibanAccredito (recupero id)
//			ISQLQueryObject sqlQueryObjectGet_tributo_ibanAccredito_readFkId = sqlQueryObjectGet.newSQLQueryObject();
//			sqlQueryObjectGet_tributo_ibanAccredito_readFkId.addFromTable(this.getTributoFieldConverter().toTable(it.govpay.orm.Tributo.model()));
//			sqlQueryObjectGet_tributo_ibanAccredito_readFkId.addSelectField("id_iban_accredito");
//			sqlQueryObjectGet_tributo_ibanAccredito_readFkId.addWhereCondition("id=?");
//			sqlQueryObjectGet_tributo_ibanAccredito_readFkId.setANDLogicOperator(true);
//			Long idFK_tributo_ibanAccredito = (Long) jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet_tributo_ibanAccredito_readFkId.createSQLQuery(), jdbcProperties.isShowSql(),Long.class,
//					new JDBCObject(tributo.getId(),Long.class));
//			
//			it.govpay.orm.IdIbanAccredito id_tributo_ibanAccredito = null;
//			if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
//				id_tributo_ibanAccredito = ((JDBCIbanAccreditoServiceSearch)(this.getServiceManager().getIbanAccreditoServiceSearch())).findId(idFK_tributo_ibanAccredito, false);
//			}else{
//				id_tributo_ibanAccredito = new it.govpay.orm.IdIbanAccredito();
//			}
//			id_tributo_ibanAccredito.setId(idFK_tributo_ibanAccredito);
//			tributo.setIbanAccredito(id_tributo_ibanAccredito);
//		}
//
//        return tributo;
		
		IField idField = new CustomField("id", Long.class, "id", this.getTributoFieldConverter().toTable(Tributo.model()));
		JDBCPaginatedExpression expression = this.newPaginatedExpression(log);
		
		expression.equals(idField, tableId);
		expression.offset(0);
		expression.limit(2);expression.addOrder(idField, org.openspcoop2.generic_project.expression.SortOrder.ASC); //per verificare la multiple results

		List<Tributo> lst = this.findAll(jdbcProperties, log, connection, sqlQueryObject.newSQLQueryObject(), expression, idMappingResolutionBehaviour);
		
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
				
		boolean existsTributo = false;

		sqlQueryObject = sqlQueryObject.newSQLQueryObject();
		sqlQueryObject.setANDLogicOperator(true);

		sqlQueryObject.addFromTable(this.getTributoFieldConverter().toTable(Tributo.model()));
		sqlQueryObject.addSelectField(this.getTributoFieldConverter().toColumn(Tributo.model().COD_TRIBUTO,true));
		sqlQueryObject.addWhereCondition("id=?");


		// Exists tributo
		existsTributo = jdbcUtilities.exists(sqlQueryObject.createSQLQuery(), jdbcProperties.isShowSql(),
			new JDBCObject(tableId,Long.class));

		
        return existsTributo;
	
	}
	
	private void _join(IExpression expression, ISQLQueryObject sqlQueryObject) throws NotImplementedException, ServiceException, Exception{

		if(expression.inUseField(new CustomField("id_applicazione", Long.class, "id_applicazione", "applicazioni_tributi"), false)) {
			sqlQueryObject.addWhereCondition("applicazioni_tributi.id_tributo=tributi.id");
		}

		if(expression.inUseModel(Tributo.model().ID_ENTE, false)) {
			String tableName1 = this.getTributoFieldConverter().toAliasTable(Tributo.model());
			String tableName2 = this.getTributoFieldConverter().toAliasTable(Tributo.model().ID_ENTE);
			sqlQueryObject.addWhereCondition(tableName1+".id_ente="+tableName2+".id");

		}
		
		if(expression.inUseModel(Tributo.model().IBAN_ACCREDITO, false)) {
			String tableName1 = this.getTributoFieldConverter().toAliasTable(Tributo.model());
			String tableName2 = this.getTributoFieldConverter().toAliasTable(Tributo.model().IBAN_ACCREDITO);
			sqlQueryObject.addWhereCondition(tableName1+".id_iban_accredito="+tableName2+".id");

		}
		
	}
	
	protected java.util.List<Object> _getRootTablePrimaryKeyValues(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTributo id) throws NotFoundException, ServiceException, NotImplementedException, Exception{
	    // Identificativi
        java.util.List<Object> rootTableIdValues = new java.util.ArrayList<Object>();
		Long longId = this.findIdTributo(jdbcProperties, log, connection, sqlQueryObject.newSQLQueryObject(), id, true);
		rootTableIdValues.add(longId);
        
        return rootTableIdValues;
	}
	
	protected Map<String, List<IField>> _getMapTableToPKColumn() throws NotImplementedException, Exception{
	
		TributoFieldConverter converter = this.getTributoFieldConverter();
		Map<String, List<IField>> mapTableToPKColumn = new java.util.Hashtable<String, List<IField>>();
		UtilsTemplate<IField> utilities = new UtilsTemplate<IField>();

		//		  If a table doesn't have a primary key, don't add it to this map

		// Tributo.model()
		mapTableToPKColumn.put(converter.toTable(Tributo.model()),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Tributo.model()))
			));

		// Tributo.model().ID_ENTE
		mapTableToPKColumn.put(converter.toTable(Tributo.model().ID_ENTE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Tributo.model().ID_ENTE))
			));

		// Tributo.model().IBAN_ACCREDITO
		mapTableToPKColumn.put(converter.toTable(Tributo.model().IBAN_ACCREDITO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Tributo.model().IBAN_ACCREDITO))
			));



        // Delete this line when you have verified the method
		int throwNotImplemented = 1;
		if(throwNotImplemented==1){
		        throw new NotImplementedException("NotImplemented");
		}
		// Delete this line when you have verified the method

        return mapTableToPKColumn;		
	}
	
	@Override
	public List<Long> findAllTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression paginatedExpression) throws ServiceException, NotImplementedException, Exception {
		
		List<Long> list = new ArrayList<Long>();

		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getTributoFieldConverter().toTable(Tributo.model())+".id");
		Class<?> objectIdClass = Long.class;
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFindAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
												this.getTributoFieldConverter(), Tributo.model());
		
		_join(paginatedExpression,sqlQueryObject);
		
		List<Object> listObjects = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.findAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
																			this.getTributoFieldConverter(), Tributo.model(), objectIdClass, listaQuery);
		for(Object object: listObjects) {
			list.add((Long)object);
		}

        return list;
		
	}
	
	@Override
	public long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws ServiceException, NotFoundException, MultipleResultException, NotImplementedException, Exception {
	
		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getTributoFieldConverter().toTable(Tributo.model())+".id");
		Class<?> objectIdClass = Long.class;
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFind(jdbcProperties, log, connection, sqlQueryObject, expression,
												this.getTributoFieldConverter(), Tributo.model());
		
		_join(expression,sqlQueryObject);

		Object res = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.find(jdbcProperties, log, connection, sqlQueryObject, expression,
														this.getTributoFieldConverter(), Tributo.model(), objectIdClass, listaQuery);
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
		
		/* 
		 * TODO: implement code that checks whether the object identified by the id parameter is used by other objects
		*/
		
		// Delete this line when you have implemented the method
		int throwNotImplemented = 1;
		if(throwNotImplemented==1){
		        throw new NotImplementedException("NotImplemented");
		}
		// Delete this line when you have implemented the method

        return inUse;

	}
	
	@Override
	public IdTributo findId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, boolean throwNotFound)
			throws NotFoundException, ServiceException, NotImplementedException, Exception {
		
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);

		ISQLQueryObject sqlQueryObjectGet = sqlQueryObject.newSQLQueryObject();

		// Object _tributo
		sqlQueryObjectGet.addFromTable(this.getTributoFieldConverter().toTable(Tributo.model()));
		sqlQueryObjectGet.addFromTable(this.getTributoFieldConverter().toTable(Tributo.model().ID_ENTE));
		sqlQueryObjectGet.addSelectField(this.getTributoFieldConverter().toColumn(Tributo.model().COD_TRIBUTO,true));
		sqlQueryObjectGet.addSelectField(this.getTributoFieldConverter().toColumn(Tributo.model().ID_ENTE.COD_ENTE,true));
		sqlQueryObjectGet.setANDLogicOperator(true);
		sqlQueryObjectGet.addWhereCondition(this.getTributoFieldConverter().toTable(Tributo.model())+".id=?");
		sqlQueryObjectGet.addWhereCondition(this.getTributoFieldConverter().toTable(Tributo.model())+".id_ente="+this.getTributoFieldConverter().toTable(Tributo.model().ID_ENTE)+".id");

		// Recupero _tributo
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] searchParams_tributo = new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] { 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tableId,Long.class)
		};
		List<Class<?>> listaFieldIdReturnType_tributo = new ArrayList<Class<?>>();
		listaFieldIdReturnType_tributo.add(Tributo.model().COD_TRIBUTO.getFieldType());
		listaFieldIdReturnType_tributo.add(Tributo.model().ID_ENTE.COD_ENTE.getFieldType());

		it.govpay.orm.IdTributo id_tributo = null;
		List<Object> listaFieldId_tributo = jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet.createSQLQuery(), jdbcProperties.isShowSql(),
				listaFieldIdReturnType_tributo, searchParams_tributo);
		if(listaFieldId_tributo==null || listaFieldId_tributo.size()<=0){
			if(throwNotFound){
				throw new NotFoundException("Not Found");
			}
		}
		else{
			id_tributo = new it.govpay.orm.IdTributo();
			id_tributo.setCodTributo((String)listaFieldId_tributo.get(0));
			
			IdEnte idEnte = new IdEnte();
			idEnte.setCodEnte((String)listaFieldId_tributo.get(1));
			
			id_tributo.setIdEnte(idEnte);
		}
		
		return id_tributo;
		
	}

	@Override
	public Long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTributo id, boolean throwNotFound)
			throws NotFoundException, ServiceException, NotImplementedException, Exception {
	
		return this.findIdTributo(jdbcProperties,log,connection,sqlQueryObject,id,throwNotFound);
			
	}
	
	@Override
	public List<List<Object>> nativeQuery(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
											String sql,List<Class<?>> returnClassTypes,Object ... param) throws ServiceException,NotFoundException,NotImplementedException,Exception{
		
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.nativeQuery(jdbcProperties, log, connection, sqlQueryObject,
																							sql,returnClassTypes,param);
														
	}
	
	protected Long findIdTributo(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTributo id, boolean throwNotFound) throws NotFoundException, ServiceException, NotImplementedException, Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);

		ISQLQueryObject sqlQueryObjectGet = sqlQueryObject.newSQLQueryObject();

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] searchParams_tributo = null;
		if(id.getIdEnte().getId() != null && id.getIdEnte().getId() > 0) {
			// Object _tributo
			sqlQueryObjectGet.addFromTable(this.getTributoFieldConverter().toTable(Tributo.model()));
			sqlQueryObjectGet.addSelectField("id");
			sqlQueryObjectGet.setANDLogicOperator(true);
			sqlQueryObjectGet.setSelectDistinct(true);
			sqlQueryObjectGet.addWhereCondition(this.getTributoFieldConverter().toColumn(Tributo.model().COD_TRIBUTO,true)+"=?");
			sqlQueryObjectGet.addWhereCondition("id_ente=?");

			// Recupero _tributo
			searchParams_tributo = new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] { 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id.getCodTributo(),Tributo.model().COD_TRIBUTO.getFieldType()),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id.getIdEnte().getId(),Long.class)
			};
			
		} else {
			// Object _tributo
			sqlQueryObjectGet.addFromTable(this.getTributoFieldConverter().toTable(Tributo.model()));
			sqlQueryObjectGet.addFromTable(this.getTributoFieldConverter().toTable(Tributo.model().ID_ENTE));
			sqlQueryObjectGet.addSelectField(this.getTributoFieldConverter().toTable(Tributo.model())+".id");
			sqlQueryObjectGet.setANDLogicOperator(true);
			sqlQueryObjectGet.setSelectDistinct(true);
			sqlQueryObjectGet.addWhereCondition(this.getTributoFieldConverter().toColumn(Tributo.model().COD_TRIBUTO,true)+"=?");
			sqlQueryObjectGet.addWhereCondition(this.getTributoFieldConverter().toColumn(Tributo.model().ID_ENTE.COD_ENTE,true)+"=?");
			sqlQueryObjectGet.addWhereCondition(this.getTributoFieldConverter().toTable(Tributo.model())+".id_ente="+this.getTributoFieldConverter().toTable(Tributo.model().ID_ENTE)+".id");

			// Recupero _tributo
			searchParams_tributo = new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] { 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id.getCodTributo(),Tributo.model().COD_TRIBUTO.getFieldType()),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id.getIdEnte().getCodEnte(),Tributo.model().ID_ENTE.COD_ENTE.getFieldType())
			};
			
		}
		Long id_tributo = null;
		try{
			id_tributo = (Long) jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet.createSQLQuery(), jdbcProperties.isShowSql(),
						Long.class, searchParams_tributo);
		}catch(NotFoundException notFound){
			if(throwNotFound){
				throw new NotFoundException(notFound);
			}
		}
		if(id_tributo==null || id_tributo<=0){
			if(throwNotFound){
				throw new NotFoundException("Not Found");
			}
		}
		
		return id_tributo;
	}
}
