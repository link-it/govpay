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
import it.govpay.orm.IdTracciatoMyPivot;
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

import it.govpay.orm.TracciatoMyPivot;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCTracciatoMyPivotServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCTracciatoMyPivotServiceImpl extends JDBCTracciatoMyPivotServiceSearchImpl
	implements IJDBCServiceCRUDWithId<TracciatoMyPivot, IdTracciatoMyPivot, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, TracciatoMyPivot tracciatoMyPivot, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				

		// Object _dominio
		Long id_dominio = null;
		it.govpay.orm.IdDominio idLogic_dominio = null;
		idLogic_dominio = tracciatoMyPivot.getIdDominio();
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


		// Object tracciatoMyPivot
		sqlQueryObjectInsert.addInsertTable(this.getTracciatoMyPivotFieldConverter().toTable(TracciatoMyPivot.model()));
		sqlQueryObjectInsert.addInsertField(this.getTracciatoMyPivotFieldConverter().toColumn(TracciatoMyPivot.model().NOME_FILE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoMyPivotFieldConverter().toColumn(TracciatoMyPivot.model().STATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoMyPivotFieldConverter().toColumn(TracciatoMyPivot.model().DATA_CREAZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoMyPivotFieldConverter().toColumn(TracciatoMyPivot.model().DATA_RT_DA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoMyPivotFieldConverter().toColumn(TracciatoMyPivot.model().DATA_RT_A,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoMyPivotFieldConverter().toColumn(TracciatoMyPivot.model().DATA_CARICAMENTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoMyPivotFieldConverter().toColumn(TracciatoMyPivot.model().DATA_COMPLETAMENTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoMyPivotFieldConverter().toColumn(TracciatoMyPivot.model().REQUEST_TOKEN,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoMyPivotFieldConverter().toColumn(TracciatoMyPivot.model().UPLOAD_URL,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoMyPivotFieldConverter().toColumn(TracciatoMyPivot.model().AUTHORIZATION_TOKEN,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoMyPivotFieldConverter().toColumn(TracciatoMyPivot.model().RAW_CONTENUTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoMyPivotFieldConverter().toColumn(TracciatoMyPivot.model().BEAN_DATI,false),"?");
		sqlQueryObjectInsert.addInsertField("id_dominio","?");

		// Insert tracciatoMyPivot
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getTracciatoMyPivotFetch().getKeyGeneratorObject(TracciatoMyPivot.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciatoMyPivot.getNomeFile(),TracciatoMyPivot.model().NOME_FILE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciatoMyPivot.getStato(),TracciatoMyPivot.model().STATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciatoMyPivot.getDataCreazione(),TracciatoMyPivot.model().DATA_CREAZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciatoMyPivot.getDataRtDa(),TracciatoMyPivot.model().DATA_RT_DA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciatoMyPivot.getDataRtA(),TracciatoMyPivot.model().DATA_RT_A.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciatoMyPivot.getDataCaricamento(),TracciatoMyPivot.model().DATA_CARICAMENTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciatoMyPivot.getDataCompletamento(),TracciatoMyPivot.model().DATA_COMPLETAMENTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciatoMyPivot.getRequestToken(),TracciatoMyPivot.model().REQUEST_TOKEN.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciatoMyPivot.getUploadUrl(),TracciatoMyPivot.model().UPLOAD_URL.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciatoMyPivot.getAuthorizationToken(),TracciatoMyPivot.model().AUTHORIZATION_TOKEN.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciatoMyPivot.getRawContenuto(),TracciatoMyPivot.model().RAW_CONTENUTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciatoMyPivot.getBeanDati(),TracciatoMyPivot.model().BEAN_DATI.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_dominio,Long.class)
		);
		tracciatoMyPivot.setId(id);

		
	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTracciatoMyPivot oldId, TracciatoMyPivot tracciatoMyPivot, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdTracciatoMyPivot(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = tracciatoMyPivot.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: tracciatoMyPivot.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			tracciatoMyPivot.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}

		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, tracciatoMyPivot, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, TracciatoMyPivot tracciatoMyPivot, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
	
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
			

		// Object _tracciatoMyPivot_dominio
		Long id_tracciatoMyPivot_dominio = null;
		it.govpay.orm.IdDominio idLogic_tracciatoMyPivot_dominio = null;
		idLogic_tracciatoMyPivot_dominio = tracciatoMyPivot.getIdDominio();
		if(idLogic_tracciatoMyPivot_dominio!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_tracciatoMyPivot_dominio = ((JDBCDominioServiceSearch)(this.getServiceManager().getDominioServiceSearch())).findTableId(idLogic_tracciatoMyPivot_dominio, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_tracciatoMyPivot_dominio = idLogic_tracciatoMyPivot_dominio.getId();
				if(id_tracciatoMyPivot_dominio==null || id_tracciatoMyPivot_dominio<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object tracciatoMyPivot
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getTracciatoMyPivotFieldConverter().toTable(TracciatoMyPivot.model()));
		boolean isUpdate_tracciatoMyPivot = true;
		java.util.List<JDBCObject> lstObjects_tracciatoMyPivot = new java.util.ArrayList<JDBCObject>();
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoMyPivotFieldConverter().toColumn(TracciatoMyPivot.model().NOME_FILE,false), "?");
		lstObjects_tracciatoMyPivot.add(new JDBCObject(tracciatoMyPivot.getNomeFile(), TracciatoMyPivot.model().NOME_FILE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoMyPivotFieldConverter().toColumn(TracciatoMyPivot.model().STATO,false), "?");
		lstObjects_tracciatoMyPivot.add(new JDBCObject(tracciatoMyPivot.getStato(), TracciatoMyPivot.model().STATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoMyPivotFieldConverter().toColumn(TracciatoMyPivot.model().DATA_CREAZIONE,false), "?");
		lstObjects_tracciatoMyPivot.add(new JDBCObject(tracciatoMyPivot.getDataCreazione(), TracciatoMyPivot.model().DATA_CREAZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoMyPivotFieldConverter().toColumn(TracciatoMyPivot.model().DATA_RT_DA,false), "?");
		lstObjects_tracciatoMyPivot.add(new JDBCObject(tracciatoMyPivot.getDataRtDa(), TracciatoMyPivot.model().DATA_RT_DA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoMyPivotFieldConverter().toColumn(TracciatoMyPivot.model().DATA_RT_A,false), "?");
		lstObjects_tracciatoMyPivot.add(new JDBCObject(tracciatoMyPivot.getDataRtA(), TracciatoMyPivot.model().DATA_RT_A.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoMyPivotFieldConverter().toColumn(TracciatoMyPivot.model().DATA_CARICAMENTO,false), "?");
		lstObjects_tracciatoMyPivot.add(new JDBCObject(tracciatoMyPivot.getDataCaricamento(), TracciatoMyPivot.model().DATA_CARICAMENTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoMyPivotFieldConverter().toColumn(TracciatoMyPivot.model().DATA_COMPLETAMENTO,false), "?");
		lstObjects_tracciatoMyPivot.add(new JDBCObject(tracciatoMyPivot.getDataCompletamento(), TracciatoMyPivot.model().DATA_COMPLETAMENTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoMyPivotFieldConverter().toColumn(TracciatoMyPivot.model().REQUEST_TOKEN,false), "?");
		lstObjects_tracciatoMyPivot.add(new JDBCObject(tracciatoMyPivot.getRequestToken(), TracciatoMyPivot.model().REQUEST_TOKEN.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoMyPivotFieldConverter().toColumn(TracciatoMyPivot.model().UPLOAD_URL,false), "?");
		lstObjects_tracciatoMyPivot.add(new JDBCObject(tracciatoMyPivot.getUploadUrl(), TracciatoMyPivot.model().UPLOAD_URL.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoMyPivotFieldConverter().toColumn(TracciatoMyPivot.model().AUTHORIZATION_TOKEN,false), "?");
		lstObjects_tracciatoMyPivot.add(new JDBCObject(tracciatoMyPivot.getAuthorizationToken(), TracciatoMyPivot.model().AUTHORIZATION_TOKEN.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoMyPivotFieldConverter().toColumn(TracciatoMyPivot.model().RAW_CONTENUTO,false), "?");
		lstObjects_tracciatoMyPivot.add(new JDBCObject(tracciatoMyPivot.getRawContenuto(), TracciatoMyPivot.model().RAW_CONTENUTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoMyPivotFieldConverter().toColumn(TracciatoMyPivot.model().BEAN_DATI,false), "?");
		lstObjects_tracciatoMyPivot.add(new JDBCObject(tracciatoMyPivot.getBeanDati(), TracciatoMyPivot.model().BEAN_DATI.getFieldType()));
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_dominio","?");
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_tracciatoMyPivot.add(new JDBCObject(id_tracciatoMyPivot_dominio, Long.class));
		}
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_tracciatoMyPivot.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_tracciatoMyPivot) {
			// Update tracciatoMyPivot
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_tracciatoMyPivot.toArray(new JDBCObject[]{}));
		}
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTracciatoMyPivot id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTracciatoMyPivotFieldConverter().toTable(TracciatoMyPivot.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getTracciatoMyPivotFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTracciatoMyPivot id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTracciatoMyPivotFieldConverter().toTable(TracciatoMyPivot.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getTracciatoMyPivotFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTracciatoMyPivot id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTracciatoMyPivotFieldConverter().toTable(TracciatoMyPivot.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getTracciatoMyPivotFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTracciatoMyPivotFieldConverter().toTable(TracciatoMyPivot.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getTracciatoMyPivotFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTracciatoMyPivotFieldConverter().toTable(TracciatoMyPivot.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getTracciatoMyPivotFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTracciatoMyPivotFieldConverter().toTable(TracciatoMyPivot.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getTracciatoMyPivotFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTracciatoMyPivot oldId, TracciatoMyPivot tracciatoMyPivot, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, tracciatoMyPivot,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, tracciatoMyPivot,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, TracciatoMyPivot tracciatoMyPivot, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, tracciatoMyPivot,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, tracciatoMyPivot,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, TracciatoMyPivot tracciatoMyPivot) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (tracciatoMyPivot.getId()!=null) && (tracciatoMyPivot.getId()>0) ){
			longId = tracciatoMyPivot.getId();
		}
		else{
			IdTracciatoMyPivot idTracciatoMyPivot = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,tracciatoMyPivot);
			longId = this.findIdTracciatoMyPivot(jdbcProperties,log,connection,sqlQueryObject,idTracciatoMyPivot,false);
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
		

		// Object tracciatoMyPivot
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getTracciatoMyPivotFieldConverter().toTable(TracciatoMyPivot.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete tracciatoMyPivot
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTracciatoMyPivot idTracciatoMyPivot) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdTracciatoMyPivot(jdbcProperties, log, connection, sqlQueryObject, idTracciatoMyPivot, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getTracciatoMyPivotFieldConverter()));

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
