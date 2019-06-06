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

import it.govpay.orm.UtenzaTipoVersamento;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCUtenzaTipoVersamentoServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCUtenzaTipoVersamentoServiceImpl extends JDBCUtenzaTipoVersamentoServiceSearchImpl
	implements IJDBCServiceCRUDWithoutId<UtenzaTipoVersamento, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, UtenzaTipoVersamento utenzaTipoVersamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				

		// Object _utenza
		Long id_utenza = null;
		it.govpay.orm.IdUtenza idLogic_utenza = null;
		idLogic_utenza = utenzaTipoVersamento.getIdUtenza();
		if(idLogic_utenza!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_utenza = ((JDBCUtenzaServiceSearch)(this.getServiceManager().getUtenzaServiceSearch())).findTableId(idLogic_utenza, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_utenza = idLogic_utenza.getId();
				if(id_utenza==null || id_utenza<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _tipoVersamento
		Long id_tipoVersamento = null;
		it.govpay.orm.IdTipoVersamento idLogic_tipoVersamento = null;
		idLogic_tipoVersamento = utenzaTipoVersamento.getIdTipoVersamento();
		if(idLogic_tipoVersamento!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_tipoVersamento = ((JDBCTipoVersamentoServiceSearch)(this.getServiceManager().getTipoVersamentoServiceSearch())).findTableId(idLogic_tipoVersamento, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_tipoVersamento = idLogic_tipoVersamento.getId();
				if(id_tipoVersamento==null || id_tipoVersamento<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object utenzaTipoVersamento
		sqlQueryObjectInsert.addInsertTable(this.getUtenzaTipoVersamentoFieldConverter().toTable(UtenzaTipoVersamento.model()));
		sqlQueryObjectInsert.addInsertField("id_utenza","?");
		sqlQueryObjectInsert.addInsertField("id_tipo_versamento","?");

		// Insert utenzaTipoVersamento
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getUtenzaTipoVersamentoFetch().getKeyGeneratorObject(UtenzaTipoVersamento.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_utenza,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_tipoVersamento,Long.class)
		);
		utenzaTipoVersamento.setId(id);

	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, UtenzaTipoVersamento utenzaTipoVersamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		Long tableId = utenzaTipoVersamento.getId();
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}

		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, utenzaTipoVersamento, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UtenzaTipoVersamento utenzaTipoVersamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
	
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
			

		// Object _utenzaTipoVersamento_utenza
		Long id_utenzaTipoVersamento_utenza = null;
		it.govpay.orm.IdUtenza idLogic_utenzaTipoVersamento_utenza = null;
		idLogic_utenzaTipoVersamento_utenza = utenzaTipoVersamento.getIdUtenza();
		if(idLogic_utenzaTipoVersamento_utenza!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_utenzaTipoVersamento_utenza = ((JDBCUtenzaServiceSearch)(this.getServiceManager().getUtenzaServiceSearch())).findTableId(idLogic_utenzaTipoVersamento_utenza, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_utenzaTipoVersamento_utenza = idLogic_utenzaTipoVersamento_utenza.getId();
				if(id_utenzaTipoVersamento_utenza==null || id_utenzaTipoVersamento_utenza<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _utenzaTipoVersamento_tipoVersamento
		Long id_utenzaTipoVersamento_tipoVersamento = null;
		it.govpay.orm.IdTipoVersamento idLogic_utenzaTipoVersamento_tipoVersamento = null;
		idLogic_utenzaTipoVersamento_tipoVersamento = utenzaTipoVersamento.getIdTipoVersamento();
		if(idLogic_utenzaTipoVersamento_tipoVersamento!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_utenzaTipoVersamento_tipoVersamento = ((JDBCTipoVersamentoServiceSearch)(this.getServiceManager().getTipoVersamentoServiceSearch())).findTableId(idLogic_utenzaTipoVersamento_tipoVersamento, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_utenzaTipoVersamento_tipoVersamento = idLogic_utenzaTipoVersamento_tipoVersamento.getId();
				if(id_utenzaTipoVersamento_tipoVersamento==null || id_utenzaTipoVersamento_tipoVersamento<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object utenzaTipoVersamento
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getUtenzaTipoVersamentoFieldConverter().toTable(UtenzaTipoVersamento.model()));
		boolean isUpdate_utenzaTipoVersamento = true;
		java.util.List<JDBCObject> lstObjects_utenzaTipoVersamento = new java.util.ArrayList<>();
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_utenza","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_tipo_versamento","?");
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_utenzaTipoVersamento.add(new JDBCObject(id_utenzaTipoVersamento_utenza, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_utenzaTipoVersamento.add(new JDBCObject(id_utenzaTipoVersamento_tipoVersamento, Long.class));
		}
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_utenzaTipoVersamento.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_utenzaTipoVersamento) {
			// Update utenzaTipoVersamento
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_utenzaTipoVersamento.toArray(new JDBCObject[]{}));
		}


	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, UtenzaTipoVersamento utenzaTipoVersamento, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getUtenzaTipoVersamentoFieldConverter().toTable(UtenzaTipoVersamento.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, utenzaTipoVersamento),
				this.getUtenzaTipoVersamentoFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, UtenzaTipoVersamento utenzaTipoVersamento, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getUtenzaTipoVersamentoFieldConverter().toTable(UtenzaTipoVersamento.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, utenzaTipoVersamento),
				this.getUtenzaTipoVersamentoFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, UtenzaTipoVersamento utenzaTipoVersamento, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getUtenzaTipoVersamentoFieldConverter().toTable(UtenzaTipoVersamento.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, utenzaTipoVersamento),
				this.getUtenzaTipoVersamentoFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getUtenzaTipoVersamentoFieldConverter().toTable(UtenzaTipoVersamento.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getUtenzaTipoVersamentoFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getUtenzaTipoVersamentoFieldConverter().toTable(UtenzaTipoVersamento.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getUtenzaTipoVersamentoFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getUtenzaTipoVersamentoFieldConverter().toTable(UtenzaTipoVersamento.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getUtenzaTipoVersamentoFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, UtenzaTipoVersamento utenzaTipoVersamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		Long id = utenzaTipoVersamento.getId();
		if(id != null && this.exists(jdbcProperties, log, connection, sqlQueryObject, id)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, utenzaTipoVersamento,idMappingResolutionBehaviour);		
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, utenzaTipoVersamento,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UtenzaTipoVersamento utenzaTipoVersamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, utenzaTipoVersamento,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, utenzaTipoVersamento,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, UtenzaTipoVersamento utenzaTipoVersamento) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if(utenzaTipoVersamento.getId()==null){
			throw new Exception("Parameter "+utenzaTipoVersamento.getClass().getName()+".id is null");
		}
		if(utenzaTipoVersamento.getId()<=0){
			throw new Exception("Parameter "+utenzaTipoVersamento.getClass().getName()+".id is less equals 0");
		}
		longId = utenzaTipoVersamento.getId();
		
		this._delete(jdbcProperties, log, connection, sqlQueryObject, longId);
		
	}

	private void _delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long id) throws NotImplementedException,ServiceException,Exception {
	
		if(id!=null && id.longValue()<=0){
			throw new ServiceException("Id is less equals 0");
		}
		
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		ISQLQueryObject sqlQueryObjectDelete = sqlQueryObject.newSQLQueryObject();
		

		// Object utenzaTipoVersamento
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getUtenzaTipoVersamentoFieldConverter().toTable(UtenzaTipoVersamento.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete utenzaTipoVersamento
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getUtenzaTipoVersamentoFieldConverter()));

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
