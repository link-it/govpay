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
import it.govpay.orm.IdRuolo;
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

import it.govpay.orm.Ruolo;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCRuoloServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCRuoloServiceImpl extends JDBCRuoloServiceSearchImpl
	implements IJDBCServiceCRUDWithId<Ruolo, IdRuolo, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Ruolo ruolo, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				


		// Object ruolo
		sqlQueryObjectInsert.addInsertTable(this.getRuoloFieldConverter().toTable(Ruolo.model()));
		sqlQueryObjectInsert.addInsertField(this.getRuoloFieldConverter().toColumn(Ruolo.model().COD_RUOLO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getRuoloFieldConverter().toColumn(Ruolo.model().DESCRIZIONE,false),"?");

		// Insert ruolo
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getRuoloFetch().getKeyGeneratorObject(Ruolo.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(ruolo.getCodRuolo(),Ruolo.model().COD_RUOLO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(ruolo.getDescrizione(),Ruolo.model().DESCRIZIONE.getFieldType())
		);
		ruolo.setId(id);

		
	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdRuolo oldId, Ruolo ruolo, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdRuolo(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = ruolo.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: ruolo.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			ruolo.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}

		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, ruolo, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Ruolo ruolo, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
	
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
		
//		boolean setIdMappingResolutionBehaviour = 
//			(idMappingResolutionBehaviour==null) ||
//			org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) ||
//			org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour);
//			


		// Object ruolo
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getRuoloFieldConverter().toTable(Ruolo.model()));
		boolean isUpdate_ruolo = true;
		java.util.List<JDBCObject> lstObjects_ruolo = new java.util.ArrayList<JDBCObject>();
		sqlQueryObjectUpdate.addUpdateField(this.getRuoloFieldConverter().toColumn(Ruolo.model().COD_RUOLO,false), "?");
		lstObjects_ruolo.add(new JDBCObject(ruolo.getCodRuolo(), Ruolo.model().COD_RUOLO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getRuoloFieldConverter().toColumn(Ruolo.model().DESCRIZIONE,false), "?");
		lstObjects_ruolo.add(new JDBCObject(ruolo.getDescrizione(), Ruolo.model().DESCRIZIONE.getFieldType()));
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_ruolo.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_ruolo) {
			// Update ruolo
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_ruolo.toArray(new JDBCObject[]{}));
		}


	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdRuolo id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getRuoloFieldConverter().toTable(Ruolo.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getRuoloFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdRuolo id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getRuoloFieldConverter().toTable(Ruolo.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getRuoloFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdRuolo id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getRuoloFieldConverter().toTable(Ruolo.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getRuoloFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getRuoloFieldConverter().toTable(Ruolo.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getRuoloFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getRuoloFieldConverter().toTable(Ruolo.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getRuoloFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getRuoloFieldConverter().toTable(Ruolo.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getRuoloFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdRuolo oldId, Ruolo ruolo, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, ruolo,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, ruolo,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Ruolo ruolo, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, ruolo,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, ruolo,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Ruolo ruolo) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (ruolo.getId()!=null) && (ruolo.getId()>0) ){
			longId = ruolo.getId();
		}
		else{
			IdRuolo idRuolo = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,ruolo);
			longId = this.findIdRuolo(jdbcProperties,log,connection,sqlQueryObject,idRuolo,false);
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
		

		// Object ruolo
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getRuoloFieldConverter().toTable(Ruolo.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete ruolo
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdRuolo idRuolo) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdRuolo(jdbcProperties, log, connection, sqlQueryObject, idRuolo, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getRuoloFieldConverter()));

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
