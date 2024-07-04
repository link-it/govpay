/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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

import org.openspcoop2.generic_project.dao.jdbc.utils.GenericJDBCUtilities;
import org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.dao.jdbc.JDBCExpression;
import org.openspcoop2.generic_project.dao.jdbc.JDBCPaginatedExpression;

import org.openspcoop2.generic_project.dao.jdbc.JDBCServiceManagerProperties;

import it.govpay.orm.UtenzaDominio;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCUtenzaDominioServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCUtenzaDominioServiceImpl extends JDBCUtenzaDominioServiceSearchImpl
	implements IJDBCServiceCRUDWithoutId<UtenzaDominio, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, UtenzaDominio utenzaDominio, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

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
		idLogic_utenza = utenzaDominio.getIdUtenza();
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

		// Object _dominio
		Long id_dominio = null;
		it.govpay.orm.IdDominio idLogic_dominio = null;
		idLogic_dominio = utenzaDominio.getIdDominio();
		if(idLogic_dominio!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_dominio = ((JDBCDominioServiceSearch)(this.getServiceManager().getDominioServiceSearch())).findTableId(idLogic_dominio, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_dominio = idLogic_dominio.getId();
				if(id_dominio==null || id_dominio<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _uo
		Long id_uo = null;
		it.govpay.orm.IdUo idLogic_uo = null;
		idLogic_uo = utenzaDominio.getIdUo();
		if(idLogic_uo!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_uo = ((JDBCUoServiceSearch)(this.getServiceManager().getUoServiceSearch())).findTableId(idLogic_uo, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_uo = idLogic_uo.getId();
				if(id_uo==null || id_uo<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object utenzaDominio
		sqlQueryObjectInsert.addInsertTable(this.getUtenzaDominioFieldConverter().toTable(UtenzaDominio.model()));
		sqlQueryObjectInsert.addInsertField("id_utenza","?");
		sqlQueryObjectInsert.addInsertField("id_dominio","?");
		sqlQueryObjectInsert.addInsertField("id_uo","?");

		// Insert utenzaDominio
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getUtenzaDominioFetch().getKeyGeneratorObject(UtenzaDominio.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_utenza,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_dominio,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_uo,Long.class)
		);
		utenzaDominio.setId(id);

		
	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, UtenzaDominio utenzaDominio, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		Long tableId = utenzaDominio.getId();
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}

		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, utenzaDominio, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UtenzaDominio utenzaDominio, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
	
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
			

		// Object _utenzaDominio_utenza
		Long id_utenzaDominio_utenza = null;
		it.govpay.orm.IdUtenza idLogic_utenzaDominio_utenza = null;
		idLogic_utenzaDominio_utenza = utenzaDominio.getIdUtenza();
		if(idLogic_utenzaDominio_utenza!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_utenzaDominio_utenza = ((JDBCUtenzaServiceSearch)(this.getServiceManager().getUtenzaServiceSearch())).findTableId(idLogic_utenzaDominio_utenza, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_utenzaDominio_utenza = idLogic_utenzaDominio_utenza.getId();
				if(id_utenzaDominio_utenza==null || id_utenzaDominio_utenza<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _utenzaDominio_dominio
		Long id_utenzaDominio_dominio = null;
		it.govpay.orm.IdDominio idLogic_utenzaDominio_dominio = null;
		idLogic_utenzaDominio_dominio = utenzaDominio.getIdDominio();
		if(idLogic_utenzaDominio_dominio!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_utenzaDominio_dominio = ((JDBCDominioServiceSearch)(this.getServiceManager().getDominioServiceSearch())).findTableId(idLogic_utenzaDominio_dominio, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_utenzaDominio_dominio = idLogic_utenzaDominio_dominio.getId();
				if(id_utenzaDominio_dominio==null || id_utenzaDominio_dominio<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _utenzaDominio_uo
		Long id_utenzaDominio_uo = null;  
		it.govpay.orm.IdUo idLogic_utenzaDominio_uo = null;
		idLogic_utenzaDominio_uo = utenzaDominio.getIdUo();
		if(idLogic_utenzaDominio_uo!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_utenzaDominio_uo = ((JDBCUoServiceSearch)(this.getServiceManager().getUoServiceSearch())).findTableId(idLogic_utenzaDominio_uo, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_utenzaDominio_uo = idLogic_utenzaDominio_uo.getId();
				if(id_utenzaDominio_uo==null || id_utenzaDominio_uo<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object utenzaDominio
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getUtenzaDominioFieldConverter().toTable(UtenzaDominio.model()));
		boolean isUpdate_utenzaDominio = true;
		java.util.List<JDBCObject> lstObjects_utenzaDominio = new java.util.ArrayList<>();
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_utenza","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_dominio","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_uo","?");
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_utenzaDominio.add(new JDBCObject(id_utenzaDominio_utenza, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_utenzaDominio.add(new JDBCObject(id_utenzaDominio_dominio, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_utenzaDominio.add(new JDBCObject(id_utenzaDominio_uo, Long.class));
		}
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_utenzaDominio.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_utenzaDominio) {
			// Update utenzaDominio
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_utenzaDominio.toArray(new JDBCObject[]{}));
		}


	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, UtenzaDominio utenzaDominio, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		GenericJDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getUtenzaDominioFieldConverter().toTable(UtenzaDominio.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, utenzaDominio),
				this.getUtenzaDominioFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, UtenzaDominio utenzaDominio, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		GenericJDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getUtenzaDominioFieldConverter().toTable(UtenzaDominio.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, utenzaDominio),
				this.getUtenzaDominioFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, UtenzaDominio utenzaDominio, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		GenericJDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getUtenzaDominioFieldConverter().toTable(UtenzaDominio.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, utenzaDominio),
				this.getUtenzaDominioFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<>();
		ids.add(tableId);
		GenericJDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getUtenzaDominioFieldConverter().toTable(UtenzaDominio.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getUtenzaDominioFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<>();
		ids.add(tableId);
		GenericJDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getUtenzaDominioFieldConverter().toTable(UtenzaDominio.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getUtenzaDominioFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<>();
		ids.add(tableId);
		GenericJDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getUtenzaDominioFieldConverter().toTable(UtenzaDominio.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getUtenzaDominioFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, UtenzaDominio utenzaDominio, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		Long id = utenzaDominio.getId();
		if(id != null && this.exists(jdbcProperties, log, connection, sqlQueryObject, id)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, utenzaDominio,idMappingResolutionBehaviour);		
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, utenzaDominio,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UtenzaDominio utenzaDominio, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, utenzaDominio,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, utenzaDominio,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, UtenzaDominio utenzaDominio) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if(utenzaDominio.getId()==null){
			throw new ServiceException("Parameter "+utenzaDominio.getClass().getName()+".id is null");
		}
		if(utenzaDominio.getId()<=0){
			throw new ServiceException("Parameter "+utenzaDominio.getClass().getName()+".id is less equals 0");
		}
		longId = utenzaDominio.getId();
		
		this._delete(jdbcProperties, log, connection, sqlQueryObject, longId);
		
	}

	private void _delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long id) throws NotImplementedException,ServiceException,Exception {
	
		if(id==null){
			throw new ServiceException("Id is null");
		}
		if(id!=null && id.longValue()<=0){
			throw new ServiceException("Id is less equals 0");
		}
		
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		ISQLQueryObject sqlQueryObjectDelete = sqlQueryObject.newSQLQueryObject();
		

		// Object utenzaDominio
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getUtenzaDominioFieldConverter().toTable(UtenzaDominio.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete utenzaDominio
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getUtenzaDominioFieldConverter()));

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
	
	@Override
	public int nativeUpdate(JDBCServiceManagerProperties jdbcProperties, Logger log,Connection connection,ISQLQueryObject sqlObject, String sql,Object ... param) throws ServiceException,NotImplementedException, Exception {
	
		return org.openspcoop2.generic_project.dao.jdbc.utils.GenericJDBCUtilities.nativeUpdate(jdbcProperties, log, connection, sqlObject,
																							sql,param);
	
	}
}
