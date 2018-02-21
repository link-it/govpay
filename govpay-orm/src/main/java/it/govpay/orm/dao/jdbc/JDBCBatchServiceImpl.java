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

import org.openspcoop2.utils.sql.ISQLQueryObject;

import org.slf4j.Logger;

import org.openspcoop2.generic_project.dao.jdbc.IJDBCServiceCRUDWithId;
import it.govpay.orm.IdBatch;
import org.openspcoop2.generic_project.beans.NonNegativeNumber;
import org.openspcoop2.generic_project.beans.UpdateField;
import org.openspcoop2.generic_project.beans.UpdateModel;

import org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities;
import org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.dao.jdbc.JDBCExpression;
import org.openspcoop2.generic_project.dao.jdbc.JDBCPaginatedExpression;

import org.openspcoop2.generic_project.dao.jdbc.JDBCServiceManagerProperties;

import it.govpay.orm.Batch;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCBatchServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCBatchServiceImpl extends JDBCBatchServiceSearchImpl
	implements IJDBCServiceCRUDWithId<Batch, IdBatch, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Batch batch, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				


		// Object batch
		sqlQueryObjectInsert.addInsertTable(this.getBatchFieldConverter().toTable(Batch.model()));
		sqlQueryObjectInsert.addInsertField(this.getBatchFieldConverter().toColumn(Batch.model().COD_BATCH,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getBatchFieldConverter().toColumn(Batch.model().NODO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getBatchFieldConverter().toColumn(Batch.model().INIZIO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getBatchFieldConverter().toColumn(Batch.model().AGGIORNAMENTO,false),"?");

		// Insert batch
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getBatchFetch().getKeyGeneratorObject(Batch.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(batch.getCodBatch(),Batch.model().COD_BATCH.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(batch.getNodo(),Batch.model().NODO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(batch.getInizio(),Batch.model().INIZIO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(batch.getAggiornamento(),Batch.model().AGGIORNAMENTO.getFieldType())
		);
		batch.setId(id);

		
	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdBatch oldId, Batch batch, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdBatch(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = batch.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: batch.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			batch.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}

		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, batch, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Batch batch, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
	
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
		ISQLQueryObject sqlQueryObjectDelete = sqlQueryObjectInsert.newSQLQueryObject();
		ISQLQueryObject sqlQueryObjectGet = sqlQueryObjectDelete.newSQLQueryObject();
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObjectGet.newSQLQueryObject();
		
		// Object batch
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getBatchFieldConverter().toTable(Batch.model()));
		boolean isUpdate_batch = true;
		java.util.List<JDBCObject> lstObjects_batch = new java.util.ArrayList<JDBCObject>();
		sqlQueryObjectUpdate.addUpdateField(this.getBatchFieldConverter().toColumn(Batch.model().COD_BATCH,false), "?");
		lstObjects_batch.add(new JDBCObject(batch.getCodBatch(), Batch.model().COD_BATCH.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getBatchFieldConverter().toColumn(Batch.model().NODO,false), "?");
		lstObjects_batch.add(new JDBCObject(batch.getNodo(), Batch.model().NODO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getBatchFieldConverter().toColumn(Batch.model().INIZIO,false), "?");
		lstObjects_batch.add(new JDBCObject(batch.getInizio(), Batch.model().INIZIO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getBatchFieldConverter().toColumn(Batch.model().AGGIORNAMENTO,false), "?");
		lstObjects_batch.add(new JDBCObject(batch.getAggiornamento(), Batch.model().AGGIORNAMENTO.getFieldType()));
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_batch.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_batch) {
			// Update batch
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_batch.toArray(new JDBCObject[]{}));
		}


	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdBatch id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getBatchFieldConverter().toTable(Batch.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getBatchFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdBatch id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getBatchFieldConverter().toTable(Batch.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getBatchFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdBatch id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getBatchFieldConverter().toTable(Batch.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getBatchFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getBatchFieldConverter().toTable(Batch.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getBatchFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getBatchFieldConverter().toTable(Batch.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getBatchFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getBatchFieldConverter().toTable(Batch.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getBatchFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdBatch oldId, Batch batch, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, batch,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, batch,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Batch batch, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, batch,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, batch,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Batch batch) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (batch.getId()!=null) && (batch.getId()>0) ){
			longId = batch.getId();
		}
		else{
			IdBatch idBatch = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,batch);
			longId = this.findIdBatch(jdbcProperties,log,connection,sqlQueryObject,idBatch,false);
			if(longId == null){
				return; // entry not exists
			}
		}		
		
		this._delete(jdbcProperties, log, connection, sqlQueryObject, longId);
		
	}

	private void _delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long id) throws NotImplementedException,ServiceException,Exception {
	
		if(id!=null && id.longValue()<=0){
			throw new ServiceException("Id is less equals 0");
		}
		
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		ISQLQueryObject sqlQueryObjectDelete = sqlQueryObject.newSQLQueryObject();
		

		// Object batch
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getBatchFieldConverter().toTable(Batch.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete batch
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdBatch idBatch) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdBatch(jdbcProperties, log, connection, sqlQueryObject, idBatch, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getBatchFieldConverter()));

	}

	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws NotImplementedException, ServiceException,Exception {

		java.util.List<Long> lst = this.findAllTableIds(jdbcProperties, log, connection, sqlQueryObject, new JDBCPaginatedExpression(expression));
		
		for(Long id : lst) {
			this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		}
		
		return new NonNegativeNumber(lst.size());
	
	}



	// -- DB
	
	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId) throws ServiceException, NotImplementedException, Exception {
		this._delete(jdbcProperties, log, connection, sqlQueryObject, Long.valueOf(tableId));
	}
}
