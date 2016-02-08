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
import it.govpay.orm.IdSingolaRendicontazione;
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

import it.govpay.orm.SingolaRendicontazione;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCSingolaRendicontazioneServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCSingolaRendicontazioneServiceImpl extends JDBCSingolaRendicontazioneServiceSearchImpl
	implements IJDBCServiceCRUDWithId<SingolaRendicontazione, IdSingolaRendicontazione, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, SingolaRendicontazione singolaRendicontazione, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				

		// Object _fr
		Long id_fr = null;
		it.govpay.orm.IdFr idLogic_fr = null;
		idLogic_fr = singolaRendicontazione.getIdFr();
		if(idLogic_fr!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_fr = ((JDBCFRServiceSearch)(this.getServiceManager().getFRServiceSearch())).findTableId(idLogic_fr, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_fr = idLogic_fr.getId();
				if(id_fr==null || id_fr<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}
		// Object _singoloVersamento
		Long id_singoloVersamento = null;
		it.govpay.orm.IdSingoloVersamento idLogic_singoloVersamento = null;
		idLogic_singoloVersamento = singolaRendicontazione.getIdSingoloVersamento();
		if(idLogic_singoloVersamento!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_singoloVersamento = ((JDBCSingoloVersamentoServiceSearch)(this.getServiceManager().getSingoloVersamentoServiceSearch())).findTableId(idLogic_singoloVersamento, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_singoloVersamento = idLogic_singoloVersamento.getId();
				if(id_singoloVersamento==null || id_singoloVersamento<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object singolaRendicontazione
		sqlQueryObjectInsert.addInsertTable(this.getSingolaRendicontazioneFieldConverter().toTable(SingolaRendicontazione.model()));
		sqlQueryObjectInsert.addInsertField(this.getSingolaRendicontazioneFieldConverter().toColumn(SingolaRendicontazione.model().IUV,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getSingolaRendicontazioneFieldConverter().toColumn(SingolaRendicontazione.model().IUR,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getSingolaRendicontazioneFieldConverter().toColumn(SingolaRendicontazione.model().SINGOLO_IMPORTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getSingolaRendicontazioneFieldConverter().toColumn(SingolaRendicontazione.model().CODICE_ESITO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getSingolaRendicontazioneFieldConverter().toColumn(SingolaRendicontazione.model().DATA_ESITO,false),"?");
		sqlQueryObjectInsert.addInsertField("id_fr","?");
		sqlQueryObjectInsert.addInsertField("id_singolo_versamento","?");

		// Insert singolaRendicontazione
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getSingolaRendicontazioneFetch().getKeyGeneratorObject(SingolaRendicontazione.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(singolaRendicontazione.getIuv(),SingolaRendicontazione.model().IUV.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(singolaRendicontazione.getIur(),SingolaRendicontazione.model().IUR.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(singolaRendicontazione.getSingoloImporto(),SingolaRendicontazione.model().SINGOLO_IMPORTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(singolaRendicontazione.getCodiceEsito(),SingolaRendicontazione.model().CODICE_ESITO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(singolaRendicontazione.getDataEsito(),SingolaRendicontazione.model().DATA_ESITO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_fr,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_singoloVersamento,Long.class)
		);
		singolaRendicontazione.setId(id);

	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdSingolaRendicontazione oldId, SingolaRendicontazione singolaRendicontazione, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdSingolaRendicontazione(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = singolaRendicontazione.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: singolaRendicontazione.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			singolaRendicontazione.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}
		
		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, singolaRendicontazione, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, SingolaRendicontazione singolaRendicontazione, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {

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
			

		// Object _singolaRendicontazione_fr
		Long id_singolaRendicontazione_fr = null;
		it.govpay.orm.IdFr idLogic_singolaRendicontazione_fr = null;
		idLogic_singolaRendicontazione_fr = singolaRendicontazione.getIdFr();
		if(idLogic_singolaRendicontazione_fr!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_singolaRendicontazione_fr = ((JDBCFRServiceSearch)(this.getServiceManager().getFRServiceSearch())).findTableId(idLogic_singolaRendicontazione_fr, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_singolaRendicontazione_fr = idLogic_singolaRendicontazione_fr.getId();
				if(id_singolaRendicontazione_fr==null || id_singolaRendicontazione_fr<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}
		// Object _singolaRendicontazione_singoloVersamento
		Long id_singolaRendicontazione_singoloVersamento = null;
		it.govpay.orm.IdSingoloVersamento idLogic_singolaRendicontazione_singoloVersamento = null;
		idLogic_singolaRendicontazione_singoloVersamento = singolaRendicontazione.getIdSingoloVersamento();
		if(idLogic_singolaRendicontazione_singoloVersamento!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_singolaRendicontazione_singoloVersamento = ((JDBCSingoloVersamentoServiceSearch)(this.getServiceManager().getSingoloVersamentoServiceSearch())).findTableId(idLogic_singolaRendicontazione_singoloVersamento, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_singolaRendicontazione_singoloVersamento = idLogic_singolaRendicontazione_singoloVersamento.getId();
				if(id_singolaRendicontazione_singoloVersamento==null || id_singolaRendicontazione_singoloVersamento<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object singolaRendicontazione
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getSingolaRendicontazioneFieldConverter().toTable(SingolaRendicontazione.model()));
		boolean isUpdate_singolaRendicontazione = true;
		java.util.List<JDBCObject> lstObjects_singolaRendicontazione = new java.util.ArrayList<JDBCObject>();
		sqlQueryObjectUpdate.addUpdateField(this.getSingolaRendicontazioneFieldConverter().toColumn(SingolaRendicontazione.model().IUV,false), "?");
		lstObjects_singolaRendicontazione.add(new JDBCObject(singolaRendicontazione.getIuv(), SingolaRendicontazione.model().IUV.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getSingolaRendicontazioneFieldConverter().toColumn(SingolaRendicontazione.model().IUR,false), "?");
		lstObjects_singolaRendicontazione.add(new JDBCObject(singolaRendicontazione.getIur(), SingolaRendicontazione.model().IUR.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getSingolaRendicontazioneFieldConverter().toColumn(SingolaRendicontazione.model().SINGOLO_IMPORTO,false), "?");
		lstObjects_singolaRendicontazione.add(new JDBCObject(singolaRendicontazione.getSingoloImporto(), SingolaRendicontazione.model().SINGOLO_IMPORTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getSingolaRendicontazioneFieldConverter().toColumn(SingolaRendicontazione.model().CODICE_ESITO,false), "?");
		lstObjects_singolaRendicontazione.add(new JDBCObject(singolaRendicontazione.getCodiceEsito(), SingolaRendicontazione.model().CODICE_ESITO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getSingolaRendicontazioneFieldConverter().toColumn(SingolaRendicontazione.model().DATA_ESITO,false), "?");
		lstObjects_singolaRendicontazione.add(new JDBCObject(singolaRendicontazione.getDataEsito(), SingolaRendicontazione.model().DATA_ESITO.getFieldType()));
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_fr","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_singolo_versamento","?");
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_singolaRendicontazione.add(new JDBCObject(id_singolaRendicontazione_fr, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_singolaRendicontazione.add(new JDBCObject(id_singolaRendicontazione_singoloVersamento, Long.class));
		}
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_singolaRendicontazione.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_singolaRendicontazione) {
			// Update singolaRendicontazione
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_singolaRendicontazione.toArray(new JDBCObject[]{}));
		}


	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdSingolaRendicontazione id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getSingolaRendicontazioneFieldConverter().toTable(SingolaRendicontazione.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getSingolaRendicontazioneFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdSingolaRendicontazione id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getSingolaRendicontazioneFieldConverter().toTable(SingolaRendicontazione.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getSingolaRendicontazioneFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdSingolaRendicontazione id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getSingolaRendicontazioneFieldConverter().toTable(SingolaRendicontazione.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getSingolaRendicontazioneFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getSingolaRendicontazioneFieldConverter().toTable(SingolaRendicontazione.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getSingolaRendicontazioneFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getSingolaRendicontazioneFieldConverter().toTable(SingolaRendicontazione.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getSingolaRendicontazioneFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getSingolaRendicontazioneFieldConverter().toTable(SingolaRendicontazione.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getSingolaRendicontazioneFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdSingolaRendicontazione oldId, SingolaRendicontazione singolaRendicontazione, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, singolaRendicontazione,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, singolaRendicontazione,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, SingolaRendicontazione singolaRendicontazione, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, singolaRendicontazione,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, singolaRendicontazione,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, SingolaRendicontazione singolaRendicontazione) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (singolaRendicontazione.getId()!=null) && (singolaRendicontazione.getId()>0) ){
			longId = singolaRendicontazione.getId();
		}
		else{
			IdSingolaRendicontazione idSingolaRendicontazione = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,singolaRendicontazione);
			longId = this.findIdSingolaRendicontazione(jdbcProperties,log,connection,sqlQueryObject,idSingolaRendicontazione,false);
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
		

		// Object singolaRendicontazione
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getSingolaRendicontazioneFieldConverter().toTable(SingolaRendicontazione.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete singolaRendicontazione
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdSingolaRendicontazione idSingolaRendicontazione) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdSingolaRendicontazione(jdbcProperties, log, connection, sqlQueryObject, idSingolaRendicontazione, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getSingolaRendicontazioneFieldConverter()));

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
