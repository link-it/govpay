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
import org.slf4j.Logger;

import it.govpay.orm.Operazione;
import it.govpay.orm.dao.jdbc.converter.OperazioneFieldConverter;
import it.govpay.orm.dao.jdbc.fetch.OperazioneFetch;

/**     
 * JDBCOperazioneServiceSearchImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCOperazioneServiceSearchImpl implements IJDBCServiceSearchWithoutId<Operazione, JDBCServiceManager> {

	private OperazioneFieldConverter _operazioneFieldConverter = null;
	public OperazioneFieldConverter getOperazioneFieldConverter() {
		if(this._operazioneFieldConverter==null){
			this._operazioneFieldConverter = new OperazioneFieldConverter(this.jdbcServiceManager.getJdbcProperties().getDatabaseType());
		}		
		return this._operazioneFieldConverter;
	}
	@Override
	public ISQLFieldConverter getFieldConverter() {
		return this.getOperazioneFieldConverter();
	}
	
	private OperazioneFetch operazioneFetch = new OperazioneFetch();
	public OperazioneFetch getOperazioneFetch() {
		return this.operazioneFetch;
	}
	@Override
	public IJDBCFetch getFetch() {
		return this.getOperazioneFetch();
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
	public List<Operazione> findAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {

		// default behaviour (id-mapping)
        if(idMappingResolutionBehaviour==null){
                idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
        }

        List<Operazione> list = new ArrayList<>();

		try{
			List<IField> fields = new ArrayList<>();
			fields.add(new CustomField("id_tracciato", Long.class, "id_tracciato", this.getOperazioneFieldConverter().toTable(Operazione.model())));
			fields.add(new CustomField("id_applicazione", Long.class, "id_applicazione", this.getOperazioneFieldConverter().toTable(Operazione.model())));
            fields.add(new CustomField("id", Long.class, "id", this.getOperazioneFieldConverter().toTable(Operazione.model())));
            fields.add(new CustomField("id_stampa", Long.class, "id_stampa", this.getOperazioneFieldConverter().toTable(Operazione.model())));
            fields.add(new CustomField("id_versamento", Long.class, "id_versamento", this.getOperazioneFieldConverter().toTable(Operazione.model())));

    		fields.add(Operazione.model().LINEA_ELABORAZIONE);
    		fields.add(Operazione.model().STATO);
    		fields.add(Operazione.model().DATI_RICHIESTA);
    		fields.add(Operazione.model().DATI_RISPOSTA);
    		fields.add(Operazione.model().DETTAGLIO_ESITO);
    		fields.add(Operazione.model().TIPO_OPERAZIONE);
    		fields.add(Operazione.model().COD_VERSAMENTO_ENTE);
    		fields.add(Operazione.model().COD_DOMINIO);
    		fields.add(Operazione.model().IUV);
    		fields.add(Operazione.model().TRN);

    		List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));

			for(Map<String, Object> map: returnMap) {
                Object idApplicazioneObject = map.remove("id_applicazione");
                Long idFK_operazione_tracciato = (Long)map.remove("id_tracciato");
                Object idStampaObject = map.remove("id_stampa");
                
                Long idVersamento = null;
				Object idVersamentoObject = map.remove("id_versamento");
				if(idVersamentoObject instanceof Long) {
					idVersamento = (Long) idVersamentoObject;
				}
                
                Operazione operazione = (Operazione)this.getOperazioneFetch().fetch(jdbcProperties.getDatabase(), Operazione.model(), map);

    			it.govpay.orm.IdTracciato id_operazione_tracciato = new it.govpay.orm.IdTracciato();
    			id_operazione_tracciato.setId(idFK_operazione_tracciato);
    			operazione.setIdTracciato(id_operazione_tracciato);

				if(idApplicazioneObject instanceof Long) {
					Long idFK_operazione_applicazione = (Long) idApplicazioneObject;
					it.govpay.orm.IdApplicazione id_operazione_applicazione = new it.govpay.orm.IdApplicazione();
					id_operazione_applicazione.setId(idFK_operazione_applicazione);
					operazione.setIdApplicazione(id_operazione_applicazione);
				}
				
				if(idStampaObject != null) {
    				if(idStampaObject instanceof Long) {
    					Long idFK_operazione_stampa = (Long) idStampaObject;
    					it.govpay.orm.IdStampa id_operazione_stampa = new it.govpay.orm.IdStampa();
    					id_operazione_stampa.setId(idFK_operazione_stampa);
    					operazione.setIdStampa(id_operazione_stampa);
    				}
				}
				
				if(idVersamento != null && idVersamento > 0) {
					it.govpay.orm.IdVersamento id_operazione_versamento = new it.govpay.orm.IdVersamento();
					id_operazione_versamento.setId(idVersamento);
					operazione.setIdVersamento(id_operazione_versamento);
				}

                list.add(operazione);

			}
		} catch(NotFoundException e) {}

        return list;  

	}
	
	@Override
	public Operazione find(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) 
		throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException,Exception {

		JDBCPaginatedExpression pagExpr = this.toPaginatedExpression(expression,log);
		
		List<Operazione> lst = this.findAll(jdbcProperties, log, connection, sqlQueryObject, pagExpr, idMappingResolutionBehaviour);

		if(lst.size() <=0)
			throw new NotFoundException("Nessuna entry corrisponde ai criteri indicati.");

		if(lst.size() > 1)
			throw new MultipleResultException("I criteri indicati individuano piu' entry.");

		return lst.get(0);
		
	}
	
	@Override
	public NonNegativeNumber count(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws NotImplementedException, ServiceException,Exception {
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareCount(jdbcProperties, log, connection, sqlQueryObject, expression,
												this.getOperazioneFieldConverter(), Operazione.model());
		
		sqlQueryObject.addSelectCountField(this.getOperazioneFieldConverter().toTable(Operazione.model())+".id","tot");
		
		this._join(expression,sqlQueryObject);
		
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.count(jdbcProperties, log, connection, sqlQueryObject, expression,
																			this.getOperazioneFieldConverter(), Operazione.model(),listaQuery);
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
												this.getOperazioneFieldConverter(), field);

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
        						expression, this.getOperazioneFieldConverter(), Operazione.model(), 
        						listaQuery,listaParams);
		
		this._join(expression,sqlQueryObject);
        
        List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.select(jdbcProperties, log, connection,
        								org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareSqlQueryObjectForSelectDistinct(sqlQueryObject,sqlQueryObjectDistinct), 
        								expression, this.getOperazioneFieldConverter(), Operazione.model(),
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
        						this.getOperazioneFieldConverter(), Operazione.model(), 
        						sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);
		
		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				this._join(expression,sqlQueryObjectInnerList.get(i));
			}
		}
        
        List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.union(jdbcProperties, log, connection, sqlQueryObject, 
        								this.getOperazioneFieldConverter(), Operazione.model(), 
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
        						this.getOperazioneFieldConverter(), Operazione.model(), 
        						sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);
		
		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				this._join(expression,sqlQueryObjectInnerList.get(i));
			}
		}
        
        NonNegativeNumber number = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.unionCount(jdbcProperties, log, connection, sqlQueryObject, 
        								this.getOperazioneFieldConverter(), Operazione.model(), 
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
			return new JDBCExpression(this.getOperazioneFieldConverter());
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}


	@Override
	public JDBCPaginatedExpression newPaginatedExpression(Logger log) throws NotImplementedException, ServiceException {
		try{
			return new JDBCPaginatedExpression(this.getOperazioneFieldConverter());
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
	public Operazione get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
		return this._get(jdbcProperties, log, connection, sqlQueryObject, Long.valueOf(tableId), idMappingResolutionBehaviour);
	}
	
	private Operazione _get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {

		IField idField = new CustomField("id", Long.class, "id", this.getOperazioneFieldConverter().toTable(Operazione.model()));
		JDBCPaginatedExpression expression = this.newPaginatedExpression(log);
		
		expression.equals(idField, tableId);
		List<Operazione> lst = this.findAll(jdbcProperties, log, connection, sqlQueryObject.newSQLQueryObject(), expression, idMappingResolutionBehaviour);
		
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
				
		boolean existsOperazione = false;

		sqlQueryObject = sqlQueryObject.newSQLQueryObject();
		sqlQueryObject.setANDLogicOperator(true);

		sqlQueryObject.addFromTable(this.getOperazioneFieldConverter().toTable(Operazione.model()));
		sqlQueryObject.addSelectField(this.getOperazioneFieldConverter().toColumn(Operazione.model().LINEA_ELABORAZIONE,true));
		sqlQueryObject.addWhereCondition("id=?");


		// Exists operazione
		existsOperazione = jdbcUtilities.exists(sqlQueryObject.createSQLQuery(), jdbcProperties.isShowSql(),
			new JDBCObject(tableId,Long.class));

		
        return existsOperazione;
	
	}
	
	private void _join(IExpression expression, ISQLQueryObject sqlQueryObject) throws NotImplementedException, ServiceException, Exception{
	
		if(expression.inUseModel(Operazione.model().ID_APPLICAZIONE,false)){
			String tableName1 = this.getOperazioneFieldConverter().toAliasTable(Operazione.model());
			String tableName2 = this.getOperazioneFieldConverter().toAliasTable(Operazione.model().ID_APPLICAZIONE);
			sqlQueryObject.addWhereCondition(tableName1+".id_applicazione="+tableName2+".id");
		}

		if(expression.inUseModel(Operazione.model().ID_TRACCIATO,false)){
			String tableName1 = this.getOperazioneFieldConverter().toAliasTable(Operazione.model());
			String tableName2 = this.getOperazioneFieldConverter().toAliasTable(Operazione.model().ID_TRACCIATO);
			sqlQueryObject.addWhereCondition(tableName1+".id_tracciato="+tableName2+".id");
		}
        
		if(expression.inUseModel(Operazione.model().ID_STAMPA,false)){
			String tableName1 = this.getOperazioneFieldConverter().toAliasTable(Operazione.model());
			String tableName2 = this.getOperazioneFieldConverter().toAliasTable(Operazione.model().ID_STAMPA);
			sqlQueryObject.addWhereCondition(tableName1+".id_stampa="+tableName2+".id");
		}
		
		if(expression.inUseModel(Operazione.model().ID_VERSAMENTO,false)){
			String tableName1 = this.getOperazioneFieldConverter().toAliasTable(Operazione.model());
			String tableName2 = this.getOperazioneFieldConverter().toAliasTable(Operazione.model().ID_VERSAMENTO);
			sqlQueryObject.addWhereCondition(tableName1+".id_versamento="+tableName2+".id");
		}
	}
	
	protected java.util.List<Object> _getRootTablePrimaryKeyValues(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Operazione operazione) throws NotFoundException, ServiceException, NotImplementedException, Exception{
	    // Identificativi
        java.util.List<Object> rootTableIdValues = new java.util.ArrayList<>();
		rootTableIdValues.add(operazione.getId());
        
        return rootTableIdValues;
	}
	
	protected Map<String, List<IField>> _getMapTableToPKColumn() throws NotImplementedException, Exception{
	
		OperazioneFieldConverter converter = this.getOperazioneFieldConverter();
		Map<String, List<IField>> mapTableToPKColumn = new java.util.Hashtable<>();
		UtilsTemplate<IField> utilities = new UtilsTemplate<>();

		// Operazione.model()
		mapTableToPKColumn.put(converter.toTable(Operazione.model()),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Operazione.model()))
			));

		// Operazione.model().ID_TRACCIATO
		mapTableToPKColumn.put(converter.toTable(Operazione.model().ID_TRACCIATO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Operazione.model().ID_TRACCIATO))
			));

		// Operazione.model().ID_APPLICAZIONE
		mapTableToPKColumn.put(converter.toTable(Operazione.model().ID_APPLICAZIONE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Operazione.model().ID_APPLICAZIONE))
			));

		// Operazione.model().ID_STAMPA
		mapTableToPKColumn.put(converter.toTable(Operazione.model().ID_STAMPA),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Operazione.model().ID_STAMPA))
			));

		// Operazione.model().ID_STAMPA.ID_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(Operazione.model().ID_STAMPA.ID_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Operazione.model().ID_STAMPA.ID_VERSAMENTO))
			));

		// Operazione.model().ID_STAMPA.ID_VERSAMENTO.ID_APPLICAZIONE
		mapTableToPKColumn.put(converter.toTable(Operazione.model().ID_STAMPA.ID_VERSAMENTO.ID_APPLICAZIONE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Operazione.model().ID_STAMPA.ID_VERSAMENTO.ID_APPLICAZIONE))
			));

		// Operazione.model().ID_STAMPA.ID_VERSAMENTO.ID_UO
		mapTableToPKColumn.put(converter.toTable(Operazione.model().ID_STAMPA.ID_VERSAMENTO.ID_UO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Operazione.model().ID_STAMPA.ID_VERSAMENTO.ID_UO))
			));

		// Operazione.model().ID_STAMPA.ID_VERSAMENTO.ID_UO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(Operazione.model().ID_STAMPA.ID_VERSAMENTO.ID_UO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Operazione.model().ID_STAMPA.ID_VERSAMENTO.ID_UO.ID_DOMINIO))
			));

		// Operazione.model().ID_STAMPA.ID_VERSAMENTO.ID_TIPO_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(Operazione.model().ID_STAMPA.ID_VERSAMENTO.ID_TIPO_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Operazione.model().ID_STAMPA.ID_VERSAMENTO.ID_TIPO_VERSAMENTO))
			));

		// Operazione.model().ID_STAMPA.ID_DOCUMENTO
		mapTableToPKColumn.put(converter.toTable(Operazione.model().ID_STAMPA.ID_DOCUMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Operazione.model().ID_STAMPA.ID_DOCUMENTO))
			));

		// Operazione.model().ID_STAMPA.ID_DOCUMENTO.ID_APPLICAZIONE
		mapTableToPKColumn.put(converter.toTable(Operazione.model().ID_STAMPA.ID_DOCUMENTO.ID_APPLICAZIONE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Operazione.model().ID_STAMPA.ID_DOCUMENTO.ID_APPLICAZIONE))
			));

		// Operazione.model().ID_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(Operazione.model().ID_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Operazione.model().ID_VERSAMENTO))
			));

		// Operazione.model().ID_VERSAMENTO.ID_APPLICAZIONE
		mapTableToPKColumn.put(converter.toTable(Operazione.model().ID_VERSAMENTO.ID_APPLICAZIONE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Operazione.model().ID_VERSAMENTO.ID_APPLICAZIONE))
			));

		// Operazione.model().ID_VERSAMENTO.ID_UO
		mapTableToPKColumn.put(converter.toTable(Operazione.model().ID_VERSAMENTO.ID_UO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Operazione.model().ID_VERSAMENTO.ID_UO))
			));

		// Operazione.model().ID_VERSAMENTO.ID_UO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(Operazione.model().ID_VERSAMENTO.ID_UO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Operazione.model().ID_VERSAMENTO.ID_UO.ID_DOMINIO))
			));

		// Operazione.model().ID_VERSAMENTO.ID_TIPO_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(Operazione.model().ID_VERSAMENTO.ID_TIPO_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Operazione.model().ID_VERSAMENTO.ID_TIPO_VERSAMENTO))
			));
        
        return mapTableToPKColumn;		
	}
	
	@Override
	public List<Long> findAllTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression paginatedExpression) throws ServiceException, NotImplementedException, Exception {
		
		List<Long> list = new ArrayList<>();

		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getOperazioneFieldConverter().toTable(Operazione.model())+".id");
		Class<?> objectIdClass = Long.class;
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFindAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
												this.getOperazioneFieldConverter(), Operazione.model());
		
		this._join(paginatedExpression,sqlQueryObject);
		
		List<Object> listObjects = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.findAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
																			this.getOperazioneFieldConverter(), Operazione.model(), objectIdClass, listaQuery);
		for(Object object: listObjects) {
			list.add((Long)object);
		}

        return list;
		
	}
	
	@Override
	public long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws ServiceException, NotFoundException, MultipleResultException, NotImplementedException, Exception {
	
		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getOperazioneFieldConverter().toTable(Operazione.model())+".id");
		Class<?> objectIdClass = Long.class;
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFind(jdbcProperties, log, connection, sqlQueryObject, expression,
												this.getOperazioneFieldConverter(), Operazione.model());
		
		this._join(expression,sqlQueryObject);

		Object res = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.find(jdbcProperties, log, connection, sqlQueryObject, expression,
														this.getOperazioneFieldConverter(), Operazione.model(), objectIdClass, listaQuery);
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
