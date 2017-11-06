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

import org.apache.log4j.Logger;

import org.openspcoop2.generic_project.dao.jdbc.IJDBCServiceCRUDWithId;
import it.govpay.orm.IdTracciato;
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

import it.govpay.orm.Tracciato;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCTracciatoServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCTracciatoServiceImpl extends JDBCTracciatoServiceSearchImpl
	implements IJDBCServiceCRUDWithId<Tracciato, IdTracciato, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Tracciato tracciato, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

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
		idLogic_operatore = tracciato.getIdOperatore();
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

		// Object _applicazione
		Long id_applicazione = null;
		it.govpay.orm.IdApplicazione idLogic_applicazione = null;
		idLogic_applicazione = tracciato.getIdApplicazione();
		if(idLogic_applicazione!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_applicazione = ((JDBCApplicazioneServiceSearch)(this.getServiceManager().getApplicazioneServiceSearch())).findTableId(idLogic_applicazione, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_applicazione = idLogic_applicazione.getId();
				if(id_applicazione==null || id_applicazione<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object tracciato
		sqlQueryObjectInsert.addInsertTable(this.getTracciatoFieldConverter().toTable(Tracciato.model()));
		sqlQueryObjectInsert.addInsertField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().DATA_CARICAMENTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().DATA_ULTIMO_AGGIORNAMENTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().STATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().LINEA_ELABORAZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().DESCRIZIONE_STATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().NUM_LINEE_TOTALI,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().NUM_OPERAZIONI_OK,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().NUM_OPERAZIONI_KO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().NOME_FILE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().RAW_DATA_RICHIESTA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().RAW_DATA_RISPOSTA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().TIPO_TRACCIATO,false),"?");
		sqlQueryObjectInsert.addInsertField("id_operatore","?");
		sqlQueryObjectInsert.addInsertField("id_applicazione","?");

		// Insert tracciato
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getTracciatoFetch().getKeyGeneratorObject(Tracciato.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciato.getDataCaricamento(),Tracciato.model().DATA_CARICAMENTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciato.getDataUltimoAggiornamento(),Tracciato.model().DATA_ULTIMO_AGGIORNAMENTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciato.getStato(),Tracciato.model().STATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciato.getLineaElaborazione(),Tracciato.model().LINEA_ELABORAZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciato.getDescrizioneStato(),Tracciato.model().DESCRIZIONE_STATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciato.getNumLineeTotali(),Tracciato.model().NUM_LINEE_TOTALI.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciato.getNumOperazioniOk(),Tracciato.model().NUM_OPERAZIONI_OK.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciato.getNumOperazioniKo(),Tracciato.model().NUM_OPERAZIONI_KO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciato.getNomeFile(),Tracciato.model().NOME_FILE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciato.getRawDataRichiesta(),Tracciato.model().RAW_DATA_RICHIESTA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciato.getRawDataRisposta(),Tracciato.model().RAW_DATA_RISPOSTA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciato.getTipoTracciato(),Tracciato.model().TIPO_TRACCIATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_operatore,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_applicazione,Long.class)
		);
		tracciato.setId(id);

		
	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTracciato oldId, Tracciato tracciato, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdTracciato(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = tracciato.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: tracciato.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			tracciato.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}

		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, tracciato, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Tracciato tracciato, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
	
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
			

		// Object _tracciato_operatore
		Long id_tracciato_operatore = null;
		it.govpay.orm.IdOperatore idLogic_tracciato_operatore = null;
		idLogic_tracciato_operatore = tracciato.getIdOperatore();
		if(idLogic_tracciato_operatore!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_tracciato_operatore = ((JDBCOperatoreServiceSearch)(this.getServiceManager().getOperatoreServiceSearch())).findTableId(idLogic_tracciato_operatore, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_tracciato_operatore = idLogic_tracciato_operatore.getId();
				if(id_tracciato_operatore==null || id_tracciato_operatore<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _tracciato_applicazione
		Long id_tracciato_applicazione = null;
		it.govpay.orm.IdApplicazione idLogic_tracciato_applicazione = null;
		idLogic_tracciato_applicazione = tracciato.getIdApplicazione();
		if(idLogic_tracciato_applicazione!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_tracciato_applicazione = ((JDBCApplicazioneServiceSearch)(this.getServiceManager().getApplicazioneServiceSearch())).findTableId(idLogic_tracciato_applicazione, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_tracciato_applicazione = idLogic_tracciato_applicazione.getId();
				if(id_tracciato_applicazione==null || id_tracciato_applicazione<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object tracciato
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getTracciatoFieldConverter().toTable(Tracciato.model()));
		boolean isUpdate_tracciato = true;
		java.util.List<JDBCObject> lstObjects_tracciato = new java.util.ArrayList<JDBCObject>();
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().DATA_CARICAMENTO,false), "?");
		lstObjects_tracciato.add(new JDBCObject(tracciato.getDataCaricamento(), Tracciato.model().DATA_CARICAMENTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().DATA_ULTIMO_AGGIORNAMENTO,false), "?");
		lstObjects_tracciato.add(new JDBCObject(tracciato.getDataUltimoAggiornamento(), Tracciato.model().DATA_ULTIMO_AGGIORNAMENTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().STATO,false), "?");
		lstObjects_tracciato.add(new JDBCObject(tracciato.getStato(), Tracciato.model().STATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().LINEA_ELABORAZIONE,false), "?");
		lstObjects_tracciato.add(new JDBCObject(tracciato.getLineaElaborazione(), Tracciato.model().LINEA_ELABORAZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().DESCRIZIONE_STATO,false), "?");
		lstObjects_tracciato.add(new JDBCObject(tracciato.getDescrizioneStato(), Tracciato.model().DESCRIZIONE_STATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().NUM_LINEE_TOTALI,false), "?");
		lstObjects_tracciato.add(new JDBCObject(tracciato.getNumLineeTotali(), Tracciato.model().NUM_LINEE_TOTALI.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().NUM_OPERAZIONI_OK,false), "?");
		lstObjects_tracciato.add(new JDBCObject(tracciato.getNumOperazioniOk(), Tracciato.model().NUM_OPERAZIONI_OK.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().NUM_OPERAZIONI_KO,false), "?");
		lstObjects_tracciato.add(new JDBCObject(tracciato.getNumOperazioniKo(), Tracciato.model().NUM_OPERAZIONI_KO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().NOME_FILE,false), "?");
		lstObjects_tracciato.add(new JDBCObject(tracciato.getNomeFile(), Tracciato.model().NOME_FILE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().RAW_DATA_RICHIESTA,false), "?");
		lstObjects_tracciato.add(new JDBCObject(tracciato.getRawDataRichiesta(), Tracciato.model().RAW_DATA_RICHIESTA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().RAW_DATA_RISPOSTA,false), "?");
		lstObjects_tracciato.add(new JDBCObject(tracciato.getRawDataRisposta(), Tracciato.model().RAW_DATA_RISPOSTA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().TIPO_TRACCIATO,false), "?");
		lstObjects_tracciato.add(new JDBCObject(tracciato.getTipoTracciato(), Tracciato.model().TIPO_TRACCIATO.getFieldType()));
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_operatore","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_applicazione","?");
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_tracciato.add(new JDBCObject(id_tracciato_operatore, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_tracciato.add(new JDBCObject(id_tracciato_applicazione, Long.class));
		}
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_tracciato.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_tracciato) {
			// Update tracciato
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_tracciato.toArray(new JDBCObject[]{}));
		}


	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTracciato id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTracciatoFieldConverter().toTable(Tracciato.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getTracciatoFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTracciato id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTracciatoFieldConverter().toTable(Tracciato.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getTracciatoFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTracciato id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTracciatoFieldConverter().toTable(Tracciato.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getTracciatoFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTracciatoFieldConverter().toTable(Tracciato.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getTracciatoFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTracciatoFieldConverter().toTable(Tracciato.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getTracciatoFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTracciatoFieldConverter().toTable(Tracciato.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getTracciatoFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTracciato oldId, Tracciato tracciato, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, tracciato,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, tracciato,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Tracciato tracciato, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, tracciato,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, tracciato,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Tracciato tracciato) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (tracciato.getId()!=null) && (tracciato.getId()>0) ){
			longId = tracciato.getId();
		}
		else{
			IdTracciato idTracciato = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,tracciato);
			longId = this.findIdTracciato(jdbcProperties,log,connection,sqlQueryObject,idTracciato,false);
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
		

		// Object tracciato
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getTracciatoFieldConverter().toTable(Tracciato.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete tracciato
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTracciato idTracciato) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdTracciato(jdbcProperties, log, connection, sqlQueryObject, idTracciato, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getTracciatoFieldConverter()));

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
