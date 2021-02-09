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
import it.govpay.orm.IdTracciatoNotificaPagamenti;
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

import it.govpay.orm.TracciatoNotificaPagamenti;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCTracciatoNotificaPagamentiServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCTracciatoNotificaPagamentiServiceImpl extends JDBCTracciatoNotificaPagamentiServiceSearchImpl
	implements IJDBCServiceCRUDWithId<TracciatoNotificaPagamenti, IdTracciatoNotificaPagamenti, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, TracciatoNotificaPagamenti tracciatoNotificaPagamenti, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

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
		idLogic_dominio = tracciatoNotificaPagamenti.getIdDominio();
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


		// Object tracciatoNotificaPagamenti
		sqlQueryObjectInsert.addInsertTable(this.getTracciatoNotificaPagamentiFieldConverter().toTable(TracciatoNotificaPagamenti.model()));
		sqlQueryObjectInsert.addInsertField(this.getTracciatoNotificaPagamentiFieldConverter().toColumn(TracciatoNotificaPagamenti.model().NOME_FILE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoNotificaPagamentiFieldConverter().toColumn(TracciatoNotificaPagamenti.model().TIPO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoNotificaPagamentiFieldConverter().toColumn(TracciatoNotificaPagamenti.model().VERSIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoNotificaPagamentiFieldConverter().toColumn(TracciatoNotificaPagamenti.model().STATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoNotificaPagamentiFieldConverter().toColumn(TracciatoNotificaPagamenti.model().DATA_CREAZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoNotificaPagamentiFieldConverter().toColumn(TracciatoNotificaPagamenti.model().DATA_RT_DA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoNotificaPagamentiFieldConverter().toColumn(TracciatoNotificaPagamenti.model().DATA_RT_A,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoNotificaPagamentiFieldConverter().toColumn(TracciatoNotificaPagamenti.model().DATA_CARICAMENTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoNotificaPagamentiFieldConverter().toColumn(TracciatoNotificaPagamenti.model().DATA_COMPLETAMENTO,false),"?");
//		sqlQueryObjectInsert.addInsertField(this.getTracciatoNotificaPagamentiFieldConverter().toColumn(TracciatoNotificaPagamenti.model().RAW_CONTENUTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoNotificaPagamentiFieldConverter().toColumn(TracciatoNotificaPagamenti.model().BEAN_DATI,false),"?");
		sqlQueryObjectInsert.addInsertField("id_dominio","?");

		// Insert tracciatoNotificaPagamenti
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getTracciatoNotificaPagamentiFetch().getKeyGeneratorObject(TracciatoNotificaPagamenti.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciatoNotificaPagamenti.getNomeFile(),TracciatoNotificaPagamenti.model().NOME_FILE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciatoNotificaPagamenti.getTipo(),TracciatoNotificaPagamenti.model().TIPO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciatoNotificaPagamenti.getVersione(),TracciatoNotificaPagamenti.model().VERSIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciatoNotificaPagamenti.getStato(),TracciatoNotificaPagamenti.model().STATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciatoNotificaPagamenti.getDataCreazione(),TracciatoNotificaPagamenti.model().DATA_CREAZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciatoNotificaPagamenti.getDataRtDa(),TracciatoNotificaPagamenti.model().DATA_RT_DA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciatoNotificaPagamenti.getDataRtA(),TracciatoNotificaPagamenti.model().DATA_RT_A.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciatoNotificaPagamenti.getDataCaricamento(),TracciatoNotificaPagamenti.model().DATA_CARICAMENTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciatoNotificaPagamenti.getDataCompletamento(),TracciatoNotificaPagamenti.model().DATA_COMPLETAMENTO.getFieldType()),
//			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciatoNotificaPagamenti.getRawContenuto(),TracciatoNotificaPagamenti.model().RAW_CONTENUTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciatoNotificaPagamenti.getBeanDati(),TracciatoNotificaPagamenti.model().BEAN_DATI.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_dominio,Long.class)
		);
		tracciatoNotificaPagamenti.setId(id);

	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTracciatoNotificaPagamenti oldId, TracciatoNotificaPagamenti tracciatoNotificaPagamenti, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdTracciatoNotificaPagamenti(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = tracciatoNotificaPagamenti.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: tracciatoNotificaPagamenti.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			tracciatoNotificaPagamenti.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}

		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, tracciatoNotificaPagamenti, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, TracciatoNotificaPagamenti tracciatoNotificaPagamenti, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
	
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
			

		// Object _tracciatoNotificaPagamenti_dominio
		Long id_tracciatoNotificaPagamenti_dominio = null;
		it.govpay.orm.IdDominio idLogic_tracciatoNotificaPagamenti_dominio = null;
		idLogic_tracciatoNotificaPagamenti_dominio = tracciatoNotificaPagamenti.getIdDominio();
		if(idLogic_tracciatoNotificaPagamenti_dominio!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_tracciatoNotificaPagamenti_dominio = ((JDBCDominioServiceSearch)(this.getServiceManager().getDominioServiceSearch())).findTableId(idLogic_tracciatoNotificaPagamenti_dominio, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_tracciatoNotificaPagamenti_dominio = idLogic_tracciatoNotificaPagamenti_dominio.getId();
				if(id_tracciatoNotificaPagamenti_dominio==null || id_tracciatoNotificaPagamenti_dominio<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object tracciatoNotificaPagamenti
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getTracciatoNotificaPagamentiFieldConverter().toTable(TracciatoNotificaPagamenti.model()));
		boolean isUpdate_tracciatoNotificaPagamenti = true;
		java.util.List<JDBCObject> lstObjects_tracciatoNotificaPagamenti = new java.util.ArrayList<JDBCObject>();
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoNotificaPagamentiFieldConverter().toColumn(TracciatoNotificaPagamenti.model().NOME_FILE,false), "?");
		lstObjects_tracciatoNotificaPagamenti.add(new JDBCObject(tracciatoNotificaPagamenti.getNomeFile(), TracciatoNotificaPagamenti.model().NOME_FILE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoNotificaPagamentiFieldConverter().toColumn(TracciatoNotificaPagamenti.model().TIPO,false), "?");
		lstObjects_tracciatoNotificaPagamenti.add(new JDBCObject(tracciatoNotificaPagamenti.getTipo(), TracciatoNotificaPagamenti.model().TIPO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoNotificaPagamentiFieldConverter().toColumn(TracciatoNotificaPagamenti.model().VERSIONE,false), "?");
		lstObjects_tracciatoNotificaPagamenti.add(new JDBCObject(tracciatoNotificaPagamenti.getVersione(), TracciatoNotificaPagamenti.model().VERSIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoNotificaPagamentiFieldConverter().toColumn(TracciatoNotificaPagamenti.model().STATO,false), "?");
		lstObjects_tracciatoNotificaPagamenti.add(new JDBCObject(tracciatoNotificaPagamenti.getStato(), TracciatoNotificaPagamenti.model().STATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoNotificaPagamentiFieldConverter().toColumn(TracciatoNotificaPagamenti.model().DATA_CREAZIONE,false), "?");
		lstObjects_tracciatoNotificaPagamenti.add(new JDBCObject(tracciatoNotificaPagamenti.getDataCreazione(), TracciatoNotificaPagamenti.model().DATA_CREAZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoNotificaPagamentiFieldConverter().toColumn(TracciatoNotificaPagamenti.model().DATA_RT_DA,false), "?");
		lstObjects_tracciatoNotificaPagamenti.add(new JDBCObject(tracciatoNotificaPagamenti.getDataRtDa(), TracciatoNotificaPagamenti.model().DATA_RT_DA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoNotificaPagamentiFieldConverter().toColumn(TracciatoNotificaPagamenti.model().DATA_RT_A,false), "?");
		lstObjects_tracciatoNotificaPagamenti.add(new JDBCObject(tracciatoNotificaPagamenti.getDataRtA(), TracciatoNotificaPagamenti.model().DATA_RT_A.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoNotificaPagamentiFieldConverter().toColumn(TracciatoNotificaPagamenti.model().DATA_CARICAMENTO,false), "?");
		lstObjects_tracciatoNotificaPagamenti.add(new JDBCObject(tracciatoNotificaPagamenti.getDataCaricamento(), TracciatoNotificaPagamenti.model().DATA_CARICAMENTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoNotificaPagamentiFieldConverter().toColumn(TracciatoNotificaPagamenti.model().DATA_COMPLETAMENTO,false), "?");
		lstObjects_tracciatoNotificaPagamenti.add(new JDBCObject(tracciatoNotificaPagamenti.getDataCompletamento(), TracciatoNotificaPagamenti.model().DATA_COMPLETAMENTO.getFieldType()));
//		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoNotificaPagamentiFieldConverter().toColumn(TracciatoNotificaPagamenti.model().RAW_CONTENUTO,false), "?");
//		lstObjects_tracciatoNotificaPagamenti.add(new JDBCObject(tracciatoNotificaPagamenti.getRawContenuto(), TracciatoNotificaPagamenti.model().RAW_CONTENUTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoNotificaPagamentiFieldConverter().toColumn(TracciatoNotificaPagamenti.model().BEAN_DATI,false), "?");
		lstObjects_tracciatoNotificaPagamenti.add(new JDBCObject(tracciatoNotificaPagamenti.getBeanDati(), TracciatoNotificaPagamenti.model().BEAN_DATI.getFieldType()));
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_dominio","?");
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_tracciatoNotificaPagamenti.add(new JDBCObject(id_tracciatoNotificaPagamenti_dominio, Long.class));
		}
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_tracciatoNotificaPagamenti.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_tracciatoNotificaPagamenti) {
			// Update tracciatoNotificaPagamenti
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_tracciatoNotificaPagamenti.toArray(new JDBCObject[]{}));
		}

	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTracciatoNotificaPagamenti id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTracciatoNotificaPagamentiFieldConverter().toTable(TracciatoNotificaPagamenti.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getTracciatoNotificaPagamentiFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTracciatoNotificaPagamenti id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTracciatoNotificaPagamentiFieldConverter().toTable(TracciatoNotificaPagamenti.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getTracciatoNotificaPagamentiFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTracciatoNotificaPagamenti id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTracciatoNotificaPagamentiFieldConverter().toTable(TracciatoNotificaPagamenti.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getTracciatoNotificaPagamentiFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTracciatoNotificaPagamentiFieldConverter().toTable(TracciatoNotificaPagamenti.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getTracciatoNotificaPagamentiFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTracciatoNotificaPagamentiFieldConverter().toTable(TracciatoNotificaPagamenti.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getTracciatoNotificaPagamentiFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTracciatoNotificaPagamentiFieldConverter().toTable(TracciatoNotificaPagamenti.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getTracciatoNotificaPagamentiFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTracciatoNotificaPagamenti oldId, TracciatoNotificaPagamenti tracciatoNotificaPagamenti, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, tracciatoNotificaPagamenti,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, tracciatoNotificaPagamenti,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, TracciatoNotificaPagamenti tracciatoNotificaPagamenti, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, tracciatoNotificaPagamenti,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, tracciatoNotificaPagamenti,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, TracciatoNotificaPagamenti tracciatoNotificaPagamenti) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (tracciatoNotificaPagamenti.getId()!=null) && (tracciatoNotificaPagamenti.getId()>0) ){
			longId = tracciatoNotificaPagamenti.getId();
		}
		else{
			IdTracciatoNotificaPagamenti idTracciatoNotificaPagamenti = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,tracciatoNotificaPagamenti);
			longId = this.findIdTracciatoNotificaPagamenti(jdbcProperties,log,connection,sqlQueryObject,idTracciatoNotificaPagamenti,false);
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
		

		// Object tracciatoNotificaPagamenti
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getTracciatoNotificaPagamentiFieldConverter().toTable(TracciatoNotificaPagamenti.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete tracciatoNotificaPagamenti
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTracciatoNotificaPagamenti idTracciatoNotificaPagamenti) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdTracciatoNotificaPagamenti(jdbcProperties, log, connection, sqlQueryObject, idTracciatoNotificaPagamenti, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getTracciatoNotificaPagamentiFieldConverter()));

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
