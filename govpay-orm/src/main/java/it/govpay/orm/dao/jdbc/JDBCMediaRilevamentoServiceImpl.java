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

import java.sql.Connection;

import org.openspcoop2.utils.sql.ISQLQueryObject;

import org.apache.log4j.Logger;

import org.openspcoop2.generic_project.dao.jdbc.IJDBCServiceCRUDWithId;
import it.govpay.orm.IdMediaRilevamento;
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

import it.govpay.orm.MediaRilevamento;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCMediaRilevamentoServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCMediaRilevamentoServiceImpl extends JDBCMediaRilevamentoServiceSearchImpl
	implements IJDBCServiceCRUDWithId<MediaRilevamento, IdMediaRilevamento, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, MediaRilevamento mediaRilevamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				

		// Object _sla
		Long id_sla = null;
		it.govpay.orm.IdSla idLogic_sla = null;
		idLogic_sla = mediaRilevamento.getIdSLA();
		if(idLogic_sla!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_sla = ((JDBCSLAServiceSearch)(this.getServiceManager().getSLAServiceSearch())).findTableId(idLogic_sla, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_sla = idLogic_sla.getId();
				if(id_sla==null || id_sla<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object mediaRilevamento
		sqlQueryObjectInsert.addInsertTable(this.getMediaRilevamentoFieldConverter().toTable(MediaRilevamento.model()));
		sqlQueryObjectInsert.addInsertField(this.getMediaRilevamentoFieldConverter().toColumn(MediaRilevamento.model().ID_APPLICAZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getMediaRilevamentoFieldConverter().toColumn(MediaRilevamento.model().DATA_OSSERVAZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getMediaRilevamentoFieldConverter().toColumn(MediaRilevamento.model().NUM_RILEVAMENTI_A,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getMediaRilevamentoFieldConverter().toColumn(MediaRilevamento.model().PERCENTUALE_A,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getMediaRilevamentoFieldConverter().toColumn(MediaRilevamento.model().NUM_RILEVAMENTI_B,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getMediaRilevamentoFieldConverter().toColumn(MediaRilevamento.model().PERCENTUALE_B,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getMediaRilevamentoFieldConverter().toColumn(MediaRilevamento.model().NUM_RILEVAMENTI_OVER,false),"?");
		sqlQueryObjectInsert.addInsertField("id_sla","?");

		// Insert mediaRilevamento
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getMediaRilevamentoFetch().getKeyGeneratorObject(MediaRilevamento.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(mediaRilevamento.getIdApplicazione(),MediaRilevamento.model().ID_APPLICAZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(mediaRilevamento.getDataOsservazione(),MediaRilevamento.model().DATA_OSSERVAZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(mediaRilevamento.getNumRilevamentiA(),MediaRilevamento.model().NUM_RILEVAMENTI_A.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(mediaRilevamento.getPercentualeA(),MediaRilevamento.model().PERCENTUALE_A.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(mediaRilevamento.getNumRilevamentiB(),MediaRilevamento.model().NUM_RILEVAMENTI_B.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(mediaRilevamento.getPercentualeB(),MediaRilevamento.model().PERCENTUALE_B.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(mediaRilevamento.getNumRilevamentiOver(),MediaRilevamento.model().NUM_RILEVAMENTI_OVER.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_sla,Long.class)
		);
		mediaRilevamento.setId(id);

	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdMediaRilevamento oldId, MediaRilevamento mediaRilevamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdMediaRilevamento(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = mediaRilevamento.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: mediaRilevamento.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			mediaRilevamento.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}
		
		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, mediaRilevamento, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, MediaRilevamento mediaRilevamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {

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
			

		// Object _mediaRilevamento_sla
		Long id_mediaRilevamento_sla = null;
		it.govpay.orm.IdSla idLogic_mediaRilevamento_sla = null;
		idLogic_mediaRilevamento_sla = mediaRilevamento.getIdSLA();
		if(idLogic_mediaRilevamento_sla!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_mediaRilevamento_sla = ((JDBCSLAServiceSearch)(this.getServiceManager().getSLAServiceSearch())).findTableId(idLogic_mediaRilevamento_sla, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_mediaRilevamento_sla = idLogic_mediaRilevamento_sla.getId();
				if(id_mediaRilevamento_sla==null || id_mediaRilevamento_sla<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object mediaRilevamento
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getMediaRilevamentoFieldConverter().toTable(MediaRilevamento.model()));
		boolean isUpdate_mediaRilevamento = true;
		java.util.List<JDBCObject> lstObjects_mediaRilevamento = new java.util.ArrayList<JDBCObject>();
		sqlQueryObjectUpdate.addUpdateField(this.getMediaRilevamentoFieldConverter().toColumn(MediaRilevamento.model().ID_APPLICAZIONE,false), "?");
		lstObjects_mediaRilevamento.add(new JDBCObject(mediaRilevamento.getIdApplicazione(), MediaRilevamento.model().ID_APPLICAZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getMediaRilevamentoFieldConverter().toColumn(MediaRilevamento.model().DATA_OSSERVAZIONE,false), "?");
		lstObjects_mediaRilevamento.add(new JDBCObject(mediaRilevamento.getDataOsservazione(), MediaRilevamento.model().DATA_OSSERVAZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getMediaRilevamentoFieldConverter().toColumn(MediaRilevamento.model().NUM_RILEVAMENTI_A,false), "?");
		lstObjects_mediaRilevamento.add(new JDBCObject(mediaRilevamento.getNumRilevamentiA(), MediaRilevamento.model().NUM_RILEVAMENTI_A.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getMediaRilevamentoFieldConverter().toColumn(MediaRilevamento.model().PERCENTUALE_A,false), "?");
		lstObjects_mediaRilevamento.add(new JDBCObject(mediaRilevamento.getPercentualeA(), MediaRilevamento.model().PERCENTUALE_A.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getMediaRilevamentoFieldConverter().toColumn(MediaRilevamento.model().NUM_RILEVAMENTI_B,false), "?");
		lstObjects_mediaRilevamento.add(new JDBCObject(mediaRilevamento.getNumRilevamentiB(), MediaRilevamento.model().NUM_RILEVAMENTI_B.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getMediaRilevamentoFieldConverter().toColumn(MediaRilevamento.model().PERCENTUALE_B,false), "?");
		lstObjects_mediaRilevamento.add(new JDBCObject(mediaRilevamento.getPercentualeB(), MediaRilevamento.model().PERCENTUALE_B.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getMediaRilevamentoFieldConverter().toColumn(MediaRilevamento.model().NUM_RILEVAMENTI_OVER,false), "?");
		lstObjects_mediaRilevamento.add(new JDBCObject(mediaRilevamento.getNumRilevamentiOver(), MediaRilevamento.model().NUM_RILEVAMENTI_OVER.getFieldType()));
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_sla","?");
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_mediaRilevamento.add(new JDBCObject(id_mediaRilevamento_sla, Long.class));
		}
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_mediaRilevamento.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_mediaRilevamento) {
			// Update mediaRilevamento
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_mediaRilevamento.toArray(new JDBCObject[]{}));
		}

	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdMediaRilevamento id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getMediaRilevamentoFieldConverter().toTable(MediaRilevamento.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getMediaRilevamentoFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdMediaRilevamento id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getMediaRilevamentoFieldConverter().toTable(MediaRilevamento.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getMediaRilevamentoFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdMediaRilevamento id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getMediaRilevamentoFieldConverter().toTable(MediaRilevamento.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getMediaRilevamentoFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getMediaRilevamentoFieldConverter().toTable(MediaRilevamento.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getMediaRilevamentoFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getMediaRilevamentoFieldConverter().toTable(MediaRilevamento.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getMediaRilevamentoFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getMediaRilevamentoFieldConverter().toTable(MediaRilevamento.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getMediaRilevamentoFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdMediaRilevamento oldId, MediaRilevamento mediaRilevamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, mediaRilevamento,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, mediaRilevamento,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, MediaRilevamento mediaRilevamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, mediaRilevamento,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, mediaRilevamento,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, MediaRilevamento mediaRilevamento) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (mediaRilevamento.getId()!=null) && (mediaRilevamento.getId()>0) ){
			longId = mediaRilevamento.getId();
		}
		else{
			IdMediaRilevamento idMediaRilevamento = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,mediaRilevamento);
			longId = this.findIdMediaRilevamento(jdbcProperties,log,connection,sqlQueryObject,idMediaRilevamento,false);
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
		

		// Object mediaRilevamento
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getMediaRilevamentoFieldConverter().toTable(MediaRilevamento.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete mediaRilevamento
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdMediaRilevamento idMediaRilevamento) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdMediaRilevamento(jdbcProperties, log, connection, sqlQueryObject, idMediaRilevamento, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getMediaRilevamentoFieldConverter()));

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
