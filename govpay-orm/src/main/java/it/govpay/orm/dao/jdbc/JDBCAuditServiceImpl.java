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

import org.openspcoop2.generic_project.dao.jdbc.IJDBCServiceCRUDWithoutId;
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

import it.govpay.orm.Audit;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCAuditServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCAuditServiceImpl extends JDBCAuditServiceSearchImpl
	implements IJDBCServiceCRUDWithoutId<Audit, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Audit audit, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				

		// Object _operatore
		Long id_operatore = null;
		it.govpay.orm.IdOperatore idLogic_operatore = null;
		idLogic_operatore = audit.getIdOperatore();
		if(idLogic_operatore!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_operatore = ((JDBCOperatoreServiceSearch)(this.getServiceManager().getOperatoreServiceSearch())).findTableId(idLogic_operatore, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_operatore = idLogic_operatore.getId();
				if(id_operatore==null || id_operatore<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object audit
		sqlQueryObjectInsert.addInsertTable(this.getAuditFieldConverter().toTable(Audit.model()));
		sqlQueryObjectInsert.addInsertField(this.getAuditFieldConverter().toColumn(Audit.model().DATA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getAuditFieldConverter().toColumn(Audit.model().ID_OGGETTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getAuditFieldConverter().toColumn(Audit.model().TIPO_OGGETTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getAuditFieldConverter().toColumn(Audit.model().OGGETTO,false),"?");
		sqlQueryObjectInsert.addInsertField("id_operatore","?");

		// Insert audit
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getAuditFetch().getKeyGeneratorObject(Audit.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(audit.getData(),Audit.model().DATA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(audit.getIdOggetto(),Audit.model().ID_OGGETTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(audit.getTipoOggetto(),Audit.model().TIPO_OGGETTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(audit.getOggetto(),Audit.model().OGGETTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_operatore,Long.class)
		);
		audit.setId(id);

		
	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Audit audit, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		Long tableId = audit.getId();
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}

		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, audit, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Audit audit, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
	
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
		
		boolean setIdMappingResolutionBehaviour = 
			(idMappingResolutionBehaviour==null) ||
			org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) ||
			org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour);
			

		// Object _audit_operatore
		Long id_audit_operatore = null;
		it.govpay.orm.IdOperatore idLogic_audit_operatore = null;
		idLogic_audit_operatore = audit.getIdOperatore();
		if(idLogic_audit_operatore!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_audit_operatore = ((JDBCOperatoreServiceSearch)(this.getServiceManager().getOperatoreServiceSearch())).findTableId(idLogic_audit_operatore, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_audit_operatore = idLogic_audit_operatore.getId();
				if(id_audit_operatore==null || id_audit_operatore<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object audit
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getAuditFieldConverter().toTable(Audit.model()));
		boolean isUpdate_audit = true;
		java.util.List<JDBCObject> lstObjects_audit = new java.util.ArrayList<>();
		sqlQueryObjectUpdate.addUpdateField(this.getAuditFieldConverter().toColumn(Audit.model().DATA,false), "?");
		lstObjects_audit.add(new JDBCObject(audit.getData(), Audit.model().DATA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getAuditFieldConverter().toColumn(Audit.model().ID_OGGETTO,false), "?");
		lstObjects_audit.add(new JDBCObject(audit.getIdOggetto(), Audit.model().ID_OGGETTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getAuditFieldConverter().toColumn(Audit.model().TIPO_OGGETTO,false), "?");
		lstObjects_audit.add(new JDBCObject(audit.getTipoOggetto(), Audit.model().TIPO_OGGETTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getAuditFieldConverter().toColumn(Audit.model().OGGETTO,false), "?");
		lstObjects_audit.add(new JDBCObject(audit.getOggetto(), Audit.model().OGGETTO.getFieldType()));
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_operatore","?");
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_audit.add(new JDBCObject(id_audit_operatore, Long.class));
		}
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_audit.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_audit) {
			// Update audit
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_audit.toArray(new JDBCObject[]{}));
		}


	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Audit audit, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getAuditFieldConverter().toTable(Audit.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, audit),
				this.getAuditFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Audit audit, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getAuditFieldConverter().toTable(Audit.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, audit),
				this.getAuditFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Audit audit, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getAuditFieldConverter().toTable(Audit.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, audit),
				this.getAuditFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getAuditFieldConverter().toTable(Audit.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getAuditFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getAuditFieldConverter().toTable(Audit.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getAuditFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getAuditFieldConverter().toTable(Audit.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getAuditFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Audit audit, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		Long id = audit.getId();
		if(id != null && this.exists(jdbcProperties, log, connection, sqlQueryObject, id)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, audit,idMappingResolutionBehaviour);		
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, audit,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Audit audit, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, audit,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, audit,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Audit audit) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if(audit.getId()==null){
			throw new Exception("Parameter "+audit.getClass().getName()+".id is null");
		}
		if(audit.getId()<=0){
			throw new Exception("Parameter "+audit.getClass().getName()+".id is less equals 0");
		}
		longId = audit.getId();
		
		this._delete(jdbcProperties, log, connection, sqlQueryObject, longId);
		
	}

	private void _delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long id) throws NotImplementedException,ServiceException,Exception {
	
		if(id!=null && id.longValue()<=0){
			throw new ServiceException("Id is less equals 0");
		}
		
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		ISQLQueryObject sqlQueryObjectDelete = sqlQueryObject.newSQLQueryObject();
		

		// Object audit
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getAuditFieldConverter().toTable(Audit.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete audit
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getAuditFieldConverter()));

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
